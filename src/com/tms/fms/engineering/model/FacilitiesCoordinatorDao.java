package com.tms.fms.engineering.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.tms.cms.core.model.InvalidKeyException;
import com.tms.fms.engineering.model.FacilityObject;
import com.tms.fms.transport.model.SetupObject;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.util.Log;

public class FacilitiesCoordinatorDao extends DataSourceDao {
	
	public void init() throws DaoException{
		try {
			super.update("CREATE TABLE fms_eng_request_outsource ( " +
				"id varchar(255) NOT NULL PRIMARY KEY, requestId varchar(255) NOT NULL, " +
				"outsourceType char(2) NULL, companyId varchar(255) NULL, " +
				"fileId varchar(255) NULL, estimatedCost float(53) NULL, " +
				"actualCost float(53) NULL,	remarks text NULL, " +
				"createdBy varchar(255) NULL, createdOn datetime NULL, " +
				"modifiedBy varchar(255) NULL, modifiedOn datetime NULL) ", null);
		} catch (Exception e) {
			
		}
		
		try {
			super.update("ALTER TABLE fms_eng_request_outsource ALTER COLUMN outsourceType char(1) NULL", null);
		} catch (Exception e) {
			
		}
		
		try {
			super.update("ALTER TABLE fms_eng_request_outsource ALTER COLUMN estimatedCost numeric(18,2) NULL", null);
		} catch (Exception e) {			
		}
		try {
			super.update("ALTER TABLE fms_eng_request_outsource ALTER COLUMN actualCost numeric(18,2) NULL", null);
		} catch (Exception e) {			
		}
		
		try {
			super.update("CREATE TABLE fms_attachment (	id varchar(255) NOT NULL PRIMARY KEY, " +
					"fileName varchar(255) NULL, path varchar(255) NULL, " +
					"referenceId varchar(255) NULL, createdBy varchar(255) NULL, " +
					"createdOn datetime NULL, modifiedBy varchar(255) NULL, " +
					"modifiedOn datetime NULL) ", null);
		} catch (Exception e){
			
		}
		
		try {
			super.update(
					"CREATE TABLE fms_facility_booking (" +
						"id varchar(255) NOT NULL, " +
						"facilityId varchar(255) NULL, " +
						"requestId varchar(255) NULL, " +
						"bookFrom datetime NULL," +
						"timeFrom varchar(4) NULL, " +
						"bookTo datetime NULL," +
						"timeTo varchar(4) NULL, " +
						"createdBy varchar(255) NULL," +
						"createdOn datetime NULL, " +
						"modifiedBy varchar(255) NULL, " +
						"modifiedOn datetime NULL, " +
						"quantity int NULL, " +
						"bookingType CHAR(1) NULL" +
					")", null);
		} catch (Exception e){			
		}
		
		try {
			super.update("ALTER TABLE fms_facility_booking ADD bookingType CHAR(1) NULL", null);
		} catch (Exception e){
		}
		
		try {
			super.update("ALTER TABLE fms_facility_booking ALTER COLUMN timeFrom VARCHAR(4) NULL", null);
		} catch (Exception e){
		}
		
		try {
			super.update("ALTER TABLE fms_facility_booking ALTER COLUMN timeTo VARCHAR(4) NULL", null);
		} catch (Exception e){
		}
		
		// create primary key for fms_facility_booking
		try {
			super.update("ALTER TABLE fms_facility_booking ADD CONSTRAINT PK_fms_facility_booking PRIMARY KEY CLUSTERED (id)", null);
		} catch (Exception e) {
		}
		
		// create index for fms_facility_booking(bookFrom, bookTo)
		try {
			super.update("CREATE INDEX bookFromTo ON fms_facility_booking(bookFrom, bookTo)", null);
		} catch (Exception e) {
		}
		
		// create index for fms_facility_booking(requestId)
		try {
			super.update("CREATE INDEX requestId ON fms_facility_booking(requestId)", null);
		} catch (Exception e) {
		}
		
		// create index for fms_facility_booking(facilityId, bookFrom, bookTo)
		try {
			super.update("CREATE INDEX facilityId_bookFromTo ON fms_facility_booking (facilityId, bookFrom, bookTo)", null);
		} catch (Exception e) {
		}
	}
	
	public Collection selectIncomingRequestFC(String search, String departmentId, String userId, String sort,boolean desc,int start,int rows) throws DaoException{
		String sql="SELECT r.requestId, r.title, r.requestType, r.clientName, r.programType, " +
				" r.program, r.description, r.requiredFrom, r.requiredTo, " +
				" r.createdBy, r.createdOn, r.modifiedBy, r.modifiedOn, r.status, r.state, " +
				" r.submittedDate," +
				" d.name AS department " +
				" FROM fms_eng_request r INNER JOIN security_user u ON (r.createdBy = u.username) " +
				" INNER JOIN fms_department d ON (u.department = d.id) " +
				" WHERE 1=1 " +
				" AND r.status='" + EngineeringModule.FC_ASSIGNED_STATUS +"' " +
				" AND r.fcId='" + userId + "' ";
		
		if(search!=null && !"".equals(search)){
			sql+=" AND ( r.requestId LIKE '%" + search + "%' OR r.title like '%"+search+"%' OR r.description like '%"+search+"%' ) ";
		}
		if(departmentId!=null && !"".equals(departmentId) && !"-1".equals(departmentId)){
			sql+=" AND ( d.id = '"+departmentId+"' ) ";
		}
		if(sort!=null && !"".equals(sort)){
			if (sort.equals("department")) {
				sort = "d." + sort;
			} else if (sort.equals("createdUserName")) {
				sort = "u.firstName" ;
			} else {
				sort = "r." + sort;
			}
			
			sql+=" order by "+sort+" ";
		}else{
			sql+=" order by r.modifiedOn DESC ";
		}
		if(desc){
			sql+=" DESC ";
		}
		
		return super.select(sql, EngineeringRequest.class, null, start, rows);
	}
	
