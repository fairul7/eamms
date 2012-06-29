package com.tms.collab.formwizard.engine;

import java.util.Date;

public class DateFieldField extends Field {
    private ValidatorNotEmptyField validatorNotEmpty;
    private Date date;


    public ValidatorNotEmptyField getValidatorNotEmpty() {
        return validatorNotEmpty;
    }

    public void setValidatorNotEmpty(ValidatorNotEmptyField validatorNotEmpty) {
        this.validatorNotEmpty = validatorNotEmpty;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
