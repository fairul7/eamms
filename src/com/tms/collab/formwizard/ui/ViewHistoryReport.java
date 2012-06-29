package com.tms.collab.formwizard.ui;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.*;
import kacang.util.Log;
import com.tms.collab.formwizard.model.*;
import com.tms.collab.formwizard.xmlwidget.FormElement;
import com.tms.collab.formwizard.xmlwidget.TemplateElement;
import com.tms.collab.formwizard.xmlwidget.DateFieldElement;
import com.tms.collab.formwizard.xmlwidget.FileUploadElement;
import com.tms.util.FormatUtil;
import org.jdom.xpath.XPath;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.apache.commons.collections.SequencedHashMap;

import java.util.*;
import java.io.IOException;
import java.text.ParseException;

public class ViewHistoryReport extends ViewReport {
    public void onRequest(Event event) {
        setData();
        super.onRequest(event);

    }

    public void initTable() {
        super.init();
        setPageSize(10);
        setModel(new ViewHistoryReportModel(getSelected()));
        setWidth("100%");
    }


    public void setData() {
        FormModule module = null;
        FormDataObject fdo = null;
        FormElement form = null;
        Map optionMap = null;
        List templateList = null;
        Element element = null;
        FormTemplate formTemplate = null;

        module = (FormModule) Application.getInstance().getModule(FormModule.class);
        try {
            fdo = module.getForm(getFormId());
            setFormName(fdo.getFormName());
            form  = Util.getFormElement(getFormId());
            optionMap = setOptionMap(form , "");

            templateList = XPath.selectNodes(form,"/form/" + TemplateElement.ELEMENT_NAME);
            for (Iterator iterator = templateList.iterator(); iterator.hasNext();) {
                element = (Element) iterator.next();
                formTemplate = new FormTemplate();
                formTemplate.setFormTemplateId(element.getAttributeValue("templateId"));
                form = Util.getTemplateElement(formTemplate);
                optionMap.putAll(setOptionMap(form,element.getAttributeValue("name")));
            }
            setSelected(optionMap);

        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
        catch (FormDocumentException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
        catch (JDOMException e) {
            Log.getLog(getClass()).error("Error selecting the label nodes",e);
        }


    }

     public Map setOptionMap(FormElement element, String templateNodeName) throws FormDocumentException {
        Map optionMap = new SequencedHashMap();
        List nodeList = null;
        try {
            nodeList = XPath.selectNodes(element,"/form/label[@name != 'submitLbl' and @hidden = '0']");
            for (Iterator iterator = nodeList.iterator(); iterator.hasNext();) {
                Element lblElem = (Element) iterator.next();
                String nameAttrStr = lblElem.getAttributeValue("name");
                if (nameAttrStr == null || nameAttrStr.endsWith(FormConstants.FIELD_TEXT_BLOCK_SUFFIX)) {
                    continue;
                }
                if (nameAttrStr.endsWith("lb")) {
                    String dbColumn = nameAttrStr.substring(0, nameAttrStr.lastIndexOf("lb"));
                    dbColumn = templateNodeName + dbColumn;
                    String lbText ="";
                    if (lblElem.getAttributeValue("text") != null) {
                        lbText = lblElem.getAttributeValue("text");
                    }
                    optionMap.put(dbColumn,lbText);
                }

            }

        }
        catch (JDOMException e) {
            throw new FormDocumentException("Error selecting the label nodes");
        }


        return optionMap;

    }


    class ViewHistoryReportModel extends ViewReportModel {
        ViewHistoryReportModel(Map selected) {

          	String nodeElement = "";
            optionMap = selected;

            Set keySet = optionMap.keySet();
            String key = "";
            addAction(new TableAction("export", Application.getInstance().getMessage("formWizard.label.viewReport.exportCSV","Export into CSV Format"),""));

			for (Iterator valueIter = keySet.iterator(); valueIter.hasNext();) {
				key = String.valueOf(valueIter.next());
				nodeElement = Util.getNodeElement(getFormId(),key);
				String colName = String.valueOf(optionMap.get(key));
				TableColumn tcTemp =new TableColumn(key, colName);
				if (DateFieldElement.ELEMENT_NAME.equals(nodeElement)) {
					tcTemp.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateFormat()));
				}
                else if (FileUploadElement.ELEMENT_NAME.equals(nodeElement)) {
                    tcTemp.setFormat(new TableGetFilenameFormat());
                }
                else {
					Map map = new HashMap();
					map.put("","N/A");
					tcTemp.setFormat(new TableReplaceStringFormat(map));
				}


				addColumn(tcTemp);
			}



			TableColumn postedDateColumn = new TableColumn("datePosted", Application.getInstance().getMessage("formWizard.label.viewReport.submissionDate","Submission Date"));
			postedDateColumn.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateFormat()));
            postedDateColumn.setUrlParam("id");
			addColumn(postedDateColumn);

