package com.tms.hr.recruit.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.ButtonGroup;
import kacang.stdui.CountrySelectBox;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.FormField;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.Radio;
import kacang.stdui.SelectBox;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorEmail;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.stdui.validator.ValidatorMessage;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

import com.tms.hr.recruit.model.ApplicantObj;
import com.tms.hr.recruit.model.RecruitAppModule;

public class JobAppPersonal extends Form{
	public static final String FORWARD_SAVED = "personalSave";
	public static final String FORWARD_ERROR = "error";
	
	protected String vacancyCode;
	protected String positionTitle;
	protected Label lblrPosApplied;
	protected TextField txtName;
	protected TextField txtAge;
	protected DatePopupField dob;
	protected TextField txtNirc;
	protected Radio rMale;
	protected Radio rFemale;
	protected ButtonGroup radioGroupGender;
	protected Radio rSingle;
	protected Radio rMarried;
	protected Radio rDivorced;
	protected ButtonGroup radioGroupMarital;
	protected TextField txtNoOfChild;
	protected TextField txtEmail;
	protected TextField txtMobileNo;
	protected TextField txtTelNo;
	protected TextBox tbAddr;
	protected TextField txtPostalCode;
	protected TextField txtCity;
	
	protected SelectBox sbNationality;
	protected SelectBox sbCountry;
	protected CountrySelectBox countrySB;
	protected TextField txtState;
	
	protected Collection personalCol;
	protected ApplicantObj applicantObj;
	
	protected Panel btnPanel;
	protected Button btnSave;
	protected Button btnReset;
	
	//validator
	protected ValidatorIsNumeric nameVIN= new ValidatorIsNumeric("nameVIN","");
	protected ValidatorIsNumeric noOfChildVIN = new ValidatorIsNumeric("noOfChildVIN","");		
	protected ValidatorMessage vMsgName;
	protected ValidatorMessage vMsgNoOfChild;
	protected ValidatorMessage vMsgEmail;
	protected ValidatorMessage vMsgNirc;
	
	public void init(){
		initForm();
	}
	
