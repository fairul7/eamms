package com.tms.fms.transport.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.tms.fms.transport.model.*;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.*;
import kacang.stdui.validator.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.*;

public class RateCardForm extends Form{
	public static final String FORWARD_ADD_SUCCESS = "form.add.success";
	public static final String FORWARD_ADD_FAIL = "form.add.fail";
	public static final String FORWARD_EDIT_SUCCESS = "form.edit.success";
	public static final String FORWARD_EDIT_FAIL = "form.edit.fail";
	public static final String FORWARD_ADD_EXIST = "form.add.exist";
	
	public static final String FORM_ACTION_ADD = "form.action.add";
	public static final String FORM_ACTION_EDIT = "form.action.edit";
	public static final String FORM_ACTION_VIEW = "form.action.view";
	public static final String FORM_ACTION_DRIVER = "form.action.driver";
	
	private Label lbName;
	private Label lbCurrentCharge;
	private Label lbChargesBasedOn;
	private Label lbChargeAmount;
	
	private TextField tfName;
	private TextField tfCharge;
	private DatePopupField dpfEffectiveDate;
	private Radio rdPerHour;
	private Radio rdByMileage;
	
	private Button btnSubmit;
	private Button btnCancel;
	private Button btnHistory;
	
	private String action;
	private String id;
	private String cancelUrl;
	private String historyUrl;
	private String whoModifyId = "";
	
	private SelectBox category;
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCancelUrl() {
		return cancelUrl;
	}

	public void setCancelUrl(String cancelUrl) {
		this.cancelUrl = cancelUrl;
	}

	public String getHistoryUrl() {
		return historyUrl;
	}

	public void setHistoryUrl(String historyUrl) {
		this.historyUrl = historyUrl;
	}

	public String getWhoModifyId() {
		return whoModifyId;
	}

	public void setWhoModifyId(String whoModifyId) {
		this.whoModifyId = whoModifyId;
	}

	public void init() {
		if (action == null || 
			(!FORM_ACTION_ADD.equals(action) && 
			 !FORM_ACTION_EDIT.equals(action) && 
			 !FORM_ACTION_VIEW.equals(action) && 
			 !FORM_ACTION_DRIVER.equals(action))) {
			action = FORM_ACTION_ADD;
		}
    }
	
	public void onRequest(Event event) {
		initForm();
	    if (FORM_ACTION_EDIT.equals(action) || 
	        FORM_ACTION_VIEW.equals(action) || 
	        FORM_ACTION_DRIVER.equals(action)) {
	    	populateFields();
	    }
    }
	
