package com.tms.fms.facility.ui;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DefaultDataObject;
import kacang.services.security.SecurityService;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Radio;
import kacang.stdui.ResetButton;
import kacang.stdui.SelectBox;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.fms.abw.model.AbwModule;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.scheduler.ManpowerAutoAssign;
import com.tms.fms.scheduler.SchedulerUtil;
import com.tms.fms.scheduler.SynchronizeProgramCode;
import com.tms.fms.setup.model.AbwEngTransferCostTask;
import com.tms.fms.setup.model.SetupModule;
import com.tms.fms.util.JobUtil;
import com.tms.fms.util.WidgetUtil;

public class GlobalAssignmentForm extends Form {
	public static final String FORWARD_ADD_SUCCESS = "form.add.success";
	public static final String FORWARD_ADD_FAILED = "form.add.failed";
	public static final String FORWARD_EDIT_SUCCESS = "form.edit.success";
	public static final String FORWARD_SETTING_SUCCESS = "form.setting.success";
	public static final String FORWARD_SETTING_FAILED = "form.setting.failed";
	public static final String FORWARD_AUTOSETTING_SUCCESS = "form.autosetting.success";
	public static final String FORWARD_AUTOSETTING_FAILED = "form.autosetting.failed";
	public static final String FORWARD_SCHEDULE_SUCCESS = "form.schedule.success";
	public static final String FORWARD_SCHEDULE_ABW_SUCCESS = "form.schedule.abw.success";
	public static final int MAXIMUM_DAYS = 3;

	private TextField tfRefreshRate;
	private TextField tfNoOfDays;
	private TextBox tbFooterMessage;
	private Radio[] daysSetting = new Radio[EngineeringModule.DAY_SETTING.size()]; 
	private Radio[] daysAutoSetting = new Radio[EngineeringModule.DAY_AUTO_SETTING.size()]; 
	private SelectBox SyncHour1;
	private SelectBox SyncMinute1;
	private SelectBox SyncHour2;
	private SelectBox SyncMinute2;
	private Label scheduleTime1Label;
	private Label scheduleTime1ModifiedDate;
	private Label scheduleTime2Label;
	private Label scheduleTime2ModifiedDate;
	private SelectBox abwSchedHour;
	private SelectBox abwSchedMins;
	private SelectBox abwEngSchedHour;
	private SelectBox abwEngSchedMins;
	
	private Button btnSubmit;
	private Button btnSubmitAssignment;
	private Button btnSubmitAutoAssignment;
	private Button btnSyncScheduleTime;
	private Button btnAbwSchedTime;
	private Button btnAbwEngTransferCostSched;
	private ResetButton btnCancel;
	
	private String abwTransSched, abwTransSchedModDate;
	private String abwEngTransSched, abwEngTransSchedModDate;
	
	private ValidatorIsNumeric vin = new ValidatorIsNumeric("vin");
		
	public void init() {
		setWidth("100%");
		initForm();
	}
	
	public void onRequest(Event event) {
		initForm();
		populateField();
	}
	
