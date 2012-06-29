package com.tms.hr.recruit.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.TimeField;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.hr.recruit.model.InterviewObj;
import com.tms.hr.recruit.model.RecruitAppModule;
import com.tms.hr.recruit.model.SendMessage;
import com.tms.hr.recruit.model.VacancyObj;

public class PopupReSchedule extends Form{
	
	private Label lblRecApplicantName;
	private Label lblRecVacancyCode;
	private Label lblRecPostion;
	private Label lblRecStageOfInterview;
	private Label lblRecApplicantStatus;
	
	private DatePopupField interviewDate;
	private TimeField startTime;
	
	private Panel btnPanel;
	private Button btnSubmit;
	private Button btnCancel;
	
	private String interviewDateId;
	private String applicantId; 
	private String vacancyCode;
	private String applicantName;
	private String postionApplied;
	
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
    	lblRecApplicantName= new Label("lblRecApplicantName", "");
    	addChild(lblRecApplicantName);
    	
    	Label lblVacancyCode= new Label("lblVacancyCode", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.vacancyCode") + "</span>");
    	lblVacancyCode.setAlign("right");
    	addChild(lblVacancyCode);
    	lblRecVacancyCode= new Label("lblRecVacancyCode", "");
    	addChild(lblRecVacancyCode);
    	
    	Label lblPostion= new Label("lblPostion", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.position") + "</span>");
    	lblPostion.setAlign("right");
    	addChild(lblPostion);
    	lblRecPostion= new Label("lblRecPostion", "");
    	addChild(lblRecPostion);
    	
    	Label lblStageOfInterview= new Label("lblStageOfInterview", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.stageOfInterview") + "</span>");
    	lblStageOfInterview.setAlign("right");
    	addChild(lblStageOfInterview);
    	lblRecStageOfInterview= new Label("lblRecStageOfInterview", "");
    	addChild(lblRecStageOfInterview);
    	
    	Label lblStatus= new Label("lblStatus", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.status") + "</span>");
    	lblStatus.setAlign("right");
    	addChild(lblStatus);
    	lblRecApplicantStatus= new Label("lblRecApplicantStatus", "");
    	addChild(lblRecApplicantStatus);
    	
    	//start fields
    	Label lblInterviewDate= new Label("lblInterviewDate", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.interviewDate") + "*</span>");
        lblInterviewDate.setAlign("right");
    	addChild(lblInterviewDate);
    	interviewDate = new DatePopupField("interviewDate");
    	addChild(interviewDate);
    	
    	Label lblTime= new Label("lblTime", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.time") + "</span>");
    	lblTime.setAlign("right");
    	addChild(lblTime);
    	startTime = new TimeField("startTime");
        startTime.setTemplate("calendar/timefield");
        addChild(startTime); 
    	
    	btnSubmit= new Button("btnSubmit", app.getMessage("recruit.general.label.submit","submit"));
     	btnCancel = new Button(Form.CANCEL_FORM_ACTION, app.getMessage("recruit.general.label.close","Close"));
     	
     	btnPanel = new Panel("btnPanel");
     	btnPanel.setColspan(2);
     	btnPanel.addChild(btnSubmit);
     	btnPanel.addChild(btnCancel);
     	Label lblspace = new Label("lblspace","");
     	addChild(lblspace);
     	addChild(btnPanel);
	}	
	
	public void onRequest(Event evt) {
		init();
		populateDate();
	}
	
	public void populateDate(){
		Application app = Application.getInstance();
		RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
		Collection interviewDateCol = ram.lookUpInterviewDateId(getInterviewDateId());//load interview date detail rec_interview_date ONLY
		HashMap interviewDateMap = (HashMap)interviewDateCol.iterator().next(); 
		lblRecStageOfInterview.setText(interviewDateMap.get("interviewStageName").toString());
	
		Date dInterviewDateTime = (Date) interviewDateMap.get("interviewDateTime");
		interviewDate.setDate(dInterviewDateTime);
		startTime.setDate(dInterviewDateTime); 
		
		Collection personalCol = ram.loadApplicantPersonal(getApplicantId()); //load ALL applicant detail including the vacancy detail
		HashMap personalMap = (HashMap)personalCol.iterator().next();
		
		lblRecApplicantName.setText(personalMap.get("name").toString());
		setApplicantName(personalMap.get("name").toString());
		
		lblRecVacancyCode.setText(personalMap.get("vacancyCode").toString());
		setVacancyCode(personalMap.get("vacancyCode").toString());
		
		lblRecPostion.setText(personalMap.get("positionDesc").toString());
		setPostionApplied(personalMap.get("positionDesc").toString());
		
		lblRecApplicantStatus.setText(personalMap.get("applicantStatus").toString());
	}
	
	//setting for interviewDate and startTime into one-onValidate
	public Date setInterviewDate(DatePopupField interviewDate, TimeField startTime){
		//set the dateTime
		 Calendar startDate = Calendar.getInstance();
		 startDate.setTime(interviewDate.getDate());
		 startDate.set(Calendar.HOUR_OF_DAY, startTime.getHour());
		 startDate.set(Calendar.MINUTE, startTime.getMinute());
		 Date getInterviewDateTime=startDate.getTime();
		 return getInterviewDateTime;
	}
	
	/*public Forward onSubmit(Event evt) {
	
		return super.onSubmit(evt);
	}*/
	
	public Forward onValidate(Event evt) {
		String action = findButtonClicked(evt);
		if(action.equals(btnSubmit.getAbsoluteName())){
			Application app = Application.getInstance();
			RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			InterviewObj interviewObj = new InterviewObj();
			
			String[] ainterviewDateId = {getInterviewDateId()};  
			interviewObj.setInterviewDateId(ainterviewDateId);
			Date dinterviewDateTime = setInterviewDate(interviewDate, startTime);
			interviewObj.setInterviewDateTime(dinterviewDateTime);
			
			//update the interview Date Time
			ram.updateInterviewDateTime(interviewObj);
			
			//auditObj-for updating the interview date & time
			VacancyObj auditObj = new VacancyObj();
			String actionTaken="";
			actionTaken="Update interview Date & time";
			auditObj.setAndInsertAudit(getVacancyCode(), getApplicantId(), actionTaken);
			
			//send mail
			DateFormat dmyDateFmt = new SimpleDateFormat(app.getProperty("globalDatetimeLong"));
			List ToList= new ArrayList();
			Collection interviewerCol = ram.lookUpInterviewer(getInterviewDateId());
			for(Iterator ite = interviewerCol.iterator(); ite.hasNext();){
				HashMap interviewerMap = (HashMap)ite.next();
				ToList.add(interviewerMap.get("interviewerId"));
			}
			
			if(ToList.size() > 0){
				SendMessage sm = new SendMessage();
				String subjectTitle = "Changes to Interview Date & Time for interviewee,"+ getApplicantName(); 
				String title = "Changes to Interview Date & Time for interviewee,"+ applicantName + ".The following are the new Interview Date & Time"; 
				StringBuffer sbTitle = new StringBuffer();
				sbTitle.append("<table cellspacing=0 cellpadding=5 width=\"100%\" border=0>\n");
				sbTitle.append("  <tr valign=center> \n");
				sbTitle.append("    <td bgcolor=\"#FFFFFF\" colspan=\"4\"><b><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\" color=\"#000000\">" + title + "</font></b></td>\n");
				sbTitle.append("  </tr>\n");
		        
				sbTitle.append("  <tr> \n");
				sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">New Interview Date & Times</font></td>\n");
				sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">Interviewee(s)</font></td>\n");
				sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">Vacancy Code</font></td>\n");
				sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">Position</font></td>\n");
				sbTitle.append("  </tr>\n");
				
				sbTitle.append("  <tr> \n");
				sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">" + dmyDateFmt.format(setInterviewDate(interviewDate, startTime)) + "</font></td>\n");
				sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">" + getApplicantName() + "</font></td>\n");
				sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">" + getVacancyCode() + "</font></td>\n");
				sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">" + getPostionApplied() + "</font></td>\n");
				sbTitle.append("  </tr>\n");
				
