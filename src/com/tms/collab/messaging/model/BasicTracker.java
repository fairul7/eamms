package com.tms.collab.messaging.model;

import java.util.Date;
import java.util.Calendar;
import java.io.Serializable;

/**
 * A simple implementation of ProgressTracker for use by MessagingUserStatus.
 *
 * @see ProgressTracker
 * @see MessagingUserStatus
 */
public class BasicTracker implements ProgressTracker, Serializable {
    protected String status;
    protected int progressPercentage;
    protected String message;
    protected Date updateDate;

    public BasicTracker() {
        reset();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(int progressPercentage) {
        if(progressPercentage<0) {
            progressPercentage=0;
        } else if(progressPercentage>100) {
            progressPercentage=100;
        } else {
            this.progressPercentage = progressPercentage;
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void update(String status, int progressPercentage, String message) {
        setStatus(status);
        setProgressPercentage(progressPercentage);
        setMessage(message);
        updateDate = Calendar.getInstance().getTime();
    }

    public void reset() {
        update(ProgressTracker.STATUS_NOT_STARTED, 0, "");
    }

    public Date getUpdateDate() {
        return updateDate;
    }

}
