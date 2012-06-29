package com.tms.collab.forum.ui;

import com.tms.collab.forum.model.Forum;
import com.tms.collab.forum.model.ForumModule;
import kacang.Application;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;
import kacang.util.Log;

import java.util.ArrayList;
import java.util.Collection;

public class ThreadList  extends LightWeightWidget
{
    public static final String DEFAULT_TEMPLATE = "forum/threadList";
    public static final String NO_PERMISSION_TEMPLATE = "forum/noPermission";

    private Collection threads = new ArrayList();
    private Forum forum;
    private String message;
    private String id;

    public void onRequest(Event evt)
    {
        String forumId = (getId() != null) ? getId() : evt.getRequest().getParameter("forumId");
        ForumModule forumModule = (ForumModule) Application.getInstance().getModule(ForumModule.class);
        String userId = evt.getWidgetManager().getUser().getId();

        // check permission
        try
        {
            boolean hasPermission = forumModule.isForumUser(forumId, userId);
            if (!hasPermission)
            {
                forum = null;
                return;
            }

            // get forum and topics
            forum = forumModule.getForum(forumId, userId);
            threads = forumModule.getAllThread(forum.getId(), userId);
        }
        catch(Exception e)
        {
            Log.getLog(ThreadList.class).error("Error retrieving forum " + forumId + ": " + e.getMessage(), e);
        }

    }

    public Collection getThreads()
    {
        return threads;
    }

    public void setThreads(Collection threads)
    {
        this.threads = threads;
    }

    public Forum getForum()
    {
        return forum;
    }

    public void setForum(Forum forum)
    {
        this.forum = forum;
    }

   public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getDefaultTemplate()
    {
        if (forum != null)
            return DEFAULT_TEMPLATE;
        else
            return NO_PERMISSION_TEMPLATE;
    }
}
