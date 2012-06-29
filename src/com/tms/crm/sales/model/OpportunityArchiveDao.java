/*
 * Created on Jan 9, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.model;

import java.util.*;
import kacang.*;
import kacang.model.*;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class OpportunityArchiveDao extends DataSourceDao {
	public void insertRecord(OpportunityArchive oppArc) throws DaoException {
		super.update(
			"INSERT INTO opportunityarchive (opportunityID, opportunityName, opportunityStatus, opportunityStage, opportunityValue, opportunityStart, opportunityEnd, opportunityLastRemarks, hasPartner, partnerCompanyID, modifiedDate, modifiedBy) " +
			"VALUES (#opportunityID#, #opportunityName#, #opportunityStatus#, #opportunityStage#, #opportunityValue#, #opportunityStart#, #opportunityEnd#, #opportunityLastRemarks#, #hasPartner#, #partnerCompanyID#, #modifiedDate#, #modifiedBy#)"
		, oppArc);
	}
	
	public OpportunityArchive selectRecord(String opportunityID, Date modifiedDate) throws DaoException {
		Collection col = super.select(
			"SELECT opportunityID, opportunityName, opportunityStatus, opportunityStage, opportunityValue, opportunityStart, opportunityEnd, opportunityLastRemarks, hasPartner, partnerCompanyID, modifiedDate, modifiedBy " +
			"FROM opportunityarchive " +
			"WHERE opportunityID = ? " +
			"  AND modifiedDate = ? "
		, OpportunityArchive.class, new Object[] {opportunityID, modifiedDate}, 0, 1);
		
		OpportunityArchive oppArc = null;
		if (col.size() == 1) {
			Iterator iterator = col.iterator();
			oppArc = (OpportunityArchive) iterator.next(); 
		}
		
		return (oppArc);
	}
	
	/**
	 * Note: cascade deletes the Opportunity Archive
	 */
	public void deleteRecord(String opportunityID) throws DaoException {
		Application application = Application.getInstance();
		OpportunityProductArchiveModule opaModule = (OpportunityProductArchiveModule) application.getModule(OpportunityProductArchiveModule.class);
		
		boolean status = opaModule.deleteOpportunityProductArchive(opportunityID);
		if (!status) {
			throw (new DaoException());
		}
		deleteRecord_OpportunityArchive(opportunityID);
	}
	
	private void deleteRecord_OpportunityArchive(String opportunityID) throws DaoException {
		super.update("DELETE FROM opportunityarchive WHERE opportunityID = ?", new String[] {opportunityID});
	}
	
	public Collection listRecords(String opportunityID, String sort, boolean desc, int start, int rows) throws DaoException {
		Collection col = super.select(
			"SELECT opportunityID, opportunityName, opportunityStatus, opportunityStage, opportunityValue, opportunityStart, opportunityEnd, opportunityLastRemarks, hasPartner, partnerCompanyID, modifiedDate, modifiedBy " +
			"FROM opportunityarchive " +
			"WHERE opportunityID = ? " + 
			"ORDER BY " + ((sort != null) ? sort : "modifiedDate") + ((desc) ? " DESC" : "")
		, Opportunity.class, new String[] {opportunityID}, start, rows);
		
		return (col);
	}
	
	public int count(String opportunityID) throws DaoException {
		Collection list = super.select("SELECT COUNT(*) AS total FROM opportunityarchive WHERE opportunityID = ? ", HashMap.class, new String[] {opportunityID}, 0, 1);
		HashMap map = (HashMap)list.iterator().next();
		return Integer.parseInt(map.get("total").toString());
	}
}
