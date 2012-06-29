package com.tms.cms.ad.model;

import kacang.Application;
import kacang.services.scheduling.BaseJob;
import kacang.services.scheduling.JobTaskExecutionContext;
import kacang.services.scheduling.SchedulingException;
import kacang.util.Log;

public class AdJob extends BaseJob {
    public void execute(JobTaskExecutionContext context) throws SchedulingException {
        AdModule module;
        Ad ad;
        String id;
        boolean active;

        id = context.getJobTaskData().getString("id");
        active = context.getJobTaskData().getBoolean("active");
        module = (AdModule) Application.getInstance().getModule(AdModule.class);
        try {
            ad = module.getAd(id);
            ad.setActive(active);
            module.updateAd(ad, null, false);
            module.refreshModule();

        } catch(AdException e) {
            Log.getLog(AdJob.class).error("Error getting Ad", e);
        }

    }
}
