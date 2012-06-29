package com.tms.fms.transport.model;

import kacang.model.DefaultDataObject;

import java.util.Date;
import java.util.Collection;


public class VehicleRequest extends DefaultDataObject
{
	private String id;
	private String name;
    private String requestId;
	private String category_id;
	private int quantity;
	private int driver;
	private String type;
	
	
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getCategory_id() {
		return category_id;
	}
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getDriver() {
		return driver;
	}
	public void setDriver(int driver) {
		this.driver = driver;
	}
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
    }
