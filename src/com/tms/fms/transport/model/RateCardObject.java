package com.tms.fms.transport.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import kacang.model.DefaultDataObject;

public class RateCardObject extends DefaultDataObject{
	protected String setup_id;
	protected String name;
	protected String type;
	
	protected String detail_id;
	protected String amount;
	protected Date effective_date;
	protected String createdby;
	protected Date createdby_date;
	private String createdby_name;
	
	public String getAmount() {
		return amount;
	}
	
	public String getSetup_id() {
		return setup_id;
	}

	public void setSetup_id(String setup_id) {
		this.setup_id = setup_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDetail_id() {
		return detail_id;
	}

	public void setDetail_id(String detail_id) {
		this.detail_id = detail_id;
	}

	public Date getEffective_date() {
		return effective_date;
	}

	public void setEffective_date(Date effective_date) {
		this.effective_date = effective_date;
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

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCreatedby_name() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		return createdby_name + "[" + formatter.format(getCreatedby_date()) + "]";
	}
	
	public void setCreatedby_name(String createdby_name) {
		this.createdby_name = createdby_name;
	}
	
}
