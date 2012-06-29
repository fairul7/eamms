package com.tms.cms.syndication.ui.validator;

import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.stdui.FormField;

public class ValidatorRefreshRate extends ValidatorIsNumeric {

    public ValidatorRefreshRate(String name, String text, boolean mandatory) {
        super(name,text,mandatory);
    }

    public boolean validate(FormField formField) {
        boolean isNumeric = super.validate(formField);

        if (isNumeric) {
            Object val = formField.getValue();

            try {
                if (Integer.parseInt((String)val) < 5)
                    return false;
            }
            catch (Exception e) {
                return false;
            }            
        }

        return isNumeric;
    }
}
