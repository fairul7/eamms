/*
 * Created on Apr 23, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.model;

import java.util.*;
import kacang.Application;
import kacang.util.Log;
import kacang.model.*;
import org.apache.commons.collections.SequencedHashMap;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SalesGroupModule extends DefaultModule {
    private SalesGroupDao dao = null;

    public void addGroup(SalesGroup group) throws SalesGroupException {
        dao = (SalesGroupDao) getDao();
        try {
            dao.insertGroup(group);
        } catch (DaoException e) {
            throw new SalesGroupException("Error adding group",e);
        }
    }

    public void updateGroup(SalesGroup group) throws SalesGroupException {
        dao = (SalesGroupDao) getDao();
        try {
            dao.deleteGroupUsers(group);
            dao.updateGroup(group);
        } catch (DaoException e) {
            throw new SalesGroupException("Error updating group",e);
        }
    }

    public Collection  listGroups(String filter,String sort,boolean desc,int startRow,int rows){
        dao = (SalesGroupDao) getDao();
        try {
            return dao.listGroups(filter, sort, desc, startRow, rows);
        } catch (DaoException e) {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }
        return null;
    }

    public void deleteGroup(String groupId) throws SalesGroupException {
        dao = (SalesGroupDao) getDao();
        try {
            dao.deleteGroup(groupId);
        } catch (DaoException e) {
            throw new SalesGroupException("Error deleting group "+e.getMessage(),e);
        }
    }

    public int countGroups(String filter){
        dao = (SalesGroupDao) getDao();
        try {
            return dao.countGroups(filter);
        } catch (DaoException e) {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }
        return 0;
    }



	public Vector getSalesAndNonSalesGroups(String userID) {
		SalesGroupDao dao = (SalesGroupDao) getDao();
		AccountManagerModule am = (AccountManagerModule) Application.getInstance().getModule(AccountManagerModule.class);
		try {
			Collection userGroups;
			if (userID == null) {
				userGroups = am.getGroups();
			} else {
				userGroups = am.getGroups(userID);
			}
			Hashtable  salesGroups = dao.getSalesGroups();
			
			Vector vec         = new Vector();
			Vector vecSales    = new Vector();
			Vector vecNonSales = new Vector();
			Iterator iterator = userGroups.iterator();
			while (iterator.hasNext()) {
				SalesGroup sg = (SalesGroup) iterator.next();
				if (salesGroups.containsKey(sg.getId())) {
					vecSales.add(sg);
				} else {
					vecNonSales.add(sg);
				}
			}
			vec.add(vecSales);
			vec.add(vecNonSales);
			return (vec);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error getting Groups " + e.toString(), e);
			return (null);
		}
	}
	
	public Collection getSalesGroups(String userID) {
        dao = (SalesGroupDao) getDao();
        try {
            return dao.selectSalesGroups(userID);
        } catch (DaoException e) {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }
        return null;
        /*Vector vec = getSalesAndNonSalesGroups(userID);
		if (vec != null) {
			return (Collection) vec.get(0);
		} else {
			return null;
		}*/
	}

    public SalesGroup getSalesGroup(String groupId) throws SalesGroupException {
        dao = (SalesGroupDao) getDao();
        try {
            return dao.selectSalesGroup(groupId);
        } catch (DaoException e) {
            throw new SalesGroupException("Error getting group with ID : "+groupId +e.getMessage(),e);
            //Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }

    }

	public Collection getNonSalesGroups(String userID) {
		Vector vec = getSalesAndNonSalesGroups(userID);
		if (vec != null) {
			return (Collection) vec.get(1);
		} else {
			return null;
		}
	}

	/**
	 * @return a Hashtable of SequencedHashMap
	 */
	public Hashtable getSalesGroupsMap() {
		Vector vec = getSalesAndNonSalesGroups(null);

		Hashtable ht = new Hashtable();
		for (int i=0; i<vec.size(); i++) {
			SequencedHashMap shm = new SequencedHashMap();

			Vector tempVec = (Vector) vec.get(i);
			for (int j=0; j<tempVec.size(); j++) {
				SalesGroup sg = (SalesGroup) tempVec.get(j);
				shm.put(sg.getId(), sg.getGroupName());
			}

			if (i == 0) {
				ht.put("salesGroup", shm);
			} else {
				ht.put("nonSalesGroup", shm);
			}
		}
		
		return ht;
	}
	
	public void updateSalesGroup(Map map) {
		SalesGroupDao dao = (SalesGroupDao) getDao();
		try {
			dao.deleteRecords();
			
			Set keySet = map.keySet();
			Iterator iterator = keySet.iterator();
			while (iterator.hasNext()) {
				SalesGroup sg = new SalesGroup();
				sg.setId((String) iterator.next());
				dao.insertRecord(sg);
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error updating Groups " + e.toString(), e);
		}
	}

}