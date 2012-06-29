package com.tms.collab.messaging.model;

import kacang.Application;
import kacang.services.scheduling.BaseJob;
import kacang.services.scheduling.JobTaskExecutionContext;
import kacang.services.scheduling.SchedulingException;
import kacang.util.Log;

/**
 * Send intranet and Smtp Message scheduling job.
 */
public class SendIntranetAndSmtpmMessagesJob extends BaseJob {

    private static final Log log = Log.getLog(SendIntranetAndSmtpmMessagesJob.class);
    
    /**
	 * @see kacang.services.scheduling.BaseJob#execute(kacang.services.scheduling.JobTaskExecutionContext)
	 */
	public void execute(JobTaskExecutionContext context) throws SchedulingException {
        try {
        log.debug("execute SendIntranetAndSmtpMessagesJob");
		MessagingQueue queue = MessagingQueue.getInstance();
        MessagingModule module = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);

        // check for thread limit
        if (queue.getSendSmtpQueueCurrent() >= module.getSendMailJobCount()) {
            log.debug("Send Intranet SMTP skipped due to thread limit");
            return;
        }
        try {
        	queue.setSendSmtpQueueCurrent(queue.getSendSmtpQueueCurrent() + 1);
        	module.sendIntranetAndSmtpMessagesNow();
        }
        finally {
        	queue.setSendSmtpQueueCurrent(queue.getSendSmtpQueueCurrent() - 1);
        }
        }
        catch(Exception e) {
           log.error(e.getMessage(), e);
        }
	}
}


