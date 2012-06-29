package com.tms.cms.ad.model;

import kacang.util.DefaultException;

public class AdException extends DefaultException {

    public AdException() {
        super();
    }

    public AdException(String message) {
        super(message);
    }

    public AdException(String message, Throwable cause) {
        super(message, cause);
    }

    public AdException(Throwable cause) {
        super(cause);
    }

}