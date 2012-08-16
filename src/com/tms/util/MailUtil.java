package com.tms.util;

import kacang.model.DefaultDataObject;
import kacang.util.Mailer;
import kacang.util.Log;
import kacang.Application;
import com.tms.ekms.setup.model.SetupModule;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Convenience class for sending emails via SMTP.
 * This implementation utilizes {@link kacang.util.Mailer} and
 * makes use of properties defined in the Setup Module.
 */
public class MailUtil {

    public static final String SETUP_PROPERTY_SMTP_SERVER = "siteSmtpServer";
    public static final String SETUP_PROPERTY_EMAIL_SENDER = "siteAdminEmail";

    /**
     * Sends email in the background, making use of the scheduling service.
     * @param smtpServer
     * @param isHtml
     * @param fromEmail
     * @param toEmail TO comma-delimited email addresses
     * @param subject
     * @param content
     * @return
     */
    public static boolean sendEmail(String smtpServer, boolean isHtml, String fromEmail, String toEmail, String subject, String content) {
        return sendEmail(smtpServer, isHtml, fromEmail, toEmail, null, null, subject, content);
    }

    /**
     * Sends email (with fields for CC and BCC) in the background, making use of the scheduling service.
     * @param smtpServer
     * @param isHtml
     * @param fromEmail
     * @param toEmail TO comma-delimited email addresses
     * @param ccEmail CC comma-delimited email addresses
     * @param bccEmail BCC comma-delimited email addresses
     * @param subject
     * @param content
     * @return
     */
    public static boolean sendEmail(String smtpServer, boolean isHtml, String fromEmail, String toEmail, String ccEmail, String bccEmail, String subject, String content) {

        // lookup default values for smtpServer and senderEmail
        Application application = Application.getInstance();
        Map propertyMap = new HashMap();
        try {
            SetupModule setup = (SetupModule)application.getModule(SetupModule.class);
            propertyMap = setup.getAll();
        } catch (Exception e) {
            Log.getLog(com.tms.util.MailUtil.class).error("Error retrieving setup properties: " + e.toString());
        }
        if (smtpServer == null || smtpServer.trim().length() == 0) {
            // load from setup module
            smtpServer = (String)propertyMap.get(SETUP_PROPERTY_SMTP_SERVER);
        }
        if (fromEmail == null || fromEmail.trim().length() == 0) {
            // load from setup module
            fromEmail = (String)propertyMap.get(SETUP_PROPERTY_EMAIL_SENDER);
        }
        return Mailer.sendEmail(smtpServer, isHtml, fromEmail, toEmail, ccEmail, bccEmail, subject, content);
    }

    public static boolean sendEmailNow(String smtpServer, String fromEmail, String toEmail, 
    		String ccEmail, String bccEmail, String subject, String content, ArrayList<DefaultDataObject> attachment) 
    {
    	long startTime = System.currentTimeMillis();
        Session session = null;
        Properties props = null;
        MimeMessage message = null;
        Address aEmailTo[] = null;

        try 
        {
            Log.getLog(Mailer.class).debug("Send email is running");

            // default values for smtpServer and senderEmail?
            Application application = Application.getInstance();
            if (smtpServer == null || smtpServer.trim().length() == 0) 
            {
                // read from application property
                smtpServer = application.getProperty(Mailer.PROPERTY_SMTP_SERVER);
            }
            if (fromEmail == null || fromEmail.trim().length() == 0) 
            {
                // read from application property
                fromEmail = application.getProperty(Mailer.PROPERTY_EMAIL_SENDER);
            }

            if (session == null) 
            {
            	// Get system properties
                props = System.getProperties();
                
                // Setup mail server
                props.put("mail.smtp.host", smtpServer);
                String port = application.getProperty(Mailer.PROPERTY_SMTP_PORT);
                if(port != null && !port.equals(""))
                {
                    props.put("mail.smtp.port", port);
                }
                // Get the default Session object.
                session = Session.getInstance(props, null);
            }
    	
            // create new MIME message
            message = new MimeMessage(session);
            
	        try
			{
	        	// set sender email
	            if (fromEmail != null)
	            {
	                message.setFrom(new InternetAddress(fromEmail));
	            }
	            else
	            {
	                message.setFrom();
	            }
	        	
	        	// set to recipients
	            if (toEmail != null) 
	            {
	                aEmailTo = InternetAddress.parse(toEmail);
	                message.setRecipients(Message.RecipientType.TO, aEmailTo);
	            }

	            // set cc recipients
	            if (ccEmail != null) 
	            {
	                aEmailTo = InternetAddress.parse(ccEmail);
	                message.setRecipients(Message.RecipientType.CC, aEmailTo);
	            }

	            // set bcc recipients
	            if (bccEmail != null) 
	            {
	                aEmailTo = InternetAddress.parse(bccEmail);
	                message.setRecipients(Message.RecipientType.BCC, aEmailTo);
	            }
	            
			    message.setSubject(subject, "UTF8");
			
			    {
			    	Multipart multipart = new MimeMultipart();
			    	BodyPart messageBodyPart = null;
			    	
			    	// Text part
				    messageBodyPart = new MimeBodyPart();
				    messageBodyPart.setContent(content, "text/html; charset=\"utf-8\"");
				    multipart.addBodyPart(messageBodyPart);
				
				    // Attachment part
				    if(attachment != null && !attachment.isEmpty())
				    {
				    	for(DefaultDataObject atchm : attachment)
				    	{
				    		String fileName = (String) atchm.getProperty("fileName");
					    	byte[] bytes = (byte[]) atchm.getProperty("attch");
					    	String type = (String) atchm.getProperty("type");
					    	
					    	messageBodyPart = new MimeBodyPart();
					    	messageBodyPart.setDataHandler(new DataHandler(bytes, type));
					    	messageBodyPart.setFileName(fileName + "." + type.substring(type.length() - 3, type.length()));
					    	multipart.addBodyPart(messageBodyPart);
				    	}
				    }
			
			        // Put parts in message
				    message.setContent(multipart);
			    }
			
			    // set the Date: header
			    message.setSentDate(new java.util.Date());
	
		        // log debug message
		        Log.getLog(com.tms.util.MailUtil.class).debug("Sending email message [" + subject + "] sent to [" + toEmail + "]");
			        
		        // Send the message
			    Transport.send(message);
			    
			    return true;
			}
			catch (AddressException e)
			{
				Log.getLog(com.tms.util.MailUtil.class).error(e);
			}
			catch (MessagingException e)
			{
				Log.getLog(com.tms.util.MailUtil.class).error(e);
			}
			
			return false;
        } 
        catch (Exception e) 
        {
            Log.getLog(com.tms.util.MailUtil.class).error("Send email completed with error in " + (System.currentTimeMillis() - startTime) + "ms", e);
            return false;
        }
    }

}


