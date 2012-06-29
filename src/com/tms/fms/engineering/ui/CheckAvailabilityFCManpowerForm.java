package com.tms.fms.engineering.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.axis.collections.SequencedHashMap;

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

import com.tms.fms.engineering.model.CheckAvailabilityModule;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.facility.ui.ManpowerPopupSelectBox;
import com.tms.fms.util.DateDiffUtil;
import com.tms.fms.widgets.BoldLabel;

/**
 * 
 * @author fahmi
 *
 */
public class CheckAvailabilityFCManpowerForm extends Form {
	protected Label lbType;
	protected Label lbStartDate;
	protected Label lbEndDate;
	protected Label lbStartTime;
	protected Label lbEndTime;
	protected Label lbManpower;
	
	private Radio rdFacilities;
	private Radio rdManpower;
	private DatePopupField dtStart;
	private DatePopupField dtEnd;
	private TimeField tmStart;
	private TimeField tmEnd;
	private ManpowerPopupSelectBox mpsbManpowerType;
	
	protected Button cancel;
	protected Button submit;
	protected Panel buttonPanel;
	protected Panel pnType;
	
	protected String requestId;
	protected EngineeringRequest request;	
	
	private Collection manpowers = new ArrayList();
	private Collection dateSelected = new ArrayList();
	private Collection pds = new ArrayList();
	private Map dateSelectedMap = new SequencedHashMap();
	
	private String bookType = "M";
	private String cancelUrl = "requestDetails.jsp?requestId=";
	private static String CANCEL_COORD = "coordCheckAvailabilityFCmanpower.jsp";
	private String mode;
	
	public void onRequest(Event event) {
		this.setInvalid(false);
		manpowers = new ArrayList(); 
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
		rdFacilities.setOnClick("goToFacility()");
		pnType.addChild(rdFacilities);
		
		rdManpower = new Radio("rdTypeManpower", app.getMessage("fms.request.label.rdManpower", "Manpower"));
		rdManpower.setGroupName("groupType");
		rdManpower.setChecked(true);
		pnType.addChild(rdManpower);
		addChild(pnType);
		
		lbStartDate = new BoldLabel("lbStartDate");
		lbStartDate.setAlign("right");
		lbStartDate.setText(app.getMessage("fms.request.label.startDate"));
		addChild(lbStartDate);
		
		dtStart = new DatePopupField("dtStart");
    	dtStart.setFormat("dd-MM-yyyy");
    	if (request != null && !request.getRequiredFrom().toString().equals("")){
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
    	if (request != null && !request.getRequiredTo().toString().equals("")){
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
		
		lbManpower = new BoldLabel("lbManpower");
		lbManpower.setAlign("right");
		lbManpower.setText(app.getMessage("fms.facility.form.selectManpower", "Select Manpower"));
		addChild(lbManpower);
		
		mpsbManpowerType = new ManpowerPopupSelectBox("mpsbManpowerType");
		mpsbManpowerType.setSortable(false);
		mpsbManpowerType.addChild(new ValidatorNotEmpty("vEmpty", app.getMessage("asset.message.vNotEmpty")));
		addChild(mpsbManpowerType);
		mpsbManpowerType.init();
		
		populateButtons();
		
	}
	
	public Forward onSubmit(Event evt){
		Forward result = super.onSubmit(evt);
		
	    //determine which button was clicked
	    String buttonName = findButtonClicked(evt);
	    if(mode.equals("check")){
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
	    		return new Forward(Form.CANCEL_FORM_ACTION, CANCEL_COORD, true);
	    	} else {
	    		return result;
	    	}
	    } else {
	    	return  null;
	    }
	}
	
	public Forward onValidate(Event event) {
		try {
			String timeFrom = "0000";
			String timeTo = "0000";
			String[] manpowerId = null;
			manpowers = new ArrayList();
			dateSelected = new ArrayList();
			
			Application app = Application.getInstance();
			CheckAvailabilityModule module = (CheckAvailabilityModule)app.getModule(CheckAvailabilityModule.class);		
        	
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
			
				// get selected manpower type (from popup) 
				manpowerId = mpsbManpowerType.getIds();
				
				if (manpowerId!=null && manpowerId.length >0){
					for (int i=0; i < manpowerId.length; i++){
						HashMap map = (HashMap) module.selectManpowerByCompetencyId(manpowerId[i]);
						//map.put("idx", manpowerId[i]);
						//map.put("countAvailable", mod.countManpowerAvailable(dateChecked, timeFrom, timeTo));
						manpowers.add(map);
					}
				}
				
				populateDateSelected(stDate, edDate, timeFrom, timeTo, manpowers);
				
				printKs();
			}
			
			return new Forward();
		}catch (Exception e) {
			Log.getLog(getClass()).error(e.toString()); 
			return new Forward("FAILED");
		} 
	}
	
	public void populateDateSelected(Date start, Date end, String timeFrom, String timeTo, Collection manpowers){
		long diff = dateDiff(start, end);
		Application application = Application.getInstance();
		CheckAvailabilityModule module = (CheckAvailabilityModule)application.getModule(CheckAvailabilityModule.class);			
		
		dateSelectedMap.clear();
			
		for (int x = 0; x <= diff; x++){
			Calendar cal = Calendar.getInstance();
			cal.setTime(start);
			cal.add(Calendar.DATE, x);
			Date dateChecked = cal.getTime();
			
			pds = module.getManpowerByDate(dateChecked, timeFrom, timeTo, getBookType(), manpowers);
			
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
		return "fms/checkavailabilityFCmanpower";
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

	public ManpowerPopupSelectBox getMpsbManpowerType() {
		return mpsbManpowerType;
	}

	public void setMpsbManpowerType(ManpowerPopupSelectBox mpsbManpowerType) {
		this.mpsbManpowerType = mpsbManpowerType;
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

	public Collection getManpowers() {
		return manpowers;
	}

	public void setManpowers(Collection manpowers) {
		this.manpowers = manpowers;
	}

	public Collection getDateSelected() {
		return dateSelected;
	}

	public void setDateSelected(Collection dateSelected) {
		this.dateSelected = dateSelected;
	}

	public Collection getPds() {
		return pds;
	}

	public void setPds(Collection pds) {
		this.pds = pds;
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
	
	public void printKs(){
		
		Object[] str = dateSelectedMap.keySet().toArray();
		
		//Log.getLog(getClass()).info(str[0]);
		//Log.getLog(getClass()).info(str[1]);
	}
	
	public String getBookType() {
		return bookType;
	}

	public void setBookType(String bookType) {
		this.bookType = bookType;
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
