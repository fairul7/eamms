package com.tms.hr.recruit.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;

import kacang.Application;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.ui.Event;

import com.tms.hr.recruit.model.RecruitModule;
import com.tms.hr.recruit.model.VacancyObj;

public class VacancyReportListView extends Form{
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
	
	//total
	private Label lblRecTotalApplied;
	private Label lblRecTotalShortlisted;
	private Label lblRecTotalScheduled;
	private Label lblRecTotalReScheduled;
	private Label lblRecTotalReScheduledRejected;
	private Label lblRecTotalInterviewUnsuccessful;
	private Label lblRecTotalJobOffered;
	private Label lblRecTotalJobAccepted;
	private Label lblRecTotalJobRejected;
	private Label lblRecTotalBlackListed;
	private Label lblRecTotalViewed;
	private Label lblRecTotalAnotherInterview;
	
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
		Label lblTotalApplied = new Label("lblTotalApplied",  "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.totalApplied") + "</span>");
		lblTotalApplied.setAlign("right");
		addChild(lblTotalApplied);
		lblRecTotalApplied = new Label("lblRecTotalApplied","");
		addChild(lblRecTotalApplied);
		
		Label lblTotalShortlisted = new Label("lblTotalShortlisted",  "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.totalShortlisted") + "</span>");
		lblTotalShortlisted.setAlign("right");
		addChild(lblTotalShortlisted);
		lblRecTotalShortlisted = new Label("lblRecTotalShortlisted","");
		addChild(lblRecTotalShortlisted);
		
		Label lblTotalScheduled = new Label("lblTotalScheduled",  "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.totalScheduled") + "</span>");
		lblTotalScheduled.setAlign("right");
		addChild(lblTotalScheduled);
		lblRecTotalScheduled = new Label("lblRecTotalScheduled","");
		addChild(lblRecTotalScheduled);
		
		Label lblTotalReScheduled = new Label("lblTotalReScheduled",  "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.totalReScheduled") + "</span>");
		lblTotalReScheduled.setAlign("right");
		addChild(lblTotalReScheduled);
		lblRecTotalReScheduled = new Label("lblRecTotalReScheduled","");
		addChild(lblRecTotalReScheduled);
		
		Label lblTotalReScheduledRejected = new Label("lblTotalReScheduledRejected",  "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.totalReScheduledRejected") + "</span>");
		lblTotalReScheduledRejected.setAlign("right");
		addChild(lblTotalReScheduledRejected);
		lblRecTotalReScheduledRejected = new Label("lblRecTotalReScheduledRejected","");
		addChild(lblRecTotalReScheduledRejected);
		
		Label lblTotalJobOffered = new Label("lblTotalJobOffered",  "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.totalJobOffered") + "</span>");
		lblTotalJobOffered.setAlign("right");
		addChild(lblTotalJobOffered);
		lblRecTotalJobOffered = new Label("lblRecTotalJobOffered","");
		addChild(lblRecTotalJobOffered);
		
		Label lblTotalInterviewUnsuccessful = new Label("lblTotalInterviewUnsuccessful",  "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.totalInterviewUnsuccessful") + "</span>");
		lblTotalInterviewUnsuccessful.setAlign("right");
		addChild(lblTotalInterviewUnsuccessful);
		lblRecTotalInterviewUnsuccessful = new Label("lblRecTotalInterviewUnsuccessful","");
		addChild(lblRecTotalInterviewUnsuccessful);
	
		Label lblTotalJobAccepted = new Label("lblTotalJobAccepted",  "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.totalJobAccepted") + "</span>");
		lblTotalJobAccepted.setAlign("right");
		addChild(lblTotalJobAccepted);
		lblRecTotalJobAccepted = new Label("lblRecTotalJobAccepted","");
		addChild(lblRecTotalJobAccepted);
		
		Label lblTotalJobRejected = new Label("lblTotalJobRejected",  "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.totalJobRejected") + "</span>");
		lblTotalJobRejected.setAlign("right");
		addChild(lblTotalJobRejected);
		lblRecTotalJobRejected = new Label("lblRecTotalJobRejected","");
		addChild(lblRecTotalJobRejected);
		
		Label lblTotalBlackListed = new Label("lblTotalBlackListed",  "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.totalBlackListed") + "</span>");
		lblTotalBlackListed.setAlign("right");
		addChild(lblTotalBlackListed);
		lblRecTotalBlackListed = new Label("lblRecTotalBlackListed","");
		addChild(lblRecTotalBlackListed);
		
		Label lblTotalViewed = new Label("lblTotalViewed",  "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.totalViewed") + "</span>");
		lblTotalViewed.setAlign("right");
		addChild(lblTotalViewed);
		lblRecTotalViewed = new Label("lblRecTotalViewed","");
		addChild(lblRecTotalViewed);
		
		Label lblTotalAnotherInterview = new Label("lblTotalAnotherInterview",  "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.totalAnotherInterview") + "</span>");
		lblTotalAnotherInterview.setAlign("right");
		addChild(lblTotalAnotherInterview);
		lblRecTotalAnotherInterview = new Label("lblRecTotalAnotherInterview","");
		addChild(lblRecTotalAnotherInterview);
	}
	
	public void onRequest(Event evt) {
		populateData();
	}
	
	public void populateData(){
		if(getVacancyCode()!=null){
			Application app = Application.getInstance();
			RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
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
			
			lblRecRespon.setText("<a href='#' onClick=\"NewWindow('vacancyPopUpDetail.jsp?type=1','Job_view','500','500','yes'); return false; \">View Job Responsibilities</a>");
			lblRecRequire.setText("<a href='#' onClick=\"NewWindow('vacancyPopUpDetail.jsp?type=2','Job_view','500','500','yes'); return false; \">View Job Requirements</a>");
		
			//lblRecRespon.setText("<a href='#' onClick=javascript:window.open('vacancyPopUpDetail.jsp?type=1','blank','toolbar=no,width=500,height=500'); return false; >View Job Responsibilities</a>");
			//lblRecRequire.setText("<a href='#' onClick=javascript:window.open('vacancyPopUpDetail.jsp?type=2','blank','toolbar=no,width=500,height=500'); return false; >View Job Requirements</a>");
			lblRecCreatedBy.setText(map.get("createdBy").toString());
			
			lblRecDateCreated.setText(dmyDateFmt.format(map.get("createdDate")));
			lblRecLastDateModified.setText(dmyDateFmt.format(map.get("lastUpdatedDate")));
			//lblRecDateCreated.setText(map.get("createdDate").toString());
			//lblRecLastDateModified.setText(map.get("lastUpdatedDate").toString());
			
			lblRecLastUpdatedBy.setText(map.get("lastUpdatedBy").toString());
			lblRecStartDate.setText(map.get("startDate").toString());
			lblRecEndDate.setText(map.get("endDate").toString());
			
			//total
			lblRecTotalApplied.setText(map.get("totalApplied").toString());
			lblRecTotalShortlisted.setText(map.get("totalShortlisted").toString());
			lblRecTotalScheduled.setText(map.get("totalScheduled").toString());
			lblRecTotalReScheduled.setText(map.get("totalReScheduled").toString());
			lblRecTotalReScheduledRejected.setText(map.get("totalReScheduledRejected").toString());
			lblRecTotalJobOffered.setText(map.get("totalJobOffered").toString());
			lblRecTotalInterviewUnsuccessful.setText(map.get("totalInterviewUnsuccessful").toString());
			lblRecTotalJobAccepted.setText(map.get("totalJobAccepted").toString());
			lblRecTotalJobRejected.setText(map.get("totalJobRejected").toString());
			lblRecTotalBlackListed.setText(map.get("totalBlackListed").toString());
			lblRecTotalViewed.setText(map.get("totalViewed").toString());
			lblRecTotalAnotherInterview.setText(map.get("totalAnotherInterview").toString());
		}
	}
	
	public String getDefaultTemplate() {
		return "recruit/vacancyReportListView";
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

	public Label getLblRecTotalApplied() {
		return lblRecTotalApplied;
	}

	public void setLblRecTotalApplied(Label lblRecTotalApplied) {
		this.lblRecTotalApplied = lblRecTotalApplied;
	}

	public Label getLblRecTotalBlackListed() {
		return lblRecTotalBlackListed;
	}

	public void setLblRecTotalBlackListed(Label lblRecTotalBlackListed) {
		this.lblRecTotalBlackListed = lblRecTotalBlackListed;
	}

	public Label getLblRecTotalJobAccepted() {
		return lblRecTotalJobAccepted;
	}

	public void setLblRecTotalJobAccepted(Label lblRecTotalJobAccepted) {
		this.lblRecTotalJobAccepted = lblRecTotalJobAccepted;
	}

	public Label getLblRecTotalJobOffered() {
		return lblRecTotalJobOffered;
	}

	public void setLblRecTotalJobOffered(Label lblRecTotalJobOffered) {
		this.lblRecTotalJobOffered = lblRecTotalJobOffered;
	}

	public Label getLblRecTotalJobRejected() {
		return lblRecTotalJobRejected;
	}

	public void setLblRecTotalJobRejected(Label lblRecTotalJobRejected) {
		this.lblRecTotalJobRejected = lblRecTotalJobRejected;
	}

	public Label getLblRecTotalInterviewUnsuccessful() {
		return lblRecTotalInterviewUnsuccessful;
	}

	public void setLblRecTotalInterviewUnsuccessful(
			Label lblRecTotalInterviewUnsuccessful) {
		this.lblRecTotalInterviewUnsuccessful = lblRecTotalInterviewUnsuccessful;
	}
	
	public Label getLblRecTotalReScheduled() {
		return lblRecTotalReScheduled;
	}

	public void setLblRecTotalReScheduled(Label lblRecTotalReScheduled) {
		this.lblRecTotalReScheduled = lblRecTotalReScheduled;
	}

	public Label getLblRecTotalReScheduledRejected() {
		return lblRecTotalReScheduledRejected;
	}

	public void setLblRecTotalReScheduledRejected(
			Label lblRecTotalReScheduledRejected) {
		this.lblRecTotalReScheduledRejected = lblRecTotalReScheduledRejected;
	}

	public Label getLblRecTotalScheduled() {
		return lblRecTotalScheduled;
	}

	public void setLblRecTotalScheduled(Label lblRecTotalScheduled) {
		this.lblRecTotalScheduled = lblRecTotalScheduled;
	}

	public Label getLblRecTotalShortlisted() {
		return lblRecTotalShortlisted;
	}

	public void setLblRecTotalShortlisted(Label lblRecTotalShortlisted) {
		this.lblRecTotalShortlisted = lblRecTotalShortlisted;
	}

	public Label getLblRecTotalViewed() {
		return lblRecTotalViewed;
	}

	public void setLblRecTotalViewed(Label lblRecTotalViewed) {
		this.lblRecTotalViewed = lblRecTotalViewed;
	}

	public Label getLblRecVacancyCode() {
		return lblRecVacancyCode;
	}

	public void setLblRecVacancyCode(Label lblRecVacancyCode) {
		this.lblRecVacancyCode = lblRecVacancyCode;
	}

	public Label getLblRecTotalAnotherInterview() {
		return lblRecTotalAnotherInterview;
	}

	public void setLblRecTotalAnotherInterview(Label lblRecTotalAnotherInterview) {
		this.lblRecTotalAnotherInterview = lblRecTotalAnotherInterview;
	}	
}

	