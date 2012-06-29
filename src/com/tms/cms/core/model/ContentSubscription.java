package com.tms.cms.core.model;

import kacang.model.DefaultDataObject;

public class ContentSubscription extends DefaultDataObject {

    private String userId;
    private String contentId;

    public ContentSubscription() {
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
