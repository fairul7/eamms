package com.tms.hr.recruit.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.ButtonGroup;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.Radio;
import kacang.stdui.TextBox;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.ekms.setup.model.SetupModule;
import com.tms.hr.recruit.model.ApplicantObj;
import com.tms.hr.recruit.model.RecruitAppModule;
import com.tms.hr.recruit.model.SendMessage;
import com.tms.hr.recruit.model.VacancyObj;

public class InterviewResult extends Form{
	public static final String FORWARD_SUBMIT = "submit";
	public static final String FORWARD_ERROR = "error";
	
	private String applicantId;
	private String interviewDateId;
	private String totalInterview;
	private String vacancyCode;
	
	private Label lblRecApplicant;
	private Label lblRecStageOfInterview;
	/*private Label lblRecInterviewer;
	private Label lblRecInterviewDateTime;*/
	private Label lblRecVacancyCode;
	private Label lblRecPostion;
	private Label lblRecTotalPosition;
	
	private Radio radioOffer;
	private Radio radioReject;
	private Radio radioAnotherInterview;
	private ButtonGroup radioInterviewResult;
	
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
    	
    	/*Label lblInterviewer= new Label("lblInterviewer", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.interviewer") + "</span>");
    	lblInterviewer.setAlign("right");
    	addChild(lblInterviewer);
    	lblRecInterviewer= new Label("lblRecInterviewer", "");
    	addChild(lblRecInterviewer);
    	
    	Label lblInterviewDateTime= new Label("lblInterviewDateTime", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.interviewDateTime") + "</span>");
    	lblInterviewDateTime.setAlign("right");
    	addChild(lblInterviewDateTime);
    	lblRecInterviewDateTime= new Label("lblRecInterviewDateTime", "");
    	addChild(lblRecInterviewDateTime);*/
    	
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
    	
