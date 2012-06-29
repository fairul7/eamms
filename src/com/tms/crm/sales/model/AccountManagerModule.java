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
import com.tms.crm.sales.misc.*;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class AccountManagerModule extends DefaultModule {
	public Collection getAccountManagers() {
		AccountManagerDao dao = (AccountManagerDao) getDao();
		try {
			Collection col = dao.listRecords();
			return (getSalesPeople(col));
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error getting Account Managers " + e.toString(), e);
			return (null);
		}
	}




	public Collection getAccountManagers(String groupID) {
		AccountManagerDao dao = (AccountManagerDao) getDao();
		try {
			Collection col = dao.getGroupMembers(groupID);
			return (getSalesPeople(col));
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error getting Account Managers (groupID) " + e.toString(), e);
			return (null);
		}
	}
	
	private Collection getSalesPeople(Collection col) {
		Vector     vec = new Vector(); 
		Iterator iterator = col.iterator();
		
		// Filter out those who cannot make sales
		while (iterator.hasNext()) {
			AccountManager acManager = (AccountManager) iterator.next();
			String userID = acManager.getId();
			if (AccessUtil.isSalesManager(userID) || AccessUtil.isSalesPerson(userID) || AccessUtil.isExternalSalesPerson(userID)) {
				vec.add(acManager);
			}
		}
		return (vec);
	}
	
	public AccountManager getAccountManager(String userID) {
		AccountManagerDao dao = (AccountManagerDao) getDao();
		try {
			return (dao.selectRecord(userID));
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error getting Account Manager " + e.toString(), e);
			return (null);
		}
	}
	
	public Map getAccountManagersMap() {
		Collection col = getAccountManagers();
		Map map = new HashMap();
		
		Iterator iterator = col.iterator();
		while (iterator.hasNext()) {
			AccountManager aManager =(AccountManager) iterator.next();
			map.put(aManager.getId(), aManager.getFullName());
		}
		
		return (map);
	}
	
	public String getNames(String idList) {
		AccountManagerDao dao = (AccountManagerDao) getDao();
		try {
			return (dao.selectNames(idList));
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error getting Account Manager names " + e.toString(), e);
			return (null);
		}
	}
	
	public SalesGroup getGroup(String groupID) {
		AccountManagerDao dao = (AccountManagerDao) getDao();
		try {
			return (dao.getGroup(groupID));
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error getting Group " + e.toString(), e);
			return (null);
		}
	}
	
	public Collection getGroups() {
		AccountManagerDao dao = (AccountManagerDao) getDao();
		try {
			return (dao.getGroups());
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error getting Groups " + e.toString(), e);
			return (null);
		}
	}
	
	public Collection getGroups(String userID) {
		AccountManagerDao dao = (AccountManagerDao) getDao();
		try {
			return (dao.getGroups(userID));
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error getting Groups " + e.toString(), e);
			return (null);
		}
	}
	
	public Collection getGroupMembers(String groupID) {
		AccountManagerDao dao = (AccountManagerDao) getDao();
		try {
			return (dao.getGroupMembers(groupID));
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error getting Group Members " + e.toString(), e);
			return (null);
		}
	}

    public Collection getSales(String userId,Date from, Date to){
        AccountManagerDao dao = (AccountManagerDao) getDao();
        try {
            return dao.selectSales(userId,from,to );
        } catch (DaoException e) {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }
        return null;
    }

}
