package com.tms.sam.po.model;

import kacang.model.DefaultDataObject;

public class BudgetObject extends DefaultDataObject {
	private String budgetID;
	private String ppID;
	private String supplierID;
	private String remarks;
	private String dateApproved;
	private String approvedBy;
	private String action;
	private int count;
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	// === [ getters/setters ] =================================================
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}
	public String getBudgetID() {
		return budgetID;
	}
	public void setBudgetID(String budgetID) {
		this.budgetID = budgetID;
	}
	public String getDateApproved() {
		return dateApproved;
	}
	public void setDateApproved(String dateApproved) {
		this.dateApproved = dateApproved;
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
	public String getSupplierID() {
		return supplierID;
	}
	public void setSupplierID(String supplierID) {
		this.supplierID = supplierID;
	}
	
}
