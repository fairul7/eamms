/*
 * Created on Dec 16, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.model;

import java.util.*;
import kacang.Application;
import kacang.model.*;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class AccountDistributionDao extends DataSourceDao {
	public void insertRecord(AccountDistribution ad) throws DaoException {
		super.update(
			"INSERT INTO accountdistribution (opportunityID, distributionSequence, userID, distributionPercentage) " +
			"VALUES (#opportunityID#, #distributionSequence#, #userID#, #distributionPercentage#)"
		, ad);
	}
	
	public void deleteAccountDistribution(String opportunityID) throws DaoException {
		super.update(
			"DELETE FROM accountdistribution " +
			"WHERE opportunityID = ? "
		, new String[] {opportunityID});
	}
	
	public String getUsers(String opportunityID) throws DaoException {
		Collection col = listRecords(opportunityID, null, false, 0, -1);
		Iterator iterator = col.iterator();
		
		String userIDs = "";
		while (iterator.hasNext()) {
			AccountDistribution dist = (AccountDistribution) iterator.next();
			String userID = dist.getUserID();
			if (userIDs.length() != 0) {
				userIDs = userIDs + ", ";
			}
			userIDs = userIDs + "'" + userID + "'";
		}
		
		String names = "";
		Application application = Application.getInstance();
		AccountManagerModule module = (AccountManagerModule) application.getModule(AccountManagerModule.class);
		names = module.getNames(userIDs);
		
		return (names);
	}
	
	public String[] getUserIDs(String opportunityID) throws DaoException {
		Collection col = listRecords(opportunityID, null, false, 0, -1);
		Iterator iterator = col.iterator();
		
		String[] userIDs = new String[col.size()];
		for (int i=0; i<col.size(); i++) {
			AccountDistribution dist = (AccountDistribution) iterator.next();
			String userID = dist.getUserID();
			userIDs[i] = userID;
		}
		
		return userIDs;
	}
	
	public Collection listRecords(String opportunityID, String sort, boolean desc, int start, int rows) throws DaoException {
		Collection col = super.select(
			"SELECT opportunityID, distributionSequence, userID, distributionPercentage " +
			"FROM accountdistribution " +
			"WHERE opportunityID = ? " +
			"ORDER BY distributionSequence"
		, AccountDistribution.class, new String[] {opportunityID}, start, rows);
		
		return (col);
	}

    public int selectOpportunityDistributionPercentage(String userId, String opportunityID) throws DaoException {
        Collection col = super.select(
                "SELECT  distributionPercentage as percentage " +
                "FROM accountdistribution " +
                "WHERE opportunityID = ? AND " +
                "userID = ? "
                , HashMap.class, new String[] {opportunityID,userId},0, -1);

        return (Integer.parseInt(((HashMap)col.iterator().next()).get("percentage").toString()));
    }



	public int count(String opportunityID) throws DaoException {
		Collection list = super.select("SELECT COUNT(*) AS total FROM accountdistribution WHERE opportunityID = ? ", HashMap.class, new String[] {opportunityID}, 0, 1);
		HashMap map = (HashMap)list.iterator().next();
		return Integer.parseInt(map.get("total").toString());
	}
}
