package com.tms.hr.recruit.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
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

import com.tms.hr.recruit.model.InterviewObj;
import com.tms.hr.recruit.model.RecruitAppModule;

public class SetupInterviewDateList extends Table{
	//session sessionApplicantIdForDate-Collection of shortlisted or Another interview (applicantId)
	//session sessionInterviewObjColData- Collection of InterviewObj
	
	private Object sessionApplicantIdForDate;
	private Object sessionInterviewObjColData;
	
	private int totalSelected;
	
	public void onRequest(Event evt) {
		init();
		sessionApplicantIdForDate=evt.getRequest().getSession().getAttribute("sessionApplicantIdForDate");
		sessionInterviewObjColData=evt.getRequest().getSession().getAttribute("sessionInterviewObjCol");
	}
	
	public void init(){
		setWidth("100%");
		setModel(new SetupInterviewDateListModel());
	}
	
	public class SetupInterviewDateListModel extends TableModel{
		private Collection interviewDateCol = new ArrayList();
	
		public SetupInterviewDateListModel(){
			Application app = Application.getInstance();
			TableColumn applicantName = new TableColumn("paramCombine", app.getMessage("recruit.general.label.applicant"));
			applicantName.setFormat(new 
			TableFormat(){
				public String format(Object value) {
					String paramCombine = value.toString();
					String[] keys = paramCombine.split("/");
					return "<a href='#' onClick=\"NewWindow('popupSetupInterviewDate.jsp?applicantId="+keys[0]+"','applicantName','500','330','yes'); return false \">"+keys[1]+"</a>"	;
					//return "<a href='#' onClick=javascript:window.open('popupSetupInterviewDate.jsp?applicantId="+keys[0]+"','blank','toolbar=no,width=500,height=330,scrollbars=yes'); return false; >"+keys[1]+"</a>";					
				}
			});
			
			addColumn(applicantName);
			
			TableColumn vacancyCode = new TableColumn("vacancyCode", app.getMessage("recruit.general.label.vacancyCode"));
			addColumn(vacancyCode);
			
			TableColumn positionApplied = new TableColumn("positionDesc", app.getMessage("recruit.general.label.position"));
			addColumn(positionApplied);
			
			TableColumn nextInterviewDateTime = new TableColumn("interviewDateTime", app.getMessage("recruit.general.label.interviewDateTimes"));
			TableFormat dateCreatedFormat = new TableDateFormat(app.getProperty("globalDatetimeLong"));
			nextInterviewDateTime.setFormat(dateCreatedFormat);
			nextInterviewDateTime.setSortable(false);
			addColumn(nextInterviewDateTime);
			
			/*TableColumn stageOfInterview = new TableColumn("interviewStageName", app.getMessage("recruit.general.label.stageOfInterview"));
			addColumn(stageOfInterview);*/
			
			TableColumn status = new TableColumn("applicantStatus", app.getMessage("recruit.general.label.status"));
			addColumn(status);
			
			//filter textbox 
			addFilter(new TableFilter("applicantFilter"));
		
			//add br-newLinse
			TableFilter lblbr = new TableFilter("lblbr");
			Label lblSpace = new Label("lblSpace","<br />");
			lblbr.setWidget(lblSpace);
			addFilter(lblbr);
			
			/*//filter start date
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
			addFilter(endDateFilter);*/
			
			addAction(new TableAction("delete", app.getMessage("recruit.general.label.delete"), app.getMessage("recruit.applicant.alert.delete")));
		}
		
		public Collection getTableRows() {
			String applicantStatus = "Short-Listed,Another Interview";
			String applicantMisc = (String)getFilterValue("applicantFilter");
			/*String startDate = (String) getFilterValue("startDateFilter");
			String endDate = (String) getFilterValue("endDateFilter");*/
			String startDate="";
			String endDate ="";
			
			Collection idCol = (Collection) sessionApplicantIdForDate;
			Collection interviewDateColSs = (Collection) sessionInterviewObjColData;
			Application app = Application.getInstance();  
			RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			
			return ram.findShortListedAndAnother(getSort(), isDesc(), getStart(), getRows(), applicantMisc, startDate, endDate, idCol, interviewDateColSs, applicantStatus);
		}
		
		public int getTotalRowCount() {
			String applicantStatus = "Short-Listed,Another Interview";
			String applicantMisc = (String)getFilterValue("applicantFilter");
			/*String startDate = (String) getFilterValue("startDateFilter");
			String endDate = (String) getFilterValue("endDateFilter");*/
			String startDate="";
			String endDate ="";
			
			Application app = Application.getInstance();  
			RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			Collection idCol = (Collection) sessionApplicantIdForDate;
			Collection interviewDateColSs = (Collection) sessionInterviewObjColData;
			
			int totalNo = ram.countAllShortListedAndAnother(applicantMisc,  startDate, endDate,  idCol, interviewDateColSs, applicantStatus);
			setTotalSelected(totalNo);
			return ram.countAllShortListedAndAnother(applicantMisc,  startDate, endDate,  idCol, interviewDateColSs, applicantStatus); 
		}
		
		public Forward processAction(Event evt, String action, String[] selectedKeys) {
			boolean hasEntered=false;
			if("delete".equals(action)){
				Collection idCol = (Collection) sessionApplicantIdForDate;
				Collection interviewDateColSs = (Collection) sessionInterviewObjColData;
				
				int j=0;
				for(int i=0; i<selectedKeys.length;i++ ){ 
					for(Iterator ite=idCol.iterator(); ite.hasNext();){
						if(selectedKeys[i].equals(ite.next())){
							ite.remove();	
						}	
					}
					
					if(interviewDateColSs!=null && interviewDateColSs.size() > 0){
						for(Iterator iter=interviewDateColSs.iterator(); iter.hasNext();){
							InterviewObj iObj= (InterviewObj)iter.next();
							if(iObj.getApplicantId()[0].equals(selectedKeys[i])){
								iter.remove();
							}
						}
					}
					j++;
				 }
				
				//remove session for sessionInterviewObjColData// if all selected
				if(getTotalSelected() == j)
					evt.getRequest().getSession().removeAttribute("sessionInterviewObjColData");
				
				hasEntered=true;
				
				 if(hasEntered)
					return new Forward("deleted");
			 }
			 return new Forward("error");
		}
		
		public String getTableRowKey() {
			return "applicantId";
		}
	}

	public Object getSessionApplicantIdForDate() {
		return sessionApplicantIdForDate;
	}

	public void setSessionApplicantIdForDate(Object sessionApplicantIdForDate) {
		this.sessionApplicantIdForDate = sessionApplicantIdForDate;
	}
	
	public Object getSessionInterviewObjColData() {
		return sessionInterviewObjColData;
	}

	public void setSessionInterviewObjColData(Object sessionInterviewObjColData) {
		this.sessionInterviewObjColData = sessionInterviewObjColData;
	}

	public int getTotalSelected() {
		return totalSelected;
	}

	public void setTotalSelected(int totalSelected) {
		this.totalSelected = totalSelected;
	}
}
