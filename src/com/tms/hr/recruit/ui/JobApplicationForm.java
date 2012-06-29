package com.tms.hr.recruit.ui;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import kacang.Application;
import kacang.services.storage.StorageException;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.Form;
import kacang.stdui.Panel;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.ekms.setup.model.SetupModule;
import com.tms.hr.recruit.model.ApplicantObj;
import com.tms.hr.recruit.model.RecruitAppModule;
import com.tms.hr.recruit.model.SendMessage;
import com.tms.hr.recruit.model.VacancyObj;

public class JobApplicationForm extends Form{
	public static final String FORWARD_SUBMIT = "jFormSubmit";
	public static final String FORWARD_ERROR = "error";
	public static final String FORWARD_EMPTYFORM = "emptyForm";
	
	//	for editing status
	private String codeStatusE;
	private String vacancyCodeE;
	private String applicantIdE;
	private String needRedirect;
	
	private CheckBox cbAgreement;
	
	private JobAppPersonal jAppPersonal;
	private JobAppEdu jAppEdu;
	private JobAppAdditional jobAppAdditonal; 
	private JobAppWorkingExpList jAppWorkingExpList;
	private JobAppWorkingExpType jAppWorkingExpType;
	private JobAppWorkingExp jAppWorkingExp;
	private JobAppSkill jAppSkill; 
	private JobAppLanguage jAppLanguage;
	
	private ApplicantObj personalObj;
	
	private Panel btnPanel;
	private Button btnSubmitJobApp;
	private Button btnCancel;
	
	private boolean hasWorkingExp=false;
	
	//editing status
	private boolean hasEditStatus;
	
	public void init(){
		setMethod("POST");
		Application app = Application.getInstance();
	
		setAlign("center");
		if(isHasEditStatus()){
			btnSubmitJobApp = new Button("btnSubmitJobApp", app.getMessage("recruit.general.label.btnUpdateJobApp","Update Job Application Form"));
		}else{
			btnSubmitJobApp = new Button("btnSubmitJobApp", app.getMessage("recruit.general.label.btnSubmitJobApp","Post Job Application Form"));
		}
		
		btnCancel = new Button(Form.CANCEL_FORM_ACTION, app.getMessage("recruit.general.label.cancel","Cancel"));
    	btnSubmitJobApp.setAlign("center");
    	btnCancel.setAlign("center");
    	btnPanel = new Panel("btnPanel");
    	btnPanel.setColspan(2);
    	btnPanel.addChild(btnSubmitJobApp);
    	btnPanel.addChild(btnCancel);
   
    	addChild(btnPanel);
	}
	
	//get the Object flag and flagMsg Status
	public boolean getPassEachValidation(Collection colObj, ApplicantObj obj, boolean flag, boolean flagMsg, String id,String type){
		if(colObj!=null && colObj.size()==0){
			if(obj!=null && obj.getByPassValidation()!=null){
				obj.setByPassValidation(null);
				flagMsg=true; //the main error msg
			}
			
			if(obj==null){ //for 1st time obj is not created
				flagMsg=true; //the main error msg
			}else if(obj.getByPassValidation()==null){
				flagMsg=true; //the main error msg
			}
			
			flag=true;
			
		}else{
			if(obj.getByPassValidation()!=null && obj.getByPassValidation().equals("no") 
					&&  id==null){
				flag=true;
			}
		}
	
		if(type.equals("flagMain"))
			return flag;
		else 
			return flagMsg;
	}
	
	public boolean getPassObjValidation(Collection colObj, ApplicantObj obj, boolean flag, boolean flagMsg, String type){
		if(colObj==null){
			flag=true;
			if(obj==null){
				flagMsg=true; //the main error msg
			}else if(obj!=null && obj.getByPassValidation()!=null){
				flagMsg=true; //the main error msg
			}
		}else{
			if(obj.getByPassValidation()!=null && obj.getByPassValidation().equals("no")){
				flag=true;
			}
		}
		
		if(type.equals("flagMain"))
			return flag;
		else 
			return flagMsg;
	}
	
	public String setTabName(boolean flag, boolean flagMsg, String oriTabName){
		String tabName="";
		if(flag && flagMsg){
			tabName = oriTabName;
		}
		return tabName;
	}
	
	public int setCounter(int error, String tabName){
		if(!tabName.equals(""));
			error++;
		
		return error;
	}
	
