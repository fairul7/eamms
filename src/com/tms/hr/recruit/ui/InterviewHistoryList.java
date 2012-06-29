package com.tms.hr.recruit.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import kacang.Application;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.hr.recruit.model.RecruitAppModule;
import com.tms.hr.recruit.model.SendMessage;
import com.tms.hr.recruit.model.VacancyObj;

public class InterviewHistoryList extends Table{
	private String applicantId;
	private String interviewDateId;
	private String totalInterview;
	
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
			
			addAction(new TableAction("add", app.getMessage("recruit.general.label.add")));
			addAction(new TableAction("delete", app.getMessage("recruit.general.label.delete"), app.getMessage("recruit.applicant.alert.delete")));
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
			return "interviewerRemarkId";
		}
		
		
		/* public boolean timeDiff(Date date1, Date date2) {
			   boolean flag=false; 
		       Calendar gc = new GregorianCalendar(2005, 9, 22, date1.getHours(), date1.getMinutes(), date1.getSeconds());//today 
		       Calendar gc2 = new GregorianCalendar(2005, 9, 22, date2.getHours(), date2.getMinutes(), date2.getSeconds());//db
		 
		       long timeln = gc.getTimeInMillis() - gc2.getTimeInMillis();
		   
		       return flag;
		}*/
		
		public Forward processAction(Event evt, String action, String[] selectedKeys) {
			boolean hasEntered=false;
			if("add".equals(action)){
				//Collection interviewDateId = new ArrayList();
				//interviewDateId.add(getInterviewDateId());
				
				//evt.getRequest().getSession().setAttribute("sessionInterviewDateId", interviewDateId);//session name:
    			evt.getRequest().getSession().setAttribute("sessionInterviewDateId", getInterviewDateId());//session name:
				
				return new Forward("addInterviewer");
			}else if("delete".equals(action)){
				
				int currentInterviewStage = Integer.parseInt(getTotalInterview()) + 1; //the real stage of interview
				
				 for(int i=0; i<selectedKeys.length;i++ ){
					Application app = Application.getInstance();
					RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
					
					VacancyObj auditObj = new VacancyObj();
					String actionTaken="";
					 
					//#1 get vacancyCode-using vacancyId
					Collection personalCol = ram.loadApplicantPersonal(getApplicantId());
					HashMap personalMap = (HashMap)personalCol.iterator().next();
					String vacancyCode = personalMap.get("vacancyCode").toString();
					String applicantName = personalMap.get("name").toString();
					String positionApplied = personalMap.get("positionDesc").toString();
					
					//#2 send mail to inform to interviewer's for the latest interview stage name
					DateFormat dmyDateFmt = new SimpleDateFormat(app.getProperty("globalDatetimeLong")); 
					HashMap interviewerIdMap = (HashMap)ram.lookUpInterviewerRemarkId(selectedKeys[i]).iterator().next();
					
					int dbInterviewStageStatus=Integer.parseInt(interviewerIdMap.get("interviewStageStatus").toString());
					
					//only send the latest interview stage - 1st Validation
					if(currentInterviewStage == dbInterviewStageStatus){
						Date getTodayDate = new Date();
						Date dbInterviewDateTime = (Date) interviewerIdMap.get("interviewDateTime");
						
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						
						//Only send message if the interview Date Time with the interviewer '>' current Date TIME 2nd Validation date
						if(sdf.format(dbInterviewDateTime).equals(sdf.format(getTodayDate)) ||  dbInterviewDateTime.after(getTodayDate)){
							List ToList= new ArrayList();
							ToList.add(interviewerIdMap.get("interviewerId"));
							//
							String subjectTitle = "You are no longer interviewer to interviewee,"+ applicantName; 
							String title = "You are no longer interviewer to interviewee,"+ applicantName + " for the following Interview Date & Time"; 
							
							StringBuffer sbTitle = new StringBuffer();
							sbTitle.append("<table cellspacing=0 cellpadding=5 width=\"100%\" border=0>\n");
							sbTitle.append("  <tr valign=center> \n");
							sbTitle.append("    <td bgcolor=\"#FFFFFF\" colspan=\"4\"><b><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\" color=\"#000000\">" + title + "</font></b></td>\n");
							sbTitle.append("  </tr>\n");
					        
							sbTitle.append("  <tr> \n");
							sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">Interview Date & Times</font></td>\n");
							sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">Interviewee(s)</font></td>\n");
							sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">Vacancy Code</font></td>\n");
							sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">Position</font></td>\n");
							sbTitle.append("  </tr>\n");
							
							
							sbTitle.append("  <tr> \n");
							sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">" + dmyDateFmt.format(interviewerIdMap.get("interviewDateTime")) + "</font></td>\n");
							sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">" + applicantName + "</font></td>\n");
							sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">" + vacancyCode + "</font></td>\n");
							sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">" + positionApplied + "</font></td>\n");
							sbTitle.append("  </tr>\n");
							
							sbTitle.append("</table>\n");
							
							SendMessage sm = new SendMessage();
							sm.sendMessage(ToList, subjectTitle, sbTitle.toString(), "");
							
							//#3 audit trail 
							String interviewerName = ram.lookUpInterviewerIdToName(selectedKeys[i]);
							actionTaken="Send eMail to ex-interviewer(s) and delete,"+ interviewerName;
							auditObj.setAndInsertAudit(vacancyCode, getApplicantId(), actionTaken);
						}
					}
					
					//#4 delete the rec_interviewer_remark 
					ram.deleteInterviewRemark(selectedKeys[i]); 
					 	
					hasEntered=true;
				 }
				 
				 if(hasEntered)
					 return new Forward("deleted");
			 }
			return new Forward("error"); 
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

	public String getTotalInterview() {
		return totalInterview;
	}

	public void setTotalInterview(String totalInterview) {
		this.totalInterview = totalInterview;
	}


	
}







