package com.tms.hr.recruit.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.TextBox;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.ekms.setup.model.SetupModule;
import com.tms.hr.recruit.model.InterviewObj;
import com.tms.hr.recruit.model.RecruitAppModule;
import com.tms.hr.recruit.model.RecruitModule;
import com.tms.hr.recruit.model.SendMessage;
import com.tms.hr.recruit.model.VacancyObj;
import com.tms.hr.recruit.model.VacancyTempFormException;

public class InterviewerRemarkForm extends Form{
	public static final String FORWARD_SUBMIT = "submit";
	public static final String FORWARD_ERROR = "error";
	
	private String applicantId;
	private String interviewDateId;
	private String vacancyCode;
	
	private Label lblRecStageOfInterview;
	private Label lblRecInterviewDateTime;
	private Label lblRecApplicant;
	private Label lblRecVacancyCode;
	private Label lblRecPostion;
	private Label lblRecTotalPosition;
	private Label lblRecDateAssigned;
	
	private TextBox tbRemark;
	
	private Panel btnPanel;
	private Button btnSubmit;
	private Button btnCancel;
	
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
    	
    	Label lblStageOfInterview= new Label("lblStageOfInterview", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.stageOfInterview") + "</span>");
    	lblStageOfInterview.setAlign("right");
    	addChild(lblStageOfInterview);
    	lblRecStageOfInterview= new Label("lblRecStageOfInterview", "");
    	addChild(lblRecStageOfInterview);
    	
    	Label lblInterviewDateTime= new Label("lblInterviewDateTime", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.interviewDateTimes") + "</span>");
    	lblInterviewDateTime.setAlign("right");
    	addChild(lblInterviewDateTime);
    	lblRecInterviewDateTime= new Label("lblRecInterviewDateTime", "");
    	addChild(lblRecInterviewDateTime);
    	
    	Label lblPostion= new Label("lblPostion", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.position") + "</span>");
    	lblPostion.setAlign("right");
    	addChild(lblPostion);
    	lblRecPostion= new Label("lblRecPostion", "");
    	addChild(lblRecPostion);
    	
    	Label lblTotalPosition= new Label("lblTotalPosition", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.noOfPosition") + "</span>");
    	lblTotalPosition.setAlign("right");
    	addChild(lblTotalPosition);
    	lblRecTotalPosition= new Label("lblRecTotalPosition", "");
    	addChild(lblRecTotalPosition);
    	
    	Label lblDateAssigned= new Label("lblDateAssigned", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.dateAssigned") + "</span>");
    	lblDateAssigned.setAlign("right");
    	addChild(lblDateAssigned);
    	lblRecDateAssigned= new Label("lblRecDateAssigned", "");
    	addChild(lblRecDateAssigned);
    	
    	Label lblRemark= new Label("lblRemark", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.remark") + "</span>");
    	lblRemark.setAlign("right");
    	addChild(lblRemark);
    	tbRemark = new TextBox("tbRemark");
    	//tbRemark.addChild(new ValidatorNotEmpty("tbRemarkVNE", app.getMessage("recruit.general.warn.empty")));
    	addChild(tbRemark);
    	
    	btnSubmit= new Button("btnSend", app.getMessage("recruit.general.label.submit","submit"));
     	btnCancel = new Button(Form.CANCEL_FORM_ACTION, app.getMessage("recruit.general.label.cancel","Cancel"));
     	
     	btnPanel = new Panel("btnPanel");
     	btnPanel.setColspan(2);
     	btnPanel.addChild(btnSubmit);
     	btnPanel.addChild(btnCancel);
     	Label lblspace = new Label("lblspace","");
     	addChild(lblspace);
     	addChild(btnPanel);
	}
	
	public void onRequest(Event evt){
		init();
		populateData();
	}
	
	public void populateData(){
		Application app = Application.getInstance();  
		RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
		Collection intervieweeCol = ram.findSpecificInterviewee(app.getCurrentUser().getId(), getInterviewDateId(), getApplicantId());
		
		HashMap map = (HashMap) intervieweeCol.iterator().next();
		
		lblRecStageOfInterview.setText(map.get("interviewStageName").toString());
		DateFormat dmyDateFmt = new SimpleDateFormat(app.getProperty("globalDatetimeLong"));
		lblRecInterviewDateTime.setText(dmyDateFmt.format(map.get("interviewDateTime")));
	
		lblRecApplicant.setText("<a href='#' onClick=\"NewWindow('jobApplicationFormViewPopUp.jsp?applicantId="+map.get("applicantId").toString()+"','applicantName','500','670','yes'); return false; \">"+map.get("name").toString()+"</a>");
		lblRecVacancyCode.setText("<a href='#' onClick=\"NewWindow('vacancyDetail.jsp?vacancyCode="+map.get("vacancyCode").toString()+"','vacancyDetails','500','500','yes'); return false; \">"+map.get("vacancyCode").toString()+"</a>");
		
		//lblRecApplicant.setText("<a href='#' onClick=javascript:window.open('jobApplicationFormViewPopUp.jsp?applicantId="+map.get("applicantId").toString()+"','blank','toolbar=no,width=500,height=670,scrollbars=yes'); return false; >"+map.get("name").toString()+"</a>")	;
		//lblRecVacancyCode.setText(map.get("vacancyCode").toString());
		//lblRecVacancyCode.setText("<a href='#' onClick=javascript:window.open('vacancyDetail.jsp?vacancyCode="+map.get("vacancyCode").toString()+"','blank','toolbar=no,width=500,height=500,scrollbars=yes'); return false; >"+map.get("vacancyCode").toString()+"</a>");
	
		lblRecPostion.setText(map.get("positionDesc").toString());
		lblRecTotalPosition.setText(map.get("totalPosition").toString());
		lblRecDateAssigned.setText(map.get("dateAssigned").toString());
		
		setVacancyCode(map.get("vacancyCode").toString());
		
		if(!map.get("remark").equals(""))
			tbRemark.setValue(map.get("remark").toString());
	}
	
	public Forward onValidate(Event evt) {
		String action = findButtonClicked(evt);
		if(action.equals(btnSubmit.getAbsoluteName())){
			Application app = Application.getInstance();
			RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
			InterviewObj interviewObj = new InterviewObj();
			Date getDate = new Date();
			interviewObj.setRemark((String)tbRemark.getValue());
			interviewObj.setLastUpdatedBy(app.getCurrentUser().getId());
			interviewObj.setLastUpdatedDate(getDate);
			ram.updateInterviewerRemark(app.getCurrentUser().getId(), getInterviewDateId(), getApplicantId(), interviewObj);
			
			//audit #1
			VacancyObj auditObj = new VacancyObj();
			String actionTaken="";
			actionTaken="Enter applicant remark";
			auditObj.setAndInsertAudit(getVacancyCode(), getApplicantId(), actionTaken);
			
			//send mail to HOD
			try{
			SetupModule setup = (SetupModule) Application.getInstance().getModule(SetupModule.class);
			String siteUrl = setup.get("siteUrl");
			
			SendMessage sm = new SendMessage();
			String subjectTitle=app.getCurrentUser().getName() + ",interviewer has gave remark";
			Collection applicantNameCol = ram.loadApplicantPersonal(getApplicantId());
			HashMap map = (HashMap)applicantNameCol.iterator().next();
			
			Collection vacancyCol = rm.findSpecificVacancy(getVacancyCode());
			HashMap vacancyMap= (HashMap)vacancyCol.iterator().next();
			List ToList = new ArrayList();
			String vacancyCreator=vacancyMap.get("createdBy").toString();
			ToList.add(vacancyCreator);
			
			String applicantName = map.get("name").toString();
			String title=applicantName +" remark's";
			String strRemark = interviewObj.getRemark();
			
			StringBuffer sbTitle = new StringBuffer();
			sbTitle.append("<table cellspacing=0 cellpadding=5 width=\"100%\" border=0>\n");
			sbTitle.append("  <tr valign=center> \n");
			sbTitle.append("    <td bgcolor=\"#FFFFFF\" ><b><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\" color=\"#000000\">" + title + "</font></b></td>\n");
			sbTitle.append("  </tr>\n");
	        
			sbTitle.append("  <tr> \n");
			sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">"+ strRemark +"</font></td>\n");
			sbTitle.append("  </tr>\n");
			
		/*	String directLink="<a href='"+siteUrl+"/ekms/recruit/interviewResult.jsp?applicantId="+getApplicantId()+"&interviewDateId="+getInterviewDateId()+
			"'>"+app.getMessage("recruit.menu.label.clickToView")+" "+applicantName+"</a>";*/
			
			String directLink="<a href='"+siteUrl+"/ekms/recruit/intervieweeList.jsp'>"+app.getMessage("recruit.menu.label.clickToView")+" "+
				app.getMessage("recruit.menu.label.intervieweeListing")+"</a>";
			
			sbTitle.append("  <tr> \n");
			sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">"+ directLink +"</font></td>\n");
			sbTitle.append("  </tr>\n");
			sbTitle.append("</table>\n");
			
			sm.sendMessage(ToList, subjectTitle, sbTitle.toString(), "");
			
			
			//audit #2
			VacancyObj mailObj = new VacancyObj();
			String actionTaken2="";
			Collection usernameCol = ram.getSecurityUserNameSingle(vacancyCreator);
			HashMap usernameMap = (HashMap)usernameCol.iterator().next();
			String creatorName = usernameMap.get("username").toString();
			actionTaken2="Send eMail to "+creatorName+",Head Of Department for the remark given";
			auditObj.setAndInsertAudit(getVacancyCode(), getApplicantId(), actionTaken2);
			
			}catch (Exception e) {
				Log.getLog(getClass()).error("Error getting site URL", e);
			}
			
			return new Forward(FORWARD_SUBMIT);
   	 	}else
   	 		return new Forward(FORWARD_ERROR);
	}	
	
	//getter setter
	public String getInterviewDateId() {
		return interviewDateId;
	}
	public void setInterviewDateId(String interviewDateId) {
		this.interviewDateId = interviewDateId;
	}

	public String getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}

	public String getVacancyCode() {
		return vacancyCode;
	}

	public void setVacancyCode(String vacancyCode) {
		this.vacancyCode = vacancyCode;
	}
}
