package com.tms.cms.digest.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import kacang.model.DefaultDataObject;

public class DigestDataObject extends DefaultDataObject {

    private String digestIssueId;
    private String digestId;
    private String digestName;
    private String digest;
    private String lastEditBy;
    private String lastEditByUser;
    private Collection contents;
    private int numOfContents;
    private Date lastEditDate;
    private Date dateCreate;
    private String ordering;
    
    public void init(){
    	contents=new ArrayList();
    }
	public Date getDateCreate() {
		return dateCreate;
	}
	public void setDateCreate(Date dateCreate) {
		this.dateCreate = dateCreate;
	}
	public String getDigest() {
		return digest;
	}
	public void setDigest(String digest) {
		this.digest = digest;
	}
	public String getDigestId() {
		return digestId;
	}
	public void setDigestId(String digestId) {
		this.digestId = digestId;
	}
	public String getDigestIssueId() {
		return digestIssueId;
	}
	public void setDigestIssueId(String digestIssueId) {
		this.digestIssueId = digestIssueId;
	}
	public String getDigestName() {
		return digestName;
	}
	public void setDigestName(String digestName) {
		this.digestName = digestName;
	}
	public String getLastEditBy() {
		return lastEditBy;
	}
	public void setLastEditBy(String lastEditBy) {
		this.lastEditBy = lastEditBy;
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
	public Date getLastEditDate() {
		return lastEditDate;
	}
	public void setLastEditDate(Date lastEditDate) {
		this.lastEditDate = lastEditDate;
	}
	public int getNumOfContents() {
		return numOfContents;
	}
	public void setNumOfContents(int numOfContents) {
		this.numOfContents = numOfContents;
	}
	public Collection getContents() {
		return contents;
	}
	public void setContents(Collection contents) {
		this.contents = contents;
	}
	public String getOrdering() {
		return ordering;
	}
	public void setOrdering(String ordering) {
		this.ordering = ordering;
	}
    
    
}
