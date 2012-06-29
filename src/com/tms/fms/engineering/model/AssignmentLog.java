package com.tms.fms.engineering.model;

import java.util.Date;

import kacang.model.DefaultDataObject;

public class AssignmentLog extends DefaultDataObject {
	private String logId;
	private String assignmentId;
	private String userId;
	private String oldUserId;
	private String assignBy;
	private Date assignDate;
	private String strAssignDate;

	public String getLogId() {
		return logId;
	}
	public void setLogId(String logId) {
		this.logId = logId;
	}
	public String getAssignmentId() {
		return assignmentId;
	}
	public void setAssignmentId(String assignmentId) {
		this.assignmentId = assignmentId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOldUserId() {
		return oldUserId;
	}
	public void setOldUserId(String oldUserId) {
		this.oldUserId = oldUserId;
	}
	public String getAssignBy() {
		return assignBy;
	}
	public void setAssignBy(String assignBy) {
		this.assignBy = assignBy;
	}
	public Date getAssignDate() {
		return assignDate;
	}
	public void setAssignDate(Date assignDate) {
		this.assignDate = assignDate;
	}
	public String getStrAssignDate() {
		return strAssignDate;
	}
	public void setStrAssignDate(String strAssignDate) {
		this.strAssignDate = strAssignDate;
	}
}
