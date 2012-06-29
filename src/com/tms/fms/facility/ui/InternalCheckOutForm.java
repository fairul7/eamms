package com.tms.fms.facility.ui;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import com.metaparadigm.jsonrpc.JSONRPCBridge;
import com.tms.fms.facility.model.*;
import com.tms.fms.facility.ui.CategoryForm.ValidatorParentSelected;
import com.tms.fms.facility.ui.validator.ValidatorItemBarcode;
import com.tms.fms.transport.model.TransportModule;
import com.tms.fms.transport.model.WriteoffObject;
import com.tms.fms.widgets.ExtendedTextField;
import com.tms.fms.department.model.*;
import com.tms.fms.department.ui.PopupHODSelectBox;
import com.tms.jsonUtil.JSONUtil;

import kacang.Application;
import kacang.stdui.*;
import kacang.stdui.validator.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.*;

public class InternalCheckOutForm extends Form{
	public static final String FORWARD_ADD_SUCCESS = "form.add.success";
	public static final String FORWARD_ADD_FAIL = "form.add.fail";
	public static final String FORWARD_MORE="form.more";
	
	private PopupHODSelectBox psusbCheckOutBy;
	private TextField tfPurpose;
	private TextField tfLocation;
	//private TextField tfItem[];
	private ExtendedTextField tfItem[];
	private Panel pnButton;
	private Button btnSubmit;
	private Button btnMore;
	private Button btnCancel;
	
	private boolean isMore=false;
	private int count=0;
	private String cancelUrl = "";
	private String whoModifyId = "";
	
	private TextField takenBy;
	private TextField preparedBy;
	
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
		FacilityModule facilityModule = (FacilityModule) Application.getInstance().getModule(FacilityModule.class);
		JSONRPCBridge jsonBridge = JSONUtil.getJsonBridge(event.getRequest());
		jsonBridge.registerObject("facilityModule", facilityModule);
		if(!isMore){
			initForm();
			//populateFields();
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
	    
	    addChild(new Label("lb1", Application.getInstance().getMessage("fms.facility.form.checkOutBy*", "Check Out By*")));
	    psusbCheckOutBy = new PopupHODSelectBox("psusbCheckOutBy");
	    psusbCheckOutBy.addChild(new ValidatorNotEmpty("psusbCheckOutByNotEmpty", msgNotEmpty));
	    addChild(psusbCheckOutBy);
	    psusbCheckOutBy.init();
	    
	    takenBy = new TextField("takenBy");
	    //takenBy.addChild(new ValidatorNotEmpty("takenByNotEmpty", msgNotEmpty));
	    addChild(takenBy);

	    preparedBy = new TextField("preparedBy");
	    //preparedBy.addChild(new ValidatorNotEmpty("preparedByNotEmpty", msgNotEmpty));
	    addChild(preparedBy);
	    
	    addChild(new Label("lb2", Application.getInstance().getMessage("fms.facility.form.purpose", "Purpose")));
	    tfPurpose = new TextField("tfPurpose");
	    tfPurpose.setMaxlength("255");
	    tfPurpose.setSize("50");
	    addChild(tfPurpose);
	    
	    addChild(new Label("lb3", Application.getInstance().getMessage("fms.facility.form.assignmentLocation", "Assignment Location")));
	    tfLocation = new TextField("tfLocation");
	    tfLocation.setMaxlength("255");
	    tfLocation.setSize("50");
	    addChild(tfLocation);
	    
	    tfItem = new ExtendedTextField[20];
	    for(count=0; count<20; count++){
	    	addChild(new Label("lbItem"+count, Application.getInstance().getMessage("fms.facility.form.checkOutItem"+(count+1), "Check Out Item"+(count+1))));
		    tfItem[count] = new ExtendedTextField("tfItem"+count);
		    tfItem[count].setMaxlength("255");
		    tfItem[count].setSize("25");
		    tfItem[count].setOnKeyUp("nextbox(this,'CheckOut.form.tfItem"+ (count+1) +"')");
		    tfItem[count].setOnChange("displayItem(this,'CheckOut.form.tfItem"+ (count) +"', 'labelItemName"+ (count+1) +"')");
		    tfItem[count].addChild(new ValidatorItemBarcode("tfItem"+count+"IsValid", ValidatorItemBarcode.CHECK_OUT));
		    addChild(tfItem[count]);
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
		Date checkoutDate=new Date();
		String groupId=UuidGenerator.getInstance().getUuid();
		for(int i=0; i<count; i++){
			String code = tfItem[i].getValue().toString();
			if(!"".equals(code.trim())){
				empty = false;
				FacilityObject item = mod.getItem(code);
				if(!(item.getBarcode() == null || "".equals(item.getBarcode()))){
					if(FacilityModule.ITEM_STATUS_CHECKED_IN.equals(item.getStatus())){
						item.setStatus(FacilityModule.ITEM_STATUS_CHECKED_OUT);
						mod.updateItem(item);
						FacilityObject o = new FacilityObject();
						o.setId(UuidGenerator.getInstance().getUuid());
						o.setCheckout_date(checkoutDate);
						o.setCheckout_by(psusbCheckOutBy.getId());
						o.setBarcode(code);
						o.setTakenBy(takenBy.getValue().toString());
						o.setPreparedBy(preparedBy.getValue().toString());
						o.setPurpose(tfPurpose.getValue().toString());
						o.setLocation(tfLocation.getValue().toString());
						o.setCreatedby(whoModifyId);
						o.setCreatedby_date(checkoutDate);
						o.setGroupId(groupId);
						mod.insertCheckOut(o);
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
		return "fms/facility/internalCheckOutForm";
	}

	public TextField getTakenBy() {
		return takenBy;
	}

	public void setTakenBy(TextField takenBy) {
		this.takenBy = takenBy;
	}

	public TextField getPreparedBy() {
		return preparedBy;
	}

	public void setPreparedBy(TextField preparedBy) {
		this.preparedBy = preparedBy;
	}
	
	
}
