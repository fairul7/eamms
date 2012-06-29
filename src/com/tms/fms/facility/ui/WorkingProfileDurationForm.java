package com.tms.fms.facility.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.SelectBox;
import kacang.stdui.validator.ValidatorSelectBoxNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.fms.engineering.ui.SingleFacilitySelectBox;
import com.tms.fms.engineering.ui.SingleTiedStudioSelectBox;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.facility.model.WorkingProfile;
import com.tms.fms.util.WidgetUtil;
import com.tms.fms.validator.ValidatorSelectBox;
import com.tms.fms.widgets.BoldLabel;


public class WorkingProfileDurationForm extends Form {
	
	private Button cancel;
	private Button submit;
	private String type;
	private Label lbFromDate;
	private Label lbToDate;
	private Label lbWorkingProfile;
	private Label lbManpower;
	private Panel panel;
	private DatePopupField fromDate;
	private DatePopupField toDate;
	private SelectBox workingProfile;
	private UnitUserSelectBox userSelectBox;
	private String workingProfileDurationId;
	private Label[] lbStudios;
	protected SingleTiedStudioSelectBox[] studioSelectBox;

	public WorkingProfileDurationForm() {}

	public WorkingProfileDurationForm(String s) {super(s);}

	public void init() {
		if (type == null || (!"Add".equals(type) && !"Edit".equals(type))) {
			type = "Add";
		}
	}

	public void onRequest(Event event) {
		initForm();
		if ("Edit".equals(type)) {
			populateFields();
		}
	}

	public void initForm() {
		setMethod("post");
		setColumns(2);
		Application app = Application.getInstance();
		String msgNotEmpty = app.getMessage("lms.elearning.validation.mandatoryField");
		
		lbFromDate = new BoldLabel("lbFromDate");
		lbFromDate.setAlign("right");
		lbFromDate.setText(app.getMessage("fms.facility.label.dateFrom") + " *");
		
		fromDate = new DatePopupField("fromDate");
		fromDate.setAlign("left");
		
		lbToDate = new BoldLabel("lbToDate");
		lbToDate.setAlign("right");
		lbToDate.setText(app.getMessage("fms.facility.label.dateTo")+ " *");
		
		toDate = new DatePopupField("toDate");
		toDate.setAlign("left");
		
		lbWorkingProfile = new BoldLabel("lbWorkingProfile");
		lbWorkingProfile.setAlign("right");
		lbWorkingProfile.setText(app.getMessage("fms.facility.label.workingProfile")+ " *");

		workingProfile = new SelectBox("workingProfile");
		ValidatorSelectBoxNotEmpty vsne=new ValidatorSelectBoxNotEmpty("workingProfileNotEmpty", msgNotEmpty);
		SetupModule module = (SetupModule)Application.getInstance().getModule(SetupModule.class);
		workingProfile.addOption("-1", app.getMessage("fms.facility.msg.selectWorkingProfile"));
		for(Iterator itr=module.getWorkingProfile().iterator();itr.hasNext();){
			WorkingProfile wp=(WorkingProfile)itr.next();
			workingProfile.addOption(wp.getWorkingProfileId(), wp.getName());
		}
		workingProfile.addChild(vsne);
		
		
		lbManpower = new BoldLabel("lbManpower");
		lbManpower.setAlign("right");
		lbManpower.setText(app.getMessage("fms.facility.label.selectManpower")+ " *");

		userSelectBox = new UnitUserSelectBox("userSelectBox");
		userSelectBox.init();
		
		panel = new Panel("panel");

		submit = new Button("submit", app.getMessage("fms.facility.submit"));

		cancel = new Button("cancel", app.getMessage("fms.facility.cancel"));
		
		
		panel.addChild(submit);
		panel.addChild(cancel);
		
		addChild(lbFromDate);
		addChild(fromDate);
		
		addChild(lbToDate);
		addChild(toDate);

		addChild(lbWorkingProfile);
		addChild(workingProfile);
		studioSelectBox= new SingleTiedStudioSelectBox[8];
		lbStudios = new BoldLabel[8];
		for(int i=0;i<8;i++){
			lbStudios[i] = new BoldLabel("lbStudios"+i);
			lbStudios[i].setAlign("right");
			lbStudios[i].setText("Studio " + (i+1));
			studioSelectBox[i] = new SingleTiedStudioSelectBox("studioSelectBox"+i);
			studioSelectBox[i].init();
			
			addChild(lbStudios[i]);
			addChild(studioSelectBox[i]);
		}
		addChild(lbManpower);
		addChild(userSelectBox);
		
		addChild(new Label("blank"));
		addChild(panel);
	}

