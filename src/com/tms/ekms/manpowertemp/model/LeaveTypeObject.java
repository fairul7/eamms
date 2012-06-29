package com.tms.ekms.manpowertemp.model;

import java.util.Date;

import kacang.model.DefaultDataObject;

public class LeaveTypeObject extends DefaultDataObject{
	
	private String id;
	private String leaveType;
	private String leaveTypeName;
	private String description;
	private String createdBy;
	private Date createdDate;
	private String updatedBy;
	private Date updatedDate;
	
	//constructor
	public LeaveTypeObject(){}
	
	public String getId(){
		return id;
	}
	public void setId(String id){
		this.id=id;
	}
	
	public String getLeaveType(){
		return leaveType;
	}
	
	public void setLeaveType(String leaveType){
		this.leaveType=leaveType;
	}
	
	public String getLeaveTypeName(){
		return leaveTypeName;
	}
	public void setLeaveTypeName(String leaveTypeName){
		this.leaveTypeName=leaveTypeName;
	}
	
	public String getDescription(){
		return description;
	}
	public void setDescription(String description){
		this.description=description;
	}
	
	public String getCreatedBy(){
		return createdBy;
	}
	public void setCreatedBy(String createdBy){
		this.createdBy=createdBy;
	}
	
	public Date getCreatedDate(){
		return createdDate;
	}
	public void setCreatedDate(Date createdDate){
		this.createdDate=createdDate;
	}
	
	public String getUpdatedBy(){
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy){
		this.updatedBy=updatedBy;
	}
	
	public Date getUpdatedDate(){
		return updatedDate;
	}
	
	public void setUpdatedDate(Date updatedDate){
		this.updatedDate=updatedDate;
	}

}
