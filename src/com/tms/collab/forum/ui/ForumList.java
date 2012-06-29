package com.tms.collab.forum.ui;

import com.tms.collab.forum.model.ForumModule;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.DaoOperator;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;
import kacang.util.Log;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: WaiHung
 * Date: May 22, 2003
 * Time: 12:54:02 PM
 * To change this template use Options | File Templates.
 */
public class ForumList  extends LightWeightWidget
{
    public static final String DEFAULT_TEMPLATE = "forum/forumList";
    public static final String LABEL_CATEGORY = "category";

    private Collection forums = new ArrayList();
    private String message;
    private String showCount;

    public void onRequest(Event evt)
    {
        try
        {
            ForumModule forumModule = (ForumModule) Application.getInstance().getModule(ForumModule.class);
            String category = evt.getRequest().getParameter(LABEL_CATEGORY);
            boolean includeCount = (showCount == null) ? true : Boolean.valueOf(showCount).booleanValue();

            if(category == null || "".equals(category)) {
                forums = forumModule.getForumsByUserGroupAccess(evt.getWidgetManager().getUser().getId(), null, "1", "name", false, 0, -1, includeCount);
            }
            else {
                DaoQuery query = new DaoQuery();
                query.addProperty(new OperatorEquals("category", category, DaoOperator.OPERATOR_AND));
                forums = forumModule.getForumsByUserGroupAccess(evt.getWidgetManager().getUser().getId(), query, "1", "name", false, 0, -1, includeCount);
            }
            forumModule.setLastPost(forums);
        }
        catch(Exception e)
        {
            Log.getLog(ForumList.class).error(e.getMessage(), e);
        }
    }

    public Collection getForums()
    {
        return forums;
    }

    public void setForums(Collection forums)
    {
        this.forums = forums;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getShowCount()
    {
        return showCount;
    }

    public void setShowCount(String showCount)
    {
        this.showCount = showCount;
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }
}
