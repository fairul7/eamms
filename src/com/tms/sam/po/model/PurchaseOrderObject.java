package com.tms.sam.po.model;

import java.util.Date;

import kacang.model.DefaultDataObject;

public class PurchaseOrderObject extends DefaultDataObject{
	private String poID;
	private String ppID;
	private String supplierID;
	private int count;
	private String deliveryOrderNo;
	private String invoiceNo;
	private String poCode;
	private Date invoiceDate;
	private int terms;
	private Date dateDelivered;
	private Date dateChecked;
	private Date dateCreated;
	private String createdBy;
	private String referenceNo;
	private String beneficiary;
	private String typeOfPayment;
	private Date datePaid;
	private double amount;
	private boolean paid;
	private Date lastUpdatedDate;
	private String lastUpdatedBy;
	
	public Date getDateChecked() {
		return dateChecked;
	}
	public void setDateChecked(Date dateChecked) {
		this.dateChecked = dateChecked;
	}
	public Date getDateDelivered() {
		return dateDelivered;
	}
	public void setDateDelivered(Date dateDelivered) {
		this.dateDelivered = dateDelivered;
	}
	public String getDeliveryOrderNo() {
		return deliveryOrderNo;
	}
	public void setDeliveryOrderNo(String deliveryOrderNo) {
		this.deliveryOrderNo = deliveryOrderNo;
	}
	public Date getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	public String getPoID() {
		return poID;
	}
	public void setPoID(String poID) {
		this.poID = poID;
	}
	public String getPpID() {
		return ppID;
	}
	public void setPpID(String ppID) {
		this.ppID = ppID;
	}
	public int getTerms() {
		return terms;
	}
	public void setTerms(int terms) {
		this.terms = terms;
	}
	public String getPoCode() {
		return poCode;
	}
	public void setPoCode(String poCode) {
		this.poCode = poCode;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getSupplierID() {
		return supplierID;
	}
	public void setSupplierID(String supplierID) {
		this.supplierID = supplierID;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getBeneficiary() {
		return beneficiary;
	}
	public void setBeneficiary(String beneficiary) {
		this.beneficiary = beneficiary;
	}
	public Date getDatePaid() {
		return datePaid;
	}
	public void setDatePaid(Date datePaid) {
		this.datePaid = datePaid;
	}
	public String getReferenceNo() {
		return referenceNo;
	}
	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}
	public String getTypeOfPayment() {
		return typeOfPayment;
	}
	public void setTypeOfPayment(String typeOfPayment) {
		this.typeOfPayment = typeOfPayment;
	}
	public boolean isPaid() {
		return paid;
	}
	public void setPaid(boolean paid) {
		this.paid = paid;
	}
	
}
