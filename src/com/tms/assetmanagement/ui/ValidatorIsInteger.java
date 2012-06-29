package com.tms.assetmanagement.ui;

import kacang.stdui.FormField;
import com.tms.assetmanagement.ui.ValidatorIsNumeric;

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
           if(val == null || val.toString().trim().length() == 0) {
        	 return true;  
           }
           if (isOptional()) {
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
