package com.tms.fms.engineering.model;

import java.util.Collection;

import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.util.Log;

public class AssignmentLogDao extends DataSourceDao {

	public void init() throws DaoException {
		try {
			String sql = 
				"CREATE TABLE fms_eng_assignment_manpower_log( " +
				"logId VARCHAR(255) NOT NULL, " +
				"assignmentId VARCHAR(255) NOT NULL, " +
				"userId VARCHAR(255), " +
				"assignBy VARCHAR(255), " +
				"assignDate DATETIME, " +
				"PRIMARY KEY(logId)) ";
			super.update(sql, null);
		} catch (Exception e) {
			// ignore error
		}
		
		try {
			String sql = 
				"ALTER TABLE fms_eng_assignment_manpower_log " +
				"ADD oldUserId VARCHAR(255)";
			super.update(sql, null);
		} catch (Exception e) {
			// ignore error
		}
		
		try {
			String sql = 
				"ALTER TABLE fms_eng_assignment_manpower_log " +
				"ALTER COLUMN userId VARCHAR(255) NULL ";
			super.update(sql, null);
		} catch (Exception e) {
			// ignore error
		}
		
		try {
			String sql = 
				"ALTER TABLE fms_eng_assignment_manpower_log " +
				"ALTER COLUMN assignBy VARCHAR(255) NULL ";
			super.update(sql, null);
		} catch (Exception e) {
			// ignore error
		}
		
		try {
			String sql = 
				"ALTER TABLE fms_eng_assignment_manpower_log " +
				"ALTER COLUMN assignDate DATETIME NULL ";
			super.update(sql, null);
		} catch (Exception e) {
			// ignore error
		}
		
		// create index for fms_eng_assignment_manpower_log(assignmentId)
		try {
			String sql = "CREATE INDEX assignmentId ON fms_eng_assignment_manpower_log(assignmentId)";
			super.update(sql, null);
		} catch (Exception e) {
		}
	}
	
	protected void insertLog(AssignmentLog assign) {
		try {
			String sql = 
				"INSERT INTO fms_eng_assignment_manpower_log " +
				"(logId, assignmentId, userId, oldUserId, assignBy, assignDate) " +
				"VALUES " +
				"(#logId#, #assignmentId#, #userId#, #oldUserId#, #assignBy#, #assignDate#) ";
			
			super.update(sql, assign);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
	}
	
	protected Collection selectLog(String assignmentId) {
		try{
			String sql = 
				"SELECT logId, assignmentId, userId, oldUserId, assignBy, CONVERT(VARCHAR(11), assignDate, 106) AS strAssignDate " +
				"FROM fms_eng_assignment_manpower_log " +
				"WHERE assignmentId = ? " +
				"ORDER BY assignDate DESC";
			return super.select(sql, AssignmentLog.class, new String[] {assignmentId}, 0, -1);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		return null;
	}
	
	protected boolean searchDriverLog(String assignmentId) {
		try {
			String sql = 
				"SELECT l.logId, l.assignmentId, l.userId, l.assignDate " +
				"FROM fms_eng_assignment_manpower_log l " +
				"WHERE assignmentId = ? " +
				"AND l.userId IS NULL " +
				"ORDER BY assignDate ASC";
			
			Collection col = super.select(sql, AssignmentLog.class, new String[] {assignmentId}, 0, 1);
			if (col!=null && col.size()>0){
				return true;
			}else{
				return false;
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		return false;
	}
	
	protected void updateDriverLog(AssignmentLog log) {
		try {
			String sql =
				"UPDATE fms_eng_assignment_manpower_log	" +
				"SET userId = #userId#, " +
				"assignBy = #assignBy#, " +
				"assignDate = #assignDate# " +
				"WHERE assignmentId = #assignmentId# AND oldUserId = #oldUserId# AND userId IS NULL";
			
			super.update(sql, log);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
	}
	
	protected String selectDriver(String assignmentId){
		try {
			String sql = 
				"SELECT oldUserId " +
				"FROM fms_eng_assignment_manpower_log " +
				"WHERE assignmentId = ? " +
				"AND userId IS NULL " +
				"ORDER BY assignDate DESC";
			Collection col = super.select(sql, AssignmentLog.class, new String[] {assignmentId}, 0, 1);
			if (col!=null && col.size()==1){
				AssignmentLog log = (AssignmentLog) col.iterator().next();
				return log.getOldUserId();
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		return null;
	}
}
