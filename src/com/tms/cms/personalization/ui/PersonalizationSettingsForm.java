package com.tms.cms.personalization.ui;

import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentPublisher;
import com.tms.cms.personalization.PersonalizationHandler;
import com.tms.cms.personalization.PersonalizationSetting;
import com.tms.cms.section.Section;
import com.tms.collab.forum.model.ForumException;
import com.tms.collab.forum.model.ForumModule;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;
import kacang.util.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Jun 18, 2003
 * Time: 10:07:13 AM
 * To change this template use Options | File Templates.
 */
public abstract class PersonalizationSettingsForm extends LightWeightWidget
{
    public static final String DEFAULT_TEMPLATE = "cms/personalization/personalizationSettings";
    public static final String DEFAULT_ATTRIBUTE_LABEL = "personalizationSettingsForm";
    public static final String DEFAULT_URL = "personalizationSettings.jsp";
    public static final String DEFAULT_FORWARD = "index.jsp";
    public static final String DEFAULT_ACTION = "Personalize";

    private PersonalizationSetting setting;
    private Collection availableSections;
    private Collection availableForums;
    private String url;
    private String forward;

    public void onRequest(Event evt)
    {
        SecurityService securityService = (SecurityService) Application.getInstance().getService(SecurityService.class);
        User user = securityService.getCurrentUser(evt.getRequest());
        if(!(user.getId().equals(SecurityService.ANONYMOUS_USER_ID)))
        {
            if(((forward == null) || ("".equals(forward))))
                setForward(DEFAULT_FORWARD);
            if(((url == null) || ("".equals(url))))
                setUrl(DEFAULT_URL);
            Object form = evt.getRequest().getAttribute(DEFAULT_ATTRIBUTE_LABEL);
            if((form == null)&&(!(form instanceof PersonalizationSettingsForm)))
            {
                if((evt.getRequest().getParameter("action") != null) && (DEFAULT_ACTION.equals(evt.getRequest().getParameter("action"))))
                    processAction(evt.getRequest(), evt.getResponse());
                init(evt.getRequest());
                evt.getRequest().setAttribute(DEFAULT_ATTRIBUTE_LABEL, this);
            }
        }
    }

    private void init(HttpServletRequest request)
    {
        PersonalizationHandler personalizationHandler = (PersonalizationHandler) Application.getInstance().getModule(PersonalizationHandler.class);
        ForumModule forumHandler = (ForumModule) Application.getInstance().getModule(ForumModule.class);
        ContentPublisher publisher = (ContentPublisher) Application.getInstance().getModule(ContentPublisher.class);
        SecurityService securityService = (SecurityService) Application.getInstance().getService(SecurityService.class);
        User user = securityService.getCurrentUser(request);
        try
        {
            setSetting(personalizationHandler.getSetting(user.getId()));
            //Retrieve Available Sections
            Collection sections = publisher.viewList(null, new String[] {Section.class.getName()}, null, null, Boolean.FALSE, "name", false, 0, -1, ContentManager.USE_CASE_VIEW, user.getId());
            sections.removeAll(setting.getSections());
            setAvailableSections(sections);
            //Retrieving Available Forums
            Collection forums = forumHandler.getForumsByUserGroupAccess(user.getId(), null, "1", "name", false, 0, -1, false);
            forums.removeAll(setting.getForums());
            setAvailableForums(forums);
        }
        catch (Exception e)
        {
            Log.getLog(PersonalizationSettingsForm.class).error(e);
        }
    }

    private void processAction(HttpServletRequest request, HttpServletResponse response)
    {
        SecurityService securityService = (SecurityService) Application.getInstance().getService(SecurityService.class);
        User user = securityService.getCurrentUser(request);
        //Populating Forums
        String[] forums = request.getParameterValues("includedForums");
        Collection forumList = new ArrayList();
        if(forums != null && (!"".equals(forums)))
        {
            ForumModule forumHandler = (ForumModule) Application.getInstance().getModule(ForumModule.class);
            for(int forumCount=0; forumCount < forums.length; forumCount++)
            {
                try
                {
                    forumList.add(forumHandler.getForum(forums[forumCount], user.getId()));
                }
                catch (ForumException e)
                {
                    Log.getLog(PersonalizationSettingsForm.class).error(e);
                }
            }
        }
        //Updating setting
        PersonalizationSetting setting = new PersonalizationSetting();
        setting.setUser(user);
        setting.setUserId(user.getId());
        setting.setNumberArticles(Integer.parseInt(request.getParameter("numberArticles")));
        setting.setNumberTopics(Integer.parseInt(request.getParameter("numberTopics")));
        setting.setForums(forumList);
        String[] sections = request.getParameterValues("includedSections");
        Collection sectionList = new ArrayList();
        if(sections != null && (!("".equals(sections))))
        {
            try
            {
                ContentPublisher publisher = (ContentPublisher) Application.getInstance().getModule(ContentPublisher.class);
                sectionList = publisher.viewList(sections, null, null, null, null, null, false, 0, -1, null, null);
            }
            catch (ContentException e)
            {
                Log.getLog(PersonalizationSettingsForm.class).error(e);
            }
        }
        setting.setSections(sectionList);

        PersonalizationHandler handler = (PersonalizationHandler) Application.getInstance().getModule(PersonalizationHandler.class);
        try
        {
            handler.updateSetting(setting);
            response.sendRedirect(response.encodeRedirectURL(getForward()));
        }
        catch (Exception e)
        {
            Log.getLog(PersonalizationSettingsForm.class).error(e);
        }
    }

    public abstract String getDefaultTemplate();

    public PersonalizationSetting getSetting()
    {
        return setting;
    }

    public void setSetting(PersonalizationSetting setting)
    {
        this.setting = setting;
    }

    public Collection getAvailableSections()
    {
        return availableSections;
    }

    public void setAvailableSections(Collection availableSections)
    {
        this.availableSections = availableSections;
    }

    public Collection getAvailableForums()
    {
        return availableForums;
    }

    public void setAvailableForums(Collection availableForums)
    {
        this.availableForums = availableForums;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getForward()
    {
        return forward;
    }

    public void setForward(String forward)
    {
        this.forward = forward;
    }
}
