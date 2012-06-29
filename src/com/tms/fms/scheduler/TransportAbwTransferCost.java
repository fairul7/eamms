package com.tms.fms.scheduler;

import java.util.Collection;

import com.tms.fms.abw.model.AbwModule;
import com.tms.fms.abw.model.AbwTransferCostObject;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.transport.model.TransportModule;

import kacang.Application;
import kacang.services.scheduling.BaseJob;
import kacang.services.scheduling.JobTaskExecutionContext;
import kacang.services.scheduling.SchedulingException;
import kacang.util.Log;
import kacang.util.UuidGenerator;

public class TransportAbwTransferCost extends BaseJob{

	public static final String TASKNAME = "TransportAbwTransferCost";
	public static final String TASKGROUP = "TransportAbwTransferCostGroup";
	public static final String TASKDESC = "To push data into table fms_trans_transfer_cost in abwdb.";
	
	@Override
	public void execute(JobTaskExecutionContext arg0) throws SchedulingException {
		// TODO Auto-generated method stub
		Log.getLog(getClass()).info("==========Start TransportAbwTransferCost scheduler==========");
		processData();
		Log.getLog(getClass()).info("===========End TransportAbwTransferCost scheduler===========");
	}
	
	private void processData(){
		Application app = Application.getInstance();
		TransportModule tModule = (TransportModule) app.getModule(TransportModule.class);
		AbwModule aModule = (AbwModule) app.getModule(AbwModule.class);
		
		Collection<AbwTransferCostObject> collAbw = tModule.getInfoToCreateAbwTransferCostObjects();
		if(collAbw != null && collAbw.size() > 0){
			UuidGenerator uuid = UuidGenerator.getInstance();
			for(AbwTransferCostObject object : collAbw){
				object.setUniqueId(uuid.getUuid());
				String cost = "0";
				try{
					cost = tModule.getAbwCost(object.getRequestId(), object.getCategoryId(), object.getType());
				}catch(Exception er){
					
				}
				object.setCost(cost);
				object.setCreatedBy(SetupModule.FMS_SYSTEM_ADMIN);
			}
			aModule.insertAbwTransferCost(collAbw);
		}
	}
}