	public void populateFields() {
		
		try {
			SetupModule module = (SetupModule)Application.getInstance().getModule(SetupModule.class);
			WorkingProfile wp = new WorkingProfile();
			Map wpmanpower = new HashMap();
			
			wp = module.getWorkingProfileDuration(workingProfileDurationId);
			
			if(null == wp)
				wp = module.getWorkingProfileDuration(workingProfileDurationId);
			
			if(wp!=null){
				wpmanpower = module.getWorkingProfileDurationManpower(workingProfileDurationId);
				workingProfile.setSelectedOption(wp.getWorkingProfileId());
				fromDate.setDate(wp.getStartDate());
				toDate.setDate(wp.getEndDate());
				userSelectBox.setIds(wp.getUsers());
				studioSelectBox[0].setIds(new String[]{(String)wpmanpower.get("studio1")});
				studioSelectBox[1].setIds(new String[]{(String)wpmanpower.get("studio2")});
				studioSelectBox[2].setIds(new String[]{(String)wpmanpower.get("studio3")});
				studioSelectBox[3].setIds(new String[]{(String)wpmanpower.get("studio4")});
				studioSelectBox[4].setIds(new String[]{(String)wpmanpower.get("studio5")});
				studioSelectBox[5].setIds(new String[]{(String)wpmanpower.get("studio6")});
				studioSelectBox[6].setIds(new String[]{(String)wpmanpower.get("studio7")});
				studioSelectBox[7].setIds(new String[]{(String)wpmanpower.get("studio8")});
				//userSelectBox.generateOptionMap();
			}
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.toString());
		}
	}

	public Forward onSubmit(Event evt) {
		String buttonName = findButtonClicked(evt);
		kacang.ui.Forward result = super.onSubmit(evt);
		if (buttonName != null && cancel.getAbsoluteName().equals(buttonName)) {
			init();
			return new Forward(Form.CANCEL_FORM_ACTION, "workingProfileDuration.jsp", true);
		}
		else {return result;}
	}

	public Forward onValidate(Event event) {		
		WorkingProfile wp = new WorkingProfile();
		SetupModule module = (SetupModule)Application.getInstance().getModule(SetupModule.class);
		wp.setWorkingProfileDurationId(workingProfileDurationId);
		wp.setWorkingProfileId(WidgetUtil.getSbValue(workingProfile));
		wp.setStartDate(fromDate.getDate());
		wp.setEndDate(toDate.getDate());
		wp.setManpowerMap(userSelectBox.getSelectedOptions());
		wp.setStudio1(studioSelectBox[0].getSelectedId());
		wp.setStudio2(studioSelectBox[1].getSelectedId());
		wp.setStudio3(studioSelectBox[2].getSelectedId());
		wp.setStudio4(studioSelectBox[3].getSelectedId());
		wp.setStudio5(studioSelectBox[4].getSelectedId());
		wp.setStudio6(studioSelectBox[5].getSelectedId());
		wp.setStudio7(studioSelectBox[6].getSelectedId());
		wp.setStudio8(studioSelectBox[7].getSelectedId());
			
		if ("Add".equals(type)) {					
			if(!duplicateExist(userSelectBox.getIds(), wp.getWorkingProfileId(), wp.getStartDate(), wp.getEndDate() )){						
				try {							
					module.insertWorkingProfileDuration(wp);
				}catch (Exception e) {
					Log.getLog(getClass()).error(e.toString()); 
					return new Forward("FAILED");
				}					
			} else					
				return new Forward("EXISTS");
		}
				
		if ("Edit".equals(type)) {			
			if(!isDuplicateExist(userSelectBox.getIds(), workingProfileDurationId, wp.getStartDate(), wp.getEndDate() )){
				try {						
					module.updateWorkingProfileDuration(wp);
				}catch (Exception e) {
					Log.getLog(getClass()).error(e.toString()); 
					return new Forward("FAILED");
				} 
			} else					
				return new Forward("EXISTS");
		}			
			
		return new Forward("ADDED");
	}

	
	protected boolean duplicateExist(String userIds[], String wpName, Date startDate, Date endDate){		
		boolean exist = false;
		Collection colId = new ArrayList();
		SetupModule module = (SetupModule)Application.getInstance().getModule(SetupModule.class);
		
		try{
			String[] userId = userIds;
			for(int i = 0; i < userId.length; i++){
				colId = module.selectWorkingProfileForUpdate(userId[i], startDate, endDate);				
				
				if(colId.size() > 0) {
					exist = true;		
					return exist;
				}
			}		
		}catch(Exception e){
			Log.getLog(getClass()).error(e);
		}
				
		return exist;
	}
	
	/**
	 * Checking for working profile duration when updating working profile
	 * 
	 * @param userIds
	 * @param wpName
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	protected boolean isDuplicateExist(String userIds[], String wpdId, Date startDate, Date endDate){		
		boolean exist = false;
		Collection colId = new ArrayList();
		SetupModule module = (SetupModule)Application.getInstance().getModule(SetupModule.class);
		
		try{
			String[] userId = userIds;
			for(int i = 0; i < userId.length; i++){
				colId = module.selectWorkingProfileForUpdate(userId[i], wpdId, startDate, endDate);				
				
				if(colId.size() > 0) {
					exist = true;		
					return exist;
				}
			}		
		}catch(Exception e){
			Log.getLog(getClass()).error(e);
		}
				
		return exist;
	}
			
	public String getType() {
		return type;
	}

	public void setType(String string) {
		type = string;
	}

	/**
	 * @return the workingProfileDurationId
	 */
	public String getWorkingProfileDurationId() {
		return workingProfileDurationId;
	}

	/**
	 * @param workingProfileDurationId the workingProfileDurationId to set
	 */
	public void setWorkingProfileDurationId(String workingProfileDurationId) {
		this.workingProfileDurationId = workingProfileDurationId;
	}

	

	

}
