package com.tms.sam.po.model;

import java.util.Date;

import kacang.model.DefaultDataObject;

public class AttachmentObject  extends DefaultDataObject  {
	private String attachmentID;
	private String ppID;
	private String userID;
	private String newFileName;
	private String path;
	private String supplierID;
	private Date dateCreated;
	private int counting;
	public int getCounting() {
		return counting;
	}
	public void setCounting(int counting) {
		this.counting = counting;
	}
	// === [ getters/setters ] =================================================
	public String getAttachmentID() {
		return attachmentID;
	}
	public void setAttachmentID(String attachmentID) {
		this.attachmentID = attachmentID;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getNewFileName() {
		return newFileName;
	}
	public void setNewFileName(String newFileName) {
		this.newFileName = newFileName;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getPpID() {
		return ppID;
	}
	public void setPpID(String ppID) {
		this.ppID = ppID;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getSupplierID() {
		return supplierID;
	}
	public void setSupplierID(String supplierID) {
		this.supplierID = supplierID;
	}
	
}