package com.tms.fms.eamms.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.model.DefaultDataObject;

public class EammsDao extends DataSourceDao {
	
	public void init() throws DaoException {
		super.init();
		
		try
		{
			super.update(
				" CREATE TABLE staff_workload ( " +
				"		id VARCHAR(100) NOT NULL, " +
				"		userId VARCHAR(250) NOT NULL, " +
				"		cmOnHand VARCHAR(5) NOT NULL DEFAULT '0', " +
				"		pmOnHand VARCHAR(5) NOT NULL DEFAULT '0', " +
				"		woOnHand VARCHAR(5) NOT NULL DEFAULT '0', " +
				"		lastUpdatedDate VARCHAR(30) NOT NULL, " +
				"		PRIMARY KEY (id) " +
				" ) "
				, null);
		}
		catch(Exception e){}
	}
	
	public void setOverdueStatus() throws DaoException {
		String sql = " update app_fd_eamms_asset_rental " +
				" SET c_status ='Overdue' " +
				" WHERE Convert(datetime,c_toDate) < getdate() " +
				" AND c_status='Rent Out' ";
		
		super.update(sql, null);
		
	}
	
	public DefaultDataObject getRentalReqestInfo(String id) throws DaoException {
		String sql = " select  r.c_rentalId as rentalId, r.c_toDate as toDate , " +
				" (s.firstName +' '+s.lastName) as requestorName, s.email1 as requestorEmail, " +
				" (se.firstName +' '+se.lastName) as engineerName, se.email1 as engineerEmail " +
				" FROM app_fd_eamms_asset_rental r " +
				" LEFT JOIN security_user s ON (s.username=r.c_createdBy) " +
				" LEFT JOIN security_user se ON (se.username=r.c_engineerAssign) " +
				" WHERE r.id = ? " ;    	
		
			Collection col = super.select(sql, DefaultDataObject.class, new String[]{id}, 0, 1);
			if (col.size() == 1){
				return (DefaultDataObject) col.iterator().next();
			}				
		return new DefaultDataObject();
	}
	
	public Collection getRentalReqestDueReminderListing() throws DaoException {
		String sql = " select id " +
				" from app_fd_eamms_asset_rental " +
				" WHERE  c_status='Rent Out' AND Convert(datetime,c_toDate,103) = Convert(datetime,DATEADD(day, 1, DATEDIFF(dd, 0, GETDATE())),103)" +
				"  " ;    	
		
			Collection col = super.select(sql, DefaultDataObject.class, null, 0, -1);
			return col;
	}
	
	public Collection getOverdueItemsListing() throws DaoException {
		String sql = " SELECT c_rentalId AS rentalId FROM  app_fd_eamms_asset_rental " +
				" WHERE Convert(datetime,c_toDate) < getdate()AND c_status='Rent Out' " ;    	
		
			Collection col = super.select(sql, DefaultDataObject.class, null, 0, -1);
			return col;
	}
	
	public void insertRentalStatusTrail(DefaultDataObject obj) throws DaoException {
		String sql = " INSERT INTO app_fd_eamms_asset_rental_status(id,rentalId,status,createdBy,dateCreated) " +
				" VALUES (#id#,#rentalId#,#status#,#createdBy#,getdate()) ";
		
		super.update(sql, obj);
		
	}
	
	//-------- PM -------------------
	public Collection getPMOverdueItemsListing() throws DaoException {
		String sql = "SELECT c_pmRequestId,c_status, c_endDate " +
			"FROM app_fd_eamms_pm_request " +
			"WHERE CONVERT(datetime,c_endDate,103) <= getdate() " +
			"AND c_status = 'N' OR c_status = 'P'" ;    	
		
		Collection col = super.select(sql, DefaultDataObject.class, null, 0, -1);
		return col;
	}
	
	public void setPMOverdueStatus(DefaultDataObject obj) throws DaoException {
		String sql = "UPDATE app_fd_eamms_pm_request SET " +
				"c_status = #c_status#," +
				"dateModified = #dateModified# " +
				"WHERE c_pmRequestId = #c_pmRequestId#";
		super.update(sql, obj);
	}
	
	public void setSoftwareExpiredStatus(DefaultDataObject obj) throws DaoException {
		String sql = "UPDATE app_fd_eamms_asset_sw SET " +
				"c_status = #c_status#," +
				"dateModified = #dateModified# " +
				"WHERE id = #id#";
		super.update(sql, obj);
	}
	
	public Collection getSoftwareExpiredDate() throws DaoException {
		String sql = "SELECT id from app_fd_eamms_asset_sw "+ 
					"WHERE CONVERT(datetime,c_licenseExpiryDate,103) <= getdate() "+
					"and c_status <> ? ";
							
		Collection col = super.select(sql, DefaultDataObject.class, new String[]{SoftwareExpiredDate.EXPIRED_STATUS}, 0, -1);
		return col;
	}

	public Collection getPMReqestDueReminderListing() throws DaoException {
		String sql = "SELECT id " +
				"FROM app_fd_eamms_pm_request " +
				"WHERE CONVERT(datetime,c_endDate,103) = CONVERT(datetime,DATEADD(day, 7, DATEDIFF(dd, 0, GETDATE())),103)" ;
		
		Collection col = super.select(sql, DefaultDataObject.class, null, 0, -1);
		return col;
	}
	
