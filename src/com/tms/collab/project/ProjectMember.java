package com.tms.collab.project;

import kacang.model.DefaultDataObject;

public class ProjectMember extends DefaultDataObject {
	
	 private String projectId;
	 private String memberId;
	 private String firstName;
	 private String lastName;
	 
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
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getName() {
        return getProperty("firstName") + " " + getProperty("lastName");
    }
}
