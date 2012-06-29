package com.tms.fms.reports.model;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DefaultModule; 
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.engineering.model.ManpowerService;
import com.tms.fms.engineering.model.OtherService;
import com.tms.fms.engineering.model.PostProductionService;
import com.tms.fms.engineering.model.ScpService;
import com.tms.fms.engineering.model.Service;
import com.tms.fms.engineering.model.StudioService;
import com.tms.fms.engineering.model.TvroService;
import com.tms.fms.engineering.model.VtrService;
import com.tms.fms.engineering.ui.ServiceDetailsForm;
import com.tms.fms.setup.model.SetupModule;
import com.tms.fms.transport.model.TransportModule;
import com.tms.fms.util.DateDiffUtil;


public class ReportsFmsModule extends DefaultModule{
	
	protected int minutes;
	public static long listCounter;
	public static long listCounter2;

	public long getTotalTransportRequestListing( String requestType, Date from, 
			Date to, String programId ) {
		try {
			ReportsFmsDao dao=(ReportsFmsDao)getDao();
			return dao.getTotalTransportRequestListing(requestType, from, to, programId);
		} catch (DaoException e) {
			return 0L;
		} 
	}
	
	public Collection getTransportRequestListing(String requestType,Date from, Date to, String programId, String sort, boolean desc, int startIndex,int maxRows){
		ArrayList args = new ArrayList();
		ReportsFmsDao dao=(ReportsFmsDao)getDao();
		listCounter=0;
		try {
			if(from != null && to != null){
			 Calendar cal = Calendar.getInstance();
	         cal.setTime(from);
	         cal.set(Calendar.HOUR_OF_DAY, 0);
	         cal.set(Calendar.MINUTE, 0);
	         cal.set(Calendar.SECOND, 0);
	         from = cal.getTime();
	         cal.setTime(to);
	         cal.set(Calendar.HOUR, 11);
	         cal.set(Calendar.MINUTE, 59);
	         cal.set(Calendar.SECOND, 59);
	         to = cal.getTime();
			}   
			Collection col = dao.getTransportRequestListing(requestType, from, to, programId, sort, desc, startIndex, maxRows);
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
			
			for (Iterator iter = col.iterator(); iter.hasNext();){
				ReportsTransportObject report = (ReportsTransportObject) iter.next();
				//ReportsTransportObject report2 = report;
				
				Date dateFromAssignment=null; 
			    Date dateToAssignment = null;
			    String dateFromStrAssignment="";
			    String dateToStrAssignment="";
			    long datediff=0;
			    
				try{
					dateFromAssignment=(Date) formatter.parse(report.getTranAssignSDate()!=null?report.getTranAssignSDate():"");
					//String tempo = dateFromAssignment.toString();
					dateToAssignment=(Date) formatter.parse(report.getTranAssignEDate()!=null?report.getTranAssignEDate():"");
					dateFromStrAssignment=sdf.format(dateFromAssignment);
					dateToStrAssignment=sdf.format(dateToAssignment);
				}
				catch(Exception e){
					
				}
				if(null!=dateFromAssignment){
					datediff = dateDiff4Assignment(dateFromAssignment,dateToAssignment);
					if(!dateFromStrAssignment.equals(dateToStrAssignment))
						datediff++;
				}
				
				Calendar cal2 = Calendar.getInstance();  
				if(null!=dateFromAssignment)
					cal2.setTime(dateFromAssignment);  
				
				if(report.getBlockBooking().equals("0")){

					if(datediff==0){
						if(null!=dateFromAssignment)
							report.setTransAssignmentDateStrMod(sdf2.format(cal2.getTime()));
						String[] numbers = dao.getVehicleNumber(report.getAssignmentId());
						
						if(numbers.length>1){
							for(int i=0;i<numbers.length;i++){
								try{
									ReportsTransportObject report3=(ReportsTransportObject)report.clone();
									
									//String driver = dao.getVehiclesDriver(report3.getAssignmentId(),numbers[i]);
									ReportsTransportObject tempReport = assignTransportDataReport(report3.getId(), 
											report3.getAssignmentId(),numbers[i],report3.getDriverRequired());
									
									report3.setSingleMeterStart(tempReport.getSingleMeterStart());
									report3.setSingleMeterEnd(tempReport.getSingleMeterEnd());
									report3.setSingleTotalMeter(tempReport.getSingleTotalMeter());
									report3.setSingleCheckInDate(tempReport.getSingleCheckInDate());
									report3.setSingleCheckOutDate(tempReport.getSingleCheckOutDate());
									report3.setSingleTimeRec(tempReport.getSingleTimeRec());
									report3.setSingleVehicleStatus(tempReport.getSingleVehicleStatus());
									report3.setSingleDriverStatus(tempReport.getSingleDriverStatus());
									report3.setVehicleAssigned(tempReport.getVehicleAssigned());
									if(report3.getTransStatus()!=null && !report3.getTransStatus().equals("") 
											&& report3.getStatus().equals(SetupModule.REJECTED_STATUS))
										report3.setDriversAssigned(Application.getInstance().getMessage("fms.report.status.batal","Batal"));
									else if (report3.getTransStatus()!=null && !report3.getTransStatus().equals("") 
											&& report3.getStatus().equals(SetupModule.OUTSOURCED_STATUS)){
										report3.setDriversAssigned(Application.getInstance().getMessage("fms.report.status.sewa","Sewa"));
										//report3.setVehicleAssigned(Application.getInstance().getMessage("fms.engineering.request.status.outsource","Outsource"));
									}
									else{
										if(report3.getSingleDriverStatus().equals(SetupModule.UNFULFILLED_STATUS))
											report3.setDriversAssigned("No.News");
										else
											report3.setDriversAssigned(tempReport.getDriversAssigned());
									}
									if(report3.getSingleVehicleStatus().equals(SetupModule.UNFULFILLED_STATUS)&&report3.getDriversAssigned().equals("Pandu Sendiri")){
										report3.setDriversAssigned("No.News");
										report3.setSingleDriverStatus(SetupModule.UNFULFILLED_STATUS);
									}
									
									args.add(report3);
									listCounter++;
								}
								catch(Exception e){
									
								}
							}
						}else{
							//String driver = dao.getVehiclesDriver(report.getAssignmentId(),numbers[0]);
							ReportsTransportObject tempReport = assignTransportDataReport(report.getId(), 
									report.getAssignmentId(),numbers[0],report.getDriverRequired());
							
							report.setSingleMeterStart(tempReport.getSingleMeterStart());
							report.setSingleMeterEnd(tempReport.getSingleMeterEnd());
							report.setSingleTotalMeter(tempReport.getSingleTotalMeter());
							report.setSingleCheckInDate(tempReport.getSingleCheckInDate());
							report.setSingleCheckOutDate(tempReport.getSingleCheckOutDate());
							report.setSingleTimeRec(tempReport.getSingleTimeRec());
							report.setSingleVehicleStatus(tempReport.getSingleVehicleStatus());
							report.setSingleDriverStatus(tempReport.getSingleDriverStatus());
							report.setVehicleAssigned(tempReport.getVehicleAssigned());
							if(report.getTransStatus()!=null && !report.getTransStatus().equals("") 
									&& report.getStatus().equals(SetupModule.REJECTED_STATUS))
								report.setDriversAssigned(Application.getInstance().getMessage("fms.report.status.batal","Batal"));
							else if (report.getTransStatus()!=null && !report.getTransStatus().equals("") 
									&& report.getStatus().equals(SetupModule.OUTSOURCED_STATUS)){
								report.setDriversAssigned(Application.getInstance().getMessage("fms.report.status.sewa","Sewa"));
								//report.setVehicleAssigned(Application.getInstance().getMessage("fms.engineering.request.status.outsource","Outsource"));
							}
							else{
								if(report.getSingleDriverStatus().equals(SetupModule.UNFULFILLED_STATUS))
									report.setDriversAssigned("No.News");
								else
									report.setDriversAssigned(tempReport.getDriversAssigned());
							}
							if(report.getSingleVehicleStatus().equals(SetupModule.UNFULFILLED_STATUS)&&report.getDriversAssigned().equals("Pandu Sendiri")){
								report.setDriversAssigned("No.News");
								report.setSingleDriverStatus(SetupModule.UNFULFILLED_STATUS);
							}
							args.add(report);
							listCounter++;
						}
							//args.add(report);
					}
					for(int cntr =0;cntr<datediff;cntr++){
						try{
							
							ReportsTransportObject report2=(ReportsTransportObject)report.clone();
							if(null!=dateFromAssignment)
								report2.setTransAssignmentDateStrMod(sdf2.format(cal2.getTime()));
							String[] numbers = dao.getVehicleNumber(report2.getAssignmentId());
							if(numbers.length>1){
								for(int i=0;i<numbers.length;i++){
									try{
										ReportsTransportObject report3=(ReportsTransportObject)report2.clone();
										//String driver = dao.getVehiclesDriver(report3.getAssignmentId(),numbers[i]);
										ReportsTransportObject tempReport = assignTransportDataReport(report3.getId(), 
												report3.getAssignmentId(),numbers[i],report3.getDriverRequired());
										
										report3.setSingleMeterStart(tempReport.getSingleMeterStart());
										report3.setSingleMeterEnd(tempReport.getSingleMeterEnd());
										report3.setSingleTotalMeter(tempReport.getSingleTotalMeter());
										report3.setSingleCheckInDate(tempReport.getSingleCheckInDate());
										report3.setSingleCheckOutDate(tempReport.getSingleCheckOutDate());
										report3.setSingleTimeRec(tempReport.getSingleTimeRec());
										report3.setSingleVehicleStatus(tempReport.getSingleVehicleStatus());
										report3.setSingleDriverStatus(tempReport.getSingleDriverStatus());
										report3.setVehicleAssigned(tempReport.getVehicleAssigned());
										if(report3.getTransStatus()!=null && !report3.getTransStatus().equals("") 
												&& report3.getStatus().equals(SetupModule.REJECTED_STATUS))
											report3.setDriversAssigned(Application.getInstance().getMessage("fms.report.status.batal","Batal"));
										else if (report3.getTransStatus()!=null && !report3.getTransStatus().equals("") 
												&& report3.getStatus().equals(SetupModule.OUTSOURCED_STATUS)){
											report3.setDriversAssigned(Application.getInstance().getMessage("fms.report.status.sewa","Sewa"));
											//report3.setVehicleAssigned(Application.getInstance().getMessage("fms.engineering.request.status.outsource","Outsource"));
										}
										else{
											if(report3.getSingleDriverStatus().equals(SetupModule.UNFULFILLED_STATUS))
												report3.setDriversAssigned("No.News");
											else
												report3.setDriversAssigned(tempReport.getDriversAssigned());
										}
										if(report3.getSingleVehicleStatus().equals(SetupModule.UNFULFILLED_STATUS)&&report3.getDriversAssigned().equals("Pandu Sendiri")){
											report3.setDriversAssigned("No.News");
											report3.setSingleDriverStatus(SetupModule.UNFULFILLED_STATUS);
										}
										args.add(report3);
										listCounter++;
									}
									catch(Exception e){
										
									}
								}
							}else
							{
								//String driver = dao.getVehiclesDriver(report2.getAssignmentId(),numbers[0]);
								ReportsTransportObject tempReport = assignTransportDataReport(report2.getId(), 
										report2.getAssignmentId(),numbers[0],report2.getDriverRequired());
								
								report2.setSingleMeterStart(tempReport.getSingleMeterStart());
								report2.setSingleMeterEnd(tempReport.getSingleMeterEnd());
								report2.setSingleTotalMeter(tempReport.getSingleTotalMeter());
								report2.setSingleCheckInDate(tempReport.getSingleCheckInDate());
								report2.setSingleCheckOutDate(tempReport.getSingleCheckOutDate());
								report2.setSingleTimeRec(tempReport.getSingleTimeRec());
								report2.setSingleVehicleStatus(tempReport.getSingleVehicleStatus());
								report2.setSingleDriverStatus(tempReport.getSingleDriverStatus());
								report2.setVehicleAssigned(tempReport.getVehicleAssigned());
								if(report2.getTransStatus()!=null && !report2.getTransStatus().equals("") 
										&& report2.getStatus().equals(SetupModule.REJECTED_STATUS))
									report2.setDriversAssigned(Application.getInstance().getMessage("fms.report.status.batal","Batal"));
								else if (report2.getTransStatus()!=null && !report2.getTransStatus().equals("") 
										&& report2.getStatus().equals(SetupModule.OUTSOURCED_STATUS)){
									report2.setDriversAssigned(Application.getInstance().getMessage("fms.report.status.sewa","Sewa"));
									//report2.setVehicleAssigned(Application.getInstance().getMessage("fms.engineering.request.status.outsource","Outsource"));
								}
								else{
									if(report2.getSingleDriverStatus().equals(SetupModule.UNFULFILLED_STATUS))
										report2.setDriversAssigned("No.News");
									else
										report2.setDriversAssigned(tempReport.getDriversAssigned());
								}
								if(report2.getSingleVehicleStatus().equals(SetupModule.UNFULFILLED_STATUS)&&report2.getDriversAssigned().equals("Pandu Sendiri")){
									report2.setDriversAssigned("No.News");
									report2.setSingleDriverStatus(SetupModule.UNFULFILLED_STATUS);
								}
								args.add(report2);
								listCounter++;
							}
							//args.add(report2);
							if(null!=dateFromAssignment){
							cal2.add(Calendar.DAY_OF_YEAR, 1); // <--  
							cal2.getTime(); }
						}catch(Exception e){
							
						}
					}
				}else{
					if((report.getId()==null && report.getId().equals(""))&& (report.getAssignmentId()!= null && !report.getAssignmentId().equals(""))){
						report.setId(report.getAssignmentId());
					}
					if(null!=dateFromAssignment)
					report.setTransAssignmentDateStrMod(sdf2.format(cal2.getTime()));
					String[] numbers = dao.getVehicleNumber(report.getAssignmentId());
					if(numbers.length>1){
						for(int i=0;i<numbers.length;i++){
							try{
								ReportsTransportObject report3=(ReportsTransportObject)report.clone();
								//String driver = dao.getVehiclesDriver(report3.getAssignmentId(),numbers[i]);
								ReportsTransportObject tempReport = assignTransportDataReport(report3.getId(), 
										report3.getAssignmentId(),numbers[i],report3.getDriverRequired());
								
								report3.setSingleMeterStart(tempReport.getSingleMeterStart());
								report3.setSingleMeterEnd(tempReport.getSingleMeterEnd());
								report3.setSingleTotalMeter(tempReport.getSingleTotalMeter());
								report3.setSingleCheckInDate(tempReport.getSingleCheckInDate());
								report3.setSingleCheckOutDate(tempReport.getSingleCheckOutDate());
								report3.setSingleTimeRec(tempReport.getSingleTimeRec());
								report3.setSingleVehicleStatus(tempReport.getSingleVehicleStatus());
								report3.setSingleDriverStatus(tempReport.getSingleDriverStatus());
								report3.setVehicleAssigned(tempReport.getVehicleAssigned());
								if(report3.getTransStatus()!=null && !report3.getTransStatus().equals("") 
										&& report3.getStatus().equals(SetupModule.REJECTED_STATUS))
									report3.setDriversAssigned(Application.getInstance().getMessage("fms.report.status.batal","Batal"));
								else if (report3.getTransStatus()!=null && !report3.getTransStatus().equals("") 
										&& report3.getStatus().equals(SetupModule.OUTSOURCED_STATUS)){
									report3.setDriversAssigned(Application.getInstance().getMessage("fms.report.status.sewa","Sewa"));
									//report3.setVehicleAssigned(Application.getInstance().getMessage("fms.engineering.request.status.outsource","Outsource"));
								}
								else{
									if(report3.getSingleDriverStatus().equals(SetupModule.UNFULFILLED_STATUS))
										report3.setDriversAssigned("No.News");
									else
										report3.setDriversAssigned(tempReport.getDriversAssigned());
								}
								if(report3.getSingleVehicleStatus().equals(SetupModule.UNFULFILLED_STATUS)&&report3.getDriversAssigned().equals("Pandu Sendiri")){
									report3.setDriversAssigned("No.News");
									report3.setSingleDriverStatus(SetupModule.UNFULFILLED_STATUS);
								}
								args.add(report3);
								listCounter++;
							}
							catch(Exception e){
								
							}
						}
					}else{
						//String driver = dao.getVehiclesDriver(report.getAssignmentId(),numbers[0]);
						ReportsTransportObject tempReport = assignTransportDataReport(report.getId(), 
								report.getAssignmentId(),numbers[0],report.getDriverRequired());
						
						report.setSingleMeterStart(tempReport.getSingleMeterStart());
						report.setSingleMeterEnd(tempReport.getSingleMeterEnd());
						report.setSingleTotalMeter(tempReport.getSingleTotalMeter());
						report.setSingleCheckInDate(tempReport.getSingleCheckInDate());
						report.setSingleCheckOutDate(tempReport.getSingleCheckOutDate());
						report.setSingleTimeRec(tempReport.getSingleTimeRec());
						report.setSingleVehicleStatus(tempReport.getSingleVehicleStatus());
						report.setSingleDriverStatus(tempReport.getSingleDriverStatus());
						report.setVehicleAssigned(tempReport.getVehicleAssigned());
						if(report.getTransStatus()!=null && !report.getTransStatus().equals("") 
								&& report.getStatus().equals(SetupModule.REJECTED_STATUS))
							report.setDriversAssigned(Application.getInstance().getMessage("fms.report.status.batal","Batal"));
						else if (report.getTransStatus()!=null && !report.getTransStatus().equals("") 
								&& report.getStatus().equals(SetupModule.OUTSOURCED_STATUS)){
							report.setDriversAssigned(Application.getInstance().getMessage("fms.report.status.sewa","Sewa"));
							//report.setVehicleAssigned(Application.getInstance().getMessage("fms.engineering.request.status.outsource","Outsource"));
						}
						else{
							if(report.getSingleDriverStatus().equals(SetupModule.UNFULFILLED_STATUS))
								report.setDriversAssigned("No.News");
							else
								report.setDriversAssigned(tempReport.getDriversAssigned());
						}
						if(report.getSingleVehicleStatus().equals(SetupModule.UNFULFILLED_STATUS)&&report.getDriversAssigned().equals("Pandu Sendiri")){
							report.setDriversAssigned("No.News");
							report.setSingleDriverStatus(SetupModule.UNFULFILLED_STATUS);
						}
						args.add(report);
						listCounter++;
					}
				}
				
			}
			return args;
		} catch (DaoException e) {
			return null;
		}
	}
	
