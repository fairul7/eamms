package com.tms.cms.portlet;

import kacang.stdui.*;
import kacang.services.security.User;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;
import kacang.Application;
import com.tms.portlet.Entity;
import com.tms.portlet.PortletPreference;
import com.tms.portlet.PortletHandler;
import com.tms.portlet.PortletException;
import com.tms.cms.core.model.ContentPublisher;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.section.Section;
import com.tms.cms.vsection.VSection;
import com.tms.cms.portlet.ContentPortlet;
import org.apache.commons.collections.SequencedHashMap;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Dec 15, 2003
 * Time: 3:21:15 PM
 * To change this template use Options | File Templates.
 */
public class ContentPortletPreference extends Form
{
    public static final String FORWARD_SUCCESSFUL = "successful";
    public static final String FORWARD_FAILED = "failed";
    public static final String FORWARD_CANCEL = "cancel";
    public static final String DEFAULT_TEMPLATE = "cms/portlet/contentPortletPreference";

    private Button update;
    private Button cancel;
    private ComboSelectBox sections;
    private SelectBox listing;
    private CheckBox articles;
    private CheckBox documents;
    private String entityId;
    private Entity entity;

    public ContentPortletPreference()
    {
    }

    public ContentPortletPreference(String name)
    {
        super(name);
    }

    public void init()
    {
        removeChildren();
        setMethod("post");

        listing = new SelectBox("listing");
        sections = new ComboSelectBox("sections");
        update = new Button("update");
        update.setText("Update");
        cancel = new Button("cancel");
        cancel.setText("Cancel");
        articles = new CheckBox("articles");
        articles.setText("View Articles");
        documents = new CheckBox("documents");
        documents.setText("View Documents");

        addChild(listing);
        addChild(sections);
        addChild(articles);
        addChild(documents);
        addChild(update);
        addChild(cancel);

        sections.init();
        refresh();
    }

    public void refresh()
    {
        if(entity != null)
        {
            ContentPublisher publisher = (ContentPublisher) Application.getInstance().getModule(ContentPublisher.class);
            User user = getWidgetManager().getUser();

            PortletPreference preferenceSections = entity.getPreference(ContentPortlet.PREFERENCE_SECTIONS);
            PortletPreference preferenceListing = entity.getPreference(ContentPortlet.PREFERENCE_LISTING);
            PortletPreference preferenceArticles = entity.getPreference(ContentPortlet.PREFERENCE_ARTICLE);
            PortletPreference preferenceDocuments = entity.getPreference(ContentPortlet.PREFERENCE_DOCUMENT);

            SequencedHashMap leftValues = new SequencedHashMap();
            SequencedHashMap rightValues = new SequencedHashMap();
            //Initializing Listing
            listing.addOption("1", "1");
            listing.addOption("2", "2");
            listing.addOption("3", "3");
            listing.addOption("4", "4");
            listing.addOption("5", "5");
            listing.addOption("10", "10");
            if(preferenceListing == null)
                listing.setSelectedOptions(new String[] {ContentPortlet.DEFAULT_LISTING});
            else
                listing.setSelectedOptions(new String[] {preferenceListing.getPreferenceValue()});
            //Initializing Sections
            if(preferenceSections == null)
            {
                preferenceSections = new PortletPreference();
                preferenceSections.setPreferenceName(ContentPortlet.PREFERENCE_SECTIONS);
                preferenceSections.setPreferenceEditable(true);
                preferenceSections.setPreferenceValue("");
            }
            if(!("".equals(preferenceSections.getPreferenceValue())))
            {
                StringTokenizer tokenizer = new StringTokenizer(preferenceSections.getPreferenceValue(), ContentPortlet.DEFAULT_DELIMETER);
                Collection sectionId = new ArrayList();
                while(tokenizer.hasMoreTokens())
                    sectionId.add(tokenizer.nextToken());
                try
                {
                    Collection sections = publisher.viewList((String[]) sectionId.toArray(new String[] {}), new String[] {Section.class.getName(), VSection.class.getName()}, null, null, Boolean.FALSE, "name", false, 0, -1, ContentManager.USE_CASE_VIEW, user.getId());
                    for(Iterator i = sections.iterator(); i.hasNext();)
                    {
                        ContentObject section = (ContentObject) i.next();
                        String name = section.getName();
                        if (name != null && name.length() > 30) {
                            name = name.substring(0, 30) + "..";
                        }
                        rightValues.put(section.getId(), name);
                    }
                }
                catch(Exception e)
                {
                    Log.getLog(ContentPortletPreference.class).error(e);
                }
            }
            try
            {
                Collection sections = publisher.viewList(null, new String[] {Section.class.getName(), VSection.class.getName()}, null, null, Boolean.FALSE, "name", false, 0, -1, ContentManager.USE_CASE_VIEW, user.getId());
                for(Iterator i = sections.iterator(); i.hasNext();)
                {
                    ContentObject section = (ContentObject) i.next();
                    if(!rightValues.containsKey(section.getId())) {
                        String name = section.getName();
                        if (name != null && name.length() > 30) {
                            name = name.substring(0, 30) + "..";
                        }
                        leftValues.put(section.getId(), name);
                    }
                }
            }
            catch(Exception e)
            {
                Log.getLog(ContentPortletPreference.class).error(e);
            }
            sections.setLeftValues(leftValues);
            sections.setRightValues(rightValues);
            //Initializing articles and documents
            if(preferenceArticles == null || "0".equals(preferenceArticles.getPreferenceValue()))
                articles.setChecked(false);
            else
                articles.setChecked(true);
            if(preferenceDocuments == null || "0".equals(preferenceDocuments.getPreferenceValue()))
                documents.setChecked(false);
            else
                documents.setChecked(true);
        }
    }

