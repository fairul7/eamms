package com.tms.collab.messaging.ui;

import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.MessagingException;
import com.tms.collab.messaging.model.MessagingQueue;
import kacang.Application;
import kacang.util.Log;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;

import java.util.List;

public class QueueStatus extends LightWeightWidget {
    private String[] jobNames;

    public void onRequest(Event event) {
        MessagingModule mm;

        mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);

        try {
            if ("stop".equals(event.getParameter("action"))) {
                mm.stopQueueProcessing();
            } else if ("start".equals(event.getParameter("action"))) {
                mm.startQueueProcessing();
            }
            jobNames = mm.getQueueProcessingStatus();

        } catch (MessagingException e) {
            Log.getLog(getClass()).error("Error processing queue status", e);
        }
    }

    public String[] getJobNames() {
        return jobNames;
    }

    public void setJobNames(String[] jobNames) {
        this.jobNames = jobNames;
    }

    public String getDefaultTemplate() {
        return "messaging/queueStatus";
    }


}
