package com.tms.fms.eamms.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Date;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DefaultDataObject;
import kacang.model.DefaultModule;
import kacang.util.Log;
import kacang.util.Mailer;
import kacang.util.UuidGenerator;

import com.tms.fms.util.JobUtil;
import com.tms.util.MailUtil;


public class EammsModule extends DefaultModule {
	public void init() {
		scheduleDailyTask();
		scheduleDailyTaskPM();
		scheduleDailyTaskSoftware();
		scheduleHourlyTaskStaffWorkload();
		super.init();
	}
	
	private static RentalOverdue  jobSetOverdueStatus; 
	private static RentalDueReminder jobSendMailDueReminder;
	private static PMOverdue jobSetPMOverdueStatus; 
	private static SoftwareExpiredDate jobSetSoftwareExpired; 
	private static PMDueReminder jobPMSendMailDueReminder; 
	
	public void setOverdueStatus() {
		EammsDao dao = (EammsDao) getDao();
		try {
			dao.setOverdueStatus();
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
	}
	
	public void scheduleDailyTask( ){
		jobSetOverdueStatus = new RentalOverdue();
		String taskName = "setOverdueStatus";
		String taskGroup ="rentalprocess";
		String taskDesc="To set rental status due to overdue";
		int hour=0; //default 0 set up to midnight
		int min =1; //default 1
		JobUtil.removeTask(taskName, taskGroup, jobSetOverdueStatus);
		JobUtil.scheduleDailyTask(taskName, taskGroup, taskDesc, jobSetOverdueStatus, hour, min);
		
		jobSendMailDueReminder = new RentalDueReminder();
		String taskName1 = "jobSendMailDueReminder";
		String taskGroup1 ="rentalprocess";
		String taskDesc1="To send mail due reminder";		
		JobUtil.removeTask(taskName1, taskGroup1, jobSetOverdueStatus);
		JobUtil.scheduleDailyTask(taskName1, taskGroup1, taskDesc1, jobSendMailDueReminder, hour, min);
		
	}
	
	public void scheduleDailyTaskPM(){
		jobSetPMOverdueStatus = new PMOverdue();
		String taskName = "setPMOverdueStatus";
		String taskGroup ="preventiveMaintenance";
		String taskDesc="To set Preventive Maintenance status due to overdue";
		int hour = 0; //default 0 set up to midnight
		int min  = 1; //default 1
		JobUtil.removeTask(taskName, taskGroup, jobSetPMOverdueStatus);
		JobUtil.scheduleDailyTask(taskName, taskGroup, taskDesc, jobSetPMOverdueStatus, hour, min);
		
		jobPMSendMailDueReminder = new PMDueReminder();
		String taskName1 = "jobPMSendMailDueReminder";
		String taskGroup1 ="pmProcess";
		String taskDesc1="To send mail due reminder for PM process";		
		JobUtil.removeTask(taskName1, taskGroup1, jobPMSendMailDueReminder);
		JobUtil.scheduleDailyTask(taskName1, taskGroup1, taskDesc1, jobPMSendMailDueReminder, hour, min);
	}
	
	public void scheduleDailyTaskSoftware(){
		jobSetSoftwareExpired = new SoftwareExpiredDate();
		String taskName = "setSoftwareExpiryDate";
		String taskGroup ="software";
		String taskDesc="To set status of the software to Expired";
		int hour=10; //default 0 set up to midnight TODO change the time
		int min =41; //default 1
		JobUtil.removeTask(taskName, taskGroup, jobSetSoftwareExpired);
		JobUtil.scheduleDailyTask(taskName, taskGroup, taskDesc, jobSetSoftwareExpired, hour, min);
	}
	
	public void scheduleHourlyTaskStaffWorkload()
	{
		String taskName = StaffWorkloadCalculationTask.TASKNAME;
		String taskGroup = StaffWorkloadCalculationTask.TASKGROUP;
		String taskDesc = StaffWorkloadCalculationTask.TASKDESC;
		
		int numMinutes = 60;
		try
		{
			numMinutes = Integer.parseInt(Application.getInstance().getProperty("staffWorkload_scheduler_min"));
		}
		catch(Exception e)
		{
			Log.getLog(getClass()).error(e, e);
		}
		JobUtil.scheduleMinuteTask(taskName, taskGroup, taskDesc, new StaffWorkloadCalculationTask(), numMinutes);
	}
	
	public void sendEmailRentalDueReminder(String id)
	{
			
		String smtpServer = Application.getInstance().getProperty("smtp.server");
		String adminEmail = Application.getInstance().getProperty("admin.email");
		
		String rentalId="";
		String requestorName ="";
		String requestorEmail ="";
		String toDate="";
		String engineerName="";
		String engineerEmail="";
		
		EammsDao dao = (EammsDao) getDao();
		try {
			DefaultDataObject reqInfo = dao.getRentalReqestInfo(id);
			if(reqInfo !=null){
				rentalId = (String)reqInfo.getProperty("rentalId");
				requestorName = (String)reqInfo.getProperty("requestorName");
				requestorEmail = (String)reqInfo.getProperty("requestorEmail");
				toDate = (String)reqInfo.getProperty("toDate");
				engineerName = (String)reqInfo.getProperty("engineerName");
				engineerEmail = (String)reqInfo.getProperty("engineerEmail");
			}
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		
		if(Mailer.isValidEmail(requestorEmail)){
			String[] subjectArgs = new String[] {rentalId};
	        String subject = Application.getInstance().getMessage("eamms.rental.emailDueReminder.subject", subjectArgs);
	        
			String[] contentArgs = new String[] {requestorName, toDate, rentalId, engineerName};
	        String content = Application.getInstance().getMessage("eamms.rental.emailDueReminder.content", contentArgs);
	         
	        MailUtil.sendEmail(smtpServer, true, adminEmail, requestorEmail, engineerEmail, null,  subject , content);
	        Log.getLog(getClass()).info("sending mail to " + requestorEmail + " subject : " + subject);
		} 
		
	}
	
	
	public Collection getRentalReqestDueReminderListing() {
		EammsDao dao = (EammsDao) getDao();
		try {
			return dao.getRentalReqestDueReminderListing();
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return new ArrayList();
		}
	}
	
	public Collection getOverdueItemsListing() {
		EammsDao dao = (EammsDao) getDao();
		try {
			return dao.getOverdueItemsListing();
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return new ArrayList();
		}
	}
	
	public void insertRentalStatusTrail(DefaultDataObject obj) {
		EammsDao dao = (EammsDao) getDao();
		try {
			dao.insertRentalStatusTrail(obj);
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
	}
	
	public Collection getPMOverdueItemsListing() {
		EammsDao dao = (EammsDao) getDao();
		try {
			return dao.getPMOverdueItemsListing();
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return new ArrayList();
		}
	}

	public void setPMOverdueStatus(DefaultDataObject obj) {
		EammsDao dao = (EammsDao) getDao();
		try {
			dao.setPMOverdueStatus(obj);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
	}
	
	public void setSoftwareExpiredStatus(DefaultDataObject obj) {
		EammsDao dao = (EammsDao) getDao();
		try {
			dao.setSoftwareExpiredStatus(obj);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
	}
	
	public Collection getSoftwareExpiredDate() {
		EammsDao dao = (EammsDao) getDao();
		try {
			return dao.getSoftwareExpiredDate();
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return new ArrayList();
		}
	}
	
	public Collection getPMReqestDueReminderListing() {
		EammsDao dao = (EammsDao) getDao();
		try {
			return dao.getPMReqestDueReminderListing();
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return new ArrayList();
		}
	}
	
	public String getActivityId(String originProcessId) {
		EammsDao dao = (EammsDao) getDao();
		try {
			return dao.getActivityId(originProcessId);
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return "";
		}
	}
	
	
	public HashMap getEngineersAssignedMSR(String id) {
		EammsDao dao = (EammsDao) getDao();
		try {
			return dao.getEngineersAssignedMSR(id);
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return new HashMap();
		}
	}
	
	public void sendEmailPMDueReminder(String id) {
		String smtpServer = Application.getInstance().getProperty("smtp.server");
		String adminEmail = Application.getInstance().getProperty("admin.email");
		
		String pmRequestId = "";
		String requestorEmail ="";
		String endDate = "";
		String engineerName="";
		String engineerEmail="";
		
		EammsDao dao = (EammsDao) getDao();
		try {
			DefaultDataObject reqInfo = dao.getPMInfo(id);
			if(reqInfo !=null){
				pmRequestId = (String)reqInfo.getProperty("c_pmRequestId");
				requestorEmail = (String)reqInfo.getProperty("requestorEmail");
				endDate = (String)reqInfo.getProperty("c_endDate");
				
				for(int i = 1; i<= 4; i++){
					engineerName = (String)reqInfo.getProperty("engName"+i);
					engineerEmail = (String)reqInfo.getProperty("emailEng"+i);
					
					if(Mailer.isValidEmail(requestorEmail)){
						String[] subjectArgs = new String[] {pmRequestId};
				        String subject = Application.getInstance().getMessage("eamms.pm.emailDueReminder.subject", subjectArgs);
				        
						String[] contentArgs = new String[] {engineerName, pmRequestId, endDate};
				        String content = Application.getInstance().getMessage("eamms.pm.emailDueReminder.content", contentArgs);
				         
				        MailUtil.sendEmail(smtpServer, true, adminEmail, engineerEmail, requestorEmail, null,  subject , content);
				        Log.getLog(getClass()).info("sending mail to " + requestorEmail + " subject : " + subject);
					} 
				}
			}
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
	}
	
	public DefaultDataObject getTXMReportInfo(String id) {
		EammsDao dao = (EammsDao) getDao();
		try {
			return dao.getTXMReportInfo(id);
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return new DefaultDataObject();
		}
	}
	
	public String getTXMReportFollowupInfo(String id) {
		EammsDao dao = (EammsDao) getDao();
		try {
			Collection col= dao.getTXMReportFollowupInfo(id);
			String ret="";
			
			for(Iterator iter=col.iterator();iter.hasNext(); ){
				HashMap map = (HashMap)iter.next();	
				ret += map.get("c_details") +",";
			}
			
			if ( !"".equals(ret) && ret.charAt(ret.length()-1)==',')
			 {
			  ret = ret.substring(0, ret.length()-1);
			  
			 }
			
			return ret;
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return "";
		}
	}
	
	public Collection getTXMReportRemarksInfo(String id) {
		EammsDao dao = (EammsDao) getDao();
		try {
			return dao.getTXMReportRemarksInfo(id);
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return new ArrayList();
		}
	}

	public void calculateStaffWorkload()
	{
		EammsDao dao = (EammsDao) getDao();
		try 
		{
			Collection<DefaultDataObject> userCol = dao.getAllStaff();
			if(userCol != null && !userCol.isEmpty())
			{
				Date lastUpdatedDate = new Date();
				for(DefaultDataObject userObj : userCol)
				{
					String userId = (String) userObj.getProperty("userId");
					String username = (String) userObj.getProperty("username");
					int numOfCM = calculateCMOnHand(username);
					int numOfPM = calculatePMOnhand(username);
					int numOfWO = calculateWOOnhand(userId);
					
					userObj.setProperty("cmOnHand", numOfCM);
					userObj.setProperty("pmOnHand", numOfPM);
					userObj.setProperty("woOnHand", numOfWO);
					userObj.setProperty("lastUpdatedDate", lastUpdatedDate);
					
					userObj.setId(UuidGenerator.getInstance().getUuid());
					dao.insertStaffWorkload(userObj);
				}
			}
		} 
		catch (Exception e) 
		{
			Log.getLog(getClass()).error("error @ EammsModule.calculateStaffWorkload()" + e, e);
		}
	}

	private int calculateWOOnhand(String userId)
	{
		EammsDao dao = (EammsDao) getDao();
		try 
		{
			int numOfWO = dao.getCountWorkloadInWO(userId);
			return numOfWO;
		} 
		catch (Exception e) 
		{
			Log.getLog(getClass()).error("error @ EammsModule.calculateWOOnhand(1)" + e, e);
			return 0;
		}
	}

	private int calculatePMOnhand(String userId)
	{
		EammsDao dao = (EammsDao) getDao();
		try 
		{
			int numOfPM = dao.getCountWorkloadInPM(userId);
			return numOfPM;
		} 
		catch (Exception e) 
		{
			Log.getLog(getClass()).error("error @ EammsModule.calculatePMOnhand(1)" + e, e);
			return 0;
		}
	}

	private int calculateCMOnHand(String userId)
	{
		EammsDao dao = (EammsDao) getDao();
		try 
		{
			int numOfCM = dao.getCountWorkloadInCM(userId);
			return numOfCM;
		} 
		catch (Exception e) 
		{
			Log.getLog(getClass()).error("error @ EammsModule.calculateCMOnHand(1)" + e, e);
			return 0;
		}
	}
}
