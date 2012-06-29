package com.tms.hr.recruit.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.ui.Event;
import kacang.util.Log;

import com.tms.hr.recruit.model.RecruitModule;
import com.tms.hr.recruit.model.VacancyObj;

public class VacancyViewPopUp extends Form{
    public static final String TYPE_USAGE = "big";
    
	private String vacancyCode;
	private VacancyObj vacancyObj;
	private Label lblRecTotalApplicantApply;
	private Label lblRecVacancyCode;
	private Label lblRecPosition;
	private Label lblRecCountry;
	private Label lblRecDepartment;
	private Label lblRecNoOfPosition;
	private Label lblRecPriority;
	private Label lblRecRespon;
	private Label lblRecRequire;
	private Label lblRecCreatedBy;
	private Label lblRecDateCreated;
	private Label lblRecLastDateModified;
	private Label lblRecLastUpdatedBy;
	private Label lblRecStartDate;
	private Label lblRecEndDate;
	
	public void init(){
		initForm();
	}
	
	public void initForm(){
		setMethod("POST");
		setColumns(2);
		setAlign("left");
		Application app = Application.getInstance();
		
		Label lbltotalApplicantApply = new Label("lbltotalApplicantApply",  "<span class=\"fieldTitle\">" + 
				app.getMessage("recruit.general.label.totalApplicantApply") + "</span>");
		lbltotalApplicantApply.setAlign("right");
		lblRecTotalApplicantApply = new Label("lblRecTotalApplicantApply","");
		addChild(lbltotalApplicantApply);
		addChild(lblRecTotalApplicantApply);
		
		Label lblVacancyCode = new Label("lblVacancyCode", "<span class=\"fieldTitle\">" + 
				app.getMessage("recruit.general.label.vacancyCode") + "</span>");
		lblVacancyCode.setAlign("right");
		lblRecVacancyCode = new Label("lblRecVacancyCode","");
		addChild(lblVacancyCode);
		addChild(lblRecVacancyCode);
		
		Label lblPosition = new Label("lblPosition", "<span class=\"fieldTitle\">" + 
				app.getMessage("recruit.general.label.position") + "</span>");
		lblPosition.setAlign("right");
		lblRecPosition = new Label("lblRecPosition","");
		addChild(lblPosition);
		addChild(lblRecPosition);
		
		Label lblCountry = new Label("lblCountry", "<span class=\"fieldTitle\">" + 
				app.getMessage("recruit.general.label.country") + "</span>");
		lblCountry.setAlign("right");
		lblRecCountry = new Label("lblRecCountry","");
		addChild(lblCountry);
		addChild(lblRecCountry);
		
		Label lblDepartment = new Label("lblDepartment", "<span class=\"fieldTitle\">" + 
				app.getMessage("recruit.general.label.department") + "</span>");
		lblDepartment.setAlign("right");
		lblRecDepartment = new Label("lblRecDepartment","");
		addChild(lblDepartment);
		addChild(lblRecDepartment);
		
		Label lblNoOfPosition = new Label("lblNoOfPosition", "<span class=\"fieldTitle\">" + 
				app.getMessage("recruit.general.label.noOfPosition") + "</span>");
		lblNoOfPosition.setAlign("right");
		lblRecNoOfPosition = new Label("lblRecNoOfPosition","");
		addChild(lblNoOfPosition);
		addChild(lblRecNoOfPosition);
		
		Label lblPriority = new Label("lblPriority", "<span class=\"fieldTitle\">" + 
				app.getMessage("recruit.general.label.priorty") + "</span>");
		lblPriority.setAlign("right");
		lblRecPriority = new Label("lblRecPriority","");
		addChild(lblPriority);
		addChild(lblRecPriority);
		
		Label lblRespon = new Label("lblRespon", "<span class=\"fieldTitle\">" + 
				app.getMessage("recruit.general.label.tbJobRespon") + "</span>");
		lblRespon.setAlign("right");
		lblRecRespon = new Label("lblRecRespon",""); 
		addChild(lblRespon);
		addChild(lblRecRespon);

		Label lblRequire= new Label("lblRequire", "<span class=\"fieldTitle\">" + 
				app.getMessage("recruit.general.label.tbJobRequire") + "</span>");
		lblRequire.setAlign("right");
		lblRecRequire = new Label("lblRecRequire","");
		addChild(lblRequire);
		addChild(lblRecRequire);
		
		Label lblCreatedBy= new Label("lblCreatedBy", "<span class=\"fieldTitle\">" + 
				app.getMessage("recruit.general.label.createdBy") + "</span>");
		lblCreatedBy.setAlign("right");
		lblRecCreatedBy= new Label("lblRecCreatedBy","");
		addChild(lblCreatedBy);
		addChild(lblRecCreatedBy);
		
		Label lblDateCreated= new Label("lblDateCreated", "<span class=\"fieldTitle\">" + 
				app.getMessage("recruit.general.label.dateCreated") + "</span>");
		lblDateCreated.setAlign("right");
		lblRecDateCreated= new Label("lblRecDateCreated","");
		addChild(lblDateCreated);
		addChild(lblRecDateCreated);
		
		Label lblLastUpdatedBy= new Label("lblLastUpdatedBy", "<span class=\"fieldTitle\">" + 
				app.getMessage("recruit.general.label.lastUpdatedBy") + "</span>");
		lblLastUpdatedBy.setAlign("right");
		lblRecLastUpdatedBy= new Label("lblRecLastUpdatedBy","");
		addChild(lblLastUpdatedBy);
		addChild(lblRecLastUpdatedBy);
		
		Label lblLastDateModified= new Label("lblLastDateModified", "<span class=\"fieldTitle\">" + 
				app.getMessage("recruit.general.label.lastDateModified") + "</span>");
		lblLastDateModified.setAlign("right");
		lblRecLastDateModified= new Label("lblRecLastDateModified","");
		addChild(lblLastDateModified);
		addChild(lblRecLastDateModified);
		
		Label lblStartDate= new Label("lblStartDate", "<span class=\"fieldTitle\">" + 
				app.getMessage("recruit.general.label.vacancyStartDate") + "</span>");
		lblStartDate.setAlign("right");
		lblRecStartDate= new Label("lblRecStartDate","");
		addChild(lblStartDate);
		addChild(lblRecStartDate);
		
		Label lblEndDate= new Label("lblEndDate", "<span class=\"fieldTitle\">" + 
				app.getMessage("recruit.general.label.vacancyEndDate") + "</span>");
		lblEndDate.setAlign("right");
		lblRecEndDate= new Label("lblRecEndDate","");
		addChild(lblEndDate);
		addChild(lblRecEndDate);
	}
		