	public Forward onSubmit(Event evt) {
		Forward forward = super.onSubmit(evt); 
		int error=1;
		boolean flag = false;
		boolean flagMsg =false;
		boolean statusForm =false;
		String tabName="";
		String action = findButtonClicked(evt);
		
		if(action.equals(btnSubmitJobApp.getAbsoluteName())){
			Application app = Application.getInstance();
			
			//personal form
			String jAppPersonalTabName=app.getMessage("recruit.general.label.tabPersonal");
			jAppPersonal = (JobAppPersonal) Application.getInstance().getWidget(evt.getRequest(), "jobApplication.tab1.panel1.Form");
			flag = getPassObjValidation(jAppPersonal.getPersonalCol(), jAppPersonal.getApplicantObj(), flag, flagMsg, "flagMain");
			flagMsg = getPassObjValidation(jAppPersonal.getPersonalCol(), jAppPersonal.getApplicantObj(), flag, flagMsg, "flagChild");
			
			tabName = setTabName(flag, flagMsg, jAppPersonalTabName); //set the tabname got error
			
			//education form
			String jAppEduTabName=app.getMessage("recruit.general.label.tabEdu");
			jAppEdu = (JobAppEdu) Application.getInstance().getWidget(evt.getRequest(), "jobApplication.tab1.panel2.Form");
			flag = getPassEachValidation(jAppEdu.getEduCol(), jAppEdu.getApplicantObj(), flag, flagMsg, jAppEdu.getEduId(), "flagMain");
			flagMsg = getPassEachValidation(jAppEdu.getEduCol(), jAppEdu.getApplicantObj(), flag, flagMsg, jAppEdu.getEduId(), "flagChild");
			
			if(!tabName.equals("")){
				//nothing
			}else	
				tabName = setTabName(flag, flagMsg, jAppEduTabName); //set the tabname got error
		
			String jAppWorkingExpListTabName=app.getMessage("recruit.general.label.tabWorkingExp");
			jAppWorkingExpList = (JobAppWorkingExpList) Application.getInstance().getWidget(evt.getRequest(), "jobApplication.tab1.panel3.List"); //get class data.
			
			//working experience type
			jAppWorkingExpType = (JobAppWorkingExpType) Application.getInstance().getWidget(evt.getRequest(), "jobApplication.tab1.panel3.Type");
			flag = getPassObjValidation(jAppWorkingExpType.getWorkingExpTypeCol(), jAppWorkingExpType.getApplicantObj(), flag, flagMsg, "flagMain");
			flagMsg = getPassObjValidation(jAppWorkingExpType.getWorkingExpTypeCol(), jAppWorkingExpType.getApplicantObj(), flag, flagMsg, "flagChild");
			
			//working experience form-optional
			if(jAppWorkingExpType.getRsNonFresh().isChecked()){
				hasWorkingExp=true;
				jAppWorkingExp = (JobAppWorkingExp) Application.getInstance().getWidget(evt.getRequest(), "jobApplication.tab1.panel3.Form");
				flag = getPassEachValidation(jAppWorkingExp.getEmpCol(), jAppWorkingExp.getApplicantObj(), flag, flagMsg, jAppWorkingExp.getEmpId(), "flagMain");
				flagMsg = getPassEachValidation(jAppWorkingExp.getEmpCol(), jAppWorkingExp.getApplicantObj(), flag, flagMsg, jAppWorkingExp.getEmpId(), "flagChild");
			}else{
				hasWorkingExp=false;
			}
			
			if(!tabName.equals("")){
				//nothing
			}else	
				tabName = setTabName(flag, flagMsg, jAppWorkingExpListTabName); //set the tabname got error
			
			//skill form
			String jAppSkillTabName=app.getMessage("recruit.general.label.tabSkill");
			jAppSkill = (JobAppSkill) Application.getInstance().getWidget(evt.getRequest(), "jobApplication.tab1.panel4.Form");
			flag = getPassEachValidation(jAppSkill.getSkillCol(), jAppSkill.getApplicantObj(), flag, flagMsg, jAppSkill.getSkillId(), "flagMain");
			flagMsg = getPassEachValidation(jAppSkill.getSkillCol(),jAppSkill.getApplicantObj(), flag, flagMsg, jAppSkill.getSkillId(), "flagChild");
			
			if(!tabName.equals("")){
				//nothing
			}else	
				tabName = setTabName(flag, flagMsg, jAppSkillTabName); //set the tabname got error
			
			//language form-got problem
			String jAppLanguageTabName=app.getMessage("recruit.general.label.tabLanguage");
			jAppLanguage = (JobAppLanguage) Application.getInstance().getWidget(evt.getRequest(), "jobApplication.tab1.panel5.Form");
			flag = getPassEachValidation(jAppLanguage.getLanguageCol(), jAppLanguage.getApplicantObj(), flag, flagMsg, jAppLanguage.getLanguageId(), "flagMain");
			flagMsg = getPassEachValidation(jAppLanguage.getLanguageCol(),jAppLanguage.getApplicantObj(), flag, flagMsg, jAppLanguage.getLanguageId(), "flagChild");
			
			if(!tabName.equals("")){
				//nothing
			}else	
				tabName = setTabName(flag, flagMsg, jAppLanguageTabName); //set the tabname got error
			
			//additional form-optional
			String jobAppAdditonalTabName=app.getMessage("recruit.general.label.tabAdditional");
			jobAppAdditonal = (JobAppAdditional) Application.getInstance().getWidget(evt.getRequest(), "jobApplication.tab1.panel6.Form");
			boolean optionalStep=true;
			if(jobAppAdditonal.getAdditionalCol()!=null && jobAppAdditonal.getAdditionalCol().size() > 0){
				optionalStep=false;
				flag = getPassObjValidation(jobAppAdditonal.getAdditionalCol(), jobAppAdditonal.getApplicantObj(), flag, flagMsg, "flagMain");
				flagMsg = getPassObjValidation(jobAppAdditonal.getAdditionalCol(), jobAppAdditonal.getApplicantObj(), flag, flagMsg, "flagChild");
				if(!tabName.equals("")){
					//nothing
				}else	
					tabName = setTabName(flag, flagMsg, jobAppAdditonalTabName); //set the tabname got error
			}else{
				if(jobAppAdditonal.isInvalid()){
					setInvalid(true);
				}else{
					optionalStep=true;
				}	
			}
			
			//flag validation
			if(flag && flagMsg){
				if(jAppPersonal.isInvalid()){
					setInvalid(false);
					return new Forward(null);
				}else if(jAppEdu.isInvalid()){
					setInvalid(false);
					return new Forward(null);
				}else if(jAppLanguage.isInvalid()){
					setInvalid(false);
					return new Forward(null);
				}
				
				else if(jobAppAdditonal.isInvalid()){
					setInvalid(true);
					//return new Forward(null);
				}
				
				else if(jAppWorkingExpType.isInvalid()){
					setInvalid(true);
					return new Forward(null);
				}else if(hasWorkingExp){
					if(jAppWorkingExp.isInvalid()){
						setInvalid(true);
						return new Forward(null);
					}else{
						setInvalid(true);
						return new Forward(FORWARD_EMPTYFORM+"_"+tabName);
					}	
				}
				else{
					if(optionalStep){
						if(flag && flagMsg){
							setInvalid(true);
							return new Forward(FORWARD_EMPTYFORM+"_"+tabName);
						}
						//nothing
					}else{
						setInvalid(true);
						return new Forward(FORWARD_EMPTYFORM+"_"+tabName);
					}
				}	
			}else{	
				if(flag){
					setInvalid(true);
					return new Forward(null);
				}
			}
			
		}
		return forward;
	}
	
