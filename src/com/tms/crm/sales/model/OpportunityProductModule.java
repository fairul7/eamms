/*
 * Created on Jan 16, 2004
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
public class OpportunityProductModule extends DefaultModule {
	Log log = Log.getLog(getClass());
	
	public boolean addOpportunityProduct(OpportunityProduct op) {
		OpportunityProductDao dao = (OpportunityProductDao) getDao();
		try {
			archiveIfNeeded(op.getOpportunityID(), op.getModifiedDate());
			op.setProductSeq(dao.nextSeq(op.getOpportunityID()));
			dao.insertRecord(op);
			updateMainValue(op.getOpportunityID());
			return (true);
		} catch (DaoException e) {
			log.error("Error adding OpportunityProduct " + e.toString(), e);
			return (false);
		}
	}
	
	public boolean updateOpportunityProduct(OpportunityProduct op) {
		OpportunityProductDao dao = (OpportunityProductDao) getDao();
		try {
			archiveIfNeeded(op.getOpportunityID(), op.getModifiedDate());
			dao.updateRecord(op);
			updateMainValue(op.getOpportunityID());
			return (true);
		} catch (DaoException e) {
			log.error("Error adding OpportunityProduct " + e.toString(), e);
			return (false);
		}
	}
	
	public boolean deleteOpportunityProduct(String opportunityID) {
		OpportunityProductDao dao = (OpportunityProductDao) getDao();
		try {
			dao.deleteRecord(opportunityID);
			return (true);
		} catch (DaoException e) {
			log.error("Error deleting OpportunityProduct " + e.toString(), e);
			return (false);
		}
	}
	
	public boolean deleteOpportunityProduct(String opportunityID, int productSeq) {
		OpportunityProductDao dao = (OpportunityProductDao) getDao();
		try {
			OpportunityProduct op = dao.selectRecord(opportunityID, productSeq);
			archiveIfNeeded(op.getOpportunityID(), op.getModifiedDate());
			dao.deleteRecord(op);
			updateMainValue(op.getOpportunityID());
			return (true);
		} catch (DaoException e) {
			log.error("Error deleting OpportunityProduct " + e.toString(), e);
			return (false);
		}
	}
	
	public OpportunityProduct getOpportunityProduct(String opportunityID, int productSeq) {
		OpportunityProductDao dao = (OpportunityProductDao) getDao();
		try {
			return (dao.selectRecord(opportunityID, productSeq));
		} catch (DaoException e) {
			log.error("Error getting OpportunityProduct " + e.toString(), e);
			return (null);
		}
	}
	
	public Collection listOpportunityProduct(String opportunityID, String sort, boolean desc, int start, int rows) {
		OpportunityProductDao dao = (OpportunityProductDao) getDao();
		try {
			Collection col = dao.listRecords(opportunityID, sort, desc, start, rows);
			return (col);
		} catch (DaoException e) {
			log.error("Error listing OpportunityProduct " + e.toString(), e);
			return (null);
		}
	}
	
	public int countOpportunityProduct(String opportunityID) {
		OpportunityProductDao dao = (OpportunityProductDao) getDao();
		try {
			return dao.count(opportunityID);
		} catch (DaoException e) {
			log.error("Error counting OpportunityProduct " + e.toString(), e);
			return(0);
		}
	}
	
	public int countOpportunityProductByCategory(String categoryID) {
		OpportunityProductDao dao = (OpportunityProductDao) getDao();
		try {
			return dao.countOpportunityProductByCategory(categoryID);
		} catch (DaoException e) {
			log.error("Error counting OpportunityProduct " + e.toString(), e);
			return(0);
		}
	}
	
	public int countOpportunityProductByProduct(String productID) {
		OpportunityProductDao dao = (OpportunityProductDao) getDao();
		try {
			return dao.countOpportunityProductByProduct(productID);
		} catch (DaoException e) {
			log.error("Error counting OpportunityProduct " + e.toString(), e);
			return(0);
		}
	}
	
	private void archiveIfNeeded(String opportunityID, Date modifiedDate) {
		OpportunityProductDao dao = (OpportunityProductDao) getDao();
		
		try {
			Collection col = dao.listRecords(opportunityID, "productSeq", false, 0, -1);
			
			// archive the OpportunityProduct if necessary
			Application application = Application.getInstance();
			OpportunityProductArchiveModule archiveModule = (OpportunityProductArchiveModule) application.getModule(OpportunityProductArchiveModule.class);
			archiveModule.archive(opportunityID, col);
			
		} catch (DaoException e) {
			log.error("Error archiving OpportunityProduct " + e.toString(), e);
		}
	}
	
	private void updateMainValue(String opportunityID) {
		OpportunityProductDao dao = (OpportunityProductDao) getDao();
		try {
			Collection col = dao.listRecords(opportunityID, "productSeq", false, 0, -1);
			double newValue = 0;
			
			Iterator iterator = col.iterator();
			while (iterator.hasNext()) {
				OpportunityProduct op = (OpportunityProduct) iterator.next();
				newValue = newValue + op.getOpValue();
			}
			
			Application application = Application.getInstance();
			OpportunityModule opportunityModule = (OpportunityModule) application.getModule(OpportunityModule.class);
			opportunityModule.updateOpportunity(opportunityID, newValue);
			
		} catch (DaoException e) {
			log.error("Error updateMainValue OpportunityProduct " + e.toString(), e);
		}
	}
}
