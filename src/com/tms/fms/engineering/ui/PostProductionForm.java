package com.tms.fms.engineering.ui;

import java.util.Calendar;

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
import com.tms.fms.engineering.model.PostProductionService;
import com.tms.fms.facility.model.RateCard;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.util.WidgetUtil;
import com.tms.fms.validator.ValidatorSelectBox;
import com.tms.fms.widgets.BoldLabel;

public class PostProductionForm extends ServiceForm {
	protected EngineeringRequest request = new EngineeringRequest();
	protected SingleFacilitySelectBox facilitySelectBox;
	protected Radio rdBBYes;
	protected Radio rdBBNo;
	protected DatePopupField requiredDateFrom;
	protected DatePopupField requiredDateTo;
	protected TimeField fromTime;
	protected TimeField toTime;
	protected TextField location;
	protected Panel timePanel;
	protected Label lbFacility;
	protected Label lbBlockBooking;
	protected Label lbDateRequiredFrom;
	protected Label lbDateRequiredTo;
	protected Label lbRequiredTime;
	protected Label lbLocation;
	
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
		facilitySelectBox.addChild(new ValidatorSelectBox("facilitySelectBoxValidate","",""));
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
		
		requiredDateFrom = new DatePopupField("requiredDateFrom");
		requiredDateFrom.setDate(dtRequiredFrom);
		addChild(requiredDateFrom);
		
		lbDateRequiredTo = new BoldLabel("lbDateRequiredTo");
		lbDateRequiredTo.setAlign("right");
		lbDateRequiredTo.setText(app.getMessage("fms.facility.label.requiredTo")+"*");
		addChild(lbDateRequiredTo);
		
		requiredDateTo = new DatePopupField("requiredDateTo");
		requiredDateTo.setDate(dtRequiredTo);
		addChild(requiredDateTo);
		
		lbRequiredTime = new BoldLabel("lbRequiredTime");
		lbRequiredTime.setAlign("right");
		lbRequiredTime.setText(app.getMessage("fms.facility.label.requiredTime")+"*");
		addChild(lbRequiredTime);
		
		timePanel= new Panel("timePanel");
		timePanel.addChild(new Label("from",app.getMessage("fms.facility.label.from")));
		fromTime = new TimeField("fromTime");
		fromTime.setTemplate("calendar/fromTimefield");
		timePanel.addChild(fromTime);
		timePanel.addChild(new Label("to"," "+app.getMessage("fms.facility.label.to")));
		toTime = new TimeField("toTime");
		toTime.setTemplate("calendar/toTimeField");
		timePanel.addChild(toTime);
		
		addChild(timePanel);
		
		lbLocation = new BoldLabel("lbLocation");
		lbLocation.setAlign("right");
		lbLocation.setText(app.getMessage("fms.facility.form.location"));
		addChild(lbLocation);
		
		location =new TextField("location");
		addChild(location);
		
