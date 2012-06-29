package com.tms.elearning.folder.model;

import com.tms.elearning.core.model.LearningContentObject;

import java.io.Serializable;


/**
 * Created by IntelliJ IDEA.
 * User: tirupati
 * Date: Oct 25, 2004
 * Time: 11:44:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class Folder extends LearningContentObject implements Serializable {
    public static final String COOKIE_COURSEID_KEY = "kacang.services.security.User.uid";
    public static final String COOKIE_COURSENAME_KEY = "kacang.services.security.User.qualifier";
    private String folderName;
    private String courseId;
    private String parentId;
    private String courseName;
    private String introduction;
    private String is_public;
    private String createdByUser;
    private String createdByUserId;
    private String createdDate;

    public String getIs_public() {
        return is_public;
    }

    public void setIs_public(String is_public) {
        this.is_public = is_public;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
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

    public Class getContentModuleClass() {
        return null;
    }
}
