package com.tms.hr.recruit.ui;

import java.util.ArrayList;
import java.util.Collection;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.ButtonGroup;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Radio;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.hr.recruit.model.ApplicantObj;

public class JobAppWorkingExpType  extends Form{
	public static final String FORWARD_SAVED_FRESH = "workingExpTypeFreshSave";
	public static final String FORWARD_SAVED_NONFRESH = "workingExpTypeNonFreshSave";
	public static final String FORWARD_ERROR = "error";
	public static final String FORWARD_MAINRESET = "workingExpTypeReset";
	
	private ButtonGroup radioGroupGender;
	private Radio rsFresh;
	private Radio rsNonFresh;
	private TextField txtYear;
	private TextField txtMonth;
	
	private Button btnSave;
	private Button btnReset;
	
	private ApplicantObj applicantObj;
	private Collection workingExpTypeCol;
	
	private JobAppWorkingExp jAppWorkingExp;
	private JobAppWorkingExpList jAppWorkingExpList;
	
	//	validator
	private ValidatorIsNumeric yearVIN= new ValidatorIsNumeric("yearVIN","");
	private ValidatorIsNumeric monthVIN= new ValidatorIsNumeric("monthVIN","");
	
	public void init(){
		initForm();
	}
	
	public void initForm(){
		setMethod("POST");
		setColumns(2);
		
    	Application app = Application.getInstance();
    	Label lblWorkingExp= new Label("lblWorkingExp", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.workingExp") + "</span>");
    	lblWorkingExp.setAlign("right");
    	Label lblSpace = new Label("lblSpace","");
    	lblWorkingExp.setColspan(4);
    	addChild(lblWorkingExp);
    	addChild(lblSpace);
    	
    	rsFresh= new Radio("rFresh","");
    	Label lblFreshMsg= new Label("lblFreshMsg", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.freshMsg") + "</span>");
    	rsFresh.setAlign("right");
    	rsNonFresh = new Radio("rNonFresh","");
    	rsNonFresh.setAlign("right");
    	Label lblNonFreshMsg= new Label("lblNonFreshMsg", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.nonFreshMsg") + "</span>");
    	
    	radioGroupGender = new ButtonGroup("radioGroupGender");
    	radioGroupGender.setType(ButtonGroup.RADIO_TYPE);
    	radioGroupGender.addButton(rsFresh);
    	radioGroupGender.addButton(rsNonFresh);
    	addChild(rsFresh);
    	addChild(lblFreshMsg);
    	addChild(rsNonFresh);
    	addChild(lblNonFreshMsg);
    	addChild(radioGroupGender);
    	
    	Label lblYears= new Label("lblYears", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.years") + "</span>");
    	txtYear = new TextField("year");
    	txtYear.setSize("5");
    	txtYear.setMaxlength("2");
    	addChild(txtYear);
    	addChild(lblYears);
    	
    	Label lblMonths= new Label("lblMonths", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.months") + "</span>");
    	txtMonth = new TextField("month");
    	txtMonth.setSize("5");
    	txtMonth.setMaxlength("2");
    	addChild(txtMonth);
    	addChild(lblMonths);
    	
    	btnSave = new Button("btnSave", app.getMessage("recruit.general.label.save","Save"));
    	btnReset = new Button("btnReset", app.getMessage("recruit.general.label.reset","Reset"));
    	addChild(btnSave);
    	addChild(btnReset);
	}
	
	public String getDefaultTemplate() {
		return "recruit/jobAppWorkingExpType";
	}
	
	public Forward onSubmit(Event evt) {
		Forward forward = super.onSubmit(evt); 
		String action = findButtonClicked(evt);
		jAppWorkingExp = (JobAppWorkingExp) Application.getInstance().getWidget(evt.getRequest(), "jobApplication.tab1.panel3.Form"); //get class data.
		jAppWorkingExpList = (JobAppWorkingExpList) Application.getInstance().getWidget(evt.getRequest(), "jobApplication.tab1.panel3.List"); //get class data.
		boolean flag = false;
		applicantObj = new ApplicantObj();
		if(action.equals(btnReset.getAbsoluteName())){
			 setFieldReset();
			 jAppWorkingExp.setHidden(true);
			 jAppWorkingExpList.setHidden(true);
			 destroyWorkingExp(true);
			 if(workingExpTypeCol!=null && workingExpTypeCol.size() > 0){
				 workingExpTypeCol=null;
			 }
			 applicantObj.setByPassValidation("yes");
			 //return new Forward(FORWARD_MAINRESET);
		 }else{ 
			if(!rsFresh.isChecked() && !rsNonFresh.isChecked()){
				rsFresh.setInvalid(true);
				rsNonFresh.setInvalid(true);
				flag=true;
			}
				
			if(rsNonFresh.isChecked()){
					 if(!yearVIN.validate(txtYear)){
						 txtYear.setInvalid(true);
						 flag=true;
					 }
					 if(!monthVIN.validate(txtMonth)){
						 txtMonth.setInvalid(true);
						 flag=true;
					 } 
			 }
		 }
		
		//make the validation fail
		 if(flag || this.isInvalid()){
			 setInvalid(true);
			 applicantObj.setByPassValidation("no"); //set obj status
		 }
		
		return forward;
	}	
	
	public Forward onValidate(Event evt) {
		String action = findButtonClicked(evt);
		String nameForward="";
		if(action.equals(btnSave.getAbsoluteName())){ 
			 workingExpTypeCol = new ArrayList();
			 String ttype="";
			 if(rsNonFresh.isChecked()){
				 ttype="t2";
				 setFormHide(false);
				 nameForward=FORWARD_SAVED_NONFRESH;
			 }else if(rsFresh.isChecked()){
				 ttype="t1";
				 setFormHide(true);
				 txtYear.setValue("");
				 txtMonth.setValue("");
				 //destroyWorkingExp(true);
				 nameForward=FORWARD_SAVED_FRESH;
			 }else{
				 setFormHide(true);
			 }
			 
			 //applicantObj.setTyear((String) txtYear.getValue());
			 //applicantObj.setTmonth((String) txtMonth.getValue());
			 applicantObj.setTtype(ttype);
			 applicantObj.setYearOfWorkingExp(hasWorkingExp());
			 applicantObj.setByPassValidation("yes");
			 workingExpTypeCol.add(applicantObj);
			 
		return new Forward(nameForward);
   	 }else
   		 return new Forward(FORWARD_ERROR);
	}
	
	//get the the workingExp form Collection if Non fresh selected
	public void isNonFresh(){
		if(rsNonFresh.isChecked())
			jAppWorkingExp.getEmpCol();
	}
	
	//delete workingExp form Collection
	public void destroyWorkingExp(boolean flag){
		if(flag){
			jAppWorkingExp.deleteEmpCol();
		}
	}
	
	//set hidden 
	public void setFormHide(boolean flag){
		jAppWorkingExp.setHidden(flag);
		jAppWorkingExpList.setHidden(flag);
	}
	
	public String hasWorkingExp(){
		String yearOfExp="";
		if(rsNonFresh.isChecked()){
			if(txtYear.getValue()!=null && txtMonth.getValue()!=null){
				yearOfExp = (String) txtYear.getValue() + " year(s) "  + txtMonth.getValue() + "month(s)";
			}else if(txtYear.getValue()!=null){
				yearOfExp = (String) txtYear.getValue() + " year(s) ";
			}else{
				yearOfExp = (String) txtMonth.getValue() + "month(s)";
			}
		}else
			yearOfExp="Fresh Graduate";
		
		return yearOfExp;
	}
	
	public void setFieldReset(){
		rsFresh.setChecked(false);
		rsNonFresh.setChecked(false);
		txtYear.setValue("");
		txtMonth.setValue("");
	}
	
	//getter setter
	public Radio getRsFresh() {
		return rsFresh;
	}

	public void setRsFresh(Radio rsFresh) {
		this.rsFresh = rsFresh;
	}
	public ButtonGroup getRadioGroupGender() {
		return radioGroupGender;
	}

	public void setRadioGroupGender(ButtonGroup radioGroupGender) {
		this.radioGroupGender = radioGroupGender;
	}

	public Radio getRsNonFresh() {
		return rsNonFresh;
	}

	public void setRsNonFresh(Radio rsNonFresh) {
		this.rsNonFresh = rsNonFresh;
	}

	public TextField getTxtMonth() {
		return txtMonth;
	}

	public void setTxtMonth(TextField txtMonth) {
		this.txtMonth = txtMonth;
	}

	public TextField getTxtYear() {
		return txtYear;
	}

	public void setTxtYear(TextField txtYear) {
		this.txtYear = txtYear;
	}

	public Button getBtnReset() {
		return btnReset;
	}

	public void setBtnReset(Button btnReset) {
		this.btnReset = btnReset;
	}

	public Button getBtnSave() {
		return btnSave;
	}

	public void setBtnSave(Button btnSave) {
		this.btnSave = btnSave;
	}

	public Collection getWorkingExpTypeCol() {
		return workingExpTypeCol;
	}

	public void setWorkingExpTypeCol(Collection workingExpTypeCol) {
		this.workingExpTypeCol = workingExpTypeCol;
	}

	public ApplicantObj getApplicantObj() {
		return applicantObj;
	}

	public void setApplicantObj(ApplicantObj applicantObj) {
		this.applicantObj = applicantObj;
	}
	
}
