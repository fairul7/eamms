package com.tms.collab.forum.ui;

import kacang.stdui.Form;
import kacang.stdui.ComboSelectBox;
import kacang.stdui.SelectBox;
import kacang.services.security.Profileable;
import kacang.services.security.User;
import kacang.ui.Widget;
import kacang.ui.Event;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorIn;
import kacang.model.operator.DaoOperator;
import kacang.util.Log;
import com.tms.collab.forum.model.ForumModule;
import com.tms.collab.forum.model.Forum;

import java.util.*;

public class ForumSubscription extends Form implements Profileable
{
    public static final String DEFAULT_TEMPLATE = "forum/forumSubscription";
    //public final String DEFAULT_LABEL = Application.getInstance().getMessage("forumSubscription.title","Forum Subscription");
    public static final String DEFAULT_NAME = "forumSubscription";

    private ComboSelectBox forums;
    private SelectBox forumSetting;
    private User user;

    public ForumSubscription()
    {
    }

    public ForumSubscription(String s)
    {
        super(s);
    }

    public void init()
    {
        forums = new ComboSelectBox("forums");
        forumSetting = new SelectBox("forumSetting");
        addChild(forums);
        addChild(forumSetting);
        forums.init();
        refresh();
    }

    private void refresh()
    {
        ForumModule module = (ForumModule) Application.getInstance().getModule(ForumModule.class);
        Map subscribed = new HashMap();
        Map unSubscribed = new HashMap();
        Collection exclude = new ArrayList();
        Collection list = null;
        try
        {
            Map settings = module.getSettings(user.getId());
            list = module.getSubscriptions(user.getId());
            for(Iterator i = list.iterator(); i.hasNext();)
            {
                Forum forum = (Forum) i.next();
                subscribed.put(forum.getForumId(), forum.getName());
                exclude.add(forum.getForumId());
            }
            String userId = getWidgetManager().getUser().getId();
            DaoQuery query = new DaoQuery();
            query.addProperty(new OperatorIn("ff.forumId", exclude.toArray(), DaoOperator.OPERATOR_NAN));
            list = module.getForumsByUserGroupAccess(userId, query, "1", "name", false, 0, -1, false);
            for(Iterator i = list.iterator(); i.hasNext();)
            {
                Forum forum = (Forum) i.next();
                unSubscribed.put(forum.getForumId(), forum.getName());
            }
            //Initializing widgets
            forumSetting.addOption(ForumModule.FORUM_TYPE_THREADED, Application.getInstance().getMessage("forum.label.threadedForum","Threaded Forum"));
            forumSetting.addOption(ForumModule.FORUM_TYPE_NON_THREADED, Application.getInstance().getMessage("forum.label.nonThreadedForum","Non Threaded Forum"));
            forumSetting.setSelectedOptions(new String[] {(String) settings.get(ForumModule.SETTINGS_LABEL_TYPE)});
            forums.setRightValues(subscribed);
            forums.setLeftValues(unSubscribed);
        }
        catch(Exception e)
        {
            Log.getLog(ForumSubscription.class).error(e);
        }
    }

    public void onRequest(Event event)
    {
        refresh();
    }

    public void process(User user)
    {
        ForumModule module = (ForumModule) Application.getInstance().getModule(ForumModule.class);
        try
        {
            module.deleteSubscriptionByUser(user.getId());
            for(Iterator i = forums.getRightValues().keySet().iterator(); i.hasNext();)
                module.addSubscription(user.getId(), (String) i.next());
            Map map = new HashMap();
            map.put(ForumModule.SETTINGS_LABEL_TYPE, forumSetting.getSelectedOptions().keySet().iterator().next());
            module.saveSettings(user.getId(), map);
        }
        catch(Exception e)
        {
            Log.getLog(ForumSubscription.class).error(e);
        }
    }

    public String getProfileableLabel()
    {
        return Application.getInstance().getMessage("forumSubscription.title","Forum Subscription");
    }

    public String getProfileableName()
    {
        return DEFAULT_NAME;
    }

    public Widget getWidget()
    {
        return this;
    }

    public void init(User user)
    {
        this.user = user;
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    public ComboSelectBox getForums()
    {
        return forums;
    }

    public void setForums(ComboSelectBox forums)
    {
        this.forums = forums;
    }

    public SelectBox getForumSetting()
    {
        return forumSetting;
    }

    public void setForumSetting(SelectBox forumSetting)
    {
        this.forumSetting = forumSetting;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }
}
