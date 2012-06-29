/*
 * Created on Dec 5, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.model;

import java.util.*;
import kacang.Application;
import kacang.model.*;
import com.tms.crm.sales.misc.*;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Opportunity extends DefaultDataObject {
	private String productName;
	private String opportunityID;
	private String companyID;
	private String opportunityName;
	private Integer opportunityStatus;
	private Integer opportunityStage;
	private double opportunityValue;
	private Date   opportunityStart;
	private Date   opportunityEnd;
	private String opportunitySource;
	private String opportunityLastRemarks;
	private String hasPartner;
	private String partnerCompanyID;
	private Date   creationDateTime;
	private Date   modifiedDate;
	private String modifiedBy;
	private String closeReferenceNo;
	public String statusCheck ;
	private double opValue;
	

	public  static String STATUS_INCOMPLETE_TEXT = Application.getInstance().getMessage("sfa.label.incomplete","Incomplete");
	public  static String STATUS_CLOSE_TEXT      = Application.getInstance().getMessage("sfa.label.closed","Closed");
	public  static Integer STATUS_INCOMPLETE = new Integer(0);
	public  static Integer STATUS_OPEN       = new Integer(1);
    public  static Integer STATUS_LOST       = new Integer(3);
	public  static Integer STATUS_CLOSE      = new Integer(100);
	
	private static Integer[] opportunityStatus_Code  = {STATUS_OPEN, new Integer(2), STATUS_LOST};
	private static String[] opportunityStatus_Text  = {Application.getInstance().getMessage("sfa.label.open","Open"), Application.getInstance().getMessage("sfa.label.postponed","Postponed"), Application.getInstance().getMessage("sfa.label.lost","Lost")};
	private static Integer[] opportunityStage_Code;
	private static String[] opportunityStage_Text;
	private static String[] opportunityStage_ShText;
	private static short[]  opportunityStage_Percent;
	private String companyNameStr;
	private String fyId;
	private String yearEnds;
	private String  monthlyProjection;
	private String currencySymbol;

	private String username;
	private String firstName;
	private String lastName;
	private String userId;
	private String fullName;
		
	private synchronized static void initOpportunityStage() {
		if (opportunityStage_Code == null) {
			Application application = Application.getInstance();
			OpportunityModule module = (OpportunityModule) application.getModule(OpportunityModule.class);
			Collection stageCol = module.getStageCollection();
			
			Integer[] code   = new Integer[stageCol.size()];
			String[] text    = new String[stageCol.size()];
			String[] shText  = new String[stageCol.size()];
			short[]  percent = new short[stageCol.size()];
			
			Iterator iterator = stageCol.iterator();
			for (int i=0; iterator.hasNext(); i++) {
				HashMap hm = (HashMap) iterator.next();
				code[i]    = new Integer(hm.get("stageID").toString());
				percent[i] = ((Number) hm.get("stagePercent")).shortValue();
				text[i]    = percent[i] + "% - " + (String) hm.get("stageText");
				shText[i]  = percent[i] + "%";
			}
			
			opportunityStage_Code    = code;
			opportunityStage_Text    = text;
			opportunityStage_ShText  = shText;
			opportunityStage_Percent = percent;
		}
	}
	
	/**
	 * The value times the stage (probability of closing sale)
	 */
	public double getAdjustedValue() {
		return (double) (opportunityValue * getStagePercent(opportunityStage));
	}
	
	public double getProductAdjustedValue() {
		return (double) (opValue * getStagePercent(opportunityStage));
	}
	
	/**
	 * The value times the stage (probability of closing sale)
	 */
	public String getAccountManagers() {
		Application application = Application.getInstance();
		AccountDistributionModule module = (AccountDistributionModule) application.getModule(AccountDistributionModule.class);
		
		String acManagers = module.getUsers(opportunityID);
		return acManagers;
	}
	
	public String getCloseReferenceNo() {
		return closeReferenceNo;
	}
	
	public String getCompanyID() {
		return companyID;
	}
	
	public Date getCreationDateTime() {
		return creationDateTime;
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
		if(opportunityLastRemarks == null || "".equals(opportunityLastRemarks))
			return " ";
		else
			return opportunityLastRemarks;
	}
	
	public String getOpportunityName() {
		return opportunityName;
	}
	
	public String getOpportunitySource() {
		return opportunitySource;
	}
	
	public Integer getOpportunityStage() {
		return opportunityStage;
	}
	
	public static Integer[] getOpportunityStage_Code() {
		if (opportunityStage_Code == null) {
			initOpportunityStage();
		}
		return(opportunityStage_Code);
	}
	
	public static String[] getOpportunityStage_Text() {
		if (opportunityStage_Code == null) {
			initOpportunityStage();
		}
		return(opportunityStage_Text);
	}
	
	public static short[] getOpportunityStage_Percent() {
		if (opportunityStage_Code == null) {
			initOpportunityStage();
		}
		return(opportunityStage_Percent);
	}
	
	public static Map getOpportunityStage_Map() {
		if (opportunityStage_Code == null) {
			initOpportunityStage();
		}
		return (MyUtil.arrayToMap(opportunityStage_Code, opportunityStage_Text));
	}
	
	/**
	 * shorter text version 
	 */ 
	public static Map getOpportunityStage_Less_Map() {
		if (opportunityStage_Code == null) {
			initOpportunityStage();
		}
		return (MyUtil.arrayToMap(opportunityStage_Code, opportunityStage_ShText));
	}
	
	public static double getStagePercent(Integer stageID) {
		if (opportunityStage_Code == null) {
			initOpportunityStage();
		}
		double percent = 0.0f;
		
		for (int i=0; i<opportunityStage_Code.length; i++) {
			if (stageID.equals(opportunityStage_Code[i])) {
				percent = opportunityStage_Percent[i] / 100.0f;
				break;
			}
		}
		
		return(percent);
	}
	
	public Date getOpportunityStart() {
		return opportunityStart;
	}
	
	public Integer getOpportunityStatus() {
		return opportunityStatus;
	}
	
	public static Integer[] getOpportunityStatus_Code() {
		return(opportunityStatus_Code);
	}
	
	public static String[] getOpportunityStatus_Text() {
		return(opportunityStatus_Text);
	}
	
	public static Map getOpportunityStatus_Map() {
		Map map = MyUtil.arrayToMap(opportunityStatus_Code, opportunityStatus_Text);
		map.put(STATUS_INCOMPLETE, STATUS_INCOMPLETE_TEXT);
		return (map);
	}
	
	/**
	 * also includes closed sale 
	 */ 
	public static Map getOpportunityStatus_More_Map() {
		Map map = MyUtil.arrayToMap(opportunityStatus_Code, opportunityStatus_Text);
		map.put(STATUS_INCOMPLETE, STATUS_INCOMPLETE_TEXT);
		map.put(STATUS_CLOSE, STATUS_CLOSE_TEXT);
		return (map);
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
	
	public void setCloseReferenceNo(String string) {
		closeReferenceNo = string;
	}
	
	public void setCompanyID(String string) {
		companyID = string;
	}
	
	public void setCreationDateTime(Date date) {
		creationDateTime = date;
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
	
	public void setOpportunitySource(String string) {
		opportunitySource = string;
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
	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String toString() {
		return(
			"<Sales Opportunity" +
			" opportunityID=\"" + getOpportunityID() + "\"" +
			" companyID=\"" + getCompanyID() + "\"" +
			" opportunityName=\"" + getOpportunityName() + "\"" +
			" opportunityStatus=\"" + getOpportunityStatus() + "\"" +
			" opportunityStage=\"" + getOpportunityStage() + "\"" +
			" opportunityValue=\"" + getOpportunityValue() + "\"" +
			" opportunityStart=\"" + getOpportunityStart() + "\"" +
			" opportunityEnd=\"" + getOpportunityEnd() + "\"" +
			" opportunitySource=\"" + getOpportunitySource() + "\"" +
			" opportunityLastRemarks=\"" + getOpportunityLastRemarks() + "\"" +
			" hasPartner=\"" + getHasPartner() + "\"" +
			" partnerCompanyID=\"" + getPartnerCompanyID() + "\"" +
			" creationDateTime=\"" + getCreationDateTime() + "\"" +
			" modifiedDate=\"" + getModifiedDate() + "\"" +
			" modifiedBy=\"" + getModifiedBy() + "\"" +
			" closeReferenceNo=\"" + getCloseReferenceNo() + "\"" +
			" />"
		);
	}

	public String getCompanyNameStr() {
		return companyNameStr;
	}

	public void setCompanyNameStr(String companyNameStr) {
		this.companyNameStr = companyNameStr;
	}

	public String getFyId() {
		return fyId;
	}

	public void setFyId(String fyId) {
		this.fyId = fyId;
	}

	public String getYearEnds() {
		return yearEnds;
	}

	public void setYearEnds(String yearEnds) {
		this.yearEnds = yearEnds;
	}

	public String getMonthlyProjection() {
		return monthlyProjection;
	}

	public void setMonthlyProjection(String monthlyProjection) {
		this.monthlyProjection = monthlyProjection;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getStatusCheck() {
		if (STATUS_INCOMPLETE.equals(opportunityStatus) || STATUS_LOST.equals(opportunityStatus) || STATUS_CLOSE.equals(opportunityStatus)) {
				statusCheck = "noImage"; }
		return statusCheck;
	}

	public void setStatusCheck(String statusCheck) {
		this.statusCheck = statusCheck;
	}

	public double getOpValue() {
		return opValue;
	}

	public void setOpValue(double opValue) {
		this.opValue = opValue;
	}

}