	public Forward onValidate(Event evt) {
		String action = findButtonClicked(evt);
		Application app = Application.getInstance();
		if(action.equals(btnSubmitJobApp.getAbsoluteName())){
			RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			 //auditObj
			 VacancyObj auditObj = new VacancyObj();
			 String actionTaken="";
			
			//inserting personal details 
			Iterator itePersonal=jAppPersonal.getPersonalCol().iterator();
		
			if(itePersonal.hasNext())
				personalObj= (ApplicantObj)itePersonal.next();
			
			//inserting additional details
			ApplicantObj additionalObj= new ApplicantObj();
			if(jobAppAdditonal!=null && jobAppAdditonal.getAdditionalCol()!=null && jobAppAdditonal.getAdditionalCol().size() > 0){
				Iterator iteAdditional = jobAppAdditonal.getAdditionalCol().iterator();
				//ApplicantObj additionalObj=null;
				if(iteAdditional.hasNext())
					additionalObj= (ApplicantObj)iteAdditional.next();
			}else{
				additionalObj.setStorageFile(null);
				additionalObj.setExpectedTypeSalary("---");
				additionalObj.setNegotiable(false);
				additionalObj.setWillingTravel("-");
				additionalObj.setWillingRelocate("-");
				additionalObj.setOwnTransport("-");
			}
			
			//inserting working experience type
			Iterator iteWorkingExpType = jAppWorkingExpType.getWorkingExpTypeCol().iterator();
			ApplicantObj workingExpTypeObj=null;
			if(iteWorkingExpType.hasNext())
				workingExpTypeObj= (ApplicantObj)iteWorkingExpType.next();
			
			//get the value from additionalObj to personalObj
			//personalObj.setResumePath(additionalObj.getResumePath());
			
			//if(additionalObj.getResumePath()!=null && !additionalObj.getResumePath().equals("")){
			if(additionalObj.getStorageFile()!=null && !additionalObj.getStorageFile().equals("")){
				try{
					doUpload(additionalObj);
				}catch(IOException e){
						
				}catch(StorageException e){
						
				}
			}
			
			personalObj.setExpectedTypeSalary(additionalObj.getExpectedTypeSalary());
			personalObj.setExpectedTypeSalaryDesc(additionalObj.getExpectedTypeSalaryDesc());
			personalObj.setExpectedSalary(additionalObj.getExpectedSalary());
			personalObj.setNegotiable(additionalObj.isNegotiable());
			personalObj.setWillingTravel(additionalObj.getWillingTravel());
			personalObj.setWillingRelocate(additionalObj.getWillingRelocate());
			personalObj.setOwnTransport(additionalObj.getOwnTransport());
			
			personalObj.setApplicantBlacklisted(false);
			personalObj.setYearOfWorkingExp(workingExpTypeObj.getYearOfWorkingExp());
			ram.insertJobAppPersonal(personalObj);
			
			//inserting working experience-Optional
			if(workingExpTypeObj.getTtype().equals("t2")){
				ApplicantObj workingExpObj=null;
				for(Iterator iteWorkingExp=jAppWorkingExp.getEmpCol().iterator(); iteWorkingExp.hasNext();){
					workingExpObj = (ApplicantObj) iteWorkingExp.next();
					workingExpObj.setApplicantId(personalObj.getApplicantId());
					ram.insertJobAppWorkingExp(workingExpObj);
				}
			}
			
			//inserting education details
			ApplicantObj eduObj=null;
			for(Iterator iteEdu=jAppEdu.getEduCol().iterator(); iteEdu.hasNext();){
				eduObj=(ApplicantObj)iteEdu.next();
				eduObj.setApplicantId(personalObj.getApplicantId());
				ram.insertJobAppEdu(eduObj);
			}
			
			//inserting skill details
			ApplicantObj skillObj=null;
			for(Iterator iteSkill=jAppSkill.getSkillCol().iterator();iteSkill.hasNext();){
				skillObj=(ApplicantObj)iteSkill.next();
				skillObj.setApplicantId(personalObj.getApplicantId());
				ram.insertJobAppSkill(skillObj);
			}
			
			//inserting language details
			ApplicantObj languageObj=null;
			for(Iterator iteLanguage=jAppLanguage.getLanguageCol().iterator(); iteLanguage.hasNext();){
				languageObj=(ApplicantObj)iteLanguage.next();
				languageObj.setApplicantId(personalObj.getApplicantId());
				ram.insertJobAppLanguage(languageObj);
			}
			
			//inserting applicant status for the 1st time
			ApplicantObj applicantStatusObj = new ApplicantObj();
			UuidGenerator uuid = UuidGenerator.getInstance();
			applicantStatusObj.setApplicantStatusId(uuid.getUuid());
			applicantStatusObj.setVacancyCode(personalObj.getVacancyCode());
			applicantStatusObj.setApplicantId(personalObj.getApplicantId());
			applicantStatusObj.setDateApplied(personalObj.getCreatedDate());
			applicantStatusObj.setApplicantStatus("New");
			ram.insertApplicantStatus(applicantStatusObj);
			
			//update the vacancy total
			VacancyObj vacancyObj = new VacancyObj();
			//Collection vacancyTotalCol=ram.lookUpVacancyTotal(personalObj.getVacancyCode());
			//HashMap map = (HashMap) vacancyTotalCol.iterator().next();
			//int iTotalApplied=Integer.parseInt(map.get("totalApplied").toString()) + 1;
			
			//vacancyObj.setVacancyCode(personalObj.getVacancyCode());
			//vacancyObj.setTotalApplied(iTotalApplied);
			vacancyObj = setVacancyTotal("totalApplied", false);
			ram.updateVacancyTotal(vacancyObj); //updating park
			
			//audit
			actionTaken="Submit Job Applicantion Form";
			auditObj.setAndInsertAudit(personalObj.getVacancyCode(), personalObj.getApplicantId(), actionTaken);
			
			jAppPersonal.resetValuePersonal();
			jAppWorkingExpType.setFieldReset();
			jobAppAdditonal.setFieldReset();
			killAllCollection();
			
			//send mail to related HOD
			try{
				SetupModule setup = (SetupModule) Application.getInstance().getModule(SetupModule.class);
				String siteUrl = setup.get("siteUrl");
			
			SendMessage sm = new SendMessage();
			String applicantEmail = personalObj.getEmail(); // set the applicant email address as 'FORM' 
			
			String subjectTitle="Job Application Form : " + personalObj.getVacancyCode() + "-" + evt.getRequest().getSession().getAttribute("positionTitle").toString();
			String notificationBody = "<p>Applicant Name :<i>" + personalObj.getName() + "</i> has submited Job Applicantion Form to you. </p>" ;
			
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
			sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">" +
					"<a href='"+siteUrl+"/ekms/recruit/applicantListing.jsp?vacancyCodeApply="+personalObj.getVacancyCode()+"'>"+personalObj.getName()+"</a></font></td>\n");
			sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">"+personalObj.getVacancyCode()+"</font></td>\n");
			sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">"+evt.getRequest().getSession().getAttribute("positionTitle").toString()+"</font></td>\n");
			sbTitle.append("  </tr>\n");
				
