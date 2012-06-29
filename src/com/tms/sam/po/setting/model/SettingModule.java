package com.tms.sam.po.setting.model;

import java.util.ArrayList;
import java.util.Collection;

import kacang.model.DaoException;
import kacang.model.DefaultModule;
import kacang.util.Log;

public class SettingModule extends DefaultModule {
	 public boolean addCurrency(CurrencyObject obj) {
	        SettingDao dao = (SettingDao) getDao();
	        try {
	        	String checkingCurrency=dao.checkCurrency(obj.getCountry(),obj.getCurrency());
	        	if (checkingCurrency == null){
	        		dao.addCurrency(obj);
		            return true;
	        	}
	        	else{
	        		return false;
	        	}
	        		
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error inserting currency record: " + error, error);
	            return false;
	        }
	 }
	 
	 public CurrencyObject getCurrency(String id) {
	        SettingDao dao = (SettingDao) getDao();
	        CurrencyObject obj= null;
	        try {
	        	
	            obj = dao.getCurrency(id);
	            
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error inserting currency record: " + error, error);
	            
	        }
	        return obj;
	 }
	 
	 public boolean editCurrency(CurrencyObject obj) {
	        SettingDao dao = (SettingDao) getDao();
	        try {
	            dao.editCurrency(obj);
	            return true;
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error inserting currency record: " + error, error);
	            return false;
	        }
	 }
	 
	 public CategoryObject getOneCategory(String id) {
	        SettingDao dao = (SettingDao) getDao();
	        CategoryObject obj= null;
	        try {
	        	
	            obj = dao.getOneCategory(id);
	            
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error getting category: " + error, error);
	            
	        }
	        return obj;
	 }
	 
	 public ItemObject getOneItem(String code) {
	        SettingDao dao = (SettingDao) getDao();
	        ItemObject obj= null;
	        try {
	        	
	            obj = dao.getOneItem(code);
	            
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error getting item: " + error, error);
	            
	        }
	        return obj;
	 }
	 public boolean addCategory(CategoryObject obj) {
	        SettingDao dao = (SettingDao) getDao();
	        try {
	        	String checkingCurrency=dao.checkCategory(obj.getCategory());
	        	if (checkingCurrency == null){
	        		dao.addCategory(obj);
		            return true;
	        	}
	        	else{
	        		return false;
	        	}
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error inserting category record: " + error, error);
	            return false;
	        }
	 }
	 
	 public boolean editCategory(CategoryObject obj) {
	        SettingDao dao = (SettingDao) getDao();
	        try {
	            dao.editCategory(obj);
	            return true;
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error inserting currency record: " + error, error);
	            return false;
	        }
	 }
	 
	 public boolean isUnique(ItemObject obj) {
		    SettingDao dao = (SettingDao) getDao();
	        try {
	            dao.isUnique(obj);
	            return true;
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error inserting currency record: " + error, error);
	            return false;
	        }
	 }
		
	 public boolean addItem(ItemObject obj) {
	        SettingDao dao = (SettingDao) getDao();
	        try {
	            dao.addItem(obj);
	            return true;
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error inserting currency record: " + error, error);
	            return false;
	        }
	 }
	 
	 public boolean editItem(ItemObject obj) {
	        SettingDao dao = (SettingDao) getDao();
	        try {
	            dao.editItem(obj);
	            return true;
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error inserting currency record: " + error, error);
	            return false;
	        }
	 }
	 
	 public Collection getListing(String name, String sort, boolean desc, int start, int rows) {
		  SettingDao dao = (SettingDao) getDao();
		  Collection requestItems = null;
	      try {
	    	  requestItems= dao.getListing(name, sort, desc, start, rows);;
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error getting currency: " + error, error);
	           
	      }
	        return requestItems;
	 }
	 
	 public int countListing(String name) {
		    SettingDao dao = (SettingDao) getDao();
	        try {
	             return dao.countListing(name);
	          
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error counting currency record: " + error, error);
	            return 0;
	        }
	 }
	 
	 public Collection getCategory(String name, String searchCol, String sort, boolean desc, int start, int rows) {
		  SettingDao dao = (SettingDao) getDao();
		  Collection requestItems = null;
	      try {
	    	  requestItems= dao.getCategory(name, searchCol, sort, desc, start, rows);;
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error getting category: " + error, error);
	           
	      }
	        return requestItems;
	 }
	 
	 public Collection getItem(String name, /*String searchCol,*/ String categoryCol, String sort, boolean desc, int start, int rows) {
		  SettingDao dao = (SettingDao) getDao();
		  Collection requestItems = null;
	      try {
	    	  requestItems= dao.getItem(name,/* searchCol, */categoryCol, sort, desc, start, rows);;
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error getting category: " + error, error);
	           
	      }
	        return requestItems;
	 }
	 
	 public int countCategory(String name, String searchCol) {
		    SettingDao dao = (SettingDao) getDao();
	        try {
	             return dao.countCategory(name, searchCol);
	          
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error counting currency record: " + error, error);
	            return 0;
	        }
	 }
	 
