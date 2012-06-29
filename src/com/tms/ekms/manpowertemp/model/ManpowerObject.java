package com.tms.ekms.manpowertemp.model;

import java.util.Collection;

import kacang.model.DefaultDataObject;

public class ManpowerObject extends DefaultDataObject{
	private String id;
	private String manpowerId;
	private String manpowerName;
	private String requestId;
	private String workprofile;
	
	private Collection assignments;
	private Collection leaves;
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	public Collection getAssignments() {
		return assignments;
	}
	public void setAssignments(Collection assignments) {
		this.assignments = assignments;
	}
	public Collection getLeaves() {
		return leaves;
	}
	public void setLeaves(Collection leaves) {
		this.leaves = leaves;
	}
	public String getWorkprofile() {
		return workprofile;
	}
	public void setWorkprofile(String workprofile) {
		this.workprofile = workprofile;
	}
	
	
}
