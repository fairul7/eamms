package com.tms.ekms.ekpmaildaemon.model;

import kacang.util.DefaultException;



public class EkpMailDaemonException extends DefaultException {
    public EkpMailDaemonException() {
    }

    public EkpMailDaemonException(String s) {
        super(s);
    }

    public EkpMailDaemonException(Throwable throwable) {
        super(throwable);
    }

    public EkpMailDaemonException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
