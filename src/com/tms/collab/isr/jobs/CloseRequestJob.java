package com.tms.collab.isr.jobs;

import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.services.scheduling.BaseJob;
import kacang.services.scheduling.JobTaskExecutionContext;
import kacang.services.scheduling.SchedulingException;
import com.tms.collab.isr.model.RequestModel;
import com.tms.collab.isr.model.RequestObject;
import com.tms.collab.isr.model.StatusObject;
import com.tms.collab.isr.setting.model.ConfigDetailObject;
import com.tms.collab.isr.setting.model.ConfigModel;

public class CloseRequestJob extends BaseJob{
	
	public void execute(JobTaskExecutionContext context) throws SchedulingException {
		ConfigModel configModel = (ConfigModel)Application.getInstance().getModule(ConfigModel.class);
		RequestModel requestModel = (RequestModel)Application.getInstance().getModule(RequestModel.class);
		ConfigDetailObject config = new ConfigDetailObject();
		Collection cols;
		
		cols = configModel.getConfigDetailsByType(ConfigDetailObject.REQUEST_AUTO_CLOSE, null);
		if(cols != null) {
			if(cols.size() > 0) {
				config = (ConfigDetailObject) cols.iterator().next();
				
				if(config.getConfigDetailName() != null && !"".equals(config.getConfigDetailName())) {
					String autoCloseDays = config.getConfigDetailName();
					
					if(autoCloseDays != null && autoCloseDays.trim().length() > 0 && Integer.parseInt(autoCloseDays) > 0){
						
						//condition to form something like > 30 in the sql
						StringBuffer condition = new StringBuffer(" >= " + autoCloseDays);
						
						Collection reqCol = requestModel.selectExpiredCompletedRequest(condition.toString());
						
						if(reqCol != null && reqCol.size() > 0){
							String ids[] = new String[reqCol.size()];
							
							int counter = 0;
							for(Iterator i=reqCol.iterator(); i.hasNext(); ){
								RequestObject ro = (RequestObject)i.next();
								ids[counter] = ro.getRequestId();
								counter++;
							}
							
							requestModel.updateRequestStatus(StatusObject.STATUS_ID_COMPLETED, StatusObject.STATUS_ID_CLOSE, ids);
						}
						
					}
				}
			}
		}
    }
	
}
