package com.tms.fms.setup.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import kacang.Application;
import kacang.services.scheduling.BaseJob;
import kacang.services.scheduling.JobTaskExecutionContext;
import kacang.services.scheduling.SchedulingException;
import kacang.util.Log;

import com.tms.fms.engineering.model.EngineeringModule;

public class AbwEngTransferCostTask extends BaseJob
{
	public static final String NAME = "AbwEngTransferCostTask";
	public static final String GROUP = "AbwEngTransferCost";
	public static final String DESC = "Handle transfer cost of request";
	
	public void execute(JobTaskExecutionContext context) throws SchedulingException
	{
		Log.getLog(getClass()).info("============== AbwEngTransferCostTask Start ===============");
		
		EngineeringModule em = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date reqDate = cal.getTime();
		
		ArrayList statusArr = new ArrayList();
		statusArr.add(EngineeringModule.ASSIGNMENT_STATUS);
		statusArr.add(EngineeringModule.FULFILLED_STATUS);
		statusArr.add(EngineeringModule.CLOSED_STATUS);
		statusArr.add(EngineeringModule.LATE_STATUS);
		
		em.pushTransferCostReq(reqDate, reqDate, statusArr);
		
		Log.getLog(getClass()).info("============== AbwEngTransferCostTask End ===============");
	}
}
