package com.tms.collab.formwizard.grid;

import kacang.stdui.Form;
import kacang.stdui.FormField;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.collections.SequencedHashMap;
import org.nfunk.jep.JEP;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;

import java.util.*;
import java.text.DecimalFormat;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.tms.collab.formwizard.model.Util;
import com.tms.collab.formwizard.model.FormDocumentException;

public class G2Field extends FormField {
    private String title;
    private List columnList;
    private List dataList;

    private int editRow;
    private List currentRowList;
    private String event;
    private boolean editButtonVisible;

    /**
     * Event to view field (default event/action)
     */
    public static final String EVENT_VIEW = "view";

    /**
     * Event to create and edit a new row
     */
    public static final String EVENT_NEW_ROW = "newRow";

    /**
     * Event to edit an existing row
     */
    public static final String EVENT_EDIT_ROW = "editRow";

    /**
     * Event to delete a row of data
     */
    public static final String EVENT_DELETE_ROW = "deleteRow";

    /**
     * Event to update a row of data
     */
    public static final String EVENT_UPDATE_ROW = "updateRow";

    /**
     * Event to cancel editing of current row
     */
    public static final String EVENT_CANCEL_ROW = "cancelRow";

    public G2Field() {
        this(null);
    }

    public G2Field(String name) {
        super(name);
        columnList = new ArrayList();
        dataList = new ArrayList();
        currentRowList = new ArrayList();
        editButtonVisible = true;
    }

    public Forward actionPerformed(Event event) {
        G2Column column;

        if (EVENT_NEW_ROW.equals(event.getType())) {
            // add new row
            List emptyRow = new ArrayList();
            for (int i = 0; i < columnList.size(); i++) {
                column = (G2Column) columnList.get(i);
                if (G2Column.TYPE_TEXT.equals(column.getType()) && G2Column.VALIDATE_NUMBER.equals(column.getValidation())) {
                    emptyRow.add("0");

                } else if(G2Column.TYPE_FORMULA.equals(column.getType())) {
                    emptyRow.add("0");

                } else {
                    emptyRow.add("");
                }

            }
            dataList.add(emptyRow);
            editRow = dataList.size();

        } else if (EVENT_EDIT_ROW.equals(event.getType())) {
            // edit row
            doEditRow(event);

        } else if (EVENT_UPDATE_ROW.equals(event.getType())) {
            // update currently editing row
            doUpdateRow(event);

        } else if (EVENT_CANCEL_ROW.equals(event.getType())) {
            // cancel editing current row
            editRow = 0;

        } else if (EVENT_DELETE_ROW.equals(event.getType())) {
            // delete current row
            String row = event.getParameter("editRow");
            int rotInt;

            try {
                rotInt = Integer.parseInt(row);
                if (rotInt > 0 && rotInt <= dataList.size()) {
                    dataList.remove(rotInt - 1);
                }

            } catch (Exception e) {
                Log.getLog(getClass()).error("Cannot delete row", e);
            }

        }

        return null;
    }

    public static DecimalFormat df = new DecimalFormat("0.##");

