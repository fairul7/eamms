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
public class ContactTypeModule extends DefaultModule {
	Log log = Log.getLog(getClass());
	
	public boolean addContactType(ContactType ct) {
		ContactTypeDao dao = (ContactTypeDao) getDao();
		try {
			dao.insertRecord(ct);
			return (true);
		} catch (DaoException e) {
			log.error("Error adding ContactType " + e.toString(), e);
			return (false);
		}
	}
	
	public boolean updateContactType(ContactType ct) {
		ContactTypeDao dao = (ContactTypeDao) getDao();
		try {
			dao.updateRecord(ct);
			return (true);
		} catch (DaoException e) {
			log.error("Error updating ContactType " + e.toString(), e);
			return (false);
		}
	}
	
	public ContactType getContactType(String contactTypeID) {
		ContactTypeDao dao = (ContactTypeDao) getDao();
		try {
			return (dao.selectRecord(contactTypeID));
		} catch (DaoException e) {
			log.error("Error getting ContactType " + e.toString(), e);
			return (null);
		}
	}
	
	public Collection listContactTypes(String sort, boolean desc, int start, int rows) {
		ContactTypeDao dao = (ContactTypeDao) getDao();
		try {
			Collection col = dao.listRecords(sort, desc, start, rows);
			return (col);
		} catch (DaoException e) {
			log.error("Error listing ContactType " + e.toString(), e);
			return (null);
		}
	}
	
	public int countContactTypes() {
		ContactTypeDao dao = (ContactTypeDao) getDao();
		try {
			return dao.count();
		} catch (DaoException e) {
			log.error("Error counting ContactType " + e.toString(), e);
			return(0);
		}
	}
	
	public boolean isUnique(ContactType ct) {
		ContactTypeDao dao = (ContactTypeDao) getDao();
		try {
			return dao.isUnique(ct);
		} catch (DaoException e) {
			log.error("Error isUnique ContactType " + e.toString(), e);
			return(false);
		}
	}

    public void deleteContactType(String id){
        ContactTypeDao dao = (ContactTypeDao) getDao();
        try {
            dao.deleteContactType(id);
        } catch (DaoException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);  //To change body of catch statement use Options | File Templates.
        }
    }


}