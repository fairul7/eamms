package com.tms.hr.leave.model;

/**
 * Exception thrown when leave is in invalid state, e.g. trying to approve leave that is not submitted.
 */
public class InvalidStatusException extends LeaveException {

    public InvalidStatusException() {
        super();
    }

    public InvalidStatusException(String s) {
        super(s);
    }

    public InvalidStatusException(Throwable cause) {
        super(cause);
    }

    public InvalidStatusException(String s, Throwable cause) {
        super(s, cause);
    }
}
