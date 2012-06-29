package com.tms.elearning.coursecategory.model;
/**
 * Created by IntelliJ IDEA.
 * User: tirupati
 * Date: Oct 19, 2004
 * Time: 5:10:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class CategoryModuleException extends RuntimeException {
    public CategoryModuleException() {
    }

    public CategoryModuleException(String toEmail) {
        super(toEmail);
    }
}