package com.tms.collab.forum.ui;

import kacang.stdui.ComboSelectBox;
import kacang.stdui.SelectBox;
import kacang.stdui.Form;
import kacang.stdui.Button;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;
import kacang.services.security.User;
import kacang.util.Log;
import com.tms.portlet.PortletHandler;
import com.tms.portlet.Entity;
import com.tms.portlet.PortletPreference;
import com.tms.portlet.PortletException;
import com.tms.collab.forum.model.ForumModule;
import com.tms.collab.forum.model.Forum;

import java.util.StringTokenizer;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections.SequencedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Dec 9, 2003
 * Time: 1:32:52 PM
 * To change this template use Options | File Templates.
 */
public class ForumPortletPreference extends Form
{
    public static final String FORWARD_SUCCESSFUL = "successful";
    public static final String FORWARD_FAILED = "failed";
    public static final String FORWARD_CANCEL = "cancel";
    public static final String DEFAULT_TEMPLATE = "forum/forumPortletPreference";

    private Button update;
    private Button cancel;
    private ComboSelectBox forums;
    private SelectBox listing;
    private String entityId;
    private Entity entity;

    public ForumPortletPreference()
    {
    }

    public ForumPortletPreference(String name)
    {
        super(name);
    }

    public void init()
    {
        if(entity != null)
        {
            ForumModule handler = (ForumModule) Application.getInstance().getModule(ForumModule.class);
            User user = getWidgetManager().getUser();
            PortletPreference preferenceForums = entity.getPreference(ForumPortlet.PREFERENCE_FORUM);
            PortletPreference preferenceListing = entity.getPreference(ForumPortlet.PREFERENCE_LISTING);
            SequencedHashMap leftValues = new SequencedHashMap();
            SequencedHashMap rightValues = new SequencedHashMap();

            removeChildren();
            setMethod("post");
            //Initializing Listing
            listing = new SelectBox("listing");
            addChild(listing);
            listing.addOption("1", "1");
            listing.addOption("2", "2");
            listing.addOption("3", "3");
            listing.addOption("4", "4");
            listing.addOption("5", "5");
            listing.addOption("10", "10");
            if(preferenceListing == null)
                listing.setSelectedOptions(new String[] {ForumPortlet.DEFAULT_LISTING});
            else
                listing.setSelectedOptions(new String[] {preferenceListing.getPreferenceValue()});
            //Initializing Forum
            forums = new ComboSelectBox("forums");
            addChild(forums);
            forums.init();
            if(preferenceForums != null)
            {
                StringTokenizer tokenizer = new StringTokenizer((preferenceForums.getPreferenceValue()==null)? "":preferenceForums.getPreferenceValue(), ForumPortlet.DEFAULT_DELIMETER);
                while(tokenizer.hasMoreTokens())
                {
                    try
                    {
                        Forum forum = handler.getForum(tokenizer.nextToken(), user.getId());
                        String name = forum.getName();
                        if (name != null && name.length() > 30) {
                            name = name.substring(0, 30) + "..";
                        }
                        rightValues.put(forum.getForumId(), name);
                    }
                    catch(Exception e)
                    {
                        Log.getLog(ForumPortletPreference.class).error(e);
                    }
                }
            }
            try
            {
                Collection forums = handler.getForumsByUserGroupAccess(user.getId(), null, "1", "name", false, 0, -1, false);
                for(Iterator i = forums.iterator(); i.hasNext();)
                {
                    Forum forum = (Forum) i.next();
                    if(!(rightValues.containsKey(forum.getForumId()))) {
                        String name = forum.getName();
                        if (name != null && name.length() > 30) {
                            name = name.substring(0, 30) + "..";
                        }
                        leftValues.put(forum.getForumId(), name);
                    }
                }
            }
            catch(Exception e)
            {
                Log.getLog(ForumPortletPreference.class).error(e);
            }
            forums.setLeftValues(leftValues);
            forums.setRightValues(rightValues);

            update = new Button("update");
            update.setText(Application.getInstance().getMessage("forum.label.update","Update"));
            cancel = new Button("cancel");
            cancel.setText(Application.getInstance().getMessage("forum.label.cancel","Cancel"));

            addChild(update);
            addChild(cancel);
        }
    }

    public Forward onValidate(Event evt)
    {
        Forward forward = null;
        if(entity != null)
        {
            String forumId = "";
            for(Iterator i = forums.getRightValues().keySet().iterator(); i.hasNext();)
            {
                if(!("".equals(forumId)))
                    forumId += ",";
                forumId += (String) i.next();
            }
            PortletPreference preferenceForum = new PortletPreference();
            preferenceForum.setPreferenceName(ForumPortlet.PREFERENCE_FORUM);
            preferenceForum.setPreferenceValue(forumId);
            preferenceForum.setPreferenceEditable(true);
            entity.getPreferences().put(ForumPortlet.PREFERENCE_FORUM, preferenceForum);

            String list = (String) listing.getSelectedOptions().keySet().iterator().next();
            PortletPreference preferenceListing = new PortletPreference();
            preferenceListing.setPreferenceName(ForumPortlet.PREFERENCE_LISTING);
            preferenceListing.setPreferenceValue(list);
            preferenceListing.setPreferenceEditable(true);
            entity.getPreferences().put(ForumPortlet.PREFERENCE_LISTING, preferenceListing);

            PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
            try
            {
                handler.editEntity(entity);
                forward = new Forward(FORWARD_SUCCESSFUL);
            }
            catch (PortletException e)
            {
                Log.getLog(ForumPortletPreference.class).error(e);
                forward = new Forward(FORWARD_FAILED);
            }
        }
        return forward;
    }

    public Forward onSubmit(Event event)
    {
        if(cancel.getAbsoluteName().equals(findButtonClicked(event)))
            return new Forward(FORWARD_CANCEL);
        else
            return super.onSubmit(event);
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

    public SelectBox getListing()
    {
        return listing;
    }

    public void setListing(SelectBox listing)
    {
        this.listing = listing;
    }

    public String getEntityId()
    {
        return entityId;
    }

    public void setEntityId(String entityId)
    {
        this.entityId = entityId;
        PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
        try
        {
            setEntity(handler.getEntity(entityId));
            init();
        }
        catch(Exception e)
        {
            Log.getLog(ForumPortletPreference.class);
        }
    }

    public Entity getEntity()
    {
        return entity;
    }

    public void setEntity(Entity entity)
    {
        this.entity = entity;
    }

    public Button getUpdate()
    {
        return update;
    }

    public void setUpdate(Button update)
    {
        this.update = update;
    }

    public Button getCancel()
    {
        return cancel;
    }

    public void setCancel(Button cancel)
    {
        this.cancel = cancel;
    }
}
