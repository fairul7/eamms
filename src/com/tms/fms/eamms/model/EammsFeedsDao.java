package com.tms.fms.eamms.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.model.DefaultDataObject;
import kacang.util.Log;

import com.tms.fms.eamms.ui.FeedRequisitionForm;
import com.tms.fms.engineering.ui.ServiceDetailsForm;

public class EammsFeedsDao extends DataSourceDao 
{
	public void init() throws DaoException 
	{
		super.init();
		
		createSetupTable("fms_feed_network_status");
		//insert default into fms_feed_network_status
		{
			ArrayList arr = new ArrayList();
			arr.add(new Object[]{"01", "New", "", "1", "", null});
			arr.add(new Object[]{"02", "Submitted", "", "1", "", null});
			arr.add(new Object[]{"03", "Unit Head Verified", "", "1", "", null});
			arr.add(new Object[]{"04", "Network Approved", "", "1", "", null});
			arr.add(new Object[]{"05", "Network Processed", "", "1", "", null});
			arr.add(new Object[]{"06", "Closed", "", "1", "", null});
			arr.add(new Object[]{"07", "Cancelled", "", "1", "", null});
			arr.add(new Object[]{"08", "Rejected", "", "1", "", null});
			
			insertSetupTable("fms_feed_network_status", arr);
		}
		
		createSetupTable("fms_feed_telco");
		//insert default into fms_feed_telco
		{
			ArrayList arr = new ArrayList();
			arr.add(new Object[]{"01", "TM", "", "1", "", null});
			arr.add(new Object[]{"02", "MEASAT", "", "1", "", null});
			arr.add(new Object[]{"03", "MAXIS", "", "1", "", null});
			arr.add(new Object[]{"04", "TIME BB", "", "1", "", null});
			arr.add(new Object[]{"05", "BGAN", "", "1", "", null});
			arr.add(new Object[]{"06", "OTHERS", "", "1", "", null});
			
			insertSetupTable("fms_feed_telco", arr);
		}
		
		createSetupTable("fms_feed_oblink");
		//insert default into fms_feed_oblink
		{
			ArrayList arr = new ArrayList();
			arr.add(new Object[]{"01", "DSNG", "", "1", "", null});
			arr.add(new Object[]{"02", "VSAT", "", "1", "", null});
			arr.add(new Object[]{"03", "MICROWAVE", "", "1", "", null});
			
			insertSetupTable("fms_feed_oblink", arr);
		}
		
		createSetupTable("fms_feed_station");
		//insert default into fms_feed_station
		{
			ArrayList arr = new ArrayList();
			arr.add(new Object[]{"01", "TV3", "", "1", "", null});
			arr.add(new Object[]{"02", "NTV7", "", "1", "", null});
			arr.add(new Object[]{"03", "8TV", "", "1", "", null});
			arr.add(new Object[]{"04", "TV9", "", "1", "", null});
			
			insertSetupTable("fms_feed_station", arr);
		}
		
		try
		{
			super.update(
				" CREATE TABLE fms_feeds_details ( " +
				"		id VARCHAR(100) NOT NULL, " +
				"		requestId VARCHAR(255) NOT NULL, " +
				"		submittedBy VARCHAR(250), " +
				"		requestedDate DATETIME, " +
				"		location VARCHAR(255), " +
				"		telco VARCHAR(30), " +
				"		oblink VARCHAR(30), " +
				"		networkStatus VARCHAR(30), " +
				"		PRIMARY KEY (id) " +
				" ) "
				, null);
		}
		catch(Exception e){}
		
		try
		{
			super.update(
				" CREATE VIEW fms_user_details " +
				" AS " +
				" SELECT su.id AS userId, firstname, lastname, email1 AS email, telMobile, telOffice, property1 AS staffId, " +
				" fd.name AS department, fu.name AS unit, su.active " +
				" FROM security_user su " +
				" INNER JOIN fms_department fd ON fd.id=su.department " +
				" INNER JOIN fms_unit fu ON fu.id=su.unit "
				, null);
		}
		catch(Exception e){}
		
		try
		{
			super.update(
				"CREATE TABLE fms_feeds_assignment ( " +
				"		id VARCHAR(100) NOT NULL, " +
				"		assignmentId VARCHAR(255) NOT NULL, " +
				"		feedsDetailsId VARCHAR(100) NOT NULL, " +
				"		requiredDateFrom DATETIME, " +
				"		requiredDateTo DATETIME, " +
				"		requiredTimeFrom VARCHAR(4) NOT NULL, " +
				"		requiredTimeTo VARCHAR(4) NOT NULL, " +
				"		timezone VARCHAR(3) NOT NULL, " +
				"		totalTimeReq VARCHAR(3) NOT NULL, " +
				"		timeMeasure CHAR(1) NOT NULL, " +
				"		tvroServiceId VARCHAR(255), " +
				"		remarks VARCHAR(255), " +
				"		bookingStatus VARCHAR(20), " +
				"		networkRemarks TEXT, " +
				"		attachment VARCHAR(100), " +
				"		status VARCHAR(20), " +
				"		createdBy VARCHAR(250), " +
				"		createdDate DATETIME, " +
				"		PRIMARY KEY (id) " +
				" ) "
				, null);
		}
		catch(Exception e){}
		
		try
		{
			super.update(
				" CREATE TABLE fms_feeds_status ( " +
				"		id VARCHAR(100) NOT NULL, " +
				"		feedsDetailsId VARCHAR(100) NOT NULL, " +
				"		status VARCHAR(20) NOT NULL, " +
				"		createdBy VARCHAR(250), " +
				"		createdDate DATETIME, " +
				"		PRIMARY KEY (id) " +
				" ) "
				, null);
		}
		catch(Exception e){}
		
		try
		{
			super.update(
				" CREATE TABLE fms_feeds_log ( " +
				"		feedLogId VARCHAR(100) NOT NULL, " +
				"		adhoc CHAR(1) NOT NULL, " +
				"		program VARCHAR(255), " +
				"		requestId VARCHAR(255), " +
				"		assignmentId VARCHAR(255), " +
				"		date DATETIME, " +
				"		location VARCHAR(255), " +
				"		station VARCHAR(30) NOT NULL, " +
				"		dateIn DATETIME, " +
				"		timeIn VARCHAR(4), " +
				"		dateOut DATETIME, " +
				"		timeOut VARCHAR(4), " +
				"		ebNo VARCHAR(10), " +
				"		assAV1 CHAR(1), " +
				"		assAV2 CHAR(1), " +
				"		mcr VARCHAR(250), " +
				"		news VARCHAR(100), " +
				"		stringer VARCHAR(100), " +
				"		telco VARCHAR(30), " +
				"		remarks VARCHAR(255), " +
				"		status VARCHAR(20), " +
				"		createdBy VARCHAR(250), " +
				"		createdDate DATETIME, " +
				"		modifiedBy VARCHAR(250), " +
				"		modifiedDate DATETIME, " +
				"		PRIMARY KEY (feedLogId) " +
				" ) "
				, null);
		}
		catch(Exception e){}
		
		try
		{
			super.update(
				" CREATE TABLE fms_feeds_assigned_eng ( " +
				"		requestId VARCHAR(100) NOT NULL, " +	
				"		userId VARCHAR(100) NOT NULL " +
				" ) "
				, null);
		}
		catch(Exception e){}
	}
	
