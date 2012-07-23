package com.tms.fms.eamms.model;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import kacang.Application;
import kacang.model.DefaultDataObject;
import kacang.services.scheduling.BaseJob;
import kacang.services.scheduling.JobTaskExecutionContext;
import kacang.services.scheduling.SchedulingException;
import kacang.util.Log;


public class SoftwareExpiredDate extends BaseJob {
	
	public static String EXPIRED_STATUS = "3";

	public void execute(JobTaskExecutionContext arg0) throws SchedulingException {
		Log.getLog(getClass()).info("=============== Set Software to Expired Status ========================");	
		setOverdueStatus();
		Log.getLog(getClass()).info("=============== DONE :: Software Status ========================");	
	}
	
	public void setOverdueStatus(){
		EammsModule mod = (EammsModule)Application.getInstance().getModule(EammsModule.class);		
		Collection col = mod.getSoftwareExpiredDate();
		
		for(Iterator iter = col.iterator(); iter.hasNext();){
			DefaultDataObject obj = (DefaultDataObject) iter.next();
			obj.setProperty("c_status", EXPIRED_STATUS);
			obj.setProperty("dateModified", new Date());
			
			mod.setSoftwareExpiredStatus(obj);
		}
	}

}
