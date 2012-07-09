package com.tms.fms.eamms.model;

import java.util.ArrayList;
import java.util.Collection;

import com.tms.fms.util.JobUtil;
import com.tms.util.MailUtil;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DefaultDataObject;
import kacang.model.DefaultModule;
import kacang.util.Log;
import kacang.util.Mailer;

public class EammsModule extends DefaultModule {
	public void init() {
		scheduleDailyTask();
		super.init();
	}
	
	private static RentalOverdue  jobSetOverdueStatus; 
	private static RentalDueReminder jobSendMailDueReminder;
	
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
		int hour=14; //default 0 set up to midnight
		int min =47; //default 1
		JobUtil.removeTask(taskName, taskGroup, jobSetOverdueStatus);
		JobUtil.scheduleDailyTask(taskName, taskGroup, taskDesc, jobSetOverdueStatus, hour, min);
		
		jobSendMailDueReminder = new RentalDueReminder();
		String taskName1 = "jobSendMailDueReminder";
		String taskGroup1 ="rentalprocess";
		String taskDesc1="To send mail due reminder";		
		JobUtil.removeTask(taskName1, taskGroup1, jobSetOverdueStatus);
		JobUtil.scheduleDailyTask(taskName1, taskGroup1, taskDesc1, jobSendMailDueReminder, hour, min);
		
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

}
