package com.tms.fms.engineering.model;

import java.util.Date;

import kacang.Application;
import kacang.model.DefaultDataObject;

public class Service extends DefaultDataObject {
	protected String title;
	protected String serviceId;
	protected String requestId;
	protected String id;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private String feedType;
	protected String additionalInfo;
	protected String internalRate;
	protected String externalRate;
	protected String cancelFlag;
	
	public String getCancelFlag() {
		return cancelFlag;
	}
	public void setCancelFlag(String cancelFlag) {
		this.cancelFlag = cancelFlag;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDisplayTitle() {
		return Application.getInstance().getMessage(title);
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getFeedType() {
		return feedType;
	}
	public void setFeedType(String feedType) {
		this.feedType = feedType;
	}
	public String getAdditionalInfo() {
		return additionalInfo;
	}
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	public String getInternalRate() {
		return internalRate;
	}
	public void setInternalRate(String internalRate) {
		this.internalRate = internalRate;
	}
	public String getExternalRate() {
		return externalRate;
	}
	public void setExternalRate(String externalRate) {
		this.externalRate = externalRate;
	}
	
}
