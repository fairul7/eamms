package com.tms.ekms.search.model;

import kacang.util.DefaultException;

/**
 * Created by IntelliJ IDEA.
 * User: blake
 * Date: Mar 23, 2005
 * Time: 10:13:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class SearchProfileException extends DefaultException {
     public SearchProfileException() {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public SearchProfileException(String s) {
        super(s);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public SearchProfileException(Throwable throwable) {
        super(throwable);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public SearchProfileException(String s, Throwable throwable) {
        super(s, throwable);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
