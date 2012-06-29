package com.tms.sam.po.setting.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DataSourceDao;

public class SettingDao extends DataSourceDao{
	
	public String checkCurrency(String country, String currency)
	throws DaoException {
		try {
			Collection checkCurrency = super.select("SELECT currencyID, country, currency " +
									  "FROM po_currency " +
									  "WHERE country=? AND currency=?",
						              CurrencyObject.class,new Object[]{country, currency}, 0, -1);
			if (checkCurrency.size() > 0){
				return "true";
			}
			else{
				return null;
			}
		} catch (DaoException e) {}
		return null;
	}
	
	public String checkCategory(String category)
	throws DaoException {
		try {
			Collection checkCategory = super.select("SELECT categoryID, category, createdBy,lastUpdatedBy,dateCreated,lastUpdatedDate " +
					  "FROM po_category " +
					  "WHERE category=? ",
		              CategoryObject.class,new Object[]{category}, 0, -1);
			if (checkCategory.size() > 0){
				return "true";
			}
			else{
				return null;
			}
		} catch (DaoException e) {}
		return null;
	}
	
	public void addCurrency(CurrencyObject obj)
	throws DaoException {
		try {
			super.update("INSERT INTO po_currency (" + 
					     "currencyID, country, currency, " +
					     "dateCreated, createdBy, lastUpdatedDate, " +
					     "lastUpdatedBy) VALUES " + 
					     "(#currencyID#,#country#, #currency#, " +
					     "now(), #createdBy#, now(), " +
					     "#lastUpdatedBy#)",obj);
		} catch (DaoException e) {}
	}
	
	public void addCategory(CategoryObject obj)
	throws DaoException {
		try {
			super.update("INSERT INTO po_category (" + 
					     "categoryID, category," +
					     "dateCreated, createdBy, lastUpdatedDate, " +
					     "lastUpdatedBy) VALUES " + 
					     "(#categoryID#,#category#," +
					     "now(), #createdBy#, now(), " +
					     "#lastUpdatedBy#)",obj);
		} catch (DaoException e) {}
	}
	
