/*
 * Created on Jan 9, 2004
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
public class OpportunityArchiveModule extends DefaultModule {
	Log log = Log.getLog(getClass());
	
	public boolean addOpportunityArchive(OpportunityArchive oppArc) {
		OpportunityArchiveDao dao = (OpportunityArchiveDao) getDao();
		try {
			dao.insertRecord(oppArc);
			return (true);
		} catch (DaoException e) {
			log.error("Error adding Opportunity Archive " + e.toString(), e);
			return (false);
		}
	}
	
	public OpportunityArchive getOpportunityArchive(String opportunityID, Date modifiedDate) {
		OpportunityArchiveDao dao = (OpportunityArchiveDao) getDao();
		try {
			return (dao.selectRecord(opportunityID, modifiedDate));
		} catch (DaoException e) {
			log.error("Error getting Opportunity Archive " + e.toString(), e);
			return (null);
		}
	}
	
	public Collection listArchive(String opportunityID, String sort, boolean desc, int start, int rows) {
		OpportunityArchiveDao dao = (OpportunityArchiveDao) getDao();
		try {
			Collection col = dao.listRecords(opportunityID, sort, desc, start, rows);
			return (col);
		} catch (DaoException e) {
			log.error("Error listing Opportunity Archive " + e.toString(), e);
			return (null);
		}
	}
	
	public int countArchive(String opportunityID) {
		OpportunityArchiveDao dao = (OpportunityArchiveDao) getDao();
		try {
			return dao.count(opportunityID);
		} catch (DaoException e) {
			log.error("Error counting Opportunity Archive " + e.toString(), e);
			return(0);
		}
	}
	
	/**
	 * Note: cascade deletes the Opportunity Archive
	 */
	public boolean deleteOpportunityArchive(String opportunityID) {
		OpportunityArchiveDao dao = (OpportunityArchiveDao) getDao();
		try {
			dao.deleteRecord(opportunityID);
			return (true);
		} catch (DaoException e) {
			log.error("Error deleting OpportunityArchive " + e.toString(), e);
			return (false);
		}
	}
}
