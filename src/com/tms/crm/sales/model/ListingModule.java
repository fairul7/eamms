/*
 * Created on Feb 16, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.model;

import java.util.*;
import kacang.Application;
import kacang.model.*;
import kacang.model.operator.*;
import kacang.util.Log;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ListingModule extends DefaultModule {
	Log log = Log.getLog(getClass());
	
	public Collection listAllOpportunities(boolean getClosedSale, Date fromDate, Date toDate, String sort, boolean desc, int start, int rows) {
		ListingDao dao = (ListingDao) getDao();
		try {
			DaoQuery dq = getDqOpportunity(getClosedSale, fromDate, toDate);
			Collection col = dao.listAllOpportunities(dq, sort, desc, start, rows);
			return (col);
		} catch (DaoException e) {
			log.error("Error ListingModule:listUserOpportunities " + e.toString(), e);
			return (null);
		}
	}

    public Collection listAllOpportunities(String filter, boolean getClosedSale, Date fromDate, Date toDate, String sort, boolean desc, int start, int rows) {
        ListingDao dao = (ListingDao) getDao();
        try {
            DaoQuery dq = getDqOpportunity(getClosedSale, fromDate, toDate);
            Collection col = dao.listAllOpportunities(filter,dq, sort, desc, start, rows);
            return (col);
        } catch (DaoException e) {
            log.error("Error ListingModule:listUserOpportunities " + e.toString(), e);
            return (null);
        }
    }

    public Collection listGroupOpportunities(String filter, boolean getClosedSale, String groupID, Date fromDate, Date toDate, String sort, boolean desc, int start, int rows) {
        ListingDao dao = (ListingDao) getDao();
        try {
            DaoQuery dq = getDqGroupOpportunity(getClosedSale, groupID, fromDate, toDate);
            Collection col = dao.listGroupOpportunities(filter,dq, sort, desc, start, rows);
            ArrayList ids = new ArrayList(col.size());
            for (Iterator iterator = col.iterator(); iterator.hasNext();) {
                Opportunity opportunity = (Opportunity) iterator.next();
                if(ids.contains(opportunity.getOpportunityID()))
                    iterator.remove();
                else
                    ids.add(opportunity.getOpportunityID());
            }
            return (col);
        } catch (DaoException e) {
            log.error("Error ListingModule:listGroupOpportunities " + e.toString(), e);
            return (null);
        }
    }


	public Collection listGroupOpportunities(boolean getClosedSale, String groupID, Date fromDate, Date toDate, String sort, boolean desc, int start, int rows) {
		ListingDao dao = (ListingDao) getDao();
		try {
			DaoQuery dq = getDqGroupOpportunity(getClosedSale, groupID, fromDate, toDate);
			Collection col = dao.listGroupOpportunities(dq, sort, desc, start, rows);
			return (col);
		} catch (DaoException e) {
			log.error("Error ListingModule:listGroupOpportunities " + e.toString(), e);
			return (null);
		}
	}
	
	public Collection listUserOpportunities(boolean getClosedSale, String userID, Date fromDate, Date toDate, String sort, boolean desc, int start, int rows) {
		ListingDao dao = (ListingDao) getDao();
		try {
			DaoQuery dq = getDqOpportunity(getClosedSale, userID, fromDate, toDate);
			Collection col = dao.listUserOpportunities(dq, sort, desc, start, rows);
			return (col);
		} catch (DaoException e) {
			log.error("Error ListingModule:listUserOpportunities " + e.toString(), e);
			return (null);
		}
	}

    public Collection listUserOpportunities(String filter,boolean getClosedSale, String userID, Date fromDate, Date toDate, String sort, boolean desc, int start, int rows){
        ListingDao dao = (ListingDao) getDao();
        try {
            DaoQuery dq = getDqOpportunity(getClosedSale, userID, fromDate, toDate);
            Collection col = dao.listUserOpportunities(filter,dq, sort, desc, start, rows);
            return (col);
        } catch (DaoException e) {
            log.error("Error ListingModule:listUserOpportunities " + e.toString(), e);
            return (null);
        }
    }

	public int countAllOpportunities(boolean getClosedSale, Date fromDate, Date toDate) {
		ListingDao dao = (ListingDao) getDao();
		try {
			DaoQuery dq = getDqOpportunity(getClosedSale, fromDate, toDate);
			return dao.countAllOpportunities(dq);
		} catch (DaoException e) {
			log.error("Error ListingModule:countAllOpportunities " + e.toString(), e);
			return(0);
		}
	}
	
	public int countGroupOpportunities(boolean getClosedSale, String groupID, Date fromDate, Date toDate) {
		ListingDao dao = (ListingDao) getDao();
		try {
			DaoQuery dq = getDqGroupOpportunity(getClosedSale, groupID, fromDate, toDate);
			return dao.countGroupOpportunities(dq);
		} catch (DaoException e) {
			log.error("Error ListingModule:countGroupOpportunities " + e.toString(), e);
			return(0);
		}
	}
	
	public int countUserOpportunities(boolean getClosedSale, String userID, Date fromDate, Date toDate) {
		ListingDao dao = (ListingDao) getDao();
		try {
			DaoQuery dq = getDqOpportunity(getClosedSale, userID, fromDate, toDate);
			return dao.countUserOpportunities(dq);
		} catch (DaoException e) {
			log.error("Error ListingModule:countUserOpportunities " + e.toString(), e);
			return(0);
		}
	}
	
	private DaoQuery getDqOpportunity(boolean getClosedSale, Date fromDate, Date toDate) {
		DaoQuery dq = new DaoQuery();
		if (getClosedSale) {
			dq.addProperty(new OperatorEquals("opportunityStatus", Opportunity.STATUS_CLOSE, ""));
		} else {
			dq.addProperty(new OperatorEquals("opportunityStatus", Opportunity.STATUS_CLOSE, "NOT"));
		}
		procFromDate(dq, fromDate);
		procToDate(dq, toDate);
		return dq;
	}
	
	private DaoQuery getDqOpportunity(boolean getClosedSale, String userID, Date fromDate, Date toDate) {
		DaoQuery dq = new DaoQuery();
		dq.addProperty(new OperatorEquals("userID", userID, DaoOperator.OPERATOR_AND));
		if (getClosedSale) {
			dq.addProperty(new OperatorEquals("opportunityStatus", Opportunity.STATUS_CLOSE, DaoOperator.OPERATOR_AND));
		} else {
			dq.addProperty(new OperatorEquals("opportunityStatus", Opportunity.STATUS_CLOSE, DaoOperator.OPERATOR_NAN));
		}
		procFromDate(dq, fromDate);
		procToDate(dq, toDate);
		return dq;
	}
	
	private DaoQuery getDqGroupOpportunity(boolean getClosedSale, String groupID, Date fromDate, Date toDate) {
		// Get the users based on the groupID
		Application application = Application.getInstance();
		AccountManagerModule module = (AccountManagerModule) application.getModule(AccountManagerModule.class);
		Collection col = module.getAccountManagers(groupID);
		
		// Get 'IN' clause
		String[] userIDs = new String[col.size()];
		Iterator iterator = col.iterator();
		for (int i=0; i<userIDs.length; i++) {
			userIDs[i] = ((AccountManager) iterator.next()).getId(); 
		}
		
		DaoQuery dq = new DaoQuery();
		dq.addProperty(new OperatorIn("userID", userIDs, DaoOperator.OPERATOR_AND));
		if (getClosedSale) {
			dq.addProperty(new OperatorEquals("opportunityStatus", Opportunity.STATUS_CLOSE, DaoOperator.OPERATOR_AND));
		} else {
			dq.addProperty(new OperatorEquals("opportunityStatus", Opportunity.STATUS_CLOSE, DaoOperator.OPERATOR_NAN));
		}
		procFromDate(dq, fromDate);
		procToDate(dq, toDate);
		return dq;
	}
	
	private void procFromDate(DaoQuery dq, Date fromDate) {
		OperatorParenthesis parenthesis = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
		parenthesis.addOperator(new OperatorEquals("opportunityEnd", fromDate, ""));
		parenthesis.addOperator(new OperatorGreaterThan("opportunityEnd", fromDate, DaoOperator.OPERATOR_OR));
		dq.addProperty(parenthesis);
	}
	
	private void procToDate(DaoQuery dq, Date toDate) {
		OperatorParenthesis parenthesis = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
		parenthesis.addOperator(new OperatorEquals("opportunityEnd", toDate, ""));
		parenthesis.addOperator(new OperatorLessThan("opportunityEnd", toDate, DaoOperator.OPERATOR_OR));
		dq.addProperty(parenthesis);
	}
}
