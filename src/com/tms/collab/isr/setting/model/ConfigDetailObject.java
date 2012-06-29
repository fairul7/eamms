package com.tms.collab.isr.setting.model;

import java.util.Collection;
import java.util.Date;

import kacang.model.DefaultDataObject;

public class ConfigDetailObject extends DefaultDataObject {
	private String configDetailId = "";
    private String configDetailName = "";
    private String configDetailDescription = "";
    private String configDetailType = "";
    private Number configDetailOrder = new Integer(1);
    private Date dateCreated;
    private String createdBy = "";
    private Date lastUpdatedDate;
    private String lastUpdatedBy = "";
    
    //Added by khaifoo
    private int newTotalReq;
    private int progressTotalReq;
    private int clarificationTotalReq;
    private int resolvedTotalReq;
    private int closedTotalReq;
    
    public static final String FILE_SIZE_UPLOAD = "File Size Upload";
    public static final String REQUEST_AUTO_CLOSE = "Request Auto Close";
    public static final String PRIORITY = "Priority";
    public static final String ALLOWED_FILE_EXTENSION = "Allowed Attachment File Extension";
    public static final String REQUEST_TYPE = "Request Type";
    public static final String DAILY_DIGEST_EXECUTION_HOUR = "Daily Digest Execution Hour";
    public static final String DAILY_DIGEST_EXECUTION_MINUTE = "Daily Digest Execution Minute";
    public static final String REMINDER_SETTING = "Reminder Setting";
    public static final String REMINDER_DEFAULT = "7";
    
    public void init(){
    	newTotalReq=0;
        progressTotalReq=0;
        clarificationTotalReq=0;
        resolvedTotalReq=0;
        closedTotalReq=0;
    }
	public String getConfigDetailDescription() {
		return configDetailDescription;
	}
	public void setConfigDetailDescription(String configDetailDescription) {
		this.configDetailDescription = configDetailDescription;
	}
	public String getConfigDetailId() {
		return configDetailId;
	}
	public void setConfigDetailId(String configDetailId) {
		this.configDetailId = configDetailId;
	}
	public String getConfigDetailName() {
		return configDetailName;
	}
	public void setConfigDetailName(String configDetailName) {
		this.configDetailName = configDetailName;
	}
	public Number getConfigDetailOrder() {
		return configDetailOrder;
	}
	public void setConfigDetailOrder(Number configDetailOrder) {
		this.configDetailOrder = configDetailOrder;
	}
	public String getConfigDetailType() {
		return configDetailType;
	}
	public void setConfigDetailType(String configDetailType) {
		this.configDetailType = configDetailType;
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
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	public int getClarificationTotalReq() {
		return clarificationTotalReq;
	}
	public void setClarificationTotalReq(int clarificationTotalReq) {
		this.clarificationTotalReq = clarificationTotalReq;
	}
	public int getClosedTotalReq() {
		return closedTotalReq;
	}
	public void setClosedTotalReq(int closedTotalReq) {
		this.closedTotalReq = closedTotalReq;
	}
	public int getNewTotalReq() {
		return newTotalReq;
	}
	public void setNewTotalReq(int newTotalReq) {
		this.newTotalReq = newTotalReq;
	}
	public int getProgressTotalReq() {
		return progressTotalReq;
	}
	public void setProgressTotalReq(int progressTotalReq) {
		this.progressTotalReq = progressTotalReq;
	}
	public int getResolvedTotalReq() {
		return resolvedTotalReq;
	}
	public void setResolvedTotalReq(int resolvedTotalReq) {
		this.resolvedTotalReq = resolvedTotalReq;
	}
	
}
