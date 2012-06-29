package com.tms.fms.transport.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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

public class InsuranceForm extends Form{
	public static final String FORWARD_ADD_SUCCESS = "form.add.success";
	public static final String FORWARD_ADD_FAIL = "form.add.fail";
	public static final String FORWARD_ADD_EXIST = "form.add.exist";
	public static final String FORWARD_EDIT_SUCCESS = "form.edit.success";
	public static final String FORWARD_EDIT_FAIL = "form.edit.fail";
	
	public static final String FORM_ACTION_ADD = "form.action.add";
	public static final String FORM_ACTION_EDIT = "form.action.edit";
	
	private Label lbVehicleNum;
	private DatePopupField dpfRTRenew;
	private TextField tfRTAmount;
	private DatePopupField dpfRTPeriodFrom;
	private DatePopupField dpfRTPeriodTo;
	private TextField tfISName;
	private DatePopupField dpfISRenew;
	private TextField tfISAmount;
	private DatePopupField dpfISPeriodFrom;
	private DatePopupField dpfISPeriodTo;
	private Button btnSubmit;
	private Button btnCancel;
	
	private String vehicle_num;
	private String id;
	private String action;
	private String cancelUrl = "";
	private String whoModifyId = "";
	
	public String getWhoModifyId() {
		return whoModifyId;
	}

	public void setWhoModifyId(String whoModifyId) {
		this.whoModifyId = whoModifyId;
	}

	public String getVehicle_num() {
		return vehicle_num;
	}
	
	public void setVehicle_num(String vehicle_num) {
		this.vehicle_num = vehicle_num;
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
	
	public void init() {
		if (action == null || (!FORM_ACTION_ADD.equals(action) && !FORM_ACTION_EDIT.equals(action))) {action = FORM_ACTION_ADD;}
		initForm();
    }
	
	public void onRequest(Event event) {
		initForm();
	    if (FORM_ACTION_EDIT.equals(action)) {populateFields();}
	    if (FORM_ACTION_ADD.equals(action)) {autoFields();}
    }
	
	public void initForm() {
		
	    Application application = Application.getInstance();
	    String msgNotEmpty  = Application.getInstance().getMessage("fms.tran.msg.mandatoryField", "Mandatory Field");
	    String initialSelect = "-1="+Application.getInstance().getMessage("fms.tran.msg.initialSelect", "--- NONE ---");
	    String msgIsNumberic = Application.getInstance().getMessage("fms.tran.msg.numbericField", "Numberic Field");

	    removeChildren();
	    setMethod("post");
	    setColumns(2);
	    
	    addChild(new Label("lb1", Application.getInstance().getMessage("fms.tran.form.vehicleNumber", "Vehicle Number")));
	    lbVehicleNum = new Label("lbVehicleNum", vehicle_num);
		addChild(lbVehicleNum);
		
		addChild(new Label("lb2", Application.getInstance().getMessage("fms.tran.form.roadTaxRenewalDate", "Road Tax Renewal Date")));
		dpfRTRenew = new DatePopupField("dpfRTRenew");
		dpfRTRenew.setFormat("dd-MM-yyyy");
		addChild(dpfRTRenew);
	    
		addChild(new Label("lb3", Application.getInstance().getMessage("fms.tran.form.roadTaxAmount", "Road Tax Amount")));
		tfRTAmount = new TextField("tfRTAmount");
		tfRTAmount.setSize("25");
		tfRTAmount.setMaxlength("255");
		tfRTAmount.addChild(new ValidatorIsNumeric("tfRTAmountIsNumberic", msgIsNumberic));
		addChild(tfRTAmount);
		
		addChild(new Label("lb4", Application.getInstance().getMessage("fms.tran.form.roadTaxPeriodCovered", "Road Tax Period Covered")));
		Panel pnRTPeriod = new Panel("pnRTPeriod");
		pnRTPeriod.addChild(new Label("lb5", Application.getInstance().getMessage("fms.tran.form.from", "From")));
		dpfRTPeriodFrom = new DatePopupField("dpfRTPeriodFrom");
		dpfRTPeriodFrom.setFormat("dd-MM-yyyy");
		pnRTPeriod.addChild(dpfRTPeriodFrom);
		pnRTPeriod.addChild(new Label("lb6", Application.getInstance().getMessage("fms.tran.form.to", "To")));
		dpfRTPeriodTo = new DatePopupField("dpfRTPeriodTo");
		dpfRTPeriodTo.setFormat("dd-MM-yyyy");
		pnRTPeriod.addChild(dpfRTPeriodTo);
		addChild(pnRTPeriod);
		
		addChild(new Label("lb7", Application.getInstance().getMessage("fms.tran.form.insuranceRenewalDate", "Insurance Renewal Date")));
		dpfISRenew = new DatePopupField("dpfISRenew");
		dpfISRenew.setFormat("dd-MM-yyyy");
		addChild(dpfISRenew);
	    
		addChild(new Label("lb12", Application.getInstance().getMessage("fms.tran.form.insuranceCompanyName", "Insurance Company Name")));
		tfISName = new TextField("tfISName");
		tfISName.setSize("50");
		tfISName.setMaxlength("255");
		addChild(tfISName);
		
		addChild(new Label("lb8", Application.getInstance().getMessage("fms.tran.form.insuranceAmount", "Insurance Amount")));
		tfISAmount = new TextField("tfISAmount");
		tfISAmount.setSize("25");
		tfISAmount.setMaxlength("255");
		tfISAmount.addChild(new ValidatorIsNumeric("tfISAmountIsNumberic", msgIsNumberic));
		addChild(tfISAmount);
		
		addChild(new Label("lb9", Application.getInstance().getMessage("fms.tran.form.insurancePeriodCovered", "Insurance Period Covered")));
		Panel pnISPeriod = new Panel("pnISPeriod");
		pnISPeriod.addChild(new Label("lb10", Application.getInstance().getMessage("fms.tran.form.from", "From")));
		dpfISPeriodFrom = new DatePopupField("dpfISPeriodFrom");
		dpfISPeriodFrom.setFormat("dd-MM-yyyy");
		pnISPeriod.addChild(dpfISPeriodFrom);
		pnISPeriod.addChild(new Label("lb11", Application.getInstance().getMessage("fms.tran.form.to", "To")));
		dpfISPeriodTo = new DatePopupField("dpfISPeriodTo");
		dpfISPeriodTo.setFormat("dd-MM-yyyy");
		pnISPeriod.addChild(dpfISPeriodTo);
		addChild(pnISPeriod);
		
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
			InsuranceObject i = new InsuranceObject();
			TransportModule module = (TransportModule)Application.getInstance().getModule(TransportModule.class);
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			try {
				i = module.getInsurance(id);
			}catch (Exception e) {
				Log.getLog(getClass()).error(e.toString());
			}
			
			dpfRTRenew.setValue(formatter.format(i.getRt_renew()));
			tfRTAmount.setValue(i.getRt_amount());
			dpfRTPeriodFrom.setValue(formatter.format(i.getRt_period_from()));
			dpfRTPeriodTo.setValue(formatter.format(i.getRt_period_to()));
			tfISName.setValue(i.getIs_name());
			dpfISRenew.setValue(formatter.format(i.getIs_renew()));
			tfISAmount.setValue(i.getIs_amount());
			dpfISPeriodFrom.setValue(formatter.format(i.getIs_period_from()));
			dpfISPeriodTo.setValue(formatter.format(i.getIs_period_to()));
		}
	}
	
