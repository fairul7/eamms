package com.tms.collab.messaging.model;

import kacang.util.DefaultException;

public class MessagingDaoException extends DefaultException {
    public MessagingDaoException() {
        super();
    }

    public MessagingDaoException(String s) {
        super(s);
    }

    public MessagingDaoException(Throwable throwable) {
        super(throwable);
    }

    public MessagingDaoException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
