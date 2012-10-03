package com.tms.fms.reports.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
 
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.setup.model.SetupModule;
import com.tms.fms.transport.model.TransportModule;
import com.tms.report.model.ReportModule;

import kacang.Application;
import kacang.model.DefaultDataObject;
import kacang.services.security.SecurityService;
import kacang.util.Log;

public class ReportsTransportObject extends DefaultDataObject implements Cloneable{

	public String id;
	public String requestTitle;
	public String requestType;
	public String program; 
	public String startDate;
	public String endDate;
	public String destination;
	public String purpose;
	public String remarks;
	public String status; 
	public String reason; 
	public String requestBy; 
	public Date requestDate;
	public String updatedBy;
	public Date updatedDate; 
	public String approvedBy;
	public Date approvedDate;
	public String statusRequest;
	public String rate; 
	public String engineeringRequestId;
	public String blockBooking;
	public String fullname;
	public String department;
	public String requestDateStr;
	public String driversAssigned;
	public String vehicleAssigned;
	public String meterStart;
	public String meterEnd;
	public String totalMeter;
	public String singleMeterStart;
	public String singleMeterEnd;
	public String singleTotalMeter;
	public String singleCheckInDate;
	public String singleCheckOutDate;
	public String singleTimeRec;
	public String checkInDate;
	public String checkOutDate;
	public String timeRec;
	public String outsourceFlag, outsourceId;
	public String driverStatus;
	public String vehicleStatus;
	public String singleDriverStatus;
	public String singleVehicleStatus;
	public String reqAsssignId;
	
	
	public String requestId, title, programType, createdBy, engManpowerBudget, facilitiesBudget, totalcost, bookFrom, bookTo;
	
	public String assignmentId, fromTime, toTime, serviceType, facilityEquip, rateCardId;
	public int duration;
	
	public String totalDuration;
	
	public String transAssignmentDateStr, transAssignmentDateStrMod, transStatus, firstname, lastname, driverRequired, tranAssignSDate, tranAssignEDate;
	
	private String programName;
	private String requestedVehicles;
	private String requestedDrivers;
	private String totalCostDaily;
	private String pfeCode;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRequestTitle() {
		return requestTitle;
	}

	public void setRequestTitle(String requestTitle) {
		this.requestTitle = requestTitle;
	}

