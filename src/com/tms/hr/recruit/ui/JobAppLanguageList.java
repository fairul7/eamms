package com.tms.hr.recruit.ui;

import java.util.Collection;
import java.util.Iterator;

import com.tms.hr.recruit.model.ApplicantObj;

import kacang.Application;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;

public class JobAppLanguageList  extends Table{
	
	private Collection languageObjSession;
	
	public void init(){
		setWidth("100%");
		initTableList();
	}
	
	public void onRequest(Event evt) {
		JobAppLanguage jAppLanguage = (JobAppLanguage) Application.getInstance().getWidget(evt.getRequest(), "jobApplication.tab1.panel5.Form"); //get class data.
		languageObjSession = jAppLanguage.getLanguageCol();
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
			language.setUrlParam("languageId");
			addColumn(language);
			
			TableColumn spoken = new TableColumn("spokenDesc", app.getMessage("recruit.general.label.spoken"));
			addColumn(spoken);
			
			TableColumn written = new TableColumn("writtenDesc", app.getMessage("recruit.general.label.written"));
			addColumn(written);
			
			//action
			addAction(new TableAction("delete", app.getMessage("recruit.general.label.delete"), app.getMessage("recruit.applicantLanguage.alert.delete")));
		}
		
	public Collection getTableRows() {
		return languageObjSession;
	}
	
	public int getTotalRowCount() {
		int total = languageObjSession.size();
		return total ;
	}
	
	public String getTableRowKey() {
		return "languageId";
	}
	
	public Forward processAction(Event evt, String action, String[] selectedKeys) {
		Application app = Application.getInstance();
		JobAppLanguage jAppLanguage = (JobAppLanguage) Application.getInstance().getWidget(evt.getRequest(), "jobApplication.tab1.panel5.Form");
		if("delete".equals(action)){
			ApplicantObj applicantObj;
			for(int i=0; i<selectedKeys.length; i++){
				for(Iterator ite = languageObjSession.iterator(); ite.hasNext();){
					applicantObj = (ApplicantObj) ite.next();
					if(applicantObj.getLanguageId().equals(selectedKeys[i])){
						ite.remove();
					}
				}
			}
			jAppLanguage.setLanguageId(null);
		}
		return new Forward("delete");
	}

	}
}