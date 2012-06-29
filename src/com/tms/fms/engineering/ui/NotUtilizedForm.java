package com.tms.fms.engineering.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.calendar.model.CalendarModule;
import com.tms.fms.department.ui.PopupHODSelectBox;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.facility.model.FInactiveObject;
import com.tms.fms.facility.model.FacilityModule;
import com.tms.fms.facility.model.FacilityObject;
import com.tms.fms.facility.ui.validator.ValidatorItemBarcode;
import com.tms.fms.widgets.ExtendedTextField;

import kacang.Application;
import kacang.model.DaoException;
import kacang.services.security.SecurityService;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.TextBox;
import kacang.stdui.TimeField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

public class NotUtilizedForm extends Form{

	protected TextBox reason;
	protected Button submit, cancelButton;
	public String requestId;
	
	public NotUtilizedForm() {
	 
	}

    public NotUtilizedForm(String s) {
        super(s);
    }
	
	public void init() {
        super.init();
        setMethod("POST");
        reason = new TextBox("reason");
        reason.setRows("8");
        submit = new Button("submit", Application.getInstance().getMessage("calendar.label.submit", "Submit"));
        cancelButton = new Button("cancelButton", Application.getInstance().getMessage("calendar.label.cancel", "Cancel"));
        addChild(cancelButton);
        addChild(reason);
        addChild(submit);
    }
	
	public Forward onValidate(Event event) {
        super.onValidate(event);
        Forward fwd = null;
        String buttonClicked = findButtonClicked(event);
        if (submit.getAbsoluteName().equals(buttonClicked)) {
        	
        	FacilityModule module = (FacilityModule) Application.getInstance().getModule(FacilityModule.class);
        	
        	try {
        		if(!(requestId == null || "".equals(requestId))){
					module.updateNotUtilizedItem(requestId);
					module.updateReasonNotUtilizedItem(requestId, (String)reason.getValue());
					fwd = new Forward("save");
        		}else{
        			fwd = new Forward("error");
        		}
			} catch (Exception e) {
				Log.getLog(getClass()).error(e.toString(), e);
			}
        	        	
        }
        if (cancelButton.getAbsoluteName().equals(buttonClicked)) {
            fwd = new Forward("cancel");
        }
        // reset form
        removeChildren();
        init();
        return fwd;
    }	
	
	public String getDefaultTemplate() {
		return "fms/engineering/notutilized";
	}
	
	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public TextBox getReason() {
		return reason;
	}

	public void setReason(TextBox reason) {
		this.reason = reason;
	}

	public Button getSubmit() {
		return submit;
	}

	public void setSubmit(Button submit) {
		this.submit = submit;
	}

	public Button getCancelButton() {
		return cancelButton;
	}

	public void setCancelButton(Button cancelButton) {
		this.cancelButton = cancelButton;
	}
	
}
