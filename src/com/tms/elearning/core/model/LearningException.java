package com.tms.elearning.core.model;

import kacang.util.DefaultException;

import kacang.util.DefaultException;

public class LearningException extends DefaultException {
    public LearningException() {
    }

    public LearningException(String toEmail) {
        super(toEmail);
    }

    public LearningException(Throwable throwable) {
        super(throwable);
    }

    public LearningException(String toEmail, Throwable throwable) {
        super(toEmail, throwable);
    }
}
