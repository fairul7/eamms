package com.tms.collab.isr.model;

import kacang.model.DefaultDataObject;

public class StatusObject extends DefaultDataObject {
	public static final String STATUS_ID_NEW = "1";
	public static final String STATUS_ID_REOPEN = "2";
	public static final String STATUS_ID_IN_PROGRESS = "3";
	public static final String STATUS_ID_COMPLETED = "4"; // Also known as Resolved
	public static final String STATUS_ID_CLOSE = "5";
	public static final String STATUS_ID_CANCEL = "6";
	public static final String STATUS_ID_CLARIFICATION = "7";
	
	private String statusId = "";
	private String statusName = "";
	
	public String getStatusId() {
		return statusId;
	}
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
}
