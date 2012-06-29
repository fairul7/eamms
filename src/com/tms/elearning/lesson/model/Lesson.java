package com.tms.elearning.lesson.model;

import kacang.model.DefaultDataObject;
import kacang.services.storage.StorageFile;
import kacang.util.Log;

import java.io.Serializable;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.util.Properties;

import com.tms.elearning.core.model.LearningContentObject;

/**
 * Created by IntelliJ IDEA.
 * User: tirupati
 * Date: Oct 26, 2004
 * Time: 4:37:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class Lesson extends LearningContentObject implements Serializable{
    public static final String COOKIE_COURSEID_KEY = "kacang.services.security.User.uid";
    public static final String COOKIE_COURSENAME_KEY = "kacang.services.security.User.qualifier";

    private String lessonName;
    private String courseId;
    private String courseName;
    private String folderId;
    private String folderName;
    private String brief;
    private String createdByUser;
    private String createdByUserId;
    private String createdDate;
    private String id;



    private String is_public;
    private String filePath;
    private String fileName;
    private transient StorageFile storageFile;
    private Properties properties;
    private String attachment;
    private String associatedoc;
    public Lesson() {
        properties = new Properties();
    }


    public String getContents() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            properties.store(os, "");
            return os.toString();
        }
        catch (IOException e) {
            return properties.toString();
        }
    }

    public void setContents(String contents) {
        properties = new Properties();
        if (contents != null) {
            ByteArrayInputStream is = new ByteArrayInputStream(contents.getBytes());
            try {
                properties.load(is);
            }
            catch (IOException e) {
                Log.getLog(getClass()).error(e.toString(), e);
            }
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    
    public long getFileSize() {
        try {
            return Long.parseLong(properties.getProperty("fileSize"));
        }
        catch (NumberFormatException e) {
            return 0;
        }
    }

    public void setFileSize(long fileSize) {
        properties.setProperty("fileSize", new Long(fileSize).toString());
    }
    public StorageFile getStorageFile() {
        return storageFile;
    }

    public void setStorageFile(StorageFile storageFile) {
        this.storageFile = storageFile;
    }
     public Class getContentModuleClass() {

        return null;
    }

    public String getContentType() {
        return properties.getProperty("contentType");
    }

    public void setContentType(String contentType) {
        if (contentType != null)
            properties.setProperty("contentType", contentType);
        else
            properties.remove("contentType");
    }


      public String getIs_public() {
        return is_public;
    }

    public void setIs_public(String is_public) {
        this.is_public = is_public;
    }

    public String getAssociatedoc() {
        return associatedoc;
    }

    public void setAssociatedoc(String associatedoc) {
        this.associatedoc = associatedoc;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

