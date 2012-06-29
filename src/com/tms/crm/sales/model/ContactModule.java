/*
 * Created on Dec 18, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.model;

import java.util.*;

import kacang.Application;
import kacang.model.*;
import kacang.util.Log;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ContactModule extends DefaultModule {
	Log log = Log.getLog(getClass());
	
	public boolean addContact(Contact con) {
		ContactDao dao = (ContactDao) getDao();
		try {
			dao.insertRecord(con);
			return (true);
		} catch (DaoException e) {
			log.error("Error adding Contact " + e.toString(), e);
			return (false);
		}
	}
	
	public boolean updateContact(Contact con) {
		ContactDao dao = (ContactDao) getDao();
		try {
			dao.updateRecord(con);
			return (true);
		} catch (DaoException e) {
			log.error("Error updating Contact " + e.toString(), e);
			return (false);
		}
	}
	
	public Contact getContact(String contactID) {
		ContactDao dao = (ContactDao) getDao();
		try {
			return (dao.selectRecord(contactID));
		} catch (DaoException e) {
			log.error("Error getting Contact " + e.toString(), e);
			return (null);
		}
	}
	
	public Collection getSalutationCollection() {
		ContactDao dao = (ContactDao) getDao();
		try {
			return (dao.getSalutationCollection());
		} catch (DaoException e) {
			log.error("Error getting Salutation Collection " + e.toString(), e);
			return (null);
		}
	}
	
	public Map getSalutationMap() {
		ContactDao dao = (ContactDao) getDao();
		try {
			return (dao.getSalutationMap());
		} catch (DaoException e) {
			log.error("Error getting Salutation Map " + e.toString(), e);
			return (null);
		}
	}
	
	public Collection listContacts(String keyword, String companyID, String opportunityID, String sort, boolean desc, int start, int rows) {
		ContactDao dao = (ContactDao) getDao();
		try {
			Collection col = dao.listRecords(keyword, companyID, opportunityID, sort, desc, start, rows);
			return (col);
		} catch (DaoException e) {
			log.error("Error listing Contact " + e.toString(), e);
			return (null);
		}
	}

    public Collection listClosedSaleContacts(String keyword, String companyID, String opportunityID, String sort, boolean desc, int start, int rows) {
        ContactDao dao = (ContactDao) getDao();
        try {
            Collection col = dao.listClosedSaleRecords(keyword, companyID, opportunityID, sort, desc, start, rows);
            return (col);
        } catch (DaoException e) {
            log.error("Error listing Contact " + e.toString(), e);
            return (null);
        }
    }



	public int countContacts(String keyword, String companyID, String opportunityID) {
		ContactDao dao = (ContactDao) getDao();
		try {
			return dao.count(keyword, companyID, opportunityID);
		} catch (DaoException e) {
			log.error("Error counting Contact " + e.toString(), e);
			return(0);
		}
	}

    public int countClosedSaleContacts(String keyword, String companyID, String opportunityID) {
        ContactDao dao = (ContactDao) getDao();
        try {
            return dao.countClosedSale(keyword, companyID, opportunityID);
        } catch (DaoException e) {
            log.error("Error counting Contact " + e.toString(), e);
            return(0);
        }
    }


    public void deleteCompanyContacts(String companyId){
        ContactDao dao = (ContactDao) getDao();
        try {
            dao.deleteCompanyContacts(companyId);
        } catch (DaoException e) {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }

    }
    
    public int countContactBySalutation(String salutationId){
    	ContactDao dao = (ContactDao) getDao();
        try {
            return dao.countContactBySalutation(salutationId);
        } catch (DaoException e) {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }
        return 0;

    }

}
