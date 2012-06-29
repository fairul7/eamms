package com.tms.crm.helpdesk;

import com.tms.crm.helpdesk.ui.SendNotificationJob;
import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DefaultModule;
import kacang.services.scheduling.JobSchedule;
import kacang.services.scheduling.JobTask;
import kacang.services.scheduling.SchedulingException;
import kacang.services.scheduling.SchedulingService;
import kacang.util.Log;

import java.util.Collection;
import java.util.Date;

public class NotificationModule extends DefaultModule	{

	// Writing the scheduler to send notification
	public void init()	{
		final int SCHEDULE_PERIOD = 1;
		SchedulingService scheduler = (SchedulingService) Application.getInstance().getService(SchedulingService.class);

		JobSchedule jobSched = new JobSchedule("SendNotificationTask", JobSchedule.MINUTELY); // Put timly setting here
        jobSched.setGroup("SendNotificationGroup");
        jobSched.setStartTime(new Date());
		jobSched.setRepeatCount(JobSchedule.REPEAT_INDEFINITELY);

        jobSched.setRepeatInterval(SCHEDULE_PERIOD);

        JobTask jobTask = new SendNotificationJob();
		jobTask.setName("SendNotificationTask");
        jobTask.setGroup("SendNotificationGroup");
        jobTask.setDescription("Sending Alert Notification");
        try {
            scheduler.deleteJobTask(jobTask);
            scheduler.scheduleJob(jobTask, jobSched);
        } catch (SchedulingException e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }
	}


	public void insertNotification(Notification alert)	{
		NotificationDao notify = (NotificationDao) getDao();
		try {
			notify.insertNotification(alert);
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
	}

	public Collection getIncidents() throws HelpdeskException
	{
		try	{
     		NotificationDao dao = (NotificationDao) getDao();
			return dao.selectIncidents();
		}	catch (Exception e)	{
			throw new HelpdeskException("Error while retrieving incidents", e);
		}
	}

	public Notification getAlertSettings() {
			NotificationDao dao = (NotificationDao) getDao();
			try {
				return (dao.getAlertSettings());
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString(), e);
				return (null);
			}
		}

	public void updateIndcidentCounter(String occurance, Date currentTime, String incidentId) throws Exception {
		NotificationDao dao = (NotificationDao) getDao();
		try {
			dao.updateIndcidentCounter(occurance, currentTime, incidentId);
			} catch (Exception e) {
			throw new Exception(e.toString());
		}
     }

	public Notification getIncidentAlert(String incidentId) {
		NotificationDao dao = (NotificationDao) getDao();
		try {
			return (dao.getIncidentAlert(incidentId));
			} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return (null);
		}
	}

	public Notification getLastAlert(String incidentId) {
		NotificationDao dao = (NotificationDao) getDao();
		try {
			return (dao.getLastAlert(incidentId));
			} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return (null);
		}
	}

	public void updateAlertSetting(Notification alert) throws Exception {
		NotificationDao dao = (NotificationDao) getDao();
		try {
			dao.updateAlertSetting(alert);
			} catch (Exception e) {
			throw new Exception(e.toString());
		}
     }

	public Collection listUsers(String sort, boolean desc, int start, int rows) throws Exception {
        NotificationDao dao = (NotificationDao) getDao();
        try {
            return dao.userList(sort, desc, start, rows);
        } catch (Exception e) {
            throw new Exception(e.toString());
        }
    }

	public Notification getContactDetails(String id) {
		NotificationDao dao = (NotificationDao) getDao();
		try {
			return (dao.getContactDetails(id));
			} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return (null);
		}
	}

	public Notification getIncident(String incidentId) throws HelpdeskException
	{
		try	{
     		NotificationDao dao = (NotificationDao) getDao();
			return dao.getIncident(incidentId);
		}	catch (Exception e)	{
			throw new HelpdeskException("Error while retrieving incidents", e);
		}
	}

	public Collection getOwner(String incidentId) {
			NotificationDao dao = (NotificationDao) getDao();
			try {
				return (dao.getOwner(incidentId));
				} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString(), e);
				return (null);
			}
		}




}



