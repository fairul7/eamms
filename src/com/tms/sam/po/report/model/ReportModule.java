package com.tms.sam.po.report.model;

import java.util.Collection;

import kacang.model.DefaultModule;
import kacang.util.Log;

public class ReportModule extends DefaultModule{
	 public Collection getSuppliersRating(Collection ids, String sort, boolean desc, int start, int rows){
		  ReportDao dao = (ReportDao) getDao();
		  Collection suppliersRating = null;
	      try {
	    	  suppliersRating= dao.getSuppliersRating(ids,sort,desc,start,rows);
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in suppliersRating: " + error, error);
	           
	      }
	        return suppliersRating;
	 }
	 
	 public int countSuppliersRating(Collection ids) {
		 	ReportDao dao = (ReportDao) getDao();
	        try {
	             return dao.countSuppliersRating(ids);
	          
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in countSuppliersRating: " + error, error);
	            return 0;
	        }
	 }
	 
	 public Collection getStatusListing(String fromDate, String toDate, String status, String reqDept,  
				String sort, boolean desc, int start, int row){
		  ReportDao dao = (ReportDao) getDao();
		  Collection suppliersRating = null;
	      try {
	    	  suppliersRating= dao.getStatusListing(fromDate, toDate, status, reqDept, sort, desc, start, row);
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in getStatusListing: " + error, error);
	           
	      }
	        return suppliersRating;
	 }
	 
	 public int countStatusListing(String fromDate, String toDate, String status, String reqDept) {
		 	ReportDao dao = (ReportDao) getDao();
	        try {
	             return dao.getStatusListing(fromDate, toDate, status, reqDept, null, false, 0, -1).size();
	          
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error in countSuppliersRating: " + error, error);
	            return 0;
	        }
	 }
}
