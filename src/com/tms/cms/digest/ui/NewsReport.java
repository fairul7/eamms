package com.tms.cms.digest.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.DateField;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.cms.digest.model.DigestException;
import com.tms.cms.digest.model.DigestModule;

public class NewsReport extends Form{
	
	protected DateField startDate;
	protected DateField endDate;
	private Button submitBtn;
	
	private Collection results;
	
	public NewsReport(){
		super();
	}
	
	public void init(){
		initForm();
		results = new ArrayList();
	}
	
	public void onRequest(Event evt){
		DigestModule dm = (DigestModule)Application.getInstance().getModule(DigestModule.class);
		results = new ArrayList();
		
		try {
			results = dm.getNewsReport(startDate.getDate(), endDate.getDate(),Application.getInstance().getMessage("digest.label.newsFormat"),"Issue List");
		} catch (DigestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initForm(){
		Calendar now = Calendar.getInstance();
        endDate = new DatePopupField("endDate");
        endDate.setDate(now.getTime());
        addChild(endDate);
        now.set(Calendar.DAY_OF_MONTH,now.get(Calendar.DAY_OF_MONTH)-1);
        startDate = new DatePopupField("startDate");
        startDate.setDate(now.getTime());
        addChild(startDate);
        submitBtn = new Button("submit", "Submit");
		addChild(submitBtn);
	}
	
	public Forward onValidate(Event evt) {
		
		String button = findButtonClicked(evt);
		
		if (button.endsWith("submit")) {
			
			DigestModule dm = (DigestModule)Application.getInstance().getModule(DigestModule.class);
			results = new ArrayList();
			
			try {
				results = dm.getNewsReport(startDate.getDate(), endDate.getDate(),Application.getInstance().getMessage("digest.label.newsFormat"),"Issue List");
			} catch (DigestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return new Forward("success");
	}
	
	public String getDefaultTemplate(){
		return "digest/newsReport";
	}

	public DateField getEndDate() {
		return endDate;
	}

	public void setEndDate(DateField endDate) {
		this.endDate = endDate;
	}

	public Collection getResults() {
		return results;
	}

	public void setResults(Collection results) {
		this.results = results;
	}

	public DateField getStartDate() {
		return startDate;
	}

	public void setStartDate(DateField startDate) {
		this.startDate = startDate;
	}

	public Button getSubmitBtn() {
		return submitBtn;
	}

	public void setSubmitBtn(Button submitBtn) {
		this.submitBtn = submitBtn;
	}
	
	
}

