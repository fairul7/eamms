package com.tms.hr.recruit.ui;

import java.util.Collection;

import com.tms.hr.recruit.model.RecruitAppModule;

import kacang.Application;
import kacang.stdui.DatePopupField;
import kacang.stdui.Label;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;

public class JobOfferList extends Table{
	private boolean hasRecruitHod;
	
	public void init(){
		setWidth("100%");
		initTableList();
	}
	
	public void initTableList(){
		setShowPageSize(false);
		setModel(new JobOfferListModel());
	}
	
	public class JobOfferListModel extends TableModel{
		public JobOfferListModel(){
			Application app = Application.getInstance();
			
			TableColumn applicantName = new TableColumn("name", app.getMessage("recruit.general.label.applicant"));
			applicantName.setUrlParam("applicantId");
			addColumn(applicantName);
			
			TableColumn vacancyCode = new TableColumn("vacancyCode", app.getMessage("recruit.general.label.vacancyCode"));
			//vacancyCode.setUrlParam("vacancyCode");
			addColumn(vacancyCode);
			
			TableColumn dateOffered = new TableColumn("jobOfferDate", app.getMessage("recruit.general.label.dateOffered"));
			addColumn(dateOffered);
			
			TableColumn position = new TableColumn("positionDesc", app.getMessage("recruit.general.label.position"));
			addColumn(position);
			
			TableColumn country = new TableColumn("countryDesc", app.getMessage("recruit.general.label.country"));
			addColumn(country);
			
			TableColumn departmentDesc = new TableColumn("departmentDesc", app.getMessage("recruit.general.label.department"));
			addColumn(departmentDesc);
			
			TableColumn offerLetterSent = new TableColumn("jobOfferLetterSentStatus", app.getMessage("recruit.general.label.OfferLetterSent"));
			addColumn(offerLetterSent);
			
			TableColumn status = new TableColumn("applicantStatus", app.getMessage("recruit.general.label.status"));
			addColumn(status);
			
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
		
		public String statusType(){
			String statusType="";
			if(isHasRecruitHod())
				statusType="'Offered', 'Job Accepted', 'Job Rejected'";
			else
				statusType="'Offered'";
			
			return statusType;
		}
		
		public Collection getTableRows() {
			String applicantMisc = (String)getFilterValue("titleFilter");
			String startDate = (String) getFilterValue("startDateFilter");
			String endDate = (String) getFilterValue("endDateFilter");
			
			Application app = Application.getInstance();  
			RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			
			String statusType=statusType();
			
			return ram.findAllJobOfferedInterviewee(getSort(), isDesc(), getStart(), getRows(), applicantMisc, startDate, endDate, statusType); //look for rec_applicant_status
			
		}
		
		public int getTotalRowCount() {
			String applicantMisc = (String)getFilterValue("titleFilter");
			String startDate = (String) getFilterValue("startDateFilter");
			String endDate = (String) getFilterValue("endDateFilter");
			
			Application app = Application.getInstance();  
			RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			
			String statusType=statusType();
			
			return ram.countAllJobOfferedInterviewee(applicantMisc,  startDate, endDate, statusType); 
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
			return "applicantId";
		}
	}

	public boolean isHasRecruitHod() {
		return hasRecruitHod;
	}

	public void setHasRecruitHod(boolean hasRecruitHod) {
		this.hasRecruitHod = hasRecruitHod;
	}
	
	//getter setter

}
