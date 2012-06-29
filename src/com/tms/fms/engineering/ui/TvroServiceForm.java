package com.tms.fms.engineering.ui;

import java.util.Calendar;

import kacang.Application;
import kacang.stdui.DatePopupField;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.Radio;
import kacang.stdui.SelectBox;
import kacang.stdui.TextField;
import kacang.stdui.TimeField;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.engineering.model.TvroService;
import com.tms.fms.facility.model.RateCard;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.util.WidgetUtil;
import com.tms.fms.validator.ValidatorSelectBox;
import com.tms.fms.widgets.BoldLabel;

public class TvroServiceForm extends ServiceForm {
	protected EngineeringRequest request = new EngineeringRequest();
	protected SingleFacilitySelectBox facilitySelectBox;
	protected TextField feedTitle;
	protected TextField location;
	protected DatePopupField requiredDate;
	protected DatePopupField requiredDateTo;
	protected TextField remarks;
	protected TimeField fromTime;
	protected TimeField toTime;
	protected SelectBox timezone;
	protected TextField timeReq;
	protected SelectBox timeMeasure;
	protected Radio rdBBYes;
	protected Radio rdBBNo;
	protected Panel timePanel;
	protected Panel timePanel1;
	protected Label lbFacility;
	protected Label lbFeedTitle;
	protected Label lbLocation;
	protected Label lbBlockBooking;
	protected Label lbDateRequiredFrom;
	protected Label lbDateRequiredTo;
	protected Label lbTotalTimeRequired;
	protected Label lbtimeZone;
	protected Label lbRemarks;
	protected Label lbRequiredTime;	
	
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
		
		lbFeedTitle = new BoldLabel("lbFeedTitle");
		lbFeedTitle.setAlign("right");
		lbFeedTitle.setText(app.getMessage("fms.facility.label.feedTitle")+"*");
		addChild(lbFeedTitle);
		
		feedTitle= new TextField("feedTitle");
		feedTitle.addChild(new ValidatorNotEmpty("feedTitleNotEmpty"));
		addChild(feedTitle);
		
		lbLocation = new BoldLabel("lbLocation");
		lbLocation.setAlign("right");
		lbLocation.setText(app.getMessage("fms.facility.table.location"));
		addChild(lbLocation);
		
		location =new TextField("location");
		addChild(location);
		
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
		
		requiredDate = new DatePopupField("requiredFrom");
		requiredDate.setDate(dtRequiredFrom);
		addChild(requiredDate);
		
		lbDateRequiredTo = new BoldLabel("lbDateRequiredTo");
		lbDateRequiredTo.setAlign("right");
		lbDateRequiredTo.setText(app.getMessage("fms.facility.label.requiredTo")+"*");
		addChild(lbDateRequiredTo);
		
		requiredDateTo = new DatePopupField("requiredTo");
		requiredDateTo.setDate(dtRequiredTo);
		addChild(requiredDateTo);
		
//		lbRequiredDate = new BoldLabel("lbRequiredDate");
//		lbRequiredDate.setAlign("right");
//		lbRequiredDate.setText(app.getMessage("fms.facility.label.requiredDate")+"*");
//		addChild(lbRequiredDate);
//		
//		requiredDate = new DatePopupField("requiredDate");
//		requiredDate.setDate(dtRequiredFrom);
//		addChild(requiredDate);
		
		
		lbRequiredTime = new BoldLabel("lbRequiredTime");
		lbRequiredTime.setAlign("right");
		lbRequiredTime.setText(app.getMessage("fms.facility.label.requiredTime"));
		addChild(lbRequiredTime);
		
		/*timePanel= new Panel("timePanel");
		fromTime = new TimeField("fromTime");
		toTime = new TimeField("toTime");
		
		timePanel.addChild(new Label("from",app.getMessage("fms.facility.label.from")));
		timePanel.addChild(fromTime);
		timePanel.addChild(new Label("to"," "+app.getMessage("fms.facility.label.to")));
		timePanel.addChild(toTime);
		addChild(timePanel);*/
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
		
