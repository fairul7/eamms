package com.tms.fms.engineering.ui;

import kacang.Application;
import kacang.stdui.DatePopupField;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.Radio;
import kacang.stdui.TextField;
import kacang.stdui.TimeField;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.engineering.model.ScpService;
import com.tms.fms.facility.model.RateCard;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.util.WidgetUtil;
import com.tms.fms.validator.ValidatorSelectBox;
import com.tms.fms.widgets.BoldLabel;

public class ScpForm extends ServiceForm {
	protected EngineeringRequest request=new EngineeringRequest();
	protected SingleFacilitySelectBox facilitySelectBox;
	protected DatePopupField requiredFrom;
	protected DatePopupField requiredTo;
	protected TimeField departureTime;
	protected TextField location;
	protected TextField segment;
	protected TimeField settingFrom;
	protected TimeField settingTo;
	protected TimeField rehearsalFrom;
	protected TimeField rehearsalTo;
	protected TimeField recordingFrom;
	protected TimeField recordingTo;
	protected Radio rdBBYes;
	protected Radio rdBBNo;
	protected Panel timePanel1;
	protected Panel timePanel2;
	protected Panel timePanel3;
	protected Label lbFacility;
	protected Label lbBlockBooking;
	protected Label lbDateRequiredFrom;
	protected Label lbDateRequiredTo;
	protected Label lbDepartureTime;
	protected Label lbLocation;
	protected Label lbSegment;
	protected Label lbSettingTime;
	protected Label lbRehearsalTime;
	protected Label lbRecordingTime;
	
	public void init() {
		if (type == null || (!"Add".equals(type) && !"Edit".equals(type))) {
			type = "Add";
		}
	}
	
	public void onRequest(Event event) {
		
		initForm();
		
		if ("Edit".equals(type)) {
			populateFields();
		} else {
			EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
			request = module.getRequestWithService(requestId);
		}
	}

