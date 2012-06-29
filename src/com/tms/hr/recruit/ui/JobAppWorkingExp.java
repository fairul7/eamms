package com.tms.hr.recruit.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.tms.hr.recruit.model.ApplicantObj;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.SelectBox;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

public class JobAppWorkingExp extends Form{
	public static final String FORWARD_SAVED = "workingExpSave";
	public static final String FORWARD_ERROR = "error";
	
	private TextField txtCompanyName;
	private TextField txtPosition;
	private SelectBox sbPositionLvl;
	private DatePopupField startDate;
	private DatePopupField endDate;
	private TextBox tbReasonForLeave;
	
	private Panel btnPanel;
	private Button btnSave;
	private Button btnReset;
	
	private JobAppWorkingExpType jAppWorkingExpType;
	private  ApplicantObj applicantObj;
	
	private String empId;
	private Collection empCol = new ArrayList();
	private Map mPositionLvl = new LinkedHashMap();
	
	public void init(){
		initForm();
	}
	
	public void initForm(){
		setMethod("POST");
		setColumns(2);
		
    	Application app = Application.getInstance();
    	
    	Label lblEmploymentHis= new Label("lblEmploymentHis", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.employmentHis") + "</span>");
    	lblEmploymentHis.setAlign("right");
    	Label lblSpace = new Label("lblSpace","");
    	addChild(lblEmploymentHis);
    	addChild(lblSpace);
    	
    	Label lblCompanyName= new Label("lblCompanyName", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.companyName") + "*</span>");
    	lblCompanyName.setAlign("right");
    	txtCompanyName = new TextField("txtCompanyName");
    	txtCompanyName.setSize("30");
    	txtCompanyName.setMaxlength("40");
    	txtCompanyName.addChild(new ValidatorNotEmpty("txtCompanyNameVNE",app.getMessage("recruit.general.warn.empty")));
    	addChild(lblCompanyName);
    	addChild(txtCompanyName);
    	
    	Label lblPosition= new Label("lblPosition", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.position") + "*</span>");
    	lblPosition.setAlign("right");
    	txtPosition = new TextField("txtPosition");
    	txtPosition.setSize("30");
    	txtPosition.setMaxlength("30");
    	txtPosition.addChild(new ValidatorNotEmpty("txtPositionVNE",app.getMessage("recruit.general.warn.empty")));
    	addChild(lblPosition);
    	addChild(txtPosition);
    	
    	Label lblPositionLvl= new Label("lblPositionLvl", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.positionLvl") + "*</span>");
    	lblPositionLvl.setAlign("right");
    	sbPositionLvl = new SelectBox("sbPositionLvl", mPositionLvl, null, false, 1);
    	getPositionMapping(sbPositionLvl);
    	
    	/*mPositionLvl.put("---", app.getMessage("recruit.general.hierachy.selectPositionLvl") );
    	mPositionLvl.put("emp1", app.getMessage("recruit.general.label.sm") );
    	mPositionLvl.put("emp2", app.getMessage("recruit.general.label.m") );
    	mPositionLvl.put("emp3", app.getMessage("recruit.general.label.se") );
    	mPositionLvl.put("emp4", app.getMessage("recruit.general.label.je") );
    	mPositionLvl.put("emp5", app.getMessage("recruit.general.label.fresh") );
    	mPositionLvl.put("emp6", app.getMessage("recruit.general.label.nonExe") );
    	sbPositionLvl.setOptionMap(mPositionLvl);*/
    	
    	addChild(lblPositionLvl);
    	addChild(sbPositionLvl);
    	
    	Label lblStartDate= new Label("lblStartDate", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.dateJoin") + "*</span>");
    	lblStartDate.setAlign("right");
    	startDate = new DatePopupField("startDate");
    	addChild(lblStartDate);
    	addChild(startDate);
		
    	Label lblendDate= new Label("lblendDate", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.dateLeft") + "*</span>");
    	lblendDate.setAlign("right");
    	endDate = new DatePopupField("endDate");
    	addChild(lblendDate);
    	addChild(endDate);
    	
    	Label lblReasonForLeave= new Label("lblReasonForLeave", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.reasonForLeave") + "</span>");
    	lblReasonForLeave.setAlign("right");
    	tbReasonForLeave = new TextBox("tbReasonForLeave");
    	addChild(lblReasonForLeave);
    	addChild(tbReasonForLeave);
    	
    	Label lblspace = new Label("lblspace","");
    	addChild(lblspace);
    	btnSave = new Button("btnSave", app.getMessage("recruit.general.label.save","Save"));
    	btnReset = new Button("btnReset", app.getMessage("recruit.general.label.reset","Reset"));
    	
    	btnPanel = new Panel("btnPanel");
    	btnPanel.setColspan(2);
    	btnPanel.addChild(btnSave);
    	btnPanel.addChild(btnReset);
    	
    	addChild(lblSpace);
    	addChild(btnPanel);
    	
    	setHidden(true);
	}
	
	public void getPositionMapping(SelectBox ff){
		mPositionLvl = getPositionMap();
		ff.setOptionMap(mPositionLvl);
	}
	
	public Map getPositionMap(){
		Application app = Application.getInstance();
		mPositionLvl.put("---", app.getMessage("recruit.general.hierachy.selectPositionLvl") );
    	mPositionLvl.put("emp1", app.getMessage("recruit.general.label.sm") );
    	mPositionLvl.put("emp2", app.getMessage("recruit.general.label.m") );
    	mPositionLvl.put("emp3", app.getMessage("recruit.general.label.se") );
    	mPositionLvl.put("emp4", app.getMessage("recruit.general.label.je") );
    	mPositionLvl.put("emp5", app.getMessage("recruit.general.label.fresh") );
    	mPositionLvl.put("emp6", app.getMessage("recruit.general.label.nonExe") );
		return mPositionLvl;
	}
	
	public Forward onSubmit(Event evt) {
		Forward forward = super.onSubmit(evt); 
		String action = findButtonClicked(evt);
		boolean flag = false;
		applicantObj = new ApplicantObj();
		 if(action.equals(btnReset.getAbsoluteName())){
			 validationFalse();
			 setFieldReset();
			 applicantObj.setByPassValidation("yes");
			 setEmpId(null); //set id to null(param id)
		 }else{
			 JobAppPersonal jap = new JobAppPersonal();
			 boolean boolsbPositionLvl=jap.validateSelectBox(sbPositionLvl, "recruit.general.hierachy.selectPositionLvl", "---");
			 if(boolsbPositionLvl){
				 sbPositionLvl.setInvalid(true);
				 flag=true;
			 }
			
			 jap.validationText(txtCompanyName,"txtCompanyNameVNE", "VNE",true);
			 jap.validationText(txtPosition,"txtPositionVNE", "VNE",true);
			 
			 if(flag || this.isInvalid()){
				 setInvalid(true);
				 applicantObj.setByPassValidation("no"); //set obj status
			 }
		 }
		
		return forward;
	}
	
	public Forward onValidate(Event evt) {
		String action = findButtonClicked(evt);
			
		 if(action.equals(btnSave.getAbsoluteName())){
			 String selectedPositionLvl= (String) sbPositionLvl.getSelectedOptions().keySet().iterator().next(); //set Key
			 String positionLvlDesc = mPositionLvl.get(selectedPositionLvl).toString(); //set Value
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			 
			 if(getEmpId()!=null){
				 for(Iterator ite=empCol.iterator();ite.hasNext();){
						ApplicantObj appObj= (ApplicantObj) ite.next();
						if(appObj.getEmpId().equals(getEmpId())){
							appObj.setCompanyName((String)txtCompanyName.getValue());
							appObj.setPositionName((String)txtPosition.getValue());
							appObj.setPositionLvl(selectedPositionLvl);
							appObj.setPositionLvlDesc(positionLvlDesc);
							appObj.setStartDate(sdf.format(startDate.getDate()));
							appObj.setEndDate(sdf.format(endDate.getDate()));
							appObj.setReasonForLeave((String) tbReasonForLeave.getValue());
							appObj.setByPassValidation("yes");
							setEmpId(null);//set the param skillId to null
						}
				 }	
			 }else{
				 UuidGenerator uuid = UuidGenerator.getInstance();
				 applicantObj.setEmpId(uuid.getUuid());
				 applicantObj.setCompanyName((String)txtCompanyName.getValue());
				 applicantObj.setPositionName((String)txtPosition.getValue());
				 applicantObj.setPositionLvl(selectedPositionLvl);
				 applicantObj.setPositionLvlDesc(positionLvlDesc);
				 applicantObj.setStartDate(sdf.format(startDate.getDate()));
				 applicantObj.setEndDate(sdf.format(endDate.getDate()));
				 applicantObj.setReasonForLeave((String) tbReasonForLeave.getValue());
				 applicantObj.setByPassValidation("yes");
				 empCol.add(applicantObj); 
			 } 
			 setFieldReset();
			 
			 return new Forward(FORWARD_SAVED);
    	 }else
    		 return new Forward(FORWARD_ERROR);
	}
	
	//delete the emp Collection
	public void deleteEmpCol(){
		empCol.clear();
	}
	
	public void onRequest(Event evt){
		populateForm();
	}
	
	public void populateForm(){
		 if(getEmpId()!=null){
			 validationFalse();
			 ApplicantObj applicantObj;
			 for(Iterator ite=empCol.iterator(); ite.hasNext(); ){
				 applicantObj = (ApplicantObj) ite.next();
				 if(applicantObj.getEmpId().equals(getEmpId())){
					 txtCompanyName.setValue(applicantObj.getCompanyName());
					 txtPosition.setValue(applicantObj.getPositionName());
					 sbPositionLvl.setSelectedOption(applicantObj.getPositionLvl());
					 startDate.setValue(applicantObj.getStartDate());
					 endDate.setValue(applicantObj.getEndDate());
					 tbReasonForLeave.setValue(applicantObj.getReasonForLeave());
				 }
			 }
		 }
	}
	
	public void setFieldReset(){
		 txtCompanyName.setValue("");
		 txtPosition.setValue("");
		 sbPositionLvl.setSelectedOption("---");
		 startDate.setValue("");
		 endDate.setValue("");
		 tbReasonForLeave.setValue("");
	}
	
	public void validationFalse(){
		 JobAppPersonal jap = new JobAppPersonal();
		 jap.validationText(txtCompanyName, "txtCompanyNameVNE", "VNE", false);
		 jap.validationText(txtPosition, "txtPositionVNE", "VNE", false);
		 txtCompanyName.setInvalid(false);
		 txtPosition.setInvalid(false);
		 startDate.setInvalid(false);
		 endDate.setInvalid(false);
		 setInvalid(false);
	}

	//getter and setter
	public Map getMPositionLvl() {
		return mPositionLvl;
	}

	public void setMPositionLvl(Map positionLvl) {
		mPositionLvl = positionLvl;
	}

	public Collection getEmpCol() {
		return empCol;
	}

	public void setEmpCol(Collection empCol) {
		this.empCol = empCol;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public Button getBtnSave() {
		return btnSave;
	}

	public void setBtnSave(Button btnSave) {
		this.btnSave = btnSave;
	}

	public ApplicantObj getApplicantObj() {
		return applicantObj;
	}

	public void setApplicantObj(ApplicantObj applicantObj) {
		this.applicantObj = applicantObj;
	}
		
}
