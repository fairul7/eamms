package com.tms.fms.transport.model;

import java.util.Date;

import kacang.model.DefaultDataObject;

public class TransportFeedbackDataObject extends DefaultDataObject {
	private String requestId;
	private String supportQuality;
	private String driverPerformance;
	private String customerService;
	private String vehicleAvailability;
	private String vehicleCondition;
	private String remarks;
	private String updatedBy;
	private Date updatedDate;
	
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getSupportQuality() {
		return supportQuality;
	}
	public void setSupportQuality(String supportQuality) {
		this.supportQuality = supportQuality;
	}
	public String getDriverPerformance() {
		return driverPerformance;
	}
	public void setDriverPerformance(String driverPerformance) {
		this.driverPerformance = driverPerformance;
	}
	public String getCustomerService() {
		return customerService;
	}
	public void setCustomerService(String customerService) {
		this.customerService = customerService;
	}
	public String getVehicleAvailability() {
		return vehicleAvailability;
	}
	public void setVehicleAvailability(String vehicleAvailability) {
		this.vehicleAvailability = vehicleAvailability;
	}
	public String getVehicleCondition() {
		return vehicleCondition;
	}
	public void setVehicleCondition(String vehicleCondition) {
		this.vehicleCondition = vehicleCondition;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
}