	public void initForm() {
		
	    Application application = Application.getInstance();
	    String msgNotEmpty  = Application.getInstance().getMessage("fms.tran.msg.mandatoryField", "Mandatory Field");
	    String initialSelect = "-1="+Application.getInstance().getMessage("fms.tran.msg.initialSelect", "--- NONE ---");
	    String msgIsNumberic = Application.getInstance().getMessage("fms.tran.msg.numbericField", "Numberic Field");

	    removeChildren();
	    setMethod("post");
	    setColumns(2);
	    
	    if(FORM_ACTION_DRIVER.equals(action)){
	    	addChild(new Label("lb1", Application.getInstance().getMessage("fms.tran.form.currentCharges", "Current Charges")));
	    	lbCurrentCharge = new Label("lbCurrentCharge");
			addChild(lbCurrentCharge);
			
	    	addChild(new Label("lb2", Application.getInstance().getMessage("fms.tran.form.chargesPerHour*", "Charges Per Hour*")));
	    	tfCharge = new TextField("tfCharge");
	    	tfCharge.setSize("25");
	    	tfCharge.setMaxlength("255");
	    	tfCharge.addChild(new ValidatorIsNumeric("tfChargeIsNumberic", msgIsNumberic));
			addChild(tfCharge);
	    	
	    	addChild(new Label("lb3", Application.getInstance().getMessage("fms.tran.form.effectDate*", "Effective Date*")));
	    	dpfEffectiveDate = new DatePopupField("dpfEffectiveDate");
	    	dpfEffectiveDate.setFormat("dd-MM-yyyy");
	    	dpfEffectiveDate.setDate(new Date());
			addChild(dpfEffectiveDate);
			
			addChild(new Label("lbbutton", ""));
			Panel pnButton = new Panel("pnButton");
			btnSubmit = new Button("btnSubmit", Application.getInstance().getMessage("fms.tran.save", "Save"));
	    	pnButton.addChild(btnSubmit);
	    	btnHistory = new Button("btnHistory", Application.getInstance().getMessage("fms.tran.historyTracking", "History Tracking"));
	    	pnButton.addChild(btnHistory);
	    	addChild(pnButton);
	    }
	    
	    if(FORM_ACTION_ADD.equals(action) || FORM_ACTION_EDIT.equals(action)){
	    	addChild(new Label("lb4", Application.getInstance().getMessage("fms.tran.form.transportChargesName*", "Transport Charges Name*")));
	    	/*tfName = new TextField("tfName");
	    	tfName.setSize("25");
	    	tfName.setMaxlength("255");
	    	tfName.addChild(new ValidatorNotEmpty("tfNameNotEmpty", msgNotEmpty));
			addChild(tfName);*/
			
	    	addChild(new Label("lb5", Application.getInstance().getMessage("fms.tran.form.chargesBasedOn*", "Charges Based On*")));
	    	Panel pnType = new Panel("pnType");
			rdPerHour = new Radio("rdPerHour", Application.getInstance().getMessage("fms.tran.form.perHour", "Per Hour"), true);
			rdByMileage = new Radio("rdByMileage", Application.getInstance().getMessage("fms.tran.form.byMileage", "By Mileage"));
			rdPerHour.setGroupName("typeGroup");
			rdByMileage.setGroupName("typeGroup");
			pnType.addChild(rdPerHour);
			pnType.addChild(rdByMileage);
			addChild(pnType);
			
			category = new SelectBox("category");
			try{
				Collection collcat = new ArrayList();
	    		TransportModule tran = (TransportModule) Application.getInstance().getModule(TransportModule.class);        
	    		
	    		collcat = tran.selectSetupObject("fms_tran_category",null,"-1",null,false,0,-1);
	    		category.addOption("-1", "--- NONE ---");
	    		for(Iterator it = collcat.iterator(); it.hasNext(); ){
	    			SetupObject so = (SetupObject) it.next();
	    			String id = so.getSetup_id();
	    			String name = so.getName();
	    			category.addOption(name, name);
	    		}
			}catch(Exception er){
				
			}
			addChild(category);
	    	
	    	addChild(new Label("lb6", Application.getInstance().getMessage("fms.tran.form.chargeAmount(RM)*", "Charge Amount(RM)*")));
	    	tfCharge = new TextField("tfCharge");
	    	tfCharge.setSize("25");
	    	tfCharge.setMaxlength("255");
	    	tfCharge.addChild(new ValidatorIsNumeric("tfChargeIsNumberic", msgIsNumberic));
			addChild(tfCharge);
			
	    	addChild(new Label("lb7", Application.getInstance().getMessage("fms.tran.form.effectiveDate*", "Effective Date*")));
	    	dpfEffectiveDate = new DatePopupField("dpfEffectiveDate");
	    	dpfEffectiveDate.setFormat("dd-MM-yyyy");
	    	dpfEffectiveDate.setDate(new Date());
			addChild(dpfEffectiveDate);
			
			addChild(new Label("lbbutton", ""));
			Panel pnButton = new Panel("pnButton");
			btnSubmit = new Button("btnSubmit", Application.getInstance().getMessage("fms.tran.submit", "Submit"));
	    	pnButton.addChild(btnSubmit);
	    	btnCancel = new Button("btnCancel", Application.getInstance().getMessage("fms.tran.cancel", "Cancel"));
		    pnButton.addChild(btnCancel);
	    	addChild(pnButton);
	    }
	    
	    if(FORM_ACTION_VIEW.equals(action)){
	    	addChild(new Label("lb8", Application.getInstance().getMessage("fms.tran.form.transportChargesName", "Transport Charges Name")));
	    	lbName = new Label("lbName");
			addChild(lbName);
			
	    	addChild(new Label("lb9", Application.getInstance().getMessage("fms.tran.form.chargesBasedOn", "Charges Based On")));
	    	lbChargesBasedOn = new Label("lbChargesBasedOn");
			addChild(lbChargesBasedOn);
			
	    	addChild(new Label("lb10", Application.getInstance().getMessage("fms.tran.form.chargeAmount(RM)", "Charge Amount(RM)")));
	    	lbChargeAmount = new Label("lbChargeAmount");
			addChild(lbChargeAmount);
			
			addChild(new Label("lbbutton", ""));
			Panel pnButton = new Panel("pnButton");
			btnCancel = new Button("btnCancel", Application.getInstance().getMessage("fms.tran.back", "Back"));
		    pnButton.addChild(btnCancel);
	    	addChild(pnButton);
	    }
	}
	
