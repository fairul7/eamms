package com.tms.hr.recruit.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.DatePopupField;
import kacang.stdui.Label;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.hr.recruit.model.ApplicantObj;
import com.tms.hr.recruit.model.InterviewObj;
import com.tms.hr.recruit.model.RecruitAppModule;
import com.tms.hr.recruit.model.RecruitModule;
import com.tms.hr.recruit.model.SendMessage;
import com.tms.hr.recruit.model.VacancyObj;

public class ApplicantList extends Table{
	//private VacancyView vacancyView;
	private VacancyDetail vacancyDetail;
	private String vacancyCode;
	private String sbCode;
	
	public void onRequest(Event evt) {
		init();
		vacancyDetail = (VacancyDetail) Application.getInstance().getWidget(evt.getRequest(), "vacancy.Detail");
		//vacancyView = (VacancyView) Application.getInstance().getWidget(evt.getRequest(), "vacancy.View");
		setVacancyCode(vacancyDetail.getVacancyCode());
	}
	
	public void init(){
		setWidth("100%");
		setModel(new ApplicantListModel());
	}
	
	public class ApplicantListModel extends TableModel{
		private Map ApplicantStatus = new LinkedHashMap();
		private SelectBox statusSb;
		
		public ApplicantListModel(){
			Application app = Application.getInstance();
			//TableColumn applicantName = new TableColumn("name", app.getMessage("recruit.general.label.applicant"));
			TableColumn applicantName = new TableColumn("paramCombine", app.getMessage("recruit.general.label.applicant"));
			applicantName.setFormat(new 
					TableFormat(){
						public String format(Object value) {
							String paramCombine = value.toString();
							String[] keys = paramCombine.split("/");
												
return "<a href='#' onClick=\"NewWindow('jobApplicationFormViewPopUp.jsp?applicantId="+keys[0]+"','applicantName','500','670','yes'); return false \">"+keys[1]+"</a>"	;					
							//return "<a href=interviewResult.jsp?applicantId="+keys[0] +">" + keys[1] + "</a>";
						}
					});
			
			//applicantName.setUrlParam("applicantId");
			
			addColumn(applicantName);
			
			TableColumn highestEduLevel = new TableColumn("highEduLevelDesc", app.getMessage("recruit.general.label.highestEduLevel"));
			highestEduLevel.setSortable(false);
			addColumn(highestEduLevel);
			
			TableColumn courseTitle = new TableColumn("courseTitle", app.getMessage("recruit.general.label.courseTitle"));
			courseTitle.setSortable(false);
			addColumn(courseTitle);
			
			TableColumn grade = new TableColumn("grade", app.getMessage("recruit.general.label.grade"));
			grade.setSortable(false);
			addColumn(grade);
			
			TableColumn workingExp = new TableColumn("yearOfWorkingExp", app.getMessage("recruit.general.label.workingExp"));
			addColumn(workingExp);
			
			TableColumn dateApplied = new TableColumn("dateApplied", app.getMessage("recruit.general.label.dateApplied"));
			addColumn(dateApplied);
			
			TableColumn status = new TableColumn("applicantStatus", app.getMessage("recruit.general.label.status"));
			addColumn(status);
			
			// filter textbox 
			addFilter(new TableFilter("applicantFilter"));
			
			//filter selectbox	
			ApplicantStatus.put("---", app.getMessage("general.hierachy.selectStatus"));
			ApplicantStatus.put("0", app.getMessage("recruit.general.label.new"));
			ApplicantStatus.put("1", app.getMessage("recruit.general.label.kiv"));
			ApplicantStatus.put("2", app.getMessage("recruit.general.label.short-listed"));
			ApplicantStatus.put("3", app.getMessage("recruit.general.label.shortlistedAndEmailSend"));
			ApplicantStatus.put("4", app.getMessage("recruit.general.label.scheduled"));
			//ApplicantStatus.put(4, app.getMessage("recruit.general.label.scheduledAndInterviewerAdded"));
			//ApplicantStatus.put(5, app.getMessage("recruit.general.label.rescheduledAndRejected"));
			ApplicantStatus.put("5", app.getMessage("recruit.general.label.offered"));
			ApplicantStatus.put("6", app.getMessage("recruit.general.label.interviewUnsuccessful"));
			ApplicantStatus.put("7", app.getMessage("recruit.general.label.anotherInterview"));
			ApplicantStatus.put("8", app.getMessage("recruit.general.label.anotherInterviewAndEmailSend"));
			ApplicantStatus.put("9", app.getMessage("recruit.general.label.jobAccepted"));
			ApplicantStatus.put("10", app.getMessage("recruit.general.label.jobRejected"));
			ApplicantStatus.put("11", app.getMessage("recruit.general.label.rejectedApplicant"));
			ApplicantStatus.put("12", app.getMessage("recruit.general.label.black-listed"));
			
			/*SelectBox statusSb = new SelectBox("statusSb", ApplicantStatus, null, false, 1);
			statusSb.setSelectedOption("0");*/
			
			statusSb = new SelectBox("statusSb");
			statusSb.setOptionMap(ApplicantStatus);
			if(getSbCode()!=null && !getSbCode().equals("")){
 				statusSb.setSelectedOption(getSbCode());
			}else
				statusSb.setSelectedOption("0");
			
			TableFilter statusFilter = new TableFilter("statusFilter");
			statusFilter.setWidget(statusSb);
			addFilter(statusFilter);
			
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
			addAction(new TableAction("kiv", app.getMessage("recruit.general.label.kiv")));
		    addAction(new TableAction("short-listed", app.getMessage("recruit.general.label.short-listed")));
	        addAction(new TableAction("setupNewInterview", app.getMessage("recruit.general.label.setupNewInterview")));
	        addAction(new TableAction("scheduled", app.getMessage("recruit.general.label.markAsSchedule")));
	        //addAction(new TableAction("rejectedApplicant", app.getMessage("recruit.general.label.rejectApplicant")));
	        addAction(new TableAction("black-listed", app.getMessage("recruit.general.label.black-listed")));
	        addAction(new TableAction("delete", app.getMessage("recruit.general.label.delete"), app.getMessage("recruit.applicant.alert.delete")));
		}
		