	public Collection selectIncomingRequestFCList(Date requiredTo, String search, Date requiredFrom, String departmentId, String userId, String sort,boolean desc,int start,int rows) throws DaoException{
		ArrayList args = new ArrayList();
		String sql="SELECT r.requestId, r.title, r.requestType, r.clientName, r.programType, " +
				" r.program, r.description, r.requiredFrom, r.requiredTo, " +
				" r.createdBy, r.createdOn, r.modifiedBy, r.modifiedOn, r.status, r.state, " +
				" r.submittedDate," +
				" d.name AS department " +
				" FROM fms_eng_request r INNER JOIN security_user u ON (r.createdBy = u.username) " +
				" INNER JOIN fms_department d ON (u.department = d.id) " +
				" WHERE 1=1 " +
				" AND r.status='" + EngineeringModule.FC_ASSIGNED_STATUS +"' " +
				" AND r.fcId='" + userId + "' ";
		
		if(search!=null && !"".equals(search)){
			sql+=" AND ( r.requestId LIKE '%" + search + "%' OR r.title like '%"+search+"%' OR r.description like '%"+search+"%' ) ";
		}
		if(departmentId!=null && !"".equals(departmentId) && !"-1".equals(departmentId)){
			sql+=" AND ( d.id = '"+departmentId+"' ) ";
		}
		
		if(requiredFrom != null && requiredTo != null){
			sql += " AND ((r.requiredFrom >= ? AND r.requiredFrom <= ?) OR (r.requiredFrom <= ? AND r.requiredTo >= ?))";
			args.add(requiredFrom);
			args.add(requiredTo);
			args.add(requiredFrom);
			args.add(requiredFrom);
		}
		if(sort!=null && !"".equals(sort)) {
			if (sort.equals("department")) {
				sort = "d." + sort;
			} else if (sort.equals("title")) {
				sort = "r.title";
			} else if (sort.equals("createdUserName")) {
				sort = "u.firstName" ;
			} else if (sort.equals("requiredDateFrom")) {
					sort = "r.requiredFrom";
			} else if (sort.equals("title")) {
				sort = "requestId";
			} else if (sort.equals("state")) {
				sort = "r.state";
			}
			
			sql+=" order by "+sort+" ";
		}else{
			sql+=" order by r.modifiedOn DESC ";
		}
		if(desc){
			sql+=" DESC ";
		}
		
		return super.select(sql, EngineeringRequest.class, args.toArray(), start, rows);
	}
	
	public Collection selectIncomingRequestFC(String search, String departmentId, String userId, String status, String sort,boolean desc,int start,int rows) throws DaoException{
		String sql="SELECT r.requestId, r.title, r.requestType, r.clientName, r.programType, " +
				" r.program, r.description, r.requiredFrom, r.requiredTo, " +
				" r.createdBy, r.createdOn, r.modifiedBy, r.modifiedOn, r.status, r.state, " +
				" r.submittedDate," +
				" d.name AS department " +
				" FROM fms_eng_request r INNER JOIN security_user u ON (r.createdBy = u.username) " +
				" INNER JOIN fms_department d ON (u.department = d.id) " +
				" WHERE 1=1 "; 
		if (status != null && !"".equals(status) && !"-1".equals(status)){
			sql += " AND r.status='" + status +"'";
		} else {
			//sql += " AND r.status='" + EngineeringModule.FC_ASSIGNED_STATUS +"'";
			sql += " AND ( " +
					"r.status <> '" + EngineeringModule.DRAFT_STATUS + "' AND " +
					"r.status <> '" + EngineeringModule.PENDING_STATUS + "' AND " +
					"r.status <> '" + EngineeringModule.PROCESS_STATUS + "' AND " +
					"r.status <> '" + EngineeringModule.FC_ASSIGNED_STATUS + "' " +
					") ";
		}
		
		if (userId != null && !"".equals(userId)){
			sql += " AND r.fcId='" + userId +"'";
		} 
		
		if(search!=null && !"".equals(search)){
			sql+=" AND ( r.requestId LIKE '%" + search + "%' OR r.title like '%"+search+"%' OR r.description like '%"+search+"%' ) ";
		}
		if(departmentId!=null && !"".equals(departmentId) && !"-1".equals(departmentId)){
			sql+=" AND ( d.id = '"+departmentId+"' ) ";
		}
		if(sort!=null && !"".equals(sort)){
			if (sort.equals("department")) {
				sort = "d." + sort;
			} else {
				if (sort.equals("createdUserName")){
					sort = "r.createdBy";
				} else {
					sort = "r." + sort;
				}
			}
			
			sql+=" order by "+sort+" ";
		}else{
			sql+=" order by r.createdOn DESC ";
		}
		if(desc){
			sql+=" DESC ";
		}
				
		return super.select(sql, EngineeringRequest.class, null, start, rows);
	}
	
	public Collection selectIncomingRequestFCTable(String search, Date requiredFrom, String departmentId, String userId, String status, String sort,boolean desc,int start,int rows) throws DaoException{
		ArrayList args = new ArrayList();
		String sql="SELECT r.requestId, r.title, r.requestType, r.clientName, r.programType, " +
				" r.program, r.description, r.requiredFrom, r.requiredTo, " +
				" r.createdBy, r.createdOn, r.modifiedBy, r.modifiedOn, r.status, r.state, " +
				" r.submittedDate," +
				" d.name AS department " +
				" FROM fms_eng_request r INNER JOIN security_user u ON (r.createdBy = u.username) " +
				" INNER JOIN fms_department d ON (u.department = d.id) " +
				" WHERE 1=1 "; 
		if (status != null && !"".equals(status) && !"-1".equals(status)){
			sql += " AND r.status='" + status +"'";
		} else {
			//sql += " AND r.status='" + EngineeringModule.FC_ASSIGNED_STATUS +"'";
			sql += " AND ( " +
					"r.status <> '" + EngineeringModule.DRAFT_STATUS + "' AND " +
					"r.status <> '" + EngineeringModule.PENDING_STATUS + "' AND " +
					"r.status <> '" + EngineeringModule.PROCESS_STATUS + "' AND " +
					"r.status <> '" + EngineeringModule.FC_ASSIGNED_STATUS + "' " +
					") ";
		}
		if(requiredFrom != null){
			sql += "AND r.requiredFrom >= ? ";
			args.add(requiredFrom);
		}
		if (userId != null && !"".equals(userId)){
			sql += " AND r.fcId='" + userId +"'";
		} 
		
		if(search!=null && !"".equals(search)){
			sql+=" AND ( r.requestId LIKE '%" + search + "%' OR r.title like '%"+search+"%' OR r.description like '%"+search+"%' ) ";
		}
		if(departmentId!=null && !"".equals(departmentId) && !"-1".equals(departmentId)){
			sql+=" AND ( d.id = '"+departmentId+"' ) ";
		}
		if(sort!=null && !"".equals(sort)){
			if (sort.equals("department")) {
				sort = "d." + sort;
			} else if (sort.equals("createdUserName")) {
					sort = "r.createdBy";
			} else if (sort.equals("requiredDateFrom")) {
					sort = "requiredFrom";				
			} else if (sort.equals("title")) {
					sort = "r.title";
			} else if (sort.equals("status")) {
					sort = "r.status";
			}
			
			sql+=" order by "+sort+" ";
		}else{
			sql+=" order by r.status, r.createdOn DESC ";
		}
		if(desc){
			sql+=" DESC ";
		}
				
		return super.select(sql, EngineeringRequest.class, args.toArray(), start, rows);
	}
	
