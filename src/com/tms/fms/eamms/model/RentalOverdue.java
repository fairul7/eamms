package com.tms.fms.eamms.model;

import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DefaultDataObject;
import kacang.services.scheduling.BaseJob;
import kacang.services.scheduling.JobTaskExecutionContext;
import kacang.services.scheduling.SchedulingException;
import kacang.util.Log;
import kacang.util.UuidGenerator;

public class RentalOverdue extends BaseJob {

	public void execute(JobTaskExecutionContext arg0)
			throws SchedulingException {
		Log.getLog(getClass()).info("=============== Set Overdue Status ========================");	
		
		setOverdueStatus();	

	}
	
	public void setOverdueStatus(){
		EammsModule mod = (EammsModule)Application.getInstance().getModule(EammsModule.class);		
		Collection col = mod.getOverdueItemsListing();
		DefaultDataObject objStatus = new DefaultDataObject();
		objStatus.setProperty("status", "Overdue");
		objStatus.setProperty("createdBy", "System");
		
		UuidGenerator uuid = UuidGenerator.getInstance();
		for(Iterator iter = col.iterator(); iter.hasNext();){
			DefaultDataObject obj = (DefaultDataObject) iter.next();
			
			objStatus.setId(uuid.getUuid());			
			objStatus.setProperty("rentalId", obj.getProperty("rentalId"));			
			mod.insertRentalStatusTrail(objStatus);
		}
		
		mod.setOverdueStatus();
		
	}

}