	private String getFormattedCheckedDate(Map map, String type){
		DateFormat tempFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		SimpleDateFormat tempsdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
		String dateStr="";
		if (map.get(type) != null && !map.get(type).toString().equals("")) {
			try {	
				Date date = (Date) tempFormatter.parse(map.get(type).toString());
				dateStr=tempsdf.format(date);			
			} catch (ParseException e) {
				Log.getLog(getClass()).error(e);
			}
		}
		return dateStr;
	}
	
	private ReportsTransportObject assignTransportDataReport(String requestId, String assignmentId, String vehicleNum, String driverRequired) {
		ReportsTransportObject reportObject = new ReportsTransportObject();
		ReportsFmsDao dao=(ReportsFmsDao)getDao();
		
		try{
			Map mpDrv = dao.getVehiclesDriverData(assignmentId,vehicleNum);
			String driver = "";
			String driverId = "";
			if(mpDrv!=null&&mpDrv.size()>0){
				driver = mpDrv.get("fullName").toString();
				driverId = mpDrv.get("driver").toString();
			}
			Map map = getSingleVehicle(assignmentId,vehicleNum);
			String meterStart = "";
			String meterEnd ="";
			String totalMeter ="" ;//later change to total
			String checkinDate ="";
			String checkoutDate ="";
			String timeRec ="";
			String status ="";
			String drvStat ="";
			
			try{
				drvStat = getDriversStatus(assignmentId,driverId);
			}catch(Exception e){}
			
			if(assignedVehicle(requestId)&&dao.checkVehicleNum(assignmentId)){
				try{
					status = map.get("status").toString();
				}
				catch(Exception e){
				}
				//System.out.println("status: "+status + "REq ID: "+requestId);
				if(status==null || "".equals(status))
					status="1";
			}
			else
				status="0";
			
			if(map!=null&&map.size()>0){
			
				meterStart = map.get("meterStart").toString();
				meterEnd = map.get("meterEnd").toString();
				int totalMeterTemp = Integer.parseInt(meterEnd)-Integer.parseInt(meterStart);//later change to total
				if(totalMeterTemp>0)
					totalMeter = String.valueOf(totalMeterTemp);
				checkinDate = getFormattedCheckedDate(map,"checkin_date");
				checkoutDate = getFormattedCheckedDate(map,"checkout_date");
				status = map.get("status").toString();
				if(map.get("timeRec")!=null && !map.get("timeRec").toString().equals("")){
					timeRec=map.get("timeRec").toString();
				}
			}
			reportObject.setSingleMeterStart(meterStart);
			reportObject.setSingleMeterEnd(meterEnd);
			reportObject.setSingleTotalMeter(totalMeter);
			reportObject.setSingleCheckInDate(checkinDate);
			reportObject.setSingleCheckOutDate(checkoutDate);
			reportObject.setSingleTimeRec(timeRec);
			reportObject.setSingleVehicleStatus(status);
			reportObject.setSingleDriverStatus(drvStat);
			reportObject.setVehicleAssigned(vehicleNum);
			if (driver!=null && !"".equals(driver))
				reportObject.setDriversAssigned(driver);
			else
			{
				if(!"".equals(vehicleNum)){
					if (driverRequired != null && driverRequired.equals("0")) {
						reportObject.setDriversAssigned("Pandu Sendiri"); 
					} else {
						reportObject.setDriversAssigned("Not Tied");
					}
					reportObject.setSingleDriverStatus("2");
				}
			}
		}catch(Exception e){
			
		}
		
		return reportObject;
	}
	
