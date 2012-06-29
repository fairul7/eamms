package com.tms.elearning.course.model;

import kacang.model.DefaultDataObject;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: mahe
 * Date: Oct 25, 2004
 * Time: 4:09:15 PM
 * To change this template use Options | File Templates.
 */
public class Topic extends DefaultDataObject implements Serializable{
    private String id;
    private String course_id;
    private String course_name;
    private String topic;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