	public void onRequest(Event evt) {
		populateData();
	}
	
	public void populateData(){
		if(getVacancyCode()!=null){
			Application app = Application.getInstance();
			RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
			DateFormat dmyDateFmt = new SimpleDateFormat(app.getProperty("globalDatetimeLong"));
			try{
			vacancyObj = rm.loadVacancy(getVacancyCode(), TYPE_USAGE);
			lblRecTotalApplicantApply.setText(String.valueOf(vacancyObj.getTotalApplied()));
			lblRecVacancyCode.setText(vacancyObj.getVacancyCode());
			lblRecPosition.setText(vacancyObj.getPositionId());
			lblRecCountry.setText(vacancyObj.getCountryId());
			lblRecDepartment.setText(vacancyObj.getDepartmentId());
			lblRecNoOfPosition.setText(vacancyObj.getNoOfPositionOffered() + "/" + vacancyObj.getNoOfPosition());
			lblRecPriority.setText(vacancyObj.getPriorityName());
		
			lblRecRespon.setText("<a href='#' onClick=\"toggle_visibility('respon');\">Click To View Job Responsibilities</a>" +
					"<div id='respon' style='display:none;'>"+vacancyObj.getResponsibilities()+"</div>");
			lblRecRequire.setText("<a href='#' onClick=\"toggle_visibility('require');\">Click To View Job Requirements</a>" +
					"<div id='require' style='display:none;'>"+vacancyObj.getRequirements()+"</div>");
			
			lblRecCreatedBy.setText(vacancyObj.getCreatedBy());
			lblRecLastUpdatedBy.setText(vacancyObj.getLastUpdatedBy());
			lblRecDateCreated.setText(dmyDateFmt.format(vacancyObj.getCreatedDate()));
			lblRecLastDateModified.setText(dmyDateFmt.format(vacancyObj.getLastUpdatedDate()));
			
			//lblRecDateCreated.setText(vacancyObj.getCreatedDate().toString()));
			//lblRecLastDateModified.setText(vacancyObj.getLastUpdatedDate().toString());
			
			lblRecStartDate.setText(vacancyObj.getStartDate().toString());
			lblRecEndDate.setText(vacancyObj.getEndDate().toString());
			}catch(DataObjectNotFoundException e){
				Log.getLog(getClass()).error("Module " + getVacancyCode() + " not found");
				init();
			}
		}
	}
	
	//getter setter
	public String getVacancyCode() {
		return vacancyCode;
	}

	public void setVacancyCode(String vacancyCode) {
		this.vacancyCode = vacancyCode;
	}

	public VacancyObj getVacancyObj() {
		return vacancyObj;
	}

	public void setVacancyObj(VacancyObj vacancyObj) {
		this.vacancyObj = vacancyObj;
	}
}
