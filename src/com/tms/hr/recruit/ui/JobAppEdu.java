package com.tms.hr.recruit.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.FormField;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.SelectBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

import com.tms.hr.recruit.model.ApplicantObj;
import com.tms.hr.recruit.model.RecruitAppModule;

public class JobAppEdu extends Form{
	public static final String FORWARD_SAVED = "eduSave";
	public static final String FORWARD_ERROR = "error";
	
	private String eduId;
	private SelectBox sbHighEduLevel;
	private TextField txtInstitute;
	private TextField txtCourseTitle;
	private DatePopupField startDate;
	private DatePopupField endDate;
	private TextField txtGrade;
	
	private Collection eduCol = new ArrayList();
	private  ApplicantObj applicantObj;
	
	private Map mHighEduLevel = new LinkedHashMap();
	
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
    	
    	Label lblEduInfo= new Label("lblEduInfo", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.eduInfo") + "</span>");
    	lblEduInfo.setAlign("right");
    	Label lblSpace = new Label("lblSpace","");
    	addChild(lblEduInfo);
    	addChild(lblSpace);
    	
    	Label lblHighEduLevel= new Label("lblHighEduLevel", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.highEduLevel") + "*</span>");
    	lblHighEduLevel.setAlign("right");
    	sbHighEduLevel = new SelectBox("sbHighEduLevel", mHighEduLevel, null, false, 1);
 
    	getEduMapping(sbHighEduLevel); //mapping the edu level
    	
    	addChild(lblHighEduLevel);
    	addChild(sbHighEduLevel);
    	
    	Label lblInstitute= new Label("lblInstitute", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.institute") + "*</span>");
    	lblInstitute.setAlign("right");
    	txtInstitute = new TextField("txtInstitute");
    	txtInstitute.setSize("30");
    	txtInstitute.setMaxlength("40");
    	txtInstitute.addChild(new ValidatorNotEmpty("txtInstituteVNE",app.getMessage("recruit.general.warn.empty")));
    	addChild(lblInstitute);
    	addChild(txtInstitute);
    	
    	Label lblCourseTitle= new Label("lblCourseTitle", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.courseTitle") + "*</span>");
    	lblCourseTitle.setAlign("right");
    	txtCourseTitle = new TextField("txtCourseTitle");
    	txtCourseTitle.setSize("30");
    	txtCourseTitle.setMaxlength("40");
    	txtCourseTitle.addChild(new ValidatorNotEmpty("txtCourseTitleVNE",app.getMessage("recruit.general.warn.empty")));
    	addChild(lblCourseTitle);
    	addChild(txtCourseTitle);
    	
    	Label lblStartDate= new Label("lblStartDate", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.startDate") + "*</span>");
    	lblStartDate.setAlign("right");
    	startDate = new DatePopupField("startDate");
    	addChild(lblStartDate);
    	addChild(startDate);
		
    	Label lblendDate= new Label("lblendDate", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.endDate") + "*</span>");
    	lblendDate.setAlign("right");
    	endDate = new DatePopupField("endDate");
    	addChild(lblendDate);
    	addChild(endDate);
    	
    	Label lblGrade= new Label("lblGrade", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.grade") + "*</span>");
    	lblGrade.setAlign("right");
    	txtGrade = new TextField("txtGrade");
    	txtGrade.setSize("15");
    	txtGrade.setMaxlength("20");
    	txtGrade.addChild(new ValidatorNotEmpty("txtGradeVNE",app.getMessage("recruit.general.warn.empty")));
    	addChild(lblGrade);
    	addChild(txtGrade);
    	
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
	
	public void getEduMapping(SelectBox ff){
		mHighEduLevel = getEduMap();
    	ff.setOptionMap(mHighEduLevel);
	}
	
	public Map getEduMap(){
		Application app = Application.getInstance();
		mHighEduLevel.put("---", app.getMessage("recruit.general.hierachy.selectHighLevel") );
    	mHighEduLevel.put("edu1", app.getMessage("recruit.general.label.primary") );
    	mHighEduLevel.put("edu2", app.getMessage("recruit.general.label.higher") );
    	mHighEduLevel.put("edu3", app.getMessage("recruit.general.label.professional") );
    	mHighEduLevel.put("edu4", app.getMessage("recruit.general.label.diploma") );
    	mHighEduLevel.put("edu5", app.getMessage("recruit.general.label.advanced") );
    	mHighEduLevel.put("edu6", app.getMessage("recruit.general.label.bachelor") );
    	mHighEduLevel.put("edu7", app.getMessage("recruit.general.label.postGraduate") );
    	mHighEduLevel.put("edu8", app.getMessage("recruit.general.label.professionalDegree") );
    	mHighEduLevel.put("edu9", app.getMessage("recruit.general.label.master") );
    	mHighEduLevel.put("edu10", app.getMessage("recruit.general.label.phd") );
    	
    	return mHighEduLevel;
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
			setEduId(null);//set the eduId to null(param) 
		 }else{
			 JobAppPersonal jap = new JobAppPersonal();
			 boolean boolsbHighEduLevel=jap.validateSelectBox(sbHighEduLevel, "recruit.general.hierachy.selectHighLevel", "---");
			 if(boolsbHighEduLevel){
				 sbHighEduLevel.setInvalid(true);
				 flag=true;
			 }
			
			 jap.validationText(txtInstitute,"txtInstituteVNE", "VNE",true);
			 jap.validationText(txtCourseTitle,"txtCourseTitleVNE", "VNE",true);
			 jap.validationText(txtGrade,"txtGradeVNE", "VNE",true);
			 
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
			 String selectedHighEduLevel= (String) sbHighEduLevel.getSelectedOptions().keySet().iterator().next(); //set key
			 String HighEduLevelDesc = mHighEduLevel.get(selectedHighEduLevel).toString(); //set value
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			 
			 if(getEduId()!=null){
				 for(Iterator ite=eduCol.iterator();ite.hasNext();){
						ApplicantObj appObj= (ApplicantObj) ite.next();
						if(appObj.getEduId().equals(getEduId())){
							appObj.setHighEduLevel(selectedHighEduLevel); //set key
							appObj.setHighEduLevelDesc(HighEduLevelDesc); //set value
							appObj.setInstitute((String)txtInstitute.getValue());
							appObj.setCourseTitle((String)txtCourseTitle.getValue());
							appObj.setStartDate(sdf.format(startDate.getDate()));
							appObj.setEndDate(sdf.format(endDate.getDate()));
							appObj.setGrade((String)txtGrade.getValue());
							appObj.setByPassValidation("yes");
							setEduId(null);
						}
				 }		
			 }else{
				 UuidGenerator uuid = UuidGenerator.getInstance();
				 applicantObj.setEduId(uuid.getUuid());
				 applicantObj.setHighEduLevel(selectedHighEduLevel); //set key
				 applicantObj.setHighEduLevelDesc(HighEduLevelDesc); //set value
				 applicantObj.setInstitute((String)txtInstitute.getValue());
				 applicantObj.setCourseTitle((String)txtCourseTitle.getValue());
				 applicantObj.setStartDate(sdf.format(startDate.getDate()));
				 applicantObj.setEndDate(sdf.format(endDate.getDate()));
				 applicantObj.setGrade((String)txtGrade.getValue());
				 applicantObj.setByPassValidation("yes");
				 eduCol.add(applicantObj);
			 }
			 setFieldReset();
		 
			 return new Forward(FORWARD_SAVED);
    	 }else
    		 return new Forward(FORWARD_ERROR);
	}
	
	public void onRequest(Event evt){
		Application app = Application.getInstance();
		RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
		//editing status
		if(evt.getRequest().getSession().getAttribute("JAFtype")!=null && evt.getRequest().getSession().getAttribute("JAFtype").equals("edit")){
			if(getEduId()!=null){
				boolean hasEduId = ram.findJobAppEduKey(getEduId());
				if(hasEduId){
					ApplicantObj applicantObj= (ApplicantObj)ram.findJobAppEduObj(getEduId());
					if(applicantObj.getEduId().equals(getEduId())){
						 sbHighEduLevel.setSelectedOption(applicantObj.getHighEduLevel());
						 txtInstitute.setValue(applicantObj.getInstitute());
						 txtCourseTitle.setValue(applicantObj.getCourseTitle());
						 startDate.setValue(applicantObj.getStartDate());
						 endDate.setValue(applicantObj.getEndDate());
						 txtGrade.setValue(applicantObj.getGrade());
					 }
				}else{
					populateForm();
				}
			}	
		}else{
			//adding status
			populateForm();
		}
	}
	
	public void populateForm(){
		 if(getEduId()!=null){
			 validationFalse();
			 ApplicantObj applicantObj;
			 for(Iterator ite=eduCol.iterator(); ite.hasNext(); ){
				 applicantObj = (ApplicantObj) ite.next();
				 if(applicantObj.getEduId().equals(getEduId())){
					 sbHighEduLevel.setSelectedOption(applicantObj.getHighEduLevel());
					 txtInstitute.setValue(applicantObj.getInstitute());
					 txtCourseTitle.setValue(applicantObj.getCourseTitle());
					 startDate.setValue(applicantObj.getStartDate());
					 endDate.setValue(applicantObj.getEndDate());
					 txtGrade.setValue(applicantObj.getGrade());
				 }
			 }
		 }
	}
	
	public void setFieldReset(){
		 sbHighEduLevel.setSelectedOption("---");
		 txtInstitute.setValue("");
		 txtCourseTitle.setValue("");
		 startDate.setValue("");
		 endDate.setValue("");
		 txtGrade.setValue("");
	}
	
	public void validationFalse(){
		 JobAppPersonal jap = new JobAppPersonal();
		 jap.validationText(txtInstitute,"txtInstituteVNE", "VNE",false);
		 jap.validationText(txtCourseTitle,"txtCourseTitleVNE", "VNE",false);
		 jap.validationText(txtGrade,"txtGradeVNE", "VNE",false);
		 txtInstitute.setInvalid(false);
		 txtCourseTitle.setInvalid(false);
		 txtGrade.setInvalid(false);
		 startDate.setInvalid(false);
		 endDate.setInvalid(false);
	}
	
	//getter and setter
	public Map getMHighEduLevel() {
		return mHighEduLevel;
	}

	public void setMHighEduLevel(Map highEduLevel) {
		mHighEduLevel = highEduLevel;
	}

	public Collection getEduCol() {
		return eduCol;
	}

	public void setEduCol(Collection eduCol) {
		this.eduCol = eduCol;
	}

	public String getEduId() {
		return eduId;
	}

	public void setEduId(String eduId) {
		this.eduId = eduId;
	}

	public ApplicantObj getApplicantObj() {
		return applicantObj;
	}

	public void setApplicantObj(ApplicantObj applicantObj) {
		this.applicantObj = applicantObj;
	}
	
}