	public void initForm() {
		setMethod("post");
		Application application = Application.getInstance();
		EngineeringModule module = (EngineeringModule) application.getModule(EngineeringModule.class);
		
		tfRefreshRate = new TextField("tfRefreshRate");
		tfRefreshRate.setSize("5");
		tfRefreshRate.setMaxlength("10");
		addChild(tfRefreshRate);
		
		tfNoOfDays = new TextField("tfNoOfDays");
		tfNoOfDays.setSize("5");
		tfNoOfDays.setMaxlength("2");
		addChild(tfNoOfDays);
		
		tbFooterMessage = new TextBox("tbFooterMessage");
		tbFooterMessage.setCols("50");
		tbFooterMessage.setRows("4");
		addChild(tbFooterMessage);
		
		btnSubmit = new Button("btnSubmit", application.getMessage("fms.facility.submit", "Submit"));
		addChild(btnSubmit);
		
		btnCancel = new ResetButton("btnCancel");
		btnCancel.setText("Reset");
		addChild(btnCancel);
		
		int i=0;
		int settingValue = module.getSettingValue();
		for(Iterator itr = EngineeringModule.DAY_SETTING.keySet().iterator();itr.hasNext();i++){
			String key=(String)itr.next();
			daysSetting[i]=new Radio("day_"+key);
			daysSetting[i].setText((String)EngineeringModule.DAY_SETTING.get(key));
			daysSetting[i].setValue(key);
			if(settingValue!=0 && settingValue==Integer.parseInt(key)){
				daysSetting[i].setChecked(true);
			}
			daysSetting[i].setGroupName("assignmentRadio");
			addChild(daysSetting[i]);
		}
		
		SyncHour1 = new SelectBox("SyncHour1");
		SyncHour2 = new SelectBox("SyncHour2");
		abwSchedHour = new SelectBox("abwSchedHour");
		abwEngSchedHour = new SelectBox("abwEngSchedHour");
		for(int j=0; j<24; j++){
			String val = ( j<10 ) ? ("0"+j) : (""+j); 
			SyncHour1.addOption(val, val);
			SyncHour2.addOption(val, val);
			abwSchedHour.addOption(Integer.toString(j), val);
			abwEngSchedHour.addOption(val, val);
		}
		addChild(SyncHour1);
		addChild(SyncHour2);
		addChild(abwSchedHour);
		addChild(abwEngSchedHour);
		
		SyncMinute1 = new SelectBox("SyncMinute1");
		SyncMinute2 = new SelectBox("SyncMinute2");
		abwSchedMins = new SelectBox("abwSchedMins");
		abwEngSchedMins = new SelectBox("abwEngSchedMins");
		for(int j=0; j<60; j++){
			String val = ( j<10 ) ? ("0"+j) : (""+j); 
			SyncMinute1.addOption(val, val);
			SyncMinute2.addOption(val, val);
			abwSchedMins.addOption(Integer.toString(j), val);
			abwEngSchedMins.addOption(val, val);
		}
		addChild(SyncMinute1);
		addChild(SyncMinute2);
		addChild(abwSchedMins);
		addChild(abwEngSchedMins);
					
		scheduleTime1Label = new Label("scheduleTime1Label");
		addChild(scheduleTime1Label);
		scheduleTime1ModifiedDate  = new Label("scheduleTime1ModifiedDate");
		addChild(scheduleTime1ModifiedDate);
		scheduleTime2Label  = new Label("scheduleTime2Label");
		addChild(scheduleTime2Label);
		scheduleTime2ModifiedDate  = new Label("scheduleTime2ModifiedDate");
		addChild(scheduleTime2ModifiedDate);
		
		btnSubmitAssignment = new Button("btnSubmitAssignment", application.getMessage("fms.facility.submit", "Submit"));
		addChild(btnSubmitAssignment);
		
		int j=0;
		int autoSettingValue = module.getAutoSettingValue();
		for(Iterator itr = EngineeringModule.DAY_AUTO_SETTING.keySet().iterator();itr.hasNext();j++){
			String key=(String)itr.next();
			daysAutoSetting[j]=new Radio("dayAuto_"+key);
			daysAutoSetting[j].setText((String)EngineeringModule.DAY_AUTO_SETTING.get(key));
			daysAutoSetting[j].setValue(key);
			if(autoSettingValue!=0 && autoSettingValue==Integer.parseInt(key)){
				daysAutoSetting[j].setChecked(true);
			}
			daysAutoSetting[j].setGroupName("autoAssignmentRadio");
			addChild(daysAutoSetting[j]);
		}
		
		btnSubmitAutoAssignment = new Button("btnSubmitAutoAssignment", application.getMessage("fms.facility.submit", "Submit"));
		addChild(btnSubmitAutoAssignment);
		
		btnSyncScheduleTime = new Button("btnSyncScheduleTime", application.getMessage("fms.facility.submit", "Submit"));
		addChild(btnSyncScheduleTime);
		
		btnAbwSchedTime = new Button("btnAbwSchedTime", application.getMessage("fms.tran.submit"));
		addChild(btnAbwSchedTime);
		
		btnAbwEngTransferCostSched = new Button("btnAbwEngTransferCostSched", application.getMessage("fms.tran.submit"));
		addChild(btnAbwEngTransferCostSched);
	}
	