	public String getRequestType() {
		
		if(requestType!=null && requestType.equals(EngineeringModule.REQUEST_TYPE_INTERNAL)){
			return Application.getInstance().getMessage("fms.facility.requestType.I");
		}else if(requestType!=null && requestType.equals(EngineeringModule.REQUEST_TYPE_EXTERNAL)){
			return Application.getInstance().getMessage("fms.facility.requestType.E");
		}else if(requestType!=null && requestType.equals(EngineeringModule.REQUEST_TYPE_NONPROGRAM)){
			return Application.getInstance().getMessage("fms.facility.requestType.N");
		}
		return "-";
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getProgram() {
		if(!program.equals("-")){
			return Application.getInstance().getMessage("fms.tran.requestProgram");
		}else{
			return Application.getInstance().getMessage("fms.tran.requestNonProgram");
		}
	}

	public void setProgram(String program) {
		this.program = program;
	}

	
	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getPurpose() {
//		return purpose!=null&&!purpose.equals("")?purpose:"-";
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getRequestBy() {
		return requestBy;
	}

	public void setRequestBy(String requestBy) {
		this.requestBy = requestBy;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public String getStatusRequest() {
		return statusRequest;
	}

	public void setStatusRequest(String statusRequest) {
		this.statusRequest = statusRequest;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getEngineeringRequestId() {
		return engineeringRequestId;
	}

	public void setEngineeringRequestId(String engineeringRequestId) {
		this.engineeringRequestId = engineeringRequestId;
	}

	public String getBlockBooking() {
		return blockBooking;
	}

	public void setBlockBooking(String blockBooking) {
		this.blockBooking = blockBooking;
	}

	public String getFullname() {
		return getFirstname()+ " " +getLastname();
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getRequestDateStr() {
		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
//		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try{
			Date dateFrom  = (Date) formatter.parse(getStartDate());
			
		    Date dateTo = dateTo = (Date) formatter.parse(getEndDate());
			
			
			String dateFromStr=sdf.format(dateFrom);
			String dateToStr=sdf.format(dateTo);
	
//			return "<table border=\"0\"><tr><td>"+dateFromStr + "</td></tr><tr><td align=\"center\">-</td></tr><tr><td> " + dateToStr+"</td></tr></table>";
			return "<table border=\"1\" width=\"160\"><tr><td align=\"center\">"+dateFromStr + " - " + dateToStr+"</td></tr></table>";
		} catch (ParseException e) {
			Log.getLog(getClass()).error(e);
			return "-";
		}
	}
	
public String getRequestDateStr0() {
		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
//		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try{
			Date dateFrom  = (Date) formatter.parse(getStartDate());
			
		    Date dateTo = dateTo = (Date) formatter.parse(getEndDate());
			
			
			String dateFromStr=sdf.format(dateFrom);
			String dateToStr=sdf.format(dateTo);
	
//			return "<table border=\"0\"><tr><td>"+dateFromStr + "</td></tr><tr><td align=\"center\">-</td></tr><tr><td> " + dateToStr+"</td></tr></table>";
			return "<table border=\"0\" width=\"160\"><tr><td align=\"center\">"+dateFromStr + " - " + dateToStr+"</td></tr></table>";
		} catch (ParseException e) {
			Log.getLog(getClass()).error(e);
			return "-";
		}
	}

	public void setRequestDateStr(String requestDateStr) {
		this.requestDateStr = requestDateStr;
	}

	public String getDriversAssigned() {
		return driversAssigned;
	}
//	public String getDriversAssigned() {
//		if(getStatus().equals(SetupModule.REJECTED_STATUS)){
//			return Application.getInstance().getMessage("fms.report.status.batal","Batal");
//		}
//		
//		TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);
//		ReportsFmsModule module = (ReportsFmsModule)Application.getInstance().getModule(ReportsFmsModule.class);
//		Collection list = module.getTransportDrivers(getAssignmentId());
//		String drivers="";
//		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
//			Map map = (Map) iterator.next();
//			SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);		
//			String status=map.get("status")!=null && !map.get("status").toString().equals("")?map.get("status").toString():"";
//			String status2="";
//			
//			if(!status.equals("")){
//				status2 = "("+TM.selectStatus(status)+")";
//			}
//			String username = "";
//			try{						
//				username = service.getUser(map.get("manpowerId").toString()).getName();		
//				
//				
//			}catch(Exception e){
//				
//			}		    		
//			drivers+= username;
//			
//			if(list.size()>1){
//				drivers+=status2+",<br>";
//			}
//			
//		}
//		if(!drivers.equals("")){
//			return drivers;	
//		}else{
//			if(module.assignedVehicle(getId()) && getDriverRequired().equals("0")){
//				return Application.getInstance().getMessage("fms.report.status.pandusendiri","Pandu Sendiri");
//			}
//			
//		}
//		if(getOutsourceId()!=null && !getOutsourceId().equals("")){
//			return Application.getInstance().getMessage("fms.report.status.sewa","Sewa");
//		}
//		return "";
//	}

	public void setDriversAssigned(String driversAssigned) {
		this.driversAssigned = driversAssigned;
	}
	
	public String getVehicleAssigned() {
		return vehicleAssigned;
	}
//	public String getVehicleAssigned() {
//		TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);
//		ReportsFmsModule module = (ReportsFmsModule)Application.getInstance().getModule(ReportsFmsModule.class);
//		Collection vehicleDetails = module.getVehicleNumByAssignmentId(getAssignmentId());
//		String vehicleAssigned="";
//		if(vehicleDetails!=null && vehicleDetails.size()>0){
//			for (Iterator iterator = vehicleDetails.iterator(); iterator.hasNext();) {
//				Map map = (Map) iterator.next();		    		
//				//vehicleAssigned.replaceAll("-", "");
//				if(map.get("vehicle_num")!=null && !map.get("vehicle_num").toString().equals("")){
//					String status=module.getStatusVehicle(getAssignmentId(), map.get("vehicle_num").toString());
//					String status2="";
//					
//					if(!status.equals("")){
//						status2 = "("+TM.selectStatus(status)+")";
//					}
//					vehicleAssigned+= map.get("vehicle_num").toString();
//					if(vehicleDetails.size()>1){
//						vehicleAssigned+=status2+",<br>";
//					}
//					vehicleAssigned+="";
//				}else{
//					vehicleAssigned="";
//				}
//			}
//			
//		}
//		
//		if(getOutsourceId()!=null && !getOutsourceId().equals("")){
//			return Application.getInstance().getMessage("fms.report.status.sewa","Sewa");
//		}
//		//return !vehicleAssigned.equals("")?vehicleAssigned:"-";
//		return vehicleAssigned;
//	}

	public void setVehicleAssigned(String vehicleAssigned) {
		this.vehicleAssigned = vehicleAssigned;
	}

	public String getMeterStart() {
		ReportsFmsModule module = (ReportsFmsModule)Application.getInstance().getModule(ReportsFmsModule.class);
		Collection vehicleDetails = module.getVehicle(getAssignmentId());
		String meterStart="";
		if(vehicleDetails!=null && vehicleDetails.size()>0){
			for (Iterator iterator = vehicleDetails.iterator(); iterator.hasNext();) {
				Map map = (Map) iterator.next();		    		
				
				if(map.get("meterStart")!=null && !map.get("meterStart").toString().equals("")){
					meterStart+= map.get("meterStart").toString();
					if(vehicleDetails.size()>1){
						meterStart= meterStart+=",<br>";
					}
				}
			}
			
		}
		//return !meterStart.equals("")?meterStart:"-";
		return meterStart;
	}

	public void setMeterStart(String meterStart) {
		this.meterStart = meterStart;
	}

	public String getMeterEnd() {
		ReportsFmsModule module = (ReportsFmsModule)Application.getInstance().getModule(ReportsFmsModule.class);
		Collection vehicleDetails = module.getVehicle(getAssignmentId());
		String meterEnd="";
		if(vehicleDetails!=null && vehicleDetails.size()>0){
			for (Iterator iterator = vehicleDetails.iterator(); iterator.hasNext();) {
				Map map = (Map) iterator.next();		    		
				
				if(map.get("meterEnd")!=null && !map.get("meterEnd").toString().equals("")){
					meterEnd+= map.get("meterEnd").toString();
					if(vehicleDetails.size()>1){
						meterEnd+=",<br>";
					}
				}
			}
			
		}
//		return !meterEnd.equals("")?meterEnd:"-";
		return meterEnd;
	}

	public void setMeterEnd(String meterEnd) {
		this.meterEnd = meterEnd;
	}

	public String getTotalMeter() {
		ReportsFmsModule module = (ReportsFmsModule)Application.getInstance().getModule(ReportsFmsModule.class);
		Collection vehicleDetails = module.getVehicle(getAssignmentId());
		String totalmeter="";
		if(vehicleDetails!=null && vehicleDetails.size()>0){
			for (Iterator iterator = vehicleDetails.iterator(); iterator.hasNext();) {
				Map map = (Map) iterator.next();		    		
				
				if((map.get("meterStart")!=null && !map.get("meterStart").toString().equals("")) 
						|| (map.get("meterEnd")!=null && !map.get("meterEnd").toString().equals(""))){
					int total=Integer.parseInt(map.get("meterEnd").toString()) - Integer.parseInt(map.get("meterStart").toString());
					totalmeter+= String.valueOf(total);
					if(vehicleDetails.size()>1){
						totalmeter+=",<br>";
					}
					
				}
			}
			
		}
//		return !totalmeter.equals("")?totalmeter:"-";
		return totalmeter;
	}

	public void setTotalMeter(String totalMeter) {
		this.totalMeter = totalMeter;
	}

	public String getCheckInDate() {
		ReportsFmsModule module = (ReportsFmsModule)Application.getInstance().getModule(ReportsFmsModule.class);
		Collection vehicleDetails = module.getVehicle(getAssignmentId());
		String checkInDate="";
		if(vehicleDetails!=null && vehicleDetails.size()>0){
			for (Iterator iterator = vehicleDetails.iterator(); iterator.hasNext();) {
				Map map = (Map) iterator.next();		    		
				
				if (map.get("checkin_date") != null && !map.get("checkin_date").toString().equals("")) {

					try {
						DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
						Date date = (Date) formatter.parse(map.get("checkin_date").toString());
						
//						SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
						String dateStr=sdf.format(date);
						
						checkInDate += dateStr;
						if (vehicleDetails.size() > 1) {
							checkInDate += ",<br>";
						}

					} catch (ParseException e) {
						Log.getLog(getClass()).error(e);
					}
				}
			}
			
		}
//		return !checkInDate.equals("")?checkInDate:"-";
		return checkInDate;
	}

	public void setCheckInDate(String checkInDate) {
		this.checkInDate = checkInDate;
	}

	public String getCheckOutDate() {
		ReportsFmsModule module = (ReportsFmsModule)Application.getInstance().getModule(ReportsFmsModule.class);
		Collection vehicleDetails = module.getVehicle(getAssignmentId());
		String checkOutDate="";
		if(vehicleDetails!=null && vehicleDetails.size()>0){
			for (Iterator iterator = vehicleDetails.iterator(); iterator.hasNext();) {
				Map map = (Map) iterator.next();		    		
				
				if (map.get("checkout_date") != null && !map.get("checkout_date").toString().equals("")) {

					try {
						DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
						Date date = (Date) formatter.parse(map.get("checkout_date").toString());
						
//						SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
						String dateStr=sdf.format(date);
						
						checkOutDate += dateStr;
						if (vehicleDetails.size() > 1) {
							checkOutDate += ",<br>";
						}

					} catch (ParseException e) {
						Log.getLog(getClass()).error(e);
					}
				}
			}
			
		}
//		return !checkOutDate.equals("")?checkOutDate:"-";
		return checkOutDate;
	}

	public void setCheckOutDate(String checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public String getTimeRec() {
		ReportsFmsModule module = (ReportsFmsModule)Application.getInstance().getModule(ReportsFmsModule.class);
		Collection vehicleDetails = module.getVehicle(getAssignmentId());
		String timeRec="";
		if(vehicleDetails!=null && vehicleDetails.size()>0){
			for (Iterator iterator = vehicleDetails.iterator(); iterator.hasNext();) {
				Map map = (Map) iterator.next();		    		
				
				if(map.get("timeRec")!=null && !map.get("timeRec").toString().equals("")){
					timeRec=map.get("timeRec").toString();
					if (vehicleDetails.size() > 1) {
						timeRec+=",<br>";
					}
				}
			}
			
		}
//		return !timeRec.equals("")?timeRec:"-";
		return timeRec;
	}

	public void setTimeRec(String timeRec) {
		this.timeRec = timeRec;
	}

	public String getOutsourceFlag() {
//		return getOutsourceId()!=null && !getOutsourceId().equals("") && !getOutsourceId().equals("NULL")?"Yes":"-";
		return getOutsourceId()!=null && !getOutsourceId().equals("") && !getOutsourceId().equals("NULL")?"Yes":"";
	}

	public void setOutsourceFlag(String outsourceFlag) {
		this.outsourceFlag = outsourceFlag;
	}

	public String getDriverStatus() {
		ReportsFmsModule module = (ReportsFmsModule)Application.getInstance().getModule(ReportsFmsModule.class);
		if(getOutsourceId()!=null && !getOutsourceId().equals("")){
			return SetupModule.OUTSOURCED_STATUS;
		}
		return module.getDriversStatus(getAssignmentId());
	}
	
	/*public String getDriverStatus() {
		ReportsFmsModule module = (ReportsFmsModule)Application.getInstance().getModule(ReportsFmsModule.class);
		Collection list = module.getTransportDrivers(getAssignmentId());
		String drivers="";
		if(getOutsourceId()!=null && !getOutsourceId().equals("")){
			return SetupModule.OUTSOURCED_STATUS;
		}
		String status="";
		if(list!=null && list.size()>0){
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map map = (Map) iterator.next();
				status=map.get("status")!=null && !map.get("status").equals("")?map.get("status").toString():"1";
			}
		}else{
			status= "0";
		}
		return status;
	}*/

	public void setDriverStatus(String driverStatus) {
		this.driverStatus = driverStatus;
	}

	public String getVehicleStatus() {
		ReportsFmsModule module = (ReportsFmsModule)Application.getInstance().getModule(ReportsFmsModule.class);
		if(getOutsourceId()!=null && !getOutsourceId().equals("")){
			return SetupModule.OUTSOURCED_STATUS;
		}
		return module.getStatusVehicleByAssignmentId(getAssignmentId(),getId());
	}
	
	
	/*public String getVehicleStatus() {
		ReportsFmsModule module = (ReportsFmsModule)Application.getInstance().getModule(ReportsFmsModule.class);
		Collection vehicleDetails = module.getVehicle(getAssignmentId());
		String checkOutDate="";
		if(module.assignedVehicle(getId())){
			if(vehicleDetails!=null && vehicleDetails.size()>0){
				for (Iterator iterator = vehicleDetails.iterator(); iterator.hasNext();) {
					Map map = (Map) iterator.next();		    		
					
					return map.get("status")!=null && !map.get("status").equals("")?map.get("status").toString():"0";
				}
				
			}
		}
		if(getOutsourceId()!=null && !getOutsourceId().equals("")){
			return SetupModule.OUTSOURCED_STATUS;
		} 
		return "0";
	}*/

	public void setVehicleStatus(String vehicleStatus) {
		this.vehicleStatus = vehicleStatus;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getProgramType() {
		return programType;
	}

	public void setProgramType(String programType) {
		this.programType = programType;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getEngManpowerBudget() {
		return engManpowerBudget;
	}

	public void setEngManpowerBudget(String engManpowerBudget) {
		this.engManpowerBudget = engManpowerBudget;
	}

	public String getFacilitiesBudget() {
		return facilitiesBudget;
	}

	public void setFacilitiesBudget(String facilitiesBudget) {
		this.facilitiesBudget = facilitiesBudget;
	}

	public String getTotalcost() {
		return totalcost;
	}

	public void setTotalcost(String totalcost) {
		this.totalcost = totalcost;
	}

	public String getBookFrom() {
		return bookFrom;
	}

	public void setBookFrom(String bookFrom) {
		this.bookFrom = bookFrom;
	}

	public String getBookTo() {
		return bookTo;
	}

	public void setBookTo(String bookTo) {
		this.bookTo = bookTo;
	}

	public String getAssignmentId() {
		return assignmentId!=null?assignmentId:"";
	}

	public void setAssignmentId(String assignmentId) {
		this.assignmentId = assignmentId;
	}

	public String getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	public String getToTime() {
		return toTime;
	}

	public void setToTime(String toTime) {
		this.toTime = toTime;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getFacilityEquip() {
		return facilityEquip;
	}

	public void setFacilityEquip(String facilityEquip) {
		this.facilityEquip = facilityEquip;
	}

	public String getRateCardId() {
		return rateCardId;
	}

	public void setRateCardId(String rateCardId) {
		this.rateCardId = rateCardId;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getTotalDuration() {
		return totalDuration;
	}

	public void setTotalDuration(String totalDuration) {
		this.totalDuration = totalDuration;
	}

	public String getTransAssignmentDateStr() {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
//		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date dateFromAssignment = (Date) formatter.parse(getTranAssignSDate()!=null?getTranAssignSDate():"");
			
		    Date dateToAssignment = (Date) formatter.parse(getTranAssignEDate()!=null?getTranAssignEDate():"");
		    String dateFromStrAssignment=sdf.format(dateFromAssignment);
			String dateToStrAssignment=sdf.format(dateToAssignment);
		    
//		    return "<table border=\"0\"><tr><td>"+dateFromStrAssignment + "</td></tr><tr><td align=\"center\">-</td></tr><tr><td> " + dateToStrAssignment+"</td></tr></table>";
			return "<table border=\"0\" width=\"160\"><tr><td align=\"center\">"+dateFromStrAssignment + " - " +dateToStrAssignment+"</td></tr></table>";
		} catch (ParseException e) {
			Log.getLog(getClass()).error(e.toString(), e);
//			return "-";
			return "";
		}
	}

	public String getTransAssignmentDateStrMod() {
		return transAssignmentDateStrMod;
	}

	public void setTransAssignmentDateStrMod(String transAssignmentDateStrMod) {
		this.transAssignmentDateStrMod = transAssignmentDateStrMod;
	}

	public void setTransAssignmentDateStr(String transAssignmentDateStr) {
		this.transAssignmentDateStr = transAssignmentDateStr;
	}

	public String getTransStatus() {
		return getStatus()+"/"+getId();
		
	}

	public void setTransStatus(String transStatus) {
		this.transStatus = transStatus;
	}

	public String getOutsourceId() {
		return outsourceId;
	}

	public void setOutsourceId(String outsourceId) {
		this.outsourceId = outsourceId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getDriverRequired() {
		return driverRequired;
	}

	public void setDriverRequired(String driverRequired) {
		this.driverRequired = driverRequired;
	}

	public String getTranAssignSDate() {
		return tranAssignSDate;
	}

	public void setTranAssignSDate(String tranAssignSDate) {
		this.tranAssignSDate = tranAssignSDate;
	}

	public String getTranAssignEDate() {
		return tranAssignEDate;
	}

	public void setTranAssignEDate(String tranAssignEDate) {
		this.tranAssignEDate = tranAssignEDate;
	}
	protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

	public String getSingleMeterStart() {
		return singleMeterStart;
	}

	public void setSingleMeterStart(String singleMeterStart) {
		this.singleMeterStart = singleMeterStart;
	}

	public String getSingleMeterEnd() {
		return singleMeterEnd;
	}

	public void setSingleMeterEnd(String singleMeterEnd) {
		this.singleMeterEnd = singleMeterEnd;
	}

	public String getSingleTotalMeter() {
		return singleTotalMeter;
	}

	public void setSingleTotalMeter(String singleTotalMeter) {
		this.singleTotalMeter = singleTotalMeter;
	}

	public String getSingleCheckInDate() {
		return singleCheckInDate;
	}

	public void setSingleCheckInDate(String singleCheckInDate) {
		this.singleCheckInDate = singleCheckInDate;
	}

	public String getSingleCheckOutDate() {
		return singleCheckOutDate;
	}

	public void setSingleCheckOutDate(String singleCheckOutDate) {
		this.singleCheckOutDate = singleCheckOutDate;
	}

	public String getSingleTimeRec() {
		return singleTimeRec;
	}

	public void setSingleTimeRec(String singleTimeRec) {
		this.singleTimeRec = singleTimeRec;
	}

	public String getSingleVehicleStatus() {
		return singleVehicleStatus;
	}

	public void setSingleVehicleStatus(String singleVehicleStatus) {
		this.singleVehicleStatus = singleVehicleStatus;
	}

	public String getSingleDriverStatus() {
		return singleDriverStatus;
	}

	public void setSingleDriverStatus(String singleDriverStatus) {
		this.singleDriverStatus = singleDriverStatus;
	}

	public String getReqAsssignId() {
		return reqAsssignId;
	}

	public void setReqAsssignId(String reqAsssignId) {
		this.reqAsssignId = reqAsssignId;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getRequestedVehicles() {
		return requestedVehicles;
	}

	public void setRequestedVehicles(String requestedVehicles) {
		this.requestedVehicles = requestedVehicles;
	}

	public String getRequestedDrivers() {
		return requestedDrivers;
	}

	public void setRequestedDrivers(String requestedDrivers) {
		this.requestedDrivers = requestedDrivers;
	}

	public String getTotalCostDaily() {
		return totalCostDaily;
	}

	public void setTotalCostDaily(String totalCostDaily) {
		this.totalCostDaily = totalCostDaily;
	}

	public String getPfeCode() {
		return pfeCode;
}

	public void setPfeCode(String pfeCode) {
		this.pfeCode = pfeCode;
	}
	
}
