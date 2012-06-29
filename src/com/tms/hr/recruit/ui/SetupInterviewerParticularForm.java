package com.tms.hr.recruit.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.FormField;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.validator.ValidatorMessage;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.ekms.setup.model.SetupModule;
import com.tms.hr.recruit.model.ApplicantObj;
import com.tms.hr.recruit.model.InterviewObj;
import com.tms.hr.recruit.model.RecruitAppModule;
import com.tms.hr.recruit.model.SendMessage;
import com.tms.hr.recruit.model.VacancyObj;

public class SetupInterviewerParticularForm extends Form{
	public static final String FORWARD_SUBMIT = "submit";
	public static final String FORWARD_ERROR = "error";
	
	private Object sessionInterviewDateId;
	private PopupInterviewer interviewer;
	
	private String vacancyCode;
	private String applicantId;
	private String interviewDateIdd;
	
	private Panel btnPanel;
	private Button btnSubmit;
	private Button btnCancel;
	
	private ValidatorMessage vMsgHasInterviewer;
	
	private Label lblRecIntervieweeSOI;
	private Label lblRecIntervieweeDT;
	
	public void init(){
		initForm();
	}

	public void initForm(){
		setColumns(2);
    	setMethod("POST");
    	//removeChildren();
    	Application app = Application.getInstance();
    	
    	Label lblIntervieweeStageOfInterview = new Label("lblVacancyCode", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.IntervieweeStageOfInterview") + "</span>");
    	lblIntervieweeStageOfInterview.setAlign("right");
    	lblRecIntervieweeSOI = new Label("lblRecIntervieweeSOI","");
		addChild(lblIntervieweeStageOfInterview);
		addChild(lblRecIntervieweeSOI);
		
		Label lblIntervieweeDT = new Label("lblIntervieweeDT", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.interviewDateTimes") + "</span>");
		lblIntervieweeDT.setAlign("right");
		lblRecIntervieweeDT = new Label("lblRecIntervieweeDT","");
		addChild(lblIntervieweeDT);
		addChild(lblRecIntervieweeDT);
    	
    	Label lblInterviewer= new Label("lblInterviewer", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.interviewer") + "*</span>");
    	lblInterviewer.setAlign("right");
    	addChild(lblInterviewer);
    	interviewer = new PopupInterviewer("interviewer");
    	vMsgHasInterviewer = new ValidatorMessage("vMsgHasInterviewer");
    	interviewer.init();
    	interviewer.addChild(vMsgHasInterviewer);
    	addChild(interviewer);
    	
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
		sessionInterviewDateId=evt.getRequest().getSession().getAttribute("sessionInterviewDateId");
		String interviewDateIds = sessionInterviewDateId.toString();
		setInterviewDateIdd(interviewDateIds);
		populateDate(interviewDateIds);
	}
	
	public void populateDate(String interviewDateId){
		Application app = Application.getInstance();
		RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
		DateFormat dmyDateFmt = new SimpleDateFormat(app.getProperty("globalDatetimeLong")); 
		
		Collection col = ram.lookUpInterviewDateId(interviewDateId);
		HashMap map = (HashMap)col.iterator().next();

		lblRecIntervieweeDT.setText(dmyDateFmt.format(map.get("interviewDateTime")));
		lblRecIntervieweeSOI.setText(map.get("interviewStageName").toString());		
	}
	
	public Forward onSubmit(Event evt) {
		Forward forward = super.onSubmit(evt); 
		Application app = Application.getInstance();
		boolean flag = false;
		String action = findButtonClicked(evt);
		
		if(action.equals(btnSubmit.getAbsoluteName())){
			 boolean boolsbInterviewer=validateSelectBox(interviewer, "recruit.general.warn.selectInterviewer");
			 if(boolsbInterviewer){
				 interviewer.setInvalid(true);
				 flag=true;
			 }
			 
			 //validation duplicate same interviewer
			 RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			 String interviewDateIdSs = evt.getRequest().getSession().getAttribute("sessionInterviewDateId").toString();
			 Collection interviewerCol = ram.lookUpInterviewer(interviewDateIdSs);
			 
			 for(Iterator ite=interviewerCol.iterator(); ite.hasNext();){
				HashMap map= (HashMap)ite.next();
			    for(int i=0; i<interviewer.getIds().length; i++){
					 if(interviewer.getIds()[i].equals(map.get("interviewerId"))){
						vMsgHasInterviewer.showError(app.getMessage("recruit.vacancy.alert.interviewerAlreadyExist") , true); //not working
						interviewer.setInvalid(true);
						flag=true; 
					 }	
				 }
			 }
			 
			/* if(!flag)
				 vMsgHasInterviewer.showError("");*/
				 
			 if(flag || this.isInvalid()){
				 setInvalid(true);
			 }
		}
		
		return forward;
	}
	
	public Forward onValidate(Event evt) {
		String action = findButtonClicked(evt);
		if(action.equals(btnSubmit.getAbsoluteName())){
			Application app = Application.getInstance();
			RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			Date getDate = new Date();
			InterviewObj interviewObj = new InterviewObj();
		
			 //auditObj
			 VacancyObj auditObj = new VacancyObj();
			 String actionTaken="";
			
			String[] interviewDateId =  {getSessionInterviewDateId().toString()};
			String[] applicantIds = {getApplicantId()};  
			
			Collection AppCol = ram.loadApplicantPersonal(applicantIds[0]);
			HashMap vacancyMap = (HashMap) AppCol.iterator().next();
			String applicantName = vacancyMap.get("name").toString();
			String positionApplied = vacancyMap.get("positionDesc").toString();
			
			Collection dateTimeCol = ram.lookUpInterviewDateId(interviewDateId[0]);
			HashMap dateTimeMap = (HashMap)dateTimeCol.iterator().next();
			//String interviewDateTimes = dateTimeMap.get("interviewDateTime").toString();
			
			setVacancyCode(vacancyMap.get("vacancyCode").toString());
			interviewObj.setVacancyCode(vacancyMap.get("vacancyCode").toString());
			
			UuidGenerator uuid = UuidGenerator.getInstance();//interview remark id
			String[] uuidCode=new String[interviewer.getIds().length];
			for(int i=0; i<interviewer.getIds().length; i++){
				//add also the interview remark id
				uuidCode[i]=uuid.getUuid();
			}
			interviewObj.setInterviewerRemarkId(uuidCode);
			
			interviewObj.setInterviewDateId(interviewDateId);
			interviewObj.setApplicantId(applicantIds);
			interviewObj.setInterviewerId((String[])interviewer.getIds()); //set interviewer id selected
			interviewObj.setRemark("");
			interviewObj.setCreatedBy(app.getCurrentUser().getId());
			interviewObj.setCreatedDate(getDate);
			interviewObj.setLastUpdatedBy(app.getCurrentUser().getId());
			interviewObj.setLastUpdatedDate(getDate);
			
			ram.insertInterviewerRemark(interviewObj);
			
			//audit
			try{
			StringBuffer sbf = new StringBuffer();
			for(int i=0; i<interviewer.getIds().length; i++){
				Collection colName = ram.getSecurityUserNameSingle(interviewer.getIds()[i]);
				HashMap map = (HashMap)colName.iterator().next();
				
				if(i>0 && i<interviewer.getIds().length)
					sbf.append(", ");
					
				sbf.append(map.get("username").toString());
			}
			String actionTakenInterviewer="set interviewer(s), "+sbf.toString(); //audit # 1
			
			//send mail to interviewer
			DateFormat dmyDateFmt = new SimpleDateFormat(app.getProperty("globalDatetimeLong")); 
			SendMessage sm = new SendMessage();
			List interviewerIdList=new ArrayList();
			for(int j=0;j < interviewer.getIds().length; j++){
				interviewerIdList.add(interviewer.getIds()[j]);
			}
			
			SetupModule setup = (SetupModule) Application.getInstance().getModule(SetupModule.class);
			String siteUrl = setup.get("siteUrl");
			
			String subjectTitle="You has been selected as Interviewer";
			String title="The following are the Interview Date & Time and Interviewee(s) details";
			
			StringBuffer sbTitle = new StringBuffer();
			sbTitle.append("<table cellspacing=0 cellpadding=5 width=\"100%\" border=0>\n");
			sbTitle.append("  <tr valign=center> \n");
			sbTitle.append("    <td bgcolor=\"#FFFFFF\" colspan=\"4\"><b><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\" color=\"#000000\">" + title + "</font></b></td>\n");
			sbTitle.append("  </tr>\n");
	        
			sbTitle.append("  <tr> \n");
			sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">Interview Date & Times</font></td>\n");
			sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">Interviewee(s)</font></td>\n");
			sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">Vacancy Code</font></td>\n");
			sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">Position</font></td>\n");
			sbTitle.append("  </tr>\n");
			
			sbTitle.append("  <tr> \n");
			sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">" + dmyDateFmt.format(dateTimeMap.get("interviewDateTime")) + "</font></td>\n");
			
			sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">" + 
					"<a href='"+siteUrl+"/ekms/recruit/interviewerRemarkForm.jsp?applicantId="+getApplicantId()+
					"&interviewDateId="+getInterviewDateIdd()+"&vacancyCode="+getVacancyCode() +"&flag=yes'>" + 
					applicantName + "</a></font></td>\n");
			
