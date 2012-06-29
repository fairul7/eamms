package com.tms.sam.po.model;

import java.util.Date;

import kacang.model.DefaultDataObject;

public class RatingObject extends DefaultDataObject {
	private String supplierID;
	private String ratingID;
	private String lastUpdateBy;
	private String qualitySystem;
	private String concern;
	private String history;
	private String actual;
	private String negotiation;
	private String technical;
	private String delivery;
	private String assistance;
	private Date lastUpdateDate;
	private int totalRating;
	private String firstname;
	private String company;
	
    public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public int getTotalRating() {
		return totalRating;
	}
	public void setTotalRating(int totalRating) {
		this.totalRating = totalRating;
	}
	//getter and setter -------------------------
	public String getActual() {
		return actual;
	}
	public void setActual(String actual) {
		this.actual = actual;
	}
	public String getAssistance() {
		return assistance;
	}
	public void setAssistance(String assistance) {
		this.assistance = assistance;
	}
	public String getConcern() {
		return concern;
	}
	public void setConcern(String concern) {
		this.concern = concern;
	}
	public String getDelivery() {
		return delivery;
	}
	public void setDelivery(String delivery) {
		this.delivery = delivery;
	}
	public String getHistory() {
		return history;
	}
	public void setHistory(String history) {
		this.history = history;
	}
	public String getLastUpdateBy() {
		return lastUpdateBy;
	}
	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public String getNegotiation() {
		return negotiation;
	}
	public void setNegotiation(String negotiation) {
		this.negotiation = negotiation;
	}
	public String getQualitySystem() {
		return qualitySystem;
	}
	public void setQualitySystem(String qualitySystem) {
		this.qualitySystem = qualitySystem;
	}
	public String getRatingID() {
		return ratingID;
	}
	public void setRatingID(String ratingID) {
		this.ratingID = ratingID;
	}
	public String getSupplierID() {
		return supplierID;
	}
	public void setSupplierID(String supplierID) {
		this.supplierID = supplierID;
	}
	public String getTechnical() {
		return technical;
	}
	public void setTechnical(String technical) {
		this.technical = technical;
	}
}
