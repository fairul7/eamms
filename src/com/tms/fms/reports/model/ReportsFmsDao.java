package com.tms.fms.reports.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.text.*;

import com.tms.fms.engineering.model.Assignment;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.engineering.model.Service;
import com.tms.fms.engineering.ui.ServiceDetailsForm;
import com.tms.fms.facility.model.FacilityDao; 

import com.tms.fms.setup.model.ProgramObject;
import com.tms.fms.setup.model.SetupModule;
import com.tms.fms.transport.model.Status;
import com.tms.fms.util.WidgetUtil;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.services.security.SecurityService;
import kacang.util.JdbcUtil;
import kacang.util.Log;

public class ReportsFmsDao extends DataSourceDao{
	
	public void init() throws DaoException{
		//create new report table 
		try{
			super.update("CREATE TABLE fms_eng_resources_report	" +
				 " (id varchar(255) NOT NULL, " +
		         " serviceId char(1) NOT NULL, " +
		         " requestId varchar(255) NOT NULL, " +
		         " facilityId varchar(255) NOT NULL, " +
		         " bookDate datetime NOT NULL,  " +
		         " hour int NOT NULL " +
		         " PRIMARY KEY(id))", null);
		} catch (Exception e) {
		}		
		
		try {
			super.update("ALTER TABLE fms_eng_resources_report ADD minutes int ", null);
		} catch (Exception e) {
		}
		
		// create index for facilityId
		try {
			super.update("CREATE INDEX facilityId ON fms_eng_resources_report(facilityId)", null);
		} catch (Exception e) {
		}
		
		// create index for requestId
		try {
			super.update("CREATE INDEX requestId ON fms_eng_resources_report(requestId)", null);
		} catch (Exception e) {
		}
	}
	
