package com.tms.fms.engineering.model;

import java.util.Date;

import kacang.model.DefaultDataObject;

public class FacilityObject extends DefaultDataObject{
	private String id;
	private String facilityId;
	private String name;
	private String description;
	private String category_id;
	private String category_name;
	private int quantity;
	private String createdby;
	private Date createdby_date;
	private String updatedby;
	private Date updatedby_date;
	
	private String bookingId;
	private String requestId;
	private String requestTitle;
	private Date bookFrom;
	private Date bookTo;
	private String timeFrom;
	private String timeTo;
	private String timeFromBooked;
	private String timeToBooked;
	private int quantityBooked;
	private String dateChecked;
	
	private String manpowerId;
	private String manpowerName;
	
	private String channel_name;
	private String bookingType;
	
	private String purpose;
	private String location_name;
	private Date checkout_date;
	private Date checkin_date;
	private String checkout_by;
	private String checkin_by_name;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCategory_id() {
		return category_id;
	}
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
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
	
	public String getBookingId() {
		return bookingId;
	}
	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public Date getBookFrom() {
		return bookFrom;
	}
	public void setBookFrom(Date bookFrom) {
		this.bookFrom = bookFrom;
	}
	public Date getBookTo() {
		return bookTo;
	}
	public void setBookTo(Date bookTo) {
		this.bookTo = bookTo;
	}
	public int getQuantityBooked() {
		return quantityBooked;
	}
	public void setQuantityBooked(int quantityBooked) {
		this.quantityBooked = quantityBooked;
	}
	public String getChannel_name() {
		return channel_name;
	}
	public void setChannel_name(String channel_name) {
		this.channel_name = channel_name;
	}
	public String getDateChecked() {
		return dateChecked;
	}
	public void setDateChecked(String dateChecked) {
		this.dateChecked = dateChecked;
	}
	public String getRequestTitle() {
		return requestTitle;
	}
	public void setRequestTitle(String requestTitle) {
		this.requestTitle = requestTitle;
	}
	public String getTimeFrom() {
		return timeFrom;
	}
	public void setTimeFrom(String timeFrom) {
		this.timeFrom = timeFrom;
	}
	public String getTimeTo() {
		return timeTo;
	}
	public void setTimeTo(String timeTo) {
		this.timeTo = timeTo;
	}
	public String getTimeFromBooked() {
		return timeFromBooked;
	}
	public void setTimeFromBooked(String timeFromBooked) {
		this.timeFromBooked = timeFromBooked;
	}
	public String getTimeToBooked() {
		return timeToBooked;
	}
	public void setTimeToBooked(String timeToBooked) {
		this.timeToBooked = timeToBooked;
	}	
	
	public String getDisplayTimeFromBooked(){
		return timeFromBooked.substring(0,2)+"."+timeFromBooked.substring(2);
	}
	
	public String getDisplayTimeToBooked(){
		return timeToBooked.substring(0,2)+"."+timeToBooked.substring(2);
	}
	public String getManpowerId() {
		return manpowerId;
	}
	public void setManpowerId(String manpowerId) {
		this.manpowerId = manpowerId;
	}
	public String getManpowerName() {
		return manpowerName;
	}
	public void setManpowerName(String manpowerName) {
		this.manpowerName = manpowerName;
	}
	public String getFacilityId() {
		return facilityId;
	}
	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}
	public String getBookingType() {
		return bookingType;
	}
	public void setBookingType(String bookingType) {
		this.bookingType = bookingType;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getLocation_name() {
		return location_name;
	}
	public void setLocation_name(String location_name) {
		this.location_name = location_name;
	}
	public Date getCheckout_date() {
		return checkout_date;
	}
	public void setCheckout_date(Date checkout_date) {
		this.checkout_date = checkout_date;
	}
	public Date getCheckin_date() {
		return checkin_date;
	}
	public void setCheckin_date(Date checkin_date) {
		this.checkin_date = checkin_date;
	}
	public String getCheckout_by() {
		return checkout_by;
	}
	public void setCheckout_by(String checkout_by) {
		this.checkout_by = checkout_by;
	}
	public String getCheckin_by_name() {
		return checkin_by_name;
	}
	public void setCheckin_by_name(String checkin_by_name) {
		this.checkin_by_name = checkin_by_name;
	}
	
	
}
