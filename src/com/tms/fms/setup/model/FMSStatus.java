package com.tms.fms.setup.model;

import java.util.Date;
import kacang.model.DefaultDataObject;
public class FMSStatus extends DefaultDataObject{
	
	
	private String requestId;
	private String status;
	private String startDate;
	private String userId;
	
	//constructor
	public FMSStatus(){}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	
}