    	Label lblInterviewResult= new Label("lblInterviewResult", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.interviewResult") + "</span>");
    	lblInterviewResult.setAlign("right");
    	addChild(lblInterviewResult);
    	radioOffer= new Radio("Offered", app.getMessage("recruit.general.label.offered"));
    	radioReject = new Radio("Interview Unsuccessful", app.getMessage("recruit.general.label.interviewUnsuccessful"));
    	radioAnotherInterview = new Radio("Another Interview", app.getMessage("recruit.general.label.anotherInterview"));
    	
    	radioInterviewResult = new ButtonGroup("radioInterviewResult");
    	radioInterviewResult.setType(ButtonGroup.RADIO_TYPE);
    	radioInterviewResult.setColumns(3);
    	radioInterviewResult.addButton(radioOffer);
    	radioInterviewResult.addButton(radioReject);
    	radioInterviewResult.addButton(radioAnotherInterview);
    	addChild(radioInterviewResult);
    	
    	Label lblRemark= new Label("lblRemark", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.remark") + "</span>");
    	lblRemark.setAlign("right");
    	addChild(lblRemark);
    	tbRemark = new TextBox("tbRemark");
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
		Collection idCol = new ArrayList();
		idCol.add(getInterviewDateId());

		Application app = Application.getInstance();  
		RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
		
		//lookup applicant status
		Collection lookupCol = ram.lookUpApplicantStatus(getApplicantId());
		ApplicantObj lookupApplicantObj= (ApplicantObj)lookupCol.iterator().next();
		Collection intervieweeCol=new ArrayList();
		
		if(lookupApplicantObj.getApplicantStatus().equals("Scheduled"))
			intervieweeCol = ram.findSelectedInterviewee(null, true, 0, -1, idCol, "Scheduled");
		/*else if(lookupApplicantObj.getApplicantStatus().equals("Scheduled & Interviewer(s) added"))
			intervieweeCol = ram.findSelectedInterviewee(null, true, 0, -1, idCol, "Scheduled & Interviewer(s) added");*/
		
		HashMap map = (HashMap) intervieweeCol.iterator().next();
		
		/*Collection interviewerNameCol = ram.getSecurityUserName(map.get("interviewDateId").toString());
		String name="";
		int i=0;
		for(Iterator ite=interviewerNameCol.iterator(); ite.hasNext();){
			HashMap mapName = (HashMap) ite.next();
			if(i>0 && i<interviewerNameCol.size())
				name+=",";
			
			name+=mapName.get("username");
			i++;
		}*/
		
		lblRecApplicant.setText("<a href='#' onClick=\"NewWindow('jobApplicationFormViewPopUp.jsp?applicantId="+map.get("applicantId").toString()+"','applicantName','500','670','yes'); return false; \">"+map.get("name").toString()+"</a>");
		lblRecVacancyCode.setText("<a href='#' onClick=\"NewWindow('vacancyDetail.jsp?vacancyCode="+map.get("vacancyCode").toString()+"','vacancyDetails','500','500','yes'); return false; \">"+map.get("vacancyCode").toString()+"</a>");
		
		//lblRecApplicant.setText(map.get("name").toString());
		//lblRecApplicant.setText("<a href='#' onClick=javascript:window.open('jobApplicationFormViewPopUp.jsp?applicantId="+map.get("applicantId").toString()+"','blank','toolbar=no,width=500,height=670,scrollbars=yes'); return false; >"+map.get("name").toString()+"</a>");		
		//lblRecVacancyCode.setText(map.get("vacancyCode").toString());
		//lblRecVacancyCode.setText("<a href='#' onClick=javascript:window.open('vacancyDetail.jsp?vacancyCode="+map.get("vacancyCode").toString()+"','blank','toolbar=no,width=500,height=500,scrollbars=yes'); return false; >"+map.get("vacancyCode").toString()+"</a>");
		
		lblRecStageOfInterview.setText(map.get("interviewStageName").toString());
		/*lblRecInterviewer.setText(name);
		lblRecInterviewDateTime.setText(map.get("interviewDateTime").toString());*/
		lblRecPostion.setText(map.get("positionDesc").toString());
		lblRecTotalPosition.setText(map.get("totalPosition").toString());
		
		setVacancyCode(map.get("vacancyCode").toString());
		
		if(map.get("jobOfferRemark")!=null && !map.get("jobOfferRemark").equals(""))
			tbRemark.setValue(map.get("jobOfferRemark"));
	}

	public Forward onSubmit(Event evt) {
		Forward forward = super.onSubmit(evt);
		
		if(!radioOffer.isChecked() && !radioReject.isChecked() && !radioAnotherInterview.isChecked()){
			radioInterviewResult.setInvalid(true);
			setInvalid(true);
		}
		
		return forward;
	}
	
//no need validation - validation OFF coz there is no status ' Scheduled & Interviewer(s) added '	
/*	public Forward onSubmit(Event evt) {
		Forward forward = super.onSubmit(evt);
		boolean noInterviewer=false;
		boolean flag = false;
		//int error=0;
		Application app = Application.getInstance();
		RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
		Collection remarkCol = ram.findAllIntervieweeRemark("", false, 0, -1, getApplicantId(), "");
		for(Iterator ite=remarkCol.iterator();ite.hasNext();){
			HashMap map = (HashMap) ite.next();
			if(map.get("remark").equals("on progress"))
				error++;
		}
		
		if(error > 0){
			flag=true;
		}	
		
		Collection applicantStatusCol = ram.lookUpApplicantStatus(getApplicantId());
		ApplicantObj applicantObj = (ApplicantObj)applicantStatusCol.iterator().next();
		if(!applicantObj.getApplicantStatus().equals(app.getMessage("recruit.general.label.scheduledAndInterviewerAdded"))){
			flag=true;
			noInterviewer=true;
		}
		
		if(flag || this.isInvalid()){
			 setInvalid(true);
			 if(noInterviewer)
				 return new Forward("noInterviewerAssigned");
			 else
				 return new Forward("noRemarkGiven");
		}
		
		return forward;
	}*/
	
	public Forward onValidate(Event evt) {
		String action = findButtonClicked(evt);
		if(action.equals(btnSubmit.getAbsoluteName())){
			Application app = Application.getInstance();
			ApplicantObj applicantObj = new ApplicantObj();
			RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			Date getDate = new Date();
			String statusType="";
			String userStatus="";
			//get the total interview value-single
			Collection col = ram.lookUpApplicantStatus(getApplicantId());
			ApplicantObj totalObj=(ApplicantObj) col.iterator().next();
			int total = totalObj.getTotalInterview();
			String vacancyCode = totalObj.getVacancyCode();
			String name = totalObj.getName(); //set the name for email usage
			String positionDesc = totalObj.getPositionApplied();
	
			applicantObj.setApplicantId(getApplicantId());
		
			//auditObj
			VacancyObj auditObj = new VacancyObj();
			String actionTaken="";
			
			if(radioOffer.isChecked()){
				applicantObj.setApplicantStatus(radioOffer.getText());
				applicantObj.setJobOfferDate(getDate);
				//total+=1;
				applicantObj.setTotalInterview(total);
				statusType="totalJobOffered";
				userStatus = radioOffer.getText(); //email usage
			}	
			else if(radioReject.isChecked()){
				applicantObj.setApplicantStatus(radioReject.getText());
				applicantObj.setTotalInterview(total);
				statusType="totalInterviewUnsuccessful";
				userStatus = radioReject.getText();
			}
			else if(radioAnotherInterview.isChecked()){
				applicantObj.setApplicantStatus(radioAnotherInterview.getText());
				total+=1;
				applicantObj.setTotalInterview(total);
				userStatus = radioAnotherInterview.getText();
				
				//add another interview +1******DO LATER if need only
				ApplicantList appList = new ApplicantList();
				VacancyObj vacancyObj = new VacancyObj(); 
				vacancyObj = appList.setVacancyTotal(vacancyCode,"totalAnotherInterview", false);
				ram.updateVacancyTotal(vacancyObj);
			}
			
			applicantObj.setJobOfferRemark((String)tbRemark.getValue());
			
			//update applicant status
			ram.updateApplicantStatusResult(applicantObj);
			
			//audit
			actionTaken="Applicant Interview Result, " + userStatus;
			auditObj.setAndInsertAudit(getVacancyCode(), getApplicantId(), actionTaken);
			
			//update vacancy totals
			if(radioOffer.isChecked() || radioReject.isChecked()){
				ApplicantList appList = new ApplicantList();
				VacancyObj vacancyObj = new VacancyObj();
				vacancyObj = appList.setVacancyTotal(vacancyCode, statusType, false);
				ram.updateVacancyTotal(vacancyObj);
			}
			
			//send mail to admin department
			SendMessage sm = new SendMessage();
			SecurityService service = (SecurityService)Application.getInstance().getService(SecurityService.class);
		     try{
			        Collection recruitAdminCol = service.getUsersByPermission("recruit.permission.recruitAdmin",Boolean.TRUE, null, false, 0, -1);
			        List ToList = new ArrayList(recruitAdminCol.size());
			        StringBuffer sbName = new StringBuffer();
			        int j = 0;
			        for(Iterator ite=recruitAdminCol.iterator(); ite.hasNext();){
		        		 User user = (User) ite.next();
		        		 ToList.add(user.getId());
		        		 
		        		 if(j>0 && j<recruitAdminCol.size())
		        			 sbName.append(", ");
		        		 
		        		 sbName.append(user.getName());
		        	 j++;	
		        	 }
		    
			        String subjectTitle="Interview Result";
			
					StringBuffer sbTitle = new StringBuffer();
					sbTitle.append("<table cellspacing=0 cellpadding=5 width=\"100%\" border=0>\n");
					sbTitle.append("  <tr valign=center> \n");
					sbTitle.append("    <td bgcolor=\"#FFFFFF\" colspan=\"4\"><b><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\" color=\"#000000\">" + subjectTitle + "</font></b></td>\n");
					sbTitle.append("  </tr>\n");
			        
					sbTitle.append("  <tr> \n");
					sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">Interviewee</font></td>\n");
					sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">Vacancy Code</font></td>\n");
					sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">Position</font></td>\n");
					sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">Result</font></td>\n");
					sbTitle.append("  </tr>\n");
					
