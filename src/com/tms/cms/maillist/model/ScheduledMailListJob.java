package com.tms.cms.maillist.model;

import kacang.Application;
import kacang.services.scheduling.BaseJob;
import kacang.services.scheduling.JobTaskExecutionContext;
import kacang.services.scheduling.SchedulingException;
import kacang.util.Log;

public class ScheduledMailListJob extends BaseJob {
    public void execute(JobTaskExecutionContext context) throws SchedulingException {
        MailListModule module;
        String id;
        ScheduledMailList ml;

        id = context.getJobTaskData().getString("id");
        module = (MailListModule) Application.getInstance().getModule(MailListModule.class);
        try {
            ml = (ScheduledMailList) module.getMailList(id);
            module.sendMailingList(ml);

        } catch(MailListException e) {
            Log.getLog(ScheduledMailListJob.class).error("Error getting scheduled mailing list", e);
        }

    }
}