	public Collection selectAllRequestFC(String search, String departmentId, String status, Date requiredFrom, Date requiredTo, String sort,boolean desc,int start,int rows) throws DaoException{
		ArrayList params=new ArrayList();
		String sql="Select count(fa.rateCardCategoryId) as qty,a.assignmentId,a.code, r.requestId, r.title,rc.name as rateCard, " +
				" (u.firstName + ' ' + u.lastName) AS firstName,d.name as department," +
				" fa.fromTime,fa.toTime, fa.requiredFrom, fa.requiredTo, fa.rateCardId, fa.rateCardCategoryId from fms_eng_assignment a INNER JOIN " +
				" fms_eng_request r on r.requestId=a.requestId INNER JOIN security_user u on r.createdBy=u.username " +
				" INNER JOIN fms_department d on u.department=d.id INNER JOIN fms_eng_assignment_equipment fa on a.assignmentId=fa.assignmentId " +
				" INNER JOIN fms_rate_card rc  ON rc.id=fa.rateCardId " +
				" WHERE 1=1 AND a.assignmentType='F' ";
				//" AND ? BETWEEN fa.requiredFrom AND fa.requiredTo  ";
		

		if(search!=null && !"".equals(search)){
			sql+=" AND (u.firstName like '%"+search+"%' OR u.lastName like '%"+search+"%' OR r.title like '%"+search+"%' ) ";
		}
		
		if(departmentId!=null && !"-1".equals(departmentId)){
			sql+=" AND ( d.id = '"+departmentId+"') ";
		}
		
		if (status != null && !"".equals(status) && !"-1".equals(status)){
			sql += " AND fa.status='" + status +"'";
		} else {
			//sql += " AND r.status='" + EngineeringModule.FC_ASSIGNED_STATUS +"'";
			sql += " AND ( " +
					"fa.status <> '" + EngineeringModule.DRAFT_STATUS + "' AND " +
					"fa.status <> '" + EngineeringModule.PENDING_STATUS + "' AND " +
					"fa.status <> '" + EngineeringModule.PROCESS_STATUS + "' AND " +
					"fa.status <> '" + EngineeringModule.FC_ASSIGNED_STATUS + "' " +
					") ";
		}
		
		if(requiredFrom != null){
			sql += "AND fa.requiredFrom >= ? ";
			params.add(requiredFrom);
		}
		
		if(requiredTo != null){
			sql += "AND fa.requiredTo <= ? ";
			params.add(requiredTo);
		}
		
		sql+=" GROUP BY a.assignmentId, a.code, r.title, r.requestId, rc.name ,u.firstName, u.lastName, d.name, fa.fromTime, fa.toTime, fa.requiredFrom, fa.requiredTo, fa.rateCardId, fa.rateCardCategoryId ";
		
		if(sort!=null && !"".equals(sort)){			
			sql+=" order by "+sort+" ";
		}
		
		if(desc){
			sql+=" DESC ";
		}
		
		return super.select(sql, HashMap.class, params.toArray(), start, rows);
	}
	
	public Collection selectAllRequestFCManpower(String search, String departmentId, String status, Date requiredFrom, Date requiredTo, String sort,boolean desc,int start,int rows) throws DaoException{
		ArrayList params = new ArrayList();
		String sql="Select count(fa.competencyId) as qty,a.assignmentId,a.code, r.requestId, r.title,rc.name as rateCard, " +
				" (u.firstName + ' ' + u.lastName) AS firstName,d.name as department," +
				" fa.fromTime,fa.toTime, fa.requiredFrom AS fromDateTime, fa.requiredTo, fa.rateCardId, fa.competencyId, fa.status " +
				" from fms_eng_assignment a INNER JOIN " +
				" fms_eng_request r on r.requestId=a.requestId INNER JOIN security_user u on r.createdBy=u.username " +
				" INNER JOIN fms_department d on u.department=d.id " +
				" INNER JOIN fms_eng_assignment_manpower fa on a.assignmentId=fa.assignmentId " +
				" INNER JOIN fms_rate_card rc  ON rc.id=fa.rateCardId " +
				" WHERE 1=1 AND a.assignmentType='M' ";
				//" AND ? BETWEEN fa.requiredFrom AND fa.requiredTo  ";
		

		if(search!=null && !"".equals(search)){
			sql+=" AND (u.firstName like '%"+search+"%' OR u.lastName like '%"+search+"%' OR r.title like '%"+search+"%' ) ";
		}
		
		if(departmentId!=null && !"-1".equals(departmentId)){
			sql+=" AND ( d.id = '"+departmentId+"') ";
		}
		
		if (status != null && !"".equals(status) && !"-1".equals(status)){
			sql += " AND fa.status='" + status +"'";
		} else {
			//sql += " AND r.status='" + EngineeringModule.FC_ASSIGNED_STATUS +"'";
			sql += " AND ( " +
					"fa.status <> '" + EngineeringModule.DRAFT_STATUS + "' AND " +
					"fa.status <> '" + EngineeringModule.PENDING_STATUS + "' AND " +
					"fa.status <> '" + EngineeringModule.PROCESS_STATUS + "' AND " +
					"fa.status <> '" + EngineeringModule.FC_ASSIGNED_STATUS + "' " +
					") ";
		}
		
		if(requiredFrom != null){
			sql += "AND fa.requiredFrom >= ? ";
			params.add(requiredFrom);
		}
		
		if(requiredTo != null){
			sql += "AND fa.requiredTo <= ? ";
			params.add(requiredTo);
		}
		
		sql+=" GROUP BY a.assignmentId, a.code, r.title, r.requestId, rc.name ,u.firstName, u.lastName, d.name, fa.status, fa.fromTime,fa.toTime, fa.requiredFrom, fa.requiredTo, fa.rateCardId,fa.competencyId ";
		
		if(sort!=null && !"".equals(sort)){			
			sql+=" order by "+sort+" ";
		}
		
		if(desc){
			sql+=" DESC ";
		}
		
		return super.select(sql, HashMap.class, params.toArray(), start, rows);
	}
	
