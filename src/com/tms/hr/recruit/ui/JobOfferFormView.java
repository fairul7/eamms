package com.tms.hr.recruit.ui;

import java.util.Collection;
import java.util.HashMap;

import com.tms.hr.recruit.model.ApplicantObj;
import com.tms.hr.recruit.model.RecruitAppModule;
import com.tms.hr.recruit.model.VacancyObj;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.ButtonGroup;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.Radio;
import kacang.stdui.TextBox;
import kacang.ui.Event;
import kacang.ui.Forward;

public class JobOfferFormView extends Form{
	private String applicantId;
	
	private Label lblRecDateOffered;
	private Label lblRecApplicant;
	private Label lblRecVacancyCode;
	private Label lblRecPostion;
	private Label lblRecCountry;
	private Label lblRecDepartment;
	private Label lblRecOfferLetterSent;
	private Label lblRecOfferLetterStatus;
	private Label lblRecRemark;
	
	public void init(){
		initForm();
	}

	public void initForm(){
		setColumns(2);
    	setMethod("POST");
    	//removeChildren();
    	Application app = Application.getInstance();
    	
    	Label lblApplicant= new Label("lblApplicant", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.applicant") + "</span>");
    	lblApplicant.setAlign("right");
    	addChild(lblApplicant);
    	lblRecApplicant= new Label("lblRecApplicant", "");
    	addChild(lblRecApplicant);
    	
    	Label lblVacancyCode= new Label("lblVacancyCode", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.vacancyCode") + "</span>");
    	lblVacancyCode.setAlign("right");
    	addChild(lblVacancyCode);
    	lblRecVacancyCode= new Label("lblRecVacancyCode", "");
    	addChild(lblRecVacancyCode);
    	
    	Label lblDateOffered= new Label("lblDateOffered", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.dateOffered") + "</span>");
    	lblDateOffered.setAlign("right");
    	addChild(lblDateOffered);
    	lblRecDateOffered= new Label("lblRecDateOffered", "");
    	addChild(lblRecDateOffered);
    	
    	Label lblPostion= new Label("lblPostion", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.position") + "</span>");
    	lblPostion.setAlign("right");
    	addChild(lblPostion);
    	lblRecPostion= new Label("lblRecPostion", "");
    	addChild(lblRecPostion);
    	
    	Label lblCountry= new Label("lblCountry", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.country") + "</span>");
    	lblCountry.setAlign("right");
    	addChild(lblCountry);
    	lblRecCountry= new Label("lblRecCountry", "");
    	addChild(lblRecCountry);
    	
    	Label lblDepartment= new Label("lblDepartment", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.department") + "</span>");
    	lblDepartment.setAlign("right");
    	addChild(lblDepartment);
    	lblRecDepartment= new Label("lblRecDepartment", "");
    	addChild(lblRecDepartment);
    	
    	Label lblOfferLetterSent= new Label("lblOfferLetterSent", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.OfferLetterSent") + "</span>");
    	lblOfferLetterSent.setAlign("right");
    	addChild(lblOfferLetterSent);
    	lblRecOfferLetterSent= new Label("lblRecOfferLetterSent", "");
    	addChild(lblRecOfferLetterSent);
    	
    	Label lblOfferLetterStatus= new Label("lblOfferLetterStatus", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.OfferLetterStatus") + "</span>");
    	lblOfferLetterStatus.setAlign("right");
    	addChild(lblOfferLetterStatus);
    	lblRecOfferLetterStatus= new Label("lblRecOfferLetterStatus", "");
    	addChild(lblRecOfferLetterStatus);
    	
    	Label lblRemark= new Label("lblRemark", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.remark") + "</span>");
    	lblRemark.setAlign("right");
    	addChild(lblRemark);
    	lblRecRemark= new Label("lblRecRemark", "");
    	addChild(lblRecRemark);
	}
	
	public void onRequest(Event evt){
		init();
		populateData();
	}
	
	public void populateData(){
		Application app = Application.getInstance();
		RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
		
		Collection col = ram.lookUpApplicantOfferedStatus(getApplicantId());
		HashMap map =(HashMap) col.iterator().next();
		
		lblRecDateOffered.setText(map.get("jobOfferDate").toString());
		
		lblRecApplicant.setText("<a href='#' onClick=\"NewWindow('jobApplicationFormViewPopUp.jsp?applicantId="+map.get("applicantId").toString()+"','applicantName','500','670','yes'); return false; \">"+map.get("name").toString()+"</a>");
		lblRecVacancyCode.setText("<a href='#' onClick=\"NewWindow('vacancyDetail.jsp?vacancyCode="+map.get("vacancyCode").toString()+"','vacancyDetails','500','500','yes'); return false; \">"+map.get("vacancyCode").toString()+"</a>");
		
		//lblRecApplicant.setText("<a href='#' onClick=javascript:window.open('jobApplicationFormViewPopUp.jsp?applicantId="+map.get("applicantId").toString()+"','blank','toolbar=no,width=500,height=670,scrollbars=yes'); return false; >"+map.get("name").toString()+"</a>")	;
		//lblRecVacancyCode.setText("<a href='#' onClick=javascript:window.open('vacancyDetail.jsp?vacancyCode="+map.get("vacancyCode").toString()+"','blank','toolbar=no,width=500,height=500,scrollbars=yes'); return false; >"+map.get("vacancyCode").toString()+"</a>");
		//lblRecVacancyCode.setText(map.get("vacancyCode").toString());
		lblRecPostion.setText(map.get("positionDesc").toString());
		lblRecCountry.setText(map.get("countryDesc").toString());
		lblRecDepartment.setText(map.get("departmentDesc").toString());
	
		if(map.get("jobOfferLetterSent").equals("0")){
			lblRecOfferLetterSent.setText("No");
			lblRecOfferLetterStatus.setText("-");
		}else{
			lblRecOfferLetterSent.setText("Yes");
			if(!map.get("applicantStatus").equals("Job Accepted") || !map.get("applicantStatus").equals("Job Rejected"))
				lblRecOfferLetterStatus.setText(app.getMessage("recruit.general.label.underConsideration"));
		}
			
		if(map.get("applicantStatus").equals("Job Accepted"))
			lblRecOfferLetterStatus.setText(map.get("applicantStatus").toString());
		
		if(map.get("applicantStatus").equals("Job Rejected"))
			lblRecOfferLetterStatus.setText(map.get("applicantStatus").toString());
		
		if(map.get("jobOfferAdminRemark")!=null && !map.get("jobOfferAdminRemark").equals(""))
			lblRecRemark.setText(map.get("jobOfferAdminRemark").toString());
		else
			lblRecRemark.setText("-");
	}
	
	public Forward onValidate(Event evt) {
		return new Forward("nothing");
	}
	
	//getter setter
	public String getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}
}
