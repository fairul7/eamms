package com.tms.collab.formwizard.xmlwidget;

import com.tms.collab.formwizard.model.FormException;




/**
 * @author Siow Lin Hue
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class RadioOptionElement extends OptionElement {

	
	public RadioOptionElement(String name, String value, String text, String checkedStr, String groupName) throws FormException {
        this(name, value, text, Boolean.valueOf(checkedStr).booleanValue(), groupName);	
	}
    
    public RadioOptionElement(String name, String value, String text, boolean checked, String groupName) throws FormException {
        super(name,value,text,checked, ButtonGroupElement.RADIO_ELEMENT_NAME);
        setAttribute("groupName",groupName);
    }
    


}
