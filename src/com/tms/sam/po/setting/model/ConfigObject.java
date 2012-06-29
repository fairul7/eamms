package com.tms.sam.po.setting.model;

import java.util.Date;

import kacang.model.DefaultDataObject;

public class ConfigObject extends DefaultDataObject{
	public static final String FILE_SIZE_UPLOAD = "File Size Upload";
	public static final String PRIORITY = "Priority";
	public static final String ALLOWED_FILE_EXTENSION = "Allowed Attachment File Extension";
	public static final String COMPANY_NAME = "Company Name";
	public static final String COMPANY_ADDRESS = "Company Address";
	public static final String COMPANY_TEL = "Company Tel";
	public static final String COMPANY_FAX = "Company Fax";
	public static final String COMPANY_LOGO = "Company Logo";
	    
	private String configDetailId = "";
    private String configDetailName = "";
    private String configDetailDescription = "";
    private String configDetailType = "";
    private Number configDetailOrder = new Integer(1);
    private Date dateCreated;
    private String createdBy = "";
    private Date lastUpdatedDate;
    private String lastUpdatedBy = "";
	
	public String getConfigDetailDescription() {
		return configDetailDescription;
	}
	public void setConfigDetailDescription(String configDetailDescription) {
		this.configDetailDescription = configDetailDescription;
	}
	public String getConfigDetailId() {
		return configDetailId;
	}
	public void setConfigDetailId(String configDetailId) {
		this.configDetailId = configDetailId;
	}
	public String getConfigDetailName() {
		return configDetailName;
	}
	public void setConfigDetailName(String configDetailName) {
		this.configDetailName = configDetailName;
	}
	public Number getConfigDetailOrder() {
		return configDetailOrder;
	}
	public void setConfigDetailOrder(Number configDetailOrder) {
		this.configDetailOrder = configDetailOrder;
	}
	public String getConfigDetailType() {
		return configDetailType;
	}
	public void setConfigDetailType(String configDetailType) {
		this.configDetailType = configDetailType;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	
	
}
