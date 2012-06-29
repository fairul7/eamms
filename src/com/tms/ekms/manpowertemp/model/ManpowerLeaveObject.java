package com.tms.ekms.manpowertemp.model;

import java.util.Date;

import kacang.model.DefaultDataObject;

public class ManpowerLeaveObject extends DefaultDataObject{
	private String id;
	private String leaveId;
	private String manpowerId;
	private String manpowerName;
	private Date startDate;
	private Date endDate;
	private String leaveType;
	private String assignmentId;
	private String requestId;
	private String flagId;

	public String getFlagId() {
		return flagId;
	}

	public void setFlagId(String flagId) {
		this.flagId = flagId;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
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

	//constructor
	public ManpowerLeaveObject(){
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLeaveId() {
		return leaveId;
	}

	public void setLeaveId(String leaveId) {
		this.leaveId = leaveId;
	}

	public String getManpowerId() {
		return manpowerId;
	}

	public void setManpowerId(String manpowerId) {
		this.manpowerId = manpowerId;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public String getManpowerName() {
		return manpowerName;
	}

	public void setManpowerName(String manpowerName) {
		this.manpowerName = manpowerName;
	}

	public String getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(String assignmentId) {
		this.assignmentId = assignmentId;
	}

}
