package com.tms.fms.abw.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DefaultDataObject;
import kacang.model.DefaultModule;
import kacang.util.Log;

import com.tms.fms.facility.model.RateCard;
import com.tms.fms.scheduler.SchedulerUtil;
import com.tms.fms.scheduler.TransportAbwTransferCost;
import com.tms.fms.setup.model.SetupModule;
import com.tms.fms.util.JobUtil;

public class AbwModule extends DefaultModule {
	public static final String SYNCHRONIZE_CODE_SCHEDULE_TASK = "SCST";
	public static final String NEW_RECORD = "N";
	public static final String UPDATED_RECORD = "U";
	
	private TransportAbwTransferCost abwTransferCostTask;
	
	public void init() {
		
		//initialize ABW transfer cost scheduler
		SetupModule stpModule = (SetupModule) Application.getInstance().getModule(SetupModule.class);
		DefaultDataObject objectSchedule = stpModule.getAbwSchedulerTime(SetupModule.ABW_TRANSFER_COST_SCHEDULE_ID);
		if(objectSchedule != null){
			int hour = Integer.parseInt(((String) objectSchedule.getProperty("scheduleTime1")).substring(0, 2));
			int min = Integer.parseInt(((String) objectSchedule.getProperty("scheduleTime1")).substring(3));
			initializeAbwTransferCostScheduler(hour, min);
		}
		
		JobUtil.scheduleDailyTask(RateCardEmailNotificationTask.NAME, RateCardEmailNotificationTask.GROUP, 
				RateCardEmailNotificationTask.DESC, new RateCardEmailNotificationTask(), 18, 00);
	}
	
	public Collection getProgramCodeListing() {
		AbwDao dao = (AbwDao) getDao();
		try {
			return dao.getProgramCodeListing();
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return new ArrayList();
		}
	}

	public Collection getNewRateCard(Date currDateTime)
	{
		return getRateCardChanges(currDateTime, AbwModule.NEW_RECORD);
	}
	
	public Collection getUpdateRateCard(Date currDateTime)
	{
		return getRateCardChanges(currDateTime, AbwModule.UPDATED_RECORD);
	}

	private Collection getRateCardChanges(Date currDateTime, String actionTaken)
	{
		AbwDao dao = (AbwDao) getDao();
		try
		{
			String dateCreatedStart = "";
			String dateCreatedEnd = "";
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try
			{
				Calendar calS = Calendar.getInstance();
				calS.setTime(currDateTime);
				calS.add(Calendar.DAY_OF_MONTH, -1);
				dateCreatedStart = sdf.format(calS.getTime());
				
				dateCreatedEnd = sdf.format(currDateTime);
			}
			catch(Exception e){}
			
			return dao.getRateCardChanges(dateCreatedStart, dateCreatedEnd, actionTaken);
		}
		catch(Exception e)
		{
			Log.getLog(getClass()).error(" error @ AbwModule.getRateCardChanges(2) : " + e, e);
			return null;
		}
	}

	public void insertRateCard(RateCard rc)
	{
		AbwDao dao = (AbwDao) getDao();
		try 
		{
			rc.setProperty("actionTaken", AbwModule.UPDATED_RECORD);
			dao.insertRateCard(rc);
		} 
		catch (DaoException e) 
		{
			Log.getLog(getClass()).error(" error @ AbwModule.insertRateCard(1) : " + e, e);
		}
	}
	
	/** code for show abwdb tables listing for testing purpose */
	public Collection columnListing( String tableName) {
		AbwDao dao = (AbwDao) getDao();
		try {
			return dao.columnListing(tableName);
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return new ArrayList();
		}
	}
	public Collection abw_projectListing( String sort, boolean desc,
			int start, int rows) {
		AbwDao dao = (AbwDao) getDao();
		try {
			return dao.abw_projectListing( sort, desc, start, rows);
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return new ArrayList();
		}
	}


	public int abw_projectCount() {
		AbwDao dao = (AbwDao) getDao();
		try {
			return dao.abw_projectCount();
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return 0;
		}
	}
	
