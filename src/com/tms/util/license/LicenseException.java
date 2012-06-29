package com.tms.util.license;

import kacang.util.DefaultException;

public class LicenseException extends DefaultException {
    public LicenseException() {
    }

    public LicenseException(String message) {
        super(message);
    }

    public LicenseException(String message, Throwable cause) {
        super(message, cause);
    }

    public LicenseException(Throwable cause) {
        super(cause);
    }
}
