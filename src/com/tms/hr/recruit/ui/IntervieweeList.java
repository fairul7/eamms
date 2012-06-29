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

public class IntervieweeList extends Table{
	
	public void init(){
		setWidth("100%");
		setModel(new IntervieweeListModel());
	}
	
	public class IntervieweeListModel extends TableModel{
		private Collection interviewDateCol = new ArrayList();
		//private Map ApplicantStatus = new LinkedHashMap();

		public IntervieweeListModel(){
			
			Application app = Application.getInstance();
			//RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
			TableColumn applicantName = new TableColumn("paramCombine", app.getMessage("recruit.general.label.applicant"));
			applicantName.setFormat(new 
			TableFormat(){
				public String format(Object value) {
					String paramCombine = value.toString();
					String[] keys = paramCombine.split("/");
					
					return "<a href=interviewResult.jsp?applicantId="+keys[0] 
					+"&interviewDateId="+keys[1] + "&totalInterview="+ keys[3] +">" + keys[2] + "</a>";
				}
			});
			
			addColumn(applicantName);
			
			TableColumn vacancyCode = new TableColumn("vacancyCode", app.getMessage("recruit.general.label.vacancyCode"));
			//vacancyCode.setUrlParam("vacancyCode");
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
			
			//filter selectbox for status
			/*ApplicantStatus.put("---", app.getMessage("general.hierachy.selectStatus"));
			ApplicantStatus.put(0, app.getMessage("recruit.general.label.black-listed"));
			ApplicantStatus.put(1, app.getMessage("recruit.general.label.black-listed"));
			TableFilter statusFilter = new TableFilter("statusFilter");
			SelectBox statusSb = new SelectBox("statusSb", ApplicantStatus, null, false, 1);
			statusFilter.setWidget(statusSb);
			addFilter(statusFilter);*/
			
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
	        //addAction(new TableAction("setInterviewer", app.getMessage("recruit.general.label.setInterviewer")));
	        addAction(new TableAction("re-schedule", app.getMessage("recruit.general.label.re-schedule")));
	        //addAction(new TableAction("re-scheduleReject", app.getMessage("recruit.general.label.re-scheduleReject")));
	        addAction(new TableAction("rejectedApplicant", app.getMessage("recruit.general.label.rejectApplicant")));
	        addAction(new TableAction("black-listed", app.getMessage("recruit.general.label.black-listed")));
	        addAction(new TableAction("delete", app.getMessage("recruit.general.label.delete"), app.getMessage("recruit.applicant.alert.delete")));
		}
		
		public Collection getTableRows() {
			/*String strApplicantStatus="";
			List selectedStatus = (List)getFilterValue("statusFilter");
			if(selectedStatus!=null && selectedStatus.size() > 0){
				strApplicantStatus = selectedStatus.get(0).toString().startsWith("---")? null : selectedStatus.get(0).toString();
			}*/

			String applicantMisc = (String)getFilterValue("applicantFilter");
			String startDate = (String) getFilterValue("startDateFilter");
			String endDate = (String) getFilterValue("endDateFilter");
			
			Application app = Application.getInstance();  
			RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			
			//interviewDateCol=ram.findAllInterviewee(getSort(), isDesc(), getStart(), getRows(), strVacancyCode, strApplicantStatus, applicantMisc, startDate, endDate);
			interviewDateCol=ram.findAllInterviewee(getSort(), isDesc(), getStart(), getRows(), applicantMisc, startDate, endDate);
			return interviewDateCol;
		}
		
		public int getTotalRowCount() {
			/*String strApplicantStatus="";
			List selectedStatus = (List)getFilterValue("statusFilter");
			if(selectedStatus!=null && selectedStatus.size() > 0){
				strApplicantStatus = selectedStatus.get(0).toString().startsWith("---")? null : selectedStatus.get(0).toString();
			}*/

			String applicantMisc = (String)getFilterValue("applicantFilter");
			String startDate = (String) getFilterValue("startDateFilter");
			String endDate = (String) getFilterValue("endDateFilter");
			
			Application app = Application.getInstance();  
			RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			
			//return ram.countAllInterviewee(strVacancyCode, strApplicantStatus ,applicantMisc,  startDate, endDate); 
			return ram.countAllInterviewee(applicantMisc,  startDate, endDate); 
		}
		
		//get the vacancy Code ver 2 (using interviewDateId to get vacancyCode)
		public HashMap getInterviewDateDetail(String selectedKey){
			HashMap storeMap=new HashMap();
			for(Iterator ite=interviewDateCol.iterator();ite.hasNext();){
				HashMap map= (HashMap) ite.next();
				if(map.get("interviewDateId").equals(selectedKey)){
					storeMap.put("vacancyCode", map.get("vacancyCode"));
					storeMap.put("applicantId", map.get("applicantId"));
					//return map.get("vacancyCode").toString();
					return storeMap;
				}
			}
			return null;
		}
		
