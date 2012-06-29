package com.tms.collab.formwizard.model;

public class FormException extends Exception{
    public FormException() {
    }

    public FormException(String s) {
        super(s);
    }


    public FormException(String s, Throwable throwable) {
        super(s, throwable);
    }
    
    public void printStackTrace() {
        super.printStackTrace();
    }
}
