package com.tms.fms.eamms.model;

import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DefaultDataObject;
import kacang.services.scheduling.BaseJob;
import kacang.services.scheduling.JobTaskExecutionContext;
import kacang.services.scheduling.SchedulingException;
import kacang.util.Log;

public class RentalDueReminder extends BaseJob {

	public void execute(JobTaskExecutionContext arg0)
			throws SchedulingException {
		Log.getLog(getClass()).info("=============== Send Mail Due Reminder ========================");	
		
		sendEmailRentalDueReminder();	

	}
	
	public void sendEmailRentalDueReminder(){
		EammsModule mod = (EammsModule)Application.getInstance().getModule(EammsModule.class);
		Collection col = mod.getRentalReqestDueReminderListing();
		
		for(Iterator iter = col.iterator(); iter.hasNext();){
			DefaultDataObject obj = (DefaultDataObject) iter.next();
			String rentalId = (String)obj.getId();
			mod.sendEmailRentalDueReminder(rentalId);
		}
		
		
	}

}
