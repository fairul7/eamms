package com.tms.collab.messaging.model;

import org.apache.commons.collections.SequencedHashMap;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * This object tracks the status of a messaging user's 'server processes'. It
 * tracks status of check, download, send and server view processes for a user.
 */
public class MessagingUserStatus implements Serializable {
    private String userId;

    private ProgressTracker trackerCheck;
    private ProgressTracker trackerDownload;
    private ProgressTracker trackerSend;
    private ProgressTracker trackerServerView;

    private Date serverViewUpdateTime;
    private Map dataMap;

    protected MessagingUserStatus(String userId) {
        this.userId = userId;

        trackerCheck = new BasicTracker();
        trackerDownload = new BasicTracker();
        trackerSend = new BasicTracker();
        trackerServerView = new BasicTracker();
        serverViewUpdateTime = null;
        dataMap = new SequencedHashMap();
    }

    public boolean isPop3Busy() {
        if(isCheckBusy() || isDownloadBusy() || isServerViewBusy()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isCheckBusy() {
        if(ProgressTracker.STATUS_PROCESSING.equals(getTrackerCheck().getStatus())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isDownloadBusy() {
        if(ProgressTracker.STATUS_PROCESSING.equals(getTrackerDownload().getStatus())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isSendBusy() {
        if(ProgressTracker.STATUS_PROCESSING.equals(getTrackerSend().getStatus())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isServerViewBusy() {
        if(ProgressTracker.STATUS_PROCESSING.equals(getTrackerServerView().getStatus())) {
            return true;
        } else {
            return false;
        }
    }


    // === [ getters/setters ] =================================================
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ProgressTracker getTrackerCheck() {
        return trackerCheck;
    }

    public void setTrackerCheck(ProgressTracker trackerCheck) {
        this.trackerCheck = trackerCheck;
    }

    public ProgressTracker getTrackerDownload() {
        return trackerDownload;
    }

    public void setTrackerDownload(ProgressTracker trackerDownload) {
        this.trackerDownload = trackerDownload;
    }

    public ProgressTracker getTrackerSend() {
        return trackerSend;
    }

    public void setTrackerSend(ProgressTracker trackerSend) {
        this.trackerSend = trackerSend;
    }

    public ProgressTracker getTrackerServerView() {
        return trackerServerView;
    }

    public void setTrackerServerView(ProgressTracker trackerServerView) {
        this.trackerServerView = trackerServerView;
    }

    public Date getServerViewUpdateTime() {
        return serverViewUpdateTime;
    }

    public void setServerViewUpdateTime(Date serverViewUpdateTime) {
        this.serverViewUpdateTime = serverViewUpdateTime;
    }

    public Map getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map dataMap) {
        this.dataMap = dataMap;
    }

}
