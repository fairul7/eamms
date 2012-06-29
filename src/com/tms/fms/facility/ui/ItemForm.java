package com.tms.fms.facility.ui;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.facility.model.*;
import com.tms.fms.facility.ui.CategoryForm.ValidatorParentSelected;
import com.tms.fms.transport.model.SetupObject;
import com.tms.fms.transport.model.TransportModule;
import com.tms.fms.transport.model.WriteoffObject;
import com.tms.fms.department.model.*;

import kacang.Application;
import kacang.stdui.*;
import kacang.stdui.validator.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.*;

public class ItemForm extends Form{

	public static final String FORWARD_ADD_SUCCESS = "form.add.success";
	public static final String FORWARD_ADD_SUCCESS_CONT = "form.add.success.continue";
	public static final String FORWARD_ADD_FAIL = "form.add.fail";
	public static final String FORWARD_ADD_EXIST = "form.add.exist";
	public static final String FORWARD_EDIT_SUCCESS = "form.edit.success";
	public static final String FORWARD_EDIT_FAIL = "form.edit.fail";
	
	public static final String FORM_ACTION_ADD = "form.action.add";
	public static final String FORM_ACTION_EDIT = "form.action.edit";
	public static final String FORM_ACTION_VIEW = "form.action.view";
	
	private Label lbItemName;
	private Label lbItemCode;
	private Label lbPurchasedDate;
	private Label lbPurchasedCost;
	private Label lbDONum;
	private Label lbEAsset;
	private Label lbLocation;
	private Label lbReplacement;
	private Label lbDate;
	private Label lbReason;
	private Label lbBy;
	private Link liLink;
	
	private TextField tfItemCode;
	private DatePopupField dpfPurchasedDate;
	private TextField tfPurchasedCost;
	private TextBox tfDONum;
	private TextField tfEAsset;
	private SelectBox sbLocation;
	private CheckBox cbReplacement;
	private Button btnSubmit;
	private Button btnSubmitNCont;
	private Button btnCancel;
	private Button btnStatus;
	private Button btnEdit;
	
	private String itemCode;
	private String facilityId;
	private String action;
	private String cancelUrl = "";
	private String statusUrl = "";
	private String editUrl = "";
	private String whoModifyId = "";
	private boolean isContinue = false;
	private String status = FacilityModule.ITEM_STATUS_CHECKED_IN;
	private String mode;
	
	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getStatusUrl() {
		return statusUrl;
	}

