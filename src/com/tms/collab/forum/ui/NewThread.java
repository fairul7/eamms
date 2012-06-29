package com.tms.collab.forum.ui;

import com.tms.collab.forum.model.ForumModule;
import kacang.Application;
import kacang.ui.Event;
import kacang.util.Log;

public class NewThread  extends PostThread
{
    public static final String DEFAULT_TEMPLATE = "forum/newThread";
    public static final String NO_PERMISSION_TEMPLATE = "forum/noPermission";

    private String forumId;
    private String forumName;
    private String forumDesc;

    public String getDefaultTemplate()
    {
        if (forumId != null)
            return DEFAULT_TEMPLATE;
        else
            return NO_PERMISSION_TEMPLATE;
    }

    public void onRequest(Event evt)
    {
        try
        {
            ForumModule forumModule = (ForumModule) Application.getInstance().getModule(ForumModule.class);
            forumId = evt.getRequest().getParameter("forumId");


            // check permission
            String userId = evt.getWidgetManager().getUser().getId();
            boolean hasPermission = forumModule.isForumUser(forumId, userId) || forumModule.isForumModerator(forumId, userId);
            if (!hasPermission)
            {
                forumId = null;
                return;
            }

            setForumId(forumId);
            setForumName(forumModule.getForumName(getForumId()));
            setForumDesc(forumModule.getForumDesc(getForumId()));
        }
        catch(Exception e)
        {
            Log.getLog(NewThread.class).error(e.getMessage(), e);
        }
        Object flag = evt.getRequest().getAttribute(ATTRIBUTE_FLAG);
        if(flag!=null && flag instanceof PostThread)
        {
            //do nothing
        }
        else
            super.onRequest(evt);
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

    public String getForumDesc()
    {
        return forumDesc;
    }

    public void setForumDesc(String forumDesc)
    {
        this.forumDesc = forumDesc;
    }
}
