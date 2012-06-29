package com.tms.collab.messaging.ui;

import kacang.ui.Widget;
import kacang.ui.Event;
import kacang.util.Log;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.Util;
import com.tms.collab.messaging.model.MessagingException;

import java.util.Collection;
import java.util.HashSet;

public class SpecialFoldersInfo extends Widget {
    private Collection specialFolders;

    public void onRequest(Event evt) {
        MessagingModule mm;

        mm = Util.getMessagingModule();
        try {
            specialFolders = mm.getFolders(getWidgetManager().getUser().getId(), 0, -1, "name", false, "specialFolder", "1");
        } catch (MessagingException e) {
            Log.getLog(getClass()).error("Error getting special folders", e);
            specialFolders = new HashSet();
        }
    }

    public String getDefaultTemplate() {
        return "messaging/specialFoldersInfo";
    }

    public Collection getSpecialFolders() {
        return specialFolders;
    }

    public void setSpecialFolders(Collection specialFolders) {
        this.specialFolders = specialFolders;
    }
}
