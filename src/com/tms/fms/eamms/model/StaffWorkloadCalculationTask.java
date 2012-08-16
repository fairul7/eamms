package com.tms.fms.eamms.model;

import kacang.Application;
import kacang.services.scheduling.BaseJob;
import kacang.services.scheduling.JobTaskExecutionContext;
import kacang.services.scheduling.SchedulingException;
import kacang.util.Log;

public class StaffWorkloadCalculationTask extends BaseJob 
{
	public static final String TASKNAME = "StaffWorkloadCalculationTask";
	public static final String TASKGROUP = "staffworkload";
	public static final String TASKDESC = "Staff Workload Calculation";
	
	public void execute(JobTaskExecutionContext arg0) throws SchedulingException
	{
		Log.getLog(getClass()).info(" ===== Start execute " + TASKDESC + " Job Task ====");
		
		Application app = Application.getInstance();
		EammsModule mod = (EammsModule)app.getModule(EammsModule.class);
		mod.calculateStaffWorkload();
	
		Log.getLog(getClass()).info(" ===== end execute " + TASKDESC + " Job Task ====");
	}
}
