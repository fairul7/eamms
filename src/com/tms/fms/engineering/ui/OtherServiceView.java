package com.tms.fms.engineering.ui;

import java.text.SimpleDateFormat;

import kacang.Application;
import kacang.stdui.Label;
import kacang.ui.Event;

import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.OtherService;

/**
 * @author fahmi
 *
 */
public class OtherServiceView extends ServiceForm {	
	private Label facility;
	private Label quantity;
	private Label requiredFrom;
	private Label requiredTo;
	private Label requiredTime;
	private Label remarks;
	private Label blockBooking;
	private Label location;
	
	public void init() {
	}
	
	public void onRequest(Event event) {
		initForm();
		if ("View".equals(type)) {
			populateFields();
		}
	}

	public void initForm() {
		setMethod("post");
				
		facility = new Label("facility");
		addChild(facility);
		
		quantity = new Label("quantity");
		addChild(quantity);
		
		requiredFrom = new Label("requiredFrom");
		addChild(requiredFrom);
		
		requiredTo = new Label("requiredTo");
		addChild(requiredTo);
		
		requiredTime =new Label("requiredTime");
		addChild(requiredTime);
		
		location = new Label("location");
		addChild(location);
		
		remarks = new Label("remarks");
		addChild(remarks);
		
		blockBooking = new Label("blockBooking");
		addChild(blockBooking);
		
		populateButtons();
		
	}
	
	private void populateFields(){
		EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		OtherService s=module.getOtherService(id);
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		String strRequiredTime = "";
		
		serviceId=s.getServiceId();
		requestId=s.getRequestId();
		facility.setText(s.getFacility());
		quantity.setText(s.getQuantity()+"");
		requiredFrom.setText(sdf.format(s.getRequiredFrom()));
		requiredTo.setText(sdf.format(s.getRequiredTo()));
		blockBooking.setText(s.getBlockBooking().equals("1")?"Yes":"No");
		location.setText(s.getLocation());
		//if (s.getFromTime().equals(s.getToTime())){
		//	strRequiredTime = s.getFromTime();
		//} else {
			strRequiredTime = s.getFromTime() + " - " + s.getToTime();
		//}
		requiredTime.setText(strRequiredTime);		
		remarks.setText(s.getRemarks());
		
		buttonPanel.setHidden(true);		
	}
	
	@Override
	public String getDefaultTemplate() {
		return "fms/engineering/otherviewtemp";
	}

	public Label getFacility() {
		return facility;
	}

	public void setFacility(Label facility) {
		this.facility = facility;
	}

	public Label getQuantity() {
		return quantity;
	}

	public void setQuantity(Label quantity) {
		this.quantity = quantity;
	}

	public Label getRequiredFrom() {
		return requiredFrom;
	}

	public void setRequiredFrom(Label requiredFrom) {
		this.requiredFrom = requiredFrom;
	}

	public Label getRequiredTo() {
		return requiredTo;
	}

	public void setRequiredTo(Label requiredTo) {
		this.requiredTo = requiredTo;
	}

	public Label getRequiredTime() {
		return requiredTime;
	}

	public void setRequiredTime(Label requiredTime) {
		this.requiredTime = requiredTime;
	}

	public Label getRemarks() {
		return remarks;
	}

	public void setRemarks(Label remarks) {
		this.remarks = remarks;
	}

	public Label getBlockBooking() {
		return blockBooking;
	}

	public void setBlockBooking(Label blockBooking) {
		this.blockBooking = blockBooking;
	}

	public Label getLocation() {
		return location;
	}

	public void setLocation(Label location) {
		this.location = location;
	}

	
}
