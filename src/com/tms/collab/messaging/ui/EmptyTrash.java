package com.tms.collab.messaging.ui;

import kacang.ui.LightWeightWidget;
import kacang.ui.Event;
import kacang.util.Log;
import kacang.services.security.User;
import com.tms.collab.messaging.model.*;

import java.util.Collection;
import java.util.Iterator;

public class EmptyTrash extends LightWeightWidget {
    private boolean emptyTrashSuccessful;

    public void onRequest(Event event) {
        User user;
        MessagingModule mm;
        Folder trashFolder;
        Collection messages;

        try {
            setEmptyTrashSuccessful(false);
            user = Util.getUser(event);
            mm = Util.getMessagingModule();
            trashFolder = mm.getSpecialFolder(user.getId(), Folder.FOLDER_TRASH);
            messages = mm.getMessages(trashFolder.getFolderId());

            // delete every message in the trash folder
            for (Iterator iterator = messages.iterator(); iterator.hasNext();) {
                Message message = (Message) iterator.next();
                mm.deleteMessage(message.getMessageId());
            }
            setEmptyTrashSuccessful(true);
            mm.updateUserDiskQuota(user.getId());

        } catch (MessagingException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
    }

    public String getTemplate() {
        return "messaging/emptyTrash";
    }


    // === [ getters/setters ] =================================================
    public boolean isEmptyTrashSuccessful() {
        return emptyTrashSuccessful;
    }

    public void setEmptyTrashSuccessful(boolean emptyTrashSuccessful) {
        this.emptyTrashSuccessful = emptyTrashSuccessful;
    }

}