		public Collection getTableRows() {
			boolean flag=true;
			String vacancyCode=getVacancyCode();
			String strApplicantStatus="";
			String applicantMisc = (String)getFilterValue("applicantFilter");
			String startDate = (String) getFilterValue("startDateFilter");
			String endDate = (String) getFilterValue("endDateFilter");
			
			List selectedStatus = (List)getFilterValue("statusFilter");
			
			if(getSbCode()!=null && !getSbCode().equals("")){
				strApplicantStatus= getSbCode();
				flag=false;
			}
			
			if(flag){
				if(selectedStatus!=null && selectedStatus.size() > 0){
					strApplicantStatus = selectedStatus.get(0).toString().startsWith("---")? null : selectedStatus.get(0).toString();
				}
			}
			setSbCode(null);
			Application app = Application.getInstance();  
			RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			
			return ram.findAllApplicantSpecial(vacancyCode, getSort(), isDesc(), getStart(), getRows(), applicantMisc,  strApplicantStatus, startDate, endDate);
		}
		
		public int getTotalRowCount() {
			boolean flag=true;
			String vacancyCode=getVacancyCode();
			String strApplicantStatus="";
			String applicantMisc = (String)getFilterValue("applicantFilter");
			String startDate = (String) getFilterValue("startDateFilter");
			String endDate = (String) getFilterValue("endDateFilter");
			
			List selectedStatus = (List)getFilterValue("statusFilter");
			
			if(getSbCode()!=null && !getSbCode().equals("")){
				strApplicantStatus= getSbCode();
				flag=false;
			}
			
			if(flag){
				if(selectedStatus!=null && selectedStatus.size() > 0){
					strApplicantStatus = selectedStatus.get(0).toString().startsWith("---")? null : selectedStatus.get(0).toString();	
				}
			}
			setSbCode(null);
			Application app = Application.getInstance();  
			RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			return ram.countAllApplicantSpecial(vacancyCode, applicantMisc, strApplicantStatus, startDate, endDate); 
		}
		
