package com.tms.collab.formwizard.xmlwidget;

import org.jdom.Element;
import org.jdom.Attribute;
import com.tms.collab.formwizard.model.FormException;
import com.tms.collab.formwizard.model.FormFieldDataObject;


public class LabelElement extends Element {
    static final public String ELEMENT_NAME = "label";

	public LabelElement(FormFieldDataObject ffdo) throws FormException {
			this(ffdo.getFormFieldId(), ffdo.getName(), ffdo.getHiddenFlag(), ffdo.getRequiredFlag(), ffdo.getLabelColspan(),
                 ffdo.getRowspan(),ffdo.getAlign(),ffdo.getValign());
	}

    public LabelElement(String name) throws FormException {
        this(name, "",null,null, "1","1","left","top");
    }

    public LabelElement(String name, String text, String hiddenField, String requiredField,
                        String colspan,String rowspan, String align, String valign)
            throws FormException {
        this(name,text,hiddenField,requiredField,colspan,rowspan,align,valign,null);
    }

    public LabelElement(String name, String text, String hiddenField, String requiredField,
                        String colspan,String rowspan, String align, String valign, String type)
            throws FormException {
        super(ELEMENT_NAME);
        if (isNullOrEmpty(name)) {
            throw new FormException("Name Can not be Null or Empty");
        }
        setAttribute(name,text,hiddenField,requiredField,colspan,rowspan, align, valign, type);
    }

    public void setAttribute(String name, String text, String hiddenField, String requiredField,
                             String colspan,String rowspan, String align, String valign, String type) {
         setAttribute("name", name);

        if (text == null) {
            text = "";
        }
        setAttribute("text", text);
		setAttribute("escapeXml","false");
		setAttribute("hidden", hiddenField);
		setAttribute("require", requiredField);
        if (colspan != null)
            setAttribute("colspan",colspan);

        if (rowspan != null)
            setAttribute("rowspan",rowspan);

        if (align != null)
            setAttribute("align",align);

        if (valign != null)
            setAttribute("valign",valign);

        if (type != null && !type.equals(""))
            setAttribute("type",type);

    }

    private boolean isNullOrEmpty(String s) {
        return (s == null || s.trim().length() < 1);
    }


    public static Element newInstance(Element e) throws FormException {
        if (!ELEMENT_NAME.equals(e.getName())) {
            return null;
        }


        // possiblily null
        Attribute textAttr = e.getAttribute("text");
        String textStr = null;
        if (textAttr != null) {
            textStr = textAttr.getValue();
        }

		Attribute hiddenFieldAttr = e.getAttribute("hidden");
		String hiddenField = null;
		if (hiddenFieldAttr != null)
			hiddenField = hiddenFieldAttr.getValue();

		Attribute requiredFieldAttr = e.getAttribute("require");
		String requiredField = null;
		if (requiredFieldAttr != null)
			requiredField = requiredFieldAttr.getValue();

        Attribute rowspanAttr = e.getAttribute("rowspan");
		String rowspan = "1";
		if (rowspanAttr != null)
			rowspan = rowspanAttr.getValue();

        Attribute colspanAttr = e.getAttribute("colspan");
		String colspan = null;
		if (colspanAttr != null)
			colspan = colspanAttr.getValue();

        Attribute alignAttr = e.getAttribute("align");
		String align = "left";
		if (alignAttr != null)
			align = alignAttr.getValue();

        Attribute valignAttr = e.getAttribute("valign");
		String valign = "top";
		if (valignAttr != null)
			valign = valignAttr.getValue();

        Attribute typeAttr = e.getAttribute("type");
		String type = "";
		if (typeAttr != null)
			type = typeAttr.getValue();

       return  new LabelElement(
                e.getAttribute("name").getValue(),
                textStr,
                hiddenField,
                requiredField,
                colspan,
                rowspan,
                align,
                valign,
                type);

    }

}
