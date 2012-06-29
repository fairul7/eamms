/*
 * MediaObject
 * Date Created: Jun 20, 2005
 * Author: Tien Soon, Law
 * Description: An object class to represent the Album
 * Company: TMS Berhad
 */
package com.tms.cms.medialib.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import kacang.model.DefaultDataObject;
import kacang.services.storage.StorageFile;
import kacang.util.Log;


public class MediaObject extends DefaultDataObject {
    private String id = "";
    private String name = "";
    private String description = "";
    private String fileName = "";
    private long fileSize = 0;
    private String fileSizeStr = "";
    private String mediaType = "";
    private String isApproved = "N";
    private int imageWidth = 0;
    private int imageHeight = 0;
    private int thumbnailWidth = 0;
    private int thumbnailHeight = 0;
    private String libraryId = "";
    private String libraryName = "";
    private String albumId = "";
    private String albumName = "";
    private Date dateCreated;
    private String createdBy = "";
    private boolean uploadSuccess = true;
    private Properties properties;
    private transient StorageFile storageFile;
    private boolean manageable = false;
    private boolean deletable = false;
    
    public MediaObject() {
        properties = new Properties();
    }
    
    public String getAlbumId() {
        return albumId;
    }
    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }
    public String getAlbumName() {
        return albumName;
    }
    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }
    public boolean isDeletable() {
        return deletable;
    }
    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }
    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    public Date getDateCreated() {
        return dateCreated;
    }
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getFileSizeStr() {
        return fileSizeStr;
    }
    public void setFileSizeStr(String fileSizeStr) {
        this.fileSizeStr = fileSizeStr;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public int getImageHeight() {
        return imageHeight;
    }
    public void setImageHeight(Number imageHeight) {
        this.imageHeight = imageHeight.intValue();
    }
    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }
    public int getImageWidth() {
        return imageWidth;
    }
    public void setImageWidth(Number imageWidth) {
        this.imageWidth = imageWidth.intValue();
    }
    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }
    public String getIsApproved() {
        return isApproved;
    }
    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }
    public String getLibraryId() {
        return libraryId;
    }
    public void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }
    public String getLibraryName() {
        return libraryName;
    }
    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }
    public boolean isManageable() {
        return manageable;
    }
    public void setManageable(boolean manageable) {
        this.manageable = manageable;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getThumbnailHeight() {
        return thumbnailHeight;
    }
    public void setThumbnailHeight(Number thumbnailHeight) {
        this.thumbnailHeight = thumbnailHeight.intValue();
    }
    public void setThumbnailHeight(int thumbnailHeight) {
        this.thumbnailHeight = thumbnailHeight;
    }
    public int getThumbnailWidth() {
        return thumbnailWidth;
    }
    public void setThumbnailWidth(Number thumbnailWidth) {
        this.thumbnailWidth = thumbnailWidth.intValue();
    }
    public void setThumbnailWidth(int thumbnailWidth) {
        this.thumbnailWidth = thumbnailWidth;
    }
    
    public boolean isUploadSuccess() {
        return uploadSuccess;
    }
    public void setUploadSuccess(boolean uploadSuccess) {
        this.uploadSuccess = uploadSuccess;
    }
    /*
     * Methods specific to Properties properties and StorageFile storageFile;
     */
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
        return properties.getProperty("fileName");
    }

    public void setFileName(String fileName) {
        if (fileName != null)
            properties.setProperty("fileName", fileName);
        else
            properties.remove("fileName");
    }

    public String getFilePath() {
        return properties.getProperty("filePath");
    }

    public void setFilePath(String filePath) {
        if (filePath != null)
            properties.setProperty("filePath", filePath);
        else
            properties.remove("filePath");
    }

    public long getFileSize() {
        try {
            return Long.parseLong(properties.getProperty("fileSize"));
        }
        catch (NumberFormatException e) {
            return 0;
        }
    }
    
    public void setFileSize(Number fileSize) {
        properties.setProperty("fileSize", new Long(fileSize.longValue()).toString());
    }
    public void setFileSize(long fileSize) {
        properties.setProperty("fileSize", new Long(fileSize).toString());
    }

    public String getMediaType() {
        return properties.getProperty("mediaType");
    }

    public void setMediaType(String mediaType) {
        if (mediaType != null)
            properties.setProperty("mediaType", mediaType);
        else
            properties.remove("mediaType");
    }

    public StorageFile getStorageFile() {
        return storageFile;
    }

    public void setStorageFile(StorageFile storageFile) {
        this.storageFile = storageFile;
    }
}
