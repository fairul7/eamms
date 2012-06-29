/*
 * Created on Feb 26, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.model;

import java.util.*;

import kacang.model.*;

import com.tms.crm.sales.misc.*;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ProductDao extends DataSourceDao {
	public void insertRecord(Product pro) throws DaoException {
		super.update(
				"INSERT INTO product (productID, productName, isArchived, category) " +
				"VALUES (#productID#, #productName#, #isArchived#, #category#)"
			, pro);
	}
	
	public void updateRecord(Product pro) throws DaoException {
		super.update(
			"UPDATE product " +
			"SET productName = #productName#, " +
			"    isArchived  = #isArchived#, " +
			"    category  = #category# " +
			"WHERE productID = #productID# "
		, pro);
	}
	
	public Product selectRecord(String productID) throws DaoException {
		Collection col = super.select(
			"SELECT productID, productName, isArchived, category  " +
			"FROM product " +
			"WHERE productID = ? "
		, Product.class, new String[] {productID}, 0, 1);
		
		Product pro = null;
		if (col.size() == 1) {
			Iterator iterator = col.iterator();
			pro = (Product) iterator.next(); 
		}
		
		return (pro);
	}
	
	public Collection getProductCollection(boolean incArchive) throws DaoException {
		Collection col = super.select(
			"SELECT productID, productName, category  " +
			"FROM product " +
			(incArchive ? "" : "WHERE isArchived = '0' ") +
			"ORDER BY productName"
		, HashMap.class, null, 0, -1);
		return (col);
	}
	
	public Collection getProductCollections(String category) throws DaoException {
		Collection col = super.select(
			"SELECT productID, productName, category " +
			"FROM product WHERE category=?" +
			"ORDER BY productName"
		, HashMap.class, new Object []{category}, 0, -1);
		return (col);
	}
	
	public Collection getProductCollection() throws DaoException {
		Collection col = super.select(
			"SELECT productID, productName, category  " +
			"FROM product " +
			"ORDER BY productName"
		, HashMap.class, null, 0, -1);
		return (col);
	}
	
	public Map getProductMap() throws DaoException {
		Map map = MyUtil.collectionToMap(getProductCollection(), "productID", "productName");
		return (map);
	}
	
	public Map getProductMapping(String category) throws DaoException {
		Map map = MyUtil.collectionToMap(getProductCollections(category), "productID", "productName");
		return (map);
	}
	
	public Map getProductMap(boolean incArchive) throws DaoException {
		Map map = MyUtil.collectionToMap(getProductCollection(incArchive), "productID", "productName");
		return (map);
	}
	
	public Collection listRecords(String name, String searchCol, String sort, boolean desc, int start, int rows) throws DaoException {
		 
		String archived = null;
		if(name != null && name.trim().length() > 0 ){
			if(name.equals("No") ||(name.equals("no")))
				archived = "0";
			else if(name.equals("Yes")||(name.equals("yes")))
				archived="1";
		}
		  
		StringBuffer sql = new StringBuffer("SELECT po.productID, po.productName, po.isArchived, po.category, cat.categoryName " +
				"FROM product po, category cat " +
				"WHERE productID <> '0' AND cat.categoryID=po.category ");
		
		if(name != null && name.trim().length() > 0 && "".equals(searchCol)){
    		sql.append(" AND (po.productName LIKE '%" + name + "%' " +
    				"OR cat.categoryName LIKE '%" + name + "%' " +
    				"OR po.isArchived LIKE '%" + archived + "%' ");
    		sql.append(") ");
    	}
		  
		if(name != null && name.trim().length() > 0 && searchCol != null && !"".equals(searchCol)){
			String columnName =searchCol;
			if(searchCol.equals("isArchived")){
				sql.append(" AND (" + columnName + " LIKE '%" + archived + "%' ");
	    		sql.append(") ");
			}else{
				sql.append(" AND (" + columnName + " LIKE '%" + name + "%' ");
	    		sql.append(") ");
			}
			
    	}
		if(sort != null){
    		sql.append(" ORDER BY " + sort);
    	}else{
    		sql.append(" ORDER BY productName ");
    	}
    	
		if (desc)
            sql.append(" DESC");
		
		Collection col = super.select( sql.toString(), Product.class,null, start, rows);
		
		return (col);
	}
	
	public int count(String name, String searchCol) throws DaoException {
		String archived = "";
		if(searchCol.equals("isArchived")&& (name != null && name.trim().length() > 0)){
			if(name.equals("No") ||(name.equals("no")))
				archived = "0";
			else if(name.equals("Yes")||(name.equals("yes")))
				archived="1";
			 	
		}
		String category ="";
		if(searchCol.equals("categoryName")&& (name != null && name.trim().length() > 0)){
			Collection cat = null;
			cat = super.select(
					"SELECT categoryID, categoryName " +
					"FROM category " +
					"WHERE categoryName=? " 
				, HashMap.class, new Object[]{name}, 0, 1);
			
			for(Iterator i = cat.iterator(); i.hasNext();){
				HashMap p = (HashMap)i.next();
				category = p.get("categoryID").toString();
			
			}
		}
		
		String product ="";
		if(searchCol.equals("product")&& (name != null && name.trim().length() > 0)){
			product = name;
		}
		
	  	StringBuffer sql = new StringBuffer("SELECT COUNT(*) AS total FROM product po, category cat" +
	  			" WHERE productID <> '0' AND cat.categoryID=po.category ");
	  
	  	if(product != null && product.trim().length() > 0){
    		sql.append(" AND (productName LIKE '%" + product + "%' ");
    		sql.append(") ");
    	}
		
		if(category != null && category.trim().length() > 0){
	   		sql.append(" AND (category LIKE '%" + category + "%' " );
	   		sql.append(") ");
	   	}
		
		if(archived != null && archived.trim().length() > 0){
	   		sql.append(" AND (isArchived LIKE '%" + archived + "%' " );
	   		sql.append(") ");
	   	}
		
		Collection list = super.select( sql.toString(), HashMap.class,null, 0, 1);
		HashMap map = (HashMap)list.iterator().next();
		return Integer.parseInt(map.get("total").toString());
	}
	
	public boolean isUnique(Product pro) throws DaoException {
		Collection list = super.select("SELECT COUNT(*) AS total " +
				"FROM product WHERE productName=? AND category=?", 
				HashMap.class, new Object[] {pro.getProductName(), pro.getCategory()}, 0, 1);
		HashMap map = (HashMap)list.iterator().next();
		if (Integer.parseInt(map.get("total").toString()) == 0) {
			return true;
		} else {
			return false;
		}
	}

    public void deleteProduct(String id) throws DaoException {
        DefaultDataObject defaultDataObject = new DefaultDataObject();
        defaultDataObject.setId(id);
        super.update("DELETE FROM product WHERE productID= #id#",defaultDataObject);

    }
    
    public Collection getProducts(String category) throws DaoException {
		Collection col = super.select(
			"SELECT productID, productName, isArchived, category " +
			"FROM product WHERE category=? ORDER BY productName" 
		, Product.class, new Object[]{category}, 0, -1);
		
		return col;
	}
}