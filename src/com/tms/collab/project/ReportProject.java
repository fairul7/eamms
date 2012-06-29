package com.tms.collab.project;

import java.util.Date;

import kacang.model.DefaultDataObject;

public class ReportProject extends DefaultDataObject{
	private String reportId;
    private String projectId;
    private String projectName;
    private String projectValue;
    private String projectSummary;
    private String clientName;
    private String currentHighlights;
    private Float projectStatus;
    private Date projectStartDate;
    private Date projectEndDate;
    private String actualProjectStartDate;
    private String actualProjectEndDate;
    private String startVariance;
    private String endVariance;
    private String estDuration;
    private String actDuration;
    
	public Date getProjectEndDate() {
		return projectEndDate;
	}
	public void setProjectEndDate(Date projectEndDate) {
		this.projectEndDate = projectEndDate;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public Date getProjectStartDate() {
		return projectStartDate;
	}
	public void setProjectStartDate(Date projectStartDate) {
		this.projectStartDate = projectStartDate;
	}
	public Float getProjectStatus() {
		return projectStatus;
	}
	public void setProjectStatus(Float projectStatus) {
		this.projectStatus = projectStatus;
	}
	public String getProjectSummary() {
		if(projectSummary==null)
			return"";
		else
		return projectSummary;
	}
	public void setProjectSummary(String projectSummary) {
		this.projectSummary = projectSummary;
	}
	public String getProjectValue() {
		return projectValue;
	}
	public void setProjectValue(String projectValue) {
		this.projectValue = projectValue;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getCurrentHighlights() {
		return currentHighlights;
	}
	public void setCurrentHighlights(String currentHighlights) {
		this.currentHighlights = currentHighlights;
	}
	public String getActualProjectEndDate() {
		return actualProjectEndDate;
	}
	public void setActualProjectEndDate(String actualProjectEndDate) {
		this.actualProjectEndDate = actualProjectEndDate;
	}
	public String getActualProjectStartDate() {
		return actualProjectStartDate;
	}
	public void setActualProjectStartDate(String actualProjectStartDate) {
		this.actualProjectStartDate = actualProjectStartDate;
	}
	public String getEndVariance() {
		return endVariance;
	}
	public void setEndVariance(String endVariance) {
		this.endVariance = endVariance;
	}
	public String getStartVariance() {
		return startVariance;
	}
	public void setStartVariance(String startVariance) {
		this.startVariance = startVariance;
	}
	public String getActDuration() {
		return actDuration;
	}
	public void setActDuration(String actDuration) {
		this.actDuration = actDuration;
	}
	public String getEstDuration() {
		return estDuration;
	}
	public void setEstDuration(String estDuration) {
		this.estDuration = estDuration;
	}	      
}
