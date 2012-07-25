package com.tms.fms.eamms.model;

import java.util.Collection;
import java.util.Iterator;
import kacang.Application;
import kacang.model.DefaultDataObject;
import kacang.services.scheduling.BaseJob;
import kacang.services.scheduling.JobTaskExecutionContext;
import kacang.services.scheduling.SchedulingException;
import kacang.util.Log;

public class PMDueReminder extends BaseJob {

	public void execute(JobTaskExecutionContext arg0) throws SchedulingException {
		Log.getLog(getClass()).info("=============== START : Send Mail Due Reminder ========================");	
		sendEmailDueReminderPM();	
		Log.getLog(getClass()).info("=============== END : Send Mail Due Reminder ========================");
	}
	
	public void sendEmailDueReminderPM(){
		EammsModule mod = (EammsModule)Application.getInstance().getModule(EammsModule.class);
		Collection col = mod.getPMReqestDueReminderListing();
		
		for(Iterator iter = col.iterator(); iter.hasNext();){
			DefaultDataObject obj = (DefaultDataObject) iter.next();
			
			String id = (String)obj.getId();
			mod.sendEmailPMDueReminder(id);
		}
	}
}
