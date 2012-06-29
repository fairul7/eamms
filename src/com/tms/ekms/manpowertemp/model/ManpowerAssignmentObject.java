package com.tms.ekms.manpowertemp.model;

import java.util.Date;

import kacang.model.DefaultDataObject;

public class ManpowerAssignmentObject extends DefaultDataObject{
	private String id;
	private String requestId;
	private String manpowerId;
	private String manpowerName;	
	private String requestTitle;
	private String status;	
	private String remarks;
	private String documents;
	private String documentsName;
	private String referenceId;
	private Date startDate;
	private Date endDate;
	private Date completionDate;	
	private Date createdDate;
	//working profile
	private String startTime;
	private String endTime;
	private String reason;
	private String workingProfileCode;
	private Date workStart;
	private Date workEnd;
	private String destination;
	
	private ManpowerLeaveObject manpowerLeaveObject;
	
	
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getRequestTitle() {
		return requestTitle;
	}
	public void setRequestTitle(String requestTitle) {
		this.requestTitle = requestTitle;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getManpowerId() {
		return manpowerId;
	}
	public void setManpowerId(String manpowerId) {
		this.manpowerId = manpowerId;
	}
	public String getManpowerName() {
		return manpowerName;
	}
	public void setManpowerName(String manpowerName) {
		this.manpowerName = manpowerName;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCompletionDate() {
		return completionDate;
	}
	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getDocuments() {
		return documents;
	}
	public void setDocuments(String documents) {
		this.documents = documents;
	}
	public String getDocumentsName() {
		return documentsName;
	}
	public void setDocumentsName(String documentsName) {
		this.documentsName = documentsName;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getWorkingProfileCode() {
		return workingProfileCode;
	}
	public void setWorkingProfileCode(String workingProfileCode) {
		this.workingProfileCode = workingProfileCode;
	}
	public Date getWorkStart() {
		return workStart;
	}
	public void setWorkStart(Date workStart) {
		this.workStart = workStart;
	}
	public Date getWorkEnd() {
		return workEnd;
	}
	public void setWorkEnd(Date workEnd) {
		this.workEnd = workEnd;
	}
	public ManpowerLeaveObject getManpowerLeaveObject() {
		return manpowerLeaveObject;
	}
	public void setManpowerLeaveObject(ManpowerLeaveObject manpowerLeaveObject) {
		this.manpowerLeaveObject = manpowerLeaveObject;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
	
	
	
	

}
