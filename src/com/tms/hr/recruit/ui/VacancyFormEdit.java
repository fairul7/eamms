package com.tms.hr.recruit.ui;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.stdui.Label;
import kacang.stdui.SelectBox;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.hr.recruit.model.RecruitModule;
import com.tms.hr.recruit.model.VacancyObj;

public class VacancyFormEdit extends VacancyForm{
	public static final String FORWARD_UPDATED = "updated";
    public static final String FORWARD_ERROR = "error";
    public static final String TYPE_USAGE = "small";
    
    private String vacancyCode;
    private Label lblnVacancyCodeName;
    private SelectBox sbnVacancyTempCode;
    private Map mapVacancyTempCode = new LinkedHashMap();
    
    public void init(){
    	initFormEdit();
    }
    
    public void initFormEdit(){   	
    	setColumns(2);
    	setMethod("POST");
    	removeChildren();
    	Application app = Application.getInstance();
    	
    	Label lblnVacancyCode= new Label("lblnVacancyCode",  "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.vacancyCode")  + "</span>");
    	lblnVacancyCode.setAlign("right");
    	lblnVacancyCodeName = new Label("lblnVacancyCodeName","");
    	addChild(lblnVacancyCode);
    	addChild(lblnVacancyCodeName);
    	
    	//calling vacancy Form
    	super.init();
    	
