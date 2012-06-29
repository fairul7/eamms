package com.tms.hr.recruit.ui;

import java.util.Collection;
import java.util.Iterator;

import com.tms.hr.recruit.model.ApplicantObj;
import com.tms.hr.recruit.model.RecruitAppModule;

import kacang.Application;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;

public class JobAppLanguageListView  extends Table{
	
	private String applicantId;
	private Collection languageCol;
	
	public void init(){
		setWidth("100%");
		initTableList();
	}
	
	public void onRequest(Event evt) {
		if(evt.getRequest().getSession().getAttribute("applicantId")!=null && !evt.getRequest().getSession().getAttribute("applicantId").equals("")){
			applicantId=evt.getRequest().getSession().getAttribute("applicantId").toString();
		}
	}
	
	public void initTableList(){
		setShowPageSize(false);
		setModel(new LanguageListModel());
	}
	
	public class LanguageListModel extends TableModel{
		public LanguageListModel(){
			setWidth("100%");
			Application app = Application.getInstance();
			TableColumn language = new TableColumn("languageDesc", app.getMessage("recruit.general.label.language"));
			addColumn(language);
			
			TableColumn spoken = new TableColumn("spokenDesc", app.getMessage("recruit.general.label.spoken"));
			addColumn(spoken);
			
			TableColumn written = new TableColumn("writtenDesc", app.getMessage("recruit.general.label.written"));
			addColumn(written);
		}
		
	public Collection getTableRows() {
		Application app = Application.getInstance();  
		RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
		languageCol = ram.lookUpLanguageDetail(getSort(), isDesc(), getStart(), getRows(),  getApplicantId());
		
		JobAppLanguage jobAppLanguage = new JobAppLanguage();
		
		for(Iterator ite=languageCol.iterator(); ite.hasNext();){
			ApplicantObj applicantObj = (ApplicantObj)ite.next();
			applicantObj.setLanguageDesc(jobAppLanguage.mapAppLanguage().get(applicantObj.getLanguage()).toString());
			applicantObj.setSpokenDesc(jobAppLanguage.mapProficiency().get(applicantObj.getSpoken()).toString());
			applicantObj.setWrittenDesc(jobAppLanguage.mapProficiency().get(applicantObj.getWritten()).toString());
		}
		return languageCol;
	}
	
	public int getTotalRowCount() {
		int total = languageCol.size();
		return total ;
	}
	
	public String getTableRowKey() {
		return "";
	}
	
	/*public Forward processAction(Event evt, String action, String[] selectedKeys) {
		
	}*/

	}
	//getter setter
	public String getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}

	public Collection getLanguageCol() {
		return languageCol;
	}

	public void setLanguageCol(Collection languageCol) {
		this.languageCol = languageCol;
	}
}