package com.tms.fms.facility.ui;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import com.tms.fms.facility.model.*;

import kacang.Application;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.stdui.*;
import kacang.stdui.validator.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.*;

public class ClosingItemForm  extends Form{

	public static final String FORWARD_ADD_SUCCESS = "form.add.success";
	public static final String FORWARD_ADD_FAIL = "form.add.fail";
	public static final String FORWARD_NO_ATTACHMENT = "form.no.attachment";
	public static final String FORWARD_EDIT_SUCCESS = "form.edit.success";
	public static final String FORWARD_EDIT_FAIL = "form.edit.fail";
	
	public static final String FORM_ACTION_ADD = "form.action.add";
	public static final String FORM_ACTION_EDIT = "form.action.edit";
	
	public static final String FORM_TYPE_WRITEOFF = "form.type.writeoff";
	public static final String FORM_TYPE_MISSING = "form.type.missing";
	
	private Label lbItemName;
	private Panel pnItemCode;
	private DatePopupField dpfDate;
	private FileUpload fuAttachment;
	private TextBox tbReason;
	private Button btnSubmit;
	private Button btnCancel;
	
	private String item_code[];
	private String facility_id;
	private String action;
	private String cancelUrl = "";
	private String whoModifyId = "";
	private String type;
	private String mode;
	
	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String[] getItem_code() {
		return item_code;
	}
	
	public void setItem_code(String[] item_code) {
		this.item_code = item_code;
	}
	
	public String getFacility_id() {
		return facility_id;
	}
	
	public void setFacility_id(String facility_id) {
		this.facility_id = facility_id;
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
	    FacilityModule mod = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
	    
	    addChild(new Label("lb6", Application.getInstance().getMessage("fms.facility.form.itemName", "Item Name*")));
	    lbItemName = new Label("lbItemName");
	    if("".equals(facility_id) || facility_id==null){
	    	FacilityObject i=mod.getItem(item_code[0]);
	    	facility_id = i.getFacility_id();
	    }
	    try {
			FacilityObject o=mod.getFacility(facility_id);
			lbItemName.setText(o.getName());
		}catch (Exception e) {
		    Log.getLog(getClass()).error(e.toString());
		}
	    addChild(lbItemName);
	    
	    addChild(new Label("lb1", Application.getInstance().getMessage("fms.facility.form.itemCode", "Item Code")));
	    pnItemCode = new Panel("pnItemCode");
	    pnItemCode.setColumns(1);
	    for(int i=0; i < item_code.length; i++){
	    	pnItemCode.addChild(new Label("lbIC"+i, item_code[i]));
	    }
	    addChild(pnItemCode);
		
	    addChild(new Label("lb4", Application.getInstance().getMessage("fms.facility.form.date", "Date")));
	    dpfDate = new DatePopupField("dpfDate");
	    dpfDate.setFormat("dd-MM-yyyy");
	    dpfDate.setDate(new Date());
	    addChild(dpfDate);
	    
		addChild(new Label("lb2", Application.getInstance().getMessage("fms.facility.form.relatedDocument", "Related Document")));
	    fuAttachment = new FileUpload("fuAttachment");
		addChild(fuAttachment);
		
		addChild(new Label("lb3", Application.getInstance().getMessage("fms.facility.form.reason", "Reason*")));
	    tbReason = new TextBox("tbReason");
	    tbReason.setCols("50");
	    tbReason.setRows("5");
	    tbReason.addChild(new ValidatorNotEmpty("tbReasonNotEmpty", msgNotEmpty));
		addChild(tbReason);

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
			for(int j=0; j<item_code.length; j++){
				ClosedItemObject w = new ClosedItemObject();
				FacilityModule module = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
				
				w.setItem_code(item_code[j]);
				w.setDate(dpfDate.getDate());
				w.setReason(tbReason.getValue().toString());

				try {
					FacilityObject v = module.getItem(item_code[j]);
					
					if((!FacilityModule.ITEM_STATUS_WRITE_OFF.equals(v.getStatus()) && FORM_TYPE_WRITEOFF.equals(type)) || (!FacilityModule.ITEM_STATUS_MISSING.equals(v.getStatus()) && !FacilityModule.ITEM_STATUS_WRITE_OFF.equals(v.getStatus()) && FORM_TYPE_MISSING.equals(type))){
						w.setCreatedby(getWhoModifyId());
						w.setCreatedby_date(new Date());
						w.setId(UuidGenerator.getInstance().getUuid());
						
						String path = "";
						if(FORM_TYPE_WRITEOFF.equals(type)){
							path = "/itemwriteoff/";
						}else if(FORM_TYPE_MISSING.equals(type)){
							path = "/itemmissing/";
						}
						StorageFile sf=null;
						try{
							sf = new StorageFile(path + w.getId(), fuAttachment.getStorageFile(event.getRequest()));
						}catch(Exception e){
							return new Forward(FORWARD_NO_ATTACHMENT);
						}
						
						if (sf != null) {
							w.setFile_name(sf.getName());
							w.setFile_size(sf.getSize());
							w.setFile_type(sf.getContentType());
							w.setFile_path(sf.getAbsolutePath());
							
							Application application = Application.getInstance();
				            StorageService storage = (StorageService) application.getService(StorageService.class);
				            storage.store(sf);
				            
						}else{
							w.setFile_name("");
							w.setFile_size(0);
							w.setFile_type("");
							w.setFile_path("");
						}
						
						if(FORM_TYPE_WRITEOFF.equals(type)){
							module.insertWriteoff(w);
							v.setStatus(FacilityModule.ITEM_STATUS_WRITE_OFF);
						}else if(FORM_TYPE_MISSING.equals(type)){
							module.insertMissing(w);
							v.setStatus(FacilityModule.ITEM_STATUS_MISSING);
						}
						
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
	
	public String getDefaultTemplate() {
		return "fms/facility/closingItemForm";
	}
}
