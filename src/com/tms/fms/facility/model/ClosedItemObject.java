package com.tms.fms.facility.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import kacang.model.DefaultDataObject;

public class ClosedItemObject extends DefaultDataObject{
	
	private String id;
	private String item_code;
	private String file_name;
	private String file_type;
	private String file_path;
	private long file_size;
	private Date date;
	private String reason;
	private String createdby;
	private Date createdby_date;
	private String createdby_name;
	private String updatedby;
	private Date updatedby_date;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getItem_code() {
		return item_code;
	}
	
	public void setItem_code(String item_code) {
		this.item_code = item_code;
	}
	
	public String getFile_name() {
		return file_name;
	}
	
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	
	public String getFile_type() {
		return file_type;
	}
	
	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}
	
	public String getFile_path() {
		return file_path;
	}
	
	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}
	
	public long getFile_size() {
		return file_size;
	}
	
	public void setFile_size(long file_size) {
		this.file_size = file_size;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
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
	
	public String getCreatedby_name() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		return createdby_name + "[" + formatter.format(getCreatedby_date()) + "]";
	}
	
	public void setCreatedby_name(String createdby_name) {
		this.createdby_name = createdby_name;
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
	
}
