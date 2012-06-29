package com.tms.fms.engineering.model;

import java.util.Date;

public class VtrService extends Service {

	protected String service;
	protected String serviceName;
	protected String serviceNameLink;
	protected Date requiredDate; 
	protected Date requiredDateTo;
	protected String requiredFrom;
	protected String requiredTo;
	protected String formatFrom;
	protected String formatTo;
	protected String conversionFrom;
	protected String conversionTo;
	protected String duration;
	protected int noOfCopies;
	protected String remarks;
	private String fileId;
	private String fileName;
	private String filePath;
	protected String facilityId;
	protected String facility;
	protected String blockBooking;
	protected String location;
	protected String submitted;
	
	public String getServiceNameLink() {
		return serviceNameLink;
	}
	public void setServiceNameLink(String serviceNameLink) {
		this.serviceNameLink = serviceNameLink;
	}
	public String getServiceName() {
		serviceName="";
		if(service!=null){
			serviceName=(String)EngineeringModule.SERVICE_PARTICULARS_MAP.get(service);
		}
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getConversionFromLabel() {
		String label="";
		if(conversionFrom!=null){
			label=(String)EngineeringModule.VTR_CONVERSION_MAP.get(conversionFrom);
		}
		return label;
	}
	public String getConversionFrom() {
		return conversionFrom;
	}
	public void setConversionFrom(String conversionFrom) {
		this.conversionFrom = conversionFrom;
	}
	public String getConversionToLabel() {
		String label="";
		if(conversionTo!=null){
			label=(String)EngineeringModule.VTR_CONVERSION_MAP.get(conversionTo);
		}
		return label;
	}
	public String getConversionTo() {
		return conversionTo;
	}
	public void setConversionTo(String conversionTo) {
		this.conversionTo = conversionTo;
	}
	
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
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
	public String getFormatFromLabel() {
		String label="";
		if(formatFrom!=null){
			if (("3".equals(service)) || ("4".equals(service)) || ("5".equals(service))){
				label=(String)EngineeringModule.VTR_INGEST_QC_FORMAT_MAP.get(formatFrom);
			} else {
				label=(String)EngineeringModule.VTR_FORMAT_MAP.get(formatFrom);
			}
		}
		return label;
	}
	public String getFormatFrom() {
		return formatFrom;
	}
	public void setFormatFrom(String formatFrom) {
		this.formatFrom = formatFrom;
	}
	public String getFormatToLabel() {
		String label="";
		if(formatTo!=null){
			if (("3".equals(service)) || ("4".equals(service)) || ("5".equals(service))){
				label=(String)EngineeringModule.VTR_INGEST_QC_FORMAT_MAP.get(formatTo);
			} else {
				label=(String)EngineeringModule.VTR_FORMAT_MAP.get(formatTo);
			}
			//label=(String)EngineeringModule.VTR_FORMAT_MAP.get(formatTo);
		}
		return label;
	}
	public String getFormatTo() {
		return formatTo;
	}
	public void setFormatTo(String formatTo) {
		this.formatTo = formatTo;
	}
	public int getNoOfCopies() {
		return noOfCopies;
	}
	public void setNoOfCopies(int noOfCopies) {
		this.noOfCopies = noOfCopies;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Date getRequiredDate() {
		return requiredDate;
	}
	public void setRequiredDate(Date requiredDate) {
		this.requiredDate = requiredDate;
	}
	public String getRequiredFrom() {
		return requiredFrom;
	}
	public void setRequiredFrom(String requiredFrom) {
		this.requiredFrom = requiredFrom;
	}
	public String getRequiredTo() {
		return requiredTo;
	}
	public void setRequiredTo(String requiredTo) {
		this.requiredTo = requiredTo;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getFacilityId() {
		return facilityId;
	}
	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}
	public String getFacility() {
		return facility;
	}
	public void setFacility(String facility) {
		this.facility = facility;
	}
	public String getBlockBooking() {
		return blockBooking;
	}
	public void setBlockBooking(String blockBooking) {
		this.blockBooking = blockBooking;
	}
	public Date getRequiredDateTo() {
		return requiredDateTo;
	}
	public void setRequiredDateTo(Date requiredDateTo) {
		this.requiredDateTo = requiredDateTo;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getSubmitted() {
		return submitted;
	}
	public void setSubmitted(String submitted) {
		this.submitted = submitted;
	}
	
}
