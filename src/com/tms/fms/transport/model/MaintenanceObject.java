package com.tms.fms.transport.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import kacang.model.DefaultDataObject;

public class MaintenanceObject extends DefaultDataObject{
	
	private String id;
	private String vehicle_num;
	private Date service_date;
	private Date send_date;
	private String ws_id;
	private String ws_name;
	private String ws_address;
	private String order_num;
	private String inv_num;
	private String cost;
	private String reason;
	private String remark;
	private String createdby;
	private Date createdby_date;
	private String createdby_name;
	private String updatedby;
	private Date updatedby_date;
	
	public String getCreatedby() {
		return createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

	public Date getCreatedby_date() {
		return createdby_date;
	}

	public String getUpdatedby() {
		return updatedby;
	}

	public void setUpdatedby(String updatedby) {
		this.updatedby = updatedby;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getVehicle_num() {
		return vehicle_num;
	}
	
	public void setVehicle_num(String vehicle_num) {
		this.vehicle_num = vehicle_num;
	}
	
	public Date getService_date() {
		return service_date;
	}
	
	public void setService_date(Date service_date) {
		this.service_date = service_date;
	}
	
	public Date getSend_date() {
		return send_date;
	}
	
	public void setSend_date(Date send_date) {
		this.send_date = send_date;
	}
	
	public String getWs_name() {
		return ws_name;
	}
	
	public void setWs_name(String ws_name) {
		this.ws_name = ws_name;
	}
	
	public String getWs_address() {
		return ws_address;
	}
	
	public void setWs_address(String ws_address) {
		this.ws_address = ws_address;
	}
	
	public String getOrder_num() {
		return order_num;
	}
	
	public void setOrder_num(String order_num) {
		this.order_num = order_num;
	}
	
	public String getInv_num() {
		return inv_num;
	}
	
	public void setInv_num(String inv_num) {
		this.inv_num = inv_num;
	}
	
	public String getCost() {
		return cost;
	}
	
	public void setCost(String cost) {
		this.cost = cost;
	}
	
	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public String getRemark() {
		return remark;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreatedby_name() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		return createdby_name + "[" + formatter.format(getCreatedby_date()) + "]";
	}

	public Date getUpdatedby_date() {
		return updatedby_date;
	}

	public void setUpdatedby_date(Date updatedby_date) {
		this.updatedby_date = updatedby_date;
	}

	public void setCreatedby_date(Date createdby_date) {
		this.createdby_date = createdby_date;
	}

	public void setCreatedby_name(String createdby_name) {
		this.createdby_name = createdby_name;
	}

	public String getWs_id() {
		return ws_id;
	}

	public void setWs_id(String ws_id) {
		this.ws_id = ws_id;
	}

}
