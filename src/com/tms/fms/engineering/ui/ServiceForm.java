package com.tms.fms.engineering.ui;

import java.util.Date;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;

public class ServiceForm extends Form {
	protected String requestId;
	protected String serviceId;
	protected Button cancel;
	protected Button submit;
	protected Panel buttonPanel;
	public String id;
	public String type;
	protected Date dtRequiredFrom;
	protected Date dtRequiredTo;
	
	public void populateButtons() {
		Application app = Application.getInstance();
		buttonPanel = new Panel("panel");
		submit = new Button("submit", app.getMessage("fms.facility.submit"));
		cancel = new Button("cancel", app.getMessage("fms.facility.cancel"));
		cancel.setOnClick("window.close();");
		buttonPanel.addChild(submit);
		buttonPanel.addChild(cancel);
		addChild(new Label(("tupuku")));
		addChild(buttonPanel);
	}
	
	protected String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public Date getDtRequiredFrom() {
		return dtRequiredFrom;
	}

	public void setDtRequiredFrom(Date dtRequiredFrom) {
		this.dtRequiredFrom = dtRequiredFrom;
	}

	public Date getDtRequiredTo() {
		return dtRequiredTo;
	}

	public void setDtRequiredTo(Date dtRequiredTo) {
		this.dtRequiredTo = dtRequiredTo;
	}
	
}
