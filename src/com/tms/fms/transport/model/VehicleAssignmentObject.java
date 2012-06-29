package com.tms.fms.transport.model;

import java.util.Collection;
import java.util.Date;

import kacang.model.DefaultDataObject;

public class VehicleAssignmentObject extends DefaultDataObject{
	
	private String vehicle_num;
	private Collection vehicleObject;
	private String category_name;
	
	private String assgId;
	private Date checkin_date;
	private Date checkout_date;
	private Long meterStart;
	private Long meterEnd;
	private String remarks;
	private String petrolCard;
	private String createdBy;
	private Date createdDate;
	private String updatedBy;
	private Date updateDate;
		
	public String getAssgId() {
		return assgId;
	}
	public void setAssgId(String assgId) {
		this.assgId = assgId;
	}
	public Date getCheckin_date() {
		return checkin_date;
	}
	public void setCheckin_date(Date checkin_date) {
		this.checkin_date = checkin_date;
	}
	public Date getCheckout_date() {
		return checkout_date;
	}
	public void setCheckout_date(Date checkout_date) {
		this.checkout_date = checkout_date;
	}
	
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getPetrolCard() {
		return petrolCard;
	}
	public void setPetrolCard(String petrolCard) {
		this.petrolCard = petrolCard;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	public String getVehicle_num() {
		return vehicle_num;
	}
	public void setVehicle_num(String vehicle_num) {
		this.vehicle_num = vehicle_num;
	}
	public Collection getVehicleObject() {
		return vehicleObject;
	}
	public void setVehicleObject(Collection vehicleObject) {
		this.vehicleObject = vehicleObject;
	}
	public Long getMeterStart() {
		return meterStart;
	}
	public void setMeterStart(Long meterStart) {
		this.meterStart = meterStart;
	}
	public Long getMeterEnd() {
		return meterEnd;
	}
	public void setMeterEnd(Long meterEnd) {
		this.meterEnd = meterEnd;
	}
	
}
