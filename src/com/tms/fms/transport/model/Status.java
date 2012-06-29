package com.tms.fms.transport.model;

import java.util.Date;

import com.tms.fms.setup.model.SetupModule;

import kacang.Application;
import kacang.model.DefaultDataObject;

public class Status extends DefaultDataObject {
	private String statusId;
	private String id;
	private String status;
	private String reason;
	private String createdBy;
	private Date createdDate;
		
	public String getStatusId() {
		return statusId;
	}
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public String getStatusLabel() {
		SetupModule sm = (SetupModule) Application.getInstance().getModule(SetupModule.class);
		TransportModule tm = (TransportModule) Application.getInstance().getModule(TransportModule.class);
		String statusNo = (String)status;					
		String status = "";
		
		status = tm.selectStatus(statusNo);
					
		
		if(sm.PENDING_STATUS.equals(statusNo))
			status=Application.getInstance().getMessage("fms.tran.sentForHODApproval");
		
		if(sm.PROCESS_STATUS.equals(statusNo))
			status=Application.getInstance().getMessage("fms.tran.hODProcess");
		
		

		return status;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	} 
}
