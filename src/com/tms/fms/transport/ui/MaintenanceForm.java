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

public class MaintenanceForm extends Form{
	public static final String FORWARD_ADD_SUCCESS = "form.add.success";
	public static final String FORWARD_ADD_FAIL = "form.add.fail";
	public static final String FORWARD_EDIT_SUCCESS = "form.edit.success";
	public static final String FORWARD_EDIT_FAIL = "form.edit.fail";
	
	public static final String FORM_ACTION_ADD = "form.action.add";
	public static final String FORM_ACTION_EDIT = "form.action.edit";
	
	private Label lbVehicleNum;  
	private DatePopupField dpfServiceDate;
	private DatePopupField dpfSendDate;
	private SelectBox sbWorkshop;
	private TextBox tbWSAddress;
	private TextField tfOrderNum;
	private TextField tfInvoiceNum;
	private TextField tfCost;
	private TextBox tbReason;
	private TextBox tbRemark;
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
	    
	    addChild(new Label("lb1", Application.getInstance().getMessage("fms.tran.form.vehicleNumber", "Vehicle Number")));
	    lbVehicleNum = new Label("lbVehicleNum", vehicle_num);
		addChild(lbVehicleNum);
		
		addChild(new Label("lb2", Application.getInstance().getMessage("fms.tran.form.serviceDate", "Service Date")));
		dpfServiceDate = new DatePopupField("dpfServiceDate");
		dpfServiceDate.setFormat("dd-MM-yyyy");
		dpfServiceDate.setDate(new Date());
		addChild(dpfServiceDate);
		
		addChild(new Label("lb3", Application.getInstance().getMessage("fms.tran.form.sendDate", "Date send vehicle to workshop")));
		dpfSendDate = new DatePopupField("dpfSendDate");
		dpfSendDate.setFormat("dd-MM-yyyy");
		dpfSendDate.setDate(new Date());
		addChild(dpfSendDate);
	    
		addChild(new Label("lb4", Application.getInstance().getMessage("fms.tran.form.workshopName", "Workshop Name")));
		sbWorkshop = new SelectBox("sbWorkshop");
		sbWorkshop.setOptions(initialSelect);
	    try {
			TransportModule mod = (TransportModule)Application.getInstance().getModule(TransportModule.class);
			Collection lstWorkshop = mod.selectSetupObject(SetupObject.SETUP_WORKSHOP, "", "1", "name", false, 0, -1);
		    if (lstWorkshop.size() > 0) {
		    	for (Iterator i=lstWorkshop.iterator(); i.hasNext();) {
		        	SetupObject o = (SetupObject)i.next();
		        	sbWorkshop.setOptions(o.getSetup_id()+"="+o.getName());
		        }
		    }
		}catch (Exception e) {
		    Log.getLog(getClass()).error(e.toString());
		}
		sbWorkshop.addChild(new ValidatorSelectBoxNotEmpty("sbWorkshopNotEmpty", msgNotEmpty));
		sbWorkshop.setOnChange("updateAddress()");
	    addChild(sbWorkshop);
		
		addChild(new Label("lb5", Application.getInstance().getMessage("fms.tran.form.workshopAddress", "Workshop Address")));
		tbWSAddress = new TextBox("tbWSAddress");
		tbWSAddress.setCols("50");
		tbWSAddress.setRows("4");
		addChild(tbWSAddress);
		
		addChild(new Label("lb6", Application.getInstance().getMessage("fms.tran.form.jobOrderNumber", "Job Order Number")));
		tfOrderNum = new TextField("tfOrderNum");
		tfOrderNum.setSize("25");
		tfOrderNum.setMaxlength("255");
		addChild(tfOrderNum);
		
		addChild(new Label("lb7", Application.getInstance().getMessage("fms.tran.form.InvoiceNumber", "Invoice Number")));
		tfInvoiceNum = new TextField("tfInvoiceNum");
		tfInvoiceNum.setSize("25");
		tfInvoiceNum.setMaxlength("255");
		addChild(tfInvoiceNum);
		
		addChild(new Label("lb8", Application.getInstance().getMessage("fms.tran.form.cost", "Cost")));
		tfCost = new TextField("tfCost");
		tfCost.setSize("25");
		tfCost.setMaxlength("255");
		tfCost.addChild(new ValidatorIsNumeric("tfCostIsNumberic", msgIsNumberic));
		addChild(tfCost);
		
		addChild(new Label("lb9", Application.getInstance().getMessage("fms.tran.form.reason", "Reason for Maintenance")));
		tbReason = new TextBox("tbReason");
		tbReason.setCols("50");
		tbReason.setRows("4");
		addChild(tbReason);
		
		addChild(new Label("lb10", Application.getInstance().getMessage("fms.tran.form.remark", "Remark")));
		tbRemark = new TextBox("tbRemark");
		tbRemark.setCols("50");
		tbRemark.setRows("4");
		addChild(tbRemark);
		
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
			MaintenanceObject m = new MaintenanceObject();
			TransportModule module = (TransportModule)Application.getInstance().getModule(TransportModule.class);
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			try {
				m = module.getMaintenance(id);
			}catch (Exception e) {
				Log.getLog(getClass()).error(e.toString());
			}
			
			dpfServiceDate.setValue(formatter.format(m.getService_date()));
			dpfSendDate.setValue(formatter.format(m.getSend_date()));
			sbWorkshop.setSelectedOption(m.getWs_id());
			tbWSAddress.setValue(m.getWs_address());
			tfOrderNum.setValue(m.getOrder_num());
			tfInvoiceNum.setValue(m.getInv_num());
			tfCost.setValue(m.getCost());
			tbReason.setValue(m.getReason());
			tbRemark.setValue(m.getRemark());
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

		if (FORM_ACTION_EDIT.equals(action) || FORM_ACTION_ADD.equals(action)) {
			MaintenanceObject m = new MaintenanceObject();
			TransportModule module = (TransportModule)Application.getInstance().getModule(TransportModule.class);
			
			if (FORM_ACTION_EDIT.equals(action)) {
				try {
					m = module.getMaintenance(id);
				}catch (Exception e) {
					Log.getLog(getClass()).error(e.toString());
				}
			}
			
			m.setVehicle_num(vehicle_num);
			m.setService_date(dpfServiceDate.getDate());
			m.setSend_date(dpfSendDate.getDate());
			m.setWs_id(getSelectBoxValue(sbWorkshop));
			m.setOrder_num(tfOrderNum.getValue().toString());
			m.setInv_num(tfInvoiceNum.getValue().toString());
			m.setCost(tfCost.getValue().toString());
			m.setReason(tbReason.getValue().toString());
			m.setRemark(tbRemark.getValue().toString());
			
			if (FORM_ACTION_ADD.equals(action)) {
				try {
					m.setCreatedby(getWhoModifyId());
					m.setCreatedby_date(new Date());
					m.setId(UuidGenerator.getInstance().getUuid());
					
					module.insertMaintenance(m);
				}catch (Exception e) {
					Log.getLog(getClass()).error(e.toString()); 
					return new Forward(FORWARD_ADD_FAIL);} 
			}
			if (FORM_ACTION_EDIT.equals(action)) {
				try {
					m.setUpdatedby(getWhoModifyId());
					m.setUpdatedby_date(new Date());
					
					module.updateMaintenance(m);
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
		return "fms/transport/maintenanceForm";
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
}