	private ReportsTransportObject copyReport(ReportsTransportObject object){
		ReportsTransportObject rto = new ReportsTransportObject();
		rto = object;
		return rto;
	}
	
	public int countTransportRequestListing(String requestType,Date from, Date to, String programId){
		ReportsFmsDao dao=(ReportsFmsDao)getDao();
		try {
			return dao.countTransportRequestListing(requestType, from, to, programId);
		} catch (DaoException e) {
			return 0;
		}
	}
	
	public Collection getTransportDrivers(String transportRequestId){
		ReportsFmsDao dao=(ReportsFmsDao)getDao();
		try {
			return dao.getTransportDrivers(transportRequestId);
		} catch (DaoException e) {
			return null;
		}
	}
	
	public Collection getVehicle(String transportRequestId){
		ReportsFmsDao dao=(ReportsFmsDao)getDao();
		try {
			return dao.getVehicle(transportRequestId);
		} catch (DaoException e) {
			return null;
		}
	}
	
	public Map getSingleVehicle(String assignmentId, String vehicleNum){
		ReportsFmsDao dao=(ReportsFmsDao)getDao();
		try {
			return dao.getSingleVehicle(assignmentId,vehicleNum);
		} catch (DaoException e) {
			return null;
		}
	}
	
	public Collection getProgram(){
		ReportsFmsDao dao=(ReportsFmsDao)getDao();
		try {
			return dao.selectProgram();
		} catch (DaoException e) {
			return null;
		}
	}
	
