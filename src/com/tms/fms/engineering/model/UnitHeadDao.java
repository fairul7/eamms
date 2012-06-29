package com.tms.fms.engineering.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.model.DefaultDataObject;
import kacang.util.Log;

/**
 * @author fairul
 *
 */
public class UnitHeadDao extends DataSourceDao {
	@Override
	public void init() throws DaoException {

		try {
			String sql = "CREATE TABLE fms_eng_assignment(" +
			" assignmentId varchar(255) NOT NULL PRIMARY KEY, code varchar(255) NOT NULL, requestId varchar(255) NOT NULL, serviceType char(1) NOT NULL," +
			" serviceId varchar(255) NOT NULL, assignmentType char(1) " +
			")";
			super.update(sql, null);
		} catch (Exception e) {
		}

		try {
			String sql = "ALTER TABLE fms_eng_assignment ADD groupId VARCHAR(255)";
			super.update(sql, null);
		} catch (Exception e) {
		}

		// create index for fms_eng_assignment(requestId)
		try {
			String sql = "CREATE INDEX requestId ON fms_eng_assignment(requestId)";
			super.update(sql, null);
		} catch (Exception e) {
		}
		
		// create index for fms_eng_assignment(serviceId)
		try {
			String sql = "CREATE INDEX serviceId ON fms_eng_assignment(serviceId)";
			super.update(sql, null);
		} catch (Exception e) {
		}
		
		// create index for fms_eng_assignment(requestId, assignmentType, serviceType)
		try {
			String sql = "CREATE INDEX requestId_assignmentType_serviceType ON fms_eng_assignment(requestId, assignmentType, serviceType)";
			super.update(sql, null);
		} catch (Exception e) {
		}

		try {
			String sql = "CREATE TABLE fms_eng_assignment_manpower(" +
			" assignmentId varchar(255) NOT NULL PRIMARY KEY, rateCardId varchar(255) NOT NULL, competencyId varchar(255) NOT NULL, " +
			" requiredFrom datetime, requiredTo datetime, fromTime varchar(6), toTime varchar(6)," +
			" userId varchar(255) NULL, status char(1), createdBy varchar(255) NULL, createdDate datetime" +
			" NULL, modifiedBy varchar(255) NULL, modifiedDate datetime NULL )";
			super.update(sql, null);
		} catch (Exception e) {
		}

		try {
			String sql = 
				"CREATE TABLE fms_eng_assignment_equipment(" +
				"id varchar(255) NOT NULL PRIMARY KEY, " +
				"assignmentId varchar(255) NOT NULL, " +
				"rateCardId varchar(255) NULL, " +
				"rateCardCategoryId varchar(255) NULL, " +
				"requiredFrom datetime NULL, " +
				"requiredTo datetime NULL, " +
				"fromTime varchar(6) NULL, " +
				"toTime varchar(6) NULL, " +
				"barcode varchar(255) NULL, " +
				"checkedOutBy varchar(255) NULL, " +
				"checkedOutDate datetime NULL, " +
				"checkedInBy varchar(255) NULL, " +
				"checkedInDate datetime NULL, " +
				"status char(1) NULL, " +
				"createdBy varchar(255) NULL, " +
				"createdDate datetime NULL, " +
				"modifiedBy varchar(255) NULL, " +
				"modifiedDate datetime NULL, " +
				"takenBy VARCHAR(255) NULL, " +
				"preparedBy VARCHAR(255) NULL, " +
				"assignmentLocation VARCHAR(255) NULL, " +
				"groupId VARCHAR(255) NULL, " +
				"reasonUnfulfilled TEXT NULL, " +
				"damage CHAR(1) NULL" +
				")";
			super.update(sql, null);
		} catch (Exception e) {
		}

		try {
			super.update("ALTER TABLE fms_eng_assignment_equipment ADD takenBy VARCHAR(255)", null);
		} catch (Exception e) {
		}

		try {
			super.update("ALTER TABLE fms_eng_assignment_equipment ADD preparedBy VARCHAR(255)", null);
		} catch (Exception e) {
		}

		try {
			super.update("ALTER TABLE fms_eng_assignment_equipment ADD assignmentLocation VARCHAR(255)", null);
		} catch (Exception e) {
		}

		try {
			String sql = "ALTER TABLE fms_eng_assignment_equipment ADD groupId VARCHAR(255)";
			super.update(sql, null);
		} catch (Exception e) {
		}

		try {
			super.update("ALTER TABLE fms_eng_assignment_equipment ALTER COLUMN rateCardId varchar(255) NULL", null);
		} catch (Exception e) {
		}

		try {
			super.update("ALTER TABLE fms_eng_assignment_equipment ALTER COLUMN rateCardCategoryId varchar(255) NULL", null);
		} catch (Exception e) {
		}

		try {
			super.update("ALTER TABLE fms_eng_assignment_equipment ADD reasonUnfulfilled TEXT NULL", null);
		} catch (Exception e) {
		}

		try {
			super.update("ALTER TABLE fms_eng_assignment_equipment ADD damage CHAR(1) NULL ", null);
		} catch (Exception e) {

		}

		try {
			super.update("ALTER TABLE fms_eng_assignment_equipment ADD id varchar(255) NOT NULL", null);
		} catch (Exception e) {
		}

		// drop old primary key 
		dropOldPrimaryKey("fms_eng_assignment_equipment");
		dropOldPrimaryKey("fms_eng_assignment_equipment_ori_01042010"); // this table causes conflict with the PK in fms_eng_assignment_equipment

		// create primary key for fms_eng_assignment_equipment
		try {
			super.update("ALTER TABLE fms_eng_assignment_equipment ADD CONSTRAINT PK_fms_eng_assignment_equipment PRIMARY KEY CLUSTERED (id)", null);
		} catch (Exception e) {
		}

		// create index for fms_eng_assignment_equipment(assignmentId)
		try {
			super.update("CREATE INDEX assignmentId ON fms_eng_assignment_equipment(assignmentId)", null);
		} catch (Exception e) {
		}

		// create index for fms_eng_assignment_equipment(groupId)
		try {
			super.update("CREATE INDEX groupId ON fms_eng_assignment_equipment(groupId)", null);
		} catch (Exception e) {
		}

		// create index for fms_eng_assignment_equipment(barcode)
		try {
			super.update("CREATE INDEX barcode ON fms_eng_assignment_equipment(barcode)", null);
		} catch (Exception e) {
		}

		// create index for fms_eng_assignment_equipment(requiredFrom, requiredTo)
		try {
			super.update("CREATE INDEX requiredFrom_requiredTo ON fms_eng_assignment_equipment(requiredFrom, requiredTo)", null);
		} catch (Exception e) {
		}

		try{
			super.update("ALTER TABLE fms_eng_assignment_manpower ADD completionDate datetime", null);
		}catch (Exception e) {
		}
		try{
			super.update("ALTER TABLE fms_eng_assignment_manpower ADD remarks TEXT NULL", null);
		} catch (Exception e){
		}
		try {
			super.update("ALTER TABLE fms_eng_assignment_manpower ADD reasonUnfulfilled TEXT NULL", null);
		} catch (Exception e){
		}
		try{
			super.update("ALTER TABLE fms_eng_assignment_manpower ADD chargeBack CHAR(1) NULL", null);
		} catch (Exception e){
		}
		try{
			super.update("ALTER TABLE fms_eng_assignment_manpower ADD callBack CHAR(1) NULL", null);
		} catch (Exception e){
		}
		
		// create index for fms_eng_assignment_manpower(userId)
		try {
			String sql = "CREATE INDEX userId ON fms_eng_assignment_manpower(userId)";
			super.update(sql, null);
		} catch (Exception e) {
		}
		
		// create index for fms_eng_assignment_manpower(userId, requiredTo, requiredFrom)
		try {
			String sql = "CREATE INDEX userId_requiredToFrom ON fms_eng_assignment_manpower(userId, requiredTo, requiredFrom)";
			super.update(sql, null);
		} catch (Exception e) {
		}
		
		// create index for fms_eng_assignment_manpower(assignmentId, status)
		try {
			String sql = "CREATE INDEX assignmentId_status ON fms_eng_assignment_manpower(assignmentId, status)";
			super.update(sql, null);
		} catch (Exception e) {
		}
	}

	private void dropOldPrimaryKey(String tbName) {
		try {
			String pkName = getPrimaryKey(tbName);

			if (pkName != null && !pkName.equals("PK_" + tbName)) {
				Log.getLog(getClass()).info("DROPPING Primary Key '" + pkName + "' for table " + tbName);
				super.update("ALTER TABLE " + tbName + " DROP CONSTRAINT " + pkName, null);
			}
		} catch (Exception e) {
		}
	}

