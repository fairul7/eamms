package com.tms.fms.transport.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.ButtonGroup;
import kacang.stdui.Form;
import kacang.stdui.Radio;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.fms.transport.model.DriverVehicleObject;
import com.tms.fms.transport.model.TransportModule;
import com.tms.fms.transport.model.VehicleObject;

public class TransportDriverVehicleForm extends Form {
	
	private String assignmentId;
	private String userId;
	private Radio[] radioVehicles;
	protected ButtonGroup groupVehicles;
    private Button submitButton, cancelButton;
    private Collection request;

    public TransportDriverVehicleForm() {
    }

    public TransportDriverVehicleForm(String s) {
        super(s);
    }

    public void init() {
    	
    	request = new ArrayList();
    	TransportModule module = (TransportModule) Application.getInstance().getModule(TransportModule.class);       
    	try{
    		if(assignmentId != null)
    			request = module.selectVehiclesByAssignmentId(assignmentId);
    		
    	}catch(Exception er){
    		
    	}   	
    	
    	groupVehicles = new ButtonGroup("groupVehicles");
    	
    	int i = 0;
    	if(request.size() > 0){
    		radioVehicles = new Radio[request.size()];
    		for(Iterator it = request.iterator(); it.hasNext(); ){
    			VehicleObject vehicle = (VehicleObject) it.next();
    			String id = vehicle.getVehicle_num();
    			String category = vehicle.getCategory_name();
    			radioVehicles[i] = new Radio(id, id + " ["+category+"]");    	
    			if(i == 0)
    				radioVehicles[i].setChecked(true);
    			radioVehicles[i].setGroupName("vehicleType");
    			addChild(radioVehicles[i]);
    			groupVehicles.addChild(radioVehicles[i]);
    			i++;
    		}
    	}
    	
    	addChild(groupVehicles);
		submitButton = new Button("submitButton", Application.getInstance().getMessage("general.label.submit","Submit"));
		cancelButton = new Button("cancelButton", Application.getInstance().getMessage("general.label.cancel","Cancel"));
        addChild(submitButton);
        addChild(cancelButton);
        
    }

	public void onRequest(Event event){
		init();
	}
	
    public Forward onValidate(Event event) {
    	
    	String button = findButtonClicked(event);
    	String vehicleNo = null;
    	if(submitButton.getAbsoluteName().equals(button)){
    		
    		for(int i = 0; i <= request.size(); i++){
    			if(radioVehicles[i].isChecked()){
    				vehicleNo = (String) radioVehicles[i].getName();
    				Log.getLog(getClass()).info(vehicleNo + userId + assignmentId);
    				break;
    			}
    		}
    		
    		if(vehicleNo != null){
    			TransportModule module = (TransportModule) Application.getInstance().getModule(TransportModule.class);   
    			DriverVehicleObject dvObject = new DriverVehicleObject();
    			dvObject.setId(UuidGenerator.getInstance().getUuid());
    			dvObject.setAssignmentId(assignmentId);
    			dvObject.setVehicle_num(vehicleNo);
    			dvObject.setDriver(userId);
    			
    			try{
    				module.insertDriverVehicle(dvObject);
    			}catch(Exception er){
    				
    			}
    		}
    		    		
    		return new Forward("submit");
    	}else{
    		return new Forward("cancel");
    	}
    	
    }

    public String getDefaultTemplate() {
        return "fms/transport/driverVehicle";
    }

	
	public String getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(String assignmentId) {
		this.assignmentId = assignmentId;
	}

	public Radio[] getRadioVehicles() {
		return radioVehicles;
	}

	public void setRadioVehicles(Radio[] radioVehicles) {
		this.radioVehicles = radioVehicles;
	}

	public Button getSubmitButton() {
		return submitButton;
	}

	public void setSubmitButton(Button submitButton) {
		this.submitButton = submitButton;
	}

	public Button getCancelButton() {
		return cancelButton;
	}

	public void setCancelButton(Button cancelButton) {
		this.cancelButton = cancelButton;
	}

	public Collection getRequest() {
		return request;
	}

	public void setRequest(Collection request) {
		this.request = request;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public ButtonGroup getGroupVehicles() {
		return groupVehicles;
	}

	public void setGroupVehicles(ButtonGroup groupVehicles) {
		this.groupVehicles = groupVehicles;
	}

	
	
    
}