	//to be removed later
	public Collection getTransportRequestListing2(String requestType, Date from, Date to, String programId, String sort, boolean desc, int startIndex,int maxRows) throws DaoException {
		Map mapresult=new HashMap();
		Collection list = new ArrayList();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rsRequest = null;
		int currentRow = 0;
		ArrayList args = new ArrayList();
		String sqlTransport="SELECT tr.id, tr.requestTitle, tr.requestType, tr.program, tr.startDate, tr.endDate, tr.destination, " +
			"tr.purpose, tr.remarks, tr.status, tr.reason, tr.requestBy, tr.requestDate, tr.updatedBy, tr.updatedDate, " +
			"tr.approvedBy, tr.approvedDate, tr.statusRequest, tr.rate, tr.engineeringRequestId, tr.blockBooking, " +
			"su.firstname,su.lastname, d.name as department, o.id as outsourceId, ta.startDate as tranAssignSDate, ta.endDate as tranAssignEDate," +
			"v.driver " +
			"FROM fms_tran_request tr " +
			"left join security_user su on su.id=tr.requestBy " +
			"left join fms_department d on su.department = d.id " +
			"left join fms_tran_request_outsource o on o.requestId=tr.id " +
			"left join fms_tran_assignment ta on ta.requestId=tr.id " +
			"left join fms_tran_request_vehicle v on v.requestId=tr.id " +
			"WHERE 1=1 AND tr.status <> '"+SetupModule.DRAFT_STATUS+"'";
		
		if(from != null && to != null){
			sqlTransport += " AND ((tr.startDate >= ? AND tr.startDate <= ?) OR (tr.startDate <= ? AND tr.endDate >= ?))";
			args.add(from);
			args.add(to);
			args.add(from);
			args.add(from);
		}
		
		if(programId!=null && !programId.equals("")&& !programId.equals("-1")){
			sqlTransport += " AND tr.program=? ";
			args.add(programId);
		}
		
				
		if(requestType!=null  && !requestType.equals("") && !requestType.equals("-1")){
			sqlTransport += " AND ( ";
			String[] value=requestType.split(",");
			int i=0;
			for (String string : value) {
				if(i==0){
					sqlTransport+="tr.requestType ='"+string+"'";
				}else{
					sqlTransport+=" OR tr.requestType ='"+string+"'";
				}
				i++;
				
			}
			sqlTransport+=" )";
			
			
		}
		
		String strSort= "";		  
		    	
		if (sort != null && !sort.equals("")) {		
			if(!sort.equals("driversAssigned") 
					&& !sort.equals("fullname")
					&& !sort.equals("vehicleAssigned")
					&& !sort.equals("meterStart")
					&& !sort.equals("meterEnd")
					&& !sort.equals("checkInDate")
					&& !sort.equals("checkOutDate")
					&& !sort.equals("totalMeter")
					&& !sort.equals("outsourceFlag")
					&& !sort.equals("vehicleStatus")
					&& !sort.equals("driverStatus")){
				
				if(sort.equals("status")){
					sqlTransport += " ORDER BY tr.status ";
					if (desc)
						sqlTransport += " DESC";
				}else{
					sqlTransport += " ORDER BY " + sort;
					if (desc)
						sqlTransport += " DESC";
				}
			}
			else{
				sqlTransport += " ORDER BY tr.id ";
				if (desc)
					sqlTransport += " DESC";
			}
			
		}else 
			sqlTransport += " ORDER BY tr.requestDate DESC";
		
		try {
			con = super.getDataSource().getConnection();
			statement = JdbcUtil.getInstance().createPreparedStatement(con,sqlTransport, args.toArray());
			try {
				 if (maxRows > 0)
	                    statement.setMaxRows(startIndex + maxRows);
				rsRequest = statement.executeQuery();
				while (rsRequest.next()) {
					if (currentRow >= startIndex && (maxRows < 0 || currentRow < (startIndex + maxRows))) {
					ReportsObject obj = new ReportsObject();
					obj.setId(rsRequest.getString("id"));
					obj.setRequestTitle(rsRequest.getString("requestTitle"));
					
					if(rsRequest.getString("requestType")!=null && rsRequest.getString("requestType").equals(EngineeringModule.REQUEST_TYPE_INTERNAL)){
						obj.setRequestType(Application.getInstance().getMessage("fms.facility.requestType.I"));
					}else if(rsRequest.getString("requestType")!=null && rsRequest.getString("requestType").equals(EngineeringModule.REQUEST_TYPE_EXTERNAL)){
						obj.setRequestType(Application.getInstance().getMessage("fms.facility.requestType.E"));
					}else if(rsRequest.getString("requestType")!=null && rsRequest.getString("requestType").equals(EngineeringModule.REQUEST_TYPE_NONPROGRAM)){
						obj.setRequestType(Application.getInstance().getMessage("fms.facility.requestType.N"));
					}
					
					obj.setProgram(rsRequest.getString("program")); 
					obj.setStartDate(rsRequest.getDate("startDate"));
					obj.setEndDate(rsRequest.getDate("endDate"));
					obj.setDestination(rsRequest.getString("destination"));
					obj.setPurpose(rsRequest.getString("purpose"));
					obj.setRemarks(rsRequest.getString("remarks"));
					obj.setStatus(rsRequest.getString("status")); 
					obj.setTransStatus(rsRequest.getString("status")+"/"+rsRequest.getString("id")); 
					obj.setReason(rsRequest.getString("reason")); 
					obj.setRequestBy(rsRequest.getString("requestBy"));
					obj.setOutsourceFlag(rsRequest.getString("outsourceId")!=null && !rsRequest.getString("outsourceId").equals("") && !rsRequest.getString("outsourceId").equals("NULL")?"Yes":"-");
					try {
						DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
						SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
						
						Date dateFrom = (Date) formatter.parse(rsRequest.getString("startDate"));
					    Date dateTo = (Date) formatter.parse(rsRequest.getString("endDate"));
						
						String dateFromStr=sdf.format(dateFrom);
						String dateToStr=sdf.format(dateTo);

						obj.setRequestDateStr("<table border=\"0\"><tr><td>"+dateFromStr + "</td></tr><tr><td align=\"center\">-</td></tr><tr><td> " + dateToStr+"</td></tr></table>");
												
						Date dateFromAssignment = (Date) formatter.parse(rsRequest.getString("tranAssignSDate")!=null?rsRequest.getString("tranAssignSDate"):"");
					    Date dateToAssignment = (Date) formatter.parse(rsRequest.getString("tranAssignEDate")!=null?rsRequest.getString("tranAssignEDate"):"");
					    String dateFromStrAssignment=sdf.format(dateFromAssignment);
						String dateToStrAssignment=sdf.format(dateToAssignment);
					    
					    obj.setTransAssignmentDateStr("<table border=\"0\"><tr><td>"+dateFromStrAssignment + "</td></tr><tr><td align=\"center\">-</td></tr><tr><td> " + dateToStrAssignment+"</td></tr></table>");
					    
					} catch (ParseException e) {
						Log.getLog(getClass()).error(e);
					}
					
					
					obj.setUpdatedBy(rsRequest.getString("updatedBy"));
					obj.setUpdatedDate(rsRequest.getDate("updatedDate")); 
					obj.setApprovedBy(rsRequest.getString("approvedBy"));
					obj.setStatusRequest(rsRequest.getString("statusRequest"));
					obj.setRate(rsRequest.getString("rate")); 
					obj.setEngineeringRequestId(rsRequest.getString("engineeringRequestId"));
					obj.setBlockBooking(rsRequest.getString("blockBooking"));
					obj.setFullname(rsRequest.getString("firstname")+ " " +rsRequest.getString("lastname"));
					obj.setDepartment(rsRequest.getString("department"));
					
					Collection manpowers=getTransportDrivers(obj.getId());
					String drivers="";
					boolean driverRequired=false;
					
					if(rsRequest.getString("driver")!=null && !rsRequest.getString("driver").equals("0")){
						driverRequired=true;
					}
					
					String driverstatus="";
					boolean driverassigned=false;
					if(manpowers!=null && manpowers.size()>0){
						for (Iterator iterator = manpowers.iterator(); iterator.hasNext();) {
							Map map = (Map) iterator.next();
							Application app = Application.getInstance();
		    				SecurityService service = (SecurityService) app.getService(SecurityService.class);		
		    							
		    				String username = "";
		    				try{						
		    					username = service.getUser(map.get("manpowerId").toString()).getName();		
		    					
		    					
		    				}catch(Exception e){
		    					
		    				}		    		
		    				drivers+= username;
		    				
		    				if(manpowers.size()>1){
		    					drivers+=",<br>";
		    				}
		    				driverstatus = map.get("status")!=null && !map.get("status").equals("")?map.get("status").toString():"1";
						}
						
						driverRequired=true;
						obj.setDriverStatus(driverstatus);
					}else{
						//driverRequired=false;
						
						obj.setDriverStatus("0");
					}
					
					
					String vehicleAssigned="";
					String meterStart="";
					String meterEnd="";
					String totalmeter="";
					String checkInDate="";
					String checkOutDate="";
					String timeRec="";
					boolean vehicleRequired=false;
					String vehicleStatus="";
					Collection vehicleDetails = getVehicle(obj.getId());
	        		if(vehicleDetails!=null && vehicleDetails.size()>0){
	        			for (Iterator iterator = vehicleDetails.iterator(); iterator.hasNext();) {
							Map map = (Map) iterator.next();		    		
							
							if(map.get("vehicle_num")!=null && !map.get("vehicle_num").toString().equals("")){
								vehicleAssigned+= map.get("vehicle_num").toString();
								if(vehicleDetails.size()>1){
									vehicleAssigned+=",<br>";
								}
							}
							
		    				if(map.get("meterStart")!=null && !map.get("meterStart").toString().equals("")){
		    					meterStart+= map.get("meterStart").toString();
		    					if(vehicleDetails.size()>1){
		    						meterStart+=",<br>";
								}
		    				}
		    				
		    				if(map.get("meterEnd")!=null && !map.get("meterEnd").toString().equals("")){
		    					meterEnd+= map.get("meterEnd").toString();
		    					if(vehicleDetails.size()>1){
		    						meterEnd+=",<br>";
								}
		    				}
		    				if((map.get("meterStart")!=null && !map.get("meterStart").toString().equals("")) 
		    						|| (map.get("meterEnd")!=null && !map.get("meterEnd").toString().equals(""))){
		    					int total=Integer.parseInt(map.get("meterStart").toString()) + Integer.parseInt(map.get("meterEnd").toString());
								totalmeter+= String.valueOf(total);
								if(vehicleDetails.size()>1){
									totalmeter+=",<br>";
								}
		    				}
		    				
		    				if (map.get("checkin_date") != null && !map.get("checkin_date").toString().equals("")) {

								try {
									DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
									Date date = (Date) formatter.parse(map.get("checkin_date").toString());
									
									//SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
									SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
									String dateStr=sdf.format(date);
									
									checkInDate += dateStr;
									if (vehicleDetails.size() > 1) {
										checkInDate += ",<br>";
									}

								} catch (ParseException e) {
									Log.getLog(getClass()).error(e);
								}
							}
		    				
		    				if(map.get("checkout_date")!=null && !map.get("checkout_date").toString().equals("")){
		    					try {
									DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
									Date date = (Date) formatter.parse(map.get("checkout_date").toString());
									
									//SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
									SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
									String dateStr=sdf.format(date);
									
									checkOutDate += dateStr;
									if (vehicleDetails.size() > 1) {
										checkOutDate += ",<br>";
									}

								} catch (ParseException e) {
									Log.getLog(getClass()).error(e);
								}
								
		    					
		    				}
		    				
		    				if(map.get("timeRec")!=null && !map.get("timeRec").toString().equals("")){
		    					timeRec=map.get("timeRec").toString();
		    					if (vehicleDetails.size() > 1) {
		    						timeRec+=",<br>";
		    					}
		    				}
		    				vehicleStatus = map.get("status")!=null && !map.get("status").equals("")?map.get("status").toString():"1";
		    				//System.out.println("vehicleStatus   ::    >> "+vehicleStatus);
						}
	        		    vehicleRequired=true;
	        			obj.setVehicleStatus(vehicleStatus);
	        			
	        			
	        		}else{
	        			obj.setVehicleStatus("0");
	        		}
	        		
	        		
	        		vehicleAssigned+="";
	        		obj.setVehicleAssigned(vehicleAssigned);
	        		obj.setMeterStart(meterStart);
	        		obj.setMeterEnd(meterEnd);
	        		obj.setTotalMeter(totalmeter);
	        		obj.setCheckInDate(checkInDate);
	        		obj.setCheckOutDate(checkOutDate);
	        		obj.setTimeRec(timeRec);
	        		obj.setDriversAssigned("-");
					
	        		if(!drivers.equals("")){
						obj.setDriversAssigned(drivers);	
					}else{
						if(assignedVehicle(obj.getId()) && rsRequest.getString("driver").equals("0")){
							obj.setDriversAssigned(Application.getInstance().getMessage("fms.report.status.pandusendiri","Pandu Sendiri"));
						}
						
					}
					if(rsRequest.getString("outsourceId")!=null && !rsRequest.getString("outsourceId").equals("")){
						obj.setDriversAssigned(Application.getInstance().getMessage("fms.report.status.sewa","Sewa"));
					}
					
	        		
					list.add(obj);
					}
					currentRow++;
		
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
	
	
	public int countTransportRequestListing(String requestType, Date from, Date to, String programId) throws DaoException{
		ArrayList args = new ArrayList();
		int result=0;
		String sqlTransport="SELECT COUNT(tr.id) as total " +
		"FROM fms_tran_request tr " +
		"left join security_user su on su.id=tr.requestBy " +
		"left join fms_department d on su.department = d.id " +
		"left join fms_tran_request_outsource o on o.requestId=tr.id " +
		"left join fms_tran_assignment ta on ta.requestId=tr.id " +
		"left join fms_tran_request_vehicle v on v.requestId=tr.id " +
		"WHERE 1=1 AND tr.status <> '"+SetupModule.DRAFT_STATUS+"'";
	
	if(from != null && to != null){
		sqlTransport += " AND ((tr.startDate >= ? AND tr.startDate <= ?) OR (tr.startDate <= ? AND tr.endDate >= ?))";
		args.add(from);
		args.add(to);
		args.add(from);
		args.add(from);
	}
	
	if(programId!=null && !programId.equals("")&& !programId.equals("-1")){
		sqlTransport += " AND tr.program=? ";
		args.add(programId);
	}

	if(requestType!=null  && !requestType.equals("")){
		if(requestType.equals("program")){
			sqlTransport += " AND (tr.program <> '-')";
		}else if(requestType.equals("nonprogram")){
			sqlTransport += " AND (tr.program = '-')";
		}else if(requestType.equals("all")){
			sqlTransport += " AND (tr.program <> '-' OR tr.program = '-')";
		}
		
	}
		
		Collection list =  super.select(sqlTransport, HashMap.class, args.toArray(), 0, -1);
		if(list!=null && list.size()>0){
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map map = (Map) iterator.next();
				
				result = Integer.parseInt(map.get("total").toString());
			}
		}
		return result;
	}
	
	
	public Collection getTransportDrivers(String transportRequestId) throws DaoException{
		String sql="select distinct manpowerId,status from fms_tran_request_driver where assignmentId=?";
		return super.select(sql, HashMap.class, new String[]{transportRequestId}, 0, -1);
		
	}
	
	public Collection getVehicle(String assignmentId) throws DaoException{
		String sql="SELECT distinct ad.vehicle_num, a.id, ad.checkin_date, ad.checkout_date, ad.meterStart as meterStart, ad.meterEnd as meterEnd," +
				"DATEDIFF(hour, ad.checkout_date, ad.checkin_date) AS timeRec, ad.status " +
				"FROM fms_tran_assignment a " +
				//"INNER JOIN fms_tran_request_assignment ra ON ra.id=a.id " +
				"left join fms_tran_request_assignment_details ad on ad.assgId=a.id " +
				"WHERE (a.id = ?)";
		return super.select(sql, HashMap.class, new String[]{assignmentId}, 0, -1);
		
	}
	
	public Map getSingleVehicle(String assignmentId, String vehicleNum) throws DaoException{
		Map map = null;
		String sql="SELECT distinct ad.vehicle_num, a.id, ad.checkin_date, ad.checkout_date, ad.meterStart as meterStart, ad.meterEnd as meterEnd," +
				"DATEDIFF(hour, ad.checkout_date, ad.checkin_date) AS timeRec, ad.status " +
				"FROM fms_tran_assignment a " +
				//"INNER JOIN fms_tran_request_assignment ra ON ra.id=a.id " +
				"left join fms_tran_request_assignment_details ad on ad.assgId=a.id " +
				"WHERE (a.id = ? and ad.vehicle_num= ? )";
		Collection vehicleDetails = super.select(sql, HashMap.class, new String[]{assignmentId, vehicleNum}, 0, -1);
		
		if(vehicleDetails!=null && vehicleDetails.size()>0){
			for (Iterator iterator = vehicleDetails.iterator(); iterator.hasNext();) {
				map =(Map) iterator.next();		    		
			}
			
		}
		return map;
	}
	
	 public Collection selectProgram() throws DaoException {
			return super.select("SELECT programId, programName,description,pfeCode,startProductionDate,endProductionDate, " +
					"department, status, createdBy, createdDate, updatedBy, updatedDate, producer, " +
					"engManpowerBudget, facilitiesBudget, vtrBudget, transportBudget from fms_prog_setup where status='1' order by programName", ProgramObject.class, null, 0, -1);
	}
	 
	
	public Collection getRequestRepListing(Date bookingFrom, Date bookingTo, String programId, String requestType, String status, String sort, boolean desc, int startIndex,int maxRows)throws DaoException{
		ArrayList args = new ArrayList();
		String sql="SELECT DISTINCT r.requestId, r.title, r.requestType, r.status, " +
				"r.programType, prog.programName as program, " +
				"r.createdBy, dept.name as department, r.requiredFrom as bookFrom,r.requiredTo as bookTo, " +
				"(r.requestId+'/'+r.requestType) as request " +
				"FROM fms_eng_request r " +
				"inner join fms_eng_assignment fea on fea.requestId= r.requestId " +
				"left join fms_prog_setup prog on prog.programId=r.program " +
				"left join fms_department dept on dept.id=prog.department WHERE NOT (r.status='M' OR r.status='D' OR r.status='H' OR r.status='P') AND 1=1 ";
		
		if(bookingFrom!=null  && bookingFrom!=null){
			sql += " AND ((r.requiredFrom >= ? AND r.requiredFrom <= ?) OR (r.requiredFrom <= ? AND r.requiredTo >= ?))";
			args.add(bookingFrom);
			args.add(bookingTo);
			args.add(bookingFrom);
			args.add(bookingFrom);
		}
		
		if(programId!=null  && !programId.equals("") && !programId.equals("-1")){
			sql += " AND (r.program=?)";
			args.add(programId);
		}
		
		/*if(requestType!=null  && !requestType.equals("") && !requestType.equals("-1")){
			sql += " AND (r.requestType=?)";
			args.add(requestType);
		}*/
		
		if(requestType!=null  && !requestType.equals("") && !requestType.equals("-1")){
			sql += " AND ( ";
			String[] value=requestType.split(",");
			int i=0;
			for (String string : value) {
				if(i==0){
					sql+="r.requestType ='"+string+"'";
				}else{
					sql+=" OR r.requestType ='"+string+"'";
				}
				i++;
				
			}
			sql+=" )";
			
			
		}
		
		if(status!=null  && !status.equals("") && !status.equals("-1")){
			sql += " AND (r.status=?)";
			args.add(status);
		}
		
		String strSort= "";		  
    	
		if (sort != null && !sort.equals("")) {	
			if(sort.equals("createdBy")){
				sql += " ORDER BY r.createdBy ";
				if (desc)
					sql += " DESC";
			}else{
				sql += " ORDER BY " + sort;
				if (desc)
					sql += " DESC";
			}
			
				
			
		}//else 
			//sql += " ORDER BY r.requestId DESC";
		
		return super.select(sql, HashMap.class, args.toArray(), startIndex, maxRows);
		
	}
	
	public int countRequestRepListing(Date bookingFrom, Date bookingTo, String programId, String requestType, String status)throws DaoException{
		ArrayList args = new ArrayList();
		/*String sql="SELECT count(DISTINCT r.requestId) as total FROM fms_eng_request r " +
				"left join fms_prog_setup prog on prog.programId=r.program " +
				"left join fms_department dept on dept.id=prog.department " +
				"left join fms_facility_booking fac on fac.requestId=r.requestId where 1=1 ";*/
		
		String sql="SELECT COUNT(DISTINCT r.requestId) as total FROM fms_eng_request r " +
				"inner join fms_eng_assignment fea on fea.requestId= r.requestId " +
				"left join fms_prog_setup prog on prog.programId=r.program " +
				"left join fms_department dept on dept.id=prog.department WHERE NOT (r.status='M' OR r.status='D' OR r.status='H' OR r.status='P') AND 1=1 ";
		
		if(bookingFrom!=null  && bookingFrom!=null){
			sql += " AND ((r.requiredFrom >= ? AND r.requiredFrom <= ?) OR (r.requiredFrom <= ? AND r.requiredTo >= ?))";
			args.add(bookingFrom);
			args.add(bookingTo);
			args.add(bookingFrom);
			args.add(bookingFrom);
		}
		
		if(programId!=null  && !programId.equals("") && !programId.equals("-1")){
			sql += " AND (r.program=?)";
			args.add(programId);
		}
		
		if(requestType!=null  && !requestType.equals("") && !requestType.equals("-1")){
			sql += " AND ( ";
			String[] value=requestType.split(",");
			int i=0;
			for (String string : value) {
				if(i==0){
					sql+="r.requestType ='"+string+"'";
				}else{
					sql+=" OR r.requestType ='"+string+"'";
				}
				i++;
				
			}
			sql+=" )";
			
			
		}
		
		if(status!=null  && !status.equals("") && !status.equals("-1")){
			sql += " AND (r.status=?)";
			args.add(status);
		}
		
		String strSort= "";		  
		
		Collection list= super.select(sql, HashMap.class, args.toArray(), 0, -1);
		int total=0;
		if(list!=null && list.size()>0){
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map map = (Map) iterator.next();
				total=Integer.parseInt(map.get("total").toString());
			}
		}
		return total;
	}
	
	public Collection getRequestServices(String requestId) throws DaoException{
		String sql="select serviceId from fms_eng_request_services where requestId=?";
		return super.select(sql, HashMap.class, new String[]{requestId}, 0, -1);
		
	}
	
	public Double getTotalManpower(String requestId, String requestType) throws DaoException{
		Collection  getServices=getRequestServices(requestId);
		String totalManpower="";
		Double totalall=0.00;
		if(getServices!=null && getServices.size()>0){
			for (Iterator iterator = getServices.iterator(); iterator.hasNext();) {
				Map map = (Map) iterator.next();
				if(map.get("serviceId").equals(ServiceDetailsForm.SERVICE_MANPOWER)){
					String sql="";
					if(requestType.equals(EngineeringModule.REQUEST_TYPE_EXTERNAL)){
						sql="SELECT externalRate AS totalRate, " +
								"DATEDIFF(day, requiredFrom, requiredTo) AS duration,requiredFrom AS startDate, " +
								"requiredTo AS endDate, quantity " +
								"FROM fms_eng_service_manpower  " +
								"where requestId = ?";
					}else {
						sql="SELECT internalRate AS totalRate, " +
							"DATEDIFF(day, requiredFrom, requiredTo) AS duration,requiredFrom AS startDate, " +
							"requiredTo AS endDate, quantity " +
							"FROM fms_eng_service_manpower  " +
							"where requestId = ?";
					}
					Collection listing=super.select(sql, HashMap.class, new String[]{requestId}, 0, -1);
					
					if(listing!=null && listing.size()>0){
						
						for (Iterator iterator2 = listing.iterator(); iterator2.hasNext();) {
							Map map2 = (Map) iterator2.next();
							Double totalrate=Double.valueOf(map2.get("totalRate").toString());
							int quantity=Integer.parseInt(map2.get("quantity").toString());
							int duration=0;
							if(map2.get("duration").toString().equals("0")){
								duration=1;
							}else{
								// 22hb to 24hb is calculated as 3 days
								duration=Integer.parseInt(map2.get("duration").toString())+1;
							}
							totalall+=totalrate*quantity*duration;
						}
					}
					totalManpower = String.valueOf(totalall);
				}
				
			}
		}
		return totalall;
	}
	
	public Double getTotalFacility(String requestId, String requestType) throws DaoException{
		Collection  getServices=getRequestServices(requestId);
		String totalFacility="";
		Double totalall=0.00;
		if(getServices!=null && getServices.size()>0){
			for (Iterator iterator = getServices.iterator(); iterator.hasNext();) {
				Map map = (Map) iterator.next();
				if(!map.get("serviceId").equals(ServiceDetailsForm.SERVICE_MANPOWER)){
					String sql="SELECT ";
					if(map.get("serviceId").equals(ServiceDetailsForm.SERVICE_SCPMCP)){
						if(requestType.equals(EngineeringModule.REQUEST_TYPE_EXTERNAL)){
							sql+="externalRate ";
						}else {
							sql+="internalRate ";
						}
						sql+=" AS totalRate, id,  DATEDIFF(day, requiredFrom, requiredTo) AS duration, requiredFrom AS startDate, " +
							"requiredTo AS endDate " +
							"FROM fms_eng_service_scp  " +
							"where requestId = ?";
					}else if(map.get("serviceId").equals(ServiceDetailsForm.SERVICE_POSTPRODUCTION)){
						if(requestType.equals(EngineeringModule.REQUEST_TYPE_EXTERNAL)){
							sql+="externalRate ";
						}else {
							sql+="internalRate ";
						}
						sql+=" AS totalRate, id,  DATEDIFF(day, requiredDate, requiredDateTo) AS duration, fromTime AS startDate, " +
						"toTime AS endDate FROM fms_eng_service_postproduction  " +
						"where requestId = ?";
					}else if(map.get("serviceId").equals(ServiceDetailsForm.SERVICE_STUDIO)){
						if(requestType.equals(EngineeringModule.REQUEST_TYPE_EXTERNAL)){
							sql+="externalRate ";
						}else{
							sql+="internalRate ";
						}
						sql+=" AS totalRate, id,  DATEDIFF(day, bookingDate, bookingDateTo) AS duration, requiredFrom AS startDate, " +
						"requiredTo AS endDate " +
						"FROM fms_eng_service_studio  " +
						"where requestId = ?";
					}else if(map.get("serviceId").equals(ServiceDetailsForm.SERVICE_VTR)){
						if(requestType.equals(EngineeringModule.REQUEST_TYPE_EXTERNAL)){
							sql+="externalRate ";
						}else{
							sql+="internalRate ";
						}
						/* remark cuz wrong date calculation
						 * 
						sql+=" AS totalRate, DATEDIFF(day, requiredFrom, requiredTo) AS duration, requiredFrom AS startDate, " +
						"requiredTo AS endDate " +
						"FROM fms_eng_service_vtr  " +
						"where requestId = ?";*/
						
						sql+=" AS totalRate, id,  DATEDIFF(day, requiredDate, requiredDateTo) AS duration, requiredDate AS startDate, " +
						"requiredDateTo AS endDate " +
						"FROM fms_eng_service_vtr  " +
						"where requestId = ?";
					}else if(map.get("serviceId").equals(ServiceDetailsForm.SERVICE_TVRO)){
						if(requestType.equals(EngineeringModule.REQUEST_TYPE_EXTERNAL)){
							sql+="externalRate ";
						}else{
							sql+="internalRate ";
						}
						sql+=" AS totalRate,id, DATEDIFF(day, requiredDate, requiredDateTo) AS duration,fromTime AS startDate, " +
						"toTime AS endDate " +
						"FROM fms_eng_service_tvro  " +
						"where requestId = ?";
					}
					else if(map.get("serviceId").equals(ServiceDetailsForm.SERVICE_OTHER)){
						if(requestType.equals(EngineeringModule.REQUEST_TYPE_EXTERNAL)){
							sql+="externalRate ";
						}else{
							sql+="internalRate ";
						}
						sql+=" AS totalRate, id, DATEDIFF(day, requiredFrom, requiredTo) AS duration, fromTime AS startDate, " +
						"toTime AS endDate, quantity " +
						"FROM fms_eng_service_other  " +
						"where requestId = ?";
					}
					
					
					Collection listing=super.select(sql, HashMap.class, new String[]{requestId}, 0, -1);
					
					if(listing!=null && listing.size()>0){
						
						for (Iterator iterator2 = listing.iterator(); iterator2.hasNext();) {
							Map map2 = (Map) iterator2.next();
							Double totalrate=Double.valueOf(map2.get("totalRate").toString());
							int duration=Integer.parseInt(map2.get("duration").toString())+1;
							//int duration=0;
							String servicesId = (String) map2.get("id");
							if(map.get("serviceId").equals(ServiceDetailsForm.SERVICE_OTHER)){
								int quantity=Integer.parseInt(map2.get("quantity").toString());
								if(duration==0)
									duration=1;	
								
								totalall+=totalrate*quantity*duration;  
							}else{
								/*if(duration!=0){
									totalall+=totalrate*duration;
								}else{
									totalall+=totalrate;
								}*/
								String sqlFacility = 
									"SELECT * " +
									"FROM fms_eng_assignment " +
									"WHERE requestId = ? " +
									"AND serviceType = '" + map.get("serviceId") +"' " +
									"AND serviceId = '"+servicesId+"'";
							Collection col2 = super.select(sqlFacility, HashMap.class, new String[] {requestId}, 0, -1);
							if (col2!=null && col2.size()>0){
								if(((Integer) map2.get("duration"))==0){
									duration=1;
								}else{
									duration=Integer.parseInt(map2.get("duration").toString())+1;
								}
								totalall+=totalrate*duration;	
							}
							
							}	
						}
					}
					totalFacility = String.valueOf(totalall);
				}
				
			}
		}
		return totalall;
	}
	// Created for select total cancelation charges
	public String selectCancelCharge(String reqId){
		try {
			String sql =
				"SELECT cancellationCharges " +
				"FROM fms_eng_request " +
				"WHERE requestId = ?";
			
			Collection col = super.select(sql, EngineeringRequest.class, new String[] {reqId}, 0, 1);
			if (col != null && col.size()==1){
				EngineeringRequest req = (EngineeringRequest) col.iterator().next();
				String charge = req.getCancellationCharges();
				return charge;
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		return null;
	}

	public Collection getResourceUtil(String programType, Date fromDate, Date toDate, String programId, String serviceType, String status, 
			String requestStatus, String sort, boolean desc, int startIndex,int maxRows)throws DaoException{
		
		ArrayList args = new ArrayList();
		String sqlfacility="select distinct a.code as assignmentId, r.requestId, r.title, prog.programName as program, " +
				"e.requiredFrom, e.requiredTo, e.fromTime, e.toTime, a.serviceType, c.name as facilityEquip, " +
				"DATEDIFF(day, e.requiredFrom, e.requiredTo) as duration, e.rateCardId " +
				"FROM fms_eng_assignment a " +
				"INNER JOIN fms_eng_request r on a.requestId=r.requestId " +
				"LEFT JOIN fms_prog_setup prog on prog.programId=r.program " +
				"LEFT JOIN fms_eng_assignment_equipment e on e.assignmentId=a.assignmentId " +
				"LEFT JOIN fms_rate_card c on c.id=e.rateCardId " +
				"LEFT JOIN fms_rate_card_equipment ce on ce.rateCardId=c.id WHERE 1=1 ";
		String sqlManpower="select distinct a.code as assignmentId, r.requestId, r.title, prog.programName as program, " +
				"e.requiredFrom, e.requiredTo, e.fromTime, e.toTime, a.serviceType, c.name as facilityEquip, " +
				"DATEDIFF(day, e.requiredFrom, e.requiredTo) as duration, e.rateCardId " +
				"FROM fms_eng_assignment a " +
				"INNER JOIN fms_eng_request r on a.requestId=r.requestId " +
				"LEFT JOIN fms_prog_setup prog on prog.programId=r.program " +
				"LEFT JOIN fms_eng_assignment_manpower e on e.assignmentId=a.assignmentId " +
				"LEFT JOIN fms_rate_card c on c.id=e.rateCardId " +
				"LEFT JOIN fms_rate_card_manpower ce on ce.rateCardId=c.id WHERE 1=1 ";
		
		String sqlCanceled = 
			"SELECT distinct a.code as assignmentId, r.requestId, r.title, prog.programName as program, " +
			"a.requiredFrom, a.requiredTo, a.fromTime, a.toTime, a.serviceType, c.name as facilityEquip, " +
			"DATEDIFF(day, a.requiredFrom, a.requiredTo) as duration, a.rateCardId " +
			"from fms_eng_cancel_service_log a " +
			"inner JOIN fms_eng_request r on a.requestId=r.requestId " +
			"LEFT JOIN fms_prog_setup prog on prog.programId=r.program " +
			"LEFT JOIN fms_rate_card c on c.id=a.rateCardId " +
			"LEFT JOIN fms_rate_card_equipment ce on ce.rateCardId=c.id " +
			"WHERE 1=1 ";
		
		if(programType!=null && !programType.equals("")){
			if(programType.equals("nonprogram")){
				sqlfacility  += " AND r.requestType = 'N' ";
				sqlManpower += " AND r.requestType = 'N' ";
				sqlCanceled += " AND r.requestType = 'N' ";
			}else if(programType.equals("program")){
				sqlfacility  += " AND (r.requestType = 'I' OR r.requestType = 'E') ";
				sqlManpower += " AND (r.requestType = 'I' OR r.requestType = 'E') ";
				sqlCanceled += " AND (r.requestType = 'I' OR r.requestType = 'E') ";
			}
		}
		
		if(fromDate!=null  && toDate!=null){
			sqlfacility += " AND ((e.requiredFrom >= ? AND e.requiredFrom <= ?) OR (e.requiredFrom <= ? AND e.requiredTo >= ?))";
			args.add(fromDate);
			args.add(toDate);
			args.add(fromDate);
			args.add(fromDate);
			
			sqlManpower += " AND ((e.requiredFrom >= ? AND e.requiredFrom <= ?) OR (e.requiredFrom <= ? AND e.requiredTo >= ?))";
			args.add(fromDate);
			args.add(toDate);
			args.add(fromDate);
			args.add(fromDate);
			
			sqlCanceled += " AND ((a.requiredFrom >= ? AND a.requiredFrom <= ?) OR (a.requiredFrom <= ? AND a.requiredTo >= ?))";
			args.add(fromDate);
			args.add(toDate);
			args.add(fromDate);
			args.add(fromDate);
		}
		
		if(programId!=null  && !programId.equals("") && !programId.equals("-1")){
			sqlfacility += " AND (r.program='"+programId+"')";
			
			sqlManpower += " AND (r.program='"+programId+"')";
			
			sqlCanceled += " AND (r.program='"+programId+"')";
		}
		
		if(serviceType!=null  && !serviceType.equals("")){
			sqlfacility += " AND a.serviceType IN( ";
			sqlManpower += " AND a.serviceType IN( ";
			sqlCanceled += " AND a.serviceType IN( ";
			String[] value=serviceType.split("#");
			int i=0;
			for (String string : value) {
				if(i==0){
					sqlfacility+=string;
					sqlManpower+=string;
					sqlCanceled+=string;
				}else{
					sqlfacility+=","+string;
					sqlManpower+=","+string;
					sqlCanceled+=","+string;
				}
				i++;
				
			}
			sqlfacility+=" )";
			sqlManpower+=" )";
			sqlCanceled+=" )";
		}
		
		if(status!=null  && !status.equals("") && !status.equals("-1")){
			if (!status.equals(EngineeringModule.CANCELLED_STATUS)){
				if (status.equals("F")){
					status = "C";
				}
				sqlfacility += " AND (e.status='"+status+"')";
			
				sqlManpower += " AND (e.status='"+status+"')";
				
				sqlCanceled += " AND (a.status='MZ')";
			}else{
				sqlfacility += " AND (e.status='X')";
				
				sqlManpower += " AND (e.status='X')";
			}
		}
		
		if(requestStatus != null && !requestStatus.equals("-1") && !requestStatus.equals("")){
			sqlfacility += " AND r.status = '" + requestStatus + "' ";
			sqlManpower += " AND r.status = '" + requestStatus + "' ";
			sqlCanceled += " AND r.status = '" + requestStatus + "' ";
		}
		
		String strSort= "";		  
    	
		if (sort != null && !sort.equals("")) {
			if(!sort.equals("blockBooking")&& !sort.equals("totalDuration")){
				
				if(sort.equals("requestId")){
					sqlCanceled += " ORDER BY r.requestId";
					if (desc)
						sqlCanceled += " DESC";
				}else if(sort.equals("requiredFrom")){
					sqlCanceled += " ORDER BY e.requiredFrom";
					if (desc)
						sqlCanceled += " DESC";
				}else if(sort.equals("requiredTo")){
					sqlCanceled += " ORDER BY e.requiredTo";
					if (desc)
						sqlCanceled += " DESC";
				}else{
					sqlCanceled += " ORDER BY " + sort;
					if (desc)
						sqlCanceled += " DESC";
					
				}
				
			}else{
				sqlCanceled += " ORDER BY r.requestId";
			}
		}else{
			sqlCanceled += " ORDER BY r.requestId";
		}
		
		String sql=sqlfacility + " UNION " + sqlManpower + " UNION " + sqlCanceled;
		return super.select(sql, ReportsObject.class, args.toArray(), startIndex, maxRows);
		
	}
	
	public int countResourceUtil(String programType, Date fromDate, Date toDate, String programId, String serviceType, String status, 
			String requestStatus) throws DaoException{
		int total=0;
		
		ArrayList args = new ArrayList();
		ArrayList args2 = new ArrayList();
		ArrayList args3 = new ArrayList();
		
		String sqlfacility="select count(distinct a.code) as total from fms_eng_assignment a " +
				"inner join fms_eng_request r on a.requestId=r.requestId " +
				"LEFT join fms_prog_setup prog on prog.programId=r.program " +
				"LEFT join fms_eng_assignment_equipment e on e.assignmentId=a.assignmentId " +
				"LEFT JOIN fms_rate_card c on c.id=e.rateCardId " +
				"left join fms_rate_card_equipment ce on ce.rateCardId=c.id where 1=1 ";
		String sqlManpower="select count(distinct a.code) as total from fms_eng_assignment a " +
				"inner join fms_eng_request r on a.requestId=r.requestId " +
				"LEFT join fms_prog_setup prog on prog.programId=r.program " +
				"LEFT join fms_eng_assignment_manpower e on e.assignmentId=a.assignmentId " +
				"LEFT JOIN fms_rate_card c on c.id=e.rateCardId " +
				"left join fms_rate_card_manpower ce on ce.rateCardId=c.id WHERE 1=1 ";
		
		String sqlCanceled = 
			"SELECT count(distinct a.code) as total " +
			"from fms_eng_cancel_service_log a " +
			"inner JOIN fms_eng_request r on a.requestId=r.requestId " +
			"LEFT JOIN fms_prog_setup prog on prog.programId=r.program " +
			"LEFT JOIN fms_rate_card c on c.id=a.rateCardId " +
			"LEFT JOIN fms_rate_card_equipment ce on ce.rateCardId=c.id " +
			"WHERE 1=1 ";
		
		if(programType!=null && !programType.equals("")){
			if(programType.equals("nonprogram")){
				sqlfacility  += " AND r.requestType = 'N' ";
				sqlManpower += " AND r.requestType = 'N' ";
				sqlCanceled += " AND r.requestType = 'N' ";
			}else if(programType.equals("program")){
				sqlfacility  += " AND (r.requestType = 'I' OR r.requestType = 'E') ";
				sqlManpower += " AND (r.requestType = 'I' OR r.requestType = 'E') ";
				sqlCanceled += " AND (r.requestType = 'I' OR r.requestType = 'E') ";
			}
		}
		
		if(fromDate!=null  && toDate!=null){
			sqlfacility += " AND ((e.requiredFrom >= ? AND e.requiredFrom <= ?) OR (e.requiredFrom <= ? AND e.requiredTo >= ?))";
			args.add(fromDate);
			args.add(toDate);
			args.add(fromDate);
			args.add(fromDate);
			
			sqlManpower += " AND ((e.requiredFrom >= ? AND e.requiredFrom <= ?) OR (e.requiredFrom <= ? AND e.requiredTo >= ?))";
			args2.add(fromDate);
			args2.add(toDate);
			args2.add(fromDate);
			args2.add(fromDate);
			
			sqlCanceled += " AND ((a.requiredFrom >= ? AND a.requiredFrom <= ?) OR (a.requiredFrom <= ? AND a.requiredTo >= ?))";
			args3.add(fromDate);
			args3.add(toDate);
			args3.add(fromDate);
			args3.add(fromDate);
		}
		
		if(programId!=null  && !programId.equals("") && !programId.equals("-1")){
			sqlfacility += " AND (r.program='"+programId+"')";
			
			sqlManpower += " AND (r.program='"+programId+"')";
			
			sqlCanceled += " AND (r.program='"+programId+"')";
		}
		
		if(serviceType!=null  && !serviceType.equals("")){
			sqlfacility += " AND a.serviceType IN( ";
			sqlManpower += " AND a.serviceType IN( ";
			sqlCanceled += " AND a.serviceType IN( ";
			String[] value=serviceType.split("#");
			int i=0;
			for (String string : value) {
				if(i==0){
					sqlfacility+=string;
					sqlManpower+=string;
					sqlCanceled+=string;
				}else{
					sqlfacility+=","+string;
					sqlManpower+=","+string;
					sqlCanceled+=","+string;
				}
				i++;
				
			}
			sqlfacility+=" )";
			sqlManpower+=" )";
			sqlCanceled+=" )";
		}
		
		if(status!=null  && !status.equals("") && !status.equals("-1")){
			if (!status.equals(EngineeringModule.CANCELLED_STATUS)){
				if (status.equals("F")){
					status = "C";
				}
				sqlfacility += " AND (e.status='"+status+"')";
			
				sqlManpower += " AND (e.status='"+status+"')";
				
				sqlCanceled += " AND (a.status='MZ')";
			}else{
				sqlfacility += " AND (e.status='X')";
				
				sqlManpower += " AND (e.status='X')";
			}
		}
		
		if(requestStatus != null && !requestStatus.equals("-1") && !requestStatus.equals("")){
			sqlfacility += " AND r.status = ? ";
			args.add(requestStatus);
			sqlManpower += " AND r.status = ? ";
			args2.add(requestStatus);
			sqlCanceled += " AND r.status = ? ";
			args3.add(requestStatus);
		}
		
		
		int totalFacility=0;
		int totalManpower=0;
		int totalCanceled = 0;
		Collection listingfacility = super.select(sqlfacility, HashMap.class, args.toArray(), 0, -1);
		Collection listingManpower = super.select(sqlManpower, HashMap.class, args2.toArray(), 0, -1);
		Collection listingCanceled = super.select(sqlCanceled, HashMap.class, args3.toArray(), 0, -1);
		
		if(listingfacility!=null && listingfacility.size()>0){
			for (Iterator iterator = listingfacility.iterator(); iterator.hasNext();) {
				Map map = (Map) iterator.next();
				totalFacility = Integer.parseInt(map.get("total").toString());
			}
		}
		
		if(listingManpower!=null && listingManpower.size()>0){
			for (Iterator iterator = listingManpower.iterator(); iterator.hasNext();) {
				Map map = (Map) iterator.next();
				totalManpower = Integer.parseInt(map.get("total").toString());
			}
		}
		
		if(listingCanceled!=null && listingCanceled.size()>0){
			for (Iterator iterator = listingCanceled.iterator(); iterator.hasNext();) {
				Map map = (Map) iterator.next();
				totalCanceled = Integer.parseInt(map.get("total").toString());
			}
		}
		total = totalFacility+totalManpower+totalCanceled;
		return total;
	}
	
	public String getBlockBooking(String requestId, String serviceType, String facilityId) throws DaoException{  
		String sql="Select p.blockBooking from fms_eng_service_";
		String tableExt="";
		String innerjoin="";
		String whereclause="";
		if(serviceType.equals(ServiceDetailsForm.SERVICE_SCPMCP)){
			tableExt="scp";
			innerjoin="INNER JOIN fms_rate_card f on f.id=p.facilityId";
			whereclause="where p.requestId=? AND serviceId=? AND p.facilityId=?";
		}else if(serviceType.equals(ServiceDetailsForm.SERVICE_POSTPRODUCTION)){
			tableExt="postproduction";
			innerjoin="INNER JOIN fms_rate_card f on f.id=p.facilityId";
			whereclause="where p.requestId=? AND serviceId=? AND p.facilityId=?";
		}else if(serviceType.equals(ServiceDetailsForm.SERVICE_STUDIO)){
			tableExt="studio";
			innerjoin="INNER JOIN fms_rate_card f on f.id=p.facilityId";
			whereclause="where p.requestId=? AND serviceId=? AND p.facilityId=?";
		}else if(serviceType.equals(ServiceDetailsForm.SERVICE_MANPOWER)){
			tableExt="manpower";
			innerjoin="INNER JOIN fms_rate_card f on f.id=p.competencyId";
			whereclause="where p.requestId=? AND serviceId=? AND p.competencyId=?";
		}else if(serviceType.equals(ServiceDetailsForm.SERVICE_TVRO)){
			tableExt="tvro";
			innerjoin="INNER JOIN fms_rate_card f on f.id=p.facilityId";
			whereclause="where p.requestId=? AND serviceId=? AND p.facilityId=?";
		}else if(serviceType.equals(ServiceDetailsForm.SERVICE_VTR)){
			tableExt="vtr";
			innerjoin="INNER JOIN fms_rate_card f on f.id=p.facilityId";
			whereclause="where p.requestId=? AND serviceId=? AND p.facilityId=?";
		}
		sql+=tableExt+" p " +innerjoin+" "+whereclause;
		String value="";
		Collection list = super.select(sql, HashMap.class, new String[]{requestId, serviceType, facilityId}, 0, -1);
		if(list!=null && list.size()>0){
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map map = (Map) iterator.next();
				value=map.get("blockBooking").toString();
			}
		}
		return value;
	}

	public boolean getAssignedStatus(String requestId) throws DaoException {
		String sql = "SELECT t.id AS id, t.status AS status, t.reason AS reason, t.createdBy AS createdBy, "
				+ "t.createdDate AS createdDate "
				+ "FROM fms_tran_request_status t "
				+ "WHERE t.id=? and t.status='G'";
		Collection list=super.select(sql, HashMap.class, new String[] { requestId }, 0,-1);
		if(list!=null && list.size()>0){
			return true;
		}
		return false;
	}
	
	public boolean assignedVehicle(String requestId)throws DaoException{
		String sql = "select id from fms_tran_request_vehicle where requestId=?";
		Collection list = super.select(sql, HashMap.class, new String[]{requestId}, 0, -1);
		if(list!=null && list.size()>0){
			return true;
		}else{
			return false;
		}
	}
	//added on 11 mar 2011
	public String getTransportRequestListingSql(ArrayList args, 
			String requestType, Date from, Date to, String programId ) throws DaoException {
		String sqlTransport="SELECT DISTINCT tr.id, ta.id as assignmentId, tr.requestTitle, tr.requestType, tr.program, tr.startDate, tr.endDate, tr.destination, " +
			"tr.purpose, tr.remarks, tr.status, tr.reason, tr.requestBy, tr.requestDate, tr.updatedBy, tr.updatedDate, " +
			"tr.approvedBy, tr.approvedDate, tr.statusRequest, tr.rate, tr.engineeringRequestId, tr.blockBooking, " +
			"su.firstname,su.lastname, d.name as department, o.id as outsourceId, ta.startDate as tranAssignSDate, ta.endDate as tranAssignEDate," +
			"v.driver as driverRequired, (tr.id+'/'+ta.id) as reqAsssignId, ps.programName, (tr.rate + tr.rateVehicle) AS totalcost " +
			"FROM fms_tran_request tr " +
			"left join security_user su on su.id=tr.requestBy " +
			"left join fms_department d on su.department = d.id " +
			"left join fms_tran_request_outsource o on o.requestId=tr.id " +
			"left join fms_tran_assignment ta on ta.requestId=tr.id " +
			"left join fms_tran_request_vehicle v on v.requestId=tr.id " +
			"LEFT JOIN fms_prog_setup ps ON ps.programId = tr.program " +
			"WHERE 1=1 AND tr.status <> '"+SetupModule.DRAFT_STATUS+"'";
		
		if(from != null && to != null){
			sqlTransport += " AND ((ta.startDate=? AND ta.endDate=?) OR (ta.startDate >= ? AND ta.endDate <= ?))";
			args.add(from);
			args.add(to);
			args.add(from);
			args.add(to); 
		}
		
		if(programId!=null && !programId.equals("")&& !programId.equals("-1")){
			sqlTransport += " AND tr.program=? ";
			args.add(programId);
		}
	
		if(requestType!=null  && !requestType.equals("")){
			if(requestType.equals("program")){
				sqlTransport += " AND (tr.program <> '-')";
			}else if(requestType.equals("nonprogram")){
				sqlTransport += " AND (tr.program = '-')";
			}else if(requestType.equals("all")){
				sqlTransport += " AND (tr.program <> '-' OR tr.program = '-')";
			}
			
		}
	
		return sqlTransport;
	}
	
	//added on 11 mar 2011
	public long getTotalTransportRequestListing( String requestType, Date from, 
			Date to, String programId ) throws DaoException {
		ArrayList args = new ArrayList();
		
		String sqlTransport = getTransportRequestListingSql( args, requestType,from, to, programId );
		sqlTransport = "SELECT COUNT(*) AS total FROM (" + sqlTransport + ") a";
		Collection result = super.select(sqlTransport, HashMap.class, args.toArray(), 0, -1 );
		if ( !result.isEmpty() ) {
			HashMap map = ( HashMap ) result.iterator().next();
			Number total = ( Number ) map.get( "total" );
			return total == null? 0L : total.longValue();
		}
		return 0L;
	}
	
	//added on 29 sept 2010
	public Collection getTransportRequestListing(String requestType, Date from, Date to, String programId, String sort, boolean desc, int startIndex,int maxRows) throws DaoException {
	ArrayList args = new ArrayList();
		
	String sqlTransport = getTransportRequestListingSql( args, requestType,from, to, programId );
	
	String strSort= "";		  
	    	
	if (sort != null && !sort.equals("")) {		
		if(!sort.equals("driversAssigned") 
				&& !sort.equals("fullname")
				&& !sort.equals("vehicleAssigned")
				&& !sort.equals("meterStart")
				&& !sort.equals("meterEnd")
				&& !sort.equals("checkInDate")
				&& !sort.equals("checkOutDate")
				&& !sort.equals("totalMeter")
				&& !sort.equals("outsourceFlag")
				&& !sort.equals("vehicleStatus")
				&& !sort.equals("driverStatus")){
			
			if(sort.equals("status")){
				sqlTransport += " ORDER BY tr.status ";
				if (desc)
					sqlTransport += " DESC";
			}else{
				sqlTransport += " ORDER BY " + sort;
				if (desc)
					sqlTransport += " DESC";
			}
		}
		else{
			sqlTransport += " ORDER BY tr.id ";
			if (desc)
				sqlTransport += " DESC";
		}
		
	}else 
		//sqlTransport += " ORDER BY tr.requestDate DESC";
		sqlTransport += " ORDER BY ta.startDate ASC";
	
		return super.select(sqlTransport, ReportsTransportObject.class, args.toArray(), startIndex, maxRows);
	}
	
	public String[] getVehicleNumber(String assignmentId) throws DaoException{
		String vehicleNum[]={""};
		String sql=" select distinct a.vehicle_num AS vehicle_num from fms_tran_request_assignment a where a.id=? ";
		Collection list =  super.select(sql, HashMap.class, new String[]{assignmentId}, 0, -1);
		if(list!=null && list.size()>0){
			vehicleNum = new String[list.size()];
			int i=0;
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map mp = (Map) iterator.next();
				vehicleNum[i]=mp.get("vehicle_num")!=null && !mp.get("vehicle_num").toString().equals("")? mp.get("vehicle_num").toString():"";
				i++;
			}
		}
		return vehicleNum;
		
	}
	
	public String getVehiclesDriver(String assignmentId, String vehicle_num) throws DaoException{
		String driver="";
		String sql=" select (su.firstName+' '+su.Lastname) as fullName from fms_tran_request_driver_vehicle rdv " +
				"inner join security_user su on su.id = rdv.driver " +
				"where assignmentId=? and vehicle_num=?";
		Collection list =  super.select(sql, HashMap.class, new String[]{assignmentId,vehicle_num}, 0, -1);
		if(list!=null && list.size()>0){
			driver="";
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map mp = (Map) iterator.next();
				driver+=mp.get("fullName").toString();
				if(iterator.hasNext())
					driver+=",<br>";
			}
		}
		return driver;
		
	}
	public Map getVehiclesDriverData(String assignmentId, String vehicle_num) throws DaoException{
		//String driver="";
		Map mp = null;
		String sql=" select (su.firstName+' '+su.Lastname) as fullName, rdv.driver from fms_tran_request_driver_vehicle rdv " +
				"inner join security_user su on su.id = rdv.driver " +
				"where assignmentId=? and vehicle_num=?";
		Collection list =  super.select(sql, HashMap.class, new String[]{assignmentId,vehicle_num}, 0, -1);
		if(list!=null && list.size()>0){
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				 mp = (Map) iterator.next();
			}
		}
		return mp;
		
	}
	