		//get the vacancyCode ver 1(using applicant id to get vacancyCode)
		/*public String getVacancyCodeName(String selectedKey){
			Application app = Application.getInstance();
			RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			
			Collection applicantCol=ram.lookUpApplicantStatus(selectedKey);
			ApplicantObj appObjVacancy = (ApplicantObj)applicantCol.iterator().next();
			
			return appObjVacancy.getVacancyCode();
		}*/
		
		public String[] getApplicantCode(String[] selectedKeys){
			String[] applicantIdCode = new String[selectedKeys.length];
			 for(int i=0; i<selectedKeys.length;i++ ){
				 HashMap getStoredMap=getInterviewDateDetail(selectedKeys[i]);
				 applicantIdCode[i]=getStoredMap.get("applicantId").toString();
			 }
			 return applicantIdCode;
		}
		
		public Forward processAction(Event evt, String action, String[] selectedKeys) {
			ApplicantList appList = new ApplicantList();
			Application app = Application.getInstance();
			ApplicantObj applicantObj = new ApplicantObj();
			VacancyObj vacancyObj = new VacancyObj();
			RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			String vacancyCode="";
			String applicantId="";
			boolean hasEntered=false;
			//auditObj
			VacancyObj auditObj = new VacancyObj();
			String actionTaken="";
			/* if("setInterviewer".equals(action)){
				 if(selectedKeys.length>0){ 
					 boolean hasApplicantStatus=appList.validateTypeApplicantStatus(getApplicantCode(selectedKeys), "Scheduled", true);
					 if(hasApplicantStatus){
						 Collection interviewDateId = new ArrayList();
						 for(int i=0; i<selectedKeys.length;i++ ){
							 interviewDateId.add(selectedKeys[i]);
						 }
						 
						 evt.getRequest().getSession().setAttribute("sessionInterviewDateId", interviewDateId);//session name:
						 
						 return new Forward("setInterviewer");
					 }else{
						 return new Forward("selectScheduled");
					 }
				 }else{
					 return new Forward("selectInterviewee");
				 }
			 }else*/ if("re-schedule".equals(action)){//open public-Off validation
				 Collection interviewDateIdSs = new ArrayList();
				 Collection applicantIdSs = new ArrayList();
					 for(int i=0; i<selectedKeys.length;i++ ){ 
						 HashMap getStoredMap=getInterviewDateDetail(selectedKeys[i]);
						 vacancyCode=getStoredMap.get("vacancyCode").toString();
						 applicantId=getStoredMap.get("applicantId").toString();
						 
						 //adding into session 
						 applicantIdSs.add(applicantId);
						 interviewDateIdSs.add(selectedKeys[i]);
						 
						 /*applicantObj = appList.setApplicantStatus(vacancyCode, applicantId, "Short-Listed");
						 ram.updateApplicantStatus(applicantObj);*/
						 
						 /*vacancyObj = appList.setVacancyTotal(vacancyCode, "totalShortlisted", false);
						 ram.updateVacancyTotal(vacancyObj);*/
						 
						/* vacancyObj = appList.setVacancyTotal(vacancyCode,"totalReScheduled", false);
						 ram.updateVacancyTotal(vacancyObj);*/
						 
						 /*//delete the interview date
						 ram.deleteInterviewDate(selectedKeys[i]);*/
			
						 hasEntered=true;
						 
						 //audit
						 /*actionTaken="Re-schedule the Interview Date and Time";
						 auditObj.setAndInsertAudit(vacancyCode, applicantId, actionTaken);*/
					 }
					 evt.getRequest().getSession().setAttribute("sessionApplicantId", applicantIdSs);
					 evt.getRequest().getSession().setAttribute("sessionInterviewDateId", interviewDateIdSs);
					 
					 if(hasEntered)
						 return new Forward("re-schedule");
			
			 }/*else if("re-scheduleReject".equals(action)){
				 boolean hasApplicantStatus=false;
				 if(selectedKeys.length > 0)
					 hasApplicantStatus=appList.validateTypeApplicantStatus(getApplicantCode(selectedKeys), "Scheduled", true);
				 if(hasApplicantStatus){
					 for(int i=0; i<selectedKeys.length;i++ ){
						 HashMap getStoredMap=getInterviewDateDetail(selectedKeys[i]);
						 //vacancyCode=getVacancyCodeName(selectedKeys[i]);//get vacancyCode
						 vacancyCode=getStoredMap.get("vacancyCode").toString();
						 applicantId=getStoredMap.get("applicantId").toString();
						 
						 //applicantObj = appList.setApplicantStatus(vacancyCode, selectedKeys[i], "Re-Scheduled Rejected");
						 applicantObj = appList.setApplicantStatus(vacancyCode, applicantId, "Re-Scheduled Rejected");
						 ram.updateApplicantStatus(applicantObj);
						 
						 vacancyObj = appList.setVacancyTotal(vacancyCode, "totalReScheduledRejected", false);
						 ram.updateVacancyTotal(vacancyObj);
						 
						 //delete the interview date
						 ram.deleteInterviewDate(selectedKeys[i]);
						 
						 hasEntered=true;
						 
						 //audit
						 actionTaken="Reject re-schedule and set applicant status to Re-Scheduled Rejected";
						 auditObj.setAndInsertAudit(vacancyCode, applicantId, actionTaken);
					 }	
					 if(hasEntered)
						 return new Forward("re-scheduleReject");
				 }else{
					 return new Forward("selectScheduled");
				 }
			 }*/else if("rejectedApplicant".equals(action)){
				
							 for(int i=0; i<selectedKeys.length;i++ ){
								 
								 HashMap getStoredMap=getInterviewDateDetail(selectedKeys[i]);
								 //vacancyCode=getVacancyCodeName(selectedKeys[i]);//get vacancyCode
								 vacancyCode=getStoredMap.get("vacancyCode").toString();
								 applicantId=getStoredMap.get("applicantId").toString();
								 
								 //send mail to interviewer-Only if 'Scheduled & Interviewer(s) added' ***
								 SendMessage sm = new SendMessage();
								 sm.sendBlackListedMailSetting(applicantId, "Rejected Applicant");
								 
								 //applicantObj = appList.setApplicantStatus(vacancyCode, selectedKeys[i], "Black-Listed");
								 applicantObj = appList.setApplicantStatus(vacancyCode, applicantId, "Rejected Applicant");
								 ram.updateApplicantStatus(applicantObj);
								 	 
								 vacancyObj = appList.setVacancyTotal(vacancyCode, "totalReScheduledRejected", false);
								 ram.updateVacancyTotal(vacancyObj);
								 
								 //delete the interview date and also will delete the interviewer remark***BUT only 1 interviewDateId is used-history will kept
								 ram.deleteInterviewDate(selectedKeys[i]);
								 
								 hasEntered=true;
								 //audit 
								 actionTaken="Set applicant Status to Rejected Applicant";
								 auditObj.setAndInsertAudit(vacancyCode, applicantId, actionTaken);
							 }	
							 if(hasEntered)
								 return new Forward("rejectedApplicant");
						
			}else if("black-listed".equals(action)){
				 boolean hasApplicantStatus=false;
				 boolean hasBeenBlackList=false;

					 for(int i=0; i<selectedKeys.length;i++ ){
						 HashMap getStoredMap=getInterviewDateDetail(selectedKeys[i]);
						 //vacancyCode=getVacancyCodeName(selectedKeys[i]);//get vacancyCode
						 vacancyCode=getStoredMap.get("vacancyCode").toString();
						 applicantId=getStoredMap.get("applicantId").toString();
						 
						 //send mail to interviewer-Only if 'Scheduled & Interviewer(s) added' ***
						 SendMessage sm = new SendMessage();
						 sm.sendBlackListedMailSetting(applicantId, "Black-Listed");
						 
						 //applicantObj = appList.setApplicantStatus(vacancyCode, selectedKeys[i], "Black-Listed");
						 applicantObj = appList.setApplicantStatus(vacancyCode, applicantId, "Black-Listed");
						 ram.updateApplicantStatus(applicantObj);
						 
						 vacancyObj = appList.setVacancyTotal(vacancyCode, "totalBlackListed", false);
						 ram.updateVacancyTotal(vacancyObj);
						 
						 //delete the interview date and also will delete the interviewer remark***
						 ram.deleteInterviewDate(selectedKeys[i]);
						 
						 hasEntered=true;
	
						 //audit
						 actionTaken="Set applicant Status to Black-Listed";
						 auditObj.setAndInsertAudit(vacancyCode, applicantId, actionTaken);
					 }
		  
					 if(hasEntered)
						 return new Forward("black-listed");
				
			 }else if("delete".equals(action)){
				 for(int i=0; i<selectedKeys.length;i++ ){
					HashMap getStoredMap=getInterviewDateDetail(selectedKeys[i]);
					//vacancyCode=getVacancyCodeName(selectedKeys[i]);//get vacancyCode 
					vacancyCode=getStoredMap.get("vacancyCode").toString();
					applicantId=getStoredMap.get("applicantId").toString();
					
					//send mail to interviewer-Only if 'Scheduled & Interviewer(s) added' ***
					SendMessage sm = new SendMessage();
					sm.sendBlackListedMailSetting(applicantId, "Deleted");
				
					//audit
					actionTaken="Delete Applicant Detail";
					auditObj.setAndInsertAudit(vacancyCode, applicantId, actionTaken);
					
					//ram.deleteAllApplicantDetail(selectedKeys[i]); //dao sql
					ram.deleteAllApplicantDetail(applicantId); //dao sql
					
					//do not update this-to kept track history
					/*vacancyObj = appList.setVacancyTotal(vacancyCode, "totalApplied", true);
					ram.updateVacancyTotal(vacancyObj);*/
					
					hasEntered=true;
				 }
				 if(hasEntered)
					 return new Forward("deleted");
			 }
			return new Forward("error"); 
		}
		
		public String getTableRowKey() {
			return "interviewDateId";
		}
		
	}
	
}
