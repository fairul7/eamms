package com.tms.collab.messaging.ui;

import com.tms.collab.messaging.model.MessagingException;
import com.tms.collab.messaging.model.MessagingQueue;
import com.tms.collab.messaging.model.MessagingUserStatus;
import kacang.ui.Event;
import kacang.ui.Widget;
import kacang.util.Log;

public class MessagingStatusWidget extends Widget {
    private MessagingUserStatus mus;

    public void init() {
        String userId;

        userId = getWidgetManager().getUser().getId();
        try {
            mus = MessagingQueue.getInstance().getUserStatus(userId);
        } catch (MessagingException e) {
            Log.getLog(MessagingStatusWidget.class).error(e.getMessage(), e);
        }
    }

    public void onRequest(Event evt) {
        init(); // just in case, get again

        String clear = evt.getParameter("clear");

        if("download".equals(clear)) {
            if(!mus.isDownloadBusy()) {
                mus.getTrackerDownload().reset();
            }
        } else if("check".equals(clear)) {
            if(!mus.isCheckBusy()) {
                mus.getTrackerCheck().reset();
            }
        } else if("serverView".equals(clear)) {
            if(!mus.isServerViewBusy()) {
                mus.getTrackerServerView().reset();
            }
        } else if("send".equals(clear)) {
            if(!mus.isSendBusy()) {
                mus.getTrackerSend().reset();
            }
        }
    }

    public String getDefaultTemplate() {
        // TODO: need to do la...
        return null;
    }


    // === [ getters/setters ] =================================================
    public MessagingUserStatus getMus() {
        return mus;
    }

    public void setMus(MessagingUserStatus mus) {
        this.mus = mus;
    }
}
