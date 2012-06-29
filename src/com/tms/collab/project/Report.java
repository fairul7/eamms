package com.tms.collab.project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import kacang.model.DefaultDataObject;

public class Report extends DefaultDataObject{
	private String reportId;
	private String reportName;
    private String projectId;
    private Date dateCreated;
    private Date reportDate;  
    private String createdBy;
    private String user;
    private String firstName;
    private String lastName;   
    private Collection roles = new ArrayList();
    private Collection milestones = new ArrayList();
    private ReportProject projects;
    private Collection tasks = new ArrayList();
    private Collection defects = new ArrayList();
    private Collection cost = new ArrayList();
    
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
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public Collection getCost() {
		return cost;
	}
	public void setCost(Collection cost) {
		this.cost = cost;
	}
	public Collection getDefects() {
		return defects;
	}
	public void setDefects(Collection defects) {
		this.defects = defects;
	}
	public Collection getMilestones() {
		return milestones;
	}
	public void setMilestones(Collection milestones) {
		this.milestones = milestones;
	}
	public ReportProject getProjects() {
		return projects;
	}
	public void setProjects(ReportProject projects) {
		this.projects = projects;
	}
	public Collection getRoles() {
		return roles;
	}
	public void setRoles(Collection roles) {
		this.roles = roles;
	}
	public Collection getTasks() {
		return tasks;
	}
	public void setTasks(Collection tasks) {
		this.tasks = tasks;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getUser() {
		return firstName+" "+lastName;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public Date getReportDate() {
		return reportDate;
	}
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	
	
}
