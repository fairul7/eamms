package com.tms.fms.transport.model;

import java.util.Date;

import kacang.model.DefaultDataObject;

public class SetupObject extends DefaultDataObject{
	
	public final static String SETUP_CATEGORY = "fms_tran_category";
	public final static String SETUP_MAKE_TYPE = "fms_tran_maketype";
	public final static String SETUP_BODY_TYPE = "fms_tran_bodytype";
	public final static String SETUP_OUTSOURCE_PANEL = "fms_tran_outsourcepanel";
	public final static String SETUP_CHANNEL = "fms_tran_channel";
	public final static String SETUP_FUEL_TYPE = "fms_tran_fueltype";
	public final static String SETUP_PETROL_CARD = "fms_tran_petrolcard";
	public final static String SETUP_INACTIVE_REASON = "fms_tran_inactive_reason";
	public final static String SETUP_WORKSHOP = "fms_tran_workshop";
	public final static String SETUP_LOCATION = "fms_facility_location";
	public final static String SETUP_F_INACTIVE_REASON = "fms_facility_inactive_reason";
	
	protected String setup_id; 
	protected String name;
	protected String description;
	protected String type;
	protected String status;
	protected String createdby;
	protected Date createdby_date;
	protected String updatedby;
	protected Date updatedby_date;
	
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
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
	
	
}
