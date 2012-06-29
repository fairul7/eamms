package com.tms.collab.isr.model;

import java.util.Date;

import kacang.model.DefaultDataObject;

public class AssignmentRemarksObject extends DefaultDataObject {
	public static final String DATE_TIME_FORMAT = "dd/MM/yy hh:mm a";
	private String assignmentRemarksId = "";
	private String requestId = "";
	private String assignmentRemarks = "";
	private Date dateCreated;
	private String createdBy = "";
	
	public String getAssignmentRemarks() {
		return assignmentRemarks;
	}
	public void setAssignmentRemarks(String assignmentRemarks) {
		this.assignmentRemarks = assignmentRemarks;
	}
	public String getAssignmentRemarksId() {
		return assignmentRemarksId;
	}
	public void setAssignmentRemarksId(String assignmentRemarksId) {
		this.assignmentRemarksId = assignmentRemarksId;
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
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String reqestId) {
		this.requestId = reqestId;
	}
}
