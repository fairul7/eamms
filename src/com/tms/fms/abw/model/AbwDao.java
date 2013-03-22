package com.tms.fms.abw.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.model.DefaultDataObject;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.facility.model.RateCard;


public class AbwDao extends DataSourceDao {
	public void init() throws DaoException {
		super.init();
		
		try{
			super.update("CREATE TABLE fms_trans_transfer_cost(" +
					"uniqueId VARCHAR(255) NOT NULL, " +
					"projectCode VARCHAR(255) NULL, " +
					"requestId VARCHAR(255) NULL, " +
					"facilityCode VARCHAR(10) NULL, " +
					"quantity INT, " +
					"requiredDateFrom DATETIME NULL, " +
					"requiredDateTo DATETIME NULL, " +
					"cost NUMERIC(18,2), " +
					"type CHAR(1), " +
					"createdDate DATETIME, " +
					"createdBy VARCHAR(255), " +
					"updatedDate DATETIME NULL, " +
					"updatedBy VARCHAR(255), " +
					"status CHAR(1) DEFAULT 'N', " +
					"PRIMARY KEY(uniqueId))", null);
		}
		catch(DaoException e){
			//do nothing
		}
		
		try {
			String sql = "CREATE TABLE abw_project (" +
					"head_project VARCHAR(25) NOT NULL PRIMARY KEY, " +
					"description VARCHAR(255) NULL, " +
					"defaultSystemDate datetime NULL, " +
					"resource_id VARCHAR(25) NULL," +
					"department VARCHAR(25) NULL," +
					"date_from datetime NULL, " +
					"date_to datetime NULL," +
					"no_of_episodes INT NULL," +
					"duration_per VARCHAR(20) NULL," +
					"hr_projectcode VARCHAR(4) NULL, " +
					"hr_projectTitle VARCHAR(30) NULL, " +
					"fms_dateExtracted datetime NULL )";
			super.update(sql, null);
		} catch (Exception e) {
		}
		

		try
		{
			super.update(
					" CREATE TABLE rate_card ( " +
					"		indicator CHAR(1), " +
					"		code VARCHAR(10), " +
					"		name VARCHAR(255), " +
					"		internalRate NUMERIC(18,2), " +
					"		actionTaken CHAR(1), " +
					"		createdDate DATETIME, " +
					"		createdBy VARCHAR(50), " +
					"		updatedDate DATETIME, " +
					"		updatedBy VARCHAR(50), " +
					"		status CHAR(1) DEFAULT 'N' " +
					" ) "
				, null);
		}
		catch(Exception e){}
		
		try
		{
			super.update(
					" CREATE TABLE fms_eng_transfer_cost ( " +
					"		uniqueId VARCHAR(255), " +
					"		projectCode VARCHAR(255), " +
					"		requestId VARCHAR(255), " +
					"		ratecardAbwCode VARCHAR(10), " +
					"		noOfUnit INT, " +
					"		requiredDateFrom DATETIME, " +
					"		requiredDateTo DATETIME, " +
					"		cost NUMERIC(18,2), " +
					"		blockBooking CHAR(1), " +
					"		type CHAR(1), " +
					"		createdDate DATETIME, " +
					"		createdBy VARCHAR(255), " +
					"		updatedDate DATETIME, " +
					"		updatedBy VARCHAR(255), " +
					"		status CHAR(1) DEFAULT 'N', " +
					"		cancellation_ind CHAR(1) DEFAULT 'N', " +
					"		cancellation_remark VARCHAR(50), " +
					"		cancellation_penalty CHAR(1), " +
					"		PRIMARY KEY(uniqueId) " +
					" ) "
				, null);
		}
		catch(Exception e){}
		
		try
		{
			super.update(" ALTER TABLE rate_card ADD unqueId VARCHAR(35) ", null);
		}
		catch(Exception e){}
		
		try
		{	//rename a unqueId column to uniqueId
			super.update(" EXEC sp_rename "+
			"@objname = 'rate_card.unqueId',"+
			"@newname = 'uniqueId', "+
			"@objtype = 'COLUMN' ", null);
		}
		catch(Exception e){}
		
		
		try
		{
			super.update(" ALTER TABLE rate_card ADD ratecardId VARCHAR(255) ", null);
		}
		catch(Exception e){}
	}
	
