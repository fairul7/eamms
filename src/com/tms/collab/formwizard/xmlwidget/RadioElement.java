package com.tms.collab.formwizard.xmlwidget;

import com.tms.collab.formwizard.model.FormException;
import com.tms.collab.formwizard.model.FormFieldDataObject;

import org.apache.commons.collections.SequencedHashMap;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Parent;

import java.util.List;
import java.util.Iterator;
import java.util.Set;




public class RadioElement extends ButtonGroupElement implements XmlWidgetAttributes{    

	public RadioElement(FormFieldDataObject ffdo) throws FormException {
		this(ffdo.getFormFieldId(),ffdo.getRequiredFlag(), ffdo.getHiddenFlag(),ffdo.getColspan(),ffdo.getRowspan(),
             ffdo.getAlign(),ffdo.getValign());
	}
	
    public RadioElement(String name, String requiredField, String hiddenField,String colspan, String rowspan,
                        String align,String valign)
            throws FormException {
       super(name,requiredField, hiddenField,colspan,rowspan,align,valign);
       

    }

   
    //public Element addContent(Element element) {
    public Parent addContent(Element element) {
        if (!element.getName().equals(ValidatorNotEmptyElement.ELEMENT_NAME))
            element.setName(RADIO_ELEMENT_NAME);
        return super.addContent(element);
    }

    public static Element newInstance(Element e) throws FormException {
        if (!ELEMENT_NAME.equals(e.getName())) {
            return null;
        }

        List options = e.getChildren(RADIO_ELEMENT_NAME);
        if (options.isEmpty()) {
            return null;
        }
        
		Attribute requiredFieldAttr = e.getAttribute("require");
		String requiredField = null;
		if (requiredFieldAttr != null)
			requiredField = requiredFieldAttr.getValue();
			
		Attribute hiddenFieldAttr = e.getAttribute("hidden");
		String hiddenField = "1";
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


        RadioElement radioGrp = new RadioElement(e.getAttributeValue("name"),requiredField,hiddenField,colspan,rowspan,
                                                 align,valign);
        for (Iterator optionsIter = options.iterator(); optionsIter.hasNext();) {
            Element elem = (Element) optionsIter.next();
            radioGrp.addContent(new RadioOptionElement(
                elem.getAttributeValue("name"),
                elem.getAttributeValue("value"),
                elem.getAttributeValue("text"),
                elem.getAttributeValue("checked"),
                elem.getAttributeValue("groupName")
            ));
        }
        return radioGrp;
    }
    
	public void addOptions(SequencedHashMap options,String groupName, Element e) throws FormException {
		Set keySet = options.keySet();
		int counter = 0;
		String key = "", value = "";
		for (Iterator it = keySet.iterator(); it.hasNext();counter++) {
			key = String.valueOf(it.next());
			value = String.valueOf(options.get(key));
			
			
			e.addContent(new RadioOptionElement(
						   String.valueOf(counter),
						   key,
						   value,
						   false,
						   groupName));
						
		}
			
	}
    

}
