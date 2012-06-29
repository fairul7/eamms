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




public class CheckBoxElement extends ButtonGroupElement implements XmlWidgetAttributes{
	
	public CheckBoxElement(FormFieldDataObject ffdo) throws FormException {
		this(ffdo.getFormFieldId(),ffdo.getRequiredFlag(), ffdo.getHiddenFlag(),ffdo.getColspan(),ffdo.getRowspan(),
             ffdo.getAlign(),ffdo.getValign());
	}
	
    public CheckBoxElement(String name, String requiredField, String hiddenField,String colspan, String rowspan,
                           String align, String valign)
            throws FormException {
        super(name, requiredField,hiddenField,colspan,rowspan,align,valign);
        setAttribute("type", "checkbox");
        
    }

    public Parent addContent(Element element) {
        if (!element.getName().equals(ValidatorNotEmptyElement.ELEMENT_NAME))
            element.setName("checkbox");
        return super.addContent(element);
    }

    public static Element newInstance(Element e) throws FormException {
        if (!ELEMENT_NAME.equals(e.getName())) {
            return null;
        }

        List options = e.getChildren("checkbox");
        if (options == null) {
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

        CheckBoxElement checkboxGrp = new CheckBoxElement(e.getAttributeValue("name"),
                                                          requiredField, hiddenField,colspan,rowspan,align,valign);
        for (Iterator optionsIter = options.iterator(); optionsIter.hasNext();) {
            Element elem = (Element) optionsIter.next();
            checkboxGrp.addContent(new OptionElement(
                elem.getAttributeValue("name"),
                elem.getAttributeValue("value"),
                elem.getAttributeValue("text"),
                elem.getAttributeValue("checked")));
        }
        return checkboxGrp;
    }
    
	public void addOptions(SequencedHashMap options, Element e) throws FormException {
		   
		int counter = 0;
		Set keySet = options.keySet();
		String key = "", value = "";
		for (Iterator it = keySet.iterator(); it.hasNext(); counter++) {
			key = String.valueOf(it.next());
			value = String.valueOf(options.get(key));
			
			e.addContent(new OptionElement(
							String.valueOf(counter),
							key,
							value,
							false, e.getAttributeValue("type")));
		}
	}
}
