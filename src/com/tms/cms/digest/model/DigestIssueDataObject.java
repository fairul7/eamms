package com.tms.cms.digest.model;

import java.util.Date;

import kacang.model.DefaultDataObject;

public class DigestIssueDataObject extends DefaultDataObject {

    private String digestIssueId;
    private String digestIssueName;
    private String digestIssue;
    private String lastEditBy;
    private String lastEditByUser;
    private int numOfDigest;
    private Date lastEditDate;
    private Date dateCreate;
    
	public Date getDateCreate() {
		return dateCreate;
	}
	public void setDateCreate(Date dateCreate) {
		this.dateCreate = dateCreate;
	}
	public String getDigestIssueId() {
		return digestIssueId;
	}
	public void setDigestIssueId(String digestIssueId) {
		this.digestIssueId = digestIssueId;
	}
	public String getDigestIssueName() {
		return digestIssueName;
	}
	public void setDigestIssueName(String digestIssueName) {
		this.digestIssueName = digestIssueName;
	}
	public String getLastEditBy() {
		return lastEditBy;
	}
	public void setLastEditBy(String lastEditBy) {
		this.lastEditBy = lastEditBy;
	}
	public Date getLastEditDate() {
		return lastEditDate;
	}
	public void setLastEditDate(Date lastEditDate) {
		this.lastEditDate = lastEditDate;
	}
	public String getLastEditByUser() {
		String name = "";
        String firstName = (String) getProperty("firstname");
        String lastName = (String) getProperty("lastname");
        if (firstName != null) name += firstName + " ";
        if (lastName != null) name += lastName;
        return name;
	}
	public void setLastEditByUser(String lastEditByUser) {
		this.lastEditByUser = lastEditByUser;
	}
	public int getNumOfDigest() {
		return numOfDigest;
	}
	public void setNumOfDigest(int numOfDigest) {
		this.numOfDigest = numOfDigest;
	}
	public String getDigestIssue() {
		return digestIssue;
	}
	public void setDigestIssue(String digestIssue) {
		this.digestIssue = digestIssue;
	} 
}
