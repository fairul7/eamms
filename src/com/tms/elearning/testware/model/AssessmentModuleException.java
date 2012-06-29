package com.tms.elearning.testware.model;

/**
 * Created by IntelliJ IDEA.
 * User: mahe
 * Date: Nov 23, 2004
 * Time: 2:53:29 PM
 * To change this template use Options | File Templates.
 */
public class AssessmentModuleException extends RuntimeException{
    public AssessmentModuleException() {
    }

    public AssessmentModuleException(String toEmail) {
        super(toEmail);
    }
}
