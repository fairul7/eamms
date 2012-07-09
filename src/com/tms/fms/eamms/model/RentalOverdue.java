package com.tms.fms.eamms.model;

import kacang.Application;
import kacang.services.scheduling.BaseJob;
import kacang.services.scheduling.JobTaskExecutionContext;
import kacang.services.scheduling.SchedulingException;
import kacang.util.Log;

public class RentalOverdue extends BaseJob {

	public void execute(JobTaskExecutionContext arg0)
			throws SchedulingException {
		Log.getLog(getClass()).info("=============== Set Overdue Status ========================");	
		
		setOverdueStatus();	

	}
	
	public void setOverdueStatus(){
		EammsModule mod = (EammsModule)Application.getInstance().getModule(EammsModule.class);
		mod.setOverdueStatus();
		
	}

}
