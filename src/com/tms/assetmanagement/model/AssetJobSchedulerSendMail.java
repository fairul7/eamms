package com.tms.assetmanagement.model;

import java.text.SimpleDateFormat;
import com.tms.ekms.setup.model.SetupModule;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import com.tms.util.MailUtil;

import kacang.Application;
import kacang.services.scheduling.BaseJob;
import kacang.services.scheduling.JobTaskExecutionContext;
import kacang.services.scheduling.SchedulingException;
import kacang.services.security.SecurityException;
import kacang.services.security.User;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.collab.calendar.ui.UserUtil;
import com.tms.collab.messaging.model.IntranetAccount;
import com.tms.collab.messaging.model.Message;
import com.tms.collab.messaging.model.MessagingException;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.SmtpAccount;
import com.tms.collab.messaging.model.Util;
import com.tms.util.FormatUtil;
import java.text.DateFormat;
import com.tms.collab.isr.setting.model.ConfigModel;

public class AssetJobSchedulerSendMail extends BaseJob {

	public void execute(JobTaskExecutionContext context) throws SchedulingException {
		 Calendar TodayDate = Calendar.getInstance();
	
		Application app = Application.getInstance();
		AssetModule  Mod = (AssetModule)app.getModule(AssetModule.class);
		
		String strNotificationId = (String)context.getJobTaskData().get("id");
		DataNotification objNoti = null; 
		
		Collection colEmailDetails = Mod.retrieveNotification(strNotificationId);
		
		if (colEmailDetails != null && colEmailDetails.size() > 0){
			  for (Iterator iterator = colEmailDetails.iterator(); iterator.hasNext();){
				  	objNoti = (DataNotification) iterator.next();
			  				
			Collection colRecipient = Mod.retrieveAddressee(strNotificationId);
			if(colRecipient != null && colRecipient.size() > 0){			
				sendNotification(colRecipient, objNoti);
			}
			  }
		} 	   				
	}
	
	public void sendNotification(Collection colRecipient, DataNotification objNoti){
		Log log = Log.getLog(getClass());						
		
		Application application = Application.getInstance();
		ConfigModel configModel = (ConfigModel) application.getModule(ConfigModel.class);
		SetupModule setupModel = (SetupModule) application.getModule(SetupModule.class);
		
		StringBuffer emailContents = new StringBuffer();
		
		int maxChar = 255; String emailSubject = null;
		String smtpServer = null;
		String adminEmail = null;
		String siteURL = null;		
		
		try {
			smtpServer = setupModel.get("siteSmtpServer");
			adminEmail = setupModel.get("siteAdminEmail");							
		}
		catch(Exception error) {
		}
			
		DateFormat dateTimeLongFormat = new SimpleDateFormat(application.getProperty("globalDatetimeLong"));
		User user = null;
		try {
			user = UserUtil.getUser((String)objNoti.getSenderID());
		} catch (SecurityException e) {		
		}
		//Subject
		emailSubject = application.getMessage("asset.label.assetNotification", "Asset Management Notification") + ": " + (String)objNoti.getNotificationTitle();	
		emailContents.append("<p>" + getHtmlText(application.getMessage("asset.label.defaultAssetMailBody"))+ "</p>");	
		emailContents.append("<p><strong>Asset Management Notification</strong></p>");
		emailContents.append("<table width=\"100%\" cellpadding=\"4\" cellspacing=\"1\" border=\"0\">" +
				"<tr style=\"font-weight:bold; background-color:#BBD5F2; color:#FFFFFF\">" +
				"<td>" + application.getMessage("asset.label.title", "Title") + "</td>" +
				"<td>" + application.getMessage("asset.label.message", "Message") + "</td>" +
				"<td>" + application.getMessage("asset.label.createddate","Created Date") + "</td>" +
				"<td>" + application.getMessage("asset.label.createdBy", "Created By") + "</td>" +
				/*	"<td>" + application.getMessage("isr.label.requesterName") + "</td>" +*/
		"</tr>");
		emailContents.append("<tr>" +			
				"<td>" + objNoti.getNotificationTitle()+ "</td>" +
				"<td>" + objNoti.getNotificationMsg() + "</td>" +
				"<td>" + dateTimeLongFormat.format(objNoti. getDateCreated()) + "</td>" +					
				"<td>" + user.getName() + "</td>" +
		"</tr>");
			
		emailContents.append("</table>");		
			

		//	Send Memo
		if(objNoti.getNotifyMethod().equals("m")||objNoti.getNotifyMethod().equals("b")){
			MessagingModule mm;
			SmtpAccount smtpAccount = null;
			Message message;

			mm = Util.getMessagingModule();
	 
			try {
				smtpAccount = mm.getSmtpAccountByUserId(objNoti.getSenderID());
			} catch (MessagingException e) {
			}

			// construct the message to send
			message = new Message();
			message.setMessageId(UuidGenerator.getInstance().getUuid());
	  
			IntranetAccount intranetAccount = null;

			List listRecepient = new ArrayList(colRecipient.size());
			List listEmail = new ArrayList(colRecipient.size());
			for (Iterator i = colRecipient.iterator(); i.hasNext();) {
				DataAddressee objAddressee = (DataAddressee) i.next();			                	            
				
				try {
					intranetAccount = Util.getMessagingModule().getIntranetAccountByUserId((String) objAddressee.getRecipientId());
				} catch (MessagingException e) {
				}	  
					
				if(intranetAccount != null) {
					String add = intranetAccount.getIntranetUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
					listRecepient.add(add);
				}	
			}
			
			if (listRecepient.size() > 0)	
				message.setToIntranetList(listRecepient);
			
			message.setSubject(emailSubject.toString());		 
			message.setBody(emailContents.toString());
			message.setMessageFormat(Message.MESSAGE_FORMAT_HTML);
			message.setDate(new java.util.Date());
			
			if(objNoti.getNotifyMethod().equals("m")||objNoti.getNotifyMethod().equals("b"))
				try {
					mm.sendMessage(smtpAccount, message, objNoti.getSenderID(), false);
			} catch (MessagingException e) {
			}	           
		}
		     		
		if(objNoti.getNotifyMethod().equals("e")||objNoti.getNotifyMethod().equals("b")){
			//send mail
			if (colRecipient != null && colRecipient.size() > 0){			
				for(Iterator iiterator = colRecipient.iterator(); iiterator.hasNext();){
					DataAddressee objAddressee = (DataAddressee) iiterator.next();
					User userMail = null;
					try {
						userMail = UserUtil.getUser((String)objNoti.getSenderID());
					} catch (SecurityException e) {
					}					
					try {
						//sendEmail(String smtpServer, boolean isHtml, String fromEmail, String toEmail, String ccEmail, String bccEmail, String subject, String content) 
						MailUtil.sendEmail(smtpServer, true, adminEmail, userMail.getProperty("email1").toString(), null, null, emailSubject , emailContents.toString());
					}
					catch(Exception error) {
						Log.getLog(getClass()).error("Error Sending Email ", error);
					}		
				}
			}
		}
				 
	}	 

	private String getHtmlText(String rawText) {
		String htmlText = "";
		char textChar[] = rawText.toCharArray();
		
		for(int i=0; i<textChar.length; i++) {
			int ascii = textChar[i];
			if(ascii == 13) {
				htmlText += "<br />";
		       }	
			else {
				htmlText += textChar[i];
			}
		} 
		return htmlText;
	}
}


