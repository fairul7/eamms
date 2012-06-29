package com.tms.crm.sales.model;

import kacang.util.DefaultException;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jun 29, 2004
 * Time: 3:30:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class SalesGroupException extends DefaultException{

    public SalesGroupException() {
    }

    public SalesGroupException(String s) {
        super(s);
    }

    public SalesGroupException(Throwable throwable) {
        super(throwable);
    }

    public SalesGroupException(String s, Throwable throwable) {
        super(s, throwable);
    }


}
