package com.tms.sam.po.model;

import java.util.Collection;

import kacang.model.DefaultModule;
import kacang.util.Log;

public class PrePurchaseModule extends DefaultModule {
	 public boolean addPurchseRequest(PurchaseRequestObject pr) {
	        PrePurchaseDao dao = (PrePurchaseDao) getDao();
	        try {
	            dao.insertRequest(pr);
	            return true;
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in addPurchseRequest: " + error, error);
	            return false;
	        }
	 }
	 
	 
	 public Collection getPurchaseCode(){
		  PrePurchaseDao dao = (PrePurchaseDao) getDao();
		  Collection purchaseCode = null;
	      try {
	           purchaseCode= dao.getPurchaseCode();
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in getPurchaseCode: " + error, error);
	           
	      }
	        return purchaseCode;
	 }
	 
	 public Collection getRequestItems(String name, String searchCol, String status, String sort, boolean desc, int start, int rows) {
		  PrePurchaseDao dao = (PrePurchaseDao) getDao();
		  Collection requestItems = null;
	      try {
	    	  requestItems= dao.getRequestItems(name, searchCol, status, sort, desc, start, rows);;
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in getRequestItems: " + error, error);
	           
	      }
	        return requestItems;
	 }
	 
	 public boolean deleteRequest(String id) {
		    PrePurchaseDao dao = (PrePurchaseDao) getDao();
	        try {
	            dao.deleteRequest(id);
	            return true;
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in deleteRequest: " + error, error);
	            return false;
	        }
     }
	 
	 public int countRequest(String name, String searchCol) {
		    PrePurchaseDao dao = (PrePurchaseDao) getDao();
	        try {
	             return dao.countRequest(name, searchCol);
	          
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in countRequest: " + error, error);
	            return 0;
	        }
	 }
	 
	 public PurchaseRequestObject singleRequest(String ppID){
		  PrePurchaseDao dao = (PrePurchaseDao) getDao();
		  PurchaseRequestObject singleRequest = null;
	      try {
	    	  singleRequest= dao.singleRequest(ppID);
	           
	    	  
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in singleRequest: " + error, error);
	           
	      }
	        return singleRequest;
	 }
	 
	 public Collection getListingItem(String name, String searchCol, String sort, boolean desc, int start, int rows) {
		  PrePurchaseDao dao = (PrePurchaseDao) getDao();
		  Collection listingItems = null;
	      try {
	    	  listingItems= dao.getListingItem(name, searchCol, sort, desc, start, rows);;
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in getListingItem: " + error, error);
	           
	      }
	        return listingItems;
	 }
	 
	 public int countListingRequest(String name, String searchCol) {
		    PrePurchaseDao dao = (PrePurchaseDao) getDao();
	        try {
	             return dao.countListingRequest(name, searchCol);
	          
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in countListingRequest: " + error, error);
	            return 0;
	        }
	 }
	 
	 public void saveDecision(PrePurchaseApprovalObject obj,PurchaseRequestObject po){
		    PrePurchaseDao dao = (PrePurchaseDao) getDao();
	        try {
	             dao.saveDecision(obj,po);
	          
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in saveDecision: " + error, error);
	            
	        }
	 }
	 
	 public String getReason(String ppID, String status){
		  PrePurchaseDao dao = (PrePurchaseDao) getDao();
		  String reason="";
		
	      try {
	    	 reason = dao.getReason(ppID, status);
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in getReason: " + error, error);
	           
	      }
	        return reason;
	 }
	 
	 public Collection getAttachment(String ppID){
		  PrePurchaseDao dao = (PrePurchaseDao) getDao();
		  Collection attachmentObj = null;
		
	      try {
	    	  attachmentObj = dao.getAttachment(ppID);
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in getAttachment: " + error, error);
	      }
	        
	        return attachmentObj;
	 }
	 
	 public AttachmentObject downloadFile(String attachmentID){
		  PrePurchaseDao dao = (PrePurchaseDao) getDao();
		  AttachmentObject obj = null;
		
	      try {
	    	  obj = dao.downloadFile(attachmentID);
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in downloadFile: " + error, error);
	      }
	        
	        return obj;
	 }
	 
	 public void changeDecision(PurchaseRequestObject po){
		    PrePurchaseDao dao = (PrePurchaseDao) getDao();
	        try {
	             dao.changeDecision(po);
	          
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in changeDecision: " + error, error);
	            
	        }
	 }
	 
