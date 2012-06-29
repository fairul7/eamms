package com.tms.fms.abw.model;

import java.util.Date;

import kacang.model.DefaultDataObject;

public class TransferCostCancellationObject extends DefaultDataObject {
	private String uniqueId;
	private String projectCode;
	private String requestId;
	private String ratecardAbwCode;
	private int noOfUnit;
	private Date requiredDateFrom;
	private Date requiredDateTo;
	private double cost;
	private String blockBooking;
	private String type;
	private Date createdDate;
	private String createdBy;
	private Date updatedDate;
	private String updatedBy;
	private String status;
	private String cancellation_ind;
	private String cancellation_remark;
	private String cancellation_penalty;
	
	public String getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getRatecardAbwCode() {
		return ratecardAbwCode;
	}
	public void setRatecardAbwCode(String ratecardAbwCode) {
		this.ratecardAbwCode = ratecardAbwCode;
	}
	public int getNoOfUnit() {
		return noOfUnit;
	}
	public void setNoOfUnit(int noOfUnit) {
		this.noOfUnit = noOfUnit;
	}
	public Date getRequiredDateFrom() {
		return requiredDateFrom;
	}
	public void setRequiredDateFrom(Date requiredDateFrom) {
		this.requiredDateFrom = requiredDateFrom;
	}
	public Date getRequiredDateTo() {
		return requiredDateTo;
	}
	public void setRequiredDateTo(Date requiredDateTo) {
		this.requiredDateTo = requiredDateTo;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public String getBlockBooking() {
		return blockBooking;
	}
	public void setBlockBooking(String blockBooking) {
		this.blockBooking = blockBooking;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCancellation_ind() {
		return cancellation_ind;
	}
	public void setCancellation_ind(String cancellation_ind) {
		this.cancellation_ind = cancellation_ind;
	}
	public String getCancellation_remark() {
		return cancellation_remark;
	}
	public void setCancellation_remark(String cancellation_remark) {
		this.cancellation_remark = cancellation_remark;
	}
	public String getCancellation_penalty() {
		return cancellation_penalty;
	}
	public void setCancellation_penalty(String cancellation_penalty) {
		this.cancellation_penalty = cancellation_penalty;
	}
}
