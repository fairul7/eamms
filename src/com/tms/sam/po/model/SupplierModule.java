package com.tms.sam.po.model;

import java.util.Collection;

import kacang.model.DaoException;
import kacang.model.DefaultModule;
import kacang.util.Log;

public class SupplierModule extends DefaultModule{
	
	 public Collection getSupplier(String ppID, int index, String sort, boolean desc, int start, int rows) {
		  SupplierDao dao = (SupplierDao) getDao();
		  Collection supplierList = null;
	      try {
	    	 supplierList= dao.getSupplier(ppID, index, sort, desc, start, rows);
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in getSupplier: " + error, error);
	           
	      }
	        return supplierList;
	 }
	 
	 public int countListing(String ppID) {
		    SupplierDao dao = (SupplierDao) getDao();
	        try {
	             return dao.countListing(ppID);
	          
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in countListing: " + error, error);
	            return 0;
	        }
	 }
	 
	 public boolean addSupplier(SupplierObject pr) {
	        SupplierDao dao = (SupplierDao) getDao();
	        try {
	            dao.addSupplier(pr);
	            return true;
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in addSupplier: " + error, error);
	            return false;
	        }
	 }
	 
	 public boolean recommendSupplier(SupplierObject obj) {
		    SupplierDao dao = (SupplierDao) getDao();
	        try {
	            dao.recommendSupplier(obj);
	            return true;
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in recommendSupplier: " + error, error);
	            return false;
	        }
	 }
	 
	 public boolean responded(SupplierObject obj) {
		    SupplierDao dao = (SupplierDao) getDao();
	        try {
	            dao.responded(obj);
	            return true;
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in responded: " + error, error);
	            return false;
	        }
	 }
	 
	 public boolean deleteSupplier(String ppID, String id, int index) {
		   SupplierDao dao = (SupplierDao) getDao();
	       try {
	            dao.deleteSupplier(ppID, id, index);
	            return true;
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in deleteSupplier: " + error, error);
	            return false;
	        }
	 }
	 
	 public SupplierObject singleRequest(String supplierID, int count, String ppID){
		  SupplierDao dao = (SupplierDao) getDao();
		  SupplierObject singleRequest = null;
	      try {
	    	  singleRequest= dao.singleRequest(supplierID, count, ppID);
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in singleRequest: " + error, error);
	           
	      }
	        return singleRequest;
	 }
	 
	 public Collection getAttachment(String supplierID, int index, String ppID){
		  SupplierDao dao = (SupplierDao) getDao();
		  Collection attachmentObj = null;
		
	      try {
	    	  attachmentObj = dao.getAttachment(supplierID, index, ppID);
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in getAttachment: " + error, error);
	      }
	        
	        return attachmentObj;
	 }
	 
	 public String getPurchaseID(String supplierID){
		  SupplierDao dao = (SupplierDao) getDao();
		  String ppID = null;
		
	      try {
	    	  ppID = dao.getPurchaseID(supplierID);
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in getPurchaseID: " + error, error);
	      }
	        
	        return ppID;
	 }
	 
	 public boolean updateSupplierInfo(SupplierObject obj) {
		    SupplierDao dao = (SupplierDao) getDao();
	        try {
	            dao.updateSupplierInfo(obj);
	            return true;
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in updateSupplierInfo: " + error, error);
	            return false;
	        }
	 }
	 
	 public boolean saveRating(RatingObject pr) {
	        SupplierDao dao = (SupplierDao) getDao();
	        try {
	            dao.saveRating(pr);
	            return true;
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in saveRating: " + error, error);
	            return false;
	        }
	 }
	 
	 public RatingObject getRating(String id) {
		 RatingObject obj = null;
	        SupplierDao dao = (SupplierDao) getDao();
	        try {
	            obj = dao.getRating(id);
	          
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in getRating: " + error, error);
	          
	        }
	        
	        return obj;
	 }
	 
	 public Collection viewQuatation(String ppID, int index, String sort, boolean desc, int start, int rows) {
		  SupplierDao dao = (SupplierDao) getDao();
		  Collection supplierList = null;
	      try {
	    	 supplierList= dao.viewQuatation(ppID, index, sort, desc, start, rows);
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in viewQuatation: " + error, error);
	           
	      }
	        return supplierList;
	 }
	 
	 public int countSupplier(String ppID) {
		    SupplierDao dao = (SupplierDao) getDao();
	        try {
	             return dao.countListing(ppID);
	          
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in countSupplier: " + error, error);
	            return 0;
	        }
	 }
	 
	 public boolean approvedSupplier(SupplierObject obj) {
		    SupplierDao dao = (SupplierDao) getDao();
	        try {
	            dao.approvedSupplier(obj);
	            return true;
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in approvedSupplier: " + error, error);
	            return false;
	        }
     }
	
	 public boolean disapprove(SupplierObject obj) {
		    SupplierDao dao = (SupplierDao) getDao();
	        try {
	            dao.disapprove(obj);
	            return true;
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in disapprove: " + error, error);
	            return false;
	        }
	}
	 
	public Collection getSupplierID(String ppID, String approved){
		  SupplierDao dao = (SupplierDao) getDao();
		  Collection supplierID = null;
		
	      try {
	    	  supplierID = dao.getSupplierID(ppID, approved);
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in getSupplierID: " + error, error);
	      }
	        
	        return supplierID;
	}
	
