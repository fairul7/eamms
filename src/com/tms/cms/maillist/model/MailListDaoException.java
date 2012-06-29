package com.tms.cms.maillist.model;

import kacang.util.DefaultException;

public class MailListDaoException extends DefaultException {

    public MailListDaoException() {
        super();
    }

    public MailListDaoException(String message) {
        super(message);
    }

    public MailListDaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public MailListDaoException(Throwable cause) {
        super(cause);
    }

}