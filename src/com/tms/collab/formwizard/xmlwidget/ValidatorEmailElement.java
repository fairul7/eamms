package com.tms.collab.formwizard.xmlwidget;

import org.jdom.Element;


public class ValidatorEmailElement extends Element {
    public static final String ELEMENT_NAME="validator_email";

    public ValidatorEmailElement() {
        super(ELEMENT_NAME);
        setAttribute("name", "email");
        setAttribute("text", "Please enter valid email");        
    }
}