	private String getPrimaryKey(String tableName){
		String sql="SELECT [name] FROM sysobjects " +
		"WHERE [xtype] = 'PK' AND [parent_obj] = OBJECT_ID(N'[dbo].["+tableName+"]')";
		try{
			Collection col= super.select(sql, HashMap.class, new String[]{}, 0, -1);
			if (col != null && col.size()>0) {
				HashMap row=(HashMap)col.iterator().next();
				String pkName=(String) row.get("name");
				return pkName;
			}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return null;
	}
	private void insertAssignment(Assignment assignment) throws DaoException {
		String sqlMain="INSERT INTO fms_eng_assignment (assignmentId, code, requestId," +
		" serviceType, serviceId,assignmentType, groupId) VALUES (" +
		"#assignmentId#, #code#, #requestId#,#serviceType#, #serviceId#, #assignmentType#, #groupId#)";
		super.update(sqlMain,assignment);

	}

	public void insertManpowerAssignment(Assignment assignment) throws DaoException {
		insertAssignment(assignment);
		String sqlChild="INSERT INTO fms_eng_assignment_manpower (assignmentId, rateCardId, competencyId," +
		" requiredFrom, requiredTo,fromTime,toTime,status,createdBy,createdDate,modifiedBy,modifiedDate) VALUES (" +
		"#assignmentId#, #rateCardId#, #competencyId#,#requiredFrom#, #requiredTo#, #fromTime#, " +
		"#toTime#, #status#, #createdBy#, #createdDate#, #modifiedBy#, #modifiedDate#)";
		super.update(sqlChild,assignment);
	}
	
	public void updateManpowerAssignment(Assignment assignment, String assignmentId) throws DaoException {
		//insertAssignment(assignment);
		String sqlChild="UPDATE fms_eng_assignment_manpower SET " +
				" requiredFrom=#requiredFrom#," +
				" requiredTo=#requiredTo#," +
				" fromTime=#fromTime#," +
				" toTime=#toTime# " +
				" WHERE assignmentId = '"+assignmentId+"'";
			super.update(sqlChild,assignment);
	}
	
	public void insertFacilityAssignment(Assignment assignment) throws DaoException {
		try{
			insertAssignment(assignment);
		}catch (Exception e) {
		}
		String sqlChild="INSERT INTO fms_eng_assignment_equipment (id,assignmentId, rateCardId, rateCardCategoryId," +
		" requiredFrom, requiredTo,fromTime,toTime,status,createdBy,createdDate,modifiedBy,modifiedDate, groupId) VALUES (" +
		"#id#,#assignmentId#, #rateCardId#, #rateCardCategoryId#,#requiredFrom#, #requiredTo#, #fromTime#, " +
		"#toTime#, #status#, #createdBy#, #createdDate#, #modifiedBy#, #modifiedDate#, #groupId#)";
		super.update(sqlChild,assignment);
	}
	
	public void updateFacilityAssignment(Assignment assignment, String assignmentId) throws DaoException {
		//insertAssignment(assignment);
		String sqlChild="UPDATE fms_eng_assignment_equipment SET " +
		" requiredFrom=#requiredFrom#," +
		" requiredTo=#requiredTo#," +
		" fromTime=#fromTime#," +
		" toTime=#toTime# " +
		" WHERE assignmentId = '"+assignmentId+"'";
			super.update(sqlChild,assignment);
	}
	
//	For HOD
	public Collection selectHOURequest(String search, Date requiredFrom, String status, String department, String sort, boolean desc, int start, int rows) throws DaoException{
		ArrayList args = new ArrayList();

		String sql = 
			"SELECT r.requestId, r.title, r.requestType, r.clientName, r.programType, r.program, " +
			"r.requiredFrom, r.requiredTo, r.createdBy, r.createdOn, r.modifiedBy, r.modifiedOn, " +
			"r.status, r.state, r.submittedDate " +
			"FROM fms_eng_request r " +
			"INNER JOIN security_user u ON (r.createdBy=u.username) " +
			"INNER JOIN fms_unit d ON (u.unit=d.id) " +
			"WHERE d.status='1' ";

		if (status != null && !"-1".equals(status)) {
			sql += "AND r.status= '"+status+"' ";
		} else {
			sql += "AND (" +
			"r.status <> '" + EngineeringModule.DRAFT_STATUS + "' AND " +
			"r.status <> '" + EngineeringModule.PENDING_STATUS + "' AND " +
			"r.status <> '" + EngineeringModule.PROCESS_STATUS + "' AND " +
			"r.status <> '" + EngineeringModule.FC_ASSIGNED_STATUS + "' AND " +
			"r.status <> '" + EngineeringModule.OUTSOURCED_STATUS + "' AND " +
			"r.status <> '" + EngineeringModule.REJECTED_STATUS + "'" +
			") ";
		}
		if (department != null && !"-1".equals(department)) {
			sql += "AND d.department_id = '"+department+"' ";
		}
		if (search != null && !"".equals(search)) {
			sql += "AND (r.requestId LIKE '%" +search+ "%' OR r.title like '%"+search+"%' OR r.description like '%"+search+"%') ";
		}
		if (requiredFrom != null) {
			sql += "AND r.requiredFrom >= ? ";
			args.add(requiredFrom);
		}

		sql += 
			"GROUP BY r.requestId, r.title, r.requestType, r.clientName, r.programType, r.program, " +
			"r.requiredFrom, r.requiredTo, r.createdBy, r.createdOn, r.modifiedBy, r.modifiedOn, " +
			"r.status, r.state, r.submittedDate ";

		if (sort != null && !"".equals(sort)) {
			if (sort.equals("requestIdWithLink")) {
				sort = "requestId";
			} else if (sort.equals("requiredDateFrom")) {
				sort = "requiredFrom";
			}

			sql += "ORDER BY r."+sort+" ";
		} else {
			sql+="ORDER BY r.modifiedOn DESC ";
		}
		if (desc) {
			sql += "DESC ";
		}
		return super.select(sql, EngineeringRequest.class, args.toArray(), start, rows);
	}

	public int selectHOURequestCount(String search,String status, String department) throws DaoException{
		String sql="Select r.requestId,r.title,r.requestType, r.clientName, r.programType, program, " +
		"r.requiredFrom,r.requiredTo,r.createdBy, r.createdOn, r.modifiedBy, r.modifiedOn, r.status, r.state,r.submittedDate " +
		" FROM fms_eng_request r " +
		" INNER JOIN security_user u on " +
		" r.createdBy=u.username INNER JOIN fms_unit d on u.unit=d.id " +
		//" LEFT JOIN fms_unit_alternate_approver a on a.unitId=d.id " +
		//" where d.status='1' ";
		" where  " +
		//"(d.HOU=? OR a.userId=?)  AND " +
		" d.status='1' ";

		if(status!=null && !"-1".equals(status)){
			sql+=" AND ( r.status= '"+status+"' ) ";
		} else {
			sql += " AND ( " +
			"r.status <> '" + EngineeringModule.DRAFT_STATUS + "' AND " +
			"r.status <> '" + EngineeringModule.PENDING_STATUS + "' AND " +
			"r.status <> '" + EngineeringModule.PROCESS_STATUS + "' AND " +
			"r.status <> '" + EngineeringModule.FC_ASSIGNED_STATUS + "' " +
			") ";
		}
		if(department!=null && !"-1".equals(department)){
			sql+=" AND ( d.department_id = '"+department+"') ";
		}
		if(search!=null && !"".equals(search)){
			sql+=" AND ( r.title like '%"+search+"%' OR r.description like '%"+search+"%' ) ";
		}
		sql+="group By r.requestId ,r.title,r.requestType, r.clientName, r.programType,program, r.requiredFrom,r.requiredTo," +
		"r.createdBy, r.createdOn, r.modifiedBy, r.modifiedOn, r.status, r.state,r.submittedDate";

		Collection col = new ArrayList();
		col = super.select(sql, EngineeringRequest.class, null , 0, -1);

		int result = 0;
		if (col != null && col.size()>0) {
			result = col.size();
		}

		return result; 
	}

	public boolean isUnitApprover(String userId,String requestId){
		boolean result=false;
		String sql="select count(distinct r.requestId) AS COUNT from fms_eng_request r INNER JOIN security_user su on su.username=r.createdBy " +
		" INNER JOIN fms_unit u on u.id=su.unit LEFT JOIN fms_unit_alternate_approver a on u.id=a.unitId " +
		" where (a.userId=? OR u.hou=? ) AND r.requestId=? " ;
		try {
			int count=0;
			Collection col=super.select(sql, HashMap.class, new String[] {userId,userId,requestId} , 0, -1);
			if(col!=null){
				HashMap map=(HashMap)col.iterator().next();
				count=(Integer)map.get("COUNT");
			}
			if(count>0){
				result=true;
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return result;
	}

	public boolean isUnitApproverByUnitId(String userId,String unitId){
		boolean result=false;
		String sql = "SELECT count(u.id) as COUNT " +
		"FROM fms_unit u LEFT join fms_unit_alternate_approver app ON (u.id = app.unitId) " +
		"WHERE u.id=? " +
		"AND (u.HOU =? " +
		"OR app.userId =?) " ;
		try {
			int count=0;
			Collection col=super.select(sql, HashMap.class, new String[] {unitId,userId,userId} , 0, -1);
			if(col!=null){
				HashMap map=(HashMap)col.iterator().next();
				count=(Integer)map.get("COUNT");
			}
			if(count>0){
				result=true;
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return result;
	}

	public boolean isAssignmentPrepared(String requestId) throws DaoException{
		String sql="Select count(*) AS COUNT " +
		" FROM fms_eng_assignment a  " +
		" where ( a.requestId= ? )";
		boolean prepared=false;
		int count=0;
		try{
			Collection col= super.select(sql, HashMap.class, new String[]{requestId}, 0, 1);
			if(col!=null){
				HashMap map=(HashMap)col.iterator().next();
				count=(Integer)map.get("COUNT");
				if(count>0){
					prepared=true;
				}
			}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return prepared;
	}

	public Collection getManpowerAssignments(String requestId){
		String sql="Select a.assignmentId,a.code,a.serviceType,a.serviceId,a.assignmentType,requiredFrom," +
		" requiredTo,fromTime,toTime,status,m.competencyId,competencyName,u.firstName + ' ' + u.lastName as assignedFacility," +
		" c.unitId, m.completionDate " +
		" from fms_eng_assignment a " +
		" INNER JOIN fms_eng_assignment_manpower m on a.assignmentId=m.assignmentId " +
		" INNER JOIN competency c on c.competencyId=m.competencyId " +
		" LEFT JOIN security_user u on m.userId=u.id " +
		" where a.requestId=? order by a.serviceType,a.assignmentType, requiredFrom, a.code ";
		Collection col=new ArrayList();
		try{
			col= super.select(sql, Assignment.class, new String[]{requestId}, 0, -1);

		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}

	public Collection getManpowerAssignmentsByServiceId(String requestId, String serviceId, Date fromTime){
		ArrayList args = new ArrayList();
		String sql="Select a.assignmentId,a.code,a.serviceType,a.serviceId,a.assignmentType,requiredFrom," +
		" requiredTo,fromTime,toTime,status,m.competencyId,competencyName,u.firstName + ' ' + u.lastName as assignedFacility," +
		" c.unitId, m.completionDate " +
		" from fms_eng_assignment a " +
		" INNER JOIN fms_eng_assignment_manpower m on a.assignmentId=m.assignmentId " +
		" INNER JOIN competency c on c.competencyId=m.competencyId " +
		" LEFT JOIN security_user u on m.userId=u.id " +
		" where a.requestId=? and a.serviceId=?	and requiredFrom = ? " +
		" order by a.serviceType,a.assignmentType, requiredFrom, a.code ";
		args.add(requestId);
		args.add(serviceId);
		args.add(fromTime);
		Collection col=new ArrayList();
		try{
			col= super.select(sql, Assignment.class, args.toArray(), 0, -1);

		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}

	public Collection getServiceIdsFromTiedSstudio(String requestId){
		String sql=" select est.id as serviceId " +
		" from fms_eng_service_studio est " +
		" inner join fms_rate_card rc on rc.id=est.facilityId " +
		" inner JOIN fms_rate_card_manpower rcm ON (rc.id = rcm.rateCardId) " +
		" INNER JOIN competency com ON (rcm.manpower = com.competencyId) " +
		" where rc.status!='0' AND rc.serviceTypeId='5' and est.requestId = ? ";
		Collection col=new ArrayList();
		Collection services = new ArrayList();
		try{


			col= super.select(sql, HashMap.class, new String[]{requestId}, 0, 1);
			if(col!=null && col.size()>0){
				for (Iterator i=col.iterator(); i.hasNext();) {
					HashMap map=(HashMap)i.next();
					services.add((String)map.get("serviceId"));
				}
			}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return services;
	}

	public Collection getRequestIdsFromTiedSstudio(Date bookingDate){
		ArrayList args = new ArrayList();
		String sql=" select est.id as serviceId, est.requestId , rc.id as rateCardId, rc.name, rc.remarksRequest, " +
		" com.competencyName from fms_eng_service_studio est " +
		" inner join fms_rate_card rc on rc.id=est.facilityId " +
		" inner JOIN fms_rate_card_manpower rcm ON (rc.id = rcm.rateCardId) " +
		" INNER JOIN competency com ON (rcm.manpower = com.competencyId) " +
		" where rc.status!='0' AND rc.serviceTypeId='5' " +
		" and bookingdate >= ? order by bookingdate ";
		Collection col=new ArrayList();
		Collection services = new ArrayList();
		args.add(bookingDate);
		try{


			col= super.select(sql, HashMap.class, args.toArray(), 0, -1);
			if(col!=null && col.size()>0){
				for (Iterator i=col.iterator(); i.hasNext();) {
					HashMap map=(HashMap)i.next();
					services.add((String)map.get("requestId"));
				}
			}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return services;
	}
	public Collection getAssignmentIdsByTiedServices(String serviceId){
		String sql=" select ea.assignmentId as assignmentId from fms_eng_assignment ea " +
		" inner join fms_eng_assignment_manpower eam on eam.assignmentId=ea.assignmentId " +
		" where serviceid= ? " +
		" and assignmentType='M' and eam.status='N' ";

		Collection col=new ArrayList();
		Collection assigns = new ArrayList();
		try{


			col= super.select(sql, HashMap.class, new String[]{serviceId}, 0, -1);
			if(col!=null && col.size()>0){
				for (Iterator i=col.iterator(); i.hasNext();) {
					HashMap map=(HashMap)i.next();
					assigns.add((String)map.get("assignmentId"));
				}
			}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return assigns;
	}

	public Collection getManpowerIdsFromWorkingProfile(Date dateFrom , Date dateTo){
		ArrayList args = new ArrayList();
		String sql=" select d.workingprofiledurationid, dm.userId as userId, d.startDate, d.endDate " +
		" from fms_working_profile_duration_manpower dm " +
		" inner join  fms_working_profile_duration d on d.workingprofiledurationid=dm.workingprofiledurationid " +
		" where d.startDate <=? and d.endDate >=? ";
		args.add(dateFrom);
		args.add(dateTo);

		Collection col=new ArrayList();
		Collection manpowers = new ArrayList();
		try{
			col= super.select(sql, HashMap.class, args.toArray(), 0, -1);
			if(col!=null && col.size()>0){
				for (Iterator i=col.iterator(); i.hasNext();) {
					HashMap map=(HashMap)i.next();
					manpowers.add(map.get("userId"));
				}
			}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return manpowers;
	}
	public Date getScheduledDate() throws DaoException {
		Date scheduledDat =null;
		String sql = "select scheduledDate from fms_eng_assignment_setting ";	
		try{
			Collection col= super.select(sql, HashMap.class,null, 0, 1);
			if(col!=null){
				HashMap map=(HashMap)col.iterator().next();
				scheduledDat = (Date)map.get("scheduledDate");
			}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return scheduledDat;
	}
	public Date getScheduledDateForAutoAssignment() throws DaoException {
		Date scheduledDat =null;
		String sql = "select scheduledDate from fms_eng_auto_assignment_setting ";	
		try{
			Collection col= super.select(sql, HashMap.class,null, 0, 1);
			if(col!=null){
				HashMap map=(HashMap)col.iterator().next();
				scheduledDat = (Date)map.get("scheduledDate");
			}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return scheduledDat;
	}
	public Map selectRequestDateRange(String requestId) throws DaoException {
		Map manpowerMap = new HashMap();

		String sql = "select requiredFrom , requiredTo from fms_eng_request where requestId=?";	
		try{
			Collection col= super.select(sql, HashMap.class, new String[]{requestId}, 0, 1);
			if(col!=null){
				HashMap map=(HashMap)col.iterator().next();
				manpowerMap.put("requiredFrom", (Date)map.get("requiredFrom"));
				manpowerMap.put("requiredTo", (Date)map.get("requiredTo"));
			}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return manpowerMap;
	}

	public boolean manpowerAvailable(String manpowerId, Date requiredFrom, Date requiredTo){

		boolean available = true;
		ArrayList args = new ArrayList();
		String sql=" select ea.assignmentId, eam.userId, eam.requiredFrom, eam.requiredTo " +
		" from fms_eng_assignment ea " +
		" inner join fms_eng_assignment_manpower eam on eam.assignmentId=ea.assignmentId " +
		" where userID=? and " +
		" eam.requiredFrom >=? and eam.requiredTo <=?";
		args.add(manpowerId);
		args.add(requiredFrom);
		args.add(requiredTo);
		Collection col=new ArrayList();

		try{
			col= super.select(sql, Assignment.class, args.toArray(), 0, -1);

		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
			Log.getLog(getClass()).error(e.getMessage(),e);
		}

		if(col.size() > 0){
			available = false;
		}
		return available;
	}

	public Collection getFacilityAssignments(String requestId){
		String sql="Select a.assignmentId,a.code,a.serviceType,a.serviceId,a.assignmentType,requiredFrom," +
		" requiredTo,fromTime,toTime,status,e.rateCardCategoryId,c.name as rateCardCategoryName,e.checkedInDate " +
		" from fms_eng_assignment a INNER JOIN fms_eng_assignment_equipment e on a.assignmentId=e.assignmentId " +
		" INNER JOIN fms_eng_rate_card_category c on c.id=e.rateCardCategoryId " +
		" where a.requestId=? group by a.assignmentId,a.code,a.serviceType,a.serviceId,a.assignmentType,requiredFrom," +
		" requiredTo,fromTime,toTime,status,e.rateCardCategoryId,c.name,e.checkedInDate " +
		" order by a.serviceType,a.assignmentType, requiredFrom";
		Collection col=new ArrayList();
		try{
			col= super.select(sql, Assignment.class, new String[]{requestId}, 0, -1);

		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}

	public boolean checkNewAssignments(String requestId){

		boolean allNew = true;
		String sql="Select a.assignmentId,a.code,a.serviceType,a.serviceId,a.assignmentType,requiredFrom," +
		" requiredTo,fromTime,toTime,status,e.rateCardCategoryId,c.name as rateCardCategoryName,e.checkedInDate " +
		" from fms_eng_assignment a INNER JOIN fms_eng_assignment_equipment e on a.assignmentId=e.assignmentId " +
		" INNER JOIN fms_eng_rate_card_category c on c.id=e.rateCardCategoryId " +
		" where a.requestId=? AND status <> 'N'	" +
		" group by a.assignmentId,a.code,a.serviceType,a.serviceId,a.assignmentType,requiredFrom," +
		" requiredTo,fromTime,toTime,status,e.rateCardCategoryId,c.name,e.checkedInDate " +
		" order by a.serviceType,a.assignmentType";
		Collection col=new ArrayList();
		try{
			col= super.select(sql, Assignment.class, new String[]{requestId}, 0, -1);

		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}

		if(col.size() > 0){
			allNew = false;
		}
		return allNew;
	}

	public Assignment getManpowerAssignment(String assignmentId){
		String sql="Select a.assignmentId,a.code,a.serviceType,a.serviceId,a.assignmentType,requiredFrom," +
		" requiredTo,fromTime,toTime,status,m.competencyId,competencyName,u.firstName as assignedFacility " +
		" from fms_eng_assignment a " +
		" INNER JOIN fms_eng_assignment_manpower m on a.assignmentId=m.assignmentId " +
		" INNER JOIN competency c on c.competencyId=m.competencyId " +
		" LEFT JOIN security_user u on m.userId=u.id " +
		" where a.assignmentId=? order by a.serviceType,a.assignmentType";
		Collection col=new ArrayList();
		Assignment assign=new Assignment();
		try{
			col= super.select(sql, Assignment.class, new String[]{assignmentId}, 0, -1);
			assign=(Assignment)col.iterator().next();
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return assign;
	}

	public Assignment getFacilityAssignment(String assignmentId){
		String sql="Select a.assignmentId,a.code,a.serviceType,a.serviceId,a.requestId,a.assignmentType,requiredFrom," +
		" requiredTo,fromTime,toTime,status,e.rateCardCategoryId,c.name as rateCardCategoryName," +
		" e.assignmentLocation, e.takenBy, e.preparedBy " +
		" from fms_eng_assignment a INNER JOIN fms_eng_assignment_equipment e on a.assignmentId=e.assignmentId " +
		" INNER JOIN fms_eng_rate_card_category c on c.id=e.rateCardCategoryId " +
		" where a.assignmentId=? group by a.assignmentId,a.code,a.serviceType,a.serviceId,a.requestId,a.assignmentType,requiredFrom," +
		" requiredTo,fromTime,toTime,status,e.rateCardCategoryId,c.name, e.assignmentLocation, e.takenBy, e.preparedBy " +
		" order by a.serviceType,a.assignmentType";
		Collection col=new ArrayList();
		Assignment assign=new Assignment();
		try{
			col= super.select(sql, Assignment.class, new String[]{assignmentId}, 0, -1);
			assign=(Assignment)col.iterator().next();
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return assign;
	}

	public Assignment getAssignmentByGroupId(String groupId){
		Assignment assign = new Assignment();
		String sql = "Select TOP 1 a.assignmentId,a.code,a.serviceType,a.serviceId,a.requestId,a.assignmentType,requiredFrom," +
		" requiredTo,fromTime,toTime,status,e.rateCardCategoryId,c.name as rateCardCategoryName," +
		" e.assignmentLocation, e.takenBy, e.preparedBy, e.groupId " +
		" from fms_eng_assignment a INNER JOIN fms_eng_assignment_equipment e on a.assignmentId=e.assignmentId " +
		" INNER JOIN fms_eng_rate_card_category c on c.id=e.rateCardCategoryId " +
		" where a.groupId=? group by a.assignmentId,a.code,a.serviceType,a.serviceId,a.requestId,a.assignmentType,requiredFrom," +
		" requiredTo,fromTime,toTime,status,e.rateCardCategoryId,c.name, e.assignmentLocation, e.takenBy, e.preparedBy," +
		" e.groupId, e.modifiedDate " +
		" order by  e.modifiedDate asc, a.serviceType,a.assignmentType";
		Collection col = new ArrayList();

		try {
			col = super.select(sql, Assignment.class, new String[]{groupId}, 0, -1);
			assign = (Assignment) col.iterator().next();
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return assign;
	}

	public Assignment getLatestAssignmentByGroupId(String groupId){
		Assignment assign = new Assignment();
		String sql = "SELECT TOP 1 id as assignmentEquipmentId, checkedOutDate, takenBy, preparedBy, assignmentLocation " +
		"FROM fms_eng_assignment_equipment WHERE groupId = ? " +
		"ORDER BY checkedOutDate DESC";
		Collection col = new ArrayList();

		try {
			col = super.select(sql, Assignment.class, new String[]{groupId}, 0, -1);
			assign = (Assignment) col.iterator().next();
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return assign;
	}

	public Assignment getLatestAssignmentByRequestId(String requestId){
		Assignment assign = new Assignment();
		String sql = "SELECT TOP 1 e.id as assignmentEquipmentId, e.checkedOutDate, e.takenBy, e.preparedBy, e.assignmentLocation," +
		"loc.name as storeLocation " +
		"FROM fms_eng_assignment_equipment e LEFT JOIN fms_eng_assignment a ON (e.groupId = a.groupId) " +
		"LEFT JOIN fms_facility_item i ON e.barcode = i.barcode " +
		"LEFT JOIN fms_facility_location loc ON i.location_id = loc.setup_id " +
		"WHERE a.requestId = ? " +
		"ORDER BY checkedOutDate DESC, loc.name DESC ";
		Collection col = new ArrayList();

		try {
			col = super.select(sql, Assignment.class, new String[]{requestId}, 0, -1);
			if (col != null && col.size() >0) {
				assign = (Assignment) col.iterator().next();
			}
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return assign;
	}

	public Collection getChildFacilityAssignments(String assignmentId){
		String sql="Select e.assignmentId,requiredFrom,e.barcode," +
		" requiredTo,fromTime,toTime,e.status,e.rateCardCategoryId,c.name as rateCardCategoryName," +
		" e.checkedOutBy,e.checkedOutDate,e.checkedInBy,e.checkedInDate, loc.name as storeLocation " +
		" from fms_eng_assignment_equipment e " +
		" LEFT JOIN fms_eng_rate_card_category c on c.id=e.rateCardCategoryId " +
		" LEFT JOIN fms_facility_item i ON e.barcode = i.barcode" +
		" LEFT JOIN fms_facility_location loc ON i.location_id = loc.setup_id" +
		" where e.assignmentId=?  " +
		" order by rateCardCategoryName desc";
		Collection col=new ArrayList();
		try{
			col= super.select(sql, Assignment.class, new String[]{assignmentId}, 0, -1);
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}

	public Collection getGroupAssignments(String requestId) {
		String sql = "SELECT distinct(a.groupId) AS groupId " +
		"FROM fms_eng_assignment a " +
		"WHERE a.requestId = ? ";
		Collection col = new ArrayList();
		try {
			col = super.select(sql, Assignment.class, new String[] { requestId }, 0, -1);
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return col;
	}

	public Collection getChildFacilityAssignmentsByGroupId(Collection groupIds){
		String sql="Select a.code, a.serviceType, e.assignmentId,e.requiredFrom,e.barcode," +
		" requiredTo,fromTime,toTime,e.status,e.rateCardCategoryId,c.name as rateCardCategoryName," +
		" e.checkedOutBy,e.checkedOutDate,e.checkedInBy,e.checkedInDate, loc.name as storeLocation " +
		//" from fms_eng_assignment_equipment e " +
		" from fms_eng_assignment a INNER JOIN fms_eng_assignment_equipment e on a.assignmentId=e.assignmentId " + 
		" LEFT JOIN fms_eng_rate_card_category c on c.id=e.rateCardCategoryId " +
		" LEFT JOIN fms_facility_item i ON e.barcode = i.barcode" +
		" LEFT JOIN fms_facility_location loc ON i.location_id = loc.setup_id" +
		" where 1=1 ";
		if (groupIds!=null && groupIds.size()>0){
			sql += " AND (";
			int x = 0;
			for (Iterator i = groupIds.iterator(); i.hasNext();){
				Assignment asg = (Assignment)i.next();
				sql += " e.groupId = '" + asg.getGroupId() + "'";
				x++;
				if (x < groupIds.size()){
					sql += " OR ";
				}

			}
			sql += ") ";
		}

		//sql += " order by rateCardCategoryName desc";
		sql += " order by a.serviceType asc";

		Collection col=new ArrayList();
		try{
			col= super.select(sql, Assignment.class, null, 0, -1);
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}

	public Collection getChildFacilityAssignmentsByGroupIdMap(String groupIds){
		String sql="Select a.code, a.serviceType, a.requestId, a.serviceId, e.assignmentId,e.requiredFrom,e.barcode, e.groupId, " +
		" requiredTo,fromTime,toTime,e.status,e.rateCardCategoryId,f.name as facilityName, c.name as rateCardCategoryName," +
		"e.takenBy, e.preparedBy," +
		" e.checkedOutBy,e.checkedOutDate,e.checkedInBy,e.checkedInDate, e.reasonUnfulfilled, e.assignmentLocation," +
		"loc.name as storeLocation " +
		" from fms_eng_assignment a RIGHT JOIN fms_eng_assignment_equipment e on a.assignmentId=e.assignmentId " + 
		" LEFT JOIN fms_eng_rate_card_category c on c.id=e.rateCardCategoryId " +
		" LEFT JOIN fms_facility_item i ON e.barcode = i.barcode" +
		" LEFT JOIN fms_facility_location loc ON i.location_id = loc.setup_id" +
		" LEFT JOIN fms_facility f ON (i.facility_id = f.id)" +
		" where 1=1 ";

		sql += " AND (e.groupId = ?) ";
		sql += " order by e.createdDate ASC, a.serviceType asc";

		Collection col=new ArrayList();
		try{
			col= super.select(sql, Assignment.class, new Object[] {groupIds}, 0, -1);
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}

	public String getRateCardCategoryByBarcode(String barcode){
		String sql = "";
		ArrayList params = new ArrayList();

		sql = "SELECT cc.name as rateCardCategoryName " +
		"FROM fms_facility_item i INNER JOIN " +
		"fms_eng_rate_card_cat_item c ON (i.facility_id = c.facilityId) " +
		"INNER JOIN fms_eng_rate_card_category cc ON (c.categoryId = cc.id) " +
		"where i.barcode = ?";
		params.add(barcode);		

		String name="";
		try{
			Collection col=super.select(sql, Assignment.class, params.toArray(), 0, 1);
			if(col!=null && col.size()>0){
				Assignment asg = (Assignment) col.iterator().next();
				name= asg.getRateCardCategoryName();
			}
		}catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return name;
	}

	public Collection selectHOURequestList(Date requiredTo, String search, Date requiredFrom, String status, String department, String userId, String sort, boolean desc, int start, int rows) throws DaoException{
		ArrayList args = new ArrayList();

		String sql = 
			"SELECT DISTINCT(r.requestId), r.title, r.requestType, r.clientName, r.programType, r.program, " +
			"r.requiredFrom, r.requiredTo, r.createdBy, r.createdOn, r.modifiedBy, r.modifiedOn, " +
			"r.status, r.state, r.submittedDate " +
			"FROM fms_eng_request r " +
			"INNER JOIN security_user u ON (r.createdBy=u.username) " +
			"INNER JOIN fms_unit d ON (u.unit=d.id) " +
			"WHERE d.status='1' ";
		
		// FC must be assigned
		sql += "AND r.fcId IS NOT NULL ";

		if (status != null && !"-1".equals(status)) {
			sql += "AND r.status= '"+status+"' ";
		} else {
			sql += "AND (" +
			"r.status <> '" + EngineeringModule.DRAFT_STATUS + "' AND " +
			"r.status <> '" + EngineeringModule.PENDING_STATUS + "' AND " +
			"r.status <> '" + EngineeringModule.PROCESS_STATUS + "' AND " +
			"r.status <> '" + EngineeringModule.FC_ASSIGNED_STATUS + "' AND " +
			"r.status <> '" + EngineeringModule.OUTSOURCED_STATUS + "' AND " +
			"r.status <> '" + EngineeringModule.REJECTED_STATUS + "'" +
			") ";
		}

		if (status != null && EngineeringModule.ASSIGNMENT_STATUS.equals(status)) {
			sql += "AND ( ((Select count(*) AS COUNT FROM fms_eng_assignment a  where ( a.requestId= r.requestId ))" +
			" <=0) " +
			" OR " +
			"  ((SELECT COUNT(*) " +
			"  FROM fms_eng_assignment a INNER JOIN " +
			"  fms_eng_assignment_manpower am ON (a.assignmentId = am.assignmentId) " +
			"  WHERE a.requestId = r.requestId " +
			"  AND am.status = '" + EngineeringModule.ASSIGNMENT_MANPOWER_STATUS_NEW + "' " +
			"  ) > 0) " +
			") ";
		}

		sql += "AND ((SELECT count (distinct ru.id) " +
		"FROM fms_eng_request_unit ru  LEFT JOIN fms_unit un ON (ru.unitId =un.id) " +
		"LEFT JOIN fms_unit_alternate_approver app ON (un.id = app.unitId) " +
		"WHERE ru.requestId = r.requestId and " +
		"(un.HOU = ? " +
		"OR  " +
		"app.userId = ? )) > 0 ) ";
		args.add(userId);
		args.add(userId);

		if (department != null && !"-1".equals(department)) {
			sql += "AND d.department_id = '"+department+"' ";
		}
		if (search != null && !"".equals(search)) {
			sql += "AND (r.requestId LIKE '%" +search+ "%' OR r.title like '%"+search+"%' OR r.description like '%"+search+"%') ";
		}
		
		if(requiredFrom != null && requiredTo != null){
			sql += "AND (r.requiredFrom <= ? AND r.requiredTo >= ?) ";
			args.add(requiredTo);
			args.add(requiredFrom);
		}

		sql += 
			"GROUP BY r.requestId, r.title, r.requestType, r.clientName, r.programType, r.program, " +
			"r.requiredFrom, r.requiredTo, r.createdBy, r.createdOn, r.modifiedBy, r.modifiedOn, " +
			"r.status, r.state, r.submittedDate ";

		if (sort != null && !"".equals(sort)) {
			if (sort.equals("requestIdWithLink")) {
				sort = "requestId";
			} else if (sort.equals("requiredDateFrom")) {
				sort = "requiredFrom";
			}

			sql += "ORDER BY r."+sort+" ";
		} else {
			sql+="ORDER BY r.modifiedOn DESC ";
		}
		if (desc) {
			sql += "DESC ";
		}
		return super.select(sql, EngineeringRequest.class, args.toArray(), start, rows);
	}

	public int countHOURequestList(Date requiredTo, String search, Date requiredFrom, String status, String department, String userId) throws DaoException{
		ArrayList args = new ArrayList();
		String sql = 
			"SELECT COUNT (DISTINCT r.requestId) AS COUNT " +
			"FROM fms_eng_request r " +
			"INNER JOIN security_user u ON (r.createdBy=u.username) " +
			"INNER JOIN fms_unit d ON (u.unit=d.id) " +
			"WHERE d.status='1' ";

		if (status != null && !"-1".equals(status)) {
			sql += "AND r.status= '"+status+"' ";
		} else {
			sql += "AND (" +
			"r.status <> '" + EngineeringModule.DRAFT_STATUS + "' AND " +
			"r.status <> '" + EngineeringModule.PENDING_STATUS + "' AND " +
			"r.status <> '" + EngineeringModule.PROCESS_STATUS + "' AND " +
			"r.status <> '" + EngineeringModule.FC_ASSIGNED_STATUS + "' AND " +
			"r.status <> '" + EngineeringModule.OUTSOURCED_STATUS + "' AND " +
			"r.status <> '" + EngineeringModule.REJECTED_STATUS + "'" +
			") ";
		}

		if (status != null && EngineeringModule.ASSIGNMENT_STATUS.equals(status)) {
			sql += "AND ( ((Select count(*) AS COUNT FROM fms_eng_assignment a  where ( a.requestId= r.requestId ))" +
				" <=0) " +
				" OR " +
				"  ((SELECT COUNT(*) " +
				"  FROM fms_eng_assignment a INNER JOIN " +
				"  fms_eng_assignment_manpower am ON (a.assignmentId = am.assignmentId) " +
				"  WHERE a.requestId = r.requestId " +
				"  AND am.status = '" + EngineeringModule.ASSIGNMENT_MANPOWER_STATUS_NEW + "' " +
				"  ) > 0) " +
				") ";
		}

		sql += "AND ((SELECT count (distinct ru.id) " +
		"FROM fms_eng_request_unit ru  LEFT JOIN fms_unit un ON (ru.unitId =un.id) " +
		"LEFT JOIN fms_unit_alternate_approver app ON (un.id = app.unitId) " +
		"WHERE ru.requestId = r.requestId and " +
		"(un.HOU = ? " +
		"OR  " +
		"app.userId = ? )) > 0 ) ";
		args.add(userId);
		args.add(userId);

		if (department != null && !"-1".equals(department)) {
			sql += "AND d.department_id = '"+department+"' ";
		}
		if (search != null && !"".equals(search)) {
			sql += "AND (r.requestId LIKE '%" +search+ "%' OR r.title like '%"+search+"%' OR r.description like '%"+search+"%') ";
		}
		
		if(requiredFrom != null && requiredTo != null){
			sql += "AND (r.requiredFrom <= ? AND r.requiredTo >= ?) ";
			args.add(requiredTo);
			args.add(requiredFrom);
		}

		int count=0;
		try{
			Collection col= super.select(sql, HashMap.class, args.toArray(), 0, 1);
			if(col!=null && col.size()>0){
				HashMap map=(HashMap)col.iterator().next();
				count=(Integer)map.get("COUNT");
			}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return count;
	}


	public Collection getManpowerDetails(String requestId, String userId) throws DaoException{

		String sql="select r.title, a.serviceType,a.code, c.unitId, am.status " +
		"from fms_eng_request r " +
		"left join fms_eng_assignment a on a.requestId=r.requestId " +
		"left join fms_eng_assignment_manpower am on am.assignmentId=a.assignmentId " +
		"left JOIN competency c on c.competencyId=am.competencyId " +
		"left join fms_unit u on u.id=c.unitId " +
		"left join fms_unit_alternate_approver uaa on u.id = uaa.unitId " +
		"where r.requestId=? " +
		"and a.serviceType='4' " +
		"AND (u.HOU =? OR uaa.userId =?)";

		return super.select(sql, HashMap.class, new String[]{requestId, userId, userId}, 0, -1);
	}

	public int selectAssignmentSettingValue(){
		try {
			String sql = 
				"SELECT settingValue " +
				"FROM fms_eng_assignment_setting ";
			Collection col = super.select(sql, HashMap.class, null, 0, -1);
			if (col!=null && col.size()==1){
				HashMap map = (HashMap) col.iterator().next();
				int value = ((Number) map.get("settingValue")).intValue();
				return value;
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return 0;
	}

	protected Collection selectHOUTodaysAssignment(int settingValue, String search, String department, String userId, String sort,
			boolean desc,int start,int rows){
		try {
			ArrayList args = new ArrayList();
			Date dateMax = new Date();
			Date dateMin = Calendar.getInstance().getTime();
			dateMax.setDate(dateMax.getDate()+settingValue-1);  
			String sql = 
				"SELECT DISTINCT(r.requestId), r.title, r.requestType, r.clientName, r.programType, r.program, " +
				"r.requiredFrom, r.requiredTo, r.createdBy, r.createdOn, r.modifiedBy, r.modifiedOn, " +
				"r.status, r.state, r.submittedDate " +
				"FROM fms_eng_request r " +
				"INNER JOIN security_user u ON (r.createdBy=u.username) " +
				"INNER JOIN fms_unit d ON (u.unit=d.id) " +
				"WHERE d.status='1' " +
				"AND ((SELECT count (distinct ru.id) " +
				"FROM fms_eng_request_unit ru  LEFT JOIN fms_unit un ON (ru.unitId =un.id) " +
				"LEFT JOIN fms_unit_alternate_approver app ON (un.id = app.unitId) " +
				"WHERE ru.requestId = r.requestId and " +
				"(un.HOU = ? " +
				"OR  " +
				"app.userId = ? )) > 0 ) ";
			args.add(userId);
			args.add(userId);

			if (department != null && !"-1".equals(department)) {
				sql += "AND d.department_id = '"+department+"' ";
			}
			if (search != null && !"".equals(search)) {
				sql += "AND (r.requestId LIKE '%" +search+ "%' OR r.title like '%"+search+"%' OR r.description like '%"+search+"%') ";
			}	
			if (settingValue!=0){
				sql += 
					/*"AND (((CAST((STR( YEAR( r.requiredFrom ) ) + '/' + STR( MONTH( r.requiredFrom ) ) + '/' + " +
					"STR( DAY( r.requiredFrom ) )) AS DATETIME) <=? ) AND (CAST((STR( YEAR( r.requiredTo ) ) + '/' + " +
					"STR( MONTH( r.requiredTo  ) ) + '/' +STR( DAY( r.requiredTo  ) )) AS DATETIME) > ?)) " +
					"OR ((CAST((STR( YEAR( r.requiredFrom ) ) + '/' + STR( MONTH( r.requiredFrom ) ) + '/' + " +
					"STR( DAY( r.requiredFrom ) )) AS DATETIME) <=? ) AND (CAST((STR( YEAR( r.requiredTo ) ) + '/' + " +
					"STR( MONTH( r.requiredTo  ) ) + '/' +STR( DAY( r.requiredTo  ) )) AS DATETIME) > ?))) ";*/
					//"AND (r.requiredFrom <= ? AND r.requiredTo >= ? ) OR (r.requiredFrom = ? OR r.requiredFrom = ?)";
					
				/*args.add(dateMin);
				args.add(dateMin);
				args.add(dateMax);
				args.add(dateMax);*/
					"AND (r.requiredFrom <= ? AND r.requiredTo >= ?) " +
					"OR (r.requiredFrom >= ? AND r.requiredFrom <= ?) ";
				args.add(dateMin);
				args.add(dateMax);
				args.add(dateMin);
				args.add(dateMax);
			}

			sql += 
				"GROUP BY r.requestId, r.title, r.requestType, r.clientName, r.programType, r.program, " +
				"r.requiredFrom, r.requiredTo, r.createdBy, r.createdOn, r.modifiedBy, r.modifiedOn, " +
				"r.status, r.state, r.submittedDate ";

			if (sort != null && !"".equals(sort)) {
				sql += "ORDER BY "+sort+(desc ? " DESC" : " ASC") ;
			} else {
				sql+="ORDER BY r.requiredFrom ASC ";
			}
			return super.select(sql, EngineeringRequest.class, args.toArray(), start, rows);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return null;
	}
	
	
	protected Collection selectHOUTodaysAssignmentNew(int settingValue, String search, String department, String userId, String sort,
			boolean desc,int start,int rows){
		try {
			ArrayList args = new ArrayList();
			Date dateMax = new Date();
			Date dateMin = Calendar.getInstance().getTime();
			dateMax.setDate(dateMax.getDate()+settingValue-1);  
			String sql = 
				"SELECT DISTINCT (r.requestId), r.title, r.requiredFrom, r.requiredTo, r.requestType, r.clientName, r.programType, r.program, " +
				"r.createdBy, r.createdOn, r.modifiedBy, r.modifiedOn, r.status, r.state, r.submittedDate " +
				"FROM fms_eng_assignment a " +
				"INNER JOIN fms_eng_assignment_manpower m ON m.assignmentId = a.assignmentId " +
				"INNER JOIN competency c ON c.competencyId = m.competencyId " +
				"INNER JOIN fms_eng_request r ON r.requestId = a.requestId " +
				"INNER JOIN fms_unit u ON u.id = c.unitId " +
				"INNER JOIN fms_unit_alternate_approver ap ON ap.unitId = c.unitId " +
				"WHERE u.status = '1' " +
				"AND (u.HOU = ? OR ap.userId = ?) ";
			args.add(userId);
			args.add(userId);

			if (department != null && !"-1".equals(department)) {
				sql += "AND u.department_id = '"+department+"' ";
			}
			if (search != null && !"".equals(search)) {
				sql += "AND (r.requestId LIKE '%" +search+ "%' OR r.title like '%"+search+"%' OR r.description like '%"+search+"%') ";
			}	
			if (settingValue!=0){
				sql += 
					/*"AND (r.requiredFrom <= ? AND r.requiredTo >= ?) " +
					"OR (r.requiredFrom >= ? AND r.requiredFrom <= ?) ";*/
					
					"AND (m.requiredFrom BETWEEN ? AND ?) " +
					"OR (m.requiredTo BETWEEN ? AND ?) " +
					"OR (m.requiredFrom <= ? AND m.requiredTo >= ?) ";
					
					args.add(dateMin);
					args.add(dateMax);
					args.add(dateMin);
					args.add(dateMax);
					args.add(dateMin);
					args.add(dateMax);

/*					args.add(dateMax);
					args.add(dateMax);
					args.add(dateMax);
					args.add(dateMin);
					args.add(dateMax);*/

			}

			sql += 
				"GROUP BY r.requestId, r.title, r.requestType, r.clientName, r.programType, r.program, " +
				"r.requiredFrom, r.requiredTo, r.createdBy, r.createdOn, r.modifiedBy, r.modifiedOn, " +
				"r.status, r.state, r.submittedDate ";

			if (sort != null && !"".equals(sort)) {
				sql += "ORDER BY "+sort+(desc ? " DESC" : " ASC") ;
			} else {
				sql+="ORDER BY r.requiredFrom ASC ";
			}
			return super.select(sql, EngineeringRequest.class, args.toArray(), start, rows);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return null;
	}

	protected int countHOUTodaysAssignment(int settingValue, String search,String department, String userId){
		ArrayList args = new ArrayList();
		Date dateMax = new Date();
		Date dateMin = Calendar.getInstance().getTime();
		dateMax.setDate(dateMax.getDate()+settingValue-1);  

		String sql = 
			"SELECT COUNT (DISTINCT r.requestId) AS COUNT " +
			"FROM fms_eng_request r " +
			"INNER JOIN security_user u ON (r.createdBy=u.username) " +
			"INNER JOIN fms_unit d ON (u.unit=d.id) " +
			"WHERE d.status='1' " +
			"AND ((SELECT count (distinct ru.id) " +
			"FROM fms_eng_request_unit ru  LEFT JOIN fms_unit un ON (ru.unitId =un.id) " +
			"LEFT JOIN fms_unit_alternate_approver app ON (un.id = app.unitId) " +
			"WHERE ru.requestId = r.requestId and " +
			"(un.HOU = ? " +
			"OR  " +
			"app.userId = ? )) > 0 ) ";
		args.add(userId);
		args.add(userId);

		if (department != null && !"-1".equals(department)) {
			sql += "AND d.department_id = '"+department+"' ";
		}
		if (search != null && !"".equals(search)) {
			sql += "AND (r.requestId LIKE '%" +search+ "%' OR r.title like '%"+search+"%' OR r.description like '%"+search+"%') ";
		}

		if (settingValue!=0){
			sql += 
				" AND ((CAST((STR( YEAR( r.requiredFrom ) ) + '/' + STR( MONTH( r.requiredFrom ) ) + '/' + " +
				"STR( DAY( r.requiredFrom ) )) AS DATETIME) <=? ) AND " +
				"(CAST((STR( YEAR( r.requiredTo ) ) + '/' + STR( MONTH( r.requiredTo  ) ) + '/' +STR( DAY( r.requiredTo  ) )) AS DATETIME) " +
				"> ?)) ";
			args.add(dateMin);
			args.add(dateMax);
		}

		int count=0;
		try{
			Collection col= super.select(sql, HashMap.class, args.toArray(), 0, 1);
			if(col!=null && col.size()>0){
				HashMap map=(HashMap)col.iterator().next();
				count=(Integer)map.get("COUNT");
			}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return count;
	}

	protected Collection selectManpowerAssignmentDetails(String userId, String requestId, String sort, boolean desc, int start,int rows){
		try {
			ArrayList args= new ArrayList();
			String sql =
				"SELECT a.requestId, a.code as assignmentCode, c.competencyName, u.firstName +' '+u.lastName as fullName, " +
				"m.fromTime +'-'+ m.toTime as requiredTime, m.status, m.userId " +
				"FROM fms_eng_assignment a " +
				"INNER JOIN fms_eng_assignment_manpower m " +
				"ON a.assignmentId = m.assignmentId " +
				"INNER JOIN competency c " +
				"ON m.competencyId = c.competencyId " +
				"LEFT JOIN security_user u " +
				"ON m.userId = u.id " +
				"WHERE a.requestId = ? " +
				"AND c.unitId IN (SELECT unit FROM security_user WHERE id=?) ";

			args.add(requestId);
			args.add(userId);
			
			if(sort!=null && !sort.equals("")){
				sql += "ORDER BY "+sort+(desc ? " DESC" : " ASC");
			}else{
				sql += "ORDER BY u.firstName ASC ";
			}

			return super.select(sql, EngineeringRequest.class,  args.toArray(), start, rows);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return null;
	}

	protected EngineeringRequest selectTodaysRequestDetail(String requestId){
		try {
			String sql =
				"SELECT r.title, CONVERT(VARCHAR(11), r.requiredFrom, 106) +' - ' + CONVERT(VARCHAR(11), " +
				"r.requiredTo, 106) as requiredDate, p.programName " +
				"FROM fms_eng_request r " +
				"LEFT JOIN fms_prog_setup p " +
				"ON r.program = p.programId " +
				"WHERE r.requestId = ? ";
			Collection col = super.select(sql, EngineeringRequest.class, new String[] {requestId}, 0, 1);
			if(col!=null && col.size()==1){
				EngineeringRequest req = (EngineeringRequest) col.iterator().next();
				return req;
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return null;
	}

	protected int countManpowerAssignment(String userId, String requestId) {
		try {
			ArrayList args= new ArrayList();
			String sql = 
				"SELECT count(*) as total " +
				"FROM fms_eng_assignment a " +
				"INNER JOIN fms_eng_assignment_manpower m ON a.assignmentId = m.assignmentId " +
				"INNER JOIN competency c ON m.competencyId = c.competencyId " +
				"LEFT JOIN security_user u ON m.userId = u.id " +
				"WHERE a.requestId = ? " +
				"AND c.unitId IN (SELECT unit FROM security_user WHERE id=?) ";
			args.add(requestId);
			args.add(userId);
			Collection col = super.select(sql, HashMap.class, args.toArray(), 0, 1);

			if (col!=null && col.size()==1){
				HashMap map = (HashMap) col.iterator().next();
				int total = ((Number) map.get("total")).intValue();
				return total;
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return 0;
	}

	protected HashMap[] getAssignmentsForAutoAssignment(String requestId, String serviceId, String unitHeadId){
		ArrayList args = new ArrayList();
		String unitHeadJoin = "";

		try {
			if(unitHeadId!=null && !unitHeadId.equals("")){
				unitHeadJoin = 
					"inner join competency c ON m.competencyId = c.competencyId " +
					"inner join fms_unit u ON c.unitId = u.id " +
					"inner join fms_unit_alternate_approver ap ON ap.unitId = c.unitId ";
			}

			String sql = 
				"SELECT DISTINCT m.assignmentId, m.rateCardId, m.competencyId, m.requiredFrom, m.requiredTo, a.groupId " +
				"FROM fms_eng_assignment_manpower m " +
				"inner join fms_eng_assignment a ON m.assignmentId = a.assignmentId " 
				+ unitHeadJoin + 
				"inner join fms_rate_card rc ON rc.id = m.rateCardId " +
				"inner join fms_rate_card_manpower rcm ON rcm.rateCardId = m.rateCardId " +
				"WHERE m.userId IS NULL " +
				"AND m.status = 'N' " +
				"AND rc.status != '0' " +
				"AND rc.serviceTypeId = '5' " +
				"AND a.requestId = ? " +
				"AND a.serviceId = ? ";

			args.add(requestId);
			args.add(serviceId);

			if(unitHeadId!=null && !unitHeadId.equals("")){
				sql += "AND (u.HOU=? OR ap.userId = ?) ";

				args.add(unitHeadId);
				args.add(unitHeadId);
			}

			Collection col = super.select(sql, HashMap.class, args.toArray(), 0, -1);

			if(col!=null && col.size()>0){
				HashMap[] assignments = new HashMap[col.size()];
				int i = 0;
				for(Iterator iter = col.iterator(); iter.hasNext(); ){
					assignments[i] = (HashMap) iter.next();

					i++;
				}

				return assignments;
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return null;
	}

	protected String[] getUserIdFromWorkingProfile(String rateCardId, String unitHeadId, String altApprover, Date dateFrom , Date dateTo){
		ArrayList args = new ArrayList();

		try {
			String sql =
				"SELECT distinct m.userId " +
				"FROM fms_working_profile_duration_manpower m " +
				"inner join fms_working_profile_duration d ON d.workingProfileDurationId = m.workingProfileDurationId " +
				"inner join competency_user cu ON m.userId = cu.userId " +
				"inner join competency c ON cu.competencyId = c.competencyId " +
				"inner join fms_unit u ON c.unitId = u.id " +
				"inner join fms_unit_alternate_approver ap ON ap.unitId = c.unitId " +
				"WHERE (u.HOU=? OR ap.userId = ?) " +
				"AND (studio1 = ? OR studio2 = ? OR studio3 = ? OR studio4 = ? OR studio5 = ? OR studio6 = ? OR studio7 = ? OR studio8 = ?) " +
				"AND d.startDate <=? and d.endDate >=? ";

			args.add(unitHeadId);
			if(altApprover!=null && !altApprover.equals("")){
				args.add(altApprover);
			}else{
				args.add(unitHeadId);
			}
			args.add(rateCardId);
			args.add(rateCardId);
			args.add(rateCardId);
			args.add(rateCardId);
			args.add(rateCardId);
			args.add(rateCardId);
			args.add(rateCardId);
			args.add(rateCardId);
			args.add(dateFrom);
			args.add(dateTo);

			Collection col =  super.select(sql, HashMap.class, args.toArray(), 0, -1); 
			if(col!=null && col.size()>0){
				String[] employee = new String[col.size()];
				int i = 0;
				for(Iterator iter = col.iterator(); iter.hasNext(); ){
					HashMap map = (HashMap) iter.next();
					employee[i] = (String) map.get("userId");

					i++;
				}

				return employee;
			}

		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return null;
	}

	protected String[] getUserIdFromWorkingProfileForAutoTimeAssign(String rateCardId, String competencyId, Date dateFrom , Date dateTo){
		ArrayList args = new ArrayList();

		try {
			String sql =
				"SELECT distinct m.userId " +
				"FROM fms_working_profile_duration_manpower m " +
				"inner join fms_working_profile_duration d ON d.workingProfileDurationId = m.workingProfileDurationId " +
				"inner join competency_user cu ON m.userId = cu.userId " +
				"inner join competency c ON cu.competencyId = c.competencyId " +
				"WHERE c.competencyId = ? " +
				"AND (studio1 = ? OR studio2 = ? OR studio3 = ? OR studio4 = ? OR studio5 = ? OR studio6 = ? OR studio7 = ? OR studio8 = ?) " +
				"AND d.startDate <=? and d.endDate >=? ";

			args.add(competencyId);
			args.add(rateCardId);
			args.add(rateCardId);
			args.add(rateCardId);
			args.add(rateCardId);
			args.add(rateCardId);
			args.add(rateCardId);
			args.add(rateCardId);
			args.add(rateCardId);
			args.add(dateFrom);
			args.add(dateTo);

			Collection col =  super.select(sql, HashMap.class, args.toArray(), 0, -1); 
			if(col!=null && col.size()>0){
				String[] employee = new String[col.size()];
				int i = 0;
				for(Iterator iter = col.iterator(); iter.hasNext(); ){
					HashMap map = (HashMap) iter.next();
					employee[i] = (String) map.get("userId");

					i++;
				}

				return employee;
			}

		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return null;
	}

	protected boolean searchAssignedEmployee(String requestId, String employeeId) {
		ArrayList args = new ArrayList();
		try {
			String sql = 
				"SELECT m.userId " +
				"FROM fms_eng_assignment_manpower m " +
				"INNER JOIN fms_eng_assignment a ON m.assignmentId = a.assignmentId " +
				"WHERE a.requestId = ? " +
				"AND m.userId = ?";
			args.add(requestId);
			args.add(employeeId);

			Collection col = super.select(sql, HashMap.class, args.toArray(), 0, 1);

			if(col!=null && col.size()>0){

				return true;
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return false;
	}
	
	protected String getMostNotBusyManpower(String rateCardId, String unitHeadId, Date dateFrom , Date dateTo){
		ArrayList args = new ArrayList();

		try {
			String sql =
				"SELECT count(distinct am.userId) as total, am.userId " +
				"FROM fms_eng_assignment_manpower am " +
				"INNER JOIN fms_working_profile_duration_manpower m ON m.userId = am.userId " +
				"INNER JOIN fms_working_profile_duration d ON d.workingProfileDurationId = m.workingProfileDurationId " +
				"INNER JOIN competency_user cu ON am.userId = cu.userId " +
				"INNER JOIN competency c ON cu.competencyId = c.competencyId " +
				"INNER JOIN fms_unit u ON c.unitId = u.id " +
				"INNER JOIN fms_unit_alternate_approver ap ON ap.unitId = c.unitId " +
				"WHERE (u.HOU=? OR ap.userId = ?) " +
				"AND (studio1 = ? OR studio2 = ? OR studio3 = ? OR studio4 = ? OR studio5 = ? OR studio6 = ? OR studio7 = ? OR studio8 = ?) " +
				"AND d.startDate <=? and d.endDate >=? " +
				"GROUP BY am.userId ";

			args.add(unitHeadId);
			args.add(unitHeadId);
			args.add(rateCardId);
			args.add(rateCardId);
			args.add(rateCardId);
			args.add(rateCardId);
			args.add(rateCardId);
			args.add(rateCardId);
			args.add(rateCardId);
			args.add(rateCardId);
			args.add(dateFrom);
			args.add(dateTo);

			Collection col =  super.select(sql, HashMap.class, args.toArray(), 0, 1); 
			if(col!=null && col.size()==1){
				HashMap map = (HashMap) col.iterator().next();
				return (String) map.get("userId");
			}

		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return null;
	}

	protected void updateAssignmentManpower(String userId, String assignmentId){
		ArrayList args = new ArrayList();

		try {
			String sql =
				"UPDATE fms_eng_assignment_manpower " +
				"SET userId = ?, " +
				"status = 'A' " +
				"WHERE assignmentId = ? ";
			args.add(userId);
			args.add(assignmentId);

			super.update(sql, args.toArray());
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}

	protected boolean searchEmpAvailability(String userId, Date dateFrom , Date dateTo, String groupId){
		ArrayList args = new ArrayList();
		try {
			String sql = 
				"select userId " +
				"from fms_eng_assignment_manpower " +
				"where userId = ? ";
			args.add(userId);
			
			if(dateFrom != null && !dateFrom.equals("")){
				sql += "AND requiredFrom <=? ";
				args.add(dateFrom);
			}
			
			if(dateTo != null && !dateTo.equals("")){
				sql += "AND requiredTo >=? ";
				args.add(dateTo);
			}
			
			if(groupId != null && !groupId.equals("")){
				sql += "AND groupId = ? ";
				args.add(groupId);
			}
			Collection col = super.select(sql, HashMap.class, args.toArray(), 0, 1);

			if(col!=null && col.size()>0){
				return true;
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return false;
	}

	protected Collection getServiceIdsAndQttyFromTiedSstudio(String requestId, Collection competencyId){
		ArrayList args = new ArrayList();
		
		String sql=
		" select est.id as serviceId,rcm.quantity, est.blockBooking, rc.id " +
		" from fms_eng_service_studio est " +
		" inner join fms_rate_card rc on rc.id=est.facilityId " +
		" inner JOIN fms_rate_card_manpower rcm ON (rc.id = rcm.rateCardId) " +
		" INNER JOIN competency com ON (rcm.manpower = com.competencyId) " +
		" where rc.status!='0' AND rc.serviceTypeId='5' and est.requestId = ? ";
		args.add(requestId);
		
		if(competencyId!=null && competencyId.size()>0){
			sql += "AND ( ";
			int i = 0;
			for(Iterator iter = competencyId.iterator(); iter.hasNext(); ){
				HashMap map = (HashMap) iter.next();
				String compId = (String) map.get("competencyId");
				
				sql += " rcm.manpower = ? ";
				args.add(compId);
				
				if(i!=competencyId.size()-1){
					sql += " OR ";
				}
				i++;
			}
			sql += " ) ";
		}
		try{
			return super.select(sql, HashMap.class, args.toArray(), 0, -1);

		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return null;
	} 
	
	public Collection getCompetencyId(String houId){
		try {
			String sql = 
				"SELECT c.competencyId " +
				"FROM competency c " +
				"INNER JOIN fms_unit u ON u.id = c.unitId " +
				"INNER JOIN fms_unit_alternate_approver ap ON ap.unitId = c.unitId " +
				"WHERE (u.HOU=? OR ap.userId = ?)";
			
			return super.select(sql, HashMap.class, new String[] {houId, houId}, 0, -1);
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return null;
	}
	
	public HashMap getManpowerBookedDate(String requestId, Collection competencyId){
		ArrayList args = new ArrayList();
		try {
			String sql = 
				"SELECT bookFrom, bookTo " +
				"FROM fms_facility_booking " +
				"WHERE requestId = ? ";
			args.add(requestId);
			
			if(competencyId!=null && competencyId.size()>0){
				sql += "AND ( ";
				int i = 0;
				for(Iterator iter = competencyId.iterator(); iter.hasNext(); ){
					HashMap map = (HashMap) iter.next();
					String compId = (String) map.get("competencyId");
					
					sql += " rcm.manpower = ? ";
					args.add(compId);
					
					if(i!=competencyId.size()-1){
						sql += " OR ";
					}
					i++;
				}
				sql += " ) ";
			}
			
			Collection col = super.select(sql, HashMap.class, args.toArray(), 0, -1);
			if(col!=null && col.size()>0){
				HashMap map = (HashMap) col.iterator().next();
				return map;
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return null;
	}
	
	protected String[] getUserIdFromLastGroupId(String requestId, String groupId){
		ArrayList args = new ArrayList();
		try {
			String sql =
				"SELECT userId " +
				"FROM fms_eng_assignment a " +
				"INNER JOIN fms_eng_assignment_manpower m ON m.assignmentId = a.assignmentId " +
				"WHERE requestId = ? " +
				"AND groupId LIKE '%"+groupId+"%'";
			args.add(requestId);
			
			Collection col =  super.select(sql, HashMap.class, args.toArray(), 0, -1);
			int i = 0;
			if(col!=null && col.size()>0){
				String[] userIds = new String[col.size()];
				for(Iterator iter = col.iterator(); iter.hasNext(); ){
					HashMap map = (HashMap) iter.next();
					userIds[i] = (String) map.get("userId");
					i++;
				}
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return null;
	}
	
	protected boolean isManpowerAvailableFOrToday(String userId, Date dateFrom, Date dateTo){
		ArrayList args = new ArrayList();
		try {
			String sql =
				"SELECT m.userId " +
				"FROM fms_working_profile_duration_manpower m " +
				"INNER JOIN fms_working_profile_duration d ON d.workingProfileDurationId = m.workingProfileDurationId " +
				"WHERE m.userId = ? AND d.startDate <=? and d.endDate >=? ";
			args.add(userId);
			args.add(dateFrom);
			args.add(dateTo);
			
			Collection col = super.select(sql, HashMap.class, args.toArray(), 0, -1);
			if(col!=null && col.size()>0){
				return false;
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return true;
	}
	
	protected HashMap getUnitHeadId(String competencyId){
		ArrayList args = new ArrayList();
		try {
			String sql = 
				"SELECT u.HOU, ap.userId " +
				"FROM fms_unit u " +
				"INNER JOIN competency c ON c.unitId = u.id " +
				"INNER JOIN fms_unit_alternate_approver ap ON ap.unitId = u.id " +
				"WHERE competencyId = ? ";
			args.add(competencyId);
			
			Collection col = super.select(sql, HashMap.class, args.toArray(), 0, -1);
			if(col!=null && col.size()>0){
				return (HashMap) col.iterator().next();
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return null;
	}

	public Collection getAssignmentsByRequestId(String requestId, boolean notAssignedOnly) throws DaoException
	{
		String sql =
			" SELECT am.assignmentId, am.rateCardId, am.competencyId, am.requiredFrom, am.requiredTo, a.groupId, " +
			" am.fromTime, am.toTime " + 
			" FROM fms_eng_assignment_manpower am " + 
			" INNER JOIN fms_eng_assignment a ON (a.assignmentId = am.assignmentId) " +
			" INNER JOIN fms_rate_card rc ON (rc.id=am.rateCardId) " + 
			" WHERE  1=1 " +
			" AND serviceTypeId = '4' " + 
			" AND a.requestId = ? ";
		
		if(notAssignedOnly)
		{
			sql += " AND (userId IS NULL OR userId = '') ";
		}
		
		Collection result = super.select(sql, DefaultDataObject.class, new Object[]{requestId}, 0, -1);
		return result;
	}

	public Collection getUserIdFromWorkingProfile(String rateCardId, Date requiredFrom, Date requiredTo) throws DaoException
	{
		String sql =
			" SELECT DISTINCT(m.userId) AS userId " +
			" FROM fms_working_profile_duration_manpower m " +
			" INNER JOIN fms_working_profile_duration d ON (d.workingProfileDurationId = m.workingProfileDurationId) " +
			" WHERE 1=1 " +
			" AND (  studio1 = ? " +
			"	OR studio2 = ? " +
			"	OR studio3 = ? " +
			"	OR studio4 = ? " +
			"	OR studio5 = ? " +
			"	OR studio6 = ? " +
			"	OR studio7 = ? " +
			"	OR studio8 = ? " +
			" ) " +
			" AND (d.startDate <= ? AND d.endDate >= ?) ";
		
		ArrayList param = new ArrayList();
		param.add(rateCardId);
		param.add(rateCardId);
		param.add(rateCardId);
		param.add(rateCardId);
		param.add(rateCardId);
		param.add(rateCardId);
		param.add(rateCardId);
		param.add(rateCardId);
		param.add(requiredFrom);
		param.add(requiredTo);
		
		Collection result = super.select(sql, DefaultDataObject.class, param.toArray(), 0, -1);
		return result;
	}

	public String getMostNotBusyManpower(String rateCardId, Date requiredFrom, Date requiredTo) throws DaoException
	{
		String sql = 
			" SELECT COUNT(DISTINCT am.userId) AS total, am.userId " +
			" FROM fms_eng_assignment_manpower am " +
			" INNER JOIN fms_working_profile_duration_manpower m ON m.userId = am.userId " +
			" INNER JOIN fms_working_profile_duration d ON d.workingProfileDurationId = m.workingProfileDurationId " +
			" WHERE 1=1 " +
			" AND ( studio1 = ? " +
			"	OR studio2 = ? " +
			"	OR studio3 = ? " +
			"	OR studio4 = ? " +
			"	OR studio5 = ? " +
			"	OR studio6 = ? " +
			"	OR studio7 = ? " +
			"	OR studio8 = ? " +
			" ) " +
			" AND (d.startDate <= ? AND d.endDate >= ?) " +  
			" GROUP BY am.userId " +
			" ORDER BY total ";
		
		ArrayList args = new ArrayList();
		args.add(rateCardId);
		args.add(rateCardId);
		args.add(rateCardId);
		args.add(rateCardId);
		args.add(rateCardId);
		args.add(rateCardId);
		args.add(rateCardId);
		args.add(rateCardId);
		args.add(requiredFrom);
		args.add(requiredTo);
		
		Collection col =  super.select(sql, HashMap.class, args.toArray(), 0, 1); 
		if(col!=null && col.size()==1)
		{
			HashMap map = (HashMap) col.iterator().next();
			return (String) map.get("userId");
		}
		return null;
	}

	public DefaultDataObject getAutoAssignmentSchedSetting() throws DaoException
	{
		String sql =
			" SELECT settingId, settingValue, scheduledDate " +
			" FROM fms_eng_auto_assignment_setting ";
		
		Collection result = super.select(sql, DefaultDataObject.class, null, 0, 1);
		if(result != null && !result.isEmpty())
		{
			return (DefaultDataObject) result.iterator().next();
		}
		return null;
	}

	public Collection getRequestIdsByDateRange(Date startDate, Date endDate) throws DaoException
	{
		ArrayList param = new ArrayList();
		String sql =
			" SELECT DISTINCT(r.requestId) AS id " +
			" FROM fms_eng_request r " +
			" INNER JOIN fms_eng_assignment a ON (r.requestId=a.requestId) " +
			" INNER JOIN fms_eng_assignment_manpower am ON (a.assignmentId=am.assignmentId) " +
			" WHERE 1=1 " +
			" AND a.serviceType = '4' " +
			" AND ( r.requiredFrom <= ? AND r.requiredTo >= ? ) ";
		
		if(startDate != null && endDate != null)
		{
			param.add(startDate);
			param.add(endDate);
		}
		else if(startDate != null)
		{
			param.add(startDate);
			param.add(startDate);
		}
		
		Collection result = super.select(sql, DefaultDataObject.class, param.toArray(), 0, -1);
		return result;
	}

	public Collection getStudioRateCardByRequestId(String requestId) throws DaoException
	{
		String sql =
			" SELECT rc.id " +
			" FROM fms_eng_service_studio est " +
			" INNER JOIN fms_rate_card rc on rc.id=est.facilityId " +  
			" WHERE rc.status!='0' " +
			" AND rc.serviceTypeId='5' " + 
			" AND est.requestId = ? ";
			
		Collection result = super.select(sql, DefaultDataObject.class, new Object[]{requestId}, 0, -1);
		return result;
	}
}