					if(radioOffer.isChecked()){
						try{
						SetupModule setup = (SetupModule) Application.getInstance().getModule(SetupModule.class);
						String siteUrl = setup.get("siteUrl");
						
						sbTitle.append("  <tr> \n");
						
						sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">" +
								"<a href='"+siteUrl+"/ekms/recruit/jobOfferForm.jsp?applicantId="+getApplicantId()+"'>"+name+"</a></font></td>\n");
						
						sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">"+vacancyCode+"</font></td>\n");
						sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">"+positionDesc+"</font></td>\n");
						sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">"+userStatus+"</font></td>\n");
						sbTitle.append("  </tr>\n");
						
						String directLink="<a href='"+siteUrl+"/ekms/recruit/jobOfferList.jsp'>"+
							app.getMessage("recruit.menu.label.clickToView") +" " +app.getMessage("recruit.menu.label.jobOfferListing")+"</a>";
						sbTitle.append("  <tr valign=center> \n");
						sbTitle.append("    <td bgcolor=\"#FFFFFF\" colspan=\"4\"><b><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\" color=\"#000000\">" + directLink + "</font></b></td>\n");
						sbTitle.append("  </tr>\n");
						
						}catch (Exception e) {
							Log.getLog(getClass()).error("Error getting site URL", e);
						}
					}else{
						sbTitle.append("  <tr> \n");
						sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">"+name+"</font></td>\n");
						sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">"+vacancyCode+"</font></td>\n");
						sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">"+positionDesc+"</font></td>\n");
						sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">"+userStatus+"</font></td>\n");
						sbTitle.append("  </tr>\n");
					}
					
					sbTitle.append("</table>\n"); 
			 
			 sm.sendMessage(ToList, subjectTitle, sbTitle.toString(), "");
			 
			//audit
			actionTaken="Send eMail to Recruit Admin(s), " + sbName.toString();
			auditObj.setAndInsertAudit(getVacancyCode(), getApplicantId(), actionTaken);
			 
		     }catch (Exception e){
		        	Log.getLog(getClass()).error(e);
		     }
			//end mail
		     
			return new Forward(FORWARD_SUBMIT);
   	 	}else
   	 		return new Forward(FORWARD_ERROR);
	}	
	
	//getter setter
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

	public String getTotalInterview() {
		return totalInterview;
	}

	public void setTotalInterview(String totalInterview) {
		this.totalInterview = totalInterview;
	}

	public String getVacancyCode() {
		return vacancyCode;
	}

	public void setVacancyCode(String vacancyCode) {
		this.vacancyCode = vacancyCode;
	}


}
