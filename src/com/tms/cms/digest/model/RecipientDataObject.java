package com.tms.cms.digest.model;

import java.util.Collection;

import kacang.model.DefaultDataObject;

public class RecipientDataObject extends DefaultDataObject {

    private String recipientId;
    private String recipientName;
    private String userRecipientId;
    private String userName;
    private Collection user;
    
	public String getRecipientId() {
		return recipientId;
	}
	public void setRecipientId(String recipientId) {
		this.recipientId = recipientId;
	}
	public String getRecipientName() {
		return recipientName;
	}
	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}
	public String getUserName() {
		String name = "";
        String firstName = (String) getProperty("firstname");
        String lastName = (String) getProperty("lastname");
        if (firstName != null) name += firstName + " ";
        if (lastName != null) name += lastName;
        return name;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserRecipientId() {
		return userRecipientId;
	}
	public void setUserRecipientId(String userRecipientId) {
		this.userRecipientId = userRecipientId;
	}
	public Collection getUser() {
		return user;
	}
	public void setUser(Collection user) {
		this.user = user;
	}       

}
