package com.tms.fms.transport.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.fms.transport.model.TransportModule;

public class CheckAvailabilityVehicles extends Form{
	
	private Button btnCancel;
	
	private String vehicle_num[];
	private String driver_id[];
	private String cancelUrl = "";
	private int count=0;
	private String assgId;
	
	private Collection viewVehicles[];
	private Collection viewDrivers[];
	
	private Date startDate;
	private Date endDate;
	
	
	public Collection[] getViewDrivers() {
		return viewDrivers;
	}

	public void setViewDrivers(Collection[] viewDrivers) {
		this.viewDrivers = viewDrivers;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}


	public String[] getVehicle_num() {
		return vehicle_num;
	}

	public void setVehicle_num(String[] vehicle_num) {
		this.vehicle_num = vehicle_num;
	}

	public String getCancelUrl() {
		return cancelUrl;
	}

	public void setCancelUrl(String cancelUrl) {
		this.cancelUrl = cancelUrl;
	}

	public void onRequest(Event event) {
		initForm();
    }
	
	public void initForm() {
		
	    removeChildren();
	    setMethod("post");
	    setColumns(2);
	    
	    TransportModule mod = (TransportModule)Application.getInstance().getModule(TransportModule.class);
	    Log.getLog(getClass()).info(startDate);
	    
	    if(vehicle_num != null){
		    viewVehicles = new ArrayList[vehicle_num.length];			    
		    try{
		    	for(int i = 0; i < vehicle_num.length; i++){
		    		viewVehicles[i] = mod.getVehicleAssignment(assgId, vehicle_num[i],startDate,endDate);	    		
		    	}
		    	
		    }catch(Exception er){Log.getLog(getClass()).error(er);}		    
	    }
	    
	    if(driver_id != null){
		    viewDrivers = new ArrayList[driver_id.length];
		    SecurityService ss =(SecurityService) Application.getInstance().getService(SecurityService.class) ;
		    try{
		    	for(int i = 0; i < viewDrivers.length; i++){
		    		//viewDrivers[i] = mod.getDrivers(driver_id[i]);
		    		viewDrivers[i] = mod.getManPower(driver_id[i],startDate,endDate);
		    		
		    	}		    	
		    }catch(Exception er){
		    	Log.getLog(getClass()).error(er);
		    }
	    }
	    
	}  
	
	public Forward onSubmit(Event evt) {
	    String buttonName = findButtonClicked(evt);
	    kacang.ui.Forward result = super.onSubmit(evt);
	    if (buttonName != null && btnCancel.getAbsoluteName().equals(buttonName)) {
	    	init();
	    	return new Forward(Form.CANCEL_FORM_ACTION, cancelUrl, true);
	    }else {
	    	return result;
	    }
	}	
	
	public String getDefaultTemplate() {
		return "fms/transport/vehicleavailable";
	}

	public Collection[] getViewVehicles() {
		return viewVehicles;
	}

	public void setViewVehicles(Collection[] viewVehicles) {
		this.viewVehicles = viewVehicles;
	}

	public String[] getDriver_id() {
		return driver_id;
	}

	public void setDriver_id(String[] driver_id) {
		this.driver_id = driver_id;
	}


	public String getAssgId() {
		return assgId;
	}

	public void setAssgId(String assgId) {
		this.assgId = assgId;
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
