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

public class JobAppSkillListView extends Table{
	
	private String applicantId;
	private Collection skillCol;
	
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
		setModel(new SkillListModel());
	}
	
	public class SkillListModel extends TableModel{
		public SkillListModel(){
			setWidth("100%");
			Application app = Application.getInstance();
			TableColumn skill = new TableColumn("skill", app.getMessage("recruit.general.label.skill"));
			addColumn(skill);
			
			TableColumn yearOfExp = new TableColumn("yearOfExpSkill", app.getMessage("recruit.general.label.yearOfExp"));
			addColumn(yearOfExp);
			
			TableColumn proficiency = new TableColumn("proficiencyDesc", app.getMessage("recruit.general.label.proficiency"));
			addColumn(proficiency);
		}
		
		public Collection getTableRows() {
			Application app = Application.getInstance();  
			RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			skillCol = ram.lookUpSkillDetail(getSort(), isDesc(), getStart(), getRows(),  getApplicantId());
			
			JobAppSkill jobAppSkill = new JobAppSkill();
			
			for(Iterator ite=skillCol.iterator(); ite.hasNext();){
				ApplicantObj applicantObj = (ApplicantObj)ite.next();
				applicantObj.setProficiencyDesc(jobAppSkill.getSkillMap().get(applicantObj.getProficiency()).toString());
			}
			
			return skillCol;
		}
		
		public int getTotalRowCount() {
			int total = skillCol.size();
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

	public Collection getSkillCol() {
		return skillCol;
	}

	public void setSkillCol(Collection skillCol) {
		this.skillCol = skillCol;
	}	
}
