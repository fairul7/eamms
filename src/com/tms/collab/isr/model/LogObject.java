package com.tms.collab.isr.model;

import java.util.Date;

import kacang.model.DefaultDataObject;

public class LogObject extends DefaultDataObject {
	public static final String LOG_ACTION_TYPE_NEW = "isr.log.action.newRequest";
	public static final String LOG_ACTION_TYPE_STATUS = "isr.log.action.statusUpdate";
	public static final String LOG_DESC_NEW_REQUEST = "isr.log.desc.newRequest";
	public static final String LOG_DESC_STATUS_UPDATE_IN_PROGRESS = "isr.log.desc.statusUpdate.inProgress";
	public static final String LOG_DESC_STATUS_UPDATE_COMPLETED = "isr.log.desc.statusUpdate.completed";
	public static final String LOG_DESC_STATUS_UPDATE_REOPEN = "isr.log.desc.statusUpdate.reopen";
	public static final String LOG_DESC_STATUS_UPDATE_CLOSE = "isr.log.desc.statusUpdate.close";
	public static final String LOG_DESC_STATUS_UPDATE_CANCEL = "isr.log.desc.statusUpdate.cancel";
	public static final String LOG_DESC_STATUS_UPDATE_CLARIFICATION = "isr.log.desc.statusUpdate.clarification";
	
	private String logId = "";
	private String requestId = "";
	private String logAction = "";
	private String logDescription = "";
	private Date dateCreated;
	private String createdBy = "";
	
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
	public String getLogAction() {
		return logAction;
	}
	public void setLogAction(String logAction) {
		this.logAction = logAction;
	}
	public String getLogDescription() {
		return logDescription;
	}
	public void setLogDescription(String logDescription) {
		this.logDescription = logDescription;
	}
	public String getLogId() {
		return logId;
	}
	public void setLogId(String logId) {
		this.logId = logId;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
}
