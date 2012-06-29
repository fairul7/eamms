package com.tms.fms.engineering.ui;

import java.util.ArrayList;
import java.util.Collection;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.util.Log;

import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.Service;
import com.tms.fms.widgets.CollapsiblePanel;

public class StatusTrail extends Form {
	
	protected String requestId;
	protected Collection statusTrail=new ArrayList();
	protected CollapsiblePanel panel;
	
	public StatusTrail() {
	}

	public StatusTrail(String s) {super(s);}

	public void init() {
		populateRequest();
	}

	private void populateRequest(){
		try{
			EngineeringModule module=(EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
			statusTrail=module.getStatusTrail(requestId);	
		}catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
	}
	
	public String getDefaultTemplate() {
		return "fms/engineering/statusTrailTemplate";
	}
	
	public void onRequest(Event arg0) {
		populateRequest();
	}

	public Collection getStatusTrail() {
		return statusTrail;
	}

	public void setStatusTrail(Collection statusTrail) {
		this.statusTrail = statusTrail;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

		
}
