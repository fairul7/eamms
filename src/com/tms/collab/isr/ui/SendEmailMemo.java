package com.tms.collab.isr.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.collab.isr.model.AssignmentRemarksObject;
import com.tms.collab.isr.model.ClarificationObject;
import com.tms.collab.isr.model.RemarksObject;
import com.tms.collab.isr.model.RequestModel;
import com.tms.collab.isr.model.RequestObject;
import com.tms.collab.isr.model.SuggestedResolutionObject;
import com.tms.collab.isr.setting.model.ConfigModel;
import com.tms.collab.isr.setting.model.EmailSetting;
import com.tms.collab.messaging.model.IntranetAccount;
import com.tms.collab.messaging.model.Message;
import com.tms.collab.messaging.model.MessagingException;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.SmtpAccount;
import com.tms.collab.messaging.model.Util;
import com.tms.ekms.setup.model.SetupException;
import com.tms.ekms.setup.model.SetupModule;
import com.tms.util.MailUtil;

public class SendEmailMemo {
	
	public void init(){
		
	}

	 public void sendNotification(String emailFor, RemarksObject remarksObject, RequestObject requestObject,
			 ClarificationObject clarificationObject, SuggestedResolutionObject suggestedResolutionObject, AssignmentRemarksObject assignmentRemarksObject ){
		 
		Log log = Log.getLog(getClass());		
		String emailSubject = null; boolean replyClarification = false;
		Application application = Application.getInstance();
		ConfigModel configModel = (ConfigModel) application.getModule(ConfigModel.class);
		SetupModule setupModel = (SetupModule) application.getModule(SetupModule.class);
		
		Collection emailSettingCols = null;		
		//set default Email Subject
		if (emailFor.equals(EmailSetting.REMARKS))			
			emailSubject = application.getMessage(EmailSetting.remarksDefaultSubject);	
		else if (emailFor.equals(EmailSetting.ASSIGNMMENT))	
			emailSubject = application.getMessage(EmailSetting.assigmentDefaultSubject);
		else if (emailFor.equals(EmailSetting.CLARIFICATION))	
			emailSubject = application.getMessage(EmailSetting.clarificationDefaultSubject);
		else if (emailFor.equals(EmailSetting.NEW_REQUEST))	
			emailSubject = application.getMessage(EmailSetting.newRequestDefaultSubject);
		else if (emailFor.equals(EmailSetting.RESOLUTION))	
			emailSubject = application.getMessage(EmailSetting.resolutionDefaultSubject);
		else if (emailFor.equals(EmailSetting.CLARIFICATION_REPLY))	{
			emailSubject = application.getMessage(EmailSetting.clarificationReplyDefaultSubject);
			replyClarification = true;
			emailFor = EmailSetting.CLARIFICATION;
		}
		emailSettingCols = configModel.selectEmailSettings(new String[] {emailFor});
		
		StringBuffer emailContents = new StringBuffer();
		int maxChar = 255;
		String smtpServer = null;
		String adminEmail = null;
		String siteURL = null;
		String strNotifyMethod="b";
		
	/*	String useMemo = application.getProperty("isr.useMemo");
		String useMemoEmail = application.getProperty("isr.useMemoEmail");
		String useEmail = application.getProperty("isr.useEmail");*/
			
		try {
			smtpServer = setupModel.get("siteSmtpServer");
			adminEmail = setupModel.get("siteAdminEmail");
			siteURL = setupModel.get("siteUrl");			
		}
		catch(SetupException error) {
		}
		
		User user = application.getCurrentUser();
        String userId = user.getId();
        String userName = user.getName();
	
		RequestModel requestModel = (RequestModel) application.getModule(RequestModel.class);
		//get recipient list
		Collection colRecipientList = null;	
		
		if(smtpServer != null && adminEmail != null) {
			if(emailSettingCols != null) {
				if(emailSettingCols.size() > 0) {
					EmailSetting emailSetting = (EmailSetting) emailSettingCols.iterator().next();
					if(emailFor.equals(emailSetting.getEmailFor())) {
						if(emailSetting.getEmailSubject() != null &&
								!"".equals(emailSetting.getEmailSubject())) {
							emailSubject = emailSetting.getEmailSubject();
						}
						if(emailSetting.getEmailBody() != null &&
								!"".equals(emailSetting.getEmailBody())) {
							emailContents.append("<p>" + getHtmlText(emailSetting.getEmailBody()) + "</p>");
						}
						strNotifyMethod = emailSetting.getNotifyMethod().toString();
					}						
				}else //set default email Body
				{
					if (emailFor.equals(EmailSetting.REMARKS))			
						emailContents.append("<p>" + getHtmlText(application.getMessage(EmailSetting.remarksDefaultBody))+ "</p>");	
					else if (emailFor.equals(EmailSetting.ASSIGNMMENT))	
						emailContents.append("<p>" + getHtmlText(application.getMessage(EmailSetting.assignmentDefaultBody))+ "</p>");
					else if (emailFor.equals(EmailSetting.CLARIFICATION))	
						emailContents.append("<p>" + getHtmlText(application.getMessage(EmailSetting.clarificationDefaultBody))+ "</p>");
					else if (emailFor.equals(EmailSetting.NEW_REQUEST))	
						emailContents.append("<p>" + getHtmlText( application.getMessage(EmailSetting.newRequestDefaultBody))+ "</p>");
					else if (emailFor.equals(EmailSetting.RESOLUTION))	
						emailContents.append("<p>" + getHtmlText(application.getMessage(EmailSetting.resolutionDefaultBody))+ "</p>");
					
				}
			}
			
			
			DateFormat todayDigestDateFormat = new SimpleDateFormat(application.getProperty("globalDateLong"));
			DateFormat dateTimeLongFormat = new SimpleDateFormat(application.getProperty("globalDatetimeLong"));
			emailContents.append("<p><strong>Date: " + todayDigestDateFormat.format(new Date()) + "</strong></p>");	
			
			if (emailFor.equals(EmailSetting.REMARKS)){
				
				colRecipientList = requestModel.getDeptAdminAssigneeUserId(requestObject.getRequestId());	
				
				emailContents.append("<p><strong>New Remarks Added</strong></p>");
				emailContents.append("<table width=\"100%\" cellpadding=\"4\" cellspacing=\"1\" border=\"0\">" +
						"<tr style=\"font-weight:bold; background-color:#BBD5F2; color:#FFFFFF\">" +
							"<td>" + application.getMessage("isr.label.requestId") + "</td>" +
							"<td>" + application.getMessage("isr.label.attentionTo") + "</td>" +
							"<td>" + application.getMessage("isr.label.subject") + "</td>" +
							"<td>" + application.getMessage("isr.label.dateTimeRemarks")+ "</td>" +
							"<td>" + application.getMessage("isr.label.newRemarkAdded") + "</td>" +
							"<td>" + application.getMessage("isr.label.createdBy") + "</td>" +
						"</tr>");            	   						
				emailContents.append("<tr>" + 
							"<td><a href='"+ siteURL + "/ekms/isr/attendantProcessRequest.jsp?requestId="+remarksObject.getRequestId()+"'>"+ remarksObject.getRequestId() + "</a></td>" +
							"<td>" + requestObject.getRequestToDeptName() + "</td>" +
							"<td>" + requestObject.getRequestSubject() + "</td>" +
							"<td>" + dateTimeLongFormat.format(new Date()) + "</td>" +
							"<td>" + remarksObject.getRemarks() + "</td>" +
							"<td>" + remarksObject.getCreatedBy() + "</td>" +
							"</tr>");
			
				emailContents.append("</table>");	
				
		}else if (emailFor.equals(EmailSetting.NEW_REQUEST)){

			colRecipientList = requestModel.getMutlipleDeptAdminEmailAdd(requestObject.getRequestId());
			
			requestObject = requestModel.getRequest(requestObject.getRequestId(), true, false, true, false);
			
			emailContents.append("<p><strong>New Request(s) / Assignment(s)</strong></p>");
			emailContents.append("<table width=\"100%\" cellpadding=\"4\" cellspacing=\"1\" border=\"0\">" +
					"<tr style=\"font-weight:bold; background-color:#BBD5F2; color:#FFFFFF\">" +
						"<td>" + application.getMessage("isr.label.requestId") + "</td>" +
						"<td>" + application.getMessage("isr.label.from") + "</td>" +
						"<td>" + application.getMessage("isr.label.subject") + "</td>" +
						"<td>" + application.getMessage("isr.label.dateTimeReported") + "</td>" +
						"<td>" + application.getMessage("isr.label.requesterName") + "</td>" +
					"</tr>");
			emailContents.append("<tr>" +			
						"<td><a href='"+ siteURL + "/ekms/isr/attendantProcessRequest.jsp?requestId="+requestObject.getRequestId()+"'>"+ requestObject.getRequestId() + "</a></td>" +
						"<td>" + requestObject.getRequestFromDeptName() + "</td>" +
						"<td>" + requestObject.getRequestSubject() + "</td>" +
						"<td>" + dateTimeLongFormat.format(new Date()) + "</td>" +
						"<td>" + requestObject.getCreatedBy() + "</td>" +
						"</tr>");
		
			emailContents.append("</table>");
			
		}else if(emailFor.equals(EmailSetting.RESOLUTION)){
			
			colRecipientList = requestModel.getRequestorEmailAdd(requestObject.getRequestId());
			
			emailContents.append("<p><strong>Resolution</strong></p>");
			emailContents.append("<table width=\"100%\" cellpadding=\"4\" cellspacing=\"1\" border=\"0\">" +
					"<tr style=\"font-weight:bold; background-color:#BBD5F2; color:#FFFFFF\">" +
						"<td>" + application.getMessage("isr.label.requestId") + "</td>" +
						"<td>" + application.getMessage("isr.label.attentionTo") + "</td>" +
						"<td>" + application.getMessage("isr.label.subject") + "</td>" +
						"<td>" + application.getMessage("isr.label.datetimeResolution")+ "</td>" +
					/*	"<td>" + application.getMessage("isr.label.conResolution") + "</td>" +*/
						"<td>" + application.getMessage("isr.label.createdBy") + "</td>" +
					"</tr>");            	   						
			emailContents.append("<tr>" + 
						"<td><a href='" + siteURL + "/ekms/isr/requestorResolveRequest.jsp?requestId="+requestObject.getRequestId()+"'>"+ requestObject.getRequestId() + "</a></td>" +
						"<td>" + requestObject.getRequestToDeptName() + "</td>" +
						"<td>" + requestObject.getRequestSubject() + "</td>" +
						"<td>" + dateTimeLongFormat.format(new Date()) + "</td>" +
				/*		"<td>" + requestObject.getRequestResolution() + "</td>" +*/
						"<td>" + userName + "</td>" +
						"</tr>");
		
			emailContents.append("</table>");
			
			
		}else if(emailFor.equals(EmailSetting.ASSIGNMMENT)){
			
			colRecipientList = requestModel.getAssigneeEmailAdd(assignmentRemarksObject.getRequestId());
			Collection colassigneeList = requestModel.getAssigneeUserId(assignmentRemarksObject.getRequestId());
			
			emailContents.append("<p><strong>New Assignee(s) Added</strong></p>");
			emailContents.append("<table width=\"100%\" cellpadding=\"4\" cellspacing=\"1\" border=\"0\">" +
					"<tr style=\"font-weight:bold; background-color:#BBD5F2; color:#FFFFFF\">" +
						"<td>" + application.getMessage("isr.label.requestId") + "</td>" +
						"<td>" + application.getMessage("isr.label.attentionTo") + "</td>" +
						"<td>" + application.getMessage("isr.label.subject") + "</td>" +
						"<td>" + application.getMessage("isr.label.datetiemeAssign")+ "</td>" +
						"<td>" + application.getMessage("isr.label.newAssignee") + "</td>" +
						"<td>" + application.getMessage("isr.label.assignmentRemark") + "</td>" +						
						"<td>" + application.getMessage("isr.label.createdBy") + "</td>" +
					"</tr>");  
			
			//get assignee's name
			String strAssigneeName = "";
			 for (Iterator i = colassigneeList.iterator(); i.hasNext();) {				 
					Map tempAdd = (Map) i.next();
					tempAdd.get("userId");
					SecurityService srv = (SecurityService)application.getService(SecurityService.class);
					try {
						User user1 = srv.getUser(tempAdd.get("userId").toString());
				
						if (!(strAssigneeName.equals("")))
							strAssigneeName += ", ";
						strAssigneeName +=user1.getName();
				
					} catch (SecurityException e) {
						e.printStackTrace();
					}			   				
			 }
			 
			emailContents.append("<tr>" + 
						"<td><a href='"+ siteURL + "/ekms/isr/attendantProcessRequest.jsp?requestId="+assignmentRemarksObject.getRequestId()+"'>"+ assignmentRemarksObject.getRequestId() + "</a></td>" +
						"<td>" + requestObject.getRequestToDeptName() + "</td>" +
						"<td>" + requestObject.getRequestSubject() + "</td>" +
						"<td>" + dateTimeLongFormat.format(new Date()) + "</td>" +
						"<td>" + strAssigneeName + "</td>" +
						"<td>" + assignmentRemarksObject.getAssignmentRemarks() + "</td>" +
						"<td>" + userName + "</td>" +
						"</tr>");
		
			emailContents.append("</table>");
			
		}else if(emailFor.equals(EmailSetting.CLARIFICATION)){
			
			if (replyClarification){
				colRecipientList = requestModel.getClarificationRecipientId(clarificationObject);
				emailContents.append("<p><strong>Replied Clarification </strong></p>");
				emailContents.append("<table width=\"100%\" cellpadding=\"4\" cellspacing=\"1\" border=\"0\">" +
						"<tr style=\"font-weight:bold; background-color:#BBD5F2; color:#FFFFFF\">" +
							"<td>" + application.getMessage("isr.label.requestId") + "</td>" +
							"<td>" + application.getMessage("isr.label.attentionTo") + "</td>" +
							"<td>" + application.getMessage("isr.label.subject") + "</td>" +
							"<td>" + application.getMessage("isr.label.datetimeClarification")+ "</td>" +
							"<td>" + application.getMessage("isr.label.clarificationReply") + "</td>" +
							"<td>" + application.getMessage("isr.label.createdBy") + "</td>" +
						"</tr>");            	   						
				emailContents.append("<tr>" + 
							"<td><a href='"+ siteURL + "/ekms/isr/attendantProcessRequest.jsp?requestId="+clarificationObject.getRequestId()+"'>"+ clarificationObject.getRequestId() + "</a></td>" +
							"<td>" + requestObject.getRequestToDeptName() + "</td>" +
							"<td>" + requestObject.getRequestSubject() + "</td>" +
							"<td>" + dateTimeLongFormat.format(new Date()) + "</td>" +
							"<td>" + clarificationObject.getClarificationAnswer() + "</td>" +
							"<td>" + userName + "</td>" +
							"</tr>");
			
				emailContents.append("</table>");	
			
			}else {
				colRecipientList = requestModel.getRequestorEmailAdd(clarificationObject.getRequestId());
				
				emailContents.append("<p><strong>New Clarification Added</strong></p>");
				emailContents.append("<table width=\"100%\" cellpadding=\"4\" cellspacing=\"1\" border=\"0\">" +
						"<tr style=\"font-weight:bold; background-color:#BBD5F2; color:#FFFFFF\">" +
							"<td>" + application.getMessage("isr.label.requestId") + "</td>" +
							"<td>" + application.getMessage("isr.label.attentionTo") + "</td>" +
							"<td>" + application.getMessage("isr.label.subject") + "</td>" +
							"<td>" + application.getMessage("isr.label.datetimeClarification")+ "</td>" +
							"<td>" + application.getMessage("isr.label.newClarificationAdded") + "</td>" +
							"<td>" + application.getMessage("isr.label.createdBy") + "</td>" +
						"</tr>");            	   						
				emailContents.append("<tr>" + 
							"<td><a href='"+ siteURL + "/ekms/isr/requestorEditRequest.jsp?requestId="+clarificationObject.getRequestId()+"'>"+ clarificationObject.getRequestId() + "</a></td>" +
							"<td>" + requestObject.getRequestToDeptName() + "</td>" +
							"<td>" + requestObject.getRequestSubject() + "</td>" +
							"<td>" + dateTimeLongFormat.format(new Date()) + "</td>" +
							"<td>" + clarificationObject.getClarificationQuestion() + "</td>" +
							"<td>" + userName + "</td>" +
							"</tr>");
			
				emailContents.append("</table>");	
				
				//Update 
				clarificationObject.setCreatedById(userId.toString());				
			
				 requestModel.updateClarificationRecipientId(clarificationObject);
			}
			
			 replyClarification = false;
		}
	 
		// Send Memo
			if ("m".equals(strNotifyMethod) || "b".equals(strNotifyMethod)) {
				 MessagingModule mm;
				 SmtpAccount smtpAccount = null;
				 Message message;	
				 mm = Util.getMessagingModule();
				 
				 try {
					smtpAccount = mm.getSmtpAccountByUserId(userId);
					} catch (MessagingException e) {
						e.printStackTrace();}		
																
				 // construct the message to send
				   message = new Message();
				   message.setMessageId(UuidGenerator.getInstance().getUuid());
				   
				   IntranetAccount intranetAccount = null;				   
				  
				   if(colRecipientList != null && colRecipientList.size() > 0){
					   List listRecepient = new ArrayList(colRecipientList.size());
						 for (Iterator i = colRecipientList.iterator(); i.hasNext();) {				 
							Map tempAdd = (Map) i.next();
							if (tempAdd.get("userId").toString() != null){
					            try {intranetAccount = Util.getMessagingModule().getIntranetAccountByUserId(tempAdd.get("userId").toString());} catch (Exception e){e.printStackTrace();}	            	   
					            if(intranetAccount != null) {
								    String add = intranetAccount.getIntranetUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
								    listRecepient.add(add);
						        }	
							}
						 }      
					
					List listBcc = new ArrayList();
					try {
						intranetAccount = Util.getMessagingModule().getIntranetAccountByUserId(userId);
						} catch (Exception e)
						{e.printStackTrace();}	            	   
		            if(intranetAccount != null) {
					    String listingBcc = intranetAccount.getIntranetUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
					    listBcc.add(listingBcc);
			        }
					
					if (listRecepient.size() > 0)
						message.setToIntranetList(listRecepient);
				   
					 message.setSubject(emailSubject + " - " + requestObject.getRequestId());				 
					 message.setBody(emailContents.toString());
					 message.setBccIntranetList(listBcc);
					 message.setMessageFormat(Message.MESSAGE_FORMAT_HTML);
					 message.setDate(new java.util.Date());
		             //send Memo
		         	try {
						mm.sendMessage(smtpAccount, message, userId, false);
					} catch (MessagingException e) {
						Log.getLog(getClass()).error("Error Sending Memo ", e);
					}
				}
			}
			if("e".equals(strNotifyMethod) || "b".equals(strNotifyMethod)){
				//send mail
				if (colRecipientList != null && colRecipientList.size() > 0){			
					for(Iterator iiterator = colRecipientList.iterator(); iiterator.hasNext();){
						Map tempAdd = (Map) iiterator.next();
						if (tempAdd.get("email1") != null && tempAdd.get("userId") != null){							
							try {
								//sendEmail(String smtpServer, boolean isHtml, String fromEmail, String toEmail, String ccEmail, String bccEmail, String subject, String content) 
								MailUtil.sendEmail(smtpServer, true, adminEmail, tempAdd.get("email1").toString(), null, null, emailSubject + " - " + requestObject.getRequestId(), emailContents.toString());
							}
							catch(Exception error) {
								Log.getLog(getClass()).error("Error Sending Email ", error);
							}
						}
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
