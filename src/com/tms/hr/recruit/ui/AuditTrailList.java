package com.tms.hr.recruit.ui;

import java.util.Collection;
import java.util.Date;

import kacang.Application;
import kacang.stdui.DatePopupField;
import kacang.stdui.Label;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.hr.recruit.model.RecruitModule;
import com.tms.hr.recruit.model.SendMessage;

public class AuditTrailList extends Table{
	
	public void init(){
		setWidth("100%");
		setModel(new AuditTrailListModel());
	}
	
	public class AuditTrailListModel extends TableModel{
		public AuditTrailListModel(){
			Application app = Application.getInstance();
			/*TableColumn id = new TableColumn("userId", app.getMessage("recruit.general.label.userId"));
			addColumn(id);*/
			
			TableColumn userName = new TableColumn("username", app.getMessage("recruit.general.label.username"));
			addColumn(userName);
			
			TableColumn vacancyCode = new TableColumn("vacancyCode", app.getMessage("recruit.general.label.vacancyCode"));
			addColumn(vacancyCode);
			
			/*TableColumn applicantId = new TableColumn("applicantId", app.getMessage("recruit.general.label.applicantId"));
			addColumn(applicantId);*/
			
			TableColumn applicantName = new TableColumn("name", app.getMessage("recruit.general.label.applicantName"));
			addColumn(applicantName);
			
			TableColumn action = new TableColumn("actionTaken", app.getMessage("recruit.general.label.action"));
			addColumn(action);
			
			TableColumn dateTime = new TableColumn("auditDate", app.getMessage("recruit.general.label.dateTime"));
			TableFormat dateCreatedFormat = new TableDateFormat(app.getProperty("globalDatetimeLong"));
			dateTime.setFormat(dateCreatedFormat);
			addColumn(dateTime);
		
			//filter textbox 
			addFilter(new TableFilter("actionFilter"));
			
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
			
			addAction(new TableAction("genCSV", app.getMessage("recruit.general.label.genCSV")));
			addAction(new TableAction("delete", app.getMessage("recruit.general.label.delete"), app.getMessage("recruit.applicant.alert.delete")));
		}
		
		public Collection getTableRows() {
			String auditMisc = (String)getFilterValue("actionFilter");
			String startDate = (String)getFilterValue("startDateFilter");
			String endDate = (String)getFilterValue("endDateFilter");
			
			Application app = Application.getInstance();
			RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
						
			return rm.findAllAudit(getSort(), isDesc(), getStart(), getRows(), auditMisc, startDate, endDate);
		}
		
		public int getTotalRowCount() {
			String auditMisc = (String)getFilterValue("actionFilter");
			String startDate = (String) getFilterValue("startDateFilter");
			String endDate = (String) getFilterValue("endDateFilter");
			
			Application app = Application.getInstance();
			RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
			
			return rm.countAllAudit(auditMisc, startDate, endDate);
		}
		
		public String getTableRowKey() {
			return "auditId";
		}
		
		public Forward processAction(Event evt, String action, String[] selectedKeys) {
			Application app = Application.getInstance();
			RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
			if("genCSV".equals(action)){
				return new Forward("genCSV");
			}
			else if("delete".equals(action)){
				 for(int i=0; i<selectedKeys.length;i++ ){ 
					 rm.deleteAudit(selectedKeys[i]);
				 }
				 return new Forward("delete");
			 }
			 return new Forward("error");
		}
		
	}
}