		public Forward processAction(Event evt, String action, String[] selectedKeys) {
			 Application app = Application.getInstance();
			 ApplicantObj applicantObj = new ApplicantObj();
			 VacancyObj vacancyObj = new VacancyObj();
			 RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			 boolean hasEntered=false;
			 //auditObj
			 VacancyObj auditObj = new VacancyObj();
			 String actionTaken="";
			 if("kiv".equals(action)){
				 boolean hasApplicantStatus=false;
				 if(selectedKeys.length > 0)
					 hasApplicantStatus=validateTypeApplicantStatus(selectedKeys, "New", true);
				 if(hasApplicantStatus){
					 for(int i=0; i<selectedKeys.length;i++ ){	
							 applicantObj = setApplicantStatus(getVacancyCode(), selectedKeys[i], "KIV");
							 ram.updateApplicantStatus(applicantObj);
							  
							 //audit
							 actionTaken="set applicant status to KIV";
							 auditObj.setAndInsertAudit(getVacancyCode(), selectedKeys[i], actionTaken);
							 
							 hasEntered=true;
					 }
					 if(hasEntered)
						 return new Forward("kiv");
				 }else{
					 return new Forward("notNewStatusKIV");	
				 }
			 }	 
			 else if("setupNewInterview".equals(action)){
				 //Collection userList = ram.loadShortlisted(ids, "Short-Listed,Another Interview");
				 boolean hasApplicantStatus=false;
				 boolean hasApplicantStatusAdditional=false;
				 if(selectedKeys.length > 0){
					 hasApplicantStatus=validateTypeApplicantStatus(selectedKeys, "Short-Listed", true);
				 	 hasApplicantStatusAdditional=validateTypeApplicantStatus(selectedKeys, "Another Interview", true);
				 }
				 if(hasApplicantStatus || hasApplicantStatusAdditional){
					 Collection applicantIdForDateCol= new ArrayList();
					 for(int i=0; i<selectedKeys.length;i++ ){
						 applicantIdForDateCol.add(selectedKeys[i]);
					 }
					 evt.getRequest().getSession().setAttribute("sessionApplicantIdForDate", applicantIdForDateCol);
					 
					 //remove the interview Date & Time session first
					 evt.getRequest().getSession().removeAttribute("sessionInterviewObjCol");
					 
					 return new Forward("setupNewInterview");
				 }else{
					 return new Forward("selectShortlistedORAnotherInterview");
				 }
			 }else if("short-listed".equals(action)){
				 //boolean flagExist=validateHasInterviewDate(selectedKeys);
				 boolean hasApplicantStatus=false;
				 boolean hasKivStatus=false;
				 if(selectedKeys.length > 0){
					 hasApplicantStatus=validateTypeApplicantStatus(selectedKeys, "New", true);
				 	hasKivStatus=validateTypeApplicantStatus(selectedKeys, "KIV", true);
				 }	
				 if(hasApplicantStatus || hasKivStatus){
					 for(int i=0; i<selectedKeys.length;i++ ){	
							 applicantObj = setApplicantStatus(getVacancyCode(), selectedKeys[i], "Short-Listed");
							 ram.updateApplicantStatus(applicantObj);
							  
							 vacancyObj = setVacancyTotal(getVacancyCode(), "totalShortlisted", false);
							 ram.updateVacancyTotal(vacancyObj);
							 hasEntered=true;
							 
							 //audit
							 actionTaken="set applicant status to Short-Listed";
							 auditObj.setAndInsertAudit(getVacancyCode(), selectedKeys[i], actionTaken);
					 }
					 if(hasEntered)
						 return new Forward("short-listed");
				 }else{
					 return new Forward("NotNewStatus");	
				 }
			 }else if("scheduled".equals(action)){
				 boolean hasApplicantStatus=false;
				 boolean hasApplicantStatusAdditional=false;
				 boolean hasMailHasSend=false;
				 if(selectedKeys.length > 0){
					 hasApplicantStatus=validateTypeApplicantStatus(selectedKeys, "Short-Listed & Email Sent", false);
				 	 hasApplicantStatusAdditional=validateTypeApplicantStatus(selectedKeys, "Another Interview & Email Sent", false);
				 }
				
				 if(hasApplicantStatus || hasApplicantStatusAdditional){
						 for(int i=0; i<selectedKeys.length;i++ ){
							 applicantObj = setApplicantStatus(getVacancyCode(), selectedKeys[i], "Scheduled");
							 ram.updateApplicantStatus(applicantObj);
							 	 
							 vacancyObj = setVacancyTotal(getVacancyCode(),"totalScheduled", false);
							 ram.updateVacancyTotal(vacancyObj);
							 hasEntered=true;
							 
							 //part
							 actionTaken="set applicant status to Scheduled";
							 auditObj.setAndInsertAudit(getVacancyCode(), selectedKeys[i], actionTaken);
						 }	
					 
						 //find interview stage status = 0, set the interview stage status from interview total
						 Collection getInfoCol=ram.findInterviewDate(selectedKeys);
						 
						 String[] interviewDateId = new String[getInfoCol.size()];
						 String[] interviewStageStatus = new String[getInfoCol.size()];
						 int i=0;
						 for(Iterator ite=getInfoCol.iterator(); ite.hasNext();){
							HashMap map = (HashMap) ite.next();
							if(map.get("interviewStageStatus").equals("0")){
								interviewDateId[i]=map.get("interviewDateId").toString();
							}
							int total = Integer.parseInt(map.get("totalInterview").toString());
							total+=1;
							
							interviewStageStatus[i] =String.valueOf(total);
							i++;
						 }	 
						 InterviewObj interviewObj = new InterviewObj();
						 interviewObj.setInterviewDateId(interviewDateId);
						 
						 //ori
						 /*interviewObj.setApplicantId(selectedKeys);
						 interviewObj.setInterviewStageStatus("1");
						 ram.updateInterviewDate(interviewObj); //update the interview stage name */
						 
						 ram.updateInterviewDate(interviewObj, interviewStageStatus); //update the interview stage name
				     
				     //send mail to admin department //setting the mail configuration
					 DateFormat dmyDateFmt = new SimpleDateFormat(app.getProperty("globalDatetimeLong"));
					 SendMessage sm = new SendMessage();
					 SecurityService service = (SecurityService)Application.getInstance().getService(SecurityService.class);
				     try{
					        Collection recruitAdminCol = service.getUsersByPermission("recruit.permission.recruitAdmin",Boolean.TRUE, null, false, 0, -1);
					        List ToList = new ArrayList(recruitAdminCol.size());
					        StringBuffer sb = new StringBuffer();
					        int j = 0;
					        for(Iterator ite=recruitAdminCol.iterator(); ite.hasNext();){
				        		 User user = (User) ite.next();
				        		 ToList.add(user.getId());
				        		 if(j>0 && j<recruitAdminCol.size())
				        			 sb.append(", ");
				        		 
				        		 sb.append(user.getName());
				        	j++;	 
				        	}
				    
					 String subjectTitle=app.getMessage("recruit.general.label.scheduledApplicant", "Scheduled Applicant(s)") + " for Interview Session";
					 String BodyTitle=
						 "<table>" +
						 "	<tr>" +
						 "		<td><b><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\" color=\"#000000\">Interview Date & Time</font></b></td>" +
						 "		<td><b><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\" color=\"#000000\">Applicant Name</font><b></td>" +
						 "	</tr>" ;
					 
					 Collection getInterviewDate=ram.findInterviewDateWithStatus(selectedKeys);
					 for(Iterator ite=getInterviewDate.iterator(); ite.hasNext();){
						HashMap map = (HashMap) ite.next();		
						String name = map.get("name").toString();
						//String interviewDateTime=map.get("interviewDateTime").toString();
						BodyTitle+="<tr>" +
								"<td><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\" color=\"#000000\">" + 
											dmyDateFmt.format(map.get("interviewDateTime"))  + "</font></td>" +
								"<td><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\" color=\"#000000\"><i>"+ 
											name +"</i></font></td>" +
											"</tr>";
					 }
					 
					 BodyTitle +="</table>";
					 sm.sendMessage(ToList, subjectTitle, BodyTitle, "");
					 hasMailHasSend=true;
					
					 //audit
					 actionTaken="Send eMail to Recruit Admin(s), " + sb.toString() ;
					 for(int k=0; k<selectedKeys.length;k++ ){
						 auditObj.setAndInsertAudit(getVacancyCode(), selectedKeys[k], actionTaken);
					 }
				     }catch (Exception e){
				        	Log.getLog(getClass()).error(e);
				     }
				     
				     //setting to redirect to setup interviewer***
				     Collection InterviewDateIdCol = ram.findInterviewDateWithStatus(selectedKeys);
				     Collection interviewDateIdSs = new ArrayList();
				     for(Iterator ite=InterviewDateIdCol.iterator();ite.hasNext();){
				    	 HashMap interviewIdMap = (HashMap)ite.next();
				    	 interviewDateIdSs.add(interviewIdMap.get("interviewDateId"));
				     }
				     
				     evt.getRequest().getSession().setAttribute("sessionInterviewDateId", interviewDateIdSs);//session name:
				     //ending***
				     
					 if(hasEntered)
						 return new Forward("scheduled");
				 }else{
					 return new Forward("MailNotSend");	
				 }
			 }/*else if("rejectedApplicant".equals(action)){
				 boolean hasApplicantStatus=false;
				 boolean hasBeenRejected=false;
				 boolean hasBeenBlackListed=false;
				 if(selectedKeys.length > 0){
					 hasApplicantStatus=validateTypeApplicantStatus(selectedKeys, "Scheduled", false);
					 hasBeenRejected=validateTypeApplicantStatus(selectedKeys, "Rejected Applicant", false);
					 hasBeenBlackListed=validateTypeApplicantStatus(selectedKeys, "Black-Listed", false);
				 }
				 if(!hasBeenRejected){
					 if(!hasBeenBlackListed){
						 if(!hasApplicantStatus){
							 for(int i=0; i<selectedKeys.length;i++ ){
								 
								 //send mail to interviewer-Only if 'Scheduled & Interviewer(s) added' ***
								 SendMessage sm = new SendMessage();
								 sm.sendBlackListedMailSetting(selectedKeys[i], "Rejected Applicant");
		
								 //update the vacancy position offered total if applicant status is Job Accepted
								 minusVacancyPositionOffered(selectedKeys[i]);
								 
								 applicantObj = setApplicantStatus(getVacancyCode(), selectedKeys[i], "Rejected Applicant");
								 ram.updateApplicantStatus(applicantObj);
								 
								 //vacancyObj = setVacancyTotal(getVacancyCode(), "totalBlackListed", false);
								 //ram.updateVacancyTotal(vacancyObj);
								 
								 hasEntered=true;
								 //audit 
								 actionTaken="Set applicant Status to Rejected Applicant";
								 auditObj.setAndInsertAudit(getVacancyCode(), selectedKeys[i], actionTaken);
							 }	
							 if(hasEntered)
								 return new Forward("rejectedApplicant");
						 }else{
							 return new Forward("cannotRejectedApplicant");
						 }
					 }else{
					 return new Forward("cannotRejectedApplicantB");
				 }
			}else{
				 return new Forward("cannotRejectedApplicantAlready");
			}
			 }*/
			 else if("black-listed".equals(action)){
				 boolean hasApplicantStatus=false;
				 boolean hasBeenBlackList=false;
				 if(selectedKeys.length > 0){
					 hasApplicantStatus=validateTypeApplicantStatus(selectedKeys, "Scheduled", false);
					 hasBeenBlackList=validateTypeApplicantStatus(selectedKeys, "Black-Listed", false);
				 }
				 if(!hasBeenBlackList){
						 if(!hasApplicantStatus){
							 for(int i=0; i<selectedKeys.length;i++ ){
								 
								 //send mail to interviewer-Only if 'Scheduled & Interviewer(s) added' ***
								 SendMessage sm = new SendMessage();
								 sm.sendBlackListedMailSetting(selectedKeys[i], "Black-Listed");
		
								 //update the vacancy position offered total if applicant status is Job Accepted
								 minusVacancyPositionOffered(selectedKeys[i]);
								 
								 applicantObj = setApplicantStatus(getVacancyCode(), selectedKeys[i], "Black-Listed");
								 ram.updateApplicantStatus(applicantObj);
								 
								 vacancyObj = setVacancyTotal(getVacancyCode(), "totalBlackListed", false);
								 ram.updateVacancyTotal(vacancyObj);
								 
								 hasEntered=true;
								 //audit 
								 actionTaken="Set applicant Status to Black-Listed";
								 auditObj.setAndInsertAudit(getVacancyCode(), selectedKeys[i], actionTaken);
							 }	
							 if(hasEntered)
								 return new Forward("black-listed");
						 }else{
							 return new Forward("cannotBlack-listed");
						 }
				 }else{
					 return new Forward("cannotBlack-listedB");
				 }
			 }else if("delete".equals(action)){
				 for(int i=0; i<selectedKeys.length;i++ ){
					 
					//send mail to interviewer-Only if 'Scheduled & Interviewer(s) added' ***
					SendMessage sm = new SendMessage();
					sm.sendBlackListedMailSetting(selectedKeys[i], "Deleted");
					 
					//update the vacancy position offered total if applicant status is Job Accepted
					minusVacancyPositionOffered(selectedKeys[i]);
					
					//audit 
					actionTaken="Delete Applicant Detail";
					auditObj.setAndInsertAudit(getVacancyCode(), selectedKeys[i], actionTaken);
					ram.deleteAllApplicantDetail(selectedKeys[i]); //dao sql
					
					//do not update this-to kept track history
					/*vacancyObj = setVacancyTotal(getVacancyCode(), "totalApplied", true);
					ram.updateVacancyTotal(vacancyObj);*/
					hasEntered=true;
				 }
				 if(hasEntered)
					 return new Forward("deleted");
			 }
			 return new Forward("error");
		}
		
