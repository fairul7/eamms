package com.tms.fms.transport.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import kacang.model.DefaultDataObject;

public class InsuranceObject  extends DefaultDataObject{
	
	private String id;
	private String vehicle_num;
	private Date rt_renew;
	private String rt_amount;
	private Date rt_period_from;
	private Date rt_period_to;
	private String is_name;
	private Date is_renew;
	private String is_amount;
	private Date is_period_from;
	private Date is_period_to;
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
	
	public String getIs_amount() {
		return is_amount;
	}
	
	public void setIs_amount(String is_amount) {
		this.is_amount = is_amount;
	}

	public String getVehicle_num() {
		return vehicle_num;
	}

	public void setVehicle_num(String vehicle_num) {
		this.vehicle_num = vehicle_num;
	}

	public Date getRt_renew() {
		return rt_renew;
	}

	public void setRt_renew(Date rt_renew) {
		this.rt_renew = rt_renew;
	}

	public String getRt_amount() {
		return rt_amount;
	}

	public void setRt_amount(String rt_amount) {
		this.rt_amount = rt_amount;
	}

	public Date getRt_period_from() {
		return rt_period_from;
	}

	public void setRt_period_from(Date rt_period_from) {
		this.rt_period_from = rt_period_from;
	}

	public Date getRt_period_to() {
		return rt_period_to;
	}

	public void setRt_period_to(Date rt_period_to) {
		this.rt_period_to = rt_period_to;
	}

	public Date getIs_renew() {
		return is_renew;
	}

	public void setIs_renew(Date is_renew) {
		this.is_renew = is_renew;
	}

	public Date getIs_period_from() {
		return is_period_from;
	}

	public void setIs_period_from(Date is_period_from) {
		this.is_period_from = is_period_from;
	}

	public Date getIs_period_to() {
		return is_period_to;
	}

	public void setIs_period_to(Date is_period_to) {
		this.is_period_to = is_period_to;
	}

	public String getCreatedby() {
		return createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
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

	public Date getCreatedby_date() {
		return createdby_date;
	}

	public void setCreatedby_date(Date createdby_date) {
		this.createdby_date = createdby_date;
	}

	public String getIs_name() {
		return is_name;
	}

	public void setIs_name(String is_name) {
		this.is_name = is_name;
	}
	
	
	
}
