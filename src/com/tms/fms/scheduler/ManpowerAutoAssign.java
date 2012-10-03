package com.tms.fms.scheduler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DefaultDataObject;
import kacang.services.scheduling.BaseJob;
import kacang.services.scheduling.JobTaskExecutionContext;
import kacang.services.scheduling.SchedulingException;
import kacang.util.Log;

import com.tms.fms.engineering.model.UnitHeadModule;

public class ManpowerAutoAssign extends BaseJob 
{
	public static final String NAME = "ManpowerAutoAssignTask";
	public static final String GROUP = "ManpowerAutoAssign";
	public static final String DESC = "Manpower Auto Assignment";
	
	@Override
	public void execute(JobTaskExecutionContext arg0)
	throws SchedulingException {
		Log.getLog(getClass()).info(" ===== Start execute ManpowerAutoAssignTask ====");

		mainProcess();

		Log.getLog(getClass()).info(" ===== End execute ManpowerAutoAssignTask ====");
	}

	public void mainProcess()
	{
		Application app = Application.getInstance();
		UnitHeadModule mod = (UnitHeadModule) app.getModule(UnitHeadModule.class);

		DefaultDataObject autoAssignSettingObj = mod.getAutoAssignmentSchedSetting();
		Number numOfDays = autoAssignSettingObj != null ? (Number) autoAssignSettingObj.getProperty("settingValue") : 3; // default to 3 days
		
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		
		Date startDate = cal.getTime();
		HashMap requestIds = new HashMap();
		for(int i = 0; i < numOfDays.intValue(); i++)
		{
			cal.add(Calendar.DAY_OF_MONTH, 1); //start from day after current day
			startDate = cal.getTime();
			
			HashMap tmpMap = mod.getRequestIdsByDateRange(startDate, null);
			requestIds.putAll(tmpMap);
		}
		
		if(requestIds != null && !requestIds.isEmpty())
		{
			ArrayList arr = new ArrayList();
			for(Iterator itr = requestIds.keySet().iterator(); itr.hasNext();)
			{
				String reqId = (String) itr.next();
				
				int status = mod.autoAssignment(reqId);
				if(status == 0)
				{
					Log.getLog(getClass()).info(" requestId : " + reqId + " fails to auto-assign ");
				}
				else if (status == 3)
				{
					arr.add(reqId);
					Log.getLog(getClass()).info(" requestId : " + reqId + " start end date error, auto-assign skip ");
			}
		}
			
			mod.notifyRespectiveUnitHead(arr);
	}
}
}
