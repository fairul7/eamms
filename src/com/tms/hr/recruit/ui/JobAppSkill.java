package com.tms.hr.recruit.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.tms.hr.recruit.model.ApplicantObj;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.SelectBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

public class JobAppSkill extends Form{
	public static final String FORWARD_SAVED = "skillSave";
	public static final String FORWARD_ERROR = "error";
	
	private String skillId;
	private TextField txtskill;
	private TextField txtyearOfExp;
	private SelectBox sbProficiency;
	
	private Map mapProficiency = new LinkedHashMap();
	
	private Collection skillCol = new ArrayList();
	private  ApplicantObj applicantObj;
	 
	private Panel btnPanel;
	private Button btnSave;
	private Button btnReset;
	
	public void init(){
		initForm();
	}
	
	public void initForm(){
		setMethod("POST");
		setColumns(2);
		
		Application app = Application.getInstance();
    	
    	Label lblTopSkill= new Label("lblTopSkill", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.topSkills") + "</span>");
    	lblTopSkill.setAlign("right");
    	Label lblSpace = new Label("lblSpace","");
    	addChild(lblTopSkill);
    	addChild(lblSpace);
    	
    	Label lblSkill= new Label("lblSkill", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.skill") + "*</span>");
    	lblSkill.setAlign("right");
    	txtskill = new TextField("txtskill");
    	txtskill.setSize("30");
    	txtskill.setMaxlength("30");
    	txtskill.addChild(new ValidatorNotEmpty("txtskillVNE",app.getMessage("recruit.general.warn.empty")));
    	addChild(lblSkill);
    	addChild(txtskill);
    	
    	Label lblYearOfExp= new Label("lblYearOfExp", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.yearOfExp") + "*</span>");
    	lblYearOfExp.setAlign("right");
    	txtyearOfExp = new TextField("txtyearOfExp");
    	txtyearOfExp.setSize("20");
    	txtyearOfExp.setMaxlength("20");
    	txtyearOfExp.addChild(new ValidatorNotEmpty("txtyearOfExpVNE",app.getMessage("recruit.general.warn.empty")));
    	addChild(lblYearOfExp);
    	addChild(txtyearOfExp);
    	
    	Label lblProficiency= new Label("lblProficiency", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.proficiency") + "*</span>");
    	lblProficiency.setAlign("right");
    	sbProficiency = new SelectBox("sbHighEduLevel", mapProficiency, null, false, 1);
    	getSkillMapping(sbProficiency);
    	/*mapProficiency.put("---", app.getMessage("recruit.general.hierachy.selectProficiency") );
    	mapProficiency.put("s1", app.getMessage("recruit.general.label.advanced") );
    	mapProficiency.put("s2", app.getMessage("recruit.general.label.intermediate") );
    	mapProficiency.put("s3", app.getMessage("recruit.general.label.beginner") );
    	sbProficiency.setOptionMap(mapProficiency);*/
    	
    	addChild(lblProficiency);
    	addChild(sbProficiency);
    	
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
	}	
	
	public void getSkillMapping(SelectBox ff){
		mapProficiency = getSkillMap();
		ff.setOptionMap(mapProficiency);
	}
	
	public Map getSkillMap(){
		Application app = Application.getInstance();
		mapProficiency.put("---", app.getMessage("recruit.general.hierachy.selectProficiency") );
    	mapProficiency.put("s1", app.getMessage("recruit.general.label.advanced") );
    	mapProficiency.put("s2", app.getMessage("recruit.general.label.intermediate") );
    	mapProficiency.put("s3", app.getMessage("recruit.general.label.beginner") );
		
    	return mapProficiency;
	}
	
	public Forward onSubmit(Event evt) {
		Forward forward = super.onSubmit(evt); 
		String action = findButtonClicked(evt);
		boolean flag = false;
		applicantObj = new ApplicantObj();
		 if(action.equals(btnReset.getAbsoluteName())){
			 validationFalse();
			 setFieldReset();
			 //set obj flag status
			applicantObj.setByPassValidation("yes");
			setSkillId(null);//set id to null(param id)
		 }else{
			 JobAppPersonal jap = new JobAppPersonal();
			 boolean boolsbProficiency=jap.validateSelectBox(sbProficiency, "recruit.general.hierachy.selectProficiency", "---");
			 if(boolsbProficiency){
				 sbProficiency.setInvalid(true);
				 flag=true;
			 }
			
			 jap.validationText(txtskill,"txtskillVNE", "VNE",true);
			 jap.validationText(txtyearOfExp,"txtyearOfExpVNE", "VNE",true);
			 
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
			 String selectedProficiency= (String) sbProficiency.getSelectedOptions().keySet().iterator().next(); //set key
			 String proficiencyDesc = mapProficiency.get(selectedProficiency).toString(); //set value

			 if(getSkillId()!=null){
				 for(Iterator ite=skillCol.iterator();ite.hasNext();){
						ApplicantObj appObj= (ApplicantObj) ite.next();
						if(appObj.getSkillId().equals(getSkillId())){
							appObj.setProficiency(selectedProficiency);
							appObj.setProficiencyDesc(proficiencyDesc);
							appObj.setSkill((String) txtskill.getValue());
							appObj.setYearOfExpSkill((String) txtyearOfExp.getValue());
							appObj.setByPassValidation("yes");
							setSkillId(null); //set the param skillId to null
						}
				 }
			 }else{
				 UuidGenerator uuid = UuidGenerator.getInstance();
				 applicantObj.setSkillId(uuid.getUuid());
				 applicantObj.setProficiency(selectedProficiency);
				 applicantObj.setProficiencyDesc(proficiencyDesc);
				 applicantObj.setSkill((String) txtskill.getValue());
				 applicantObj.setYearOfExpSkill((String) txtyearOfExp.getValue());
				 applicantObj.setByPassValidation("yes");
				 skillCol.add(applicantObj);
			 }
			 setFieldReset();
		 
			 return new Forward(FORWARD_SAVED);
    	 }else
    		 return new Forward(FORWARD_ERROR);
	}
	
	public void onRequest(Event evt){
 		populateForm();
	}
	
	public void populateForm(){
		 if(getSkillId()!=null){
			 validationFalse();
			 ApplicantObj applicantObj;
			 for(Iterator ite=skillCol.iterator(); ite.hasNext(); ){
				 applicantObj = (ApplicantObj) ite.next();
				 if(applicantObj.getSkillId().equals(getSkillId())){
					 sbProficiency.setSelectedOption(applicantObj.getProficiency());
					 txtskill.setValue(applicantObj.getSkill());
					 txtyearOfExp.setValue(applicantObj.getYearOfExpSkill());
				 }
			 }
		 }
	}
	
	public void setFieldReset(){
		 txtskill.setValue("");
		 txtyearOfExp.setValue("");
		 sbProficiency.setSelectedOption("---");
	}
	
	public void validationFalse(){
		JobAppPersonal jap = new JobAppPersonal();
		jap.validationText(txtskill,"txtskillVNE", "VNE",false);
		jap.validationText(txtyearOfExp,"txtyearOfExpVNE", "VNE",false);
		txtskill.setInvalid(false);
		txtyearOfExp.setInvalid(false);
		setInvalid(false);
	}

	//getter and setter
	public Map getMapProficiency() {
		return mapProficiency;
	}

	public void setMapProficiency(Map mapProficiency) {
		this.mapProficiency = mapProficiency;
	}

	public Collection getSkillCol() {
		return skillCol;
	}

	public void setSkillCol(Collection skillCol) {
		this.skillCol = skillCol;
	}

	public String getSkillId() {
		return skillId;
	}

	public void setSkillId(String skillId) {
		this.skillId = skillId;
	}

	public ApplicantObj getApplicantObj() {
		return applicantObj;
	}

	public void setApplicantObj(ApplicantObj applicantObj) {
		this.applicantObj = applicantObj;
	}
	
}
