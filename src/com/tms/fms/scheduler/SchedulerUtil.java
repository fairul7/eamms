package com.tms.fms.scheduler;

import kacang.services.scheduling.JobTask;

import com.tms.fms.util.JobUtil;

public class SchedulerUtil {
	
	public static void scheduleDailyTask(String taskName, String taskGroup, String taskDesc, JobTask jobTask, int hour,int min ){
		JobUtil.removeTask(taskName, taskGroup, jobTask);
		JobUtil.scheduleDailyTask(taskName, taskGroup, taskDesc, jobTask, hour, min);
	}

}
