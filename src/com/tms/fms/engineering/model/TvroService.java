package com.tms.fms.engineering.model;

import java.util.Date;

public class TvroService extends Service {

	protected String facilityId;
	protected String facility;
	protected String feedTitle;
	protected String location;
	protected Date requiredDate;
	protected Date requiredDateTo;
	protected String fromTime;
	protected String toTime;
	protected String timezone;
	protected int totalTimeReq;
	protected String timeMeasure;
	protected String remarks;
	protected String blockBooking;
	protected String submitted;
	
	public String getFeedTitle() {
		return feedTitle;
	}
	public void setFeedTitle(String feedTitle) {
		this.feedTitle = feedTitle;
	}
	public String getFromTime() {
		return fromTime;
	}
	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Date getRequiredDate() {
		return requiredDate;
	}
	public void setRequiredDate(Date requiredDate) {
		this.requiredDate = requiredDate;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	public String getToTime() {
		return toTime;
	}
	public void setToTime(String toTime) {
		this.toTime = toTime;
	}
	public String getTimeMeasure() {
		return timeMeasure;
	}
	public String getTimeMeasureLabel() {
		return (String)EngineeringModule.TIMEMEASURES.get(timeMeasure);
	}
	public void setTimeMeasure(String timeMeasure) {
		this.timeMeasure = timeMeasure;
	}
	public int getTotalTimeReq() {
		return totalTimeReq;
	}
	public void setTotalTimeReq(int totalTimeReq) {
		this.totalTimeReq = totalTimeReq;
	}
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
	public Date getRequiredDateTo() {
		return requiredDateTo;
	}
	public void setRequiredDateTo(Date requiredDateTo) {
		this.requiredDateTo = requiredDateTo;
	}
	public String getBlockBooking() {
		return blockBooking;
	}
	public void setBlockBooking(String blockBooking) {
		this.blockBooking = blockBooking;
	}
	public String getSubmitted() {
		return submitted;
	}
	public void setSubmitted(String submitted) {
		this.submitted = submitted;
	}
	
}
