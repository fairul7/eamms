package com.tms.fms.transport.model;

import kacang.model.DefaultDataObject;

import java.util.Date;
import java.util.Collection;


public class DriverVehicleObject extends DefaultDataObject
{
	private String id;
	private String assignmentId;
    private String vehicle_num;
    private String driver;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAssignmentId() {
		return assignmentId;
	}
	public void setAssignmentId(String assignmentId) {
		this.assignmentId = assignmentId;
	}
	public String getVehicle_num() {
		return vehicle_num;
	}
	public void setVehicle_num(String vehicleNum) {
		vehicle_num = vehicleNum;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}	
		
    }