	public Collection getRateCardChanges(String dateCreatedStart, String dateCreatedEnd, String actionTaken) throws DaoException
	{
		ArrayList param = new ArrayList();
		String sql = 
			" SELECT code, name, internalRate " +
			" FROM rate_card " +
			" WHERE 1=1 ";
		
		sql += " AND (createdDate >= ? AND createdDate <= ?) ";
		param.add(dateCreatedStart);
		param.add(dateCreatedEnd);
		
		if(actionTaken != null && !actionTaken.equals(""))
		{
			sql += " AND actionTaken = ? ";
			param.add(actionTaken);
		}
		
		Collection result = super.select(sql, DefaultDataObject.class, param.toArray(), 0, -1);
		return result;
	}
	
	public void insertRateCard(RateCard rc) throws DaoException 
	{
		String sql =
			" INSERT INTO rate_card ( " +
			" 	indicator, code, name, internalRate, actionTaken, createdDate, createdBy, uniqueId, ratecardId ) " +
			" VALUES ( " +
			"	#indicator#, #abwCode#, #name#, #internalRate#, #actionTaken#, #createdOn#, #createdBy#, #uniqueId#, #id# ) ";
		
		super.update(sql, rc);
	}

	public Collection getProgramCodeListing() throws DaoException {		
		
		//Change pfeCode to use head_project
		Collection col = new ArrayList();		
		String sql = " SELECT head_project as programId, description, date_from as startProductionDate," +
				//" date_to as endProductionDate, hr_projectCode as pfeCode, " +
				" date_to as endProductionDate, head_project as pfeCode, " +
				" CASE WHEN hr_projectTitle IS NULL THEN description ELSE hr_projectTitle END as programName, " +
				" no_of_episodes as noOfEpisodes, duration_per as duration " +
				" FROM abw_project WHERE fms_dateExtracted IS NULL  " ; 
		
		col = super.select(sql, DefaultDataObject.class, null, 0, -1);
		return col;
	}
	
	public void updateProgramCodeDateExtracted(String head_project) throws DaoException {		
		
		String sql = " UPDATE abw_project SET fms_dateExtracted = GETDATE() " +
				"  WHERE head_project =  '" + head_project + "'" ; 
				
		 super.update(sql, null);
		
	}
	
	public void insertAbwTransferCost(AbwTransferCostObject object) throws DaoException{
		String query = "INSERT INTO fms_trans_transfer_cost (uniqueId, projectCode, requestId, " +
				"facilityCode, quantity, requiredDateFrom, requiredDateTo, cost, type, " +
				"createdDate, createdBy) " +
				"VALUES (#uniqueId#, #projectCode#, #requestId#, #facilityCode#, #quantity#, " +
				"#requiredDateFrom#, #requiredDateTo#, #cost#, #type#, #createdDate#, #createdBy#)";
		
		super.update(query, object);
	}
	
