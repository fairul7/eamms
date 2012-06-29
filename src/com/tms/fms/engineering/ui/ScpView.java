package com.tms.fms.engineering.ui;

import java.text.SimpleDateFormat;

import kacang.Application;
import kacang.stdui.Label;
import kacang.ui.Event;

import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.ScpService;

public class ScpView extends ServiceForm {
	protected Label lbFacility;
	protected Label lbDateRequiredFrom;
	protected Label lbDateRequiredTo;
	protected Label lbDepartureTime;
	protected Label lbLocation;
	protected Label lbSegment;
	protected Label lbSettingTime;
	protected Label lbRehearsalTime;
	protected Label lbRecordingTime;
	
	private Label facility;
	private Label requiredFrom;
	private Label requiredTo;
	private Label departureTime;
	private Label location;
	private Label segment;
	private Label setting;
	private Label rehearsal;
	private Label recording;
	private Label blockBooking;
	
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
		
		requiredFrom = new Label("requiredFrom");
		addChild(requiredFrom);
		
		requiredTo = new Label("requiredTo");
		addChild(requiredTo);
		
		departureTime = new Label("departureTime");
		addChild(departureTime);
		
		blockBooking = new Label("blockBooking");
		addChild(blockBooking);
		
		location =new Label("location");
		addChild(location);
		
		segment = new Label("segment");
		addChild(segment);
		
		setting = new Label("setting");
		addChild(setting);
		
		rehearsal = new Label("rehearsal");
		addChild(rehearsal);
		
		recording = new Label("recording");
		addChild(recording);
		
		populateButtons();
		
	}
	
	private void populateFields(){
		EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		ScpService s=module.getScpService(id);
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		String strSetting = "";
		String strRehearsal = "";
		String strRecording = "";
		
		serviceId=s.getServiceId();
		requestId=s.getRequestId();
		
		facility.setText(s.getFacility());
		requiredFrom.setText(sdf.format(s.getRequiredFrom()));
		requiredTo.setText(sdf.format(s.getRequiredTo()));
		departureTime.setText(s.getDepartureTime());
		location.setText(s.getLocation());
		segment.setText(s.getSegment());		
		blockBooking.setText(s.getBlockBooking().equals("1")?"Yes":"No");
		
		//if (s.getSettingFrom().equals(s.getSettingTo())){
		//	strSetting = s.getSettingFrom();
		//} else {
			strSetting = s.getSettingFrom() + " - " + s.getSettingTo();
		//}
		setting.setText(strSetting);
		
		//if (s.getRehearsalFrom().equals(s.getRehearsalTo())){
		//	strRehearsal = s.getRehearsalFrom();
		//} else {
			strRehearsal = s.getRehearsalFrom() + " - " + s.getRehearsalTo();
		//}
		rehearsal.setText(strRehearsal);
		
		//if (s.getRecordingFrom().equals(s.getRecordingTo())) {
		//	strRecording = s.getRecordingFrom();
		//} else {
			strRecording = s.getRecordingFrom() + " - " + s.getRecordingTo();
		//}
		recording.setText(strRecording);
		
		buttonPanel.setHidden(true);
		
	}
	
	@Override
	public String getDefaultTemplate() {
		return "fms/engineering/scpviewtemp";
	}

	public Label getFacility() {
		return facility;
	}

	public void setFacility(Label facility) {
		this.facility = facility;
	}

	public Label getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(Label departureTime) {
		this.departureTime = departureTime;
	}

	public Label getLocation() {
		return location;
	}

	public void setLocation(Label location) {
		this.location = location;
	}

	public Label getSegment() {
		return segment;
	}

	public void setSegment(Label segment) {
		this.segment = segment;
	}

	public Label getSetting() {
		return setting;
	}

	public void setSetting(Label setting) {
		this.setting = setting;
	}

	public Label getRehearsal() {
		return rehearsal;
	}

	public void setRehearsal(Label rehearsal) {
		this.rehearsal = rehearsal;
	}

	public Label getRecording() {
		return recording;
	}

	public void setRecording(Label recording) {
		this.recording = recording;
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

	public Label getBlockBooking() {
		return blockBooking;
	}

	public void setBlockBooking(Label blockBooking) {
		this.blockBooking = blockBooking;
	}
	
}
