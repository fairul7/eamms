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

public class CheckAvailabilityDetailForm extends Form{
	
	private Button btnCancel;
	
	private String facility_id[];
	private String cancelUrl = "";
	private int count=0;
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

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
	    
	    Panel pnDetail = new Panel("pnDetail");
	    pnDetail.setColumns(2);
	    try {
			FacilityModule mod = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
			Collection lstFacility = mod.selectFacilityAvailability(facility_id);
		    if (lstFacility.size() > 0) {
		    	count = 0;
		    	Label lbFacility[] = new Label[lstFacility.size()+1];
		    	Label lbQuantity[] = new Label[lstFacility.size()+1];
		    	for (Iterator i=lstFacility.iterator(); i.hasNext();) {
		    		FacilityObject o = (FacilityObject)i.next();
		    		lbFacility[count] = new Label("lbFacility"+count, o.getName());
		    		pnDetail.addChild(lbFacility[count]);
		    		lbQuantity[count] = new Label("lbQuantity"+count, Integer.toString(o.getQuantity()));
		    		pnDetail.addChild(lbQuantity[count]);
		    		count++;
		        }
		    }
		}catch (Exception e) {
		    Log.getLog(getClass()).error(e.toString());
		}
	    addChild(pnDetail);

		addChild(new Label("lbbutton", ""));
		Panel pnButton = new Panel("pnButton");
	    btnCancel = new Button("btnCancel", Application.getInstance().getMessage("fms.facility.close", "Close"));
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
	
	public String getDefaultTemplate() {
		return "fms/facility/checkAvailabilityDetailForm";
	}
}
