package com.tms.fms.engineering.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kacang.Application;
import kacang.model.DefaultDataObject;
import kacang.util.Log;

public class Assignment extends DefaultDataObject {
	
	private String assignmentId;
	private String code,competencyName,rateCardCategoryName,unitId; 
	private String requestId, title,serviceType,serviceTypeLabel, serviceId, assignmentType=EngineeringModule.ASSIGNMENT_TYPE_FACILITY,assignedFacility;
	private String rateCardId, competencyId,rateCardCategoryId;
	private Date requiredFrom, requiredTo, createdDate, modifiedDate, checkedOutDate, checkedInDate, completionDate,convertedCheckedOutDate2;
	private String fromTime, toTime, userId, status, createdBy, modifiedBy, checkedOutBy, checkedOutByFullName, checkedInBy, checkedInByFullName, barcode;
	private boolean isApprover = false;
	private String unitApprover = "0";
	private String assignmentLocation, takenBy, preparedBy, storeLocation,convertedCheckedOutDate;
	private String blockBooking;
	private String groupId;
	private String reasonUnfulfilled;
	private String assignmentEquipmentId;
	private String assignee;
	private String facilityName;
	private String cancelBy;
	private int totalCheckedIn;
	private int totalCheckedOut;
	

	private String prepareCheckOutBy;
	private Date prepareCheckOutDate;
	
