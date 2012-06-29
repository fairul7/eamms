package com.tms.elearning.lesson.model;

/**
 * Created by IntelliJ IDEA.
 * User: tirupati
 * Date: Oct 26, 2004
 * Time: 3:53:03 PM
 * To change this template use File | Settings | File Templates.
 */

public class LessonException extends RuntimeException {
    public LessonException() {
    }

    public LessonException(String toEmail) {
        super(toEmail);
    }
}