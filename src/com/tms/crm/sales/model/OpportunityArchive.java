/*
 * Created on Jan 9, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.model;

import java.util.Date;
import kacang.model.*;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class OpportunityArchive extends DefaultDataObject {
	private String opportunityID;
	private String opportunityName;
	private Integer opportunityStatus;
	private Integer opportunityStage;
	private double    opportunityValue;
	private Date   opportunityStart;
	private Date   opportunityEnd;
	private String opportunityLastRemarks;
	private String hasPartner;
	private String partnerCompanyID;
	private Date   modifiedDate;
	private String modifiedBy;
	
	
	public OpportunityArchive() {
	}
	
	public OpportunityArchive(Opportunity opp) {
		opportunityID          = opp.getOpportunityID();
		opportunityName        = opp.getOpportunityName();
		opportunityStatus      = opp.getOpportunityStatus();
		opportunityStage       = opp.getOpportunityStage();
		opportunityValue       = opp.getOpportunityValue();
		opportunityStart       = opp.getOpportunityStart();
		opportunityEnd         = opp.getOpportunityEnd();
		opportunityLastRemarks = opp.getOpportunityLastRemarks();
		hasPartner             = opp.getHasPartner();
		partnerCompanyID       = opp.getPartnerCompanyID();
		modifiedDate           = opp.getModifiedDate();
		modifiedBy             = opp.getModifiedBy();
	}
	
	public String getModifiedBy() {
		return modifiedBy;
	}
	
	public Date getModifiedDate() {
		return modifiedDate;
	}
	
	public Date getOpportunityEnd() {
		return opportunityEnd;
	}
	
	public String getOpportunityID() {
		return opportunityID;
	}
	
	public String getOpportunityLastRemarks() {
		return opportunityLastRemarks;
	}
	
	public String getOpportunityName() {
		return opportunityName;
	}
	
	public Integer getOpportunityStage() {
		return opportunityStage;
	}
	
	public Date getOpportunityStart() {
		return opportunityStart;
	}
	
	public Integer getOpportunityStatus() {
		return opportunityStatus;
	}
	
	public double getOpportunityValue() {
		return opportunityValue;
	}
	
	public String getHasPartner() {
		return hasPartner;
	}
	
	public String getPartnerCompanyID() {
		return partnerCompanyID;
	}
	
	public void setModifiedBy(String string) {
		modifiedBy = string;
	}
	
	public void setModifiedDate(Date date) {
		modifiedDate = date;
	}
	
	public void setOpportunityEnd(Date date) {
		opportunityEnd = date;
	}
	
	public void setOpportunityID(String string) {
		opportunityID = string;
	}
	
	public void setOpportunityLastRemarks(String string) {
		opportunityLastRemarks = string;
	}
	
	public void setOpportunityName(String string) {
		opportunityName = string;
	}
	
	public void setOpportunityStage(Integer integer) {
		opportunityStage = integer;
	}
	
	public void setOpportunityStart(Date date) {
		opportunityStart = date;
	}
	
	public void setOpportunityStatus(Integer integer) {
		opportunityStatus = integer;
	}
	
	public void setOpportunityValue(double integer) {
		opportunityValue = integer;
	}
	
	public void setHasPartner(String string) {
		hasPartner = string;
	}
	
	public void setPartnerCompanyID(String string) {
		partnerCompanyID = string;
	}
	
	public String toString() {
		return(
			"<Sales OpportunityArchive" +
			" opportunityID=\"" + getOpportunityID() + "\"" +
			" opportunityName=\"" + getOpportunityName() + "\"" +
			" opportunityStatus=\"" + getOpportunityStatus() + "\"" +
			" opportunityStage=\"" + getOpportunityStage() + "\"" +
			" opportunityValue=\"" + getOpportunityValue() + "\"" +
			" opportunityStart=\"" + getOpportunityStart() + "\"" +
			" opportunityEnd=\"" + getOpportunityEnd() + "\"" +
			" opportunityLastRemarks=\"" + getOpportunityLastRemarks() + "\"" +
			" hasPartner=\"" + getHasPartner() + "\"" +
			" partnerCompanyID=\"" + getPartnerCompanyID() + "\"" +
			" modifiedDate=\"" + getModifiedDate() + "\"" +
			" modifiedBy=\"" + getModifiedBy() + "\"" +
			" />"
		);
	}
}
