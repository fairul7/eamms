/*
 * Created on Feb 16, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.model;

import java.util.*;
import kacang.model.*;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ListingDao extends DataSourceDao {



	public Collection listAllOpportunities(String filter,DaoQuery dq, String sort, boolean desc, int start, int rows) throws DaoException {
        StringBuffer sql = new StringBuffer("SELECT opportunity.opportunityID, opportunity.companyID, opportunityName, opportunityStatus, opportunityStage, opportunityValue, opportunityStart, opportunityEnd, opportunityLastRemarks, hasPartner, partnerCompanyID, modifiedDate, modifiedBy, closeReferenceNo " +
                "FROM opportunity, company " +
                "WHERE opportunity.companyID = company.companyID AND" +
                dq.getStatement()) ;
        Collection args = new ArrayList(Arrays.asList(dq.getArray()));
        String orderBy = " ORDER BY " + ((sort != null) ? sort : "opportunityName") + ((desc) ? " DESC" : "");
        if(filter!=null&&filter.trim().length()>0){
            filter = "%"+filter+"%";
            sql.append(" AND (opportunityName LIKE ? OR companyName LIKE ? OR closeReferenceNo LIKE ?)");
            args.add(filter);
            args.add(filter);
            args.add(filter);
        }
        Collection col = super.select(sql.append(orderBy).toString(), Opportunity.class, args.toArray(), start, rows);
        return (col);
    }


    public Collection listAllOpportunities(DaoQuery dq, String sort, boolean desc, int start, int rows) throws DaoException {
        String orderBy = "ORDER BY " + ((sort != null) ? sort : "opportunityName") + ((desc) ? " DESC" : "");
        Collection col = super.select(
                "SELECT opportunityID, companyID, opportunityName, opportunityStatus, opportunityStage, opportunityValue, opportunityStart, opportunityEnd, opportunityLastRemarks, hasPartner, partnerCompanyID, modifiedDate, modifiedBy, closeReferenceNo " +
                "FROM opportunity " +
                "WHERE " +
                dq.getStatement() +
                orderBy
                , Opportunity.class, dq.getArray(), start, rows);
        return (col);
    }

	public Collection listGroupOpportunities(DaoQuery dq, String sort, boolean desc, int start, int rows) throws DaoException {
		return listUserOpportunities(dq, sort, desc, start, rows);
	}

    public Collection listGroupOpportunities(String filter,DaoQuery dq, String sort, boolean desc, int start, int rows) throws DaoException {
        return listUserOpportunities(filter,dq, sort, desc, start, rows);
    }


	public Collection listUserOpportunities(DaoQuery dq, String sort, boolean desc, int start, int rows) throws DaoException {
		String orderBy = "ORDER BY " + ((sort != null) ? sort : "opportunityName") + ((desc) ? " DESC" : "");
		Collection col = super.select(
			"SELECT opportunity.opportunityID, companyID, opportunityName, opportunityStatus, opportunityStage, opportunityValue, opportunityStart, opportunityEnd, opportunityLastRemarks, hasPartner, partnerCompanyID, modifiedDate, modifiedBy, closeReferenceNo, distributionPercentage " +
			"FROM opportunity, accountdistribution " +
			"WHERE opportunity.opportunityID = accountdistribution.opportunityID " +
			dq.getStatement() +
			orderBy
		, Opportunity.class, dq.getArray(), start, rows);
		return (col);
	}

    public Collection listUserOpportunities(String filter,DaoQuery dq, String sort, boolean desc, int start, int rows) throws DaoException {
        StringBuffer sql = new StringBuffer("SELECT opportunity.opportunityID, opportunity.companyID, opportunityName, opportunityStatus, opportunityStage, opportunityValue, opportunityStart, opportunityEnd, opportunityLastRemarks, hasPartner, partnerCompanyID, modifiedDate, modifiedBy, closeReferenceNo, distributionPercentage " +
                "FROM opportunity, accountdistribution,company " +
                "WHERE opportunity.opportunityID = accountdistribution.opportunityID AND opportunity.companyID = company.companyID " +
                dq.getStatement()) ;
        Collection args = new ArrayList(Arrays.asList(dq.getArray()));
        String orderBy = " ORDER BY " + ((sort != null) ? sort : "opportunityName") + ((desc) ? " DESC" : "");
        if(filter!=null&&filter.trim().length()>0){
            filter = "%"+filter+"%";
            sql.append(" AND (opportunityName LIKE ? OR companyName LIKE ? OR closeReferenceNo LIKE ?)");
            args.add(filter);
            args.add(filter);
            args.add(filter);
        }
        //dq.
        Collection col = super.select(sql.append(orderBy).toString(), Opportunity.class, args.toArray(), start, rows);
        return (col);
    }

	public int countAllOpportunities(DaoQuery dq) throws DaoException {
		Collection list = super.select("SELECT COUNT(*) AS total FROM opportunity WHERE " + dq.getStatement(), HashMap.class, dq.getArray(), 0, 1);
		HashMap map = (HashMap)list.iterator().next();
		return Integer.parseInt(map.get("total").toString());
	}
	
	public int countGroupOpportunities(DaoQuery dq) throws DaoException {
		return countUserOpportunities(dq);
	}
	
	public int countUserOpportunities(DaoQuery dq) throws DaoException {
		Collection list = super.select("SELECT COUNT(*) AS total FROM opportunity, accountdistribution WHERE opportunity.opportunityID = accountdistribution.opportunityID " + dq.getStatement(), HashMap.class, dq.getArray(), 0, 1);
		HashMap map = (HashMap)list.iterator().next();
		return Integer.parseInt(map.get("total").toString());
	}
}