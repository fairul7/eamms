package com.tms.fms.engineering.model;

import java.util.Date;

public class OtherService extends Service {

	protected String facilityId;
	protected int quantity;
	protected String facility;
	protected Date requiredFrom; 
	protected Date requiredTo;
	protected String remarks;
	protected String fromTime;
	protected String toTime;
	protected String blockBooking;
	protected String location;
	protected String submitted;
	
	public String getFacilityId() {
		return facilityId;
	}
	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}
	public String getFacility() {
		return facility;
	}
	public void setFacility(String facility) {
		this.facility = facility;
	}
	public String getFromTime() {
		return fromTime;
	}
	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Date getRequiredFrom() {
		return requiredFrom;
	}
	public void setRequiredFrom(Date requiredFrom) {
		this.requiredFrom = requiredFrom;
	}
	public Date getRequiredTo() {
		return requiredTo;
	}
	public void setRequiredTo(Date requiredTo) {
		this.requiredTo = requiredTo;
	}
	public String getToTime() {
		return toTime;
	}
	public void setToTime(String toTime) {
		this.toTime = toTime;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getBlockBooking() {
		return blockBooking;
	}
	public void setBlockBooking(String blockBooking) {
		this.blockBooking = blockBooking;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getSubmitted() {
		return submitted;
	}
	public void setSubmitted(String submitted) {
		this.submitted = submitted;
	}
	
}
