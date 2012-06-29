package com.tms.collab.messaging.ui;

import com.tms.collab.messaging.model.Folder;
import com.tms.collab.messaging.model.MessagingException;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.Util;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.Widget;
import kacang.util.Log;

import java.util.Collection;

public class InboxPortlet extends Widget {
    public static final String DEFAULT_MAX_MESSAGES = "5";

    private int maxMessages;

    private Collection messages;

    public void init() {
        super.init();
    }

    public void onRequest(Event event) {
        MessagingModule mm;
        Folder folder;
        User user;
        getWidgetManager().setAttribute("InboxPortlet","InboxPortlet");
        // make sure user has activated messaging module
        mm = Util.getMessagingModule();
        user = Util.getUser(event);
        try {
            if(mm.getIntranetAccountByUserId(user.getId())==null) {
                event.getRequest().setAttribute("notActivated", Boolean.TRUE);
                return;
            }
        } catch (MessagingException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }

        // TODO: set maxMessages from preferences
        maxMessages = Integer.parseInt(DEFAULT_MAX_MESSAGES);

        // process inbox
        try {
            folder = mm.getSpecialFolder(user.getId(), Folder.FOLDER_INBOX);
            DaoQuery query = new DaoQuery();
            query.addProperty(new OperatorEquals("folderId", folder.getFolderId(), DaoOperator.OPERATOR_AND));
            messages = mm.getMessages(query, 0, maxMessages, "messageDate", true);

        } catch (MessagingException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
    }

    public String getTemplate() {
        return "messaging/inboxPortlet";
    }

    // === [ getters/setters ] =================================================
    public int getMaxMessages() {
        return maxMessages;
    }

    public void setMaxMessages(int maxMessages) {
        this.maxMessages = maxMessages;
    }

    public Collection getMessages() {
        return messages;
    }

    public void setMessages(Collection messages) {
        this.messages = messages;
    }

}
