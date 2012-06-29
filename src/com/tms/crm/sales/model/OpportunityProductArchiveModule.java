/*
 * Created on Jan 19, 2004
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
public class OpportunityProductArchiveModule extends DefaultModule {
	Log log = Log.getLog(getClass());
	
	public boolean archive(String opportunityID, Collection colRecords) {
		OpportunityProductArchiveDao dao = (OpportunityProductArchiveDao) getDao();
		try {
			Date today = DateUtil.getDate();
			int archiveSet = dao.nextArchiveSet(opportunityID);
			
			// Check that OpportunityProduct not archived before
			if (!dao.hasArchive(opportunityID, today)) {
				OpportunityProductArchive opa = new OpportunityProductArchive();
				opa.setOpportunityID(opportunityID);
				opa.setArchiveSet(archiveSet);
				opa.setArchivedOn(today);
				dao.insertRecord(opa);
				
				Iterator iterator = colRecords.iterator();
				while (iterator.hasNext()) {
					OpportunityProduct op = (OpportunityProduct) iterator.next();
					OpportunityProductArchiveItem opai = new OpportunityProductArchiveItem(op, archiveSet);
					dao.insertRecord(opai);
				}
			}
			
			return (true);
		} catch (DaoException e) {
			log.error("Error archiving OpportunityProduct (part 2) " + e.toString(), e);
			return (false);
		}
	}
	
	public boolean deleteOpportunityProductArchive(String opportunityID) {
		OpportunityProductArchiveDao dao = (OpportunityProductArchiveDao) getDao();
		try {
			dao.deleteRecord(opportunityID);
			return (true);
		} catch (DaoException e) {
			log.error("Error deleting OpportunityProductArchive " + e.toString(), e);
			return (false);
		}
	}
	
	public int countOpportunityArchiveProductByProduct(String productID) {
		OpportunityProductArchiveDao dao = (OpportunityProductArchiveDao) getDao();
		try {
			return dao.countOpportunityArchiveProductByProduct(productID);
		} catch (DaoException e) {
			log.error("Error counting Archive OpportunityProduct " + e.toString(), e);
			return(0);
		}
	}
	
	public int countOpportunityArchiveProductByCategory(String categoryID) {
		OpportunityProductArchiveDao dao = (OpportunityProductArchiveDao) getDao();
		try {
			return dao.countOpportunityArchiveProductByCategory(categoryID);
		} catch (DaoException e) {
			log.error("Error counting Archive OpportunityProduct " + e.toString(), e);
			return(0);
		}
	}
}
