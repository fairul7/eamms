package com.tms.elearning.core.ui;

import kacang.stdui.FormField;
import kacang.stdui.validator.Validator;

import java.util.Collection;
import java.util.Map;


public class ValidatorNotEmpty extends Validator {

    public ValidatorNotEmpty() {
        super();
    }

    public ValidatorNotEmpty(String name) {
        super(name);
    }

    public ValidatorNotEmpty(String name, String text) {
        super(name);
        setText(text);
    }

    public boolean validate(FormField formField) {
        Object value = formField.getValue();
        if (value == null) {
            return false;
        }
        else if (value instanceof Collection || value instanceof Map) {
            if (((Collection) value).size() == 0) {
                return false;
            }
            else {
                Object obj = ((Collection)value).iterator().next();
                return (obj != null && obj.toString().trim().length() > 0);
            }
        }
        else {
            return value.toString().trim().length() > 0;
        }
    }

}
