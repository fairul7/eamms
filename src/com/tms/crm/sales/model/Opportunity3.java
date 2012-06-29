package com.tms.crm.sales.model;


public class Opportunity3 extends Opportunity {
	
	private String distributionSequence;
	private String userID;
	private String distributionPercentage;
	
	public String getDistributionSequence() {
		return distributionSequence;
	}
	
	public String getUserID() {
		return userID;
	}
	
	public String getDistributionPercentage() {
		return distributionPercentage;
	}

	public void setDistributionSequence(String string) {
		distributionSequence = string;	
	}
	
	public void setUserID(String string) {
		userID = string;	
	}
	
	public void setDistributionPercentage(String string) {
		distributionPercentage = string;	
	}
}