	public Collection selectAllRequestFCForBatch(String search, String departmentId, String programId, Date fromDate, Date toDate) throws DaoException{
		ArrayList params=new ArrayList();
		String sql="Select a.assignmentId,a.code, r.requestId, r.title,rc.name as rateCard, " +
				" (u.firstName + ' ' + u.lastName) AS firstName,d.name as department," +
				" fa.fromTime,fa.toTime,fa.rateCardId, fa.competencyId, fa.status, fa.completionDate," +
				" fa.requiredFrom, fa.requiredTo, fa.userId " +
				" FROM fms_eng_assignment a INNER JOIN fms_eng_request r ON r.requestId=a.requestId " +
				" INNER JOIN security_user u ON r.createdBy=u.username " +
				" INNER JOIN fms_department d ON u.department=d.id " +
				" INNER JOIN fms_eng_assignment_manpower fa ON a.assignmentId=fa.assignmentId " +
				" INNER JOIN fms_rate_card rc  ON rc.id=fa.rateCardId " +
				" WHERE 1=1 AND a.assignmentType= '" + EngineeringModule.ASSIGNMENT_TYPE_MANPOWER + "' ";
				//" AND ? BETWEEN fa.requiredFrom AND fa.requiredTo  ";

		if(fromDate != null){
			sql += " AND ((fa.requiredFrom BETWEEN ? AND ?) " +
					" OR (fa.requiredTo BETWEEN ? AND ?)) ";
			params.add(fromDate);
			params.add(toDate);
			params.add(fromDate);
			params.add(toDate);
		}
		
		if(search!=null && !"".equals(search)){
			sql+=" AND (r.requestId = ?) ";// OR u.firstName like '%"+search+"%' OR u.lastName like '%"+search+"%' OR r.title like '%"+search+"%' ) ";
			params.add(search);
		}
		
		if(departmentId!=null && !"-1".equals(departmentId)){
			sql+=" AND ( d.id = ?) ";
			params.add(departmentId);
		}
		
		if(programId!=null && !"-1".equals(programId)){
			sql+=" AND ( r.program = ?) ";
			params.add(programId);
		}
		
		//sql+=" GROUP BY a.assignmentId, a.code, r.title, r.requestId, rc.name ,u.firstName, u.lastName, d.name, fa.status, fa.fromTime,fa.toTime,fa.rateCardId,fa.competencyId, fa.completionDate ";
		
		return super.select(sql, HashMap.class, params.toArray(), 0, -1);
	}
	