				sbTitle.append("</table>\n");
				sm.sendMessage(ToList, subjectTitle, sbTitle.toString(), "");
				
				//auditObj-for email part
				Collection interviewerNameCol = ram.getSecurityUserName(getInterviewDateId());
				int i=0;
				StringBuffer sbcoma= new StringBuffer();
				for(Iterator iter = interviewerNameCol.iterator();iter.hasNext();){
					HashMap interviewerNameMap = (HashMap)iter.next();
					
					sbcoma.append(interviewerNameMap.get("username"));
					
					if(i>0 && i<interviewerCol.size())
						sbcoma.append(" ,");
					
					i++;
				}
				
				actionTaken="Send eMail to interviewer(s), "+ sbcoma.toString() + " for interview Date & Time changes" ;
				auditObj.setAndInsertAudit(getVacancyCode(), getApplicantId(), actionTaken);
				
				//update the rescheduled 
				ApplicantList appList = new ApplicantList();
				VacancyObj vacancyObj = new VacancyObj();
				vacancyObj = appList.setVacancyTotal(vacancyCode,"totalReScheduled", false);
				ram.updateVacancyTotal(vacancyObj);
			}
			return new Forward("submit");
	 	}else
	 		return new Forward("error");
	}
	
	//getter and setter
	public String getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}

	public String getInterviewDateId() {
		return interviewDateId;
	}

	public void setInterviewDateId(String interviewDateId) {
		this.interviewDateId = interviewDateId;
	}
	
	//for onValidate
	public String getVacancyCode() {
		return vacancyCode;
	}

	public void setVacancyCode(String vacancyCode) {
		this.vacancyCode = vacancyCode;
	}
	
	public String getApplicantName() {
		return applicantName;
	}

	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}

	public String getPostionApplied() {
		return postionApplied;
	}

	public void setPostionApplied(String postionApplied) {
		this.postionApplied = postionApplied;
	}
}
