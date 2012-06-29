/*
 * Created on Dec 2, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.model;

import java.util.Map;
import java.util.Date;

import kacang.model.*;
import com.tms.crm.sales.misc.*;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Company extends DefaultDataObject {
	private String companyID;
	private String companyName;
	private String companyType;
	private String companyStreet1;
	private String companyStreet2;
	private String companyCity;
	private String companyState;
	private String companyPostcode;
	private String companyCountry;
	private String companyTel;
	private String companyFax;
	private String companyWebsite;
	private Integer companyPartnerTypeID;
    private Date lastModified;
	private static String[] companyType_Code = {"C", "E", "F", "G"};
	private static String[] companyType_Text = {"Community", "Education", "Enterprise", "Government"};
	
	
	/* Special -  start */
	public String getWebsiteLink(String target) {
		String websiteLink = "";
		
		if (target == null || target.equals("")) {
			target = "_blank";
		}
		
		if (companyWebsite != null && !companyWebsite.trim().equals("")) {
			websiteLink = companyWebsite.trim();
			if (websiteLink.startsWith("http://")) {
				websiteLink = "<a href='" + websiteLink + "' target='" + target + "'>" + websiteLink + "</a>";
			} else {
				websiteLink = "<a href='http://" + websiteLink + "' target='" + target + "'>http://" + websiteLink + "</a>";
			}
		}
		return websiteLink;
	}
	/* Special -  end   */
	
	
	public String getCompanyCity() {
		return companyCity;
	}
	
	public String getCompanyCountry() {
		return companyCountry;
	}
	
	public String getCompanyFax() {
		return companyFax;
	}
	
	public String getCompanyID() {
		return companyID;
	}
	
	public String getCompanyName() {
		return companyName;
	}
	
	public static String[] getCompanyType_Code() {
		return(companyType_Code);
	}
	
	public static String[] getCompanyType_Text() {
		return(companyType_Text);
	}
	
	public static Map getCompanyType_Map() {
		return (MyUtil.arrayToMap(companyType_Code, companyType_Text));
	}
	
	public Integer getCompanyPartnerTypeID() {
		return companyPartnerTypeID;
	}
	
	public String getCompanyPostcode() {
		return companyPostcode;
	}
	
	public String getCompanyState() {
		return companyState;
	}
	
	public String getCompanyStreet1() {
		return companyStreet1;
	}
	
	public String getCompanyStreet2() {
		return companyStreet2;
	}
	
	public String getCompanyTel() {
		return companyTel;
	}
	
	public String getCompanyType() {
		return companyType;
	}
	
	public String getCompanyWebsite() {
		return companyWebsite;
	}
	
	public void setCompanyCity(String string) {
		companyCity = string;
	}
	
	public void setCompanyCountry(String string) {
		companyCountry = string;
	}
	
	public void setCompanyFax(String string) {
		companyFax = string;
	}
	
	public void setCompanyID(String string) {
		companyID = string;
	}
	
	public void setCompanyName(String string) {
		companyName = string;
		if (companyName != null) {
			companyName = companyName.trim();
		}
	}
	
	public void setCompanyPartnerTypeID(Integer integer) {
		companyPartnerTypeID = integer;
	}
	
	public void setCompanyPostcode(String string) {
		companyPostcode = string;
	}
	
	public void setCompanyState(String string) {
		companyState = string;
	}
	
	public void setCompanyStreet1(String string) {
		companyStreet1 = string;
	}
	
	public void setCompanyStreet2(String string) {
		companyStreet2 = string;
	}
	
	public void setCompanyTel(String string) {
		companyTel = string;
	}
	
	public void setCompanyType(String string) {
		companyType = string;
	}
	
	public void setCompanyWebsite(String string) {
		companyWebsite = string;
	}

	public String toString() {
		return(
			"<Sales Company" +
			" companyID=\"" + getCompanyID() + "\"" +
			" companyName=\"" + getCompanyName() + "\"" +
			" companyType=\"" + getCompanyType() + "\"" +
			" companyStreet1=\"" + getCompanyStreet1() + "\"" +
			" companyStreet2=\"" + getCompanyStreet2() + "\"" +
			" companyCity=\"" + getCompanyCity() + "\"" +
			" companyState=\"" + getCompanyState() + "\"" +
			" companyPostcode=\"" + getCompanyPostcode() + "\"" +
			" companyCountry=\"" + getCompanyCountry() + "\"" +
			" companyTel=\"" + getCompanyTel() + "\"" +
			" companyFax=\"" + getCompanyFax() + "\"" +
			" companyWebsite=\"" + getCompanyWebsite() + "\"" +
			" companyPartnerTypeID=\"" + getCompanyPartnerTypeID() + "\"" +
			" />"
		);
	}

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
}
