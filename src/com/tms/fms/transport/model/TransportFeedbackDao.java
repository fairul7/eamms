package com.tms.fms.transport.model;

import java.util.Collection;

import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.util.Log;

public class TransportFeedbackDao extends DataSourceDao {

	public void init() throws DaoException {
		try {
			String sql = 
				"CREATE TABLE fms_tran_req_feedback( " +
				"requestId VARCHAR(255) NOT NULL, " +
				"supportQuality VARCHAR(2), " +
				"driverPerformance VARCHAR(2), " +
				"customerService VARCHAR(2), " +
				"vehicleAvailability VARCHAR(2), " +
				"vehicleCondition VARCHAR(2), " +
				"remarks TEXT, " +
				"updatedBy VARCHAR(255), " +
				"updatedDate DATETIME, " +
				"PRIMARY KEY (requestId))";
			super.update(sql, null);
		} catch (Exception e) {
			
		}
	}
	
	protected void insertFeedback(TransportFeedbackDataObject trans) {	
		try {
			String sql = 
				"INSERT INTO fms_tran_req_feedback( " +
				"requestId, supportQuality, driverPerformance, customerService, vehicleAvailability, " +
				"vehicleCondition, remarks, updatedBy, updatedDate) " +
				"VALUES( " +
				"#requestId#, #supportQuality#, #driverPerformance#, #customerService#, #vehicleAvailability#, " +
				"#vehicleCondition#, #remarks#, #updatedBy#, #updatedDate#)";
			
			super.update(sql, trans);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
	}
	
	protected boolean getRequestId(String id) {
		try {
			String sql = 
				"SELECT requestId " +
				"FROM fms_tran_req_feedback " +
				"WHERE requestId = ? ";
			
			Collection col = super.select(sql, TransportFeedbackDataObject.class, new String[] {id}, 0, 1);
			if (col.size()>0){
				return true;
			}else {
				return false;
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		return false;
	}
}