	public void populateField() {
		EngineeringRequest er = new EngineeringRequest();
		EngineeringModule mod = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
			
		er = mod.getGlobalAssignment();
		if (er != null) {
			tfRefreshRate.setValue(String.valueOf(er.getRefreshRate()));
			tfNoOfDays.setValue(String.valueOf(er.getNoOfDays()));
			tbFooterMessage.setValue(er.getFooterMessage());
		}
		try {
			DefaultDataObject objSchedule1 = mod.selectGlobalSetup(AbwModule.SYNCHRONIZE_CODE_SCHEDULE_TASK);
			String time1= (String)objSchedule1.getProperty("scheduleTime1");
			time1 = time1 ==null ? "-" : time1;
			time1.trim();
			scheduleTime1Label.setText(time1);
			
			String time2= (String)objSchedule1.getProperty("scheduleTime2");
			time2 = time2 ==null ? "-" : time2;
			time2.trim();
			scheduleTime2Label.setText(time2);
			
			Format format =     new SimpleDateFormat("dd-MM-yyyy");
			String cdate = objSchedule1.getProperty("createdDate") == null ? "" : ""+format.format(objSchedule1.getProperty("createdDate"));
			String mdate = objSchedule1.getProperty("modifiedDate") == null ? "" : ""+format.format(objSchedule1.getProperty("modifiedDate"));
			
			String text = ( objSchedule1.getProperty("modifiedDate") == null ) ? cdate : mdate;
			text = text ==null  || "".equals(text)? "-" : text;
			scheduleTime1ModifiedDate.setText(text);
			scheduleTime2ModifiedDate.setText(text);
			
			String times[] = time1.split(":");
			if(times.length>1){
				SyncHour1.setSelectedOption(times[0]);
				SyncMinute1.setSelectedOption(times[1]);
			}	
			
			String times2[] = time2.split(":");
			if(times2.length>1){
				SyncHour2.setSelectedOption(times2[0]);
				SyncMinute2.setSelectedOption(times2[1]);
			}		
			
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		
		
		SetupModule stpMod = (SetupModule) Application.getInstance().getModule(SetupModule.class);
		DefaultDataObject abwTransSchedObj = stpMod.getAbwSchedulerTime(SetupModule.ABW_TRANSFER_COST_SCHEDULE_ID);
		if(abwTransSchedObj != null){
			abwTransSched = (String) abwTransSchedObj.getProperty("scheduleTime1");
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			abwTransSchedModDate = sdf.format((Date) abwTransSchedObj.getProperty("modifiedDate"));
			
			int hour = Integer.parseInt(abwTransSched.substring(0, 2));
			int minute = Integer.parseInt(abwTransSched.substring(3));
			abwSchedHour.setSelectedOption(Integer.toString(hour));
			abwSchedMins.setSelectedOption(Integer.toString(minute));
		}
		else{
			abwTransSched = "-";
			abwTransSchedModDate = "";
		}
		
		abwEngTransSched = "-";
		abwEngTransSchedModDate = "-";
		DefaultDataObject abwEngScheObj = mod.selectGlobalSetup(SetupModule.ABW_ENG_TRANSFER_COST_SCHEDULE_ID);
		if(abwEngScheObj != null)
		{
			String time = (String)abwEngScheObj.getProperty("scheduleTime1");
			if(time != null)
			{
				abwEngTransSched = time;
				
				String[] t = time.split(":");
				abwEngSchedHour.setSelectedOption(t[0]);
				if(t[1] != null)
				{
					abwEngSchedMins.setSelectedOption(t[1]);
				}
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			Date lastModDate = (Date) abwEngScheObj.getProperty("modifiedDate");
			if(lastModDate == null)
			{
				lastModDate = (Date) abwEngScheObj.getProperty("createdDate");
			}
			abwEngTransSchedModDate = lastModDate != null ? sdf.format(lastModDate) : "-";
		}
	}
	
	/* (non-Javadoc)
	 * @see kacang.stdui.Form#onValidate(kacang.ui.Event)
	 */
	public Forward onValidate(Event event) {
		
		EngineeringRequest er = new EngineeringRequest();
		EngineeringModule mod = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		String button = findButtonClicked(event);
		boolean isAllFieldValid = true;
		String fwd = FORWARD_ADD_FAILED;
		
		if (button != null && btnSubmit.getAbsoluteName().equals(button)){
			boolean isGlobalAssignmentExist = mod.isGlobalAssignmentExist();
			
			if (tfRefreshRate.getValue()!=null && !"".equals((String)tfRefreshRate.getValue()) 
					&& vin.validate(tfRefreshRate) 
					&& Integer.parseInt(tfRefreshRate.getValue().toString())>0) {
				er.setRefreshRate(Integer.parseInt(tfRefreshRate.getValue().toString()));
			} else {
				tfRefreshRate.setInvalid(true);
				isAllFieldValid = false;
			}
			
			if (tfNoOfDays.getValue()!=null && !"".equals((String)tfNoOfDays.getValue()) && vin.validate(tfNoOfDays)) {
				
				if (Integer.parseInt(tfNoOfDays.getValue().toString())>=1 && 
						Integer.parseInt(tfNoOfDays.getValue().toString())<=MAXIMUM_DAYS) {
					er.setNoOfDays(Integer.parseInt(tfNoOfDays.getValue().toString()));
				} else {
					tfNoOfDays.setInvalid(true);
					isAllFieldValid = false;
				}
			} else {
				tfNoOfDays.setInvalid(true);
				isAllFieldValid = false;
			}
			
			er.setFooterMessage((String)tbFooterMessage.getValue());
			
			if (!isAllFieldValid) {
				return new Forward("invalid-field");
			} else {
				// if not exist in the table - add, else edit
				if (!isGlobalAssignmentExist) {
					fwd = FORWARD_ADD_SUCCESS;
					mod.insertGlobalAssignment(er);				
				} else {
					fwd = FORWARD_EDIT_SUCCESS;
					mod.updateGlobalAssignment(er);
				}
			}
		}else if(button != null && btnSubmitAssignment.getAbsoluteName().equals(button)){
			int value = Integer.parseInt(WidgetUtil.getRadioValue(daysSetting));
			if(WidgetUtil.getRadioValue(daysSetting) != null && !WidgetUtil.getRadioValue(daysSetting).equals("")){
				mod.updateAssignmentSetting(value);
//				JobUtil.scheduleDefinitelyDailyTask("ManPowerAutoAssignTask", "ManPowerAutoAssignTask", 
//						"ManPowerAutoAssignTask", new ManpowerAutoAssign(), value, 1, 1);
				fwd = FORWARD_SETTING_SUCCESS;
			}else{
				fwd = FORWARD_SETTING_FAILED;
			}
		}
		else if(button != null && btnSubmitAutoAssignment.getAbsoluteName().equals(button))
		{
			int value = Integer.parseInt(WidgetUtil.getRadioValue(daysAutoSetting));
			if(WidgetUtil.getRadioValue(daysAutoSetting) != null && !WidgetUtil.getRadioValue(daysAutoSetting).equals(""))
			{
				mod.updateAutoAssignmentSetting(value);
				
				int h = 23;
				int min = 0;
				try
				{
					h = Integer.parseInt(Application.getInstance().getMessage("fms.facility.autoAssign.sched.hour"));
					if(h > 23)
					{
						h = 0;
					}
				}
				catch(Exception e){}
			
				try
				{
					min = Integer.parseInt(Application.getInstance().getMessage("fms.facility.autoAssign.sched.min"));
					if(min > 59)
					{
						min = 0;
					}
				}
				catch(Exception e){}
				
				SchedulerUtil.scheduleDailyTask(ManpowerAutoAssign.NAME, ManpowerAutoAssign.GROUP, ManpowerAutoAssign.DESC, 
						new ManpowerAutoAssign(), h, min);
				
				fwd = FORWARD_AUTOSETTING_SUCCESS;
			}
			else{
				fwd = FORWARD_AUTOSETTING_FAILED;
			}
		}
		else if(button != null && btnSyncScheduleTime.getAbsoluteName().equals(button)){
			String hour1 = WidgetUtil.getSbValue(SyncHour1);
			String minute1 = WidgetUtil.getSbValue(SyncMinute1);
			
			String hour2 = WidgetUtil.getSbValue(SyncHour2);
			String minute2 = WidgetUtil.getSbValue(SyncMinute2);
			
			UuidGenerator uuid = UuidGenerator.getInstance();
			SecurityService securityService = (SecurityService) Application.getInstance().getService(SecurityService.class);
			
			
			DefaultDataObject objSchedule1= new DefaultDataObject();
			objSchedule1.setId(uuid.getUuid());
			objSchedule1.setProperty("taskId", AbwModule.SYNCHRONIZE_CODE_SCHEDULE_TASK);
			objSchedule1.setProperty("scheduleTime1",hour1+":"+minute1);
			objSchedule1.setProperty("scheduleTime2",hour2+":"+minute2);
			objSchedule1.setProperty("createdBy",securityService.getCurrentUser(event.getRequest()).getId() );
			objSchedule1.setProperty("createdDate",new Date());
			mod.insertOrUpdateGlobalSetup(objSchedule1);
			
			try {
				JobUtil.removeTask(AbwModule.SYNCHRONIZE_CODE_SCHEDULE_TASK +"_1", "SYNC", new SynchronizeProgramCode());
				JobUtil.scheduleDailyTask(AbwModule.SYNCHRONIZE_CODE_SCHEDULE_TASK +"_1", "SYNC", "Synchronize Program Code", new SynchronizeProgramCode(), Integer.parseInt(hour1), Integer.parseInt(minute1));
			} catch (Exception e) {
				Log.getLog(getClass()).error(e.toString(), e);
			}
			
			try {
								
					JobUtil.removeTask(AbwModule.SYNCHRONIZE_CODE_SCHEDULE_TASK +"_2", "SYNC", new SynchronizeProgramCode());
					JobUtil.scheduleDailyTask(AbwModule.SYNCHRONIZE_CODE_SCHEDULE_TASK +"_2", "SYNC", "Synchronize Program Code", new SynchronizeProgramCode(), Integer.parseInt(hour2), Integer.parseInt(minute2));
				} catch (Exception e) {
					Log.getLog(getClass()).error(e.toString(), e);
				}
			
			
			
			fwd = FORWARD_SCHEDULE_SUCCESS;
			
		}
		else if(button != null && btnAbwSchedTime.getAbsoluteName().equals(button)){
			SetupModule stpModule = (SetupModule) Application.getInstance().getModule(SetupModule.class);
			DefaultDataObject object = new DefaultDataObject();
			object.setId(UuidGenerator.getInstance().getUuid());
			object.setProperty("taskId", SetupModule.ABW_TRANSFER_COST_SCHEDULE_ID);
			object.setProperty("scheduleTime1", WidgetUtil.getSbSelectionText(abwSchedHour) + ":" + WidgetUtil.getSbSelectionText(abwSchedMins));
			object.setProperty("scheduleTime2", null);
			object.setProperty("scheduleTime3", null);
			object.setProperty("modifiedBy", Application.getInstance().getCurrentUser().getId());
			object.setProperty("modifiedDate", new Date());
			object.setProperty("createdBy", object.getProperty("modifiedBy"));
			object.setProperty("createdDate", object.getProperty("modifiedDate"));
			
			stpModule.updateAbwSchedulerTime(object);
			fwd = FORWARD_AUTOSETTING_SUCCESS;
		}
		else if(button != null && button.equals(btnAbwEngTransferCostSched.getAbsoluteName()))
		{
			String hour = WidgetUtil.getSbValue(abwEngSchedHour);
			String minute = WidgetUtil.getSbValue(abwEngSchedMins);
			
			DefaultDataObject schdObj= new DefaultDataObject();
			schdObj.setId(UuidGenerator.getInstance().getUuid());
			schdObj.setProperty("taskId", SetupModule.ABW_ENG_TRANSFER_COST_SCHEDULE_ID);
			schdObj.setProperty("scheduleTime1", hour + ":" + minute);
			schdObj.setProperty("scheduleTime2", null);
			schdObj.setProperty("scheduleTime3", null);
			schdObj.setProperty("createdBy", Application.getInstance().getCurrentUser().getId());
			schdObj.setProperty("createdDate", new Date());
			schdObj.setProperty("modifiedBy", Application.getInstance().getCurrentUser().getId());
			schdObj.setProperty("modifiedDate", new Date());
			
			mod.insertOrUpdateGlobalSetup(schdObj);
			
			int h = Integer.parseInt(hour);
			int min = Integer.parseInt(minute);
			SchedulerUtil.scheduleDailyTask(AbwEngTransferCostTask.NAME, AbwEngTransferCostTask.GROUP, AbwEngTransferCostTask.DESC, 
					new AbwEngTransferCostTask(), h, min);
			
			fwd = FORWARD_SCHEDULE_ABW_SUCCESS; 
		}
		
		return new Forward(fwd);
	}
	
	public Radio[] getDaysSetting() {
		return daysSetting;
	}

	public void setDaysSetting(Radio[] daysSetting) {
		this.daysSetting = daysSetting;
	}
	
	public Radio[] getDaysAutoSetting() {
		return daysAutoSetting;
	}

	public void setDaysAutoSetting(Radio[] daysAutoSetting) {
		this.daysAutoSetting = daysAutoSetting;
	}

	public String getDefaultTemplate(){
		return "fms/facility/assignmentInformation";
	}

	public String getAbwTransSched() {
		return abwTransSched;
	}

	public void setAbwTransSched(String abwTransSched) {
		this.abwTransSched = abwTransSched;
	}

	public String getAbwTransSchedModDate() {
		return abwTransSchedModDate;
	}

	public void setAbwTransSchedModDate(String abwTransSchedModDate) {
		this.abwTransSchedModDate = abwTransSchedModDate;
	}

	public String getAbwEngTransSched()
	{
		return abwEngTransSched;
	}

	public String getAbwEngTransSchedModDate()
	{
		return abwEngTransSchedModDate;
	}
}
