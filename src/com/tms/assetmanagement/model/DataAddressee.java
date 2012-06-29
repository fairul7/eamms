package com.tms.assetmanagement.model;

import kacang.model.DefaultDataObject;

public class DataAddressee  extends DefaultDataObject   {
	
	private String addresseeId;
	private String notificationId;
	private String recipientId;
	
	public String getAddresseeId() {
		return addresseeId;
	}
	public void setAddresseeId(String addresseeId) {
		this.addresseeId = addresseeId;
	}
	public String getNotificationId() {
		return notificationId;
	}
	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}
	public String getRecipientId() {
		return recipientId;
	}
	public void setRecipientId(String recipientId) {
		this.recipientId = recipientId;
	}
	
}
