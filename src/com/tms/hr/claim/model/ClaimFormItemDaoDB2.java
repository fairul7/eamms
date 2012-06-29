package com.tms.hr.claim.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import kacang.model.DaoException;

public class ClaimFormItemDaoDB2 extends ClaimFormItemDao{
	
	public double selectTotalByMonthAndCategory(String userId,
            String categoryId, String type, String monthStr)
		throws DaoException {
		double dbl = 0.0;
		
		String[] monthStrBreak = monthStr.split("-");
		
		String sql =
			"SELECT SUM(t.amount) as total FROM claim_form_item t LEFT JOIN claim_form_index i ON " +
			"t.formId=i.id WHERE t.categoryId=?   AND standardTypeId=? " +
			"AND i.userOwner=? " + " AND MONTH(i.claimMonth) = " + Integer.parseInt(monthStrBreak[1]) +
			" AND YEAR(i.claimMonth) = " + Integer.parseInt(monthStrBreak[0]) + " AND i.state='clo'";
		
		//Collection col = super.select(sql,HashMap.class,new String[] {categoryId,userId},0,-1);
		Collection col = super.select(sql, HashMap.class,
		new String[] { categoryId, type, userId }, 0, -1);
		HashMap map = (HashMap) col.iterator().next();
		
		try {
			String s = map.get("total").toString();
		
			if (s != null) {
				dbl = Double.parseDouble(s);
			}
		} catch (Exception e) {
		}
		
		return dbl;
	}
	
	public ClaimFormItem retriveDraftItem(String id) throws DaoException{

        Collection col = super.select("SELECT id, formId, categoryId, standardTypeId, projectId, timeFrom, timeTo, timeFinancial, " +
        		"currency, amount, qty, unitPrice, uom, description, remarks, rejectReason, state, status, travelFrom, travelTo " +
        		"from claim_form_item WHERE id=?", ClaimFormItem.class, new Object[]{id}, 0,1);

         if (col.size() > 0) {
            Iterator iterator = col.iterator();
            ClaimFormItem obj = (ClaimFormItem) iterator.next();
            return obj;
        }
        else
         return null;

    }
	
	public double selectTotalByMonthAndCategory(String userId, String categoryId, String monthStr )
    	throws DaoException {
		
		String[] monthStrBreak = monthStr.split("-");
		

		double dbl = 0.0;
		String sql = "SELECT SUM(t.amount) as total FROM claim_form_item t LEFT JOIN claim_form_index i ON "+
	                "t.formId=i.id WHERE t.categoryId=? " +
	                "AND i.userOwner=? " +
	                "AND YEAR(i.claimMonth) = "+Integer.parseInt(monthStrBreak[0])+" " +
	                "AND MONTH(i.claimMonth) = "+Integer.parseInt(monthStrBreak[1])+" " +
	                "AND i.state='clo'";
	
		Collection col = super.select(sql,HashMap.class,new String[] {categoryId,userId},0,-1);
		HashMap map = (HashMap)col.iterator().next();
		try {
			String s = map.get("total").toString();
			if (s!=null)
				dbl = Double.parseDouble(s);
		}catch(Exception e) {}
		return dbl;
	}

}
