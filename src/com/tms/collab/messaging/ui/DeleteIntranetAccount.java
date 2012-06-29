package com.tms.collab.messaging.ui;

import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.Util;
import com.tms.collab.messaging.model.MessagingException;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;
import kacang.util.Log;

public class DeleteIntranetAccount extends LightWeightWidget {
    private String action;

    public void onRequest(Event event) {
        String confirm = event.getRequest().getParameter("confirm");

        if("DELETE ALL MY MESSAGING DATA".equals(confirm)) {
            // delete account
            MessagingModule mm;
            User user;

            user = Util.getUser(event);
            mm = Util.getMessagingModule();
            try {
                mm.deleteIntranetAccount(user.getId());
                action = "deleted";

            } catch (MessagingException e) {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
        } else {
            action = "showForm";
        }
    }

    public String getTemplate() {
        return "messaging/deleteIntranetAccount";
    }


    // === [ getters/setters ] =================================================
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
