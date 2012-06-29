package com.tms.sam.po.setting.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DefaultModule;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

public class ConfigModule extends DefaultModule{
	public Map getPriorityOptionsMap() {
		Application application = Application.getInstance();
		ConfigModule model = (ConfigModule)application.getModule(ConfigModule.class);
		ConfigObject config = new ConfigObject();
		Map optionsMap = new SequencedHashMap();
		Collection cols = model.getConfigDetailsByType(ConfigObject.PRIORITY, null);
		
		for(Iterator i=cols.iterator(); i.hasNext();) {
			config = (ConfigObject) i.next();
			if(config.getConfigDetailName() != null && !"".equals(config.getConfigDetailName())) {
				optionsMap.put(config.getConfigDetailName(), config.getConfigDetailName());
			}
		}
		
		return optionsMap;
    }
	
	public boolean deleteConfigDetailsByType(String configDetailType) {
		ConfigDao dao = (ConfigDao)getDao();
		boolean isSuccess = true;
		
		try {
			dao.deleteConfigDetailsByType(configDetailType);
		}
		catch(DaoException error) {
			isSuccess = false;
			Log.getLog(getClass()).error(error, error);
		}
		
		return isSuccess;
	}
	
	public boolean insertConfigDetail(ConfigObject configDetail) {
		ConfigDao dao = (ConfigDao)getDao();
		boolean isSuccess = true;
		
		try {
			dao.insertConfigDetail(configDetail);
		}
		catch(DaoException error) {
			isSuccess = false;
			Log.getLog(getClass()).error(error, error);
		}
		
		return isSuccess;
	}
	
	public Collection getConfigDetailsByType(String configDetailType, String orderBy) {
		ConfigDao dao = (ConfigDao)getDao();
		
		try {
			return dao.getConfigDetailsByType(configDetailType, orderBy);
		}
		catch(DaoException error) {
			Log.getLog(getClass()).error(error, error);
			return null;
		}
	}
//	public String get(String configDetailType, String orderBy) {
//		ConfigDao dao = (ConfigDao)getDao();
//		
//		try {
//			return dao.getConfigDetailsByType(configDetailType, orderBy);
//		}
//		catch(DaoException error) {
//			Log.getLog(getClass()).error(error, error);
//			return null;
//		}
//	}
}
