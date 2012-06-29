package com.tms.sam.po.model;

import java.util.Collection;
import java.util.Date;

import kacang.model.DefaultDataObject;

public class SupplierObject extends DefaultDataObject {
	private double totalQuotation;
	private int counting = 0;
	
	private String supplierID;
	private String lastKnownSuppName;
	private String lastKnownCompany;
	private String lastKnownTelephone;
	private String ppID;
	private String responded;
	private String recommended;
	private String lastUpdateBy;
	private String quotationDetails;
	private String quotationID;
	private String approved;
	private String item;
	private String currencyUsed;
	private String currency;
	private Collection itemID;
	private Date dateSent;
	private Date dateReceived;
	private Date dateDelivery;
	private Date lastUpdateDate;
	private Collection attachmentObject;
	private String id;
	private int totalRating;
	
	
	// === [ getters/setters ] =================================================
	public String getApproved() {
		return approved;
	}
	public int getTotalRating() {
		return totalRating;
	}
	public void setTotalRating(int totalRating) {
		this.totalRating = totalRating;
	}
	public void setApproved(String approved) {
		this.approved = approved;
	}
	public Collection getAttachmentObject() {
		return attachmentObject;
	}
	public void setAttachmentObject(Collection attachmentObject) {
		this.attachmentObject = attachmentObject;
	}
	public Date getDateReceived() {
		return dateReceived;
	}
	public void setDateReceived(Date dateReceived) {
		this.dateReceived = dateReceived;
	}
	public Date getDateSent() {
		return dateSent;
	}
	public void setDateSent(Date dateSent) {
		this.dateSent = dateSent;
	}
	public String getLastKnownCompany() {
		return lastKnownCompany;
	}
	public void setLastKnownCompany(String lastKnownCompany) {
		this.lastKnownCompany = lastKnownCompany;
	}
	public String getLastKnownSuppName() {
		return lastKnownSuppName;
	}
	public void setLastKnownSuppName(String lastKnownSuppName) {
		this.lastKnownSuppName = lastKnownSuppName;
	}
	public String getLastKnownTelephone() {
		return lastKnownTelephone;
	}
	public void setLastKnownTelephone(String lastKnownTelephone) {
		this.lastKnownTelephone = lastKnownTelephone;
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
	public String getPpID() {
		return ppID;
	}
	public void setPpID(String ppID) {
		this.ppID = ppID;
	}
	public String getRecommended() {
		return recommended;
	}
	public void setRecommended(String recommended) {
		this.recommended = recommended;
	}
	public String getResponded() {
		return responded;
	}
	public void setResponded(String responded) {
		this.responded = responded;
	}
	public String getSupplierID() {
		return supplierID;
	}
	public void setSupplierID(String supplierID) {
		this.supplierID = supplierID;
	}
	public String getQuotationDetails() {
		return quotationDetails;
	}
	public void setQuotationDetails(String quotationDetails) {
		this.quotationDetails = quotationDetails;
	}
	public String getQuotationID() {
		return quotationID;
	}
	public void setQuotationID(String quotationID) {
		this.quotationID = quotationID;
	}
	public Collection getItemID() {
		return itemID;
	}
	public void setItemID(Collection itemID) {
		this.itemID = itemID;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public int getCounting() {
		return counting;
	}
	public void setCounting(int counting) {
		this.counting = counting;
	}
	public String getCurrencyUsed() {
		return currencyUsed;
	}
	public void setCurrencyUsed(String currencyUsed) {
		this.currencyUsed = currencyUsed;
	}
	public double getTotalQuotation() {
		return totalQuotation;
	}
	public void setTotalQuotation(double totalQuotation) {
		this.totalQuotation = totalQuotation;
	}
	public Date getDateDelivery() {
		return dateDelivery;
	}
	public void setDateDelivery(Date dateDelivery) {
		this.dateDelivery = dateDelivery;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
