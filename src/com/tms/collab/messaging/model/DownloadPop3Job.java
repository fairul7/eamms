package com.tms.collab.messaging.model;

import kacang.Application;
import kacang.services.scheduling.BaseJob;
import kacang.services.scheduling.JobTaskExecutionContext;
import kacang.services.scheduling.SchedulingException;
import kacang.util.Log;
import com.tms.collab.messaging.model.queue.QueueItem;

public class DownloadPop3Job extends BaseJob {
    
    private static final Log log = Log.getLog(DownloadPop3Job.class);
    
    public void execute(JobTaskExecutionContext jobTaskExecutionContext) throws SchedulingException {
        
        log.debug("execute DownloadPop3Job");
        
        MessagingQueue queue;
        QueueItem item;
        MessagingModule module;

        queue = MessagingQueue.getInstance();
        module = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);

        // check for thread limit
        if (queue.getDownloadPop3Current() >= module.getDownloadPop3JobCount()) {
            Log.getLog(DownloadPop3Job.class).debug("Download POP3 skipped due to thread limit");
            return;
        }

        item = queue.popDownloadPop3();
        while (item != null) {
            try {
                queue.setDownloadPop3Current(queue.getDownloadPop3Current() + 1);

                // download emails
                try {
                    Log.getLog(DownloadPop3Job.class).info("DownloadPop3Job: Begining download for user " + item.getUserId());
                    module.downloadPop3MessagesNow(item.getUserId());
                } catch (Throwable e) {
                    Log.getLog(DownloadPop3Job.class).error("Error downloading emails", e);
                }
            } finally {
                queue.setDownloadPop3Current(queue.getDownloadPop3Current() - 1);
            }

            item = queue.popDownloadPop3();
        }
    }
}