	public void addFacilityReport(ResourcesObject resource)throws DaoException {
		
		String sql = "INSERT INTO fms_eng_resources_report " +
				"(id, serviceId, requestId, facilityId, bookDate, hour, minutes) VALUES " +
				"(#id#, #serviceId#, #requestId#, #facilityId#, #bookDate#, #hour#, #minutes#)";
		
		super.update(sql, resource);
	}
	
	public Collection getFacilityResource(String serviceId, String programId, String departmentId, Date start, Date end)throws DaoException{
		
		ArrayList args = new ArrayList();
		
		String sql = "SELECT DISTINCT(facilityId), frc.name	"+
		"FROM fms_eng_resources_report report 	"+
		"INNER JOIN fms_rate_card frc on frc.id=report.facilityId 	"+
		"INNER JOIN fms_eng_request r on r.requestId=report.requestId 	"+
		"INNER JOIN security_user u on r.createdBy=u.username 	"+
		"INNER JOIN fms_department d on u.department=d.id 	";
		
		if(programId!=null  && !programId.equals("") && !programId.equals("-1")){
			sql += "INNER JOIN fms_prog_setup prog on prog.programId=r.program 	";
		}		
		sql +=  "WHERE report.serviceId = ?	";
		args.add(serviceId);
		
		if(start != null && end != null){
			sql += " AND (bookDate >= ?	AND bookDate <= ?) ";
			args.add(start);		
			args.add(end);
		}
		
		if(programId!=null  && !programId.equals("") && !programId.equals("-1")){
			sql += " AND (r.program=?) ";
			args.add(programId);
		}
		
		if(departmentId != null && !"".equals(departmentId) && !"-1".equals(departmentId)){
			sql += " AND (d.id=?) ";
			args.add(departmentId);
		}
		
		sql += "GROUP BY bookDate, facilityId, frc.name, d.name	ORDER BY frc.name";
				
		Collection list = super.select(sql, HashMap.class, args.toArray(), 0, -1);
		
		return list;
	}
	
