package com.tms.fms.facility.ui;

import com.tms.elearning.core.ui.ValidatorNotEmpty;
import com.tms.fms.facility.model.FacilityModule;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;

public class AssignmentPreCheckin extends Form {
	private Label requestIdLabel;
	private Label errorLabel;
	private TextField requestId;
	private Button submit;
	public void init() {
		setMethod("POST");
		setColumns(3);
		removeChildren();
		
		requestIdLabel = new Label("requestIdLabel", "Request ID");
		addChild(requestIdLabel);
		
		errorLabel= new Label("errorLabel","");
		
		requestId = new TextField("requestId");
		requestId.setSize("30");
		requestId.addChild(new ValidatorNotEmpty("vneRequestId", Application.getInstance().getMessage("fms.facility.msg.emptyRequestId")));
		requestId.addChild(errorLabel);
		addChild(requestId);
		
		submit = new Button("submit", "Submit");
		addChild(submit);
		
	}
	public void onRequest(Event evt) {
		requestId.setValue("");
	}
	public Forward onSubmit(Event evt) {
		Forward fwd = super.onSubmit(evt);
		
		
		if(requestId.getValue() != null && !"".equals(requestId.getValue())){
			FacilityModule mod = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
			if(!mod.isExistRequestId((String)requestId.getValue())){
				requestId.setInvalid(true);
				setInvalid(true);
				errorLabel.setText(Application.getInstance().getMessage("fms.facility.msg.requestIdNotExist"));
			}else{
				errorLabel.setText("");
			}
		}else{
			errorLabel.setText("");
		}
		
		return fwd;
		 
	}
	public Forward onValidate(Event evt) {
		return new Forward("direct_checkin", "assignmentDirectCheckIn.jsp?requestId=" + requestId.getValue(), true);
		 
	}
	public String getDefaultTemplate() {
		return "fms/facility/assignmentPreCheckinTemplate";
	}

}
