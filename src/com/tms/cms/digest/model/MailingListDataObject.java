package com.tms.cms.digest.model;

import java.util.Collection;
import java.util.Date;

import kacang.Application;
import kacang.model.DefaultDataObject;
import kacang.services.security.User;

public class MailingListDataObject extends DefaultDataObject {
	
    private String mailingListId;
    private String mailingListName;
    private String digestIssue;
    private String digestIssueName;   
    private String mailFormat;
    private String emailFormat;
    private String emailFormatType;
    private Collection recipients;
    private Collection digest;
    private int digestSize;
    private String lastEditBy;
    private String lastEditByUser;
    private User user;
    private Date lastEditDate;
    private Date dateCreate;
    private Date lastSendDate;
    private String reportId;
    private String createdBy;
       
	public Date getDateCreate() {
		return dateCreate;
	}
	public void setDateCreate(Date dateCreate) {
		this.dateCreate = dateCreate;
	}
	public String getDigestIssue() {
		return digestIssue;
	}
	public void setDigestIssue(String digestIssue) {
		this.digestIssue = digestIssue;
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
	public String getMailFormat() {
		return mailFormat;
	}
	public void setMailFormat(String mailFormat) {
		this.mailFormat = mailFormat;
	}
	public String getMailingListId() {
		return mailingListId;
	}
	public void setMailingListId(String mailingListId) {
		this.mailingListId = mailingListId;
	}
	public String getMailingListName() {
		return mailingListName;
	}
	public void setMailingListName(String mailingListName) {
		this.mailingListName = mailingListName;
	}
	public Collection getRecipients() {
		return recipients;
	}
	public void setRecipients(Collection recipients) {
		this.recipients = recipients;
	}
	public String getDigestIssueName() {
		return digestIssueName;
	}
	public void setDigestIssueName(String digestIssueName) {
		this.digestIssueName = digestIssueName;
	}
	public String getEmailFormat() {
		return Application.getInstance().getMessage("digest.label."+mailFormat);
	}
	public void setEmailFormat(String emailFormat) {
		this.emailFormat = emailFormat;
	}
	public Collection getDigest() {
		return digest;
	}
	public void setDigest(Collection digest) {
		this.digest = digest;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getEmailFormatType() {
		return emailFormatType;
	}
	public void setEmailFormatType(String emailFormatType) {
		this.emailFormatType = emailFormatType;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public int getDigestSize() {
		return digestSize;
	}
	public void setDigestSize(int digestSize) {
		this.digestSize = digestSize;
	}
	public Date getLastSendDate() {
		return lastSendDate;
	}
	public void setLastSendDate(Date lastSendDate) {
		this.lastSendDate = lastSendDate;
	}

}