    	//remove the field
    	removeChild(lblTempName);
    	removeChild(sbVacancyTempCode);
    	removeChild(lblVacancyCode);
    	removeChild(txtVacancyCode);
    	removeChild(lblcbVacancyTemp);
    	removeChild(cbVacancyTemp);
    	removeChild(lblVTempName);
    	removeChild(txtVacancyTempCode);
    }
    
    public void onRequest(Event evt){
    		populateForm();
    }
    
    public void populateForm(){
    	if(getVacancyCode()!=null){
    		Application app = Application.getInstance();
    		RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
    		try{
    			VacancyObj vacancyObj = rm.loadVacancy(getVacancyCode(), TYPE_USAGE);
    			lblnVacancyCodeName.setText(vacancyObj.getVacancyCode());
    			sbPosition.setSelectedOption(vacancyObj.getPositionId());
    			sbCountry.setSelectedOption(vacancyObj.getCountryId());
    			sbDepartment.setSelectedOption(vacancyObj.getDepartmentId());
    			tbJobRespon.setValue(vacancyObj.getResponsibilities());
    			tbJobRequire.setValue(vacancyObj.getRequirements());
    			txtNoOfPosition.setValue(vacancyObj.getNoOfPosition());
    			
    			if(vacancyObj.isPriority()){
    				rPriortyUrgent.setChecked(true);
    			}else{
    				rPriortyNormal.setChecked(true);
    			}
    			startDate.setDate(vacancyObj.getStartDate());
    			endDate.setDate(vacancyObj.getEndDate());
    			
    		}catch(DataObjectNotFoundException  e){
				Log.getLog(getClass()).error("Module " + getVacancyCode() + " not found");
				init(); 
			}   
    	}	
    }
    
    //onSubmit-override the vacancy form
	public Forward onSubmit(Event evt) {
		Forward forward = super.fireFormSubmitEvent(evt);
		String action = findButtonClicked(evt);
		boolean hasDateExist=false;
		
		if(action.equals(btnSubmit.getAbsoluteName())){
			Application app = Application.getInstance();
			validate(txtNoOfPosition, "txtInt", null);
			validate(sbPosition, "sb", null);
			validate(sbCountry, "sb", null);
			validate(sbDepartment,"sb", null);
			validate(tbJobRespon, "tb", null);
			validate(tbJobRequire, "tb", null);	
			
			if(validate(sbPosition, "sb", null) && validate(sbCountry, "sb", null) && validate(sbDepartment,"sb", null))
				hasDateExist=lookUpPosition(sbPosition, sbCountry, sbDepartment, getVacancyCode());
			
			//checking date validation
			VacancyObj cDateObj = new VacancyObj();
			try{
				boolean csDate = cDateObj.checkDate(startDate.getDate());
				if(csDate || hasDateExist){
					if(hasDateExist){
						super.vMsgStartDate.showError(app.getMessage("recruit.vacancy.alert.startDateExist"));
					}
					if(csDate){
						super.vMsgStartDate.showError("");
					}
					
					startDate.setInvalid(true);
					setInvalid(true);
				}
				
				boolean ceDate= cDateObj.checkDate(endDate.getDate());
				if(ceDate || hasDateExist){
					if(hasDateExist){
						super.vMsgEndDate.showError(app.getMessage("recruit.vacancy.alert.endDateExist"));
					}
					if(ceDate){
						super.vMsgEndDate.showError("");
					}
					endDate.setInvalid(true);
					setInvalid(true);
				}
			}catch(Exception e){
				//nothing
			}
						
			boolean cseDate = cDateObj.checkDate(startDate.getDate(), endDate.getDate());
			if(cseDate){
				startDate.setInvalid(true);
				endDate.setInvalid(true);
				setInvalid(true);
			}
			//end date validation
    	 }else{
    		 super.vMsgStartDate.showError("", false);
    	 }
		
		return forward;
	}

	//onValidate-override the vacancy form
	public Forward onValidate(Event evt) {
		String action = findButtonClicked(evt);
		if(action.equals(btnSubmit.getAbsoluteName())){
			 Application app = Application.getInstance();
    		 RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
    		 //auditObj
			 VacancyObj auditObj = new VacancyObj();
			 String actionTaken="";
			 
    		 //get Date
    		 Date getDate = new Date();
    		 //get the selectbox value
    		 String selectedPosition = (String) sbPosition.getSelectedOptions().keySet().iterator().next(); 
    		 String selectedCountry = (String) sbCountry.getSelectedOptions().keySet().iterator().next();
    		 String selectedDept = (String) sbDepartment.getSelectedOptions().keySet().iterator().next();
    		 
    		 //create obj and put value into obj
    		 VacancyObj vacancyObj = new VacancyObj();
    		 vacancyObj.setVacancyCode(vacancyCode);
    		 vacancyObj.setPositionId(selectedPosition);
    		 vacancyObj.setCountryId(selectedCountry);
    		 vacancyObj.setDepartmentId(selectedDept);
    		 vacancyObj.setNoOfPosition(Integer.parseInt(txtNoOfPosition.getValue().toString()));
    		 
    		 if(rPriortyNormal.isChecked())
    			 vacancyObj.setPriority(false);
    		 else
    			 vacancyObj.setPriority(true);
    			 
    		 vacancyObj.setResponsibilities((String)tbJobRespon.getValue());
    		 vacancyObj.setRequirements((String)tbJobRequire.getValue());
    		 vacancyObj.setStartDate(startDate.getDate());
    		 vacancyObj.setEndDate(endDate.getDate()); 
    		 vacancyObj.setLastUpdatedBy(app.getCurrentUser().getId());
    		 vacancyObj.setLastUpdatedDate(getDate);
    		 rm.updateVacancy(vacancyObj); 
    		 
    		 //audit
			 actionTaken="Update Vacancy";
			 auditObj.setAndInsertAudit(vacancyObj.getVacancyCode(), "", actionTaken);
    		 
    		 return new Forward(FORWARD_UPDATED);
 		}else
 			 return new Forward(FORWARD_ERROR);
	}
	
	public String getDefaultTemplate() {
		return "recruit/vacancyFormEdit";
	}
	
	//getter setter
	public Label getLblnVacancyCodeName() {
		return lblnVacancyCodeName;
	}

	public void setLblnVacancyCodeName(Label lblnVacancyCodeName) {
		this.lblnVacancyCodeName = lblnVacancyCodeName;
	}

	public String getVacancyCode() {
		return vacancyCode;
	}

	public void setVacancyCode(String vacancyCode) {
		this.vacancyCode = vacancyCode;
	}

	public Map getMapVacancyTempCode() {
		return mapVacancyTempCode;
	}

	public void setMapVacancyTempCode(Map mapVacancyTempCode) {
		this.mapVacancyTempCode = mapVacancyTempCode;
	}

	public SelectBox getSbnVacancyTempCode() {
		return sbnVacancyTempCode;
	}

	public void setSbnVacancyTempCode(SelectBox sbnVacancyTempCode) {
		this.sbnVacancyTempCode = sbnVacancyTempCode;
	}
	
}





















