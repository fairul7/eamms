package com.tms.assetmanagement.model;

import kacang.model.DefaultDataObject;

public class DataMonthlyDepDisposal extends DefaultDataObject   {
	
	private String id;
	private String disposalId;
	private String monthlyDepId;
	private int  disposalYear;
	private int disposalMonth;
	private float disposalQty;
	private int assetYear;
	
	//----- setter and getter ----------------
	
	public int getAssetYear() {
		return assetYear;
	}
	public void setAssetYear(int assetYear) {
		this.assetYear = assetYear;
	}
	public String getDisposalId() {
		return disposalId;
	}
	public void setDisposalId(String disposalId) {
		this.disposalId = disposalId;
	}
	public int getDisposalMonth() {
		return disposalMonth;
	}
	public void setDisposalMonth(int disposalMonth) {
		this.disposalMonth = disposalMonth;
	}
	public float getDisposalQty() {
		return disposalQty;
	}
	public void setDisposalQty(float disposalQty) {
		this.disposalQty = disposalQty;
	}
	public int getDisposalYear() {
		return disposalYear;
	}
	public void setDisposalYear(int disposalYear) {
		this.disposalYear = disposalYear;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMonthlyDepId() {
		return monthlyDepId;
	}
	public void setMonthlyDepId(String monthlyDepId) {
		this.monthlyDepId = monthlyDepId;
	}
}