		public String getTableRowKey() {
			return "applicantId";
		}
		
	}

	public String getVacancyCode() {
		return vacancyCode;
	}

	public void setVacancyCode(String vacancyCode) {
		this.vacancyCode = vacancyCode;
	}

	public VacancyDetail getVacancyDetail() {
		return vacancyDetail;
	}

	public void setVacancyDetail(VacancyDetail vacancyDetail) {
		this.vacancyDetail = vacancyDetail;
	}

	//set applicant status to Short-listed, Scheduled
	public ApplicantObj setApplicantStatus(String vacancyCode, String selectedKey, String title){
		 Application app = Application.getInstance();
		 RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
		 Collection applicantStatusCol = ram.lookUpApplicantStatus(selectedKey);
		 ApplicantObj aObj= (ApplicantObj)applicantStatusCol.iterator().next();
		 ApplicantObj applicantObj = new ApplicantObj();
		 applicantObj.setVacancyCode(vacancyCode);
		 applicantObj.setApplicantId(selectedKey);
		 applicantObj.setDateApplied(aObj.getDateApplied());
		 applicantObj.setApplicantStatus(title);
		 applicantObj.setJobOfferDate(aObj.getJobOfferDate());
		 applicantObj.setJobOfferLetterSent(aObj.isJobOfferLetterSent());
		 applicantObj.setJobOfferRemark(aObj.getJobOfferRemark());
		 applicantObj.setTotalInterview(aObj.getTotalInterview());
		 return applicantObj;
	}
	
