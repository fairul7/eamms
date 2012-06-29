package com.tms.fms.engineering.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.Radio;
import kacang.stdui.TimeField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import org.apache.axis.collections.SequencedHashMap;

import com.tms.fms.engineering.model.CheckAvailabilityModule;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.facility.model.RateCard;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.facility.ui.FacilityCategoryPopupSelectBox;
import com.tms.fms.util.DateDiffUtil;
import com.tms.fms.widgets.BoldLabel;

/**
 * 
 * @author fahmi
 *
 */
public class CheckAvailabilityFCFacilityForm extends Form {
	
	protected Label lbType;
	protected Label lbStartDate;
	protected Label lbEndDate;
	protected Label lbStartTime;
	protected Label lbEndTime;
	protected Label lbFacilities;
	
	private Radio rdFacilities;
	private Radio rdManpower;
	private DatePopupField dtStart;
	private DatePopupField dtEnd;
	private TimeField tmStart;
	private TimeField tmEnd;
	private FacilityCategoryPopupSelectBox fpsbFacilityCategory;
	
	protected Button cancel;
	protected Button submit;
	protected Panel buttonPanel;
	protected Panel pnType;
	
	protected String requestId;
	protected EngineeringRequest request = new EngineeringRequest();	
	
	private Collection facilities = new ArrayList();
	private Collection dateSelected = new ArrayList();
	private Collection pds = new ArrayList();
	private Map dateSelectedMap = new SequencedHashMap();
	
	private String bookType = "F";
	private String cancelUrl = "requestDetails.jsp?requestId=";
	private static String CANCEL_ON_COORDFC = "coordCheckAvailability.jsp";
	private String mode;
	
	
	public void onRequest(Event event) {
		this.setInvalid(false);
		facilities = new ArrayList(); 
		dateSelected = new ArrayList();
		dateSelectedMap = new SequencedHashMap();
		pds = new ArrayList();
		initForm();
	}
	
	public void populateButtons() {
		Application app = Application.getInstance();
		buttonPanel = new Panel("panel");
		submit = new Button("submit", app.getMessage("fms.facility.submit"));
		cancel = new Button("cancel", app.getMessage("fms.facility.cancel"));
		buttonPanel.addChild(submit);
		buttonPanel.addChild(cancel);
		addChild(new Label(("tupuku")));
		addChild(buttonPanel);
	}

	public void initForm() {
		setMethod("post");
		setColumns(2);
		Application app = Application.getInstance();
		EngineeringModule module= (EngineeringModule)app.getModule(EngineeringModule.class);
		if (requestId!=null && !requestId.equals("")){
			request=module.getRequestWithService(requestId);
		}
		
		lbType = new BoldLabel("lbTitle");
		lbType.setAlign("right");
		lbType.setText(app.getMessage("fms.request.label.checkType"));
		addChild(lbType);
		
		pnType = new Panel("pnType");
		rdFacilities = new Radio("rdTypeFacilities", app.getMessage("fms.request.label.rdFacilities", "Facilities"));
		rdFacilities.setGroupName("groupType");
		rdFacilities.setChecked(true);
		pnType.addChild(rdFacilities);
		
		rdManpower = new Radio("rdTypeManpower", app.getMessage("fms.request.label.rdManpower", "Manpower"));
		rdManpower.setGroupName("groupType");
		rdManpower.setOnClick("goToManpower()");
		pnType.addChild(rdManpower);
		addChild(pnType);
		
		lbStartDate = new BoldLabel("lbStartDate");
		lbStartDate.setAlign("right");
		lbStartDate.setText(app.getMessage("fms.request.label.startDate"));
		addChild(lbStartDate);
		
		dtStart = new DatePopupField("dtStart");
    	dtStart.setFormat("dd-MM-yyyy");
    	if (request.getRequiredFrom() != null && !request.getRequiredFrom().toString().equals("")){
    		dtStart.setDate(request.getRequiredFrom());
    	} else {
    		dtStart.setDate(new Date());
    	}
    	dtStart.addChild(new ValidatorNotEmpty("vEmpty", app.getMessage("asset.message.vNotEmpty")));
		addChild(dtStart);
		
		lbEndDate = new BoldLabel("lbEndDate");
		lbEndDate.setAlign("right");
		lbEndDate.setText(app.getMessage("fms.request.label.endDate"));
		addChild(lbEndDate);
		
		dtEnd = new DatePopupField("dtEnd");
    	dtEnd.setFormat("dd-MM-yyyy");
    	if (request.getRequiredTo() != null && !request.getRequiredTo().toString().equals("")){
    		dtEnd.setDate(request.getRequiredTo());
    	}else{
    		dtEnd.setDate(new Date());
    	}
    	dtEnd.addChild(new ValidatorNotEmpty("vEmpty", app.getMessage("asset.message.vNotEmpty")));
		addChild(dtEnd);
		
		lbStartTime = new BoldLabel("lbStartTime");
		lbStartTime.setAlign("right");
		lbStartTime.setText(app.getMessage("fms.request.label.startTime"));
		addChild(lbStartTime);
		
		Date dateStartTime = new Date();
		dateStartTime.setHours(0);
		dateStartTime.setMinutes(0);
		
		tmStart = new TimeField("tmStart");
		tmStart.setDate(dateStartTime);
		addChild(tmStart);
		
		lbEndTime = new BoldLabel("lbEndTime");
		lbEndTime.setAlign("right");
		lbEndTime.setText(app.getMessage("fms.request.label.endTime"));
		addChild(lbEndTime);
		
		Date dateEndTime = new Date();
		dateEndTime.setHours(23);
		dateEndTime.setMinutes(45);
		
		tmEnd = new TimeField("tmEnd");
		tmEnd.setDate(dateEndTime);
		addChild(tmEnd);
		
		lbFacilities = new BoldLabel("lbFacilities");
		lbFacilities.setAlign("right");
		lbFacilities.setText(app.getMessage("fms.facility.form.selectFacilityEquipment", "Select Facility/ Equipment"));
		addChild(lbFacilities);
		
	    fpsbFacilityCategory = new FacilityCategoryPopupSelectBox("fpsbFacility");
	    fpsbFacilityCategory.setSortable(false);
	    fpsbFacilityCategory.addChild(new ValidatorNotEmpty("vEmpty", app.getMessage("asset.message.vNotEmpty")));
	    addChild(fpsbFacilityCategory);
	    fpsbFacilityCategory.init();
		
		populateButtons();
		
	}	
	
