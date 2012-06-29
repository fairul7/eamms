package com.tms.collab.isr.setting.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DefaultModule;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

public class ConfigModel extends DefaultModule {
	
	public boolean insertConfigDetail(ConfigDetailObject configDetail) {
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
	
	public boolean updateConfigDetail(ConfigDetailObject configDetail) {
		ConfigDao dao = (ConfigDao)getDao();
		boolean isSuccess = true;
		
		try {
			dao.updateConfigDetail(configDetail);
		}
		catch(DaoException error) {
			isSuccess = false;
			Log.getLog(getClass()).error(error, error);
		}
		
		return isSuccess;
	}
	
	public ConfigDetailObject getConfigDetail(String configDetailId) {
		ConfigDao dao = (ConfigDao)getDao();
		
		try {
			return dao.getConfigDetail(configDetailId);
		}
		catch(DaoException error) {
			Log.getLog(getClass()).error(error, error);
			return null;
		}
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
	
	public String getConfigDetailName(String configDetailId) {
		ConfigDao dao = (ConfigDao)getDao();
		
		try {
			return dao.getConfigDetailName(configDetailId);
		}
		catch(DaoException error) {
			Log.getLog(getClass()).error(error, error);
			return null;
		}
	}
	
	public boolean deleteConfigDetail(String configDetailId) {
		ConfigDao dao = (ConfigDao)getDao();
		boolean isSuccess = true;
		
		try {
			dao.deleteConfigDetail(configDetailId);
		}
		catch(DaoException error) {
			isSuccess = false;
			Log.getLog(getClass()).error(error, error);
		}
		
		return isSuccess;
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
	
	public Map getPriorityOptionsMap() {
		Application application = Application.getInstance();
		ConfigModel model = (ConfigModel)application.getModule(ConfigModel.class);
		ConfigDetailObject config = new ConfigDetailObject();
		Map optionsMap = new SequencedHashMap();
		Collection cols = model.getConfigDetailsByType(ConfigDetailObject.PRIORITY, null);
		
		//optionsMap.put("", "---" + application.getMessage("isr.label.pleaseSelect") + "---");
		for(Iterator i=cols.iterator(); i.hasNext();) {
			config = (ConfigDetailObject) i.next();
			if(config.getConfigDetailName() != null && !"".equals(config.getConfigDetailName())) {
				optionsMap.put(config.getConfigDetailName(), config.getConfigDetailName());
			}
		}
		
		return optionsMap;
    }
	
	public Map getRequestTypeOptionsMap() {
		Application application = Application.getInstance();
		ConfigModel model = (ConfigModel)application.getModule(ConfigModel.class);
		ConfigDetailObject config = new ConfigDetailObject();
		Map optionsMap = new SequencedHashMap();
		Collection cols = model.getConfigDetailsByType(ConfigDetailObject.REQUEST_TYPE, null);
		
		for(Iterator i=cols.iterator(); i.hasNext();) {
			config = (ConfigDetailObject) i.next();
			if(config.getConfigDetailName() != null && !"".equals(config.getConfigDetailName())) {
				optionsMap.put(config.getConfigDetailName(), config.getConfigDetailName());
			}
		}
		
		return optionsMap;
	}
	
	public boolean updateEmailSettings(ArrayList emailSettings){
		boolean isSuccess = true;
		ConfigDao dao = (ConfigDao)getDao();
		try{
			dao.updateEmailSettings(emailSettings);
			
		}catch(DaoException error){
			isSuccess = false;
			Log.getLog(getClass()).error(error, error);
		}
		return isSuccess;
	}
	
	public Collection selectEmailSettings(String[] emailFor){
		Collection emailSettings = null;
		ConfigDao dao = (ConfigDao)getDao();
		try{
			emailSettings = dao.selectEmailSettings(emailFor);
			
		}catch(DaoException error){
			Log.getLog(getClass()).error(error, error);
		}
		
		return emailSettings;
	}
	
	public boolean recreateStatusTable() {
		boolean isSuccess = true;
		ConfigDao dao = (ConfigDao)getDao();
		
		try {
			dao.recreateStatusTable();
		}catch(DaoException error){
			isSuccess = false;
			Log.getLog(getClass()).error(error, error);
		}
		
		return isSuccess;
	}
}
