package com.tms.fms.facility.ui;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import com.tms.fms.facility.model.FInactiveObject;
import com.tms.fms.facility.model.FacilityModule;
import com.tms.fms.facility.model.FacilityObject;
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
	
	private Label lbItemName;
	private Label lbItemBarcode;
	private DatePopupField dpfDateFrom;
	private DatePopupField dpfDateTo;
	private SelectBox sbReason;
	private Button btnSubmit;
	private Button btnCancel;
	
	private String facility_id;
	private String item_barcode[];
	private String id;
	private String action;
	private String cancelUrl = "";
	private String whoModifyId = "";
	private String mode;
	
	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

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

	public String[] getItem_barcode() {
		return item_barcode;
	}
	
	public void setItem_barcode(String[] item_barcode) {
		this.item_barcode = item_barcode;
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
	    
	    FacilityModule mod2 = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
	    
	    addChild(new Label("lb6", Application.getInstance().getMessage("fms.facility.form.itemName", "Item Name*")));
	    lbItemName = new Label("lbItemName");
	    if("".equals(facility_id)){
	    	FacilityObject i=mod2.getItem(item_barcode[0]);
	    	facility_id = i.getFacility_id();
	    }
	    try {
			FacilityObject o=mod2.getFacility(facility_id);
			lbItemName.setText(o.getName());
		}catch (Exception e) {
		    Log.getLog(getClass()).error(e.toString());
		}
	    addChild(lbItemName);
	    
	    String itemString = item_barcode[0];
	    
	    for(int i=1; i < item_barcode.length; i++){
	    	itemString = itemString + ", " + item_barcode[i];
	    }
	    
	    addChild(new Label("lb1", Application.getInstance().getMessage("fms.facility.form.itemBarcode", "Item Barcode")));
	    lbItemBarcode = new Label("lbItemBarcode", itemString);
		addChild(lbItemBarcode);
		
		addChild(new Label("lb2", Application.getInstance().getMessage("fms.facility.form.dateFrom", "Date From")));
		dpfDateFrom = new DatePopupField("dpfDateFrom");
		dpfDateFrom.setFormat("dd-MM-yyyy");
		dpfDateFrom.setDate(new Date());
		addChild(dpfDateFrom);
	    
		addChild(new Label("lb3", Application.getInstance().getMessage("fms.facility.form.dateTo", "Date To")));
		dpfDateTo = new DatePopupField("dpfDateTo");
		dpfDateTo.setFormat("dd-MM-yyyy");
		dpfDateTo.setDate(new Date());
		addChild(dpfDateTo);
		
		addChild(new Label("lb4", Application.getInstance().getMessage("fms.tran.form.InactiveReason", "Inactive Reason")));
		sbReason = new SelectBox("sbReason");
		sbReason.setOptions(initialSelect);
	    try {
			TransportModule mod = (TransportModule)Application.getInstance().getModule(TransportModule.class);
			Collection lstChannel = mod.selectSetupObject(SetupObject.SETUP_F_INACTIVE_REASON, "", "1", "name", false, 0, -1);
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
	    	btnSubmit = new Button("btnSubmit", Application.getInstance().getMessage("fms.facility.submit", "Submit"));
	    	pnButton.addChild(btnSubmit);
	    }
		if (FORM_ACTION_EDIT.equals(action)) {
			btnSubmit = new Button("btnSubmit", Application.getInstance().getMessage("fms.facility.update", "Update"));
			pnButton.addChild(btnSubmit);
		}   
		btnCancel = new Button("btnCancel", Application.getInstance().getMessage("fms.facility.cancel", "Cancel"));
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
			for(int j=0; j<item_barcode.length; j++){
				FInactiveObject i = new FInactiveObject();
				FacilityModule module = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
				
				i.setItem_barcode(item_barcode[j]);
				i.setDate_from(dpfDateFrom.getDate());
				i.setDate_to(dpfDateTo.getDate());
				i.setReason_id(getSelectBoxValue(sbReason));
				
				try {
					FacilityObject v = module.getItem(item_barcode[j]);
					
					if(FacilityModule.ITEM_STATUS_CHECKED_IN.equals(v.getStatus())){
						i.setCreatedby(getWhoModifyId());
						i.setCreatedby_date(new Date());
						i.setId(UuidGenerator.getInstance().getUuid());
							
						module.insertFInactive(i);
						
						v.setStatus(FacilityModule.ITEM_STATUS_INACTIVE);
						module.updateItem(v);
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
		return "fms/facility/inactiveForm";
	}

	public String getFacility_id() {
		return facility_id;
	}

	public void setFacility_id(String facility_id) {
		this.facility_id = facility_id;
	}
}