	public Forward onSubmit(Event evt){
		Forward result = super.onSubmit(evt);
		
	    //determine which button was clicked
	    String buttonName = findButtonClicked(evt);
	    if (mode.equals("check")){
		    //if the cancel button was pressed
		    if (buttonName != null && cancel.getAbsoluteName().equals(buttonName)) {
		    	init();
		      	return new Forward(Form.CANCEL_FORM_ACTION, getCancelUrl() + getRequestId(), true);
		    } else {
		    	return result;
		    }
	    } else if (mode.equals("coordinator")){
	    	if (buttonName != null && cancel.getAbsoluteName().equals(buttonName)) {
		    	init();
		      	return new Forward(Form.CANCEL_FORM_ACTION, this.CANCEL_ON_COORDFC, true);
		    } else {
		    	return result;
		    }
	    }else {
	    	return null;
	    }
	    
	}
	
	public Forward onValidate(Event event) {
		try {
			String timeFrom = "0000";
			String timeTo = "0000";
			String[] facilitiesCatId = null;
			facilities = new ArrayList(); 
			dateSelected = new ArrayList();
			
			Application application = Application.getInstance();
			SetupModule module = (SetupModule) application.getModule(SetupModule.class);

			Date stDate=dtStart.getDate();
			Date edDate=dtEnd.getDate();
			
			timeFrom = hourToString(tmStart.getHour()) + minuteToString(tmStart.getMinute());
			timeTo = hourToString(tmEnd.getHour()) + minuteToString(tmEnd.getMinute());
			
			// compare date checking : start v.s. end
			if(stDate.after(edDate)){
				dtEnd.setInvalid(true);
				this.setInvalid(true);
			}
			
			// compare time checking : start v.s. end
			// any better ways ???
			if (((tmStart.getHour()*100)+tmStart.getMinute()) > ((tmEnd.getHour()*100)+tmEnd.getMinute())){
				tmEnd.setInvalid(true);
				this.setInvalid(true);
				
			} else {
			
				// get selected facility category (from popup) 
				facilitiesCatId = fpsbFacilityCategory.getIds();
							
				if (facilitiesCatId!=null && facilitiesCatId.length >0){
					for (int i=0; i < facilitiesCatId.length; i++){
						RateCard rc = module.getRateCardCheckAvailability(facilitiesCatId[i]);
						facilities.add(rc);
					}
				}
				populateDateSelected(stDate, edDate, timeFrom, timeTo, facilities);
			}
			
			return new Forward();
			
		}catch (Exception e) {
			
			Log.getLog(getClass()).error(e.toString()); 
			return new Forward("FAILED");
		} 
	}

