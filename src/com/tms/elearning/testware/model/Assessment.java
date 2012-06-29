package com.tms.elearning.testware.model;

import com.tms.elearning.core.model.LearningContentObject;

import java.io.Serializable;

import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User: mahe
 * Date: Nov 23, 2004
 * Time: 2:45:38 PM
 * To change this template use Options | File Templates.
 */
public class Assessment extends LearningContentObject implements Serializable {
    private String id;
    private String examid;
    private String course_id;
    private String course;
    private String module_id;
    private String lesson_id;
    private String name;
    private String start_date;
    private String end_date;
    private double time_limit;
    private int level;


    //private String is_active;
    private String is_public;
    private String modifiedDateByUser;
    private String modifiedDateByUserId;
    private String modifiedDate;
    private String parentId;
    private String questionId;

    //for statistic
    private Date lastTakenDate;
    private int numbTaken;
    private Date startDate;
    private Date endDate;

    public Date getLastTakenDate() {
        return lastTakenDate;
    }

    public void setLastTakenDate(Date lastTakenDate) {
        this.lastTakenDate = lastTakenDate;
    }

    public int getNumbTaken() {
        return numbTaken;
    }

    public void setNumbTaken(int numbTaken) {
        this.numbTaken = numbTaken;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getModifiedDateByUser() {
        return modifiedDateByUser;
    }

    public void setModifiedDateByUser(String modifiedDateByUser) {
        this.modifiedDateByUser = modifiedDateByUser;
    }

    public String getModifiedDateByUserId() {
        return modifiedDateByUserId;
    }

    public void setModifiedDateByUserId(String modifiedDateByUserId) {
        this.modifiedDateByUserId = modifiedDateByUserId;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }




    public String getExamid() {
        return examid;
    }

    public void setExamid(String examid) {
        this.examid = examid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getModule_id() {
        return module_id;
    }

    public void setModule_id(String module_id) {
        this.module_id = module_id;
    }

    public String getLesson_id() {
        return lesson_id;
    }

    public void setLesson_id(String lesson_id) {
        this.lesson_id = lesson_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public double getTime_limit() {
        return time_limit;
    }

    public void setTime_limit(double time_limit) {
        this.time_limit = time_limit;
    }

    public Class getContentModuleClass() {
        return null;
    }

    public String getIs_public() {
        return is_public;
    }

    public void setIs_public(String is_public) {
        this.is_public = is_public;
    }


}
