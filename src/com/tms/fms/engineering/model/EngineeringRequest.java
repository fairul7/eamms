package com.tms.fms.engineering.model;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.tms.fms.facility.model.SetupModule;

import kacang.Application;
import kacang.model.DefaultDataObject;
import kacang.util.Log;


@SuppressWarnings("serial")
public class EngineeringRequest extends DefaultDataObject {
	private String requestId;
	private String title;
	private String requestType;
	private String programType;
	private String clientName;
	private String program;
	private String description;
	private Date requiredFrom;
	private Date requiredTo;
	private String createdBy;
	private Date createdOn;
	private String modifiedBy;
	private Date modifiedOn;
	private String status;
	private String servicesCSV;
	private String serviceId;
	private Collection services;
	private String state;
	private String programName;
	private Date submittedDate;
	private boolean viewMode=true;
	private String department;
	private String departmentId;
	private String approverRemarks;
	private Date approvedDate;
	private String approvedBy;
	private String outsourceId;
	private String outsourceType;
	private String companyId;
	private String companyName;
	private String fileId;
	private String fileName;
	private String filePath;
	private String estimatedCost;
	private String actualCost;
	private String cancellationCharges;
	private String systemCalculatedCharges;
	
	private String lateCharges;
	private String fcId;
	private String fullName;
	
	// for assignment request
	private String userId;
	private String manpowerId;
	private String manpowerName;
	private String assignmentId;
	private String assignmentCode;
	private String fromTime;
	private String toTime;
	private String competencyId;
	private String competencyName;
	private String requiredDate;
	private String requiredTime;
	private Date completionDate;
	private String completionTime;
	private String requestBy;
	private String groupId;
	private String serviceType;
	private String assignmentType;
	
	private String assignmentEquipmentId;
	private String rateCardId;
	private String rateCardCategoryId;
	private String barcode;
	private Date checkedOutDate;
	private String checkedOutBy;
	private Date checkedInDate;
	private String checkedInBy;
	private String damage;
	private String remarks;
	private String reasonUnfulfilled;
	
	private String createdUserName;
	private String approverUserName;
	
	private String takenBy;
	private String preparedBy;
	private String assignmentLocation;
	
	private Double totalInternalRate;
	private Double totalExternalRate;
	
	private String chargeBack;
	private String callBack;
	
	private String globalAssignmentId;
	private int refreshRate;
	private int noOfDays;
	private String footerMessage;
	
	private Collection assignments;
	private String producer;
	//added on 150310 - CR# 158
	private String prepareCheckOutBy;
	private Date prepareCheckOutDate;
	public String checkoutFlag;
	
	private String cancelBy;
	private Date cancelDate;
	private String cancelFlag;
	
	private String flagMainCharges;
	