	//update the vacancy total status
	public VacancyObj setVacancyTotal(String vacancyCode, String updateType, boolean toMinus){
		 VacancyObj vacancyObj = new VacancyObj();
		 vacancyObj.setVacancyCode(vacancyCode);
		 vacancyObj.setTotalApplied(vacancyCode, updateType, toMinus);
		 vacancyObj.setTotalShortlisted(vacancyCode, updateType, toMinus);//update the total shortlisted +1
		 vacancyObj.setTotalScheduled(vacancyCode, updateType, toMinus);//update the total scheduled +1 
		 //vacancyObj.setTotalScheduleRejected(getVacancyCode(), updateType, toMinus);
		 vacancyObj.setTotalReScheduled(vacancyCode, updateType, toMinus); //new
		 vacancyObj.setTotalReScheduledRejected(vacancyCode, updateType, toMinus); //new
		 vacancyObj.setTotalAnotherInterview(vacancyCode, updateType, toMinus);//new
		 vacancyObj.setTotalJobOffered(vacancyCode, updateType, toMinus);
		 vacancyObj.setTotalInterviewUnsuccessful(vacancyCode, updateType, toMinus);
		 vacancyObj.setTotalJobAccepted(vacancyCode, updateType, toMinus);
		 vacancyObj.setTotalJobRejected(vacancyCode, updateType, toMinus);
		 vacancyObj.setTotalBlackListed(vacancyCode, updateType, toMinus);//update the total blacklisted +1 
		 vacancyObj.setTotalViewed(vacancyCode, updateType, toMinus);
		 return vacancyObj;
	}
	