			sbTitle.append("</table>\n"); 
			
			String hodId=evt.getRequest().getSession().getAttribute("hodId").toString();
			List ToList=new ArrayList();
			ToList.add(hodId);
			
			sm.sendMessage(ToList, subjectTitle, sbTitle.toString(), applicantEmail);
			
			//audit
			actionTaken="Send notification to head of department";
			auditObj.setAndInsertAudit(personalObj.getVacancyCode(), personalObj.getApplicantId(), actionTaken);
			
			//send mail to applicant who submit the job application form
			List ToListApplicant = new ArrayList();
			ToListApplicant.add( personalObj.getEmail());
			String subjectTitleApplicant="Thank you";
			StringBuffer sbBodyTitleApplicant = new StringBuffer();
			String subjectApplicant="Dear "+ personalObj.getName() + ",";
			sbBodyTitleApplicant.append("<font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\" color=\"#000000\">" + subjectApplicant+"");
			sbBodyTitleApplicant.append("<p>Thank you for submitting the Job Application Form. </p>");
			sbBodyTitleApplicant.append("<p>If you would like to view and delete your Job Application Form, " +
					//"Please Click <a href= '"+siteUrl+"/cms/jobApplicationFormEdit.jsp?vacancyCode="+personalObj.getVacancyCode()+"&applicantIds="+personalObj.getApplicantId()+"&codeStatus=edit'>" +
					//"Here.</a><br />" +
					//		"However you are only allowed to edit it within 24 hours start from now. Thank you.</p>");
					"Please Click <a href= '"+siteUrl+"/cms/jobApplicationFormView.jsp?applicantId="+personalObj.getApplicantId()+"'>" +
					"Here.</a></p><p>Thank you.</p>");
			sbBodyTitleApplicant.append("</font>");
			
