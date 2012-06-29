package com.tms.cms.ad.model;

import kacang.util.DefaultException;

public class AdDaoException extends DefaultException {

    public AdDaoException() {
        super();
    }

    public AdDaoException(String message) {
        super(message);
    }

    public AdDaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public AdDaoException(Throwable cause) {
        super(cause);
    }

}