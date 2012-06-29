package com.tms.hr.leave.model;

public class DuplicateKeyException extends LeaveException {

    public DuplicateKeyException() {
        super();
    }

    public DuplicateKeyException(String message) {
        super(message);
    }

}