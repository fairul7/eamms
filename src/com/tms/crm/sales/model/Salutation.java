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
public class Salutation extends DefaultDataObject {
	private String  salutationID;
	private String  salutationText;
	private boolean isArchived;
	
	
	public String getSalutationID() {
		return salutationID;
	}
	
	public String getSalutationText() {
		return salutationText;
	}
	
	public String getIsArchived() {
		if (isArchived) {
			return "1";
		} else {
			return "0";
		}
	}
	
	public void setSalutationID(String string) {
		salutationID = string;
	}
	
	public void setSalutationText(String string) {
		salutationText = string;
		if (salutationText != null) {
			salutationText = salutationText.trim();
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
			"<Sales Salutation" +
			" salutationID=\"" + getSalutationID() + "\"" +
			" salutationText=\"" + getSalutationText() + "\"" +
			" isArchived=\"" + getIsArchived() + "\"" +
			" />"
		);
	}
}