package com.tms.collab.isr.model;

import java.util.Date;

import kacang.model.DefaultDataObject;

public class RemarksObject extends DefaultDataObject {
	public static final String DATE_TIME_FORMAT = "dd/MM/yy hh:mm a";
	private String remarksId = "";
	private String requestId = "";
	private String remarks = "";
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
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getRemarksId() {
		return remarksId;
	}
	public void setRemarksId(String remarksId) {
		this.remarksId = remarksId;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
}
