package com.tms.fms.reports.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.tms.fms.engineering.ui.ServiceDetailsForm;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DefaultDataObject;

public class ResourcesObject extends DefaultDataObject{
	
	private String id;
	private String serviceId;
	private String requestId;
	private String facilityId;
	private Date bookDate;	
	private int hour;
	private int minutes;
	private int requestNo;
	private int average;
	private int modMinutes;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getFacilityId() {
		return facilityId;
	}
	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}
	
	public Date getBookDate() {
		return bookDate;
	}
	public void setBookDate(Date bookDate) {
		this.bookDate = bookDate;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getRequestNo() {
		return requestNo;
	}
	public void setRequestNo(int requestNo) {
		this.requestNo = requestNo;
	}
	public int getAverage() {
		return average;
	}
	public void setAverage(int average) {
		this.average = average;
	}
	public int getMinutes() {
		return minutes;
	}
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
	public int getModMinutes() {
		return modMinutes;
	}
	public void setModMinutes(int modMinutes) {
		this.modMinutes = modMinutes;
	}
	
}
