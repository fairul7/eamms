package com.tms.ekms.manpowertemp.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.tms.fms.util.SqlUtil;

import kacang.model.DaoException;
import kacang.model.DataSourceDao;

public class ManpowerDao extends DataSourceDao {
	public void init() throws DaoException{
/*		
		try{
			super.update("CREATE TABLE fms_leaveType_setup ("+
					"id varchar(255) NOT NULL default '',"+
					"leaveType varchar(255) default NULL,"+
					"leaveTypeName varchar(255) default NULL,"+
					"description varchar(255) default NULL,"+
					"createdBy varchar(255),"+
					"createdDate datetime NULL,"+
					"updatedBy varchar(255) ,"+
					"updatedDate datetime NULL," +
					"PRIMARY KEY (id))",null); 
		}catch (Exception e){	
		}
		
		try{
			super.update("CREATE TABLE fms_manpow_leave_setup ("+
					"id varchar(255) NOT NULL default '',"+
					"type CHAR(1),"+
					"leaveType varchar(255),"+
					"holiday varchar(255),"+
					"dateFrom datetime NULL,"+
					"dateTo datetime NULL,"+
					"createdBy varchar(255),"+
					"createdDate datetime NULL,"+
					"updatedBy varchar(255) ,"+
					"updatedDate datetime NULL," +
					"PRIMARY KEY (id))",null); 		
		}catch (Exception e){	
		}
	
		
		try{
			super.update("CREATE TABLE fms_manpower_leave ("+
					"id varchar(255) NOT NULL default '',"+
					"leaveId varchar(255),"+
					"manpowerId varchar(255),"+
					"PRIMARY KEY (id))", null);
					//"FOREIGN KEY(leaveId) references fms_manpow_leave_setup(id)", null);   //remove this line because of this error:ERROR 16 Mar 2009 18:11:12 com.tms.ekms.manpowertemp.model.ManpowerDao  - kacang.model.DaoException: com.microsoft.sqlserver.jdbc.SQLServerException: Incorrect syntax near ','.
		}catch (Exception e){	
			
			Log.getLog(getClass()).error("Error creating fms_manpower_leave"+e);
			
		}
*/		
	}
	
	//LeaveTypeSetup
	 public void addLeaveType(LeaveTypeObject object) throws DaoException {
	        super.update("INSERT INTO fms_leaveType_setup(id,leaveType,leaveTypeName,description,createdBy,createdDate,updatedBy,updatedDate) VALUES " +
	            		"(#id#, #leaveType#, #leaveTypeName#, #description#, #createdBy#, #createdDate#, #updatedBy#, " +
	            		"#updatedDate#)", object);
	 }
	
	public void updateLeaveType(LeaveTypeObject leaveobj)throws DaoException{
		String sql= "UPDATE fms_leaveType_setup SET leaveType=#leaveType#,leaveTypeName=#leaveTypeName#,description=#description#," +
					"createdBy=#createdBy#, createdDate=#createdDate#," +
					"updatedBy=#updatedBy#, updatedDate=#updatedDate# WHERE id=#id#"; 
		super.update(sql, leaveobj);
	}
	
	public void deleteLeaveType(String id) throws DaoException{
		super.update("DELETE fms_leaveType_setup WHERE id=?", new String[] {id});
	}
	
	public Collection selectLeaveType(String id) throws DaoException{
		return super.select("SELECT * from fms_leaveType_setup WHERE id=?", LeaveTypeObject.class, new String[] {id}, 0, -1);
	}
	
	//LeaveTypeSetup table rows
	public Collection selectLeaveType(String search, String sort, boolean desc, int startIndex,int maxRows) throws DaoException{
    	
    	String strSort= "";
        if (sort != null) {
            strSort += " ORDER BY " + sort;
            if (desc)
                strSort += " DESC";
        }else 
        	strSort += " ORDER BY leaveType,leaveTypeName ASC";
        
        String searchByLeaveType=""; 	        
        searchByLeaveType="WHERE leaveTypeName LIKE '%"+ ((search==null || search.equals(""))?"":search) +"%'  ";
        String sql = "SELECT id,leaveType,leaveTypeName,description " +
        			 "FROM fms_leaveType_setup ";
        return super.select(sql + searchByLeaveType + strSort,HashMap.class,null,startIndex,maxRows);	       
       	        	        	
     }
	       	        	        	
