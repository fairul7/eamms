package com.tms.collab.isr.model;

import java.util.Date;

import kacang.model.DefaultDataObject;

public class SuggestedResolutionObject extends DefaultDataObject {
	public static final String DATE_TIME_FORMAT = "dd/MM/yy hh:mm a";
	private String suggestedResolutionId = "";
	private String requestId = "";
	private String resolution = "";
	private Date dateCreated;
	private String createdBy = "";
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
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
	public String getResolution() {
		return resolution;
	}
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	public String getSuggestedResolutionId() {
		return suggestedResolutionId;
	}
	public void setSuggestedResolutionId(String suggestedResolutionId) {
		this.suggestedResolutionId = suggestedResolutionId;
	}
	
}