		populateButtons();
		submit.setOnClick("return saveIt()");
	}
	
	private void populateFields(){
		EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		PostProductionService pps=module.getPostProductionService(id);
		serviceId=pps.getServiceId();
		requestId=pps.getRequestId();
		request = module.getRequestWithService(requestId);
		
		facilitySelectBox.setServiceId(serviceId);
		facilitySelectBox.setIds(new String[]{pps.getFacilityId()});
		if ("1".equals(pps.getBlockBooking())){
			rdBBYes.setChecked(true);
			rdBBNo.setChecked(false);
		} else {
			rdBBNo.setChecked(true);
			rdBBYes.setChecked(false);
		}
		requiredDateFrom.setDate(pps.getRequiredDate());
		requiredDateTo.setDate(pps.getRequiredDateTo());
		WidgetUtil.populateTimeField(fromTime, pps.getFromTime());
		WidgetUtil.populateTimeField(toTime, pps.getToTime());
		location.setValue(pps.getLocation());
	}
	
	public Forward onValidate(Event event) {
			
		PostProductionService service = new PostProductionService();
		EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		SetupModule mod = (SetupModule)Application.getInstance().getModule(SetupModule.class);
		
		service.setRequestId(requestId);
		service.setServiceId(serviceId);
		service.setFacilityId(facilitySelectBox.getSelectedId());
		service.setBlockBooking(rdBBYes.isChecked()?"1":"0");
		service.setRequiredDate(requiredDateFrom.getDate());
		service.setRequiredDateTo(requiredDateTo.getDate());
		/*service.setFromTime(WidgetUtil.getTime(fromTime));
		service.setToTime(WidgetUtil.getTime(toTime));*/
		service.setLocation((String)location.getValue());
		service.setSubmitted("0");
		// get internal rate and external rate from rate card
		RateCard rc = mod.getRateCardDetail(service.getFacilityId());
		service.setInternalRate(rc.getInternalRate());
		service.setExternalRate(rc.getExternalRate());
		
		if(service.getRequiredDate().after(service.getRequiredDateTo())){
			requiredDateTo.setInvalid(true);
			this.setInvalid(true);
			return new Forward();
		}
		
		if (service.getRequiredDate().before(request.getRequiredFrom()) || service.getRequiredDate().after(request.getRequiredTo())){
			requiredDateFrom.setInvalid(true);
			this.setInvalid(true);
			return new Forward();
		}
		
		if (service.getRequiredDateTo().before(request.getRequiredFrom()) || service.getRequiredDateTo().after(request.getRequiredTo())){
			requiredDateTo.setInvalid(true);
			this.setInvalid(true);
			return new Forward();
		}
		
		//============== START : date and time checking =============================
		service.setFromTime(WidgetUtil.getTime(fromTime));
		
		int hour = toTime.getHour(); int minute = toTime.getMinute();
		if(requiredDateFrom.getDate().compareTo(requiredDateTo.getDate())==0){
			if(WidgetUtil.getTime(toTime).compareTo(WidgetUtil.getTime(fromTime))< 0){
				if(hour != 0){
					fromTime.setInvalid(true);
					this.setInvalid(true);
					return new Forward("checkTimeFrom");
				}
			}
			
			if(hour == 0){
				if(minute > 0){
					Calendar timeT = Calendar.getInstance();
					timeT.setTime(dtRequiredTo);
					timeT.add(Calendar.DATE, 1);
					timeT.set(Calendar.HOUR_OF_DAY, hour);
					timeT.set(Calendar.MINUTE, minute);
					toTime.setDate(timeT.getTime());
					service.setRequiredDateTo(timeT.getTime());
					service.setToTime(WidgetUtil.getTime(toTime));
				}else{
					Calendar timeT = Calendar.getInstance();
					timeT.set(Calendar.HOUR_OF_DAY, 23);
					timeT.set(Calendar.MINUTE, 59);
					toTime.setDate(timeT.getTime());
					service.setToTime(WidgetUtil.getTime(toTime));		
				}
			}else{
				service.setFromTime(WidgetUtil.getTime(fromTime));
				service.setToTime(WidgetUtil.getTime(toTime));
			}
		}else{
			if(hour == 0 && minute == 0){
				/*kuarkan alert
				 * 	tanya kalau btul nak return on 00:00
				 * 	klik ok 
				 * 		save 00:00 on dat day
				 *  klik cancel
				 *  	save date yg dia pilih
				 * */ 
				service.setFromTime(WidgetUtil.getTime(fromTime));
				Calendar timeT = Calendar.getInstance();
				timeT.set(Calendar.HOUR_OF_DAY, 23);
				timeT.set(Calendar.MINUTE, 59);
				toTime.setDate(timeT.getTime());
				service.setToTime(WidgetUtil.getTime(toTime));		
			}else{
				service.setFromTime(WidgetUtil.getTime(fromTime));
				service.setToTime(WidgetUtil.getTime(toTime));
			}
		}
		//============== END : date and time checking =============================
		
		// get facility name for logging
		service.setFacility(facilitySelectBox.getSelectedText());
		
		if ("Add".equals(type)) {
			try {
				module.insertPostProductionService(service);
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
				module.updatePostProductionService(service);
				if(EngineeringModule.ASSIGNMENT_STATUS.equals(request.getStatus()))
					return new Forward("MODIFIED");
				return new Forward("ADDED");
			}catch (Exception e) {
				Log.getLog(getClass()).error(e.toString()); 
				return new Forward("FAILED");
			} 
		}
		
		//populateFields();
		return new Forward("ADDED");
	}
}
