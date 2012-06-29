package com.tms.collab.messaging.model;

import kacang.Application;
import kacang.services.scheduling.BaseJob;
import kacang.services.scheduling.JobTaskExecutionContext;
import kacang.services.scheduling.SchedulingException;
import kacang.util.Log;
import com.tms.collab.messaging.model.queue.QueueItem;

public class CheckPop3Job extends BaseJob {

    public void execute(JobTaskExecutionContext jobTaskExecutionContext) throws SchedulingException {
        MessagingQueue queue;
        QueueItem item;
        MessagingModule module;

        queue = MessagingQueue.getInstance();
        module = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);

        if (queue.getDownloadPop3Size() > 0 || queue.getDownloadPop3Current() > 0) {
            // if download POP3 is busy, prioritize it, don't process check POP3
            Log.getLog(CheckPop3Job.class).debug("Check POP3 skipped to prioritize download POP3");
            return;
        }

        // check for thread limit
        if (queue.getCheckPop3Current() >= module.getCheckPop3JobCount()) {
            Log.getLog(CheckPop3Job.class).debug("Check POP3 skipped due to thread limit");
            return;
        }

        item = queue.popCheckPop3();
        while (item != null) {
            try {
                queue.setCheckPop3Current(queue.getCheckPop3Current() + 1);

                // check emails
                try {
                    module.checkPop3MessagesNow(item.getUserId());
                } catch (Throwable e) {
                    Log.getLog(CheckPop3Job.class).debug("Error checking POP3 emails", e);
                }
            } finally {
                queue.setCheckPop3Current(queue.getCheckPop3Current() - 1);
            }

            item = queue.popCheckPop3();
        }
    }
}
