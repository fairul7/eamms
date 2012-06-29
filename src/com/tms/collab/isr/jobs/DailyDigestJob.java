package com.tms.collab.isr.jobs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.services.scheduling.BaseJob;
import kacang.services.scheduling.JobTaskExecutionContext;
import kacang.services.scheduling.SchedulingException;
import kacang.services.security.User;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.collab.isr.model.RequestModel;
import com.tms.collab.isr.model.RequestObject;
import com.tms.collab.isr.model.StatusObject;
import com.tms.collab.isr.permission.model.ISRGroup;
import com.tms.collab.isr.permission.model.PermissionModel;
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

public class DailyDigestJob extends BaseJob {
	private static final String DAILY_DIGEST_ACTIVE_PROPERTY = "isr.dailyDigest.active";
	
	public void execute(JobTaskExecutionContext context) throws SchedulingException {
		Application application = Application.getInstance();
		String dailyDigestActive = application.getProperty(DAILY_DIGEST_ACTIVE_PROPERTY);
		PermissionModel permissionModel = (PermissionModel) application.getModule(PermissionModel.class);
		RequestModel requestModel = (RequestModel) application.getModule(RequestModel.class);
		
		if("true".equals(dailyDigestActive)) {
			Log.getLog(getClass()).debug("ISR Daily Digest is Triggered");
			Collection allISRUsers = permissionModel.getAllISRUsers();
			if(allISRUsers != null) {
				if(allISRUsers.size() > 0) {
					for(Iterator itr=allISRUsers.iterator(); itr.hasNext();) {
						User user = (User) itr.next();
						boolean isAdmin = permissionModel.hasISRRole(user.getId(), ISRGroup.ROLE_DEPT_ADMIN);
						Collection attendantNewRequest = null;
						Collection attendantReopenRequest = null;
						Collection attendantClosedRequest = null;
						Collection attendantAnsweredClarification = null;
						Collection requesterCompletedRequest = null;
						Collection requesterPendingClarification = null;
						Collection reminderRequest = null;
						
						if(isAdmin) {
							attendantNewRequest = requestModel.selectDailyDigestAttendingRequestByStatus(StatusObject.STATUS_ID_NEW, user.getId(), 0, -1);
						}
						else {
							attendantNewRequest = requestModel.selectDailyDigestAttendingRequestNewAssignment(user.getId(), 0, -1);
						}
						if(permissionModel.hasPermission(user.getId(), ISRGroup.PERM_CLARIFICATION)) {
							attendantAnsweredClarification = requestModel.selectDailyDigestAttendingRequestAnsweredClarification(user.getId(), 0, -1);
						}
						attendantReopenRequest = requestModel.selectDailyDigestAttendingRequestByStatus(StatusObject.STATUS_ID_REOPEN, user.getId(), 0, -1);
						attendantClosedRequest = requestModel.selectDailyDigestAttendingRequestByStatus(StatusObject.STATUS_ID_CLOSE, user.getId(), 0, -1);
						
						if(permissionModel.hasPermission(user.getId(), ISRGroup.PERM_NEW_REQUEST)) {
							requesterCompletedRequest = requestModel.selectDailyDigestRequestByStatus(StatusObject.STATUS_ID_COMPLETED, user.getId(), 0, -1);
							
							if(permissionModel.hasPermission(user.getId(), ISRGroup.PERM_EDIT_REQUEST)) {
								requesterPendingClarification = requestModel.selectDailyDigestRequesterPendingClarification(user.getId(), 0, -1);
							}
						}
						reminderRequest=requestModel.selectDailyDigestReminder(user.getId(), 0, -1);;
						
						boolean hasDailyDigest = false;
						if(attendantNewRequest != null) {
							if(attendantNewRequest.size() > 0) {
								hasDailyDigest = true;
							}
						}
						if(!hasDailyDigest) {
							if(attendantReopenRequest != null) {
								if(attendantReopenRequest.size() > 0) {
									hasDailyDigest = true;
								}
							}
						}
						if(!hasDailyDigest) {
							if(attendantClosedRequest != null) {
								if(attendantClosedRequest.size() > 0) {
									hasDailyDigest = true;
								}
							}
						}
						if(!hasDailyDigest) {
							if(attendantAnsweredClarification != null) {
								if(attendantAnsweredClarification.size() > 0) {
									hasDailyDigest = true;
								}
							}
						}
						if(!hasDailyDigest) {
							if(requesterCompletedRequest != null) {
								if(requesterCompletedRequest.size() > 0) {
									hasDailyDigest = true;
								}
							}
						}
						if(!hasDailyDigest) {
							if(requesterPendingClarification != null) {
								if(requesterPendingClarification.size() > 0) {
									hasDailyDigest = true;
								}
							}
						}
						if(!hasDailyDigest) {
							if(reminderRequest != null) {
								if(reminderRequest.size() > 0) {
									hasDailyDigest = true;
								}
							}
						}
						
						
						if(hasDailyDigest) {
							sendDailyDigestAsMail(user, attendantNewRequest, attendantReopenRequest, 
									attendantClosedRequest, attendantAnsweredClarification, 
									requesterCompletedRequest, requesterPendingClarification, reminderRequest);
						}
					}
				}
			}
		}
		else {
			Log.getLog(getClass()).debug("ISR Daily Digest is Disabled");
		}
	}