	public Collection checkAddedSupplier(String ppID) {
		  SupplierDao dao = (SupplierDao) getDao();
		  Collection supplierList = null;
	      try {
	    	 supplierList= dao.checkAddedSupplier(ppID);;
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in checkAddedSupplier: " + error, error);
	           
	      }
	        return supplierList;
	}
	
	public Collection checkAddedItem(String ppID) {
		  SupplierDao dao = (SupplierDao) getDao();
		  Collection supplierList = null;
	      try {
	    	 supplierList= dao.checkAddedItem(ppID);;
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in checkAddedItem: " + error, error);
	           
	      }
	        return supplierList;
	}
	
	public boolean getApprovedSupplier(String ppID) {
		  SupplierDao dao = (SupplierDao) getDao();
		  Collection supplierList = null;
	      try {
	    	 return dao.getApprovedSupplier(ppID);
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in getApprovedSupplier: " + error, error);
	           
	      }
	      return false;
	}
	
	 public AttachmentObject downloadFile(String attachmentID){
		 SupplierDao dao = (SupplierDao) getDao();
		  AttachmentObject obj = null;
		
	      try {
	    	  obj = dao.downloadFile(attachmentID);
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in downloadFile: " + error, error);
	      }
	        
	        return obj;
	 }
	 
	 public Collection getSupplierItem(String ppID){
		  SupplierDao dao = (SupplierDao) getDao();
		  Collection supplierItem = null;
		
	      try {
	    	  supplierItem = dao.getSupplierItem(ppID);
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in getSupplierItem: " + error, error);
	      }
	        
	        return supplierItem;
	 }
	 
	 public String countTable(String ppID) {
		  SupplierDao dao = (SupplierDao) getDao();
		  String supplierList = null;
	      try {
	    	 supplierList= dao.countTable(ppID);
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in countTable: " + error, error);
	           
	      }
	        return supplierList;
	 }
	 
	 public Collection getSupplierItems(String supplierID, String ppID, int count){
		 SupplierDao dao = (SupplierDao) getDao();
		  Collection singleRequest = null;
	      try {
	    	  singleRequest= dao.getSupplierItems(supplierID, ppID, count);
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in getSupplierItems: " + error, error);
	           
	      }
	        return singleRequest;
	 }
	 
	 public Collection getItems(String ppID, int index) {
		  SupplierDao dao = (SupplierDao) getDao();
		  Collection itemList = null;
	      try {
	    	  itemList= dao.getItems(ppID, index);
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in getItems: " + error, error);
	           
	      }
	        return itemList;
	 }
	 
	 public Collection getApprovedSupplier(String ppID, int index, String tableName, String sort, boolean desc, int start, int rows) {
		  SupplierDao dao = (SupplierDao) getDao();
		  Collection supplierList = null;
	      try {
	    	 supplierList= dao.getApprovedSupplier(ppID, index, tableName, sort, desc, start, rows);
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in getApprovedSupplier: " + error, error);
	           
	      }
	        return supplierList;
	 }
	 
	 public int countAprrovedSupplierListing(String ppID, String name, String searchCol) {
		    SupplierDao dao = (SupplierDao) getDao();
	        try {
	             return dao.countAprrovedSupplierListing(ppID, name, searchCol);
	          
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in countAprrovedSupplierListing: " + error, error);
	            return 0;
	        }
	 }
	 
	 public boolean checkInsertedData(String ppID) {
		  SupplierDao dao = (SupplierDao) getDao();
		  Collection supplierList = null;
	      try {
	    	return dao.checkInsertedData(ppID);
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in checkInsertedData: " + error, error);
	           
	      }
	        return false;
	}
	 
	 public Collection supplierInfo(String ppID){
		  SupplierDao dao = (SupplierDao) getDao();
		  Collection singleRequest = null;
	      try {
	    	  singleRequest= dao.supplierInfo(ppID);
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in supplierInfo: " + error, error);
	           
	      }
	        return singleRequest;
	 }
	 
	 public String getInfo(String type){
		  SupplierDao dao = (SupplierDao) getDao();
	      try {
	    	  return dao.getInfo(type);
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in getInfo: " + error, error);
	           
	      }
	        return null;
	 }
	 
	 public Collection getRequester(String ppID){
		  SupplierDao dao = (SupplierDao) getDao();
		  Collection singleRequest = null;
	      try {
	    	  singleRequest= dao.getRequester(ppID);
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in getRequester: " + error, error);
	           
	      }
	        return singleRequest;
	 }
	 
	 public boolean isSupplierNameExist(String ppID, String company, String tel) {
		 SupplierDao dao = (SupplierDao) getDao();
			boolean isSupplierNameExist = false;
			
			try {
				isSupplierNameExist = dao.isSupplierNameExist(ppID, company, tel);
			}
			catch(DaoException e){
				Log.getLog(getClass()).error("Error in isSupplierNameExist: ", e);
			}
			
			return isSupplierNameExist;
	 }

	 
	 public SupplierObject getSupplier(String id){
		  SupplierDao dao = (SupplierDao) getDao();
		  SupplierObject singleRequest = null;
	      try {
	    	  singleRequest= dao.getSupplier(id);
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in singleRequest: " + error, error);
	           
	      }
	        return singleRequest;
	 }
}