    private void doUpdateRow(Event event) {
        String val;
        String row;
        row = event.getParameter("editRow");
        List rowList;

        try {
            editRow = Integer.parseInt(row);
            if (editRow <= 0) {
                Log.getLog(getClass()).warn("Updating row 0 or less not allowed");
                editRow = 0;
            }
        } catch (Exception e) {
            editRow = 0;    // no need to edit
        }

        if (editRow > dataList.size()) {
            Log.getLog(getClass()).warn("Updating row that does not exist not allowed");
            editRow = 0;    // no need to edit
        }

        if (editRow == 0) {
            return;
        }

        // get current row
        rowList = (List) dataList.get(editRow - 1);

        // update currently editing row (first pass - does not process formula)
        for (int i = 0; i < columnList.size(); i++) {
            G2Column column = (G2Column) columnList.get(i);

				try {
					String columName=URLDecoder.decode(column.getName(),"UTF-8");
					val = event.getParameter(columName);				
				if (val == null) {
	                val = "";
	            }
				val=URLDecoder.decode(val,"UTF-8");
				if (G2Column.TYPE_TEXT.equals(column.getType()) ||
			            G2Column.TYPE_DROP_DOWN.equals(column.getType())) {

			                if (G2Column.VALIDATE_NUMBER.equals(column.getValidation())) {
			                    // if a number, try to make it to decimal only
			                    double dval;
			                    try {
			                        dval = Double.parseDouble(val);
			                        val = df.format(dval);
			                    } catch (Exception e) {
			                        // just ignore
			                    }

			                }
			                rowList.set(i, val);
			            } else if (G2Column.TYPE_FORMULA.equals(column.getType())) {
			                // do nothing
			            } else {
			                Log.getLog(getClass()).error("Unknown column type");
			            }
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            

            
        }

        // update currently editing row (second pass - process formula)
        for (int i = 0; i < columnList.size(); i++) {
            G2Column column = (G2Column) columnList.get(i);
            val = event.getParameter(column.getName());
            if (val == null) {
                val = "";
            }

            // get current row
            rowList = (List) dataList.get(editRow - 1);

            if (G2Column.TYPE_FORMULA.equals(column.getType())) {
                // handle formula
                String computedValue;
                computedValue = computeFormula(column, rowList);

                // if a number, try to make it to decimal only
                double dval;
                try {
                    dval = Double.parseDouble(computedValue);
                    computedValue = df.format(dval);
                } catch (Exception e) {
                    // just ignore
                }

                rowList.set(i, computedValue);
            }
        }

        editRow = 0;

    }

    private String computeFormula(G2Column column, List rowList) {
        String formula = column.getFormula();

        for (int i = 0; i < columnList.size(); i++) {
            G2Column g2Column = (G2Column) columnList.get(i);
            //if (G2Column.VALIDATE_NUMBER.equals(g2Column.getValidation())) {
                // replace value in formula
                formula = StringUtils.replace(formula, g2Column.getName(), (String) rowList.get(i));
            //}
        }

        JEP jep = new JEP();
        jep.parseExpression(formula);
        if (jep.getErrorInfo() != null) {
            // error parsing
            Log.getLog(getClass()).error("Error parsing formula: " + jep.getErrorInfo());
            return "#ERROR#";
        } else {
            Double d = new Double(jep.getValue());
            return d.toString();
        }
    }

    private void doEditRow(Event event) {
        String row;
        row = event.getParameter("editRow");

        try {
            editRow = Integer.parseInt(row);
            if (editRow <= 0) {
                Log.getLog(getClass()).warn("Editing row 0 or less not allowed");
                editRow = 0;
            }
        } catch (Exception e) {
            editRow = 0;    // no need to edit
        }

        if (editRow > dataList.size()) {
            Log.getLog(getClass()).warn("Editing row that does not exist not allowed");
            editRow = 0;    // no need to edit
        }

        if (editRow > 0) {
            Log.getLog(getClass()).debug("Editing row " + editRow);
        }
    }

    public String getDefaultTemplate() {
        return "formwizard/grid/g2Field";
    }

    /**
     * Adds a new column to this field. This should be called during the
     * construction of this field.
     *
     * @param column
     */
    public void addColumn(G2Column column) {
        if (columnList == null) {
            columnList = new ArrayList();
        }
        columnList.add(column);
    }

    public boolean isInvalid() {
        for (int rowNo = 0; rowNo < dataList.size(); rowNo++) {
            List rowList = (List) dataList.get(rowNo);

            for (int colNo = 0; colNo < columnList.size(); colNo++) {
                G2Column column = (G2Column) columnList.get(colNo);

                if (!column.isValidValue((String) rowList.get(colNo))) {
                    return true;
                }
            }
        }
        return false;
    }

    public void performValidation(Event evt) {
        if (isInvalid()) {
            Form form = findParentForm(this);
            if (form != null)
                form.setInvalid(true);
        }
    }

    public Object getValue() {
        List list = new ArrayList();
        StringBuffer buffer = new StringBuffer();
        String value = "";
        for (Iterator iterator = dataList.iterator(); iterator.hasNext();) {
            list = (List) iterator.next();
            buffer.append("<row>");
            for (Iterator iterator1 = list.iterator(); iterator1.hasNext();) {
                value =  StringEscapeUtils.escapeXml(String.valueOf(iterator1.next()));

                buffer.append("<col>"+value+"</col>");

            }
            buffer.append("</row>");

        }

        return buffer.toString();
    }

    public void setValue(Object o) {
        ByteArrayInputStream bais;
        String value = null;
        org.w3c.dom.Document domDocument = null;

        if (o instanceof String) {
            //value = StringEscapeUtils.unescapeXml((String) o);

            value = "<gridfield>" + o + "</gridfield>";

            try {
                bais = new ByteArrayInputStream(value.getBytes("UTF-8"));
                domDocument = Util.buildDOMDocument(bais);
                parseDataElement(domDocument.getDocumentElement());
            }
            catch (FormDocumentException e) {
                Log.getLog(getClass()).error(e.getMessage(),e);
            }
            catch (UnsupportedEncodingException e) {
                Log.getLog(getClass()).error("Unsupported Encoding - UTF-8",e);
            }
        }
        else {
            throw new RuntimeException("Must be a String object (to set dataList)");
        }
    }


    public void parseDataElement(Node node) {
        Node rowNode = null;
        NodeList nodeList = node.getChildNodes();
        NodeList colNodeList = null;
        dataList = new ArrayList();
        List list = new ArrayList();

        for (int i = 0; i < nodeList.getLength();i++) {
            rowNode = nodeList.item(i);
            colNodeList = rowNode.getChildNodes();
            Node textNode;
            list = new ArrayList();
            for (int j = 0; j < colNodeList.getLength();j++) {
                textNode = colNodeList.item(j).getFirstChild();
                if (textNode != null && textNode.getNodeType()== Node.TEXT_NODE) {
                    list.add(textNode.getNodeValue());
                }
                else {
                    list.add("");
                }

                //list.add(colNodeList.item(j).getFirstChild().get);
            }
            dataList.add(list);
        }
        dataList.toString();
    }

    public Forward onSubmit(Event event) {
        performValidation(event);
        return null;
    }



    public String getColumnListXml() {
        String key = "", value="";
        StringBuffer sb = new StringBuffer();
        Set keySet = null;
        Map map = null;
        G2Column g2Column = null;

        for (Iterator iterator = columnList.iterator(); iterator.hasNext();) {
            g2Column = (G2Column) iterator.next();

            sb.append("<gridcolumn name=\""+ g2Column.getName() + "\" ");
            sb.append("header=\""+ g2Column.getHeader() + "\" ");
            sb.append("calculateTotal=\""+ g2Column.isColumnTotal() + "\" ");
            sb.append("validation=\"" + g2Column.getValidation() + "\" ");
            sb.append("formula=\"" + g2Column.getFormula() + "\" ");
            sb.append("type=\""+ g2Column.getType()+"\">");

            map = g2Column.getItemMap();
            if (map != null && !map.isEmpty()) {
                keySet = g2Column.getItemMap().keySet();
                for (Iterator iterator1 = keySet.iterator(); iterator1.hasNext();) {
                    key =  String.valueOf(iterator1.next());
                    value = String.valueOf(map.get(key));
                    sb.append("<item key=\"" + key + "\" ");
                    sb.append("value=\""+value+"\" />");
                }
            }

            sb.append("</gridcolumn>");
        }
        return StringEscapeUtils.escapeXml(sb.toString());
    }


    public void setColumnListXml(String xmlData) {
        String unescapedXmlData = StringEscapeUtils.unescapeXml(xmlData);
        unescapedXmlData = "<gridfield>" + unescapedXmlData + "</gridfield>";

        org.w3c.dom.Document domDocument = null;


        try {
            ByteArrayInputStream bais;

				bais = new ByteArrayInputStream(unescapedXmlData.getBytes("UTF-8"));
			
            domDocument = Util.buildDOMDocument(bais);
            // parse xmlData
            parseElement(domDocument.getDocumentElement());

        }
        catch (FormDocumentException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}






    }

    public void parseElement(Node node) {
        NodeList nodeList = node.getChildNodes();
        Node childNodes = null;
        NamedNodeMap attributes = null;
        G2Column g2Column = null;
        columnList = new ArrayList();
        String name = "", header ="", type="", validation="", formula = "";
        boolean calculateTotal = false;
        LinkedHashMap map = new LinkedHashMap();

        for (int i =0; i < nodeList.getLength(); i++) {

            childNodes = nodeList.item(i);
            attributes = childNodes.getAttributes();

            name = attributes.getNamedItem("name").getNodeValue();
            header = attributes.getNamedItem("header").getNodeValue();
            type = attributes.getNamedItem("type").getNodeValue();
            validation = attributes.getNamedItem("validation").getNodeValue();
            
            calculateTotal = (Boolean.valueOf(attributes.getNamedItem("calculateTotal").getNodeValue())).booleanValue();
            formula = attributes.getNamedItem("formula").getNodeValue();
            
            try {
			name = URLDecoder.decode(name, "UTF-8");			
            header = URLDecoder.decode(header, "UTF-8");
            type = URLDecoder.decode(type, "UTF-8");
            validation = URLDecoder.decode(validation, "UTF-8");
            formula = URLDecoder.decode(formula, "UTF-8");
            } catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            g2Column = new  G2Column(name,header,type);
            g2Column.setValidation(validation);
            g2Column.setColumnTotal(calculateTotal);
            g2Column.setFormula(formula);


            map = parseItemElement(childNodes);
            if (map != null && !map.isEmpty())
                g2Column.setItemMap(map);

            // recreate columnList based on xmlData
            columnList.add(g2Column);

        }

    }

    public LinkedHashMap parseItemElement(Node node) {
        NodeList itemNodeList = node.getChildNodes();
        LinkedHashMap map = new LinkedHashMap();
        Node itemNode = null;
        String value = "", key = "";
        NamedNodeMap attributes = null;

        for (int i =0; i < itemNodeList.getLength(); i++) {
            itemNode =  itemNodeList.item(i);
            attributes = itemNode.getAttributes();
            value = attributes.getNamedItem("value").getNodeValue();
            key = attributes.getNamedItem("key").getNodeValue();
            try {
				value = URLDecoder.decode(value, "UTF-8");
				key = URLDecoder.decode(key, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
            
            map.put(key,value);
        }
        return map;
    }


    // === [ getters/setters ] =================================================
    /**
     * List of G2Column objects defined for this field.
     *
     * @return List of G2Column objects
     */
    public List getColumnList() {
        return columnList;
    }

    public void setColumnList(List columnList) {
        this.columnList = columnList;
    }

    /**
     * Returns the data value for this field. This method returns a List of
     * List (rows) objects. Each of this List (rows) object returns yet
     * another List (columns) of String objects that represent value of each
     * column.
     * <p.
     * Data is modelled as a grid of rows and columns.
     *
     * @return
     */
    public List getDataList() {
        return dataList;
    }

    public void setDataList(List dataList) {
        this.dataList = dataList;
    }

    /**
     * Returns the currently editing row. 1 = first row. 0 = not editing data.
     *
     * @return
     */
    public int getEditRow() {
        return editRow;
    }

    public void setEditRow(int editRow) {
        this.editRow = editRow;
    }

    /**
     * Returns a List of String values that represent the currently editing
     * row's data. Values stored here is not validated.
     *
     * @return
     */
    public List getCurrentRowList() {
        return currentRowList;
    }

    public void setCurrentRowList(List currentRowList) {
        this.currentRowList = currentRowList;
    }

    /**
     * Returns the current event that this field is handling.
     *
     * @return
     */
    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isEditButtonVisible() {
        return editButtonVisible;
    }

    public void setEditButtonVisible(boolean editButtonVisible) {
        this.editButtonVisible = editButtonVisible;
    }

}