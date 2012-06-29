package com.tms.collab.messaging.ui;

import kacang.ui.LightWeightWidget;
import kacang.ui.Event;
import kacang.services.security.User;
import kacang.util.Log;
import com.tms.collab.messaging.model.Util;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.MessagingException;

import java.util.Collection;

public class MessagingController extends LightWeightWidget {
    public void onRequest(Event event) {
        User user;
        MessagingModule mm;
        Collection folders;

        if(!Util.hasIntranetAccount(event)) {
            return;
        }

        user = Util.getUser(event);
        mm = Util.getMessagingModule();
        try {
            folders = mm.getFolders(user.getId());
            event.getRequest().setAttribute("folders", folders);

        } catch (MessagingException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }

    }

    public String getTemplate() {
        return "messaging/messagingController";
    }
}
