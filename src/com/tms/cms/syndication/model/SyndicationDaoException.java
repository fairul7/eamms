package com.tms.cms.syndication.model;

public class SyndicationDaoException extends Exception {
    public SyndicationDaoException(){
    }

    public SyndicationDaoException(String s) {
        super(s);
    }

    public SyndicationDaoException(String s, Throwable throwable) {
        super(s,throwable);
    }

    public void printStackTrace() {
        super.printStackTrace();
    }
}
