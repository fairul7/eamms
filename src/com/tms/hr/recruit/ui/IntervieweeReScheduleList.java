package com.tms.hr.recruit.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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

import com.tms.hr.recruit.model.ApplicantObj;
import com.tms.hr.recruit.model.RecruitAppModule;
import com.tms.hr.recruit.model.SendMessage;
import com.tms.hr.recruit.model.VacancyObj;

public class IntervieweeReScheduleList extends Table{
	private Object sessionInterviewDateId;
	//private Object sessionApplicantId;
	
	public void onRequest(Event evt) {
		init();
		sessionInterviewDateId=evt.getRequest().getSession().getAttribute("sessionInterviewDateId");
		//sessionApplicantId=evt.getRequest().getSession().getAttribute("sessionApplicantId");
	}
	
	public void init(){
		setWidth("100%");
		setModel(new IntervieweeReScheduleListModel());
	}
	
	public class IntervieweeReScheduleListModel extends TableModel{
		private Collection interviewDateCol = new ArrayList();
	
		public IntervieweeReScheduleListModel(){
			Application app = Application.getInstance();
			TableColumn applicantName = new TableColumn("paramCombine", app.getMessage("recruit.general.label.applicant"));
			applicantName.setFormat(new 
			TableFormat(){
				public String format(Object value) {
					String paramCombine = value.toString();
					String[] keys = paramCombine.split("/");
					return "<a href='#' onClick=javascript:window.open('popupReSchedule.jsp?interviewDateId="+keys[0]+"&applicantId="+keys[1]+"','blank','toolbar=no,width=500,height=330,scrollbars=yes'); return false; >"+keys[2]+"</a>";					
					//return "<a href=interviewResult.jsp?interviewDateId="+keys[0] +">" + keys[2] + "</a>";
				}
			});
			
			addColumn(applicantName);
			
			TableColumn vacancyCode = new TableColumn("vacancyCode", app.getMessage("recruit.general.label.vacancyCode"));
			addColumn(vacancyCode);
			
			TableColumn positionApplied = new TableColumn("positionDesc", app.getMessage("recruit.general.label.position"));
			addColumn(positionApplied);
			
			TableColumn nextInterviewDateTime = new TableColumn("interviewDateTime", app.getMessage("recruit.general.label.interviewDateTime"));
			TableFormat dateCreatedFormat = new TableDateFormat(app.getProperty("globalDatetimeLong"));
			nextInterviewDateTime.setFormat(dateCreatedFormat);
			addColumn(nextInterviewDateTime);
			
			TableColumn stageOfInterview = new TableColumn("interviewStageName", app.getMessage("recruit.general.label.stageOfInterview"));
			addColumn(stageOfInterview);
			
			TableColumn status = new TableColumn("applicantStatus", app.getMessage("recruit.general.label.status"));
			addColumn(status);
			
			//filter textbox 
			addFilter(new TableFilter("applicantFilter"));
		
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
		}
		
		public Collection getTableRows() {
			String applicantMisc = (String)getFilterValue("applicantFilter");
			String startDate = (String) getFilterValue("startDateFilter");
			String endDate = (String) getFilterValue("endDateFilter");
		
			Collection idCol = (Collection) sessionInterviewDateId;
			Application app = Application.getInstance();  
			RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			
			return ram.findReScheduleInterviewee(getSort(), isDesc(), getStart(), getRows(), applicantMisc, startDate, endDate, idCol, "Scheduled");//can use this SQL query
		}
		
		public int getTotalRowCount() {
			String applicantMisc = (String)getFilterValue("applicantFilter");
			String startDate = (String) getFilterValue("startDateFilter");
			String endDate = (String) getFilterValue("endDateFilter");
			
			Application app = Application.getInstance();  
			RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			Collection idCol = (Collection) sessionInterviewDateId;
			
			return ram.countAllReSchedule(applicantMisc,  startDate, endDate,  idCol, "Scheduled"); 
			/*Collection idCol = (Collection) sessionInterviewDateId;
			int total = idCol.size();
			return total ;*/
		}
		
		/*public Forward processAction(Event evt, String action, String[] selectedKeys) {	
			return new Forward("error"); 
		}*/
		
		public String getTableRowKey() {
			return "";
		}
	}
	
	public Object getSessionInterviewDateId() {
		return sessionInterviewDateId;
	}

	public void setSessionInterviewDateId(Object sessionInterviewDateId) {
		this.sessionInterviewDateId = sessionInterviewDateId;
	}

	/*public Object getSessionApplicantId() {
		return sessionApplicantId;
	}

	public void setSessionApplicantId(Object sessionApplicantId) {
		this.sessionApplicantId = sessionApplicantId;
	}*/
	
}
