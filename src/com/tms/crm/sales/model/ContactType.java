/*
 * Created on Feb 26, 2004
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
public class ContactType extends DefaultDataObject {
	private String  contactTypeID;
	private String  contactTypeName;
	private boolean isArchived;
	
	
	public String getContactTypeID() {
		return contactTypeID;
	}
	
	public String getContactTypeName() {
		return contactTypeName;
	}
	
	public String getIsArchived() {
		if (isArchived) {
			return "1";
		} else {
			return "0";
		}
	}
	
	public void setContactTypeID(String string) {
		contactTypeID = string;
	}
	
	public void setContactTypeName(String string) {
		contactTypeName = string;
		if (contactTypeName != null) {
			contactTypeName = contactTypeName.trim();
		}
	}
	
	public void setIsArchived(String string) {
		if (string.equals("1")) {
			isArchived = true;
		} else {
			isArchived = false;
		}
	}
	
	public String toString() {
		return(
			"<Sales ContactType" +
			" contactTypeID=\"" + getContactTypeID() + "\"" +
			" contactTypeName=\"" + getContactTypeName() + "\"" +
			" isArchived=\"" + getIsArchived() + "\"" +
			" />"
		);
	}
}