package com.tms.hr.recruit.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.ButtonGroup;
import kacang.stdui.CheckBox;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.Radio;
import kacang.stdui.TextBox;
import kacang.stdui.validator.ValidatorMessage;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.ekms.setup.model.SetupModule;
import com.tms.hr.recruit.model.ApplicantObj;
import com.tms.hr.recruit.model.RecruitAppModule;
import com.tms.hr.recruit.model.RecruitModule;
import com.tms.hr.recruit.model.SendMessage;
import com.tms.hr.recruit.model.VacancyObj;

public class JobOfferForm extends Form{
	public static final String FORWARD_SUBMIT = "submit";
	public static final String FORWARD_ERROR = "error";
	
	private String applicantId;
	private String name;
	private String vacancyCode;
	private String positionDesc;
	private String hodId;
	
	private Label lblRecDateOffered;
	private Label lblRecApplicant;
	private Label lblRecVacancyCode;
	private Label lblRecPostion;
	private Label lblRecCountry;
	private Label lblRecDepartment;
	
	private CheckBox cbOfferLetterSent;
	/*private Radio radioYes;
	private Radio radioNo;
	private ButtonGroup radioOfferLetterSent;*/
	
	private Radio radioUnderConsideration;
	private Radio radioAccept;
	private Radio radioReject;
	private ButtonGroup radioOfferLetterStatus;
	
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
    	
