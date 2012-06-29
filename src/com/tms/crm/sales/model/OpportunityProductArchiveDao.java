/*
 * Created on Jan 16, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.model;

import java.util.*;

import kacang.model.*;


/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class OpportunityProductArchiveDao extends DataSourceDao {
	public void insertRecord(OpportunityProductArchive opa) throws DaoException {
		super.update(
			"INSERT INTO opportunityproductarchive (opportunityID, archiveSet, archivedOn) " +
			"VALUES (#opportunityID#, #archiveSet#, #archivedOn#)"
		, opa);
	}
	
	public void insertRecord(OpportunityProductArchiveItem opai) throws DaoException {
		super.update(
			"INSERT INTO opportunityproductarchiveitem (opportunityID, productSeq, productID, opDesc, opValue, modifiedDate, modifiedBy, archiveSet) " +
			"VALUES (#opportunityID#, #productSeq#, #productID#, #opDesc#, #opValue#, #modifiedDate#, #modifiedBy#, #archiveSet#)"
		, opai);
	}
	
	public void deleteRecord(String opportunityID) throws DaoException {
		deleteRecord_ProductArchiveItem(opportunityID);
		deleteRecord_ProductArchive(opportunityID);
	}
	
	private void deleteRecord_ProductArchive(String opportunityID) throws DaoException {
		super.update("DELETE FROM opportunityproductarchive WHERE opportunityID = ?", new String[] {opportunityID});
	}
	
	private void deleteRecord_ProductArchiveItem(String opportunityID) throws DaoException {
		super.update("DELETE FROM opportunityproductarchiveitem WHERE opportunityID = ?", new String[] {opportunityID});
	}
	
	public int count(String opportunityID) throws DaoException {
		Collection list = super.select("SELECT COUNT(*) AS total FROM opportunityproductarchive WHERE opportunityID = ? ", 
							HashMap.class, new String[] {opportunityID}, 0, 1);
		HashMap map = (HashMap)list.iterator().next();
		return Integer.parseInt(map.get("total").toString());
	}
	
	public boolean hasArchive(String opportunityID, Date archivedOn) throws DaoException {
		Collection list = super.select("SELECT COUNT(*) AS total FROM opportunityproductarchive WHERE opportunityID = ? AND archivedOn = ? ", 
							HashMap.class, new Object[] {opportunityID, archivedOn}, 0, 1);
		HashMap map = (HashMap)list.iterator().next();
		
		if (Integer.parseInt(map.get("total").toString()) == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	public int countOpportunityArchiveProductByProduct(String productID) throws DaoException {
		Collection list = super.select("SELECT COUNT(*) AS total FROM opportunityproductarchiveitem WHERE productID = ? ", 
							HashMap.class, new String[] {productID}, 0, 1);
		HashMap map = (HashMap)list.iterator().next();
		return Integer.parseInt(map.get("total").toString());
	}
	
	public int nextArchiveSet(String opportunityID) throws DaoException {
		return count(opportunityID) + 1;
	}
	
	public int countOpportunityArchiveProductByCategory(String categoryID) throws DaoException {
		Collection list = super.select("SELECT COUNT(*) AS total FROM product WHERE category = ? ", 
							HashMap.class, new String[] {categoryID}, 0, 1);
		HashMap map = (HashMap)list.iterator().next();
		return Integer.parseInt(map.get("total").toString());
	}
}
