package com.tms.collab.messaging.ui;

import kacang.ui.LightWeightWidget;
import kacang.ui.Event;
import kacang.services.security.User;
import kacang.util.Log;
import com.tms.collab.messaging.model.Util;
import com.tms.collab.messaging.model.MessagingException;

public class QuotaCheck extends LightWeightWidget {
    private static final String ATTRIBUTE_QUOTA_EXCEEDED = "exceedQuota";

    public void onRequest(Event event) {

        User user = Util.getUser(event);

        try {
            if (Util.getMessagingModule().isQuotaExceeded(user.getId())) {
                event.getRequest().setAttribute(ATTRIBUTE_QUOTA_EXCEEDED, Boolean.TRUE);
            }

        } catch (MessagingException e) {
            event.getRequest().setAttribute(ATTRIBUTE_QUOTA_EXCEEDED, Boolean.TRUE);
            Log.getLog(getClass()).error("Error getting quota information", e);
        }

    }

    public String getDefaultTemplate() {
        return "messaging/quotaCheck";
    }
}
