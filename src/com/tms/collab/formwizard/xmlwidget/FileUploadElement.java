package com.tms.collab.formwizard.xmlwidget;

import org.jdom.Element;
import org.jdom.Attribute;
import com.tms.collab.formwizard.model.FormFieldDataObject;
import com.tms.collab.formwizard.model.FormException;

public class FileUploadElement extends Element implements XmlWidgetAttributes {
    public static final String ELEMENT_NAME = "fileupload";

    public FileUploadElement(FormFieldDataObject ffdo) throws FormException {
        this(ffdo.getFormFieldId(),ffdo.getRequiredFlag(),ffdo.getHiddenFlag(), ffdo.getColspan(),ffdo.getRowspan(),
             ffdo.getAlign(),ffdo.getValign());
    }

    public FileUploadElement(String name, String requiredField, String hiddenField, String colspan, String rowspan,
                             String align, String valign)
            throws FormException {
        super(ELEMENT_NAME);
        if (isNullOrEmpty(name))
            throw new FormException("Name can not be null");

        setAttribute("name",name);
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


        //add validator if required flag is selected
        if ("1".equals(requiredField)) {
			Element validator = new Element("validator_notempty");
			validator.setAttribute("name","required");
			validator.setAttribute("text", "Required Field!");
			addContent(validator);
		}



    }

    public int getSize() {
        return 255;
    }

    public int getType() {
       return FormElement.FORM_VARCHAR_TYPE;
    }
    public void removeMetaData() {
        removeAttribute("template");
    }

    public void removeMetaData(String attrName) {
       removeAttribute(attrName);
    }

    public static Element newInstance(Element e) throws FormException {
        if (!ELEMENT_NAME.equals(e.getName())) {
            return null;
        }

		Attribute requiredFieldAttr = e.getAttribute("require");
		String requiredField = null;
		if (requiredFieldAttr != null)
			requiredField = requiredFieldAttr.getValue();

		Attribute hiddenFieldAttr = e.getAttribute("hidden");
		String hiddenField = null;
		if (hiddenFieldAttr != null)
			hiddenField = hiddenFieldAttr.getValue();

         Attribute colspanAttr = e.getAttribute("colspan");
		String colspan = "1";
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


        return new FileUploadElement(e.getAttribute("name").getValue(),requiredField, hiddenField,colspan,rowspan,
                                     align,valign);
    }


    private boolean isNullOrEmpty(String s) {
        return (s == null || s.trim().length() < 1);
    }

}
