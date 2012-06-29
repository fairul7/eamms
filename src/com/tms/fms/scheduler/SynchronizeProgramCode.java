package com.tms.fms.scheduler;


import java.util.Collection;
import java.util.Iterator;

import com.tms.fms.abw.model.AbwModule;
import com.tms.fms.engineering.model.EngineeringModule;

import kacang.Application;
import kacang.model.DefaultDataObject;
import kacang.services.scheduling.BaseJob;
import kacang.services.scheduling.JobTaskExecutionContext;
import kacang.services.scheduling.SchedulingException;
import kacang.util.Log;

public class SynchronizeProgramCode extends BaseJob {

	public void execute(JobTaskExecutionContext arg0)
			throws SchedulingException {
		Log.getLog(getClass()).info("===== Start execute Synchronize Program Code Task ====");		
		mainProcess();
		Log.getLog(getClass()).info(" ===== End execute Synchronize Program Code Task ====");

	}
	public void mainProcess(){
		AbwModule abw = (AbwModule)Application.getInstance().getModule(AbwModule.class);
		EngineeringModule mod = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		Collection col = abw.getProgramCodeListing();
		
		for(Iterator iter=col.iterator();iter.hasNext();){
			DefaultDataObject obj = (DefaultDataObject) iter.next();
			mod.insertProgramCode(obj);
		}
	}

}
