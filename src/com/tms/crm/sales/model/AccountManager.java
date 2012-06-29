/*
 * Created on Dec 16, 2003
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
public class AccountManager extends DefaultDataObject {
	private String id;
	private String firstName;
	private String lastName;
	private String email1;
	
	public String getEmail1() {
		return email1;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getId() {
		return id;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getFullName() {
		return firstName + " " + lastName;
	}
	
	public void setEmail1(String string) {
		email1 = string;
	}
	
	public void setFirstName(String string) {
		firstName = string;
	}
	
	public void setId(String string) {
		id = string;
	}
	
	public void setLastName(String string) {
		lastName = string;
	}
	
	public String toString() {
		return(
			"<Sales AccountManager" +
			" id=\"" + getId() + "\"" +
			" firstName=\"" + getFirstName() + "\"" +
			" lastName=\"" + getLastName() + "\"" +
			" email1=\"" + getEmail1() + "\"" +
			" />"
		);
	}


}