	public void initForm(){
		setMethod("POST");
		setColumns(2);
		
		removeChildren();
    	Application app = Application.getInstance();
    	countrySB = new CountrySelectBox();
    	
    	Label lblPosApplied= new Label("lblPosApplied", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.posApplied") + "</span>");
    	lblPosApplied.setAlign("right");
    	lblrPosApplied = new Label("lblrPosApplied", "");
    	addChild(lblPosApplied);
    	addChild(lblrPosApplied);
    	
    	Label lblPersonalDetails= new Label("lblPersonalDetails", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.personalDetails") + "</span>");
    	lblPersonalDetails.setAlign("right");
    	Label lblSpace = new Label("lblSpace","");
    	addChild(lblPersonalDetails);
    	addChild(lblSpace);
    	
    	Label lblName= new Label("lblName", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.name") + "*</span>");
    	lblName.setAlign("right");
    	txtName = new TextField("txtName");
    	txtName.setSize("30");
    	txtName.setMaxlength("40");
    	vMsgName = new ValidatorMessage("vMsgName");
    	txtName.addChild(vMsgName);
    	addChild(lblName);
    	addChild(txtName);
    	
    	Label lblAge= new Label("lblAge", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.age") + "*</span>");
    	lblAge.setAlign("right");
    	txtAge = new TextField("txtAge");
    	txtAge.setSize("5");
    	txtAge.setMaxlength("2");
    	txtAge.addChild(new ValidatorIsNumeric("txtAgeVIN",app.getMessage("recruit.general.warn.notNumericVIN")));
    	addChild(lblAge);
    	addChild(txtAge);
    	
    	Label lblDob= new Label("lblDob", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.dob") + "*</span>");
    	lblDob.setAlign("right");
    	dob = new DatePopupField("dob");
    	addChild(lblDob);
    	addChild(dob);
    	
    	Label lblNirc= new Label("lblNirc", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.nirc") + "*</span>");
    	lblNirc.setAlign("right");
    	txtNirc = new TextField("txtNirc");
    	txtNirc.setSize("30");
    	txtNirc.setMaxlength("20");
    	txtNirc.addChild(new ValidatorNotEmpty("txtNircVNE",app.getMessage("recruit.general.warn.empty")));
    	vMsgNirc = new ValidatorMessage("vMsgNirc");
    	txtNirc.addChild(vMsgNirc);
    	addChild(lblNirc);
    	addChild(txtNirc);
    	
    	Label lblGender= new Label("lblGender", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.gender") + "*</span>");
    	lblGender.setAlign("right");
    	rMale= new Radio("rMale", app.getMessage("recruit.general.label.male"));
    	rFemale = new Radio("rFemale", app.getMessage("recruit.general.label.female"));
    	
    	radioGroupGender = new ButtonGroup("radioGroupGender");
    	radioGroupGender.setType(ButtonGroup.RADIO_TYPE);
    	radioGroupGender.setColumns(2);
    	radioGroupGender.addButton(rMale);
    	radioGroupGender.addButton(rFemale);
    	addChild(lblGender);
    	addChild(radioGroupGender);
    	
    	Label lblMarital= new Label("lblMarital", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.marital") + "*</span>");
    	lblMarital.setAlign("right");
    	rSingle= new Radio("rSingle", app.getMessage("recruit.general.label.single"));
    	rMarried = new Radio("rMarried", app.getMessage("recruit.general.label.married"));
    	rDivorced = new Radio("rDivorced", app.getMessage("recruit.general.label.divorced"));
    	
    	radioGroupMarital = new ButtonGroup("radioGroupMarital");
    	radioGroupMarital.setType(ButtonGroup.RADIO_TYPE);
    	radioGroupMarital.setColumns(3);
    	radioGroupMarital.addButton(rSingle);
    	radioGroupMarital.addButton(rMarried);
    	radioGroupMarital.addButton(rDivorced);
    	addChild(lblMarital);
    	addChild(radioGroupMarital);
    	
    	Label lblNoOfChild= new Label("lblNoOfChild", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.noOfChild") + "</span>");
    	lblNoOfChild.setAlign("right");
    	txtNoOfChild = new TextField("txtNoOfChild");
    	txtNoOfChild.setSize("5");
    	txtNoOfChild.setMaxlength("2");
    	vMsgNoOfChild = new ValidatorMessage("vMsgNoOfChild");
    	txtNoOfChild.addChild(vMsgNoOfChild);
    	addChild(lblNoOfChild);
    	addChild(txtNoOfChild);
    	
    	//nationality
    	Label lblNationality= new Label("lblNationality", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.nationality") + "*</span>");
    	lblNationality.setAlign("right");
    	sbNationality = new SelectBox("sbNationality");
    	sbNationality.setOptionMap(countrySB.getOptionMap());
    	addChild(lblNationality);
    	addChild(sbNationality);
    	
    	Label lblContactInfo= new Label("lblContactInfo", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.contactInfo") + "</span>");
    	lblContactInfo.setAlign("right");
    	Label lblSpace2 = new Label("lblSpace2","");
    	addChild(lblContactInfo);
    	addChild(lblSpace2);
    	
    	Label lblEmailAddr= new Label("lblEmailAddr", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.emailAddr") + "*</span>");
    	lblEmailAddr.setAlign("right");
    	txtEmail = new TextField("txtEmail");
    	txtEmail.setSize("30");
    	txtEmail.setMaxlength("40");
    	txtEmail.addChild(new ValidatorEmail("txtEmailVE",app.getMessage("recruit.general.warn.emailVE")));
    	vMsgEmail = new ValidatorMessage("vMsgEmail");
    	txtEmail.addChild(vMsgEmail);
    	addChild(lblEmailAddr);
    	addChild(txtEmail);
    	
    	Label lblTelNo= new Label("lblTelNo", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.telNo") + "</span>");
    	lblTelNo.setAlign("right");
    	txtTelNo = new TextField("txtTelNo");
    	txtTelNo.setSize("15");
    	txtTelNo.setMaxlength("15");
    	addChild(lblTelNo);
    	addChild(txtTelNo);
    	
    	Label lblMobileNo= new Label("lblMobileNo", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.mobileNo") + "*</span>");
    	lblMobileNo.setAlign("right");
    	txtMobileNo = new TextField("txtMobileNo");
    	txtMobileNo.setSize("15");
    	txtMobileNo.setMaxlength("15");
    	txtMobileNo.addChild(new ValidatorNotEmpty("txtMobileNoVNE",app.getMessage("recruit.general.warn.empty")));
    	addChild(lblMobileNo);
    	addChild(txtMobileNo);
    	
    	Label lblAddr= new Label("lblAddr", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.addr") + "*</span>");
    	lblAddr.setAlign("right");
    	tbAddr = new TextBox("tbAddr");
    	tbAddr.addChild(new ValidatorNotEmpty("tbAddrVNE",app.getMessage("recruit.general.warn.empty")));
    	addChild(lblAddr);
    	addChild(tbAddr);
    	
    	Label lblPostalCode= new Label("lblPostalCode", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.postalCode") + "*</span>");
    	lblPostalCode.setAlign("right");
    	txtPostalCode = new TextField("txtPostalCode");
    	txtPostalCode.addChild(new ValidatorNotEmpty("txtPostalCodeVNE",app.getMessage("recruit.general.warn.empty")));
    	txtPostalCode.setSize("15");
    	txtPostalCode.setMaxlength("10");
    	addChild(lblPostalCode);
    	addChild(txtPostalCode);
    	
    	Label lblCity= new Label("lblCity", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.city") + "*</span>");
    	lblCity.setAlign("right");
    	txtCity = new TextField("txtCity");
    	txtCity.addChild(new ValidatorNotEmpty("txtCityVNE",app.getMessage("recruit.general.warn.empty")));
    	txtCity.setSize("15");
    	txtCity.setMaxlength("30");
    	addChild(lblCity);
    	addChild(txtCity);
    	
      	Label lblState= new Label("lblState", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.state") + "*</span>");
    	lblState.setAlign("right");
    	txtState= new TextField("sbState");
    	txtState.addChild(new ValidatorNotEmpty("txtStateVNE",app.getMessage("recruit.general.warn.empty")));
    	txtState.setSize("15");
    	txtState.setMaxlength("30");
    	addChild(lblState);
    	addChild(txtState);
    	
       	Label lblCountry= new Label("lblCountry", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.country") + "*</span>");
    	lblCountry.setAlign("right");
    	sbCountry= new SelectBox("sbCountry");
    	sbCountry.setOptionMap(countrySB.getOptionMap());
    	addChild(lblCountry);
    	addChild(sbCountry);
    	
    	//btn
    	btnSave = new Button("btnSave", app.getMessage("recruit.general.label.save","Save"));
    	btnReset = new Button("btnReset", app.getMessage("recruit.general.label.reset","Reset"));
    	btnPanel = new Panel("btnPanel");
    	btnPanel.setColspan(2);
    	btnPanel.addChild(btnSave);
    	btnPanel.addChild(btnReset);
    	
    	Label lblspace = new Label("lblspace","");
    	addChild(lblspace);
    	addChild(btnPanel);
	}
	
	public Forward onSubmit(Event evt) {
		Forward forward = super.onSubmit(evt); 
		Application app = Application.getInstance();
		boolean flag = false;
		String action = findButtonClicked(evt);
		applicantObj = new ApplicantObj();
		if(action.equals(btnReset.getAbsoluteName())){
			 setValidationInvalid();
			 
			 if(personalCol!=null && personalCol.size() > 0){
				 //personalCol.remove(applicantObj);
				 personalCol=null;
			 }
			 
			 //set obj flag status
			 applicantObj.setByPassValidation("yes");
			 
			 //validator setInvalid
			 vMsgName.showError("", false);
			 vMsgNoOfChild.showError("",false);
			 vMsgEmail.showError("",false);
			 vMsgNirc.showError("",false);
			 
			 //validation text msg
			 validationText(txtAge,"txtAgeVIN", "VIN",false);
			 validationText(txtNirc,"txtNircVNE", "VNE",false);
			 validationText(txtMobileNo,"txtMobileNoVNE", "VNE",false);
			 validationText(tbAddr,"tbAddrVNE", "VNE",false);
			 validationText(txtPostalCode,"txtPostalCodeVNE", "VNE",false);
			 validationText(txtState,"txtStateVNE", "VNE",false);
			 validationText(txtCity,"txtCityVNE", "VNE",false);
			 
			 //formfield value
			 resetValuePersonal();
			
		 }else{
			 
			 //validation radio buton
			 if(!rMale.isChecked() && !rFemale.isChecked()){
				 radioGroupGender.setInvalid(true);
				 flag=true;
			 }
			 
			 if(!rSingle.isChecked() && !rMarried.isChecked() && !rDivorced.isChecked()){ 
				 radioGroupMarital.setInvalid(true);
				 flag=true;
			 }
			 
			 //validation name
			 if(!txtName.getValue().equals("")){
				 if(nameVIN.validate(txtName)){
					 vMsgName.showError(app.getMessage("recruit.general.warn.nameVE"));
					 txtName.setInvalid(true);
					 flag=true;
				 }
			 }else{
				 vMsgName.showError(app.getMessage("recruit.general.warn.empty"));
				 txtName.setInvalid(true);
				 flag=true;
			 }
			 
			 //validation noOfChild if married and divorced radio button selected
			 if(rMarried.isChecked() || rDivorced.isChecked()){
					 if(!noOfChildVIN.validate(txtNoOfChild)){
						 vMsgNoOfChild.showError(app.getMessage("recruit.general.warn.notNumericVIN"));
						 txtNoOfChild.setInvalid(true);
						 flag=true;
					 }
			 }
			 
			 //selectbox validation
			 boolean boolsbNationality=validateSelectBox(sbNationality, "recruit.general.warn.sbNationality", "-1");
			 if(boolsbNationality){
				 sbNationality.setInvalid(true);
				 flag=true;
			 }
				 
			 boolean boolsbCountry=validateSelectBox(sbCountry, "recruit.general.warn.sbCountry", "-1");
			 if(boolsbCountry){
				 sbCountry.setInvalid(true);
				 flag=true;
			 }
			 
			 if(txtEmail.getValue().equals("")){
				 vMsgEmail.showError(app.getMessage("recruit.general.warn.empty"));
				 txtEmail.setInvalid(true);
				 flag=true;
			 }
			
			 //checking NIRC-make sure not duplicated nircs
			 if(!txtNirc.getValue().equals("")){
				 RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
				 boolean hasNircExist= ram.lookUpNircExist((String)txtNirc.getValue());
				 if(hasNircExist){
					 vMsgNirc.showError(app.getMessage("recruit.general.warn.NircExist"));
					 txtNirc.setInvalid(true);
					 flag=true;
				 }
			 } 
			  
			 validationText(txtAge,"txtAgeVIN", "VIN",true);
			 validationText(txtNirc,"txtNircVNE", "VNE",true);
			 validationText(txtMobileNo,"txtMobileNoVNE", "VNE",true);
			 validationText(tbAddr,"tbAddrVNE", "VNE",true);
			 validationText(txtPostalCode,"txtPostalCodeVNE", "VNE",true);
			 validationText(txtState,"txtStateVNE", "VNE",true);
			 validationText(txtCity,"txtCityVNE", "VNE",true);
			 
			 if(flag || this.isInvalid()){
				 setInvalid(true);
				 applicantObj.setByPassValidation("no"); //set obj status
			 }
		 }
		
		return forward;
	}
	
	public void resetValuePersonal(){
		 txtName.setValue(""); 
		 txtAge.setValue("");
		 dob.setValue("");
		 txtNirc.setValue("");
		 txtNoOfChild.setValue("");
		 rMale.setChecked(false);
		 rFemale.setChecked(false);
		 rSingle.setChecked(false);
		 rMarried.setChecked(false);
		 rDivorced.setChecked(false);
		 sbNationality.setSelectedOption("-1");
		 sbCountry.setSelectedOption("-1");
		 txtEmail.setValue("");
		 txtMobileNo.setValue("");
		 txtTelNo.setValue("");
		 tbAddr.setValue("");
		 txtPostalCode.setValue("");
		 txtState.setValue("");
		 txtCity.setValue("");
	}
	
	public boolean validateSelectBox(FormField ff, String msg, String startKey){
        Application app = Application.getInstance();
		boolean key=false;	
        	List list = (List) ff.getValue();
	        String strSb = (String) list.get(0);
	        if(strSb.startsWith(startKey)){
	        	ff.setMessage(app.getMessage(msg));
	        	key=true;
        }
	    return key;    
	}
	
	public void validationText(FormField ff, String validatorName, String type, boolean status){
		Application app = Application.getInstance();

		if(type.equals("VNE")){
			ValidatorNotEmpty vne = (ValidatorNotEmpty)ff.getChild(validatorName);
			if(status){
				vne.setText(app.getMessage("recruit.general.warn.empty"));
			}else{
				vne.setText("");
			}
		}else if(type.equals("VIN")){
			ValidatorIsNumeric vin = (ValidatorIsNumeric)ff.getChild(validatorName);
			if(status){
				vin.setText(app.getMessage("recruit.general.warn.notNumericVIN"));
			}else{
				vin.setText("");
			}
		}else if(type.equals("VE")){
			ValidatorEmail ve =  (ValidatorEmail)ff.getChild(validatorName);
			if(status){
				ve.setText(app.getMessage("recruit.general.warn.emailVE"));
			}else{
				ve.setText("");
			}
		}
	}
	
	public void setValidationInvalid(){
		 txtAge.setInvalid(false);
		 dob.setInvalid(false);
		 txtNirc.setInvalid(false);
		 txtMobileNo.setInvalid(false);
		 tbAddr.setInvalid(false);
		 txtPostalCode.setInvalid(false);
		 txtState.setInvalid(false);
		 txtCity.setInvalid(false);
		 setInvalid(false);
	}
	
	public Forward onValidate(Event evt) {
		String action = findButtonClicked(evt);
		
		 if(action.equals(btnSave.getAbsoluteName())){
			 String selectedNationality= (String) sbNationality.getSelectedOptions().keySet().iterator().next(); //set key
			 String nationalityDesc = countrySB.getOptionMap().get(selectedNationality).toString(); //set value
			 
			 String selectedCountry= (String) sbCountry.getSelectedOptions().keySet().iterator().next(); //set key
			 String countryDesc = countrySB.getOptionMap().get(selectedCountry).toString(); //set value
			 
			 UuidGenerator uuid = UuidGenerator.getInstance();
			 Date getDate = new Date();
			 
			 //applicantObj = new ApplicantObj();
			 personalCol = new ArrayList();
			 
			 applicantObj.setApplicantId(uuid.getUuid());
			 applicantObj.setCreatedDate(getDate);
			 applicantObj.setName((String)txtName.getValue());
			 applicantObj.setAge((String)txtAge.getValue());
			 applicantObj.setDob(dob.getDate());
			 applicantObj.setNirc((String)txtNirc.getValue());
			 
			 if(rMale.isChecked()){
				 applicantObj.setGender(true);
			 }else{
				 applicantObj.setGender(false);
			 }
			 
			 if(rSingle.isChecked()){
				 applicantObj.setMaritalStatus("Single");
			 }else if(rMarried.isChecked()){
				 applicantObj.setMaritalStatus("Married");
			 }else{
				 applicantObj.setMaritalStatus("Divorced");
			 }
				 
			 if(rMarried.isChecked() || rDivorced.isChecked()){
				 applicantObj.setNoOfChild(Integer.parseInt(txtNoOfChild.getValue().toString()));
			 }
			 applicantObj.setVacancyCode(vacancyCode);
			 applicantObj.setNationality(selectedNationality);
			 applicantObj.setEmail((String)txtEmail.getValue());
			 applicantObj.setMobileNo((String)txtMobileNo.getValue());
			 applicantObj.setTelephoneNo((String)txtTelNo.getValue());
			 applicantObj.setAddress((String)tbAddr.getValue());
			 applicantObj.setPostalCode((String)txtPostalCode.getValue());
			 applicantObj.setCountry(selectedCountry);
			 applicantObj.setState((String)txtState.getValue());
			 applicantObj.setCity((String)txtCity.getValue());
			 applicantObj.setByPassValidation("yes");
			 personalCol.add(applicantObj);
		 
			 return new Forward(FORWARD_SAVED);
    	 }else
    		 return new Forward(FORWARD_ERROR);
	}
	
	public void onRequest(Event evt) {
		//important stage
		if(evt.getRequest().getSession().getAttribute("JAFtype")!=null && evt.getRequest().getSession().getAttribute("JAFtype").equals("edit")){
				btnReset.setHidden(true);
				//validation for editing status
				ApplicantObj editMethod = new ApplicantObj();
				boolean hasEditStatus=editMethod.validateEditStatus(evt);
				if(hasEditStatus){
					String vacancyCodeE=editMethod.getSessionData(evt, "vacancyCodeE");
					String applicantIdE=editMethod.getSessionData(evt, "applicantIdE");
					String codeStatusE=editMethod.getSessionData(evt, "codeStatusE");
					populateEditType(vacancyCodeE, applicantIdE, codeStatusE);
				}	
		}else{
			btnReset.setHidden(false);
			//adding status
			//evt.getRequest().getSession().removeAttribute("JAFtype");
			setPositionTitle(evt.getRequest().getSession().getAttribute("positionTitle").toString());
			populate();
		}
	}
	
	//adding status
	public void populate(){
		lblrPosApplied.setText(getPositionTitle());
	}
	
	//editing status
	public void populateEditType(String vacancyCodeE, String applicantIdE, String codeStatusE){
		Application app = Application.getInstance();
		RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
		HashMap dataMap= (HashMap)ram.loadApplicantPersonal(applicantIdE).iterator().next();
		
		//start populate the data
		lblrPosApplied.setText(dataMap.get("positionDesc").toString());
		
		txtName.setValue(dataMap.get("name")); 
		txtAge.setValue(dataMap.get("age"));
		dob.setValue(dataMap.get("dob"));
		txtNirc.setValue(dataMap.get("nirc"));
		
		if(dataMap.get("gender").equals("1"))
			rMale.setChecked(true);
		else 
			rFemale.setChecked(true);
		
		if(dataMap.get("maritalStatus").toString().equals("Single")){
			rSingle.setChecked(true);
		}else if(dataMap.get("maritalStatus").toString().equals("Married")){
			rMarried.setChecked(true);
			txtNoOfChild.setValue(dataMap.get("noOfChild"));
		}else if(dataMap.get("maritalStatus").toString().equals("Divorced")){
			rDivorced.setChecked(true);
			txtNoOfChild.setValue(dataMap.get("noOfChild"));
		}
		
		sbNationality.setSelectedOption(dataMap.get("nationality").toString());
		sbCountry.setSelectedOption(dataMap.get("country").toString());
		
		txtEmail.setValue(dataMap.get("email"));
		txtMobileNo.setValue(dataMap.get("mobileNo"));
		txtTelNo.setValue(dataMap.get("telephoneNo"));
		tbAddr.setValue(dataMap.get("address"));
		txtPostalCode.setValue(dataMap.get("postalCode"));
		txtState.setValue(dataMap.get("state"));
		txtCity.setValue(dataMap.get("city"));
	}
	
	//getter setter
	public ApplicantObj getApplicantObj() {
		return applicantObj;
	}

	public void setApplicantObj(ApplicantObj applicantObj) {
		this.applicantObj = applicantObj;
	}

	public Collection getPersonalCol() {
		return personalCol;
	}

	public void setPersonalCol(Collection personalCol) {
		this.personalCol = personalCol;
	}

	public String getVacancyCode() {
		return vacancyCode;
	}

	public void setVacancyCode(String vacancyCode) {
		this.vacancyCode = vacancyCode;
	}

	public String getPositionTitle() {
		return positionTitle;
	}

	public void setPositionTitle(String positionTitle) {
		this.positionTitle = positionTitle;
	}
	
}






