	public void initForm() {
		setMethod("post");
		setColumns(2);
		Application app = Application.getInstance();
		
		lbFacility = new BoldLabel("lbFacility");
		lbFacility.setAlign("right");
		lbFacility.setText(app.getMessage("fms.facility.label.facility")+"*");
		addChild(lbFacility);
		
		facilitySelectBox= new SingleFacilitySelectBox("facilitySelectBox");
		facilitySelectBox.addChild(new ValidatorSelectBox("validateFacilitySelectBox","",""));
		facilitySelectBox.setServiceId(serviceId);
		facilitySelectBox.init();
		addChild(facilitySelectBox);
		
		lbBlockBooking = new BoldLabel("lbBlockBooking");
		lbBlockBooking.setAlign("right");
		lbBlockBooking.setText(app.getMessage("fms.facility.label.blockBooking"));
		addChild(lbBlockBooking);
		
		Panel pnBB = new Panel("pnBB");
		rdBBYes = new Radio("rdBBYes", "Yes");
		rdBBYes.setGroupName("blockBooking");
		pnBB.addChild(rdBBYes);
		rdBBNo = new Radio("rdBBNo", "No");
		rdBBNo.setGroupName("blockBooking");
		rdBBNo.setChecked(true);
		pnBB.addChild(rdBBNo);
		addChild(pnBB);
		
		lbDateRequiredFrom = new BoldLabel("lbDateRequiredFrom");
		lbDateRequiredFrom.setAlign("right");
		lbDateRequiredFrom.setText(app.getMessage("fms.facility.label.requiredFrom")+"*");
		addChild(lbDateRequiredFrom);
		
		requiredFrom = new DatePopupField("requiredFrom");
		requiredFrom.setDate(dtRequiredFrom);
		addChild(requiredFrom);
		
		lbDateRequiredTo = new BoldLabel("lbDateRequiredTo");
		lbDateRequiredTo.setAlign("right");
		lbDateRequiredTo.setText(app.getMessage("fms.facility.label.requiredTo")+"*");
		addChild(lbDateRequiredTo);
		
		requiredTo = new DatePopupField("requiredTo");
		requiredTo.setDate(dtRequiredTo);
		addChild(requiredTo);
		
		lbDepartureTime= new BoldLabel("lbDepartureTime");
		lbDepartureTime.setAlign("right");
		lbDepartureTime.setText(app.getMessage("fms.facility.label.departureTime"));
		addChild(lbDepartureTime);
		
		departureTime = new TimeField("departureTime");
		addChild(departureTime);
		
		lbLocation = new BoldLabel("lbLocation");
		lbLocation.setAlign("right");
		lbLocation.setText(app.getMessage("fms.facility.table.location"));
		addChild(lbLocation);
		
		location =new TextField("location");
		addChild(location);
		
		lbSegment = new BoldLabel("lbSegment");
		lbSegment.setAlign("right");
		lbSegment.setText(app.getMessage("fms.facility.label.segment"));
		addChild(lbSegment);
		
		segment =new TextField("segment");
		addChild(segment);
		
		lbSettingTime = new BoldLabel("lbSettingTime");
		lbSettingTime.setAlign("right");
		lbSettingTime.setText(app.getMessage("fms.facility.label.settingTime"));
		addChild(lbSettingTime);
		
		timePanel1= new Panel("timePanel1");
		timePanel1.addChild(new Label("from",app.getMessage("fms.facility.label.from")));
		settingFrom = new TimeField("settingFrom");
		timePanel1.addChild(settingFrom);
		timePanel1.addChild(new Label("to"," "+app.getMessage("fms.facility.label.to")));
		settingTo = new TimeField("settingTo");
		timePanel1.addChild(settingTo);
		addChild(timePanel1);
		
		lbRehearsalTime = new BoldLabel("lbRehearsalTime");
		lbRehearsalTime.setAlign("right");
		lbRehearsalTime.setText(app.getMessage("fms.facility.label.rehearsalTime"));
		addChild(lbRehearsalTime);
		
		timePanel2= new Panel("timePanel2");
		timePanel2.addChild(new Label("from",app.getMessage("fms.facility.label.from")));
		rehearsalFrom = new TimeField("rehearsalFrom");
		timePanel2.addChild(rehearsalFrom);
		timePanel2.addChild(new Label("to"," "+app.getMessage("fms.facility.label.to")));
		rehearsalTo = new TimeField("rehearsalTo");
		timePanel2.addChild(rehearsalTo);
		addChild(timePanel2);
		
		lbRecordingTime = new BoldLabel("lbRecordingTime");
		lbRecordingTime.setAlign("right");
		lbRecordingTime.setText(app.getMessage("fms.facility.label.recordingTime"));
		addChild(lbRecordingTime);
		
		timePanel3= new Panel("timePanel3");
		timePanel3.addChild(new Label("from",app.getMessage("fms.facility.label.from")));
		recordingFrom = new TimeField("recordingFrom");
		timePanel3.addChild(recordingFrom);
		timePanel3.addChild(new Label("to"," "+app.getMessage("fms.facility.label.to")));
		recordingTo = new TimeField("recordingTo");
		timePanel3.addChild(recordingTo);
		addChild(timePanel3);
		
		populateButtons();
		
	}
	
