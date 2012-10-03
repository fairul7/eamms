package com.tms.fms.facility.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.model.DefaultDataObject;
import kacang.services.security.User;
import kacang.util.Log;

import com.tms.crm.sales.misc.DateUtil;
import com.tms.fms.abw.model.AbwCodeObject;
import com.tms.fms.department.model.FMSUnit;
import com.tms.fms.engineering.model.Service;
import com.tms.fms.engineering.ui.ServiceDetailsForm;

public class SetupDao extends DataSourceDao {

	public void init() throws DaoException {

		try {
			String sql = "CREATE TABLE fms_working_profile (workingProfileId varchar(255) NOT NULL PRIMARY KEY,  "
					+ " name varchar(255) NOT NULL, startTime varchar(10) NOT NULL, endTime varchar(10) NOT NULL, defaultProfile char(1) NOT NULL Default '0', "
					+ " createdBy varchar(255) NULL, createdOn datetime NULL, modifiedBy varchar(255) NULL, modifiedOn datetime NULL)";
			super.update(sql, null);
		} catch (Exception e) {
		}

		try {
			String sql = "CREATE TABLE fms_working_profile_duration (workingProfileDurationId varchar(255) NOT NULL PRIMARY KEY,  "
					+ " workingProfileId varchar(255) NOT NULL, startDate DATETIME NOT NULL, endDate DATETIME NOT NULL, "
					+ " createdBy varchar(255) NULL, createdOn datetime NULL, modifiedBy varchar(255) NULL, modifiedOn datetime NULL)";
			super.update(sql, null);
		} catch (Exception e) {
		}
		
		// create index for fms_working_profile_duration(workingProfileId)
		try {
			String sql = "CREATE INDEX workingProfileId ON fms_working_profile_duration(workingProfileId)";
			super.update(sql, null);
		} catch (Exception e) {
		}
		
		// create index for fms_working_profile_duration(startDate, endDate)
		try {
			String sql = "CREATE INDEX startDate_endDate ON fms_working_profile_duration(startDate, endDate)";
			super.update(sql, null);
		} catch (Exception e) {
		}
		
		// create index for fms_working_profile_duration(workingProfileDurationId, endDate, startDate)
		try {
			String sql = "CREATE INDEX id_endDate_startDate ON fms_working_profile_duration(workingProfileDurationId, endDate, startDate)";
			super.update(sql, null);
		} catch (Exception e) {
		}
		
		try {
			String sql = "CREATE TABLE fms_working_profile_duration_manpower (workingProfileDurationId varchar(255) NOT NULL,  "
					+ " userId varchar(255) NOT NULL, PRIMARY KEY (workingProfileDurationId,userId))";
			super.update(sql, null);
		} catch (Exception e) {
		}

		// fms_rate_card
		try {
			String sql = "CREATE TABLE fms_rate_card(id VARCHAR(255) NOT NULL PRIMARY KEY, name VARCHAR(255), "
					+ "serviceTypeId VARCHAR(255), "
					+ "description TEXT, remarksRequest TEXT, "
					+ "status CHAR(1), createdBy VARCHAR(255), createdOn datetime, modifiedBy VARCHAR(255), modifiedOn datetime "
					+ ") ";
			super.update(sql, null);
		} catch (Exception e) {
		}
		
		try {
			super.update("ALTER TABLE fms_rate_card ADD transportRequest CHAR(1)", null);
		} catch (Exception e){
		}
		
		try {
			super.update("ALTER TABLE fms_rate_card ADD vehicleCategoryId VARCHAR(255)", null);
		} catch (Exception e){
		}
		
		try {
			super.update("ALTER TABLE fms_working_profile ADD description VARCHAR(255)", null);
		} catch (Exception e){
		}

		
		// fms_rate_card_detail
		try {
			String sql = "CREATE TABLE fms_rate_card_detail(id VARCHAR(255) NOT NULL PRIMARY KEY, rateCardId VARCHAR(255), "
					+ "internalRate numeric(18,0), externalRate numeric(18,0), effectiveDate datetime, createdBy VARCHAR(255), "
					+ "createdOn datetime, modifiedBy VARCHAR(255), modifiedOn datetime)";
			super.update(sql, null);
		} catch (Exception e) {
		}
		try{
			super.update("ALTER TABLE fms_rate_card_detail ALTER COLUMN internalRate numeric(18,2)", null);
			super.update("ALTER TABLE fms_rate_card_detail ALTER COLUMN externalRate numeric(18,2)", null);
		}catch (Exception e) {
		}

		// fms_rate_card_equipment
		try {
			String sql = "CREATE TABLE fms_rate_card_equipment(id VARCHAR(255) NOT NULL PRIMARY KEY, rateCardId VARCHAR(255), "
					+ "equipment VARCHAR(255), quantity INT, createdBy VARCHAR(255), "
					+ "createdOn datetime, modifiedBy VARCHAR(255), modifiedOn datetime)";
			super.update(sql, null);
		} catch (Exception e) {
		}
		try{
			super.update("ALTER TABLE fms_rate_card_equipment ADD modifiedBy VARCHAR(255)", null);
			super.update("ALTER TABLE fms_rate_card_equipment ADD modifiedOn datetime", null);
		}catch (Exception e) {
		}
		
		//create index for fms_rate_card_detailOct2010
		try {
			super
					.update(
							"CREATE INDEX idx_fms_rate_card_detailOct2010_id on fms_rate_card_detailOct2010 (effectiveDate, rateCardId)", null);
		} catch (Exception e) {
		}
		
		//create index for fms_rate_card_detail
		try {
			super
					.update(
							"CREATE INDEX idx_fms_rate_card_detail_id on fms_rate_card_detail (effectiveDate, rateCardId)", null);
		} catch (Exception e) {
		}
		
		//create index for fms_eng_rate_card_cat_item
		try {
			super
					.update(
							"CREATE INDEX idx_fms_eng_rate_card_cat_item_id on fms_eng_rate_card_cat_item (categoryId)", null);
		} catch (Exception e) {
		}
		
		//create index for fms_rate_card
		try {
			super
					.update(
							"CREATE INDEX idx_fms_rate_card_id on fms_rate_card (status, serviceTypeId)", null);
		} catch (Exception e) {
		}
		
		//create index for fms_rate_cardOct2010
		try {
			super
					.update(
							"CREATE INDEX idx_fms_rate_cardOct2010_id on fms_rate_cardOct2010 (status, serviceTypeId)", null);
		} catch (Exception e) {
		}

		// fms_rate_card_manpower
		try {
			String sql = "CREATE TABLE fms_rate_card_manpower(id VARCHAR(255) NOT NULL PRIMARY KEY, rateCardId VARCHAR(255), "
					+ "manpower VARCHAR(255), quantity INT, createdBy VARCHAR(255), "
					+ "createdOn datetime, modifiedBy VARCHAR(255), modifiedOn datetime)";
			super.update(sql, null);
		} catch (Exception e) {
		}
		try{
			super.update("ALTER TABLE fms_rate_card_manpower ADD modifiedBy VARCHAR(255)", null);
			super.update("ALTER TABLE fms_rate_card_manpower ADD modifiedOn datetime", null);
		}catch (Exception e) {
		}
		
		// fms_eng_rate_card_category
		try {
			String sql = "CREATE TABLE fms_eng_rate_card_category " +
					"(id VARCHAR(255) NOT NULL PRIMARY KEY, " +
					"name VARCHAR(255), " +
					"createdBy VARCHAR(255), " +
					"createdOn datetime, " +
					"modifiedBy VARCHAR(255), " +
					"modifiedOn datetime)";
			super.update(sql, null);
		} catch (Exception e){
		}
		
		try {
			super.update("ALTER TABLE fms_eng_rate_card_category ALTER COLUMN name VARCHAR(500) null", null);
		} catch (Exception e){
			
		}
		
		try {
			super.update("ALTER TABLE fms_working_profile_duration_manpower ADD studio1 VARCHAR(255)", null);
			super.update("ALTER TABLE fms_working_profile_duration_manpower ADD studio2 VARCHAR(255)", null);
			super.update("ALTER TABLE fms_working_profile_duration_manpower ADD studio3 VARCHAR(255)", null);
			super.update("ALTER TABLE fms_working_profile_duration_manpower ADD studio4 VARCHAR(255)", null);
			super.update("ALTER TABLE fms_working_profile_duration_manpower ADD studio5 VARCHAR(255)", null);
			super.update("ALTER TABLE fms_working_profile_duration_manpower ADD studio6 VARCHAR(255)", null);
			super.update("ALTER TABLE fms_working_profile_duration_manpower ADD studio7 VARCHAR(255)", null);
			super.update("ALTER TABLE fms_working_profile_duration_manpower ADD studio8 VARCHAR(255)", null);
		} catch (Exception e){
			
		}
		
		// create index for fms_working_profile_duration_manpower(userId)
		try {
			String sql = "CREATE INDEX userId ON fms_working_profile_duration_manpower(userId)";
			super.update(sql, null);
		} catch (Exception e) {
		}
		
		// fms_eng_rate_card_cat_item
		try {
			String sql = "CREATE TABLE fms_eng_rate_card_cat_item " +
					"(categoryId VARCHAR(255)," +
					"facilityId VARCHAR(255))";
			super.update(sql, null);
		} catch (Exception e){
		}
		
		try 
		{
			super.update(" ALTER TABLE fms_rate_card ADD abw_code VARCHAR(10) ", null);
		} 
		catch (Exception e) {}
		
		try
		{
			super.update(
					" CREATE TABLE fms_rate_card_abw_code ( " +
					"		abw_code VARCHAR(10) NOT NULL, " +
					"		description VARCHAR(255), " +
					"		PRIMARY KEY (abw_code) " +
					" ) "
				, null);
		}
		catch(Exception e){}
		
		try
		{
			super.update(
					" CREATE TABLE rate_card_email_notification ( " +
					"		code VARCHAR(10), " +
					"		name VARCHAR(255), " +
					"		internalRate NUMERIC(18,2), " +
					"		createdDate DATETIME " +
					" ) "
				, null);
		}
		catch(Exception e){}
	}