	private void createSetupTable(String tbName)
	{
		try
		{
			String sql =
				" CREATE TABLE " + tbName + " ( " +
				"		id VARCHAR(30) NOT NULL, " +
				"		c_name VARCHAR(50) NOT NULL, " +
				"		description VARCHAR(255), " +
				"		active CHAR(1) NOT NULL, " +
				"		createdBy VARCHAR(250), " +
				"		dateCreated DATETIME, " +
				"		PRIMARY KEY (id) " +
				" ) ";
			
			super.update(sql, null);
		}
		catch(Exception e){}
	}
	
	private void insertSetupTable(String tbName, ArrayList arr)
	{
		String sql = 
			" INSERT INTO " + tbName + " ( " +
			"	id, c_name, description, active, createdBy, dateCreated) " +
			" VALUES (?, ?, ?, ?, ?, ?) ";
		
		for(Iterator itr = arr.iterator();itr.hasNext();)
		{
			try
			{
				Object[] objArr = (Object[]) itr.next();
				super.update(sql, objArr);
			}
			catch(Exception e){}
		}
	}
	
	public Collection getSetupTable(String tableName, String id) throws DaoException 
	{
		ArrayList param = new ArrayList();
		String sql = 
			" SELECT id, c_name, description, active, createdBy, dateCreated " +
			" FROM " + tableName;
			
		if(id != null && !id.equals(""))
		{
			sql += " WHERE id = ? ";
			param.add(id);
		}
		
		Collection result = super.select(sql, DefaultDataObject.class, param.toArray(), 0, -1);
		return result;
	}
	
	public void insertEammsFeedsDetails(EammsFeedsDetails obj) throws DaoException 
	{
		String sql =
			" INSERT INTO fms_feeds_details ( " +
			"	id, requestId, submittedBy, requestedDate, location, telco, oblink, networkStatus ) " +
			" VALUES ( " +
			"	#id#, #requestId#, #submittedBy#, #requestedDate#, #location#, #telco#, #oblink#, #networkStatus# ) ";
		
		super.update(sql, obj);
	}
	
	public void updateFeedsRequestDetails(DefaultDataObject obj) throws DaoException 
	{
		String sql = " UPDATE fms_feeds_details SET ";
		
		String mode = (String) obj.getProperty("mode");
		if(FeedRequisitionForm.ENGINEERING_COORDINATOR_VIEW.equals(mode))
		{
			sql += " telco = #telco#, oblink = #oblink#, location = #location#, " +
					" networkStatus = #networkStatus#, submittedBy = #submittedBy# ";
		}
		else if(FeedRequisitionForm.UNIT_HEAD_ENG_VIEW.equals(mode))
		{
			sql += " telco = #telco#, oblink = #oblink#, networkStatus = #networkStatus# ";
		}
		else if (FeedRequisitionForm.UNIT_HEAD_NET_VEIW.equals(mode))
		{
			if("08".equals(obj.getProperty("networkStatus")))
			{
				sql += " networkStatus = #networkStatus# ";
			}
			else
			{
				sql += " telco = #telco#, oblink = #oblink#, networkStatus = #networkStatus# ";
			}
		}
		else if(FeedRequisitionForm.NETWORK_ENGINEER_VIEW.equals(mode))
		{
			sql += " networkStatus = #networkStatus# ";
		}
		
		sql += " WHERE id = #id# ";
		
		super.update(sql, obj);
	}
	
	public Collection getEammsFeedsDetails(String requestId, String feedsDetailsId) throws DaoException 
	{
		String sql =
			" SELECT id, requestId, submittedBy, requestedDate, location, telco, oblink, networkStatus " +
			" FROM fms_feeds_details " +
			" WHERE 1=1 ";
		
		if(requestId != null && !requestId.equals(""))
		{
			sql += " AND requestId = ? ";
		}
		else if (feedsDetailsId != null && !feedsDetailsId.equals(""))
		{
			sql += " AND id = ? ";
		}
		
		Collection result = super.select(sql, EammsFeedsDetails.class, new Object[]{requestId}, 0, -1);
		return result;
	}
	
