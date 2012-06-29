package com.tms.fms.engineering.model;

import java.util.Date;

public class StudioService extends Service {

	protected String facilityId;
	protected String facility;
	protected Date bookingDate;
	protected Date bookingDateTo;
	protected String segment;
	protected String requiredFrom, requiredTo, settingFrom, settingTo,
			rehearsalFrom, rehearsalTo, vtrFrom, vtrTo;
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

	public Date getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}

	public Date getBookingDateTo() {
		return bookingDateTo;
	}

	public void setBookingDateTo(Date bookingDateTo) {
		this.bookingDateTo = bookingDateTo;
	}

	public String getRequiredFrom() {
		return requiredFrom;
	}

	public void setRequiredFrom(String requiredFrom) {
		this.requiredFrom = requiredFrom;
	}

	public String getRequiredTo() {
		return requiredTo;
	}

	public void setRequiredTo(String requiredTo) {
		this.requiredTo = requiredTo;
	}

	public String getVtrFrom() {
		return vtrFrom;
	}

	public void setVtrFrom(String vtrFrom) {
		this.vtrFrom = vtrFrom;
	}

	public String getVtrTo() {
		return vtrTo;
	}

	public void setVtrTo(String vtrTo) {
		this.vtrTo = vtrTo;
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
