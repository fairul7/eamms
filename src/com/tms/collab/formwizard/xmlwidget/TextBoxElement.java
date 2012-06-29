package com.tms.collab.formwizard.xmlwidget;




import com.tms.collab.formwizard.model.FormException;
import com.tms.collab.formwizard.model.FormFieldDataObject;

import org.jdom.Element;
import org.jdom.Attribute;


public class TextBoxElement extends Element implements XmlWidgetAttributes{
    static final public String ELEMENT_NAME = "textbox";

	public TextBoxElement(FormFieldDataObject ffdo) throws FormException {
			this(ffdo.getFormFieldId(),
				 ffdo.getDefaultValue(),
				 ffdo.getMaxRows(),
				 ffdo.getMaxCols(),
				 ffdo.getRequiredFlag(),
				 ffdo.getHiddenFlag(),
                 ffdo.getColspan(),
                 ffdo.getRowspan(),
                 ffdo.getAlign(),
                 ffdo.getValign());
	}

	
    public TextBoxElement(String name) throws FormException {
        this(name, null, null, null, null,null,"1","1","left","top");
    }

    public TextBoxElement(String name, String defaultValue,
                     String rowsStr, String columnsStr, String requiredField, String hiddenField,
                     String colspan, String rowspan,String align, String valign)
            throws FormException {
        super(ELEMENT_NAME);
        if (isNullOrEmpty(name)) {
            throw new FormException("Name can not be null or empty");
        }
        setAttribute("name", name);

        if (!isNullOrEmpty(rowsStr)) {
            setAttribute("rows", rowsStr);
        }

        if (!isNullOrEmpty(columnsStr)) {
            setAttribute("cols", columnsStr);
        }

        if (!isNullOrEmpty(defaultValue)) {
            setAttribute("value", defaultValue);
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

    public int getSize() {
        return 0;
    }

    public int getDecimal() {
        return 0;
    }

    public int getType() {
        return FormElement.FORM_CLOB_TYPE;
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

    public static Element newInstance(Element e) throws FormException {
        if (!ELEMENT_NAME.equals(e.getName())) {
            return null;
        }
        TextBoxElement textbox = null;

        // possiblily null
        Attribute valueAttr = e.getAttribute("value");
        String valueStr = null;
        if (valueAttr != null) {
            valueStr = valueAttr.getValue();
        }

        // possiblily null
        Attribute rowsAttr = e.getAttribute("rows");
        String rowsStr = null;
        if (rowsAttr != null) {
            rowsStr = rowsAttr.getValue();
        }

        // possiblily null
        Attribute colsAttr = e.getAttribute("cols");
        String colsStr = null;
        if (colsAttr != null) {
            colsStr = colsAttr.getValue();
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



        textbox = new TextBoxElement(
            e.getAttribute("name").getValue(),
            valueStr,
            rowsStr,
            colsStr,
			requiredField,
			hiddenField,
            colspan,
            rowspan,
            align,
            valign);
        return textbox;
    }
}
