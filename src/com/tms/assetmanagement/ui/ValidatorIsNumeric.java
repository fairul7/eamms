package com.tms.assetmanagement.ui;

import kacang.stdui.FormField;
import kacang.stdui.validator.Validator;

public class ValidatorIsNumeric extends Validator {

    private boolean optional;

    public ValidatorIsNumeric() {
        super();
    }

    public ValidatorIsNumeric(String name) {
        super(name);
    }

    public ValidatorIsNumeric(String name, String text) {
        super(name);
        setText(text);
    }

    public ValidatorIsNumeric(String name, String text, boolean mandatory) {
        super(name);
        setText(text);
        setOptional(mandatory);
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
            if (val == null || val.toString().trim().length() == 0){
            	return true;
            }
            if (optional) {
                return true;
            }
            Float.parseFloat(val.toString());
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
