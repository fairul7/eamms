package com.tms.cms.mobile.model;

import kacang.model.DefaultDataObject;

import java.util.Date;

public class MobileChannel extends DefaultDataObject {

    public static final String REFRESH_ALWAYS = "always";
    public static final String REFRESH_HOURLY = "hourly";
    public static final String REFRESH_DAILY = "daily";
    public static final String REFRESH_ONCE = "once";

    private String title;
    private Date dateCreated;
    private Date dateModified;

    private long maxSize;
    private int depth;
    private boolean allowImages;
    private boolean offsiteLinks;
    private String refresh;

    private int refreshHourlyDuration;
    private Date refreshDailyTime;

    private String contentId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    public long getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public boolean isAllowImages() {
        return allowImages;
    }

    public void setAllowImages(boolean allowImages) {
        this.allowImages = allowImages;
    }

    public boolean isOffsiteLinks() {
        return offsiteLinks;
    }

    public void setOffsiteLinks(boolean offsiteLinks) {
        this.offsiteLinks = offsiteLinks;
    }

    public int getRefreshHourlyDuration() {
        return refreshHourlyDuration;
    }

    public void setRefreshHourlyDuration(int refreshHourlyDuration) {
        this.refreshHourlyDuration = refreshHourlyDuration;
    }

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }

    public Date getRefreshDailyTime() {
        return refreshDailyTime;
    }

    public void setRefreshDailyTime(Date refreshDailyTime) {
        this.refreshDailyTime = refreshDailyTime;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

}
