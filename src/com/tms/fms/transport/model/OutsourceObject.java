package com.tms.fms.transport.model;

import java.util.Date;
import kacang.model.DefaultDataObject;

public class OutsourceObject extends DefaultDataObject{
	
	
	private String id;
	private Date startDate;
	private Date endDate;
	private String requestId;
	private String[] setup_id;
	private String quotationNo;
	private double quotationPrice;
	private String invoiceNo;
	private double invoicePrice;
	private String remark;
	private String createdBy;
	private Date createdDate;
	private String updatedBy;
	private Date updatedDate;	
	private String requestTitle;
	private String requestType;
	 
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getQuotationNo() {
		return quotationNo;
	}
	public void setQuotationNo(String quotationNo) {
		this.quotationNo = quotationNo;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public double getQuotationPrice() {
		return quotationPrice;
	}
	public void setQuotationPrice(double quotationPrice) {
		this.quotationPrice = quotationPrice;
	}
	public double getInvoicePrice() {
		return invoicePrice;
	}
	public void setInvoicePrice(double invoicePrice) {
		this.invoicePrice = invoicePrice;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String[] getSetup_id() {
		return setup_id;
	}
	public void setSetup_id(String[] setup_id) {
		this.setup_id = setup_id;
	}
	public String getRequestTitle() {
		return requestTitle;
	}
	public void setRequestTitle(String requestTitle) {
		this.requestTitle = requestTitle;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	
	
	
	
	
}
