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
public class AccountDistribution extends DefaultDataObject {
	private String opportunityID;
	private int    distributionSequence;
	private String userID;
	private int    distributionPercentage;
	
	public int getDistributionPercentage() {
		return distributionPercentage;
	}
	
	public int getDistributionSequence() {
		return distributionSequence;
	}
	
	public String getOpportunityID() {
		return opportunityID;
	}
	
	public String getUserID() {
		return userID;
	}
	
	public void setDistributionPercentage(int i) {
		distributionPercentage = i;
	}
	
	public void setDistributionSequence(int i) {
		distributionSequence = i;
	}
	
	public void setOpportunityID(String string) {
		opportunityID = string;
	}
	
	public void setUserID(String string) {
		userID = string;
	}
	
	public String toString() {
		return(
			"<Sales AccountDistribution" +
			" opportunityID=\"" + getOpportunityID() + "\"" +
			" distributionSequence=\"" + getDistributionSequence() + "\"" +
			" userID=\"" + getUserID() + "\"" +
			" distributionPercentage=\"" + getDistributionPercentage() + "\"" +
			" />"
		);
	}
}
