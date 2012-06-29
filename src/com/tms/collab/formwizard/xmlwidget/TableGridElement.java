package com.tms.collab.formwizard.xmlwidget;

import org.jdom.Element;
import org.jdom.Attribute;
import com.tms.collab.formwizard.model.FormFieldDataObject;
import com.tms.collab.formwizard.model.FormException;





public class TableGridElement extends Element implements XmlWidgetAttributes{
    public static final String ELEMENT_NAME = "gridfield";

    public TableGridElement(FormFieldDataObject ffdo, String columnListXml) throws FormException {
        this(ffdo.getFormFieldId(),columnListXml,ffdo.getName(),ffdo.getColspan(),ffdo.getRowspan(),
             ffdo.getAlign(),ffdo.getValign());
    }

    public TableGridElement(String name,String columnListXml, String title, String colspan, String rowspan, String align,
                            String valign)
            throws FormException {
        super(ELEMENT_NAME);

        if (isNullOrEmpty(title)) {
			throw new FormException("Title Can not be Null");
		}

        setAttribute("name",name);
        setAttribute("title",title);
        setAttribute("columnListXml",columnListXml);
        if (colspan != null)
            setAttribute("colspan",colspan);

        if (rowspan != null)
            setAttribute("rowspan",rowspan);

        if (align != null)
            setAttribute("align",align);

        if (valign != null)
            setAttribute("valign",valign);

    }

    public int getSize() {
        return 0;
    }

    public int getType() {
        return FormElement.FORM_CLOB_TYPE;
    }

    public void removeMetaData() {
       removeAttribute("template");
    }

    private boolean isNullOrEmpty(String s) {
        return (s == null || s.trim().length() < 1);
    }

    public static Element newInstance(Element e) throws FormException {
        if (!ELEMENT_NAME.equals(e.getName())) {
            return null;
        }

        TableGridElement tableGrid = null;


        Attribute titleAttr = e.getAttribute("title");
        String title = null;
        if (titleAttr != null) {
            title = titleAttr.getValue();
        }

        Attribute columnListXmlAttr = e.getAttribute("columnListXml");
        String columnListXml = null;
        if (columnListXmlAttr != null) {
            columnListXml = columnListXmlAttr.getValue();
        }

        Attribute nameAttr = e.getAttribute("name");
        String name = null;
        if (nameAttr != null) {
            name = nameAttr.getValue();
        }

        Attribute colspanAttr = e.getAttribute("colspan");
        String colspan = "2";
        if (colspanAttr != null)
            colspan = colspanAttr.getValue();

        Attribute rowsspanAttr = e.getAttribute("rowspan");
        String rowspan = "1";
        if (rowsspanAttr != null)
            rowspan = rowsspanAttr.getValue();


        Attribute alignAttr = e.getAttribute("align");
        String align = "left";
        if (alignAttr != null)
            align = alignAttr.getValue();

        Attribute valignAttr = e.getAttribute("valign");
        String valign = "top";
        if (valignAttr != null)
            valign = valignAttr.getValue();


        tableGrid = new TableGridElement(name,columnListXml,title,colspan,rowspan,align,valign);

        return tableGrid;
    }
}
