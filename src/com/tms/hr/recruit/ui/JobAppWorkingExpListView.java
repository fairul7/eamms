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

public class JobAppWorkingExpListView extends Table{
	
	private String applicantId;
	private Collection workingExpCol;
	
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
		setModel(new EmpListModel());
	}
	
	public class EmpListModel extends TableModel{
		public EmpListModel(){
			setWidth("100%");
			Application app = Application.getInstance();
			TableColumn companyName = new TableColumn("companyName", app.getMessage("recruit.general.label.companyName"));
			addColumn(companyName);
			
			TableColumn positionName = new TableColumn("positionName", app.getMessage("recruit.general.label.position"));
			addColumn(positionName);
			
			TableColumn positionLvlDesc = new TableColumn("positionLvlDesc", app.getMessage("recruit.general.label.positionLvl"));
			addColumn(positionLvlDesc);
			
			TableColumn dateJoin = new TableColumn("startDate", app.getMessage("recruit.general.label.dateJoin"));
			addColumn(dateJoin);
			
			TableColumn dateLeft = new TableColumn("endDate", app.getMessage("recruit.general.label.dateLeft"));
			addColumn(dateLeft);
			
			TableColumn reasonForLeave = new TableColumn("reasonForLeave", app.getMessage("recruit.general.label.reasonForLeave"));
			addColumn(reasonForLeave);
			
			//action
		}
		
		public Collection getTableRows() {
			Application app = Application.getInstance();  
			RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			workingExpCol = ram.lookUpWorkingExpDetail(getSort(), isDesc(), getStart(), getRows(),  getApplicantId());
			
			JobAppWorkingExp jobAppWorkingExp = new JobAppWorkingExp();
			
			for(Iterator ite=workingExpCol.iterator(); ite.hasNext();){
				ApplicantObj applicantObj = (ApplicantObj)ite.next();
				applicantObj.setPositionLvlDesc(jobAppWorkingExp.getPositionMap().get(applicantObj.getPositionLvl()).toString());
			}
			
			return workingExpCol;
		}
		
		public int getTotalRowCount() {
			int total = workingExpCol.size();
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

	public Collection getWorkingExpCol() {
		return workingExpCol;
	}

	public void setWorkingExpCol(Collection workingExpCol) {
		this.workingExpCol = workingExpCol;
	}
	
}