			sm.sendMessageToApplicant(ToListApplicant, subjectTitleApplicant, sbBodyTitleApplicant.toString(), hodId , hodId);
			
			//audit
			actionTaken="Send notification to Applicant";
			auditObj.setAndInsertAudit(personalObj.getVacancyCode(), personalObj.getApplicantId(), actionTaken);
		    
			}catch (Exception e) {
				Log.getLog(getClass()).error("Error getting site URL", e);
			}
			
			jAppPersonal.init();
			jAppEdu.init();
			jobAppAdditonal.init(); 
			jAppWorkingExpType.init();
			if(jAppWorkingExp!=null){
				jAppWorkingExp.init();
				jAppWorkingExpList.setHidden(true);
			}
			jAppSkill.init(); 
			jAppLanguage.init();	
			init();
		 return new Forward(FORWARD_SUBMIT);
   	 }else
   		 return new Forward(FORWARD_ERROR);
	}
	
	public void onRequest(Event evt) {
		if(evt.getRequest().getSession().getAttribute("JAFtype")!=null && evt.getRequest().getSession().getAttribute("JAFtype").equals("edit")){
			setHasEditStatus(true);
			init();
		}else{
			setHasEditStatus(false);
			init(); 
		}
	}
	
	//setting for updating vacancy total
	public VacancyObj setVacancyTotal(String updateType, boolean toMinus){
		 VacancyObj vacancyObj = new VacancyObj();
		 vacancyObj.setVacancyCode(personalObj.getVacancyCode());
		 vacancyObj.setTotalApplied(personalObj.getVacancyCode(), updateType, toMinus);//update the total applied + 1
		 
		 vacancyObj.setTotalShortlisted(personalObj.getVacancyCode(), updateType, toMinus);//update the total shortlisted +1
		 
		 vacancyObj.setTotalScheduled(personalObj.getVacancyCode(), updateType, toMinus);//update the total scheduled +1
		 
		 //vacancyObj.setTotalScheduleRejected(personalObj.getVacancyCode(), updateType, toMinus);
		 vacancyObj.setTotalReScheduled(personalObj.getVacancyCode(), updateType, toMinus);
		 vacancyObj.setTotalReScheduledRejected(personalObj.getVacancyCode(), updateType, toMinus);
		 vacancyObj.setTotalJobOffered(personalObj.getVacancyCode(), updateType, toMinus);
		 vacancyObj.setTotalInterviewUnsuccessful(personalObj.getVacancyCode(), updateType, toMinus);
		 vacancyObj.setTotalJobAccepted(personalObj.getVacancyCode(), updateType, toMinus);
		 vacancyObj.setTotalJobRejected(personalObj.getVacancyCode(), updateType, toMinus);
		 
		 vacancyObj.setTotalBlackListed(personalObj.getVacancyCode(), updateType, toMinus);//update the total blacklisted +1
		 
		 vacancyObj.setTotalViewed(personalObj.getVacancyCode(), updateType, toMinus);
		 return vacancyObj;
	}
	
	public void killAllCollection(){
		Collection personalCol = jAppPersonal.getPersonalCol();
		Collection workingExpTypeCol = jAppWorkingExpType.getWorkingExpTypeCol();
	
		if(jAppWorkingExp!=null){
			Collection workingExpCol = jAppWorkingExp.getEmpCol();
			workingExpCol.clear();
		}
		
		Collection eduCol = jAppEdu.getEduCol();
		Collection skillCol = jAppSkill.getSkillCol();
		Collection languageCol = jAppLanguage.getLanguageCol();
		
		personalCol.clear();
		
		if(jobAppAdditonal!=null && jobAppAdditonal.getAdditionalCol()!=null && jobAppAdditonal.getAdditionalCol().size() > 0){
			Collection additionalCol = jobAppAdditonal.getAdditionalCol();
			additionalCol.clear();
		}
		
		workingExpTypeCol.clear();
		eduCol.clear();
		skillCol.clear();
		languageCol.clear();
	}
	
	public void doUpload(ApplicantObj obj) throws IOException, StorageException{ //upload the file to file storage
		StorageFile sf;
	    StorageService ss = (StorageService) Application.getInstance().getService(StorageService.class);
	    sf = obj.getStorageFile();
	    
	    String fileName = sf.getName();
        String resumeName = fileName.substring(0, fileName.lastIndexOf("."));
        String fileExt = fileName.substring(fileName.lastIndexOf("."));
	    String newFileName = resumeName + personalObj.getApplicantId() + fileExt;
	    
        personalObj.setResumePath(newFileName);//store to database
        sf.setName(newFileName);
    	sf.setParentDirectoryPath("/recruit/");
    	ss.store(sf);
	}
	
	//getter setter for Editing type
	public String getApplicantIdE() {
		return applicantIdE;
	}

	public void setApplicantIdE(String applicantIdE) {
		this.applicantIdE = applicantIdE;
	}

	public String getCodeStatusE() {
		return codeStatusE;
	}

	public void setCodeStatusE(String codeStatusE) {
		this.codeStatusE = codeStatusE;
	}

	public String getVacancyCodeE() {
		return vacancyCodeE;
	}

	public void setVacancyCodeE(String vacancyCodeE) {
		this.vacancyCodeE = vacancyCodeE;
	}

	public String getNeedRedirect() {
		return needRedirect;
	}

	public void setNeedRedirect(String needRedirect) {
		this.needRedirect = needRedirect;
	}

	public boolean isHasEditStatus() {
		return hasEditStatus;
	}

	public void setHasEditStatus(boolean hasEditStatus) {
		this.hasEditStatus = hasEditStatus;
	}
	
}