      public int selectLeaveTypeCount(String search, String sort, boolean desc, int startIndex,int maxRows) throws DaoException{
    	int total=0;
    	
        String searchByLeaveType=""; 	        
        searchByLeaveType="WHERE leaveTypeName LIKE '%"+ ((search==null || search.equals(""))?"":search) +"%'  ";
        String sql = "SELECT COUNT(id) as total " +
		 			 "FROM fms_leaveType_setup ";
        
        Collection list=  super.select(sql + searchByLeaveType,HashMap.class,null,startIndex,maxRows);
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Map object = (Map) iterator.next();
			total=Integer.parseInt(object.get("total").toString());
		}
       	return total;	        	
      }       	

	 //setholidayleaveSetup
	 public void addHolidayLeave(SetHolidayLeaveObject s) throws DaoException{
		super.update("INSERT INTO fms_manpow_leave_setup (id, type,leaveType,holiday,dateFrom,dateTo," +
				     "createdBy,createdDate,updatedBy,updatedDate) VALUES "+
					 "(#id#, #type#, #leaveType#, #holiday#, #dateFrom#, #dateTo#, #createdBy#, #createdDate#, #updatedBy#, " +
					 "#updatedDate#)", s);
	 }
	
	 public void updateHolidayLeave(SetHolidayLeaveObject hlobj)throws DaoException{
		String sql= "UPDATE fms_manpow_leave_setup SET type=#type#,leaveType=#leaveType#,holiday=#holiday#," +
				    "dateFrom=#dateFrom#,dateTo=#dateTo#,createdBy=#createdBy#, createdDate=#createdDate#," +
				    "updatedBy=#updatedBy#, updatedDate=#updatedDate# " +
				    "WHERE id=#id#"; 
		super.update(sql, hlobj);
	 }
	 
	 public void deleteHolidayLeave(String id) throws DaoException{
			super.update("DELETE fms_manpow_leave_setup WHERE id=?", new String[] {id});
	 }
	 
	 public Collection selectHolidayLeave(String id) throws DaoException{
			return super.select("SELECT * from fms_manpow_leave_setup WHERE id=?", SetHolidayLeaveObject.class, new String[] {id}, 0, -1);
		}
	 
	 public Collection getLeaveType() throws DaoException {
		Collection list=new ArrayList();
		
		String sql="SELECT id,leaveType,leaveTypeName,description,createdBy,createdDate,updatedBy,updatedDate FROM fms_leaveType_setup ";
		
		list=super.select(sql, HashMap.class , null, 0, -1);
		
		return list;
	 }
	
	 //table row for set holiday
	 public Collection selectHoliday(String year, String sort, boolean desc, int startIndex, int maxRows) throws DaoException{
		Collection holidays = new ArrayList();
		String strSort= "";
        if (sort != null) {
            strSort += " ORDER BY " + sort;
            if (desc)
                strSort += " DESC";
        }else 
        	//strSort += " ORDER BY id ASC";
        	strSort += " ORDER BY dateFrom ASC";
        
        String sql = "SELECT id,holiday,dateFrom,dateTo FROM fms_manpow_leave_setup WHERE type='H' AND year(dateFrom) = " + year;
        holidays = super.select(sql + strSort, HashMap.class, null, startIndex, maxRows);
        return holidays;
	 }
	//table row count for set holiday
	 public int selectHolidayCount(String year, String sort, boolean desc, int startIndex, int maxRows) throws DaoException{
		int total=0;
        
        String sql = "SELECT COUNT(id) as total FROM fms_manpow_leave_setup WHERE type='H' AND year(dateFrom) = " + year;
        Collection list=  super.select(sql,HashMap.class,null,startIndex,maxRows);
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Map object = (Map) iterator.next();
			total=Integer.parseInt(object.get("total").toString());
		}
        return total;
	 }
	 
	 public boolean isExistHoliday(String holiday) throws DaoException{
		 Collection list = new ArrayList();
		 boolean isExist=false;
		 list=super.select("SELECT id FROM fms_manpow_leave_setup WHERE holiday like ?", HashMap.class, new String[] {holiday},0,-1);
		 if(list.size()>0){
			 isExist=true;
		 }else{
			 isExist=false;
		 }
		 return isExist;
	 }
	 
	//table row for set leave
	public Collection selectLeave(String year, Date fromDate, Date toDate, ArrayList unitIds, String sort, boolean desc, int startIndex, int maxRows) throws DaoException{
		Collection leaves = new ArrayList();
		String strSort="";
		if (sort != null) {
            strSort += " ORDER BY " + sort;
            if (desc)
                strSort += " DESC";
        }else 
        	strSort += " ORDER BY manpowerName, dateFrom ASC ";

		ArrayList params = new ArrayList();
		
		String sql="SELECT distinct fml.id AS manpowerLeaveId, u.firstName + ' ' + u.lastName AS manpowerName, fml.manpowerId as manpowerId, fmls.id,fls.leaveTypeName as leaveType,fmls.dateFrom,fmls.dateTo " +
		"FROM fms_manpow_leave_setup fmls, fms_leaveType_setup fls, fms_manpower_leave fml, security_user u " +
		"WHERE fml.leaveId=fmls.id AND fmls.leaveType=fls.id AND fml.manpowerId = u.id ";
		
		// filter by year
		sql += "AND year(fmls.dateFrom) = ? ";
		params.add(year);
		
		// filter by unit
		sql += "AND u.unit IN (" + SqlUtil.placeholders(unitIds.size()) + ") ";
		params.addAll(unitIds);
		
		// filter by date
		if (fromDate!=null && toDate!=null) {
			sql+=" AND (( fmls.dateFrom between ? AND ? ) OR ( fmls.dateTo between ? AND ? )) ";
			params.add(fromDate);
			params.add(toDate);
			params.add(fromDate);
			params.add(toDate);
		}
		
		leaves = super.select(sql+strSort, HashMap.class, params.toArray(), startIndex, maxRows);
		return leaves;
	}
	 
	//table row count for set leave
	 public int selectLeaveCount(String year, Date fromDate, Date toDate, String sort, boolean desc, int startIndex, int maxRows) throws DaoException{
		int total=0;
		
		String strSort= "";
    	
        if (sort != null) {
            strSort += " ORDER BY " + sort;
            if (desc)
                strSort += " DESC";
        }else 
        	strSort += " ORDER BY id ASC";
        
        String sql = "SELECT COUNT(fmls.id) as total FROM fms_manpow_leave_setup fmls, fms_leaveType_setup fls " +
        		     "WHERE 1=1 AND fmls.leaveType=fls.id AND year(fmls.dateFrom) = " + year;
        ArrayList params = new ArrayList();
		
		if(fromDate!=null && toDate!=null){
			sql+=" AND (( fmls.dateFrom between ? AND ? ) OR ( fmls.dateTo between ? AND ? )) ";
			params.add(fromDate);
			params.add(toDate);
			params.add(fromDate);
			params.add(toDate);
		}
		
        Collection lst= super.select(sql+strSort,HashMap.class, params.toArray(),startIndex,maxRows);
        	for(Iterator iterator = lst.iterator(); iterator.hasNext();){
        		Map obj=(Map)iterator.next();
        		total=Integer.parseInt(obj.get("total").toString());
        	}
		return total;
	}
	//manpowerLeave 
	public void addManpowerLeave(ManpowerLeaveObject mlo) throws DaoException{
		super.update("INSERT INTO fms_manpower_leave(id, leaveId, manpowerId) VALUES " +
				     "(#id#, #leaveId#, #manpowerId#)", mlo);
	}
	
	public void updateManpowerLeave(ManpowerLeaveObject mlo) throws DaoException{
		String sql="UPDATE fms_manpower_leave SET leaveId=#leaveId#, manpowerId=#manpowerId# " +
				    "WHERE id=#id#";
		super.update(sql, mlo);
	}
	
	public void deleteManpowerLeave(String id) throws DaoException{
		super.update("DELETE fms_manpower_leave WHERE leaveId=?", new String[] {id});
	}
	
	public void deleteManpowerLeaveById(String id) throws DaoException{
		super.update("DELETE fms_manpower_leave WHERE id=?", new String[] {id});
	}
	
	public void deleteManpowerLeaveSetupById(String id) throws DaoException{
		super.update("DELETE fms_manpow_leave_setup WHERE id=?", new String[] {id});
	}
	
	public Collection selectManpowerLeave(String id) throws DaoException{
		return super.select("SELECT * from fms_manpower_leave WHERE id=?", ManpowerLeaveObject.class, new String[] {id}, 0, 1);
	}
	
	public Collection selectManpowerLeaveByLeaveId(String leaveId) throws DaoException{
		return super.select("SELECT * from fms_manpower_leave WHERE leaveId=?", ManpowerLeaveObject.class, new String[] {leaveId}, 0, -1);
	}
}
