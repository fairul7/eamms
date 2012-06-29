package com.tms.collab.directory.model;

import kacang.util.DefaultException;

public class AddressBookException extends DefaultException {
    public AddressBookException() {
    }

    public AddressBookException(String toEmail) {
        super(toEmail);
    }

    public AddressBookException(Throwable throwable) {
        super(throwable);
    }

    public AddressBookException(String toEmail, Throwable throwable) {
        super(toEmail, throwable);
    }
}
