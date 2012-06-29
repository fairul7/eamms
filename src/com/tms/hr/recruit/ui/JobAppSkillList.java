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

public class JobAppSkillList extends Table{
	
	private Collection skillSession;
	
	public void init(){
		setWidth("100%");
		initTableList();
	}
	
	public void onRequest(Event evt) {
		JobAppSkill jAppSkill = (JobAppSkill) Application.getInstance().getWidget(evt.getRequest(), "jobApplication.tab1.panel4.Form"); //get class data.
		skillSession = jAppSkill.getSkillCol();
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
			skill.setUrlParam("skillId");
			addColumn(skill);
			
			TableColumn yearOfExp = new TableColumn("yearOfExpSkill", app.getMessage("recruit.general.label.yearOfExp"));
			addColumn(yearOfExp);
			
			TableColumn proficiency = new TableColumn("proficiencyDesc", app.getMessage("recruit.general.label.proficiency"));
			addColumn(proficiency);
			
			//action
			addAction(new TableAction("delete", app.getMessage("recruit.general.label.delete"), app.getMessage("recruit.applicantSkill.alert.delete")));
		}
		
		public Collection getTableRows() {
			return skillSession;
		}
		
		public int getTotalRowCount() {
			int total = skillSession.size();
			return total ;
		}
		
		public String getTableRowKey() {
			return "skillId";
		}
		
		public Forward processAction(Event evt, String action, String[] selectedKeys) {
			Application app = Application.getInstance();
			JobAppSkill jAppSkill = (JobAppSkill) Application.getInstance().getWidget(evt.getRequest(), "jobApplication.tab1.panel4.Form");
			if("delete".equals(action)){
				ApplicantObj applicantObj;
				for(int i=0; i<selectedKeys.length; i++){
					for(Iterator ite = skillSession.iterator(); ite.hasNext();){
						applicantObj = (ApplicantObj) ite.next();
						if(applicantObj.getSkillId().equals(selectedKeys[i])){
							ite.remove();
						}
					}
				}
				jAppSkill.setSkillId(null);
			}
			return new Forward("delete");
		}
	}	
}
