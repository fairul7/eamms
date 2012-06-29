package com.tms.collab.messaging.model;

import kacang.Application;
import kacang.services.scheduling.BaseJob;
import kacang.services.scheduling.JobTaskExecutionContext;
import kacang.services.scheduling.SchedulingException;
import kacang.util.Log;

import javax.mail.internet.MimeMessage;
import javax.mail.*;
import java.io.PrintWriter;
import java.io.StringWriter;

public class SendSmtpJob extends BaseJob {
    public void execute(JobTaskExecutionContext jobTaskExecutionContext) throws SchedulingException {
        MessagingQueue queue;
        MessagingModule module;
        Object[] objArr;
        SmtpAccount smtpAccount;
        MimeMessage mimeMessage;

        queue = MessagingQueue.getInstance();
        module = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);

        // check for thread limit
        if (queue.getSendSmtpQueueCurrent() >= module.getSendMailJobCount()) {
            Log.getLog(SendSmtpJob.class).debug("Send SMTP skipped due to thread limit");
            return;
        }

        objArr = queue.popSendSmtp();
        while (objArr != null) {
            try {
                queue.setSendSmtpQueueCurrent(queue.getSendSmtpQueueCurrent() + 1);

                smtpAccount = (SmtpAccount) objArr[0];
                mimeMessage = (MimeMessage) objArr[1];

                if (smtpAccount != null && smtpAccount.getUserId() == null) {
                    // send notification email, not belonging to any user
                    try {
                        module.sendSmtpMessageNow(smtpAccount, mimeMessage);
                    } catch (Exception e) {
                        Log.getLog(getClass()).error("Error sending SMTP email", e);
                    }
                } else {

                    try {
                        MessagingUserStatus mus;
                        mus = queue.getUserStatus(smtpAccount.getUserId());

                        // send email
                        mus.getTrackerSend().update(ProgressTracker.STATUS_PROCESSING, 0, "Sending email \"" + mimeMessage.getSubject() + "\"");
                        module.sendSmtpMessageNow(smtpAccount, mimeMessage);
                        mus.getTrackerSend().update(ProgressTracker.STATUS_COMPLETED, 100, "Email \"" + mimeMessage.getSubject() + "\" sent");

                    } catch (Exception e) {
                        MessagingUserStatus mus;
                        try {
                            mus = queue.getUserStatus(smtpAccount.getUserId());
                            mus.getTrackerSend().update(ProgressTracker.STATUS_ERROR, 100, "Error sending email: " + e.getMessage());
                        } catch (MessagingException e2) {
                            Log.getLog(getClass()).error("Error updating send tracker", e2);
                        }

                        try {
                            queue.getUserStatus(smtpAccount.getUserId()).getTrackerSend().update(ProgressTracker.STATUS_ERROR, 0, "Error sending email \"" + mimeMessage.getSubject() + "\"");
                            Log.getLog(SendSmtpJob.class).error("Error sending Internet email: " + mimeMessage.getSubject(), e);

                            String subject = "Messaging Error: Error sending Internet email";
                            String title = subject;
                            StringBuffer sb = new StringBuffer();

                            StringWriter sw = new StringWriter();
                            e.printStackTrace(new PrintWriter(sw));

                            sb.append("An error occurred while sending your message with the subject:<br><br><b>");
                            sb.append(mimeMessage.getSubject());
                            sb.append("</b><br><br>Exception message: <b>" + e.getMessage() + "</b><br><br>");
                            sb.append("<pre>" + sw.toString() + "</pre>");
                            sb.append("<br><br><hr>This is a system generated message. DO NOT REPLY.");
                            String content = sb.toString();

                            IntranetAccount ia = module.getIntranetAccountByUserId(smtpAccount.getUserId());
                            String to = ia.getIntranetUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;

                            module.sendStandardHtmlEmail(smtpAccount.getUserId(), to, "", "", subject, title, content);
                        } catch (MessagingException ee) {
                            Log.getLog(getClass()).fatal("Error reporting failure to send SMTP email", e);
                        } catch (javax.mail.MessagingException ee) {
                            Log.getLog(getClass()).fatal("Error reporting failure to send SMTP email", e);
                        }
                    }
                }

            } finally {
                queue.setSendSmtpQueueCurrent(queue.getSendSmtpQueueCurrent() - 1);
            }

            objArr = queue.popSendSmtp();
        }
    }
}
