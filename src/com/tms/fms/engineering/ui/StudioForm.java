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
import com.tms.fms.engineering.model.StudioService;
import com.tms.fms.facility.model.RateCard;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.util.WidgetUtil;
import com.tms.fms.validator.ValidatorSelectBox;
import com.tms.fms.widgets.BoldLabel;

public class StudioForm extends ServiceForm {
	protected EngineeringRequest request = new EngineeringRequest();
	protected SingleFacilitySelectBox facilitySelectBox;
	protected DatePopupField bookingDate;
	protected DatePopupField bookingDateTo;
	protected Radio rdBBYes;
	protected Radio rdBBNo;
	protected TimeField requiredFrom;
	protected TimeField requiredTo;
	protected TextField segment;
	protected TextField location;
	protected TimeField settingFrom;
	protected TimeField settingTo;
	protected TimeField rehearsalFrom;
	protected TimeField rehearsalTo;
	protected TimeField vtrFrom;
	protected TimeField vtrTo;
	protected Panel timePanel1;
	protected Panel timePanel2;
	protected Panel timePanel3;
	protected Panel timePanel4;
	protected Label lbFacility;
	protected Label lbBookingDate;
	protected Label lbBookingDateTo;
	protected Label lbSegment;
	protected Label lbRequiredTime;
	protected Label lbSettingTime;
	protected Label lbRehearsalTime;
	protected Label lbVtrTime;
	protected Label lbBlockBooking;
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
		lbFacility.setText(app.getMessage("fms.facility.label.selectStudio")+"*");
		addChild(lbFacility);
		
		facilitySelectBox= new SingleFacilitySelectBox("facilitySelectBox");
		facilitySelectBox.addChild(new ValidatorSelectBox("facilitySelectBoxValidator","",""));
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
		
		lbBookingDate = new BoldLabel("lbBookingDate");
		lbBookingDate.setAlign("right");
		//lbBookingDate.setText(app.getMessage("fms.facility.label.bookingDate")+"*");
		lbBookingDate.setText(app.getMessage("fms.facility.label.requiredFrom")+"*");
		addChild(lbBookingDate);
		
		bookingDate = new DatePopupField("bookingDate");
		bookingDate.setDate(dtRequiredFrom);
		addChild(bookingDate);
		
		lbBookingDateTo = new BoldLabel("lbBookingDateTo");
		lbBookingDateTo.setAlign("right");
		lbBookingDateTo.setText(app.getMessage("fms.facility.label.requiredTo")+"*");
		addChild(lbBookingDateTo);
		
		bookingDateTo = new DatePopupField("bookingDateTo");
		bookingDateTo.setDate(dtRequiredTo);
		addChild(bookingDateTo);
		
		lbSegment = new BoldLabel("lbSegment");
		lbSegment.setAlign("right");
		lbSegment.setText(app.getMessage("fms.facility.label.segment"));
		addChild(lbSegment);
		
		segment =new TextField("segment");
		addChild(segment);
		
		lbRequiredTime = new BoldLabel("lbRequiredTime");
		lbRequiredTime.setAlign("right");
		lbRequiredTime.setText(app.getMessage("fms.facility.label.requiredTime"));
		addChild(lbRequiredTime);
		
		timePanel4= new Panel("timePanel4");
		timePanel4.addChild(new Label("from",app.getMessage("fms.facility.label.from")));
		requiredFrom = new TimeField("requiredFrom");
		requiredFrom.setTemplate("calendar/fromTimefield");
		timePanel4.addChild(requiredFrom);
		timePanel4.addChild(new Label("to"," "+app.getMessage("fms.facility.label.to")));
		requiredTo = new TimeField("requiredTo");
		requiredTo.setTemplate("calendar/toTimeField");
		timePanel4.addChild(requiredTo);
		addChild(timePanel4);
		
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
		
		lbVtrTime = new BoldLabel("lbVtrTime");
		lbVtrTime.setAlign("right");
		lbVtrTime.setText(app.getMessage("fms.facility.label.VTRTime"));
		addChild(lbVtrTime);
		
		timePanel3= new Panel("timePanel3");
		timePanel3.addChild(new Label("from",app.getMessage("fms.facility.label.from")));
		vtrFrom = new TimeField("vtrFrom");
		timePanel3.addChild(vtrFrom);
		timePanel3.addChild(new Label("to"," "+app.getMessage("fms.facility.label.to")));
		vtrTo = new TimeField("vtrTo");
		timePanel3.addChild(vtrTo);
		addChild(timePanel3);
		
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
		StudioService s=module.getStudioService(id);
		serviceId=s.getServiceId();
		requestId=s.getRequestId();
		request = module.getRequestWithService(requestId);
		if ("1".equals(s.getBlockBooking())){
			rdBBYes.setChecked(true);
			rdBBNo.setChecked(false);
		} else {
			rdBBNo.setChecked(true);
			rdBBYes.setChecked(false);
		}
		facilitySelectBox.setServiceId(serviceId);
		facilitySelectBox.setIds(new String[]{s.getFacilityId()});
		
