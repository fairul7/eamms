package com.tms.fms.transport.model;

import java.util.Date;
import kacang.model.DefaultDataObject;

public class VehicleObject extends DefaultDataObject{
	
	private String vehicle_num;
	private String channel_id;
	private String channel_name;
	private String category_id;
	private String category_name;
	private String type;
	private String engine_num;
	private String casis_no;
	private String maketype_id;
	private String maketype_name;
	private String model;
	private String engine_cap;
	private String color;	
	private String bodytype_id;
	private String bodytype_name;
	private String location;
	private String year;
	private String ncb;
	private Date reg_date;
	private int passenger_cap;
	private String charge_id;
	private String charge_name;
	private String rental_pd;
	private String rental_ph;
	private String maintain_type;
	private String by_km;
	private String by_month;
	private Date roadtax_date;
	private Date ins_date;
	private String status;
	private String createdby;
	private Date createdby_date;
	private String updatedby;
	private Date updatedby_date;
	
	private String id;	
	private String requestId;
	private String flagId;
	private Date startDate;
	private Date endDate;
	private String requestTitle;
	
	
	public String getCharge_name() {
		return charge_name;
	}

	public void setCharge_name(String charge_name) {
		this.charge_name = charge_name;
	}

	public String getVehicle_num() {
		return vehicle_num;
	}
	
	public void setVehicle_num(String vehicle_num) {
		this.vehicle_num = vehicle_num;
	}
	
	public String getChannel_id() {
		return channel_id;
	}
	
	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}
	
	public String getCategory_id() {
		return category_id;
	}
	
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getEngine_num() {
		return engine_num;
	}
	
	public void setEngine_num(String engine_num) {
		this.engine_num = engine_num;
	}
	
	public String getMaketype_id() {
		return maketype_id;
	}
	
	public void setMaketype_id(String maketype_id) {
		this.maketype_id = maketype_id;
	}
	
	public String getModel() {
		return model;
	}
	
	public void setModel(String model) {
		this.model = model;
	}
	
	public String getEngine_cap() {
		return engine_cap;
	}
	
	public void setEngine_cap(String engine_cap) {
		this.engine_cap = engine_cap;
	}
	
	public String getBodytype_id() {
		return bodytype_id;
	}
	
	public void setBodytype_id(String bodytype_id) {
		this.bodytype_id = bodytype_id;
	}
	
	public Date getReg_date() {
		return reg_date;
	}
	
	public void setReg_date(Date reg_date) {
		this.reg_date = reg_date;
	}
	
	public int getPassenger_cap() {
		return passenger_cap;
	}
	
	public void setPassenger_cap(int passenger_cap) {
		this.passenger_cap = passenger_cap;
	}
	
	public String getCharge_id() {
		return charge_id;
	}
	
	public void setCharge_id(String charge_id) {
		this.charge_id = charge_id;
	}
	
	public String getRental_pd() {
		return rental_pd;
	}
	
	public void setRental_pd(String rental_pd) {
		this.rental_pd = rental_pd;
	}
	
	public String getRental_ph() {
		return rental_ph;
	}
	
	public void setRental_ph(String rental_ph) {
		this.rental_ph = rental_ph;
	}
	
	public String getMaintain_type() {
		return maintain_type;
	}
	
	public void setMaintain_type(String maintain_type) {
		this.maintain_type = maintain_type;
	}
	
	public String getBy_km() {
		return by_km;
	}
	
	public void setBy_km(String by_km) {
		this.by_km = by_km;
	}
	
	public String getBy_month() {
		return by_month;
	}
	
	public void setBy_month(String by_month) {
		this.by_month = by_month;
	}
	
	public Date getRoadtax_date() {
		return roadtax_date;
	}
	
	public void setRoadtax_date(Date roadtax_date) {
		this.roadtax_date = roadtax_date;
	}
	
	public Date getIns_date() {
		return ins_date;
	}
	
	public void setIns_date(Date ins_date) {
		this.ins_date = ins_date;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
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

	public String getMaketype_name() {
		return maketype_name;
	}

	public void setMaketype_name(String maketype_name) {
		this.maketype_name = maketype_name;
	}

	public String getChannel_name() {
		return channel_name;
	}

	public void setChannel_name(String channel_name) {
		this.channel_name = channel_name;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public String getCasis_no() {
		return casis_no;
	}

	public void setCasis_no(String casis_no) {
		this.casis_no = casis_no;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getBodytype_name() {
		return bodytype_name;
	}

	public void setBodytype_name(String bodytype_name) {
		this.bodytype_name = bodytype_name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getNcb() {
		return ncb;
	}

	public void setNcb(String ncb) {
		this.ncb = ncb;
	}
	
	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getRequestTitle() {
		return requestTitle;
	}

	public void setRequestTitle(String requestTitle) {
		this.requestTitle = requestTitle;
	}

	public String getFlagId() {
		return flagId;
	}

	public void setFlagId(String flagId) {
		this.flagId = flagId;
	}

}
