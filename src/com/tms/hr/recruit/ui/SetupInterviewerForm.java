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

public class SetupInterviewerForm extends Form{
	public static final String FORWARD_SUBMIT = "submit";
	public static final String FORWARD_ERROR = "error";
	
	private Object sessionInterviewDateId;
	private PopupInterviewer interviewer;
	
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
    	
    	Label lblInterviewer= new Label("lblInterviewer", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.interviewer") + "*</span>");
    	lblInterviewer.setAlign("right");
    	addChild(lblInterviewer);
    	interviewer = new PopupInterviewer("interviewer");
    	interviewer.init();
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
		sessionInterviewDateId=evt.getRequest().getSession().getAttribute("sessionInterviewDateId");
		init();
	}
	
	public Forward onSubmit(Event evt) {
		Forward forward = super.onSubmit(evt); 
		Application app = Application.getInstance();
		boolean flag = false;
		String action = findButtonClicked(evt);
		
		if(action.equals(btnSubmit.getAbsoluteName())){
			 Collection intervieweeCol = getIntervieweeCol();
			 if(intervieweeCol.size()==0){
				 flag=true;
			 }
			
			 boolean boolsbInterviewer=validateSelectBox(interviewer, "recruit.general.warn.selectInterviewer");
			 if(boolsbInterviewer){
				 interviewer.setInvalid(true);
				 flag=true;
			 }
			 
			 if(flag || this.isInvalid()){
				 setInvalid(true);
			 }
		}
		
		return forward;
	}
	
	public Collection getIntervieweeCol(){
		Collection idCol = (Collection) sessionInterviewDateId;
		Collection intervieweeCol = new ArrayList();
		if(idCol!=null){
			Application app = Application.getInstance();  
			RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			intervieweeCol = ram.findSelectedInterviewee(null, true, 0, -1, idCol, "Scheduled");
		}
		return intervieweeCol;
	}
	
