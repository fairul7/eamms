package com.tms.fms.transport.model;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import kacang.model.DefaultDataObject;
import kacang.util.Log;

public class AssignmentObject extends DefaultDataObject{
				
			
	private String id;
	private Date startDate;
	private Date endDate;
	private String requestId;
	private String assgId;
	private Date checkin_date;
	private Date checkout_date;	
	private double meterStart;
	private double meterEnd;
	private String remarks;
	private String petrolCard;
	private String createdBy;
	private Date createdDate;
	private String updatedBy;
	private Date updatedDate;
	private String status;
	private String vehicle_num;
	
	public String getVehicle_num() {
		return vehicle_num;
	}
	public void setVehicle_num(String vehicle_num) {
		this.vehicle_num = vehicle_num;
	}
	public String getAssgId() {
		return assgId;
	}
	public void setAssgId(String assgId) {
		this.assgId = assgId;
	}
	public Date getCheckin_date() {
		return checkin_date;
	}
	public void setCheckin_date(Date checkin_date) {
		this.checkin_date = checkin_date;
	}
	public Date getCheckout_date() {
		return checkout_date;
	}
	public void setCheckout_date(Date checkout_date) {
		this.checkout_date = checkout_date;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getPetrolCard() {
		return petrolCard;
	}
	public void setPetrolCard(String petrolCard) {
		this.petrolCard = petrolCard;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public double getMeterStart() {
		return meterStart;
	}
	public void setMeterStart(double meterStart) {
		this.meterStart = meterStart;
	}
	public double getMeterEnd() {
		
		return meterEnd;
	}
	public void setMeterEnd(double meterEnd) {
		this.meterEnd = meterEnd;
	}
	
	public String getMeterLabelStart(){
		
		String meter = "";
		try{  
		      NumberFormat nf = NumberFormat.getInstance();  
		      nf.setMaximumFractionDigits(0);// set as you need  
		      meter = nf.format(meterStart); 
		        
		    }catch(Exception e){  
		    	Log.getLog(getClass()).error(e);
		    }  
		return meter;
	}
	
	public String getMeterLabelEnd(){
		
		String meter = "";
		try{  
		      NumberFormat nf = NumberFormat.getInstance();  
		      nf.setMaximumFractionDigits(0);// set as you need  
		      meter = nf.format(meterEnd); 
		        
		    }catch(Exception e){  
		    	Log.getLog(getClass()).error(e);
		    }  
		return meter;
	}
	
		
	public String getDateCheckInLabel(){		
		String checkinTime = "-";		
		
		//NumberFormat nf = NumberFormat.getInstance(); 		
		//nf.setMinimumIntegerDigits(2);       			//make format 00	
		 
		try {
			/*
	        String hourS = (nf.format((int)checkin_date.getHours()));
	        String minS = (nf.format((int)checkin_date.getMinutes()));
	        checkinTime = hourS +":"+ minS;
	        */
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			checkinTime = sdf.format(checkin_date);
		} catch (Exception e) {
		}
                    	
        return checkinTime;
	}
	
	public String getDateCheckOutLabel(){		
		String checkoutTime = "-";
				
		//NumberFormat nf = NumberFormat.getInstance(); 		
		//nf.setMinimumIntegerDigits(2);       			//make format 00	
		 
		try {
			/*
			String hourS = (nf.format((int)checkout_date.getHours()));
			String minS = (nf.format((int)checkout_date.getMinutes()));
			checkoutTime = hourS +":"+ minS;  
			*/
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			checkoutTime = sdf.format(checkout_date);
		} catch (Exception e){
		}
		
        return checkoutTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	
	
	
}
