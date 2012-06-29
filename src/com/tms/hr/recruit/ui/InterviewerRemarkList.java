package com.tms.hr.recruit.ui;

import java.util.Collection;

import com.tms.hr.recruit.model.RecruitAppModule;

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

public class InterviewerRemarkList extends Table{
	
	public void init(){
		setWidth("100%");
		initTableList();
	}
	
	public void initTableList(){
		setShowPageSize(false);
		setModel(new InterviewerRemarkListModel());
	}
	
	public class InterviewerRemarkListModel extends TableModel{
		public InterviewerRemarkListModel(){
			Application app = Application.getInstance();
			//TableColumn vacancyCode = new TableColumn("vacancyCode", app.getMessage("recruit.general.label.vacancyCode"));
			//vacancyCode.setUrlParam("vacancyCode");
			//addColumn(vacancyCode);
			
			TableColumn applicantName = new TableColumn("paramCombine", app.getMessage("recruit.general.label.applicant"));
			applicantName.setFormat(new 
					TableFormat(){
						public String format(Object value) {
							String paramCombine = value.toString();
							String[] keys = paramCombine.split("/");
							
							return "<a href=interviewerRemarkForm.jsp?applicantId="+keys[0] 
							+"&interviewDateId="+keys[1] +"&vacancyCode="+keys[3]+"&flag="+keys[4]+">" + keys[2] + "</a>";
						}
					});
			
			addColumn(applicantName);
			
			TableColumn positionApplied = new TableColumn("positionDesc", app.getMessage("recruit.general.label.position"));
			addColumn(positionApplied);
			
			TableColumn nextInterviewDateTime = new TableColumn("interviewDateTime", app.getMessage("recruit.general.label.interviewDateTimes"));
			TableFormat dateCreatedFormat = new TableDateFormat(app.getProperty("globalDatetimeLong"));
			nextInterviewDateTime.setFormat(dateCreatedFormat);
			addColumn(nextInterviewDateTime);
			
			TableColumn stageOfInterview = new TableColumn("interviewStageName", app.getMessage("recruit.general.label.stageOfInterview"));
			addColumn(stageOfInterview);
			
			TableColumn dateAssigned = new TableColumn("dateAssigned", app.getMessage("recruit.general.label.dateAssigned"));
			addColumn(dateAssigned);
			
			TableColumn remark = new TableColumn("remark", app.getMessage("recruit.general.label.remark"));
			addColumn(remark);
			
			//filter textbox 
			addFilter(new TableFilter("titleFilter"));
			
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
			//addAction(new TableAction("delete", app.getMessage("recruit.general.label.delete"), app.getMessage("recruit.applicant.alert.delete")));
		}
		
		public Collection getTableRows() {
			Application app = Application.getInstance();
			RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			
			String interviewerId=app.getCurrentUser().getId(); //current user
			String applicantMisc = (String)getFilterValue("titleFilter");
			String startDate = (String) getFilterValue("startDateFilter");
			String endDate = (String) getFilterValue("endDateFilter");
			
			return ram.findInterviewer(getSort(), isDesc(), getStart(), getRows(), interviewerId, applicantMisc, startDate, endDate);
		}
		
		public int getTotalRowCount() {
			Application app = Application.getInstance();  
			RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			
			String interviewerId=app.getCurrentUser().getId(); //current user
			String applicantMisc = (String)getFilterValue("titleFilter");
			String startDate = (String) getFilterValue("startDateFilter");
			String endDate = (String) getFilterValue("endDateFilter");
				
			return ram.countFindInterviewer(interviewerId, applicantMisc,  startDate, endDate); 
		}
		
		/*public Forward processAction(Event evt, String action, String[] selectedKeys) {
			ApplicantList appList = new ApplicantList();
			if("delete".equals(action)){
				 for(int i=0; i<selectedKeys.length;i++ ){
					 
				 }
				 return new Forward("deleted"); 
			}	 
			return new Forward("error"); 
		}	*/
		
		public String getTableRowKey() {
			//return "interviewDateId";
			return "";
		}
	}
	
}