	public DefaultDataObject getPMInfo(String id) throws DaoException {
		String sql = "SELECT s1.firstName +' '+ s1.lastName AS engName1,s1.email1 AS emailEng1," +
							"s2.firstName +' '+ s2.lastName AS engName2,s2.email1 AS emailEng2," +
							"s3.firstName +' '+ s3.lastName AS engName3,s3.email1 AS emailEng3," +
							"s4.firstName +' '+ s4.lastName AS engName4,s4.email1 AS emailEng4," +
							"c_pmRequestId,c_endDate," +
							"s.firstName +' '+ s.lastName AS requestorName,s.email1 AS requestorEmail " +
							"FROM app_fd_eamms_pm_request r " +
							"LEFT JOIN security_user s ON (s.username=c_createdBy) " +
							"LEFT JOIN security_user s1 ON (s1.username=c_engineer1UserId) " +
							"LEFT JOIN security_user s2 ON (s2.username=c_engineer2UserId) " +
							"LEFT JOIN security_user s3 ON (s3.username=c_engineer3UserId) " +
							"LEFT JOIN security_user s4 ON (s4.username=c_engineer4UserId) " +
							"WHERE r.id=?" ;    	
		
			Collection col = super.select(sql, DefaultDataObject.class, new String[]{id}, 0, 1);
			if (col.size() == 1){
				return (DefaultDataObject) col.iterator().next();
			}				
		return new DefaultDataObject();
	}

	public Collection<DefaultDataObject> getAllStaff() throws DaoException
	{
		String sql =
			" SELECT su.id AS userId, su.username " +
			" FROM security_user su " +
			" INNER JOIN fms_unit fu ON (su.unit=fu.id) " +
			" INNER JOIN fms_department fd ON (fd.id=fu.department_id) " +
			" WHERE fd.id = 'D0014' " + // ENGINEERING DEPARTMENT
			" AND su.active = '1' ";
		
		Collection result = super.select(sql, DefaultDataObject.class, null, 0, -1);
		return result;
	}

	public void insertStaffWorkload(DefaultDataObject userObj) throws DaoException
	{
		String sql = 
			" INSERT INTO staff_workload ( " +
			"	id, userId, cmOnHand, pmOnHand, woOnHand, lastUpdatedDate ) " +
			" VALUES ( " +
			"	#id#, #userId#, #cmOnHand#, #pmOnHand#, #woOnHand#, #lastUpdatedDate# ) ";
		
		super.update(sql, userObj);
	}

	public int getCountWorkloadInWO(String userId) throws DaoException
	{
		ArrayList param = new ArrayList();
		String sql =
			" SELECT COUNT(wr.id) AS total " +
			" FROM app_fd_eamms_wo_request wr " +
			" INNER JOIN app_fd_eamms_status_wo swo ON (wr.c_status=swo.c_id) " +
			" WHERE 1=1 " +
			" AND (c_engineer1UserId = ? OR c_engineer2UserId = ? OR " +
			"		c_engineer3UserId = ? OR c_engineer4UserId = ?) " +
			" AND (swo.c_id = '01' OR swo.c_id = '02') ";
		param.add(userId);
		param.add(userId);
		param.add(userId);
		param.add(userId);
		
		Collection result = super.select(sql, HashMap.class, param.toArray(), 0, 1);
		if(result != null && !result.isEmpty())
		{
			HashMap map = (HashMap) result.iterator().next();
			Number total = (Number)map.get("total");
			
			return total.intValue();
		}
		return 0;
	}

	public int getCountWorkloadInPM(String userId) throws DaoException
	{
		ArrayList param = new ArrayList();
		String sql =
			" SELECT COUNT(pr.id) AS total " +
			" FROM app_fd_eamms_pm_request pr " +
			" INNER JOIN app_fd_eamms_status_pm spm ON (pr.c_status=spm.id) " +
			" WHERE 1=1 " +
			" AND (c_engineer1UserId = ? OR c_engineer2UserId = ? OR  " +
			"		c_engineer3UserId = ? OR c_engineer4UserId = ?) " +
			" AND spm.id <> 'E' ";
		param.add(userId);
		param.add(userId);
		param.add(userId);
		param.add(userId);
		
		Collection result = super.select(sql, HashMap.class, param.toArray(), 0, 1);
		if(result != null && !result.isEmpty())
		{
			HashMap map = (HashMap) result.iterator().next();
			Number total = (Number)map.get("total");
			
			return total.intValue();
		}
		return 0;
	}

	public int getCountWorkloadInCM(String userId) throws DaoException
	{
		ArrayList param = new ArrayList();
		String sql =
			" SELECT COUNT(cr.id) AS total " +
			" FROM app_fd_eamms_cm_request cr " +
			" WHERE 1=1 " +
			" AND (c_engineer1UserId = ? OR c_engineer2UserId = ? OR " +
			"		c_engineer3UserId = ? OR c_engineer4UserId = ?) " +
			" AND (cr.c_status = 'In Progress' OR cr.c_status = 'Assigned') ";
		param.add(userId);
		param.add(userId);
		param.add(userId);
		param.add(userId);
		
		Collection result = super.select(sql, HashMap.class, param.toArray(), 0, 1);
		if(result != null && !result.isEmpty())
		{
			HashMap map = (HashMap) result.iterator().next();
			Number total = (Number)map.get("total");
			
			return total.intValue();
		}
		return 0;
	}
}
