package com.tms.hr.leave.model;

/**
 * Exception thrown when there is insufficient balance.
 */
public class BalanceException extends LeaveException {
    public BalanceException() {
        super();
    }

    public BalanceException(String s) {
        super(s);
    }

    public BalanceException(Throwable cause) {
        super(cause);
    }

    public BalanceException(String s, Throwable cause) {
        super(s, cause);
    }
}