	protected void insertWorkingProfile(WorkingProfile wp) throws DaoException {
		String sql = "INSERT INTO fms_working_profile (workingProfileId,name,startTime, endTime, defaultProfile,"
				+ "createdBy, createdOn, modifiedBy, modifiedOn, description) VALUES (#workingProfileId#,#name#,#startTime#, #endTime#, #defaultProfile#,"
				+ "#createdBy#, #createdOn#, #modifiedBy#, #modifiedOn#, #description# ) ";
		super.update(sql, wp);
	}

	protected void updateWorkingProfile(WorkingProfile wp) throws DaoException {
		String sql = "Update fms_working_profile set startTime=#startTime#, endTime=#endTime#, defaultProfile=#defaultProfile#,"
				+ " modifiedBy=#modifiedBy#, modifiedOn=#modifiedOn#, description=#description# where workingProfileId=#workingProfileId#";
		super.update(sql, wp);
	}

	public Collection selectWorkingProfile(String search, String sort,
			boolean desc, int start, int rows) throws DaoException {
		String sql = "Select workingProfileId,name,startTime, endTime, defaultProfile,createdBy, createdOn, modifiedBy, modifiedOn, description"
				+ " FROM fms_working_profile where 1=1 ";
		if (search != null && !"".equals(search)) {
			sql += " AND name like '%" + search + "%' ";
		}
		if (sort != null && !"".equals(sort)) {
			if (sort.equals("workingHours")) {
				sort = "startTime";
			}
			sql += " order by " + sort + (desc ? " DESC" : "");
		}else {
			sql += " ORDER BY name ASC ";
		}

		return super.select(sql, WorkingProfile.class, null, start, rows);
	}