    	Label lblDateOffered= new Label("lblDateOffered", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.dateOffered") + "</span>");
    	lblDateOffered.setAlign("right");
    	addChild(lblDateOffered);
    	lblRecDateOffered= new Label("lblRecDateOffered", "");
    	addChild(lblRecDateOffered);
    	
    	Label lblPostion= new Label("lblPostion", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.position") + "</span>");
    	lblPostion.setAlign("right");
    	addChild(lblPostion);
    	lblRecPostion= new Label("lblRecPostion", "");
    	addChild(lblRecPostion);
    	
    	Label lblCountry= new Label("lblCountry", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.country") + "</span>");
    	lblCountry.setAlign("right");
    	addChild(lblCountry);
    	lblRecCountry= new Label("lblRecCountry", "");
    	addChild(lblRecCountry);
    	
    	Label lblDepartment= new Label("lblDepartment", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.department") + "</span>");
    	lblDepartment.setAlign("right");
    	addChild(lblDepartment);
    	lblRecDepartment= new Label("lblRecDepartment", "");
    	addChild(lblRecDepartment);
    	
    	Label lblOfferLetterSent= new Label("lblOfferLetterSent", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.OfferLetterSent") + "</span>");
    	lblOfferLetterSent.setAlign("right");
    	addChild(lblOfferLetterSent);
    	
    	cbOfferLetterSent = new CheckBox("cbOfferLetterSent");
    	cbOfferLetterSent.setOnClick("show_row(2);");
    	//cbOfferLetterSent.setName("Called");
    	
    	//cbVacancyTemp.setOnClick("document.getElementById('info1').style.visibility = this.checked ? 'visible' : 'hidden',  document.getElementById('info2').style.visibility = this.checked ? 'visible' : 'hidden'");
    	addChild(cbOfferLetterSent);
    	
    	Label lblOfferLetterStatus= new Label("lblOfferLetterStatus", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.OfferLetterStatus") + "</span>");
    	lblOfferLetterStatus.setAlign("right");
    	addChild(lblOfferLetterStatus);
    	
    	radioUnderConsideration= new Radio("Under Consideration", app.getMessage("recruit.general.label.underConsideration"));
    	radioAccept= new Radio("Job Accepted", app.getMessage("recruit.general.label.accept"));
    	radioReject = new Radio("Job Rejected", app.getMessage("recruit.general.label.reject"));
    	
    	radioOfferLetterStatus = new ButtonGroup("radioOfferLetterStatus");
    	radioOfferLetterStatus.setType(ButtonGroup.RADIO_TYPE);
    	radioOfferLetterStatus.setColumns(3);
    	radioOfferLetterStatus.addButton(radioUnderConsideration);
    	radioOfferLetterStatus.addButton(radioAccept);
    	radioOfferLetterStatus.addButton(radioReject);
    	addChild(radioOfferLetterStatus);
    	
    	
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
		Application app = Application.getInstance();
		RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
		
		Collection col = ram.lookUpApplicantOfferedStatus(getApplicantId());
		HashMap map =(HashMap) col.iterator().next();
		
		lblRecDateOffered.setText(map.get("jobOfferDate").toString());
		
		lblRecApplicant.setText("<a href='#' onClick=\"NewWindow('jobApplicationFormViewPopUp.jsp?applicantId="+map.get("applicantId").toString()+"','applicantName','500','670','yes'); return false; \">"+map.get("name").toString()+"</a>");
		lblRecVacancyCode.setText("<a href='#' onClick=\"NewWindow('vacancyDetail.jsp?vacancyCode="+map.get("vacancyCode").toString()+"','vacancyDetails','500','500','yes'); return false; \">"+map.get("vacancyCode").toString()+"</a>");
		
		//lblRecApplicant.setText("<a href='#' onClick=javascript:window.open('jobApplicationFormViewPopUp.jsp?applicantId="+map.get("applicantId").toString()+"','blank','toolbar=no,width=500,height=670,scrollbars=yes'); return false; >"+map.get("name").toString()+"</a>")	;
		//lblRecVacancyCode.setText("<a href='#' onClick=javascript:window.open('vacancyDetail.jsp?vacancyCode="+map.get("vacancyCode").toString()+"','blank','toolbar=no,width=500,height=500,scrollbars=yes'); return false; >"+map.get("vacancyCode").toString()+"</a>");
		
		//lblRecVacancyCode.setText(map.get("vacancyCode").toString());
		lblRecPostion.setText(map.get("positionDesc").toString());
		lblRecCountry.setText(map.get("countryDesc").toString());
		lblRecDepartment.setText(map.get("departmentDesc").toString());
		
		setVacancyCode(map.get("vacancyCode").toString());
		setPositionDesc(map.get("positionDesc").toString());
		setName(map.get("name").toString());
		setHodId(map.get("createdBy").toString());
		
		if(map.get("jobOfferLetterSent").equals("0")){
			cbOfferLetterSent.setChecked(false);
		}else{
			cbOfferLetterSent.setChecked(true);
		}
		
		if(!map.get("applicantStatus").equals("Job Accepted") || !map.get("applicantStatus").equals("Job Rejected"))
			radioUnderConsideration.setChecked(true);
			
		if(map.get("applicantStatus").equals("Job Accepted"))
			radioAccept.setChecked(true);
		
		if(map.get("applicantStatus").equals("Job Rejected"))
			radioReject.setChecked(true);
		
		if(map.get("jobOfferAdminRemark")!=null && !map.get("jobOfferAdminRemark").equals(""))
			tbRemark.setValue(map.get("jobOfferAdminRemark"));
		
	}
	
	/*public Forward onSubmit(Event evt) {
		Forward forward = super.onSubmit(evt);
		boolean flag=false;
		
		if(!cbOfferLetterSent.isChecked()){
			if(radioAccept.isChecked() || radioReject.isChecked()){
				flag=true;
			}
		}
			
		if(flag || this.isInvalid()){
			 setInvalid(true);
			 return new Forward("offerletterNotSent");
		}
		
		return forward;
	}	*/
	
	public Forward onValidate(Event evt) {
		String action = findButtonClicked(evt);
		if(action.equals(btnSubmit.getAbsoluteName())){
			String updateStatusType="";
			String statusName="";
			boolean updateStatus=false;
			boolean needSendMail=false;
			Application app = Application.getInstance();
			RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
			
			 //auditObj
			 VacancyObj auditObj = new VacancyObj();
			 String actionTaken="";
			
			ApplicantObj applicantObj = new ApplicantObj();
			
			if(cbOfferLetterSent.isChecked()){
				applicantObj.setJobOfferLetterSent(true);
			}else{	
				applicantObj.setJobOfferLetterSent(false);
			}
			
			if(!cbOfferLetterSent.isChecked()){
				radioUnderConsideration.setChecked(true);
				radioAccept.setChecked(false);
				radioReject.setChecked(false);
			}
			
			if(radioUnderConsideration.isChecked()){
				updateStatus=false;
				needSendMail=false;
			}
			
			if(radioAccept.isChecked()){
				applicantObj.setApplicantStatus("Job Accepted");
				updateStatusType="totalJobAccepted";
				updateStatus=true;
				needSendMail=true;
				statusName="Accepted";
			}
			
			if(radioReject.isChecked()){
				applicantObj.setApplicantStatus("Job Rejected");
				updateStatusType="totalJobRejected";
				updateStatus=true;
				needSendMail=true;
				statusName="Rejected";
			}
			
			applicantObj.setApplicantId(getApplicantId());
			applicantObj.setJobOfferAdminRemark((String) tbRemark.getValue());
			ram.updateApplicantOfferStatusResult(applicantObj, updateStatus);
			
			//update the vacancy Total
			if(radioAccept.isChecked() || radioReject.isChecked()){
				 VacancyObj vacancyObj = new VacancyObj();
				 ApplicantList appList = new ApplicantList();
				 
				 vacancyObj = appList.setVacancyTotal(getVacancyCode(), updateStatusType, false);
				 ram.updateVacancyTotal(vacancyObj);
			}
			
			//update the vacancy Detail for the no of position offered
			if(radioAccept.isChecked()){ 
				Collection vacancyCol = rm.findSpecificVacancy(getVacancyCode());
				HashMap map = (HashMap) vacancyCol.iterator().next();
				int total;
				
				if(map.get("noOfPositionOffered")!=null && !map.get("noOfPositionOffered").equals("")){
					total = Integer.parseInt(map.get("noOfPositionOffered").toString());
					total+=1;
				}else
					total=1;
				
				VacancyObj vacancyObj = new VacancyObj();
				vacancyObj.setVacancyCode(getVacancyCode());
				vacancyObj.setNoOfPositionOffered(total);
				
				rm.updateSpecificVacancy(vacancyObj);
			}
			
			//send mail
			if(needSendMail){
				SendMessage sm = new SendMessage();
				
				String subjectTitle="Answer From Applicant Offered";
				
				StringBuffer sbTitle = new StringBuffer();
				sbTitle.append("<table cellspacing=0 cellpadding=5 width=\"100%\" border=0>\n");
				sbTitle.append("  <tr valign=center> \n");
				sbTitle.append("    <td bgcolor=\"#FFFFFF\" colspan=\"4\"><b><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\" color=\"#000000\">" + subjectTitle + "</font></b></td>\n");
				sbTitle.append("  </tr>\n");
		        
				sbTitle.append("  <tr> \n");
				sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">Applicant</font></td>\n");
				sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">Vacancy Code</font></td>\n");
				sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">Position</font></td>\n");
				sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">Result</font></td>\n");
				sbTitle.append("  </tr>\n");
				
				if(radioAccept.isChecked()){
					try{
						SetupModule setup = (SetupModule) Application.getInstance().getModule(SetupModule.class);
						String siteUrl = setup.get("siteUrl");
						
					sbTitle.append("  <tr> \n");
					
					sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">" +
							"<a href='"+siteUrl+"/ekms/recruit/jobOfferFormView.jsp?applicantId="+getApplicantId()+"'>"+getName()+"</a></font></td>\n");
		
					sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">"+getVacancyCode()+"</font></td>\n");
					sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">"+getPositionDesc()+"</font></td>\n");
					sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">"+statusName+"</font></td>\n");
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
					sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">"+getName()+"</font></td>\n");
					sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">"+getVacancyCode()+"</font></td>\n");
					sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">"+getPositionDesc()+"</font></td>\n");
					sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">"+statusName+"</font></td>\n");
					sbTitle.append("  </tr>\n");
				}
				
				sbTitle.append("</table>\n"); 
				
				List toList = new ArrayList();
				toList.add(getHodId());
				sm.sendMessage(toList, subjectTitle, sbTitle.toString(), "");
				
				 //audit
				 actionTaken="Send eMail to Head Of department";
				 auditObj.setAndInsertAudit(getVacancyCode(), getApplicantId(), actionTaken);
			}
			
			return new Forward(FORWARD_SUBMIT);
   	 	}else
   	 		return new Forward(FORWARD_ERROR);
	}	
	
	public String getDefaultTemplate() {
		return "recruit/jobOfferForm";
	}
	
	
	//getter setter
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPositionDesc() {
		return positionDesc;
	}

	public void setPositionDesc(String positionDesc) {
		this.positionDesc = positionDesc;
	}

	public String getHodId() {
		return hodId;
	}

	public void setHodId(String hodId) {
		this.hodId = hodId;
	}

	public Button getBtnCancel() {
		return btnCancel;
	}

	public void setBtnCancel(Button btnCancel) {
		this.btnCancel = btnCancel;
	}

	public Panel getBtnPanel() {
		return btnPanel;
	}

	public void setBtnPanel(Panel btnPanel) {
		this.btnPanel = btnPanel;
	}

	public Button getBtnSubmit() {
		return btnSubmit;
	}

	public void setBtnSubmit(Button btnSubmit) {
		this.btnSubmit = btnSubmit;
	}

	public CheckBox getCbOfferLetterSent() {
		return cbOfferLetterSent;
	}

	public void setCbOfferLetterSent(CheckBox cbOfferLetterSent) {
		this.cbOfferLetterSent = cbOfferLetterSent;
	}

	public Label getLblRecApplicant() {
		return lblRecApplicant;
	}

	public void setLblRecApplicant(Label lblRecApplicant) {
		this.lblRecApplicant = lblRecApplicant;
	}

	public Label getLblRecCountry() {
		return lblRecCountry;
	}

	public void setLblRecCountry(Label lblRecCountry) {
		this.lblRecCountry = lblRecCountry;
	}

	public Label getLblRecDateOffered() {
		return lblRecDateOffered;
	}

	public void setLblRecDateOffered(Label lblRecDateOffered) {
		this.lblRecDateOffered = lblRecDateOffered;
	}

	public Label getLblRecDepartment() {
		return lblRecDepartment;
	}

	public void setLblRecDepartment(Label lblRecDepartment) {
		this.lblRecDepartment = lblRecDepartment;
	}

	public Label getLblRecPostion() {
		return lblRecPostion;
	}

	public void setLblRecPostion(Label lblRecPostion) {
		this.lblRecPostion = lblRecPostion;
	}

	public Label getLblRecVacancyCode() {
		return lblRecVacancyCode;
	}

	public void setLblRecVacancyCode(Label lblRecVacancyCode) {
		this.lblRecVacancyCode = lblRecVacancyCode;
	}

	public Radio getRadioAccept() {
		return radioAccept;
	}

	public void setRadioAccept(Radio radioAccept) {
		this.radioAccept = radioAccept;
	}

	public ButtonGroup getRadioOfferLetterStatus() {
		return radioOfferLetterStatus;
	}

	public void setRadioOfferLetterStatus(ButtonGroup radioOfferLetterStatus) {
		this.radioOfferLetterStatus = radioOfferLetterStatus;
	}

	public Radio getRadioReject() {
		return radioReject;
	}

	public void setRadioReject(Radio radioReject) {
		this.radioReject = radioReject;
	}

	public Radio getRadioUnderConsideration() {
		return radioUnderConsideration;
	}

	public void setRadioUnderConsideration(Radio radioUnderConsideration) {
		this.radioUnderConsideration = radioUnderConsideration;
	}

	public TextBox getTbRemark() {
		return tbRemark;
	}

	public void setTbRemark(TextBox tbRemark) {
		this.tbRemark = tbRemark;
	}	
	
}
