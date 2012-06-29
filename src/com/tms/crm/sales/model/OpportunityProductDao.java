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
public class OpportunityProductDao extends DataSourceDao {
	public void init() throws DaoException {
        super.init();    //To change body of overridden methods use File | Settings | File Templates.
        try{
            super.update("ALTER TABLE opportunityproduct ADD category varchar(255)",null);
        }catch(DaoException e){
            
        }
	}
	
	public void insertRecord(OpportunityProduct op) throws DaoException {
		super.update(
				"INSERT INTO opportunityproduct (opportunityID, productSeq, productID, opDesc, opValue, category, modifiedDate, modifiedBy) " +
				"VALUES (#opportunityID#, #productSeq#, #productID#, #opDesc#, #opValue#, #category#, #modifiedDate#, #modifiedBy#)"
			, op);
	}
	
	public void updateRecord(OpportunityProduct op) throws DaoException {
		super.update(
			"UPDATE opportunityproduct " +
			"SET productID    = #productID#, " +
			"    opDesc       = #opDesc#, " +
			"    category       = #category#, " +
			"    opValue      = #opValue#, " +
			"    modifiedDate = #modifiedDate#, " +
			"    modifiedBy   = #modifiedBy# " +
			"WHERE opportunityID = #opportunityID# " +
			"  AND productSeq = #productSeq#"
		, op);
	}
	
	public void deleteRecord(String opportunityID) throws DaoException {
		super.update("DELETE FROM opportunityproduct WHERE opportunityID = ?", new String[] {opportunityID});
	}
	
	public void deleteRecord(OpportunityProduct op) throws DaoException {
		super.update("DELETE FROM opportunityproduct WHERE opportunityID = #opportunityID# AND productSeq = #productSeq#", op);
		rePosition(op.getOpportunityID(), op.getProductSeq());
	}
	
	private void rePosition(String opportunityID, int productSeq) throws DaoException {
		super.update(
			"UPDATE opportunityproduct " +
			"SET productSeq = productSeq - 1 " +
			"WHERE opportunityID = ? " +
			"  AND productSeq > ? "
		, new Object[] {opportunityID, new Integer(productSeq)});
	}
	
	public OpportunityProduct selectRecord(String opportunityID, int productSeq) throws DaoException {
		Collection col = super.select(
			"SELECT opportunityID, productSeq, productID, opDesc, opValue, op.category, cat.categoryName, modifiedDate, modifiedBy " +
			"FROM opportunityproduct op, category cat " +
			"WHERE opportunityID = ? " +
			"  AND productSeq    = ? " +
			"AND cat.categoryID=op.category "
		, OpportunityProduct.class, new Object[] {opportunityID, new Integer(productSeq)}, 0, 1);
		
		OpportunityProduct op = null;
		
		if (col.size() == 1) {
			Iterator iterator = col.iterator();
			op = (OpportunityProduct) iterator.next(); 
		
		}
		
		return (op);
	}
	
	public Collection listRecords(String opportunityID, String sort, boolean desc, int start, int rows) throws DaoException {
		String orderBy = "ORDER BY " + ((sort != null) ? sort : "productSeq") + ((desc) ? " DESC" : "");
	
		Collection col = super.select(
			"SELECT productName, opportunityID, productSeq, op.productID, opDesc," +
			" opValue, op.category, cat.categoryID, cat.categoryName, modifiedDate, modifiedBy " +
			"FROM opportunityproduct op ,product p, category cat " +
			"WHERE opportunityID = ? AND p.productID = op.productID AND cat.categoryID=op.category " +
			orderBy
		, OpportunityProduct.class, new String[] {opportunityID}, start, rows);
		
		
		return (col);
		
	}
	
	public int count(String opportunityID) throws DaoException {
		Collection list = super.select("SELECT COUNT(*) AS total FROM opportunityproduct WHERE opportunityID = ? ", 
							HashMap.class, new String[] {opportunityID}, 0, 1);
		HashMap map = (HashMap)list.iterator().next();
		return Integer.parseInt(map.get("total").toString());
	}
	
	public int countOpportunityProductByProduct(String productID) throws DaoException {
		Collection list = super.select("SELECT COUNT(*) AS total FROM opportunityproduct WHERE productID = ? ", 
							HashMap.class, new String[] {productID}, 0, 1);
		HashMap map = (HashMap)list.iterator().next();
		return Integer.parseInt(map.get("total").toString());
	}
	
	public int nextSeq(String opportunityID) throws DaoException {
		return count(opportunityID) + 1;
	}
	
	public int countOpportunityProductByCategory(String category) throws DaoException {
		Collection list = super.select("SELECT COUNT(*) AS total FROM product WHERE category = ? ", 
							HashMap.class, new String[] {category}, 0, 1);
		HashMap map = (HashMap)list.iterator().next();
		return Integer.parseInt(map.get("total").toString());
	}
}
