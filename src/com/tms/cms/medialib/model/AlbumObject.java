/*
 * AlbumObject
 * Date Created: Jun 20, 2005
 * Author: Tien Soon, Law
 * Description: An object class to represent the Album
 * Company: TMS Berhad
 */
package com.tms.cms.medialib.model;

import java.util.Date;

import kacang.model.DefaultDataObject;


public class AlbumObject extends DefaultDataObject {
    private String id = "";
    private String name = "";
    private String description = "";
    private Date eventDate;
    private boolean featured = false;
    private String libraryId = "";
    private String libraryName = "";
    private Date dateCreated;
    private Date lastUpdatedDate;
    private String createdBy = "";
    private boolean manageable = false;
    
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
    public Date getEventDate() {
        return eventDate;
    }
    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }
    public boolean isFeatured() {
        return featured;
    }
    public void setFeatured(boolean featured) {
        this.featured = featured;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }
    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
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
}
