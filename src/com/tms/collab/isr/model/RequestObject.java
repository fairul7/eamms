package com.tms.collab.isr.model;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import kacang.Application;
import kacang.model.DefaultDataObject;

public class RequestObject extends DefaultDataObject {
	private String requestId = "";
	private String requestIdRequestorUrl = "";
	private String requestFromDept = ""; // aa_dept_country.associativity 
    private String requestFromDeptName = "";
	private String requestToDept = ""; // aa_dept_country.associativity
    private String requestToDeptName = "";
    private Map requestMultipleToDept;
	private String requestSubject = "";
	private String requestDescription = "";
	private String requestPriority = ""; // The name of priority 
	private String requestPriorityByAdmin = ""; // The name of priority 
	private String requestStatus = ""; // aa_isr_status.statusId
    private String requestStatusName = "";
    private String requestType = Application.getInstance().getMessage("isr.label.requestTypeDefaultOption");
	private String requestResolution = "";
	private boolean requestResolutionAccepted = false;
	private Date requestResolutionDate;
	private String relatedRequests = "";
	private Date dateCreated;
	private Date dueDate;
	private String createdBy = "";
	private Date lastUpdatedDate;
	private String lastUpdatedBy = "";
	private String assigneeList = "";
	private boolean staffResponded = false;
	private String createdById = "";
	
	private Collection attachments;
	private Collection resolutionAttachments;
	private Collection remarks;
	private Collection clarification;
	private Collection relatedRequestsCol;
	
	public String getCreatedById() {
		return createdById;
	}
	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}
	public String getAssigneeList() {
		return assigneeList;
	}
	public void setAssigneeList(String assigneeList) {
		this.assigneeList = assigneeList;
	}
	public Collection getAttachments() {
		return attachments;
	}
	public void setAttachments(Collection attachments) {
		this.attachments = attachments;
	}
	public Collection getClarification() {
		return clarification;
	}
	public void setClarification(Collection clarification) {
		this.clarification = clarification;
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
	public String getRelatedRequests() {
		return relatedRequests;
	}
	public void setRelatedRequests(String relatedRequests) {
		this.relatedRequests = relatedRequests;
	}
	public Collection getRelatedRequestsCol() {
		return relatedRequestsCol;
	}
	public void setRelatedRequestsCol(Collection relatedRequestsCol) {
		this.relatedRequestsCol = relatedRequestsCol;
	}
	public String getRequestDescription() {
		return requestDescription;
	}
	public void setRequestDescription(String requestDescription) {
		this.requestDescription = requestDescription;
	}
	public String getRequestFromDept() {
		return requestFromDept;
	}
	public void setRequestFromDept(String requestFromDept) {
		this.requestFromDept = requestFromDept;
	}
	public String getRequestFromDeptName() {
        return requestFromDeptName;
    }
    public void setRequestFromDeptName(String requestFromDeptName) {
        this.requestFromDeptName = requestFromDeptName;
    }
    public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getRequestIdRequestorUrl() {
		return requestIdRequestorUrl;
	}
	public void setRequestIdRequestorUrl(String requestIdRequestorUrl) {
		this.requestIdRequestorUrl = requestIdRequestorUrl;
	}
	public Map getRequestMultipleToDept() {
		return requestMultipleToDept;
	}
	public void setRequestMultipleToDept(Map requestMultipleToDept) {
		this.requestMultipleToDept = requestMultipleToDept;
	}
	public String getRequestPriority() {
		return requestPriority;
	}
	public void setRequestPriority(String requestPriority) {
		this.requestPriority = requestPriority;
	}
	public String getRequestPriorityByAdmin() {
		return requestPriorityByAdmin;
	}
	public void setRequestPriorityByAdmin(String requestPriorityByAdmin) {
		this.requestPriorityByAdmin = requestPriorityByAdmin;
	}
	public String getRequestResolution() {
		return requestResolution;
	}
	public void setRequestResolution(String requestResolution) {
		this.requestResolution = requestResolution;
	}
	public boolean isRequestResolutionAccepted() {
		return requestResolutionAccepted;
	}
	public void setRequestResolutionAccepted(boolean requestResolutionAccepted) {
		this.requestResolutionAccepted = requestResolutionAccepted;
	}
	public Date getRequestResolutionDate() {
		return requestResolutionDate;
	}
	public void setRequestResolutionDate(Date requestResolutionDate) {
		this.requestResolutionDate = requestResolutionDate;
	}
	public String getRequestStatus() {
		return requestStatus;
	}
	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}
	public String getRequestStatusName() {
        return requestStatusName;
    }
    public void setRequestStatusName(String requestStatusName) {
        this.requestStatusName = requestStatusName;
    }
    public String getRequestSubject() {
		return requestSubject;
	}
	public void setRequestSubject(String requestSubject) {
		this.requestSubject = requestSubject;
	}
	public String getRequestToDept() {
		return requestToDept;
	}
	public void setRequestToDept(String requestToDept) {
		this.requestToDept = requestToDept;
	}
    public String getRequestToDeptName() {
        return requestToDeptName;
    }
    public void setRequestToDeptName(String requestToDeptName) {
        this.requestToDeptName = requestToDeptName;
    }
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public Collection getResolutionAttachments() {
		return resolutionAttachments;
	}
	public void setResolutionAttachments(Collection resolutionAttachments) {
		this.resolutionAttachments = resolutionAttachments;
	}
	public Collection getRemarks() {
		return remarks;
	}
	public void setRemarks(Collection remarks) {
		this.remarks = remarks;
	}
	public boolean isStaffResponded() {
		return staffResponded;
	}
	public void setStaffResponded(boolean staffResponded) {
		this.staffResponded = staffResponded;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}	
}
