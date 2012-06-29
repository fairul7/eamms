package com.tms.collab.formwizard.xmlwidget;



import com.tms.collab.formwizard.model.FormException;
import com.tms.collab.formwizard.model.FormFieldDataObject;

import org.jdom.Attribute;
import org.jdom.Element;


public class DateFieldElement extends Element implements XmlWidgetAttributes{
    static final public String ELEMENT_NAME = "datefield";
    
	public DateFieldElement(FormFieldDataObject ffdo) throws FormException {
		this(ffdo.getFormFieldId(),ffdo.getRequiredFlag(),ffdo.getHiddenFlag(),ffdo.getColspan(),ffdo.getRowspan(),
             ffdo.getAlign(),ffdo.getValign());
	}
	
    public DateFieldElement(String name, String requiredField, String hiddenField, String colspan, String rowspan,
                            String align, String valign)
            throws FormException {
        super(ELEMENT_NAME);
        if (isNullOrEmpty(name)) {
            throw new FormException("Name can not be null");
        }
        setAttribute("name", name);
        setAttribute("type", Integer.toString(FormElement.FORM_DATETIME_TYPE));
        
		setAttribute("hidden", hiddenField);
				
		
		

        
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

    public int getType() {
        return FormElement.FORM_DATETIME_TYPE;
    }

    public void removeMetaData() {
        removeAttribute("type");        
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


        return new DateFieldElement(e.getAttribute("name").getValue(),requiredField, hiddenField,colspan,rowspan,
                                    align,valign);
    }
}