	public boolean validateTypeApplicantStatus(String[] selectedkey, String statusName, boolean checkForMultiple){
		boolean flag=false;
		int noError=0;
		Application app = Application.getInstance();
		RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
		Collection lookupCol=ram.lookUpApplicantStatus(selectedkey);
		
		
		for(Iterator ite=lookupCol.iterator(); ite.hasNext();){
			ApplicantObj applicantObj = (ApplicantObj) ite.next();
			if(applicantObj.getApplicantStatus().equals(statusName))
				noError++;
		}
			
		if(checkForMultiple){
			if(lookupCol.size() == noError)
				flag=true;
		}else{
			if(noError > 0)
				flag=true;
		}
			
		return flag;
	}
	
	//looking for applicant status 'Job Accepted' for deletion part. minus 1 is found
	public void minusVacancyPositionOffered(String applicantId){
		Application app = Application.getInstance();
		RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
		RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
		Collection lookUpStatusCol = ram.loadApplicantPersonal(applicantId);
		HashMap statusMap = (HashMap)lookUpStatusCol.iterator().next();
		if(statusMap.get("applicantStatus").equals("Job Accepted")){
			VacancyObj vacancyObj = new VacancyObj();
			if(statusMap.get("noOfPositionOffered").toString()!=null){
				int totalPositionOffered = Integer.parseInt(statusMap.get("noOfPositionOffered").toString());
				totalPositionOffered-=1;
				vacancyObj.setNoOfPositionOffered(totalPositionOffered);
				vacancyObj.setVacancyCode(statusMap.get("vacancyCode").toString());
				rm.updateVacancyPositionOffered(vacancyObj);//update the vacancy position offered
			}
		}
	}