	/** code for show abwdb tables listing for testing purpose */
	public Collection abw_projectListing( String sort,
			boolean desc, int start, int rows) throws DaoException {
    	Collection col = new ArrayList();    	
    	String sql = 
    			"SELECT name as namax " +
    			"FROM sys.Tables " +
    			"WHERE name IN ('abw_project', 'fms_eng_transfer_cost', 'fms_trans_transfer_cost', 'rate_card') " +
    			"ORDER BY name ";
    	
    	if(sort!=null && !"".equals(sort)){
    		sql += " ORDER BY " + sort + (desc ? " DESC" : "");
    	}
    	
    	
    	col = super.select(sql, DefaultDataObject.class, null, start, rows);
    	return col;
	}
	public int abw_projectCount() throws DaoException {
		Collection col = new ArrayList();		
		String sql = 
				"SELECT count(*) as total " +
				"FROM sys.Tables " +
				"WHERE name IN ('abw_project', 'fms_eng_transfer_cost', 'fms_trans_transfer_cost', 'rate_card') ";
		
    	col = super.select(sql, HashMap.class, null, 0, 1);
		if (col.size() == 1) {
			HashMap map = (HashMap) col.iterator().next();
			Number total = (Number) map.get("total");
			
			return total.intValue();
		}
		return 0;
	}
	
	
	public Collection columnListing( String tableName) throws DaoException {
    	Collection col = new ArrayList();    	
    	String sql = " SELECT column_name" +
    			"   FROM INFORMATION_SCHEMA.COLUMNS" +
    			"	   WHERE TABLE_NAME   = '" + tableName +"'" ;
    	if(tableName ==null || "".equals(tableName) ){
    		sql = "select 'No Table Selected' as column_name";
		}
    	
    	col = super.select(sql, DefaultDataObject.class, null, 0, -1);
    	return col;
	}
	
	public Collection abwTableListing( String keyword,String tableName,String sort,
			boolean desc, int start, int rows) throws DaoException {
    	Collection col = new ArrayList();    	
    	String sql = " SELECT * FROM " +tableName ;
    	if(tableName ==null || "".equals(tableName) ){
			sql = "select 'Please choose table beside to view' as 'No Table Selected' ";
		}
    	sql += " WHERE 1=1 ";
    	
    	ArrayList  args = new ArrayList();
    	if(keyword !=null && !"".equals(keyword)){
    		sql += " AND ( 1=2  " ;
    		Collection colFilter = columnListing(tableName);
	    	for(Iterator iter=colFilter.iterator();iter.hasNext();){
	    		DefaultDataObject objCol = (DefaultDataObject)iter.next();
	    		sql += " OR " + objCol.getProperty("column_name") + " LIKE ? ";
	    		args.add("%"+ keyword+ "%");
	    	}
	    	sql +=" )";
    	}    	
    	
    	if(sort!=null && !"".equals(sort)){
    		sql += " ORDER BY " + sort + (desc ? " DESC" : "");
    	}
    	
    	
    	col = super.select(sql, DefaultDataObject.class, args.toArray(), start, rows);
    	return col;
	}
	public int abwTableCount(String keyword, String tableName) throws DaoException {
		
		if(tableName == null || "".equals(tableName)){
			return 0;
		}else{
			Collection col = new ArrayList();		
			String sql = " SELECT count(*) as total " +
			" FROM " + tableName ;	
			
			sql += " WHERE 1=1 ";
	    	
	    	ArrayList  args = new ArrayList();
	    	
	    	if(keyword !=null && !"".equals(keyword)){
	    		sql += " AND ( 1=2  " ;
	    		Collection colFilter = columnListing(tableName);
		    	for(Iterator iter=colFilter.iterator();iter.hasNext();){
		    		DefaultDataObject objCol = (DefaultDataObject)iter.next();
		    		sql += " OR " + objCol.getProperty("column_name") + " LIKE ? ";
		    		args.add("%"+ keyword+ "%");
		    	}
		    	sql +=" )";
	    	}
	    	
					
	    	col = super.select(sql, HashMap.class, args.toArray(), 0, 1);
			if (col.size() == 1) {
				HashMap map = (HashMap) col.iterator().next();
				Number total = (Number) map.get("total");
				
				return total.intValue();
		}
		
		
		return 0;
	}
	
	
	}
	/** end test **********************************************/

