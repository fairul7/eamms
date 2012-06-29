package com.tms.hr.leave.model;

/**
 * Exception thrown when leave is already applied.
 */
public class DuplicateDateException extends LeaveException {

    public DuplicateDateException() {
        super();
    }

    public DuplicateDateException(String s) {
        super(s);
    }

    public DuplicateDateException(Throwable cause) {
        super(cause);
    }

    public DuplicateDateException(String s, Throwable cause) {
        super(s, cause);
    }
}
