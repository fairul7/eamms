package com.tms.fms.transport.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import kacang.model.DefaultDataObject;

public class InactiveObject extends DefaultDataObject {

	private String id;
	private String vehicle_num;
	private Date date_from;
	private Date date_to;
	private String reason_id;
	private String reason;
	private String createdby;
	private Date createdby_date;
	private String createdby_name;
	private String updatedby;
	private Date updatedby_date;
	
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getVehicle_num() {
		return vehicle_num;
	}
	
	public void setVehicle_num(String vehicle_num) {
		this.vehicle_num = vehicle_num;
	}
	
	public Date getDate_from() {
		return date_from;
	}
	
	public void setDate_from(Date date_from) {
		this.date_from = date_from;
	}
	
	public Date getDate_to() {
		return date_to;
	}
	
	public void setDate_to(Date date_to) {
		this.date_to = date_to;
	}
	
	public String getReason_id() {
		return reason_id;
	}
	
	public void setReason_id(String reason_id) {
		this.reason_id = reason_id;
	}
	
	public String getCreatedby() {
		return createdby;
	}
	
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	
	public Date getCreatedby_date() {
		return createdby_date;
	}
	
	public void setCreatedby_date(Date createdby_date) {
		this.createdby_date = createdby_date;
	}
	
	public String getUpdatedby() {
		return updatedby;
	}
	
	public void setUpdatedby(String updatedby) {
		this.updatedby = updatedby;
	}
	
	public Date getUpdatedby_date() {
		return updatedby_date;
	}
	
	public void setUpdatedby_date(Date updatedby_date) {
		this.updatedby_date = updatedby_date;
	}
	
	public String getCreatedby_name() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		return createdby_name + "[" + formatter.format(getCreatedby_date()) + "]";
	}
	
	public void setCreatedby_name(String createdby_name) {
		this.createdby_name = createdby_name;
	}
}
