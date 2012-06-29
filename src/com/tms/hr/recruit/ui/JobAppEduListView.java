package com.tms.hr.recruit.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import kacang.Application;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableModel;
import kacang.ui.Event;

import com.tms.hr.recruit.model.ApplicantObj;
import com.tms.hr.recruit.model.RecruitAppModule;
import com.tms.hr.recruit.model.RecruitModule;

public class JobAppEduListView extends Table{
	private String applicantId;
	private Collection eduCol=new ArrayList();
	
	public void onRequest(Event evt) {
		if(evt.getRequest().getSession().getAttribute("applicantId")!=null && !evt.getRequest().getSession().getAttribute("applicantId").equals("")){
			applicantId=evt.getRequest().getSession().getAttribute("applicantId").toString();
		}
	}
	
	public void init(){
		setWidth("100%");
		initTableList();
	}
	
	public void initTableList(){
		setShowPageSize(false);
		setModel(new EduListModelView());
	}

	public class EduListModelView extends TableModel{
		public EduListModelView(){
			setWidth("100%");
			Application app = Application.getInstance();
			TableColumn highEduLevel = new TableColumn("highEduLevelDesc", app.getMessage("recruit.general.label.highEduLevel"));
			addColumn(highEduLevel);
			
			TableColumn institute = new TableColumn("institute", app.getMessage("recruit.general.label.institute"));
			addColumn(institute);
			
			TableColumn courseTitle = new TableColumn("courseTitle", app.getMessage("recruit.general.label.courseTitle"));
			addColumn(courseTitle);
			
			TableColumn cdateStart = new TableColumn("startDate", app.getMessage("recruit.general.label.startDate"));
			addColumn(cdateStart);
			
			TableColumn cendStart = new TableColumn("endDate", app.getMessage("recruit.general.label.endDate"));
			addColumn(cendStart);
			
			TableColumn cgrade = new TableColumn("grade", app.getMessage("recruit.general.label.grade"));
			addColumn(cgrade);
		}
		
	public Collection getTableRows() {
		Application app = Application.getInstance();  
		RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
		eduCol = ram.lookUpEducationDetail(getSort(), isDesc(), getStart(), getRows(),  getApplicantId());
		
		JobAppEdu jobAppEdu = new JobAppEdu();
	
		for(Iterator ite=eduCol.iterator(); ite.hasNext();){
			ApplicantObj applicantObj = (ApplicantObj)ite.next();
			applicantObj.setHighEduLevelDesc(jobAppEdu.getEduMap().get(applicantObj.getHighEduLevel()).toString());
		}
		
		return eduCol;
	}
	
	public int getTotalRowCount() {
		int total = eduCol.size();
		return total ;
	}
	
	public String getTableRowKey() {
		return "";
	}
	
	/*public Forward processAction(Event evt, String action, String[] selectedKeys) {
	 * 
	}*/

	}
	//getter setter
	public String getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}

	public Collection getEduCol() {
		return eduCol;
	}

	public void setEduCol(Collection eduCol) {
		this.eduCol = eduCol;
	}
}