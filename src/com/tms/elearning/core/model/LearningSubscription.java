package com.tms.elearning.core.model;

import kacang.model.DefaultDataObject;

/**
 * Created by IntelliJ IDEA.
 * User: vivek
 * Date: Mar 4, 2005
 * Time: 11:57:17 AM
 * To change this template use File | Settings | File Templates.
 */

import kacang.model.DefaultDataObject;

public class LearningSubscription extends DefaultDataObject {

    private String userId;
    private String contentId;

    public LearningSubscription() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

}