	public void insertAbwEngTransferCost(DefaultDataObject pushObj) throws DaoException 
	{
		String sql =
			" INSERT INTO fms_eng_transfer_cost ( " +
			" 	uniqueId, projectCode, requestId, ratecardAbwCode, noOfUnit, requiredDateFrom, requiredDateTo, " +
			"	cost, blockBooking, type, createdDate, createdBy ) " +
			" VALUES ( " +
			" 	#uniqueId#, #projectCode#, #requestId#, #ratecardAbwCode#, #noOfUnit#, #requiredDateFrom#, #requiredDateTo#, " +
			"	#cost#, #blockBooking#, #type#, #createdDate#, #createdBy# ) ";
		
		super.update(sql, pushObj);
	}
	
	public boolean existAbwTransferCost(String requestId) throws DaoException {
		
			String sql = " SELECT count(*) as total " +
			" FROM fms_trans_transfer_cost WHERE requestId = ? " ;	
					
	    	Collection col = super.select(sql, HashMap.class, new String[]{requestId}, 0, 1);
			if (col.size() == 1) {
				HashMap map = (HashMap) col.iterator().next();
				Number total = (Number) map.get("total");
				
				if(total.intValue() >0)return true;
				else return false;
		}else{
			return false;
		}
	
	}
	
	public boolean existAbwEngTransferCost(String requestId) throws DaoException {
		
		String sql = " SELECT count(*) as total " +
		" FROM fms_eng_transfer_cost WHERE requestId = ? " ;	
				
    	Collection col = super.select(sql, HashMap.class, new String[]{requestId}, 0, 1);
		if (col.size() == 1) {
			HashMap map = (HashMap) col.iterator().next();
			Number total = (Number) map.get("total");
			
			if(total.intValue() >0)return true;
			else return false;
		}else{
			return false;
		}
	}	
	
	
	//TODO
	protected void updateTransferCost(EngineeringRequest er) throws DaoException {
		String sql = "UPDATE fms_eng_transfer_cost " +
				"SET cancellationCharges=#cancellationCharges#, " +
				"status=#status#, " +
				"modifiedBy=#modifiedBy#, modifiedOn=#modifiedOn#," +
				"cancellationChargeManpower=#cancellationCostManpower# " +
				"WHERE requestId=#requestId# ";
		super.update(sql, er);
	}
	
	//TODO
	public void insertTransferCost(EngineeringRequest er) throws DaoException {
		String sql =
			" INSERT INTO rate_card ( " +
			" 	indicator, code, name, internalRate, actionTaken, createdDate, createdBy ) " +
			" VALUES ( " +
			"	#indicator#, #abwCode#, #name#, #internalRate#, #actionTaken#, #createdOn#, #createdBy# ) ";
		
		super.update(sql, er);
	}
	
	
	
	public void insertTransferCostReversal(TransferCostCancellationObject obj) throws DaoException {
		String sql =
			"INSERT INTO fms_eng_transfer_cost (" +
			"uniqueId,projectCode,requestId,ratecardAbwCode,noOfUnit,requiredDateFrom,requiredDateTo,cost,blockBooking,type,createdDate,createdBy," +
			"status,cancellation_ind,cancellation_remark,cancellation_penalty) " +
			"VALUES ( " +
			"#uniqueId#,#projectCode#,#requestId#,#ratecardAbwCode#,#noOfUnit#,#requiredDateFrom#,#requiredDateTo#,#cost#,#blockBooking#,#type#,#createdDate#,#createdBy#," +
			"#status#,#cancellation_ind#,#cancellation_remark#,#cancellation_penalty#)";
		super.update(sql, obj);
	}
	
	public void insertTransferCostFacilities(TransferCostCancellationObject obj) throws DaoException {
		String sql =
			"INSERT INTO fms_eng_transfer_cost (" +
			"uniqueId,projectCode,requestId,cost,type,createdDate,createdBy," +
			"status,cancellation_ind,cancellation_remark)" +
			"VALUES ( " +
			"#uniqueId#,#projectCode#,#requestId#,#cost#,#type#,#createdDate#,#createdBy#," +
			"#status#,#cancellation_ind#,#cancellation_remark#)";
		super.update(sql, obj);
	}
}