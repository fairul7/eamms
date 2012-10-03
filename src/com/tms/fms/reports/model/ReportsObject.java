package com.tms.fms.reports.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.tms.fms.engineering.ui.ServiceDetailsForm;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DefaultDataObject;

public class ReportsObject extends DefaultDataObject{
	
	public String id;
	public String requestTitle;
	public String requestType;
	public String program; 
	public Date startDate;
	public Date endDate;
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
	public String checkInDate;
	public String checkOutDate;
	public String timeRec;
	public String outsourceFlag;
	public String driverStatus;
	public String vehicleStatus;
	public String requestStatus;
	
	
	public String requestId, title, programType, createdBy, engManpowerBudget, facilitiesBudget, totalcost, bookFrom, bookTo;
	
	public String assignmentId, fromTime, toTime, serviceType, facilityEquip, rateCardId;
	public int duration;
	
	public String totalDuration;
	
	public String transAssignmentDateStr,transStatus;
	
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
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getProgram() {
		return program;
	}
	public void setProgram(String program) {
		this.program = program;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getPurpose() {
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
	
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		//this.department = department;
		if (!department.equals("")){
			this.department = "-";
		}else{
			this.department = department;
		}
	}
	public String getRequestDateStr() {
		return requestDateStr;
	}
	public void setRequestDateStr(String requestDateStr) {
		this.requestDateStr = requestDateStr;
	}
	public String getDriversAssigned() {
		return driversAssigned;
	}
	public void setDriversAssigned(String driversAssigned) {
		this.driversAssigned = driversAssigned;
	}
	public String getVehicleAssigned() {
		return vehicleAssigned;
	}
	public void setVehicleAssigned(String vehicleAssigned) {
		this.vehicleAssigned = vehicleAssigned;
	}
	public String getMeterStart() {
		return meterStart;
	}
	public void setMeterStart(String meterStart) {
		this.meterStart = meterStart;
	}
	public String getMeterEnd() {
		return meterEnd;
	}
	public void setMeterEnd(String meterEnd) {
		this.meterEnd = meterEnd;
	}
	public String getTotalMeter() {
		return totalMeter;
	}
	public void setTotalMeter(String totalMeter) {
		this.totalMeter = totalMeter;
	}
	public String getCheckInDate() {
		return checkInDate;
	}
	public void setCheckInDate(String checkInDate) {
		this.checkInDate = checkInDate;
	}
	public String getCheckOutDate() {
		return checkOutDate;
	}
	public void setCheckOutDate(String checkOutDate) {
		this.checkOutDate = checkOutDate;
	}
	public String getTimeRec() {
		return timeRec;
	}
	public void setTimeRec(String timeRec) {
		this.timeRec = timeRec;
	}
	public String getOutsourceFlag() {
		return outsourceFlag;
	}
	public void setOutsourceFlag(String outsourceFlag) {
		this.outsourceFlag = outsourceFlag;
	}
	public String getDriverStatus() {
		return driverStatus;
	}
	public void setDriverStatus(String driverStatus) {
		this.driverStatus = driverStatus;
	}
	public String getVehicleStatus() {
		return vehicleStatus;
	}
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
		return assignmentId;
	}
	public void setAssignmentId(String assignmentId) {
		this.assignmentId = assignmentId;
	}
	public String getFromTime() {
		if(fromTime.equals("00:00"))
			return "24:00";
		return fromTime;
	}
	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}
	public String getToTime() {
		if(toTime.equals("00:00"))
			return "24:00";
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
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public String getTotalDuration(){
		//String value="";
		if(getFromTime().equals("00:00")){
			setFromTime("24:00");
		}
		if(getToTime().equals("00:00")){
			setToTime("24:00");
		}
		String from=getFromTime().replace(":", "");
		String to=getToTime().replace(":", "");
		int duration=getDuration();
		
		String hourFromStr=from.substring(0, from.length()-2);
		if(hourFromStr.startsWith("0")){
			hourFromStr=hourFromStr.substring(hourFromStr.length()-1, hourFromStr.length());
		}
		String hourToStr=to.substring(0, to.length()-2);
		if(hourToStr.startsWith("0")){
			hourToStr=hourToStr.substring(hourToStr.length()-1, hourToStr.length());
		}
		
		int hourFrom=Integer.parseInt(hourFromStr);
		int hourTo=Integer.parseInt(hourToStr);
		int hour=0;
		
		if(duration>0){
			int firstday=60-hourFrom;
			int remainingdays=(duration-1)*60;
			int lastday=hourTo;
			hour=firstday+remainingdays+lastday;
		}else{
			hour=hourTo-hourFrom;
		}
		
		if(String.valueOf(hour).startsWith("-")){
			return String.valueOf(hour).replace("-", "");
		}else{
			return String.valueOf(hour);
		}
		
	}
	
	public void setTotalDuration(String totalDuration) {
		this.totalDuration = totalDuration;
	}
	
	
	public String getRateCardId() {
		return rateCardId;
	}
	public void setRateCardId(String rateCardId) {
		this.rateCardId = rateCardId;
	}
	
	
	public String getBlockBooking() {
		String serviceType=getServiceType();
		String requestId=getRequestId();
		String facilityId=getRateCardId();
		ReportsFmsModule module = (ReportsFmsModule)Application.getInstance().getModule(ReportsFmsModule.class);
		String value="";
		if((requestId!=null && !requestId.equals("")) 
				||(serviceType!=null && !serviceType.equals(""))
				||(facilityId!=null && !facilityId.equals(""))){
			
			value = module.getBlockBooking(requestId, serviceType, facilityId).equals("1")?"Yes":"No";
		}
		
		
		return value;
	}
	
	public void setBlockBooking(String blockBooking) {
		this.blockBooking = blockBooking;
	}
	
	public static void main(String[] args) {
		String tt="-14";
		System.out.println(tt.replace("-", ""));
	}
	public String getTransAssignmentDateStr() {
		return transAssignmentDateStr;
	}
	public void setTransAssignmentDateStr(String transAssignmentDateStr) {
		this.transAssignmentDateStr = transAssignmentDateStr;
	}
	public String getTransStatus() {
		return transStatus;
	}
	public void setTransStatus(String transStatus) {
		this.transStatus = transStatus;
	}
	public String getRequestStatus() {
		return requestStatus;
	}
	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}
	
}
