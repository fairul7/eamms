package com.tms.collab.isr.model;

import java.util.Date;

import kacang.model.DefaultDataObject;

public class ClarificationObject extends DefaultDataObject {
	public static final String DATE_TIME_FORMAT = "dd/MM/yy hh:mm a";
	private String clarificationId = "";
	private String requestId = "";
	private String clarificationQuestion = "";
	private String clarificationAnswer = "";
	private Date dateCreated;
	private String createdBy = "";
	private Date lastUpdatedDate;
	private String lastUpdatedBy = "";
	private String createdById = ""; 
	

	public String getCreatedById() {
		return createdById;
	}
	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}
	public String getClarificationAnswer() {
		return clarificationAnswer;
	}
	public void setClarificationAnswer(String clarificationAnswer) {
		this.clarificationAnswer = clarificationAnswer;
	}
	public String getClarificationId() {
		return clarificationId;
	}
	public void setClarificationId(String clarificationId) {
		this.clarificationId = clarificationId;
	}
	public String getClarificationQuestion() {
		return clarificationQuestion;
	}
	public void setClarificationQuestion(String clarificationQuestion) {
		this.clarificationQuestion = clarificationQuestion;
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
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
}
