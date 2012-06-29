package com.tms.sam.po.model;

import java.util.Collection;

import kacang.model.DefaultModule;
import kacang.util.Log;

public class PurchaseOrderModule extends DefaultModule{
	 public boolean insertPurchaseOrder(PurchaseOrderObject pr, PurchaseRequestObject prObj) {
		 	PurchaseOrderDao dao = (PurchaseOrderDao) getDao();
	        try {
	            
	            return dao.insertPurchaseOrder(pr, prObj);
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in insertPurchaseOrder: " + error, error);
	            return false;
	        }
	 }
	 
	 public boolean checkRecord(String ppID) {
		 	PurchaseOrderDao dao = (PurchaseOrderDao) getDao();
	        try {
	            
	            return dao.checkRecord(ppID);
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in checkRecord: " + error, error);
	            return false;
	        }
	 }
	 
	 public Collection getRequestListing(String name, String searchCol, String sort, boolean desc, int start, int rows) {
		  PurchaseOrderDao dao = (PurchaseOrderDao) getDao();
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
		 PurchaseOrderDao dao = (PurchaseOrderDao) getDao();
	        try {
	             return dao.countRequestListing(name, searchCol);
	          
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in countRequestListing: " + error, error);
	            return 0;
	        }
	 }
	 
	 public PurchaseOrderObject getPurchaseOrder(String ppID, String supplierId, int count) {
		 	PurchaseOrderDao dao = (PurchaseOrderDao) getDao();
	        try {
	            return dao.getPurchaseOrder(ppID, supplierId, count);
	           
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in getPurchaseOrder: " + error, error);
	            
	        }
	        
	        return null;
	 }
	 
	 public PurchaseOrderObject getClosedPurchaseOrder(String ppID, String supplierId, int count) {
		 	PurchaseOrderDao dao = (PurchaseOrderDao) getDao();
	        try {
	            return dao.getClosedPurchaseOrder(ppID, supplierId, count);
	           
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in getClosedPurchaseOrder: " + error, error);
	            
	        }
	        
	        return null;
	 }
	 
	 public boolean insertClosedPurchaseOrder(PurchaseOrderObject pr, PurchaseRequestObject prObj) {
		 	PurchaseOrderDao dao = (PurchaseOrderDao) getDao();
	        try {
	            dao.insertClosedPurchaseOrder(pr, prObj);
	            return true;
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in insertClosedPurchaseOrder: " + error, error);
	            return false;
	        }
	 }
}
