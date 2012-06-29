package com.tms.sam.po.model;

import java.util.Date;

import kacang.model.DefaultDataObject;

public class PurchaseItemObject extends DefaultDataObject {
	
	private String itemID;
	private String ppID;
	private String userID;
	private String itemCode;
	private String suggestedVendor;
	private double qty;
	private String itemDesc;
	private String unitOfMeasure;
	private Date dateCreated;
	
	// === [ getters/setters ] =================================================
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getItemID() {
		return itemID;
	}
	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getPpID() {
		return ppID;
	}
	public void setPpID(String ppID) {
		this.ppID = ppID;
	}
	public String getSuggestedVendor() {
		return suggestedVendor;
	}
	public void setSuggestedVendor(String suggestedVendor) {
		this.suggestedVendor = suggestedVendor;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public double getQty() {
		return qty;
	}
	public void setQty(double qty) {
		this.qty = qty;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}
	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}
	
}