	private String facilityId;
	private String cancellationCostManpower;
	
	
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getProgram() {
		return program;
	}
	public void setProgram(String program) {
		this.program = program;
	}
	public String getProgramType() {
		return programType;
	}
	public void setProgramType(String programType) {
		this.programType = programType;
	}
	public String getRequestId() {
		return requestId;
	}
	public boolean isViewMode() {
		if(EngineeringModule.DRAFT_STATUS.equals(status)){
			viewMode=false;
		}
		return viewMode;
	}
	public String getRequestIdWithLink() {
		String link="";
		if(EngineeringModule.DRAFT_STATUS.equals(status)){
			link="<a href='requestEdit.jsp?requestId="+requestId+"'>"+requestId+"<a/>";
		}else{
			link="<a href='requestDetails.jsp?requestId="+requestId+"'>"+requestId+"<a/>";
		}
		return link;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public Date getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public String getServicesCSV() {
		String servicesCSV="";
		try{
			for(Iterator itr2=services.iterator();itr2.hasNext();){
				Service service=(Service)itr2.next();
				String serviceName=service.getDisplayTitle();
				if(serviceName!=null && serviceName.length()>0){
					if("".equals(servicesCSV)){
						servicesCSV+=serviceName;
					}else{
						servicesCSV+=", "+serviceName;
					}
				}
			}
		}catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return servicesCSV;
	}
	public void setServicesCSV(String servicesCSV) {
		this.servicesCSV = servicesCSV;
	}
	public String getStatusLabel() {
		return (String)EngineeringModule.STATUS_MAP.get(status);
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public Collection getServices() {
		return services;
	}
	public void setServices(Collection services) {
		this.services = services;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public Date getSubmittedDate() {
		return submittedDate;
	}
	public void setSubmittedDate(Date submittedDate) {
		this.submittedDate = submittedDate;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	public boolean isCurrentUserRequest() {
		boolean result=false;
		if(createdBy!=null){
			if(createdBy.equals(Application.getInstance().getCurrentUser().getUsername())){
				result=true;
			}
		}
		return result;
	}
	public String getActualCost() {
		return actualCost;
	}
	public void setActualCost(String actualCost) {
		this.actualCost = actualCost;
	}
	public String getEstimatedCost() {
		return estimatedCost;
	}
	public void setEstimatedCost(String estimatedCost) {
		this.estimatedCost = estimatedCost;
	}
	public String getOutsourceId() {
		return outsourceId;
	}
	public void setOutsourceId(String outsourceId) {
		this.outsourceId = outsourceId;
	}
	public String getOutsourceType() {
		return outsourceType;
	}
	public void setOutsourceType(String outsourceType) {
		this.outsourceType = outsourceType;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
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
	public String getApproverRemarks() {
		return approverRemarks;
	}
	public void setApproverRemarks(String approverRemarks) {
		this.approverRemarks = approverRemarks;
	}
	public String getCancellationCharges() {
		return cancellationCharges;
	}
	public void setCancellationCharges(String cancellationCharges) {
		this.cancellationCharges = cancellationCharges;
	}
	public String getLateCharges() {
		return lateCharges;
	}
	public void setLateCharges(String lateCharges) {
		this.lateCharges = lateCharges;
	}
	public String getFcId() {
		return fcId;
	}
	public void setFcId(String fcId) {
		this.fcId = fcId;
	}
	public String getAssignmentId() {
		return assignmentId;
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
	public String getCompetencyId() {
		return competencyId;
	}
	public void setCompetencyId(String competencyId) {
		this.competencyId = competencyId;
	}
	public String getCompetencyName() {
		return competencyName;
	}
	public void setCompetencyName(String competencyName) {
		this.competencyName = competencyName;
	}
	public String getAssignmentCode() {
		return assignmentCode;
	}
	public void setAssignmentCode(String assignmentCode) {
		this.assignmentCode = assignmentCode;
	}
	public String getRequiredTime() {
		return requiredTime;
	}
	public void setRequiredTime(String requiredTime) {
		this.requiredTime = requiredTime;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getManpowerId() {
		return manpowerId;
	}
	public void setManpowerId(String manpowerId) {
		this.manpowerId = manpowerId;
	}
	public String getRequiredDate() {
		return requiredDate;
	}
	public void setRequiredDate(String requiredDate) {
		this.requiredDate = requiredDate;
	}
	public String getManpowerName() {
		return manpowerName;
	}
	public void setManpowerName(String manpowerName) {
		this.manpowerName = manpowerName;
	}
	public Date getCompletionDate() {
		return completionDate;
	}
	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}
	public String getCompletionTime() {
		return completionTime;
	}
	public void setCompletionTime(String completionTime) {
		this.completionTime = completionTime;
	}
	public String getRateCardId() {
		return rateCardId;
	}
	public void setRateCardId(String rateCardId) {
		this.rateCardId = rateCardId;
	}
	public String getRateCardCategoryId() {
		return rateCardCategoryId;
	}
	public void setRateCardCategoryId(String rateCardCategoryId) {
		this.rateCardCategoryId = rateCardCategoryId;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public Date getCheckedOutDate() {
		return checkedOutDate;
	}
	public void setCheckedOutDate(Date checkedOutDate) {
		this.checkedOutDate = checkedOutDate;
	}
	public String getCheckedOutBy() {
		return checkedOutBy;
	}
	public void setCheckedOutBy(String checkedOutBy) {
		this.checkedOutBy = checkedOutBy;
	}
	public String getAssignmentEquipmentId() {
		return assignmentEquipmentId;
	}
	public void setAssignmentEquipmentId(String assignmentEquipmentId) {
		this.assignmentEquipmentId = assignmentEquipmentId;
	}
	public Date getCheckedInDate() {
		return checkedInDate;
	}
	public void setCheckedInDate(Date checkedInDate) {
		this.checkedInDate = checkedInDate;
	}
	public String getCheckedInBy() {
		return checkedInBy;
	}
	public void setCheckedInBy(String checkedInBy) {
		this.checkedInBy = checkedInBy;
	}
	public String getDamage() {
		return damage;
	}
	public void setDamage(String damage) {
		this.damage = damage;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getReasonUnfulfilled() {
		return reasonUnfulfilled;
	}
	public void setReasonUnfulfilled(String reasonUnfulfilled) {
		this.reasonUnfulfilled = reasonUnfulfilled;
	}
	public String getRequestBy() {
		return requestBy;
	}
	public void setRequestBy(String requestBy) {
		this.requestBy = requestBy;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public String getModifierUserName(){
		return getFullname(modifiedBy);
	}
	
	public String getApproverUserName(){
		this.approverUserName = getFullname(approvedBy);
		return this.approverUserName;
	}
	
	public String getCreatedUserName(){
		this.createdUserName = getFullname(createdBy);
		return this.createdUserName;
	}
	
	public static String getFullname(String username){
		EngineeringDao dao=(EngineeringDao)Application.getInstance().getModule(EngineeringModule.class).getDao();
		return dao.getFullName(username);
	}
	public void setCreatedUserName(String createdUserName) {
		this.createdUserName = createdUserName;
	}
	public void setApproverUserName(String approverUserName) {
		this.approverUserName = approverUserName;
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
	public String getAssignmentLocation() {
		return assignmentLocation;
	}
	public void setAssignmentLocation(String assignmentLocation) {
		this.assignmentLocation = assignmentLocation;
	}
	public Double getTotalInternalRate() {
		return totalInternalRate;
	}
	public void setTotalInternalRate(Double totalInternalRate) {
		this.totalInternalRate = totalInternalRate;
	}
	public Double getTotalExternalRate() {
		return totalExternalRate;
	}
	public void setTotalExternalRate(Double totalExternalRate) {
		this.totalExternalRate = totalExternalRate;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getAssignmentType() {
		return assignmentType;
	}
	public void setAssignmentType(String assignmentType) {
		this.assignmentType = assignmentType;
	}
	public String getChargeBack() {
		return chargeBack;
	}
	public void setChargeBack(String chargeBack) {
		this.chargeBack = chargeBack;
	}
	public String getCallBack() {
		return callBack;
	}
	public void setCallBack(String callBack) {
		this.callBack = callBack;
	}
	public String getRequiredDateFrom() {
		SimpleDateFormat sdf = new SimpleDateFormat(SetupModule.DATE_FORMAT);
		
		String requiredDateFrom = sdf.format(getRequiredFrom());
		String requiredDateTo = sdf.format(getRequiredTo());
		
		return requiredDateFrom + " - " + requiredDateTo;
	}
	public int getRefreshRate() {
		return refreshRate;
	}
	public void setRefreshRate(int refreshRate) {
		this.refreshRate = refreshRate;
	}
	public int getNoOfDays() {
		return noOfDays;
	}
	public void setNoOfDays(int noOfDays) {
		this.noOfDays = noOfDays;
	}
	public String getFooterMessage() {
		return footerMessage;
	}
	public void setFooterMessage(String footerMessage) {
		this.footerMessage = footerMessage;
	}
	public String getGlobalAssignmentId() {
		return globalAssignmentId;
	}
	public void setGlobalAssignmentId(String globalAssignmentId) {
		this.globalAssignmentId = globalAssignmentId;
	}
	public Collection getAssignments() {
		return assignments;
	}
	public void setAssignments(Collection assignments) {
		this.assignments = assignments;
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
	public String getProducer() {
		return producer;
	}
	public void setProducer(String producer) {
		this.producer = producer;
	}
	public String getCheckoutFlag() {
		return checkoutFlag;
	}
	public void setCheckoutFlag(String checkoutFlag) {
		this.checkoutFlag = checkoutFlag;
	}
	public String getCancelBy() {
		return cancelBy;
	}
	public void setCancelBy(String cancelBy) {
		this.cancelBy = cancelBy;
	}
	public Date getCancelDate() {
		return cancelDate;
	}
	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}
	public String getCancelFlag() {
		return cancelFlag;
	}
	public void setCancelFlag(String cancelFlag) {
		this.cancelFlag = cancelFlag;
	}
	public String getSystemCalculatedCharges() {
		return systemCalculatedCharges;
	}
	public void setSystemCalculatedCharges(String systemCalculatedCharges) {
		this.systemCalculatedCharges = systemCalculatedCharges;
	}
	public String getFlagMainCharges() {
		return flagMainCharges;
	}
	public void setFlagMainCharges(String flagMainCharges) {
		this.flagMainCharges = flagMainCharges;
	}
	public String getFacilityId() {
		return facilityId;
	}
	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}
	/*public String getCancellationCostFacilities() {
		return cancellationCostFacilities;
	}
	public void setCancellationCostFacilities(String cancellationCostFacilities) {
		this.cancellationCostFacilities = cancellationCostFacilities;
	}*/
	public String getCancellationCostManpower() {
		return cancellationCostManpower;
	}
	public void setCancellationCostManpower(String cancellationCostManpower) {
		this.cancellationCostManpower = cancellationCostManpower;
	}
	
	
	
}
