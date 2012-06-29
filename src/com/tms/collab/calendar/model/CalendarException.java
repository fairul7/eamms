package com.tms.collab.calendar.model;


/**
 * Generic exception thrown for Calendar-related errors.
 */
public class CalendarException extends Exception {

    public CalendarException() {
        super();
    }

    public CalendarException(String message) {
        super(message);
    }

    public CalendarException(String message, Throwable cause) {
        super(message, cause);
    }

}