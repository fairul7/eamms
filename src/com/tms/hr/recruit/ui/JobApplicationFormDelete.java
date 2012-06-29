package com.tms.hr.recruit.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Panel;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.ekms.setup.model.SetupModule;
import com.tms.hr.recruit.model.RecruitAppModule;
import com.tms.hr.recruit.model.RecruitModule;
import com.tms.hr.recruit.model.SendMessage;
import com.tms.hr.recruit.model.VacancyObj;

public class JobApplicationFormDelete extends Form{
	public static final String FORWARD_DELETED = "JAFdeleted";
	public static final String FORWARD_ERROR = "error";
	
	private String applicantId;
	private boolean canDelete;
	private Panel btnPanel;
	private Button btnDeleteJobApp;
	
	public void init(){
		setMethod("POST");
		Application app = Application.getInstance();
		setAlign("center");
		btnDeleteJobApp = new Button("btnDeleteJobApp", app.getMessage("recruit.general.label.btnDeleteJobApp","Delete Job Application Form"));
		btnDeleteJobApp.setAlign("center");
    	btnPanel = new Panel("btnPanel");
    	btnPanel.setColspan(1);
    	btnPanel.addChild(btnDeleteJobApp);
    	addChild(btnPanel);
	}
	
	public Forward onSubmit(Event evt) {
		Forward forward = super.onSubmit(evt); 
		Application app = Application.getInstance();
		RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
		String action = findButtonClicked(evt);
		if(action.equals(btnDeleteJobApp.getAbsoluteName())){
			HashMap dataMap = (HashMap)ram.loadApplicantPersonal(getApplicantId()).iterator().next();
			if(dataMap.get("applicantStatus").equals("New")){
				setCanDelete(true);
				//return new Forward("yes");
			}else{
				setCanDelete(false);
				return new Forward("cannotDelete");
			}
		}
		return forward;
	}
	
	public Forward onValidate(Event evt) {
		String action = findButtonClicked(evt);
		Application app = Application.getInstance();
		RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
		RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
		
		VacancyObj vacancyObj = new VacancyObj();
		ApplicantList appList = new ApplicantList();
		
		//auditObj
		VacancyObj auditObj = new VacancyObj();
		String actionTaken="";
		 
		if(action.equals(btnDeleteJobApp.getAbsoluteName())){
			if(isCanDelete()){
				try{
					SetupModule setup = (SetupModule) Application.getInstance().getModule(SetupModule.class);
					String siteUrl = setup.get("siteUrl");
					
					SendMessage sm = new SendMessage();
					Collection col = ram.loadApplicantPersonal(getApplicantId());
					HashMap applicantMap = (HashMap) col.iterator().next();
					
					String applicantEmail = applicantMap.get("email").toString();
					String applicantName = applicantMap.get("name").toString();
					String vacancyCode = applicantMap.get("vacancyCode").toString();
					String positionDesc = applicantMap.get("positionDesc").toString();
					
					//send email to HOD 
					String subjectTitle="Job Application Form : " + vacancyCode + "-" + positionDesc;
					String notificationBody = "<p>Applicant Name :<i>" + applicantName + "</i> has deleted Job Applicantion Form. </p>" ;
					
					StringBuffer sbTitle = new StringBuffer();
					sbTitle.append("<table cellspacing=0 cellpadding=5 width=\"100%\" border=0>\n");
					sbTitle.append("  <tr valign=center> \n");
					sbTitle.append("    <td bgcolor=\"#FFFFFF\" colspan=\"3\"><b><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\" color=\"#000000\">" + notificationBody + "</font></b></td>\n");
					sbTitle.append("  </tr>\n");
			        
					sbTitle.append("  <tr> \n");
					sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">Applicant</font></td>\n");
					sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">Vacancy Code</font></td>\n");
					sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">Position</font></td>\n");
					sbTitle.append("  </tr>\n");
						
					sbTitle.append("  <tr> \n");
					sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">" +applicantName+"</font></td>\n");
					sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">"+vacancyCode+"</font></td>\n");
					sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">"+positionDesc+"</font></td>\n");
					sbTitle.append("  </tr>\n");
						
					sbTitle.append("</table>\n"); 
					
					HashMap vacancyMap = (HashMap)rm.findSpecificVacancy(vacancyCode).iterator().next();
					String hodId = vacancyMap.get("createdBy").toString();
					
					List ToList=new ArrayList();
					ToList.add(hodId);
					
					sm.sendMessage(ToList, subjectTitle, sbTitle.toString(), applicantEmail);
					
					//audit
					actionTaken="Send notification to head of department about "+ applicantName +" has deleted the Job Application Form";
					auditObj.setAndInsertAudit(vacancyCode, getApplicantId(), actionTaken);
					
					//send email again to Applicant
					List ToListApplicant = new ArrayList();
					ToListApplicant.add(applicantEmail);
					String subjectTitleApplicant="Thank you";
					StringBuffer sbBodyTitleApplicant = new StringBuffer();
					String subjectApplicant="Dear "+ applicantName + ",";
					sbBodyTitleApplicant.append("<font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\" color=\"#000000\">" + subjectApplicant+"");
					sbBodyTitleApplicant.append("<p>Thank you for submitting the Job Application Form to us before. </p>");
					sbBodyTitleApplicant.append("<p>If you would like to apply again, " +
							"Please Click <a href= '"+siteUrl+"/cms/carrerList.jsp'>" +
							"Here.</a></p><p>Thank you.</p>");
					sbBodyTitleApplicant.append("</font>");
					
					sm.sendMessageToApplicant(ToListApplicant, subjectTitleApplicant, sbBodyTitleApplicant.toString(), hodId , hodId);
					
					//audit
					actionTaken="Send notification to Applicant for the Job Application Form Deletion";
					auditObj.setAndInsertAudit(vacancyCode, getApplicantId(), actionTaken);
					
					//update the total applied -1-to keep history for the vacancy report thus cannot update this
					//vacancyObj = appList.setVacancyTotal(vacancyCode, "totalApplied", true);
					//ram.updateVacancyTotal(vacancyObj);
					
					//audit 
					actionTaken="Delete Applicant Detail";
					auditObj.setAndInsertAudit(vacancyCode, getApplicantId(), actionTaken);
					
					//delete applicant Details
					ram.deleteAllApplicantDetail(getApplicantId());
					
					//to stop the on request...at JobAppPersonalView.java
					JobAppPersonalView jAppPersonalView = (JobAppPersonalView) Application.getInstance().getWidget(evt.getRequest(), "jobApplicationFormView.tab1.panel1.view");
					jAppPersonalView.setApplicantId(null);
					
				}catch (Exception e) {
					Log.getLog(getClass()).error("Error getting site URL", e);
				}
			}
		
		 return new Forward(FORWARD_DELETED);
  	 }else
  		 return new Forward(FORWARD_ERROR);
	}

	public String getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}

	public boolean isCanDelete() {
		return canDelete;
	}

	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}
	
}
