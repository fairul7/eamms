package com.tms.collab.messaging.model;

import kacang.Application;
import kacang.services.scheduling.BaseJob;
import kacang.services.scheduling.JobTaskExecutionContext;
import kacang.services.scheduling.SchedulingException;
import kacang.util.Log;

import java.util.List;
import java.util.Date;
import java.util.Map;

import com.tms.collab.messaging.model.queue.ServerViewQueueItem;

public class ServerViewJob extends BaseJob {

    public void execute(JobTaskExecutionContext jobTaskExecutionContext) throws SchedulingException {
    	// do nothing
    	/*
        MessagingQueue queue;
        MessagingModule module;
        ServerViewQueueItem item;

        try {

            queue = MessagingQueue.getInstance();
            module = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);

            // check for thread limit
            if(queue.getServerViewCurrent() >= module.getServerViewJobCount()) {
                Log.getLog(ServerViewJob.class).debug("Server view skipped due to thread limit");
                return;
            }

            item = queue.popServerView();
            while(item!=null) {
                doCheck(module, item);
                item = queue.popServerView();
            }

        } catch(Exception e) {
            Log.getLog(ServerViewJob.class).error("Error running server view job", e);
        }
        */
    }

    private void doCheck(MessagingModule module, ServerViewQueueItem item) {
        Map tempMap;
        MessagingUserStatus status;
        MessagingQueue queue;

        try {
            queue = MessagingQueue.getInstance();
            status = queue.getUserStatus(item.getUserId());

            tempMap = module.downloadPop3MessageSummaryListNow(item.getUserId(), item.isPreview(), item.getAccountIdList());
            status.setDataMap(tempMap);
            status.setServerViewUpdateTime(new Date());

        } catch (MessagingException e) {
            Log.getLog(ServerViewJob.class).error("Error checking server view", e);

            try {
                MessagingUserStatus mus = MessagingQueue.getInstance().getUserStatus(item.getUserId());
                if(!mus.isServerViewBusy()) {
                    mus.getTrackerServerView().update(ProgressTracker.STATUS_ERROR, 0, "Error checking server view: " + e.getMessage());
                }

            } catch (MessagingException e1) {
                // cannot get account, too bad
                Log.getLog(ServerViewJob.class).error("Error checking server view", e);
            }
        }

    }
}
