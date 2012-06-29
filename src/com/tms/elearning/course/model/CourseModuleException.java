package com.tms.elearning.course.model;

/**
 * Created by IntelliJ IDEA.
 * User: tirupati
 * Date: Oct 19, 2004
 * Time: 5:10:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class CourseModuleException extends RuntimeException {
    public CourseModuleException() {
    }

    public CourseModuleException(String toEmail) {
        super(toEmail);
    }
}