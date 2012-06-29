package com.tms.fms.engineering.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.ui.Event;

import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.engineering.model.UnitHeadModule;

public class ManpowerAssignmentDetailForm extends Form {
	
	protected String requestId;
	private Label reqTitle;
	private Label reqDate;
	private Label reqProgram;

	public void initForm() {
		Application app = Application.getInstance();
		UnitHeadModule mod = (UnitHeadModule) app.getModule(UnitHeadModule.class);
		
		EngineeringRequest req = mod.getTodaysRequestDetail(requestId);
		
		reqTitle = new Label("reqTitle");
		reqTitle.setText(req.getTitle());
		addChild(getReqTitle());
		
		reqDate = new Label("reqDate");
		reqDate.setText(req.getRequiredDate());
		addChild(getReqDate());
		
		reqProgram = new Label("reqProgram");
		reqProgram.setText(req.getProgramName());
		addChild(getReqProgram());
	}
	
	@Override
	public void onRequest(Event evt) {
		initForm();
	}

	public String getDefaultTemplate() {
		return "fms/engineering/manpowerAssignmentDetailTpl";
	}
	
	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public void setReqTitle(Label reqTitle) {
		this.reqTitle = reqTitle;
	}

	public Label getReqTitle() {
		return reqTitle;
	}

	public void setReqDate(Label reqDate) {
		this.reqDate = reqDate;
	}

	public Label getReqDate() {
		return reqDate;
	}

	public void setReqProgram(Label reqProgram) {
		this.reqProgram = reqProgram;
	}

	public Label getReqProgram() {
		return reqProgram;
	}
}
