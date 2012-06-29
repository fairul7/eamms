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
import kacang.stdui.validator.ValidatorMessage;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

import com.tms.hr.recruit.model.InterviewObj;
import com.tms.hr.recruit.model.RecruitAppModule;
import com.tms.hr.recruit.model.SendMessage;
import com.tms.hr.recruit.model.VacancyObj;

public class PopupSetupInterviewDateTime extends Form{
	//session used is - sessionInterviewObjCol for Collection of interviewObj
	
	private Label lblRecApplicantName;
	private Label lblRecVacancyCode;
	private Label lblRecPostion;
	//private Label lblRecStageOfInterview;
	private Label lblRecApplicantStatus;
	
	private DatePopupField interviewDate;
	private TimeField startTime;
	
	private Panel btnPanel;
	private Button btnSubmit;
	private Button btnCancel;
	
	private String applicantId; 
	private String vacancyCode;
	private String applicantName;
	private String postionApplied;
	private boolean hasInterviewDateId;
	private String interviewDateId;
	
	private ValidatorMessage vMsgStartDate;
	
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
    	
    /*	Label lblStageOfInterview= new Label("lblStageOfInterview", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.stageOfInterview") + "</span>");
    	lblStageOfInterview.setAlign("right");
    	addChild(lblStageOfInterview);
    	lblRecStageOfInterview= new Label("lblRecStageOfInterview", "");
    	addChild(lblRecStageOfInterview);*/
    	
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
    	vMsgStartDate = new ValidatorMessage("vMsgStartDate");
    	interviewDate.addChild(vMsgStartDate);
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
		Collection interviewObjColSs = (Collection)evt.getRequest().getSession().getAttribute("sessionInterviewObjCol");
		populateDate(interviewObjColSs);
	}
	
	public void populateDate(Collection interviewObjColSs){
		Application app = Application.getInstance();
		RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
		
		if(interviewObjColSs!=null && interviewObjColSs.size() > 0){
			for(Iterator ite=interviewObjColSs.iterator(); ite.hasNext();){
				InterviewObj iObj = (InterviewObj)ite.next();
				if(iObj.getApplicantId()[0].equals(getApplicantId())){
					interviewDate.setDate(iObj.getInterviewDateTime());
					startTime.setDate(iObj.getInterviewDateTime());
					
					setInterviewDateId(iObj.getInterviewDateId()[0]);
					setHasInterviewDateId(true);
				}	
			}	
		}else{
			setHasInterviewDateId(false);
		}
		
		/*HashMap interviewDateExistMap = ram.findInterviewDateId(getApplicantId());
		if(interviewDateExistMap!=null && !interviewDateExistMap.equals("")){
			Collection interviewDateCol = ram.lookUpInterviewDateId(interviewDateExistMap.get("interviewDateId").toString());//load interview date detail rec_interview_date ONLY
			HashMap interviewDateMap = (HashMap)interviewDateCol.iterator().next(); 
			//lblRecStageOfInterview.setText(interviewDateMap.get("interviewStageName").toString());
		
			Date dInterviewDateTime = (Date) interviewDateMap.get("interviewDateTime");
			interviewDate.setDate(dInterviewDateTime);
			startTime.setDate(dInterviewDateTime);
			
			setInterviewDateId(interviewDateMap.get("interviewDateid").toString());
			setHasInterviewDateId(true);
		}*/
		
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
	
	//validation date
	public boolean checkDate(Date date) throws Exception{
		boolean flag=true;
		Date todayDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	String nowDate = sdf.format(todayDate);
    	
		if(sdf.format(date).equals(nowDate) || date.after(todayDate))// today date and onward date will return true
			flag=false;
		
		return flag;
	}
	
	
	public Forward onSubmit(Event evt) {
		Forward forward = super.onSubmit(evt);
		boolean flag=false;
		Application app = Application.getInstance();
		String action = findButtonClicked(evt);
		
		if(action.equals(btnSubmit.getAbsoluteName())){
			try{
				flag = checkDate(interviewDate.getDate());
				if(flag){
					vMsgStartDate.showError(app.getMessage("recruit.vacancy.alert.interviewDateMustGreater"));
					interviewDate.setInvalid(true);
				}else{
					vMsgStartDate.showError("");
				}
			}catch(Exception e){
				//nothing
			}
		}
		
		if(flag || this.isInvalid()){
			 setInvalid(true);
		}	 
		
		return forward;
		
	}
	
	public Forward onValidate(Event evt) {
		String action = findButtonClicked(evt);
		if(action.equals(btnSubmit.getAbsoluteName())){
			Application app = Application.getInstance();
			RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			
			Collection interviewObjCol = new ArrayList();
			InterviewObj interviewObj = new InterviewObj();
			String[] aapplicantId={getApplicantId()};
			Date dinterviewDateTime = setInterviewDate(interviewDate, startTime);
			
			//add Collection to Session, not physical insert to database
			if(isHasInterviewDateId()){ //update data to session
				String[] ainterviewDateId = {getInterviewDateId()};  
				interviewObj.setInterviewDateId(ainterviewDateId); //interviewDateId    
				
				//add session 
				//ram.updateInterviewDateTime(interviewObj); //not yet
				Collection interviewObjColSs = (Collection)evt.getRequest().getSession().getAttribute("sessionInterviewObjCol");
				if(interviewObjColSs!=null && interviewObjColSs.size() > 0){
					for(Iterator ite=interviewObjColSs.iterator(); ite.hasNext();){
						InterviewObj iObj= (InterviewObj)ite.next();
						if(iObj.getInterviewDateId()[0].equals(ainterviewDateId[0])){
							iObj.setInterviewDateTime(dinterviewDateTime);//update the interview date & time in the session
						}
					}
				}
				
			}else{ //insert new data to session
				//generate uuid here
				UuidGenerator uuid = UuidGenerator.getInstance();
				String[] ainterviewDateId = {uuid.getUuid()};
				
				interviewObj.setInterviewDateId(ainterviewDateId); //interviewDateId
				interviewObj.setInterviewDateTime(dinterviewDateTime);// interview Date & Time
				interviewObj.setInterviewStageStatus("0"); //interview Stage Status
				interviewObj.setApplicantId(aapplicantId); // interview applicantId 
				
				//add session
				Collection interviewObjColSs = (Collection)evt.getRequest().getSession().getAttribute("sessionInterviewObjCol");
				if(interviewObjColSs!=null &&  interviewObjColSs.size() > 0){
					interviewObjColSs.add(interviewObj);
				}else{
					interviewObjCol.add(interviewObj);
					evt.getRequest().getSession().setAttribute("sessionInterviewObjCol", interviewObjCol);
				}	
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

	public boolean isHasInterviewDateId() {
		return hasInterviewDateId;
	}

	public void setHasInterviewDateId(boolean hasInterviewDateId) {
		this.hasInterviewDateId = hasInterviewDateId;
	}
}
