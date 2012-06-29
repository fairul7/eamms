package com.tms.collab.formwizard.xmlwidget;

import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

import com.tms.collab.formwizard.model.FormException;
import com.tms.collab.formwizard.model.FormFieldDataObject;


public class ButtonGroupElement extends Element implements XmlWidgetAttributes {
    static final public String ELEMENT_NAME = "buttongroup";
    public static final String CHECKBOX_ELEMENT_NAME = "checkbox";
    public static final String RADIO_ELEMENT_NAME = "radio";

    public ButtonGroupElement(FormFieldDataObject ffdo) throws FormException {
        this(ffdo.getFormFieldId(), ffdo.getRequiredFlag(), ffdo.getHiddenFlag(),ffdo.getColspan(),ffdo.getRowspan(),
             ffdo.getAlign(),ffdo.getValign());

    }

    public ButtonGroupElement(String name, String requiredField, String hiddenField, String colspan, String rowspan,
                              String align, String valign)
            throws FormException {
        super(ELEMENT_NAME);
        if (isNullOrEmpty(name)) 
            throw new FormException("Name can not be null");
        
        setAttribute("name", name);

        
		if ("1".equals(hiddenField)) {
			setAttribute("hidden", "true");
		}
		
        if ("1".equals(requiredField)) {
            Element validator = new Element("validator_notempty");
            validator.setAttribute("name","required");
            validator.setAttribute("text", "Required Field!");
            addContent(validator);                        
        }
        if (!isNullOrEmpty(requiredField))
            setAttribute("require", requiredField);
            
		if (!isNullOrEmpty(hiddenField)) 
			setAttribute("hidden", hiddenField);


        if (colspan != null)
            setAttribute("colspan",colspan);

        if (rowspan != null)
            setAttribute("rowspan",rowspan);

        if (align != null)
            setAttribute("align",align);

        if (valign != null)
            setAttribute("valign",valign);

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
        String name = elementType(e);
        
        
        if (name != null && name.equals(CHECKBOX_ELEMENT_NAME))
            return CheckBoxElement.newInstance(e);
        else if (name != null && name.equals(RADIO_ELEMENT_NAME))
            return RadioElement.newInstance(e);
                
        
        return null;
    }
    
    public static String elementType(Element e) {
        String name = null;
        Element childElement;
        List children = e.getChildren();
        
        for (Iterator it = children.iterator(); it.hasNext();) {            
            childElement = (Element) it.next();
            name = childElement.getName();
            if (name.equals(CHECKBOX_ELEMENT_NAME)) {
                return CHECKBOX_ELEMENT_NAME;            
            }
            else if (name.equals(RADIO_ELEMENT_NAME)) {
                return RADIO_ELEMENT_NAME;                
            }
                
        }
        return null;
    
    }

   

}