	public boolean isUnique(ItemObject obj) throws DaoException {
		Collection list = super.select("SELECT COUNT(*) AS total " +
				"FROM po_item WHERE itemCode=? ", 
				HashMap.class, new Object[] {obj.getItemCode()}, 0, 1);
		HashMap map = (HashMap)list.iterator().next();
		if (Integer.parseInt(map.get("total").toString()) == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public void addItem(ItemObject obj)
	throws DaoException {
		try {
			super.update("INSERT INTO po_item (" + 
					     "itemCode, itemDesc, categoryID, " +
					     "minQty, unitOfMeasure, approved, " +
					     "dateCreated, createdBy, lastUpdatedDate, " +
					     "lastUpdatedBy) VALUES " + 
					     "(#itemCode#, #itemDesc#, #categoryID#," +
					     "#minQty#, #unitOfMeasure#, #approved#, " +
					     "now(), #createdBy#, now(), " +
					     "#lastUpdatedBy#)",obj);
		} catch (DaoException e) {}
	}
	
	public void editCurrency(CurrencyObject obj)
	throws DaoException {
			super.update(
				"UPDATE po_currency " +
				"SET country = #country#, " +
				"    currency  = #currency#, " +
				"    lastUpdatedDate  = #lastUpdatedDate#, " +
				"    lastUpdatedBy  = #lastUpdatedBy# " +
				"WHERE currencyID = #currencyID# "
			, obj);
	}
	
	public void editCategory(CategoryObject obj)
	throws DaoException {
			super.update(
				"UPDATE po_category " +
				"SET category = #category#, " +
				"    lastUpdatedDate  = #lastUpdatedDate#, " +
				"    lastUpdatedBy  = #lastUpdatedBy# " +
				"WHERE categoryID = #categoryID# "
			, obj);
	}
	
	public void editItem(ItemObject obj)
	throws DaoException {
			super.update(
				"UPDATE po_item " +
				"SET itemDesc  = #itemDesc#, " +
				" categoryID = #categoryID#, " +
				"    minQty  = #minQty#, " +
				"    approved  = #approved#, " +
				"    unitOfMeasure  = #unitOfMeasure#, " +
				"    lastUpdatedDate  = #lastUpdatedDate#, " +
				"    lastUpdatedBy  = #lastUpdatedBy# " +
				"WHERE itemCode = #itemCode# "
			, obj);
	}
	
	public CurrencyObject getCurrency(String currencyID) throws DaoException, DataObjectNotFoundException{
		Collection request = null;
		CurrencyObject obj = new CurrencyObject();
		request = super.select("SELECT currencyID, country, currency " +
				  "FROM po_currency " +
				  "WHERE currencyID=?",
	             CurrencyObject.class,new Object[]{currencyID}, 0, 1);
		for(Iterator i = request.iterator();i.hasNext();){
			obj = (CurrencyObject)i.next();
		}
		
		return obj;
	}
	
	public CategoryObject getOneCategory(String categoryID) throws DaoException, DataObjectNotFoundException{
		Collection request = null;
		CategoryObject obj = new CategoryObject();
		request = super.select("SELECT categoryID, category " +
				  "FROM po_category " +
				  "WHERE categoryID=?",
				  CategoryObject.class,new Object[]{categoryID}, 0, 1);
		for(Iterator i = request.iterator();i.hasNext();){
			obj = (CategoryObject)i.next();
		}
		
		return obj;
	}
	
	public ItemObject getOneItem(String itemCode) throws DaoException, DataObjectNotFoundException{
		Collection request = null;
		ItemObject obj = new ItemObject();
		request = super.select("SELECT itemCode, itemDesc, categoryID, minQty, unitOfMeasure, approved " +
				  "FROM po_item " +
				  "WHERE itemCode=?",
				  ItemObject.class,new Object[]{itemCode}, 0, 1);
		for(Iterator i = request.iterator();i.hasNext();){
			obj = (ItemObject)i.next();
		}
		
		return obj;
	}
	
	public Collection getListing(String name, String sort, boolean desc, int start, int rows) throws DaoException ,DataObjectNotFoundException{
		
		String condition = (name != null) ? "%" + name + "%" : "%";
		String orderBy = (sort != null) ? sort : "country";
		if (desc)
            orderBy += " DESC";
		
		Object[] args = {condition, condition };
		
      
		Collection request = null;

		request = super.select("SELECT currencyID, country, currency " +
				  "FROM po_currency " +
				  "WHERE " +
				  "(country LIKE ? OR currency LIKE ? ) " +
	              "ORDER BY " + orderBy, CurrencyObject.class,args, start, rows);
		
		return request;
	}
	
	public int countListing(String name) throws DaoException, DataObjectNotFoundException{
		String condition = (name != null) ? "%" + name + "%" : "%";
		int total = 0;	
		Object[] args = {condition, condition };
		Collection countrequest = null;
       
        
        countrequest = super.select("SELECT COUNT(*) AS total " + 
				       "FROM po_currency " +
				       "WHERE " +
						"(country LIKE ? OR currency LIKE ? ) " 
	                   , HashMap.class,args, 0, 1);
        
        if(countrequest != null) {
        	for(Iterator i= countrequest.iterator(); i.hasNext(); ) {
        		HashMap map = (HashMap) i.next();
        		total = Integer.parseInt(map.get("total").toString());
        	}
        }
        
        return total;
	}
	
	public Collection getCategory(String name, String searchCol, String sort, boolean desc, int start, int rows) throws DaoException ,DataObjectNotFoundException{
		
		String condition = (name != null) ? "%" + name + "%" : "%";
		String orderBy = (sort != null) ? sort : "category";
		if (desc)
            orderBy += " DESC";
		
		Object[] args = {condition };
		
		String columnName = "category";
	        
	    if(! "".equals(searchCol)) {
	       if("category".equals(searchCol)) {
	           columnName = "category";
	       }
	    }
        
		Collection request = null;

		request = super.select("SELECT categoryID, category " +
				  "FROM po_category " +
				  "WHERE " +
	              columnName + " LIKE ? " +
	              "ORDER BY " + orderBy, CategoryObject.class,args, start, rows);
		return request;
	}

	public int countCategory(String name, String searchCol) throws DaoException, DataObjectNotFoundException{
		String condition = (name != null) ? "%" + name + "%" : "%";
		int total = 0;	
		Object[] args = {condition };
		Collection countrequest = null;
        String columnName = "category";
        
        if(! "".equals(searchCol)) {
        	 if("category".equals(searchCol)) {
                 columnName = "category";
             }
        }
        
        
        countrequest = super.select("SELECT COUNT(*) AS total " + 
				       "FROM po_category " +
				       "WHERE " +
	                   columnName + " LIKE ? "
	                   , HashMap.class,args, 0, 1);
        
        if(countrequest != null) {
        	for(Iterator i= countrequest.iterator(); i.hasNext(); ) {
        		HashMap map = (HashMap) i.next();
        		total = Integer.parseInt(map.get("total").toString());
        	}
        }
        
        return total;
	}
	
	public Collection getItem(String name, /*String searchCol,*/ String categoryCol, String sort, boolean desc, int start, int rows) throws DaoException ,DataObjectNotFoundException{
		
		String condition = (name != null) ? "%" + name + "%" : "%";
		String orderBy = (sort != null) ? sort : "itemCode";
		if (desc)
            orderBy += " DESC";
		
		Object[] args = {condition,categoryCol };
		ArrayList paramList = new ArrayList();
		
		/*String columnName = "itemCode";
        
        if(! "".equals(searchCol)) {
            if("itemDesc".equalsIgnoreCase(searchCol)) {
                columnName = "itemDesc";
            }
        }*/
		Collection request = null;
		
		StringBuffer sql = new StringBuffer("SELECT i.itemCode, i.itemDesc, i.approved, i.categoryID, c.category " +
				  "FROM po_item i " +
				  "LEFT OUTER JOIN po_category c ON c.categoryID = i.categoryID " +
				  "WHERE itemCode LIKE ? OR itemDesc LIKE ? ");
		paramList.add(condition);
		paramList.add(condition);
		
		if(categoryCol!=null && !"".equals(categoryCol)){
			sql.append("AND i.categoryID=? ");
			paramList.add(categoryCol);
			
		}
		sql.append(" ORDER BY " + orderBy);
		request = super.select(sql.toString(), ItemObject.class,paramList.toArray(), start, rows);
		
		
		
		return request;
	}

	public boolean isCurrencyNameExist(String currencyName, String currencyId) throws DaoException {
		boolean isCurrencyNameExist = false;
		Collection paramList = new ArrayList();
		StringBuffer sql = new StringBuffer("SELECT currencyID FROM po_currency " +
				"WHERE LOWER(currency) = ?");
		paramList.add(currencyName);
		if(currencyId!=null){
			sql.append(" AND currencyID<> ? ");
			paramList.add(currencyId);
		}
		
		Collection col = super.select(sql.toString(), HashMap.class,paramList.toArray(), 0, -1);
		
		if(col != null) {
			Iterator i = col.iterator();
			if(i.hasNext()){
				isCurrencyNameExist = true;
			}
		}
		
		return isCurrencyNameExist;
	}
	
	public boolean isCategoryExist(String categoryName) throws DaoException {
		boolean isCategoryExist = false;
		
		Collection col = super.select("SELECT categoryID FROM po_category " +
				"WHERE LOWER(category) = ?", HashMap.class, new Object[] {categoryName}, 0, -1);
		
		if(col != null) {
			Iterator i = col.iterator();
			if(i.hasNext()){
				isCategoryExist = true;
			}
		}
		
		return isCategoryExist;
	}
	
	public void deleteCurrency(String id) throws DaoException, DataObjectNotFoundException{
		Object[] args = new String[] {
		            id
		        };
	   	   
	    super.update("DELETE FROM po_currency WHERE currencyID=?", args);
	}
	
	public void deleteCategory(String id) throws DaoException, DataObjectNotFoundException{
		Object[] args = new String[] {
		            id
		        };
	   	   
	    super.update("DELETE FROM po_category WHERE categoryID=?", args);
	}
	
	public void deleteItem(String id) throws DaoException, DataObjectNotFoundException{
		Object[] args = new String[] {
		            id
		        };
	   	   
	    super.update("DELETE FROM po_item WHERE itemCode=?", args);
	}
	
	public Collection selectCategory() throws DaoException {
	        Collection category = new ArrayList();
	        String sql = "SELECT categoryID ,category FROM po_category ";
	        category = super.select(sql, CategoryObject.class, null, 0, -1);

	        return category;
	}
	
	public Collection getListingItem(String name, String searchCriteriaColValue, String searchCategoryCol, String sort, boolean desc, int start, int rows) throws DaoException ,DataObjectNotFoundException{
		String condition = (name != null) ? "%" + name + "%" : "%";
		Collection paramList = new ArrayList();
    	StringBuffer sql = new StringBuffer("SELECT i.itemCode, i.itemDesc, " +
    			 "i.minQty, i.unitOfMeasure, i.categoryID, c.categoryID, c.category " +
				 "FROM po_item i, po_category c " +
				 "WHERE c.categoryID = i.categoryID AND 1=1 AND i.approved = '1'");
    	
    
    	if(searchCriteriaColValue != null && searchCriteriaColValue.trim().length() > 0){
    		sql.append("AND i." + searchCriteriaColValue + " LIKE ? ");
    		paramList.add(condition);
    	}
    	
    	
    	if(name != null && name.trim().length() > 0 && searchCriteriaColValue == ""){
    		sql.append(" AND (i.itemCode LIKE '%" + name + "%' " +
    				"OR i.itemDesc LIKE '%" + name + "%' " +
    				"OR i.unitOfMeasure LIKE '%" + name +"%' ");
    		sql.append(") ");
    	}
    	
    	if(searchCategoryCol != null && searchCategoryCol.trim().length() > 0){
    		sql.append("AND i.categoryID = '" + searchCategoryCol + "' ");
    	}
    	
    	if(sort != null){
    		sql.append(" ORDER BY " + sort);
    	}else{
    		sql.append(" ORDER BY c.category ");
    	}
    	
		if (desc)
            sql.append(" DESC");
		
		Collection request = null;
		
		request = super.select( sql.toString(), ItemObject.class,paramList.toArray(), start, rows);
		
		return request;
    }
	
	public int countListingRequest(String name,  String searchCriteriaColValue, String searchCategoryCol) throws DaoException ,DataObjectNotFoundException{
		String condition = (name != null) ? "%" + name + "%" : "%";
		Collection request = null;
	   	Collection paramList = new ArrayList();
	   	int total = 0;	
	   	StringBuffer sql = new StringBuffer("SELECT COUNT(*) AS total " + 
	   			"FROM po_item i, po_category c " +
	   			"WHERE c.categoryID = i.categoryID AND 1=1 ");
  	
	   	if(searchCriteriaColValue != null && searchCriteriaColValue.trim().length() > 0){
    		sql.append("AND i." + searchCriteriaColValue + " LIKE ? ");
    		paramList.add(condition);
    	}
    	
    	if(name != null && name.trim().length() > 0 && searchCriteriaColValue == ""){
    		sql.append(" AND (i.itemCode LIKE '%" + name + "%' " +
    				"OR i.itemDesc LIKE '%" + name + "%' " +
    				"OR i.unitOfMeasure LIKE '%" + name +"%' ");
    		sql.append(") ");
    	}
    	
    	if(searchCategoryCol != null && searchCategoryCol.trim().length() > 0){
    		sql.append("AND i.categoryID = '" + searchCategoryCol + "' ");
    	}
    	
    	
	  	request = super.select( sql.toString(), HashMap.class,null, 0, 1);
     
       
       if(request != null) {
       	for(Iterator i= request.iterator(); i.hasNext(); ) {
       		HashMap map = (HashMap) i.next();
       		total = Integer.parseInt(map.get("total").toString());
       	}
       }
       
       return total;
   }
	
	public Collection getSelectedItem(String id) throws DaoException ,DataObjectNotFoundException{
		StringBuffer sql = new StringBuffer("SELECT itemCode, itemDesc, minQty, unitOfMeasure " +
				  "FROM po_item " +
	   			  "WHERE itemCode = ?  ");
		
		Collection request = super.select( sql.toString(), ItemObject.class,new Object[]{id}, 0, -1);
		
		
		return request;
	}
	
	public Collection getSelectedItemByCode(String id) throws DaoException ,DataObjectNotFoundException{
		

		Collection request = super.select("SELECT itemID, itemCode, itemDesc, minQty, unitOfMeasure " +
				  "FROM po_item " +
				  "WHERE itemCode=? "
	             , ItemObject.class,new Object[]{id}, 0, -1);
		
		
		
		return request;
	}
	public boolean isCodeNameExist(String itemCode) throws DaoException {
		boolean isCodeNameExist = false;
		
		Collection col = super.select("SELECT itemCode FROM po_item " +
				"WHERE LOWER(itemCode) = ?", HashMap.class, new Object[] {itemCode}, 0, -1);
		
		if(col != null) {
			Iterator i = col.iterator();
			if(i.hasNext()){
				isCodeNameExist = true;
			}
		}
		
		return isCodeNameExist;
	}
	
	public Collection getItems(String[] ids) throws DaoException ,DataObjectNotFoundException{
		Collection paramList = new ArrayList();
		StringBuffer sql = new StringBuffer("SELECT itemCode, itemDesc, minQty, unitOfMeasure " +
				  "FROM po_item " +
	   			  "WHERE 1=1 ");
		
		sql.append(" AND itemCode IN (");

		for (int i = 0; i < ids.length; i++) {

			if (i > 0) {
				sql.append(",");

			}
			sql.append("?");

			paramList.add(ids[i]);
		}

		sql.append(")");
		
		Collection request = super.select( sql.toString(), ItemObject.class, paramList.toArray(), 0, -1);
		
		
		return request;
	}
	
	public Collection getItems(String ppID, String name, String searchCriteriaColValue, String searchCategoryCol, String sort, boolean desc, int start, int rows) throws DaoException ,DataObjectNotFoundException{
		String condition = (name != null) ? "%" + name + "%" : "%";
		Collection paramList = new ArrayList();
		StringBuffer sql = new StringBuffer("SELECT pp.itemCode, item.itemDesc, item.minQty, " +
   			 "item.unitOfMeasure, item.categoryID, c.category, c.categoryID " +
				 "FROM po_pre_purchase_item pp, po_item item, po_category c  " +
				 "WHERE pp.ppID=?  AND pp.itemCode NOT IN(" +
				 "Select i.itemCode from po_supplier_item i " +
				 "where i.ppID=pp.ppID) " +
				 "AND item.itemCode=pp.itemCode AND item.categoryID = c.categoryID AND 1=1 ");
    	
    	
    	 
    	paramList.add(ppID);
    
    	if(searchCriteriaColValue != null && searchCriteriaColValue.trim().length() > 0){
    		sql.append("AND i." + searchCriteriaColValue + " LIKE ? ");
    		paramList.add(condition);
    	}
    	
    	
    	if(name != null && name.trim().length() > 0 && searchCriteriaColValue == ""){
    		sql.append(" AND (i.itemCode LIKE '%" + name + "%' " +
    				"OR i.itemDesc LIKE '%" + name + "%' " +
    				"OR i.unitOfMeasure LIKE '%" + name +"%' ");
    		sql.append(") ");
    	}
    	
    	if(searchCategoryCol != null && searchCategoryCol.trim().length() > 0){
    		sql.append("AND i.categoryID = '" + searchCategoryCol + "' ");
    	}
    	
    	if(sort != null){
    		sql.append(" ORDER BY " + sort);
    	}else{
    		sql.append(" ORDER BY c.category ");
    	}
    	
		if (desc)
            sql.append(" DESC");
		
		Collection request = null;
		
		request = super.select( sql.toString(), ItemObject.class,paramList.toArray(), start, rows);
		
		return request;
    }
	
	public Collection getCurrency() throws DaoException {
        Collection currency = new ArrayList();
        String sql = "SELECT currencyID, currency FROM po_currency";
        currency = super.select(sql, CurrencyObject.class, null, 0, -1);

        return currency;
}
}
