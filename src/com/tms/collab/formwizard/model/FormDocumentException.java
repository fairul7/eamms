package com.tms.collab.formwizard.model;


public class FormDocumentException extends Exception{
    public FormDocumentException() {
    }

    public FormDocumentException(String s) {
        super(s);
    }


    public FormDocumentException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public void printStackTrace() {
        super.printStackTrace();
    }
}

