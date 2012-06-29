package com.tms.cms.personalization.ui;

import com.tms.cms.article.Article;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentPublisher;
import com.tms.cms.core.model.DefaultContentObject;
import com.tms.cms.personalization.PersonalizationHandler;
import com.tms.cms.personalization.PersonalizationSetting;
import com.tms.cms.document.Document;
import com.tms.cms.image.Image;
import com.tms.collab.forum.model.Forum;
import com.tms.collab.forum.model.ForumModule;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;

import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Jun 23, 2003
 * Time: 5:05:22 PM
 * To change this template use Options | File Templates.
 */
public class PersonalizationDisplay extends LightWeightWidget
{
    public static final String DEFAULT_TEMPLATE = "cms/personalization/personalization";
    public static final String DEFAULT_ATTRIBUTE_LABEL = "personalization";
    public static final String DEFAULT_URL = "myPage.jsp";

    public PersonalizationSetting setting;
    public SequencedHashMap sectionContent;
    public SequencedHashMap forumContent;

    public void onRequest(Event evt)
    {
        SecurityService securityService = (SecurityService) Application.getInstance().getService(SecurityService.class);
        User user = securityService.getCurrentUser(evt.getRequest());
        if(!(user.getId().equals(SecurityService.ANONYMOUS_USER_ID)))
        {
            try
            {
                ContentPublisher publisher = (ContentPublisher) Application.getInstance().getModule(ContentPublisher.class);
                ForumModule module = (ForumModule) Application.getInstance().getModule(ForumModule.class);
                PersonalizationHandler handler = (PersonalizationHandler) Application.getInstance().getModule(PersonalizationHandler.class);
                setting = handler.getSetting(user.getId());
                sectionContent = new SequencedHashMap();
                forumContent = new SequencedHashMap();
                for(Iterator i = setting.getSections().iterator(); i.hasNext();)
                {
                    DefaultContentObject section = (DefaultContentObject) i.next();
                    sectionContent.put(section, publisher.viewList(null, new String[] {Article.class.getName(), Document.class.getName(), Image.class.getName()}, null, section.getId(), Boolean.FALSE, "name", false, 0, setting.getNumberArticles(), ContentManager.USE_CASE_VIEW, user.getId()));
                }
                for(Iterator it = setting.getForums().iterator(); it.hasNext();)
                {
                    Forum forum = (Forum) it.next();
                    forumContent.put(forum, module.getThreads(forum.getId(), user.getId(), 0, setting.getNumberTopics()));
                }
                evt.getRequest().setAttribute(DEFAULT_ATTRIBUTE_LABEL, this);
            }
            catch (Exception e)
            {
                Log.getLog(PersonalizationDisplay.class).error(e);
            }
        }
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    public PersonalizationSetting getSetting()
    {
        return setting;
    }

    public void setSetting(PersonalizationSetting setting)
    {
        this.setting = setting;
    }

    public SequencedHashMap getSectionContent()
    {
        return sectionContent;
    }

    public void setSectionContent(SequencedHashMap sectionContent)
    {
        this.sectionContent = sectionContent;
    }

    public SequencedHashMap getForumContent()
    {
        return forumContent;
    }

    public void setForumContent(SequencedHashMap forumContent)
    {
        this.forumContent = forumContent;
    }
}