	public void setStatusUrl(String statusUrl) {
		this.statusUrl = statusUrl;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEditUrl() {
		return editUrl;
	}

	public void setEditUrl(String editUrl) {
		this.editUrl = editUrl;
	}

	public String getItemCode() {
		return itemCode;
	}
	
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
	public String getFacilityId() {
		return facilityId;
	}
	
	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
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
	
	public void onRequest(Event event) {
		initForm();
	    if (FORM_ACTION_EDIT.equals(action) || FORM_ACTION_VIEW.equals(action)) {populateFields();}
    }
	
	public Collection getAllRequestList() {
		EngineeringModule mod = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		return mod.getFCHeadRequestListByBarcode(lbItemCode.getText());
	}
	
	public Collection getInternalCheckoutHistoryList() {
		EngineeringModule mod = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		return mod.getInternalCheckoutListByBarcode(lbItemCode.getText());
	}
	
	public void initForm() {
		
	    Application application = Application.getInstance();
	    String msgNotEmpty  = Application.getInstance().getMessage("fms.tran.msg.mandatoryField", "Mandatory Field");
	    String initialSelect = "-1="+Application.getInstance().getMessage("fms.tran.msg.initialSelect", "--- NONE ---");
	    String msgIsNumberic = Application.getInstance().getMessage("fms.tran.msg.numbericField", "Numberic Field");

	    removeChildren();
	    setMethod("post");
	    setColumns(2);
	    
	    addChild(new Label("lb1", Application.getInstance().getMessage("fms.facility.form.itemName", "Item Name*")));
	    lbItemName = new Label("lbItemName");
	    try {
			FacilityModule mod = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
			FacilityObject o=mod.getFacility(facilityId);
			lbItemName.setText(o.getName());
		}catch (Exception e) {
		    Log.getLog(getClass()).error(e.toString());
		}
	    addChild(lbItemName);
		
	    addChild(new Label("lb2", Application.getInstance().getMessage("fms.facility.form.itemCode", "Item Code")));
	    if (FORM_ACTION_ADD.equals(action)) {
		    tfItemCode = new TextField("tfItemCode");
	    	tfItemCode.setSize("25");
	    	tfItemCode.setMaxlength("255");
	    	tfItemCode.addChild(new ValidatorNotEmpty("tfItemCodeNotEmpty", msgNotEmpty));
	    	tfItemCode.setOnChange("checkLength()");
		    addChild(tfItemCode);
	    }else{
	    	lbItemCode = new Label("lbItemCode");
	    	addChild(lbItemCode);
	    }
	    
	    addChild(new Label("lb3", Application.getInstance().getMessage("fms.facility.form.purchasedDate", "Purchased Date")));
	    if (FORM_ACTION_ADD.equals(action) || FORM_ACTION_EDIT.equals(action)) {
		    dpfPurchasedDate = new DatePopupField("dpfPurchasedDate");
		    dpfPurchasedDate.setFormat("dd-MM-yyyy");
		    dpfPurchasedDate.setDate(new Date());
		    addChild(dpfPurchasedDate);
	    }else{
	    	lbPurchasedDate = new Label("lbPurchasedDate");
	    	addChild(lbPurchasedDate);
	    }
	    
	    addChild(new Label("lb4", Application.getInstance().getMessage("fms.facility.form.purchasedCost", "Purchased Cost")));
	    if (FORM_ACTION_ADD.equals(action) || FORM_ACTION_EDIT.equals(action)) {
		    tfPurchasedCost = new TextField("tfPurchasedCost");
		    tfPurchasedCost.setSize("25");
		    tfPurchasedCost.setMaxlength("255");
		    tfPurchasedCost.addChild(new ValidatorIsNumeric("tfPurchasedCostIsNumberic", msgIsNumberic));
	    	addChild(tfPurchasedCost);
		}else{
			lbPurchasedCost = new Label("lbPurchasedCost");
			addChild(lbPurchasedCost);
	    }
    	
	    addChild(new Label("lb5", Application.getInstance().getMessage("fms.facility.form.doNumber", "DO Number")));
	    if (FORM_ACTION_ADD.equals(action) || FORM_ACTION_EDIT.equals(action)) {
		    tfDONum = new TextBox("tfDONum");
		    tfDONum.setCols("50");
		    tfDONum.setRows("5");
	    	addChild(tfDONum);
		}else{
			lbDONum = new Label("lbDONum");
			addChild(lbDONum);
	    }
	    
	    addChild(new Label("lb6", Application.getInstance().getMessage("fms.facility.form.eAssetNumber", "E-Asset Number")));
	    if (FORM_ACTION_ADD.equals(action) || FORM_ACTION_EDIT.equals(action)) {
		    tfEAsset = new TextField("tfEAsset");
		    tfEAsset.setSize("25");
		    tfEAsset.setMaxlength("255");
	    	addChild(tfEAsset);
		}else{
			lbEAsset = new Label("lbEAsset");
			addChild(lbEAsset);
			
			lbDate = new Label("lbDate");
			addChild(lbDate);
			lbReason= new Label("lbReason");
			addChild(lbReason);
			lbBy = new Label("lbBy");
			addChild(lbBy);
			liLink = new Link("liLink");
			addChild(liLink);
	    }
	    
	    addChild(new Label("lb7", Application.getInstance().getMessage("fms.facility.form.location", "Location*")));
		
	    if (FORM_ACTION_ADD.equals(action) || FORM_ACTION_EDIT.equals(action)) {
			sbLocation = new SelectBox("sbLocation");
			sbLocation.setOptions(initialSelect);
		    try {
				TransportModule mod = (TransportModule)Application.getInstance().getModule(TransportModule.class);
				Collection lstLocation = mod.selectSetupObject(SetupObject.SETUP_LOCATION, "", "1", "name", false, 0, -1);
			    if (lstLocation.size() > 0) {
			    	for (Iterator i=lstLocation.iterator(); i.hasNext();) {
			        	SetupObject o = (SetupObject)i.next();
			        	sbLocation.setOptions(o.getSetup_id()+"="+o.getName());
			        }
			    }
			}catch (Exception e) {
			    Log.getLog(getClass()).error(e.toString());
			}
			sbLocation.addChild(new ValidatorSelectBoxNotEmpty("sbLocationNotEmpty", msgNotEmpty));
			addChild(sbLocation);
		}else{
	    	lbLocation = new Label("lbLocation");
	    	addChild(lbLocation);
		}
	    
	    addChild(new Label("lb7", Application.getInstance().getMessage("fms.facility.form.replacement", "Replacement")));
		
	    if (FORM_ACTION_ADD.equals(action) || FORM_ACTION_EDIT.equals(action)) {
			cbReplacement = new CheckBox("cbReplacement");
			addChild(cbReplacement);
		}else{
	    	lbReplacement = new Label("lbReplacement");
	    	addChild(lbReplacement);
		}
	    
		addChild(new Label("lbbutton", ""));
		Panel pnButton = new Panel("pnButton");
	    if (FORM_ACTION_ADD.equals(action)) {
	    	btnSubmit = new Button("btnSubmit", Application.getInstance().getMessage("fms.facility.submit", "Submit"));
	    	pnButton.addChild(btnSubmit);
	    	btnSubmitNCont = new Button("btnSubmitNCont", Application.getInstance().getMessage("fms.facility.submitAndContinue", "Submit & Continue"));
	    	pnButton.addChild(btnSubmitNCont);
	    	btnCancel = new Button("btnCancel", Application.getInstance().getMessage("fms.facility.cancel", "Cancel"));
		    pnButton.addChild(btnCancel);
	    }
		if (FORM_ACTION_EDIT.equals(action)) {
			btnSubmit = new Button("btnSubmit", Application.getInstance().getMessage("fms.facility.update", "Update"));
			pnButton.addChild(btnSubmit);
			btnCancel = new Button("btnCancel", Application.getInstance().getMessage("fms.facility.cancel", "Cancel"));
		    pnButton.addChild(btnCancel);
		}   
		if (FORM_ACTION_VIEW.equals(action)) {
			btnEdit = new Button("btnEdit", Application.getInstance().getMessage("fms.facility.edit", "Edit"));
			pnButton.addChild(btnEdit);
			btnStatus = new Button("btnStatus", Application.getInstance().getMessage("fms.tran.form.statusLogs", "Status Logs"));
			pnButton.addChild(btnStatus);
			btnCancel = new Button("btnCancel", Application.getInstance().getMessage("fms.facility.back", "Back"));
		    pnButton.addChild(btnCancel);
		}  
		
	    addChild(pnButton);  
	}
	
	public void populateFields() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		if (FORM_ACTION_EDIT.equals(action)){
			FacilityObject o = new FacilityObject();
			FacilityModule mod = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
			try {
				o = mod.getItem(itemCode);
			}catch (Exception e) {
				Log.getLog(getClass()).error(e.toString());
			}
			
			lbItemName.setText(o.getName());
			lbItemCode.setText(o.getBarcode());
			dpfPurchasedDate.setValue(formatter.format(o.getPurchased_date()));
			tfPurchasedCost.setValue(o.getPurchased_cost());
			tfDONum.setValue(o.getDo_num());
			tfEAsset.setValue(o.getEasset_num());
			if(o.getLocation_id() != null || !"".equals(o.getLocation_id())){
				sbLocation.setSelectedOption(o.getLocation_id());
			}
			cbReplacement.setChecked("Y".equals(o.getReplacement()));
		}else if(FORM_ACTION_VIEW.equals(action)){
			FacilityObject o = new FacilityObject();
			FacilityModule mod = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
			try {
				o = mod.getItem(itemCode);
			}catch (Exception e) {
				Log.getLog(getClass()).error(e.toString());
			}
			
			lbItemName.setText(o.getName());
			lbItemCode.setText(o.getBarcode());
			lbPurchasedDate.setText(formatter.format(o.getPurchased_date()));
			lbPurchasedCost.setText(o.getPurchased_cost());
			lbDONum.setText(o.getDo_num());
			lbEAsset.setText(o.getEasset_num());
			String location = o.getLocation_name();
			if(location == null || "".equals(location)){
				location = "  -  ";
			}
			lbLocation.setText(location);
			lbReplacement.setText(("Y".equals(o.getReplacement())?"Yes":"No"));
			status = o.getStatus();
			if(FacilityModule.ITEM_STATUS_WRITE_OFF.equals(status)){
				ClosedItemObject w = mod.getWriteoff(itemCode);
				lbDate.setText(formatter.format(w.getDate()));
	    		lbReason.setText(w.getReason());
	    		lbBy.setText(w.getCreatedby_name());
	    		if(!"".equals(w.getFile_name())){
	    			liLink.setText(w.getFile_name());
	    			liLink.setUrl("/storage"+w.getFile_path());
	    		}else{
	    			liLink.setText(" - ");
	    			liLink.setUrl("");
	    		}
			}
			if(FacilityModule.ITEM_STATUS_MISSING.equals(status)){
				ClosedItemObject w = mod.getMissing(itemCode);
				lbDate.setText(formatter.format(w.getDate()));
	    		lbReason.setText(w.getReason());
	    		lbBy.setText(w.getCreatedby_name());
	    		if(!"".equals(w.getFile_name())){
	    			liLink.setText(w.getFile_name());
	    			liLink.setUrl("/storage"+w.getFile_path());
	    		}else{
	    			liLink.setText(" - ");
	    			liLink.setUrl("");
	    		}
			}
		}
	}
	
