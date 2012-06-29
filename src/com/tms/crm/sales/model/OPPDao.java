package com.tms.crm.sales.model;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.model.DaoQuery;
import kacang.model.DefaultDataObject;

public class OPPDao extends DataSourceDao {
	
	public Collection selectOpportunity() throws DaoException {
		return super.select("select opportunityID, companyID, opportunityName, opportunityStatus, opportunityStage, opportunityValue, opportunityStart, opportunityEnd, opportunityLastRemarks, modifiedDate, modifiedBy, closeReferenceNo from opportunity where opportunityStatus != 0", Opportunity3.class, new String[] {}, 0, -1);
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
	
	public void insertProjection(Projection p) throws DaoException {
		super.update("insert into projection (projectionID, userID, year, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12) values (#projectionID#, #userID#, #year#, #month1#, #month2#, #month3#, #month4#, #month5#, #month6#, #month7#, #month8#, #month9#, #month10#, #month11#, #month12#)", p);
	}
	
	public Collection selectProjection() throws DaoException {
		return super.select("select projectionID, userID, year, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12 from projection", Projection.class, new String[] {}, 0, -1);
	}
	
	public Collection selectProjection(int year) throws DaoException {
		String sql =
			"SELECT projectionID, userID, year, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12 " +
			"FROM projection, security_user " +
			"WHERE projection.userID=security_user.id AND security_user.active='1' AND year = ? ";
		return super.select(sql, Projection.class, new Object[] {new Integer(year)}, 0, -1);
	}


    public Collection listProjection( DaoQuery query,String sort, boolean desc,int startIndex,int rows) throws DaoException {
        String sql = "SELECT projectionID,security_user.id, month1+month2+month3+month4+month5+month6+month7+month8+month9+month10+month11+month12 as projection, firstName, lastName " +
                "FROM security_user, projection WHERE security_user.id = projection.userID  " +
                query.getStatement() +
                (sort==null?"": " ORDER BY "+sort +(desc?" DESC":"") );

        return super.select(sql,Projection.class,query.getArray(),startIndex,rows);
    }

	public Collection selectProjection(String userid) throws DaoException {
		return super.select("select projectionID, userID, year, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12 from projection where userID=?", Projection.class, new String[] {userid}, 0, -1);
	}
	
	public Collection selectProjection(String userid, int year) throws DaoException {
		String sql =
			"SELECT projectionID, userID, year, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12 " +
			"FROM projection " +
			"WHERE userID = ? " + 
			"AND year = ? ";
		return super.select(sql, Projection.class, new Object[] {userid, new Integer(year)}, 0, -1);
	}
	
	public Projection selectProjection(String userid, String year) throws DaoException {
		Collection col = super.select("select projectionID, userID, year, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12 from projection where userID=? and year=?", Projection.class, new Object[] {userid, new Integer(year)}, 0, -1);
		Projection proj = null;
		if (col.size() == 1) {
			Iterator iterator = col.iterator();
			proj = (Projection) iterator.next(); 
		}
		return proj;
	}
	
	public void updateProjection(Projection proj) throws DaoException {
		super.update(
			"UPDATE projection " +
			"SET month1  = #month1#, " +
			"    month2  = #month2#, " +
			"    month3  = #month3#, " +
			"    month4  = #month4#, " +
			"    month5  = #month5#, " +
			"    month6  = #month6#, " +
			"    month7  = #month7#, " +
			"    month8  = #month8#, " +
			"    month9  = #month9#, " +
			"    month10 = #month10#, " +
			"    month11 = #month11#, " +
			"    month12 = #month12# " +
			"WHERE projectionID = #projectionID#"
		, proj);
	}
	
	public void deleteProjection(String projectionID) throws DaoException {
        
        DefaultDataObject obj = new DefaultDataObject();
        obj.setProperty("projectionID",projectionID);
        super.update("DELETE FROM projection WHERE projectionID=#projectionID#",obj);


    }
}