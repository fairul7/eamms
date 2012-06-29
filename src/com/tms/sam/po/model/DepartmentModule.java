package com.tms.sam.po.model;

import java.util.ArrayList;
import java.util.Collection;

import kacang.model.DefaultModule;
import kacang.util.Log;

public class DepartmentModule extends DefaultModule{
	 public static final String PERMISSION_CREATE_DEPARTMENT = "com.tms.sam.po.AddDept"; // Permission to create new department
	 
	 public boolean addDept(DepartmentObject dpt) {
	        DepartmentDao dao = (DepartmentDao) getDao();
	        try {
	            dao.insertDept(dpt);
	            return true;
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error inserting department record: " + error, error);
	            return false;
	        }
	 }
	 
	 public ArrayList getDepartmentSelectList() {
	        DepartmentDao dao = (DepartmentDao) getDao();
	        ArrayList alist = new ArrayList();
	        
	        try {
	            alist = dao.getDepartmentSelectList();
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error retrieving department list: " + error, error);
	        }
	        
	        return alist;
	   }
	  
	 public Collection getUserID(String departmentID){
		 DepartmentDao dao = (DepartmentDao) getDao();
		 
		 Collection userID = null;
		 try 
		 {
			 userID = dao.getUserID(departmentID);
	     }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error retrieving user ID: " + error, error);
	     }
	        return userID;
	 }
	 
	 public Collection getDeptCode(String departmentID){
		 DepartmentDao dao = (DepartmentDao) getDao();
		 Collection deptCode = null;
		 
		 try 
		 {
			 deptCode = dao.getDeptCode(departmentID);
	     }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error retrieving department code: " + error, error);
	     }
	        return deptCode;
	 }
	 
	 public boolean isHOD(String userID) {
		    boolean isHOD = false;
	        DepartmentDao dao = (DepartmentDao) getDao();
	        try {
	            isHOD = dao.isHOD(userID);
	           
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error validating if user is HOD: " + error, error);
	            
	        }
	        
	        return isHOD;
	 }
	 
	 public Collection getDepartmentListing(String name, String searchCol, String sort, boolean desc, int start, int rows) {
		 DepartmentDao dao = (DepartmentDao) getDao();
		  Collection supplierList = null;
	      try {
	    	 supplierList= dao.getDepartmentListing(name, searchCol, sort, desc, start, rows);;
	           
	      }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error finding Listing Item: " + error, error);
	           
	      }
	        return supplierList;
	 }
	 
	 public int countListing(String name, String searchCol) {
		 DepartmentDao dao = (DepartmentDao) getDao();
	        try {
	             return dao.countListing(name, searchCol);
	          
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error deleting request record: " + error, error);
	            return 0;
	        }
	 }
	 
	 public DepartmentObject getDepartment(String departmentID){
		 DepartmentDao dao = (DepartmentDao) getDao();
		 
		 DepartmentObject deparmentinfo = null;
		 try 
		 {
			 deparmentinfo = dao.getDepartment(departmentID);
	     }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error retrieving user ID: " + error, error);
	     }
	        return deparmentinfo;
	 }
	 
	 public boolean deleteDept(String id) {
		 DepartmentDao dao = (DepartmentDao) getDao();
	       try {
	            dao.deleteDept(id);
	            return true;
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error deleting request record: " + error, error);
	            return false;
	        }
	 }
}
