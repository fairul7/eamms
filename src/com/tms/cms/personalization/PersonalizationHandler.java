package com.tms.cms.personalization;

import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentPublisher;
import com.tms.cms.core.model.DefaultContentObject;
import com.tms.cms.section.Section;
import com.tms.collab.forum.model.Forum;
import com.tms.collab.forum.model.ForumModule;
import com.tms.collab.forum.model.ForumException;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DefaultModule;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Jun 16, 2003
 * Time: 3:27:43 PM
 * To change this template use Options | File Templates.
 */
public class PersonalizationHandler extends DefaultModule
{
    public static final int DEFAULT_NUMBER_ARTICLES = 5;
    public static final int DEFAULT_NUMBER_TOPICS = 5;
    public static final String SECTION_FLAG = "1";
    public static final String FORUM_FLAG = "0";

    public PersonalizationHandler() {}

    public PersonalizationSetting getSetting(String userId) throws PersonalizationException
    {
        PersonalizationSetting setting = new PersonalizationSetting();
        PersonalizationDao dao = (PersonalizationDao) getDao();
        try
        {
            if(userId == null)
                throw new PersonalizationException("InvalidUser ID Specified");
            setting = dao.selectSettings(userId);
            DaoQuery properties = new DaoQuery();
            properties.addProperty(new OperatorEquals("userId", userId, DaoOperator.OPERATOR_AND));
            setting.setSections(getSections(userId));
            setting.setForums(getForums(userId));
        }
        catch(DataObjectNotFoundException de)
        {
            //Inserting new Settings
            setting.setUserId(userId);
            setting.setNumberArticles(DEFAULT_NUMBER_ARTICLES);
            setting.setNumberTopics(DEFAULT_NUMBER_TOPICS);
            addSetting(setting);
        }
        catch(Exception e)
        {
            throw new PersonalizationException(e.toString(), e);
        }

        return setting;
    }

    private Collection getSections(String userId) throws PersonalizationException
    {
        PersonalizationDao dao = (PersonalizationDao) getDao();
        DaoQuery properties = new DaoQuery();
        properties.addProperty(new OperatorEquals("userId", userId, DaoOperator.OPERATOR_AND));
        Collection sections = new ArrayList();
        try
        {
            Collection list = dao.selectSections(properties, userId);
            if(list.size() > 0)
            {
                ContentPublisher publisher = (ContentPublisher) Application.getInstance().getModule(ContentPublisher.class);
                sections.addAll(publisher.viewList((String[]) list.toArray(new String[] {}), new String[] {Section.class.getName()}, null, null, Boolean.FALSE, "name", false, 0, -1, ContentManager.USE_CASE_VIEW, userId));
            }
        }
        catch (Exception e)
        {
            throw new PersonalizationException(e.toString(), e);
        }
        return sections;
    }

    private Collection getForums(String userId) throws PersonalizationException
    {
        PersonalizationDao dao = (PersonalizationDao) getDao();
        DaoQuery properties = new DaoQuery();
        properties.addProperty(new OperatorEquals("userId", userId, DaoOperator.OPERATOR_AND));
        Collection forums = new ArrayList();
        ForumModule module = (ForumModule) Application.getInstance().getModule(ForumModule.class);
        try
        {
            Collection list = dao.selectForums(properties, userId);
            for(Iterator i = list.iterator(); i.hasNext();)
                try {
                    forums.add(module.getForum((String) i.next(), userId));
                } catch (ForumException e) {
                    ;
                }
        }
        catch (Exception e)
        {
            throw new PersonalizationException(e.toString(), e);
        }
        return forums;
    }

    public void addSetting(PersonalizationSetting setting) throws PersonalizationException
    {
        try
        {
            PersonalizationDao dao = (PersonalizationDao) getDao();
            dao.insertSettings(setting);
        }
        catch(Exception e)
        {
            throw new PersonalizationException(e.toString(), e);
        }
    }

    public void updateSetting(PersonalizationSetting setting) throws PersonalizationException
    {
        try
        {
            PersonalizationDao dao = (PersonalizationDao) getDao();
            dao.updateSettings(setting);
            dao.deleteDetails(setting.getUserId(), FORUM_FLAG);
            for(Iterator i = setting.getForums().iterator(); i.hasNext();)
                dao.insertDetails(setting.getUserId(), ((Forum) i.next()).getId(), FORUM_FLAG);
            dao.deleteDetails(setting.getUserId(), SECTION_FLAG);
            for(Iterator it = setting.getSections().iterator(); it.hasNext();)
                dao.insertDetails(setting.getUserId(), ((DefaultContentObject) it.next()).getId(), SECTION_FLAG);
        }
        catch(Exception e)
        {
            throw new PersonalizationException(e.toString(), e);
        }
    }

}
