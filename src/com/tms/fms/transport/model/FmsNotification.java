package com.tms.fms.transport.model;

import java.util.*;

import kacang.Application;
import kacang.model.DefaultDataObject;
import kacang.util.Log;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class FmsNotification extends DefaultDataObject {
		
	public void send(ArrayList emailList, String subject, String body) {
		String[] emailTo = new String[emailList.size()];
		
		for (int i=0; i<emailList.size(); i++) {
			emailTo[i] = (String) emailList.get(i);
		}
		
		send(emailTo, subject, body);
	}
	
	public void send(String[] emailTo, String subject, String body){
    		try {
			
				String smtpServer = null;
				String adminEmail = null;
				
				try {
    				
    				smtpServer = Application.getInstance().getProperty("smtp.server");
    				adminEmail = Application.getInstance().getProperty("admin.email");							
    			}
    			catch(Exception error) {
    			}
				
				if(!((null == smtpServer || "".equals(smtpServer)) 
						|| (null == adminEmail || "".equals(adminEmail)))){
				  HtmlEmail email = new HtmlEmail ();
				  
				  email.setHostName(smtpServer);				 
				  email.setFrom(adminEmail);
				  email.setSubject(subject);
				  email.setHtmlMsg(body);
				  
				  if (emailTo.length>0) {
					  Map emailMap = new HashMap();
					  for(int i=0; i < emailTo.length; i++){
						  String[] ext = emailTo[i].split(",");		
					      
					      for (int em = 0; em < ext.length; em++) {
					    	  String emailAddr = ext[em];
					    	  
					    	  if (!emailMap.containsKey(emailAddr)) { // check for duplicate email
					    		  emailMap.put(emailAddr, "");
					    		  email.addTo(emailAddr, "FMS User");
					    		  Log.getLog(getClass()).info("Sending notification to: " + emailAddr);
					    	  }
					      }
					  }
					  email.send();	
					  Log.getLog(getClass()).info("Success sent out notification: subject=" + subject);
				  } else {
					  Log.getLog(getClass()).warn("Email not sent, emailTo is blank: subject=" + subject);
				  }
				}
				  
			} catch (EmailException e) {
				Log.getLog(getClass()).error("Cannot send out notification: subject=" + subject, e);
			}
    				
    	}
}
