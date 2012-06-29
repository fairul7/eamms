package com.tms.cms.core.jobs;

import com.tms.cms.core.model.ContentManager;
import kacang.Application;
import kacang.services.scheduling.BaseJob;
import kacang.services.scheduling.JobTaskExecutionContext;
import kacang.services.scheduling.SchedulingException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.util.Log;

public class ContentWithdrawJobTask extends BaseJob {

    Log log = Log.getLog(getClass());

    public void execute(JobTaskExecutionContext context) throws SchedulingException {
        String id = context.getJobTaskData().getString("id");
        String userId = context.getJobTaskData().getString("userId");
        boolean recursive = context.getJobTaskData().getBoolean("recursive");
        log.debug("=== Withdrawing " + id + " ===");

        try {
            Application application = Application.getInstance();
            SecurityService ss = (SecurityService)application.getService(SecurityService.class);
            ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
            User user = ss.getUser(userId);
            cm.withdraw(id, recursive, user);
        }
        catch (Exception e) {
            log.error("Error withdrawing " + id, e);
        }
    }

}
