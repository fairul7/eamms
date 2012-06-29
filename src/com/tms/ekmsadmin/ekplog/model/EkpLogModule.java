package com.tms.ekmsadmin.ekplog.model;

import java.util.Date;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DefaultModule;
import kacang.services.scheduling.JobSchedule;
import kacang.services.scheduling.JobTask;
import kacang.services.scheduling.SchedulingException;
import kacang.services.scheduling.SchedulingService;
import kacang.ui.Event;
import kacang.util.Log;

public class EkpLogModule extends DefaultModule {
	private static final String EKP_LOG = "ekp_log";
	private static final String ZIP_STORAGE_HOUSE_KEEPING = "Ekp Log Zip Storage House Keeping";
	private static final String ZIP_STORAGE_HOUSE_KEEPING_DESC = "Remove the downloaded compressed files of ekp logs";
	
	public void init() {
		JobSchedule schedule = new JobSchedule(getClass().getName(), JobSchedule.DAILY);
		schedule.setRepeatCount(JobSchedule.REPEAT_INDEFINITELY);
        schedule.setGroup(EKP_LOG);

        JobTask task = new EkpLogZipRemovalJob();
        task.setName(ZIP_STORAGE_HOUSE_KEEPING);
        task.setGroup(EKP_LOG);
        task.setDescription(ZIP_STORAGE_HOUSE_KEEPING_DESC);

        SchedulingService service = (SchedulingService) Application.getInstance().getService(SchedulingService.class);

        try {
        	Log.getLog(getClass()).debug("Delete existing job and re-schedule a new one.");
            service.deleteJobTask(task);
            service.scheduleJob(task, schedule);
        } 
        catch (SchedulingException e) {
            Log.getLog(getClass()).error("Error removing job: " + task.getName(), e);
        }
	}
	
	public EkpLogResult getLogs(Date lastUpdatedDateBegin, Date lastUpdatedDateEnd, String sort, boolean desc, int start, int rows) {
		EkpLogDao dao = (EkpLogDao) getDao();
		EkpLogResult logResult = new EkpLogResult();
        
        try {
        	logResult = dao.getLogs(lastUpdatedDateBegin, lastUpdatedDateEnd, sort, desc, start, rows);
        }
        catch(Exception error) {
            Log.getLog(getClass()).error(error, error);
        }
        
        return logResult;
	}
	
	public boolean downloadLogs(Event evt, String[] selectedKeys) {
		EkpLogDao dao = (EkpLogDao) getDao();
		boolean isSuccess = true;
		
		try {
			dao.downloadLogs(evt, selectedKeys);
		}
		catch(DaoException error) {
			isSuccess = false;
			Log.getLog(getClass()).error(error, error);
		}
		
		return isSuccess;
	}
	
	public String getTomcatLogsRealPath() {
		EkpLogDao dao = (EkpLogDao) getDao();
		
		return dao.getTomcatLogsRealPath();
	}
	
	public boolean isTomcatLogsFolderReadable() {
		boolean isTomcatLogsFolderReadable = false;
		EkpLogDao dao = (EkpLogDao) getDao();
		
		isTomcatLogsFolderReadable = dao.isTomcatLogsFolderReadable();
		
		return isTomcatLogsFolderReadable;
	}
}
