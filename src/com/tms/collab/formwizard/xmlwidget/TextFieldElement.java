package com.tms.collab.formwizard.xmlwidget;


import com.tms.collab.formwizard.model.FormException;
import com.tms.collab.formwizard.model.FormFieldDataObject;

import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Element;



public class TextFieldElement extends Element implements XmlWidgetAttributes{
    public static final String ELEMENT_NAME = "textfield";
    
    public TextFieldElement(FormFieldDataObject ffdo) throws FormException {
    	this(ffdo.getFormFieldId(),
    		 ffdo.getDefaultValue(),
			 ffdo.getFieldSize(),
			 ffdo.getMaxLength(),
			 ffdo.getDataType(),
			 ffdo.getRequiredFlag(),
			 ffdo.getHiddenFlag(),
             ffdo.getColspan(),
             ffdo.getRowspan(),
             ffdo.getAlign(),
             ffdo.getValign());
    }

    public TextFieldElement(String name) throws FormException {
        this(name, null, null, null, null, null, null,null,null,"left","top");
    }
    
	public TextFieldElement(String name, String value, String sizeStr, String maxLengthStr,
		String typeStr, String requiredField, String hiddenField, String colspan, String rowspan,String align, String valign)
            throws FormException {
			
		super(ELEMENT_NAME);
		if (isNullOrEmpty(name)) {
			throw new FormException("Name Can not be Null");
		}
		setAttribute("name", name);

		if (!isNullOrEmpty(value)) {
			setAttribute("value", value);
		}

		if (!isNullOrEmpty(sizeStr)) {
			setAttribute("size", sizeStr);
		}

		if (!isNullOrEmpty(maxLengthStr)) {
			setAttribute("maxLength", maxLengthStr);
		}
		
		if ("1".equals(hiddenField)) {
			setAttribute("hidden", "true");
		}
		
		if (!isNullOrEmpty(hiddenField)) {
			setAttribute("hidden", hiddenField);
		}
		
		int sqlType = -1;
		try {
			sqlType = Integer.parseInt(typeStr);
		}
		catch (NumberFormatException numExp) {
			sqlType=1;
		}
		setAttribute("type", Integer.toString(sqlType));

		if(sqlType==FormElement.FORM_NUMERIC_TYPE){
            addContent(new ValidatorIsIntegerElement());
		} 
		else if(sqlType ==FormElement.FORM_EMAIL_TYPE){
			Element validator = new Element("kacang.stdui.validator.ValidatorEmail");
			validator.setAttribute("name", "email");
			validator.setAttribute("text", "Please enter valid email");
			addContent(validator);
		}
        else if (FormElement.FORM_DECIMAL_NUMBER == sqlType) {
            addContent(new ValidatorIsNumericElement());
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

    
    public int getSize() {
        return getIntAttrWithDef("maxLength", 35);
    }

    public int getType() {
        return getIntAttrWithDef("type", -1);
    }

    public void removeMetaData() {
        removeAttribute("template");                
    }

    public void removeMetaData(String attrName) {
        removeAttribute(attrName);
    }

    private boolean isNullOrEmpty(String s) {
        return (s == null || s.trim().length() < 1);
    }

    private int getIntAttrWithDef(String key, int defaultValue) {
        try {
            return getAttribute(key).getIntValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static Element newInstance(Element e) throws FormException {
        if (!ELEMENT_NAME.equals(e.getName())) {
            return null;
        }

        // possiblily null
        Attribute valueAttr = e.getAttribute("value");
        String valueStr = null;
        if (valueAttr != null) {
            valueStr = valueAttr.getValue();
        }

        // possiblily null
        Attribute sizeAttr = e.getAttribute("size");
        String sizeStr = null;
        if (sizeAttr != null) {
            sizeStr = sizeAttr.getValue();
        }

        // possiblily null
        Attribute maxLengthAttr = e.getAttribute("maxLength");
        String maxLengthStr = null;
        if (maxLengthAttr != null) {
            maxLengthStr = maxLengthAttr.getValue();
        }

        Attribute requiredFieldAttr = e.getAttribute("require");
        String requiredField = null;
        if (requiredFieldAttr != null)
        	requiredField = requiredFieldAttr.getValue();

		Attribute hiddenFieldAttr = e.getAttribute("hidden");
		String hiddenField = null;
	    if (hiddenFieldAttr != null)
			hiddenField = hiddenFieldAttr.getValue();

		Attribute typeAttr = e.getAttribute("type");
		String type = null;
		if (typeAttr != null)
			type = typeAttr.getValue();

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


        return new TextFieldElement(
            e.getAttribute("name").getValue(),
            valueStr,
            sizeStr,
            maxLengthStr,
            type,
			requiredField,
			hiddenField,
            colspan,
            rowspan,
            align,
            valign);
    }

}
