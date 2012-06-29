package com.tms.collab.isr.report.model;

import java.util.Date;

import kacang.model.DefaultDataObject;

public class ReportObject extends DefaultDataObject{
	
	private String reqDept;
	private String recDept;
	private String status;
	private String noOfReq;
	private String priority;
	private String requestType;
	private String priorityByAdmin;
	private String reqDeptNoOfReq;
	private String recDeptNoOfReq;
	private String dept;
	private String type;
	private String staffName;
	private String userId;
	
	//used in report detail
	private String requestId;
	private Date dateCreated;
	private String requestPriority;
	private String requestPriorityByAdmin;
	private String requestSubject;
	private String assignee;
	
	//used in staff report
	private String noOfReqNew;
	private String noOfReqReopen;
	private String noOfReqInProgress;
	private String noOfReqCompleted;
	private String noOfReqClose;
	private String noOfReqCancel;
	private String noOfReqClarification;
	
	private String reportLinkNew;
	private String reportLinkReopen;
	private String reportLinkInProgress;
	private String reportLinkCompleted;
	private String reportLinkClose;
	private String reportLinkCancel;
	private String reportLinkClarification;
	
	
	private String reportLink;		//concate required data for report detail
	
	private String reqDeptId;
	private String recDeptId;
	private String statusId;
	
	public String getNoOfReq() {
		return noOfReq;
	}
	public void setNoOfReq(String noOfReq) {
		this.noOfReq = noOfReq;
	}
	public String getRecDept() {
		return recDept;
	}
	public void setRecDept(String recDept) {
		this.recDept = recDept;
	}
	public String getReqDept() {
		return reqDept;
	}
	public void setReqDept(String reqDept) {
		this.reqDept = reqDept;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getPriorityByAdmin() {
		return priorityByAdmin;
	}
	public void setPriorityByAdmin(String priorityByAdmin) {
		this.priorityByAdmin = priorityByAdmin;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getRecDeptNoOfReq() {
		return recDeptNoOfReq;
	}
	public void setRecDeptNoOfReq(String recDeptNoOfReq) {
		this.recDeptNoOfReq = recDeptNoOfReq;
	}
	public String getReqDeptNoOfReq() {
		return reqDeptNoOfReq;
	}
	public void setReqDeptNoOfReq(String reqDeptNoOfReq) {
		this.reqDeptNoOfReq = reqDeptNoOfReq;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStaffName() {
		return staffName;
	}
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	public String getRecDeptId() {
		return recDeptId;
	}
	public void setRecDeptId(String recDeptId) {
		this.recDeptId = recDeptId;
	}
	public String getReqDeptId() {
		return reqDeptId;
	}
	public void setReqDeptId(String reqDeptId) {
		this.reqDeptId = reqDeptId;
	}
	public String getStatusId() {
		return statusId;
	}
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	public String getReportLink() {
		return reportLink;
	}
	public void setReportLink(String reportLink) {
		this.reportLink = reportLink;
	}
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
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
	public void setRequestId(String requestId) {
		this.requestId = requestId;
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
	public String getRequestSubject() {
		return requestSubject;
	}
	public void setRequestSubject(String requestSubject) {
		this.requestSubject = requestSubject;
	}
	public String getNoOfReqCancel() {
		return noOfReqCancel;
	}
	public void setNoOfReqCancel(String noOfReqCancel) {
		this.noOfReqCancel = noOfReqCancel;
	}
	public String getNoOfReqClarification() {
		return noOfReqClarification;
	}
	public void setNoOfReqClarification(String noOfReqClarification) {
		this.noOfReqClarification = noOfReqClarification;
	}
	public String getNoOfReqClose() {
		return noOfReqClose;
	}
	public void setNoOfReqClose(String noOfReqClose) {
		this.noOfReqClose = noOfReqClose;
	}
	public String getNoOfReqCompleted() {
		return noOfReqCompleted;
	}
	public void setNoOfReqCompleted(String noOfReqCompleted) {
		this.noOfReqCompleted = noOfReqCompleted;
	}
	public String getNoOfReqInProgress() {
		return noOfReqInProgress;
	}
	public void setNoOfReqInProgress(String noOfReqInProgress) {
		this.noOfReqInProgress = noOfReqInProgress;
	}
	public String getNoOfReqNew() {
		return noOfReqNew;
	}
	public void setNoOfReqNew(String noOfReqNew) {
		this.noOfReqNew = noOfReqNew;
	}
	public String getNoOfReqReopen() {
		return noOfReqReopen;
	}
	public void setNoOfReqReopen(String noOfReqReopen) {
		this.noOfReqReopen = noOfReqReopen;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getReportLinkCancel() {
		return reportLinkCancel;
	}
	public void setReportLinkCancel(String reportLinkCancel) {
		this.reportLinkCancel = reportLinkCancel;
	}
	public String getReportLinkClarification() {
		return reportLinkClarification;
	}
	public void setReportLinkClarification(String reportLinkClarification) {
		this.reportLinkClarification = reportLinkClarification;
	}
	public String getReportLinkClose() {
		return reportLinkClose;
	}
	public void setReportLinkClose(String reportLinkClose) {
		this.reportLinkClose = reportLinkClose;
	}
	public String getReportLinkCompleted() {
		return reportLinkCompleted;
	}
	public void setReportLinkCompleted(String reportLinkCompleted) {
		this.reportLinkCompleted = reportLinkCompleted;
	}
	public String getReportLinkInProgress() {
		return reportLinkInProgress;
	}
	public void setReportLinkInProgress(String reportLinkInProgress) {
		this.reportLinkInProgress = reportLinkInProgress;
	}
	public String getReportLinkNew() {
		return reportLinkNew;
	}
	public void setReportLinkNew(String reportLinkNew) {
		this.reportLinkNew = reportLinkNew;
	}
	public String getReportLinkReopen() {
		return reportLinkReopen;
	}
	public void setReportLinkReopen(String reportLinkReopen) {
		this.reportLinkReopen = reportLinkReopen;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
