package com.tms.hr.employee.model;

/**
 * Created by IntelliJ IDEA.
 * User: vijay
 * Date: Feb 3, 2004
 * Time: 9:15:45 AM
 * To change this template use Options | File Templates.
 */
public class EmailExistsException extends EmployeeException {
    public EmailExistsException() {
    }

    public EmailExistsException(String s) {
        super(s);
    }
}
