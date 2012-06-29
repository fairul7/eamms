package com.tms.crm.sales.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.model.DefaultDataObject;
   
public class CategoryDao  extends DataSourceDao {
	public void init() throws DaoException {
        super.init();    //To change body of overridden methods use File | Settings | File Templates.
        try{
            super.update("ALTER TABLE product ADD category varchar(255)",null);
          
        }catch(DaoException e){
        }
        
        try{
            super.update("ALTER TABLE product DROP INDEX product_productName, ADD INDEX product_productName (productName)",null);
          
        }catch(DaoException e){
        }
        
        try{
            super.update("CREATE TABLE category (  categoryID varchar(255) NOT NULL default '',  categoryName varchar(255) NOT NULL default '',  PRIMARY KEY  (categoryID),  UNIQUE KEY category_categoryName (categoryName)) TYPE=MyISAM;",null);
          
        }catch(DaoException e){
            
        }

        //Initializing products and categories
        try
        {
            Collection list = super.select("SELECT productId FROM product WHERE (category='' OR isNull(category))", HashMap.class, new Object[] {}, 0, 1);
            if(list.size() > 0)
            {
                super.update("INSERT INTO category(categoryId, categoryName) VALUES('1', 'General')", null);
                super.update("UPDATE product SET category='1' WHERE (category='' OR isNull(category))", null);
            }
        }
        catch(DaoException e)
        {
        }
        
        try
        {
            Collection list = super.select("SELECT productId FROM product WHERE category='1'", HashMap.class, new Object[] {}, 0, 1);
            if(list.size() > 0)
            {
                super.update("UPDATE opportunityproduct SET category='1' WHERE (category='' OR isNull(category))", null);
            }
        }
        catch(DaoException e)
        {
        }
	}
	
	public boolean isUnique(CategoryObject obj) throws DaoException {
		Collection list = super.select("SELECT COUNT(*) AS total FROM category " +
				"WHERE categoryName = ? AND categoryID <> ? ", 
				HashMap.class, new Object[] {obj.getCategoryName(), obj.getCategoryID()}, 0, 1);
		HashMap map = (HashMap)list.iterator().next();
		if (Integer.parseInt(map.get("total").toString()) == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public void insertRecord(CategoryObject obj) throws DaoException {
		super.update(
			"INSERT INTO category (categoryID, categoryName) " +
			"VALUES (#categoryID#, #categoryName#)"
		, obj);
	}
	
	public CategoryObject selectRecord(String categoryID) throws DaoException {
		Collection col = super.select(
			"SELECT categoryID, categoryName " +
			"FROM category " +
			"WHERE categoryID = ? "
		, CategoryObject.class, new String[] {categoryID}, 0, 1);
		
		CategoryObject obj = null;
		if (col.size() == 1) {
			Iterator iterator = col.iterator();
			obj = (CategoryObject) iterator.next(); 
		}
		
		return (obj);
	}
	
	public void updateRecord(CategoryObject obj) throws DaoException {
		super.update(
			"UPDATE category " +
			"SET categoryName = #categoryName# " +
			"WHERE categoryID = #categoryID# "
		, obj);
	} 
	
	public Collection listRecords(String sort, boolean desc, int start, int rows) throws DaoException {
		String orderBy = "ORDER BY " + ((sort != null) ? sort : "categoryName") + ((desc) ? " DESC" : "");
		
		Collection col = super.select(
			"SELECT categoryID, categoryName " +
			"FROM category " +
			"WHERE categoryID <> '0' " +
			orderBy
		, CategoryObject.class, null, start, rows);
		
		return (col);
	}
	
	public int count() throws DaoException {
		Collection list = super.select("SELECT COUNT(*) AS total FROM category WHERE categoryID <> '0' ", HashMap.class, null, 0, 1);
		HashMap map = (HashMap)list.iterator().next();
		return Integer.parseInt(map.get("total").toString());
	}
	
	public void deleteCategory(String id) throws DaoException {
        DefaultDataObject defaultDataObject = new DefaultDataObject();
        defaultDataObject.setId(id);
        super.update("DELETE FROM category WHERE categoryID= #id#",defaultDataObject);
        super.update("DELETE FROM product WHERE category= #id#",defaultDataObject);
    }
	
	 public Collection selectCategory() throws DaoException {
	        Collection category = new ArrayList();
	        String sql = "SELECT categoryName ,categoryID FROM category ";
	        category = super.select(sql, CategoryObject.class, null, 0, -1);

	        return category;
	    }
}
