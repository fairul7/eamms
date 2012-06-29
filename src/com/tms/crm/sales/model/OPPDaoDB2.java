package com.tms.crm.sales.model;

import java.util.Collection;
import java.util.Date;

import kacang.model.DaoException;

public class OPPDaoDB2 extends OPPDao {
	
	public Collection selectOpportunity() throws DaoException {
		return super.select("select opportunityID, companyID, opportunityName, opportunityStatus, opportunityStage, opportunityValue, opportunityStart, opportunityEnd, opportunityLastRemarks, modifiedDate, modifiedBy, closeReferenceNo from opportunity where opportunityStatus!=0", Opportunity3.class, new String[] {}, 0, -1);
	}
	
	public Collection selectOpportunity(Date fromDate, Date toDate) throws DaoException {
		String sql =
			"SELECT opportunityID, companyID, opportunityName, opportunityStatus, opportunityStage, " +
			"opportunityValue, opportunityStart, opportunityEnd, opportunityLastRemarks, modifiedDate, " +
			"modifiedBy, closeReferenceNo " +
			"FROM opportunity " +
			"WHERE opportunityStatus = 1 " +
			"AND opportunityEnd >= ? " +
			"AND opportunityEnd <= ? ";
	
		return super.select(sql, Opportunity3.class, new Object[] {fromDate, toDate}, 0, -1);
	}

    public Collection selectSales(Date fromDate, Date toDate) throws DaoException {
        String sql =
                "SELECT opportunityID, companyID, opportunityName, opportunityStatus, opportunityStage, " +
                "opportunityValue, opportunityStart, opportunityEnd, opportunityLastRemarks, modifiedDate, " +
                "modifiedBy, closeReferenceNo " +
                "FROM opportunity " +
                "WHERE opportunityStatus = 100 " +
                "AND opportunityEnd >= ? " +
                "AND opportunityEnd <= ? ";

        return super.select(sql, Opportunity3.class, new Object[] {fromDate, toDate}, 0, -1);
    }

	public Collection selectOpportunity(String userid) throws DaoException {
		return super.select("select opportunity.opportunityID, companyID, opportunityName, opportunityStatus, opportunityStage, opportunityValue, opportunityStart, opportunityEnd, opportunityLastRemarks, modifiedDate, modifiedBy, closeReferenceNo, accountdistribution.distributionSequence, accountdistribution.userID, accountdistribution.distributionPercentage from opportunity, accountdistribution where accountdistribution.opportunityID=opportunity.opportunityID and accountdistribution.userID=?", Opportunity3.class, new String[] {userid}, 0, -1);
	}

    public Collection selectSales(Date fromDate, Date toDate, String userid) throws DaoException {
        String sql =
                "SELECT opportunity.opportunityID, companyID, opportunityName, opportunityStatus, " +
                "opportunityStage, opportunityValue, opportunityStart, opportunityEnd, opportunityLastRemarks, " +
                "modifiedDate, modifiedBy, closeReferenceNo, accountdistribution.distributionSequence, " +
                "accountdistribution.userID, accountdistribution.distributionPercentage " +
                "FROM opportunity, accountdistribution " +
                "WHERE accountdistribution.opportunityID = opportunity.opportunityID " +
                "AND accountdistribution.userID = ? " +
                "AND opportunityStatus = 100 " +
                "AND opportunityEnd >= ? " +
                "AND opportunityEnd <= ? ";

        return super.select(sql, Opportunity3.class, new Object[] {userid, fromDate, toDate}, 0, -1);
    }

	public Collection selectOpportunity(Date fromDate, Date toDate, String userid) throws DaoException {
		String sql =
			"SELECT opportunity.opportunityID, companyID, opportunityName, opportunityStatus, " +
			"opportunityStage, opportunityValue, opportunityStart, opportunityEnd, opportunityLastRemarks, " +
			"modifiedDate, modifiedBy, closeReferenceNo, accountdistribution.distributionSequence, " +
			"accountdistribution.userID, accountdistribution.distributionPercentage " +
			"FROM opportunity, accountdistribution " +
			"WHERE accountdistribution.opportunityID = opportunity.opportunityID " +
			"AND accountdistribution.userID = ? " +
            "AND opportunityStatus = 1 " +
			"AND opportunityEnd >= ? " +
			"AND opportunityEnd <= ? ";
		
		return super.select(sql, Opportunity3.class, new Object[] {userid, fromDate, toDate}, 0, -1);
	}
	

}