package com.tms.hr.leave.model;

/**
 * Exception thrown when the start and/or end dates for a leave is invalid 
 */
public class InvalidDateException extends LeaveException {

    public InvalidDateException() {
        super();
    }

    public InvalidDateException(String s) {
        super(s);
    }

    public InvalidDateException(Throwable cause) {
        super(cause);
    }

    public InvalidDateException(String s, Throwable cause) {
        super(s, cause);
    }

}
