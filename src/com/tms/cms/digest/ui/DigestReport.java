package com.tms.cms.digest.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.DateField;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.SelectBox;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.cms.digest.model.DigestException;
import com.tms.cms.digest.model.DigestModule;

public class DigestReport extends Form{
	
	protected DateField startDate;
	protected DateField endDate;
	protected SelectBox select;
	private Button submitBtn;
	protected String type;
	private Collection results;
	
	public DigestReport(){
		super();
	}
	
	public void init(){
		initForm();
		results = new ArrayList();
		type="S";
	}
	
	public void onRequest(Event evt){
		DigestModule dm = (DigestModule)Application.getInstance().getModule(DigestModule.class);
		results = new ArrayList();
		
		try {
			List list= (List) select.getValue();
			String types=(String)list.get(0);
			type=types;
			if("I".equals(types)){
				types="Issue List";
			}else if("S".equals(types)){
				types="Summaries List";
			}
			results = dm.getNewsReport(startDate.getDate(), endDate.getDate(),Application.getInstance().getMessage("digest.label.digestFormat"),types);
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
        select= new SelectBox("select");
        select.setOptions("I="+Application.getInstance().getMessage("digest.email.topicList"));
        select.setOptions("S="+Application.getInstance().getMessage("digest.email.issueList"));
        select.setSelectedOption("S");
        addChild(select);
        submitBtn = new Button("submit", "Submit");
		addChild(submitBtn);
	}
	public Forward onSubmit(Event evt) {
		Forward fwd=super.onSubmit(evt);
		List list= (List) select.getValue();
		String types=(String)list.get(0);
		type=types;
		return fwd;
	}
	public Forward onValidate(Event evt) {
		Forward fwd= super.onValidate(evt);
		String button = findButtonClicked(evt);
		
		if (button.endsWith("submit")) {
			
			DigestModule dm = (DigestModule)Application.getInstance().getModule(DigestModule.class);
			results = new ArrayList();
			
			try {
				List list= (List) select.getValue();
				String types=(String)list.get(0);
				type=types;
				if("I".equals(types)){
					types="Issue List";
				}else if("S".equals(types)){
					types="Summaries List";
				}
				results = dm.getNewsReport(startDate.getDate(), endDate.getDate(),Application.getInstance().getMessage("digest.label.digestFormat"),types);
			} catch (DigestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return fwd;
	}
	
	public String getDefaultTemplate(){
		return "digest/digestReport";
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

	public SelectBox getSelect() {
		return select;
	}

	public void setSelect(SelectBox select) {
		this.select = select;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}		
}


