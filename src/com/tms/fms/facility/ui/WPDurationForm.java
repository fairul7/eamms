package com.tms.fms.facility.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.facility.model.WorkingProfile;
import com.tms.fms.widgets.BoldLabel;

public class WPDurationForm extends Form {
	
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
	private UnitUserSelectBox userSelectBox;
	private String workingProfileDurationId;
	
	private Date startDate;
	private Date endDate;
	private Map manpowers;
	private String action = "";

	public WPDurationForm() {}

	public WPDurationForm(String s) {super(s);}

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
		fromDate.setFormat("dd-MM-yyyy");
		fromDate.setDate(new Date());
		fromDate.setAlign("left");
		
		lbToDate = new BoldLabel("lbToDate");
		lbToDate.setAlign("right");
		lbToDate.setText(app.getMessage("fms.facility.label.dateTo")+ " *");
		
		toDate = new DatePopupField("toDate");
		toDate.setFormat("dd-MM-yyyy");
		toDate.setDate(new Date());
		toDate.setAlign("left");
		
		lbManpower = new BoldLabel("lbManpower");
		lbManpower.setAlign("right");
		lbManpower.setText(app.getMessage("fms.facility.label.selectManpower")+ " *");

		userSelectBox = new UnitUserSelectBox("userSelectBox");
		userSelectBox.init();
		
		panel = new Panel("panel");

		submit = new Button("submit", app.getMessage("fms.facility.submit"));
		
		panel.addChild(submit);
		
		addChild(lbFromDate);
		addChild(fromDate);
		
		addChild(lbToDate);
		addChild(toDate);
		
		addChild(lbManpower);
		addChild(userSelectBox);
		
		addChild(new Label("blank"));
		addChild(panel);
	}

	public void populateFields() {
		
		try {
			SetupModule module = (SetupModule)Application.getInstance().getModule(SetupModule.class);
			WorkingProfile wp = new WorkingProfile();
			
			wp = module.getWorkingProfileDuration(workingProfileDurationId);
			
			if(null == wp)
				wp = module.getWorkingProfileDuration(workingProfileDurationId);
			
			if(wp!=null){
				//workingProfile.setSelectedOption(wp.getWorkingProfileId());
				fromDate.setDate(wp.getStartDate());
				toDate.setDate(wp.getEndDate());
				userSelectBox.setIds(wp.getUsers());
				//userSelectBox.generateOptionMap();
			}
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.toString());
		}
	}
	public Forward onSubmit(Event evt) {
	    return super.onSubmit(evt);
	}
	
	public Forward onValidate(Event event) {
		startDate = fromDate.getDate();
		endDate = toDate.getDate();
		manpowers = userSelectBox.getSelectedOptions();
		action = "submit";
		
		return super.onValidate(event);
	}
	
	protected boolean duplicateExist(String userIds[], String wpName, Date startDate, Date endDate){
		
		boolean exist = false;
		Collection colId = new ArrayList();
		SetupModule module = (SetupModule)Application.getInstance().getModule(SetupModule.class);
		try{
		
			String[] userId = userIds;
			for(int i = 0; i < userId.length; i++){
				colId = module.selectWorkingProfileForUpdate(userId[i], startDate, endDate);				
			}
		
				
		if(colId.size() > 0)
			exist = true;
		
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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Map getManpowers() {
		return manpowers;
	}

	public void setManpowers(Map manpowers) {
		this.manpowers = manpowers;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
}
