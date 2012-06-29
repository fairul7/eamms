package com.tms.fms.facility.model;

import kacang.model.DefaultDataObject;
import kacang.model.DefaultModule;
import kacang.util.Log;
import java.util.Collection;
import java.util.Iterator;

public class MonitorModule  extends DefaultModule {
 
	public Collection showTop10SlowQuery() {
		try {
			MonitorDao dao = (MonitorDao) getDao();
			Collection col= null;
			col=dao.showTop10SlowQuery();
			return col;
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error showTop10SlowQuery ", e); 
		}
		return null;
	}
	
	public DefaultDataObject showHandlerQuery(DefaultDataObject sqlHandler) {
		try {
			MonitorDao dao = (MonitorDao) getDao();
			Collection col= null;
			col=dao.showHandlerQuery(sqlHandler);
			for (Iterator iterate = col.iterator();iterate.hasNext();) {
				DefaultDataObject s = (DefaultDataObject) iterate.next(); 
				return s;
			}
			return null; 
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error showHandlerQuery ", e); 
			return null;
		} 
	}
	
}

