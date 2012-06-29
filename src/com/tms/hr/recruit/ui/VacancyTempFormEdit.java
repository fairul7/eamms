package com.tms.hr.recruit.ui;

import java.util.Date;

import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.hr.recruit.model.RecruitModule;
import com.tms.hr.recruit.model.VacancyObj;

public class VacancyTempFormEdit extends VacancyTempFormDefault{
    public static final String FORWARD_UPDATED = "updated";
    public static final String FORWARD_ERROR = "error";
    
	private String vacancyTempCode;
	
	private Label lbVacancyTempCode;
    private Panel btnPanel;
    private Button btnSubmit;
    private Button btnCancel;
	
	public void init(){
		setColumns(2);
    	setMethod("POST");

		Application app = Application.getInstance();
		Label lblTemplateName = new Label("lblTemplateName", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.tempName") + "</span>");
		lblTemplateName.setAlign("right");
		lbVacancyTempCode = new Label("lbVacancyTempCode","");
		addChild(lblTemplateName);
		addChild(lbVacancyTempCode);
		
		//calling VacancyTempFormDefault
		super.init();
		
		btnSubmit = new Button("btnSubmit", app.getMessage("recruit.general.label.submit","Submit"));
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
		populateForm();
	}
	
	public void populateForm(){
		if(getVacancyTempCode()!=null){
			Application app = Application.getInstance();
			RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
			try{
				VacancyObj vacancyObj = rm.loadVacancyTemp(getVacancyTempCode());
				lbVacancyTempCode.setText(vacancyObj.getVacancyTempCode());
				sbPosition.setSelectedOption(vacancyObj.getPositionId());
				sbDepartment.setSelectedOption(vacancyObj.getDepartmentId());
				sbCountry.setSelectedOption(vacancyObj.getCountryId());
				tbJobRespon.setValue(vacancyObj.getResponsibilities());
				tbJobRequire.setValue(vacancyObj.getRequirements());
				
			}catch(DataObjectNotFoundException e){
				Log.getLog(getClass()).error("Module " + getVacancyTempCode() + " not found");
				init();
			}
		}	
	}
	
	//onSubmit
	public Forward onSubmit(Event evt) {
		Forward forward = super.onSubmit(evt); 
		String action = findButtonClicked(evt);
		if(action.equals(btnSubmit.getAbsoluteName())){
			validate(sbPosition, "sb", null);
			validate(sbDepartment, "sb", null);
			validate(sbCountry, "sb", null);
			validate(tbJobRespon, "tb", null);
			validate(tbJobRequire, "tb", null);
		}
		return forward;
	}
	
	//onValidate
	public Forward onValidate(Event evt){
		String action = findButtonClicked(evt);
		if(action.equals(btnSubmit.getAbsoluteName())){
			Application app = Application.getInstance();
			RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
			
			//auditObj
			VacancyObj auditObj = new VacancyObj();
			String actionTaken="";
			
			//getDate
			Date getDate = new Date();
			
			//get the selectbox value
   		 	String selectedPosition = (String) sbPosition.getSelectedOptions().keySet().iterator().next(); 
   		 	String selectedCountry = (String) sbCountry.getSelectedOptions().keySet().iterator().next();
   		 	String selectedDept = (String) sbDepartment.getSelectedOptions().keySet().iterator().next();
   		 	
   		 	VacancyObj vacancyTempObj = new VacancyObj();
   		 	vacancyTempObj.setVacancyTempCode(vacancyTempCode);
   		 	vacancyTempObj.setPositionId(selectedPosition);
   		 	vacancyTempObj.setCountryId(selectedCountry);
   		 	vacancyTempObj.setDepartmentId(selectedDept);
   		 	vacancyTempObj.setResponsibilities((String)tbJobRespon.getValue());
   		 	vacancyTempObj.setRequirements((String)tbJobRequire.getValue());
   		 	vacancyTempObj.setLastUpdatedBy(app.getCurrentUser().getId());
   		 	vacancyTempObj.setLastUpdatedDate(getDate);
   		 	rm.updateVacancyTemp(vacancyTempObj);
   		 	
   		 	// audit
   		 	actionTaken="Update Vacancy Template";
			auditObj.setAndInsertAudit(vacancyTempObj.getVacancyTempCode(), "", actionTaken);
   		 	
   		 	return new Forward(FORWARD_UPDATED);
		}else
			return new Forward(FORWARD_ERROR);
	}
	
	//getter setter
	public String getVacancyTempCode() {
		return vacancyTempCode;
	}

	public void setVacancyTempCode(String vacancyTempCode) {
		this.vacancyTempCode = vacancyTempCode;
	}

	public Label getLbVacancyTempCode() {
		return lbVacancyTempCode;
	}

	public void setLbVacancyTempCode(Label lbVacancyTempCode) {
		this.lbVacancyTempCode = lbVacancyTempCode;
	}
	
}
