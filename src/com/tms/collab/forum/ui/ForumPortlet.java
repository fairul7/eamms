package com.tms.collab.forum.ui;

import kacang.ui.Widget;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.services.security.User;
import kacang.Application;
import kacang.util.Log;

import java.util.*;

import org.apache.commons.collections.SequencedHashMap;
import com.tms.portlet.Entity;
import com.tms.portlet.PortletPreference;
import com.tms.portlet.theme.ThemeManager;
import com.tms.collab.forum.model.ForumModule;
import com.tms.collab.forum.model.Forum;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Dec 9, 2003
 * Time: 10:41:35 AM
 * To change this template use Options | File Templates.
 */
public class ForumPortlet extends Widget
{
    public static final String EVENT_FORUM = "forum";
    public static final String EVENT_THREAD = "thread";

    public static final String FORUM_KEY = "forumId";
    public static final String THREAD_KEY = "threadId";
    public static final String MESSAGE_KEY = "messageId";

    public static final String FORWARD_FORUM = "forum";
    public static final String FORWARD_THREAD = "thread";

    public static final String DEFAULT_DELIMETER = ",";
    public static final String DEFAULT_TEMPLATE = "forum/forumPortlet";
    public static final String DEFAULT_LISTING = "5";

    public static final String PREFERENCE_FORUM = "forums";
    public static final String PREFERENCE_LISTING = "listing";

    private Entity entity;
    private Collection forums;
    private Map threads;

    public ForumPortlet()
    {
    }

    public ForumPortlet(String name)
    {
        super(name);
    }

    public void init()
    {
        forums = new ArrayList();
        threads = new SequencedHashMap();
        if(entity != null)
        {
            PortletPreference preferenceForum = entity.getPreference(PREFERENCE_FORUM);
            PortletPreference preferenceListing = entity.getPreference(PREFERENCE_LISTING);
            ForumModule handler = (ForumModule) Application.getInstance().getModule(ForumModule.class);
            User user = getWidgetManager().getUser();
            if(preferenceListing == null)
            {
                preferenceListing = new PortletPreference();
                preferenceListing.setPreferenceName(PREFERENCE_LISTING);
                preferenceListing.setPreferenceValue(DEFAULT_LISTING);
                preferenceListing.setPreferenceEditable(true);
            }
            if(!(preferenceForum == null || preferenceForum.getPreferenceValue() == null))
            {
                StringTokenizer tokenizer = new StringTokenizer(preferenceForum.getPreferenceValue(), DEFAULT_DELIMETER);
                while(tokenizer.hasMoreTokens())
                {
                    try
                    {
                        Forum forum = handler.getForum(tokenizer.nextToken(), user.getId());
                        Collection thread = handler.getLatestMessages(forum, Integer.parseInt(preferenceListing.getPreferenceValue()), user.getId());
                        forums.add(forum);
                        threads.put(forum.getForumId(), thread);
                    }
                    catch(Exception e)
                    {
                        Log.getLog(ForumPortlet.class).error(e);
                    }
                }
            }
        }
    }

    public void onRequest(Event evt)
    {
        Object entity = evt.getRequest().getAttribute(ThemeManager.LABEL_ENTITY);
        if(entity instanceof Entity)
        {
            setEntity((Entity) entity);
            init();
        }
    }

    public Forward actionPerformed(Event evt)
    {
        Forward forward = null;
        if(EVENT_FORUM.equals(evt.getType()))
            forward = new Forward(FORWARD_FORUM);
        else if(EVENT_THREAD.equals(evt.getType()))
            forward = new Forward(FORWARD_THREAD);
        return forward;
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    public Collection getForums()
    {
        return forums;
    }

    public void setForums(Collection forums)
    {
        this.forums = forums;
    }

    public Map getThreads()
    {
        return threads;
    }

    public void setThreads(Map threads)
    {
        this.threads = threads;
    }

    public Entity getEntity()
    {
        return entity;
    }

    public void setEntity(Entity entity)
    {
        this.entity = entity;
    }
}
