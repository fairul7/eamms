package com.tms.hr.recruit.ui;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.FormField;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.TextField;
import kacang.stdui.validator.Validator;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.hr.recruit.model.RecruitModule;
import com.tms.hr.recruit.model.VacancyObj;

public class VacancyTempForm extends VacancyTempFormDefault{
    public static final String FORWARD_SUBMITED = "submited";
    public static final String FORWARD_ERROR = "error";
    
    private TextField txtVacancyTempCode;
  
    private Panel btnPanel;
    private Button btnSubmit;
    private Button btnCancel;
    
    public void init(){
    	initFormVacancyTemp();
    }
    
    public void initFormVacancyTemp(){
    	setColumns(2);
    	setMethod("POST");
    	
    	Application app = Application.getInstance();
    	
    	txtVacancyTempCode = new TextField("txtVacancyTempCode");
    	txtVacancyTempCode.setSize("30");
    	txtVacancyTempCode.setMaxlength("20");
    	//txtVacancyTempCode.addChild(new ValidateVacancyTempCode("txtVacancyTempCodeVdc",app.getMessage("recruit.general.warn.codeKey")));
    	Label lblTempName= new Label("lblTempName",  "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.tempName")  + "*</span>");
    	addChild(lblTempName);
    	addChild(txtVacancyTempCode);
      	
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
    
    public void onRequest(Event evt) {
    }
    
    //onSubmit
    public Forward onSubmit(Event evt) {
    	Forward forward = super.onSubmit(evt);
    	String action = findButtonClicked(evt);
    	if(action.equals(btnSubmit.getAbsoluteName())){    
			validate(txtVacancyTempCode, "codeExist", "vacancyTempCode"); //validate the textbox
			validate(sbPosition, "sb", null);
			validate(sbDepartment, "sb", null);
			validate(sbCountry, "sb", null);
			validate(tbJobRespon, "tb", null);
			validate(tbJobRequire, "tb", null);
    	}
    	return forward;
    }
    
    //onValidate
    public Forward onValidate(Event evt) {
    	 String action = findButtonClicked(evt);
    	 if(action.equals(btnSubmit.getAbsoluteName())){    		 
    		 Application app = Application.getInstance();
    		 RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
    		 //auditObj
			 VacancyObj auditObj = new VacancyObj();
			 String actionTaken="";
    		 //get date
    		 Date getDate = new Date();
    		 
    		 //get the selectbox value
    		 String selectedPosition = (String) sbPosition.getSelectedOptions().keySet().iterator().next(); 
    		 String selectedCountry = (String) sbCountry.getSelectedOptions().keySet().iterator().next();
    		 String selectedDept = (String) sbDepartment.getSelectedOptions().keySet().iterator().next();
    		 
    		 //create obj and put value into obj
    		 VacancyObj vacancyTempObj = new VacancyObj();
    		 
    		 vacancyTempObj.setVacancyTempCode((String)txtVacancyTempCode.getValue());
    		 vacancyTempObj.setPositionId(selectedPosition);
    		 vacancyTempObj.setCountryId(selectedCountry);
    		 vacancyTempObj.setDepartmentId(selectedDept);
    		 vacancyTempObj.setResponsibilities((String)tbJobRespon.getValue());
    		 vacancyTempObj.setRequirements((String)tbJobRequire.getValue());
    		 vacancyTempObj.setCreatedBy(app.getCurrentUser().getId());
    		 vacancyTempObj.setCreatedDate(getDate); 
    		 vacancyTempObj.setLastUpdatedBy(app.getCurrentUser().getId());
    		 vacancyTempObj.setLastUpdatedDate(getDate);
    		 rm.insertVacancyTemp(vacancyTempObj);
    		 
    		 //audit
			 actionTaken="Add Vacancy Template";
			 auditObj.setAndInsertAudit(vacancyTempObj.getVacancyTempCode(), "", actionTaken);
    		 
    		 return new Forward(FORWARD_SUBMITED);
    	 }else
    		 return new Forward(FORWARD_ERROR); 
    }
    
    //my tempate
   /* public String getDefaultTemplate() {
        return "recruit/vacancyTempForm";
    }*/
    
}
