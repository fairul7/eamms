package com.tms.hr.recruit.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.ButtonGroup;
import kacang.stdui.CountrySelectBox;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Radio;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.hr.recruit.model.ApplicantObj;
import com.tms.hr.recruit.model.RecruitAppModule;

public class JobAppWorkingExpTypeView  extends Form{
	private String applicantId;
	private Label lblRecFreshMsg;
	private boolean hasWorkingExp=false;
	
	public void onRequest(Event evt) {
		if(evt.getRequest().getSession().getAttribute("applicantId")!=null && !evt.getRequest().getSession().getAttribute("applicantId").equals("")){
			applicantId=evt.getRequest().getSession().getAttribute("applicantId").toString();
			populateData();
		}
	}
	
	public void init(){
		initForm();
	}
	
	public void initForm(){
		setMethod("POST");
		setColumns(2);
		
    	Application app = Application.getInstance();
    	Label lblWorkingExp= new Label("lblWorkingExp", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.workingExp") + "</span>");
    	lblWorkingExp.setAlign("right");
    	addChild(lblWorkingExp);
    	lblRecFreshMsg = new Label("lblRecFreshMsg","");
    	addChild(lblRecFreshMsg);
	}
	
	public void populateData(){
		if(getApplicantId()!=null){
			Application app = Application.getInstance();
			RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			
			Collection col=ram.loadApplicantPersonal(getApplicantId());
			HashMap map = (HashMap) col.iterator().next();
				
			if(!map.get("yearOfWorkingExp").equals("Fresh Graduate")){
				setHasWorkingExp(true);
			}
			
			lblRecFreshMsg.setText(map.get("yearOfWorkingExp").toString());
		}
	}
	
	//getter setter
	public String getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}
	
	public boolean isHasWorkingExp() {
		return hasWorkingExp;
	}

	public void setHasWorkingExp(boolean hasWorkingExp) {
		this.hasWorkingExp = hasWorkingExp;
	}

}
