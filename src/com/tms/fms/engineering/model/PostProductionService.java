package com.tms.fms.engineering.model;

import java.util.Date;

public class PostProductionService extends Service {

	protected String facilityId;
	protected String facility;
	protected Date requiredDate;
	protected Date requiredDateTo;
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
	public String getFromTime() {
		return fromTime;
	}
	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}
	public Date getRequiredDate() {
		return requiredDate;
	}
	public Date getRequiredDateTo() {
		return requiredDateTo;
	}
	public void setRequiredDateTo(Date requiredDateTo) {
		this.requiredDateTo = requiredDateTo;
	}
	public void setRequiredDate(Date requiredDate) {
		this.requiredDate = requiredDate;
	}
	public String getToTime() {
		return toTime;
	}
	public void setToTime(String toTime) {
		this.toTime = toTime;
	}
	public String getFacility() {
		return facility;
	}
	public void setFacility(String facility) {
		this.facility = facility;
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
