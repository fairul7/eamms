package com.tms.fms.engineering.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DaoException;
import kacang.services.scheduling.BaseJob;
import kacang.services.scheduling.JobTaskExecutionContext;
import kacang.services.scheduling.SchedulingException;

import kacang.util.Log;

@SuppressWarnings("serial")
public class PendingRequestNotificationTask extends BaseJob {
	private static boolean iamRunning = false;
	
	int checkPeriod = Integer.parseInt(Application.getInstance().getMessage("fms.notification.checkPeriodInHours"));
	long hoursInMilliSeconds = 60 * 60 * 1000;
	long checkPeriodInMS = checkPeriod * hoursInMilliSeconds;
	
	private synchronized static boolean canRun() {
		if (!iamRunning) {
			iamRunning = true;
			return true;
		}
		return false;
	}
	
	private synchronized static void resetRunning() {
		iamRunning = false;
	}
	
	@Override
	public void execute(JobTaskExecutionContext arg0)
			throws SchedulingException {
		if (canRun()) {
			try {
				hodPendingRequestNotification();
			} catch (Exception e) {
				Log.getLog(getClass()).error(e.getMessage(), e);
			}
			
			try {
				houPendingRequestNotification(); 				
			} catch (Exception e) {
				Log.getLog(getClass()).error(e.getMessage(), e);
			}
			
			resetRunning();
		} else {
			Log.getLog(getClass()).warn("Cannot run. Still waiting.");
		}
	}

	public void hodPendingRequestNotification() {
		Log.getLog(getClass()).info("RUNNING hodPendingRequestNotification()");		
		
		EngineeringModule module = (EngineeringModule) Application.getInstance().getModule(EngineeringModule.class);
		Collection pendingRequest = new ArrayList();
		
		//get collection of request where status = 'Pending HOD Approval'
		try {
			pendingRequest = module.getHODRequest();
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		
		if (pendingRequest!= null && pendingRequest.size()>0){
			Date now = new Date();
			
			for (Iterator itr = pendingRequest.iterator(); itr.hasNext();){
				EngineeringRequest er = (EngineeringRequest) itr.next();
				
				try {
					Collection not = module.selectNotification(er.getRequestId(), "D");
					
					if (not == null || not.size()<=0){
						long diff = hoursDiff(er.getSubmittedDate(), now);
						
						if (diff >= checkPeriod ) {
							module.sendSubmissionEmail(er.getRequestId(), false);
							module.insertNotification(er.getRequestId(), "D");
						}
					}
					
				} catch (DaoException e) {
					Log.getLog(getClass()).error(e.toString(), e);
				}
				
			}
		}
	}
	
	public void houPendingRequestNotification() {
		Log.getLog(getClass()).info("RUNNING houPendingRequestNotification()");
		Date startDate = new Date();
		
		EngineeringModule module = (EngineeringModule) Application.getInstance().getModule(EngineeringModule.class);
		UnitHeadModule UHModule=(UnitHeadModule)Application.getInstance().getModule(UnitHeadModule.class);
		Collection pendingHOUApproval = new ArrayList();
		
		//get collection of request where status = 'Assignment'
		try {
			pendingHOUApproval = module.getHOURequest();
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		
		if (pendingHOUApproval!= null && pendingHOUApproval.size()>0){
			Date now = new Date();
			
			for (Iterator itr = pendingHOUApproval.iterator(); itr.hasNext();){
				EngineeringRequest er = (EngineeringRequest) itr.next();
				
				if (!UHModule.isAssignmentPrepared(er.getRequestId())){
					try {
						Collection not = module.selectNotification(er.getRequestId(), "U");
						
						if (not == null || not.size()<=0){
							long diff = hoursDiff(er.getModifiedOn(), now);
							
							if (diff >= checkPeriod ) {
								module.sendHOUEmail(er.getRequestId());
								module.insertNotification(er.getRequestId(), "U");
							}
						}
						
					} catch (DaoException e) {
						Log.getLog(getClass()).error(e.toString(), e);
					}
				}
			}
		}
	}
	
	public long hoursDiff(Date start, Date end){		
		long diff = Math.round((end.getTime() - start.getTime()) / (hoursInMilliSeconds));		
		return diff;
	}
}
