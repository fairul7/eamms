package com.tms.collab.formwizard.ui.validator;

import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.stdui.FormField;

public class ValidatorIsInteger extends ValidatorIsNumeric {
    public ValidatorIsInteger() {
        super();
    }
    
    public ValidatorIsInteger(String name) {
        super(name);
    }

    public ValidatorIsInteger(String name, String text) {
        super(name,text);
    }

    public boolean validate(FormField formField) {
        boolean isNumeric = super.validate(formField);
        if (isNumeric) {

           Object val = formField.getValue();
            if (isOptional() && (val == null || val.toString().trim().length() == 0)) {
                return true;
            }

            try {
                Integer.parseInt(val.toString());
            }
            catch (Exception e) {
                return false;
            }

        }
        return isNumeric;
    }
}
