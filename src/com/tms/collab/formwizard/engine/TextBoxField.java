package com.tms.collab.formwizard.engine;

public class TextBoxField extends Field{
    private String rows;
    private String cols;


    private ValidatorNotEmptyField validatorNotEmpty;

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    public String getCols() {
        return cols;
    }

    public void setCols(String cols) {
        this.cols = cols;
    }

    public ValidatorNotEmptyField getValidatorNotEmpty() {
        return validatorNotEmpty;
    }

    public void setValidatorNotEmpty(ValidatorNotEmptyField validatorNotEmpty) {
        this.validatorNotEmpty = validatorNotEmpty;
    }

}