	public void populateDateSelected(Date start, Date end, String timeFrom, String timeTo, Collection facilities){
		long diff = dateDiff(start, end);
		Application application = Application.getInstance();
		CheckAvailabilityModule module = (CheckAvailabilityModule)application.getModule(CheckAvailabilityModule.class);	
		
		dateSelectedMap.clear();
				
		for (int x = 0; x <= diff; x++){
			Calendar cal = Calendar.getInstance();
			cal.setTime(start);
			cal.add(Calendar.DATE, x);
			Date dateChecked = cal.getTime();
			
			pds = module.getFacilityByDate(dateChecked, timeFrom, timeTo, getBookType(), facilities);
			
			dateSelectedMap.put(dateChecked, pds);
		}
	}
	
	/**
	 * 
	 * @param hour (integer)
	 * @return String of hour (adding prefix "0" if hour < 10)
	 */
	public String hourToString(int hour){
		String H = "00";
		if (hour >= 0 && hour < 24) {
			if (hour < 10) {
				H = "0" + hour;
			} else {
				H = hour + "";
			}
		}
		return H;
	}
	
	/**
	 * 
	 * @param minute (integer)
	 * @return String of minute (adding prefix "0" if <code>minute</code> < 10)
	 */
	public String minuteToString(int minute){
		String M = "00";
		if (minute >= 0 && minute <= 60) {
			if (minute < 10) {
				M = "0" + minute;
			} else {
				M = minute + "";
			}
		}
		return M;
	}
	
	/**
	 * 
	 * Count the differences between date start and date end
	 * 
	 * @param start (Date From)
	 * @param end	(Date To)
	 * @return 
	 */
	public long dateDiff(Date start, Date end){
		
		long diff = Math.round((end.getTime() - start.getTime()) / (DateDiffUtil.MS_IN_A_DAY));
		
		return diff;
	}
	
	public String getDefaultTemplate(){
		return "fms/checkavailabilityFCfacility";
	}
	
	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public Radio getRdFacilities() {
		return rdFacilities;
	}

	public void setRdFacilities(Radio rdFacilities) {
		this.rdFacilities = rdFacilities;
	}

	public Radio getRdManpower() {
		return rdManpower;
	}

	public void setRdManpower(Radio rdManpower) {
		this.rdManpower = rdManpower;
	}

	public DatePopupField getDtStart() {
		return dtStart;
	}

	public void setDtStart(DatePopupField dtStart) {
		this.dtStart = dtStart;
	}

	public DatePopupField getDtEnd() {
		return dtEnd;
	}

	public void setDtEnd(DatePopupField dtEnd) {
		this.dtEnd = dtEnd;
	}

	public TimeField getTmStart() {
		return tmStart;
	}

	public void setTmStart(TimeField tmStart) {
		this.tmStart = tmStart;
	}

	public TimeField getTmEnd() {
		return tmEnd;
	}

	public void setTmEnd(TimeField tmEnd) {
		this.tmEnd = tmEnd;
	}

	public FacilityCategoryPopupSelectBox getFpsbFacilityCategory() {
		return fpsbFacilityCategory;
	}

	public void setFpsbFacilityCategory(
			FacilityCategoryPopupSelectBox fpsbFacilityCategory) {
		this.fpsbFacilityCategory = fpsbFacilityCategory;
	}

	public Panel getButtonPanel() {
		return buttonPanel;
	}

	public void setButtonPanel(Panel buttonPanel) {
		this.buttonPanel = buttonPanel;
	}

	public Panel getPnType() {
		return pnType;
	}

	public void setPnType(Panel pnType) {
		this.pnType = pnType;
	}

	public Collection getFacilities() {
		return facilities;
	}

	public void setFacilities(Collection facilities) {
		this.facilities = facilities;
	}

	public Collection getDateSelected() {
		return dateSelected;
	}

	public void setDateSelected(Collection dateSelected) {
		this.dateSelected = dateSelected;
	}

	public String getBookType() {
		return bookType;
	}

	public void setBookType(String bookType) {
		this.bookType = bookType;
	}

	public Map getDateSelectedMap() {
		return dateSelectedMap;
	}

	public void setDateSelectedMap(Map dateSelectedMap) {
		this.dateSelectedMap = dateSelectedMap;
	}
	
	// using keySet()
	public Collection getDates(){
		return dateSelectedMap.keySet();
	}

	public String getCancelUrl() {
		return cancelUrl;
	}

	public void setCancelUrl(String cancelUrl) {
		this.cancelUrl = cancelUrl;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
}
