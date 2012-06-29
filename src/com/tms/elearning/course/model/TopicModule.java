package com.tms.elearning.course.model;

import kacang.model.DefaultModule;
import kacang.model.DaoException;
import kacang.util.Log;
import kacang.Application;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: mahe
 * Date: Oct 26, 2004
 * Time: 5:34:20 PM
 * To change this template use Options | File Templates.
 */
public class TopicModule extends DefaultModule {
    Log log = Log.getLog(getClass());

    public Collection getTopics(String keyword, String course, String sort, boolean desc, int start, int rows) {
        TopicModuleDao dao = (TopicModuleDao)getDao();
        try {
            return dao.getTopics(keyword, course, sort, desc, start, rows);
        } catch (DaoException e) {
            log.error("Error finding topics " + e.toString(),e);
            throw new TopicModuleException(e.toString());
        }
    }

    public int countTopics(String keyword, String course) {
        TopicModuleDao dao = (TopicModuleDao)getDao();
        try {
            return dao.countTopics(keyword, course);
        } catch (DaoException e) {
            log.error("Error counting topics " + e.toString(),e);
            throw new TopicModuleException(e.toString());
        }
    }


    public boolean insertTopic(Topic topic) {
        TopicModuleDao dao = (TopicModuleDao)getDao();
        try {
            dao.insertTopic(topic);
            return true;
        } catch(DaoException e) {
            log.error("Error inserting topic" + e.toString(),e);
            throw new TopicModuleException(e.toString());
        }
    }

    public Topic getTopic(String id) {
        TopicModuleDao dao = (TopicModuleDao)getDao();
        try {
            return dao.getTopic(id);
        } catch (DaoException e)  {
            log.error("Error finding topic " + e.toString(), e);
            throw new TopicModuleException(e.toString());
        }
    }

    public boolean updateTopic(Topic topic) {
        TopicModuleDao dao = (TopicModuleDao)getDao();
        try {
            dao.updateTopic(topic);
            return true;
        } catch (DaoException e) {
            log.error("Error updating topic " + e.toString(), e);
            throw new TopicModuleException(e.toString());
        }
    }

    public boolean deleteTopic(String id) {
        TopicModuleDao dao = (TopicModuleDao)getDao();
        try {
            dao.deleteTopic(id);
            return true;
        } catch (DaoException e) {
            log.error("Error deleting topic " + e.toString(), e);
            throw new TopicModuleException(e.toString());
        }
    }

    public Topic topicExists(String topic) {
        TopicModuleDao dao = (TopicModuleDao)getDao();
        try {
            return dao.topicExists(topic);
        } catch (DaoException e)  {
            log.error("Error finding topic " + e.toString(), e);
            throw new TopicModuleException(e.toString());
        }
    }

    public Topic topicExists(String topic, String id) {
        TopicModuleDao dao = (TopicModuleDao)getDao();
        try {
            return dao.topicExists(topic, id);
        } catch (DaoException e)  {
            log.error("Error finding topic " + e.toString(), e);
            throw new TopicModuleException(e.toString());
        }
    }
}
