package com.tms.collab.qmessaging.ui;

import kacang.stdui.Form;
import kacang.stdui.SelectBox;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.services.presence.PresenceService;
import kacang.services.presence.PresenceException;
import kacang.services.security.User;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.DaoOperator;
import kacang.util.Log;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.SequencedHashMap;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.Folder;
import com.tms.collab.messaging.model.MessagingException;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Jan 28, 2004
 * Time: 10:36:12 AM
 * To change this template use Options | File Templates.
 */
public class QuickMessenger extends Form
{
    public static final String DEFAULT_TEMPLATE = "qmessaging/qMessenger";
    public static final String DEFAULT_SELECT_VALUE = "-1";
    public static final String EVENT_MESSAGE_SELECT = "messageSelect";
    public static final String FORWARD_MESSAGE_SELECTED = "messageSelected";
    public static final String FORWARD_USER_SELECTED = "userSelected";
    public static final String ATTRIBUTE_MESSAGE_KEY = "messageId";
    public static final String ATTRIBUTE_USER_KEY = "userId";

    private SelectBox onlineUsers;
    private SelectBox offlineUsers;
    private Collection messages;
    private String selectedUser;
    private String selectedMessage;

    private String sendUrl;
    private String replyUrl;

    public QuickMessenger()
    {
    }

    public QuickMessenger(String s)
    {
        super(s);
    }

    public void init()
    {
        onlineUsers = new SelectBox("onlineUsers");
        offlineUsers = new SelectBox("offlineUsers");

        addChild(onlineUsers);
        addChild(offlineUsers);

        sendUrl = "";
        replyUrl = "";

        refresh();
    }

    protected void refresh()
    {
        try
        {
            //Populating Online and Offline Users
            PresenceService service = (PresenceService) Application.getInstance().getService(PresenceService.class);
            Collection online = service.getOnlineUsers();
            Map onlineMap = new SequencedHashMap();
            onlineMap.put(DEFAULT_SELECT_VALUE, Application.getInstance().getMessage("qmessaging.label.onlineUsers","Online Users"));
            for(Iterator i = online.iterator(); i.hasNext();)
            {
                User user = (User) i.next();
                onlineMap.put(user.getId(), user.getProperty("firstName") + " " + user.getProperty("lastName"));
            }
            Collection offline = service.getOfflineUsers();
            Map offlineMap = new SequencedHashMap();
            offlineMap.put(DEFAULT_SELECT_VALUE, Application.getInstance().getMessage("qmessaging.label.offlineUsers","Offline Users"));
            for(Iterator i = offline.iterator(); i.hasNext();)
            {
                User user = (User) i.next();
                offlineMap.put(user.getId(), user.getProperty("firstName") + " " + user.getProperty("lastName"));
            }
            onlineUsers.setOptionMap(onlineMap);
            offlineUsers.setOptionMap(offlineMap);

            //Retrieving Incoming Messages
            messages = null;
            try
            {
                MessagingModule module = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
                User user = getWidgetManager().getUser();
                Folder folder = module.getSpecialFolder(user.getId(), Folder.FOLDER_QM);
                DaoQuery query = new DaoQuery();
                query.addProperty(new OperatorEquals("folderId", folder.getFolderId(), DaoOperator.OPERATOR_AND));
                query.addProperty(new OperatorEquals("readFlag", Boolean.FALSE, DaoOperator.OPERATOR_AND));
                messages = module.getMessages(query, 0, 5, "messageDate", true);
            }
            catch (MessagingException e)
            {
                Log.getLog(QuickMessenger.class).debug(e.getMessage(), e);
            }

            selectedUser = "";
            selectedMessage = "";

            if("".equals(sendUrl))
            {
                onlineUsers.setOnChange("submit();");
                offlineUsers.setOnChange("submit();");
            }
            else
            {
                setSendOnChange();
            }
        }
        catch(PresenceException e)
        {
            Log.getLog(QuickMessenger.class).error(e.getMessage(), e);
        }
    }

    protected void setSendOnChange()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("qMessengerWindowOpen('");
        buffer.append(sendUrl);
        buffer.append("?");
        buffer.append(ATTRIBUTE_USER_KEY);
        buffer.append("=' + this.options[this.selectedIndex].value");
        buffer.append(", 'sendWindow'); ");
        buffer.append("this.selectedIndex = 0;");

        onlineUsers.setOnChange(buffer.toString());
        offlineUsers.setOnChange(buffer.toString());
    }

    public Forward onValidate(Event event)
    {
        Forward forward = null;
        if(EVENT_MESSAGE_SELECT.equals(event.getType()))
        {
            selectedMessage = event.getParameter(ATTRIBUTE_MESSAGE_KEY);
            forward = new Forward(FORWARD_MESSAGE_SELECTED);
        }
        else
        {
            String selectedOnline = "";
            String selectedOffline = "";
            if(!onlineUsers.getSelectedOptions().isEmpty())
                selectedOnline = (String) onlineUsers.getSelectedOptions().keySet().iterator().next();
            if(!offlineUsers.getSelectedOptions().isEmpty())
                selectedOffline = (String) offlineUsers.getSelectedOptions().keySet().iterator().next();
            if(!("".equals(selectedOnline) || DEFAULT_SELECT_VALUE.equals(selectedOnline)))
            {
                onlineUsers.setSelectedOptions(new String[] {DEFAULT_SELECT_VALUE});
                selectedUser = selectedOnline;
                forward = new Forward(FORWARD_USER_SELECTED);
            }
            else if(!("".equals(selectedOffline) || DEFAULT_SELECT_VALUE.equals(selectedOffline)))
            {
                offlineUsers.setSelectedOptions(new String[] {DEFAULT_SELECT_VALUE});
                selectedUser = selectedOffline;
                forward = new Forward(FORWARD_USER_SELECTED);
            }
        }
        return forward;
    }

    public void onRequest(Event event)
    {
        refresh();
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    //Getters and Setters
    public SelectBox getOfflineUsers()
    {
        return offlineUsers;
    }

    public void setOfflineUsers(SelectBox offlineUsers)
    {
        this.offlineUsers = offlineUsers;
    }

    public SelectBox getOnlineUsers()
    {
        return onlineUsers;
    }

    public void setOnlineUsers(SelectBox onlineUsers)
    {
        this.onlineUsers = onlineUsers;
    }

    public Collection getMessages()
    {
        return messages;
    }

    public void setMessages(Collection messages)
    {
        this.messages = messages;
    }

    public String getSelectedMessage()
    {
        return selectedMessage;
    }

    public void setSelectedMessage(String selectedMessage)
    {
        this.selectedMessage = selectedMessage;
    }

    public String getSelectedUser()
    {
        return selectedUser;
    }

    public void setSelectedUser(String selectedUser)
    {
        this.selectedUser = selectedUser;
    }

    public String getReplyUrl()
    {
        return replyUrl;
    }

    public void setReplyUrl(String replyUrl)
    {
        this.replyUrl = replyUrl;
    }

    public String getSendUrl()
    {
        return sendUrl;
    }

    public void setSendUrl(String sendUrl)
    {
        this.sendUrl = sendUrl;
        setSendOnChange();
    }
}
