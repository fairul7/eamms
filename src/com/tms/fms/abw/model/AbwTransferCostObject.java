package com.tms.fms.abw.model;

import java.util.Date;

import kacang.model.DefaultDataObject;

public class AbwTransferCostObject extends DefaultDataObject{

	private String uniqueId;
	private String projectCode;
	private String requestId;
	private String facilityCode;
	private int quantity;
	private Date requiredDateFrom;
	private Date requiredDateTo;
	private String cost;
	private String type;
	private Date createdDate;
	private String createdBy;
	private Date updatedDate;
	private String updatedBy;
	private String status;
	private String categoryId;
	
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
	public String getFacilityCode() {
		return facilityCode;
	}
	public void setFacilityCode(String facilityCode) {
		this.facilityCode = facilityCode;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
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
	public String getCost() {
		return cost;
	}
	public void setCost(String cost) {
		this.cost = cost;
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
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	
	
}