	public void autoFields() {
		if (FORM_ACTION_ADD.equals(action)){
			InsuranceObject i = new InsuranceObject();
			TransportModule module = (TransportModule)Application.getInstance().getModule(TransportModule.class);
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			try {
				i = module.getLastestInsurance(vehicle_num);
			}catch (Exception e) {
				Log.getLog(getClass()).error(e.toString());
			}
			
			dpfISRenew.setValue(formatter.format(new Date()));
			dpfRTRenew.setValue(formatter.format(new Date()));
			
			if(i.getId()!=null || !"".equals(i.getId()) || !"null".equals(i.getId())){
				
				tfRTAmount.setValue(i.getRt_amount());
				Calendar cRTPeriodForm = Calendar.getInstance();
				Calendar cRTPeriodTo = Calendar.getInstance();
				Calendar cISPeriodForm = Calendar.getInstance();
				Calendar cISPeriodTo = Calendar.getInstance();
				cRTPeriodForm.setTime(i.getRt_period_from());
				cRTPeriodTo.setTime(i.getRt_period_to());
				cISPeriodForm.setTime(i.getIs_period_from());
				cISPeriodTo.setTime(i.getIs_period_to());
				cRTPeriodForm.set(Calendar.YEAR, cRTPeriodForm.get(Calendar.YEAR)+1);
				cRTPeriodTo.set(Calendar.YEAR, cRTPeriodTo.get(Calendar.YEAR)+1);
				cISPeriodForm.set(Calendar.YEAR, cISPeriodForm.get(Calendar.YEAR)+1);
				cISPeriodTo.set(Calendar.YEAR, cISPeriodTo.get(Calendar.YEAR)+1);
				dpfRTPeriodFrom.setValue(formatter.format(cRTPeriodForm.getTime()));
				dpfRTPeriodTo.setValue(formatter.format(cRTPeriodTo.getTime()));
				tfISName.setValue(i.getIs_name());
				tfISAmount.setValue(i.getIs_amount());
				dpfISPeriodFrom.setValue(formatter.format(cISPeriodForm.getTime()));
				dpfISPeriodTo.setValue(formatter.format(cISPeriodTo.getTime()));
			}else{
				tfRTAmount.setValue("");
				Calendar cRTPeriodForm = Calendar.getInstance();
				Calendar cRTPeriodTo = Calendar.getInstance();
				Calendar cISPeriodForm = Calendar.getInstance();
				Calendar cISPeriodTo = Calendar.getInstance();
				cRTPeriodForm.set(Calendar.YEAR, cRTPeriodForm.get(Calendar.YEAR));
				cRTPeriodTo.set(Calendar.YEAR, cRTPeriodTo.get(Calendar.YEAR)+1);
				cISPeriodForm.set(Calendar.YEAR, cISPeriodForm.get(Calendar.YEAR));
				cISPeriodTo.set(Calendar.YEAR, cISPeriodTo.get(Calendar.YEAR)+1);
				dpfRTPeriodFrom.setValue(formatter.format(cRTPeriodForm.getTime()));
				dpfRTPeriodTo.setValue(formatter.format(cRTPeriodTo.getTime()));
				tfISName.setValue("");
				tfISAmount.setValue("");
				dpfISPeriodFrom.setValue(formatter.format(cISPeriodForm.getTime()));
				dpfISPeriodTo.setValue(formatter.format(cISPeriodTo.getTime()));
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
	
	public Forward onValidate(Event event) {

		if(dpfRTPeriodFrom.getDate().compareTo(dpfRTPeriodTo.getDate())==0	||
			dpfRTPeriodFrom.getDate().after(dpfRTPeriodTo.getDate())){
			
	    	dpfRTPeriodFrom.setInvalid(true);
	    	dpfRTPeriodTo.setInvalid(true);
	    	return new Forward(FORWARD_ADD_FAIL);	    		    	
	    }
		
		if(dpfISPeriodFrom.getDate().compareTo(dpfISPeriodTo.getDate())==0	||
				dpfISPeriodFrom.getDate().after(dpfISPeriodTo.getDate())){
				
				dpfISPeriodFrom.setInvalid(true);
				dpfISPeriodTo.setInvalid(true);
		    	return new Forward(FORWARD_ADD_FAIL);	    		    	
		    }
		
		if (FORM_ACTION_EDIT.equals(action) || FORM_ACTION_ADD.equals(action)) {
			InsuranceObject i = new InsuranceObject();
			TransportModule module = (TransportModule)Application.getInstance().getModule(TransportModule.class);
			
			if (FORM_ACTION_EDIT.equals(action)) {
				try {
					i = module.getInsurance(id);
				}catch (Exception e) {
					Log.getLog(getClass()).error(e.toString());
				}
			}
			
			i.setVehicle_num(vehicle_num);
			i.setRt_renew(dpfRTRenew.getDate());
			i.setRt_amount(tfRTAmount.getValue().toString());
			i.setRt_period_from(dpfRTPeriodFrom.getDate());
			i.setRt_period_to(dpfRTPeriodTo.getDate());
			i.setIs_amount(tfISAmount.getValue().toString());
			i.setIs_name(tfISName.getValue().toString());
			i.setIs_period_from(dpfISPeriodFrom.getDate());
			i.setIs_period_to(dpfISPeriodTo.getDate());
			i.setIs_renew(dpfISRenew.getDate());
			
			if (FORM_ACTION_ADD.equals(action)) {

				try {
					i.setCreatedby(getWhoModifyId());
					i.setCreatedby_date(new Date());
					i.setId(UuidGenerator.getInstance().getUuid());
					
					module.insertInsurance(i);
				}catch (Exception e) {
					Log.getLog(getClass()).error(e.toString()); 
					return new Forward(FORWARD_ADD_FAIL);} 
			}
			if (FORM_ACTION_EDIT.equals(action)) {
				try {
					i.setUpdatedby(getWhoModifyId());
					i.setUpdatedby_date(new Date());
					
					module.updateInsurance(i);
					return new Forward(FORWARD_EDIT_SUCCESS);
				}catch (Exception e) {
					Log.getLog(getClass()).error(e.toString()); 
					return new Forward(FORWARD_EDIT_FAIL);
				} 
			}
		}
		return new Forward(FORWARD_ADD_SUCCESS);
	}	
	
	public String getDefaultTemplate() {
		return "fms/transport/insuranceForm";
	}
}