	private Map getEammsFeedsDetailsMap(HashMap args)
	{
		HashMap map = new HashMap();
		String feedsDetailsId = (String) args.get("feedsDetailsId");
		String searchBy = (String) args.get("searchBy");
		String nwStatus = (String) args.get("nwStatus");
		String dateFrom = (String) args.get("dateFrom"); 
		String dateTo = (String) args.get("dateTo");
		String userId = (String) args.get("userId");
		
		String sqlSelect =
			" SELECT r.requestId, p.programName, r.title, r.requiredFrom, r.requiredTo, " +
			" rs.feedType, d.networkStatus " +
			" FROM fms_feeds_details d " +
			" INNER JOIN fms_eng_request r on (r.requestId=d.requestId) " +
			" LEFT JOIN fms_prog_setup p on (r.program=p.programId) " +
			" LEFT JOIN fms_eng_request_services rs on (d.requestId=rs.requestId AND rs.serviceId = '7') ";
		map.put(EammsFeedsModule.SQL_SELECT, sqlSelect);
		
		String sqlSelectCount =
			" SELECT COUNT(r.requestId) AS total " +
			" FROM fms_feeds_details d " +
			" INNER JOIN fms_eng_request r on (r.requestId=d.requestId) " +
			" LEFT JOIN fms_prog_setup p on (r.program=p.programId) " +
			" LEFT JOIN fms_eng_request_services rs on (d.requestId=rs.requestId AND rs.serviceId = '7') ";
		map.put(EammsFeedsModule.SQL_SELECT_COUNT, sqlSelectCount);
		
		ArrayList param = new ArrayList();
		String condition = " WHERE 1=? ";
		param.add("1");
		if(searchBy != null && !searchBy.equals(""))
		{
			searchBy = "%" + searchBy + "%";
			
			condition += " AND ( r.requestId LIKE ? OR p.programName LIKE ? OR r.title LIKE ? ) ";
			param.add(searchBy);
			param.add(searchBy);
			param.add(searchBy);
		}
		
		if(nwStatus != null && !nwStatus.equals("") && !nwStatus.equals("-1"))
		{
			if(nwStatus.length() >= 4 )
			{
				condition += " AND d.networkStatus IN ( " + nwStatus + " ) ";
			}
			else
			{
				condition += " AND d.networkStatus = ? ";
				param.add(nwStatus);
			}
		}
		
		if(feedsDetailsId != null && !feedsDetailsId.equals(""))
		{
			condition += " AND d.id = ? ";
			param.add(feedsDetailsId);
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		if((dateFrom != null && !dateFrom.equals("")) && (dateTo != null && !dateTo.equals("")))
		{
			try
			{
				condition += " AND ( r.requiredFrom <= ? AND r.requiredTo >= ? ) ";
				param.add(sdf.parse(dateFrom));
				param.add(sdf.parse(dateTo));
			}
			catch(Exception e){}
		}
		else
		{
			if(dateFrom != null && !dateFrom.equals(""))
			{
				try
				{
					condition += " AND r.requiredFrom <= ? ";
					param.add(sdf.parse(dateFrom));
				}
				catch(Exception e){}
			}
			else if(dateTo != null && !dateTo.equals(""))
			{
				try
				{
					condition += " AND r.requiredTo >= ? ";
					param.add(sdf.parse(dateTo));
				}
				catch(Exception e){}
			}
		}
		
		if(userId != null && !userId.equals(""))
		{
			condition += 
				" AND d.requestId IN ( " +
				"		SELECT requestId " +
				"		FROM fms_feeds_assigned_eng " +
				"		WHERE userId = ? " +
				" ) ";
			param.add(userId);
		}
		
		map.put(EammsFeedsModule.SQL_WHERE, condition);
		map.put(EammsFeedsModule.SQL_PARAM, param);
		return map;
	}

	public Collection getEammsFeedsDetails(String searchBy, String nwStatus, String dateFrom, 
			String dateTo, String sort, boolean isDesc, int start, int rows) throws DaoException
	{
		HashMap args = new HashMap();
		args.put("searchBy", searchBy);
		args.put("nwStatus", nwStatus);
		args.put("dateFrom", dateFrom); 
		args.put("dateTo", dateTo);
		
		HashMap map = (HashMap) getEammsFeedsDetailsMap(args);
		String select = (String) map.get(EammsFeedsModule.SQL_SELECT);
		String where =  (String) map.get(EammsFeedsModule.SQL_WHERE);
		ArrayList p = (ArrayList) map.get(EammsFeedsModule.SQL_PARAM);
		
		ArrayList param = new ArrayList();
		String sql = select + where;
		param.addAll(p);
		
		if(sort != null && !sort.equals(""))
		{
			sql += " ORDER BY " + sort;
			if(isDesc)
			{
				sql += " DESC ";
			}
		}
		else
		{
			sql += " ORDER BY r.requiredFrom DESC "; 
		}
		
		Collection result = super.select(sql, DefaultDataObject.class, param.toArray(), start, rows);
		return result;
	}

	public int getCountEammsFeedsDetails(String searchBy, String nwStatus, String dateFrom, String dateTo) throws DaoException
	{
		HashMap args = new HashMap();
		args.put("searchBy", searchBy);
		args.put("nwStatus", nwStatus);
		args.put("dateFrom", dateFrom); 
		args.put("dateTo", dateTo);
		
		HashMap map = (HashMap) getEammsFeedsDetailsMap(args);
		String select = (String) map.get(EammsFeedsModule.SQL_SELECT_COUNT);
		String where =  (String) map.get(EammsFeedsModule.SQL_WHERE);
		ArrayList p = (ArrayList) map.get(EammsFeedsModule.SQL_PARAM);
		
		ArrayList param = new ArrayList();
		String sql = select + where;
		param.addAll(p);
		
		Collection result = super.select(sql, HashMap.class, param.toArray(), 0, 1);
		if(result != null && !result.isEmpty())
		{
			HashMap m = (HashMap) result.iterator().next();
			Number total = (Number) m.get("total");
			
			return total.intValue();
		}
		return 0;
	}
	
	public EammsFeedsDetails getFeedsRequestDetails(String requestId) throws DaoException
	{
		String sql =
			" SELECT (ud.firstName + ' ' + ud.lastName) AS submittedBy, ud.department, " +
			" ud.staffId, d.requestedDate, d.location, d.networkStatus, d.telco AS telcoId, d.oblink AS oblinkId, " +
			" t.c_name AS telco, o.c_name AS oblink, d.id, " +
			" p.programName " +
			" FROM fms_feeds_details d " +
			" INNER JOIN fms_eng_request r ON (r.requestId=d.requestId) " +
			" LEFT JOIN fms_prog_setup p ON (r.program=p.programId) " +
			" LEFT JOIN fms_feed_telco t ON (d.telco=t.id) " +
			" LEFT JOIN fms_feed_oblink o ON (d.oblink=o.id) " +
			" LEFT JOIN fms_user_details ud ON (d.submittedBy=ud.userId) " +
			" WHERE 1=1 " +
			" AND d.requestId = ? ";
		
		Collection result = super.select(sql, EammsFeedsDetails.class, new Object[]{requestId}, 0, 1);
		if(result != null && !result.isEmpty())
		{
			EammsFeedsDetails obj = (EammsFeedsDetails) result.iterator().next();
			String sql2 = 
				" SELECT (ud.firstName + ' ' + ud.lastName) AS ecAssigned " +
				" FROM fms_feeds_assigned_eng en " +
				" INNER JOIN fms_user_details ud ON (en.userId=ud.userId) " +
				" WHERE 1=1 " +
				" AND requestId = ? ";
			
			Collection result2 = super.select(sql2, DefaultDataObject.class, new Object[]{requestId}, 0, -1);
			if(result2 != null && !result2.isEmpty())
			{
				String assignedEcStr = "";
				for(Iterator itr = result2.iterator(); itr.hasNext();)
				{
					DefaultDataObject ddo = (DefaultDataObject)itr.next();
					String ecAssigned = (String) ddo.getProperty("ecAssigned");
					if(assignedEcStr.equals(""))
					{
						assignedEcStr = ecAssigned;
					}
					else
					{
						assignedEcStr = assignedEcStr + ", " + ecAssigned;
					}
				}
				obj.setProperty("assignedEcStr", assignedEcStr);
			}
			return obj;
		}
		return new EammsFeedsDetails();
	}
	
	public void insertAssignment(EammsAssignment assignObj) throws DaoException
	{
		String sql =
			" INSERT INTO fms_feeds_assignment ( id, assignmentId, feedsDetailsId, requiredDateFrom, requiredDateTo, " +
			"	requiredTimeFrom, requiredTimeTo, timezone, totalTimeReq, timeMeasure, tvroServiceId, remarks, " +
			"	bookingStatus, networkRemarks, attachment, status, createdBy, createdDate ) " +
			" VALUES (#id#, #assignmentId#, #feedsDetailsId#, #requiredDateFrom#, #requiredDateTo#, " +
			"	#requiredTimeFrom#, #requiredTimeTo#, #timezone#, #totalTimeReq#, #timeMeasure#, #tvroServiceId#, #remarks#, " +
			"	#bookingStatus#, #networkRemarks#, #attachment#, #status#, #createdBy#, #createdDate# ) ";
		
		super.update(sql, assignObj);
	}
	
	public void editAssignment(EammsAssignment assignObj) throws DaoException
	{
		String sql =
			" UPDATE fms_feeds_assignment SET requiredTimeFrom=#requiredTimeFrom#, " +
			"	requiredTimeTo=#requiredTimeTo#, timezone=#timezone#, totalTimeReq=#totalTimeReq#, timeMeasure=#timeMeasure#, " +
			"	remarks=#remarks#, #requiredDateFrom#, #requiredDateTo# " +
			" WHERE assignmentId=#assignmentId# ";
		
		super.update(sql, assignObj);
	}
	
	public void updateAssignment(EammsAssignment assignObj) throws DaoException
	{
		String sql =
			" UPDATE fms_feeds_assignment SET bookingStatus=#bookingStatus#, networkRemarks=#networkRemarks#, " +
			"	attachment=#attachment#, status=#status# " +
			" WHERE assignmentId=#assignmentId# ";
		
		super.update(sql, assignObj);
	}

	public Collection getAssignments(String requestId, String assignmentId) throws DaoException
	{
		ArrayList param = new ArrayList();
		String sql =
			" SELECT a.id, a.assignmentId, a.feedsDetailsId, a.requiredTimeFrom, a.requiredTimeTo, " +
			" a.timezone, a.totalTimeReq, a.timeMeasure, a.tvroServiceId, a.remarks, a.bookingStatus, a.networkRemarks, a.attachment, " +
			" a.status, a.createdBy, a.createdDate, a.requiredDateFrom, a.requiredDateTo, " +
			" t.feedTitle AS feedTitle, t.blockbooking " +
			" FROM fms_feeds_assignment a " +
			" INNER JOIN fms_feeds_details s ON (a.feedsDetailsId = s.id) " +
			" INNER JOIN fms_eng_request r on (r.requestId = s.requestId) " +
			" INNER JOIN fms_eng_service_tvro t ON (a.tvroServiceId = t.id) " +
			" WHERE 1=? ";
		param.add("1");
		
		if(requestId != null && !requestId.equals(""))
		{
			sql += " AND s.requestId = ? ";
			param.add(requestId);
		}
		else if (assignmentId != null && !assignmentId.equals(""))
		{
			sql += " AND a.assignmentId = ? ";
			param.add(assignmentId);
		}
		
		Collection result = super.select(sql, EammsAssignment.class, param.toArray(), 0, -1);
		return result;
	}
	
	public void insertStatusTrail(StatusTrail obj) throws DaoException
	{
		String sql = 
			" INSERT INTO fms_feeds_status ( id, feedsDetailsId, status, createdBy, createdDate ) " +
			" VALUES (#id#, #feedsDetailsId#, #status#, #createdBy#, #createdDate# ) ";
		
		super.update(sql, obj);
	}
	
	public Collection getStatusTrail(String feedsDetailsId) throws DaoException
	{
		String sql = 
			" SELECT s.id, s.feedsDetailsId, ns.c_name AS status, (ud.firstName + ' ' + ud.lastName) AS createdBy, s.createdDate " +
			" FROM fms_feeds_status s " +
			" LEFT JOIN fms_user_details ud ON (s.createdBy=ud.userId) " +
			" LEFT JOIN fms_feed_network_status ns ON (s.status=ns.id) " +
			" WHERE feedsDetailsId = ? ";
		
		Collection result = super.select(sql, StatusTrail.class, new Object[]{feedsDetailsId}, 0, -1);
		return result;
	}

	public boolean isAllAssignmentsHvStatus(String feedsDetailsId) throws DaoException
	{
		String sql = " SELECT id FROM fms_feeds_assignment WHERE feedsDetailsId = ? AND ( status = '' OR status IS NULL ) ";
		
		Collection result = super.select(sql, DefaultDataObject.class, new Object[]{feedsDetailsId}, 0, -1);
		if(result != null && !result.isEmpty())
		{
			return false;
		}
		return true;
	}

	public String getLatestAssignId() throws DaoException
	{
		String sql = " SELECT assignmentId FROM fms_feeds_assignment ORDER BY assignmentId DESC ";
		
		Collection result = super.select(sql, DefaultDataObject.class, null, 0, 1);
		if(result != null && !result.isEmpty())
		{
			DefaultDataObject obj = (DefaultDataObject) result.iterator().next();
			return (String) obj.getProperty("assignmentId");
		}
		return "";
	}

	public Collection getTvroServiceIds(String feedsDetailsId) throws DaoException
	{
		String sql =
			" SELECT t.id, t.feedTitle " +
			" FROM fms_feeds_details d " +
			" INNER JOIN fms_eng_service_tvro t ON (d.requestId=t.requestId) " +
			" WHERE 1=1 " +
			" AND d.id = ? ";
		
		Collection result = super.select(sql, DefaultDataObject.class, new Object[]{feedsDetailsId}, 0, -1);
		return result;
	}

	public void deleteAssignment(String assignmentId) throws DaoException
	{
		String sql = " DELETE FROM fms_feeds_assignment WHERE assignmentId = ? ";
		super.update(sql, new Object[]{assignmentId});
	}

	public Collection getEammsFeedsDetailsEC(String userId, String searchBy, String nwStatus, String dateFrom, String dateTo, 
			String sort, boolean isDesc, int start, int rows) throws DaoException
	{
		HashMap args = new HashMap();
		args.put("searchBy", searchBy);
		args.put("nwStatus", nwStatus);
		args.put("dateFrom", dateFrom); 
		args.put("dateTo", dateTo);
		args.put("userId", userId);
		
		HashMap map = (HashMap) getEammsFeedsDetailsMap(args);
		String select = (String) map.get(EammsFeedsModule.SQL_SELECT);
		String where =  (String) map.get(EammsFeedsModule.SQL_WHERE);
		ArrayList p = (ArrayList) map.get(EammsFeedsModule.SQL_PARAM);
		
		ArrayList param = new ArrayList();
		String sql = select + where;
		param.addAll(p);
		
		if(sort != null && !sort.equals(""))
		{
			sql += " ORDER BY " + sort;
			if(isDesc)
			{
				sql += " DESC ";
			}
		}
		else
		{
			sql += " ORDER BY r.requiredFrom DESC "; 
		}
		
		Collection result = super.select(sql, DefaultDataObject.class, param.toArray(), start, rows);
		return result;
	}

	public int getCountEammsFeedsDetailsEC(String userId, String searchBy, String nwStatus,
			String dateFrom, String dateTo) throws DaoException
	{
		HashMap args = new HashMap();
		args.put("searchBy", searchBy);
		args.put("nwStatus", nwStatus);
		args.put("dateFrom", dateFrom); 
		args.put("dateTo", dateTo);
		args.put("userId", userId);
		
		HashMap map = (HashMap) getEammsFeedsDetailsMap(args);
		String select = (String) map.get(EammsFeedsModule.SQL_SELECT_COUNT);
		String where =  (String) map.get(EammsFeedsModule.SQL_WHERE);
		ArrayList p = (ArrayList) map.get(EammsFeedsModule.SQL_PARAM);
		
		ArrayList param = new ArrayList();
		String sql = select + where;
		param.addAll(p);
		
		Collection result = super.select(sql, HashMap.class, param.toArray(), 0, 1);
		if(result != null && !result.isEmpty())
		{
			HashMap m = (HashMap) result.iterator().next();
			Number total = (Number) m.get("total");
			
			return total.intValue();
		}
		return 0;
	}

	public String getLatestFeedsLogId() throws DaoException
	{
		String sql = " SELECT feedLogId FROM fms_feeds_log ORDER BY feedLogId DESC ";
		
		Collection result = super.select(sql, DefaultDataObject.class, null, 0, 1);
		if(result != null && !result.isEmpty())
		{
			DefaultDataObject obj = (DefaultDataObject) result.iterator().next();
			return (String) obj.getProperty("feedLogId");
		}
		return "";
	}

	public void insertFeedsLog(FeedsLogObject obj) throws DaoException
	{
		String sql =
			" INSERT INTO fms_feeds_log ( " +
			"	feedLogId, adhoc, program, requestId, assignmentId, date, location, station, dateIn, timeIn, " +
			"	dateOut, timeOut, ebNo, assAV1, assAV2, mcr, news, stringer, telco, remarks, status, " +
			"	createdBy, createdDate ) " +
			" VALUES ( " +
			"	#feedLogId#, #adhoc#, #program#, #requestId#, #assignmentId#, #date#, #location#, #station#, #dateIn#, #timeIn#, " +
			"	#dateOut#, #timeOut#, #ebNo#, #assAV1#, #assAV2#, #mcr#, #news#, #stringer#, #telco#, #remarks#, #status#, " +
			"	#createdBy#, #createdDate# ) ";
		
		super.update(sql, obj);
	}

	public void updateFeedsLog(FeedsLogObject obj) throws DaoException
	{
		String sql =
			" UPDATE fms_feeds_log SET adhoc = #adhoc#, program = #program#, requestId = #requestId#, assignmentId = #assignmentId#, date = #date#, " +
			" location = #location#, station = #station#, dateIn = #dateIn#, timeIn = #timeIn#, dateOut = #dateOut#, " +
			" timeOut = #timeOut#, ebNo = #ebNo#, assAV1 = #assAV1#, assAV2 = #assAV2#, mcr = #mcr#, news = #news#, " +
			" stringer = #stringer#, telco = #telco#, remarks = #remarks#, status = #status#, " +
			" modifiedBy = #modifiedBy#, modifiedDate = #modifiedDate# " +
			" WHERE feedLogId = #feedLogId# ";
		
		super.update(sql, obj);
	}

	public Collection getFeedsLog(String feedsLogId) throws DaoException
	{
		ArrayList param = new ArrayList();
		String sql =
			" SELECT feedLogId, adhoc, program, requestId, assignmentId, date, location, station, dateIn, timeIn, " +
			" dateOut, timeOut, ebNo, assAV1, assAV2, mcr, news, stringer, telco, remarks, status, " +
			" createdBy, createdDate, modifiedBy, modifiedDate " +
			" FROM fms_feeds_log " +
			" WHERE 1=? ";
		param.add("1");
		
		if(feedsLogId != null && !feedsLogId.equals(""))
		{
			sql += " AND feedLogId = ? ";
			param.add(feedsLogId);
		}
		
		Collection result = super.select(sql, FeedsLogObject.class, param.toArray(), 0, -1);
		return result;
	}
	
	public Collection selectFeedsLog(String searchBy, String telco, String adhoc, Date dateFrom, Date dateTo, 
			String sort, boolean isDesc, int start, int rows) throws DaoException
	{
		ArrayList param = new ArrayList();
		String sql =
			" SELECT feedLogId AS id, feedLogId, l.date, l.program, p.programName, l.requestId, r.title, l.assignmentId, " +
			" a.code, am.fromTime, am.toTime, l.location, l.station, s.c_name AS stationName, l.dateIn, " +
			" l.dateOut, l.timeIn, l.timeOut, " +
			" l.ebNo, l.assAV1, l.assAV2, l.mcr, (ud.firstname + ' ' + ud.lastname) AS mcrName, l.news, l.stringer, l.telco, " +
			" t.c_name AS telcoName, l.remarks, l.status, l.adhoc, " +
			" (a.code + ' | ' + am.fromTime + ' - ' + am.toTime) AS assign_time, " +
			" (am.fromTime + ' - ' + am.toTime) AS assign_reqTime, " +
			" (CONVERT(VARCHAR, l.dateIn, 101) + ', ' + l.timeIn) AS dateTimeIn, " +
			" (CONVERT(VARCHAR, l.dateOut, 101) + ', ' + l.timeOut) AS dateTimeOut, " +
			" (l.assAV1 + '/' + l.assAV2) AS assAV " +
			" FROM fms_feeds_log l " +
			" LEFT JOIN fms_feed_station s ON (l.station=s.id) " +
			" LEFT JOIN fms_feed_telco t ON (t.id=l.telco) " +
			" LEFT JOIN fms_user_details ud ON (l.mcr=ud.userId) " +
			" LEFT JOIN fms_prog_setup p ON (p.programId=l.program) " +
			" LEFT JOIN fms_eng_request r ON (r.requestId=l.requestId) " +
			" LEFT JOIN fms_eng_assignment a ON (a.requestId = r.requestId AND a.assignmentId = l.assignmentId) " +
			" LEFT JOIN fms_eng_assignment_manpower am ON (a.assignmentId=am.assignmentId) " +
			" WHERE 1=? ";
		param.add("1");

		if(searchBy != null && !searchBy.equals(""))
		{
			sql += " AND (r.title LIKE ? OR l.location LIKE ? OR ud.firstname LIKE ? OR ud.lastname LIKE ?) ";
			searchBy = "%" + searchBy + "%";
			param.add(searchBy);
			param.add(searchBy);
			param.add(searchBy);
			param.add(searchBy);
		}
		
		if(telco != null && !telco.equals("") && !telco.equals("-1"))
		{
			sql += " AND l.telco = ? ";
			param.add(telco);
		}
		
		if(adhoc != null && !adhoc.equals("") && !adhoc.equals("-1"))
		{
			sql += " AND l.adhoc = ? ";
			param.add(adhoc);
		}
		
		if(dateFrom != null && dateTo != null)
		{
			sql += " AND l.date <= ? AND l.date >= ? ";
			param.add(dateFrom);
			param.add(dateTo);
		}
		else
		{
			if(dateFrom != null)
			{
				sql += " AND l.date <= ? ";
				param.add(dateFrom);
			}
			
			if(dateTo != null)
			{
				sql += " AND l.date >= ? ";
				param.add(dateTo);
			}
		}
		
		
		if(sort != null && !sort.equals(""))
		{
			sql += " ORDER BY " + sort;
			if(isDesc)
			{
				sql += " DESC ";
			}
		}
		else
		{
			sql += " ORDER BY l.date DESC "; 
		}
		
		Collection result = super.select(sql, FeedsLogObject.class, param.toArray(), start, rows);
		return result;
	}
	
	public int selectCountFeedsLog(String searchBy, String telco, String adhoc, Date dateFrom, Date dateTo) throws DaoException
	{
		ArrayList param = new ArrayList();
		String sql =
			" SELECT COUNT(feedLogId) AS total " +
			" FROM fms_feeds_log l " +
			" LEFT JOIN fms_feed_station s ON (l.station=s.id) " +
			" LEFT JOIN fms_feed_telco t ON (t.id=l.telco) " +
			" LEFT JOIN fms_user_details ud ON (l.mcr=ud.userId) " +
			" LEFT JOIN fms_prog_setup p ON (p.programId=l.program) " +
			" LEFT JOIN fms_eng_request r ON (r.requestId=l.requestId) " +
			" LEFT JOIN fms_eng_assignment a ON (a.requestId = r.requestId AND a.assignmentId = l.assignmentId) " +
			" LEFT JOIN fms_eng_assignment_manpower am ON (a.assignmentId=am.assignmentId) " +
			" WHERE 1=? ";
		param.add("1");

		if(searchBy != null && !searchBy.equals(""))
		{
			sql += " AND (r.title LIKE ? OR l.location LIKE ? OR ud.firstname LIKE ? OR ud.lastname LIKE ?) ";
			searchBy = "%" + searchBy + "%";
			param.add(searchBy);
			param.add(searchBy);
			param.add(searchBy);
			param.add(searchBy);
		}
		
		if(telco != null && !telco.equals("") && !telco.equals("-1"))
		{
			sql += " AND l.telco = ? ";
			param.add(telco);
		}
		
		if(adhoc != null && !adhoc.equals("") && !adhoc.equals("-1"))
		{
			sql += " AND l.adhoc = ? ";
			param.add(adhoc);
		}
		
		if(dateFrom != null && dateTo != null)
		{
			sql += " AND l.date <= ? AND l.date >= ? ";
			param.add(dateFrom);
			param.add(dateTo);
		}
		else
		{
			if(dateFrom != null)
			{
				sql += " AND l.date <= ? ";
				param.add(dateFrom);
			}
			
			if(dateTo != null)
			{
				sql += " AND l.date >= ? ";
				param.add(dateTo);
			}
		}
		
		Collection result = super.select(sql, HashMap.class, param.toArray(), 0, 1);
		if(result != null && !result.isEmpty())
		{
			HashMap m = (HashMap) result.iterator().next();
			Number total = (Number) m.get("total");
			
			return total.intValue();
		}
		return 0;
	}

	public Collection getUserBelongToGroup(String groupIds) throws DaoException
	{
		ArrayList param = new ArrayList();
		String sql =
			" SELECT ud.userId AS id, (ud.firstname + ' ' + ud.lastname) AS fullname " +
			" FROM fms_user_details ud " +
			" INNER JOIN security_user_group sug ON (ud.userId=sug.userId) " +
			" WHERE 1=? ";
		param.add("1");
		
		if(groupIds != null && !groupIds.equals(""))
		{
			sql += " AND sug.groupId IN ( " + groupIds + " ) ";
		}
			
		Collection result = super.select(sql, DefaultDataObject.class, param.toArray(), 0, -1);
		return result;
	}

	public Collection getFmsRequest(String requestId, String programId, String keyword, 
			String sort, boolean desc, int start, int rows) throws DaoException
	{
		ArrayList param = new ArrayList();
		String sql = 
			" SELECT requestId AS id, requestId, title, description, requiredFrom, requiredTo " +
			" FROM fms_eng_request " +
			" WHERE 1=? ";
		param.add("1");
		
		if(requestId != null && !requestId.equals(""))
		{
			sql += " AND requestId = ? ";
			param.add(requestId);
		}
		
		if(programId != null && !programId.equals(""))
		{
			sql += " AND program = ? ";
			param.add(programId);
		}
		
		if(keyword != null && !keyword.equals(""))
		{
			keyword = "%" + keyword + "%";
			sql += " AND ( title LIKE ? OR requestId LIKE ? ) ";
			param.add(keyword);
			param.add(keyword);
		}
		
		if(sort != null && !sort.equals(""))
		{
			sql += " ORDER BY " + sort;
			if(desc)
			{
				sql += " DESC ";
			}
		}
		else
		{
			sql += " ORDER BY requestId DESC "; 
		}
		
		Collection result = super.select(sql, DefaultDataObject.class, param.toArray(), start, rows);
		return result;
	}

	public int getCountFmsRequest(String programId, String keyword) throws DaoException
	{
		ArrayList param = new ArrayList();
		String sql = 
			" SELECT COUNT(requestId) AS total " +
			" FROM fms_eng_request " +
			" WHERE 1=? ";
		param.add("1");
		
		if(programId != null && !programId.equals(""))
		{
			sql += " AND program = ? ";
			param.add(programId);
		}
		
		if(keyword != null && !keyword.equals(""))
		{
			keyword = "%" + keyword + "%";
			sql += " AND ( title LIKE ? OR requestId LIKE ? ) ";
			param.add(keyword);
			param.add(keyword);
		}
		
		Collection result = super.select(sql, HashMap.class, param.toArray(), 0, 1);
		if(result != null && !result.isEmpty())
		{
			HashMap m = (HashMap) result.iterator().next();
			Number total = (Number) m.get("total");
			
			return total.intValue();
		}
		return 0;
	}

	public Collection getFmsAssignments(String requestId) throws DaoException
	{
		String sql = 
			" SELECT a.assignmentId AS id, a.code, am.fromTime, am.toTime " +
			" FROM fms_eng_assignment a " +
			" INNER JOIN fms_eng_assignment_manpower am ON (a.assignmentId=am.assignmentId) " +
			" INNER JOIN fms_eng_service_tvro t ON (a.serviceId=t.id) " +
			" INNER JOIN fms_eng_request r ON (a.requestId = r.requestId) " +
			" WHERE a.requestId = ? ";
		
		Collection result = super.select(sql, DefaultDataObject.class, new Object[]{requestId}, 0, -1);
		return result;
	}
	
	public Collection getFmsTvroService(String requestId, String tvroServiceId, Date dateFrom, Date dateTo) throws DaoException
	{
		ArrayList param = new ArrayList();
		String sql =
			" SELECT * " +
			" FROM fms_eng_service_tvro " +
			" WHERE 1=? ";
		param.add("1");
		
		if(requestId != null && !requestId.equals(""))
		{
			sql += " AND requestId = ? ";
			param.add(requestId);
		}
		
		if(tvroServiceId != null && !tvroServiceId.equals(""))
		{
			sql += " AND id = ? ";
			param.add(tvroServiceId);
		}
		
		if(dateFrom != null && dateTo != null)
		{
			sql += " AND ( requiredDate <= ? AND requiredDateTo >= ? ) ";
			param.add(dateFrom);
			param.add(dateTo);
		}
		else
		{
			if(dateFrom != null)
			{
				sql += " AND requiredDate <= ? ";
				param.add(dateFrom);
			}
			
			if(dateTo != null)
			{
				sql += " AND requiredDateTo >= ? "; 
				param.add(dateTo);
			}
		}
		
		Collection result = super.select(sql, DefaultDataObject.class, param.toArray(), 0, -1);
		return result;
	}

	public void insertFeedsAssignedEng(DefaultDataObject obj) throws DaoException
	{
		String sql = " INSERT INTO fms_feeds_assigned_eng ( requestId, userId ) VALUES ( #requestId#, #userId# ) ";
		super.update(sql, obj);
	}

	public Collection getFmsServices(String requestId, String serviceTypeId, String serviceId, boolean assignedOnly) throws DaoException
	{
		ArrayList param = new ArrayList();
		String sql =
			" SELECT code, requiredFrom, requiredTo, fromTime, toTime, userId, serviceId, remarks " +
			" FROM fms_eng_assignment a " +
			" INNER JOIN fms_eng_assignment_manpower am ON (a.assignmentId=am.assignmentId) " +
			" WHERE requestId = ? ";
		param.add(requestId);
		
		if(serviceTypeId != null && !serviceTypeId.equals(""))
		{
			sql += " AND serviceType = ? ";
			param.add(serviceTypeId);
			
			if(serviceTypeId.equals(ServiceDetailsForm.SERVICE_MANPOWER))
			{
				String ecRateCardIds = Application.getInstance().getProperty("ec_rate_card_id");
				int invalidRateCard = 0;
				if(ecRateCardIds != null && !ecRateCardIds.equals(""))
				{
					String[] ecRateCardIdsArr = ecRateCardIds.split(",");
					invalidRateCard = verifyFMSRateCardId(ecRateCardIdsArr, requestId);
				}
				else
				{
					invalidRateCard = 2;
				}
				
				if(invalidRateCard != 0)
				{
					if(assignedOnly)
					{
						if(invalidRateCard == 2)
						{
							Log.getLog(getClass()).info("RequestId : " + requestId + " - EC Rate Card ID not set");
						}
						sql += " AND am.rateCardId = 'empty' "; // make query return empty
					}
				}
				else
				{
					sql += " AND am.rateCardId IN ( ?";
					
					String[] ecRateCardIdsArr = ecRateCardIds.split(",");
					param.add(ecRateCardIdsArr[0].trim());
					
					for(int i = 1; i < ecRateCardIdsArr.length; i++)
					{
						sql += ", ? ";
						param.add(ecRateCardIdsArr[i].trim());
					}
					
					sql += " ) ";
				}
			}
		}
		
		if(serviceId != null && !serviceId.equals(""))
		{
			sql += " AND serviceId = ? ";
			param.add(serviceId);
		}
		
		if(assignedOnly)
		{
			sql += " AND userId <> '' ";
		}
		
		Collection result = super.select(sql, DefaultDataObject.class, param.toArray(), 0, -1);
		return result;
	}

	private int verifyFMSRateCardId(String[] ecRateCardIdsArr, String requestId)
	{
		String sql = " SELECT id FROM fms_rate_card WHERE id = ? ";
		boolean invalid = false;
		for(int i = 0; i < ecRateCardIdsArr.length; i++)
		{
			try
			{
				Collection result = super.select(sql, DefaultDataObject.class, new Object[]{ecRateCardIdsArr[i].trim()}, 0, 1);
				if(result == null || result.isEmpty())
				{
					invalid = true;
					Log.getLog(getClass()).info("RequestId : " + requestId + 
							" Rate Card ID : " + ecRateCardIdsArr[i].trim() + " - Invalid EC Rate Card ID");
				}
			}
			catch (DaoException e)
			{
				Log.getLog(getClass()).error(e, e);
			}
		}
		
		if(invalid)
		{
			return 1;
		}
		return 0;
	}

	public Collection isRequestExistInEamms(String requestId) throws DaoException
	{
		String sql = " SELECT requestedDate FROM fms_feeds_details WHERE requestId = ? ORDER BY requestedDate DESC ";
		
		Collection result = super.select(sql, DefaultDataObject.class, new Object[]{requestId}, 0, 1);
		return result;
	}
}
