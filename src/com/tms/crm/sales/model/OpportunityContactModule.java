/*
 * Created on Jan 27, 2004
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
public class OpportunityContactModule extends DefaultModule {
	Log log = Log.getLog(getClass());
	
	public boolean addOpportunityContact(OpportunityContact opCon) {
		OpportunityContactDao dao = (OpportunityContactDao) getDao();
		try {
			dao.insertRecord(opCon);
			return (true);
		} catch (DaoException e) {
			log.error("Error adding OpportunityContact " + e.toString(), e);
			return (false);
		}
	}


    public void addClosedOpportunityContact(Contact opContact){
        OpportunityContactDao dao = (OpportunityContactDao) getDao();
        try {
            dao.insertClosedOpportunityContact(opContact);
        } catch (DaoException e) {
            log.error("Error adding ClosedOpportunityContact " + e.toString(), e);
        }

    }

	public boolean updateOpportunityContact(OpportunityContact opCon) {
		OpportunityContactDao dao = (OpportunityContactDao) getDao();
		try {
			dao.updateRecord(opCon);
			return (true);
		} catch (DaoException e) {
			log.error("Error updating OpportunityContact " + e.toString(), e);
			return (false);
		}
	}
	
	public OpportunityContact getOpportunityContact(String opportunityID, String contactID, String opportunityContactType) {
		OpportunityContactDao dao = (OpportunityContactDao) getDao();
		try {
			return (dao.selectRecord(opportunityID, contactID, opportunityContactType));
		} catch (DaoException e) {
			log.error("Error getting OpportunityContact " + e.toString(), e);
			return (null);
		}
	}
	
	public boolean deleteOpportunityContact(String opportunityID, String contactID, String opportunityContactType) {
		OpportunityContactDao dao = (OpportunityContactDao) getDao();
		try {
			OpportunityContact opCon = dao.selectRecord(opportunityID, contactID, opportunityContactType);
			dao.deleteRecord(opCon);
			return (true);
		} catch (DaoException e) {
			log.error("Error deleting OpportunityContact " + e.toString(), e);
			return (false);
		}
	}
	
	public boolean deleteOpportunityContacts(String opportunityID) {
		OpportunityContactDao dao = (OpportunityContactDao) getDao();
		try {
			dao.deleteRecord(opportunityID);
			return (true);
		} catch (DaoException e) {
			log.error("Error deleting OpportunityContact(s) " + e.toString(), e);
			return (false);
		}
	}
	
	public boolean deleteOpportunityContacts(String opportunityID, String opportunityContactType) {
		OpportunityContactDao dao = (OpportunityContactDao) getDao();
		try {
			dao.deleteRecords(opportunityID, opportunityContactType);
			return (true);
		} catch (DaoException e) {
			log.error("Error deleting OpportunityContact(s) " + e.toString(), e);
			return (false);
		}
	}
	
	public Collection getContactTypeCollection() {
		OpportunityContactDao dao = (OpportunityContactDao) getDao();
		try {
			return (dao.getContactTypeCollection());
		} catch (DaoException e) {
			log.error("Error getting ContactType Collection " + e.toString(), e);
			return (null);
		}
	}
	
	public Map getContactTypeMap() {
		OpportunityContactDao dao = (OpportunityContactDao) getDao();
		try {
			return (dao.getContactTypeMap());
		} catch (DaoException e) {
			log.error("Error getting ContactType Map " + e.toString(), e);
			return (null);
		}
	}
	
	public Collection listOpportunityContacts(String opportunityID, String opportunityContactType, String sort, boolean desc, int start, int rows) {
		OpportunityContactDao dao = (OpportunityContactDao) getDao();
		try {
			Collection col = dao.listRecords(opportunityID, opportunityContactType, sort, desc, start, rows);
			return (col);
		} catch (DaoException e) {
			log.error("Error listing OpportunityContact " + e.toString(), e);
			return (null);
		}
	}
	
	public int countOpportunityContacts(String opportunityID, String opportunityContactType) {
		OpportunityContactDao dao = (OpportunityContactDao) getDao();
		try {
			return dao.count(opportunityID, opportunityContactType);
		} catch (DaoException e) {
			log.error("Error counting OpportunityContact " + e.toString(), e);
			return(0);
		}
	}

    public int countContactOpportunities(String contactId){
        OpportunityContactDao dao = (OpportunityContactDao) getDao();
        try {
            return dao.countContactOpportunities(contactId);
        } catch (DaoException e) {
            log.error("Error counting contact's opportunities " + e.toString(), e);
        }
        return 0;
    }
    
    public int countContactByContactType(String contactTypeId){
        OpportunityContactDao dao = (OpportunityContactDao) getDao();
        try {
            return dao.countContactByContactType(contactTypeId);
        } catch (DaoException e) {
            log.error("Error counting contact's  " + e.toString(), e);
        }
        return 0;
    }
    
    public int countContactBySalutation(String salutationId){
        OpportunityContactDao dao = (OpportunityContactDao) getDao();
        try {
            return dao.countContactBySalutation(salutationId);
        } catch (DaoException e) {
            log.error("Error counting sale closure contact's  " + e.toString(), e);
        }
        return 0;
    }


    public Collection getOpportunities(String contactId){
        OpportunityContactDao dao = (OpportunityContactDao) getDao();
        try {
            return dao.selectOpportunites(contactId);
        } catch (DaoException e) {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }
        return null;

    }



}
