package com.tms.cms.core.model;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Mar 20, 2003
 * Time: 12:06:21 PM
 * To change this template use Options | File Templates.
 */
public class InvalidKeyException extends Exception {
    public InvalidKeyException() {
    }

    public InvalidKeyException(String s) {
        super(s);
    }
}
