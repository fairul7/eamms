package com.tms.collab.formwizard.xmlwidget;

import com.tms.collab.formwizard.model.FormException;
import com.tms.collab.formwizard.model.FormFieldDataObject;
import org.jdom.Element;
import org.jdom.Attribute;

public class TemplateElement extends Element {
    public static final String ELEMENT_NAME = "formwizardtemplate";

    public TemplateElement(String name, String templateId,FormFieldDataObject ffDO) throws FormException {
        this(name,templateId,ffDO.getColspan(),ffDO.getRowspan(),ffDO.getAlign(),ffDO.getValign());
    }
    public TemplateElement(String name, String templateId,String colspan, String rowspan, String align, String valign)
            throws FormException {
        super(ELEMENT_NAME);

        if (isNullOrEmpty(name)) {
			throw new FormException("Name Can not be Null");
		}

        setAttribute("name", name);
        setAttribute("templateId",templateId);

        if (colspan != null)
            setAttribute("colspan",colspan);

        if (rowspan != null)
            setAttribute("rowspan",rowspan);

        if (align != null)
            setAttribute("align",align);

        if (valign != null)
            setAttribute("valign",valign);

    }

    protected boolean isNullOrEmpty(String s) {
        return (s == null || s.trim().length() < 1);
    }

    public static Element newInstance(Element e) throws FormException {
        if (!ELEMENT_NAME.equals(e.getName())) {
            return null;
        }

        Attribute colspanAttr = e.getAttribute("colspan");
		String colspan = "1";
		if (colspanAttr != null)
			colspan = colspanAttr.getValue();


        Attribute rowspanAttr = e.getAttribute("rowspan");
		String rowspan = "1";
		if (rowspanAttr != null)
			rowspan = rowspanAttr.getValue();

        Attribute alignAttr = e.getAttribute("align");
        String align = "left";
        if (alignAttr != null)
            align = alignAttr.getValue();

        Attribute valignAttr = e.getAttribute("valign");
        String valign = "top";
        if (valignAttr != null)
            valign = valignAttr.getValue();


        return new TemplateElement(e.getAttribute("name").getValue(), e.getAttribute("templateId").getValue(),
                                   colspan,rowspan,align,valign);
    }


}
