package com.tms.hr.leave.model;

/**
 * Exception thrown when leave applied falls directly on weekends or holidays.
 */
public class HolidayException extends LeaveException {
    public HolidayException() {
        super();
    }

    public HolidayException(String s) {
        super(s);
    }

    public HolidayException(Throwable cause) {
        super(cause);
    }

    public HolidayException(String s, Throwable cause) {
        super(s, cause);
    }
}
