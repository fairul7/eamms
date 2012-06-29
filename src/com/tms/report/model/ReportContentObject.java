package com.tms.report.model;

import kacang.model.DefaultDataObject;

public class ReportContentObject extends DefaultDataObject{
	
	private String sectionName;
	private String division;
	private String uniqueCount;
	private String totalCount;
	
	private String groupName;
	
	private String contentName;
	private int submittedCount;
	private int approvedBySupervisorCount;
	private int approvedByEditorCount;
	private int approvedByComplianceCount;
	private int publishCount;
	
	private String groupId;
	private String contentSectionId;
	private String userName;
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getContentSectionId() {
		return contentSectionId;
	}
	public void setContentSectionId(String contentSectionId) {
		this.contentSectionId = contentSectionId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	public String getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}
	public String getUniqueCount() {
		return uniqueCount;
	}
	public void setUniqueCount(String uniqueCount) {
		this.uniqueCount = uniqueCount;
	}
	public int getApprovedByComplianceCount() {
		return approvedByComplianceCount;
	}
	public void setApprovedByComplianceCount(int approvedByComplianceCount) {
		this.approvedByComplianceCount = approvedByComplianceCount;
	}
	public int getApprovedByEditorCount() {
		return approvedByEditorCount;
	}
	public void setApprovedByEditorCount(int approvedByEditorCount) {
		this.approvedByEditorCount = approvedByEditorCount;
	}
	public int getApprovedBySupervisorCount() {
		return approvedBySupervisorCount;
	}
	public void setApprovedBySupervisorCount(int approvedBySupervisorCount) {
		this.approvedBySupervisorCount = approvedBySupervisorCount;
	}
	public String getContentName() {
		return contentName;
	}
	public void setContentName(String contentName) {
		this.contentName = contentName;
	}
	
	public int getPublishCount() {
		return publishCount;
	}
	public void setPublishCount(int publishCount) {
		this.publishCount = publishCount;
	}
	public int getSubmittedCount() {
		return submittedCount;
	}
	public void setSubmittedCount(int submittedCount) {
		this.submittedCount = submittedCount;
	}
	
	
}
