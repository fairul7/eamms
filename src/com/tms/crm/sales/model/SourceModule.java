/*
 * Created on Feb 26, 2004
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
public class SourceModule extends DefaultModule {
	Log log = Log.getLog(getClass());
	
	public boolean addSource(Source src) {
		SourceDao dao = (SourceDao) getDao();
		try {
			dao.insertRecord(src);
			return (true);
		} catch (DaoException e) {
			log.error("Error adding Source " + e.toString(), e);
			return (false);
		}
	}
	
	public boolean updateSource(Source src) {
		SourceDao dao = (SourceDao) getDao();
		try {
			dao.updateRecord(src);
			return (true);
		} catch (DaoException e) {
			log.error("Error updating Source " + e.toString(), e);
			return (false);
		}
	}
	
	public Source getSource(String sourceID) {
		SourceDao dao = (SourceDao) getDao();
		try {
			return (dao.selectRecord(sourceID));
		} catch (DaoException e) {
			log.error("Error getting Source " + e.toString(), e);
			return (null);
		}
	}
	
	public Collection listSources(String sort, boolean desc, int start, int rows) {
		SourceDao dao = (SourceDao) getDao();
		try {
			Collection col = dao.listRecords(sort, desc, start, rows);
			return (col);
		} catch (DaoException e) {
			log.error("Error listing Source " + e.toString(), e);
			return (null);
		}
	}
	
	public int countSources() {
		SourceDao dao = (SourceDao) getDao();
		try {
			return dao.count();
		} catch (DaoException e) {
			log.error("Error counting Source " + e.toString(), e);
			return(0);
		}
	}
	
	public boolean isUnique(Source src) {
		SourceDao dao = (SourceDao) getDao();
		try {
			return dao.isUnique(src);
		} catch (DaoException e) {
			log.error("Error isUnique Source " + e.toString(), e);
			return(false);
		}
	}

    public void deleteSource(String id){
        SourceDao dao = (SourceDao)getDao();
        try {
            dao.deleteSource(id);
        } catch (DaoException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);  //To change body of catch statement use Options | File Templates.
        }

    }
}