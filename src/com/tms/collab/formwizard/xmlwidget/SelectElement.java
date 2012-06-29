package com.tms.collab.formwizard.xmlwidget;

import com.tms.collab.formwizard.model.FormException;
import com.tms.collab.formwizard.model.FormFieldDataObject;

import java.util.StringTokenizer;



import org.jdom.Attribute;
import org.jdom.Element;


public class SelectElement extends Element implements XmlWidgetAttributes{
    static final public String ELEMENT_NAME = "selectbox";
	
	public SelectElement(FormFieldDataObject ffdo) throws FormException {
			this(ffdo.getFormFieldId(), ffdo.getOptions(), false, ffdo.getRequiredFlag(), ffdo.getHiddenFlag(),
                 ffdo.getColspan(),ffdo.getRowspan(),ffdo.getAlign(),ffdo.getValign());
	}

    public SelectElement(String name, String options, String requiredField,String hiddenField,
                         String colspan, String rowspan, String align, String valign)
            throws FormException {
        this(name, options, false, requiredField, hiddenField, colspan, rowspan, align, valign);
    }

    public SelectElement(String name, String options, boolean multiple, String requiredField, String hiddenField,
                         String colspan, String rowspan, String align, String valign)
        throws FormException {
        super(ELEMENT_NAME);
        if (isNullOrEmpty(name)) {
            throw new FormException("Name can not be null");
        }
        setAttribute("name", name);
        setOptions(options);
        if (multiple) {
            setAttribute("multiple", "true");
        }
        
		if ("1".equals(hiddenField)) {
			setAttribute("hidden", "true");
		}
		
		if (!isNullOrEmpty(hiddenField)) {
			setAttribute("hidden", hiddenField);
		}
		

        
		if ("1".equals(requiredField)) {
			Element validator = new Element("validator_notempty");
			validator.setAttribute("name","required");
			validator.setAttribute("text", "Required Field!");
			addContent(validator);						
		}
		
		if (!isNullOrEmpty(requiredField))
			setAttribute("require", requiredField);

        if (colspan != null)
            setAttribute("colspan",colspan);

        if (rowspan != null)
            setAttribute("rowspan",rowspan);

        if (align != null)
            setAttribute("align",align);

        if (valign != null)
            setAttribute("valign",valign);
    }

    public void setOptions(String options) {
        if (isNullOrEmpty(options)) {
            return;
        }
        StringTokenizer tokenizer = new StringTokenizer(options, "\r\n");
        StringBuffer optionBuffer = new StringBuffer();
        while (tokenizer.hasMoreTokens()) {
            String option = tokenizer.nextToken();
            int ndx = option.indexOf('=');
            if (ndx < 0) {
                optionBuffer.append(option).append('=').append(option);
            } else {
                optionBuffer.append(option);
            }
            optionBuffer.append(';');
        }
        setAttribute("options", optionBuffer.toString().substring(0, optionBuffer.length() - 1));
    }

    private boolean isNullOrEmpty(String s) {
        return (s == null || s.trim().length() < 1);
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

        String multipleStr = e.getAttributeValue("multiple");
        if (multipleStr == null) {
            multipleStr = "false";
        }

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


        SelectElement selection = new SelectElement(
            e.getAttributeValue("name"),
            e.getAttributeValue("options"),
            Boolean.valueOf(multipleStr).booleanValue(),
			requiredField,
			hiddenField,
            colspan,
            rowspan,
            align,
            valign);
        return selection;
    }
}