		lbtimeZone= new BoldLabel("lbtimeZone");
		lbtimeZone.setAlign("right");
		lbtimeZone.setText(app.getMessage("fms.facility.label.timezone"));
		addChild(lbtimeZone);
		
		timezone =new SelectBox("timezone");
		timezone.setOptionMap(EngineeringModule.TIMEZONES);
		addChild(timezone);
		
		lbTotalTimeRequired = new BoldLabel("lbTotalTimeRequired");
		lbTotalTimeRequired.setAlign("right");
		lbTotalTimeRequired.setText(app.getMessage("fms.facility.label.totalTimeReq") + "*");
		addChild(lbTotalTimeRequired);
		
		timePanel1= new Panel("timePanel1");
		timeReq= new TextField("timeReq");
		timeReq.setSize("10");
		timeReq.addChild(new ValidatorIsNumeric("timeReqValidator","",false));
		timeMeasure = new SelectBox("timeMeasure");
		timeMeasure.setOptionMap(EngineeringModule.TIMEMEASURES);
		timePanel1.addChild(timeReq);
		timePanel1.addChild(timeMeasure);
		addChild(timePanel1);
				
		lbRemarks = new BoldLabel("lbRemarks");
		lbRemarks.setAlign("right");
		lbRemarks.setText(app.getMessage("fms.facility.label.remarks"));
		addChild(lbRemarks);
		
		remarks =new TextField("remarks");
		addChild(remarks);
		
		
		populateButtons();
		submit.setOnClick("return saveIt()");
	}
	
	private void populateFields(){
		EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		TvroService s=module.getTvroService(id);
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
		feedTitle.setValue(s.getFeedTitle());
		location.setValue(s.getLocation());
		requiredDate.setDate(s.getRequiredDate());
		requiredDateTo.setDate(s.getRequiredDateTo());
		timezone.setSelectedOption(s.getTimezone());
		timeReq.setValue(s.getTotalTimeReq());
		timeMeasure.setSelectedOption(s.getTimeMeasure());
		remarks.setValue(s.getRemarks());
		WidgetUtil.populateTimeField(fromTime, s.getFromTime());
		WidgetUtil.populateTimeField(toTime, s.getToTime());
	}
	
	public Forward onValidate(Event event) {
			
			TvroService service = new TvroService();
			EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
			SetupModule mod = (SetupModule)Application.getInstance().getModule(SetupModule.class);
			
			service.setRequestId(requestId);
			service.setServiceId(serviceId);
			service.setFacilityId(facilitySelectBox.getSelectedId());
			service.setBlockBooking(rdBBYes.isChecked()?"1":"0");
			service.setFeedTitle((String)feedTitle.getValue());
			service.setLocation((String)location.getValue());
			service.setRequiredDate(requiredDate.getDate());
			service.setRequiredDateTo(requiredDateTo.getDate());
			service.setTimezone(WidgetUtil.getSbValue(timezone));
			try{ service.setTotalTimeReq(Integer.parseInt(timeReq.getValue()+"")); }catch (Exception e) {}
			service.setTimeMeasure(WidgetUtil.getSbValue(timeMeasure));
			/*service.setFromTime(WidgetUtil.getTime(fromTime));
			service.setToTime(WidgetUtil.getTime(toTime));*/
			service.setRemarks((String)remarks.getValue());
			service.setSubmitted("0");
			//RateCard rcService = mod.getRateCardByService("7");
			//if (rcService!=null){
				//RateCard rc = mod.getRateCardDetail(rcService.getId());
				RateCard rc = mod.getRateCardDetail(service.getFacilityId());
				service.setInternalRate(rc.getInternalRate());
				service.setExternalRate(rc.getExternalRate());
			//}
			
			if(service.getRequiredDate().after(service.getRequiredDateTo())){
				requiredDateTo.setInvalid(true);
				this.setInvalid(true);
				return new Forward();
			}
			
			if (service.getRequiredDate().before(request.getRequiredFrom()) || service.getRequiredDate().after(request.getRequiredTo())){
				requiredDate.setInvalid(true);
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
			if(requiredDate.getDate().compareTo(requiredDateTo.getDate())==0){
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
					module.insertTvroService(service);
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
					module.updateTvroService(service);
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
