package com.tms.collab.isr.ui;

import kacang.stdui.FormField;
import kacang.stdui.validator.Validator;

public class ValidatorIsInteger extends Validator {
	private boolean optional;

    public ValidatorIsInteger() {
        super();
    }

    public ValidatorIsInteger(String name) {
        super(name);
    }

    public ValidatorIsInteger(String name, String text) {
        super(name);
        setText(text);
    }

    public ValidatorIsInteger(String name, String text, boolean optional) {
        super(name);
        setText(text);
        setOptional(optional);
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public boolean validate(FormField formField) {
        try {
            Object val = formField.getValue();
            if (optional && (val == null || val.toString().trim().length() == 0)) {
                return true;
            }
            Integer.parseInt(val.toString());
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
