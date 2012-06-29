package com.tms.sam.po.model;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import kacang.model.DefaultDataObject;

public class PurchaseRequestObject extends DefaultDataObject {
	private String ppID;	
	private String poID;
	private String poCode;
	private String purchaseCode;
	private String requesterUserID;
	private String requester;
	private String requesterDepartmentID;
	private String department;
	private String status;
	private String suggestedVendor;
	private String reason;
	private String lastUpdatedBy;
	private String delimitedItems;
	private int rank;
	private String priority;
	private String combinedData;
	private Date neededBy;
	private Date lastUpdatedDate;
	private Date dateCreated;
	private Collection attachmentObject;
	private Collection purchaseItemsObject;
	private List storageFileList;
	
	// === [ getters/setters ] =================================================
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public String getPpID() {
		return ppID;
	}
	public void setPpID(String ppID) {
		this.ppID = ppID;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getRequesterDepartmentID() {
		return requesterDepartmentID;
	}
	public void setRequesterDepartmentID(String requesterDepartmentID) {
		this.requesterDepartmentID = requesterDepartmentID;
	}
	public String getRequesterUserID() {
		return requesterUserID;
	}
	public void setRequesterUserID(String requesterUserID) {
		this.requesterUserID = requesterUserID;
	}
	public String getSuggestedVendor() {
		return suggestedVendor;
	}
	public void setSuggestedVendor(String suggestedVendor) {
		this.suggestedVendor = suggestedVendor;
	}
	public Date getNeededBy() {
		return neededBy;
	}
	public void setNeededBy(Date neededBy) {
		this.neededBy = neededBy;
	}
	public Collection getAttachmentObject() {
		return attachmentObject;
	}
	public void setAttachmentObject(Collection attachmentObject) {
		this.attachmentObject = attachmentObject;
	}
	public Collection getPurchaseItemsObject() {
		return purchaseItemsObject;
	}
	public void setPurchaseItemsObject(Collection purchaseItemsObject) {
		this.purchaseItemsObject = purchaseItemsObject;
	}
	public String getPurchaseCode() {
		return purchaseCode;
	}
	public void setPurchaseCode(String purchaseCode) {
		this.purchaseCode = purchaseCode;
	}
	public String getDelimitedItems() {
		return delimitedItems;
	}
	public void setDelimitedItems(String delimitedItems) {
		this.delimitedItems = delimitedItems;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getRequester() {
		return requester;
	}
	public void setRequester(String requester) {
		this.requester = requester;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getPoCode() {
		return poCode;
	}
	public void setPoCode(String poCode) {
		this.poCode = poCode;
	}
	public String getPoID() {
		return poID;
	}
	public void setPoID(String poID) {
		this.poID = poID;
	}
	public String getCombinedData() {
		return combinedData;
	}
	public void setCombinedData(String combinedData) {
		this.combinedData = combinedData;
	}
	public List getStorageFileList() {
		return storageFileList;
	}
	public void setStorageFileList(List storageFileList) {
		this.storageFileList = storageFileList;
	}
	
}