            TextField tfSearch = new TextField("tfSearch");
            tfSearch.setSize("20");
            TableFilter searchFilter = new TableFilter("searchFilter");
            searchFilter.setWidget(tfSearch);
            addFilter(searchFilter);

            setOperatorFilter();
            setColumnFilter();
        }


        public Collection getTableRows() {
            String tableName = FormModule.FORM_PREFIX + getFormName();
            Collection data =null;
            Set values = optionMap.keySet();
            StringBuffer columnBuffer = new StringBuffer();
            StringBuffer orderBuffer = new StringBuffer();

            for (Iterator valuesIter = values.iterator(); valuesIter.hasNext();) {
                String value = String.valueOf(valuesIter.next()).trim();
				columnBuffer.append(value).append(",");

                if(getSort()!=null &&  getSort() .equals(value))  {
                    orderBuffer = orderBuffer.append(" ORDER BY "+ value  );
                    if(isDesc())
                        orderBuffer =orderBuffer.append(" DESC");
                }
            }

            columnBuffer.append(tableName).append(".formUid AS id,").append(tableName).append(".datePosted");

            if (orderBuffer.length() == 0 && getSort() != null) {
            	if (getSort().equals("datePosted"))
					orderBuffer = orderBuffer.append(" ORDER BY ").append(tableName).append(".").append(getSort() );
                else if (getSort().equals("username"))
                    orderBuffer = orderBuffer.append(" ORDER BY ").append(getSort() );
				//Order
				if(isDesc())
					orderBuffer.append(" DESC");
            }


            FormDao dao  = (FormDao) Application.getInstance().getModule(FormModule.class).getDao();
            try {
                data = dao.getDynamicRows(generateDaoProperties(), tableName,columnBuffer.toString(),
                                          getWidgetManager().getUser().getId(),
                                          orderBuffer.toString(),getStart(),getRows());
            } catch (FormDaoException e) {
                Log.getLog(getClass()).error(e.getMessage(),e);
            }
            return data;
        }

        public int getTotalRowCount() {
            int rowCount = -1;
            String tableName = FormModule.FORM_PREFIX +getFormName();
            FormDao dao  = (FormDao) Application.getInstance().getModule(FormModule.class).getDao();
            try {
                rowCount =dao.getDynamicRows(generateDaoProperties(), tableName,getWidgetManager().getUser().getId());
            } catch (FormDaoException e) {
                Log.getLog(getClass()).error(e.getMessage(),e);
            }
            return rowCount;
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
		    exportCSV(evt);
			return null;
    	}

        private void exportCSV(Event evt) {


			ExportCSVServlet export = new  ExportCSVServlet();
			try {
				export.doGet(evt.getRequest(),evt.getResponse(),getFormId(),optionMap,
                             getWidgetManager().getUser().getId(),
                             getSort(),isDesc(),generateDaoProperties());
			}
			catch (IOException e) {
				Log.getLog(getClass()).error(e.getMessage(),e);
			}
			catch (ParseException e) {
				Log.getLog(getClass()).error(e.getMessage(),e);
			}
            catch (FormDaoException e) {
                Log.getLog(getClass()).error(e.getMessage(),e);
            }
            catch (DaoException e) {
                Log.getLog(getClass()).error(e.getMessage(),e);
            }
		}


    }
}

