package com.tms.hr.recruit.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.storage.StorageException;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.collab.messaging.model.Folder;
import com.tms.collab.messaging.model.IntranetAccount;
import com.tms.collab.messaging.model.Message;
import com.tms.collab.messaging.model.MessagingDao;
import com.tms.collab.messaging.model.MessagingDaoException;
import com.tms.collab.messaging.model.MessagingException;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.SmtpAccount;
import com.tms.collab.messaging.model.Util;
import com.tms.ekms.setup.model.SetupModule;
import com.tms.util.MailUtil;

public class SendMessage {
	public void sendMessageToApplicant(List ToList, String subjectTitle, String BodyTitle, String hodId, String FROM){
		Log log = Log.getLog(getClass());
		try {
			Application app = Application.getInstance();
            MessagingModule mm;
            SmtpAccount smtpAccount;
            Message message;
            User user;
            mm = Util.getMessagingModule();
            user = app.getCurrentUser();
            
            //construct the message to send
            message = new Message();
            
            //send message id
            message.setMessageId(UuidGenerator.getInstance().getUuid());
            
            smtpAccount = mm.getSmtpAccountByUserId(hodId);
           
            IntranetAccount intranetAccount;
            intranetAccount = Util.getMessagingModule().getIntranetAccountByUserId(hodId);
            
            String fromSender="";
            if(!FROM.equals("")){
            	fromSender=FROM;
            }else{
            	fromSender = intranetAccount.getIntranetUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
            }
            //set mail from
            message.setFrom(fromSender);
 
            //set to applicant
            message.setToList(ToList);
    
            String subject="";
            if(!subjectTitle.equals("")){
            	subject=subjectTitle;
            }	
            
            //set subject
            message.setSubject(subject);
            
            String content="";
            if(!BodyTitle.equals("")){
            	content = BodyTitle;
            }
            
            //send body
            message.setBody(content);
            
            //set message format
            message.setMessageFormat(Message.MESSAGE_FORMAT_HTML);
            
            //set date
            message.setDate(new Date());
            
            //processing and send mail
            mm.sendMessage(smtpAccount, message, hodId, false);
            
		}catch (Exception e) {
			log.error("Error sending notification to applicant: ", e);
	    }
	}
	
	public void sendMessage(List ToList, String subjectTitle, String BodyTitle, String FROM){
		Log log = Log.getLog(getClass());
		try {
            Application app = Application.getInstance();
            MessagingModule mm;
            SmtpAccount smtpAccount;
            Message message;
            User user;
            mm = Util.getMessagingModule();
            user = app.getCurrentUser();
            
            //construct the message to send
            message = new Message();
            
            //send message id
            message.setMessageId(UuidGenerator.getInstance().getUuid());
            
            IntranetAccount intranetAccount;
            intranetAccount = Util.getMessagingModule().getIntranetAccountByUserId(user.getId());
            String fromSender="";
            if(!FROM.equals("")){
            	String hodId = (String)ToList.iterator().next();
                smtpAccount = mm.getSmtpAccountByUserId(hodId);
            	fromSender=FROM;
            }else{
            	//set the smtp account using current user id
                smtpAccount = mm.getSmtpAccountByUserId(user.getId());
            	fromSender = intranetAccount.getIntranetUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
            }
            //set mail from
            message.setFrom(fromSender);
            
            List toList=new ArrayList(ToList.size());
            for(Iterator ite=ToList.iterator(); ite.hasNext();){
            	intranetAccount = Util.getMessagingModule().getIntranetAccountByUserId(ite.next().toString()); 
            	 if(intranetAccount != null) {
            		 String intranetUserEmail = intranetAccount.getIntranetUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
            		 toList.add(intranetUserEmail);
            	 }
            }
            
            //set intranet users
            message.setToIntranetList(toList);
            String subject="";
            if(!subjectTitle.equals("")){
            	//subject="<b><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\" color=\"#000000\">"+subjectTitle+"</font></b>";
            	subject=subjectTitle;
            }	
            //set subject
            message.setSubject(subject);
            
            String content="";
            if(!BodyTitle.equals("")){
            	content = BodyTitle;
            }
            //send body
            message.setBody(content);
            
            //set message format
            message.setMessageFormat(Message.MESSAGE_FORMAT_HTML);
            
            //set date
            message.setDate(new Date());
            
            //processing and send mail
            mm.sendMessage(smtpAccount, message, user.getId(), false);
        }
        catch (Exception e) {
            log.error("Error sending notification: ", e);
        }
	}
	
	//	send mail to interviewer-Only if 'Scheduled & Interviewer(s) added' ***
	//	blacklisted/deleted applicant mail setting-single applicantId#1
	public void sendBlackListedMailSetting(String applicantId, String statusType){
		Application app = Application.getInstance();
		RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
		Collection lookUpStatusCol = ram.loadApplicantPersonal(applicantId);
		 HashMap statusMap = (HashMap)lookUpStatusCol.iterator().next();
		 if(statusMap.get("applicantStatus").equals("Scheduled & Interviewer(s) added")){
			 List toList = new ArrayList();
			 String[] appId = {statusMap.get("applicantId").toString()};
			 Collection getInterviewDateIdCol = ram.findInterviewDateWithStatus(appId);
			 for(Iterator iter=getInterviewDateIdCol.iterator(); iter.hasNext();){
				 HashMap map = (HashMap)iter.next();
				 Collection getInterviewerCol = ram.lookUpInterviewer(map.get("interviewDateId").toString());
				 for(Iterator itera = getInterviewerCol.iterator(); itera.hasNext();){
					 HashMap InterviewerIdMap = (HashMap)itera.next();
					 toList.add(InterviewerIdMap.get("interviewerId"));
				 }
			 }
			 sendBlackListedMail(toList, statusMap.get("name").toString(), statusType);
		 }
	}
	
	//blacklisted/deleted applicant send content-single applicantId#2
	public void sendBlackListedMail(List toList, String applicantName, String statusType){
		 //send mail to notif the interviewer(s) tat applicant had been blacklisted and all data is deleted
		 Application app = Application.getInstance();
		 SendMessage sm = new SendMessage();
		 String subjectTitle=app.getMessage("recruit.general.label.scheduledApplicant", "Scheduled Applicant(s)") + " for Interview Session have "+ statusType;
		 String title = "Below are the "+statusType+" applicant(s) for the Interview session";
		 StringBuffer sb= new StringBuffer();
		 sb.append("<table cellspacing=0 cellpadding=5 width=\"100%\" border=0>\n");
		 sb.append("  <tr valign=center> \n");
		 sb.append("    <td bgcolor=\"#FFFFFF\" colspan=\"1\"><b><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\" color=\"#000000\">" + title + "</font></b></td>\n");
		 sb.append("  </tr>\n");

		 sb.append("  <tr> \n");
		 sb.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">"+applicantName+"</font></td>\n");
		 sb.append("  </tr>\n");
	 
		 sb.append("</table>");
		 sm.sendMessage(toList, subjectTitle, sb.toString(), ""); 
	}
}











