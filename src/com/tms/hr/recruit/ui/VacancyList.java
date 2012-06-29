package com.tms.hr.recruit.ui;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.stdui.DatePopupField;
import kacang.stdui.Label;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.hr.recruit.model.RecruitModule;
import com.tms.hr.recruit.model.VacancyObj;

public class VacancyList extends Table{
	
	public void init(){
		setWidth("100%");
		setModel(new VacancyListModel());
	}
	
	public class VacancyListModel extends TableModel{

		public VacancyListModel(){
			Application app = Application.getInstance();
			TableColumn vacancyCode = new TableColumn("vacancyCodeName", app.getMessage("recruit.general.label.vacancyCode"));
			vacancyCode.setUrlParam("vacancyCode");
			addColumn(vacancyCode);
			
			TableColumn totalApplicant = new TableColumn("totalApplicantNo", app.getMessage("recruit.general.label.totalApplicant"));
			//totalApplicant.setSortable(false);
			totalApplicant.setUrlParam("vacancyCodeApply");
			addColumn(totalApplicant);
			
			TableColumn position = new TableColumn("positionDesc", app.getMessage("recruit.general.label.position"));
			addColumn(position);
			
			TableColumn noOfPosition = new TableColumn("noOfPositionDetail", app.getMessage("recruit.general.label.noOfPosition"));
			//noOfPosition.setSortable(false);
			addColumn(noOfPosition);
			
			TableColumn priority = new TableColumn("priorityName", app.getMessage("recruit.general.label.priorty"));
			addColumn(priority);
			
			TableColumn starteDate = new TableColumn("startDate", app.getMessage("recruit.general.label.vacancyStartDate"));
			addColumn(starteDate);
			
			//filter textbox 
			addFilter(new TableFilter("vacancyCodeFilter"));
	        
			//filter selectbox
			Map priorityStatus = new LinkedHashMap();
			priorityStatus.put("---", app.getMessage("general.hierachy.selectPriority"));
			priorityStatus.put("0", app.getMessage("recruit.general.label.priortyNormal"));
			priorityStatus.put("1", app.getMessage("recruit.general.label.priortyUrgent"));
			TableFilter priorityFilter = new TableFilter("priorityFilter");
			SelectBox prioritySb = new SelectBox("prioritySb", priorityStatus, null, false, 1);
			priorityFilter.setWidget(prioritySb);
			addFilter(priorityFilter);
			
//			TableFilter priorityFilter = new TableFilter("priorityFilter");
//			SelectBox prioritySb = new SelectBox("prioritySb");
//			prioritySb.addOption("---", app.getMessage("general.hierachy.selectPriority"));
//			prioritySb.addOption("0", app.getMessage("recruit.general.label.priortyNormal"));
//			prioritySb.addOption("1", app.getMessage("recruit.general.label.priortyUrgent"));
//			priorityFilter.setWidget(prioritySb);
//			addFilter(priorityFilter);
			
			//add br-newLinse
			TableFilter lblbr = new TableFilter("lblbr");
			Label lblSpace = new Label("lblSpace","<br />");
			lblbr.setWidget(lblSpace);
			addFilter(lblbr);
			
			//filter start date
			TableFilter startDateFilter = new TableFilter("startDateFilter");
			DatePopupField startDate = new DatePopupField("startDate");
			startDate.setOptional(true);
			startDateFilter.setWidget(startDate);
			addFilter(startDateFilter);
			
			//filter endDate
			TableFilter endDateFilter = new TableFilter("endDateFilter");
			DatePopupField endDate = new DatePopupField("endDate");
			endDate.setOptional(true);
			endDateFilter.setWidget(endDate);
			addFilter(endDateFilter);
			
			//button
	        addAction(new TableAction("add", app.getMessage("recruit.general.label.add")));
	        addAction(new TableAction("delete", app.getMessage("recruit.general.label.delete"), app.getMessage("recruit.vacancy.alert.delete")));
		}
		
		public Collection getTableRows() {
			String strPriority="";
			
			String vacancyCode = (String)getFilterValue("vacancyCodeFilter");
			String startDate = (String) getFilterValue("startDateFilter");
			String endDate = (String) getFilterValue("endDateFilter");
			
			List selectedPriority = (List)getFilterValue("priorityFilter");
			if(selectedPriority!=null && selectedPriority.size() > 0){
				strPriority = selectedPriority.get(0).toString().startsWith("---")? null : selectedPriority.get(0).toString();
			}
			
			Application app = Application.getInstance();  
			RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
			
			return rm.findAllVacancyCodeFilter(vacancyCode, getSort(), isDesc(), getStart(), getRows(), strPriority, startDate, endDate);
		}
		
		public int getTotalRowCount() {
			 String strPriority="";
			 
			 String vacancyCode = (String)getFilterValue("vacancyCodeFilter");
			 String startDate = (String) getFilterValue("startDateFilter");
			 String endDate = (String) getFilterValue("endDateFilter");
				
			 List selectedPriority = (List)getFilterValue("priorityFilter");
			 if(selectedPriority!=null && selectedPriority.size() > 0){
				strPriority = selectedPriority.get(0).toString().startsWith("---")? null : selectedPriority.get(0).toString();
			 }
				
			Application app = Application.getInstance();  
			RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
			
			return rm.countAllVacancy(vacancyCode, strPriority, startDate, endDate); 
		}
		
		public String getTableRowKey() {
			return "vacancyCode";
		}
		  
		public Forward processAction(Event evt, String action, String[] selectedKeys) {
			 Application app = Application.getInstance();
			 if("add".equals(action)){
				 return new Forward("add");
			 }else if("delete".equals(action)){
				 RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
				 //auditObj
				 VacancyObj auditObj = new VacancyObj();
				 String actionTaken="";
				 for(int i=0; i<selectedKeys.length;i++ ){
					 //audit
					 actionTaken="Delete Vacancy and all the information related to it";
					 auditObj.setAndInsertAudit(selectedKeys[i], "", actionTaken);
					 rm.deleteVacancyAndApplicant(selectedKeys[i]); //dao sql
				 }
				 return new Forward("delete");
			 }
			 return new Forward("error");
		}
		  
	}
}
