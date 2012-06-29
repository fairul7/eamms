package com.tms.sam.po.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DataSourceDao;

public class DepartmentDao extends DataSourceDao {
	public void init() throws DaoException {
		try{
		    super.update("CREATE TABLE po_dept (" +
		        		 "deptID VARCHAR(100) NOT NULL, " +
		        		 "deptCode VARCHAR(100) NOT NULL, " +
		        		 "deptName VARCHAR(255),"+
		        		 "PRIMARY KEY(deptID))", null);
		 }catch(DaoException e){}
	     
	     try{
		     super.update("CREATE TABLE po_hod (" +
		    		 	  "id VARCHAR(100) NOT NULL, " +
	       		          "userID VARCHAR(100))", null);
	     }catch(DaoException e){}    
     
	     try{
	     	super.update("CREATE INDEX user_index ON po_hod(userID)",null);
	     }catch(DaoException e){}
     }
	 
	 public void insertDept(DepartmentObject dpt) throws DaoException {
		 String code = "";
		 code =  dpt.getDeptCode();
		 
		 Collection dptID = null;
		 dptID = super.select("SELECT deptID "+ 
							  "FROM po_dept " +
						      "WHERE deptID=?",
				              HashMap.class,new Object[] {dpt.getDeptID()}, 0, 1);
			 
		 if(dptID.size()== 0){
			 Collection dptCode = null;
			 dptCode = super.select("SELECT deptCode "+ 
									"FROM po_dept " +
									"WHERE deptCode=?",
									HashMap.class,new Object[] {code}, 0, 1);
			 if(dptCode.size()== 0){
			    
			      super.update("INSERT INTO po_dept (" +
			         		   "deptID, deptCode, deptName) VALUES " +
			         		   "(#deptID#,#deptCode#, #deptName#)", dpt);
					 
			      super.update("INSERT INTO po_hod (" +
			         		   "id, userID) VALUES " +
			         		   "(#deptID#, #userID#)", dpt);
			 }
				
		 }else{
			  super.update("UPDATE po_dept SET deptCode=#deptCode#, " +
				 		   "deptName=#deptName# WHERE deptID=#deptID#", dpt);
			  super.update("UPDATE po_hod SET userID=#userID# WHERE id=#deptID#", dpt);
		 }
		
	 }
	 
	 public ArrayList getDepartmentSelectList() throws DaoException {
		 ArrayList alist = new ArrayList();
		 Collection col = super.select("SELECT deptID, deptName FROM po_dept ",
				 SelectDepartmentObject.class, null, 0, -1);
         
         for(Iterator i=col.iterator(); i.hasNext(); ) {
             alist.add((SelectDepartmentObject) i.next());
         }
         return alist;
	 }
	 
	 public Collection getUserID(String departmentID) throws DaoException, DataObjectNotFoundException {
		  Object[] args = new Object[] {
				  departmentID
		        };
		  Collection userID = null;
		 
		  userID = super.select("SELECT userId FROM po_hod WHERE id=? " ,
	               HashMap.class,args, 0, -1);
		  
		  if (userID.size() == 0) {
	            throw new DataObjectNotFoundException();
	      } else {
	            return userID;
	      }
	 }
	 
	 public Collection getDeptCode(String departmentID)throws DaoException, DataObjectNotFoundException{
		  Object[] args = new Object[] {
				  departmentID
		        };
		  Collection deptCode = null;
		  
		  deptCode = super.select("SELECT deptCode FROM po_dept WHERE deptID=? " ,
	                 HashMap.class,args, 0, -1);
		  
		  if (deptCode.size() == 0) {
	            throw new DataObjectNotFoundException();
	      } else {
	            return deptCode;
	      }
	 }
	 
	 public boolean isHOD(String userID) throws DaoException, DataObjectNotFoundException {
		  boolean isHOD = false;
		  Collection headOfDpt = null;
		  Object[] args = new Object[] {
				  userID
		        };
		 
		  headOfDpt = super.select("SELECT id FROM po_hod WHERE userID=? " ,
	                 HashMap.class,args, 0, -1);
		  
		  if(headOfDpt.size() > 0) {
			  isHOD = true;
          }
		  
		  return isHOD;
	 }
	 
	 public Collection getDepartmentListing(String name, String searchCol, String sort, boolean desc, int start, int rows) throws DaoException ,DataObjectNotFoundException{
			
			String condition = (name != null) ? "%" + name + "%" : "%";
			
			String orderBy = (sort != null) ? sort : "deptID";
			if (desc)
	            orderBy += " DESC";
			
			Object[] args = {condition};
			
	        String columnName = "deptCode";
	        
	        if(! "".equals(searchCol)) {
	           if("deptName".equals(searchCol)) {
	                    columnName = "deptName";
	           }
	           
	        }
	        
			Collection request = null;
			request = super.select("SELECT dept.deptID, " +
					  "dept.deptCode, dept.deptName, user.username " +
					  "FROM po_dept dept, po_hod hod, security_user user " +
					  "WHERE dept.deptID = hod.id AND " +
					  "hod.userID = user.id AND " +
		              columnName + " LIKE ? " +
		              "ORDER BY " + orderBy, DepartmentObject.class,args, start, rows);
			
			return request;
	  }
	 
	  public int countListing(String name, String searchCol) throws DaoException, DataObjectNotFoundException{
			String condition = (name != null) ? "%" + name + "%" : "%";
			int total = 0;	
			
			Object[] args = {condition };
			Collection countrequest = null;
			String columnName = "deptCode";
		        
		    if(! "".equals(searchCol)) {
		       if("responded".equals(searchCol)) {
		                    columnName = "deptName";
		       }
		    }
	        
	        countrequest = super.select("SELECT COUNT(*) AS total " + 
					       "FROM po_dept " +
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
	 
	 public DepartmentObject getDepartment(String departmentID) throws DaoException, DataObjectNotFoundException {
		  Object[] args = new Object[] {
				  departmentID
		        };
		  
		  Collection info = null;
		  DepartmentObject deptInfo = null;
		  info = super.select("SELECT dept.deptID, dept.deptCode, " +
		  		 "dept.deptName, user.username, hod.userID " +
				 "FROM po_dept dept, po_hod hod, security_user user " +
				 "WHERE dept.deptID=? AND " +
				 "hod.id = dept.deptID AND " +
				 "user.id = hod.userID"  ,
	             DepartmentObject.class,args, 0, 1);
		  
		  if (info.size() == 0) {
	            throw new DataObjectNotFoundException();
	      } else {
	            for(Iterator i = info.iterator();i.hasNext();){
	            	deptInfo = (DepartmentObject)i.next();
	            }
	      }
		  return deptInfo;
	 }
	 
	 public void deleteDept(String id) throws DaoException, DataObjectNotFoundException{
		  Object[] args = new String[] {
		           id
		  };
			
		  super.update("DELETE FROM po_dept WHERE deptID=?", args);
		  super.update("DELETE FROM po_hod WHERE id=?", args);
	 }
}
