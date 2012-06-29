package com.tms.portlet.portlets.bookmark;

import com.tms.portlet.Entity;
import com.tms.portlet.PortletException;
import com.tms.portlet.PortletHandler;
import com.tms.portlet.PortletPreference;
import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Jan 29, 2004
 * Time: 2:08:20 PM
 * To change this template use Options | File Templates.
 */
public class BookmarkPreference extends Form
{
    public static final String FORWARD_SUCCESSFUL = "forward_successful";
    public static final String FORWARD_FAILED = "forward_failed";
    public static final String FORWARD_CANCEL = "forward_cancel";
    public static final String EVENT_TYPE_DELETE = "delete";
    public static final String DELETE_KEY = "link";
    public static final String DEFAULT_TEMPLATE = "portal/portlets/bookmarkPreference";

    private Entity entity;
    private String entityId;
    private Map links;

    private TextField link;
    private TextField linkLabel;
    private Button submit;
    private Button cancel;
    private ValidatorNotEmpty validLink;
    private ValidatorNotEmpty validLabel;

    public BookmarkPreference()
    {
    }

    public BookmarkPreference(String s)
    {
        super(s);
    }

    public void init()
    {
        setMethod("POST");
        link = new TextField("link");
        linkLabel = new TextField("linkLabel");
        submit = new Button("submit");
        submit.setText(Application.getInstance().getMessage("portlet.label.addLink","Add Link"));
        cancel = new Button("cancel");
        cancel.setText(Application.getInstance().getMessage("portlet.label.cancel","Cancel"));

        validLink = new ValidatorNotEmpty("validLink");
        link.addChild(validLink);
        validLabel = new ValidatorNotEmpty("validLabel");
        linkLabel.addChild(validLabel);

        addChild(link);
        addChild(linkLabel);
        addChild(submit);
        addChild(cancel);

        refresh();
    }

    private void refresh()
    {
        if(entity != null)
        {
            PortletPreference preferenceLinks = entity.getPreference(Bookmark.PREFERENCE_LINKS);
            String preferenceValue = "";
            if(preferenceLinks != null)
                preferenceValue = preferenceLinks.getPreferenceValue();
            links = Bookmark.getLinks(preferenceValue);
        }
    }

    public void onRequest(Event event)
    {
        refresh();
    }

    public Forward onValidate(Event event)
    {
        Forward forward = null;
        if(entity != null)
        {
            String linkValue = (String)link.getValue();
            if (linkValue.startsWith("http://")) {
                linkValue = linkValue.substring("http://".length());
            }
            links.put(linkValue, linkLabel.getValue());
            link.setValue("");
            linkLabel.setValue("");
            forward = savePreference(links);
        }
        return forward;
    }

    public Forward actionPerformed(Event event)
    {
        Forward forward = null;
        if(entity != null)
        {
            if(EVENT_TYPE_DELETE.equals(event.getType()))
            {
                if(event.getParameter(DELETE_KEY) != null)
                {
                    String key  =event.getParameter(DELETE_KEY);
                    key = key.replaceAll("~","&");
                    key = key.replaceAll(" ","+");
                    links.remove(key);
                    savePreference(links);
                }
            }
            else
            {
                if(cancel.getAbsoluteName().equals(findButtonClicked(event)))
                    forward = new Forward(FORWARD_CANCEL);
                else
                    forward = super.actionPerformed(event);
            }
        }
        return forward;
    }

    private Forward savePreference(Map stations)
    {
        Forward forward = null;
        PortletPreference preferenceLinks = null;
        if(entity.getPreference(Bookmark.PREFERENCE_LINKS) == null)
        {
            preferenceLinks = new PortletPreference();
            preferenceLinks.setPreferenceName(Bookmark.PREFERENCE_LINKS);
            preferenceLinks.setPreferenceEditable(true);
            preferenceLinks.setPreferenceValue("");
        }
        else
            preferenceLinks = entity.getPreference(Bookmark.PREFERENCE_LINKS);
        preferenceLinks.setPreferenceValue(constructPreferenceString(stations));
        entity.getPreferences().put(Bookmark.PREFERENCE_LINKS, preferenceLinks);
        PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
        try
        {
            handler.editEntity(entity);
            forward = new Forward(FORWARD_SUCCESSFUL);
        }
        catch (PortletException e)
        {
            Log.getLog(BookmarkPreference.class).error(e);
            forward = new Forward(FORWARD_FAILED);
        }
        return forward;
    }

    private String constructPreferenceString(Map links)
    {
        String output = "";
        for(Iterator i = links.keySet().iterator(); i.hasNext();)
        {
            String key = (String) i.next();
            output += key + Bookmark.BOOKMARK_DELIMITER + links.get(key) + ";";
        }
        return output;
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    //Getters and Setters
    public Button getCancel()
    {
        return cancel;
    }

    public void setCancel(Button cancel)
    {
        this.cancel = cancel;
    }

    public Entity getEntity()
    {
        return entity;
    }

    public void setEntity(Entity entity)
    {
        this.entity = entity;
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
            entity = handler.getEntity(entityId);
        }
        catch(PortletException e)
        {
            Log.getLog(BookmarkPreference.class).error(e.getMessage(), e);
        }
    }

    public TextField getLink()
    {
        return link;
    }

    public void setLink(TextField link)
    {
        this.link = link;
    }

    public TextField getLinkLabel()
    {
        return linkLabel;
    }

    public void setLinkLabel(TextField linkLabel)
    {
        this.linkLabel = linkLabel;
    }

    public Map getLinks()
    {
        return links;
    }

    public void setLinks(Map links)
    {
        this.links = links;
    }

    public Button getSubmit()
    {
        return submit;
    }

    public void setSubmit(Button submit)
    {
        this.submit = submit;
    }

    public ValidatorNotEmpty getValidLabel()
    {
        return validLabel;
    }

    public void setValidLabel(ValidatorNotEmpty validLabel)
    {
        this.validLabel = validLabel;
    }

    public ValidatorNotEmpty getValidLink()
    {
        return validLink;
    }

    public void setValidLink(ValidatorNotEmpty validLink)
    {
        this.validLink = validLink;
    }
}
