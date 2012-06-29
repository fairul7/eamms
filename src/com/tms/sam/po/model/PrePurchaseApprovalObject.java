package com.tms.sam.po.model;

import java.util.Date;

import kacang.model.DefaultDataObject;

public class PrePurchaseApprovalObject extends DefaultDataObject {
	private String ppaID;
	private String ppID;
	private String remarks;
	private String checkedBy;
	private boolean approved;
	private Date dateChecked;
	
	// === [ getters/setters ] =================================================
	public boolean isApproved() {
		return approved;
	}
	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	public String getCheckedBy() {
		return checkedBy;
	}
	public void setCheckedBy(String checkedBy) {
		this.checkedBy = checkedBy;
	}
	public Date getDateChecked() {
		return dateChecked;
	}
	public void setDateChecked(Date dateChecked) {
		this.dateChecked = dateChecked;
	}
	public String getPpaID() {
		return ppaID;
	}
	public void setPpaID(String ppaID) {
		this.ppaID = ppaID;
	}
	public String getPpID() {
		return ppID;
	}
	public void setPpID(String ppID) {
		this.ppID = ppID;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
	
}
