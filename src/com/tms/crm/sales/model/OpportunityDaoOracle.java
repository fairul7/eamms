package com.tms.crm.sales.model;

import com.tms.crm.sales.misc.MyUtil;
import com.tms.crm.sales.misc.DateUtil;

import java.util.Date;

public class OpportunityDaoOracle extends OpportunityDao
{
	/**
	 *
	 * @param statusFilter	if -100, will exclude closed sales
	 * @param keyword
	 * @param companyID
	 * @param accountManagerID
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	protected String getWhereClause(String keyword, String companyID, String accountManagerID, String stageID, int statusFilter, boolean showClosed,boolean showLost, Date fromDate, Date toDate)
	{
		String whereClause = "opportunity.companyID = company.companyID ";
		if (keyword != null && keyword.trim().length() != 0)
		{
			keyword = keyword.trim();
			if (whereClause.length() != 0) whereClause = whereClause + "AND ";
			whereClause = whereClause +
					"(opportunityName LIKE '%" + keyword + "%' " +
					" OR  companyName LIKE '%" + keyword + "%') ";
		}
		if (companyID != null && !companyID.equals(""))
		{
			if (whereClause.length() != 0) whereClause = whereClause + "AND ";
			whereClause = whereClause + "opportunity.companyID = '" + MyUtil.escapeSingleQuotes(companyID) + "' ";
		}
		if (accountManagerID != null && !accountManagerID.equals(""))
		{
			if (whereClause.length() != 0) whereClause = whereClause + "AND ";
			whereClause = whereClause + "opportunity.opportunityID = accountdistribution.opportunityID ";
			whereClause = whereClause + "AND accountdistribution.userID = '" + MyUtil.escapeSingleQuotes(accountManagerID) + "' ";
		}
		if (stageID != null && !stageID.equals(""))
		{
			if (whereClause.length() != 0) whereClause = whereClause + "AND ";
			whereClause = whereClause + "opportunityStage = " + Integer.parseInt(stageID) + " ";
		}
		if (statusFilter == -100 )
		{
			if (whereClause.length() != 0) whereClause = whereClause + "AND ";
			whereClause = whereClause + "(opportunityStatus = 0 OR opportunityStatus = 1 or opportunityStatus = 2  ";
		}
		else if (statusFilter == -1000)
		{
            if (whereClause.length() != 0) whereClause = whereClause + "AND ";
            whereClause = whereClause + "(opportunityStatus = 0 OR opportunityStatus = 1 or opportunityStatus = 2  ";
            showClosed = true;
            showLost = true;
			// return all status
		}
		else
		{
			if (whereClause.length() != 0) whereClause = whereClause + "AND ";
			whereClause = whereClause + "(opportunityStatus = " + statusFilter + " ";
		}
        if(showClosed)
            whereClause =  whereClause + " OR opportunityStatus = "+ Opportunity.STATUS_CLOSE + " ";
        if(showLost)
            whereClause =  whereClause + " OR opportunityStatus = "+ Opportunity.STATUS_LOST+ " ";
        whereClause = whereClause+ ") ";
		if (fromDate != null)
		{
			if (whereClause.length() != 0) whereClause = whereClause + "AND ";
			String from = DateUtil.getDateTimeString(fromDate);
			whereClause = whereClause + "opportunityEnd >= to_date('" + from + "', 'YYYY-MM-DD HH24:MI:SS') ";
		}
		if (toDate != null)
		{
			if (whereClause.length() != 0) whereClause = whereClause + "AND ";
			String to = DateUtil.getDateTimeString(toDate);
			whereClause = whereClause + "opportunityEnd <= to_date('" + to + "', 'YYYY-MM-DD HH24:MI:SS') ";
		}
		if (whereClause.length() != 0)
			whereClause = "WHERE " + whereClause;
		return (whereClause);
	}

	protected String getLostWhereClause(String keyword, String companyID, String accountManagerID, String stageID, int statusFilter, boolean showClosed,boolean showLost, Date fromDate, Date toDate)
	{
        String whereClause = "opportunity.companyID = company.companyID ";
        if (keyword != null && keyword.trim().length() != 0)
		{
            keyword = keyword.trim();
            if (whereClause.length() != 0) whereClause = whereClause + "AND ";
            whereClause = whereClause +
                    "(opportunityName LIKE '%" + keyword + "%' " +
                    " OR  companyName LIKE '%" + keyword + "%') ";
        }
        if (companyID != null && !companyID.equals(""))
		{
            if (whereClause.length() != 0) whereClause = whereClause + "AND ";
            whereClause = whereClause + "opportunity.companyID = '" + MyUtil.escapeSingleQuotes(companyID) + "' ";
        }
        if (accountManagerID != null && !accountManagerID.equals(""))
		{
            if (whereClause.length() != 0) whereClause = whereClause + "AND ";
            whereClause = whereClause + "opportunity.opportunityID = accountdistribution.opportunityID ";
            whereClause = whereClause + "AND accountdistribution.userID = '" + MyUtil.escapeSingleQuotes(accountManagerID) + "' ";
        }
        if (stageID != null && !stageID.equals(""))
		{
            if (whereClause.length() != 0) whereClause = whereClause + "AND ";
            whereClause = whereClause + "opportunityStage = " + Integer.parseInt(stageID) + " ";
        }
        if (statusFilter == -100 )
		{
            if (whereClause.length() != 0) whereClause = whereClause + "AND ";
            whereClause = whereClause + "(opportunityStatus = 0 OR opportunityStatus = 1 or opportunityStatus = 2  ";
        }
		else if (statusFilter == -1000)
		{
            if (whereClause.length() != 0) whereClause = whereClause + "AND ";
            whereClause = whereClause + "(opportunityStatus = 0 OR opportunityStatus = 1 or opportunityStatus = 2  ";
            showClosed = true;
            showLost = true;
            // return all status
        }
		else
		{
            if (whereClause.length() != 0) whereClause = whereClause + "AND ";
            whereClause = whereClause + "(opportunityStatus = " + statusFilter + " ";
        }
        if(showClosed)
            whereClause =  whereClause + " OR opportunityStatus = "+ Opportunity.STATUS_CLOSE + " ";
        if(showLost)
            whereClause =  whereClause + " OR opportunityStatus = "+ Opportunity.STATUS_LOST+ " ";
        whereClause = whereClause+ ") ";
        if (fromDate != null) 
        {
            if (whereClause.length() != 0) whereClause = whereClause + "AND ";
            if (showClosed)
            {
                whereClause += "opportunityEnd";
            }
            else
            {
                whereClause += "modifiedDate";
            }
            String from = DateUtil.getDateTimeString(fromDate);
            whereClause = whereClause + "modifiedDate >= to_date('" + from + "', 'YYYY-MM-DD HH24:MI:SS') ";
        }

        if (toDate != null)
        {
            if (whereClause.length() != 0) whereClause = whereClause + "AND ";
            if (showClosed)
            {
                whereClause += "opportunityEnd";
            }
            else
            {
                whereClause += "modifiedDate";
            }
            String to = DateUtil.getDateTimeString(toDate);
            whereClause = whereClause + "modifiedDate <= to_date('" + to + "', 'YYYY-MM-DD HH24:MI:SS') ";
        }
        if (whereClause.length() != 0)
            whereClause = "WHERE " + whereClause;
        return (whereClause);
    }
}