	public Forward onValidate(Event evt) {
		String action = findButtonClicked(evt);
		if(action.equals(btnSubmit.getAbsoluteName())){
			Application app = Application.getInstance();
			RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			Date getDate = new Date();
			InterviewObj interviewObj = new InterviewObj();
		
			Collection intervieweeCol = getIntervieweeCol();
			
			 //auditObj
			VacancyObj auditObj = new VacancyObj();
			String actionTaken="";
			
			
			/*String[] uuidCode=new String[interviewer.getIds().length];
			
			StringBuffer sbf = new StringBuffer();
			for(int i=0; i<interviewer.getIds().length; i++){
					Collection colName = ram.getSecurityUserNameSingle(interviewer.getIds()[i]);
					HashMap map = (HashMap)colName.iterator().next();
					
					if(i>0 && i<interviewer.getIds().length)
						sbf.append(", ");
						
					sbf.append(map.get("username").toString());
					
					//add also the interview remark id
					uuidCode[i]=uuid.getUuid();
			}*/
			
			StringBuffer sbf = new StringBuffer();
			for(int i=0; i<interviewer.getIds().length; i++){
				Collection colName = ram.getSecurityUserNameSingle(interviewer.getIds()[i]);
				HashMap map = (HashMap)colName.iterator().next();
				
				if(i>0 && i<interviewer.getIds().length)
					sbf.append(", ");
					
				sbf.append(map.get("username").toString());
			}
			
			/*UuidGenerator uuid = UuidGenerator.getInstance();//interview remark id
			String[] uuidCode=new String[intervieweeCol.size()];
			for(int i=0; i<intervieweeCol.size(); i++){
					//add also the interview remark id
					uuidCode[i]=uuid.getUuid();
			}*/
			
			String actionTakenInterviewer="set interviewer(s), "+sbf.toString(); //audit # 1
			
			//interviewObj.setInterviewerRemarkId(uuidCode); //interview remark id
			interviewObj.setInterviewerId((String[])interviewer.getIds()); //set interviewer id selected
			interviewObj.setRemark("");
			interviewObj.setCreatedBy(app.getCurrentUser().getId());
			interviewObj.setCreatedDate(getDate);
			interviewObj.setLastUpdatedBy(app.getCurrentUser().getId());
			interviewObj.setLastUpdatedDate(getDate);
			
			ram.insertInterviewer(intervieweeCol, interviewObj);
			
			 //update the applicantStatus
			 ApplicantList appList=new ApplicantList();
			 ApplicantObj applicantObj = new ApplicantObj();
			
			 //lookup vacancy applied by applicant
			 int i =0;
			 String[] applicantIds = new String[intervieweeCol.size()];
			 for(Iterator ite=intervieweeCol.iterator(); ite.hasNext();){
				 HashMap map = (HashMap) ite.next();
				 applicantIds[i] = map.get("applicantId").toString();
				 i++;
			 }
			 
			 //8-1-07-closing the status - 'Scheduled & Interviewer(s) added'
			 /* Collection lookUpVacancyCol = ram.loadShortlisted(applicantIds, "Scheduled");
			for(Iterator ite=lookUpVacancyCol.iterator(); ite.hasNext();){
				ApplicantObj appObj= (ApplicantObj)ite.next();	
				applicantObj = appList.setApplicantStatus(appObj.getVacancyCode(), appObj.getApplicantId(), "Scheduled & Interviewer(s) added");
				ram.updateApplicantStatus(applicantObj);//update the applicant status
				
				//audit
				actionTaken="set applicant status to Scheduled & Interviewer(s) added."; //audit # 2
				auditObj.setAndInsertAudit(appObj.getVacancyCode(), appObj.getApplicantId(), actionTakenInterviewer);
				auditObj.setAndInsertAudit(appObj.getVacancyCode(), appObj.getApplicantId(), actionTaken);
			 }*/
			
			//send mail to interviewer
			 DateFormat dmyDateFmt = new SimpleDateFormat(app.getProperty("globalDatetimeLong")); 
			SendMessage sm = new SendMessage();
			List interviewerIdList=new ArrayList();
			for(int j=0;j < interviewer.getIds().length; j++){
				interviewerIdList.add(interviewer.getIds()[j]);
			}
			
			try{
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
			
			SetupModule setup = (SetupModule) Application.getInstance().getModule(SetupModule.class);
			String siteUrl = setup.get("siteUrl");
			
			for(Iterator ite = intervieweeCol.iterator(); ite.hasNext();){
			HashMap map = (HashMap) ite.next();
		
			sbTitle.append("  <tr> \n");
			sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">" + dmyDateFmt.format(map.get("interviewDateTime")) + "</font></td>\n");
			sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">" + 
					"<a href='"+siteUrl+"/ekms/recruit/interviewerRemarkForm.jsp?applicantId="+map.get("applicantId").toString()+
					"&interviewDateId="+map.get("interviewDateId").toString()+"&vacancyCode="+map.get("vacancyCode").toString()+"&flag=yes'>" + 
					map.get("name").toString() + "</a></font></td>\n");
			sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">" + map.get("vacancyCode").toString() + "</font></td>\n");
			sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">" + map.get("positionDesc").toString() + "</font></td>\n");
			sbTitle.append("  </tr>\n");
			
			//audit
			actionTaken="Send eMail to interviewer(s), " + sbf.toString() ; //audit #3
			auditObj.setAndInsertAudit(map.get("vacancyCode").toString(), map.get("applicantId").toString(), actionTaken);
			}
			
			String directLink="<a href='"+siteUrl+"/ekms/recruit/interviewerRemarkList.jsp'>"+
				app.getMessage("recruit.menu.label.clickToView") + " " + app.getMessage("recruit.menu.label.interviewerRemarkListing")+"</a>";
			sbTitle.append("  <tr valign=center> \n");
			sbTitle.append("    <td bgcolor=\"#FFFFFF\" colspan=\"4\"><b><font size=\"2\" face=\"Arial, Helvetica, sans-serif\" color=\"#000000\">" + directLink + "</font></b></td>\n");
			sbTitle.append("  </tr>\n");
			sbTitle.append("</table>\n");
			
			sm.sendMessage(interviewerIdList, subjectTitle, sbTitle.toString(), ""); 
			
			}catch (Exception e) {
				Log.getLog(getClass()).error("Error getting site URL", e);
			}
			
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
	
}