	public Forward onSubmit(Event evt) {
	    String buttonName = findButtonClicked(evt);
	    kacang.ui.Forward result = super.onSubmit(evt);
	    if (buttonName != null && btnCancel.getAbsoluteName().equals(buttonName)) {
	    	init();
	    	return new Forward(Form.CANCEL_FORM_ACTION, cancelUrl, true);
	    }else if(FORM_ACTION_ADD.equals(action)){
	    	if (buttonName != null && btnSubmitNCont.getAbsoluteName().equals(buttonName)) {
		    	isContinue = true;
		    }else{
		    	isContinue = false;
		    }
	    	return result;
	    }else if(FORM_ACTION_VIEW.equals(action)){
	    	if (buttonName != null && btnEdit.getAbsoluteName().equals(buttonName)) {
	    		init();
	    		return new Forward(Form.CANCEL_FORM_ACTION, editUrl, true);
	    	}else if (buttonName != null && btnStatus.getAbsoluteName().equals(buttonName)) {
		    	init();
		    	return new Forward(Form.CANCEL_FORM_ACTION, statusUrl, true);
	    	}else{
	    		return result;
	    	}
    	}else{
	    	return result;
	    }
	}
	
	public Forward onValidate(Event event) {

		if (FORM_ACTION_EDIT.equals(action) || FORM_ACTION_ADD.equals(action)) {
			FacilityObject o = new FacilityObject();
			FacilityModule mod = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
			
			if(FORM_ACTION_EDIT.equals(action)){
				try {
					o = mod.getItem(itemCode);
				}catch (Exception e) {
					Log.getLog(getClass()).error(e.toString());
				}
			}
			if(FORM_ACTION_ADD.equals(action)){
				try {
					o = mod.getItem(tfItemCode.getValue().toString());
				}catch (Exception e) {
					Log.getLog(getClass()).error(e.toString());
				}
				if(o.getBarcode() != null && !"".equals(o.getBarcode()) && !"null".equals(o.getBarcode())){
					return new Forward(FORWARD_ADD_EXIST);
				}
			}
			
			o.setPurchased_date(dpfPurchasedDate.getDate());
			o.setPurchased_cost(tfPurchasedCost.getValue().toString());
			o.setDo_num(tfDONum.getValue().toString());
			o.setEasset_num(tfEAsset.getValue().toString());
			o.setLocation_id(getSelectBoxValue(sbLocation));
			o.setReplacement((cbReplacement.isChecked())?"Y":"N");
			if (FORM_ACTION_ADD.equals(action)) {
				try {
					o.setBarcode(tfItemCode.getValue().toString());
					o.setFacility_id(facilityId);
					o.setCreatedby(getWhoModifyId());
					o.setCreatedby_date(new Date());
					o.setStatus(FacilityModule.ITEM_STATUS_CHECKED_IN);
					mod.insertItem(o);
					
					FacilityObject f = mod.getFacility(facilityId);
					f.setQuantity(f.getQuantity()+1);
					mod.updateFacility(f);
				}catch (Exception e) {
					Log.getLog(getClass()).error(e.toString()); 
					return new Forward(FORWARD_ADD_FAIL);} 
			}
			if (FORM_ACTION_EDIT.equals(action)) {
				try {
					o.setUpdatedby(getWhoModifyId());
					o.setUpdatedby_date(new Date());
					
					mod.updateItem(o);
					return new Forward(FORWARD_EDIT_SUCCESS);
				}catch (Exception e) {
					Log.getLog(getClass()).error(e.toString()); 
					return new Forward(FORWARD_EDIT_FAIL);
				} 
			}
		}
		if(isContinue){
			return new Forward(FORWARD_ADD_SUCCESS_CONT);
		}else{
			return new Forward(FORWARD_ADD_SUCCESS);
		}
	}	
	
	public String getDefaultTemplate() {
		return "fms/facility/itemForm";
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
