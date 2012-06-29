package com.tms.fms.facility.ui;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import com.tms.fms.facility.model.*;
import com.tms.fms.facility.ui.CategoryForm.ValidatorParentSelected;
import com.tms.fms.transport.model.TransportModule;
import com.tms.fms.transport.model.WriteoffObject;
import com.tms.fms.department.model.*;
import com.tms.fms.department.ui.PopupHODSelectBox;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.*;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.stdui.*;
import kacang.stdui.validator.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.*;

public class CheckAvailabilityForm extends Form{
	
	public static final String FORWARD_CHECK = "form.check";
	public static final String FORWARD_EMPTY = "form.empty";
	
	private FacilityPopupSelectBox fpsbFacility;
	private Button btnSubmit;
	private Button btnCancel;
	
	private String facility_id[];
	private String cancelUrl = "";
	
	public String[] getFacility_id() {
		return facility_id;
	}

	public void setFacility_id(String[] facility_id) {
		this.facility_id = facility_id;
	}

	public String getCancelUrl() {
		return cancelUrl;
	}

	public void setCancelUrl(String cancelUrl) {
		this.cancelUrl = cancelUrl;
	}

	public void onRequest(Event event) {
		initForm();
    }
	
	public void initForm() {
		
	    removeChildren();
	    setMethod("post");
	    setColumns(2);
	    
	    addChild(new Label("lb6", Application.getInstance().getMessage("fms.facility.form.selectFacilityEquipment", "Select Facility/ Equipment")));
	    fpsbFacility = new FacilityPopupSelectBox("fpsbFacility");
	    fpsbFacility.setSortable(false);
	    addChild(fpsbFacility);
	    fpsbFacility.init();

		addChild(new Label("lbbutton", ""));
		Panel pnButton = new Panel("pnButton");
	    btnSubmit = new Button("btnSubmit", Application.getInstance().getMessage("fms.facility.viewAvailability", "View Availability"));
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
	    }else {
	    	return result;
	    }
	}
	
	public Forward onValidate(Event event) {
		facility_id = fpsbFacility.getIds();
		
		if(facility_id.length == 0){
			return new Forward(FORWARD_EMPTY);
		}else{
			HttpServletRequest request = event.getRequest();
    		request.setAttribute("selectedKeys", facility_id);
    		event.setRequest(request);
    	
    		return new Forward(FORWARD_CHECK);
		}
	}	
	
	public String getDefaultTemplate() {
		return "fms/facility/checkAvailabilityForm";
	}
}
