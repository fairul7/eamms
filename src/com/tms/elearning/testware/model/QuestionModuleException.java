package com.tms.elearning.testware.model;

/**
 * Created by IntelliJ IDEA.
 * User: mahe
 * Date: Nov 2, 2004
 * Time: 4:16:59 PM
 * To change this template use Options | File Templates.
 */
public class QuestionModuleException extends RuntimeException {
    public QuestionModuleException() {
    }

    public QuestionModuleException(String toEmail) {
        super(toEmail);
    }
}