	 public boolean isCurrencyNameExist(String currencyName, String id) {
		 	SettingDao dao = (SettingDao) getDao();
			boolean isCurrencyNameExist = false;
			
			try {
				isCurrencyNameExist = dao.isCurrencyNameExist(currencyName, id);
			}
			catch(DaoException e){
				Log.getLog(getClass()).error("Error in isCountryNameExist: ", e);
			}
			
			return isCurrencyNameExist;
	 }
	 
	 public boolean isCategoryExist(String currencyName) {
		 	SettingDao dao = (SettingDao) getDao();
			boolean isCategoryExist = false;
			
			try {
				isCategoryExist = dao.isCategoryExist(currencyName);
			}
			catch(DaoException e){
				Log.getLog(getClass()).error("Error in isCategoryExist: ", e);
			}
			
			return isCategoryExist;
	 }
	 
	 public boolean deleteCurrency(String id) {
		 	SettingDao dao = (SettingDao) getDao();
	        try {
	            dao.deleteCurrency(id);
	            return true;
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error deleting currency record: " + error, error);
	            return false;
	        }
	 }
	 
	 public boolean deleteCategory(String id) {
		 	SettingDao dao = (SettingDao) getDao();
	        try {
	            dao.deleteCategory(id);
	            return true;
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error deleting category record: " + error, error);
	            return false;
	        }
	 }

	 public boolean deleteItem(String id) {
		 	SettingDao dao = (SettingDao) getDao();
	        try {
	            dao.deleteItem(id);
	            return true;
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error deleting category record: " + error, error);
	            return false;
	        }
	 }
	 
	 public Collection getCategory(){
	        Collection service = new ArrayList();
	        SettingDao dao = (SettingDao) getDao();
	        try {
	            service = dao.selectCategory();
	        }
	        catch (Exception e) {
	        	 Log.getLog(getClass()).error(e.getMessage(), e);
	        }
	        return service;
	}
	 
	 public Collection getListingItem(String name, String searchCriteriaColValue, String searchCategoryCol, String sort, boolean desc, int start, int rows) {
		 SettingDao dao = (SettingDao) getDao();
		  Collection listingItems = null;
	      try {
	    	listingItems= dao.getListingItem(name, searchCriteriaColValue, searchCategoryCol, sort, desc, start, rows);
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error finding Listing Item: " + error, error);
	           
	      }
	        return listingItems;
	 }
	 
	 public int countListingRequest(String name, String searchCriteriaColValue, String searchCategoryCol) {
		 SettingDao dao = (SettingDao) getDao();
	        try {
	             return dao.countListingRequest(name, searchCriteriaColValue, searchCategoryCol);
	          
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error couting listing request: " + error, error);
	            return 0;
	        }
	 }
	 
	 public Collection getSelectedItem(String id) {
		  SettingDao dao = (SettingDao) getDao();
		  Collection requestItems = null;
	      try {
	    	  requestItems= dao.getSelectedItem(id);;
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error getting category: " + error, error);
	           
	      }
	        return requestItems;
	 }
	 
	 public Collection getSelectedItemByCode(String id) {
		  SettingDao dao = (SettingDao) getDao();
		  Collection requestItems = null;
	      try {
	    	  requestItems= dao.getSelectedItemByCode(id);;
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error getting category: " + error, error);
	           
	      }
	        return requestItems;
	 }
	 
	 public boolean isCodeNameExist(String groupName) {
		  SettingDao dao = (SettingDao) getDao();
			boolean isCodeNameExist = false;
			
			try {
				isCodeNameExist = dao.isCodeNameExist(groupName);
			}
			catch(DaoException e){
				Log.getLog(getClass()).error("Error in isCodeNameExist: ", e);
			}
			
			return isCodeNameExist;
	 }
	 
	 public Collection getItems(String[] ids) {
		  SettingDao dao = (SettingDao) getDao();
		  Collection items = null;
	      try {
	    	  items= dao.getItems(ids);;
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error getting items: " + error, error);
	           
	      }
	        return items;
	 }
	 
	 public Collection getItems(String ppID ,String name, String searchCriteriaColValue, String searchCategoryCol, String sort, boolean desc, int start, int rows) {
		  SettingDao dao = (SettingDao) getDao();
		  Collection requestItems = null;
	      try {
	    	  requestItems= dao.getItems(ppID, name, searchCriteriaColValue, searchCategoryCol, sort, desc, start, rows);
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error getting category: " + error, error);
	           
	      }
	        return requestItems;
	 }
	 
	 public Collection getCurrency(){
	        Collection service = new ArrayList();
	        SettingDao dao = (SettingDao) getDao();
	        try {
	            service = dao.getCurrency();
	        }
	        catch (Exception e) {
	        	 Log.getLog(getClass()).error(e.getMessage(), e);
	        }
	        return service;
	}
}
