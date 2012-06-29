package com.tms.fms.transport.ui;

import kacang.ui.Event;
import kacang.ui.Forward;

public class RequestDetailFeedbackForm extends AdminForm {
	
	public void init() {
		super.init();
	}
	
	public void onRequest(Event event) {
		super.onRequest(event);
		id = event.getRequest().getParameter("requestId");
		populateFormSubmit(id);
	}
	

	public Forward onValidate(Event evt) {
		return null;
	}
	
	public String getDefaultTemplate() {
		return "fms/transport/detailReqFeedbackTpl";
	}
}
