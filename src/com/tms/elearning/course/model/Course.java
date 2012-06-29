package com.tms.elearning.course.model;

import com.tms.elearning.core.model.LearningContentObject;

import java.io.Serializable;


public class Course extends LearningContentObject implements Serializable {
    public static final String COOKIE_COURSEID_KEY = "kacang.services.security.User.uid";
    public static final String COOKIE_COURSENAME_KEY = "kacang.services.security.User.qualifier";
    private String courseName;
    private String instructor;
    private String author;
    private String Synopsis;
    private String categoryid;
    private String category; // name of the category in cel_content_course_category
    private String createdByUser;
    private String createdByUserId;
    private String createdDate;
    private String is_public;

    //for query course statistic
    private String moduleName;
    private String moduleId;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getIs_public() {
        return is_public;
    }

    public void setIs_public(String is_public) {
        this.is_public = is_public;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(String createdByUser) {
        this.createdByUser = createdByUser;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdUserId) {
        this.createdByUserId = createdUserId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getSynopsis() {
        return Synopsis;
    }

    public void setSynopsis(String synopsis) {
        Synopsis = synopsis;
    }

    public Class getContentModuleClass() {
        return null;
    }

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