	public Collection abwTableListing( String keyword,String tableName, String sort, boolean desc,
			int start, int rows) {
		AbwDao dao = (AbwDao) getDao();
		try {
			return dao.abwTableListing( keyword,tableName,sort, desc, start, rows);
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return new ArrayList();
		}
	}


	public int abwTableCount(String keyword, String tableName) {
		AbwDao dao = (AbwDao) getDao();
		try {
			return dao.abwTableCount(keyword, tableName);
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return 0;
		}
	}
	
	
	/** end test **********************************************/
  
	/**
	 * Initialize scheduler for Transport ABW transfer cost
	 * @param hour
	 * @param min
	 */
	public void initializeAbwTransferCostScheduler(int hour, int min){
		abwTransferCostTask = new TransportAbwTransferCost();
		SchedulerUtil.scheduleDailyTask(TransportAbwTransferCost.TASKNAME, 
				TransportAbwTransferCost.TASKGROUP, TransportAbwTransferCost.TASKDESC, 
				abwTransferCostTask, hour, min);
	}
	
	/**
	 * Insert into table fms_trans_transfer_cost
	 * @param collection
	 */
	public void insertAbwTransferCost(Collection<AbwTransferCostObject> collection){
		try{
			if(collection != null && collection.size() > 0){
				AbwDao dao = (AbwDao) getDao();
				for(AbwTransferCostObject object : collection){
					if(object.getQuantity() > 0)
						dao.insertAbwTransferCost(object);
				}
			}
		}
		catch(DaoException e){
			Log.getLog(getClass()).error(e, e);
		}
	}

	public void insertAbwEngTransferCost(DefaultDataObject pushObj)
	{
		AbwDao dao = (AbwDao) getDao();
		try 
		{
			dao.insertAbwEngTransferCost(pushObj);
		} 
		catch (DaoException e) 
		{
			Log.getLog(getClass()).error(" error @ AbwModule.insertAbwEngTransferCost(1) : " + e, e);
		}
	}
	
	public boolean existAbwTransferCost(String requestId) {
		AbwDao dao = (AbwDao) getDao();
		try {
			return dao.existAbwTransferCost(requestId);
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return false;
		}
	}
	
	public boolean existAbwEngTransferCost(String requestId) {
		AbwDao dao = (AbwDao) getDao();
		try {
			return dao.existAbwEngTransferCost(requestId);
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return false;
		}
	}
	
	public Collection insertAbwTransferCostBackDated(Collection<AbwTransferCostObject> collection){
		Collection<AbwTransferCostObject> colRet = new ArrayList();
		String idProcessing = "";		//processing id and skip duplicate checking
		try{
			if(collection != null && collection.size() > 0){
				AbwDao dao = (AbwDao) getDao();
				for(AbwTransferCostObject object : collection){
					if(!existAbwTransferCost(object.getRequestId()) || idProcessing.equals(object.getRequestId())){
						if(object.getQuantity() > 0){			
							idProcessing = object.getRequestId();
							dao.insertAbwTransferCost(object);
							colRet.add(object);
						}
					}
					
				}
			}
		}		
		catch(DaoException e){
			Log.getLog(getClass()).error(e, e);
			return new ArrayList();
		}
		return colRet;
	}
	
	
	
	
	public void insertTransferCostReversal(TransferCostCancellationObject obj){
		AbwDao dao = (AbwDao) getDao();
		try {
			dao.insertTransferCostReversal(obj);
		} catch (DaoException e){
			Log.getLog(getClass()).error(" error @ AbwModule.insertTransferCostReversal "+obj.getRequestId()+" : " + e, e);
		}
	}
	
	public void insertTransferCostFacilities(TransferCostCancellationObject obj){
		AbwDao dao = (AbwDao) getDao();
		try {
			dao.insertTransferCostFacilities(obj);
		} catch (DaoException e){
			Log.getLog(getClass()).error(" error @ AbwModule.insertTransferCostFacilities "+obj.getRequestId()+" : " + e, e);
		}
	}
	
	
}
