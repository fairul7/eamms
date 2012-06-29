package com.tms.fms.transport.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.tms.fms.facility.model.SetupModule;

import kacang.model.DefaultDataObject;

public class TransportRequest extends DefaultDataObject {

	private String id;
	private String requestId;
	private String requestTitle;
	private String requestType;
	private String program;
	private Date startDate;
	private Date endDate;
	private String destination;
	private String purpose;
	private String remarks;
	private String status;
	private String reason;
	private String requestBy;
	private Date requestDate;
	private String updatedBy;
	private Date updatedDate;
	private String approvedBy;
	private Date approvedDate;
	private String statusRequest;
	private String manpowerId;
	private String vehicle_num;
	private String department;
	private String idAssignment;
	private String blockBooking;
	private String engineeringRequestId;
	private String driver;
	private String rate;
	private String rateVehicle;

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getId() {
		return id;
	}

	public String getRequestTitle() {
		return requestTitle;
	}

	public void setRequestTitle(String requestTitle) {
		this.requestTitle = requestTitle;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
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

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getRequestBy() {
		return requestBy;
	}

	public void setRequestBy(String requestBy) {
		this.requestBy = requestBy;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
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

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public String getStatusRequest() {
		return statusRequest;
	}

	public void setStatusRequest(String statusRequest) {
		this.statusRequest = statusRequest;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getManpowerId() {
		return manpowerId;
	}

	public void setManpowerId(String manpowerId) {
		this.manpowerId = manpowerId;
	}

	public String getVehicle_num() {
		return vehicle_num;
	}

	public void setVehicle_num(String vehicle_num) {
		this.vehicle_num = vehicle_num;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getIdAssignment() {
		return idAssignment;
	}

	public void setIdAssignment(String idAssignment) {
		this.idAssignment = idAssignment;
	}

	public String getBlockBooking() {
		return blockBooking;
	}

	public void setBlockBooking(String blockBooking) {
		this.blockBooking = blockBooking;
	}

	public String getEngineeringRequestId() {
		return engineeringRequestId;
	}

	public void setEngineeringRequestId(String engineeringRequestId) {
		this.engineeringRequestId = engineeringRequestId;
	}
	
	public String getRequiredDateFrom() {
		SimpleDateFormat sdf = new SimpleDateFormat(SetupModule.DATE_FORMAT);
		
		String requiredDateFrom = sdf.format(getStartDate());
		String requiredDateTo = sdf.format(getEndDate());
		
		return requiredDateFrom + " - " + requiredDateTo;
	}
	
	public String getRequiredDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat stf = new SimpleDateFormat("k:mm");
		try {
			String requiredDateFrom = sdf.format(getStartDate());
			String requiredDateTo = sdf.format(getEndDate());
			
			String timeStart = stf.format(getStartDate());
	        String timeEnd = stf.format(getEndDate());
	        
	        return requiredDateFrom + " [" + timeStart + "] - <br/>" + requiredDateTo + " [" + timeEnd +"]";
		} catch (Exception e) {
			return "-";
		}
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getRateVehicle() {
		return rateVehicle;
	}

	public void setRateVehicle(String rateVehicle) {
		this.rateVehicle = rateVehicle;
	}
	
}
