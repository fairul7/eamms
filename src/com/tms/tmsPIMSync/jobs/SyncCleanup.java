package com.tms.tmsPIMSync.jobs;

import kacang.services.scheduling.BaseJob;
import kacang.services.scheduling.JobTaskExecutionContext;
import kacang.services.scheduling.SchedulingException;
import kacang.Application;
import com.tms.tmsPIMSync.model.PIMSyncModule;

public class SyncCleanup extends BaseJob
{
    public void execute(JobTaskExecutionContext context) throws SchedulingException
    {
        PIMSyncModule module = (PIMSyncModule) Application.getInstance().getModule(PIMSyncModule.class);
        if(module != null)
            module.cleanup();
    }
}
