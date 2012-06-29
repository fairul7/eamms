package com.tms.elearning.course.model;

import kacang.model.DataSourceDao;
import kacang.model.DaoException;

import java.util.Collection;
import java.util.HashMap;

import com.tms.elearning.course.model.Topic;
import com.tms.elearning.testware.model.Question;

/**
 * Created by IntelliJ IDEA.
 * User: mahe
 * Date: Oct 25, 2004
 * Time: 5:58:00 PM
 * To change this template use Options | File Templates.
 */

public class TopicModuleDao extends DataSourceDao {

    public Collection getTopics(String keyword, String course, String sort, boolean desc, int start, int rows) throws DaoException {
        String condition = (keyword != null)?"%"+keyword+"%":"%";
        String orderBy = (sort != null)?sort:"topic";

        if (desc) orderBy += " DESC";

        if (course.equalsIgnoreCase("-1")) {
            Object[] args = {condition};
            return super.select("SELECT cel_content_course_topic.id, name AS course_name, topic FROM cel_content_course_topic,cel_content_course WHERE topic LIKE ?  AND course_id = cel_content_course.id ORDER BY " + orderBy,Topic.class,args,start,rows);
        } else {
            Object[] args = {condition,course};
            return super.select("SELECT cel_content_course_topic.id, name AS course_name, topic FROM cel_content_course_topic,cel_content_course WHERE topic LIKE ? AND course_id = ? AND course_id = cel_content_course.id ORDER BY " + orderBy,Topic.class,args,start,rows);
        }
    }

     public int countTopics(String keyword, String course) throws DaoException {
        Collection list = null;
        String condition = (keyword != null)?"%"+keyword+"%":"%";

        if (course.equalsIgnoreCase("-1")) {
            Object[] args = {condition};
            list = super.select("SELECT COUNT(*) AS total FROM cel_content_course_topic WHERE topic LIKE ?", HashMap.class,args,0,1);
        } else {
            Object[] args = {condition,course};
            list = super.select("SELECT COUNT(*) AS total FROM cel_content_course_topic WHERE topic LIKE ? AND course_id = ?", HashMap.class,args,0,1);
        }

        HashMap map = (HashMap)list.iterator().next();
        return Integer.parseInt(map.get("total").toString());
    }

    public Topic getTopic(String id) throws DaoException {
        Object[] args = { id };
        Collection results = super.select("SELECT id, course_id, topic FROM cel_content_course_topic WHERE id = ?", Topic.class, args, 0, 1);
        if (results.size() == 0) {
            return null;
        }
        return (Topic)results.iterator().next();
    }

    public void updateTopic(Topic topic) throws DaoException {
        super.update("UPDATE cel_content_course_topic SET topic=#topic#, course_id=#course_id# WHERE id=#id#", topic);
    }

    public Collection getCourses(String userId) throws DaoException {
 //       Object[] args = {userId};
    //    return super.select("SELECT id AS course_id, name AS course_name FROM cel_content_course WHERE public=1 OR createdByUser=? ORDER BY course_name",Topic.class,args,0,-1);

     //   return super.select("SELECT id AS course_id, name AS course_name FROM cel_content_course WHERE createdByUser=? ORDER BY course_name",Topic.class,args,0,-1);
        return super.select("SELECT id AS course_id, name AS course_name FROM cel_content_course",Topic.class,"",0,-1);

    }

     public Collection getCourses() throws DaoException {
        return super.select("SELECT id AS course_id, name AS course_name FROM cel_content_course ORDER BY course_name",Topic.class,null,0,-1);
    }


    public Collection getTopicList() throws DaoException {
        return super.select("SELECT id, topic FROM cel_content_course_topic ORDER BY topic",Topic.class,null,0,-1);
    }

    public void insertTopic(Topic topic) throws DaoException {
        super.update("INSERT INTO cel_content_course_topic (id, course_id, topic) VALUES (#id#, #course_id#, #topic#)", topic);
    }

    public void deleteTopic(String id) throws DaoException {
        Object[] args = new String[] { id };
        super.update("DELETE FROM cel_content_course_topic WHERE id=?", args);
    }

    public Topic topicExists(String topic) throws DaoException {
        Object[] args = { topic };
        Collection results = super.select("SELECT id FROM cel_content_course_topic WHERE topic = ?", Topic.class, args, 0, 1);
        if (results.size() == 0) {
            return null;
        }
        return (Topic)results.iterator().next();
    }

    public Topic topicExists(String topic, String id) throws DaoException {
        Object[] args = { topic,id };
        Collection results = super.select("SELECT id FROM cel_content_course_topic WHERE topic = ? AND id <> ?", Topic.class, args, 0, 1);
        if (results.size() == 0) {
            return null;
        }
        return (Topic)results.iterator().next();
    }







}
