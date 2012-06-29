package com.tms.collab.messaging.model;

import kacang.util.DefaultException;

public class MessagingException extends DefaultException {
    public MessagingException() {
        super();
    }

    public MessagingException(String s) {
        super(s);
    }

    public MessagingException(Throwable throwable) {
        super(throwable);
    }

    public MessagingException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
