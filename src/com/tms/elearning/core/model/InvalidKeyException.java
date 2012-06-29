package com.tms.elearning.core.model;

/**
 * Created by IntelliJ IDEA.
 * User: vivek
 * Date: Mar 4, 2005
 * Time: 11:43:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class InvalidKeyException extends Exception{

    public InvalidKeyException() {
    }

    public InvalidKeyException(String s) {
        super(s);
    }
}
