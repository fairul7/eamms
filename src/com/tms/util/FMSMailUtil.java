package com.tms.util;

import kacang.util.Mailer;
import kacang.util.Log;
import kacang.Application;
import com.tms.ekms.setup.model.SetupModule;

import java.util.Map;
import java.util.HashMap;

/**
 * Convenience class for sending emails via SMTP.
 * This implementation utilizes {@link kacang.util.Mailer} and
 * makes use of properties defined in the Setup Module.
 */
public class FMSMailUtil {

    public static final String SETUP_PROPERTY_SMTP_SERVER = "smtp.server";
    public static final String SETUP_PROPERTY_EMAIL_SENDER = "admin.email";

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
            Log.getLog(FMSMailUtil.class).error("Error retrieving setup properties: " + e.toString());
        }
        if (smtpServer == null || smtpServer.trim().length() == 0) {
            // load from setup module
            //smtpServer = (String)propertyMap.get(SETUP_PROPERTY_SMTP_SERVER);
        	smtpServer = Application.getInstance().getProperty("smtp.server");
			
        }
        if (fromEmail == null || fromEmail.trim().length() == 0) {
            // load from setup module
           // fromEmail = (String)propertyMap.get(SETUP_PROPERTY_EMAIL_SENDER);
        	fromEmail = Application.getInstance().getProperty("admin.email");
        }
        if(Mailer.sendEmail(smtpServer, isHtml, fromEmail, toEmail, ccEmail, bccEmail, subject, content)){
        	//System.out.println("EMAIL SUCCESS SENT TO "+toEmail+" FROM "+fromEmail);
        	return true;
        }else{
        	return false;
        }
    }

}