	public Collection getFacilityResourceDate(String serviceId, String programId, String departmentId, Date start, Date end)throws DaoException{
		
		ArrayList args = new ArrayList();	
		
		String sql = "SELECT DISTINCT(facilityId), bookDate	"+
		"FROM fms_eng_resources_report report 	"+
		"INNER JOIN fms_rate_card frc on frc.id=report.facilityId 	"+
		"INNER JOIN fms_eng_request r on r.requestId=report.requestId 	"+
		"INNER JOIN security_user u on r.createdBy=u.username 	"+
		"INNER JOIN fms_department d on u.department=d.id 	";
		
		if(programId!=null  && !programId.equals("") && !programId.equals("-1")){
			sql += "INNER JOIN fms_prog_setup prog on prog.programId=r.program 	";
		}		
		sql +=  "WHERE report.serviceId = ?	";
		args.add(serviceId);
		
		if(start != null && end != null){
			sql += " AND (bookDate >= ?	AND bookDate <= ?) ";
			args.add(start);		
			args.add(end);
		}
		
		if(programId!=null  && !programId.equals("") && !programId.equals("-1")){
			sql += " AND (r.program=?) ";
			args.add(programId);
		}
		
		if(departmentId != null && !"".equals(departmentId) && !"-1".equals(departmentId)){
			sql += " AND (d.id=?) ";
			args.add(departmentId);
		}
		
		sql += "GROUP BY bookDate, facilityId, frc.name, d.name	";
				
		Collection list = super.select(sql, HashMap.class, args.toArray(), 0, -1);
		
		return list;
	}
	