	public int countAllRequestFC(String search, String departmentId) throws DaoException{
		String sql="Select COUNT(*) AS COUNT " +
				" FROM fms_eng_assignment a INNER JOIN " +
				" fms_eng_request r on r.requestId=a.requestId INNER JOIN security_user u on r.createdBy=u.username " +
				" INNER JOIN fms_department d on u.department=d.id INNER JOIN fms_eng_assignment_equipment fa on a.assignmentId=fa.assignmentId " +
				" INNER JOIN fms_rate_card rc  ON rc.id=fa.rateCardId " +
				" WHERE 1=1 AND a.assignmentType='F' ";
				//" AND ? BETWEEN fa.requiredFrom AND fa.requiredTo  ";

		if(search!=null && !"".equals(search)){
			sql+=" AND (u.firstName like '%"+search+"%' OR u.lastName like '%"+search+"%' OR r.title like '%"+search+"%' ) ";
		}
		
		if(departmentId!=null && !"-1".equals(departmentId)){
			sql+=" AND ( d.id = '"+departmentId+"') ";
		}
		
		sql+=" GROUP BY a.assignmentId, a.code, r.title, rc.name ,u.firstName, d.name, fa.fromTime,fa.toTime,fa.rateCardId,fa.rateCardCategoryId ";
		
		int count=0;
		try{
			Collection col=super.select(sql, HashMap.class, null, 0, -1);
			if(col!=null){
				//HashMap map=(HashMap)col.iterator().next();
				//count=(Integer)map.get("COUNT");
				count = col.size();
			}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return count; 
	}
	
	public Collection selectOutsource(String search, String outsourceType, String requestId, String sort, boolean desc, int start, int rows) throws DaoException {
		String sql = "SELECT out.id AS outsourceId, " +
				"out.outsourceType AS outsourceType, " +
				//"req.clientName AS clientName, " +
				"out.estimatedCost AS estimatedCost, " +
				"out.actualCost AS actualCost," +
				"out.remarks AS remarks, " +
				"tranOut.name AS clientName " +
				"FROM fms_eng_request req INNER JOIN fms_eng_request_outsource out ON (req.requestId = out.requestId) " +
				"LEFT JOIN fms_tran_outsourcepanel tranOut ON (out.companyId = tranOut.setup_id) " +
				"WHERE 1=1 ";
		
		if (search!=null && !"".equals(search)){
			//sql+=" AND ( req.title like '%"+search+"%' OR r.description like '%"+search+"%' ) ";
		}
		
		if (outsourceType!=null && !"".equals(outsourceType) && !"-1".equals(outsourceType)){
			sql+=" AND ( out.outsourceType = '"+outsourceType+"' ) ";
		}
		
		if (requestId!=null && !"".equals(requestId) && !"-1".equals(requestId)){
			sql+=" AND ( out.requestId = '"+ requestId +"' ) ";
		}
		
		if (sort!=null && !"".equals(sort)){
//			if (sort.equals("department")) {
//				sort = "out." + sort;
//			} else {
				sort = "out." + sort;
//			}
			
			sql+=" order by "+sort+" ";
		} else {
			sql+=" order by out.id DESC ";
		}
		
		if (desc){
			sql+=" DESC ";
		}
		return super.select(sql, EngineeringRequest.class, null, start, rows);
	}
	
	public EngineeringRequest selectOutsourceById(String requestId) throws DaoException {
		String sql = "SELECT " +
				"req.requestId AS requestId, " +
				"req.title AS title, " +
				"pro.programName AS programName, " +
				"req.clientName AS clientName," +
				"req.createdBy AS createdBy, " +
				"req.submittedDate AS submittedDate," +
				"requestType,requiredTo " +
				"FROM fms_eng_request req " +
				"LEFT JOIN fms_prog_setup pro ON(req.program = pro.programId) " +
				"WHERE 1=1 ";
		if (requestId!=null && !"".equals(requestId)){
			sql += " AND req.requestId = '" + requestId + "'";
		}		
		Collection col=super.select(sql, EngineeringRequest.class, null, 0, 1);
		
		if(col!=null && col.size()>0){
			return (EngineeringRequest)col.iterator().next();
		}
		return null;	
	}
	
	public EngineeringRequest selectOutsourceById(String requestId, String outsourceId) throws DaoException {
		
		String sql = "SELECT " +
				"req.title AS title, " +
				"pro.programName AS programName, " +
				"req.clientName AS clientName," +
				"req.createdBy AS createdBy, " +
				"req.submittedDate AS submittedDate, " +
				"out.id AS outsourceId, " +
				"out.outsourceType AS outsourceType, " +
				"out.estimatedCost AS estimatedCost, " +
				"out.actualCost AS actualCost," +
				"out.remarks AS description," +
				"out.companyId AS  companyId " +
				"FROM fms_eng_request req INNER JOIN fms_eng_request_outsource out ON (req.requestId = out.requestId) " +
				"LEFT JOIN fms_prog_setup pro ON(req.program = pro.programId) " +
				"WHERE 1=1 ";
		if (requestId!=null && !"".equals(requestId)){
			sql += " AND req.requestId = '" + requestId + "'";
		}	
		if (outsourceId!=null && !"".equals(outsourceId)){
			sql += "AND out.id = '"+ outsourceId + "'";
		}
		Collection col=super.select(sql, EngineeringRequest.class, null , 0, 1);
		
		if(col!=null && col.size()>0){
			return (EngineeringRequest)col.iterator().next();
		}
		return null;	
	}
	
	protected Collection selectRequestServices(String requestId) throws DaoException{
		String sql="Select s.serviceId,s.title,rs.requestId,rs.feedType from fms_eng_services s" +
				" Inner join fms_eng_request_services rs on s.serviceId=rs.serviceId where rs.requestId=? order By s.serviceId";
		return super.select(sql, Service.class, new String[]{requestId}, 0, -1);
	}
	
	protected Collection selectRequestServices(String requestId, String serviceId) throws DaoException{
		String sql="Select s.serviceId,s.title,rs.requestId,rs.feedType from fms_eng_services s" +
				" Inner join fms_eng_request_services rs on s.serviceId=rs.serviceId where rs.requestId=? AND rs.serviceId = ? order By s.serviceId";
		return super.select(sql, Service.class, new String[]{requestId, serviceId}, 0, -1);
	}
	
	public Collection selectOutsourceCompany() throws DaoException {
		String sql = "SELECT * FROM fms_tran_outsourcepanel WHERE type='E' AND status = '1' ";
		return super.select(sql, SetupObject.class, null, 0, -1);
	}
	
	public Collection selectFiles(String outsourceId) throws DaoException {
		String sql = "SELECT id AS fileId, fileName AS fileName, path AS filePath FROM fms_attachment WHERE referenceId=?";
		return super.select(sql, EngineeringRequest.class, new String[] {outsourceId}, 0, -1);
	}
	
	public EngineeringRequest selectFile(String fileId) throws DaoException {
		String sql = "SELECT id AS fileId, fileName AS fileName, path AS filePath FROM fms_attachment WHERE id=?";
		
		Collection col=super.select(sql, EngineeringRequest.class, new String[] {fileId} , 0, -1);
		
		if(col!=null && col.size()>0){
			return (EngineeringRequest)col.iterator().next();
		}
		return null;	
	}
	
	public int selectRequestCount(String search, String departmentId, String userId) throws DaoException{
		String sql="Select count(*) As COUNT " +
					" FROM fms_eng_request r INNER JOIN security_user u ON (r.createdBy = u.username) " +
					" INNER JOIN fms_department d ON (u.department = d.id) " +
					" WHERE 1=1 " +
					" AND r.status='" + EngineeringModule.FC_ASSIGNED_STATUS +"'" +
					" AND r.fcId='" + userId + "'";
		
		if(search!=null && !"".equals(search)){
			sql+=" AND ( r.requestId LIKE '%" + search + "%' OR r.title like '%"+search+"%' OR r.description like '%"+search+"%' ) ";
		}
		if(departmentId!=null && !"".equals(departmentId) && !"-1".equals(departmentId)){
			sql+=" AND ( d.id = '"+departmentId+"' ) ";
		}
		
		int count=0;
		
		try{
		Collection col= super.select(sql, HashMap.class, null, 0, 1);
		if(col!=null){
			HashMap map=(HashMap)col.iterator().next();
			count=(Integer)map.get("COUNT");
		}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return count;
	}
	
	public int selectRequestCountList(Date requiredTo, String search, Date requiredDate, String departmentId, String userId) throws DaoException{
		String sql="Select count(*) As COUNT " +
					" FROM fms_eng_request r INNER JOIN security_user u ON (r.createdBy = u.username) " +
					" INNER JOIN fms_department d ON (u.department = d.id) " +
					" WHERE 1=1 " +
					" AND r.status='" + EngineeringModule.FC_ASSIGNED_STATUS +"'" +
					" AND r.fcId='" + userId + "'";
		
		if(search!=null && !"".equals(search)){
			sql+=" AND ( r.requestId LIKE '%" + search + "%' OR r.title like '%"+search+"%' OR r.description like '%"+search+"%' ) ";
		}
		if(departmentId!=null && !"".equals(departmentId) && !"-1".equals(departmentId)){
			sql+=" AND ( d.id = '"+departmentId+"' ) ";
		}
		
		ArrayList args = new ArrayList();
		if(requiredDate != null && requiredTo != null){
			sql += " AND ((r.requiredFrom >= ? AND r.requiredFrom <= ?) OR (r.requiredFrom <= ? AND r.requiredTo >= ?))";
			args.add(requiredDate);
			args.add(requiredTo);
			args.add(requiredDate);
			args.add(requiredDate);
		}
		int count=0;
		
		try{
		Collection col= super.select(sql, HashMap.class, args.toArray(), 0, 1);
		if(col!=null){
			HashMap map=(HashMap)col.iterator().next();
			count=(Integer)map.get("COUNT");
		}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return count;
	}
	
	public int selectRequestCount(String search, String departmentId, String status, String userId) throws DaoException{
		String sql="Select count(*) As COUNT " +
					" FROM fms_eng_request r INNER JOIN security_user u ON (r.createdBy = u.username) " +
					" INNER JOIN fms_department d ON (u.department = d.id) " +
					" WHERE 1=1 " ;
		if (status !=null && !"".equals(status) && !"-1".equals(status)){
			sql += " AND r.status='" + status  +"'";
		} else {
			sql += " AND ( " +
					"r.status <> '" + EngineeringModule.DRAFT_STATUS + "' AND " +
					"r.status <> '" + EngineeringModule.PENDING_STATUS + "' AND " +
					"r.status <> '" + EngineeringModule.PROCESS_STATUS + "' AND " +
					"r.status <> '" + EngineeringModule.FC_ASSIGNED_STATUS + "' " +
					") ";
		}
		if (userId != null && !"".equals(userId)){
			sql += " AND r.fcId='" + userId +"'";
		} 
		
		if(search!=null && !"".equals(search)){
			sql+=" AND ( r.requestId LIKE '%" + search + "%' OR r.title like '%"+search+"%' OR r.description like '%"+search+"%' ) ";
		}
		if(departmentId!=null && !"".equals(departmentId) && !"-1".equals(departmentId)){
			sql+=" AND ( d.id = '"+departmentId+"' ) ";
		}
		
		int count=0;
		
		try{
		Collection col= super.select(sql, HashMap.class, null, 0, 1);
		if(col!=null){
			HashMap map=(HashMap)col.iterator().next();
			count=(Integer)map.get("COUNT");
		}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return count;
	}
	
	public int selectRequestFCTableCount(String search, Date requiredFrom, String departmentId, String status, String userId) throws DaoException{
		ArrayList args = new ArrayList();
		String sql="Select count(*) As COUNT " +
					" FROM fms_eng_request r INNER JOIN security_user u ON (r.createdBy = u.username) " +
					" INNER JOIN fms_department d ON (u.department = d.id) " +
					" WHERE 1=1 " ;
		if (status !=null && !"".equals(status) && !"-1".equals(status)){
			sql += " AND r.status='" + status  +"'";
		} else {
			//sql += " AND r.status='" + EngineeringModule.FC_ASSIGNED_STATUS +"'";
			sql += " AND ( " +
					"r.status <> '" + EngineeringModule.DRAFT_STATUS + "' AND " +
					"r.status <> '" + EngineeringModule.PENDING_STATUS + "' AND " +
					"r.status <> '" + EngineeringModule.PROCESS_STATUS + "' AND " +
					"r.status <> '" + EngineeringModule.FC_ASSIGNED_STATUS + "' " +
					") ";
		}
		if(requiredFrom != null){
			sql += "AND r.requiredFrom >= ? ";
			args.add(requiredFrom);
		}
		if (userId != null && !"".equals(userId)){
			sql += " AND r.fcId='" + userId +"'";
		} 
		
		if(search!=null && !"".equals(search)){
			sql+=" AND ( r.requestId LIKE '%" + search + "%' OR r.title like '%"+search+"%' OR r.description like '%"+search+"%' ) ";
		}
		if(departmentId!=null && !"".equals(departmentId) && !"-1".equals(departmentId)){
			sql+=" AND ( d.id = '"+departmentId+"' ) ";
		}
		
		int count=0;
		
		try{
		Collection col= super.select(sql, HashMap.class, args.toArray(), 0, 1);
		if(col!=null){
			HashMap map=(HashMap)col.iterator().next();
			count=(Integer)map.get("COUNT");
		}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return count;
	}
	
	
	public void storeFile(StorageFile sf) throws InvalidKeyException, DaoException {
        // store uploaded file
        if (sf != null) {
            try {
                Application application = Application.getInstance();
                StorageService storage = (StorageService) application.getService(StorageService.class);
                storage.store(sf);
                
            } catch (Exception e) {
                throw new DaoException("Unable to save uploaded file " +
                		sf.getAbsolutePath() + ": " + e.toString());
            }
        }
    }
	
	public void deleteFile(StorageFile sf) throws InvalidKeyException, DaoException {
        // store uploaded file
        if (sf != null) {
            try {
                Application application = Application.getInstance();
                StorageService storage = (StorageService) application.getService(StorageService.class);
                storage.delete(sf);
                
            } catch (Exception e) {
                throw new DaoException("Unable to save delete file " +
                		sf.getAbsolutePath() + ": " + e.toString());
            }
        }
    }
	
	protected void insertOutsource(EngineeringRequest er) throws DaoException{
		String sql = "INSERT INTO fms_eng_request_outsource " +
				"(id, requestId, outsourceType, companyId, estimatedCost, actualCost, remarks, " +
				"createdBy, createdOn, modifiedBy, modifiedOn) " +
				"VALUES " +
				"(#outsourceId#, #requestId#, #outsourceType#, #companyId#, #estimatedCost#, #actualCost#, #description#, " +
				"#createdBy#, #createdOn#, #modifiedBy#, #modifiedOn#) ";
		
		super.update(sql, er);
	}
	
	protected void updateOutsource(EngineeringRequest er) throws DaoException{
		String sql = "UPDATE fms_eng_request_outsource " +
				"SET outsourceType=#outsourceType#, companyId=#companyId#, estimatedCost=#estimatedCost#, " +
				"actualCost=#actualCost#, " +
				"remarks=#description#, modifiedBy=#modifiedBy#, modifiedOn=#modifiedOn# " +
				"WHERE id=#outsourceId# ";
		super.update(sql, er);
	}
	
	protected void updateRequestFCCancel(EngineeringRequest er) throws DaoException {
		String sql = "UPDATE fms_eng_request " +
				"SET cancellationCharges=#cancellationCharges#, " +
				"status=#status#, " +
				"modifiedBy=#modifiedBy#, modifiedOn=#modifiedOn#," +
				"cancellationChargeManpower=#cancellationCostManpower# " +
				"WHERE requestId=#requestId# ";
		super.update(sql, er);
	}
	
	protected void updateRequestLate(EngineeringRequest er) throws DaoException {
		String sql = "UPDATE fms_eng_request " +
				"SET lateCharges=#lateCharges#, " +
				"modifiedBy=#modifiedBy#, modifiedOn=#modifiedOn# " +
				"WHERE requestId=#requestId# ";
		super.update(sql, er);
	}
	
	protected void insertOutsourceAttachment(EngineeringRequest er) throws DaoException {
		String sqlFile = "INSERT INTO fms_attachment " +
		"(id, fileName, path, referenceId, createdBy, createdOn, modifiedBy, modifiedOn) " +
		"VALUES " +
		"(#fileId#, #fileName#, #filePath#, #outsourceId#, #createdBy#, #createdOn#, #modifiedBy#, #modifiedOn#) ";
		
		super.update(sqlFile, er);
	}
	
	protected void deleteOutsource(String outsourceId) throws DaoException{
		super.update("DELETE FROM fms_eng_request_outsource WHERE id=?", new String[] {outsourceId});
		super.update("DELETE FROM fms_attachment WHERE referenceId=?", new String[] {outsourceId});
	}
	
	protected void deleteOutsourceFile(String fileId) throws DaoException{
		super.update("DELETE FROM fms_attachment WHERE id=?", new String[] {fileId});
	}
	
	public double selectOutsourceCost(String requestId) throws DaoException {
		String sql = "SELECT SUM(estimatedCost) AS totalEstimatedCost FROM fms_eng_request_outsource "+
				"WHERE 1=1 ";
		if (requestId!=null && !"".equals(requestId)){
			sql += " AND requestId = '" + requestId + "'";
		}		
		
		double count=0;
		
		try{
		Collection col= super.select(sql, HashMap.class, null, 0, 1);
		if(col!=null){
			HashMap map=(HashMap)col.iterator().next();
			if (map.get("totalEstimatedCost") != null){
				Double totalRate = new Double(((Number)map.get("totalEstimatedCost")).doubleValue());
				count = totalRate;
			}
		}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return count;	
	}
	
	public double selectOutsourceActualCost(String requestId) throws DaoException {
		String sql = "SELECT SUM(actualCost) AS totalActualCost FROM fms_eng_request_outsource "+
				"WHERE 1=1 ";
		if (requestId!=null && !"".equals(requestId)){
			sql += " AND requestId = '" + requestId + "'";
		}		
		
		double count=0;
		
		try{
		Collection col= super.select(sql, HashMap.class, null, 0, 1);
		if(col!=null){
			HashMap map=(HashMap)col.iterator().next();
			if (map.get("totalActualCost") != null){
				Double totalRate = new Double(((Number)map.get("totalActualCost")).doubleValue());
				count = totalRate;
			}
		}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return count;	
	}
	
	public int selectOutsourceCount(String search, String outsourceType, String requestId, String sort, boolean desc, int start, int rows) throws DaoException {
		String sql = "SELECT count(*) AS COUNT " +
				"FROM fms_eng_request req INNER JOIN fms_eng_request_outsource out ON (req.requestId = out.requestId) " +
				"WHERE 1=1 ";
		
		if (search!=null && !"".equals(search)){
			//sql+=" AND ( req.title like '%"+search+"%' OR r.description like '%"+search+"%' ) ";
		}
		
		if (outsourceType!=null && !"".equals(outsourceType) && !"-1".equals(outsourceType)){
			sql+=" AND ( out.outsourceType = '"+outsourceType+"' ) ";
		}
		
		if (requestId!=null && !"".equals(requestId) && !"-1".equals(requestId)){
			sql+=" AND ( out.requestId = '"+ requestId +"' ) ";
		}
		
		int count=0;
		
		try{
		Collection col= super.select(sql, HashMap.class, null, 0, 1);
		if(col!=null){
			HashMap map=(HashMap)col.iterator().next();
			count=(Integer)map.get("COUNT");
		}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return count;
	}
	
	public void updateRequestStatus(EngineeringRequest request)  throws DaoException{
		String sql="Update fms_eng_request set status=#status#," +
				" modifiedBy=#modifiedBy#, modifiedOn=#modifiedOn# where requestId=#requestId#";
		super.update(sql,request);
	}
	
	public int countDays(Date startDate, Date endDate) throws DaoException {
		int count = 0;
		String sql = "SELECT DATEDIFF(day, '" + startDate + "', '" + endDate + "')";
		
		try {
			Collection col = super.select(sql, HashMap.class, null, 0, 1);
			if (col != null){
				HashMap map = (HashMap)col.iterator().next();
				count = (Integer)map.get("COUNT");
			}
		} catch (Exception e){
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return count;
	}
	
	public Collection selectFacility(String id) throws DaoException {
		return super.select("SELECT f.*, c.name as category_name, ch.name as channel_name from fms_facility " +
				            "f, fms_facility_category c, fms_tran_channel ch WHERE f.category_id=c.id and " +
				            "f.channel_id=ch.setup_id and f.id=?", FacilityObject.class, new String[] {id}, 0, -1);
	}
	
	public Collection selectIncomingRequestFCTable(String search, Date requiredFrom, Date requiredTo, String departmentId, String userId, String status, String sort,boolean desc,int start,int rows) throws DaoException{
		ArrayList args = new ArrayList();
		String sql="SELECT r.requestId, r.title, r.requestType, r.clientName, r.programType, " +
				" r.program, r.description, r.requiredFrom, r.requiredTo, " +
				" r.createdBy, r.createdOn, r.modifiedBy, r.modifiedOn, r.status, r.state, " +
				" r.submittedDate," +
				" d.name AS department " +
				" FROM fms_eng_request r INNER JOIN security_user u ON (r.createdBy = u.username) " +
				" INNER JOIN fms_department d ON (u.department = d.id) " +
				" WHERE 1=1 "; 
		if (status != null && !"".equals(status) && !"-1".equals(status)){
			sql += " AND r.status='" + status +"'";
		} else {
			//sql += " AND r.status='" + EngineeringModule.FC_ASSIGNED_STATUS +"'";
			sql += " AND ( " +
					"r.status <> '" + EngineeringModule.DRAFT_STATUS + "' AND " +
					"r.status <> '" + EngineeringModule.PENDING_STATUS + "' AND " +
					"r.status <> '" + EngineeringModule.PROCESS_STATUS + "' AND " +
					"r.status <> '" + EngineeringModule.FC_ASSIGNED_STATUS + "' " +
					") ";
		}
		if(requiredFrom != null && requiredTo != null){
			sql += " AND ((r.requiredFrom >= ? AND r.requiredFrom <= ?) OR (r.requiredFrom <= ? AND r.requiredTo >= ?))";
			args.add(requiredFrom);
			args.add(requiredTo);
			args.add(requiredFrom);
			args.add(requiredFrom);
		}
		if (userId != null && !"".equals(userId)){
			sql += " AND r.fcId='" + userId +"'";
		} 
		
		if(search!=null && !"".equals(search)){
			sql+=" AND ( r.requestId LIKE '%" + search + "%' OR r.title like '%"+search+"%' OR r.description like '%"+search+"%' ) ";
		}
		if(departmentId!=null && !"".equals(departmentId) && !"-1".equals(departmentId)){
			sql+=" AND ( d.id = '"+departmentId+"' ) ";
		}
		if(sort!=null && !"".equals(sort)){
			if (sort.equals("department")) {
				sort = "d." + sort;
			} else if (sort.equals("createdUserName")) {
					sort = "r.createdBy";
			} else if (sort.equals("requiredDateFrom")) {
					sort = "requiredFrom";				
			} else if (sort.equals("title")) {
					sort = "r.title";
			} else if (sort.equals("status")) {
					sort = "r.status";
			}
			
			sql+=" order by "+sort+" ";
		}else{
			sql+=" order by r.status, r.createdOn DESC ";
		}
		if(desc){
			sql+=" DESC ";
		}
				
		return super.select(sql, EngineeringRequest.class, args.toArray(), start, rows);
	}
	
	public int selectRequestFCTableCount(String search, Date requiredFrom, Date requiredTo,  String departmentId, String status, String userId) throws DaoException{
		ArrayList args = new ArrayList();
		String sql="Select count(*) As COUNT " +
					" FROM fms_eng_request r INNER JOIN security_user u ON (r.createdBy = u.username) " +
					" INNER JOIN fms_department d ON (u.department = d.id) " +
					" WHERE 1=1 " ;
		if (status !=null && !"".equals(status) && !"-1".equals(status)){
			sql += " AND r.status='" + status  +"'";
		} else {
			//sql += " AND r.status='" + EngineeringModule.FC_ASSIGNED_STATUS +"'";
			sql += " AND ( " +
					"r.status <> '" + EngineeringModule.DRAFT_STATUS + "' AND " +
					"r.status <> '" + EngineeringModule.PENDING_STATUS + "' AND " +
					"r.status <> '" + EngineeringModule.PROCESS_STATUS + "' AND " +
					"r.status <> '" + EngineeringModule.FC_ASSIGNED_STATUS + "' " +
					") ";
		}
		if(requiredFrom != null && requiredTo != null){
			sql += " AND ((r.requiredFrom >= ? AND r.requiredFrom <= ?) OR (r.requiredFrom <= ? AND r.requiredTo >= ?))";
			args.add(requiredFrom);
			args.add(requiredTo);
			args.add(requiredFrom);
			args.add(requiredFrom);
		}
		if (userId != null && !"".equals(userId)){
			sql += " AND r.fcId='" + userId +"'";
		} 
		
		if(search!=null && !"".equals(search)){
			sql+=" AND ( r.requestId LIKE '%" + search + "%' OR r.title like '%"+search+"%' OR r.description like '%"+search+"%' ) ";
		}
		if(departmentId!=null && !"".equals(departmentId) && !"-1".equals(departmentId)){
			sql+=" AND ( d.id = '"+departmentId+"' ) ";
		}
		
		int count=0;
		
		try{
		Collection col= super.select(sql, HashMap.class, args.toArray(), 0, 1);
		if(col!=null){
			HashMap map=(HashMap)col.iterator().next();
			count=(Integer)map.get("COUNT");
		}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return count;
	}
	
	public double selectTotalSystemCalculatedCharges(String requestId) throws DaoException{

		String sql="select sum(systemCalculatedCharges) AS total " +
				" from fms_eng_cancel_service_log " +
				" where flagMainCharges='1' " +
				" and requestId=?";
		
		double charges=0.0;
		try {
			Collection col = super.select(sql, HashMap.class, new String[] {requestId} , 0, -1);
			if (col != null && col.size()>0){
				for (Iterator iterator = col.iterator(); iterator.hasNext();) {
					try {
						HashMap map = (HashMap) iterator.next();
						if(map.get("total") != null)
							charges = (Double) map.get("total");
								
					} catch (Exception e) {
						Log.getLog(getClass()).error(e.toString(), e);
					}
				}
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return charges;
	}
	
	public Collection getCheckedOutEquipmentForCancelledRequest(String assignmentId) {
		String sql =
				"SELECT ae.id, ae.assignmentId, ae.barcode, ae.checkedOutBy, ae.checkedOutDate " +
				"FROM fms_eng_assignment a " +
				"INNER JOIN fms_eng_assignment_equipment ae ON (ae.assignmentId = a.assignmentId) " +
				"WHERE a.assignmentId = ? " +
				"AND ae.barcode IS NOT NULL " +
				"AND ae.checkedInDate IS NULL " +
					"UNION " +
				"SELECT ae.id, ae.assignmentId, ae.barcode, ae.checkedOutBy, ae.checkedOutDate " +
				"FROM fms_eng_assignment a " +
				"INNER JOIN fms_eng_assignment_equipment ae ON (ae.groupId = a.groupId AND ae.assignmentId = '-') " +
				"WHERE a.assignmentId = ? " +
				"AND ae.barcode IS NOT NULL " +
				"AND ae.checkedInDate IS NULL ";
		
		try {
			Collection col = super.select(sql, Assignment.class, new String[] {assignmentId, assignmentId}, 0, -1);
			return col;
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return new ArrayList();
		}
	}
}
