package com.tms.collab.formwizard.xmlwidget;

import org.jdom.Element;
import kacang.Application;


public class ValidatorIsNumericElement extends Element {
    public static final String ELEMENT_NAME="validator_numeric";

    public ValidatorIsNumericElement() {
        super(ELEMENT_NAME);
        setAttribute("name", "numeric");
        setAttribute("text", Application.getInstance().getMessage("formWizard.xmlwidget.validatorIsNumericElement.message",
                                                                  "Please enter number"));
        setAttribute("optional", "true");
    }


}
