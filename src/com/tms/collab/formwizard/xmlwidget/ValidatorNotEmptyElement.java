package com.tms.collab.formwizard.xmlwidget;

import org.jdom.Element;


/**
 * @author Siow Lin Hue
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ValidatorNotEmptyElement extends Element {
    public static final String ELEMENT_NAME = "validator_notempty";
	
	public ValidatorNotEmptyElement() {
		super(ELEMENT_NAME);
        setAttribute("name", "required");
        setAttribute("text", "Required Field!");
        
		
	}
    
    
    
    

	
}