	private boolean sendDailyDigestAsMail(User user, Collection attendantNewRequest, Collection attendantReopenRequest, 
			Collection attendantClosedRequest, Collection attendantAnsweredClarification, 
			Collection requesterCompletedRequest, Collection requesterPendingClarification, Collection reminderRequest) {
		boolean isSuccess = true;
		Application application = Application.getInstance();
		ConfigModel configModel = (ConfigModel) application.getModule(ConfigModel.class);
		SetupModule setupModel = (SetupModule) application.getModule(SetupModule.class);
		Collection emailSettingCols = configModel.selectEmailSettings(new String[] {EmailSetting.DAILY_DIGEST});
		String emailSubject = application.getMessage("isr.label.dailyDigestDefaultEmailSubject");
		StringBuffer emailContents = new StringBuffer();
		int maxChar = 255;
		String smtpServer = null;
		String adminEmail = null;
		String strNotifyMethod="b";
		
		try {
			smtpServer = setupModel.get("siteSmtpServer");
			adminEmail = setupModel.get("siteAdminEmail");
		}
		catch(SetupException error) {
		}
		
		if(smtpServer != null && adminEmail != null) {
			if(emailSettingCols != null) {
				if(emailSettingCols.size() > 0) {
					EmailSetting emailSetting = (EmailSetting) emailSettingCols.iterator().next();
					if(EmailSetting.DAILY_DIGEST.equals(emailSetting.getEmailFor())) {
						if(emailSetting.getEmailSubject() != null &&
								!"".equals(emailSetting.getEmailSubject())) {
							emailSubject = emailSetting.getEmailSubject();
						}
						if(emailSetting.getEmailBody() != null &&
								!"".equals(emailSetting.getEmailBody())) {
							emailContents.append("<p>" + getHtmlText(emailSetting.getEmailBody()) + "</p>");
						}
						strNotifyMethod =  emailSetting.getNotifyMethod().toString();
		
					}
				}
			}
			
			DateFormat todayDigestDateFormat = new SimpleDateFormat(application.getProperty("globalDateLong"));
			DateFormat dateTimeLongFormat = new SimpleDateFormat(application.getProperty("globalDatetimeLong"));
			emailContents.append("<p><strong>Date: " + todayDigestDateFormat.format(new Date()) + "</strong></p>");
			
			
			if(reminderRequest != null) {
				if(reminderRequest.size() > 0) {
					Calendar now=Calendar.getInstance();
					emailContents.append("<p><strong>Reminder</strong></p>");
					emailContents.append("<table width=\"100%\" cellpadding=\"4\" cellspacing=\"1\" border=\"0\">" +
							"<tr style=\"font-weight:bold; background-color:#BBD5F2; color:#FFFFFF\">" +
								"<td>" + application.getMessage("isr.label.requestId") + "</td>" +
								"<td>" + application.getMessage("isr.label.from") + "</td>" +
								"<td>" + application.getMessage("isr.label.subject") + "</td>" +
								"<td>" + application.getMessage("isr.label.dateTimeReported") + "</td>" +
								"<td>" + application.getMessage("isr.label.requesterName") + "</td>" +
								"<td>" + application.getMessage("isr.message.dueDate") + "</td>" +								
							"</tr>");
					RequestObject requestObject;
					for(Iterator itr=reminderRequest.iterator(); itr.hasNext();) {
						requestObject = (RequestObject) itr.next();
						String overDue="";
						if(now.getTime().after(requestObject.getDueDate())){
							overDue="<span style=\"color:red\">"+dateTimeLongFormat.format(requestObject.getDueDate())+"</span>";
						}
						else{
							overDue=dateTimeLongFormat.format(requestObject.getDueDate());
						}
						emailContents.append("<tr>" +
								"<td>" + requestObject.getRequestIdRequestorUrl() + "</td>" +
								"<td>" + requestObject.getRequestFromDeptName() + "</td>" +
								"<td>" + requestObject.getRequestSubject() + "</td>" +
								"<td>" + dateTimeLongFormat.format(requestObject.getDateCreated()) + "</td>" +
								"<td>" + requestObject.getCreatedBy() + "</td>" +
								"<td>" + overDue + "</td>" +
								"</tr>");
					}
					emailContents.append("</table>");
				}
			}
			
			if(attendantNewRequest != null) {
				if(attendantNewRequest.size() > 0) {
					emailContents.append("<p><strong>New Request(s) / Assignment(s)</strong></p>");
					emailContents.append("<table width=\"100%\" cellpadding=\"4\" cellspacing=\"1\" border=\"0\">" +
							"<tr style=\"font-weight:bold; background-color:#BBD5F2; color:#FFFFFF\">" +
								"<td>" + application.getMessage("isr.label.requestId") + "</td>" +
								"<td>" + application.getMessage("isr.label.from") + "</td>" +
								"<td>" + application.getMessage("isr.label.subject") + "</td>" +
								"<td>" + application.getMessage("isr.label.dateTimeReported") + "</td>" +
								"<td>" + application.getMessage("isr.label.requesterName") + "</td>" +
							"</tr>");
					RequestObject requestObject;
					for(Iterator itr=attendantNewRequest.iterator(); itr.hasNext();) {
						requestObject = (RequestObject) itr.next();
						emailContents.append("<tr>" +
								"<td>" + requestObject.getRequestIdRequestorUrl() + "</td>" +
								"<td>" + requestObject.getRequestFromDeptName() + "</td>" +
								"<td>" + requestObject.getRequestSubject() + "</td>" +
								"<td>" + dateTimeLongFormat.format(requestObject.getDateCreated()) + "</td>" +
								"<td>" + requestObject.getCreatedBy() + "</td>" +
								"</tr>");
					}
					emailContents.append("</table>");
				}
			}
			if(attendantReopenRequest != null) {
				if(attendantReopenRequest.size() > 0) {
					emailContents.append("<p><strong>Re-opened Request(s)</strong></p>");
					emailContents.append("<table width=\"100%\" cellpadding=\"4\" cellspacing=\"1\" border=\"0\">" +
							"<tr style=\"font-weight:bold; background-color:#BBD5F2; color:#FFFFFF\">" +
								"<td>" + application.getMessage("isr.label.requestId") + "</td>" +
								"<td>" + application.getMessage("isr.label.from") + "</td>" +
								"<td>" + application.getMessage("isr.label.subject") + "</td>" +
								"<td>" + application.getMessage("isr.label.dateTimeReported") + "</td>" +
								"<td>" + application.getMessage("isr.label.requesterName") + "</td>" +
							"</tr>");
					RequestObject requestObject;
					for(Iterator itr=attendantReopenRequest.iterator(); itr.hasNext();) {
						requestObject = (RequestObject) itr.next();											
						emailContents.append("<tr>" +
								"<td>" + requestObject.getRequestIdRequestorUrl() + "</td>" +
								"<td>" + requestObject.getRequestFromDeptName() + "</td>" +
								"<td>" + requestObject.getRequestSubject() + "</td>" +
								"<td>" + dateTimeLongFormat.format(requestObject.getDateCreated()) + "</td>" +
								"<td>" + requestObject.getCreatedBy() + "</td>" +
								"</tr>");
					}
					emailContents.append("</table>");
				}
			}
			if(requesterCompletedRequest != null) {
				if(requesterCompletedRequest.size() > 0) {
					emailContents.append("<p><strong>Completed Request(s)</strong></p>");
					emailContents.append("<table width=\"100%\" cellpadding=\"4\" cellspacing=\"1\" border=\"0\">" +
							"<tr style=\"font-weight:bold; background-color:#BBD5F2; color:#FFFFFF\">" +
								"<td>" + application.getMessage("isr.label.requestId") + "</td>" +
								"<td>" + application.getMessage("isr.label.attentionTo") + "</td>" +
								"<td>" + application.getMessage("isr.label.subject") + "</td>" +
								"<td>" + application.getMessage("isr.label.dateTimeReported") + "</td>" +
								"<td>" + application.getMessage("isr.label.resolution") + "</td>" +
							"</tr>");
					RequestObject requestObject;
					for(Iterator itr=requesterCompletedRequest.iterator(); itr.hasNext();) {
						requestObject = (RequestObject) itr.next();
						emailContents.append("<tr>" +
								"<td>" + requestObject.getRequestIdRequestorUrl() + "</td>" +
								"<td>" + requestObject.getRequestToDeptName() + "</td>" +
								"<td>" + requestObject.getRequestSubject() + "</td>" +
								"<td>" + dateTimeLongFormat.format(requestObject.getDateCreated()) + "</td>" +
								"<td>" + requestObject.getRequestResolution().substring(0, (requestObject.getRequestResolution().length() > maxChar ? maxChar : requestObject.getRequestResolution().length())) + (requestObject.getRequestResolution().length() > maxChar ? "..." : "") + "</td>" +
								"</tr>");
					}
					emailContents.append("</table>");
				}
			}
			if(attendantClosedRequest != null) {
				if(attendantClosedRequest.size() > 0) {
					emailContents.append("<p><strong>Closed Request(s)</strong></p>");
					emailContents.append("<table width=\"100%\" cellpadding=\"4\" cellspacing=\"1\" border=\"0\">" +
							"<tr style=\"font-weight:bold; background-color:#BBD5F2; color:#FFFFFF\">" +
								"<td>" + application.getMessage("isr.label.requestId") + "</td>" +
								"<td>" + application.getMessage("isr.label.from") + "</td>" +
								"<td>" + application.getMessage("isr.label.subject") + "</td>" +
								"<td>" + application.getMessage("isr.label.dateTimeReported") + "</td>" +
								"<td>" + application.getMessage("isr.label.requesterName") + "</td>" +
							"</tr>");
					RequestObject requestObject;
					for(Iterator itr=attendantClosedRequest.iterator(); itr.hasNext();) {
						requestObject = (RequestObject) itr.next();
						emailContents.append("<tr>" +
								"<td>" + requestObject.getRequestIdRequestorUrl() + "</td>" +
								"<td>" + requestObject.getRequestFromDeptName() + "</td>" +
								"<td>" + requestObject.getRequestSubject() + "</td>" +
								"<td>" + dateTimeLongFormat.format(requestObject.getDateCreated()) + "</td>" +
								"<td>" + requestObject.getCreatedBy() + "</td>" +
								"</tr>");
					}
					emailContents.append("</table>");
				}
			}
			if(requesterPendingClarification != null) {
				if(requesterPendingClarification.size() > 0) {
					emailContents.append("<p><strong>Request(s) with Pending Clarification(s)</strong></p>");
					emailContents.append("<table width=\"100%\" cellpadding=\"4\" cellspacing=\"1\" border=\"0\">" +
							"<tr style=\"font-weight:bold; background-color:#BBD5F2; color:#FFFFFF\">" +
								"<td>" + application.getMessage("isr.label.requestId") + "</td>" +
								"<td>" + application.getMessage("isr.label.attentionTo") + "</td>" +
								"<td>" + application.getMessage("isr.label.subject") + "</td>" +
								"<td>" + application.getMessage("isr.label.dateTimeReported") + "</td>" +
							"</tr>");
					RequestObject requestObject;
					for(Iterator itr=requesterPendingClarification.iterator(); itr.hasNext();) {
						requestObject = (RequestObject) itr.next();
						emailContents.append("<tr>" +
								"<td>" + requestObject.getRequestIdRequestorUrl() + "</td>" +
								"<td>" + requestObject.getRequestToDeptName() + "</td>" +
								"<td>" + requestObject.getRequestSubject() + "</td>" +
								"<td>" + dateTimeLongFormat.format(requestObject.getDateCreated()) + "</td>" +
								"</tr>");
					}
					emailContents.append("</table>");
				}
			}
			if(attendantAnsweredClarification != null) {
				if(attendantAnsweredClarification.size() > 0) {
					emailContents.append("<p><strong>Request(s) with Answered Clarification(s)</strong></p>");
					emailContents.append("<table width=\"100%\" cellpadding=\"4\" cellspacing=\"1\" border=\"0\">" +
							"<tr style=\"font-weight:bold; background-color:#BBD5F2; color:#FFFFFF\">" +
								"<td>" + application.getMessage("isr.label.requestId") + "</td>" +
								"<td>" + application.getMessage("isr.label.from") + "</td>" +
								"<td>" + application.getMessage("isr.label.subject") + "</td>" +
								"<td>" + application.getMessage("isr.label.dateTimeReported") + "</td>" +
								"<td>" + application.getMessage("isr.label.requesterName") + "</td>" +
							"</tr>");
					RequestObject requestObject;
					for(Iterator itr=attendantAnsweredClarification.iterator(); itr.hasNext();) {
						requestObject = (RequestObject) itr.next();
						emailContents.append("<tr>" +
								"<td>" + requestObject.getRequestIdRequestorUrl() + "</td>" +
								"<td>" + requestObject.getRequestFromDeptName() + "</td>" +
								"<td>" + requestObject.getRequestSubject() + "</td>" +
								"<td>" + dateTimeLongFormat.format(requestObject.getDateCreated()) + "</td>" +
								"<td>" + requestObject.getCreatedBy() + "</td>" +
								"</tr>");
					}
					emailContents.append("</table>");
				}
			}
			// Send Memo
			if ("m".equals(strNotifyMethod) || "b".equals(strNotifyMethod)) {

				 MessagingModule mm;
				 SmtpAccount smtpAccount = null;
				 Message message;	
				 mm = Util.getMessagingModule();
				 String adminUserId= null;
				 
				 //get admin UserId 
				 RequestModel requestModel = (RequestModel) application.getModule(RequestModel.class);
				 Collection colAdminId = requestModel.getSystemAdminUserId();
				 if(colAdminId != null && colAdminId.size() >0){			 
					 for (Iterator i = colAdminId.iterator(); i.hasNext();) {				 
							Map tempAdd = (Map) i.next();
						try {
							adminUserId = tempAdd.get("userId").toString();
								smtpAccount = mm.getSmtpAccountByUserId(adminUserId);
								} 
						catch (MessagingException e) {
							e.printStackTrace();}
					 }
				 }else {
					 
				 }
					 		
			 // construct the message to send
			   message = new Message();
			   message.setMessageId(UuidGenerator.getInstance().getUuid());
			   
			   IntranetAccount intranetAccount = null;			   
			   List listRecepient = new ArrayList(2);
	           try {
				intranetAccount = Util.getMessagingModule().getIntranetAccountByUserId(user.getId().toString());
	           } catch (MessagingException e) {
				e.printStackTrace();
	           }	  
	           
		       if(intranetAccount != null) {
					 String add = intranetAccount.getIntranetUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
					 listRecepient.add(add);
			   }	
				  
		       if (listRecepient.size() > 0)
					message.setToIntranetList(listRecepient);
		       
		       message.setSubject(emailSubject);				 
		       message.setBody(emailContents.toString());

		       message.setMessageFormat(Message.MESSAGE_FORMAT_HTML);
		       message.setDate(new java.util.Date());
	             //send Memo
	         	try {
					mm.sendMessage(smtpAccount, message, adminUserId, false);
				} catch (MessagingException e) {
					Log.getLog(getClass()).error("Error Sending Memo ", e);
				}
			}
				
			// Send Email
			if("e".equals(strNotifyMethod) || "b".equals(strNotifyMethod)){
				try {
					MailUtil.sendEmail(smtpServer, true, adminEmail, user.getProperty("email1").toString(), emailSubject, emailContents.toString());
				}
				catch(Exception error) {
					Log.getLog(getClass()).error(error, error);
				}
			}
		}
		else {
			Log.getLog(getClass()).warn("Setup properties siteSmtpServer and siteAdminEmail are not configured.");
		}
		
		return isSuccess;
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
