package com.tms.collab.isr.model;

import kacang.model.DaoException;
import kacang.model.DefaultModule;
import kacang.util.Log;

public class LogModel extends DefaultModule {
	public boolean insertLog(LogObject log) {
		LogDao dao = (LogDao)getDao();
		boolean isSuccess = true;
		
		try {
			dao.insertLog(log);
		}
		catch(DaoException error) {
			isSuccess = false;
			Log.getLog(getClass()).error(error, error);
		}
		
		return isSuccess;
	}
}