		bookingDate.setDate(s.getBookingDate());
		bookingDateTo.setDate(s.getBookingDateTo());
		segment.setValue(s.getSegment());
		location.setValue(s.getLocation());
		WidgetUtil.populateTimeField(requiredFrom,s.getRequiredFrom());
		WidgetUtil.populateTimeField(requiredTo,s.getRequiredTo());
		WidgetUtil.populateTimeField(settingFrom, s.getSettingFrom());
		WidgetUtil.populateTimeField(settingTo, s.getSettingTo());
		WidgetUtil.populateTimeField(rehearsalFrom, s.getRehearsalFrom());
		WidgetUtil.populateTimeField(rehearsalTo, s.getRehearsalTo());
		WidgetUtil.populateTimeField(vtrFrom, s.getVtrFrom());
		WidgetUtil.populateTimeField(vtrTo, s.getVtrTo());
	}
	
	public Forward onValidate(Event event) {
			
			StudioService service = new StudioService();
			EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
			SetupModule mod = (SetupModule)Application.getInstance().getModule(SetupModule.class);
			
			service.setRequestId(requestId);
			service.setServiceId(serviceId);
			service.setFacilityId(facilitySelectBox.getSelectedId());
			service.setBookingDate(bookingDate.getDate());
			service.setBookingDateTo(bookingDateTo.getDate());
			service.setBlockBooking(rdBBYes.isChecked()?"1":"0");
			service.setSegment((String)segment.getValue());
			/*service.setRequiredFrom(WidgetUtil.getTime(requiredFrom));
			service.setRequiredTo(WidgetUtil.getTime(requiredTo));*/
			service.setSettingFrom(WidgetUtil.getTime(settingFrom));
			service.setSettingTo(WidgetUtil.getTime(settingTo));
			service.setRehearsalFrom(WidgetUtil.getTime(rehearsalFrom));
			service.setRehearsalTo(WidgetUtil.getTime(rehearsalTo));
			service.setVtrFrom(WidgetUtil.getTime(vtrFrom));
			service.setVtrTo(WidgetUtil.getTime(vtrTo));
			service.setLocation((String)location.getValue());
			service.setSubmitted("0");
			// get internal rate and external rate from rate card
			RateCard rc = mod.getRateCardDetail(service.getFacilityId());
			service.setInternalRate(rc.getInternalRate());
			service.setExternalRate(rc.getExternalRate());
			
			if(service.getBookingDate().after(service.getBookingDateTo())){
				bookingDateTo.setInvalid(true);
				this.setInvalid(true);
				return new Forward();
			}
			
			if (service.getBookingDate().before(request.getRequiredFrom()) || service.getBookingDate().after(request.getRequiredTo())){
				bookingDate.setInvalid(true);
				this.setInvalid(true);
				return new Forward();
			}
			
			if (service.getBookingDateTo().before(request.getRequiredFrom()) || service.getBookingDateTo().after(request.getRequiredTo())){
				bookingDateTo.setInvalid(true);
				this.setInvalid(true);
				return new Forward();
			}
			
			//============== START : date and time checking =============================
			service.setRequiredFrom(WidgetUtil.getTime(requiredFrom));
			
			int hour = requiredTo.getHour(); int minute = requiredTo.getMinute();
			if(bookingDate.getDate().compareTo(bookingDateTo.getDate())==0){
				if(WidgetUtil.getTime(requiredTo).compareTo(WidgetUtil.getTime(requiredFrom))< 0){
					if(hour != 0){
						requiredFrom.setInvalid(true);
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
						requiredTo.setDate(timeT.getTime());
						service.setBookingDateTo(timeT.getTime());
						service.setRequiredTo(WidgetUtil.getTime(requiredTo));
					}else{
						Calendar timeT = Calendar.getInstance();
						timeT.set(Calendar.HOUR_OF_DAY, 23);
						timeT.set(Calendar.MINUTE, 59);
						requiredTo.setDate(timeT.getTime());
						service.setRequiredTo(WidgetUtil.getTime(requiredTo));		
					}
				}else{
					service.setRequiredFrom(WidgetUtil.getTime(requiredFrom));
					service.setRequiredTo(WidgetUtil.getTime(requiredTo));
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
					service.setRequiredFrom(WidgetUtil.getTime(requiredFrom));
					Calendar timeT = Calendar.getInstance();
					timeT.set(Calendar.HOUR_OF_DAY, 23);
					timeT.set(Calendar.MINUTE, 59);
					requiredTo.setDate(timeT.getTime());
					service.setRequiredTo(WidgetUtil.getTime(requiredTo));		
				}else{
					service.setRequiredFrom(WidgetUtil.getTime(requiredFrom));
					service.setRequiredTo(WidgetUtil.getTime(requiredTo));
				}
			}
			//============== END : date and time checking =============================
			
			// get facility name for logging
			service.setFacility(facilitySelectBox.getSelectedText());
			
			if ("Add".equals(type)) {
				try {
					module.insertStudioService(service);
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
					module.updateStudioService(service);
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