	public int getCountFacilityByDate(String serviceId, String facilityId, Date date)throws DaoException{
		
		int total = 0;
		String sql = "SELECT count(*) as total FROM fms_eng_resources_report report " +
				"INNER JOIN fms_rate_card frc on frc.id=report.facilityId " +
				"WHERE report.serviceId = ? AND facilityId = ? AND bookDate = ? ";
		
		Collection list = super.select(sql, HashMap.class, new Object[]{serviceId, facilityId, date}, 0, -1);
		
		if(list!=null && list.size()>0){
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map map = (Map) iterator.next();
				total = Integer.parseInt(map.get("total").toString());
			}
		}
		//total = totalFacility+totalManpower;
		return total;
	
	}
	
	public Collection getHoursRequestAverage(Date bookDate, String serviceId, String facilityId)throws DaoException{
		
		ArrayList args = new ArrayList();
		
		String sql = 
			"SELECT distinct report.facilityId, sum(hour) AS hour, COUNT(distinct report.requestId) AS requestNo, sum(minutes) AS minutes, " +
			"(sum(hour)*60 + sum(minutes))/COUNT(distinct report.requestId) AS average, (sum(hour)*60/COUNT(distinct report.requestId))%60 AS modMinutes " +
			"FROM fms_eng_resources_report report	" +
			"INNER JOIN fms_facility_booking fb ON report.requestId = fb.requestId " +
			"INNER JOIN fms_rate_card frc on frc.id=report.facilityId	" +
			"WHERE report.serviceId = ? AND report.facilityId = ? ";
				
		args.add(serviceId);
		args.add(facilityId);
		
		if(bookDate != null){
			sql += " AND (? BETWEEN fb.bookFrom AND fb.bookTo )	";
			args.add(bookDate);		
		}
		
		sql += "GROUP BY report.facilityId ORDER BY report.facilityId ";
		Collection list = super.select(sql, HashMap.class, args.toArray(), 0, -1);
		
		return list;
	}
	
	public Collection getVehicleNumByAssignmentId(String assignmentId) throws DaoException{
		String sql="select distinct a.vehicle_num " +
				"from fms_tran_request_assignment a " +
				"where a.id=?";
		return super.select(sql, HashMap.class, new String[]{assignmentId}, 0, -1);
		
	}
	
	public String getStatusVehicle(String assignmentId, String vehicleNum) throws DaoException{
		String status="";
		String sql="select status from fms_tran_request_assignment_details where assgId=? and vehicle_num=?";
		Collection list =  super.select(sql, HashMap.class, new String[]{assignmentId, vehicleNum}, 0, -1);
		if(list!=null && list.size()>0){
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map mp = (Map) iterator.next();
				status=mp.get("status")!=null && !mp.get("status").toString().equals("")? mp.get("status").toString():"1";
				
			}
		}
		return status;
		
	}
	
	public String getStatusVehicleByAssignmentId(String assignmentId, String requestId) throws DaoException{
		String status="";
		String sql="select status from fms_tran_request_assignment_details where assgId=?";
		Collection list =  super.select(sql, HashMap.class, new String[]{assignmentId}, 0, -1);
		if(assignedVehicle(requestId)&&checkVehicleNum(assignmentId)){
			if(list!=null && list.size()>0){
				Iterator appIt = list.iterator();
				Map appMap_i[] = new HashMap[list.size()];
				Map appMap_j[] = new HashMap[list.size()];
				for (int i = 0; i < list.size(); i++) {
					appMap_i[i] = (Map)appIt.next();	
					String status_i = appMap_i[i].get("status")!=null?appMap_i[i].get("status").toString():"1";

					for (int j = 0; j < list.size(); j++) {
						appMap_j[j] = (Map)list.iterator().next();	
						String status_j=appMap_j[j].get("status")!=null?appMap_j[j].get("status").toString():"1";
						if(!status_j.equals(status_i)){
							return "1";
						}else{
							status=status_i;
						}
					}
				}
			}else{
				status="1";
			}
		}else{
			status="0";
		}
		
		return status;
		
	}
	
	public String getStatusVehicleByAssignmentAndVehicleId(String assignmentId, String requestId, String vehicleNum) throws DaoException{
		String status="";
		String sql="select status from fms_tran_request_assignment_details where assgId=? and vehicle_num=?";
		Collection list =  super.select(sql, HashMap.class, new String[]{assignmentId}, 0, -1);
		if(assignedVehicle(requestId)&&checkVehicleNum(assignmentId)){
			if(list!=null && list.size()>0){
				Iterator appIt = list.iterator();
				Map appMap_i[] = new HashMap[list.size()];
				Map appMap_j[] = new HashMap[list.size()];
				for (int i = 0; i < list.size(); i++) {
					appMap_i[i] = (Map)appIt.next();	
					String status_i = appMap_i[i].get("status")!=null?appMap_i[i].get("status").toString():"1";

					for (int j = 0; j < list.size(); j++) {
						appMap_j[j] = (Map)list.iterator().next();	
						String status_j=appMap_j[j].get("status")!=null?appMap_j[j].get("status").toString():"1";
						if(!status_j.equals(status_i)){
							return "1";
						}else{
							status=status_i;
						}
					}
				}
			}else{
				status="1";
			}
		}else{
			status="0";
		}
		
		return status;
		
	}
	
	public String getDriversStatus(String assignmentId) throws DaoException{
		String status="";
		String sql="select status from fms_tran_request_driver where assignmentId=?";
		Collection list =  super.select(sql, HashMap.class, new String[]{assignmentId}, 0, -1);
		
		if(list!=null && list.size()>0){
			Iterator appIt = list.iterator();
			Map appMap_i[] = new HashMap[list.size()];
			Map appMap_j[] = new HashMap[list.size()];
			for (int i = 0; i < list.size(); i++) {
				appMap_i[i] = (Map)appIt.next();	
				String status_i = appMap_i[i].get("status")!=null?appMap_i[i].get("status").toString():"1";

				for (int j = 0; j < list.size(); j++) {
					appMap_j[j] = (Map)list.iterator().next();	
					String status_j=appMap_j[j].get("status")!=null?appMap_j[j].get("status").toString():"1";
					if(!status_j.equals(status_i)){
						return "1";
					}else{
						status=status_i;
					}
				}
			}
		}else{
			status="0";
		}
		return status;
		
	}
	
	public String getDriversStatus(String assignmentId, String manpowerId) throws DaoException{
		String status="";
		String sql="select status from fms_tran_request_driver where assignmentId=? and manpowerId=? ";
		Collection list =  super.select(sql, HashMap.class, new String[]{assignmentId, manpowerId}, 0, -1);
		
		if(list!=null && list.size()>0){
			Iterator appIt = list.iterator();
			Map appMap_i =(Map)appIt.next();	
			status= appMap_i.get("status")!=null?appMap_i.get("status").toString():"1";
		}else{
			status="0";
		}
		return status;
		
	}
	
	public boolean checkVehicleNum(String assignmentId)throws DaoException{
		String sql="select distinct a.vehicle_num " +
		"from fms_tran_request_assignment a " +
		"where a.id=?";
		Collection list = super.select(sql, HashMap.class, new String[]{assignmentId}, 0, -1);
		if(list!=null && list.size()>0){
			return true;
		}else{
			return false;
		}
	}
	
	public int countTransportRequestListingNew(String requestType, Date from, Date to, String programId) throws DaoException{
		ArrayList args = new ArrayList();
		int result=0;
		String sqlTransport="" +
				"SELECT COUNT(*) as total FROM (" +
				"SELECT DISTINCT tr.id,ta.id as assignmentId,tr.requestTitle,tr.requestType,tr.program,tr.startDate,tr.endDate,tr.destination," +
				"tr.purpose,tr.remarks,tr.status,tr.reason,tr.requestBy, tr.requestDate, tr.updatedBy, tr.updatedDate, tr.approvedBy," +
				"tr.approvedDate, tr.statusRequest, tr.rate, tr.engineeringRequestId, tr.blockBooking, su.firstname,su.lastname," +
				"d.name as department,o.id as outsourceId,ta.startDate as tranAssignSDate,ta.endDate as tranAssignEDate,v.driver as driverRequired " +
				"FROM fms_tran_request tr " +
				"left join security_user su on su.id=tr.requestBy " +
				"left join fms_department d on su.department = d.id " +
				"left join fms_tran_request_outsource o on o.requestId=tr.id " +
				"left join fms_tran_assignment ta on ta.requestId=tr.id " +
				"left join fms_tran_request_vehicle v on v.requestId=tr.id " +
				"WHERE 1=1 AND tr.status <> '"+SetupModule.DRAFT_STATUS+"'";
	
	if(from != null && to != null){
		sqlTransport += " AND ((tr.startDate >= ? AND tr.startDate <= ?) OR (tr.startDate <= ? AND tr.endDate >= ?))";
		args.add(from);
		args.add(to);
		args.add(from);
		args.add(from);
	}
	
	if(programId!=null && !programId.equals("")&& !programId.equals("-1")){
		sqlTransport += " AND tr.program=? ";
		args.add(programId);
	}

	if(requestType!=null  && !requestType.equals("")){
		if(requestType.equals("program")){
			sqlTransport += " AND (tr.program <> '-')";
		}else if(requestType.equals("nonprogram")){
			sqlTransport += " AND (tr.program = '-')";
		}else if(requestType.equals("all")){
			sqlTransport += " AND (tr.program <> '-' OR tr.program = '-')";
		}
		
	}
	
	sqlTransport +=")T ";
		
		Collection list =  super.select(sqlTransport, HashMap.class, args.toArray(), 0, -1);
		if(list!=null && list.size()>0){
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map map = (Map) iterator.next();
				
				result = Integer.parseInt(map.get("total").toString());
			}
		}
		return result;
	}
	
	public String[] getRequestedVehiclesAndDrivers(String requestId) throws DaoException{
		String[] result = new String[2];
		
		if(requestId != null && !requestId.equals("")){
			String query = "SELECT v.category_Id, v.requestId, " +
					"c.name + ' (' + CAST(SUM(v.quantity) AS VARCHAR) + ')' AS requestedVehicles, " +
					"CAST(SUM(v.driver) AS VARCHAR) AS requestedDrivers, " +
					"v.category_id " +
					"FROM fms_tran_request_vehicle v " +
					"LEFT JOIN fms_tran_category c ON c.setup_id = v.category_id " +
					"WHERE v.requestId = ? " +
					"GROUP BY v.requestId, v.category_id, c.name " +
					"ORDER BY c.name ";
			
			Collection<HashMap<String, String>> colTemp = super.select(query, HashMap.class, new Object[]{requestId}, 0, -1);
			if(colTemp != null && colTemp.size() > 0){
				result[0] = "";
				result[1] = "";
				int countDriver = 0;
				int counter = 0;
				for(HashMap<String, String> hash : colTemp){
					counter++;
					result[0] += hash.get("requestedVehicles") + ((counter == colTemp.size()) ? "" : "<br/>");
					countDriver += Integer.parseInt(hash.get("requestedDrivers"));
				}
				result[1] = Integer.toString(countDriver);
			}
		}
		
		return result;
	}
	
}
