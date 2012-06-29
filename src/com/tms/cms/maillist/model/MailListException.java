package com.tms.cms.maillist.model;

import kacang.util.DefaultException;

public class MailListException extends DefaultException {

    public MailListException() {
        super();
    }

    public MailListException(String message) {
        super(message);
    }

    public MailListException(String message, Throwable cause) {
        super(message, cause);
    }

    public MailListException(Throwable cause) {
        super(cause);
    }

}