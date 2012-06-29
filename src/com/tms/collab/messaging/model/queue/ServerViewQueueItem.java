package com.tms.collab.messaging.model.queue;

import java.util.List;

public class ServerViewQueueItem extends QueueItem {
    private boolean preview;
    private List accountIdList;


    // === [ getters/setters ] =================================================
    public boolean isPreview() {
        return preview;
    }

    public void setPreview(boolean preview) {
        this.preview = preview;
    }

    public List getAccountIdList() {
        return accountIdList;
    }

    public void setAccountIdList(List accountIdList) {
        this.accountIdList = accountIdList;
    }
}