	public String getAssignmentId() {
		return assignmentId;
	}
	public void setAssignmentId(String assignmentId) {
		this.assignmentId = assignmentId;
	}
	public String getAssignmentType() {
		return assignmentType;
	}
	public void setAssignmentType(String assignmentType) {
		this.assignmentType = assignmentType;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCompetencyId() {
		return competencyId;
	}
	public void setCompetencyId(String competencyId) {
		this.competencyId = competencyId;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedByFullName() {
		return getFullname(createdBy);
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getFromTime() {
		return fromTime;
	}
	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getRateCardId() {
		return rateCardId;
	}
	public void setRateCardId(String rateCardId) {
		this.rateCardId = rateCardId;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public Date getRequiredFrom() {
		return requiredFrom;
	}
	public void setRequiredFrom(Date requiredFrom) {
		this.requiredFrom = requiredFrom;
	}
	public Date getRequiredTo() {
		return requiredTo;
	}
	public void setRequiredTo(Date requiredTo) {
		this.requiredTo = requiredTo;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getToTime() {
		return toTime;
	}
	public void setToTime(String toTime) {
		this.toTime = toTime;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getCheckedOutBy() {
		return checkedOutBy;
	}
	public void setCheckedOutBy(String checkedOutBy) {
		this.checkedOutBy = checkedOutBy;
	}
	public String getRateCardCategoryId() {
		return rateCardCategoryId;
	}
	public void setRateCardCategoryId(String rateCardCategoryId) {
		this.rateCardCategoryId = rateCardCategoryId;
	}
	public String getCheckedInBy() {
		return checkedInBy;
	}
	public void setCheckedInBy(String checkedInBy) {
		this.checkedInBy = checkedInBy;
	}
	public Date getCheckedInDate() {
		return checkedInDate;
	}
	public void setCheckedInDate(Date checkedInDate) {
		this.checkedInDate = checkedInDate;
	}
	public Date getCheckedOutDate() {
		return checkedOutDate;
	}
	public void setCheckedOutDate(Date checkedOutDate) {
		this.checkedOutDate = checkedOutDate;
	}
	public String getAssignedFacility() {
		return assignedFacility;
	}
	public void setAssignedFacility(String assignedFacility) {
		this.assignedFacility = assignedFacility;
	}
	public String getCompetencyName() {
		return competencyName;
	}
	public void setCompetencyName(String competencyName) {
		this.competencyName = competencyName;
	}
	public String getRateCardCategoryName() {
		return rateCardCategoryName;
	}
	public void setRateCardCategoryName(String rateCardCategoryName) {
		this.rateCardCategoryName = rateCardCategoryName;
	} 
	public String getCodeWithLink() {
		if(EngineeringModule.ASSIGNMENT_TYPE_MANPOWER.equals(assignmentType)){
			if (EngineeringModule.ASSIGNMENT_MANPOWER_STATUS_COMPLETED.equals(status) || 
					EngineeringModule.ASSIGNMENT_MANPOWER_STATUS_UNFULFILLED.equals(status)){
				return "<a href=\"javascript:pops('mains','assignManpowerPopup.jsp?id="+assignmentId+"',400,520)\">"+code+"</a>";
			} else {
				return "<a href=\"javascript:pops('assign','assignManpower.jsp?id="+assignmentId+"&unitId="+unitId+"',500,620)\">"+code+"</a>";
			}
				
		}else{
			return code;
		}
	}
	public String getStatusLabel() {
		if(EngineeringModule.ASSIGNMENT_TYPE_MANPOWER.equals(assignmentType)){
			return (String)EngineeringModule.ASSIGNMENT_MANPOWER_STATUS_MAP.get(status);
		}else{
			return (String)EngineeringModule.ASSIGNMENT_FACILITY_STATUS_MAP.get(status);
		}
	}
	public String getServiceTypeLabel() {
		return (String)EngineeringModule.SERVICES_MAP.get(serviceType);
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public boolean isApprover() {
		return isApprover;
	}
	public void setApprover(boolean isApprover) {
		this.isApprover = isApprover;
	}
	public String getUnitApprover() {
		return unitApprover;
	}
	public void setUnitApprover(String unitApprover) {
		this.unitApprover = unitApprover;
	}
	public Date getCompletionDate() {
		return completionDate;
	}
	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}
	public static String getFullname(String username){
		EngineeringDao dao=(EngineeringDao)Application.getInstance().getModule(EngineeringModule.class).getDao();
		return dao.getFullName(username);
	}
	public String getCheckedOutByFullName() {
		return getFullname(checkedOutBy);
	}
	public void setCheckedOutByFullName(String checkedOutByFullName) {
		this.checkedOutByFullName = checkedOutByFullName;
	}
	public String getCheckedInByFullName() {
		return getFullname(checkedInBy);
	}
	public void setCheckedInByFullName(String checkedInByFullName) {
		this.checkedInByFullName = checkedInByFullName;
	}
	public String getAssignmentLocation() {
		return assignmentLocation;
	}
	public void setAssignmentLocation(String assignmentLocation) {
		this.assignmentLocation = assignmentLocation;
	}
	public String getTakenBy() {
		return takenBy;
	}
	public void setTakenBy(String takenBy) {
		this.takenBy = takenBy;
	}
	public String getPreparedBy() {
		return preparedBy;
	}
	public void setPreparedBy(String preparedBy) {
		this.preparedBy = preparedBy;
	}
	public String getStoreLocation() {
		return storeLocation;
	}
	public void setStoreLocation(String storeLocation) {
		this.storeLocation = storeLocation;
	}
	public String getBlockBooking() {
		return blockBooking;
	}
	public void setBlockBooking(String blockBooking) {
		this.blockBooking = blockBooking;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getReasonUnfulfilled() {
		return reasonUnfulfilled;
	}
	public void setReasonUnfulfilled(String reasonUnfulfilled) {
		this.reasonUnfulfilled = reasonUnfulfilled;
	}
	public String getAssignmentEquipmentId() {
		return assignmentEquipmentId;
	}
	public void setAssignmentEquipmentId(String assignmentEquipmentId) {
		this.assignmentEquipmentId = assignmentEquipmentId;
	}
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public String getFacilityName() {
		return facilityName;
	}
	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}
	public String getCancelBy() {
		return cancelBy;
	}
	public void setCancelBy(String cancelBy) {
		this.cancelBy = cancelBy;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getTotalCheckedIn() {
		return totalCheckedIn;
	}
	public void setTotalCheckedIn(int totalCheckedIn) {
		this.totalCheckedIn = totalCheckedIn;
	}
	public int getTotalCheckedOut() {
		return totalCheckedOut;
	}
	public void setTotalCheckedOut(int totalCheckedOut) {
		this.totalCheckedOut = totalCheckedOut;
	}
	public String getPrepareCheckOutBy() {
		return prepareCheckOutBy;
	}
	public void setPrepareCheckOutBy(String prepareCheckOutBy) {
		this.prepareCheckOutBy = prepareCheckOutBy;
	}
	public Date getPrepareCheckOutDate() {
		return prepareCheckOutDate;
	}
	public void setPrepareCheckOutDate(Date prepareCheckOutDate) {
		this.prepareCheckOutDate = prepareCheckOutDate;
	}
	public void setServiceTypeLabel(String serviceTypeLabel) {
		this.serviceTypeLabel = serviceTypeLabel;
	}
	
	public String getConvertedCheckedOutDate() {
		return convertedCheckedOutDate;
	}
	public void setConvertedCheckedOutDate(String convertedCheckedOutDate) {
		this.convertedCheckedOutDate = convertedCheckedOutDate;
	}
	public void setConvertedCheckedOutDate2(Date convertedCheckedOutDate2) {
		this.convertedCheckedOutDate2 = convertedCheckedOutDate2;
	}
	public Date getConvertedCheckedOutDate2() {
		DateFormat df = new SimpleDateFormat("MMM dd yyyy hh:mmaa");
		Date convDate=null;
		try {
			convDate = df.parse(convertedCheckedOutDate);
			//System.out.println("============================== "+convDate);
		} catch (ParseException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		return convDate;
	}
	
	
	
}