	public String getSbCode() {
		return sbCode;
	}

	public void setSbCode(String sbCode) {
		this.sbCode = sbCode;
	}
	
	/*public Map getApplicantStatusMap(){
		Map ApplicantStatus = new LinkedHashMap();
		Application app = Application.getInstance();
		
		ApplicantStatus.put("---", app.getMessage("general.hierachy.selectStatus"));
		ApplicantStatus.put(0, app.getMessage("recruit.general.label.new"));
		ApplicantStatus.put(1, app.getMessage("recruit.general.label.kiv"));
		ApplicantStatus.put(2, app.getMessage("recruit.general.label.short-listed"));
		ApplicantStatus.put(3, app.getMessage("recruit.general.label.shortlistedAndEmailSend"));
		ApplicantStatus.put(4, app.getMessage("recruit.general.label.scheduled"));
		//ApplicantStatus.put(4, app.getMessage("recruit.general.label.scheduledAndInterviewerAdded"));
		//ApplicantStatus.put(5, app.getMessage("recruit.general.label.rescheduledAndRejected"));
		ApplicantStatus.put(5, app.getMessage("recruit.general.label.offered"));
		ApplicantStatus.put(6, app.getMessage("recruit.general.label.interviewUnsuccessful"));
		ApplicantStatus.put(7, app.getMessage("recruit.general.label.anotherInterview"));
		ApplicantStatus.put(8, app.getMessage("recruit.general.label.anotherInterviewAndEmailSend"));
		ApplicantStatus.put(9, app.getMessage("recruit.general.label.jobAccepted"));
		ApplicantStatus.put(10, app.getMessage("recruit.general.label.jobRejected"));
		ApplicantStatus.put(11, app.getMessage("recruit.general.label.rejectedApplicant"));
		ApplicantStatus.put(12, app.getMessage("recruit.general.label.black-listed"));
		
		return ApplicantStatus;
	}*/
	
	
}