	public Collection getRequestRepListing(Date bookingFrom, Date bookingTo, String programId, String requestType, String status, String sort, boolean desc, int startIndex,int maxRows){
		ReportsFmsDao dao=(ReportsFmsDao)getDao();
		try {
			return dao.getRequestRepListing(bookingFrom, bookingTo, programId, requestType, status, sort, desc, startIndex, maxRows);
		} catch (DaoException e) {
			return null;
		}
	}
	
	public int countRequestRepListing(Date bookingFrom, Date bookingTo, String programId, String requestType, String status){
		ReportsFmsDao dao=(ReportsFmsDao)getDao();
		try {
			return dao.countRequestRepListing(bookingFrom, bookingTo, programId, requestType, status);
		} catch (DaoException e) {
			return 0;
		}
	}
	
	public Double getTotalManpower(String requestId, String requestType){
		ReportsFmsDao dao=(ReportsFmsDao)getDao();
		
		try {
			return dao.getTotalManpower(requestId, requestType);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
			return null;
		}
	}
	
	public Double getTotalFacility(String requestId, String requestType){
		ReportsFmsDao dao=(ReportsFmsDao)getDao();
		
		try {
			return dao.getTotalFacility(requestId, requestType);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
			return null;
		}
	}
	
	public String getCancelCharge(String reqId){
		ReportsFmsDao dao=(ReportsFmsDao)getDao();
		return dao.selectCancelCharge(reqId);
	}
	
