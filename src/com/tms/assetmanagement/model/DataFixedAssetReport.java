package com.tms.assetmanagement.model;

import kacang.model.DefaultDataObject;

public class DataFixedAssetReport extends DefaultDataObject   {
	
	private String fixedAssetId;
	private String itemId;
	private String categoryId;
	private String monthlyDepId;
	private float assetUnit;
	private java.util.Date assetDate;
	private int assetYear;
	private String endingMonth;
	private double costBalBf;
	private double costAdditon;
	private double costDisposal; 
	private double costBalCf;
	private double accumDepnBalBf;
	private double accumDepnDCharge; 
	private double accumDepnDisposal;
	private double accumDepnBalCf; 
	private double nbv;
	
	//---------- Setters/Getters ------------
	
	public double getAccumDepnBalBf() {
		return accumDepnBalBf;
	}
	public void setAccumDepnBalBf(double accumDepnBalBf) {
		this.accumDepnBalBf = accumDepnBalBf;
	}
	public double getAccumDepnBalCf() {
		return accumDepnBalCf;
	}
	public void setAccumDepnBalCf(double accumDepnBalCf) {
		this.accumDepnBalCf = accumDepnBalCf;
	}
	public double getAccumDepnDCharge() {
		return accumDepnDCharge;
	}
	public void setAccumDepnDCharge(double accumDepnDCharge) {
		this.accumDepnDCharge = accumDepnDCharge;
	}
	public double getAccumDepnDisposal() {
		return accumDepnDisposal;
	}
	public void setAccumDepnDisposal(double accumDepnDisposal) {
		this.accumDepnDisposal = accumDepnDisposal;
	}
	public java.util.Date getAssetDate() {
		return assetDate;
	}
	public void setAssetDate(java.util.Date assetDate) {
		this.assetDate = assetDate;
	}
	public float getAssetUnit() {
		return assetUnit;
	}
	public void setAssetUnit(float assetUnit) {
		this.assetUnit = assetUnit;
	}
	public int getAssetYear() {
		return assetYear;
	}
	public void setAssetYear(int assetYear) {
		this.assetYear = assetYear;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public double getCostAdditon() {
		return costAdditon;
	}
	public void setCostAdditon(double costAdditon) {
		this.costAdditon = costAdditon;
	}
	public double getCostBalBf() {
		return costBalBf;
	}
	public void setCostBalBf(double costBalBf) {
		this.costBalBf = costBalBf;
	}
	public double getCostBalCf() {
		return costBalCf;
	}
	public void setCostBalCf(double costBalCf) {
		this.costBalCf = costBalCf;
	}
	public double getCostDisposal() {
		return costDisposal;
	}
	public void setCostDisposal(double costDisposal) {
		this.costDisposal = costDisposal;
	}
	public String getFixedAssetId() {
		return fixedAssetId;
	}
	public void setFixedAssetId(String fixedAssetId) {
		this.fixedAssetId = fixedAssetId;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getMonthlyDepId() {
		return monthlyDepId;
	}
	public void setMonthlyDepId(String monthlyDepId) {
		this.monthlyDepId = monthlyDepId;
	}
	public double getNbv() {
		return nbv;
	}
	public void setNbv(double nbv) {
		this.nbv = nbv;
	}
	public String getEndingMonth() {
		return endingMonth;
	}
	public void setEndingMonth(String endingMonth) {
		this.endingMonth = endingMonth;
	}
	
}
