package com.tms.fms.facility.ui;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.TimeField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

import com.tms.fms.department.ui.PopupHODSelectBox;
import com.tms.fms.facility.model.FInactiveObject;
import com.tms.fms.facility.model.FacilityModule;
import com.tms.fms.facility.model.FacilityObject;
import com.tms.fms.facility.ui.validator.ValidatorItemBarcode;
import com.tms.fms.widgets.ExtendedTextField;

public class InternalCheckInForm extends Form{
	public static final String FORWARD_ADD_SUCCESS = "form.add.success";
	public static final String FORWARD_ADD_FAIL = "form.add.fail";
	public static final String FORWARD_MORE="form.more";
	
	private PopupHODSelectBox psusbCheckInBy;
	private DatePopupField dpfCheckInDate;
	private TimeField tifCheckInTime;
	//private TextField tfItem[];
	private ExtendedTextField tfItem[];
	private CheckBox cbDamageItem[];
	private Panel pnButton;
	private Button btnSubmit;
	private Button btnMore;
	private Button btnCancel;
	
	private boolean isMore=false;
	private int count=0;
	private String cancelUrl = "";
	private String whoModifyId = "";
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
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
		if(!isMore){
			initForm();
		}
    }
	
	public void initForm() {
	    String msgNotEmpty  = Application.getInstance().getMessage("fms.tran.msg.mandatoryField", "Mandatory Field");

	    removeChildren();
	    setMethod("post");
	    setColumns(2);
	    
	    addChild(new Label("lb1", Application.getInstance().getMessage("fms.facility.form.checkInBy*", "Check In By*")));
	    psusbCheckInBy = new PopupHODSelectBox("psusbCheckInBy");
	    psusbCheckInBy.addChild(new ValidatorNotEmpty("psusbCheckInByNotEmpty", msgNotEmpty));
	    addChild(psusbCheckInBy);
	    psusbCheckInBy.init();
	    
	    //add default value
	    User user = Application.getInstance().getCurrentUser();
	    psusbCheckInBy.addOption(user.getId(), user.getProperty("firstName") + " " + user.getProperty("lastName"));
	    
	    addChild(new Label("lb2", Application.getInstance().getMessage("fms.facility.form.checkInDate", "Check In Date*")));
	    dpfCheckInDate = new DatePopupField("dpfCheckInDate");
	    dpfCheckInDate.setFormat("dd-MM-yyyy");
	    addChild(dpfCheckInDate);
	    
	    addChild(new Label("lb3", Application.getInstance().getMessage("fms.facility.form.checkInTime", "Check In Time")));
	    tifCheckInTime = new TimeField("tifCheckInTime");
	    addChild(tifCheckInTime);
	    
	    tfItem = new ExtendedTextField[20];
	    cbDamageItem = new CheckBox[20];
	    for(count=0; count<20; count++){
	    	addChild(new Label("lbItem"+count, Application.getInstance().getMessage("fms.facility.form.checkInItem"+(count+1), "Check In Item"+(count+1))));
		    tfItem[count] = new ExtendedTextField("tfItem"+count);
		    tfItem[count].setMaxlength("255");
		    tfItem[count].setSize("25");
		    tfItem[count].setOnKeyUp("nextbox(this,'CheckIn.form.tfItem"+ (count+1) +"')");
		    tfItem[count].addChild(new ValidatorItemBarcode("tfItem"+count+"IsValid", ValidatorItemBarcode.INTERNAL_CHECK_IN));
		    addChild(tfItem[count]);
		    cbDamageItem[count] = new CheckBox("cbDamageItem"+count);
		    cbDamageItem[count].setText(Application.getInstance().getMessage("fms.facility.form.damageItem", "Damage Item"));
		    addChild(cbDamageItem[count]);
	    }
	    
		addChild(new Label("lbbutton", ""));
		pnButton = new Panel("pnButton");
		btnMore = new Button("btnMore", Application.getInstance().getMessage("fms.facility.more", "More"));
	    pnButton.addChild(btnMore);
	    btnSubmit = new Button("btnSubmit", Application.getInstance().getMessage("fms.facility.done", "Done"));
    	pnButton.addChild(btnSubmit);
	    btnCancel = new Button("btnCancel", Application.getInstance().getMessage("fms.facility.cancel", "Cancel"));
		pnButton.addChild(btnCancel);
		
	    addChild(pnButton);  
	}
	
	public Forward onSubmit(Event evt) {
	    String buttonName = findButtonClicked(evt);
	    kacang.ui.Forward result = super.onSubmit(evt);
	    if (buttonName != null && btnCancel.getAbsoluteName().equals(buttonName)) {
	    	init();
	    	return new Forward(Form.CANCEL_FORM_ACTION, cancelUrl, true);
	    }else if (buttonName != null && btnMore.getAbsoluteName().equals(buttonName)) {
	    	isMore = true;
	    	return result;
	    }else{
	    	isMore = false;
	    	return result;
	    }
	}
	
	public Forward onValidate(Event event) {
		boolean success=true;
		boolean empty=true;
		
		String message ="";
		FacilityModule mod = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
		
		Calendar cDate = Calendar.getInstance();
		cDate.setTime(dpfCheckInDate.getDate());
		cDate.set(Calendar.HOUR_OF_DAY , tifCheckInTime.getHour());
		cDate.set(Calendar.MINUTE , tifCheckInTime.getMinute());
        
		for(int i=0; i<count; i++){
			String code = tfItem[i].getValue().toString();
			boolean damage = cbDamageItem[i].isChecked();
			if(!"".equals(code.trim())){
				empty = false;
				FacilityObject item = mod.getItem(code);
				if(!(item.getBarcode() == null || "".equals(item.getBarcode()))){
					if(FacilityModule.ITEM_STATUS_CHECKED_OUT.equals(item.getStatus())){
						if(damage){
							item.setStatus(FacilityModule.ITEM_STATUS_INACTIVE);
							FInactiveObject w = new FInactiveObject();
							w.setItem_barcode(code);
							w.setDate_from(dpfCheckInDate.getDate());
							w.setDate_to(dpfCheckInDate.getDate());
							w.setReason_id("0");

							w.setCreatedby(getWhoModifyId());
							w.setCreatedby_date(new Date());
							w.setId(UuidGenerator.getInstance().getUuid());
							mod.insertFInactive(w);
						}else{
							item.setStatus(FacilityModule.ITEM_STATUS_CHECKED_IN);
						}
						mod.updateItem(item);
						FacilityObject o = new FacilityObject();
						o.setCheckin_date(cDate.getTime());
						o.setCheckin_by(psusbCheckInBy.getId());
						o.setBarcode(code);
						o.setUpdatedby(whoModifyId);
						o.setUpdatedby_date(new Date());
						mod.updateCheckOut(o);
						tfItem[i].setValue("");
					}
				}
			}
		}

		if(empty){
			success=false;
			message = Application.getInstance().getMessage("fms.facility.msg.pleaseKeyInAtLeastOneBarcode");
		}
		
    	if(success){
    		if(isMore){
    			for(count=0; count<20; count++){
    		    	tfItem[count].setValue("");
    		    	cbDamageItem[count].setChecked(false);
    		    }
    			return new Forward(FORWARD_MORE);
    		}else{
    			return new Forward(FORWARD_ADD_SUCCESS);
    		}
    	}else{
    		HttpServletRequest request = event.getRequest();
        	request.setAttribute("message", message);
        	event.setRequest(request);
    		return new Forward(FORWARD_ADD_FAIL);
    	}
	}	
	
	public String getDefaultTemplate() {
		return "fms/facility/internalCheckInForm";
	}
}