	public Double getTotalCost(String requestId, String requestType){
		ReportsFmsDao dao=(ReportsFmsDao)getDao();
		
		Double facility=null;
		try {
			facility = dao.getTotalFacility(requestId, requestType);
		} catch (NumberFormatException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		Double manpower=null;
		
		try {
			if(dao.getTotalManpower(requestId, requestType)!=null && !dao.getTotalManpower(requestId, requestType).equals("")){
				manpower = dao.getTotalManpower(requestId, requestType);
			}else{
				manpower = Double.parseDouble("0.00");
			}
			
		} catch (NumberFormatException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		if(facility==null){
			facility=0.00;
		}
		Double total=manpower+facility;
		return total;
	}
	
	public Collection getResourceUtil(String programType, Date fromDate, Date toDate, String programId, String serviceType, String status, 
			String requestStatus, String sort, boolean desc, int startIndex,int maxRows){
		ReportsFmsDao dao=(ReportsFmsDao)getDao();
		ArrayList args = new ArrayList();
		try {
			Collection col = dao.getResourceUtil(programType, fromDate, toDate, programId, serviceType, status, requestStatus, sort, desc, startIndex, maxRows);
			for(Iterator iter = col.iterator(); iter.hasNext(); ){
				ReportsObject report = (ReportsObject) iter.next();
				if(report.getProgram()==null || report.getProgram().equals("")){
					report.setProgram("-");
				}
				args.add(report);
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		return args;
	}
	
	public int countResourceUtil(String programType, Date fromDate, Date toDate, String programId, String serviceType, String status, String requesStatus){
		ReportsFmsDao dao=(ReportsFmsDao)getDao();
		try {
			return dao.countResourceUtil(programType, fromDate, toDate, programId, serviceType, status, requesStatus);
		} catch (DaoException e) {
			return 0;
		}
	}
	
	public String getBlockBooking(String requestId, String serviceType, String facilityId){  
		ReportsFmsDao dao=(ReportsFmsDao)getDao();
		try {
			return dao.getBlockBooking(requestId, serviceType, facilityId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return "";
		}
	}
	
	public boolean getAssignedStatus(String requestId){
		ReportsFmsDao dao=(ReportsFmsDao)getDao();
		try {
			return dao.getAssignedStatus(requestId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return false;
		}
	}
	
	public boolean assignedVehicle(String requestId){
		ReportsFmsDao dao=(ReportsFmsDao)getDao();
		try {
			return dao.assignedVehicle(requestId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return false;
		}
	}
	
	public void generateReport(String requestId){
		
		EngineeringModule module= (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);		
		String userId=Application.getInstance().getCurrentUser().getId();		
		EngineeringRequest request = new EngineeringRequest();
				
		try{
			request = module.getRequestWithService(requestId);
			Collection services=request.getServices();
			for(Iterator<Service> itr=services.iterator();itr.hasNext();){
				Service service=(Service)itr.next();
				String serviceId=service.getServiceId();
				if(ServiceDetailsForm.SERVICE_SCPMCP.equals(serviceId)){
					Collection col=module.getScpService(requestId, serviceId);
					for(Iterator<ScpService> serItr=col.iterator();serItr.hasNext();){
						ScpService scp=(ScpService)serItr.next();
						addReportResources(serviceId, requestId, scp.getFacilityId(), scp.getRequiredFrom(),scp.getRequiredTo(),scp.getDepartureTime(),scp.getRecordingTo(), scp.getBlockBooking());
						
					}
				}else if(ServiceDetailsForm.SERVICE_POSTPRODUCTION.equals(serviceId)){
					Collection col=module.getPostProductionService(requestId, serviceId);
					for(Iterator<PostProductionService> serItr=col.iterator();serItr.hasNext();){
						PostProductionService post=(PostProductionService)serItr.next();
						addReportResources(serviceId, requestId, post.getFacilityId(), post.getRequiredDate(),post.getRequiredDateTo(),post.getFromTime(),post.getToTime(),post.getBlockBooking());
						
					}
				}else if(ServiceDetailsForm.SERVICE_VTR.equals(serviceId)){
					Collection col=module.getVtrService(requestId, serviceId);
					for(Iterator<VtrService> serItr=col.iterator();serItr.hasNext();){
						VtrService vtr=(VtrService)serItr.next();
						addReportResources(serviceId, requestId, vtr.getFacilityId(), vtr.getRequiredDate(),vtr.getRequiredDateTo(),vtr.getRequiredFrom(),vtr.getRequiredTo(),vtr.getBlockBooking());
					
					}
				}else if(ServiceDetailsForm.SERVICE_MANPOWER.equals(serviceId)){
					Collection col=module.getManpowerService(requestId, serviceId);
					for(Iterator<ManpowerService> serItr=col.iterator();serItr.hasNext();){
						ManpowerService man=(ManpowerService)serItr.next();
						addReportResources(serviceId, requestId, man.getCompetencyId(), man.getRequiredFrom(),man.getRequiredTo(),man.getFromTime(),man.getToTime(),man.getBlockBooking());
					
					}
				}else if(ServiceDetailsForm.SERVICE_STUDIO.equals(serviceId)){
					Collection col=module.getStudioService(requestId, serviceId);
					for(Iterator<StudioService> serItr=col.iterator();serItr.hasNext();){
						StudioService studio=(StudioService)serItr.next();
						addReportResources(serviceId, requestId, studio.getFacilityId(), studio.getBookingDate(),studio.getBookingDateTo(),studio.getRequiredFrom(),studio.getRequiredTo(),studio.getBlockBooking());
					
					}
				}else if(ServiceDetailsForm.SERVICE_OTHER.equals(serviceId)){
					Collection col=module.getOtherService(requestId, serviceId);
					for(Iterator<OtherService> serItr=col.iterator();serItr.hasNext();){
						OtherService other=(OtherService)serItr.next();
						addReportResources(serviceId, requestId, other.getFacilityId(), other.getRequiredFrom(),other.getRequiredTo(),other.getFromTime(),other.getToTime(), other.getBlockBooking());
					
					}
				}else if(ServiceDetailsForm.SERVICE_TVRO.equals(serviceId)) {
					Collection col=module.getTvroService(requestId, serviceId);
					for(Iterator<TvroService> serItr=col.iterator();serItr.hasNext();){
						TvroService tvro = (TvroService) serItr.next();
						addReportResources(serviceId, requestId, tvro.getFacilityId(), tvro.getRequiredDate(), tvro.getRequiredDateTo(),tvro.getFromTime(),tvro.getToTime(), tvro.getBlockBooking());
					
					}
				}
			}
		}catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}  
	
		////
	}
	
	public void addReportResources(String serviceId, String requestId, String facilityId, Date startDate,Date endDate,String fromTime,String toTime, String blockBooking){
		
		Calendar c1 = Calendar.getInstance();
		Application application = Application.getInstance();
		ReportsFmsModule report = (ReportsFmsModule)application.getModule(ReportsFmsModule.class);
		
		//add checking
		if(startDate != null && endDate != null){
			long diffdate = dateDiff4Assignment(startDate, endDate);      
			int hour = 0;
			for(int i = 0; i <= diffdate; i++){				
				ResourcesObject resource = new ResourcesObject();
				resource.setId(UuidGenerator.getInstance().getUuid());
				resource.setServiceId(serviceId);
				resource.setRequestId(requestId);
				resource.setFacilityId(facilityId);			
				c1.setTime(startDate);
				c1.add(Calendar.DATE, i);
				Date bookDate = c1.getTime();			
				resource.setBookDate(bookDate);
				
				if("1".equals(blockBooking))
					hour = calculateBlockBookingHour(fromTime, toTime);
				else{
					hour = calculateHour(bookDate, startDate, endDate, fromTime, toTime);
				}
					
				resource.setHour(hour);		
				resource.setMinutes(minutes);
				try {				
					report.insertFacilityReport(resource);
				} catch (Exception e) {
					Log.getLog(getClass()).error(e.getMessage(),e);
				}
			}			
		}
	}
	
	private int calculateHour(Date currentDate, Date startDate, Date endDate, String fromTime,String toTime){
		int hour = 0;
		minutes = 0;
		int startDiff = currentDate.compareTo(startDate);
		int endDiff = currentDate.compareTo(endDate);
		
		//Change 00:00 to 24:00
		if("00:00".equals(toTime)){
			toTime = "24:00";
		}
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm"); 
			Date s = sdf.parse(fromTime);
    		Date e = sdf.parse(toTime);
    		Date endTime = sdf.parse("24:00");
			
			if(startDiff == 0 && endDiff == 0){
				long hours = (e.getTime() - s.getTime())/60000;
	    		hour = (int)(hours/60);
	    		minutes = (int)hours%60;
				
			}else if(startDiff == 0){
				long hours = (endTime.getTime() - s.getTime())/60000;
	    		hour = (int)(hours/60);
				
			}else if(startDiff > 0 && endDiff < 0){
				hour = 24;
				
			}else if(endDiff == 0){
				hour = (int)(e.getTime()/60000)/60;
				minutes = (int)(e.getTime()/60000)%60;
			}
		}catch(Exception er){
			
		}
		
		return hour;
	}
	
	private int calculateBlockBookingHour(String fromTime,String toTime){
		
		int hour = 0;
		minutes = 0;
				
			DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM);
		    df.setTimeZone(TimeZone.getTimeZone("GMT"));			 
		    
		    //Change 00:00 to 24:00
			if("00:00".equals(toTime)){
				toTime = "24:00";
			}
		    try{
		    	SimpleDateFormat sdf = new SimpleDateFormat("hh:mm"); 
		    	try {
		    		Date s = sdf.parse(fromTime);
		    		Date e = sdf.parse(toTime);
		    		long hours = (e.getTime() - s.getTime())/60000;
		    		hour = (int)(hours/60);
		    		minutes = (int)(hours%60);
	    		} catch ( Exception e){
		    		Log.getLog(getClass()).error(e.toString(), e);
	    		}
		    				    	
		    }catch(Exception er){
		    	Log.getLog(getClass()).error(er);
		    }			    			   
		
		return hour;
	}
	
	public long dateDiff4Assignment(Date start, Date end){		 
		long diff = Math.round((end.getTime() - start.getTime()) / (DateDiffUtil.MS_IN_A_DAY));			
		return diff;
	}	
	
	public void insertFacilityReport(ResourcesObject resource){
		
		ReportsFmsDao dao=(ReportsFmsDao)getDao();
		try {
			dao.addFacilityReport(resource);
		} catch (DaoException e) {
			
		}		
	}
	
	public Collection getFacilityResource(String serviceId, String programId, String departmentId, Date start, Date end){
		ReportsFmsDao dao=(ReportsFmsDao)getDao();
		try {
			return dao.getFacilityResource(serviceId, programId, departmentId, start, end);
		} catch (DaoException e) {
			return null;
		}
	}
	
	public Collection getFacilityResourceDate(String serviceId, String programId, String departmentId, Date start, Date end){
		ReportsFmsDao dao=(ReportsFmsDao)getDao();
		try {
			return dao.getFacilityResourceDate(serviceId, programId, departmentId, start, end);
		} catch (DaoException e) {
			return null;
		}
	}
	
	public int getCountFacilityByDate(String serviceId, String facilityId, Date date){
		int total = 0;
		ReportsFmsDao dao=(ReportsFmsDao)getDao();
		try {
			total = dao.getCountFacilityByDate(serviceId, facilityId, date);
		} catch (DaoException e) {
			
		}
		
		return total;
	}
	
	public Collection getHoursRequestAverage(Date bookDate, String serviceId, String facilityId){
		
		ReportsFmsDao dao=(ReportsFmsDao)getDao();
		try {
			return dao.getHoursRequestAverage(bookDate, serviceId, facilityId);
		} catch (DaoException e) {
			return null;
		}
	}
	
	public Collection getVehicleNumByAssignmentId(String assigmentId){
		ReportsFmsDao dao=(ReportsFmsDao)getDao();
		try {
			return dao.getVehicleNumByAssignmentId(assigmentId);
		} catch (DaoException e) {
			return null;
		}
	}
	
	public String getStatusVehicle(String assignmentId, String vehicleNum){
		ReportsFmsDao dao=(ReportsFmsDao)getDao();
		try {
			return dao.getStatusVehicle(assignmentId, vehicleNum);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return "";
		}
	}
	
	public String getDriversStatus(String assignmentId){
		ReportsFmsDao dao=(ReportsFmsDao)getDao();
		try {
			return dao.getDriversStatus(assignmentId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return "";
		}
	}
	
	public String getDriversStatus(String assignmentId, String manpower){
		ReportsFmsDao dao=(ReportsFmsDao)getDao();
		try {
			return dao.getDriversStatus(assignmentId,manpower);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return "";
		}
	}
	
	public String getStatusVehicleByAssignmentId(String assignmentId, String requestId){
		ReportsFmsDao dao=(ReportsFmsDao)getDao();
		try {
			return dao.getStatusVehicleByAssignmentId(assignmentId, requestId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return "";
		}
	}
	
	public int countTransportRequestListingNew(String requestType,Date from, Date to, String programId){
		ReportsFmsDao dao=(ReportsFmsDao)getDao();
		try {
			return dao.countTransportRequestListingNew(requestType, from, to, programId);
		} catch (DaoException e) {
			return 0;
		}
	}
	
	public Collection getTransportRequestWithTotalListing(String requestType,Date from, Date to, String programId, String sort, boolean desc, int startIndex,int maxRows){
		ArrayList args = new ArrayList();
		ReportsFmsDao dao=(ReportsFmsDao)getDao();
		listCounter2=0;
		try {
			if(from != null && to != null){
			 Calendar cal = Calendar.getInstance();
	         cal.setTime(from);
	         cal.set(Calendar.HOUR_OF_DAY, 0);
	         cal.set(Calendar.MINUTE, 0);
	         cal.set(Calendar.SECOND, 0);
	         from = cal.getTime();
	         cal.setTime(to);
	         cal.set(Calendar.HOUR, 11);
	         cal.set(Calendar.MINUTE, 59);
	         cal.set(Calendar.SECOND, 59);
	         to = cal.getTime();
			}   
			Collection col = dao.getTransportRequestListing(requestType, from, to, programId, sort, desc, startIndex, maxRows);
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
			
			for (Iterator iter = col.iterator(); iter.hasNext();){
				ReportsTransportObject report = (ReportsTransportObject) iter.next();
				//ReportsTransportObject report2 = report;
				
				Date dateFromAssignment=null; 
			    Date dateToAssignment = null;
			    String dateFromStrAssignment="";
			    String dateToStrAssignment="";
			    long datediff=0;
			    
				try{
					dateFromAssignment=(Date) formatter.parse(report.getTranAssignSDate()!=null?report.getTranAssignSDate():"");
					//String tempo = dateFromAssignment.toString();
					dateToAssignment=(Date) formatter.parse(report.getTranAssignEDate()!=null?report.getTranAssignEDate():"");
					dateFromStrAssignment=sdf.format(dateFromAssignment);
					dateToStrAssignment=sdf.format(dateToAssignment);
				}
				catch(Exception e){
					
				}
				if(null!=dateFromAssignment){
					datediff = dateDiff4Assignment(dateFromAssignment,dateToAssignment);
					if(!dateFromStrAssignment.equals(dateToStrAssignment))
						datediff++;
				}
				
				Calendar cal2 = Calendar.getInstance();  
				if(null!=dateFromAssignment)
					cal2.setTime(dateFromAssignment);  
				
				if(report.getBlockBooking().equals("0")){

					if(datediff==0){
						if(null!=dateFromAssignment)
							report.setTransAssignmentDateStrMod(sdf2.format(cal2.getTime()));
						String[] numbers = dao.getVehicleNumber(report.getAssignmentId());
						
						if(numbers.length>1){
							for(int i=0;i<numbers.length;i++){
								try{
									ReportsTransportObject report3=(ReportsTransportObject)report.clone();
									
									//String driver = dao.getVehiclesDriver(report3.getAssignmentId(),numbers[i]);
									ReportsTransportObject tempReport = assignTransportDataReport(report3.getId(), 
											report3.getAssignmentId(),numbers[i],report3.getDriverRequired());
									
									report3.setSingleMeterStart(tempReport.getSingleMeterStart());
									report3.setSingleMeterEnd(tempReport.getSingleMeterEnd());
									report3.setSingleTotalMeter(tempReport.getSingleTotalMeter());
									report3.setSingleCheckInDate(tempReport.getSingleCheckInDate());
									report3.setSingleCheckOutDate(tempReport.getSingleCheckOutDate());
									report3.setSingleTimeRec(tempReport.getSingleTimeRec());
									report3.setSingleVehicleStatus(tempReport.getSingleVehicleStatus());
									report3.setSingleDriverStatus(tempReport.getSingleDriverStatus());
									report3.setVehicleAssigned(tempReport.getVehicleAssigned());
									if(report3.getTransStatus()!=null && !report3.getTransStatus().equals("") 
											&& report3.getStatus().equals(SetupModule.REJECTED_STATUS))
										report3.setDriversAssigned(Application.getInstance().getMessage("fms.report.status.batal","Batal"));
									else if (report3.getTransStatus()!=null && !report3.getTransStatus().equals("") 
											&& report3.getStatus().equals(SetupModule.OUTSOURCED_STATUS)){
										report3.setDriversAssigned(Application.getInstance().getMessage("fms.report.status.sewa","Sewa"));
										//report3.setVehicleAssigned(Application.getInstance().getMessage("fms.engineering.request.status.outsource","Outsource"));
									}
									else{
										if(report3.getSingleDriverStatus().equals(SetupModule.UNFULFILLED_STATUS))
											report3.setDriversAssigned("No.News");
										else
											report3.setDriversAssigned(tempReport.getDriversAssigned());
									}
									if(report3.getSingleVehicleStatus().equals(SetupModule.UNFULFILLED_STATUS)&&report3.getDriversAssigned().equals("Pandu Sendiri")){
										report3.setDriversAssigned("No.News");
										report3.setSingleDriverStatus(SetupModule.UNFULFILLED_STATUS);
									}
									
									args.add(report3);
									listCounter2++;
								}
								catch(Exception e){
									
								}
							}
						}else{
							//String driver = dao.getVehiclesDriver(report.getAssignmentId(),numbers[0]);
							ReportsTransportObject tempReport = assignTransportDataReport(report.getId(), 
									report.getAssignmentId(),numbers[0],report.getDriverRequired());
							
							report.setSingleMeterStart(tempReport.getSingleMeterStart());
							report.setSingleMeterEnd(tempReport.getSingleMeterEnd());
							report.setSingleTotalMeter(tempReport.getSingleTotalMeter());
							report.setSingleCheckInDate(tempReport.getSingleCheckInDate());
							report.setSingleCheckOutDate(tempReport.getSingleCheckOutDate());
							report.setSingleTimeRec(tempReport.getSingleTimeRec());
							report.setSingleVehicleStatus(tempReport.getSingleVehicleStatus());
							report.setSingleDriverStatus(tempReport.getSingleDriverStatus());
							report.setVehicleAssigned(tempReport.getVehicleAssigned());
							if(report.getTransStatus()!=null && !report.getTransStatus().equals("") 
									&& report.getStatus().equals(SetupModule.REJECTED_STATUS))
								report.setDriversAssigned(Application.getInstance().getMessage("fms.report.status.batal","Batal"));
							else if (report.getTransStatus()!=null && !report.getTransStatus().equals("") 
									&& report.getStatus().equals(SetupModule.OUTSOURCED_STATUS)){
								report.setDriversAssigned(Application.getInstance().getMessage("fms.report.status.sewa","Sewa"));
								//report.setVehicleAssigned(Application.getInstance().getMessage("fms.engineering.request.status.outsource","Outsource"));
							}
							else{
								if(report.getSingleDriverStatus().equals(SetupModule.UNFULFILLED_STATUS))
									report.setDriversAssigned("No.News");
								else
									report.setDriversAssigned(tempReport.getDriversAssigned());
							}
							if(report.getSingleVehicleStatus().equals(SetupModule.UNFULFILLED_STATUS)&&report.getDriversAssigned().equals("Pandu Sendiri")){
								report.setDriversAssigned("No.News");
								report.setSingleDriverStatus(SetupModule.UNFULFILLED_STATUS);
							}
							args.add(report);
							listCounter2++;
						}
							//args.add(report);
					}
					for(int cntr =0;cntr<datediff;cntr++){
						try{
							
							ReportsTransportObject report2=(ReportsTransportObject)report.clone();
							if(null!=dateFromAssignment)
								report2.setTransAssignmentDateStrMod(sdf2.format(cal2.getTime()));
							String[] numbers = dao.getVehicleNumber(report2.getAssignmentId());
							if(numbers.length>1){
								for(int i=0;i<numbers.length;i++){
									try{
										ReportsTransportObject report3=(ReportsTransportObject)report2.clone();
										//String driver = dao.getVehiclesDriver(report3.getAssignmentId(),numbers[i]);
										ReportsTransportObject tempReport = assignTransportDataReport(report3.getId(), 
												report3.getAssignmentId(),numbers[i],report3.getDriverRequired());
										
										report3.setSingleMeterStart(tempReport.getSingleMeterStart());
										report3.setSingleMeterEnd(tempReport.getSingleMeterEnd());
										report3.setSingleTotalMeter(tempReport.getSingleTotalMeter());
										report3.setSingleCheckInDate(tempReport.getSingleCheckInDate());
										report3.setSingleCheckOutDate(tempReport.getSingleCheckOutDate());
										report3.setSingleTimeRec(tempReport.getSingleTimeRec());
										report3.setSingleVehicleStatus(tempReport.getSingleVehicleStatus());
										report3.setSingleDriverStatus(tempReport.getSingleDriverStatus());
										report3.setVehicleAssigned(tempReport.getVehicleAssigned());
										if(report3.getTransStatus()!=null && !report3.getTransStatus().equals("") 
												&& report3.getStatus().equals(SetupModule.REJECTED_STATUS))
											report3.setDriversAssigned(Application.getInstance().getMessage("fms.report.status.batal","Batal"));
										else if (report3.getTransStatus()!=null && !report3.getTransStatus().equals("") 
												&& report3.getStatus().equals(SetupModule.OUTSOURCED_STATUS)){
											report3.setDriversAssigned(Application.getInstance().getMessage("fms.report.status.sewa","Sewa"));
											//report3.setVehicleAssigned(Application.getInstance().getMessage("fms.engineering.request.status.outsource","Outsource"));
										}
										else{
											if(report3.getSingleDriverStatus().equals(SetupModule.UNFULFILLED_STATUS))
												report3.setDriversAssigned("No.News");
											else
												report3.setDriversAssigned(tempReport.getDriversAssigned());
										}
										if(report3.getSingleVehicleStatus().equals(SetupModule.UNFULFILLED_STATUS)&&report3.getDriversAssigned().equals("Pandu Sendiri")){
											report3.setDriversAssigned("No.News");
											report3.setSingleDriverStatus(SetupModule.UNFULFILLED_STATUS);
										}
										args.add(report3);
										listCounter2++;
									}
									catch(Exception e){
										
									}
								}
							}else
							{
								//String driver = dao.getVehiclesDriver(report2.getAssignmentId(),numbers[0]);
								ReportsTransportObject tempReport = assignTransportDataReport(report2.getId(), 
										report2.getAssignmentId(),numbers[0],report2.getDriverRequired());
								
								report2.setSingleMeterStart(tempReport.getSingleMeterStart());
								report2.setSingleMeterEnd(tempReport.getSingleMeterEnd());
								report2.setSingleTotalMeter(tempReport.getSingleTotalMeter());
								report2.setSingleCheckInDate(tempReport.getSingleCheckInDate());
								report2.setSingleCheckOutDate(tempReport.getSingleCheckOutDate());
								report2.setSingleTimeRec(tempReport.getSingleTimeRec());
								report2.setSingleVehicleStatus(tempReport.getSingleVehicleStatus());
								report2.setSingleDriverStatus(tempReport.getSingleDriverStatus());
								report2.setVehicleAssigned(tempReport.getVehicleAssigned());
								if(report2.getTransStatus()!=null && !report2.getTransStatus().equals("") 
										&& report2.getStatus().equals(SetupModule.REJECTED_STATUS))
									report2.setDriversAssigned(Application.getInstance().getMessage("fms.report.status.batal","Batal"));
								else if (report2.getTransStatus()!=null && !report2.getTransStatus().equals("") 
										&& report2.getStatus().equals(SetupModule.OUTSOURCED_STATUS)){
									report2.setDriversAssigned(Application.getInstance().getMessage("fms.report.status.sewa","Sewa"));
									//report2.setVehicleAssigned(Application.getInstance().getMessage("fms.engineering.request.status.outsource","Outsource"));
								}
								else{
									if(report2.getSingleDriverStatus().equals(SetupModule.UNFULFILLED_STATUS))
										report2.setDriversAssigned("No.News");
									else
										report2.setDriversAssigned(tempReport.getDriversAssigned());
								}
								if(report2.getSingleVehicleStatus().equals(SetupModule.UNFULFILLED_STATUS)&&report2.getDriversAssigned().equals("Pandu Sendiri")){
									report2.setDriversAssigned("No.News");
									report2.setSingleDriverStatus(SetupModule.UNFULFILLED_STATUS);
								}
								args.add(report2);
								listCounter2++;
							}
							//args.add(report2);
							if(null!=dateFromAssignment){
							cal2.add(Calendar.DAY_OF_YEAR, 1); // <--  
							cal2.getTime(); }
						}catch(Exception e){
							
						}
					}
				}else{
					if((report.getId()==null && report.getId().equals(""))&& (report.getAssignmentId()!= null && !report.getAssignmentId().equals(""))){
						report.setId(report.getAssignmentId());
					}
					if(null!=dateFromAssignment)
					report.setTransAssignmentDateStrMod(sdf2.format(cal2.getTime()));
					String[] numbers = dao.getVehicleNumber(report.getAssignmentId());
					if(numbers.length>1){
						for(int i=0;i<numbers.length;i++){
							try{
								ReportsTransportObject report3=(ReportsTransportObject)report.clone();
								//String driver = dao.getVehiclesDriver(report3.getAssignmentId(),numbers[i]);
								ReportsTransportObject tempReport = assignTransportDataReport(report3.getId(), 
										report3.getAssignmentId(),numbers[i],report3.getDriverRequired());
								
								report3.setSingleMeterStart(tempReport.getSingleMeterStart());
								report3.setSingleMeterEnd(tempReport.getSingleMeterEnd());
								report3.setSingleTotalMeter(tempReport.getSingleTotalMeter());
								report3.setSingleCheckInDate(tempReport.getSingleCheckInDate());
								report3.setSingleCheckOutDate(tempReport.getSingleCheckOutDate());
								report3.setSingleTimeRec(tempReport.getSingleTimeRec());
								report3.setSingleVehicleStatus(tempReport.getSingleVehicleStatus());
								report3.setSingleDriverStatus(tempReport.getSingleDriverStatus());
								report3.setVehicleAssigned(tempReport.getVehicleAssigned());
								if(report3.getTransStatus()!=null && !report3.getTransStatus().equals("") 
										&& report3.getStatus().equals(SetupModule.REJECTED_STATUS))
									report3.setDriversAssigned(Application.getInstance().getMessage("fms.report.status.batal","Batal"));
								else if (report3.getTransStatus()!=null && !report3.getTransStatus().equals("") 
										&& report3.getStatus().equals(SetupModule.OUTSOURCED_STATUS)){
									report3.setDriversAssigned(Application.getInstance().getMessage("fms.report.status.sewa","Sewa"));
									//report3.setVehicleAssigned(Application.getInstance().getMessage("fms.engineering.request.status.outsource","Outsource"));
								}
								else{
									if(report3.getSingleDriverStatus().equals(SetupModule.UNFULFILLED_STATUS))
										report3.setDriversAssigned("No.News");
									else
										report3.setDriversAssigned(tempReport.getDriversAssigned());
								}
								if(report3.getSingleVehicleStatus().equals(SetupModule.UNFULFILLED_STATUS)&&report3.getDriversAssigned().equals("Pandu Sendiri")){
									report3.setDriversAssigned("No.News");
									report3.setSingleDriverStatus(SetupModule.UNFULFILLED_STATUS);
								}
								args.add(report3);
								listCounter2++;
							}
							catch(Exception e){
								
							}
						}
					}else{
						//String driver = dao.getVehiclesDriver(report.getAssignmentId(),numbers[0]);
						ReportsTransportObject tempReport = assignTransportDataReport(report.getId(), 
								report.getAssignmentId(),numbers[0],report.getDriverRequired());
						
						report.setSingleMeterStart(tempReport.getSingleMeterStart());
						report.setSingleMeterEnd(tempReport.getSingleMeterEnd());
						report.setSingleTotalMeter(tempReport.getSingleTotalMeter());
						report.setSingleCheckInDate(tempReport.getSingleCheckInDate());
						report.setSingleCheckOutDate(tempReport.getSingleCheckOutDate());
						report.setSingleTimeRec(tempReport.getSingleTimeRec());
						report.setSingleVehicleStatus(tempReport.getSingleVehicleStatus());
						report.setSingleDriverStatus(tempReport.getSingleDriverStatus());
						report.setVehicleAssigned(tempReport.getVehicleAssigned());
						if(report.getTransStatus()!=null && !report.getTransStatus().equals("") 
								&& report.getStatus().equals(SetupModule.REJECTED_STATUS))
							report.setDriversAssigned(Application.getInstance().getMessage("fms.report.status.batal","Batal"));
						else if (report.getTransStatus()!=null && !report.getTransStatus().equals("") 
								&& report.getStatus().equals(SetupModule.OUTSOURCED_STATUS)){
							report.setDriversAssigned(Application.getInstance().getMessage("fms.report.status.sewa","Sewa"));
							//report.setVehicleAssigned(Application.getInstance().getMessage("fms.engineering.request.status.outsource","Outsource"));
						}
						else{
							if(report.getSingleDriverStatus().equals(SetupModule.UNFULFILLED_STATUS))
								report.setDriversAssigned("No.News");
							else
								report.setDriversAssigned(tempReport.getDriversAssigned());
						}
						if(report.getSingleVehicleStatus().equals(SetupModule.UNFULFILLED_STATUS)&&report.getDriversAssigned().equals("Pandu Sendiri")){
							report.setDriversAssigned("No.News");
							report.setSingleDriverStatus(SetupModule.UNFULFILLED_STATUS);
						}
						args.add(report);
						listCounter2++;
					}
				}
				
			}
			if(args != null && args.size() > 0){
				for(Iterator i = args.iterator(); i.hasNext();){
					ReportsTransportObject object = (ReportsTransportObject) i.next();
					String[] requestedVehAndDriv = getRequestedVehiclesAndDrivers(object.getId());
					if(requestedVehAndDriv != null && requestedVehAndDriv.length == 2){
						object.setRequestedVehicles(requestedVehAndDriv[0]);
						object.setRequestedDrivers(requestedVehAndDriv[1]);
						try{
							Date tempDate = sdf2.parse(object.getTransAssignmentDateStrMod());
							object.setTransAssignmentDateStrMod(sdf.format(tempDate));
							
							TransportModule tModule = (TransportModule) Application.getInstance().getModule(TransportModule.class);
							SimpleDateFormat sdf4 = new SimpleDateFormat("HH:mm");
							SimpleDateFormat sdf5 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
							String fromTime = sdf4.format(formatter.parse(object.getTranAssignSDate()));
							String toTime = sdf4.format(formatter.parse(object.getTranAssignEDate()));
							Date startDate = formatter.parse(object.getTranAssignSDate());
							Date endDate = formatter.parse(object.getTranAssignEDate());
							
							Date dateTimeFrom = sdf5.parse(object.getTransAssignmentDateStrMod() + " " + fromTime);
							Date dateTimeTo = sdf5.parse(object.getTransAssignmentDateStrMod() + " " + toTime);
							
							if(dateTimeFrom.compareTo(startDate) > 0){
								dateTimeFrom.setHours(0);
								dateTimeFrom.setMinutes(0);
								fromTime = "00:00";
							}
							
							if(!(dateTimeTo.compareTo(dateTimeFrom) > 0)){
								Calendar calendar = Calendar.getInstance();
								calendar.setTime(dateTimeTo);
								long tempTime = calendar.getTimeInMillis();
								tempTime += 24 * 60 * 60 * 1000;
								calendar.setTimeInMillis(tempTime);
								dateTimeTo = calendar.getTime();
							}
							
							if(dateTimeTo.compareTo(endDate) < 0){
								dateTimeTo.setHours(0);
								dateTimeTo.setMinutes(0);
								toTime = "00:00";
							}
							
							String rateDriver = tModule.getRateDriver(object.getId(), dateTimeFrom, 
									dateTimeTo, fromTime, toTime, object.getBlockBooking());
							String rateVehicle = tModule.getRateFacility(object.getId(), dateTimeFrom, 
									dateTimeTo, fromTime, toTime, object.getBlockBooking());
							double dailyTotalCost = Double.parseDouble(rateDriver) + Double.parseDouble(rateVehicle);
							DecimalFormat df = new DecimalFormat("#.##");
							object.setTotalCostDaily(df.format(dailyTotalCost));
						}
						catch (ParseException e) {
							// TODO: handle exception
							Log.getLog(getClass()).error(e, e);
						}
					}
				}
			}
			return args;
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e, e);
			return null;
		}
	}
	
	public String[] getRequestedVehiclesAndDrivers(String requestId){
		try{
			ReportsFmsDao dao = (ReportsFmsDao) getDao();
			return dao.getRequestedVehiclesAndDrivers(requestId);
		}
		catch(DaoException e){
			Log.getLog(getClass()).error(e, e);
			return null;
		}
	}
	
}
