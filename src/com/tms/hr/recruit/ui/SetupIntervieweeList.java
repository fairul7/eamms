package com.tms.hr.recruit.ui;

import java.util.Collection;

import kacang.Application;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.ui.Event;

import com.tms.hr.recruit.model.RecruitAppModule;

public class SetupIntervieweeList extends Table{
	private Object sessionInterviewDateId;
	
	public void onRequest(Event evt) {
		sessionInterviewDateId=evt.getRequest().getSession().getAttribute("sessionInterviewDateId");
	}
	
	public void init(){
		setWidth("100%");
		initTableList();
	}
	
	public void initTableList(){
		setShowPageSize(false);
		setModel(new IntervieweeListModel());
	}
	
	public class IntervieweeListModel extends TableModel{
		public IntervieweeListModel(){
			setWidth("100%");
			Application app = Application.getInstance();
			
			TableColumn vacancyCode = new TableColumn("vacancyCode", app.getMessage("recruit.general.label.vacancyCode"));
			addColumn(vacancyCode);
			
			TableColumn name = new TableColumn("name", app.getMessage("recruit.general.label.applicant"));
			addColumn(name);
			
			TableColumn interviewDate = new TableColumn("interviewDateTime", app.getMessage("recruit.general.label.interviewDateTime"));
			TableFormat dateCreatedFormat = new TableDateFormat(app.getProperty("globalDatetimeLong"));
			interviewDate.setFormat(dateCreatedFormat);
			addColumn(interviewDate);
			
			TableColumn positionDesc = new TableColumn("positionDesc", app.getMessage("recruit.general.label.position"));
			addColumn(positionDesc);
			
			TableColumn noOfPositionAndTotal = new TableColumn("totalPosition", app.getMessage("recruit.general.label.noOfPosition"));
			addColumn(noOfPositionAndTotal);
		}
		
		public Collection getTableRows() {
			Collection idCol = (Collection) sessionInterviewDateId;
			Application app = Application.getInstance();  
			RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			
			return ram.findSelectedInterviewee(getSort(), isDesc(), getStart(), getRows(), idCol, "Scheduled");
		}
		
		public int getTotalRowCount() {
			Collection idCol = (Collection) sessionInterviewDateId;
			int total = idCol.size();
			return total ;
		}
		
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
	
}