	public int selectWorkingProfileCount(String search) throws DaoException {
		String sql = "Select count(*) As COUNT "
				+ " FROM fms_working_profile where 1=1 ";
		if (search != null && !"".equals(search)) {
			sql += " AND name like '%" + search + "%' ";
		}
		int count = 0;
		try {
			Collection col = super.select(sql, HashMap.class, null, 0, 1);
			if (col != null) {
				HashMap map = (HashMap) col.iterator().next();
				count = (Integer) map.get("COUNT");
			}
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return count;
	}

	public WorkingProfile selectWorkingProfile(String workingProfileId)
			throws DaoException {
		String sql = "Select workingProfileId,name,startTime, endTime, defaultProfile,createdBy, createdOn, modifiedBy, modifiedOn, description "
				+ " FROM fms_working_profile where workingProfileId=? ";
		Collection col = super.select(sql, WorkingProfile.class,
				new Object[] { workingProfileId }, 0, 1);
		try {
			return (WorkingProfile) col.iterator().next();
		} catch (Exception e) {
		}

		return null;
	}

	public void deleteRecord(String tableName, String columnName, String id)
			throws DaoException {
		String sql = "Delete FROM " + tableName + " where " + columnName
				+ " = '" + id + "'";
		super.update(sql, null);
	}
	
	public void deleteWorkingProfileDurationManpower(String workingProfileDurationId, String manpowerId)
			throws DaoException {
		String sql = "DELETE FROM fms_working_profile_duration_manpower " +
				"WHERE workingProfileDurationId = '" + workingProfileDurationId + "' " +
						"AND userId = '" + manpowerId + "' ";
		super.update(sql, null);
	}
	
	public int countWorkingProfileDurationManpower(String workingProfileDurationId) throws DaoException {
		String sql = "SELECT COUNT(*) AS COUNT FROM fms_working_profile_duration_manpower " +
				"WHERE workingProfileDurationId = ? ";
		
		ArrayList params = new ArrayList();
		params.add(workingProfileDurationId);
		
		int count = 0;
		try {
			Collection col = super.select(sql, HashMap.class, params.toArray(), 0, 1);
			if (col != null) {
				HashMap map = (HashMap) col.iterator().next();
				count = (Integer) map.get("COUNT");
			}
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return count;
		
	}

	public boolean isDuplicate(String tableName, String columnName,
			String value, String excludeColumn, String excludeId)
			throws DaoException {
		String sql = "Select count(*) As COUNT " + " FROM " + tableName
				+ " where " + columnName + " = ? ";
		if (excludeId != null && !"".equals(excludeId)) {
			sql += " AND " + excludeColumn + " != '" + excludeId + "' ";
		}
		try {
			Collection col = super.select(sql, HashMap.class, new String[] {value}, 0, 1);
			if (col != null) {
				HashMap map = (HashMap) col.iterator().next();
				int count = (Integer) map.get("COUNT");
				if (count > 0) {
					return true;
				}
			}
		} catch (Exception e) {
			Log.getLog(getClass()).error("SQL :" + sql, e);
		}
		return false;
	}

	public Collection selectUnitUsers(String search, String unitId,
			String unitHeadId, String groupId, String sort, boolean desc, int start, int rows)
			throws DaoException {
		String sql = "Select distinct su.id as id,su.username,firstName,lastName,email1,active,department,u.id as unitId,u.name as unitName"
				+ " from fms_unit u Inner join security_user su on u.id=su.unit " +
				"INNER JOIN security_user_group g ON (su.id = g.userId) " +
				"LEFT join fms_unit_alternate_approver app ON (u.id = app.unitId) " +
				"WHERE su.active='1' and u.status = '1' " +
				"AND (u.HOU ='" + unitHeadId + "' OR app.userId = '" + unitHeadId + "') ";
		if (search != null && !"".equals(search)) {
			sql += " AND ( su.username like '%" + search
					+ "%' OR su.firstName like '%" + search
					+ "%' OR su.lastName like '%" + search + "%' ) ";
		}
		if (unitHeadId != null && !"".equals(unitHeadId)) {
			sql += " AND ( HOU= '" + unitHeadId + "' ) ";
		}
		if (unitId != null && !"".equals(unitId)) {
			sql += " AND ( u.id= '" + unitId + "' ) ";
		}
		if (groupId != null && !"".equals(groupId)){
			if (!groupId.equals("-1")){
				sql += " AND (g.groupId = '" + groupId + "') ";
			}
		}
		if (sort != null && !"".equals(sort)) {
			sql += " order by " + sort + " ";
		}
		if (desc) {
			sql += " DESC ";
		}
		
		return super.select(sql, User.class, null, start, rows);
	}

	public int selectUnitUsersCount(String search, String unitId,
			String unitHeadId, String groupId) {
		String sql = "Select count(distinct(su.id)) as COUNT "
				+ " from fms_unit u Inner join security_user su on u.id=su.unit " +
				"INNER JOIN security_user_group g ON (su.id = g.userId) " +
				"LEFT join fms_unit_alternate_approver app ON (u.id = app.unitId) " +
				"WHERE su.active='1' and u.status = '1' " +
				"AND u.HOU ='" + unitHeadId + "' OR app.userId = '" + unitHeadId + "' ";	
		if (search != null && !"".equals(search)) {
			sql += " AND ( su.username like '%" + search
					+ "%' OR su.firstName like '%" + search
					+ "%' OR su.lastName like '%" + search + "%' ) ";
		}
		if (unitHeadId != null && !"".equals(unitHeadId)) {
			sql += " AND ( HOU= '" + unitHeadId + "' ) ";
		}
		if (unitId != null && !"".equals(unitId)) {
			sql += " AND ( u.id= '" + unitId + "' ) ";
		}
		if (groupId != null && !"".equals(groupId)){
			sql += " AND (g.groupId = '" + groupId + "') ";
		}
		int count = 0;
		try {
			Collection col = super.select(sql, HashMap.class, null, 0, 1);
			if (col != null) {
				HashMap map = (HashMap) col.iterator().next();
				count = (Integer) map.get("COUNT");
			}
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return count;
	}
	
	public Collection selectUnits(String unitHeadId)
			throws DaoException {
		String sql = "Select distinct u.id as id,u.name as name"
				+ " from fms_unit u " +
				//"Inner join security_user su on u.id=su.unit " +
				//"INNER JOIN security_user_group g ON (su.id = g.userId) " +
				"LEFT join fms_unit_alternate_approver app ON (u.id = app.unitId) " +
				"WHERE u.status = '1' " +
				"AND (u.HOU ='" + unitHeadId + "' OR app.userId = '" + unitHeadId + "') " +
				"ORDER BY u.name ASC ";
	
		return super.select(sql, FMSUnit.class, null, 0, -1);
	}
	
	public String selectCurrentUnits(String userId)
	throws DaoException {
		String sql = 
			"SELECT unit as unitId " +
			"FROM security_user " +
			"WHERE id = ? ";

		Collection col =  super.select(sql, HashMap.class, new String[] {userId}, 0, -1);
		
		if(col!= null && col.size()>0){
			HashMap hm = (HashMap) col.iterator().next();
			String unitId = (String) hm.get("unitId");
			return unitId;
		}
		return null;
	}
	
	public Collection selectUnitsForUnitHead(String unitHeadId)
	throws DaoException {
		String sql = 
			"Select distinct u.id as id,u.name as name"
			+ " from fms_unit u " +
			"WHERE u.status = '1' " +
			"AND u.HOU ='" + unitHeadId + "' " +
			"ORDER BY u.name ASC ";

		return super.select(sql, FMSUnit.class, null, 0, -1);
	}

	// Working Profile Duration

	public Collection selectWorkingProfileDuration(String search, String sort,
			boolean desc, int start, int rows) throws DaoException {
		String sql = "Select d.workingProfileDurationId, p.workingProfileId, p.name, d.startDate, d.endDate "
				+ " FROM fms_working_profile_duration d INNER JOIN fms_working_profile p ON p.workingProfileId=d.workingProfileId "
				+ " where 1=1 ";
		if (search != null && !"".equals(search)) {
			sql += " AND name like '%" + search + "%' ";
		}
		if (sort != null && !"".equals(sort)) {
			if (sort.equals("workingHours")) {
				sort = "startTime";
			}
			sql += " order by " + sort + " ";
		}
		if (desc) {
			sql += " DESC ";
		}

		return super.select(sql, WorkingProfile.class, null, start, rows);
	}

	public int selectWorkingProfileDurationCount(String search)
			throws DaoException {
		String sql = "Select count(*) As COUNT "
				+"FROM fms_working_profile_duration d INNER JOIN fms_working_profile p ON p.workingProfileId=d.workingProfileId "
				+"WHERE 1=1 ";
				//+ " FROM fms_working_profile where 1=1 ";
		if (search != null && !"".equals(search)) {
			sql += " AND name like '%" + search + "%' ";
		}
		int count = 0;
		try {
			Collection col = super.select(sql, HashMap.class, null, 0, 1);
			if (col != null) {
				HashMap map = (HashMap) col.iterator().next();
				count = (Integer) map.get("COUNT");
			}
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return count;
	}
	
	public Collection selectWorkingProfileDurationByManpower(String search, Map manpowers, Date startDate, Date endDate, String sort,
			boolean desc, int start, int rows) throws DaoException {
		ArrayList params=new ArrayList();
		String sql = "SELECT (u.firstName + ' ' + u.lastName) AS manpower, u.id AS manpowerId, d.workingProfileDurationId, " +
				"p.workingProfileId, p.name, d.startDate, d.endDate, " +
				"(u.id + '~' + d.workingProfileDurationId) AS wpdManpowerId " +
				"FROM fms_working_profile_duration d INNER JOIN fms_working_profile p ON (p.workingProfileId = d.workingProfileId) " +
				"INNER JOIN fms_working_profile_duration_manpower m ON (d.workingProfileDurationId = m.workingProfileDurationId) " +
				"INNER JOIN security_user u ON (m.userId = u.id) " +
				"WHERE 1=1 ";
		
		if (manpowers != null && manpowers.size()>0){
			sql += " AND (";
			int i = 1;
			for(Iterator itr = manpowers.keySet().iterator();itr.hasNext();i++){
				String userId = (String)itr.next();
				sql += "u.id = '" + userId + "' ";
				
				if (i < manpowers.size()) {
					sql += " OR ";
				}
			}
			sql += ") ";
		}
		
		if (startDate != null && endDate != null) {
			sql+=" AND (d.startDate <= ? and d.endDate >= ?) ";
			params.add(endDate);
			params.add(startDate);
		}

		if (search != null && !"".equals(search)) {
			sql += " AND ( " +
					"p.name like '%" + search + "%' " +
					"OR u.firstName like '%" + search + "%' " +
					"OR u.lastName like '%" + search + "%' " +
					"OR u.username like '%" + search + "%' " +
					")";
		}
		if (sort != null && !"".equals(sort)) {
			if (sort.equals("workingHours")) {
				sort = "startTime";
			}
			sql += " order by " + sort + " ";
		} else {
			sql += " order by d.startDate  ";
		}
		
		if (desc) {
			sql += " DESC ";
		}

		return super.select(sql, WorkingProfile.class, params.toArray(), start, rows);
	}
	
	public int selectWorkingProfileDurationCountByManpower(String search, Map manpowers, Date startDate, Date endDate)
		throws DaoException {
		ArrayList params=new ArrayList();
		String sql = "SELECT COUNT(*) AS COUNT " +
			"FROM fms_working_profile_duration d INNER JOIN fms_working_profile p ON (p.workingProfileId = d.workingProfileId) " +
			"INNER JOIN fms_working_profile_duration_manpower m ON (d.workingProfileDurationId = m.workingProfileDurationId) " +
			"INNER JOIN security_user u ON (m.userId = u.id) " +
			"WHERE 1=1 ";
		
		if (manpowers != null && manpowers.size()>0){
			sql += " AND (";
			int i = 1;
			for(Iterator itr = manpowers.keySet().iterator();itr.hasNext();i++){
				String userId = (String)itr.next();
				sql += "u.id = '" + userId + "' ";
				
				if (i < manpowers.size()) {
					sql += " OR ";
				}
			}
			sql += ") ";
		}
		
		if (startDate != null && endDate != null) {
			sql+=" AND (( d.startDate between ? AND ? ) OR ( d.endDate between ? AND ? )) ";
			params.add(startDate);
			params.add(endDate);
			params.add(startDate);
			params.add(endDate);
		}
//		String sql = "Select count(*) As COUNT "
//				+"FROM fms_working_profile_duration d INNER JOIN fms_working_profile p ON p.workingProfileId=d.workingProfileId "
//				+"WHERE 1=1 ";
				//+ " FROM fms_working_profile where 1=1 ";
		if (search != null && !"".equals(search)) {
			//sql += " AND name like '%" + search + "%' ";
			sql += " AND ( " +
				"p.name like '%" + search + "%' " +
				"OR u.firstName like '%" + search + "%' " +
				"OR u.lastName like '%" + search + "%' " +
				"OR u.username like '%" + search + "%' " +
				")";
		}
		int count = 0;
		try {
			Collection col = super.select(sql, HashMap.class, params.toArray(), 0, 1);
			if (col != null) {
				HashMap map = (HashMap) col.iterator().next();
				count = (Integer) map.get("COUNT");
			}
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return count;
	}

	protected void insertWorkingProfileDuration(WorkingProfile wp)
			throws DaoException {
		String sql = "INSERT INTO fms_working_profile_duration (workingProfileDurationId,workingProfileId,startDate, endDate,"
				+ "createdBy, createdOn, modifiedBy, modifiedOn) VALUES (#workingProfileDurationId#,#workingProfileId#,#startDate#, #endDate#,"
				+ "#createdBy#, #createdOn#, #modifiedBy#, #modifiedOn# ) ";
		super.update(sql, wp);
	}

	protected void insertDurationManpower(String workingProfileDurationId,
			String userId, String studio1,String studio2,String studio3,String studio4,String studio5,
			String studio6,String studio7,String studio8) throws DaoException {
		DefaultDataObject ddo = new DefaultDataObject();
		ddo.setProperty("workingProfileDurationId", workingProfileDurationId);
		ddo.setProperty("userId", userId);
		ddo.setProperty("studio1", studio1);
		ddo.setProperty("studio2", studio2);
		ddo.setProperty("studio3", studio3);
		ddo.setProperty("studio4", studio4);
		ddo.setProperty("studio5", studio5);
		ddo.setProperty("studio6", studio6);
		ddo.setProperty("studio7", studio7);
		ddo.setProperty("studio8", studio8);
		String sql = "INSERT INTO fms_working_profile_duration_manpower (workingProfileDurationId,userId," +
				" studio1, studio2,studio3,studio4,studio5,studio6,studio7,studio8"
				+ ") VALUES (#workingProfileDurationId#,#userId#, #studio1#, #studio2#, #studio3#, #studio4#, " +
						"#studio5#, #studio6#, #studio7#, #studio8#) ";
		super.update(sql, ddo);
	}

	protected void updateWorkingProfileDuration(WorkingProfile wp)
			throws DaoException {
		String sql = "Update fms_working_profile_duration set startDate=#startDate#, endDate=#endDate#, workingProfileId=#workingProfileId#,"
				+ " modifiedBy=#modifiedBy#, modifiedOn=#modifiedOn# where workingProfileDurationId=#workingProfileDurationId#";
		super.update(sql, wp);
	}

	public WorkingProfile selectWorkingProfileDuration(
			String workingProfileDurationId) throws DaoException {
		String sql = "Select d.workingProfileDurationId, p.workingProfileId, p.name, d.startDate, d.endDate, "
				+ " d.createdBy, d.createdOn, d.modifiedBy, d.modifiedOn"
				+ " FROM fms_working_profile_duration d INNER JOIN fms_working_profile p ON p.workingProfileId=d.workingProfileId "
				+ " where workingProfileDurationId=? ";
		Collection col = super.select(sql, WorkingProfile.class,
				new Object[] { workingProfileDurationId }, 0, 1);
		try {
			WorkingProfile wp = (WorkingProfile) col.iterator().next();
			wp.setManpowerMap(selectDurationManpower(workingProfileDurationId));
			return wp;
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}

		return null;
	}
	
	public Map selectWorkingProfileDurationManpower(
			String workingProfileDurationId) throws DaoException {
		String sql = "Select studio1,studio2,studio3,studio4,studio5,studio6,studio7,studio8"
				+ " FROM fms_working_profile_duration_manpower "
				+ " where workingProfileDurationId=? ";
		Collection col = super.select(sql, HashMap.class,
				new Object[] { workingProfileDurationId }, 0, 1);
		Map userMap = new HashMap();
		try {
			for (Iterator itr = col.iterator(); itr.hasNext();) {
				HashMap map = (HashMap) itr.next();
				userMap.put("studio1", (String) map.get("studio1"));
				userMap.put("studio2", (String) map.get("studio2"));
				userMap.put("studio3", (String) map.get("studio3"));
				userMap.put("studio4", (String) map.get("studio4"));
				userMap.put("studio5", (String) map.get("studio5"));
				userMap.put("studio6", (String) map.get("studio6"));
				userMap.put("studio7", (String) map.get("studio7"));
				userMap.put("studio8", (String) map.get("studio8"));
			}
			return userMap;
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}

		return null;
	}

	public Map selectDurationManpower(String workingProfileDurationId)
			throws DaoException {
		String sql = "Select workingProfileDurationId, userId "
				+ " FROM fms_working_profile_duration_manpower  "
				+ " where workingProfileDurationId=? ";
		Collection col = super.select(sql, HashMap.class,
				new Object[] { workingProfileDurationId }, 0, -1);
		Map userMap = new HashMap();
		for (Iterator itr = col.iterator(); itr.hasNext();) {
			HashMap map = (HashMap) itr.next();
			userMap.put((String) map.get("userId"), (String) map.get("userId"));
		}
		return userMap;
	}

	// Manpower Setup

	public Collection selectManpowerSetup(String search, String unitId, String sort,
			boolean desc, int start, int rows) throws DaoException {
		String sql = "Select u.id as userId, u.firstName,u.lastName, (u.firstName + ' ' + u.lastName) AS manpowerName, " +
				"c.competencyName,cu.competencyLevel "
				+ " FROM competency_user cu INNER JOIN security_user u ON u.id=cu.userId "
				+ "INNER JOIN competency c ON c.competencyId=cu.competencyId "
				+ " where 1=1 " +
				"AND u.unit = '" + unitId + "' ";
		
		if (search != null && !"".equals(search)) {
			sql += " AND ( u.firstName LIKE '%" + search
					+ "%' OR u.lastName LIKE '%" + search
					+ "%' OR c.competencyName LIKE '%" + search
					+ "%' OR cu.competencyLevel LIKE '%" + search + "%' ) ";
		}
		if (sort != null && !"".equals(sort)) {
//			if (sort.equals("firstName")) {
//				sql += " order by u.firstName " + (desc ? " DESC " : " ");
//			} else {
//				sql += " order by u.firstName , " + sort + (desc ? " DESC " : " ");
//			}
			sql += " order by " + sort + (desc ? " DESC " : " ");

		} else {
			sql += " ORDER BY u.firstname ASC ";
		}
		
		return super.select(sql, HashMap.class, null, start, rows);
	}
	
	// Rate Card Setup
	protected void insertRateCard(RateCard rc) throws DaoException {
		String sql = "INSERT INTO fms_rate_card (id, name, serviceTypeId, "
				+ "description, remarksRequest, status, transportRequest, vehicleCategoryId, createdBy, createdOn, modifiedBy, modifiedOn, " 
				+ "abw_code) "
				+ "VALUES (#id#,#name#, #serviceTypeId#, #description#,"
				+ "#remarksRequest#, #status#, #transportRequest#, #vehicleCategoryId#, #createdBy#, #createdOn#, #modifiedBy#, #modifiedOn#, " 
				+ "#abwCode#) ";

		String sql2 = "INSERT INTO fms_rate_card_detail (id, rateCardId, internalRate, externalRate, effectiveDate, "
				+ "createdBy, createdOn) "
				+ "VALUES (#idDetail#, #id#, #internalRate#, #externalRate#, #effectiveDate#, #createdBy#, #createdOn#) ";
		super.update(sql, rc);
		super.update(sql2, rc);
	}

	protected void insertRateCardEquipment(RateCard rc) throws DaoException {
		String sql = "INSERT INTO fms_rate_card_equipment (id, rateCardId, equipment, quantity, createdBy, createdOn, modifiedBy, modifiedOn) " +
				"VALUES (#idDetail#, #id#, #idEquipment#, 1, #createdBy#, #createdOn#, #modifiedBy#, #modifiedOn#) ";
		super.update(sql, rc);
	}
	
	protected void updateRateCardEquipment(RateCard rc) throws DaoException {
		String sql = "UPDATE fms_rate_card_equipment " +
				"SET modifiedBy = #modifiedBy#, modifiedOn =#modifiedOn# ";
			if (rc.getEquipmentQty() > 1) {
				sql += ", quantity = " + rc.getEquipmentQty();
			}
			sql += " WHERE rateCardId = #id# AND equipment = #idEquipment# ";
			
		super.update(sql, rc);
	}
	
	protected void updateRateCardEquipmentQty(RateCard rc) throws DaoException {
		String sql = "UPDATE fms_rate_card_equipment ";
				sql += "SET quantity = #equipmentQty# ";
				sql += "WHERE id = #idEquipment#";
		
		super.update(sql, rc);
	}
	
	protected void insertRateCardManpower(RateCard rc) throws DaoException {
		String sql = "INSERT INTO fms_rate_card_manpower (id, rateCardId, manpower, quantity, createdBy, createdOn, modifiedBy, modifiedOn) " +
				"VALUES (#idDetail#, #id#, #idManpower#, 1, #createdBy#, #createdOn#, #modifiedBy#, #modifiedOn#) ";
		super.update(sql, rc);
	}
	
	protected void updateRateCardManpower(RateCard rc) throws DaoException {
		String sql = "UPDATE fms_rate_card_manpower " +
				"SET modifiedBy = #modifiedBy#, modifiedOn =#modifiedOn# ";
			if (rc.getManpowerQty() > 1) {
				sql += ", quantity = " + rc.getManpowerQty();
			}
			sql += " WHERE rateCardId = #id# AND manpower = #idManpower# ";
			
		super.update(sql, rc);
	}
	
	protected void updateRateCardManpowerQty(RateCard rc) throws DaoException {
		String sql = "UPDATE fms_rate_card_manpower ";
				sql += "SET quantity = #manpowerQty# ";
				sql += "WHERE id = #idManpower#";
		
		super.update(sql, rc);
	}
	
	protected void insertRateCardDetail(RateCard rc) throws DaoException {
		String sql = "INSERT INTO fms_rate_card_detail (id, rateCardId, internalRate, externalRate, effectiveDate, "
				+ "createdBy, createdOn) "
				+ "VALUES (#idDetail#, #id#, #internalRate#, #externalRate#, #effectiveDate#, #createdBy#, #createdOn#) ";
		super.update(sql, rc);
	}

	public void updateRateCard(RateCard rc) throws DaoException {
		String sql = "UPDATE fms_rate_card "
				+ "SET serviceTypeId=#serviceTypeId#,"
				+ "description=#description#, remarksRequest=#remarksRequest#, "
				+ "modifiedBy=#modifiedBy#, modifiedOn=#modifiedOn#,"
				+ "status=#status#," 
				+ "transportRequest=#transportRequest#," 
				+ "vehicleCategoryId=#vehicleCategoryId#," 
				+ "abw_code=#abwCode# " 
				+ "WHERE id=#id#";
		super.update(sql, rc);
	}

	public void deleteRateCard(String id) throws DaoException {
		super.update("DELETE FROM fms_rate_card WHERE id=?", new String[] { id });
		super.update("DELETE FROM fms_rate_card_detail WHERE rateCardId=?", new String[] { id });
		super.update("DELETE FROM fms_rate_card_equipment WHERE rateCardId=?", new String[] { id });
		super.update("DELETE FROM fms_rate_card_manpower WHERE rateCardId=?", new String[] { id });
	}

	public Collection selectRateCard(String id) throws DaoException {
		String sql = "SELECT TOP 1 "
				+ "rc.id AS id, "
				+ "rc.name AS name, "
				+ "rc.serviceTypeId AS serviceTypeId, "
				+ "rc.description AS description, "
				+ "rc.remarksRequest AS remarksRequest,"
				+ "rcd.internalRate AS internalRate, rcd.externalRate AS externalRate, "
				+ "rcd.effectiveDate AS effectiveDate,"
				+ "rc.status AS status, " 
				+ "rc.transportRequest AS transportRequest, " +
				  "rc.vehicleCategoryId AS vehicleCategoryId, "
				+ "fes.title AS serviceType, "
				+ "rc.abw_code as abwCode," 
				+ "abw.description as abwCodeDesc "
				+ " FROM fms_rate_card rc LEFT JOIN fms_rate_card_detail rcd ON (rc.id = rcd.rateCardId) "
				+ " LEFT JOIN fms_eng_services fes ON (rc.serviceTypeId = fes.serviceId) " 
				+ " LEFT JOIN fms_rate_card_abw_code abw ON (rc.abw_code = abw.abw_code)  "
				+ " WHERE rc.id=? "; 
		String whereClause = " AND rcd.effectiveDate <= GETDATE() ";
		String orderClause = " ORDER BY rcd.effectiveDate DESC, rcd.createdOn DESC ";
				//+ " AND (rcd.effectiveDate <= GETDATE() OR rcd.effectiveDate > GETDATE()) "
				//+ " AND rcd.effectiveDate <= GETDATE() "
				//+ " ORDER BY rcd.effectiveDate desc";
				//+ " ORDER BY rcd.effectiveDate DESC, rcd.createdOn DESC " + " ";
		
		Collection col = super.select(sql + whereClause + orderClause, RateCard.class, new String[] { id }, 0, -1);
		if (col.size() <= 0){
			col = super.select(sql + orderClause, RateCard.class, new String[] { id }, 0, -1);
		}
		
		return col;
	}

	public Collection selectRateCard(String search, String status, String serviceTypeId, String sort, boolean desc,
			int start, int rows) throws DaoException {
		String sql = "SELECT "
				+ "rc.id AS id, rc.name AS name, "
				+ "rc.serviceTypeId AS serviceTypeId, "
				+ "rc.description AS description, "
				+ "rc.remarksRequest AS remarksRequest,"
				+ "rc.status AS status, "
				+ "fes.title AS serviceType," 
				+ "rc.abw_code as abwCode "
				+ " FROM fms_rate_card rc "
				+ " LEFT JOIN fms_eng_services fes ON (rc.serviceTypeId = fes.serviceId) " 
				+ " WHERE 1=1 ";
		if (search != null && !"".equals(search)) {
			sql += " AND rc.name like '%" + search + "%' ";
		}
		
		if (serviceTypeId != null && !"".equals(serviceTypeId)) {
			sql += " AND rc.serviceTypeId LIKE '%" + serviceTypeId + "%' ";
		}
		
		sql += " AND rc.status LIKE '%"+ ((status==null || status.equals("-1"))?"":status) +"%'  ";

		if (sort != null) {
			if (sort.equals("status")){
				sort = "rc." + sort;
			} else if (sort.equals("serviceType")) {
				sort = "fes." + sort;
			}
			
			sql += " ORDER BY " + sort;
			if (desc)
				sql += " DESC";
		} else
			sql += " ORDER BY rc.name ASC";

		return super.select(sql, RateCard.class, null, start, rows);
	}

	public Collection selectRequestServices(String requestId)
			throws DaoException {
		String sql = "Select s.serviceId,s.title,rs.requestId from fms_eng_services s"
				+ " Inner join fms_eng_request_services rs on s.serviceId=rs.serviceId where rs.requestId=? order By s.serviceId";
		return super.select(sql, Service.class, new String[] { requestId }, 0,
				-1);
	}

	public RateCard selectRateCardDetail(String id) throws DaoException {
		String sql = "SELECT TOP 1 " + " rcd.id AS idDetail, "
				+ " rcd.internalRate AS internalRate, "
				+ " rcd.externalRate AS externalRate, "
				+ " rcd.effectiveDate AS effectiveDate "
				+ " FROM fms_rate_card_detail rcd "
				+ " WHERE rcd.rateCardId=? ";
		String whereClause = " AND rcd.effectiveDate <= GETDATE() ";
		String orderClause = " ORDER BY rcd.effectiveDate DESC, rcd.createdOn DESC  ";
				//+ " AND (rcd.effectiveDate <= GETDATE() OR rcd.effectiveDate > GETDATE()) "
				//+ " AND rcd.effectiveDate <= GETDATE() "
				//+ " ORDER BY rcd.effectiveDate desc";
		Collection col = super.select(sql + whereClause + orderClause, RateCard.class, new Object[] { id },	0, 1);
		if (col.size() <= 0){
			col = super.select(sql + orderClause, RateCard.class, new Object[] { id }, 0, 1);
		}
		try {
			return (RateCard) col.iterator().next();
		} catch (Exception e) {
		}

		return null;
	}

	public Collection selectRateCardHistory(String id) throws DaoException {
		String sql = "SELECT rc.id AS id, rc.name AS name, "
				+ "rcd.id AS idDetail, "
				+ "rcd.internalRate AS internalRate, "
				+ "rcd.externalRate AS externalRate, "
				+ "rcd.effectiveDate AS effectiveDate, "
				+ "rcd.createdBy AS createdBy, "
				+ "rcd.createdOn AS createdOn "
				+ "FROM fms_rate_card rc LEFT JOIN fms_rate_card_detail rcd ON (rc.id = rcd.rateCardId) "
				+ "WHERE rcd.rateCardId=? " + "ORDER BY rcd.effectiveDate ASC";
		return super.select(sql, RateCard.class, new String[] { id }, 0, -1);
	}

	public Collection selectRateCardAllEquipment(String search, String sort, boolean desc, int start, int rows) throws DaoException {
		Collection args = new ArrayList();
		String sql = "SELECT f.id AS idEquipment, " +
					 "f.name AS equipment, " +
					 "f.quantity AS equipmentQty " +
					 "FROM fms_facility f," + 
				     "fms_facility_category c, fms_tran_channel ch WHERE f.category_id=c.id and " + 
				     "f.channel_id=ch.setup_id" ;
		
		if(!"".equals(search) && !"null".equals(search) && search != null){
			sql = sql + " AND (f.name LIKE ? or f.description like ?)";
			args.add("%"+search+"%");
			args.add("%"+search+"%");
		}
		if (sort != null) {
			if (sort.equals("equipment")){
				sort = "f." + sort;
			}
			
			sql += " ORDER BY " + sort;
			if (desc)
				sql += " DESC";
		} else
			sql += " ORDER BY f.name ASC";
		return super.select(sql, RateCard.class, args.toArray(), start, rows);
	}
	
	public Collection selectRateCardEquipment(String id, String idEquipment) throws DaoException {
		String sql = "SELECT rc.id AS id, "
				+ "rce.id AS idEquipment, "
				+ "cat.name AS equipment, " +
				 "cat.id AS categoryId, "
				+ "rce.quantity AS equipmentQty, "
				+ "rc.serviceTypeId AS serviceTypeId "
				+ "FROM fms_rate_card rc INNER JOIN fms_rate_card_equipment rce "
				+ "ON (rc.id = rce.rateCardId) " 
				//+ "INNER JOIN fms_facility fac ON (rce.equipment = fac.id) "
				+ "INNER JOIN fms_eng_rate_card_category cat ON (rce.equipment = cat.id) "
				+ "WHERE 1=1 ";
		if (id != null && !"".equals(id)) {
				sql += "AND rce.rateCardId='" + id + "'";
		}
		if (idEquipment != null && !"".equals(idEquipment)) {
			sql += " AND rce.equipment='" + idEquipment + "'";
		}
		
		return super.select(sql, RateCard.class, null, 0, -1);
	}
	
	public Collection selectRateCardEquipmentForChecking(String rateCardId) throws DaoException {
		String sql = 
				"SELECT rce.rateCardId AS id, rce.id AS idEquipment, cat.name AS equipment, cat.id AS categoryId, rce.quantity AS equipmentQty " +
				"FROM fms_rate_card_equipment rce " +
				"LEFT OUTER JOIN fms_eng_rate_card_category cat ON (rce.equipment = cat.id) " +
				"WHERE rce.rateCardId = ? ";
		return super.select(sql, RateCard.class, new String[] {rateCardId}, 0, -1);
	}

	public Collection selectRateCardAllManpower(String search) throws DaoException {
		Collection args = new ArrayList();
		String sql = "SELECT com.competencyId AS idManpower, " +
					 "com.competencyName AS manpower," +
					 "com.competencyType AS competencyType " +				
					 "FROM competency com " +
					 "WHERE 1=1 " ;
		
		if(!"".equals(search) && !"null".equals(search) && search != null){
			sql = sql + " AND (com.competencyName LIKE ?)";
			args.add("%"+search+"%");
		}
		return super.select(sql, RateCard.class, args.toArray(), 0, -1);
	}
	
	public Collection selectRateCardManpower(String id, String idManpower) throws DaoException {
		String sql = "SELECT rc.id AS id, " + 
				"rcm.id AS idManpower, " +
				"com.competencyId AS competencyId, " +
				"com.competencyName AS manpower, " +
				"com.competencyType AS competencyType, " +
				"rcm.quantity AS manpowerQty " +
				"FROM fms_rate_card rc INNER JOIN fms_rate_card_manpower rcm " +
				"ON (rc.id = rcm.rateCardId) " +
				"INNER JOIN competency com ON (rcm.manpower = com.competencyId) "+
				"WHERE 1=1 ";
		if (id != null && !"".equals(id)) {
			sql += "AND rcm.rateCardId='" + id + "'";
		}
		if (idManpower != null && !"".equals(idManpower)) {
			sql += " AND rcm.manpower='" + idManpower + "'";
		}		
		
		return super.select(sql, RateCard.class, null, 0, -1);
	}
	
	public Collection selectRateCardManpowerForChecking(String rateCardId) throws DaoException {
		String sql =
			"SELECT rcm.rateCardId AS id, rcm.id AS idManpower, rcm.quantity AS manpowerQty, " + 
			"       com.competencyId AS competencyId, com.competencyName AS manpower, com.competencyType AS competencyType " +
			"FROM fms_rate_card_manpower rcm " +
			"LEFT OUTER JOIN competency com ON (rcm.manpower = com.competencyId) " + 
			"WHERE rcm.rateCardId = ? ";
		return super.select(sql, RateCard.class, new String[] {rateCardId}, 0, -1);
	}
	
	public int selectRateCardCount(String search, String status, String serviceTypeId) throws DaoException {
		String sql = "SELECT count(*) As COUNT "
				//+ " FROM fms_rate_card LEFT JOIN fms_rate_card_detail ON (fms_rate_card.id = fms_rate_card_detail.rateCardId)"
				+ " FROM fms_rate_card rc "
				+ " LEFT JOIN fms_eng_services fes ON (rc.serviceTypeId = fes.serviceId) " 
				+ " WHERE 1=1 ";
		if (search != null && !"".equals(search)) {
			sql += " AND rc.name like '%" + search + "%' ";
		}
		if (serviceTypeId != null && !"".equals(serviceTypeId)) {
			sql += " AND rc.serviceTypeId LIKE '%" + serviceTypeId + "%' ";
		}		

		sql += " AND rc.status LIKE '%"+ ((status==null || status.equals("-1"))?"":status) +"%'  ";  
		
		int count = 0;
		try {
			Collection col = super.select(sql, HashMap.class, null, 0, 1);
			if (col != null) {
				HashMap map = (HashMap) col.iterator().next();
				count = (Integer) map.get("COUNT");
			}
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return count;
	}
	
	public RateCard selectRateCardByService(String serviceTypeId) throws DaoException {
		String sql = "SELECT TOP 1 " +
				"rc.id AS id, " +
				"rc.name AS name " +
				"FROM fms_rate_card rc " +
				"WHERE rc.serviceTypeId=? " + 
				"ORDER BY rc.modifiedOn DESC ";
				//+ " AND (rcd.effectiveDate <= GETDATE() OR rcd.effectiveDate > GETDATE()) "
				
		Collection col = super.select(sql, RateCard.class, new Object[] { serviceTypeId },
				0, 1);
		try {
			return (RateCard) col.iterator().next();
		} catch (Exception e) {
		}

		return null;
	}

	public int countRateCardHistory(String id) throws DaoException {
		String sql = "SELECT count(*) AS COUNT "
				+ "FROM fms_rate_card LEFT JOIN fms_rate_card_detail ON (fms_rate_card.id = fms_rate_card_detail.rateCardId) "
				+ "WHERE fms_rate_card_detail.rateCardId='" + id + "'";

		int count = 0;
		try {
			Collection col = super.select(sql, HashMap.class, null, 0, 1);
			if (col != null) {
				HashMap map = (HashMap) col.iterator().next();
				count = (Integer) map.get("COUNT");
			}
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return count;
	}

	public int countRateCardAllEquipment(String search) throws DaoException {
		Collection args = new ArrayList();
		String sql = "SELECT count(*) AS COUNT " +
					 "FROM fms_facility f," + 
				     "fms_facility_category c, fms_tran_channel ch WHERE f.category_id=c.id AND " + 
				     "f.channel_id=ch.setup_id";
		
		if(!"".equals(search) && !"null".equals(search) && search != null){
			sql = sql + " AND (f.name LIKE ? or f.description like ?)";
			args.add("%"+search+"%");
			args.add("%"+search+"%");
		}
		
		int count = 0;
		try {
			Collection col = super.select(sql, HashMap.class, args.toArray(), 0, 1);
			if (col != null){
				HashMap map = (HashMap) col.iterator().next();
				count = (Integer) map.get("COUNT");
			} 
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		
		return count;
	}
	
	public int countRateCardEquipment(String id, String idEquipment) throws DaoException {
		String sql = "SELECT count(*) AS COUNT "
				+ "FROM fms_rate_card rc INNER JOIN fms_rate_card_equipment rce "
				+ "ON (rc.id = rce.rateCardId) " 
				+ "WHERE 1=1 ";
		if (id != null && !"".equals(id)) {
				sql += " AND rce.rateCardId='" + id + "'";
		}
		if (idEquipment != null && !"".equals(idEquipment)) {
			sql += " AND rce.equipment='" + idEquipment + "'";
		}

		int count = 0;
		try {
			Collection col = super.select(sql, HashMap.class, null, 0, 1);
			if (col != null) {
				HashMap map = (HashMap) col.iterator().next();
				count = (Integer) map.get("COUNT");
			}
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return count;
	}
	
	public int countRateCardAllManpower(String search) throws DaoException {
		Collection args = new ArrayList();
		String sql = "SELECT count(*) AS COUNT " +
					 "FROM competency com " +
					 "WHERE 1=1 ";
		
		if(!"".equals(search) && !"null".equals(search) && search != null){
			sql = sql + " AND (com.competencyName LIKE ?)";
			args.add("%"+search+"%");
		}
		
		int count = 0;
		try {
			Collection col = super.select(sql, HashMap.class, args.toArray(), 0, 1);
			if (col != null){
				HashMap map = (HashMap) col.iterator().next();
				count = (Integer) map.get("COUNT");
			} 
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		
		return count;
	}
	
	public int countRateCardManpower(String id, String idManpower) throws DaoException {
		String sql = "SELECT count(*) AS COUNT "
				+ "FROM fms_rate_card rc INNER JOIN fms_rate_card_manpower rcm "
				+ "ON (rc.id = rcm.rateCardId) " 
				+ "WHERE 1=1 ";
		if (id != null && !"".equals(id)) {
			sql += " AND rcm.rateCardId='" + id + "'";
		}
		if (idManpower != null && !"".equals(idManpower)) {
			sql += " AND rcm.manpower='" + idManpower + "'";
		}
		
		int count = 0;
		try {
			Collection col = super.select(sql, HashMap.class, null, 0, 1);
			if (col != null) {
				HashMap map = (HashMap) col.iterator().next();
				count = (Integer) map.get("COUNT");
			}
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return count;
	}
	
	public void deleteRateCardEquipment(String id, String idEquipment) throws DaoException {
		String sql = "DELETE FROM fms_rate_card_equipment WHERE id = '" + idEquipment + "' AND rateCardId= '"+ id +"'";
		
		super.update(sql, null);
	}
	
	public void deleteRateCardManpower(String id, String idManpower) throws DaoException {
		String sql = "DELETE FROM fms_rate_card_manpower WHERE id = '" + idManpower + "' AND rateCardId= '"+ id +"'";

		super.update(sql, null);
	}
	
	// Rate Card - Item Category DAO
	protected void insertRateCardCategory(RateCard rc) throws DaoException {
		String sql = "INSERT INTO fms_eng_rate_card_category (id, name,"
				+ "createdBy, createdOn, modifiedBy, modifiedOn) "
				+ "VALUES (#idCategory#,#categoryName#, "
				+ "#createdBy#, #createdOn#, #modifiedBy#, #modifiedOn# ) ";
		super.update(sql, rc);
	}
	
	protected void updateRateCardCategory(RateCard rc) throws DaoException {
		String sql = "UPDATE fms_eng_rate_card_category SET " 
				+ "modifiedBy = #modifiedBy#, modifiedOn=#modifiedOn# " 
				+ "WHERE id=#idCategory#";
		super.update(sql, rc);
	}
	
	protected void insertCategoryItems(RateCard rc) throws DaoException {
		String sql = "INSERT INTO fms_eng_rate_card_cat_item (categoryId, facilityId) "
				+ "VALUES (#idCategory#,#idEquipment#) ";
		super.update(sql, rc);
	}
	
	public Collection selectRateCardCategory(String id) throws DaoException {
		String sql = "SELECT "
				+ "cat.id AS idCategory, cat.name AS categoryName, "
				+ "cat.createdBy AS createdBy, "
				+ "cat.createdOn AS createdOn, "
				+ "cat.modifiedBy AS modifiedBy,"
				+ "cat.modifiedOn AS modifiedOn "
				+ "FROM fms_eng_rate_card_category cat " 
				+ "WHERE cat.id=? " ;
		return super.select(sql, RateCard.class, new String[] { id }, 0, -1);
	}
	
	public RateCard selectRateCardCategoryById(String id) throws DaoException {
		String sql = "SELECT "
				+ "cat.id AS idCategory, cat.name AS categoryName, "
				+ "cat.createdBy AS createdBy, "
				+ "cat.createdOn AS createdOn, "
				+ "cat.modifiedBy AS modifiedBy,"
				+ "cat.modifiedOn AS modifiedOn "
				+ "FROM fms_eng_rate_card_category cat " 
				+ "WHERE cat.id=? " ;
		
		Collection result = super.select(sql, RateCard.class, new String[] { id }, 0, -1);
		RateCard rc = null;		
		if(result.size()>0){
			rc = (RateCard) result.iterator().next();
			rc.setEquipments(selectRateCardCategoryDetail(id));
			rc.setEquipmentQty(selectRateCardSumQty(id));
		}
		
		return rc;
	}
	
	/**
	 * for FC Check Availability
	 * 
	 * @param id
	 * @return
	 * @throws DaoException
	 */
	public RateCard selectRateCardCategoryByIdCheck(String id) throws DaoException {
		String sql = "SELECT "
				+ "cat.id AS idCategory, cat.name AS categoryName, "
				+ "cat.createdBy AS createdBy, "
				+ "cat.createdOn AS createdOn, "
				+ "cat.modifiedBy AS modifiedBy,"
				+ "cat.modifiedOn AS modifiedOn "
				+ "FROM fms_eng_rate_card_category cat " 
				+ "WHERE cat.id=? " ;
		
		Collection result = super.select(sql, RateCard.class, new String[] { id }, 0, -1);
		RateCard rc = null;		
		if(result.size()>0){
			rc = (RateCard) result.iterator().next();
			rc.setEquipments(selectRateCardCategoryDetail(id));
			rc.setEquipmentQty(selectRateCardCountQtyInPool(id));
		}
		
		return rc;
	}
	
	public String[] selectRateCardCategoryDetail(String idCategory) throws DaoException, SecurityException {
        Collection items = new ArrayList();
        
        //String sql = "SELECT userId as userId FROM fms_department_alternate_approver WHERE departmentId=?";
        String sql = "SELECT f.id AS idEquipment, f.name AS equipment " +
					"FROM fms_facility f INNER JOIN fms_eng_rate_card_cat_item i " +
					"ON (f.id = i.facilityId)" +
					"WHERE i.categoryId = ?";
        Object[] args = new Object[] { idCategory };

        Collection tmp = super.select(sql.toString(), HashMap.class, args, 0, -1);
        for (Iterator i=tmp.iterator(); i.hasNext();) {
            HashMap m = (HashMap)i.next();
            items.add(m.get("idEquipment"));
        }

        return (String[])items.toArray(new String[0]);
	}
	
	
	public Collection selectRateCardCategory(String search, String sort, boolean desc, int start, int rows) throws DaoException {
		String sql = "SELECT "
				+ "cat.id AS idCategory, cat.name AS categoryName, "
				+ "cat.createdBy AS createdBy, "
				+ "cat.createdOn AS createdOn, "
				+ "cat.modifiedBy AS modifiedBy,"
				+ "cat.modifiedOn AS modifiedOn "
				+ "FROM fms_eng_rate_card_category cat " 
				+ "WHERE 1=1 ";
		
		if (search != null && !"".equals(search)) {
			sql += " AND cat.name LIKE '%" + search + "%' ";
		}
		
		if (sort != null) {
			if (sort.equals("name")){
				sort = "cat." + sort;
			} 
			
			sql += " ORDER BY " + sort;
			if (desc)
				sql += " DESC";
		} else
			sql += " ORDER BY cat.name ASC";
		
		return super.select(sql, RateCard.class, null, start, rows);
	}
	
	public Collection selectRateCardCategoryDetail(String idCategory, String operator) throws DaoException {
		String opr = "=";
		if (operator != null && !"".equals(operator)) {
			opr = operator;
		}
		String sql = "SELECT f.id AS idEquipment, f.name AS equipment " +
				"FROM fms_facility f INNER JOIN fms_eng_rate_card_cat_item i " +
				"ON (f.id = i.facilityId)" +
				"WHERE i.categoryId " + opr + " ?";
		
		return super.select(sql, RateCard.class, new String[] {idCategory}, 0, -1);
	}
	
	
	public Collection selectFacility(String facilityId, String operator) throws DaoException {
		String opr = "=";
		if (operator != null && !"".equals(operator)) {
			opr = operator;
		}
		String sql = "SELECT f.id AS idEquipment, f.name AS equipment " +
				"FROM fms_facility f, " +
				"fms_facility_category c, fms_tran_channel ch WHERE f.category_id=c.id AND " + 
			    "f.channel_id=ch.setup_id " +
				"AND f.id " + opr + " ?";
		
		return super.select(sql, RateCard.class, new String[] {facilityId}, 0, -1);
	}

	public int selectRateCardCategoryCount(String name) throws DaoException {
		String sql = "SELECT count(*) As COUNT "
				+ " FROM fms_eng_rate_card_category cat "
				+ " WHERE 1=1 ";
		if (name != null && !"".equals(name)) {
			sql += " AND cat.name LIKE '%" + name + "%' ";
		}	

		int count = 0;
		try {
			Collection col = super.select(sql, HashMap.class, null, 0, 1);
			if (col != null) {
				HashMap map = (HashMap) col.iterator().next();
				count = (Integer) map.get("COUNT");
			}
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return count;
	}
	
	public int selectRateCardSumQty(String idCategory) throws DaoException {
		int quantity = 0;
		String sql = "SELECT SUM(f.quantity) AS equipmentQty " +
					"FROM fms_facility f INNER JOIN fms_eng_rate_card_cat_item i " +
					"ON (f.id = i.facilityId) WHERE i.categoryId = ? GROUP BY i.categoryId";
		try {
			Collection col =  super.select(sql, HashMap.class, new String[] {idCategory}, 0, -1);
			if (col != null) {
				if (col.size() >0 ){
					HashMap map = (HashMap) col.iterator().next();
					quantity = (Integer) map.get("equipmentQty");
				}
			}
		} catch (Exception e){
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		
		return quantity;
	}
	
	public int selectRateCardCountQtyInPool(String idCategory) throws DaoException {
		int quantity = 0;
		String sql = "SELECT COUNT(*) AS equipmentQty " +
			"FROM fms_facility f " +
			"INNER JOIN fms_eng_rate_card_cat_item i ON (f.id = i.facilityId) " +
			"INNER JOIN fms_facility_item fi ON (f.id = fi.facility_id AND fi.status = '1') " +
			"WHERE i.categoryId =? ";
		
		try {
			Collection col =  super.select(sql, HashMap.class, new String[] {idCategory}, 0, -1);
			if (col != null) {
				if (col.size() >0 ){
					HashMap map = (HashMap) col.iterator().next();
					quantity = (Integer) map.get("equipmentQty");
				}
			}
		} catch (Exception e){
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		
		return quantity;
	}
	
	public void deleteRateCardCategory(String id) throws DaoException {
		super.update("DELETE FROM fms_eng_rate_card_category WHERE id=?", new String[] { id });
		super.update("DELETE FROM fms_eng_rate_card_cat_item WHERE categoryId=?", new String[] { id });
	}
	
	public void deleteRateCardCategoryItems(String idCategory) throws DaoException {
		super.update("DELETE FROM fms_eng_rate_card_cat_item WHERE categoryId=?", new String[] { idCategory });
	}	
	// End of Rate Card - Item Category DAO
	
	
	
	public String getCodeByWorkingProfileName(String wpName) throws DaoException {
    	String name = "";

    	String sql = "SELECT workingProfileId FROM fms_working_profile " +
	    			"WHERE  name = ? ";
	    			

	    	Collection col = super.select(sql, HashMap.class, new Object[] {wpName}, 0, 1);
	    	if(col != null) {
	    		if(col.size() > 0) {
	    			HashMap map = (HashMap) col.iterator().next();
	    			name = map.get("workingProfileId").toString();
	    		}
	    	}
    	
    	return name;
    }
	
	public Collection selectWorkingProfile(String userId, String wpName, Date startDate, Date endDate) throws DaoException {
		
		
		Object arg[] = null; 
		arg = new Object[]{userId,wpName};
		String filterDate = "";
		
		
		if(startDate != null && endDate != null){
			
			arg = new Object[]{userId,wpName,startDate,endDate,
				startDate,endDate,
				startDate,endDate,endDate,
				startDate,startDate,endDate};
				
        	filterDate += " AND " +	        
        	"((Dur.startDate >= ?  AND Dur.endDate <= ?) OR  " +
        	"(Dur.startDate <= ? AND Dur.endDate >= ?)  OR  " +
        	"(Dur.startDate >= ? AND Dur.startDate <= ? AND Dur.endDate >= ?) OR  " +
        	"(Dur.startDate <= ? AND Dur.endDate >= ? AND Dur.endDate <= ?))  ";	        	
        } 
		
		String sql = "select Man.userId,Pro.name AS workingProfileCode, Dur.startDate AS workStart,Dur.endDate AS workEnd " +
				"from fms_working_profile_duration_manpower Man  " +
				"INNER JOIN fms_working_profile_duration Dur ON Dur.workingProfileDurationId=Man.workingProfileDurationId  " + 
			    "INNER JOIN fms_working_profile Pro ON pro.workingProfileId = Dur.workingProfileId  " +
				"WHERE Man.userId=? AND "+
				"Pro.workingProfileId=?  "+filterDate;
				
		return super.select(sql, WorkingProfile.class, arg , 0, -1);
	}
	
	public Collection selectWorkingProfileForUpdate(String userId, Date startDate, Date endDate) throws DaoException {
		
		Object arg[] = null; 
		arg = new Object[]{userId};
		String filterDate = "";
		
		
		if(startDate != null && endDate != null){
			
			arg = new Object[]{userId, startDate,endDate,
				startDate,endDate,
				startDate,endDate,endDate,
				startDate,startDate,endDate};
				
        	filterDate += " AND " +	        
        	"((Dur.startDate >= ?  AND Dur.endDate <= ?) OR  " +
        	"(Dur.startDate <= ? AND Dur.endDate >= ?)  OR  " +
        	"(Dur.startDate >= ? AND Dur.startDate <= ? AND Dur.endDate >= ?) OR  " +
        	"(Dur.startDate <= ? AND Dur.endDate >= ? AND Dur.endDate <= ?))  ";	        	
        } 
		
		String sql = "select DISTINCT Man.workingProfileDurationId, Man.userId,Pro.name AS workingProfileCode, Dur.startDate AS workStart,Dur.endDate AS workEnd " +
				"from fms_working_profile_duration_manpower Man  " +
				"INNER JOIN fms_working_profile_duration Dur ON Dur.workingProfileDurationId=Man.workingProfileDurationId  " + 
			    "INNER JOIN fms_working_profile Pro ON pro.workingProfileId = Dur.workingProfileId  " +
				"WHERE Man.userId=?   "+filterDate;
				
		return super.select(sql, WorkingProfile.class, arg, 0, -1);
	}
	
	/**
	 * Checking on updating working profile duration 
	 * 
	 * @param userId
	 * @param wpdId
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DaoException
	 */
	public Collection selectWorkingProfileForUpdate(String userId, String wpdId, Date startDate, Date endDate) throws DaoException {
		
		Object arg[] = null; 
		arg = new Object[]{userId};
		String filterDate = "";
		
		
		if(startDate != null && endDate != null){
			
			arg = new Object[]{userId, wpdId, startDate,endDate,
				startDate,endDate,
				startDate,endDate,endDate,
				startDate,startDate,endDate};
				
        	filterDate += " AND " +	        
        	"((Dur.startDate >= ?  AND Dur.endDate <= ?) OR  " +
        	"(Dur.startDate <= ? AND Dur.endDate >= ?)  OR  " +
        	"(Dur.startDate >= ? AND Dur.startDate <= ? AND Dur.endDate >= ?) OR  " +
        	"(Dur.startDate <= ? AND Dur.endDate >= ? AND Dur.endDate <= ?))  ";	        	
        } 
		
		String sql = "select DISTINCT Man.workingProfileDurationId, Man.userId,Pro.name AS workingProfileCode, Dur.startDate AS workStart,Dur.endDate AS workEnd " +
				"from fms_working_profile_duration_manpower Man  " +
				"INNER JOIN fms_working_profile_duration Dur ON Dur.workingProfileDurationId=Man.workingProfileDurationId  " + 
			    "INNER JOIN fms_working_profile Pro ON pro.workingProfileId = Dur.workingProfileId  " +
				"WHERE Man.userId=? AND Man.workingProfileDurationId <> ? " + filterDate;
				
		return super.select(sql, WorkingProfile.class, arg, 0, -1);
	}
	
	protected String isStudioExist(String userId, String studioName){
		try {
			String sql = 
				" select rc.id, rc.name, rc.remarksRequest from fms_rate_card rc " +
				" where rc.status!='0' AND rc.serviceTypeId='5' " +
				" AND rc.name = ?";
			
			Collection col = super.select(sql, HashMap.class, new String[] {studioName}, 0, 1);
			
			if(col!= null && col.size()==1){
				HashMap map = (HashMap) col.iterator().next();
				return (String) map.get("id");
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return null;
	}
	
	public Collection getStudiosByCode(String code){
		try {
			code = code+"%-%";
			String sql = 
				" select rc.name from fms_rate_card rc " +
				" where rc.status!='0' AND rc.serviceTypeId='5' " +
				" AND rc.name like ?";
	
			Collection col = super.select(sql, HashMap.class, new String[] {code}, 0, -1);
			//if(tempReqId!=null && tempReqId.size()>0){
			//	for (Iterator iter = tempReqId.iterator(); iter.hasNext();) {
			//		HashMap map = (HashMap) col.iterator().next();
			//		return (String) map.get("id");
			//	}
			//}
			return col;
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return null;
	}
	
	
	/***** rate card checking *****/
	protected HashMap checkRateCard(String rateCardId) {
		String sql = 
			"SELECT id, name, transportRequest, vehicleCategoryId, status " +
			"FROM fms_rate_card " +
			"WHERE id = ? ";
		try {
			Collection col = super.select(sql, HashMap.class, new String[] {rateCardId}, 0, 1);
			if (col.size() == 1) {
				return (HashMap) col.iterator().next();
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error("rateCardId=" + rateCardId + " " + e.toString(), e);
		}
		return null;
	}
	
	protected Collection checkRateCardEquipment(String rateCardId) {
		String sql =
			"SELECT rce.rateCardId, cat.id AS categoryId, cat.name AS categoryName, item.categoryId AS itemCategory, " + 
			"       fa.name AS facilityName, fa.status AS facilityStatus " +
			"FROM fms_rate_card_equipment rce " +
			"LEFT OUTER JOIN fms_eng_rate_card_category cat ON (cat.id = rce.equipment) " +
			"LEFT OUTER JOIN fms_eng_rate_card_cat_item item ON (item.categoryId = rce.equipment) " +
			"LEFT OUTER JOIN fms_facility fa ON (fa.id = item.facilityId) " +
			"WHERE rce.rateCardId = ? ";
		
		try {
			Collection col = super.select(sql, HashMap.class, new String[] {rateCardId}, 0, -1);
			return col;
		} catch (DaoException e) {
			Log.getLog(getClass()).error("rateCardId=" + rateCardId + " " + e.toString(), e);
			return null;
		}
	}
	
	protected Collection checkRateCardManpower(String rateCardId) {
		String sql =
			"SELECT rcm.rateCardId, com.competencyId, com.competencyName " +
			"FROM fms_rate_card_manpower rcm " +
			"LEFT OUTER JOIN competency com ON (com.competencyId = rcm.manpower) " +
			"WHERE rcm.rateCardId = ? ";
		
		try {
			Collection col = super.select(sql, HashMap.class, new String[] {rateCardId}, 0, -1);
			return col;
		} catch (DaoException e) {
			Log.getLog(getClass()).error("rateCardId=" + rateCardId + " " + e.toString(), e);
			return null;
		}
	}
	
	protected Collection listRequestRateCard(String requestId) {
		String sql = 
			"SELECT DISTINCT facilityId AS rateCardId " +
			"FROM fms_eng_service_scp " +
			"WHERE requestId = ? " +
				"UNION " +
			"SELECT DISTINCT facilityId AS rateCardId " +
			"FROM fms_eng_service_postproduction " +
			"WHERE requestId = ? " +
				"UNION " +
			"SELECT DISTINCT facilityId AS rateCardId " +
			"FROM fms_eng_service_vtr " +
			"WHERE requestId = ? " +
				"UNION " +
			"SELECT DISTINCT competencyId AS rateCardId " +
			"FROM fms_eng_service_manpower " +
			"WHERE requestId = ? " +
				"UNION " +
			"SELECT DISTINCT facilityId AS rateCardId " +
			"FROM fms_eng_service_studio " +
			"WHERE requestId = ? " +
				"UNION " +
			"SELECT DISTINCT facilityId AS rateCardId " +
			"FROM fms_eng_service_other " +
			"WHERE requestId = ? " +
				"UNION " +
			"SELECT DISTINCT facilityId AS rateCardId " +
			"FROM fms_eng_service_tvro " +
			"WHERE requestId = ? ";
		
		try {
			Collection col = super.select(sql, HashMap.class, new String[] {requestId, requestId, requestId, requestId, requestId, requestId, requestId}, 0, -1);
			return col;
		} catch (DaoException e) {
			Log.getLog(getClass()).error("requestId=" + requestId + " " + e.toString(), e);
			return null;
		}
	}
	
	protected Collection checkRateCardCategory(String rateCardCategoryId) {
		String sql =
			"SELECT item.categoryId AS itemCategory, fa.name AS facilityName, fa.status AS facilityStatus " +
			"FROM fms_eng_rate_card_cat_item item " +
			"LEFT OUTER JOIN fms_facility fa ON (fa.id = item.facilityId) " + 
			"WHERE item.categoryId = ? ";
		
		try {
			Collection col = super.select(sql, HashMap.class, new String[] {rateCardCategoryId}, 0, -1);
			return col;
		} catch (DaoException e) {
			Log.getLog(getClass()).error("rateCardCategoryId=" + rateCardCategoryId + " " + e.toString(), e);
			return null;
		}
	}
	
	protected boolean isRateCardUsedInFuture(String rateCardId, String serviceId) {
		Date today = DateUtil.getDateOnly(new Date());
		String tableName = getTableFromServiceType(serviceId);
		String rateCardColumn = "facilityId";
		String dateToColumn = "requiredTo";
		
		if (serviceId.equals(ServiceDetailsForm.SERVICE_POSTPRODUCTION)) {
			dateToColumn = "requiredDateTo";
		} else if (serviceId.equals(ServiceDetailsForm.SERVICE_VTR)) {
			dateToColumn = "requiredDateTo";
		} else if (serviceId.equals(ServiceDetailsForm.SERVICE_TVRO)) {
			dateToColumn = "requiredDateTo";
		} else if (serviceId.equals(ServiceDetailsForm.SERVICE_MANPOWER)) {
			rateCardColumn = "competencyId";
		} else if (serviceId.equals(ServiceDetailsForm.SERVICE_STUDIO)) {
			dateToColumn = "bookingDateTo";
		}
		
		// query service request in the future (excluding today)
		String sql = 
			"SELECT requestId " +
			"FROM " + tableName + " p " +
			"WHERE " + rateCardColumn + " = ? " +
			"AND " + dateToColumn + " > ? ";
		
		try {
			Collection col = super.select(sql, HashMap.class, new Object[] {rateCardId, today}, 0, 1);
			if (col.size() == 0) {
				return false;
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		
		return true;
	}
	
	public String getTableFromServiceType(String serviceId) {
		if (serviceId.equals(ServiceDetailsForm.SERVICE_SCPMCP)) {
			return "fms_eng_service_scp";
		} else if (serviceId.equals(ServiceDetailsForm.SERVICE_POSTPRODUCTION)) {
			return "fms_eng_service_postproduction";
		} else if (serviceId.equals(ServiceDetailsForm.SERVICE_OTHER)) {
			return "fms_eng_service_other";			
		} else if (serviceId.equals(ServiceDetailsForm.SERVICE_STUDIO)) {
			return "fms_eng_service_studio";
		} else if (serviceId.equals(ServiceDetailsForm.SERVICE_VTR)){
			return "fms_eng_service_vtr";
		} else if (serviceId.equals(ServiceDetailsForm.SERVICE_MANPOWER)){
			return "fms_eng_service_manpower";
		} else if (serviceId.equals(ServiceDetailsForm.SERVICE_TVRO)){
			return "fms_eng_service_tvro";
		}
		return null;
	}
	
	public int getTotalFacility(String requestId, String times, String facilityId, boolean today) {
		
		int total = 0;
		String sql = null;
		
		try{
			sql = "SELECT  rateCardCategoryId,e.fromTime, count(rateCardCategoryId) as total "+
				"from fms_eng_assignment a  "+
				"LEFT JOIN fms_eng_assignment_equipment e on (a.assignmentId=e.assignmentId)   "+
				"where 1=1 AND a.requestId = ? AND e.assignmentId not like '-'  "+
				"AND (a.serviceType='1' or a.serviceType='6') "+
				"AND rateCardCategoryId = ? AND e.fromTime = ? ";
				
				if(today)
					sql += "AND convert(varchar, GETDATE(), 1) between requiredFrom and requiredTo ";	//to get today assignment
				
				sql += "group by e.fromTime,rateCardCategoryId ";
			
		Collection collFac = super.select(sql, HashMap.class, new String[]{requestId, facilityId, times}, 0, -1);
		if (collFac != null && collFac.size() > 0){
			HashMap map = (HashMap)collFac.iterator().next();
			total = (Integer)map.get("total");
		}
		}catch(Exception er){
			Log.getLog(getClass()).error("ERROR getTotalFacility >> "+er);
		}
		return total;
	}
	
	public void insertAbwCode(DefaultDataObject abwObj) throws DaoException
	{
		String sql = " INSERT INTO fms_rate_card_abw_code (abw_code, description) VALUES (#abw_code#, #description#) ";
		super.update(sql, abwObj);
	}
	
	public void updateAbwCode(DefaultDataObject abwObj) throws DaoException
	{
		String sql = " UPDATE fms_rate_card_abw_code SET description=#description# WHERE abw_code=#abw_code# ";
		super.update(sql, abwObj);
	}
	
	public void deleteAbwCode(String abwCode) throws DaoException
	{
		String sql = " DELETE FROM fms_rate_card_abw_code WHERE abw_code = ? ";
		super.update(sql, new Object[]{abwCode});
	}

	public Collection getAbwCodes(String abwCode, String keyword, boolean desc, String sort,
			int start, int rows) throws DaoException
	{
		ArrayList param = new ArrayList();
		String sql =
			" SELECT abw_code AS id, abw_code, description " +
			" FROM fms_rate_card_abw_code " +
			" WHERE 1=1 ";
		
		if(abwCode != null && !abwCode.equals(""))
		{
			sql += " AND abw_code = ? ";
			param.add(abwCode);
		}
		else if(keyword != null && !keyword.equals(""))
		{
			sql += " AND ( abw_code LIKE ? OR description LIKE ? ) ";
			keyword = "%" + keyword + "%";
			param.add(keyword);
			param.add(keyword);
		}
				
		if(sort == null || "".equals(sort) || "abw_code".equals(sort))
		{
			sort =  "case IsNumeric(abw_code) "+
					"when 1 then Replicate(Char(0), 100 - Len(abw_code)) + abw_code "+
					"else abw_code "+
					"end ";						
		}		
		
		sql += " ORDER BY " + sort;
		if(desc)
		{
			sql += " DESC "; 
		}
		
		
		Collection result = super.select(sql, DefaultDataObject.class, param.toArray(), start, rows);
		return result;
	}

	public int getAbwCodesCount(String abwCode, String keyword) throws DaoException
	{
		ArrayList param = new ArrayList();
		String sql =
			" SELECT COUNT(abw_code) AS total " +
			" FROM fms_rate_card_abw_code " +
			" WHERE 1=1 ";
		
		if(abwCode != null && !abwCode.equals(""))
		{
			sql += " AND abw_code = ? ";
			param.add(abwCode);
		}
		else if(keyword != null && !keyword.equals(""))
		{
			sql += " AND ( abw_code LIKE ? OR description LIKE ? ) ";
			keyword = "%" + keyword + "%";
			param.add(keyword);
			param.add(keyword);
		}
		
		Collection result = super.select(sql, HashMap.class, param.toArray(), 0, 1);
		if(result != null && !result.isEmpty())
		{
			HashMap map = (HashMap) result.iterator().next();
			return Integer.parseInt(String.valueOf(map.get("total")));
		}
		return 0;
	}
	
	protected Collection selectABWCode() throws DaoException{
		String sql="SELECT abw_code,description " +
				"FROM fms_rate_card_abw_code " +
				"ORDER BY case IsNumeric(abw_code) "+
				"when 1 then Replicate(Char(0), 100 - Len(abw_code)) + abw_code "+
				"else abw_code "+
				"end  ";
		return super.select(sql, AbwCodeObject.class, null, 0, -1);
	}

	public boolean isAbwCodeInUse(String abwCode) throws DaoException
	{
		String sql=
			" SELECT abw_code " +
			" FROM fms_rate_card " +
			" WHERE abw_code = ? ";
		Collection result =  super.select(sql, DefaultDataObject.class, new Object[]{abwCode}, 0, -1);
		if(result != null && !result.isEmpty())
		{
			return true;
		}
		return false;
	}
	
	public void insertRateCardEmailNotification(DefaultDataObject obj) throws DaoException
	{
		String sql = 
			" INSERT INTO rate_card_email_notification ( " +
			" 	code, name, internalRate, createdDate ) " +
			" VALUES ( " +
			"	#abwCode#, #rate_card_name#, #internalRate#, #createdDate# ) ";
		super.update(sql, obj);
	}

	public Collection getRateCardEmailNotification(String dateCreatedStart, String dateCreatedEnd) throws DaoException
	{
		ArrayList param = new ArrayList();
		String sql = 
			" SELECT code, name, internalRate, 'inactive' AS inactive " +
			" FROM rate_card_email_notification " +
			" WHERE 1=1 ";
		
		/*sql += " AND (createdDate >= ? AND createdDate <= ?) ";
		param.add(dateCreatedStart);
		param.add(dateCreatedEnd);*/
		
		Collection result = super.select(sql, DefaultDataObject.class, param.toArray(), 0, -1);
		return result;
	}

	public void clearRateCardEmailNotification(String dateCreatedStart, String dateCreatedEnd) throws DaoException
	{
		ArrayList param = new ArrayList();
		String sql = 
			" DELETE FROM rate_card_email_notification " +
			" WHERE 1=1 ";

			/*sql += " AND (createdDate >= ? AND createdDate <= ?) ";
			param.add(dateCreatedStart);
			param.add(dateCreatedEnd);*/
			
		super.update(sql, param.toArray());
	}
	
}
