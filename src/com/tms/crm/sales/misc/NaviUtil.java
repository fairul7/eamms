/*
 * Created on Jan 28, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.misc;

import java.util.*;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.PageContext;
import kacang.Application;
import com.tms.crm.sales.model.*;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class NaviUtil {
	PageContext pageContext;
	Application app;
	
	public NaviUtil(PageContext pageContext) {
		app = Application.getInstance();
		this.pageContext = pageContext;
	}
	
	public AccountManager getAccountManager(String userID) {
		AccountManagerModule amModule = (AccountManagerModule) app.getModule(AccountManagerModule.class);
		return amModule.getAccountManager(userID);
	}
	
	public void getAccountManager(String inputVar, String outputVar) {
		String userID = (String) pageContext.getAttribute(inputVar);
		write2PageContext(outputVar, getAccountManager(userID));
	}
	
	public Company getCompany(String companyID) {
		CompanyModule comModule = (CompanyModule) app.getModule(CompanyModule.class);
		return comModule.getCompany(companyID);
	}
	
	public void getCompany(String inputVar, String outputVar) {
		String companyID = (String) pageContext.getAttribute(inputVar);
		write2PageContext(outputVar, getCompany(companyID));
	}
	
	public Opportunity getOpportunity(String opportunityID) {
		OpportunityModule opModule = (OpportunityModule) app.getModule(OpportunityModule.class);
		return opModule.getOpportunity(opportunityID);
	}
	
	public void getOpportunity(String inputVar, String outputVar) {
		String opportunityID = (String) pageContext.getAttribute(inputVar);
		write2PageContext(outputVar, getOpportunity(opportunityID));
	}
	
	public Contact getContact(String contactID) {
		ContactModule conModule = (ContactModule) app.getModule(ContactModule.class);
		return conModule.getContact(contactID);
	}
	
	public void getContact(String inputVar, String outputVar) {
		String contactID = (String) pageContext.getAttribute(inputVar);
		write2PageContext(outputVar, getContact(contactID));
	}
	
	public void hasPartner(String inputVar, String outputVar) {
		String opportunityID = (String) pageContext.getAttribute(inputVar);
		Opportunity opp = getOpportunity(opportunityID);
		
		boolean gotPartner = false;
		if (opp.getHasPartner().equals("1")) {
			gotPartner = true;
		}
		write2PageContext(outputVar, new Boolean(gotPartner));
	}
	
	public void canCloseOpportunity(String inputVar, String outputVar) {
		String opportunityID = (String) pageContext.getAttribute(inputVar);
		Opportunity opp = getOpportunity(opportunityID);
		
		Integer opportunityStatus = opp.getOpportunityStatus();
		boolean canClose = false;
		if (!opportunityStatus.equals(Opportunity.STATUS_INCOMPLETE) && !opportunityStatus.equals(Opportunity.STATUS_CLOSE)) {
			canClose = true;
		}
		write2PageContext(outputVar, new Boolean(canClose));
	}
	
	public Integer getStatus(String opportunityID) {
		Opportunity opp = getOpportunity(opportunityID);
		return opp.getOpportunityStatus();
	}
	
	public void getStatus(String inputVar, String outputVar) {
		String opportunityID = (String) pageContext.getAttribute(inputVar);
		write2PageContext(outputVar, getStatus(opportunityID));
	}
	
	public String getCompanyID4Opportunity(String opportunityID) {
		Opportunity opp = getOpportunity(opportunityID);
		return opp.getCompanyID();
	}
	
	public void getCompanyID4Opportunity(String inputVar, String outputVar) {
		String opportunityID = (String) pageContext.getAttribute(inputVar);
		write2PageContext(outputVar, getCompanyID4Opportunity(opportunityID));
	}
	
	public String getPartnerCompanyID4Opportunity(String opportunityID) {
		Opportunity opp = getOpportunity(opportunityID);
		return opp.getPartnerCompanyID();
	}
	
	public void getPartnerCompanyID4Opportunity(String inputVar, String outputVar) {
		String opportunityID = (String) pageContext.getAttribute(inputVar);
		write2PageContext(outputVar, getPartnerCompanyID4Opportunity(opportunityID));
	}
	
	public String getCompanyID4Contact(String contactID) {
		Contact con = getContact(contactID);
		return con.getCompanyID();
	}
	
	public void getCompanyID4Contact(String inputVar, String outputVar) {
		String contactID = (String) pageContext.getAttribute(inputVar);
		write2PageContext(outputVar, getCompanyID4Contact(contactID));
	}
	
	public static void dump(PageContext pageContext) {
		int scope;
		String scopeName;
		Enumeration enum2;
		
		ServletRequest request = pageContext.getRequest();
		enum2 = request.getParameterNames();
		if (enum2.hasMoreElements()) {
			System.out.println("REQUEST_PARAMETER: (only one value)");
			while(enum2.hasMoreElements()) {
				String name = (String) enum2.nextElement();
				String val  = request.getParameter(name);
				System.out.println("  " + name + "=" + val);
			}
			System.out.println();
		}
		
		scope = PageContext.REQUEST_SCOPE;
		scopeName = "REQUEST_SCOPE";
		enum2 = pageContext.getAttributeNamesInScope(scope);
		if (enum2.hasMoreElements()) {
			System.out.println(scopeName + ":");
			while(enum2.hasMoreElements()) {
				String name = (String) enum2.nextElement();
				Object val  = pageContext.getAttribute(name, scope);
				System.out.println("  " + name + "=" + val);
			}
			System.out.println();
		}
		
		scope = PageContext.PAGE_SCOPE;
		scopeName = "PAGE_SCOPE";
		enum2 = pageContext.getAttributeNamesInScope(scope);
		if (enum2.hasMoreElements()) {
			System.out.println(scopeName + ":");
			while(enum2.hasMoreElements()) {
				String name = (String) enum2.nextElement();
				Object val  = pageContext.getAttribute(name, scope);
				System.out.println("  " + name + "=" + val);
			}
			System.out.println();
		}
	}
	
	private void write2PageContext(String name, Object attribute) {
		if (attribute == null) {
			attribute = "";
		}
		pageContext.setAttribute(name, attribute);
	}
}
