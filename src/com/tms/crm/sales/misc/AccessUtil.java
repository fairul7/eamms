/*
 * Created on Apr 5, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.misc;

import kacang.Application;
import kacang.services.security.*;
import com.tms.crm.sales.model.*;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class AccessUtil {
	public static boolean isSalesManager(String userID) {
		return hasPermission(userID, "com.tms.crm.sales.SalesManager");
	}
	
	public static boolean isSalesPerson(String userID) {
		return hasPermission(userID, "com.tms.crm.sales.SalesPerson");
	}
	
	public static boolean isExternalSalesPerson(String userID) {
		return hasPermission(userID, "com.tms.crm.sales.ExternalSalesPerson");
	}
	
	public static boolean isDashboardUser(String userID) {
		return hasPermission(userID, "com.tms.crm.sales.Dashboard");
	}
	
	public static boolean isSalesAdmin(String userID) {
		return hasPermission(userID, "com.tms.crm.sales.SalesAdmin");
	}
	
	private static boolean hasPermission(String userID, String permission) {
		Application application = Application.getInstance();
		SecurityService security = (SecurityService) application.getService(SecurityService.class);
		
		try {
			return security.hasPermission(userID, permission, "com.tms.crm.sales.model.AccountManagerModule", null);
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean isOpportunityOwner(String userID, String opportunityID) {
		Application application = Application.getInstance();
		AccountDistributionModule module = (AccountDistributionModule) application.getModule(AccountDistributionModule.class);
		
		String[] userIDs = module.getUserIDs(opportunityID);
		boolean isOwner = false;
		for (int i=0; i<userIDs.length; i++) {
			if (userID.equals(userIDs[i])) {
				isOwner = true;
				break;
			}
		}
		
		return isOwner;
	}

    public static boolean isLeadOwner(String userId, String leadId){
        LeadModule lm = (LeadModule) Application.getInstance().getModule(LeadModule.class);
        try {
            Lead lead = lm.getLead(leadId);
            if(lead.getUserId().equals(userId))
                return true;
            
        } catch (LeadException e) {
            return false;
        }
        return false;

    }

}