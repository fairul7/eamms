package com.tms.hr.recruit.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;

import kacang.Application;
import kacang.stdui.CountrySelectBox;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.ui.Event;

import com.tms.hr.recruit.model.ApplicantObj;
import com.tms.hr.recruit.model.RecruitAppModule;

public class JobAppPersonalView extends Form{
	
	private ApplicantObj applicantObj;
	private String applicantId;
	private Label lblrDateApplied;
	private Label lblrPosApplied;
	private Label lblRecName;
	private Label lblRecAge;
	private Label lblRecDob;
	private Label lblRecNirc;
	private Label lblRecGender;
	private Label lblRecMaritalStatus;
	private Label lblRecNoOfChild;
	private Label lblRecNationality;
	private Label lblRecEmail;
	private Label lblRecTelNo;
	private Label lblRecMobileNo;
	private Label lblRecAddr;
	private Label lblRecPostalCode;
	private Label lblRecCity;
	private Label lblRecState;
	private Label lblRecCountry;

	public void onRequest(Event evt) {
		populateData();
		evt.getRequest().getSession().setAttribute("applicantId", getApplicantId());
	}
	
	public void init(){
		initForm();
	}
	
	public void initForm(){
		setMethod("POST");
		setColumns(2);
		Application app = Application.getInstance();
		
		Label lblDateApplied= new Label("lblDateCreated", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.dateApplied") + "</span>");
		lblDateApplied.setAlign("right");
    	lblrDateApplied = new Label("lblrDateApplied","");
    	addChild(lblDateApplied);
    	addChild(lblrDateApplied);
		
		Label lblPosApplied= new Label("lblPosApplied", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.posApplied") + "</span>");
    	lblPosApplied.setAlign("right");
    	lblrPosApplied = new Label("lblrPosApplied","");
    	addChild(lblPosApplied);
    	addChild(lblrPosApplied);
    	
    	Label lblPersonalDetails= new Label("lblPersonalDetails", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.personalDetails") + "</span>");
    	lblPersonalDetails.setAlign("right");
    	Label lblSpace = new Label("lblSpace","");
    	addChild(lblPersonalDetails);
    	addChild(lblSpace);
    	
    	Label lblName= new Label("lblName", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.name") + "</span>");
    	lblName.setAlign("right");
    	lblRecName = new Label("lblRecName","");
    	addChild(lblName);
    	addChild(lblRecName);
    	
    	Label lblAge= new Label("lblAge", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.age") + "</span>");
    	lblAge.setAlign("right");
    	lblRecAge = new Label("lblRecAge","");
    	addChild(lblAge);
    	addChild(lblRecAge);
    	
    	Label lblDob= new Label("lblDob", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.dob") + "</span>");
    	lblDob.setAlign("right");
    	lblRecDob = new Label("lblRecDob","");
    	addChild(lblDob);
    	addChild(lblRecDob);
    	
    	Label lblNirc= new Label("lblNirc", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.nirc") + "</span>");
    	lblNirc.setAlign("right");
    	lblRecNirc = new Label("lblRecNirc","");
    	addChild(lblNirc);
    	addChild(lblRecNirc);
    	
    	Label lblGender= new Label("lblGender", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.gender") + "</span>");
    	lblGender.setAlign("right");
    	lblRecGender = new Label("lblRecGender","");
    	addChild(lblGender);
    	addChild(lblRecGender);
    	
    	Label lblMarital= new Label("lblMarital", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.marital") + "</span>");
    	lblMarital.setAlign("right");
    	lblRecMaritalStatus = new Label("lblRecMaritalStatus","");
    	addChild(lblMarital);
    	addChild(lblRecMaritalStatus);
    	
    	Label lblNoOfChild= new Label("lblNoOfChild", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.noOfChild") + "</span>");
    	lblNoOfChild.setAlign("right");
    	lblRecNoOfChild = new Label("lblRecNoOfChild","");
    	addChild(lblNoOfChild);
    	addChild(lblRecNoOfChild);
    	
    	Label lblNationality= new Label("lblNationality", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.nationality") + "</span>");
    	lblNationality.setAlign("right");
    	lblRecNationality = new Label("lblRecNationality","");
    	addChild(lblNationality);
    	addChild(lblRecNationality);
    	
    	Label lblContactInfo= new Label("lblContactInfo", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.contactInfo") + "</span>");
    	lblContactInfo.setAlign("right");
    	Label lblSpace2 = new Label("lblSpace2","");
    	addChild(lblContactInfo);
    	addChild(lblSpace2);
    	
    	Label lblEmailAddr= new Label("lblEmailAddr", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.emailAddr") + "</span>");
    	lblEmailAddr.setAlign("right");
    	lblRecEmail = new Label("lblRecEmail","");
    	addChild(lblEmailAddr);
    	addChild(lblRecEmail);
    	
    	Label lblTelNo= new Label("lblTelNo", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.telNo") + "</span>");
    	lblTelNo.setAlign("right");
    	lblRecTelNo = new Label("lblRecTelNo","");
    	addChild(lblTelNo);
    	addChild(lblRecTelNo);
    	
    	Label lblMobileNo= new Label("lblMobileNo", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.mobileNo") + "</span>");
    	lblMobileNo.setAlign("right");
    	lblRecMobileNo = new Label("lblRecMobileNo","");
    	addChild(lblMobileNo);
    	addChild(lblRecMobileNo);
    	
    	Label lblAddr= new Label("lblAddr", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.addr") + "</span>");
    	lblAddr.setAlign("right");
    	lblRecAddr = new Label("lblRecAddr","");
    	addChild(lblAddr);
    	addChild(lblRecAddr);
    	
    	Label lblPostalCode= new Label("lblPostalCode", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.postalCode") + "</span>");
    	lblPostalCode.setAlign("right");
    	lblRecPostalCode = new Label("lblRecPostalCode","");
    	addChild(lblPostalCode);
    	addChild(lblRecPostalCode);
    	
    	Label lblCity= new Label("lblCity", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.city") + "</span>");
    	lblCity.setAlign("right");
    	lblRecCity = new Label("lblRecCity","");
    	addChild(lblCity);
    	addChild(lblRecCity);
    	
      	Label lblState= new Label("lblState", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.state") + "</span>");
    	lblState.setAlign("right");
    	lblRecState = new Label("lblRecState","");
    	addChild(lblState);
    	addChild(lblRecState);
    	
    	Label lblCountry= new Label("lblCountry", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.country") + "</span>");
    	lblCountry.setAlign("right");
    	lblRecCountry = new Label("lblRecCountry","");
    	addChild(lblCountry);
    	addChild(lblRecCountry);
    	
	}
	
	public void populateData(){
		if(getApplicantId()!=null){
			Application app = Application.getInstance();
			RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			CountrySelectBox sbCountry = new CountrySelectBox();
			
		
				Collection col=ram.loadApplicantPersonal(getApplicantId());
				HashMap map = (HashMap) col.iterator().next();
				
				//lblrDateApplied.setText(map.get("createdDate").toString());				
				DateFormat dmyDateFmt = new SimpleDateFormat(app.getProperty("globalDatetimeLong"));
				lblrDateApplied.setText(dmyDateFmt.format(map.get("createdDate")));
				
				lblrPosApplied.setText(map.get("positionDesc").toString());
				lblRecName.setText(map.get("name").toString());
				lblRecAge.setText(map.get("age").toString());
				lblRecDob.setText(map.get("dob").toString());
				lblRecNirc.setText(map.get("nirc").toString());
				
				if(map.get("gender").equals("1"))
					lblRecGender.setText("Male");
				else
					lblRecGender.setText("Female");
				
				lblRecMaritalStatus.setText(map.get("maritalStatus").toString());
				lblRecNoOfChild.setText(map.get("noOfChild").toString());
				
				lblRecNationality.setText(sbCountry.getOptionMap().get(map.get("nationality")).toString());
				
				lblRecEmail.setText(map.get("email").toString());
				lblRecTelNo.setText(map.get("telephoneNo").toString());
				lblRecMobileNo.setText(map.get("mobileNo").toString());
				lblRecAddr.setText(map.get("address").toString());
				lblRecPostalCode.setText(map.get("postalCode").toString());
				lblRecCity.setText(map.get("city").toString());
				lblRecState.setText(map.get("state").toString());
				
				lblRecCountry.setText(sbCountry.getOptionMap().get(map.get("country")).toString());
		}
	}
	
	//getter setter
	public String getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}

	public ApplicantObj getApplicantObj() {
		return applicantObj;
	}

	public void setApplicantObj(ApplicantObj applicantObj) {
		this.applicantObj = applicantObj;
	}
	
}

























