package com.tms.collab.formwizard.model;

public class FormDaoException extends Exception{
    public FormDaoException() {
    }

    public FormDaoException(String s) {
        super(s);
    }


    public FormDaoException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public void printStackTrace() {
        super.printStackTrace();
    }
}
