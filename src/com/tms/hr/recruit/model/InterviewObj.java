package com.tms.hr.recruit.model;

import java.util.Date;

import kacang.model.DefaultDataObject;

public class InterviewObj extends DefaultDataObject{
	//	interviewee
	private String[] applicantId;
	private String[] interviewDateId;
	private Date interviewDateTime;
	private String interviewStageStatus;
	private String interviewStageStatusDesc;
	private String name;
	private String email;
	private String positionDesc;
	
	//interviewer remark
	private String[] interviewerRemarkId;
	private String[] interviewerId;
	private String remark;
	private String createdBy;
	private Date createdDate;
	private String lastUpdatedBy;
	private Date lastUpdatedDate;
	private String vacancyCode;
	
	public String getVacancyCode() {
		return vacancyCode;
	}
	public void setVacancyCode(String vacancyCode) {
		this.vacancyCode = vacancyCode;
	}
	public String[] getApplicantId() {
		return applicantId;
	}
	public void setApplicantId(String[] applicantId) {
		this.applicantId = applicantId;
	}
	
	public String[] getInterviewDateId() {
		return interviewDateId;
	}
	public void setInterviewDateId(String[] interviewDateId) {
		this.interviewDateId = interviewDateId;
	}

	public String getInterviewStageStatus() {
		return interviewStageStatus;
	}
	public void setInterviewStageStatus(String interviewStageStatus) {
		this.interviewStageStatus = interviewStageStatus;
	}
	public String getInterviewStageStatusDesc() {
		return interviewStageStatusDesc;
	}
	public void setInterviewStageStatusDesc(String interviewStageStatusDesc) {
		this.interviewStageStatusDesc = interviewStageStatusDesc;
	}
	public Date getInterviewDateTime() {
		return interviewDateTime;
	}
	public void setInterviewDateTime(Date interviewDateTime) {
		this.interviewDateTime = interviewDateTime;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String[] getInterviewerId() {
		return interviewerId;
	}
	public void setInterviewerId(String[] interviewerId) {
		this.interviewerId = interviewerId;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String[] getInterviewerRemarkId() {
		return interviewerRemarkId;
	}
	public void setInterviewerRemarkId(String[] interviewerRemarkId) {
		this.interviewerRemarkId = interviewerRemarkId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPositionDesc() {
		return positionDesc;
	}
	public void setPositionDesc(String positionDesc) {
		this.positionDesc = positionDesc;
	}
	
}
