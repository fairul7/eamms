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
import kacang.stdui.FormField;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.SelectBox;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

public class JobAppLanguage extends Form{
	public static final String FORWARD_SAVED = "languageSave";
	public static final String FORWARD_ERROR = "error";
	
	private String languageId;
	private SelectBox sbLanguage;
	private SelectBox sbSpoken;
	private SelectBox sbWritten;
	
	private Collection languageCol = new ArrayList();
	private ApplicantObj applicantObj;
	
	private Map mapLanguage = new LinkedHashMap();
	private Map mapProficiency =  new LinkedHashMap();
	
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
    	Label lblAddLanguage= new Label("lblAddLanguage", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.addLanguageDesc") + "</span>");
    	lblAddLanguage.setAlign("right");
    	Label lblSpace = new Label("lblSpace","");
    	addChild(lblAddLanguage);
    	addChild(lblSpace);
    	
    	Label lbProficiencyDesc= new Label("lbProficiencyDesc", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.lblProficiency") + "</span>");
    	lbProficiencyDesc.setAlign("right");
    	Label lblSpace2 = new Label("lblSpace2","");
    	addChild(lbProficiencyDesc);
    	addChild(lblSpace2);
    	
    	Label lblLanguage= new Label("lblLanguage", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.language") + "*</span>");
    	lblLanguage.setAlign("right");
    	sbLanguage = new SelectBox("sbLanguage", mapLanguage, null, false, 1);
    	mapAppLanguageList(sbLanguage); //mapping language list 
    	
    	addChild(lblLanguage);
    	addChild(sbLanguage);
    	
    	Label lblSpoken= new Label("lblSpoken", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.spoken") + "*</span>");
    	lblSpoken.setAlign("right");
    	sbSpoken = new SelectBox("sbSpoken", mapProficiency, null, false, 1);
    	mapProficiencyList("selectProficiency", sbSpoken); //mapping proficiency list
    	
    	addChild(lblSpoken);
    	addChild(sbSpoken);
    	
    	Label lblWritten= new Label("lblWritten", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.written") + "*</span>");
    	lblWritten.setAlign("right");
    	sbWritten = new SelectBox("sbWritten", mapProficiency, null, false, 1);
    	mapProficiencyList("selectProficiency", sbWritten);
    	
    	addChild(lblWritten);
    	addChild(sbWritten);
    
    	Label lblSpace3 = new Label("lblSpace3","");
    	addChild(lblSpace3);
    	btnSave = new Button("btnSave", app.getMessage("recruit.general.label.save","Save"));
    	btnReset = new Button("btnReset", app.getMessage("recruit.general.label.reset","Reset"));
    	
    	btnPanel = new Panel("btnPanel");
    	btnPanel.setColspan(2);
    	btnPanel.addChild(btnSave);
    	btnPanel.addChild(btnReset);

    	addChild(btnPanel);
	}	
	
	public void mapAppLanguageList(SelectBox ff){
		mapLanguage = mapAppLanguage();
    	ff.setOptionMap(mapLanguage);
	}
	
	public Map mapAppLanguage(){
		Application app = Application.getInstance();
		mapLanguage.put("---", app.getMessage("recruit.general.hierachy.selectLanguage"));
    	mapLanguage.put("lan1", app.getMessage("recruit.general.label.lan1") );
    	mapLanguage.put("lan2", app.getMessage("recruit.general.label.lan2") );
    	mapLanguage.put("lan3", app.getMessage("recruit.general.label.lan3") );
    	mapLanguage.put("lan4", app.getMessage("recruit.general.label.lan4") );
    	mapLanguage.put("lan5", app.getMessage("recruit.general.label.lan5") );
    	mapLanguage.put("lan6", app.getMessage("recruit.general.label.lan6") );
    	mapLanguage.put("lan7", app.getMessage("recruit.general.label.lan7") );
    	mapLanguage.put("lan8", app.getMessage("recruit.general.label.lan8") );
    	mapLanguage.put("lan9", app.getMessage("recruit.general.label.lan9") );
    	mapLanguage.put("lan10", app.getMessage("recruit.general.label.lan10") );
    	mapLanguage.put("lan11", app.getMessage("recruit.general.label.lan11") );
    	mapLanguage.put("lan12", app.getMessage("recruit.general.label.lan12") );
    	mapLanguage.put("lan13", app.getMessage("recruit.general.label.lan13") );
    	mapLanguage.put("lan14", app.getMessage("recruit.general.label.lan14") );
    	mapLanguage.put("lan15", app.getMessage("recruit.general.label.lan15") );
    	mapLanguage.put("lan16", app.getMessage("recruit.general.label.lan16") );
    	mapLanguage.put("lan17", app.getMessage("recruit.general.label.lan17") );
    	mapLanguage.put("lan18", app.getMessage("recruit.general.label.lan18") );
    	mapLanguage.put("lan19", app.getMessage("recruit.general.label.lan19") );
    	mapLanguage.put("lan20", app.getMessage("recruit.general.label.lan20") );
    	mapLanguage.put("lan21", app.getMessage("recruit.general.label.lan21") );
		return mapLanguage;
	}
	
	public void mapProficiencyList(String type, SelectBox ff){
		Application app = Application.getInstance();
		mapProficiency.put("---", app.getMessage("recruit.general.hierachy."+ type));
		mapProficiency = mapProficiency();
		ff.setOptionMap(mapProficiency);
	}
	
	public Map mapProficiency(){
		Application app = Application.getInstance();
		mapProficiency.put("1", 1);
		mapProficiency.put("2", 2 );
		mapProficiency.put("3", 3 );
		mapProficiency.put("4", 4 );
		mapProficiency.put("5", 5 );
		mapProficiency.put("6", 6 );
		mapProficiency.put("7", 7 );
		mapProficiency.put("8", 8 );
		mapProficiency.put("9", 9 );
		mapProficiency.put("10", 10 );
		return mapProficiency;
	}
	
	public Forward onSubmit(Event evt) {
		Forward forward = super.onSubmit(evt); 
		String action = findButtonClicked(evt);
		boolean flag = false;
		applicantObj = new ApplicantObj();
		 if(action.equals(btnReset.getAbsoluteName())){
			 setFieldReset();
			 //set obj flag status
			 applicantObj.setByPassValidation("yes");
			 setLanguageId(null);//set the eduId to null(param) 
		 }else{
			 JobAppPersonal jap = new JobAppPersonal();
			 boolean boolsbLanguage=jap.validateSelectBox(sbLanguage, "recruit.general.hierachy.selectLanguage", "---");
			 if(boolsbLanguage){
				 sbLanguage.setInvalid(true);
				 flag=true;
			 }
			 
			 boolean boolsbSpoken=jap.validateSelectBox(sbSpoken, "recruit.general.hierachy.selectProficiency", "---");
			 if(boolsbSpoken){
				 sbSpoken.setInvalid(true);
				 flag=true;
			 }
			 
			 boolean boolsbWritten=jap.validateSelectBox(sbWritten, "recruit.general.hierachy.selectProficiency", "---");
			 if(boolsbWritten){
				 sbWritten.setInvalid(true);
				 flag=true;
			 }
			 
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
			 String selectedLanguage= (String) sbLanguage.getSelectedOptions().keySet().iterator().next(); //set key
			 String languageDesc = mapLanguage.get(selectedLanguage).toString(); //set value
			 
			 String selectedSpoken= (String) sbSpoken.getSelectedOptions().keySet().iterator().next(); //set key
			 String spokenDesc = mapProficiency.get(selectedSpoken).toString(); //set value
			 
			 String selectedWritten= (String) sbWritten.getSelectedOptions().keySet().iterator().next(); //set key
			 String writtenDesc = mapProficiency.get(selectedWritten).toString(); //set value
			 //ApplicantObj applicantObj = new ApplicantObj();
			 
			 if(getLanguageId()!=null){
				 for(Iterator ite=languageCol.iterator();ite.hasNext();){
					ApplicantObj appObj= (ApplicantObj) ite.next();
					if(appObj.getLanguageId().equals(getLanguageId())){
						appObj.setLanguage(selectedLanguage);
						appObj.setLanguageDesc(languageDesc);
						appObj.setSpoken(selectedSpoken);
						appObj.setSpokenDesc(spokenDesc);
						appObj.setWritten(selectedWritten);
						appObj.setWrittenDesc(writtenDesc);
						appObj.setByPassValidation("yes");
						setLanguageId(null);
					}
				 }
			 }else{
				 UuidGenerator uuid = UuidGenerator.getInstance();
				 applicantObj.setLanguageId(uuid.getUuid());
				 applicantObj.setLanguage(selectedLanguage);
				 applicantObj.setLanguageDesc(languageDesc);
				 applicantObj.setSpoken(selectedSpoken);
				 applicantObj.setSpokenDesc(spokenDesc);
				 applicantObj.setWritten(selectedWritten);
				 applicantObj.setWrittenDesc(writtenDesc);
				 applicantObj.setByPassValidation("yes");
				 languageCol.add(applicantObj);
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
		 if(getLanguageId()!=null){
			 //validationFalse();
			 ApplicantObj applicantObj;
			 for(Iterator ite=languageCol.iterator(); ite.hasNext(); ){
				 applicantObj = (ApplicantObj) ite.next();
				 if(applicantObj.getLanguageId().equals(getLanguageId())){
					 sbLanguage.setSelectedOption(applicantObj.getLanguage());
					 sbSpoken.setSelectedOption(applicantObj.getSpoken());
					 sbWritten.setSelectedOption(applicantObj.getWritten());
				 }
			 }
		 }
	}
	
	public void setFieldReset(){
		sbLanguage.setSelectedOption("---");
		sbSpoken.setSelectedOption("---");
		sbWritten.setSelectedOption("---");
	}
	
	//getter and setter
	public Collection getLanguageCol() {
		return languageCol;
	}

	public void setLanguageCol(Collection languageCol) {
		this.languageCol = languageCol;
	}

	public String getLanguageId() {
		return languageId;
	}

	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}

	public Map getMapLanguage() {
		return mapLanguage;
	}

	public void setMapLanguage(Map mapLanguage) {
		this.mapLanguage = mapLanguage;
	}

	public Map getMapProficiency() {
		return mapProficiency;
	}

	public void setMapProficiency(Map mapProficiency) {
		this.mapProficiency = mapProficiency;
	}

	public ApplicantObj getApplicantObj() {
		return applicantObj;
	}

	public void setApplicantObj(ApplicantObj applicantObj) {
		this.applicantObj = applicantObj;
	}
	
}
