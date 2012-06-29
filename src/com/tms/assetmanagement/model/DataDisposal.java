package com.tms.assetmanagement.model;

import kacang.model.DefaultDataObject;

public class DataDisposal extends DefaultDataObject {
	
	private String disposalId;
	private String itemId;
	private float disposalQty;
	private double disposalCost;
	private java.util.Date dateDisposal;
	private String disposalReason;	
	
	//	-------getters/setters--------

	public java.util.Date getDateDisposal() {
		return dateDisposal;
	}
	public void setDateDisposal(java.util.Date dateDisposal) {
		this.dateDisposal = dateDisposal;
	}
	public double getDisposalCost() {
		return disposalCost;
	}
	public void setDisposalCost(double disposalCost) {
		this.disposalCost = disposalCost;
	}
	public String getDisposalId() {
		return disposalId;
	}
	public void setDisposalId(String disposalId) {
		this.disposalId = disposalId;
	}
	public float getDisposalQty() {
		return disposalQty;
	}
	public void setDisposalQty(float disposalQty) {
		this.disposalQty = disposalQty;
	}
	public String getDisposalReason() {
		return disposalReason;
	}
	public void setDisposalReason(String disposalReason) {
		this.disposalReason = disposalReason;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
}
