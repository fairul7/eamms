package com.tms.collab.formwizard.model;


public class FormNameExistsException extends FormException{
    public FormNameExistsException() {
    }

    public FormNameExistsException(String s) {
        super(s);
    }
}
