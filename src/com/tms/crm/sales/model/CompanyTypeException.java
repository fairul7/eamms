package com.tms.crm.sales.model;

import kacang.util.DefaultException;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jul 1, 2004
 * Time: 3:14:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class CompanyTypeException extends DefaultException{

    public CompanyTypeException() {
    }

    public CompanyTypeException(String s) {
        super(s);
    }

    public CompanyTypeException(Throwable cause) {
        super(cause);
    }

    public CompanyTypeException(String s, Throwable cause) {
        super(s, cause);
    }
}
