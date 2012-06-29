package com.tms.hr.recruit.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;

import kacang.Application;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.ui.Event;

import com.tms.hr.recruit.model.RecruitAppModule;
import com.tms.hr.recruit.model.RecruitModule;
import com.tms.hr.recruit.model.VacancyObj;

public class VacancyDetail extends Form{
	private String vacancyCode;
	private VacancyObj vacancyObj=new VacancyObj(); 
	
	//vacancy detail
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
	
	//Current applicant total***
	private Label lblRecNew;
	private Label lblRecKiv;
	private Label lblRecShortListed;
	private Label lblRecShortListedAndMailed;
	private Label lblRecScheduled;
	private Label lblRecOffered;
	private Label lblRecInterviewUnSuccessful;
	private Label lblRecAnotherInterview;
	private Label lblRecAnotherInterviewAndMailed;
	private Label lblRecJobAccepted;
	private Label lblRecJobRejected;
	private Label lblRecRejectedApplicant;
	private Label lblRecBlacklisted;
	
	public void init(){
		initForm();
	}
	
	public void initForm(){
		setMethod("POST");
		setColumns(2);
		Application app = Application.getInstance();
		//vacancy detail
		Label lblVacancyCode = new Label("lblVacancyCode", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.vacancyCode") + "</span>");
		lblVacancyCode.setAlign("right");
		lblRecVacancyCode = new Label("lblRecVacancyCode","");
		addChild(lblVacancyCode);
		addChild(lblRecVacancyCode);
		
		Label lblPosition = new Label("lblPosition", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.position") + "</span>");
		lblPosition.setAlign("right");
		lblRecPosition = new Label("lblRecPosition","");
		addChild(lblPosition);
		addChild(lblRecPosition);
		
		Label lblCountry = new Label("lblCountry", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.country") + "</span>");
		lblCountry.setAlign("right");
		lblRecCountry = new Label("lblRecCountry","");
		addChild(lblCountry);
		addChild(lblRecCountry);
		
		Label lblDepartment = new Label("lblDepartment", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.department") + "</span>");
		lblDepartment.setAlign("right");
		lblRecDepartment = new Label("lblRecDepartment","");
		addChild(lblDepartment);
		addChild(lblRecDepartment);
		
		Label lblNoOfPosition = new Label("lblNoOfPosition", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.noOfPosition") + "</span>");
		lblNoOfPosition.setAlign("right");
		lblRecNoOfPosition = new Label("lblRecNoOfPosition","");
		addChild(lblNoOfPosition);
		addChild(lblRecNoOfPosition);
		
		Label lblPriority = new Label("lblPriority", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.priorty") + "</span>");
		lblPriority.setAlign("right");
		lblRecPriority = new Label("lblRecPriority","");
		addChild(lblPriority);
		addChild(lblRecPriority);
		
		Label lblRespon = new Label("lblRespon", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.tbJobRespon") + "</span>");
		lblRespon.setAlign("right");
		lblRecRespon = new Label("lblRecRespon",""); 
		addChild(lblRespon);
		addChild(lblRecRespon);

		Label lblRequire= new Label("lblRequire", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.tbJobRequire") + "</span>");
		lblRequire.setAlign("right");
		lblRecRequire = new Label("lblRecRequire","");
		addChild(lblRequire);
		addChild(lblRecRequire);
		
		Label lblCreatedBy= new Label("lblCreatedBy", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.createdBy") + "</span>");
		lblCreatedBy.setAlign("right");
		lblRecCreatedBy= new Label("lblRecCreatedBy","");
		addChild(lblCreatedBy);
		addChild(lblRecCreatedBy);
		
		Label lblDateCreated= new Label("lblDateCreated", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.dateCreated") + "</span>");
		lblDateCreated.setAlign("right");
		lblRecDateCreated= new Label("lblRecDateCreated","");
		addChild(lblDateCreated);
		addChild(lblRecDateCreated);
		
		Label lblLastUpdatedBy= new Label("lblLastUpdatedBy", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.lastUpdatedBy") + "</span>");
		lblLastUpdatedBy.setAlign("right");
		lblRecLastUpdatedBy= new Label("lblRecLastUpdatedBy","");
		addChild(lblLastUpdatedBy);
		addChild(lblRecLastUpdatedBy);
		
		Label lblLastDateModified= new Label("lblLastDateModified", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.lastDateModified") + "</span>");
		lblLastDateModified.setAlign("right");
		lblRecLastDateModified= new Label("lblRecLastDateModified","");
		addChild(lblLastDateModified);
		addChild(lblRecLastDateModified);
		
		Label lblStartDate= new Label("lblStartDate", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.vacancyStartDate") + "</span>");
		lblStartDate.setAlign("right");
		lblRecStartDate= new Label("lblRecStartDate","");
		addChild(lblStartDate);
		addChild(lblRecStartDate);
		
		Label lblEndDate= new Label("lblEndDate", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.vacancyEndDate") + "</span>");
		lblEndDate.setAlign("right");
		lblRecEndDate= new Label("lblRecEndDate","");
		addChild(lblEndDate);
		addChild(lblRecEndDate);
		
		//total
		Label lblNew = new Label("lblNew",  "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.new") + "</span>");
		lblNew.setAlign("right");
		addChild(lblNew);
		lblRecNew = new Label("lblRecNew","");
		addChild(lblRecNew);
		
		Label lblKiv = new Label("lblKiv",  "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.kiv") + "</span>");
		lblKiv.setAlign("right");
		addChild(lblKiv);
		lblRecKiv = new Label("lblRecKiv","");
		addChild(lblRecKiv);
		
		Label lblShortListed = new Label("lblShortListed",  "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.short-listed") + "</span>");
		lblShortListed.setAlign("right");
		addChild(lblShortListed);
		lblRecShortListed = new Label("lblRecShortListed","");
		addChild(lblRecShortListed);
		
		Label lblShortListedAndMailed = new Label("lblShortListedAndMailed",  "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.shortlistedAndEmailSend") + "</span>");
		lblShortListedAndMailed.setAlign("right");
		addChild(lblShortListedAndMailed);
		lblRecShortListedAndMailed = new Label("lblRecShortListedAndMailed","");
		addChild(lblRecShortListedAndMailed);
		
		Label lblScheduled = new Label("lblScheduled",  "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.scheduled") + "</span>");
		lblScheduled.setAlign("right");
		addChild(lblScheduled);
		lblRecScheduled = new Label("lblRecScheduled","");
		addChild(lblRecScheduled);
		
		Label lblOffered = new Label("lblOffered",  "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.offered") + "</span>");
		lblOffered.setAlign("right");
		addChild(lblOffered);
		lblRecOffered = new Label("lblRecOffered","");
		addChild(lblRecOffered);
		
		Label lblInterviewUnSuccessful = new Label("lblInterviewUnSuccessful",  "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.interviewUnsuccessful") + "</span>");
		lblInterviewUnSuccessful.setAlign("right");
		addChild(lblInterviewUnSuccessful);
		lblRecInterviewUnSuccessful = new Label("lblRecInterviewUnSuccessful","");
		addChild(lblRecInterviewUnSuccessful);
		
		Label lblAnotherInterview = new Label("lblAnotherInterview",  "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.anotherInterview") + "</span>");
		lblAnotherInterview.setAlign("right");
		addChild(lblAnotherInterview);
		lblRecAnotherInterview = new Label("lblRecAnotherInterview","");
		addChild(lblRecAnotherInterview);
		
		Label lblAnotherInterviewAndMailed = new Label("lblAnotherInterviewAndMailed",  "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.anotherInterviewAndEmailSend") + "</span>");
		lblAnotherInterviewAndMailed.setAlign("right");
		addChild(lblAnotherInterviewAndMailed);
		lblRecAnotherInterviewAndMailed = new Label("lblRecAnotherInterviewAndMailed","");
		addChild(lblRecAnotherInterviewAndMailed);
		
		Label lblJobAccepted = new Label("lblJobAccepted",  "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.jobAccepted") + "</span>");
		lblJobAccepted.setAlign("right");
		addChild(lblJobAccepted);
		lblRecJobAccepted = new Label("lblRecJobAccepted","");
		addChild(lblRecJobAccepted);
		
		Label lblJobRejected = new Label("lblJobRejected",  "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.jobRejected") + "</span>");
		lblJobRejected.setAlign("right");
		addChild(lblJobRejected);
		lblRecJobRejected = new Label("lblRecJobRejected","");
		addChild(lblRecJobRejected);
		
		Label lblRejectedApplicant = new Label("lblRejectedApplicant",  "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.rejectedApplicant") + "</span>");
		lblRejectedApplicant.setAlign("right");
		addChild(lblRejectedApplicant);
		lblRecRejectedApplicant = new Label("lblRecRejectedApplicant","");
		addChild(lblRecRejectedApplicant);
		
		Label lblBlacklisted = new Label("lblBlacklisted",  "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.black-listed") + "</span>");
		lblBlacklisted.setAlign("right");
		addChild(lblBlacklisted);
		lblRecBlacklisted = new Label("lblRecBlacklisted","");
		addChild(lblRecBlacklisted);
	}
	
	public void onRequest(Event evt) {
		populateData(evt);
	}
	
	public void populateData(Event evt){
		if(getVacancyCode()!=null){
			Application app = Application.getInstance();
			RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
			RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			Collection vacancyTotalCol = rm.findAllVacancyTotal(null, false, 0, 1, "", "", "", "", "", "", getVacancyCode());
			HashMap map= (HashMap)vacancyTotalCol.iterator().next();
			
			vacancyObj.setVacancyCode(getVacancyCode());
			vacancyObj.setResponsibilities(map.get("responsibilities").toString());
			vacancyObj.setRequirements(map.get("requirements").toString());
			DateFormat dmyDateFmt = new SimpleDateFormat(app.getProperty("globalDatetimeLong"));
			//vacancy detail
			lblRecVacancyCode.setText(getVacancyCode());
			lblRecPosition.setText(map.get("positionDesc").toString());
			lblRecCountry.setText(map.get("countryDesc").toString());
			lblRecDepartment.setText(map.get("departmentDesc").toString());
			if(map.get("noOfPositionOffered")!=null && !map.get("noOfPositionOffered").equals(""))
				lblRecNoOfPosition.setText(map.get("noOfPositionOffered").toString() +"/"+ map.get("noOfPosition").toString());
			else
				lblRecNoOfPosition.setText("0/"+ map.get("noOfPosition").toString());
			
			lblRecPriority.setText(map.get("priorityName").toString());
			
			lblRecRespon.setText("<a href='#' onClick=\"NewWindow('vacancyPopUp.jsp?type=1','Job_view','500','500','yes'); return false; \">View Job Responsibilities</a>");
			lblRecRequire.setText("<a href='#' onClick=\"NewWindow('vacancyPopUp.jsp?type=2','Job_view','500','500','yes'); return false; \">View Job Requirements</a>");
			
			lblRecCreatedBy.setText(map.get("createdBy").toString());
			
			lblRecDateCreated.setText(dmyDateFmt.format(map.get("createdDate")));
			lblRecLastDateModified.setText(dmyDateFmt.format(map.get("lastUpdatedDate")));
		
			lblRecLastUpdatedBy.setText(map.get("lastUpdatedBy").toString());
			lblRecStartDate.setText(map.get("startDate").toString());
			lblRecEndDate.setText(map.get("endDate").toString());
			
			//total**
			lblRecNew.setText(String.valueOf(ram.countByApplicantStatus(getVacancyCode(), app.getMessage("recruit.general.label.new"))));
			lblRecKiv.setText(String.valueOf(ram.countByApplicantStatus(getVacancyCode(), app.getMessage("recruit.general.label.kiv"))));
			lblRecShortListed.setText(String.valueOf(ram.countByApplicantStatus(getVacancyCode(), app.getMessage("recruit.general.label.short-listed"))));
			lblRecShortListedAndMailed.setText(String.valueOf(ram.countByApplicantStatus(getVacancyCode(), app.getMessage("recruit.general.label.shortlistedAndEmailSend"))));
			lblRecScheduled.setText(String.valueOf(ram.countByApplicantStatus(getVacancyCode(), app.getMessage("recruit.general.label.scheduled"))));
			lblRecOffered.setText(String.valueOf(ram.countByApplicantStatus(getVacancyCode(), app.getMessage("recruit.general.label.offered"))));
			lblRecInterviewUnSuccessful.setText(String.valueOf(ram.countByApplicantStatus(getVacancyCode(), app.getMessage("recruit.general.label.interviewUnsuccessful"))));
			lblRecAnotherInterview.setText(String.valueOf(ram.countByApplicantStatus(getVacancyCode(), app.getMessage("recruit.general.label.anotherInterview"))));
		    lblRecAnotherInterviewAndMailed.setText(String.valueOf(ram.countByApplicantStatus(getVacancyCode(), app.getMessage("recruit.general.label.anotherInterviewAndEmailSend"))));
			lblRecJobAccepted.setText(String.valueOf(ram.countByApplicantStatus(getVacancyCode(), app.getMessage("recruit.general.label.jobAccepted"))));
			lblRecJobRejected.setText(String.valueOf(ram.countByApplicantStatus(getVacancyCode(), app.getMessage("recruit.general.label.jobRejected"))));
			lblRecRejectedApplicant.setText(String.valueOf(ram.countByApplicantStatus(getVacancyCode(), app.getMessage("recruit.general.label.rejectedApplicant"))));
			lblRecBlacklisted.setText(String.valueOf(ram.countByApplicantStatus(getVacancyCode(), app.getMessage("recruit.general.label.black-listed"))));
		}
	}
	
	public String getDefaultTemplate() {
		return "recruit/vacancyDetail";
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
	
	//28-12-06 added
	public Label getLblRecCountry() {
		return lblRecCountry;
	}

	public void setLblRecCountry(Label lblRecCountry) {
		this.lblRecCountry = lblRecCountry;
	}

	public Label getLblRecCreatedBy() {
		return lblRecCreatedBy;
	}

	public void setLblRecCreatedBy(Label lblRecCreatedBy) {
		this.lblRecCreatedBy = lblRecCreatedBy;
	}

	public Label getLblRecDateCreated() {
		return lblRecDateCreated;
	}

	public void setLblRecDateCreated(Label lblRecDateCreated) {
		this.lblRecDateCreated = lblRecDateCreated;
	}

	public Label getLblRecDepartment() {
		return lblRecDepartment;
	}

	public void setLblRecDepartment(Label lblRecDepartment) {
		this.lblRecDepartment = lblRecDepartment;
	}

	public Label getLblRecEndDate() {
		return lblRecEndDate;
	}

	public void setLblRecEndDate(Label lblRecEndDate) {
		this.lblRecEndDate = lblRecEndDate;
	}

	public Label getLblRecLastDateModified() {
		return lblRecLastDateModified;
	}

	public void setLblRecLastDateModified(Label lblRecLastDateModified) {
		this.lblRecLastDateModified = lblRecLastDateModified;
	}

	public Label getLblRecLastUpdatedBy() {
		return lblRecLastUpdatedBy;
	}

	public void setLblRecLastUpdatedBy(Label lblRecLastUpdatedBy) {
		this.lblRecLastUpdatedBy = lblRecLastUpdatedBy;
	}

	public Label getLblRecNoOfPosition() {
		return lblRecNoOfPosition;
	}

	public void setLblRecNoOfPosition(Label lblRecNoOfPosition) {
		this.lblRecNoOfPosition = lblRecNoOfPosition;
	}

	public Label getLblRecPosition() {
		return lblRecPosition;
	}

	public void setLblRecPosition(Label lblRecPosition) {
		this.lblRecPosition = lblRecPosition;
	}

	public Label getLblRecPriority() {
		return lblRecPriority;
	}

	public void setLblRecPriority(Label lblRecPriority) {
		this.lblRecPriority = lblRecPriority;
	}

	public Label getLblRecRequire() {
		return lblRecRequire;
	}

	public void setLblRecRequire(Label lblRecRequire) {
		this.lblRecRequire = lblRecRequire;
	}

	public Label getLblRecRespon() {
		return lblRecRespon;
	}

	public void setLblRecRespon(Label lblRecRespon) {
		this.lblRecRespon = lblRecRespon;
	}

	public Label getLblRecStartDate() {
		return lblRecStartDate;
	}

	public void setLblRecStartDate(Label lblRecStartDate) {
		this.lblRecStartDate = lblRecStartDate;
	}

	public Label getLblRecVacancyCode() {
		return lblRecVacancyCode;
	}

	public void setLblRecVacancyCode(Label lblRecVacancyCode) {
		this.lblRecVacancyCode = lblRecVacancyCode;
	}

	public Label getLblRecAnotherInterview() {
		return lblRecAnotherInterview;
	}

	public void setLblRecAnotherInterview(Label lblRecAnotherInterview) {
		this.lblRecAnotherInterview = lblRecAnotherInterview;
	}

	public Label getLblRecAnotherInterviewAndMailed() {
		return lblRecAnotherInterviewAndMailed;
	}

	public void setLblRecAnotherInterviewAndMailed(
			Label lblRecAnotherInterviewAndMailed) {
		this.lblRecAnotherInterviewAndMailed = lblRecAnotherInterviewAndMailed;
	}

	public Label getLblRecBlacklisted() {
		return lblRecBlacklisted;
	}

	public void setLblRecBlacklisted(Label lblRecBlacklisted) {
		this.lblRecBlacklisted = lblRecBlacklisted;
	}

	public Label getLblRecInterviewUnSuccessful() {
		return lblRecInterviewUnSuccessful;
	}

	public void setLblRecInterviewUnSuccessful(Label lblRecInterviewUnSuccessful) {
		this.lblRecInterviewUnSuccessful = lblRecInterviewUnSuccessful;
	}

	public Label getLblRecJobAccepted() {
		return lblRecJobAccepted;
	}

	public void setLblRecJobAccepted(Label lblRecJobAccepted) {
		this.lblRecJobAccepted = lblRecJobAccepted;
	}

	public Label getLblRecJobRejected() {
		return lblRecJobRejected;
	}

	public void setLblRecJobRejected(Label lblRecJobRejected) {
		this.lblRecJobRejected = lblRecJobRejected;
	}

	public Label getLblRecKiv() {
		return lblRecKiv;
	}

	public void setLblRecKiv(Label lblRecKiv) {
		this.lblRecKiv = lblRecKiv;
	}

	public Label getLblRecNew() {
		return lblRecNew;
	}

	public void setLblRecNew(Label lblRecNew) {
		this.lblRecNew = lblRecNew;
	}

	public Label getLblRecOffered() {
		return lblRecOffered;
	}

	public void setLblRecOffered(Label lblRecOffered) {
		this.lblRecOffered = lblRecOffered;
	}

	public Label getLblRecRejectedApplicant() {
		return lblRecRejectedApplicant;
	}

	public void setLblRecRejectedApplicant(Label lblRecRejectedApplicant) {
		this.lblRecRejectedApplicant = lblRecRejectedApplicant;
	}

	public Label getLblRecScheduled() {
		return lblRecScheduled;
	}

	public void setLblRecScheduled(Label lblRecScheduled) {
		this.lblRecScheduled = lblRecScheduled;
	}

	public Label getLblRecShortListed() {
		return lblRecShortListed;
	}

	public void setLblRecShortListed(Label lblRecShortListed) {
		this.lblRecShortListed = lblRecShortListed;
	}

	public Label getLblRecShortListedAndMailed() {
		return lblRecShortListedAndMailed;
	}

	public void setLblRecShortListedAndMailed(Label lblRecShortListedAndMailed) {
		this.lblRecShortListedAndMailed = lblRecShortListedAndMailed;
	}	
	
	
}

	