package com.tms.fms.engineering.ui;

import java.text.SimpleDateFormat;

import kacang.Application;
import kacang.stdui.Label;
import kacang.ui.Event;

import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.ManpowerService;

public class ManpowerServiceView extends ServiceForm {	
	private Label manpower;
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
				
		manpower = new Label("manpower");
		addChild(manpower);
		
		quantity = new Label("quantity");
		addChild(quantity);
		
		requiredFrom = new Label("requiredFrom");
		addChild(requiredFrom);
		
		requiredTo = new Label("requiredTo");
		addChild(requiredTo);
		
		requiredTime =new Label("requiredTime");
		addChild(requiredTime);
		
		blockBooking = new Label("blockBooking");
		addChild(blockBooking);
		
		location = new Label("location");
		addChild(location);
		
		remarks = new Label("remarks");
		addChild(remarks);
		
		populateButtons();
		
	}
	
	private void populateFields(){
		EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		ManpowerService s=module.getManpowerService(id);
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		String strTime = "";
		
		serviceId=s.getServiceId();
		requestId=s.getRequestId();
		manpower.setText(s.getCompetencyName());
		quantity.setText(s.getQuantity()+"");
		requiredFrom.setText(sdf.format(s.getRequiredFrom()));
		requiredTo.setText(sdf.format(s.getRequiredTo()));
		blockBooking.setText(s.getBlockBooking().equals("1")?"Yes":"No");
		location.setText(s.getLocation());
		
		//if (s.getFromTime().equals(s.getToTime())){
		//	strTime = s.getFromTime();
		//} else {
			strTime = s.getFromTime() + " - " + s.getToTime();
		//}
		requiredTime.setText(strTime);		
		
		remarks.setText(s.getRemarks());
	
		buttonPanel.setHidden(true);		
	}
	
	@Override
	public String getDefaultTemplate() {
		return "fms/engineering/manpowerviewtemp";
	}

	public Label getManpower() {
		return manpower;
	}

	public void setManpower(Label manpower) {
		this.manpower = manpower;
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
