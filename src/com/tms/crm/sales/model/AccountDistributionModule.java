/*
 * Created on Dec 16, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.model;

import java.util.*;
import kacang.model.*;
import kacang.util.Log;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class AccountDistributionModule extends DefaultModule {
	Log log = Log.getLog(getClass());
	
	public boolean addAccountDistribution(AccountDistribution ad) {
		AccountDistributionDao dao = (AccountDistributionDao) getDao();
		try {
			dao.insertRecord(ad);
			return (true);
		} catch (DaoException e) {
			log.error("Error adding Account Distribution " + e.toString(), e);
			return (false);
		}
	}
	
	public void deleteAccountDistribution(String opportunityID) {
		AccountDistributionDao dao = (AccountDistributionDao) getDao();
		try {
			dao.deleteAccountDistribution(opportunityID);
		} catch (DaoException e) {
			log.error("Error deleting Account Distribution " + e.toString(), e);
		}
	}
	
	public String getUsers(String opportunityID) {
		AccountDistributionDao dao = (AccountDistributionDao) getDao();
		try {
			return (dao.getUsers(opportunityID));
		} catch (DaoException e) {
			log.error("Error getting Account Distribution users " + e.toString(), e);
			return (null);
		}
	}
	
	public String[] getUserIDs(String opportunityID) {
		AccountDistributionDao dao = (AccountDistributionDao) getDao();
		try {
			return (dao.getUserIDs(opportunityID));
		} catch (DaoException e) {
			log.error("Error getting Account Distribution userIDs " + e.toString(), e);
			return (null);
		}
	}
	
	public Collection getAccountDistribution(String opportunityID, String sort, boolean desc, int start, int rows) {
		AccountDistributionDao dao = (AccountDistributionDao) getDao();
		try {
			return (dao.listRecords(opportunityID, sort, desc, start, rows));
		} catch (DaoException e) {
			log.error("Error getting Account Distribution " + e.toString(), e);
			return (null);
		}
	}

    public int getOpportunityDistributionPercentage(String userId, String opportunityId){
        AccountDistributionDao dao = (AccountDistributionDao) getDao();
        try {
            return dao.selectOpportunityDistributionPercentage(userId,opportunityId);
        } catch (DaoException e) {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }

        return 0;
    }

	public int countAccountDistribution(String opportunityID) {
		AccountDistributionDao dao = (AccountDistributionDao) getDao();
		try {
			return dao.count(opportunityID);
		} catch (DaoException e) {
			log.error("Error counting Account Distribution " + e.toString(), e);
			return(0);
		}
	}
}
