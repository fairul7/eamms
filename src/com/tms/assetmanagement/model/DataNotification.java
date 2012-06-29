package com.tms.assetmanagement.model;

import java.util.Date;

import kacang.model.DefaultDataObject;

public class DataNotification  extends DefaultDataObject {
	
	  private String notificationId;
	  private String notificationTitle;
	  private java.util.Date notificationDate;
	  private java.util.Date notificationTime;
	  private String notificationMsg;
	  private String notifyMethod;
	  private String senderID;
	  private Date dateCreated;
	  
	  

	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public java.util.Date getNotificationTime() {
		return notificationTime;
	}
	public void setNotificationTime(java.util.Date notificationTime) {
		this.notificationTime = notificationTime;
	}
	public java.util.Date getNotificationDate() {
		return notificationDate;
	}
	public void setNotificationDate(java.util.Date notificationDate) {
		this.notificationDate = notificationDate;
	}
	public String getNotificationId() {
		return notificationId;
	}
	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}
	public String getNotificationMsg() {
		return notificationMsg;
	}
	public void setNotificationMsg(String notificationMsg) {
		this.notificationMsg = notificationMsg;
	}
	public String getNotificationTitle() {
		return notificationTitle;
	}
	public void setNotificationTitle(String notificationTitle) {
		this.notificationTitle = notificationTitle;
	}
	
	public String getNotifyMethod() {
		return notifyMethod;
	}
	public void setNotifyMethod(String notifyMethod) {
		this.notifyMethod = notifyMethod;
	}
	public String getSenderID() {
		return senderID;
	}
	public void setSenderID(String senderID) {
		this.senderID = senderID;
	}

	  
	  
}
