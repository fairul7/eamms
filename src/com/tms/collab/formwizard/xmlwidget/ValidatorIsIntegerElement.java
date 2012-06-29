package com.tms.collab.formwizard.xmlwidget;

import org.jdom.Element;
import com.tms.collab.formwizard.ui.validator.ValidatorIsInteger;
import kacang.Application;


public class ValidatorIsIntegerElement extends Element {
    public static final String ELEMENT_NAME= ValidatorIsInteger.class.getName();


    public ValidatorIsIntegerElement() {
        super(ELEMENT_NAME);
        setAttribute("name", "integer");
		setAttribute("text", Application.getInstance().getMessage("formWizard.xmlwidget.validatorIsIntegerElement.message",
                                                                  "Please enter integer number"));
        setAttribute("optional","true");
    }
}
