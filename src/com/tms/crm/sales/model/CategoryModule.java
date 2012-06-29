package com.tms.crm.sales.model;

import java.util.ArrayList;
import java.util.Collection;

import kacang.model.DaoException;
import kacang.model.DefaultModule;
import kacang.util.Log;

public class CategoryModule extends DefaultModule {
	Log log = Log.getLog(getClass());
	
	public boolean isUnique(CategoryObject obj) {
		CategoryDao dao = (CategoryDao) getDao();
		try {
			return dao.isUnique(obj);
		} catch (DaoException e) {
			log.error("Error isUnique Category " + e.toString(), e);
			return(false);
		}
	} 
	
	public boolean addCategory(CategoryObject obj) {
		CategoryDao dao = (CategoryDao) getDao();
		try {
			dao.insertRecord(obj);
			return (true);
		} catch (DaoException e) {
			log.error("Error adding Category " + e.toString(), e);
			return (false);
		}
	} 
	
	public CategoryObject getCategory(String categoryID) {
		CategoryDao dao = (CategoryDao) getDao();
		try {
			return (dao.selectRecord(categoryID));
		} catch (DaoException e) {
			log.error("Error getting Category " + e.toString(), e);
			return (null); 
		}
	}
	
	public boolean updateCategory(CategoryObject obj) {
		CategoryDao dao = (CategoryDao) getDao();
		try {
			dao.updateRecord(obj);
			return (true);
		} catch (DaoException e) {
			log.error("Error updating Category " + e.toString(), e);
			return (false);
		}
	}
	
	public Collection listCategories(String sort, boolean desc, int start, int rows) {
		CategoryDao dao = (CategoryDao) getDao();
		try {
			Collection col = dao.listRecords(sort, desc, start, rows);
			return (col);
		} catch (DaoException e) {
			log.error("Error listing Category " + e.toString(), e);
			return (null);
		}
	}
	
	public int countCategories() {
		CategoryDao dao = (CategoryDao) getDao();
		try {
			return dao.count();
		} catch (DaoException e) {
			log.error("Error counting Product " + e.toString(), e);
			return(0);
		}
	}
	
	public void deleteCategory(String id){
		CategoryDao dao = (CategoryDao) getDao();
        try {
            dao.deleteCategory(id);
        } catch (DaoException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);  //To change body of catch statement use Options | File Templates.
        }
    }
	
	public Collection getCategory(){
	        Collection service = new ArrayList();
	        CategoryDao dao = (CategoryDao) getDao();
	        try {
	            service = dao.selectCategory();
	        }
	        catch (Exception e) {
	        	 Log.getLog(getClass()).error(e.getMessage(), e);
	        }
	        return service;
	}
	
	
}
