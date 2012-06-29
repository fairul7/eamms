package com.tms.collab.formwizard.engine;

public class TextFieldField extends Field {    
    private String type;

    private String size;
    private String maxLength;


    private ValidatorNotEmptyField validatorNotEmpty;
    private ValidatorIsIntegerField validatorIsInteger;
    private ValidatorIsNumericField validatorIsNumeric;
    private ValidatorEmailField validatorEmail;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(String maxLength) {
        this.maxLength = maxLength;
    }

    
    public ValidatorNotEmptyField getValidatorNotEmpty() {
        return validatorNotEmpty;
    }

    public void setValidatorNotEmpty(ValidatorNotEmptyField validatorNotEmpty) {
        this.validatorNotEmpty = validatorNotEmpty;
    }

    public ValidatorIsIntegerField getValidatorIsInteger() {
        return validatorIsInteger;
    }

    public void setValidatorIsInteger(ValidatorIsIntegerField validatorIsInteger) {
        this.validatorIsInteger = validatorIsInteger;
    }

    public ValidatorIsNumericField getValidatorIsNumeric() {
        return validatorIsNumeric;
    }

    public void setValidatorIsNumeric(ValidatorIsNumericField validatorIsNumeric) {
        this.validatorIsNumeric = validatorIsNumeric;
    }

    public ValidatorEmailField getValidatorEmail() {
        return validatorEmail;
    }

    public void setValidatorEmail(ValidatorEmailField validatorEmail) {
        this.validatorEmail = validatorEmail;
    }
}

