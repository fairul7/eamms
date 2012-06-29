/*
 * Created on Dec 18, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.model;

import kacang.model.*;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Contact extends DefaultDataObject {
	private String contactID;
	private String companyID;
	private String contactLastName;
	private String contactFirstName;
	private String contactDesignation;
	private String salutationID;
	private String contactStreet1;
	private String contactStreet2;
	private String contactCity;
	private String contactState;
	private String contactPostcode;
	private String contactCountry;
	private String contactDirectNum;
	private String contactMobile;
	private String contactEmail;
	private String contactRemarks;
	
	
	/* Special -  start */
	public String getEmailLink() {
		String emailLink = "";
		
		if (contactEmail != null && !contactEmail.trim().equals("")) {
			emailLink = contactEmail.trim();
			emailLink = "<a href='mailto:" + emailLink + "'>" + emailLink + "</a>";
		}
		return emailLink;
	}
	/* Special -  end   */
	
	
	public String getCompanyID() {
		return companyID;
	}
	
	public String getContactCity() {
		return contactCity;
	}
	
	public String getContactCountry() {
		return contactCountry;
	}
	
	public String getContactDesignation() {
		return contactDesignation;
	}
	
	public String getContactDirectNum() {
		return contactDirectNum;
	}
	
	public String getContactEmail() {
		return contactEmail;
	}
	
	public String getContactID() {
		return contactID;
	}
	
	public String getContactMobile() {
		return contactMobile;
	}
	
	public String getContactLastName() {
		return contactLastName;
	}
	
	public String getContactFirstName() {
		return contactFirstName;
	}
	
	public String getContactPostcode() {
		return contactPostcode;
	}
	
	public String getContactRemarks() {
		return contactRemarks;
	}
	
	public String getSalutationID() {
		return salutationID;
	}
	
	public String getContactState() {
		return contactState;
	}
	
	public String getContactStreet1() {
		return contactStreet1;
	}
	
	public String getContactStreet2() {
		return contactStreet2;
	}
	
	public void setCompanyID(String string) {
		companyID = string;
	}
	
	public void setContactCity(String string) {
		contactCity = string;
	}
	
	public void setContactCountry(String string) {
		contactCountry = string;
	}
	
	public void setContactDesignation(String string) {
		contactDesignation = string;
	}
	
	public void setContactDirectNum(String string) {
		contactDirectNum = string;
	}
	
	public void setContactEmail(String string) {
		contactEmail = string;
	}
	
	public void setContactID(String string) {
		contactID = string;
	}
	
	public void setContactMobile(String string) {
		contactMobile = string;
	}
	
	public void setContactLastName(String string) {
		contactLastName = string;
	}
	
	public void setContactFirstName(String string) {
		contactFirstName = string;
	}
	
	public void setContactPostcode(String string) {
		contactPostcode = string;
	}
	
	public void setContactRemarks(String string) {
		contactRemarks = string;
	}
	
	public void setSalutationID(String string) {
		salutationID = string;
	}
	
	public void setContactState(String string) {
		contactState = string;
	}
	
	public void setContactStreet1(String string) {
		contactStreet1 = string;
	}
	
	public void setContactStreet2(String string) {
		contactStreet2 = string;
	}
	
	public String toString() {
		return(
			"<Sales Contact" +
			" contactID=\"" + getContactID() + "\"" +
			" companyID=\"" + getCompanyID() + "\"" +
			" contactLastName=\"" + getContactLastName() + "\"" +
			" contactFirstName=\"" + getContactFirstName() + "\"" +
			" contactDesignation=\"" + getContactDesignation() + "\"" +
			" salutationID=\"" + getSalutationID() + "\"" +
			" contactStreet1=\"" + getContactStreet1() + "\"" +
			" contactStreet2=\"" + getContactStreet2() + "\"" +
			" contactCity=\"" + getContactCity() + "\"" +
			" contactState=\"" + getContactState() + "\"" +
			" contactPostcode=\"" + getContactPostcode() + "\"" +
			" contactCountry=\"" + getContactCountry() + "\"" +
			" contactDirectNum=\"" + getContactDirectNum() + "\"" +
			" contactMobile=\"" + getContactMobile() + "\"" +
			" contactEmail=\"" + getContactEmail() + "\"" +
			" contactRemarks=\"" + getContactRemarks() + "\"" +
			" />"
		);
	}
}
