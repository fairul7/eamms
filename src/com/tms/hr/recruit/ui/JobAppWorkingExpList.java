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

public class JobAppWorkingExpList extends Table{
	
	private Collection empSessionObj;
	
	public void onRequest(Event evt) {
		JobAppWorkingExp jAppWorkingExp = (JobAppWorkingExp) Application.getInstance().getWidget(evt.getRequest(), "jobApplication.tab1.panel3.Form"); //get class data.
		empSessionObj = jAppWorkingExp.getEmpCol();
	}
	
	public void init(){
		setWidth("100%");
		initTableList();
		setHidden(true);
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
			companyName.setUrlParam("empId");
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
			addAction(new TableAction("delete", app.getMessage("recruit.general.label.delete"), app.getMessage("recruit.applicantEmp.alert.delete")));
		}
		
		public Collection getTableRows() {
			return empSessionObj;
		}
		
		public int getTotalRowCount() {
			int total = empSessionObj.size();
			return total ;
		}
		
		public String getTableRowKey() {
			return "empId";
		}
		
		public Forward processAction(Event evt, String action, String[] selectedKeys) {
			Application app = Application.getInstance();
			JobAppWorkingExp jAppWorkingExp = (JobAppWorkingExp) Application.getInstance().getWidget(evt.getRequest(), "jobApplication.tab1.panel3.Form");
			if("delete".equals(action)){
				ApplicantObj applicantObj;
				for(int i=0; i<selectedKeys.length; i++){
					for(Iterator ite = empSessionObj.iterator(); ite.hasNext();){
						applicantObj = (ApplicantObj) ite.next();
						if(applicantObj.getEmpId().equals(selectedKeys[i])){
							ite.remove();
						}
					}
				}
				jAppWorkingExp.setEmpId(null);
			}
			return new Forward("delete");
		}
		
	}
	
}
