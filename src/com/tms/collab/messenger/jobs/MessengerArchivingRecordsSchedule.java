package com.tms.collab.messenger.jobs;

import kacang.Application;
import kacang.services.scheduling.BaseJob;
import kacang.services.scheduling.JobTaskExecutionContext;
import kacang.services.scheduling.SchedulingException;

import com.tms.collab.messenger.MessageModule;

public class MessengerArchivingRecordsSchedule extends BaseJob
{
	 public void execute(JobTaskExecutionContext jobTaskExecutionContext) throws SchedulingException
	    {
			 MessageModule module = (MessageModule)Application.getInstance().getModule(MessageModule.class);
			 module.archiveMessages();
	    } 
}
