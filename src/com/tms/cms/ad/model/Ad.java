package com.tms.cms.ad.model;

import kacang.model.DefaultDataObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Ad extends DefaultDataObject {
    private String name;

    // --- integration?
    // will only show com.tms.cms.ad if context is null or in current context
    // context is determined by URL parameter - or other means
    private List contextList;

    // --- active flag
    private boolean active;

    // --- com.tms.cms.ad's URL link
    private String url;
    private boolean newWindow;

    // --- standard com.tms.cms.ad - image file
    private String imageFile;
    private String alternateText;

    // --- for non-image ads, use script
    private boolean useScript;
    private String script;

    // --- content scheduling data
    private Date startDate;
    private boolean startDateEnabled;
    private Date endDate;
    private boolean endDateEnabled;

    public Ad() {
        contextList = new ArrayList();
    }

    public boolean equals(Object obj) {
        if(obj instanceof Ad) {
            if(((Ad)obj).getId().equals(getId())) {
                return true;
            }
        }

        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getAlternateText() {
        return alternateText;
    }

    public void setAlternateText(String alternateText) {
        this.alternateText = alternateText;
    }

    public List getContextList() {
        return contextList;
    }

    public void setContextList(List contextList) {
        this.contextList = contextList;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isEndDateEnabled() {
        return endDateEnabled;
    }

    public void setEndDateEnabled(boolean endDateEnabled) {
        this.endDateEnabled = endDateEnabled;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public boolean isNewWindow() {
        return newWindow;
    }

    public void setNewWindow(boolean newWindow) {
        this.newWindow = newWindow;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public boolean isStartDateEnabled() {
        return startDateEnabled;
    }

    public void setStartDateEnabled(boolean startDateEnabled) {
        this.startDateEnabled = startDateEnabled;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isUseScript() {
        return useScript;
    }

    public void setUseScript(boolean useScript) {
        this.useScript = useScript;
    }

    public String getAdId() {
        return super.getId();
    }

    public void setAdId(String id) {
        super.setId(id);
    }

}