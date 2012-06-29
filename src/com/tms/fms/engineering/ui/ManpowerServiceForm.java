package com.tms.fms.engineering.ui;

import java.util.Calendar;

import kacang.Application;
import kacang.stdui.DatePopupField;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.Radio;
import kacang.stdui.TextField;
import kacang.stdui.TimeField;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.engineering.model.ManpowerService;
import com.tms.fms.facility.model.RateCard;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.util.WidgetUtil;
import com.tms.fms.validator.ValidatorSelectBox;
import com.tms.fms.widgets.BoldLabel;

public class ManpowerServiceForm extends ServiceForm {
	protected EngineeringRequest request = new EngineeringRequest();
	protected SingleFacilitySelectBox manpowerSelectBox;
	protected TextField quantity;
	protected DatePopupField requiredFrom;
	protected DatePopupField requiredTo;
	protected TextField remarks;
	protected TextField location;
	protected TimeField fromTime;
	protected TimeField toTime;
	protected Radio rdBBYes;
	protected Radio rdBBNo;
	protected Panel timePanel;
	protected Label lbFacility;
	protected Label lbBlockBooking;
	protected Label lbDateRequiredFrom;
	protected Label lbDateRequiredTo;
	protected Label lbQuantity;
	protected Label lbRemarks;
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
		removeChildren();
		setMethod("post");
		setColumns(2);
		Application app = Application.getInstance();
		
		lbFacility = new BoldLabel("lbFacility");
		lbFacility.setAlign("right");
		lbFacility.setText(app.getMessage("fms.facility.label.manpower")+"*");
		addChild(lbFacility);
		
		manpowerSelectBox= new SingleFacilitySelectBox("manpowerSelectBox");
		manpowerSelectBox.addChild(new ValidatorSelectBox("manpowerSelectBoxValidator","",""));
		manpowerSelectBox.setServiceId(serviceId);
		manpowerSelectBox.init();
		addChild(manpowerSelectBox);
		
		lbQuantity = new BoldLabel("lbQuantity");
		lbQuantity.setAlign("right");
		lbQuantity.setText(app.getMessage("fms.facility.table.quantity")+"*");
		addChild(lbQuantity);
		
		quantity =new TextField("quantity");
		ValidatorIsNumeric vii=new ValidatorIsNumeric("quantityValidator","",false);
		quantity.addChild(vii);
		quantity.setSize("10");
		addChild(quantity);
		
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
		
		lbRequiredTime = new BoldLabel("lbRequiredTime");
		lbRequiredTime.setAlign("right");
		lbRequiredTime.setText(app.getMessage("fms.facility.label.requiredTime"));
		addChild(lbRequiredTime);
		
		timePanel= new Panel("timePanel");
		fromTime = new TimeField("fromTime");
		fromTime.setTemplate("calendar/fromTimefield");
		toTime = new TimeField("toTime");
		timePanel.addChild(new Label("from",app.getMessage("fms.facility.label.from")));
		timePanel.addChild(fromTime);
		timePanel.addChild(new Label("to"," "+app.getMessage("fms.facility.label.to")));
		toTime.setTemplate("calendar/toTimeField");
		timePanel.addChild(toTime);
		addChild(timePanel);
		
		lbLocation = new BoldLabel("lbLocation");
		lbLocation.setAlign("right");
		lbLocation.setText(app.getMessage("fms.facility.form.location"));
		addChild(lbLocation);
		
		location =new TextField("location");
		addChild(location);
		
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
		ManpowerService s=module.getManpowerService(id);
		serviceId=s.getServiceId();
		requestId=s.getRequestId();
		request = module.getRequestWithService(requestId);
		
		manpowerSelectBox.setServiceId(serviceId);
		manpowerSelectBox.setIds(new String[]{s.getCompetencyId()});
		if ("1".equals(s.getBlockBooking())){
			rdBBYes.setChecked(true);
			rdBBNo.setChecked(false);
		} else {
			rdBBNo.setChecked(true);
			rdBBYes.setChecked(false);
		}
		quantity.setValue(s.getQuantity()+"");
		requiredFrom.setDate(s.getRequiredFrom());
		requiredTo.setDate(s.getRequiredTo());
		remarks.setValue(s.getRemarks());
		location.setValue(s.getLocation());
		WidgetUtil.populateTimeField(fromTime, s.getFromTime());
		WidgetUtil.populateTimeField(toTime, s.getToTime());
	}
	
	public Forward onValidate(Event event) {
			
			ManpowerService service = new ManpowerService();
			EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
			SetupModule mod = (SetupModule)Application.getInstance().getModule(SetupModule.class);
			
			service.setRequestId(requestId);
			service.setServiceId(serviceId);
			service.setCompetencyId(manpowerSelectBox.getSelectedId());
			service.setQuantity(Integer.parseInt((String)quantity.getValue()));
			service.setBlockBooking(rdBBYes.isChecked()?"1":"0");
			service.setRequiredFrom(requiredFrom.getDate());
			service.setRequiredTo(requiredTo.getDate());
			/*service.setFromTime(WidgetUtil.getTime(fromTime));
			service.setToTime(WidgetUtil.getTime(toTime));*/
			service.setRemarks((String)remarks.getValue());
			service.setLocation((String)location.getValue());
			service.setSubmitted("0");
			// get internal rate and external rate from rate card
			RateCard rc = mod.getRateCardDetail(service.getCompetencyId());
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
			
			//============== START : date and time checking =============================
			service.setFromTime(WidgetUtil.getTime(fromTime));
			
			int hour = toTime.getHour(); int minute = toTime.getMinute();
			if(requiredFrom.getDate().compareTo(requiredTo.getDate())==0){
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
						service.setRequiredTo(timeT.getTime());
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
			
			// get competency name for logging
			service.setCompetencyName(manpowerSelectBox.getSelectedText());
			
			if ("Add".equals(type)) {
				try {
					module.insertManpowerService(service);
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
					module.updateManpowerService(service);
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