	private void populateFields(){
		EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		ScpService s=module.getScpService(id);
		
		
		serviceId=s.getServiceId();
		requestId=s.getRequestId();
		request = module.getRequestWithService(requestId);
		
		facilitySelectBox.setServiceId(serviceId);
		facilitySelectBox.setIds(new String[]{s.getFacilityId()});
		if ("1".equals(s.getBlockBooking())){
			rdBBYes.setChecked(true);
			rdBBNo.setChecked(false);
		} else {
			rdBBNo.setChecked(true);
			rdBBYes.setChecked(false);
		}
		//rdBBNo.setChecked("0".equals(s.getBlockBooking()));
		requiredFrom.setDate(s.getRequiredFrom());
		requiredTo.setDate(s.getRequiredTo());
		WidgetUtil.populateTimeField(departureTime,s.getDepartureTime());
		location.setValue(s.getLocation());
		segment.setValue(s.getSegment());
		WidgetUtil.populateTimeField(settingFrom, s.getSettingFrom());
		WidgetUtil.populateTimeField(settingTo, s.getSettingTo());
		WidgetUtil.populateTimeField(rehearsalFrom, s.getRehearsalFrom());
		WidgetUtil.populateTimeField(rehearsalTo, s.getRehearsalTo());
		WidgetUtil.populateTimeField(recordingFrom, s.getRecordingFrom());
		WidgetUtil.populateTimeField(recordingTo, s.getRecordingTo());
	}
	
	public Forward onValidate(Event event) {
			
		ScpService service = new ScpService();
		
		EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		SetupModule mod = (SetupModule)Application.getInstance().getModule(SetupModule.class);
		
		service.setRequestId(requestId);
		service.setServiceId(serviceId);
		service.setFacilityId(facilitySelectBox.getSelectedId());
		service.setBlockBooking(rdBBYes.isChecked()?"1":"0");
		service.setRequiredFrom(requiredFrom.getDate());
		service.setRequiredTo(requiredTo.getDate());
		service.setDepartureTime(WidgetUtil.getTime(departureTime));
		service.setLocation((String)location.getValue());
		service.setSegment((String)segment.getValue());
		service.setSettingFrom(WidgetUtil.getTime(settingFrom));
		service.setSettingTo(WidgetUtil.getTime(settingTo));
		service.setRehearsalFrom(WidgetUtil.getTime(rehearsalFrom));
		service.setRehearsalTo(WidgetUtil.getTime(rehearsalTo));
		service.setRecordingFrom(WidgetUtil.getTime(recordingFrom));
		service.setRecordingTo(WidgetUtil.getTime(recordingTo));
		service.setSubmitted("0");
		
		// get internal rate and external rate from rate card
		RateCard rc = mod.getRateCardDetail(service.getFacilityId());
		service.setInternalRate(rc.getInternalRate());
		service.setExternalRate(rc.getExternalRate());
		
		if(service.getRequiredFrom().after(service.getRequiredTo())){
			requiredTo.setInvalid(true);
			this.setInvalid(true);
			return new Forward();
		}
		
		if (service.getRequiredFrom().before(request.getRequiredFrom()) || service.getRequiredFrom().after(request.getRequiredTo())){
			requiredFrom.setInvalid(true);
			this.setInvalid(true);
			return new Forward();
		}
		
		if (service.getRequiredTo().before(request.getRequiredFrom()) || service.getRequiredTo().after(request.getRequiredTo())){
			requiredTo.setInvalid(true);
			this.setInvalid(true);
			return new Forward();
		}
		
		// get facility name for logging
		service.setFacility(facilitySelectBox.getSelectedText());
		
		if ("Add".equals(type)) {
			try {
				module.insertScpService(service);
				if(EngineeringModule.ASSIGNMENT_STATUS.equals(request.getStatus()))
					return new Forward("MODIFIED");
				
				return new Forward("ADDED");
			}catch (Exception e) {
				Log.getLog(getClass()).error(e.toString()); 
				return new Forward("FAILED");
			} 
		}
		if ("Edit".equals(type)) {
			try {
				service.setId(id);
				module.updateScpService(service);
				if(EngineeringModule.ASSIGNMENT_STATUS.equals(request.getStatus()))
					return new Forward("MODIFIED");
				
				return new Forward("ADDED");
			}catch (Exception e) {
				Log.getLog(getClass()).error(e.toString()); 
				return new Forward("FAILED");
			} 
		}
		return new Forward("ADDED");
	}
	
}
