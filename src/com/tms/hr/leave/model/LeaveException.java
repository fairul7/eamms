package com.tms.hr.leave.model;

import kacang.util.DefaultException;

public class LeaveException extends DefaultException {

    public LeaveException() {
        super();
    }

    public LeaveException(String s) {
        super(s);
    }

    public LeaveException(Throwable cause) {
        super(cause);
    }

    public LeaveException(String s, Throwable cause) {
        super(s, cause);
    }
    
}