	public void populateFields() {
		TransportModule module = (TransportModule)Application.getInstance().getModule(TransportModule.class);
		RateCardObject o = new RateCardObject();
		try {	
			o = module.getRateCard(id, new Date());
			
			if(FORM_ACTION_DRIVER.equals(action)){
				if(o.getAmount()==null || "null".equals(o.getAmount())||"".equals(o.getAmount())){
					o.setAmount("0");
				}
				lbCurrentCharge.setText("RM " + o.getAmount() + " per Hour");
			}
			
			if(FORM_ACTION_EDIT.equals(action)){			
				
				category.setSelectedOptions(new String[]{o.getName()});
				rdPerHour.setChecked(("H".equals(o.getType())));
				rdByMileage.setChecked(("M".equals(o.getType())));
				tfCharge.setValue(o.getAmount());
			}
			
			if(FORM_ACTION_VIEW.equals(action)){
				lbName.setText(o.getName());
				lbChargesBasedOn.setText((o.getType() == "H")?Application.getInstance().getMessage("fms.tran.form.perHour", "Per Hour"):Application.getInstance().getMessage("fms.tran.form.byMileage", "By Mileage"));
				lbChargeAmount.setText(o.getAmount());
			}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.toString());
		}
	}
	
	public Forward onSubmit(Event evt) {
	    String buttonName = findButtonClicked(evt);
	    kacang.ui.Forward result = super.onSubmit(evt);
	    if(FORM_ACTION_ADD.equals(action)||FORM_ACTION_EDIT.equals(action)){
	    	
	    	 if("-1".equals(category.getSelectedOptions().keySet().iterator().next())){	    		
		    		category.setInvalid(true);
		    		setInvalid(true);    		
		    	}
		    	     
	    	 
		    if (buttonName != null && btnCancel.getAbsoluteName().equals(buttonName)) {
		    	init();
		    	
		    	return new Forward(Form.CANCEL_FORM_ACTION, cancelUrl, true);
		    }else{
	    		return result;
	    	}
	    }else if(FORM_ACTION_DRIVER.equals(action)){
	    	if (buttonName != null && btnHistory.getAbsoluteName().equals(buttonName)) {
		    	init();
		    	return new Forward(Form.CANCEL_FORM_ACTION, historyUrl, true);
		    }else{
	    		return result;
	    	}
	    }else if(FORM_ACTION_VIEW.equals(action)){
	    	if (buttonName != null && btnCancel.getAbsoluteName().equals(buttonName)) {
		    	init();
		    	return new Forward(Form.CANCEL_FORM_ACTION, cancelUrl, true);
		    }else{
	    		return result;
	    	}
	    }else {
	    	return result;
	    }
	}
	
	public Forward onValidate(Event event) {
		RateCardObject o = new RateCardObject();
		TransportModule module = (TransportModule)Application.getInstance().getModule(TransportModule.class);
		
		if(FORM_ACTION_ADD.equals(action)||FORM_ACTION_EDIT.equals(action)){
			if(FORM_ACTION_EDIT.equals(action)){
				try {
					o = module.getRateCard(id, new Date());
				}catch (Exception e) {
					Log.getLog(getClass()).error(e.toString());
				}
			}
			
			//String tmpName = tfName.getValue().toString();
			String tmpName = (String)category.getSelectedOptions().keySet().iterator().next();
			if(module.isRateCardExist(tmpName) && !tmpName.equals(o.getName())){
				return new Forward(FORWARD_ADD_EXIST);
			}
			
			o.setName(tmpName);
			o.setType((rdPerHour.isChecked())?"H":"M");
			o.setEffective_date(dpfEffectiveDate.getDate());
			o.setAmount(tfCharge.getValue().toString());
			o.setCreatedby(whoModifyId);
			o.setCreatedby_date(new Date());
			o.setDetail_id(UuidGenerator.getInstance().getUuid());
			
			if(FORM_ACTION_ADD.equals(action)){
				try{
					o.setSetup_id(UuidGenerator.getInstance().getUuid());
					module.insertRateCard(o);
					return new Forward(FORWARD_ADD_SUCCESS);
				}catch(Exception e){
					Log.getLog(getClass()).error(e.toString());
					return new Forward(FORWARD_ADD_FAIL);
				}
			}
			if(FORM_ACTION_EDIT.equals(action)){
				try{
					module.updateRateCard(o);
					return new Forward(FORWARD_EDIT_SUCCESS);
				}catch(Exception e){
					Log.getLog(getClass()).error(e.toString());
					return new Forward(FORWARD_EDIT_FAIL);
				}
			}
		}else if(FORM_ACTION_DRIVER.equals(action)){
			try {
				o.setSetup_id("default");
				o.setName("Driver");
				o.setType("H");
				o.setDetail_id(UuidGenerator.getInstance().getUuid());
				o.setAmount(tfCharge.getValue().toString());
				o.setEffective_date(dpfEffectiveDate.getDate());
				o.setCreatedby(whoModifyId);
				o.setCreatedby_date(new Date());
				
				module.updateRateCard(o);
				return new Forward(FORWARD_EDIT_SUCCESS);
			}catch (Exception e) {
				Log.getLog(getClass()).error(e.toString());
				return new Forward(FORWARD_EDIT_FAIL);
			}
		}
		return new Forward(FORWARD_EDIT_FAIL);
	}
	
	public String getDefaultTemplate() {
		return "fms/transport/rateCardForm";
	}
}
