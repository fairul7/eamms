package com.tms.hr.recruit.ui;

import java.util.Collection;

import kacang.Application;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;

import com.tms.hr.recruit.model.RecruitAppModule;

public class InterviewHistory extends Table{
	private String applicantId;
	private String interviewDateId;
	
	public void init(){
		setWidth("100%");
		initTableList();
	}
	
	public void initTableList(){
		setShowPageSize(false);
		setModel(new InterviewHistoryListModel());
	}
	
	public class InterviewHistoryListModel extends TableModel{
		private Collection remarkCol;
		public InterviewHistoryListModel(){
			setWidth("100%");
			Application app = Application.getInstance();
			
			TableColumn InterviewerName = new TableColumn("username", app.getMessage("recruit.general.label.interviewer"));
			addColumn(InterviewerName);
			
			/*TableColumn position = new TableColumn("positionDesc", app.getMessage("recruit.general.label.position"));
			addColumn(position);
			
			TableColumn country = new TableColumn("countryDesc", app.getMessage("recruit.general.label.country"));
			addColumn(country);
			
			TableColumn departmentDesc = new TableColumn("departmentDesc", app.getMessage("recruit.general.label.department"));
			addColumn(departmentDesc);*/
			
			TableColumn lastUpdatedDate = new TableColumn("lastUpdatedDate", app.getMessage("recruit.general.label.date"));
			addColumn(lastUpdatedDate);
			
			TableColumn stageOfInterview = new TableColumn("interviewStageName", app.getMessage("recruit.general.label.IntervieweeStageOfInterview"));
			addColumn(stageOfInterview);
			
			TableColumn interviewDateTime = new TableColumn("interviewDateTime", app.getMessage("recruit.general.label.interviewDateTimes"));
			TableFormat dateCreatedFormat = new TableDateFormat(app.getProperty("globalDatetimeLong"));
			interviewDateTime.setFormat(dateCreatedFormat);
			addColumn(interviewDateTime);
			
			TableColumn remark = new TableColumn("remark", app.getMessage("recruit.general.label.remark"));
			addColumn(remark);
		}
		
		public Collection getTableRows() {
			//rec_interviewer_remark
			Application app = Application.getInstance();  
			RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			
			remarkCol  = ram.findAllIntervieweeRemark(getSort(), isDesc(), getStart(), getRows(), getApplicantId(), getInterviewDateId());
				
			return remarkCol;
		}
		
		public int getTotalRowCount() {
			int total = remarkCol.size();
			return total;
		}
		
		public String getTableRowKey() {
			return "";
		}
	}	
	
	//getter setter
	public String getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}
	
	public String getInterviewDateId() {
		return interviewDateId;
	}

	public void setInterviewDateId(String interviewDateId) {
		this.interviewDateId = interviewDateId;
	}

}