			//sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">" + applicantName + "</font></td>\n");
			
			sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">" + getVacancyCode() + "</font></td>\n");
			sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">" + positionApplied + "</font></td>\n");
			sbTitle.append("  </tr>\n");
			
			String directLink="<a href='"+siteUrl+"/ekms/recruit/interviewerRemarkList.jsp'>"+
			 	app.getMessage("recruit.menu.label.clickToView") + " " + app.getMessage("recruit.menu.label.interviewerRemarkListing")+"</a>";
			sbTitle.append("  <tr valign=center> \n");
			sbTitle.append("    <td bgcolor=\"#FFFFFF\" colspan=\"4\"><b><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\" color=\"#000000\">" + directLink + "</font></b></td>\n");
			sbTitle.append("  </tr>\n");
			
			//audit
			actionTaken="Send eMail to interviewer(s), " + sbf.toString() ; //audit #3
			auditObj.setAndInsertAudit(getVacancyCode(), getApplicantId(), actionTaken);
			
			sbTitle.append("</table>\n");
			
			sm.sendMessage(interviewerIdList, subjectTitle, sbTitle.toString(), ""); 
			
			}catch (Exception e) {
				Log.getLog(getClass()).error("Error getting site URL", e);
			}
			
			init();
			return new Forward(FORWARD_SUBMIT);
   	 	}else
   	 		return new Forward(FORWARD_ERROR);
	}
	
	//	validate empty listbox
	public boolean validateSelectBox(FormField ff, String msg){
        Application app = Application.getInstance();
		boolean key=false;	
        	List list = (List) ff.getValue();
	        if(list.size() == 0){
        		ff.setMessage(app.getMessage(msg));
	        	key=true;
	        }
	    return key;    
	}

	public Object getSessionInterviewDateId() {
		return sessionInterviewDateId;
	}

	public void setSessionInterviewDateId(Object sessionInterviewDateId) {
		this.sessionInterviewDateId = sessionInterviewDateId;
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

	public String getInterviewDateIdd() {
		return interviewDateIdd;
	}

	public void setInterviewDateIdd(String interviewDateIdd) {
		this.interviewDateIdd = interviewDateIdd;
	}
	
}
