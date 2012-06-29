package com.tms.collab.messaging.model.queue;

import java.io.Serializable;

public class QueueItem implements Serializable {
    private String userId;

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QueueItem)) return false;

        final QueueItem queueItem = (QueueItem) o;

        if (userId != null ? !userId.equals(queueItem.userId) : queueItem.userId != null) return false;

        return true;
    }

    // === [ getters/setters ] =================================================
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
