package com.tms.assetmanagement.model;

import kacang.model.DefaultDataObject;

public class DataMonthlyDepReport extends DefaultDataObject  {
	private String monthlyDepId;
	private String itemId; 
	private String disposalId; 
	private String categoryId; 
	private float assetUnit;
	private java.util.Date assetDate; 
	private int assetYear;
	private String endingMonth;
	private double totalCost;
	private double jan; 
	private double feb;
	private double mar;
	private double apr;
	private double may;
	private double jun;
	private double jul;
	private double aug;
	private double sept;
	private double oct;
	private double nov;
	private double dece;
	private double totalDepreciation;
	
	//-------------- Setters and Getters ------------------
	
	public double getApr() {
		return apr;
	}
	public void setApr(double apr) {
		this.apr = apr;
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
	public double getAug() {
		return aug;
	}
	public void setAug(double aug) {
		this.aug = aug;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public double getDece() {
		return dece;
	}
	public void setDece(double dece) {
		this.dece = dece;
	}
	public String getDisposalId() {
		return disposalId;
	}
	public void setDisposalId(String disposalId) {
		this.disposalId = disposalId;
	}
	public double getFeb() {
		return feb;
	}
	public void setFeb(double feb) {
		this.feb = feb;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public double getJan() {
		return jan;
	}
	public void setJan(double jan) {
		this.jan = jan;
	}
	public double getJul() {
		return jul;
	}
	public void setJul(double jul) {
		this.jul = jul;
	}
	public double getJun() {
		return jun;
	}
	public void setJun(double jun) {
		this.jun = jun;
	}
	public double getMar() {
		return mar;
	}
	public void setMar(double mar) {
		this.mar = mar;
	}
	public double getMay() {
		return may;
	}
	public void setMay(double may) {
		this.may = may;
	}
	public String getMonthlyDepId() {
		return monthlyDepId;
	}
	public void setMonthlyDepId(String monthlyDepId) {
		this.monthlyDepId = monthlyDepId;
	}
	public double getNov() {
		return nov;
	}
	public void setNov(double nov) {
		this.nov = nov;
	}
	public double getOct() {
		return oct;
	}
	public void setOct(double oct) {
		this.oct = oct;
	}
	public double getSept() {
		return sept;
	}
	public void setSept(double sept) {
		this.sept = sept;
	}
	public double getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}
	public double getTotalDepreciation() {
		return totalDepreciation;
	}
	public void setTotalDepreciation(double totalDepreciation) {
		this.totalDepreciation = totalDepreciation;
	}
	public String getEndingMonth() {
		return endingMonth;
	}
	public void setEndingMonth(String endingMonth) {
		this.endingMonth = endingMonth;
	}

	
}
