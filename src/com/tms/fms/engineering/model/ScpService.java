package com.tms.fms.engineering.model;

import java.util.Date;

public class ScpService extends Service {

	protected String facilityId;
	protected String facility;
	protected Date requiredFrom; 
	protected Date requiredTo;
	protected String departureTime;
	protected String location;
	protected String segment;
	protected String settingFrom,settingTo,rehearsalFrom,rehearsalTo,recordingFrom,recordingTo;
	protected String blockBooking;
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
	public String getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getRecordingFrom() {
		return recordingFrom;
	}
	public void setRecordingFrom(String recordingFrom) {
		this.recordingFrom = recordingFrom;
	}
	public String getRecordingTo() {
		return recordingTo;
	}
	public void setRecordingTo(String recordingTo) {
		this.recordingTo = recordingTo;
	}
	public String getRehearsalFrom() {
		return rehearsalFrom;
	}
	public void setRehearsalFrom(String rehearsalFrom) {
		this.rehearsalFrom = rehearsalFrom;
	}
	public String getRehearsalTo() {
		return rehearsalTo;
	}
	public void setRehearsalTo(String rehearsalTo) {
		this.rehearsalTo = rehearsalTo;
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
	public String getSegment() {
		return segment;
	}
	public void setSegment(String segment) {
		this.segment = segment;
	}
	public String getSettingFrom() {
		return settingFrom;
	}
	public void setSettingFrom(String settingFrom) {
		this.settingFrom = settingFrom;
	}
	public String getSettingTo() {
		return settingTo;
	}
	public void setSettingTo(String settingTo) {
		this.settingTo = settingTo;
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
