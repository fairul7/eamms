package com.tms.fms.transport.ui;

import java.util.Date;

import com.tms.fms.transport.model.TransportFeedbackDataObject;
import com.tms.fms.transport.model.TransportFeedbackModule;
import com.tms.fms.util.WidgetUtil;

import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Radio;
import kacang.stdui.TextBox;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.Widget;
import kacang.ui.WidgetManager;

public class FeedbackForm extends Form {
	private String requestId;
	private static int RBPoint = 8;
	private Radio[] quality = new Radio[RBPoint];
	private Radio[] driver = new Radio[RBPoint];
	private Radio[] customer = new Radio[RBPoint];
	private Radio[] availability = new Radio[RBPoint];
	private Radio[] condition = new Radio[RBPoint];
	private TextBox remark;
	private Button submit;
	
	public void initForm() {
		Application app = Application.getInstance();
		
		createRadio(quality, app, "quality");
		createRadio(driver, app, "driver");
		createRadio(customer, app, "customer");
		createRadio(availability, app, "availability");
		createRadio(condition, app, "condition");
		
		remark = new TextBox("remark");
		remark.setSize("60");
		addChild(remark);
		
		submit = new Button("submit", app.getMessage("fms.tranFeedback.submit", "Submit"));
		addChild(submit);
	}

	public void onRequest(Event evt) {
		initForm();
	}

	private void createRadio(Radio[] type, Application app, String name) {
		for (int i = 0; i < RBPoint; i++) {
			type[i] = new Radio(name+i);
			if (i==0){
				type[i].setText(app.getMessage("fms.tranFeedback.na", "Not Applicable"));
				type[i].setChecked(true);
			}else{
				type[i].setText(String.valueOf(i));
			}
			type[i].setValue(i);
			type[i].setGroupName(name);
			addChild(type[i]);
		}
	}
	
	public Forward onValidate(Event evt) {
		String buttonName = findButtonClicked(evt);
		Application app = Application.getInstance();
		TransportFeedbackModule mod = (TransportFeedbackModule) app.getModule(TransportFeedbackModule.class);
		TransportFeedbackDataObject feed = new TransportFeedbackDataObject();
		
		if (buttonName != null && submit.getAbsoluteName().equals(buttonName)) {	
			User user = getWidgetManager().getUser();
			// Setting data to DO
			feed.setRequestId(requestId);
			feed.setSupportQuality(WidgetUtil.getRadioValue(quality));
			feed.setDriverPerformance(WidgetUtil.getRadioValue(driver));
			feed.setCustomerService(WidgetUtil.getRadioValue(customer));
			feed.setVehicleAvailability(WidgetUtil.getRadioValue(availability));
			feed.setVehicleCondition(WidgetUtil.getRadioValue(condition));
			feed.setRemarks((String) remark.getValue());
			feed.setUpdatedBy(user.getId());
			feed.setUpdatedDate(new Date());
			if (mod.searchId(requestId)){
				return new Forward("EXIST");
			}else {
				mod.addFeedback(feed);
			}
			
			return new Forward("SUBMITTED");
		}
		return null;
	}

	public String getDefaultTemplate() {
		return "fms/transport/feedbackFormTpl";
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}


	public static int getRBPoint() {
		return RBPoint;
	}

	public static void setRBPoint(int point) {
		RBPoint = point;
	}

	public Radio[] getQuality() {
		return quality;
	}

	public void setQuality(Radio[] quality) {
		this.quality = quality;
	}

	public Radio[] getDriver() {
		return driver;
	}

	public void setDriver(Radio[] driver) {
		this.driver = driver;
	}

	public Radio[] getCustomer() {
		return customer;
	}

	public void setCustomer(Radio[] customer) {
		this.customer = customer;
	}

	public Radio[] getAvailability() {
		return availability;
	}

	public void setAvailability(Radio[] availability) {
		this.availability = availability;
	}

	public Radio[] getCondition() {
		return condition;
	}

	public void setCondition(Radio[] condition) {
		this.condition = condition;
	}

	public TextBox getRemark() {
		return remark;
	}

	public void setRemark(TextBox remark) {
		this.remark = remark;
	}

	public Button getSubmit() {
		return submit;
	}

	public void setSubmit(Button submit) {
		this.submit = submit;
	}
}
