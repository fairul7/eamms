package com.tms.collab.formwizard.engine;

import java.util.Map;

public class SelectBoxField extends Field {
    private String options;
    private ValidatorNotEmptyField validatorNotEmpty;
    private Map values;

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public ValidatorNotEmptyField getValidatorNotEmpty() {
        return validatorNotEmpty;
    }

    public void setValidatorNotEmpty(ValidatorNotEmptyField validatorNotEmpty) {
        this.validatorNotEmpty = validatorNotEmpty;
    }

    public Map getValues() {
        return values;
    }

    public void setValues(Map values) {
        this.values = values;
    }
}
