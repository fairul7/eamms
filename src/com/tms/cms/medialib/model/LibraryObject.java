/*
 * LibraryObject
 * Date Created: Jun 16, 2005
 * Author: Tien Soon, Law
 * Description: An object class to represent the Library
 * Company: TMS Berhad
 */
package com.tms.cms.medialib.model;

import java.util.Date;
import java.util.Map;

import kacang.model.DefaultDataObject;


public class LibraryObject extends DefaultDataObject {
    private String id = "";
    private String name = "";
    private String description = "";
    private String approvalNeeded = "Y";
    private int maxWidth = 500;
    private Map managerGroup;
    private Map contributorGroup;
    private Map viewerGroup;
    private String createdBy = "";
    private Date dateCreated;
    private Date lastUpdated;
    private boolean manageable = false;
    private String totalAlbum = "0";
    
    public String getApprovalNeeded() {
        return approvalNeeded;
    }
    public void setApprovalNeeded(String approvalNeeded) {
        this.approvalNeeded = approvalNeeded;
    }
    public Map getContributorGroup() {
        return contributorGroup;
    }
    public void setContributorGroup(Map contributorGroup) {
        this.contributorGroup = contributorGroup;
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
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Date getLastUpdated() {
        return lastUpdated;
    }
    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    public Map getManagerGroup() {
        return managerGroup;
    }
    public void setManagerGroup(Map managerGroup) {
        this.managerGroup = managerGroup;
    }
    public boolean isManageable() {
        return manageable;
    }
    public void setManageable(boolean manageable) {
        this.manageable = manageable;
    }
    public int getMaxWidth() {
        return maxWidth;
    }
    public void setMaxWidth(Number maxWidth) {
        this.maxWidth = maxWidth.intValue();
    }
    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
	public String getTotalAlbum() {
		return totalAlbum;
	}
	public void setTotalAlbum(String totalAlbum) {
		this.totalAlbum = totalAlbum;
	}
	public Map getViewerGroup() {
        return viewerGroup;
    }
    public void setViewerGroup(Map viewerGroup) {
        this.viewerGroup = viewerGroup;
    }
}
