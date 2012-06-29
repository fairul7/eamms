package com.tms.fms.facility.ui;

import com.tms.fms.engineering.model.EngineeringModule;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.ui.Forward;

@SuppressWarnings("serial")
public class RevalidateBookingForm extends Form {
	private Button btnSubmit;
	
	public void init() {
		setWidth("100%");
		initForm();
	}
	
	public void onRequest(Event event){
		init();
	}
	
	public void initForm() {
		setMethod("post");
		Application application = Application.getInstance();
		
		btnSubmit = new Button("btnSubmit", application.getMessage("fms.facility.submit", "Submit"));
		addChild(btnSubmit);
	}
	
	public Forward onValidate(Event event) {
		EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		module.revalidateBooking();
		return new Forward("finish-booking");
	}
	
	public String getDefaultTemplate() {
		return "fms/facility/revalidateBookingTpl";
	}
}
