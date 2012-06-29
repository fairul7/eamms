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
public class SalutationModule extends DefaultModule {
	Log log = Log.getLog(getClass());
	
	public boolean addSalutation(Salutation salu) {
		SalutationDao dao = (SalutationDao) getDao();
		try {
			dao.insertRecord(salu);
			return (true);
		} catch (DaoException e) {
			log.error("Error adding Salutation " + e.toString(), e);
			return (false);
		}
	}
	
	public boolean updateSalutation(Salutation salu) {
		SalutationDao dao = (SalutationDao) getDao();
		try {
			dao.updateRecord(salu);
			return (true);
		} catch (DaoException e) {
			log.error("Error updating Salutation " + e.toString(), e);
			return (false);
		}
	}
	
	public Salutation getSalutation(String salutationID) {
		SalutationDao dao = (SalutationDao) getDao();
		try {
			return (dao.selectRecord(salutationID));
		} catch (DaoException e) {
			log.error("Error getting Salutation " + e.toString(), e);
			return (null);
		}
	}
	
	public Collection listSalutations(String sort, boolean desc, int start, int rows) {
		SalutationDao dao = (SalutationDao) getDao();
		try {
			Collection col = dao.listRecords(sort, desc, start, rows);
			return (col);
		} catch (DaoException e) {
			log.error("Error listing Salutation " + e.toString(), e);
			return (null);
		}
	}
	
	public int countSalutations() {
		SalutationDao dao = (SalutationDao) getDao();
		try {
			return dao.count();
		} catch (DaoException e) {
			log.error("Error counting Salutation " + e.toString(), e);
			return(0);
		}
	}
	
	public boolean isUnique(Salutation salu) {
		SalutationDao dao = (SalutationDao) getDao();
		try {
			return dao.isUnique(salu);
		} catch (DaoException e) {
			log.error("Error isUnique Salutation " + e.toString(), e);
			return(false);
		}
	}

    public void deleteSalutation(String id){
        SalutationDao dao = (SalutationDao) getDao();
        try {
            dao.deleteSalutation(id);
        } catch (DaoException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);  //To change body of catch statement use Options | File Templates.
        }
    }
}