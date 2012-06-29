package com.tms.fms.util;

import kacang.Application;
import kacang.services.scheduling.*;
import kacang.util.*;
import com.tms.crm.sales.misc.DateUtil;
import java.text.SimpleDateFormat;
import java.util.*;

public class JobUtil {
	public static void scheduleDefinitelyDailyTask(String taskName, String taskGroup, String taskDesc,
			JobTask jobTask, int repeatCount,int hour, int min) {

		// Note: made schedule name the same with task name (because of bug in System Administration > Scheduled Tasks)
		String schedName = taskName;
		
		// task
		JobSchedule jobSched = new JobSchedule(schedName, JobSchedule.DAILY);
		jobSched.setHour(hour);
		jobSched.setMinute(min);
		jobSched.setGroup(taskGroup);
		jobSched.setRepeatCount(repeatCount);
		jobSched.setRepeatInterval(1);
		
		jobTask.setName(taskName);
		jobTask.setGroup(taskGroup);
		jobTask.setDescription(taskDesc);
		
		// display
		String dispTime = timeDisp(hour, min);
		
		// scheduling
		try {
		SchedulingService scheduler = (SchedulingService) Application.getInstance().getService(SchedulingService.class);
		Log.getLog(JobUtil.class).info("Creating task '" + jobTask.getName() + "' (running daily at " + dispTime + ")");
		scheduler.deleteJobTask(jobTask);
		scheduler.scheduleJob(jobTask, jobSched);
		
		} catch (SchedulingException e) {
		Log.getLog(JobUtil.class).error(e.toString(), e);
		}
}
	
	public static void scheduleDailyTask(String taskName, String taskGroup, String taskDesc,
								JobTask jobTask, int hour, int min) {
		
		// Note: made schedule name the same with task name (because of bug in System Administration > Scheduled Tasks)
		String schedName = taskName;
		
		// task
		JobSchedule jobSched = new JobSchedule(schedName, JobSchedule.DAILY);
		jobSched.setHour(hour);
		jobSched.setMinute(min);
		jobSched.setGroup(taskGroup);
		jobSched.setRepeatCount(JobSchedule.REPEAT_INDEFINITELY);
		jobSched.setRepeatInterval(1);
		
		jobTask.setName(taskName);
		jobTask.setGroup(taskGroup);
		jobTask.setDescription(taskDesc);
		
		// display
		String dispTime = timeDisp(hour, min);
		
		// scheduling
		try {
			SchedulingService scheduler = (SchedulingService) Application.getInstance().getService(SchedulingService.class);
			Log.getLog(JobUtil.class).info("Creating task '" + jobTask.getName() + "' (running daily at " + dispTime + ")");
			scheduler.deleteJobTask(jobTask);
			scheduler.scheduleJob(jobTask, jobSched);
			
		} catch (SchedulingException e) {
			Log.getLog(JobUtil.class).error(e.toString(), e);
		}
	}
	
	public static void scheduleMinuteTask(String taskName, String taskGroup, String taskDesc,
								JobTask jobTask, int numMinutes) {
		
		// Note: made schedule name the same with task name (because of bug in System Administration > Scheduled Tasks)
		String schedName = taskName;
		
		// task
		JobSchedule jobSched = new JobSchedule(schedName, JobSchedule.MINUTELY);
		jobSched.setGroup(taskGroup);
		jobSched.setRepeatCount(JobSchedule.REPEAT_INDEFINITELY);
		jobSched.setRepeatInterval(numMinutes);
		
		jobTask.setName(taskName);
		jobTask.setGroup(taskGroup);
		jobTask.setDescription(taskDesc);
		
		// scheduling
		try {
			SchedulingService scheduler = (SchedulingService) Application.getInstance().getService(SchedulingService.class);
			Log.getLog(JobUtil.class).info("Creating task '" + jobTask.getName() + "' (running every " + numMinutes + " minute(s))");
			scheduler.deleteJobTask(jobTask);
			scheduler.scheduleJob(jobTask, jobSched);
			
		} catch (SchedulingException e) {
			Log.getLog(JobUtil.class).error(e.toString(), e);
		}
	}
	
	public static void removeTask(String taskName, String taskGroup, JobTask jobTask) {
		
		// task
		jobTask.setName(taskName);
		jobTask.setGroup(taskGroup);
		
		// scheduling
		try {
			SchedulingService scheduler = (SchedulingService) Application.getInstance().getService(SchedulingService.class);
			Log.getLog(JobUtil.class).info("Removing task " + jobTask.getName());
			scheduler.deleteJobTask(jobTask);
			
		} catch (SchedulingException e) {
			Log.getLog(JobUtil.class).error(e.toString(), e);
		}
	}
	
	private static String timeDisp(int hour, int min) {
		Date dispDate = DateUtil.getDateOnly(new Date());
		dispDate = DateUtil.dateAdd(dispDate, Calendar.HOUR_OF_DAY, hour);
		dispDate = DateUtil.dateAdd(dispDate, Calendar.MINUTE, min);
		SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
		return timeFormat.format(dispDate);
	}
}