    public Forward onValidate(Event evt)
    {
        Forward forward = null;
        if(entity != null)
        {
            String sectionId = "";
            for(Iterator i = sections.getRightValues().keySet().iterator(); i.hasNext();)
            {
                if(!("".equals(sectionId)))
                    sectionId += ",";
                sectionId += (String) i.next();
            }
            PortletPreference preferenceSection = new PortletPreference();
            preferenceSection.setPreferenceName(ContentPortlet.PREFERENCE_SECTIONS);
            preferenceSection.setPreferenceValue(sectionId);
            preferenceSection.setPreferenceEditable(true);
            entity.getPreferences().put(ContentPortlet.PREFERENCE_SECTIONS, preferenceSection);

            String list = (String) listing.getSelectedOptions().keySet().iterator().next();
            PortletPreference preferenceListing = new PortletPreference();
            preferenceListing.setPreferenceName(ContentPortlet.PREFERENCE_LISTING);
            preferenceListing.setPreferenceValue(list);
            preferenceListing.setPreferenceEditable(true);
            entity.getPreferences().put(ContentPortlet.PREFERENCE_LISTING, preferenceListing);

            PortletPreference preferenceArticles = new PortletPreference();
            preferenceArticles.setPreferenceName(ContentPortlet.PREFERENCE_ARTICLE);
            preferenceArticles.setPreferenceValue(articles.isChecked() ? "1":"0");
            preferenceArticles.setPreferenceEditable(true);
            entity.getPreferences().put(ContentPortlet.PREFERENCE_ARTICLE, preferenceArticles);

            PortletPreference preferenceDocuments = new PortletPreference();
            preferenceDocuments.setPreferenceName(ContentPortlet.PREFERENCE_DOCUMENT);
            preferenceDocuments.setPreferenceValue(documents.isChecked() ? "1":"0");
            preferenceDocuments.setPreferenceEditable(true);
            entity.getPreferences().put(ContentPortlet.PREFERENCE_DOCUMENT, preferenceDocuments);

            PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
            try
            {
                handler.editEntity(entity);
                forward = new Forward(FORWARD_SUCCESSFUL);
            }
            catch (PortletException e)
            {
                Log.getLog(ContentPortletPreference.class).error(e);
                forward = new Forward(FORWARD_FAILED);
            }
        }
        return forward;
    }

    public void onRequest(Event event)
    {
        refresh();
    }

    public Forward onSubmit(Event evt)
    {
        if(findButtonClicked(evt).equals(cancel.getAbsoluteName()))
            return new Forward(FORWARD_CANCEL);
        else
            return super.onSubmit(evt);
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    public ComboSelectBox getSections()
    {
        return sections;
    }

    public void setSections(ComboSelectBox sections)
    {
        this.sections = sections;
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
            Log.getLog(ContentPortletPreference.class);
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

    public CheckBox getArticles()
    {
        return articles;
    }

    public void setArticles(CheckBox articles)
    {
        this.articles = articles;
    }

    public CheckBox getDocuments()
    {
        return documents;
    }

    public void setDocuments(CheckBox documents)
    {
        this.documents = documents;
    }
}
