/*
 * Created on Jan 27, 2004
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
public class OpportunityContact extends DefaultDataObject {
	private String opportunityID;
	private String opportunityContactType;
	private String contactID;
	private String contactTypeID;
	private String contactLastName;		// not in database
	private String contactFirstName;	// not in database
	
	public static final String DEFAULT = "0";
	public static final String COMPANY_CONTACT = "C";
	public static final String PARTNER_CONTACT = "P";
	
	
	public String getContactID() {
		return contactID;
	}
	
	public String getContactLastName() {
		return contactLastName;
	}
	
	public String getContactFirstName() {
		return contactFirstName;
	}
	
	public String getContactTypeID() {
		return contactTypeID;
	}
	
	public String getOpportunityID() {
		return opportunityID;
	}
	
	public String getOpportunityContactType() {
		return opportunityContactType;
	}
	
	public void setContactID(String string) {
		contactID = string;
	}
	
	public void setContactLastName(String string) {
		contactLastName = string;
	}
	
	public void setContactFirstName(String string) {
		contactFirstName = string;
	}
	
	public void setContactTypeID(String string) {
		contactTypeID = string;
	}
	
	public void setOpportunityID(String string) {
		opportunityID = string;
	}
	
	public void setOpportunityContactType(String string) {
		opportunityContactType = string;
	}
	
	public String toString() {
		return(
			"<Sales OpportunityContact" +
			" opportunityID=\"" + getOpportunityID() + "\"" +
			" opportunityContactType=\"" + getOpportunityContactType() + "\"" +
			" contactID=\"" + getContactID() + "\"" +
			" contactTypeID=\"" + getContactTypeID() + "\"" +
			" />"
		);
	}
}
