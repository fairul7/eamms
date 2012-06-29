package com.tms.fms.facility.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.DataSourceDao;
import kacang.model.DefaultDataObject;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorIn;
import kacang.util.JdbcUtil;
import kacang.util.Log;

import com.tms.crm.sales.misc.DateUtil;
import com.tms.fms.engineering.model.Assignment;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.engineering.model.UnitHeadDao;
import com.tms.fms.engineering.model.UnitHeadModule;
import com.tms.fms.engineering.ui.ServiceDetailsForm;

public class FacilityDao extends DataSourceDao{
	
	public void init() throws DaoException{
		
		try{super.update("CREATE TABLE fms_facility_category(id varchar(255) NOT NULL PRIMARY KEY, name " +
				         "varchar(255) NULL, description text NULL, department_id varchar(255) NULL, " +
				         "unit_id varchar(255) NULL, parent_cat char(1) NULL, parent_cat_id varchar(255) " +
				         "NULL, status char(1) NULL, createdby varchar(255) NULL, createdby_date datetime" +
				         " NULL, updatedby varchar(255) NULL, updatedby_date datetime NULL)", null);
		} catch (Exception e) {}
		try{super.update("CREATE TABLE fms_facility(id varchar(255) NOT NULL PRIMARY KEY, name varchar(255)" +
				         " NULL, description text NULL, category_id varchar(255) NULL, channel_id varchar(255)" +
				         " NULL, maketype varchar(255) NULL, model_name varchar(255) NULL, quantity int NULL," +
				         " is_pm char(1) NULL, pm_type char(1) NULL, pm_month varchar(50) NULL, pm_year " +
				         "varchar(50) NULL, is_pool char(1) NULL, have_child char(1) NULL, status char(1)" +
				         " NULL, createdby varchar(255) NULL, createdby_date datetime NULL, updatedby" +
				         " varchar(255) NULL, updatedby_date datetime NULL)", null);
		} catch (Exception e) {}
		try{super.update("CREATE TABLE fms_facility_related_item(id varchar(255) NOT NULL PRIMARY KEY, " +
				         "facility_id varchar(255) NULL, related_id varchar(255) NULL)", null);
		} catch (Exception e) {}
		try{super.update("CREATE TABLE fms_facility_item(barcode varchar(255) NOT NULL PRIMARY KEY, " +
						 "facility_id varchar(255) NULL, purchased_date datetime NULL, purchased_cost " +
						 "varchar(255) NULL, do_num varchar(255) NULL, easset_num varchar(255) NULL, " +
						 "status char(1) NUll, createdby varchar(255) NULL, createdby_date datetime " +
						 "NULL, updatedby varchar(255) NULL, updatedby_date datetime NULL)", null);
		} catch (Exception e) {}
		try{super.update("ALTER TABLE fms_facility_item add location_id int NULL, replacement char(1) null", null);
		} catch (Exception e) {}
		try{super.update("ALTER TABLE fms_facility_item alter column location_id varchar(255) NULL", null);
		} catch (Exception e) {}
		try{super.update("ALTER TABLE fms_facility_item ALTER Column do_num text NULL", null);
		} catch (Exception e) {}
		try{super.update("CREATE TABLE fms_facility_writeoff(id varchar(255) PRIMARY KEY, item_code " +
				         "varchar(255), file_name varchar(255) NULL, file_path varchar(255) NULL, " +
				         "file_type varchar(255) NULL, file_size int NULL, date datetime NULL, " +
				         "reason text NULL, createdby varchar(255) NULL, createdby_date datetime " +
				         "NULL, updatedby varchar(255) NULL, updatedby_date datetime NULL)", null);
		} catch (Exception e) {}
		try{super.update("CREATE TABLE fms_facility_missing(id varchar(255) PRIMARY KEY, item_code " +
				         "varchar(255), file_name varchar(255) NULL, file_path varchar(255) NULL, " +
				         "file_type varchar(255) NULL, file_size int NULL, date datetime NULL, " +
				         "reason text NULL, createdby varchar(255) NULL, createdby_date datetime " +
				         "NULL, updatedby varchar(255) NULL, updatedby_date datetime NULL)", null);
		} catch (Exception e) {}
		try{super.update("CREATE TABLE fms_facility_item_checkout(id varchar(255) PRIMARY KEY, " +
				         "checkout_date datetime null, checkout_by " +
				         "varchar(255) null, checkin_date datetime null, " +
				         "checkin_by varchar(255) null, barcode varchar(255) null, purpose " +
				         "varchar(255) null, status char(1) null, createdby " +
				         "varchar(255) NULL, createdby_date datetime NULL, updatedby varchar(255) " +
				         "NULL, updatedby_date datetime NULL)", null);
		} catch (Exception e) {}
		try{super.update("ALTER TABLE fms_facility_item_checkout add location varchar(255) NULL", null);
		} catch (Exception e) {}
		try{
			super.update("ALTER TABLE fms_facility_item_checkout ADD takenBy varchar(255) NULL", null);
		} catch (Exception e) {}
		try{
			super.update("ALTER TABLE fms_facility_item_checkout ADD preparedBy varchar(255) NULL", null);
		} catch (Exception e) {}
		
		try{super.update("CREATE TABLE fms_facility_inactive(id varchar(255) PRIMARY KEY, item_barcode " +
				         "varchar(255), date_from datetime NULL, date_to datetime NULL, reason_id int " +
				         "NULL, createdby varchar(255) NULL, createdby_date datetime NULL, updatedby " +
				         "varchar(255) NULL, updatedby_date datetime NULL)", null);
		} catch (Exception e) {}
		try{super.update("ALTER TABLE fms_facility_inactive ALTER Column reason_id varchar(255) NULL", null);
		} catch (Exception e) {}
		try{super.update("SET IDENTITY_INSERT fms_facility_inactive_reason ON;" +
				         "INSERT INTO fms_facility_inactive_reason(setup_id, name, description, status) " +
						 "values('0', 'Item Damaged', 'Item demaged when user checked in.', 1);" +
						 "SET IDENTITY_INSERT fms_facility_inactive_reason OFF;", null);
		} catch (Exception e) {}
		try{super.update("ALTER TABLE fms_facility_item_checkout add groupId varchar(255) NULL", null);
		} catch (Exception e) {}
		try{super.update("ALTER TABLE fms_facility_item_checkout ADD takenBy VARCHAR(255) NULL", null);
		} catch (Exception e) {}
		
		try{super.update("ALTER TABLE fms_eng_assignment_equipment add utilized char(1) null", null);
		} catch (Exception e) {}
		
		try{super.update("ALTER TABLE fms_eng_request add reason text NULL", null);
		} catch (Exception e) {}
		
		try {
			super.update("CREATE INDEX barcode ON fms_facility_item_checkout(barcode)", null);
		} catch (Exception e) {}
		
		
	}
	
	/*Inactive*/
	public void insertFInactive(FInactiveObject i) throws DaoException {
		String sql = "INSERT INTO fms_facility_inactive (id, item_barcode, date_from, date_to, reason_id, " +
	     "createdby, createdby_date) VALUES (#id#, #item_barcode#, #date_from#, #date_to#, #reason_id#," +
	     " #createdby#, #createdby_date#)";
		super.update(sql, i);
	}
	
	public void deleteFInactive(String id)throws DaoException {
		super.update("DELETE FROM fms_facility_inactive WHERE id=?", new String[] {id});
	}
		
	public Collection selectFInactive(String search, String item_barcode, String reason_id, String sort, boolean desc, int start, int rows) throws DaoException {
		Collection args = new ArrayList();
		String sql = "SELECT i.*, s.username as createdby_name, r.name as reason FROM fms_facility_inactive i, fms_facility_inactive_reason r, security_user s WHERE 1=1 AND i.reason_id=r.setup_id AND s.id=i.createdby";

		if(!"".equals(search) && !"null".equals(search) && search != null){
			sql = sql + " AND (r.name like ? OR s.username like ?)";
			args.add("%"+search+"%");
			args.add("%"+search+"%");
		}
		if(!"".equals(item_barcode) && !"null".equals(item_barcode) && item_barcode != null){
			sql = sql + " AND i.item_barcode=?";
			args.add(item_barcode);
		}
		if(!"-1".equals(reason_id)){
			sql = sql+" AND i.reason_id = ?";
		    args.add(reason_id);
		}
		if (!"".equals(sort) && !"null".equals(sort) && sort != null){
		    sql = sql+" ORDER BY " + sort;
		}   
		if (desc){
			sql = sql+" DESC";
		}
		return super.select(sql, FInactiveObject.class, args.toArray(), start, rows);
	}
	
	public int selectFInactiveCount(String search, String item_barcode, String reason_id) throws DaoException {
		Collection args = new ArrayList();
		String sql = "SELECT count(*) as total FROM fms_facility_inactive i, fms_facility_inactive_reason r, security_user s WHERE 1=1 AND i.reason_id=r.setup_id AND s.id=i.createdby";

		if(!"".equals(search) && !"null".equals(search) && search != null){
			sql = sql + " AND (r.name like ? OR s.username like ?)";
			args.add("%"+search+"%");
			args.add("%"+search+"%");
		}
		if(!"".equals(item_barcode) && !"null".equals(item_barcode) && item_barcode != null){
			sql = sql + " AND i.item_barcode=?";
			args.add(item_barcode);
		}
		if(!"-1".equals(reason_id)){
			sql = sql+" AND i.reason_id = ?";
		    args.add(reason_id);
		}
		Map row = (Map)super.select(sql, HashMap.class, args.toArray(), 0, -1).iterator().next();
	    return Integer.parseInt(row.get("total").toString());
	}
	
	//checkout
	public void insertCheckOut(FacilityObject o) throws DaoException {
		String sql = "INSERT INTO fms_facility_item_checkout (id, checkout_date, " +
				     "checkout_by, barcode, purpose, location, createdby, createdby_date, status,groupId, " +
				     "takenBy, preparedBy) VALUES (#id#, " +
				     "#checkout_date#, #checkout_by#, #barcode#, #purpose#, #location#, " +
				     "#createdby#, #createdby_date#, 'O',#groupId#," +
				     "#takenBy#, #preparedBy#)";
		super.update(sql, o);
	}
	
	public void updateCheckOut(FacilityObject o) throws DaoException {
		String sql = "update fms_facility_item_checkout set checkin_date=#checkin_date#, " +
				     "checkin_by=#checkin_by#, updatedby=#updatedby#, " +
				     "updatedby_date=#updatedby_date#, status='C' where barcode=#barcode# and " +
				     "status='O'";
		super.update(sql, o);
	}
	
	public void updateCheckOutTakenBy(FacilityObject fo) throws DaoException {
		String sql = "UPDATE fms_facility_item_checkout SET takenBy = #takenBy#, " +
				"updatedby=#updatedby#, updatedby_date=#updatedby_date# " +
				"WHERE id = #id# ";
		super.update(sql, fo);
	}
	
	public Collection latestCheckOut(String id)throws DaoException {
		return super.select("SELECT FROM fms_facility_item_checkout WHERE barcode=? order by createdby_date desc", FacilityObject.class, new String[] {id},0,1);
	}
	
	public int undoInternalCheckOut(String id)throws DaoException {
		return super.update("DELETE FROM fms_facility_item_checkout WHERE barcode=? and status='O'", new String[] {id});
	}
	
