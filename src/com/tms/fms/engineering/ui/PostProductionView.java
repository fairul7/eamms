package com.tms.fms.engineering.ui;

import java.text.SimpleDateFormat;

import kacang.Application;
import kacang.stdui.Label;
import kacang.ui.Event;

import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.PostProductionService;

public class PostProductionView extends ServiceForm {
	
	private Label facility;
	private Label dateRequired;
	private Label timeRequired;
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
		
		dateRequired = new Label("dateRequired");
		addChild(dateRequired);
		
		timeRequired = new Label("timeRequired");
		addChild(timeRequired);		
		
		blockBooking = new Label("blockBooking");
		addChild(blockBooking);
		
		location = new Label("location");
		addChild(location);
		
		populateButtons();
		
	}
	
	private void populateFields(){
		EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		PostProductionService pps=module.getPostProductionService(id);
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		String strTime  = "";
		
		serviceId=pps.getServiceId();
		requestId=pps.getRequestId();
		facility.setText(pps.getFacility());
		dateRequired.setText(sdf.format(pps.getRequiredDate()) + " - " + sdf.format(pps.getRequiredDateTo()));
		blockBooking.setText(pps.getBlockBooking().equals("1")?"Yes":"No");
		location.setText(pps.getLocation());
		
		//if (pps.getFromTime().equals(pps.getToTime())){
		//	strTime = pps.getFromTime();
		//} else {
			strTime = pps.getFromTime() + " - " + pps.getToTime();
		//}
		timeRequired.setText(strTime);
		
		buttonPanel.setHidden(true);
	}	
	
	@Override
	public String getDefaultTemplate() {
		return "fms/engineering/postproductionviewtemp";
	}

	public Label getFacility() {
		return facility;
	}

	public void setFacility(Label facility) {
		this.facility = facility;
	}

	public Label getDateRequired() {
		return dateRequired;
	}

	public void setDateRequired(Label dateRequired) {
		this.dateRequired = dateRequired;
	}

	public Label getTimeRequired() {
		return timeRequired;
	}

	public void setTimeRequired(Label timeRequired) {
		this.timeRequired = timeRequired;
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
