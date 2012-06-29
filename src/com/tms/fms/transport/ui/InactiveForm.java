package com.tms.fms.transport.ui;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import com.tms.fms.transport.model.*;

import kacang.Application;
import kacang.stdui.*;
import kacang.stdui.validator.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.*;

public class InactiveForm extends Form{
	public static final String FORWARD_ADD_SUCCESS = "form.add.success";
	public static final String FORWARD_ADD_FAIL = "form.add.fail";
	public static final String FORWARD_EDIT_SUCCESS = "form.edit.success";
	public static final String FORWARD_EDIT_FAIL = "form.edit.fail";
	
	public static final String FORM_ACTION_ADD = "form.action.add";
	public static final String FORM_ACTION_EDIT = "form.action.edit";
	
	private Label lbVehicleNum;
	private DatePopupField dpfDateFrom;
	private DatePopupField dpfDateTo;
	private SelectBox sbReason;
	private Button btnSubmit;
	private Button btnCancel;
	
	private String vehicle_num[];
	private String id;
	private String action;
	private String cancelUrl = "";
	private String whoModifyId = "";
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getCancelUrl() {
		return cancelUrl;
	}

	public void setCancelUrl(String cancelUrl) {
		this.cancelUrl = cancelUrl;
	}

	public String getWhoModifyId() {
		return whoModifyId;
	}

	public void setWhoModifyId(String whoModifyId) {
		this.whoModifyId = whoModifyId;
	}

	public String[] getVehicle_num() {
		return vehicle_num;
	}
	
	public void setVehicle_num(String[] vehicle_num) {
		this.vehicle_num = vehicle_num;
	}
	
	public void onRequest(Event event) {
		initForm();
	    if (FORM_ACTION_EDIT.equals(action)) {populateFields();}
    }
	
	public void initForm() {
		
	    Application application = Application.getInstance();
	    String msgNotEmpty  = Application.getInstance().getMessage("fms.tran.msg.mandatoryField", "Mandatory Field");
	    String initialSelect = "-1="+Application.getInstance().getMessage("fms.tran.msg.initialSelect", "--- NONE ---");
	    String msgIsNumberic = Application.getInstance().getMessage("fms.tran.msg.numbericField", "Numberic Field");

	    removeChildren();
	    setMethod("post");
	    setColumns(2);
	    
	    String vehicleString = vehicle_num[0];
	    
	    for(int i=1; i < vehicle_num.length; i++){
	    	vehicleString = vehicleString + ", " + vehicle_num[i];
	    }
	    
	    addChild(new Label("lb1", Application.getInstance().getMessage("fms.tran.form.vehicleNumber", "Vehicle Number")));
	    lbVehicleNum = new Label("lbVehicleNum", vehicleString);
		addChild(lbVehicleNum);
		
		addChild(new Label("lb2", Application.getInstance().getMessage("fms.tran.form.DateFrom", "Date From")));
		dpfDateFrom = new DatePopupField("dpfDateFrom");
		dpfDateFrom.setFormat("dd-MM-yyyy");
		dpfDateFrom.setDate(new Date());
		addChild(dpfDateFrom);
	    
		addChild(new Label("lb3", Application.getInstance().getMessage("fms.tran.form.DateTo", "Date To")));
		dpfDateTo = new DatePopupField("dpfDateTo");
		dpfDateTo.setFormat("dd-MM-yyyy");
		dpfDateTo.setDate(new Date());
		addChild(dpfDateTo);
		
		addChild(new Label("lb4", Application.getInstance().getMessage("fms.tran.form.InactiveReason", "Inactive Reason")));
		sbReason = new SelectBox("sbReason");
		sbReason.setOptions(initialSelect);
	    try {
			TransportModule mod = (TransportModule)Application.getInstance().getModule(TransportModule.class);
			Collection lstChannel = mod.selectSetupObject(SetupObject.SETUP_INACTIVE_REASON, "", "1", "name", false, 0, -1);
		    if (lstChannel.size() > 0) {
		    	for (Iterator i=lstChannel.iterator(); i.hasNext();) {
		        	SetupObject o = (SetupObject)i.next();
		        	sbReason.setOptions(o.getSetup_id()+"="+o.getName());
		        }
		    }
		}catch (Exception e) {
		    Log.getLog(getClass()).error(e.toString());
		}
		sbReason.addChild(new ValidatorSelectBoxNotEmpty("sbReasonNotEmpty", msgNotEmpty));
	    addChild(sbReason);
		
		addChild(new Label("lbbutton", ""));
		Panel pnButton = new Panel("pnButton");
	    if (FORM_ACTION_ADD.equals(action)) {
	    	btnSubmit = new Button("btnSubmit", Application.getInstance().getMessage("fms.tran.submit", "Submit"));
	    	pnButton.addChild(btnSubmit);
	    }
		if (FORM_ACTION_EDIT.equals(action)) {
			btnSubmit = new Button("btnSubmit", Application.getInstance().getMessage("fms.tran.update", "Update"));
			pnButton.addChild(btnSubmit);
		}   
		btnCancel = new Button("btnCancel", Application.getInstance().getMessage("fms.tran.cancel", "Cancel"));
	    pnButton.addChild(btnCancel);
	    addChild(pnButton);  
	}  
	
	public void populateFields() {
		if (FORM_ACTION_EDIT.equals(action)){
			
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
	
	public Forward onValidate(Event event) {
		boolean success = true;
		if (FORM_ACTION_ADD.equals(action)) {
			for(int j=0; j<vehicle_num.length; j++){
				InactiveObject i = new InactiveObject();
				TransportModule module = (TransportModule)Application.getInstance().getModule(TransportModule.class);
				
				i.setVehicle_num(vehicle_num[j]);
				i.setDate_from(dpfDateFrom.getDate());
				i.setDate_to(dpfDateTo.getDate());
				i.setReason_id(getSelectBoxValue(sbReason));
				
				try {
					VehicleObject v = module.getVehicle(vehicle_num[j]);
					
					if("1".equals(v.getStatus())){
						i.setCreatedby(getWhoModifyId());
						i.setCreatedby_date(new Date());
						i.setId(UuidGenerator.getInstance().getUuid());
							
						module.insertInactive(i);
						
						v.setStatus("0");
						module.updateVehicle(v);
					}else{
						success = false;
					}
				}catch (Exception e) {
					Log.getLog(getClass()).error(e.toString()); 
					success = false;
				}
			}
		}
		if(success){
			return new Forward(FORWARD_ADD_SUCCESS);
		}else{
			return new Forward(FORWARD_ADD_FAIL);
		}
	}	
	
	private String getSelectBoxValue(SelectBox sb) {
	    if (sb != null) {
	    	Map selected = sb.getSelectedOptions();
	    	if (selected.size() == 1) {
	    		return (String)selected.keySet().iterator().next();
	    	}
	    }
	    return null;
	}
	
	public String getDefaultTemplate() {
		return "fms/transport/inactiveForm";
	}
}
