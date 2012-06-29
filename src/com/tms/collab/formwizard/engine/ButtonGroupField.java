package com.tms.collab.formwizard.engine;

import java.util.List;

public class ButtonGroupField extends Field {
    private String type;
    private ValidatorNotEmptyField validatorNotEmpty;
    private List checkBoxList;
    private List radioList;



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public ValidatorNotEmptyField getValidatorNotEmpty() {
        return validatorNotEmpty;
    }

    public void setValidatorNotEmpty(ValidatorNotEmptyField validatorNotEmpty) {
        this.validatorNotEmpty = validatorNotEmpty;
    }

    public List getCheckBoxList() {
        return checkBoxList;
    }

    public void setCheckBoxList(List checkBoxList) {
        this.checkBoxList = checkBoxList;
    }

    public List getRadioList() {
        return radioList;
    }

    public void setRadioList(List radioList) {
        this.radioList = radioList;
    }
}