	 public Collection getRequesterID(String ppID){
		  PrePurchaseDao dao = (PrePurchaseDao) getDao();
		  Collection id= null;
		
	      try {
	    	  id = dao.getRequesterID(ppID);
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in getRequesterID: " + error, error);
	           
	      }
	        return id;
	 }
	 
	 public String getPCode(String ppID){
		  PrePurchaseDao dao = (PrePurchaseDao) getDao();
		  String purchaseCode = "";
	      try {
	           purchaseCode= dao.getPCode(ppID);
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in getPCode: " + error, error);
	           
	      }
	        return purchaseCode;
	 }
	 
	 public Collection getHODListing(String name, String searchCol, String sort, boolean desc, int start, int rows) {
		  PrePurchaseDao dao = (PrePurchaseDao) getDao();
		  Collection listingItems = null;
	      try {
	    	  listingItems= dao.getHODListing(name, searchCol, sort, desc, start, rows);;
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in getHODListing: " + error, error);
	           
	      }
	        return listingItems;
	 }
	 
	 public int countHODListing(String name, String searchCol, String user) {
		    PrePurchaseDao dao = (PrePurchaseDao) getDao();
	        try {
	             return dao.countHODListing(name, searchCol, user);
	          
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in countHODListing: " + error, error);
	            return 0;
	        }
	 }
	 
	 public Collection getRequestListing(String name, String searchCol, String sort, boolean desc, int start, int rows) {
		  PrePurchaseDao dao = (PrePurchaseDao) getDao();
		  Collection listingItems = null;
	      try {
	    	  listingItems= dao.getRequestListing(name, searchCol, sort, desc, start, rows);
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in getRequestListing: " + error, error);
	           
	      }
	        return listingItems;
	 }
	 
	 public int countRequestListing(String name, String searchCol) {
		    PrePurchaseDao dao = (PrePurchaseDao) getDao();
	        try {
	             return dao.countRequestListing(name, searchCol);
	          
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in countRequestListing: " + error, error);
	            return 0;
	        }
	 }
	 
	 public String getStatus(String ppID) {
		    String status = "";
		    PrePurchaseDao dao = (PrePurchaseDao) getDao();
	        try {
	        	status = dao.getStatus(ppID);
	        		           
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in getStatus: " + error, error);
	            
	        }
	        
	        return status;
	 }
	 
	 public Collection getHOD(String currenctUserId){
		 PrePurchaseDao dao = (PrePurchaseDao) getDao();
		 
		 Collection hod = null;
		 try 
		 {
			 hod = dao.getHOD(currenctUserId);
	     }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in getHOD: " + error, error);
	     }
	        return hod;
	 }
	 
	 public Collection getSupplierItems(String ppID){
		  PrePurchaseDao dao = (PrePurchaseDao) getDao();
		  Collection singleRequest = null;
	      try {
	    	  singleRequest= dao.getSupplierItems(ppID);
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in getSupplierItems: " + error, error);
	           
	      }
	        return singleRequest;
	 }
	 
	 public Collection getPurchaseOrderCode(){
		  PrePurchaseDao dao = (PrePurchaseDao) getDao();
		  Collection purchaseCode = null;
	      try {
	           purchaseCode= dao.getPurchaseOrderCode();
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in getPurchaseOrderCode: " + error, error);
	           
	      }
	        return purchaseCode;
	 }
	 
	 public int countRequest(String status) {
		    PrePurchaseDao dao = (PrePurchaseDao) getDao();
	        try {
	             return dao.countRequest(status);
	          
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in countRequest: " + error, error);
	            return 0;
	        }
	 }
	 
	 public Collection getItems(String ppID){
		  PrePurchaseDao dao = (PrePurchaseDao) getDao();
		  Collection items = null;
	      try {
	    	  items= dao.getItems(ppID);
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in getPurchaseCode: " + error, error);
	           
	      }
	        return items;
	 }
	 
	 public boolean resubmitRequest(String id) {
		    PrePurchaseDao dao = (PrePurchaseDao) getDao();
	        try {
	            dao.resubmitRequest(id);
	            return true;
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in deleteRequest: " + error, error);
	            return false;
	        }
  }
}
