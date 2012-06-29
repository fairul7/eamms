package com.tms.fms.engineering.ui;

import java.text.SimpleDateFormat;

import kacang.Application;
import kacang.stdui.Label;
import kacang.ui.Event;

import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.StudioService;

/**
 * @author fahmi
 *
 */
public class StudioView extends ServiceForm {	
	private Label facility;
	private Label bookingDate;
	private Label segment;
	private Label requiredTime;
	private Label settingTime;
	private Label rehearsalTime;
	private Label vtrTime;
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
		
		bookingDate = new Label("bookingDate");
		addChild(bookingDate);
		
		segment = new Label("segment");
		addChild(segment);
		
		requiredTime =new Label("requiredTime");
		addChild(requiredTime);
		
		settingTime = new Label("settingTime");
		addChild(settingTime);
		
		rehearsalTime = new Label("rehearsalTime");
		addChild(rehearsalTime);
		
		vtrTime = new Label("vtrTime");
		addChild(vtrTime);
		
		blockBooking = new Label("blockBooking");
		addChild(blockBooking);
		
		location = new Label("location");
		addChild(location);
		
		populateButtons();
		
	}
	
	private void populateFields(){
		EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		StudioService s=module.getStudioService(id);
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		String strRequiredTime = "";
		String strSettingTime = "";
		String strRehearsalTime = "";
		String strVtrTime = "";
		
		serviceId=s.getServiceId();
		requestId=s.getRequestId();
		facility.setText(s.getFacility());
		bookingDate.setText(sdf.format(s.getBookingDate()) + " - " + sdf.format(s.getBookingDateTo()));
		segment.setText(s.getSegment());
		blockBooking.setText(s.getBlockBooking().equals("1")?"Yes":"No");
		location.setText(s.getLocation());
		//if (s.getRequiredFrom().equals(s.getRequiredTo())){
		//	strRequiredTime = s.getRequiredFrom();
		//} else {
			strRequiredTime = s.getRequiredFrom() + " - " + s.getRequiredTo();
		//}
		requiredTime.setText(strRequiredTime);
		
		//if (s.getSettingFrom().equals(s.getSettingTo())){
		//	strSettingTime = s.getSettingFrom();
		//} else {
			strSettingTime = s.getSettingFrom() + " - " + s.getSettingTo();
		//}
		settingTime.setText(strSettingTime);
		
		//if (s.getRehearsalFrom().equals(s.getRehearsalTo())){
		//	strRehearsalTime = s.getRehearsalFrom();
		//} else {
			strRehearsalTime = s.getRehearsalFrom() + " - " + s.getRehearsalTo();
		//}
		rehearsalTime.setText(strRehearsalTime);
		
		//if (s.getVtrFrom().equals(s.getVtrTo())){
		//	strVtrTime = s.getVtrFrom();
		//} else {
			strVtrTime = s.getVtrFrom() + " - " + s.getVtrTo();
		//}
		vtrTime.setText(strVtrTime);
		
		buttonPanel.setHidden(true);		
	}
	
	@Override
	public String getDefaultTemplate() {
		return "fms/engineering/studioviewtemp";
	}

	public Label getFacility() {
		return facility;
	}

	public void setFacility(Label facility) {
		this.facility = facility;
	}

	public Label getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(Label bookingDate) {
		this.bookingDate = bookingDate;
	}

	public Label getSegment() {
		return segment;
	}

	public void setSegment(Label segment) {
		this.segment = segment;
	}

	public Label getRequiredTime() {
		return requiredTime;
	}

	public void setRequiredTime(Label requiredTime) {
		this.requiredTime = requiredTime;
	}

	public Label getSettingTime() {
		return settingTime;
	}

	public void setSettingTime(Label settingTime) {
		this.settingTime = settingTime;
	}

	public Label getRehearsalTime() {
		return rehearsalTime;
	}

	public void setRehearsalTime(Label rehearsalTime) {
		this.rehearsalTime = rehearsalTime;
	}

	public Label getVtrTime() {
		return vtrTime;
	}

	public void setVtrTime(Label vtrTime) {
		this.vtrTime = vtrTime;
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