	public boolean hasInternalCheckOut(String barcode) throws DaoException {
		String sql = "SELECT barcode FROM fms_facility_item_checkout WHERE barcode=? and status='O'";
		Collection col = super.select(sql, DefaultDataObject.class, new String[] {barcode}, 0, 1);
		if (col.size() != 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public Collection checkOutListByGroupId(String groupId, String search)throws DaoException {
		ArrayList params = new ArrayList();
		
		String sql="SELECT c.id, c.checkout_date, c.checkout_by, c.checkin_date, c.checkin_by, c.barcode, c.purpose, c.status, " +
				"c.createdby, c.createdby_date, c.updatedby, c.updatedby_date, c.location, c.groupId, c.takenBy, " +
				"(u.firstName + ' ' + u.lastName) as checkout_by, f.name as name" +
				", loc.name AS location_name " +
				" from fms_facility_item_checkout c INNER JOIN fms_facility_item i on c.barcode=i.barcode " +
				"LEFT JOIN fms_facility_location loc ON (i.location_id = loc.setup_id) " +
				"INNER JOIN fms_facility f on i.facility_id=f.id INNER JOIN security_user u on c.checkout_by=u.id " +
				"WHERE c.groupId=? ";
		params.add(groupId);
		if(search!=null && !"".equals(search)){
			sql += " AND (c.barcode LIKE ? OR f.name LIKE ? OR loc.name LIKE ? OR c.takenBy LIKE ? ) ";
			params.add("%"+search+"%");
			params.add("%"+search+"%");
			params.add("%"+search+"%");
			params.add("%"+search+"%");
		}
		
		sql += "order by f.name asc ";
		return super.select(sql, FacilityObject.class, params.toArray(),0,-1);
	}
	
	private String returnCheckoutQuery(int equal,String query){
		if (equal==1)
			query+="AND (SELECT COUNT(id) FROM fms_eng_assignment_equipment e WHERE e.groupId = asg.groupId and e.barcode is not null and e.checkedInBy is not null) like (SELECT COUNT(id) FROM fms_eng_assignment_equipment e WHERE e.groupId = asg.groupId AND e.barcode IS NOT NULL AND e.checkedOutBy IS NOT NULL) ";
		else if(equal==0)
			query+="AND (SELECT COUNT(id) FROM fms_eng_assignment_equipment e WHERE e.groupId = asg.groupId and e.barcode is not null and e.checkedInBy is not null) not like (SELECT COUNT(id) FROM fms_eng_assignment_equipment e WHERE e.groupId = asg.groupId AND e.barcode IS NOT NULL AND e.checkedOutBy IS NOT NULL) ";
		return query;
	}
	public Collection selectAssignmentCheckOutList(String search, Date fromDate, Date toDate, String sort,boolean desc,int start,int rows) throws DaoException{

		boolean isDefault = true;
		String sql = 
			"SELECT DISTINCT(asg.groupId) AS groupId, " +
			"(SELECT TOP 1 e.checkedOutDate FROM fms_eng_assignment_equipment e WHERE e.groupId = asg.groupId ORDER BY e.checkedOutDate DESC) AS convertedCheckedOutDate," +
			"r.requestId as requestId,title,r.createdBy,assignmentLocation " +
			"FROM fms_eng_assignment_equipment asg " +
			"INNER JOIN fms_eng_assignment a ON (asg.groupId = a.groupId) " +
			"INNER JOIN fms_eng_request r ON (a.requestId = r.requestId) " +
			"WHERE 1=1 " +
			"AND asg.barcode IS NOT NULL AND asg.checkedOutBy IS NOT NULL  ";
			
		ArrayList params=new ArrayList(); 
		
		if(search!=null && !"".equals(search)){
			sql+=" AND (r.requestId like '%"+search+"%' " +
					"OR r.title like '%"+search+"%' " +
					"OR r.createdBy like '%"+search+"%' " +
					"OR asg.groupId like '%"+search+"%' " +
					") ";
		}
		
		if(fromDate!=null && toDate!=null){
			//sql+=" AND ( checkedOutDate between ? AND ? ) ";
			sql+=" AND ( asg.requiredFrom between ? AND ? ) AND (asg.requiredTo between ? AND ?) ";
			params.add(fromDate);
			params.add(toDate);
			params.add(fromDate);
			params.add(toDate);
		} else if (fromDate!=null){
			sql+=" AND ( asg.requiredFrom <= ? ) ";
			params.add(fromDate);
		}else{
				//if date are empty fix it to 7days 
			 	Calendar startD = Calendar.getInstance();
			 	startD.setTime(new Date());                
			 	startD.add(Calendar.DAY_OF_WEEK, -7);
			 	startD.set(Calendar.HOUR_OF_DAY, 23);
			 	startD.set(Calendar.MINUTE, 59);
			 	startD.set(Calendar.SECOND, 59);
		        
		        Calendar endD = Calendar.getInstance();
		        endD.setTime(new Date());                
		        endD.set(Calendar.HOUR_OF_DAY, 23);
		        endD.set(Calendar.MINUTE, 59);
		        endD.set(Calendar.SECOND, 59);
		        
		        sql+=" AND ( asg.requiredFrom between ? AND ? ) AND (asg.requiredTo between ? AND ?) ";
		        params.add(startD.getTime());
				params.add(endD.getTime());
				params.add(startD.getTime());
				params.add(endD.getTime());
		}
		
		if(sort!=null && !"".equals(sort)){
			isDefault = false;
			if(sort.equals("workingHours")){
				sort="startTime";
			}else if(sort.equals("createdByFullName")){
				sort = "r.createdBy";
			}
			if(sort.equals("totalCheckedOut") || sort.equals("totalCheckedIn")){
				sql+="";
			}else{
				sql+=" order by "+sort+" ";
			}
			
		}
		
		if(desc){
			if(sort.equals("totalCheckedOut") || sort.equals("totalCheckedIn")){
				isDefault = false;
				sql+="";
			}else{
				isDefault = false;
				sql+=" DESC ";
			}
			
		}
		
		if (isDefault){
			sql = "select requestId from(("+returnCheckoutQuery(0,sql)+") " +
					" UNION ALL " +
					" ("+returnCheckoutQuery(1,sql)+")) as combination " +
				  "group by combination.requestId ";
		
				if(fromDate!=null && toDate!=null){
					// sql+=" AND ( checkedOutDate between ? AND ? ) ";
					params.add(fromDate);
					params.add(toDate);
					params.add(fromDate);
					params.add(toDate);
				} else if (fromDate!=null){
					// sql+=" AND ( checkedOutDate >= ? ) ";
					params.add(fromDate);
				}else{
					//if date are empty fix it to 7days 
				 	Calendar startD = Calendar.getInstance();
				 	startD.setTime(new Date());                
				 	startD.add(Calendar.DAY_OF_WEEK, -7);
				 	startD.set(Calendar.HOUR_OF_DAY, 23);
				 	startD.set(Calendar.MINUTE, 59);
				 	startD.set(Calendar.SECOND, 59);
			        
			        Calendar endD = Calendar.getInstance();
			        endD.setTime(new Date());                
			        endD.set(Calendar.HOUR_OF_DAY, 23);
			        endD.set(Calendar.MINUTE, 59);
			        endD.set(Calendar.SECOND, 59);
			        
			        //sql+=" AND ( asg.requiredFrom <= ? AND asg.requiredTo >= ? ) ";
			        params.add(startD.getTime());
					params.add(endD.getTime());
					params.add(startD.getTime());
					params.add(endD.getTime());
			}
		}
		
		ArrayList list = new ArrayList();
		int totalCheckedOut=0;
		int totalCheckedIn=0;
		try{
			Collection tempReqId = super.select(sql, HashMap.class, params.toArray(), start, rows);
			if(tempReqId!=null && tempReqId.size()>0){
				for (Iterator iter = tempReqId.iterator(); iter.hasNext();) {
					HashMap map=(HashMap)iter.next();
					Assignment assgn = selectAssignmentCheckOutListDetails(map.get("requestId").toString());
					totalCheckedOut=countAssignmentCheckOutDetailsAll(map.get("requestId").toString(),"","");
					totalCheckedIn=countAssignmentCheckOutDetailsAll(map.get("requestId").toString(),"","IN");
					assgn.setTotalCheckedOut(totalCheckedOut);
					assgn.setTotalCheckedIn(totalCheckedIn);
					//int total=((Number) map.get("total")).intValue();
					//return total;
					list.add(assgn);
				}
			}
			
			//to sort totalCheckedIn and totalCheckedOut
			if (sort != null && !"".equals(sort)) {
				if (sort.equals("totalCheckedIn")) {
					if (desc) {
						Collections.sort(list, new Comparator() {
							public int compare(Object o1, Object o2) {
								Assignment p1 = (Assignment) o1;
								Assignment p2 = (Assignment) o2;
								/*String p1v = String.valueOf(p1.getTotalCheckedIn());
								String p2v = String.valueOf(p2.getTotalCheckedIn());
								return p1v.compareToIgnoreCase(p2v);*/
								
								if (p1.getTotalCheckedIn() < p2.getTotalCheckedIn()) {
									return 1;
								} else if (p1.getTotalCheckedIn() > p2.getTotalCheckedIn()) {
									return -1;
								} else {
									return 0;
								}
							}
						});
					}else{
						Collections.sort(list, new Comparator() {
							public int compare(Object o1, Object o2) {
								Assignment p1 = (Assignment) o1;
								Assignment p2 = (Assignment) o2;
								if (p1.getTotalCheckedIn() > p2.getTotalCheckedIn()) {
									return 1;
								} else if (p1.getTotalCheckedIn() < p2.getTotalCheckedIn()) {
									return -1;
								} else {
									return 0;
								}
							}
						});
					}
				} else if (sort.equals("totalCheckedOut")) {
					if (desc) {
						Collections.sort(list, new Comparator() {
							public int compare(Object o1, Object o2) {
								Assignment p1 = (Assignment) o1;
								Assignment p2 = (Assignment) o2;
								if (p1.getTotalCheckedOut() < p2.getTotalCheckedOut()) {
									return 1;
								} else if (p1.getTotalCheckedOut() > p2.getTotalCheckedOut()) {
									return -1;
								} else {
									return 0;
								}
							}
						});
					} else {
						Collections.sort(list, new Comparator() {
							public int compare(Object o1, Object o2) {
								Assignment p1 = (Assignment) o1;
								Assignment p2 = (Assignment) o2;

								if (p1.getTotalCheckedOut() > p2.getTotalCheckedOut()) {
									return 1;
								} else if (p1.getTotalCheckedOut() < p2.getTotalCheckedOut()) {
									return -1;
								} else {
									return 0;
								}
							}
						});
					}
				}
			}

		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return list;
	}
	
	private Assignment selectAssignmentCheckOutListDetails(String requestId) throws DaoException{
		//boolean isDefault = true;
		String sql = 
			" select top 1 requestId, convertedCheckedOutDate, createdBy, title, assignmentLocation " +
			" from(select distinct(eq.groupId) , " +
			" CONVERT(VARCHAR, " +
			" (SELECT TOP 1 e.checkedOutDate " +
			"FROM fms_eng_assignment_equipment e " +
			"WHERE e.groupId = eq.groupId ORDER BY e.checkedOutDate DESC), 0) AS convertedCheckedOutDate" +
			", ea.requestId, " +
			"(SELECT TOP 1 e.assignmentLocation FROM fms_eng_assignment_equipment e " +
			"WHERE e.groupId = eq.groupId  ORDER BY e.assignmentLocation DESC) AS assignmentLocation, " +
			"r.createdBy as createdBy, r.title as title " +
			"FROM fms_eng_request r " +
			"INNER JOIN fms_eng_assignment ea ON (ea.requestId = r.requestId) " +
			"INNER JOIN fms_eng_assignment_equipment eq on (eq.groupId = ea.groupid) " +
			"where ea.requestId=? AND eq.barcode IS NOT NULL AND eq.checkedOutBy IS NOT NULL)as cdetails " +
			"group by cdetails.requestId, cdetails.createdBy, cdetails.convertedCheckedOutDate,cdetails.assignmentLocation,cdetails.title";
			
		Assignment detail = new Assignment();
		try{
			Collection tempReqId = super.select(sql, Assignment.class, new String[] {requestId}, 0, -1);
			if(tempReqId!=null){
				 detail=(Assignment)tempReqId.iterator().next();
				//int total=((Number) map.get("total")).intValue();
				//return total;
			}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return detail;
	}
	public int selectAssignmentCheckOutListCount(String search, Date fromDate, Date toDate) throws DaoException{
		//String equaliser = " like ";
		String sql = 
			"SELECT COUNT (DISTINCT r.requestId) AS total " +
			"FROM fms_eng_assignment_equipment asg " +
			"INNER JOIN fms_eng_assignment a ON (asg.groupId = a.groupId) " +
			"INNER JOIN fms_eng_request r ON (a.requestId = r.requestId) " +
			"WHERE 1=1 " +
			"AND asg.barcode IS NOT NULL AND asg.checkedOutBy IS NOT NULL ";
		
			
//			sql+="AND (SELECT COUNT(id) " +
//			"FROM fms_eng_assignment_equipment e " +
//			"WHERE e.groupId = asg.groupId and e.barcode is not null and e.checkedInBy is not null) " +
//			equaliser + " (SELECT COUNT(id) " +
//			"FROM fms_eng_assignment_equipment e " +
//			"WHERE e.groupId = asg.groupId AND e.barcode IS NOT NULL AND e.checkedOutBy IS NOT NULL)";
		
		ArrayList params=new ArrayList();
		
		if(search!=null && !"".equals(search)){
			sql+=" AND (r.requestId like '%"+search+"%' " +
			"OR r.title like '%"+search+"%' " +
			"OR r.createdBy like '%"+search+"%' " +
			"OR asg.groupId like '%"+search+"%' " +
			") ";
		}
		
		if(fromDate!=null && toDate!=null){
			sql+=" AND ( asg.requiredFrom between ? AND ? ) AND (asg.requiredTo between ? AND ?) ";
			params.add(fromDate);
			params.add(toDate);
			params.add(fromDate);
			params.add(toDate);
		}else if (fromDate!=null){
			sql+=" AND ( checkedOutDate >= ? ) ";
			params.add(fromDate);
		}else{
			//if date are empty fix it to 7days 
		 	Calendar startD = Calendar.getInstance();
		 	startD.setTime(new Date());                
		 	startD.add(Calendar.DAY_OF_WEEK, -7);
		 	startD.set(Calendar.HOUR_OF_DAY, 23);
		 	startD.set(Calendar.MINUTE, 59);
		 	startD.set(Calendar.SECOND, 59);
	        
	        Calendar endD = Calendar.getInstance();
	        endD.setTime(new Date());                
	        endD.set(Calendar.HOUR_OF_DAY, 23);
	        endD.set(Calendar.MINUTE, 59);
	        endD.set(Calendar.SECOND, 59);
	        
	        sql+=" AND ( asg.requiredFrom between ? AND ? ) AND (asg.requiredTo between ? AND ?) ";
	        params.add(startD.getTime());
			params.add(endD.getTime());
			params.add(startD.getTime());
			params.add(endD.getTime());
	}
	

		try{
			Collection col=super.select(sql, HashMap.class, params.toArray(), 0, -1);
			if(col!=null){
				HashMap map=(HashMap)col.iterator().next();
				int total=((Number) map.get("total")).intValue();
				return total;
			}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return 0; 
	}
	
	public Collection selectCheckOutList(String search, Date fromDate, Date toDate, String sort,boolean desc,int start,int rows) throws DaoException{
		String sql="Select groupId,checkout_date, u.firstName as checkout_by,location,purpose, count(groupId) as noOfCheckedOut," +
				" (Select count(*) from fms_facility_item_checkout c1 where c1.groupId=c.groupId AND checkin_date is NOT NULL ) as noOfCheckedIn " +
				" from fms_facility_item_checkout c INNER JOIN security_user u on u.id=c.checkout_by where 1=1 and c.groupId is not null ";
		ArrayList params=new ArrayList();
		if(search!=null && !"".equals(search)){
			sql+=" AND (u.firstName like '%"+search+"%' OR u.lastName like '%"+search+"%' OR location like '%"+search+"%' OR purpose like '%"+search+"%' ) ";
		}
		if(fromDate!=null && toDate!=null){
			sql+=" AND ( checkout_date between ? AND ? )";
			params.add(fromDate);
			params.add(toDate);
		}
		sql+=" GROUP BY groupId,checkout_date, u.firstName,location,purpose ";
		if(sort!=null && !"".equals(sort)){
			if(sort.equals("workingHours")){
				sort="startTime";
			}
			sql+=" order by "+sort+" ";
		} else {
			sql += " order by noOfCheckedIn, checkout_date DESC ";
		}
		
		if(desc){
			sql+=" DESC ";
		}
		
		return super.select(sql, FacilityObject.class, params.toArray(), start, rows);
		
	}
	
	public int selectCheckOutListCount(String search, Date fromDate, Date toDate) throws DaoException{
		String sql="Select count( distinct groupId) as Count " +
				"FROM fms_facility_item_checkout c  " +
				"INNER JOIN security_user u on u.id=c.checkout_by " +
				"WHERE 1=1 ";
		ArrayList params=new ArrayList();
		if(search!=null && !"".equals(search)){
			sql+=" AND (u.firstName like '%"+search+"%' OR u.lastName like '%"+search+"%' OR location like '%"+search+"%' OR purpose like '%"+search+"%' ) ";
		}
		if(fromDate!=null && toDate!=null){
			sql+=" AND ( checkout_date between ? AND ? )";
			params.add(fromDate);
			params.add(toDate);
		}
		
		int count=0;
		try{
			Collection col=super.select(sql, HashMap.class, params.toArray(), 0, -1);
			if(col!=null){
				HashMap map=(HashMap)col.iterator().next();
				count=(Integer)map.get("Count");
			}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return count; 
	}
	
	public Collection selectTodaysAssignments(String search, String department, Date fromDate, Date toDate, String sort, boolean desc, boolean isToday, int start,int rows) throws DaoException{
		ArrayList params=new ArrayList();
//		String sql="Select count(fa.rateCardCategoryId) as qty,a.assignmentId,a.code, r.requestId, r.title,rc.name as rateCard,u.firstName,d.name as department," +
//				" fa.requiredFrom, fa.requiredTo,fa.fromTime,fa.toTime,fa.rateCardId, fa.rateCardCategoryId from fms_eng_assignment a INNER JOIN " +
//				" fms_eng_request r on r.requestId=a.requestId INNER JOIN security_user u on r.createdBy=u.username " +
//				" INNER JOIN fms_department d on u.department=d.id INNER JOIN fms_eng_assignment_equipment fa on a.assignmentId=fa.assignmentId " +
//				" INNER JOIN fms_rate_card rc  ON rc.id=fa.rateCardId where 1=1 AND a.assignmentType='F' ";
		String sql = "SELECT a.groupId, r.title, r.requestId, a.serviceType, (u.firstName + ' ' + u.lastName) AS firstName, d.name as department, fa.requiredFrom, fa.requiredTo,fa.fromTime,fa.toTime " +
				"FROM fms_eng_request r INNER JOIN fms_eng_assignment a ON (r.requestId = a.requestId) " +
				"INNER JOIN security_user u ON (r.createdBy=u.username) " +
				"INNER JOIN fms_department d on u.department=d.id " +
				"INNER JOIN fms_eng_assignment_equipment fa on a.assignmentId=fa.assignmentId " +
				"WHERE 1=1 AND a.assignmentType = '" + EngineeringModule.ASSIGNMENT_TYPE_FACILITY+ "' " +
				"AND (a.serviceType = '" + ServiceDetailsForm.SERVICE_SCPMCP + "' " +
						"OR " +
					" a.serviceType = '" + ServiceDetailsForm.SERVICE_OTHER+ "') ";
		
		if (isToday){
			sql += " AND ? BETWEEN fa.requiredFrom AND fa.requiredTo  ";
			params.add(DateUtil.getToday());
		}
		
		if(fromDate!=null && toDate!=null){
			sql+=" AND (( fa.requiredFrom between ? AND ? ) OR ( fa.requiredTo between ? AND ? )) ";
			params.add(fromDate);
			params.add(toDate);
			params.add(fromDate);
			params.add(toDate);
		}
		
		if(search!=null && !"".equals(search)){
			sql+=" AND (u.firstName like '%"+search+"%' OR u.lastName like '%"+search+"%' OR r.title like '%"+search+"%' ) ";
		}
		if(department!=null && !"-1".equals(department)){
			sql+=" AND ( d.id = '"+department+"') ";
		}
		//sql+=" GROUP BY a.assignmentId, a.code, r.requestId, r.title, rc.name ,u.firstName, d.name, fa.requiredFrom, fa.requiredTo, fa.fromTime,fa.toTime,fa.rateCardId,fa.rateCardCategoryId ";
		sql += "GROUP BY a.groupId, r.title, r.requestId, a.serviceType, u.firstName, u.lastName, d.name, fa.requiredFrom, fa.requiredTo,fa.fromTime,fa.toTime";
		if(sort!=null && !"".equals(sort)){
			
			sql += " order by "+sort+" ";
		} else { 
			sql += " ORDER BY fa.requiredFrom ASC, r.title ASC ";
		}
		
		if(desc){
			sql+=" DESC ";
		}
		
		return super.select(sql, HashMap.class, params.toArray(), start, rows);
	}
	
	public int selectTodaysAssignmentCount(String search, String department) throws DaoException{
		ArrayList params=new ArrayList();
		String sql="Select COUNT(a.assignmentId) as COUNT " +
				" from fms_eng_assignment a INNER JOIN " +
				" fms_eng_request r on r.requestId=a.requestId INNER JOIN security_user u on r.createdBy=u.username " +
				" INNER JOIN fms_department d on u.department=d.id INNER JOIN fms_eng_assignment_equipment fa on a.assignmentId=fa.assignmentId " +
				" INNER JOIN fms_rate_card rc  ON rc.id=fa.rateCardId where 1=1 AND a.assignmentType='F' ";
		sql += " AND ? BETWEEN fa.requiredFrom AND fa.requiredTo  ";
		params.add(DateUtil.getToday());
		
		if(search!=null && !"".equals(search)){
			sql+=" AND (u.firstName like '%"+search+"%' OR u.lastName like '%"+search+"%' OR r.title like '%"+search+"%' ) ";
		}
		if(department!=null && !"-1".equals(department)){
			sql+=" AND ( d.id = '"+department+"') ";
		}
		sql+=" GROUP BY a.assignmentId, a.code, r.title, rc.name ,u.firstName, d.name, fa.requiredFrom, fa.requiredTo,fa.fromTime,fa.toTime,fa.rateCardId,fa.rateCardCategoryId ";
		
		int count=0;
		try{
			Collection col=super.select(sql, HashMap.class, params.toArray(), 0, -1);
			if(col!=null){
				//HashMap map=(HashMap)col.iterator().next();
				count=col.size();//(Integer)map.get("COUNT");
			}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return count; 
	}
	
	public int selectAssignmentCount(String search, String department, Date fromDate, Date toDate) throws DaoException{
		ArrayList params=new ArrayList();
		//String sql="Select COUNT(a.assignmentId) as COUNT " +
		//		" from fms_eng_assignment a INNER JOIN " +
		//		" fms_eng_request r on r.requestId=a.requestId INNER JOIN security_user u on r.createdBy=u.username " +
		//		" INNER JOIN fms_department d on u.department=d.id INNER JOIN fms_eng_assignment_equipment fa on a.assignmentId=fa.assignmentId " +
		//		" INNER JOIN fms_rate_card rc  ON rc.id=fa.rateCardId where 1=1 AND a.assignmentType='F' ";
		String sql = "SELECT a.groupId, r.title, r.requestId, u.firstName, d.name, fa.requiredFrom, fa.requiredTo,fa.fromTime,fa.toTime " +
			"FROM fms_eng_request r INNER JOIN fms_eng_assignment a ON (r.requestId = a.requestId) " +
			"INNER JOIN security_user u ON (r.createdBy=u.username) " +
			"INNER JOIN fms_department d on u.department=d.id " +
			"INNER JOIN fms_eng_assignment_equipment fa on a.assignmentId=fa.assignmentId " +
			"WHERE 1=1 AND a.assignmentType = '" + EngineeringModule.ASSIGNMENT_TYPE_FACILITY+ "' " +
			"AND (a.serviceType = '" + ServiceDetailsForm.SERVICE_SCPMCP + "' " +
				"OR " +
			" a.serviceType = '" + ServiceDetailsForm.SERVICE_OTHER+ "') ";

		//if (isToday){
		//	sql += " AND ? BETWEEN fa.requiredFrom AND fa.requiredTo  ";
		//	params.add(DateUtil.getToday());
		//}

		if(fromDate!=null && toDate!=null){
			sql+=" AND (( fa.requiredFrom between ? AND ? ) OR ( fa.requiredTo between ? AND ? )) ";
			params.add(fromDate);
			params.add(toDate);
			params.add(fromDate);
			params.add(toDate);
		}
		
		if(search!=null && !"".equals(search)){
			sql+=" AND (u.firstName like '%"+search+"%' OR u.lastName like '%"+search+"%' OR r.title like '%"+search+"%' ) ";
		}
		if(department!=null && !"-1".equals(department)){
			sql+=" AND ( d.id = '"+department+"') ";
		}
		//sql+=" GROUP BY a.assignmentId, a.code, r.title, rc.name ,u.firstName, d.name, fa.requiredFrom, fa.requiredTo,fa.fromTime,fa.toTime,fa.rateCardId,fa.rateCardCategoryId ";		
		sql += "GROUP BY a.groupId, r.title, r.requestId, u.firstName, d.name, fa.requiredFrom, fa.requiredTo,fa.fromTime,fa.toTime";
		
		int count=0;
		try{
			Collection col=super.select(sql, HashMap.class, params.toArray(), 0, -1);
			if(col!=null){
				//HashMap map=(HashMap)col.iterator().next();
				count=col.size();//(Integer)map.get("COUNT");
			}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return count; 
	}
	
	//writeoff
	public void insertWriteoff(ClosedItemObject w) throws DaoException {
		String sql = "INSERT INTO fms_facility_writeoff (id, item_code, file_name, file_path, " +
				     "file_type, file_size, date, reason, createdby, createdby_date) VALUES (#id#, " +
				     "#item_code#, #file_name#, #file_path#, #file_type#, #file_size#, #date#, " +
				     "#reason#, #createdby#, #createdby_date#)";
		super.update(sql, w);
	}
	
	public Collection selectWriteoff(String itemCode) throws DaoException {
		return super.select("SELECT w.*, s.username as createdby_name from fms_facility_writeoff w, " +
				            "security_user s WHERE item_code=? AND s.id=w.createdby", ClosedItemObject.class, new String[] {itemCode}, 0, -1);
	}
	
	//missing
	public void insertMissing(ClosedItemObject w) throws DaoException {
		String sql = "INSERT INTO fms_facility_missing (id, item_code, file_name, file_path, " +
				     "file_type, file_size, date, reason, createdby, createdby_date) VALUES (#id#, " +
				     "#item_code#, #file_name#, #file_path#, #file_type#, #file_size#, #date#, " +
				     "#reason#, #createdby#, #createdby_date#)";
		super.update(sql, w);
	}
	
	public Collection selectMissing(String itemCode) throws DaoException {
		return super.select("SELECT w.*, s.username as createdby_name from fms_facility_missing w, " +
				            "security_user s WHERE item_code=? AND s.id=w.createdby", ClosedItemObject.class, new String[] {itemCode}, 0, -1);
	}
	
	//item
	public void insertItem(FacilityObject o) throws DaoException {
		String sql = "INSERT INTO fms_facility_item(barcode, facility_id, purchased_date, purchased_cost, " +
				     "do_num, easset_num, location_id, replacement, status, createdby, createdby_date) VALUES (#barcode#, #facility_id#, " +
				     "#purchased_date#, #purchased_cost#, #do_num#, #easset_num#, #location_id#, #replacement#, " +
				     "#status#, #createdby#, #createdby_date#)";
		super.update(sql, o);
	}
	
	public void updateItem(FacilityObject c) throws DaoException {
		String sql = "update fms_facility_item set barcode=#barcode#, facility_id=#facility_id#, " +
				     "purchased_date=#purchased_date#, purchased_cost=#purchased_cost#, do_num=#do_num#, location_id=#location_id#, replacement=#replacement#, " +
				     "easset_num=#easset_num#, status=#status#, updatedby=#updatedby#, " +
				     "updatedby_date=#updatedby_date# where barcode=#barcode#";
		super.update(sql, c);
	}
	
	public Collection selectItem(String barcode) throws DaoException {
		return super.select("SELECT i.*, f.name, l.name as location_name from fms_facility f , fms_facility_item i left join fms_facility_location l on i.location_id=l.setup_id where " +
				            "i.facility_id=f.id AND i.barcode=?", FacilityObject.class, new String[] {barcode}, 0, -1);
	}
	
	public Collection selectItem(String search, String facility_id, String location_id, String status, String sort, boolean desc, int start, int rows) throws DaoException {
		Collection args = new ArrayList();
		String sql = "SELECT i.*, l.name as location_name, f.name from fms_facility f, fms_facility_item i " +
				     "left join fms_facility_location l on i.location_id=l.setup_id where 1=1 and f.id=i.facility_id";

		if(!"".equals(search) && !"null".equals(search) && search != null){
			sql = sql + " AND (barcode LIKE ? or l.name like ?)";
			args.add("%"+search+"%");
			args.add("%"+search+"%");
		}
		if(!"".equals(facility_id) && !"null".equals(facility_id) && facility_id != null && !"-1".endsWith(facility_id)){
			sql = sql + " AND facility_id=?";
			args.add(facility_id);
		}
		if(!"".equals(location_id) && !"null".equals(location_id) && location_id != null && !"-1".endsWith(location_id)){
			sql = sql + " AND location_id=?";
			args.add(location_id);
		}
		if(!("-1".equals(status) || "".equals(status))){
			sql = sql+" AND i.status = ?";
		    args.add(status);
		}
		if (!"".equals(sort) && !"null".equals(sort) && sort != null){
		    sql = sql+" ORDER BY i." + sort;
		}else{
			sql = sql+" ORDER BY barcode";
		}
		if (desc){
			sql = sql+" DESC";
		}
		return super.select(sql, FacilityObject.class, args.toArray(), start, rows);
	}
	
	public Collection selectFacilityAvailability(String ids[])throws DaoException {
		DaoQuery query = new DaoQuery();
        query.addProperty(new OperatorIn("fms_facility.id", ids, DaoOperator.OPERATOR_AND));
		String sql = "select fms_facility.name, ISNULL(count(barcode),0) as quantity from fms_facility " +
				     "left join fms_facility_item on fms_facility.id = fms_facility_item.facility_id and fms_facility_item.status='1'" +
				     " where fms_facility.status='1'" + 
				     query.getStatement() + " group by fms_facility.name";
		return super.select(sql, FacilityObject.class, query.getArray(), 0, -1);
	}
	
	public int selectItemCount(String search, String facility_id, String location_id, String status) throws DaoException {
		Collection args = new ArrayList();
		String sql = "SELECT count(*) as total from fms_facility_item i, fms_facility_location l where 1=1 and i.location_id=l.setup_id";

		if(!"".equals(search) && !"null".equals(search) && search != null){
			sql = sql + " AND (barcode LIKE ? or l.name like ?)";
			args.add("%"+search+"%");
			args.add("%"+search+"%");
		}
		if(!"".equals(facility_id) && !"null".equals(facility_id) && facility_id != null && !"-1".endsWith(facility_id)){
			sql = sql + " AND facility_id=?";
			args.add(facility_id);
		}
		if(!"".equals(location_id) && !"null".equals(location_id) && location_id != null && !"-1".endsWith(location_id)){
			sql = sql + " AND location_id=?";
			args.add(location_id);
		}
		if(!("-1".equals(status) || "".equals(status))){
			sql = sql+" AND i.status = ?";
		    args.add(status);
		}
		Map row = (Map)super.select(sql, HashMap.class, args.toArray(), 0, -1).iterator().next();
	    return Integer.parseInt(row.get("total").toString());
	}
	
	//facility
	public void insertRelatedItem(FacilityObject o) throws DaoException {
		String sql = "INSERT INTO fms_facility_related_item(id, facility_id, related_id) VALUES (#id#, " +
				     "#facility_id#, #related_id#)";
		super.update(sql, o);
	}
	
	public void deleteRelatedItem(String id) throws DaoException {
		super.update("DELETE FROM fms_facility_related_item WHERE facility_id=?", new String[] {id});
	}
	
	public Collection selectRelatedItem(String id) throws DaoException {
		return super.select("select r.related_id as id, f.name as name from fms_facility_related_item" +
				            " r, fms_facility f where r.related_id=f.id and r.facility_id=?", FacilityObject.class, new String[]{id}, 0, -1);
	}
	
	public void insertFacility(FacilityObject o) throws DaoException {
		String sql = "INSERT INTO fms_facility(id, name, description, category_id, channel_id, maketype, " + 
					 "model_name, quantity, is_pm, pm_type, pm_month, pm_year, is_pool, have_child, status, " +
					 "createdby, createdby_date) VALUES (#id#, #name#, #description#, #category_id#, " + 
					 "#channel_id#, #maketype#, #model_name#, #quantity#, #is_pm#, #pm_type#, #pm_month#, " +
					 "#pm_year#, #is_pool#, #have_child#, #status#, #createdby#, #createdby_date#)";
		super.update(sql, o);
	}
	
	public void updateFacility(FacilityObject c) throws DaoException {
		String sql = "update fms_facility set name=#name#, description=#description#, category_id=#category_id#, channel_id=#channel_id#," +
		             " maketype=#maketype#, model_name=#model_name#, quantity=#quantity#, is_pm=#is_pm#, " +
		             "pm_type=#pm_type#, pm_month=#pm_month#, pm_year=#pm_year#, is_pool=#is_pool#, have_child=#have_child#, " +
		             "status=#status#, updatedby=#updatedby#, updatedby_date=#updatedby_date# where id=#id#";
		super.update(sql, c);
	}
	
	public void deleteFacility(String id) throws DaoException {
		super.update("DELETE FROM fms_facility WHERE id=?", new String[] {id});
	}
	
	public Collection selectFacility(String id) throws DaoException {
		return super.select("SELECT f.*, c.name as category_name, ch.name as channel_name from fms_facility " +
				            "f, fms_facility_category c, fms_tran_channel ch WHERE f.category_id=c.id and " +
				            "f.channel_id=ch.setup_id and f.id=?", FacilityObject.class, new String[] {id}, 0, -1);
	}
	
	public Collection selectFacility(String search, String category_id, String channel_id, String status, String sort, boolean desc, int start, int rows) throws DaoException {
		Collection args = new ArrayList();
		String sql = "SELECT f.*, c.name as category_name, ch.name as channel_name ";
		sql += ", (select count(barcode) from fms_facility_item where facility_id = f.id AND status ='1') as quantityAvailable ";
		sql += ", (select count(barcode) from fms_facility_item where facility_id = f.id AND status ='C') as quantityCheckedOut ";
		
		sql += "from fms_facility f, " +
				     "fms_facility_category c, fms_tran_channel ch ";
		sql += "WHERE f.category_id=c.id and " +
				     "f.channel_id=ch.setup_id";

		if(!"".equals(search) && !"null".equals(search) && search != null){
			sql = sql + " AND (f.name LIKE ? or f.description like ?)";
			args.add("%"+search+"%");
			args.add("%"+search+"%");
		}
		if(!"".equals(category_id) && !"null".equals(category_id) && category_id != null && !"-1".endsWith(category_id)){
			sql = sql + " AND f.category_id=?";
			args.add(category_id);
		}
		if(!"".equals(channel_id) && !"null".equals(channel_id) && channel_id != null && !"-1".endsWith(channel_id)){
			sql = sql + " AND f.channel_id=?";
			args.add(channel_id);
		}
		if(!("-1".equals(status) || "".equals(status))){
			sql = sql + " AND f.status=?";
			args.add(status);
		}
		if (!"".equals(sort) && !"null".equals(sort) && sort != null){
		    if ("quantityAvailable".equals(sort)) {
		    	sql += " ORDER BY quantityAvailable ";
		    } else if ("quantityCheckedOut".equals(sort)) {
		    	sql += " ORDER BY quantityCheckedOut ";
		    } else {
		    	sql = sql+" ORDER BY f." + sort;
		    }
		}else{
			sql = sql+" ORDER BY f.name";
		}
		if (desc){
			sql = sql+" DESC";
		}
		return super.select(sql, FacilityObject.class, args.toArray(), start, rows);
	}
	
	public Collection selectFacilityForCheckAvailability(String search, String category_id, String channel_id, String status, String sort, boolean desc, int start, int rows) throws DaoException {
		Collection args = new ArrayList();
		String sql = "SELECT f.*, c.name as category_name, ch.name as channel_name ";
		sql += ", (select count(barcode) from fms_facility_item where facility_id = f.id AND status ='1') as quantityAvailable ";
		sql += ", (select count(barcode) from fms_facility_item where facility_id = f.id AND status ='C') as quantityCheckedOut ";
		
		sql += "from fms_facility f, " +
				     "fms_facility_category c, fms_tran_channel ch ";
		sql += "WHERE f.category_id=c.id and " +
				     "f.channel_id=ch.setup_id and " +
				     "f.status = '1' ";

		if(!"".equals(search) && !"null".equals(search) && search != null){
			sql = sql + " AND (f.name LIKE ? or f.description like ?)";
			args.add("%"+search+"%");
			args.add("%"+search+"%");
		}
		if(!"".equals(category_id) && !"null".equals(category_id) && category_id != null && !"-1".endsWith(category_id)){
			sql = sql + " AND f.category_id=?";
			args.add(category_id);
		}
		if(!"".equals(channel_id) && !"null".equals(channel_id) && channel_id != null && !"-1".endsWith(channel_id)){
			sql = sql + " AND f.channel_id=?";
			args.add(channel_id);
		}
		if(!("-1".equals(status) || "".equals(status))){
			sql = sql + " AND f.status=?";
			args.add(status);
		}
		if (!"".equals(sort) && !"null".equals(sort) && sort != null){
		    if ("quantityAvailable".equals(sort)) {
		    	sql += " ORDER BY quantityAvailable ";
		    } else if ("quantityCheckedOut".equals(sort)) {
		    	sql += " ORDER BY quantityCheckedOut ";
		    } else {
		    	sql = sql+" ORDER BY f." + sort;
		    }
		}else{
			sql = sql+" ORDER BY f.name";
		}
		if (desc){
			sql = sql+" DESC";
		}
		return super.select(sql, FacilityObject.class, args.toArray(), start, rows);
	}
	
	public int selectFacilityCount(String search, String category_id, String channel_id, String status) throws DaoException {
		Collection args = new ArrayList();
		String sql = "SELECT count(*) as total from fms_facility f, " +
				     "fms_facility_category c, fms_tran_channel ch WHERE f.category_id=c.id and " +
				     "f.channel_id=ch.setup_id";

		if(!"".equals(search) && !"null".equals(search) && search != null){
			sql = sql + " AND (f.name LIKE ? or f.description like ?)";
			args.add("%"+search+"%");
			args.add("%"+search+"%");
		}
		if(!"".equals(category_id) && !"null".equals(category_id) && category_id != null && !"-1".endsWith(category_id)){
			sql = sql + " AND f.category_id=?";
			args.add(category_id);
		}
		if(!"".equals(channel_id) && !"null".equals(channel_id) && channel_id != null && !"-1".endsWith(channel_id)){
			sql = sql + " AND f.channel_id=?";
			args.add(channel_id);
		}
		if(!("-1".equals(status) || "".equals(status))){
			sql = sql + " AND f.status=?";
			args.add(status);
		}
		Map row = (Map)super.select(sql, HashMap.class, args.toArray(), 0, -1).iterator().next();
	    return Integer.parseInt(row.get("total").toString());
	}
	
	//category
	public void insertCategory(CategoryObject c) throws DaoException {
		String sql = "INSERT INTO fms_facility_category (id, name, description, department_id, unit_id, parent_cat, parent_cat_id, status, " +
	     "createdby, createdby_date) VALUES (#id#, #name#, #description#, #department_id#, #unit_id#, #parent_cat#, #parent_cat_id#, #status#," +
	     " #createdby#, #createdby_date#)";
		super.update(sql, c);
	}
	
	public void updateCategory(CategoryObject c) throws DaoException {
		String sql = "update fms_facility_category set name=#name#, description=#description#, department_id=#department_id#, unit_id=#unit_id#," +
		             " parent_cat=#parent_cat#, parent_cat_id=#parent_cat_id#, status=#status#, updatedby=#updatedby#, updatedby_date=#updatedby_date# where id=#id#";
		super.update(sql, c);
	}
	
	public void deleteCategory(String id) throws DaoException {
		super.update("DELETE FROM fms_facility_category WHERE id=?", new String[] {id});
	}
	
	public Collection selectCategory(String id) throws DaoException {
		return super.select("SELECT c.*, d.name as department_name, u.name as unit_name from fms_facility_category c, fms_department d, fms_unit u WHERE c.department_id=d.id and c.unit_id=u.id and c.id=?", CategoryObject.class, new String[] {id}, 0, -1);
	}
	
	public Collection selectCategoryWithParent(String search, String department_id, String unit_id, String parent_id, boolean isParent, String status, String sort, boolean desc, int start, int rows) throws DaoException {
		Collection args = new ArrayList();
		String sql = "SELECT c.*, d.name as department_name, u.name as unit_name, c2.name as parent_cat_name from " +
					 "fms_department d, fms_unit u, fms_facility_category c left join fms_facility_category c2 on c.parent_cat_id=c2.id " +
				     "WHERE c.department_id=d.id and c.unit_id=u.id";

		if(!"".equals(search) && !"null".equals(search) && search != null){
			sql = sql + " AND (c.name LIKE ? or c.description like ?)";
			args.add("%"+search+"%");
			args.add("%"+search+"%");
		}
		if(!"".equals(department_id) && !"null".equals(department_id) && department_id != null && !"-1".endsWith(department_id)){
			sql = sql + " AND c.department_id=?";
			args.add(department_id);
		}
		if(!"".equals(unit_id) && !"null".equals(unit_id) && unit_id != null && !"-1".endsWith(unit_id)){
			sql = sql + " AND c.unit_id=?";
			args.add(unit_id);
		}
		if (isParent){
			sql = sql+" AND c.parent_cat='Y'";
		}else{
			sql = sql+" AND c.parent_cat='N'";
		}
		if(!"".equals(parent_id) && !"null".equals(parent_id) && parent_id != null && !"-1".endsWith(parent_id)){
			sql = sql + " AND c.parent_cat_id=?";
			args.add(parent_id);
		}
		if(!"".equals(status) && !"null".equals(status) && status != null && !"-1".endsWith(status)){
			sql = sql + " AND c.status=?";
			args.add(status);
		}
		if (!"".equals(sort) && !"null".equals(sort) && sort != null){
		    sql = sql+" ORDER BY c." + sort;
		}else{
			sql = sql+" ORDER BY c.name";
		}
		if (desc){
			sql = sql+" DESC";
		}
		return super.select(sql, CategoryObject.class, args.toArray(), start, rows);
	}
	
	public Collection selectCategory(String search, String department_id, String unit_id, String parent_id, boolean isParent, String status, String sort, boolean desc, int start, int rows) throws DaoException {
		Collection args = new ArrayList();
		String sql = "SELECT c.*, d.name as department_name, u.name as unit_name from fms_facility_category c, fms_department d, fms_unit u WHERE c.department_id=d.id and c.unit_id=u.id";

		if(!"".equals(search) && !"null".equals(search) && search != null){
			sql = sql + " AND (c.name LIKE ? or c.description like ?)";
			args.add("%"+search+"%");
			args.add("%"+search+"%");
		}
		if(!"".equals(department_id) && !"null".equals(department_id) && department_id != null && !"-1".endsWith(department_id)){
			sql = sql + " AND c.department_id=?";
			args.add(department_id);
		}
		if(!"".equals(unit_id) && !"null".equals(unit_id) && unit_id != null && !"-1".endsWith(unit_id)){
			sql = sql + " AND c.unit_id=?";
			args.add(unit_id);
		}
		if (isParent){
			sql = sql+" AND c.parent_cat='Y'";
		}else{
			sql = sql+" AND c.parent_cat='N'";
		}
		if(!"".equals(parent_id) && !"null".equals(parent_id) && parent_id != null && !"-1".endsWith(parent_id)){
			sql = sql + " AND c.parent_cat_id=?";
			args.add(parent_id);
		}
		if( status != null && !"".equals(status) /*&& !"null".equals(status)*/ && !"-1".equals(status)){
			sql = sql + " AND c.status=?";
			args.add(status);
		}
		if (!"".equals(sort) && !"null".equals(sort) && sort != null){
		    sql = sql+" ORDER BY c." + sort;
		}else{
			sql = sql+" ORDER BY c.name";
		}
		if (desc){
			sql = sql+" DESC";
		}
		return super.select(sql, CategoryObject.class, args.toArray(), start, rows);
	}
	
	public int selectCategoryCount(String search, String department_id, String unit_id, String parent_id, boolean isParent, String status) throws DaoException {
		Collection args = new ArrayList();
		String sql = "SELECT count(*) as total from fms_facility_category c, fms_department d, fms_unit u WHERE c.department_id=d.id and c.unit_id=u.id";

		if(!"".equals(search) && !"null".equals(search) && search != null){
			sql = sql + " AND (c.name LIKE ? or c.description like ?)";
			args.add("%"+search+"%");
			args.add("%"+search+"%");
		}
		if(!"".equals(department_id) && !"null".equals(department_id) && department_id != null && !"-1".endsWith(department_id)){
			sql = sql + " AND c.department_id=?";
			args.add(department_id);
		}
		if(!"".equals(unit_id) && !"null".equals(unit_id) && unit_id != null && !"-1".endsWith(unit_id)){
			sql = sql + " AND c.unit_id=?";
			args.add(unit_id);
		}
		if (isParent){
			sql = sql+" AND c.parent_cat='Y'";
		}else{
			sql = sql+" AND c.parent_cat='N'";
		}
		if(!"".equals(parent_id) && !"null".equals(parent_id) && parent_id != null && !"-1".endsWith(parent_id)){
			sql = sql + " AND c.parent_cat_id=?";
			args.add(parent_id);
		}
		//if(!"".equals(status) && !"null".equals(status) && status != null && !"-1".endsWith(status)){
		if( status != null && !"".equals(status) /*&& !"null".equals(status)*/ && !"-1".equals(status)){
			sql = sql + " AND c.status=?";
			args.add(status);
		}
		Map row = (Map)super.select(sql, HashMap.class, args.toArray(), 0, -1).iterator().next();
	    return Integer.parseInt(row.get("total").toString());
	}
	
	public int selectCategoryCount(String search) throws DaoException {
		Collection args = new ArrayList();
		String sql = "SELECT count(*) as total from fms_facility_category WHERE 1=1";

		if(!"".equals(search) && !"null".equals(search) && search != null){
			sql = sql + " AND name like ?";
			args.add("%"+search+"%");
		}
		
		Map row = (Map)super.select(sql, HashMap.class, args.toArray(), 0, -1).iterator().next();
	    return Integer.parseInt(row.get("total").toString());
	}
	
	//data migration
	public FacilityObject selectFacility(String itemName,String barcode)throws DaoException {
		
		FacilityObject fo = new FacilityObject();
		fo = null;
		Collection colFO = new ArrayList();
		
		if(!"".equals(itemName)){
			if(barcode == null || "".equals(barcode)){
				 colFO = super.select("SELECT id, name, description, category_id, channel_id, maketype, " + 
						 "model_name, quantity, is_pm, pm_type, pm_month, pm_year, is_pool, have_child, status, " +
						 "createdby, createdby_date from fms_facility WHERE name=?", FacilityObject.class, new Object[] {itemName}, 0, 1);			
			}else{			
				colFO = super.select("SELECT i.*, f.name from fms_facility_item i, fms_facility f where " +
			            "f.id = ? AND i.facility_id=f.id AND i.barcode=?", FacilityObject.class, new String[] {itemName,barcode}, 0, -1);									
			}
		}
		
		if(colFO.size() > 0){
			fo = (FacilityObject) colFO.iterator().next();			
		}
				
		return fo;
	}
	
	public Collection getCategory(String id) throws DaoException {
		return super.select("SELECT id, name, description, department_id, unit_id, parent_cat, parent_cat_id, status,createdby, createdby_date FROM fms_facility_category WHERE id=?", CategoryObject.class, new String[] {id}, 0, -1);
	}
	
	public Collection getBarcodeSerialNo(String barcode) throws DaoException {
		return super.select("SELECT barcode, facility_id, purchased_date, purchased_cost, " +
				"do_num, easset_num, location_id, replacement, status, createdby, createdby_date from fms_facility_item where " +
				"barcode = ? ", FacilityObject.class, new String[] {barcode}, 0, -1);
				//"barcode = ? OR facility_id = ?", FacilityObject.class, new String[] {barcode,serialno}, 0, -1);
	}

	public String getFullName(String id)  {
		String sql = "SELECT firstName + ' ' + lastName AS fullName FROM security_user " +
				"WHERE id = ? ";
		
		String name="";
		
		try{
		Collection col=super.select(sql, EngineeringRequest.class, new String[]{ id }, 0, 1);
		if(col!=null && col.size()>0){
			EngineeringRequest er = (EngineeringRequest)col.iterator().next();
			name= er.getFullName();
		}
		}catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return name;
	}
	
	public Collection getRateCardByGroupId(String groupId) throws DaoException {
		String sql = "SELECT distinct(rateCardId) AS rateCardId, rateCardCategoryId " +
				"FROM fms_eng_assignment_equipment " +
				"WHERE groupId = ?";
		
		return super.select(sql, HashMap.class, new String[] {groupId}, 0, -1);
	}
	
	public Collection selectTodaysAssignmentsHOU(String search, String department, Date fromDate, Date toDate, String sort, boolean desc, boolean isToday, int start,int rows) throws DaoException{
		ArrayList params=new ArrayList();
//		String sql="Select count(fa.rateCardCategoryId) as qty,a.assignmentId,a.code, r.requestId, r.title,rc.name as rateCard,u.firstName,d.name as department," +
//				" fa.requiredFrom, fa.requiredTo,fa.fromTime,fa.toTime,fa.rateCardId, fa.rateCardCategoryId from fms_eng_assignment a INNER JOIN " +
//				" fms_eng_request r on r.requestId=a.requestId INNER JOIN security_user u on r.createdBy=u.username " +
//				" INNER JOIN fms_department d on u.department=d.id INNER JOIN fms_eng_assignment_equipment fa on a.assignmentId=fa.assignmentId " +
//				" INNER JOIN fms_rate_card rc  ON rc.id=fa.rateCardId where 1=1 AND a.assignmentType='F' ";
		String sql = 
				"SELECT a.groupId, a.serviceType, r.title, r.requestId, " +
				"(u.firstName + ' ' + u.lastName) AS firstName, d.name as department, fa.requiredFrom, " +
				"fa.requiredTo,fa.fromTime,fa.toTime,un.unitId " +
				"FROM fms_eng_request r " +
				"INNER JOIN fms_eng_assignment a ON (r.requestId = a.requestId) " +
				"INNER JOIN fms_eng_request_unit un ON (r.requestId = un.requestId) " +
				"INNER JOIN security_user u ON (r.createdBy=u.username) " +
				"INNER JOIN fms_department d on u.department=d.id " +
				"INNER JOIN fms_eng_assignment_equipment fa on a.assignmentId=fa.assignmentId " +
				"WHERE a.assignmentType = '" + EngineeringModule.ASSIGNMENT_TYPE_FACILITY+ "' " +
				"AND (a.serviceType <> '" + ServiceDetailsForm.SERVICE_SCPMCP + "' " +
				"AND a.serviceType <> '" + ServiceDetailsForm.SERVICE_OTHER+ "') " +
				"AND un.serviceId = a.serviceType ";
		
		if (isToday){
			sql += " AND ? BETWEEN fa.requiredFrom AND fa.requiredTo  ";
			params.add(DateUtil.getToday());
		}
		
		if(fromDate!=null && toDate!=null){
			sql+=" AND (( fa.requiredFrom between ? AND ? ) OR ( fa.requiredTo between ? AND ? )) ";
			params.add(fromDate);
			params.add(toDate);
			params.add(fromDate);
			params.add(toDate);
		}
		
		if(search!=null && !"".equals(search)){
			sql+=" AND (u.firstName like '%"+search+"%' OR u.lastName like '%"+search+"%' OR r.title like '%"+search+"%' ) ";
		}
		if(department!=null && !"-1".equals(department)){
			sql+=" AND ( d.id = '"+department+"') ";
		}
		//sql+=" GROUP BY a.assignmentId, a.code, r.requestId, r.title, rc.name ,u.firstName, d.name, fa.requiredFrom, fa.requiredTo, fa.fromTime,fa.toTime,fa.rateCardId,fa.rateCardCategoryId ";
		sql += "GROUP BY a.groupId, a.serviceType, r.title, r.requestId, u.firstName, u.lastName, d.name, fa.requiredFrom, fa.requiredTo,fa.fromTime,fa.toTime,un.unitId ";
		if(sort!=null && !"".equals(sort)){
			String prefix = "r";
			if ("groupId".equals(sort)) {
				prefix = "a";
			} else if ("firstName".equals(sort)) {
				prefix = "u";
			} else if ("department".equals(sort)) {
				prefix = "d";
				sort = "name";
			} else if (("requiredFrom".equals(sort)) || ("requiredTo".equals(sort)) ) {
				prefix = "fa";
			} else if ("requiredTime".equals(sort)) {
				prefix = "fa";
				sort = "fromTime";
			}
			sql += " order by "+ prefix + "." + sort+" ";
		} else {
			sql += " order by fa.requiredFrom ASC, r.title ASC ";
		}
		
		if(desc){
			sql+=" DESC ";
		}
		
		return super.select(sql, HashMap.class, params.toArray(), start, rows);
	}
	
	public int selectAssignmentHOUCount(String search, String department, Date fromDate, Date toDate) throws DaoException{
		ArrayList params=new ArrayList();
		//String sql="Select COUNT(a.assignmentId) as COUNT " +
		//		" from fms_eng_assignment a INNER JOIN " +
		//		" fms_eng_request r on r.requestId=a.requestId INNER JOIN security_user u on r.createdBy=u.username " +
		//		" INNER JOIN fms_department d on u.department=d.id INNER JOIN fms_eng_assignment_equipment fa on a.assignmentId=fa.assignmentId " +
		//		" INNER JOIN fms_rate_card rc  ON rc.id=fa.rateCardId where 1=1 AND a.assignmentType='F' ";
		String sql = "SELECT a.groupId, r.title, r.requestId, u.firstName, d.name, fa.requiredFrom, fa.requiredTo,fa.fromTime,fa.toTime " +
			"FROM fms_eng_request r INNER JOIN fms_eng_assignment a ON (r.requestId = a.requestId) " +
			"INNER JOIN security_user u ON (r.createdBy=u.username) " +
			"INNER JOIN fms_department d on u.department=d.id " +
			"INNER JOIN fms_eng_assignment_equipment fa on a.assignmentId=fa.assignmentId " +
			"WHERE 1=1 AND a.assignmentType = '" + EngineeringModule.ASSIGNMENT_TYPE_FACILITY+ "' " +
			"AND (a.serviceType <> '" + ServiceDetailsForm.SERVICE_SCPMCP + "' " +
				"AND " +
			" a.serviceType <> '" + ServiceDetailsForm.SERVICE_OTHER+ "') ";

		//if (isToday){
		//	sql += " AND ? BETWEEN fa.requiredFrom AND fa.requiredTo  ";
		//	params.add(DateUtil.getToday());
		//}

		if(fromDate!=null && toDate!=null){
			sql+=" AND (( fa.requiredFrom between ? AND ? ) OR ( fa.requiredTo between ? AND ? )) ";
			params.add(fromDate);
			params.add(toDate);
			params.add(fromDate);
			params.add(toDate);
		}
		
		if(search!=null && !"".equals(search)){
			sql+=" AND (u.firstName like '%"+search+"%' OR u.lastName like '%"+search+"%' OR r.title like '%"+search+"%' ) ";
		}
		if(department!=null && !"-1".equals(department)){
			sql+=" AND ( d.id = '"+department+"') ";
		}
		//sql+=" GROUP BY a.assignmentId, a.code, r.title, rc.name ,u.firstName, d.name, fa.requiredFrom, fa.requiredTo,fa.fromTime,fa.toTime,fa.rateCardId,fa.rateCardCategoryId ";		
		sql += "GROUP BY a.groupId, r.title, r.requestId, u.firstName, d.name, fa.requiredFrom, fa.requiredTo,fa.fromTime,fa.toTime";
		
		int count=0;
		try{
			Collection col=super.select(sql, HashMap.class, params.toArray(), 0, -1);
			if(col!=null){
				//HashMap map=(HashMap)col.iterator().next();
				count=col.size();//(Integer)map.get("COUNT");
			}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return count; 
	}
	
	public FacilityObject selectLatestGroupId(String createdBy) throws DaoException {
		FacilityObject fo = new FacilityObject();
		Collection col = new ArrayList();
		
		String sql = "SELECT TOP 1 ic.groupId, ic.location, ic.takenBy, ic.preparedBy, " +
				"ic.purpose, loc.name as storeLocation,ic.checkout_by FROM fms_facility_item_checkout ic " +
				"LEFT JOIN fms_facility_item i ON ic.barcode = i.barcode " +
				"LEFT JOIN fms_facility_location loc ON i.location_id = loc.setup_id " +
				"WHERE ic.createdby = ? " +
				"AND ic.status = 'O' " +
				"AND ic.checkin_by IS NULL " +
				"AND ic.checkin_date IS NULL " +
				"ORDER BY ic.createdby_date DESC";
		col = super.select(sql, FacilityObject.class, new String[] {createdBy}, 0, -1);
		
		if (col != null && col.size()>0){
			fo = (FacilityObject) col.iterator().next();
		}
		
		return fo;
	}
	
	public Collection selectFacilityByGroupId(String groupId) throws DaoException {
		return super.select("SELECT f.name AS name, fi.barcode, fic.groupId, fic.checkout_by, fic.checkin_by, fic.checkout_date, fic.checkin_date," +
				"fic.status " +
				"FROM fms_facility_item_checkout fic INNER JOIN fms_facility_item fi ON (fic.barcode = fi.barcode) " +
				"INNER JOIN fms_facility f ON (fi.facility_id = f.id) " +
				"WHERE fic.groupId = ? ", FacilityObject.class, new String[] {groupId}, 0, -1);
	}
	
	public Collection getRequestListing(String search, String department,
			Date fromDate, Date toDate, String sort, boolean desc, boolean isToday,
			int start, int rows) throws DaoException {
		// sort clause
        String sortClause = "ORDER BY ";
		if (sort != null) {
			if (sort.equals("requiredFrom")) {
				sort = "r.requiredFrom";
			} else if (sort.equals("requiredTo")) {
				sort = "r.requiredTo";
			}
			
			sortClause += sort + " " + (desc ? "DESC " : "");
		} else {
			sortClause += "r.title ";
		}
		
		Map map = sqlRequestListing(search, department, fromDate, toDate, isToday);
		String sql = (String) map.get("selectClause") + (String) map.get("fromClause") + (String) map.get("whereClause") + sortClause;
		Object[] args = ((Collection) map.get("args")).toArray();

		try {
			return super.select(sql, HashMap.class, args, start, rows);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		return new ArrayList();
	}
	
	public int countRequestListing(String search, String department, 
			Date fromDate, Date toDate, boolean isToday) throws DaoException {
		Map map = sqlRequestListing(search, department, fromDate, toDate, isToday);
		String sql = (String) map.get("countClause") + (String) map.get("fromClause") + (String) map.get("whereClause");
		Object[] args = ((Collection) map.get("args")).toArray();

		try {
			Collection results = super.select(sql, HashMap.class, args, 0, -1);
			if (results.size() > 0) {
				Map totalMap = (Map) results.iterator().next();
				return Integer.parseInt(totalMap.get("total").toString());
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		return 0;
	}
	
	protected Map sqlRequestListing(String search, String department, Date fromDate, Date toDate, boolean isToday) {
		ArrayList args = new ArrayList();
		String selectClause = "SELECT " +
			"DISTINCT r.requestId, r.title as requestTitle, " +
			"(u.firstName + ' ' + u.lastName) AS requestor, " +
			"d.name as department, r.requiredFrom, r.requiredTo ";
		String countClause = "SELECT COUNT(DISTINCT r.requestId) AS total ";
		String fromClause =
			"FROM fms_eng_request r " +
			"LEFT JOIN security_user u ON (r.createdBy=u.username) " +
			"LEFT JOIN fms_department d ON (u.department=d.id) " +
			"LEFT JOIN fms_eng_assignment a ON (a.requestId=r.requestId) ";
		
		String whereClause = 
			"WHERE 1 = 1 " +
			"AND a.assignmentType = 'F' " +
			"AND (a.serviceType = '1' OR  a.serviceType = '6') ";

		if (search != null && !"".equals(search)) {
			whereClause += " AND (u.firstName like ? OR u.lastName like ? OR r.title like ? OR r.requestId like ?) ";
			args.add("%" + search + "%");
			args.add("%" + search + "%");
			args.add("%" + search + "%");
			args.add("%" + search + "%");
		}
		
		if (department != null && !"-1".equals(department)) {
			whereClause += "AND d.id = ? ";
			args.add(department);
		}
		
		// checking on different table
		if (isToday) {
			fromClause += "LEFT JOIN fms_eng_assignment_equipment e ON (a.groupId=e.groupId) ";
			whereClause += "AND ? BETWEEN e.requiredFrom AND e.requiredTo ";
			args.add(DateUtil.getToday());
		} else if (fromDate != null && toDate != null) {
			whereClause += "AND (r.requiredFrom <= ? AND r.requiredTo >= ?) ";
			args.add(toDate);
			args.add(fromDate);
		}

		HashMap hm = new HashMap();
		hm.put("selectClause", selectClause);
		hm.put("countClause", countClause);
		hm.put("fromClause", fromClause);
		hm.put("whereClause", whereClause);
		hm.put("args", args);
		return hm;
	}
	
	public Collection getAssignmentByRequestId(String requestId) throws DaoException {
		String sql = "select distinct a.groupId,a.assignmentId, a.serviceType, fa.requiredFrom, fa.requiredTo,fa.fromTime,fa.toTime "
				+ "from fms_eng_assignment a "
				+ "LEFT JOIN fms_eng_assignment_equipment fa on a.assignmentId=fa.assignmentId  "
				+ "where 1=1 "
				+ "AND a.requestId=? " 
				+ "AND a.assignmentType = '" + EngineeringModule.ASSIGNMENT_TYPE_FACILITY+ "' "
				+ "AND (a.serviceType = '"
				+ ServiceDetailsForm.SERVICE_SCPMCP
				+ "' OR  a.serviceType = '"
				+ ServiceDetailsForm.SERVICE_OTHER
				+ "')";
		;

		Collection list = super.select(sql, HashMap.class, new Object[] { requestId },0, -1);

		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}

	// added on 12 Mar 2010 for CR:# 158
		
	public Collection getRateCardIdByRequestId(String requestId) throws DaoException{
				
		ArrayList params = new ArrayList();
		String sql="select ae.rateCardId, ae.rateCardCategoryId, ae.fromTime, a.serviceType, ae.utilized " +
				"from fms_eng_request r " +
				"left join fms_eng_assignment a on a.requestId=r.requestId " +
				"left join fms_eng_assignment_equipment ae on ae.assignmentId=a.assignmentId " +
				"where (a.serviceType = ? OR a.serviceType = ?) " +
				"and ae.groupId=a.groupId " +
				"and a.requestId=? " +
				"group by ae.rateCardId, ae.rateCardCategoryId, ae.fromTime, a.serviceType, ae.utilized ";

		params.add(ServiceDetailsForm.SERVICE_SCPMCP);
		params.add(ServiceDetailsForm.SERVICE_OTHER);
		params.add(requestId);
		Collection list = super.select(sql, HashMap.class, params.toArray(), 0, -1);
		if(list!=null && list.size()>0){
			return list;
		}else{
			return null;
		}
		
	}
	
	public FacilityObject getItemNameByBarcode(String barcode) throws DaoException{
		String sql="SELECT f.name as item " +
				"from fms_facility f , fms_facility_item i " +
				"left join fms_facility_location l on i.location_id=l.setup_id " +
				"where i.facility_id=f.id AND i.barcode=?";
		Collection list = super.select(sql, FacilityObject.class, new String[]{barcode}, 0, -1);
		if(list!=null && list.size()>0){
			return (FacilityObject) list.iterator().next();
		}else{
			return null;
		}
	}
	
	public Collection getGroupIdsList(String requestId) throws DaoException{
		String sql="select distinct groupId as groupId "
				+ "from fms_eng_assignment where requestId=?";
		Collection list= super.select(sql, HashMap.class, new String[]{requestId}, 0, -1);
		if(list!=null && list.size()>0){
			return list;
		}else{
			return null;
		}
	}
		
	public Collection getServiceType(String requestId)throws DaoException{
		String sql = "select distinct serviceType from fms_eng_assignment where requestId=?";
		return super.select(sql, HashMap.class, new String[]{requestId}, 0, -1);
		
	}
	
	public void removePrepareItem(String id, String columnName) throws DaoException{
		if(columnName!=null && columnName.equals("assignmentEquipmentId")){
			Collection list = super.select("select barcode from fms_eng_assignment_equipment where id=?", HashMap.class, new String[]{id}, 0, -1);
			if(list!=null && list.size()>0){
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					Map mp = (Map) iterator.next();
					super.update("update fms_facility_item set status='1' where barcode='"+mp.get("barcode").toString()+"'", null);
					
				}
			}
			super.update("delete from fms_eng_assignment_equipment where id='"+id+"'", null);
		}else{
			Collection list = super.select("select barcode from fms_eng_assignment_equipment where assignmentId=?", HashMap.class, new String[]{id}, 0, -1);
			if(list!=null && list.size()>0){
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					Map mp = (Map) iterator.next();
					super.update("update fms_facility_item set status='1' where barcode='"+mp.get("barcode").toString()+"'", null);
					
				}
			}
			super.update("delete from fms_eng_assignment where assignmentId='"+id+"'", null);
			super.update("delete from fms_eng_assignment_equipment where assignmentId='"+id+"'", null);
		}
		
	}
	
	
	public Collection selectRequestItems(String requestId, boolean today)
			throws DaoException {
		Collection list = new ArrayList();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rsRequest = null;
		
		ArrayList params = new ArrayList();
		String sqlEquipment = "select distinct e.barcode, e.id, a.groupId,"
				+ "a.code, a.serviceType, a.serviceId, e.assignmentId, "
				+ "e.requiredFrom,requiredTo,requiredFrom, fromTime,"
				+ "toTime,e.status,e.rateCardCategoryId,e.takenBy, e.preparedBy,"
				+ "e.checkedOutBy,e.checkedOutDate,e.checkedInBy,e.checkedInDate, e.prepareCheckOutBy, e.prepareCheckoutDate, " 
				+ "e.status, a.requestId "
				+ "from fms_eng_assignment a "
				+ "LEFT JOIN fms_eng_assignment_equipment e on (a.assignmentId=e.assignmentId) "
				//+ "LEFT JOIN fms_eng_rate_card_category c on c.id=e.rateCardCategoryId  "
				//+ "LEFT JOIN fms_facility_item i ON e.barcode = i.barcode "
				//+ "LEFT JOIN fms_facility_location loc ON i.location_id = loc.setup_id   "
				+ " where 1=1 AND a.requestId = ?  " + "AND e.assignmentId not like '-' AND (a.serviceType=? or a.serviceType=?)";
		params.add(requestId);
		params.add(ServiceDetailsForm.SERVICE_SCPMCP);
		params.add(ServiceDetailsForm.SERVICE_OTHER);
		if (today) {
			sqlEquipment += " AND ? BETWEEN e.requiredFrom AND e.requiredTo ";
			params.add(DateUtil.getToday());
		}
		
		try {
			con = super.getDataSource().getConnection();
			statement = JdbcUtil.getInstance().createPreparedStatement(con,sqlEquipment, params.toArray());
			try {
				HashMap catMap = new HashMap();
				HashMap locMap = new HashMap();
				
				rsRequest = statement.executeQuery();
				while (rsRequest.next()) {

					Assignment obj = new Assignment();
					obj.setGroupId(rsRequest.getString("groupId"));
					obj.setBarcode(rsRequest.getString("barcode"));
					obj.setCode(rsRequest.getString("code"));
					obj.setServiceType(rsRequest.getString("serviceType"));
					obj.setServiceId(rsRequest.getString("serviceId"));
					obj.setAssignmentId(rsRequest.getString("assignmentId"));
					obj.setRequiredFrom(rsRequest.getDate("requiredFrom"));
					obj.setRequiredTo(rsRequest.getDate("requiredTo"));
					obj.setCheckedOutBy(rsRequest.getString("checkedOutBy"));
					obj.setCheckedOutDate(rsRequest.getTimestamp("checkedOutDate"));
					obj.setCheckedInBy(rsRequest.getString("checkedInBy"));
					obj.setCheckedInDate(rsRequest.getTimestamp("checkedInDate"));
					obj.setPrepareCheckOutBy(rsRequest.getString("prepareCheckOutBy"));
					obj.setPrepareCheckOutDate(rsRequest.getTimestamp("prepareCheckoutDate"));
					obj.setStatus(rsRequest.getString("status"));
					obj.setRequestId(rsRequest.getString("requestId"));
					obj.setAssignmentEquipmentId(rsRequest.getString("id"));
					
					String rateCardCategoryId = rsRequest.getString("rateCardCategoryId");
					if (catMap.containsKey(rateCardCategoryId)) {
						String rateCardCategoryName = (String) catMap.get(rateCardCategoryId);
						String storeLocation = (String) locMap.get(rateCardCategoryId);
						
						obj.setRateCardCategoryName(rateCardCategoryName);
						obj.setStoreLocation(storeLocation);
					} else {
						String sql="select e.assignmentId, c.name as rateCardCategoryName, loc.name as storeLocation " +
								"from fms_eng_assignment a " +
								"LEFT JOIN fms_eng_assignment_equipment e on a.assignmentId=e.assignmentId " +
								"LEFT JOIN fms_eng_rate_card_category c on c.id=e.rateCardCategoryId " +
								"LEFT JOIN fms_facility_item i ON i.barcode = e.barcode " +
								"LEFT JOIN fms_facility_location loc ON i.location_id = loc.setup_id " +
								"where c.id=? and a.requestId=?";
						Collection listItem = super.select(sql, HashMap.class, new String[]{rateCardCategoryId, requestId}, 0, 1);
						
						if (listItem!=null && listItem.size() > 0) {
							for (Iterator iterator = listItem.iterator(); iterator.hasNext();) {
								Map mp = (Map) iterator.next();
								
								String rateCardCategoryName = (String) mp.get("rateCardCategoryName");
								obj.setRateCardCategoryName(rateCardCategoryName);
								catMap.put(rateCardCategoryId, rateCardCategoryName);
								
								String storeLocation = (String) mp.get("storeLocation");
								if (storeLocation == null) {
									storeLocation = "";
								}
								obj.setStoreLocation(storeLocation);
								locMap.put(rateCardCategoryId, storeLocation);
							}
						}
					}
					list.add(obj);

				}

			} catch (SQLException e) {
				Log.getLog(FacilityDao.class).error(e);
			} finally {
				if (rsRequest != null)
					rsRequest.close();
				if (statement != null)
					statement.close();
				if (con != null)
					con.close();
			}
		} catch (SQLException e) {
			throw new DaoException(e.toString());
		}
		return list;
	}
	
	public Collection selectRequestItemsGroupAssignment(String requestId, String groupId, Date reqFrom, Date reqTo,boolean today)
	throws DaoException {
			Collection list = new ArrayList();
			Connection con = null;
			PreparedStatement statement = null;
			ResultSet rsRequest = null;
			
			ArrayList params = new ArrayList();
			String sqlEquipment = "select distinct e.barcode, e.id, a.groupId,"
					+ "e.requiredFrom,requiredTo,requiredFrom, fromTime,"
					+ "toTime,e.status,e.rateCardCategoryId,e.takenBy, e.preparedBy,"
					+ "e.checkedOutBy,e.checkedOutDate,e.checkedInBy,e.checkedInDate, e.prepareCheckOutBy, e.prepareCheckoutDate, " 
					+ "e.status, a.requestId "
					+ "from fms_eng_assignment a "
					+ "LEFT JOIN fms_eng_assignment_equipment e on a.groupId=e.groupId "
					//+ "LEFT JOIN fms_eng_rate_card_category c on c.id=e.rateCardCategoryId  "
					//+ "LEFT JOIN fms_facility_item i ON e.barcode = i.barcode "
					//+ "LEFT JOIN fms_facility_location loc ON i.location_id = loc.setup_id   "
					+ " where 1=1 and e.status != 'N'  AND a.requestId = ?  " + "AND e.assignmentId != '-'  AND (a.serviceType=? or a.serviceType=?)";
			params.add(requestId);
			params.add(ServiceDetailsForm.SERVICE_SCPMCP);
			params.add(ServiceDetailsForm.SERVICE_OTHER);
			 DateFormat formatter ; 
		     Date dateReqFr=null ;
		     Date dateReqTo=null ;
		     Date dateToday=null ;
		     formatter = new SimpleDateFormat("yyyy-MM-dd");
		     if(today){
		    	 String sToday = formatter.format(DateUtil.getToday());
		    	 try {
	            	  dateToday = (Date)formatter.parse(sToday);
				} catch (ParseException e1) {
					Log.getLog(getClass()).error(e1.toString(), e1);
				}    
	            
				sqlEquipment += " AND ? BETWEEN e.requiredFrom AND e.requiredTo ";
				params.add(dateToday);
				
		     }else if(reqFrom!=null && reqTo!=null){
		    	 
	              String sfrom = formatter.format(reqFrom);
	              String sto = formatter.format(reqTo);
	              try {
	            	  dateReqFr = (Date)formatter.parse(sfrom);
	            	  dateReqTo = (Date)formatter.parse(sto);
				} catch (ParseException e1) {
					Log.getLog(getClass()).error(e1.toString(), e1);
				}    
	            
				sqlEquipment += " AND (e.requiredFrom >= ? AND e.requiredTo<= ?) ";
				params.add(dateReqFr);
				params.add(dateReqTo);
		     }
		             
			
			//sqlEquipment += " AND a.groupId = ? ";
			//params.add(groupId);
			
			try {
				con = super.getDataSource().getConnection();
				statement = JdbcUtil.getInstance().createPreparedStatement(con,sqlEquipment, params.toArray());
				try {
					rsRequest = statement.executeQuery();
					while (rsRequest.next()) {
			
						Assignment obj = new Assignment();
						obj.setGroupId(rsRequest.getString("groupId"));
						obj.setBarcode(rsRequest.getString("barcode"));
						obj.setRequiredFrom(rsRequest.getDate("requiredFrom"));
						obj.setRequiredTo(rsRequest.getDate("requiredTo"));
						obj.setCheckedOutBy(rsRequest.getString("checkedOutBy"));
						obj.setCheckedOutDate(rsRequest.getTimestamp("checkedOutDate"));
						obj.setCheckedInBy(rsRequest.getString("checkedInBy"));
						obj.setCheckedInDate(rsRequest.getTimestamp("checkedInDate"));
						obj.setPrepareCheckOutBy(rsRequest.getString("prepareCheckOutBy"));
						obj.setPrepareCheckOutDate(rsRequest.getTimestamp("prepareCheckoutDate"));
						obj.setStatus(rsRequest.getString("status"));
						obj.setRequestId(rsRequest.getString("requestId"));
						obj.setAssignmentEquipmentId(rsRequest.getString("id"));
						String sql="select a.code, a.serviceType, a.serviceId, e.assignmentId , c.name as rateCardCategoryName, loc.name as storeLocation " +
								"from fms_eng_assignment a " +
								"LEFT JOIN fms_eng_assignment_equipment e on a.assignmentId=e.assignmentId " +
								"LEFT JOIN fms_eng_rate_card_category c on c.id=e.rateCardCategoryId " +
								"LEFT JOIN fms_facility_item i ON i.barcode = e.barcode " +
								"LEFT JOIN fms_facility_location loc ON i.location_id = loc.setup_id " +
								"where c.id=? and a.requestId=?";
						Collection listItem = super.select(sql, HashMap.class, new String[]{rsRequest.getString("rateCardCategoryId"), requestId}, 0, -1);
						if(listItem!=null && listItem.size()>0){
							for (Iterator iterator = listItem.iterator(); iterator.hasNext();) {
								Map mp = (Map) iterator.next();
								obj.setRateCardCategoryName(mp.get("rateCardCategoryName").toString());
								if(mp.get("storeLocation")!=null){
									obj.setStoreLocation(mp.get("storeLocation").toString());
								}else{
									obj.setStoreLocation("");
								}
								
								obj.setAssignmentId(mp.get("assignmentId").toString());
								obj.setCode(mp.get("code").toString());
								obj.setServiceType(mp.get("serviceType").toString());
								obj.setServiceId(mp.get("serviceId").toString());
								
							}
						}
						list.add(obj);
			
					}
			
				} catch (SQLException e) {
					Log.getLog(FacilityDao.class).error(e);
				} finally {
					if (rsRequest != null)
						rsRequest.close();
					if (statement != null)
						statement.close();
					if (con != null)
						con.close();
				}
			} catch (SQLException e) {
				throw new DaoException(e.toString());
			}
			return list;
		}

	public Collection selectRequestItemsForCheckOut(String requestId, boolean today)
	throws DaoException {
		Collection list = new ArrayList();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rsRequest = null;
		
		ArrayList params = new ArrayList();
		String sqlEquipment = "select distinct e.barcode, e.id, a.groupId, "
			+ "e.requiredFrom,requiredTo,requiredFrom, fromTime, "
			+ "toTime,e.status,e.rateCardCategoryId,e.takenBy, e.preparedBy, "
			+ "e.checkedOutBy,e.checkedOutDate,e.checkedInBy,e.checkedInDate, e.prepareCheckOutBy, e.prepareCheckoutDate, " 
			+ "e.status, a.requestId, a.groupId, c.name "
			+ "from fms_eng_assignment a "
			+ "LEFT JOIN fms_eng_assignment_equipment e on a.groupId=e.groupId "
			+ "LEFT JOIN fms_eng_rate_card_category c on c.id=e.rateCardCategoryId  "
			+ "LEFT JOIN fms_facility_item i ON e.barcode = i.barcode "
			+ "LEFT JOIN fms_facility_location loc ON i.location_id = loc.setup_id where 1=1  "
			+ "AND a.requestId = ?  " + "AND e.assignmentId not like '-' AND (a.serviceType=? or a.serviceType=?) ";
		params.add(requestId);
		params.add(ServiceDetailsForm.SERVICE_SCPMCP);
		params.add(ServiceDetailsForm.SERVICE_OTHER);
		if (today) {
			sqlEquipment += " AND ? BETWEEN e.requiredFrom AND e.requiredTo ";
			params.add(DateUtil.getToday());
		}
		sqlEquipment += "ORDER BY c.name ASC ";
		try {
			con = super.getDataSource().getConnection();
			statement = JdbcUtil.getInstance().createPreparedStatement(con,sqlEquipment, params.toArray());
			try {
				rsRequest = statement.executeQuery();
				while (rsRequest.next()) {

					Assignment obj = new Assignment();
					obj.setGroupId(rsRequest.getString("groupId"));
					obj.setBarcode(rsRequest.getString("barcode"));
					obj.setRequiredFrom(rsRequest.getDate("requiredFrom"));
					obj.setRequiredTo(rsRequest.getDate("requiredTo"));
					obj.setCheckedOutBy(rsRequest.getString("checkedOutBy"));
					obj.setCheckedOutDate(rsRequest.getDate("checkedOutDate"));
					obj.setCheckedInBy(rsRequest.getString("checkedInBy"));
					obj.setCheckedInDate(rsRequest.getDate("checkedInDate"));
					obj.setPrepareCheckOutBy(rsRequest.getString("prepareCheckOutBy"));
					obj.setPrepareCheckOutDate(rsRequest.getDate("prepareCheckoutDate"));
					obj.setStatus(rsRequest.getString("status"));
					obj.setRequestId(rsRequest.getString("requestId"));
					obj.setAssignmentEquipmentId(rsRequest.getString("id"));
					String sql="select a.code, a.serviceType, a.serviceId, e.assignmentId , c.name as rateCardCategoryName, loc.name as storeLocation " +
					"from fms_eng_assignment a " +
					"LEFT JOIN fms_eng_assignment_equipment e on a.assignmentId=e.assignmentId " +
					"LEFT JOIN fms_eng_rate_card_category c on c.id=e.rateCardCategoryId " +
					"LEFT JOIN fms_facility_item i ON i.barcode = e.barcode " +
					"LEFT JOIN fms_facility_location loc ON i.location_id = loc.setup_id " +
					"where c.id=? and a.requestId=? " +
					"ORDER BY c.name ASC ";
					Collection listItem = super.select(sql, HashMap.class, new String[]{rsRequest.getString("rateCardCategoryId"), requestId}, 0, -1);
					if(listItem!=null && listItem.size()>0){
						for (Iterator iterator = listItem.iterator(); iterator.hasNext();) {
							Map mp = (Map) iterator.next();
							obj.setRateCardCategoryName(mp.get("rateCardCategoryName").toString());
							if(mp.get("storeLocation")!=null){
								obj.setStoreLocation(mp.get("storeLocation").toString());
							}else{
								obj.setStoreLocation("");
							}

							obj.setAssignmentId(mp.get("assignmentId").toString());
							obj.setCode(mp.get("code").toString());
							obj.setServiceType(mp.get("serviceType").toString());
							obj.setServiceId(mp.get("serviceId").toString());

						}
					}
					list.add(obj);

				}

			} catch (SQLException e) {
				Log.getLog(FacilityDao.class).error(e);
			} finally {
				if (rsRequest != null)
					rsRequest.close();
				if (statement != null)
					statement.close();
				if (con != null)
					con.close();
			}
		} catch (SQLException e) {
			throw new DaoException(e.toString());
		}
		return list;
	}
	
	public Collection selectExtraRequestItems(String requestId, boolean today) throws DaoException {
		Collection list = new ArrayList();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rsRequest = null;
		
		ArrayList params = new ArrayList();
		String sqlEquipment = "select distinct e.barcode, e.id, a.groupId,"
				+ "e.requiredFrom,requiredTo,requiredFrom, fromTime,"
				+ "toTime,e.status,e.rateCardCategoryId,e.takenBy, e.preparedBy,"
				+ "e.checkedOutBy,e.checkedOutDate,e.checkedInBy,e.checkedInDate, e.prepareCheckOutBy, e.prepareCheckoutDate, " 
				+ "e.status, a.requestId, a.groupId, a.serviceId, a.serviceType "
				+ "from fms_eng_assignment a "
				+ "LEFT JOIN fms_eng_assignment_equipment e on a.groupId=e.groupId "
				+ "LEFT JOIN fms_eng_rate_card_category c on c.id=e.rateCardCategoryId  "
				+ "LEFT JOIN fms_facility_item i ON e.barcode = i.barcode "
				+ "LEFT JOIN fms_facility_location loc ON i.location_id = loc.setup_id where 1=1  "
				+ "AND a.requestId = ?  " + "AND e.assignmentId = '-' AND (a.serviceType=? or a.serviceType=?) ";
		
		params.add(requestId);
		params.add(ServiceDetailsForm.SERVICE_SCPMCP);
		params.add(ServiceDetailsForm.SERVICE_OTHER);
		if (today) {
			sqlEquipment += " AND ? BETWEEN e.requiredFrom AND e.requiredTo ";
			params.add(DateUtil.getToday());
		}
		
		try {
			con = super.getDataSource().getConnection();
			statement = JdbcUtil.getInstance().createPreparedStatement(con,sqlEquipment, params.toArray());
			try {
				rsRequest = statement.executeQuery();
				while (rsRequest.next()) {
		
					Assignment obj = new Assignment();
					obj.setGroupId(rsRequest.getString("groupId"));
					obj.setBarcode(rsRequest.getString("barcode"));
					obj.setRequiredFrom(rsRequest.getDate("requiredFrom"));
					obj.setRequiredTo(rsRequest.getDate("requiredTo"));
					obj.setCheckedOutBy(rsRequest.getString("checkedOutBy"));
					obj.setCheckedOutDate(rsRequest.getTimestamp("checkedOutDate"));
					obj.setCheckedInBy(rsRequest.getString("checkedInBy"));
					obj.setCheckedInDate(rsRequest.getTimestamp("checkedInDate"));
					obj.setPrepareCheckOutBy(rsRequest.getString("prepareCheckOutBy"));
					obj.setPrepareCheckOutDate(rsRequest.getTimestamp("prepareCheckoutDate"));
					obj.setStatus(rsRequest.getString("status"));
					obj.setRequestId(rsRequest.getString("requestId"));
					obj.setAssignmentEquipmentId(rsRequest.getString("id"));
					
					obj.setServiceType(rsRequest.getString("serviceType"));
					obj.setServiceId(rsRequest.getString("serviceId"));
					
					if(rsRequest.getString("rateCardCategoryId")!=null && !rsRequest.getString("rateCardCategoryId").equals("")){
						String sql="select a.code, a.serviceType, a.serviceId, e.assignmentId , c.name as rateCardCategoryName, loc.name as storeLocation " +
								"from fms_eng_assignment a " +
								"LEFT JOIN fms_eng_assignment_equipment e on a.assignmentId=e.assignmentId " +
								"LEFT JOIN fms_eng_rate_card_category c on c.id=e.rateCardCategoryId " +
								"LEFT JOIN fms_facility_item i ON i.barcode = e.barcode " +
								"LEFT JOIN fms_facility_location loc ON i.location_id = loc.setup_id " +
								"where c.id=? and a.requestId=?";
						Collection listItem = super.select(sql, HashMap.class, new String[]{rsRequest.getString("rateCardCategoryId"), requestId}, 0, -1);
						if(listItem!=null && listItem.size()>0){
							for (Iterator iterator = listItem.iterator(); iterator.hasNext();) {
								Map mp = (Map) iterator.next();
								obj.setRateCardCategoryName(mp.get("rateCardCategoryName").toString());
								if(mp.get("storeLocation")!=null){
									obj.setStoreLocation(mp.get("storeLocation").toString());
								}else{
									obj.setStoreLocation("");
								}
								
								obj.setAssignmentId(mp.get("assignmentId").toString());
								obj.setCode(mp.get("code").toString());
								
							}
						}
					}
					
					list.add(obj);
		
				}
		
			} catch (SQLException e) {
				Log.getLog(FacilityDao.class).error(e);
			} finally {
				if (rsRequest != null)
					rsRequest.close();
				if (statement != null)
					statement.close();
				if (con != null)
					con.close();
			}
		} catch (SQLException e) {
			throw new DaoException(e.toString());
		}
		return list;
	}
	public Collection selectExtraRequestItemsByGroup(String requestId, String groupId, Date reqFrom, Date reqTo, boolean today) throws DaoException {
		Collection list = new ArrayList();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rsRequest = null;
		
		ArrayList params = new ArrayList();
		String sqlEquipment = "select distinct e.barcode, f.name as facilityName, e.id,"
				+ "e.requiredFrom,requiredTo,requiredFrom, fromTime,"
				+ "toTime,e.status,e.rateCardCategoryId,e.takenBy, e.preparedBy,"
				+ "e.checkedOutBy,e.checkedOutDate,e.checkedInBy,e.checkedInDate, e.prepareCheckOutBy, e.prepareCheckoutDate, " 
				+ "e.status, a.requestId, a.groupId, a.serviceId, a.serviceType "
				+ "from fms_eng_assignment a "
				+ "LEFT JOIN fms_eng_assignment_equipment e on a.groupId=e.groupId "
				+ "LEFT JOIN fms_eng_rate_card_category c on c.id=e.rateCardCategoryId  "
				+ "LEFT JOIN fms_facility_item i ON e.barcode = i.barcode "
				+ "LEFT JOIN fms_facility_location loc ON i.location_id = loc.setup_id " 
				+ "LEFT JOIN fms_facility f ON (i.facility_id = f.id) "
				+ "where 1=1  "
				+ "AND a.requestId = ? " + "AND e.assignmentId = '-' AND (a.serviceType=? or a.serviceType=?) ";
		
		params.add(requestId);
		params.add(ServiceDetailsForm.SERVICE_SCPMCP);
		params.add(ServiceDetailsForm.SERVICE_OTHER);
		 DateFormat formatter ; 
	     Date dateReqFr=null ;
	     Date dateReqTo=null ;
	     Date dateToday = null;
	     formatter = new SimpleDateFormat("yyyy-MM-dd");

		if (today) {
			String sToday = formatter.format(DateUtil.getToday());
			try {
				dateToday = (Date)formatter.parse(sToday);
			} catch (ParseException e1) {
				Log.getLog(getClass()).error(e1.toString(), e1);
			}

			sqlEquipment += " AND ? BETWEEN e.requiredFrom AND e.requiredTo ";
			params.add(dateToday);

		} else if (reqFrom != null && reqTo != null) {
			String sfrom = formatter.format(reqFrom);
			String sto = formatter.format(reqTo);
			try {
				dateReqFr = (Date)formatter.parse(sfrom);
				dateReqTo = (Date)formatter.parse(sto);
			} catch (ParseException e1) {
				Log.getLog(getClass()).error(e1.toString(), e1);
			}
			
			sqlEquipment += " AND (e.requiredFrom >= ? AND e.requiredTo<= ?) ";
			params.add(dateReqFr);
			params.add(dateReqTo);
		}

	     
		//sqlEquipment += " AND a.groupId = ? ";
		//params.add(groupId);
		
		try {
			con = super.getDataSource().getConnection();
			statement = JdbcUtil.getInstance().createPreparedStatement(con,sqlEquipment, params.toArray());
			try {
				rsRequest = statement.executeQuery();
				while (rsRequest.next()) {
		
					Assignment obj = new Assignment();
					obj.setGroupId(rsRequest.getString("groupId"));
					obj.setBarcode(rsRequest.getString("barcode"));
					obj.setFacilityName(rsRequest.getString("facilityName"));
					obj.setRequiredFrom(rsRequest.getDate("requiredFrom"));
					obj.setRequiredTo(rsRequest.getDate("requiredTo"));
					obj.setCheckedOutBy(rsRequest.getString("checkedOutBy"));
					obj.setCheckedOutDate(rsRequest.getTimestamp("checkedOutDate"));
					obj.setCheckedInBy(rsRequest.getString("checkedInBy"));
					obj.setCheckedInDate(rsRequest.getTimestamp("checkedInDate"));
					obj.setPrepareCheckOutBy(rsRequest.getString("prepareCheckOutBy"));
					obj.setPrepareCheckOutDate(rsRequest.getTimestamp("prepareCheckoutDate"));
					obj.setStatus(rsRequest.getString("status"));
					obj.setRequestId(rsRequest.getString("requestId"));
					obj.setAssignmentEquipmentId(rsRequest.getString("id"));
					
					obj.setServiceType(rsRequest.getString("serviceType"));
					obj.setServiceId(rsRequest.getString("serviceId"));
					
					if(rsRequest.getString("rateCardCategoryId")!=null && !rsRequest.getString("rateCardCategoryId").equals("")){
						String sql="select a.code, a.serviceType, a.serviceId, e.assignmentId , c.name as rateCardCategoryName, loc.name as storeLocation " +
								"from fms_eng_assignment a " +
								"LEFT JOIN fms_eng_assignment_equipment e on a.assignmentId=e.assignmentId " +
								"LEFT JOIN fms_eng_rate_card_category c on c.id=e.rateCardCategoryId " +
								"LEFT JOIN fms_facility_item i ON i.barcode = e.barcode " +
								"LEFT JOIN fms_facility_location loc ON i.location_id = loc.setup_id " +
								"where c.id=? and a.requestId=?";
						Collection listItem = super.select(sql, HashMap.class, new String[]{rsRequest.getString("rateCardCategoryId"), requestId}, 0, -1);
						if(listItem!=null && listItem.size()>0){
							for (Iterator iterator = listItem.iterator(); iterator.hasNext();) {
								Map mp = (Map) iterator.next();
								obj.setRateCardCategoryName(mp.get("rateCardCategoryName").toString());
								if(mp.get("storeLocation")!=null){
									obj.setStoreLocation(mp.get("storeLocation").toString());
								}else{
									obj.setStoreLocation("");
								}
								
								obj.setAssignmentId(mp.get("assignmentId").toString());
								obj.setCode(mp.get("code").toString());
								
							}
						}
					}
					
					list.add(obj);
		
				}
		
			} catch (SQLException e) {
				Log.getLog(FacilityDao.class).error(e);
			} finally {
				if (rsRequest != null)
					rsRequest.close();
				if (statement != null)
					statement.close();
				if (con != null)
					con.close();
			}
		} catch (SQLException e) {
			throw new DaoException(e.toString());
		}
		return list;
	}
	public Collection getPreparedItems(String requestId)throws DaoException {
		ArrayList params = new ArrayList();
		String sqlQuery = "select distinct e.id, e.groupId, e.takenBy, e.prepareCheckOutBy, e.prepareCheckOutDate, " +
			"e.preparedBy, e.rateCardCategoryId, e.barcode, e.status, f.name as rateCardCategoryName " +
			"from fms_eng_assignment a " +
			"left join fms_eng_assignment_equipment e on e.groupId=a.groupId " +
			"LEFT JOIN  fms_facility_item fi on e.barcode = fi.barcode " +
			"left join fms_facility f on f.id=fi.facility_id " +
			"where a.requestId=? " +
			"and e.status=?";
		params.add(requestId);
		params.add(EngineeringModule.ASSIGNMENT_FACILITY_STATUS_PREPARE_CHECKOUT);
		Collection list = super.select(sqlQuery, Assignment.class, params.toArray(), 0, -1);
		if(list!=null && list.size()>0){
			return list;
		}else{
			return null;
		}
		
	}
	
	public Collection selectAssignmentCheckOutDetails(String requestId, String search, String sort, boolean desc, int start, int rows) throws DaoException{
		ArrayList params=new ArrayList();
		String sql="Select a.code, a.serviceType, a.requestId, a.serviceId, e.assignmentId,e.requiredFrom,e.barcode AS barcode, e.groupId, " +
				"requiredTo,fromTime,toTime,e.status,e.rateCardCategoryId,f.name as facilityName, c.name as rateCardCategoryName," +
				"e.takenBy, e.preparedBy," +
				"e.checkedOutBy,e.checkedOutDate,e.checkedInBy,e.checkedInDate, e.reasonUnfulfilled, e.assignmentLocation," +
				"loc.name as storeLocation " +
				"FROM fms_eng_assignment a RIGHT JOIN fms_eng_assignment_equipment e on a.assignmentId=e.assignmentId " + 
				"LEFT JOIN fms_eng_rate_card_category c on c.id=e.rateCardCategoryId " +
				"LEFT JOIN fms_facility_item i ON e.barcode = i.barcode " +
				"LEFT JOIN fms_facility_location loc ON i.location_id = loc.setup_id " +
				"LEFT JOIN fms_facility f ON (i.facility_id = f.id) " +
				"WHERE 1=1 AND e.barcode IS NOT NULL ";

		sql += " AND (a.requestId = ?) ";
		params.add(requestId);
		
		if(search!=null && !"".equals(search)){
			sql += " AND (f.name LIKE ? OR e.barcode LIKE ? OR e.checkedOutBy LIKE ? OR e.checkedInBy LIKE ? ) ";
			params.add("%"+search+"%");
			params.add("%"+search+"%");
			params.add("%"+search+"%");
			params.add("%"+search+"%");
		}
		
		if(sort!=null && !"".equals(sort)){
			sql += " order by "+sort+" ";
		} else {
			sql += " order by facilityName ASC ";
		}
		
		if(desc){
			sql+=" DESC ";
		}
		
		Collection col=new ArrayList();
		try{
			col= super.select(sql, Assignment.class, params.toArray(), start, rows);
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public Collection selectAssignmentCheckOutDetailsUnion(String requestId, String search, String sort, boolean desc, int start, int rows) throws DaoException{
		ArrayList params=new ArrayList();
		String sql=" (Select distinct e.barcode AS barcode, f.name as facilityName, e.checkedOutDate, e.checkedOutBy, e.checkedInDate, e.checkedInBy " +
				" FROM fms_eng_assignment a RIGHT JOIN fms_eng_assignment_equipment e on a.assignmentId=e.assignmentId " +
				" LEFT JOIN fms_facility_item i ON e.barcode = i.barcode  " +
				" LEFT JOIN fms_facility f ON (i.facility_id = f.id)" +
				" WHERE 1=1 AND e.barcode IS NOT NULL AND a.requestId=? ";
				params.add(requestId);
		
		if(search!=null && !"".equals(search)){
			sql += " AND (f.name LIKE ? OR e.barcode LIKE ? OR e.checkedOutBy LIKE ? OR e.checkedInBy LIKE ? )";
			params.add("%"+search+"%");
			params.add("%"+search+"%");
			params.add("%"+search+"%");
			params.add("%"+search+"%");
		}
		
		sql+=") union all ";
		
		sql+=" (select distinct e.barcode AS barcode, f.name as facilityName, e.checkedOutDate, e.checkedOutBy, e.checkedInDate, e.checkedInBy " +
				" from fms_eng_assignment a " +
				" LEFT JOIN fms_eng_assignment_equipment e on a.groupId=e.groupId " +
				" LEFT JOIN fms_eng_rate_card_category c on c.id=e.rateCardCategoryId " +
				" LEFT JOIN fms_facility_item i ON e.barcode = i.barcode  " +
				" LEFT JOIN fms_facility_location loc ON i.location_id = loc.setup_id  " +
				" LEFT JOIN fms_facility f ON (i.facility_id = f.id)  " +
				" where 1=1 " +
				" AND a.requestId = ? AND e.assignmentId = '-' " +
				" AND (a.serviceType=? or a.serviceType=?) ";
		params.add(requestId);
		params.add(ServiceDetailsForm.SERVICE_SCPMCP);
		params.add(ServiceDetailsForm.SERVICE_OTHER);
		
		if(search!=null && !"".equals(search)){
			sql += " AND (f.name LIKE ? OR e.barcode LIKE ? OR e.checkedOutBy LIKE ? OR e.checkedInBy LIKE ? )";
			params.add("%"+search+"%");
			params.add("%"+search+"%");
			params.add("%"+search+"%");
			params.add("%"+search+"%");
		}
		sql+=" ) ";
		if(sort!=null && !"".equals(sort)){
			sql += " order by "+sort+" ";
		} else {
			sql += " order by facilityName ASC ";
		}
		
		if(desc){
			sql+=" DESC ";
		}
		
		Collection col=new ArrayList();
		try{
			col= super.select(sql, Assignment.class, params.toArray(), start, rows);
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public int countAssignmentCheckOutDetails(String requestId, String search) throws DaoException{
		ArrayList params=new ArrayList();
		
		String sql="Select COUNT(a.code) AS total " +
				"FROM fms_eng_assignment a RIGHT JOIN fms_eng_assignment_equipment e on a.assignmentId=e.assignmentId " + 
				"LEFT JOIN fms_eng_rate_card_category c on c.id=e.rateCardCategoryId " +
				"LEFT JOIN fms_facility_item i ON e.barcode = i.barcode " +
				"LEFT JOIN fms_facility_location loc ON i.location_id = loc.setup_id " +
				"LEFT JOIN fms_facility f ON (i.facility_id = f.id) " +
				"WHERE 1=1 AND e.barcode IS NOT NULL ";
		
		sql += " AND (a.requestId = ?) ";
		params.add(requestId);
		
		if(search!=null && !"".equals(search)){
			sql += " AND (f.name LIKE ? OR e.barcode LIKE ? OR e.checkedOutBy LIKE ? OR e.checkedInBy LIKE ? ) ";
			params.add("%"+search+"%");
			params.add("%"+search+"%");
			params.add("%"+search+"%");
			params.add("%"+search+"%");
		}

		int count=0;
		try{
			Collection col=super.select(sql, HashMap.class, params.toArray(), 0, -1);
			if(col!=null && col.size() > 0){
				HashMap map=(HashMap)col.iterator().next();
				count = (Integer)map.get("total");
			}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return count; 
	}
	
	public int countAssignmentCheckOutDetailsAll(String requestId, String search, String type) throws DaoException{
		ArrayList params=new ArrayList();
		
		String sql="select count(barcode) as total from ( " +
				" select distinct e.barcode AS barcode, f.name as facilityName, e.checkedOutDate, e.checkedOutBy, " +
				" e.checkedInDate, e.checkedInBy  from fms_eng_assignment a RIGHT JOIN fms_eng_assignment_equipment e on a.assignmentId=e.assignmentId  " +
				" LEFT JOIN fms_facility_item i ON e.barcode = i.barcode " +
				" LEFT JOIN fms_facility f ON (i.facility_id = f.id) " +
				" WHERE 1=1 AND e.barcode IS NOT NULL AND a.requestId=?  ";
			params.add(requestId);
			
		if(type.equals("IN"))
			sql+=" AND e.status='I' ";
		else if(type.equals("OUT"))
			sql+=" AND e.status='O' ";
		
		
		if(search!=null && !"".equals(search)){
			sql += " AND (f.name LIKE ? OR e.barcode LIKE ? OR e.checkedOutBy LIKE ? OR e.checkedInBy LIKE ? ) ";
			params.add("%"+search+"%");
			params.add("%"+search+"%");
			params.add("%"+search+"%");
			params.add("%"+search+"%");
		}
		sql+=") as chkoutLst ";
		int count=0;
		try{
			Collection col=super.select(sql, HashMap.class, params.toArray(), 0, -1);
			if(col!=null && col.size() > 0){
				HashMap map=(HashMap)col.iterator().next();
				count = (Integer)map.get("total");
				count += countAssignmentCheckOutDetailsExtra(requestId, search,type);
			}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return count; 
	}
	
	public int countAssignmentCheckOutDetailsExtra(String requestId, String search,String type) throws DaoException{
		ArrayList params=new ArrayList();
		
		String sql=" select count(barcode) as total from ( " +
				" select distinct e.barcode AS barcode, f.name as facilityName, e.checkedOutDate, e.checkedOutBy," +
				" e.checkedInDate, e.checkedInBy  from fms_eng_assignment a  " +
				" LEFT JOIN fms_eng_assignment_equipment e on a.groupId=e.groupId " +
				" LEFT JOIN fms_facility_item i ON e.barcode = i.barcode  " +
				" LEFT JOIN fms_facility f ON (i.facility_id = f.id)  " +
				" where 1=1 " +
				" AND a.requestId = ? AND e.assignmentId = '-' " +
				" AND (a.serviceType=? or a.serviceType=?) ";
			params.add(requestId);
			params.add(ServiceDetailsForm.SERVICE_SCPMCP);
			params.add(ServiceDetailsForm.SERVICE_OTHER);
			
			if(type.equals("IN"))
				sql+=" AND e.status='I' ";
			else if(type.equals("OUT"))
				sql+=" AND e.status='O' ";
		
		if(search!=null && !"".equals(search)){
			sql += " AND (f.name LIKE ? OR e.barcode LIKE ? OR e.checkedOutBy LIKE ? OR e.checkedInBy LIKE ? ) ";
			params.add("%"+search+"%");
			params.add("%"+search+"%");
			params.add("%"+search+"%");
			params.add("%"+search+"%");
		}
		sql+=") as extrChkoutLst ";
		int count=0;
		try{
			Collection col=super.select(sql, HashMap.class, params.toArray(), 0, -1);
			if(col!=null && col.size() > 0){
				HashMap map=(HashMap)col.iterator().next();
				count = (Integer)map.get("total");
				
			}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return count; 
	}
	
	public Assignment selectAssignmentCheckOutDetails(String requestId) throws DaoException{
		ArrayList params=new ArrayList();
		String sql="SELECT TOP 1 r.requestId, r.title, r.createdBy " +
				"FROM fms_eng_request r " +
				"WHERE 1=1 ";

		sql += " AND (r.requestId = ?) ";
		params.add(requestId);
		
		Collection col=new ArrayList();
		Assignment asg = null;
		try{
			col= super.select(sql, Assignment.class, params.toArray(), 0, -1);
			if (col != null && col.size() > 0) {
				asg = (Assignment) col.iterator().next();
			}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return asg;
	}
	
	public Collection getRequestListingByTime(String search, String department,Date fromDate, Date toDate, 
			String sort, boolean desc, boolean isToday,int start, int rows) throws DaoException {
		// sort clause
        String sortClause = "ORDER BY ";
		if (sort != null) {
			if (sort.equals("requiredFrom")) {
				//sort = "r.requiredFrom";
				sort = "requiredFrom";
			} else if (sort.equals("requiredTo")) {
				//sort = "r.requiredTo";
				sort = "requiredTo";
			}
			
			sortClause += sort + " " + (desc ? "DESC " : "");
		} else {
			sortClause += "fromTime ";
		}
		
		Map map = sqlRequestListingByTime(search, department, fromDate, toDate, isToday);
		String sql = (String) map.get("selectClause") + (String) map.get("fromClause") + (String) map.get("whereClause") + (String) map.get("groupByClause") + sortClause;
		Object[] args = ((Collection) map.get("args")).toArray();

		try {
			return super.select(sql, HashMap.class, args, start, rows);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		return new ArrayList();
	}
	
	protected Map sqlRequestListingByTime(String search, String department, Date fromDate, Date toDate, boolean isToday) {
		ArrayList args = new ArrayList();
		String selectClause = "SELECT requestId,requestTitle,requestor, department, requiredFrom, requiredTo, MIN( fromTime ) AS fromTime " +
				"FROM (" +
					"SELECT DISTINCT r.requestId, r.title as requestTitle, " +
					"(u.firstName + ' ' + u.lastName) AS requestor, d.name as department, r.requiredFrom, r.requiredTo,e.fromTime ";
		String countClause = "SELECT COUNT(DISTINCT r.requestId) AS total ";
		
		String fromClause =
			"FROM fms_eng_request r " +
			"LEFT JOIN security_user u ON (r.createdBy=u.username) " +
			"LEFT JOIN fms_department d ON (u.department=d.id) " +
			"LEFT JOIN fms_eng_assignment a ON (a.requestId=r.requestId) ";
		
		String whereClause = 
			"WHERE 1 = 1 " +
			"AND a.assignmentType = 'F' " +
			"AND (a.serviceType = '1' OR  a.serviceType = '6') ";

		if (search != null && !"".equals(search)) {
			whereClause += " AND (u.firstName like ? OR u.lastName like ? OR r.title like ? OR r.requestId like ?) ";
			args.add("%" + search + "%");
			args.add("%" + search + "%");
			args.add("%" + search + "%");
			args.add("%" + search + "%");
		}
		
		if (department != null && !"-1".equals(department)) {
			whereClause += "AND d.id = ? ";
			args.add(department);
		}
		
		// checking on different table
		if (isToday) {
			UnitHeadDao dao1 = (UnitHeadDao)Application.getInstance().getModule(UnitHeadModule.class).getDao();
			int settingValue = dao1.selectAssignmentSettingValue();
			Date dateMax = new Date();
			Date dateMin = Calendar.getInstance().getTime();
			dateMax.setDate(dateMax.getDate()+settingValue-1); 
			
			fromClause += "LEFT JOIN fms_eng_assignment_equipment e ON (a.groupId=e.groupId) ";
			whereClause += "AND (r.requiredFrom <= ? AND r.requiredTo >= ?) " +
					")a ";
			args.add(dateMax);
			args.add(dateMin);
		} else if (fromDate != null && toDate != null) {
			fromClause += "LEFT JOIN fms_eng_assignment_equipment e ON (a.groupId=e.groupId) ";
			whereClause += "AND (r.requiredFrom <= ? AND r.requiredTo >= ?) " +
					")a ";
			args.add(toDate);
			args.add(fromDate);
		}
		
		String groupByClause = 
			"GROUP BY requestId, requestTitle,requestor, department, requiredFrom, requiredTo  ";

		HashMap hm = new HashMap();
		hm.put("selectClause", selectClause);
		hm.put("countClause", countClause);
		hm.put("fromClause", fromClause);
		hm.put("whereClause", whereClause);
		hm.put("groupByClause", groupByClause);
		hm.put("args", args);
		return hm;
	}

	protected void updateNotUtilizedItem(String requestId)  throws DaoException{	
		String sql=	"UPDATE fms_eng_assignment_equipment SET utilized = ? " +
					"WHERE fms_eng_assignment_equipment.assignmentId IN (SELECT assignmentId from fms_eng_assignment a	"+ 
					"INNER JOIN fms_eng_request r on a.requestId=r.requestId WHERE a.requestId = ?) ";
					
		super.update(sql,new String[]{EngineeringModule.NOT_UTILIZED_ITEM, requestId});
	}
	
	protected void updateReasonNotUtilizedItem(String requestId, String reason)  throws DaoException{
		String sql=	"UPDATE fms_eng_request SET reason = ? " +
					"WHERE requestId = ? ";
					
		super.update(sql,new String[]{reason, requestId});
	}
	
	protected boolean isItemNotUtilized(String requestId){
		try {
			String sql = 
				"SELECT utilized " +
				"FROM fms_eng_assignment_equipment " +
				"WHERE fms_eng_assignment_equipment.assignmentId IN (SELECT assignmentId from fms_eng_assignment a	"+ 
				"INNER JOIN fms_eng_request r on a.requestId=r.requestId WHERE a.requestId = ?) ";
			
			Collection col = super.select(sql, HashMap.class, new String[] {requestId}, 0, -1);
			
			if(col!=null && col.size()>0){
				for (Iterator iterate = col.iterator(); iterate.hasNext(); ) {
					HashMap map = (HashMap) iterate.next();
					String utilized = (String) map.get("utilized");
					if(utilized!=null && utilized.equals("0")){
						return true;
					}else{
						return false;
					}
				}
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		
		return false;
	}
	
	public Date selectAssignmentCheckOutDetailsDate(String requestId,String requiredType) throws DaoException{
		ArrayList params=new ArrayList();
		String dateFilter="";
		if(requiredType.equals("FROM")){
			dateFilter="requiredFrom";
		}else if(requiredType.equals("TO")){
			dateFilter="requiredTo";
		}
		
		String sql="  select TOP 1 "+dateFilter+" from ((Select distinct e.barcode AS barcode, f.name as facilityName," +
				" e.requiredFrom,requiredTo, " +
				" e.checkedOutDate, e.checkedOutBy, e.checkedInDate, e.checkedInBy " +
				" FROM fms_eng_assignment a RIGHT JOIN fms_eng_assignment_equipment e on a.assignmentId=e.assignmentId " +
				" LEFT JOIN fms_facility_item i ON e.barcode = i.barcode  " +
				" LEFT JOIN fms_facility f ON (i.facility_id = f.id)" +
				" WHERE 1=1 AND e.barcode IS NOT NULL AND a.requestId=? ";
				params.add(requestId);
		
		sql+=") union all ";
		
		sql+=" (select distinct e.barcode AS barcode, f.name as facilityName, " +
				" e.requiredFrom,requiredTo, " +
				" e.checkedOutDate, e.checkedOutBy, e.checkedInDate, e.checkedInBy " +
				" from fms_eng_assignment a " +
				" LEFT JOIN fms_eng_assignment_equipment e on a.groupId=e.groupId " +
				" LEFT JOIN fms_eng_rate_card_category c on c.id=e.rateCardCategoryId " +
				" LEFT JOIN fms_facility_item i ON e.barcode = i.barcode  " +
				" LEFT JOIN fms_facility_location loc ON i.location_id = loc.setup_id  " +
				" LEFT JOIN fms_facility f ON (i.facility_id = f.id)  " +
				" where 1=1 " +
				" AND a.requestId = ? AND e.assignmentId = '-' " +
				" AND (a.serviceType=? or a.serviceType=?) ";
		params.add(requestId);
		params.add(ServiceDetailsForm.SERVICE_SCPMCP);
		params.add(ServiceDetailsForm.SERVICE_OTHER);
		
		sql+=" )) as yyy ";
		sql += " order by "+dateFilter;
		
		if(requiredType.equals("FROM")){
			sql+=" ASC ";
		}else if(requiredType.equals("TO")){
			sql+=" DESC ";
		}
		
		Collection col=new ArrayList();
		Date dateRequired = null;
		try{
			col= super.select(sql, HashMap.class, params.toArray(), 0, -1);
			if(col!=null && col.size() > 0){
				HashMap map=(HashMap)col.iterator().next();
				dateRequired = (Date)map.get(dateFilter);
				
			}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		
		return dateRequired;
	}

	public boolean isExistRequestId(String requestId) throws DaoException {
		
		String sql= " SELECT count(*) as total FROM fms_eng_request WHERE requestId = ? " ;
				
		Collection col = super.select(sql, HashMap.class, new String[]{requestId}, 0, 1);
		if (col.size() >= 1) {
			HashMap map = (HashMap) col.iterator().next();
			int total = Integer.parseInt(map.get("total").toString());
			
			if(total > 0) return true;
			else return false;
		}
		return false;
	}

	public Collection getItemNotCheckin(String requestId) throws DaoException {
    	
    	String sql = " SELECT i.barcode,f.name as itemName  " +
    			" FROM fms_eng_assignment_equipment e " +
    			" INNER JOIN fms_facility_item i ON (e.barcode = i.barcode ) " +
    			" INNER JOIN fms_facility f ON (f.id=i.facility_id) " +
    			" INNER JOIN fms_eng_assignment a ON (e.assignmentId = a.assignmentId )" +
    			" WHERE a.requestId= ? AND i.status = ? " ;    			
    	
    	
    	Collection col = super.select(sql, DefaultDataObject.class, new String[]{requestId,FacilityModule.ITEM_STATUS_CHECKED_OUT}, 0, -1);
    	return col;
	}

	public DefaultDataObject getAssignmentInfo(String requestId) throws DaoException {
		String sql = " SELECT r.title as requestTitle, (u.firstName +' ' +u.lastName) as requestor, r.description as remarks " +
		" FROM  fms_eng_request r " +
		" LEFT JOIN security_user u ON (u.username = r.createdBy )" +
		" WHERE r.requestId = ? " ;    	
	
		Collection col = super.select(sql, DefaultDataObject.class, new String[]{requestId}, 0, 1);
		if (col.size() == 1){
			return (DefaultDataObject) col.iterator().next();
		}				
	return new DefaultDataObject();
	}
	
}
