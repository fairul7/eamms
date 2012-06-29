package com.tms.collab.forum.ui;

import com.tms.collab.forum.model.ForumModule;
import com.tms.collab.forum.model.Thread;
import com.tms.collab.forum.model.Message;
import com.tms.collab.forum.model.ForumException;
import kacang.Application;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.util.Log;

public class NewMessage extends PostMessage
{
    public static final String DEFAULT_TEMPLATE = "forum/newMessage";
    public static final String NO_PERMISSION_TEMPLATE = "forum/noPermission";

    private String forumId;
    private String forumName;
    private Thread thread;
    private Message parentMessage;

    public void onRequest(Event evt)
    {
        try
        {
            ForumModule forumModule = (ForumModule) Application.getInstance().getModule(ForumModule.class);
            User user = evt.getWidgetManager().getUser();
            String parentId = evt.getRequest().getParameter("parentMessageId");
            setThread(forumModule.getThread(evt.getRequest().getParameter("threadId"), user.getId()));

            setForumId(getThread().getForumId());
            setForumName(forumModule.getForumName(getThread().getForumId()));

            // check permission
            String userId = evt.getWidgetManager().getUser().getId();
            boolean hasPermission = forumModule.isForumUser(forumId, userId) || forumModule.isForumModerator(forumId, userId);
            if (!hasPermission)
            {
                thread = null;
                return;
            }

            if(!(parentId == null || "".equals(parentId)))
            {
                try
                {
                    parentMessage = forumModule.getPostedMessage(parentId, user.getId());
                }
                catch(ForumException e)
                {
                }
            }
        }
        catch(Exception e)
        {
            Log.getLog(NewMessage.class).error(e.getMessage(), e);
        }

        Object flag = evt.getRequest().getAttribute(ATTRIBUTE_FLAG);
        if(flag!=null && flag instanceof PostMessage)
        {
            //do nothing
        }
        else
            super.onRequest(evt);
    }

    public String getDefaultTemplate()
    {
        if (thread != null)
            return DEFAULT_TEMPLATE;
        else
            return NO_PERMISSION_TEMPLATE;
    }

    public String getForumId()
    {
        return forumId;
    }

    public void setForumId(String forumId)
    {
        this.forumId = forumId;
    }

    public String getForumName()
    {
        return forumName;
    }

    public void setForumName(String forumName)
    {
        this.forumName = forumName;
    }

    public Thread getThread()
    {
        return thread;
    }

    public void setThread(Thread thread)
    {
        this.thread = thread;
    }

    public Message getParentMessage()
    {
        return parentMessage;
    }

    public void setParentMessage(Message parentMessage)
    {
        this.parentMessage = parentMessage;
    }
}
