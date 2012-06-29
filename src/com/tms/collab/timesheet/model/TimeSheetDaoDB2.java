package com.tms.collab.timesheet.model;

import java.util.Calendar;

import kacang.model.DaoException;
import kacang.util.UuidGenerator;

public class TimeSheetDaoDB2 extends TimeSheetDao
{
	
	public void init() throws DaoException{
        try {
        super.update("CREATE TABLE timesheet ("+
                "id VARCHAR(255),"+
                "createdDateTime TIMESTAMP,"+
                "projectId VARCHAR(255),"+
                "taskId VARCHAR(255),"+
                "createdBy VARCHAR(255),"+
                "tsDate DATE,"+
                "duration FLOAT,"+
                "description CLOB(65K),"+
                "adjustment VARCHAR(1) DEFAULT 'N',"+
                "adjustmentDateTime TIMESTAMP,"+
                "adjustmentBy VARCHAR(255),"+
                "adjustmentById VARCHAR(255),"+
                "adjustmentDescription CLOB(65K),"+
                "adjustedDuration FLOAT, " +
                "taskTitle VARCHAR(255) DEFAULT '', " +
                "taskCategoryName VARCHAR(255) DEFAULT ''" + 
                ")",null);
        }catch(Exception e) {}
        
        try {
        	super.update("CREATE TABLE timesheet_manday_remarks (" +
        			"id VARCHAR(255) NOT NULL, " +
        			"taskId VARCHAR(255), " +
        			"assigneeUserId VARCHAR(255), " +
        			"remarks VARCHAR(255), " +
        			"lastUpdatedBy VARCHAR(255), " +
        			"lastUpdatedDate TIMESTAMP, " +
        			"PRIMARY KEY(id))", null);
        }
        catch(Exception e) {}
        
    }
	
	public void insertMandayReportRemarks(String taskId, String assigneeUserId, String remarks) throws DaoException {
    	if(taskId != null && !"".equals(taskId)) {
    		if(assigneeUserId == null) {
    			assigneeUserId = "";
    		}
    		
    		String sqlDelete = "DELETE FROM timesheet_manday_remarks " +
    				"WHERE taskId = ? and assigneeUserId = ?";
    		
	    	String sqlInsert = "INSERT INTO timesheet_manday_remarks " +
	    			"(id, taskId, assigneeUserId, remarks, lastUpdatedBy, lastUpdatedDate) values (" +
	    			"?, ?, ?, ?, ?, ?)";
	    	
	    	String newId = UuidGenerator.getInstance().getUuid();
	    	String userId = getCurrentUserFromDWR().getId();
	    	
	    	super.update(sqlDelete, new Object[] {taskId, assigneeUserId});
	    	Calendar now = Calendar.getInstance();  
	    	super.update(sqlInsert, new Object[] {newId, taskId, assigneeUserId, remarks, userId, now.getTime()});
    	}
    	else {
    		throw new DaoException("Parameter taskId shouldn't be null");
    	}
    }
	
	public void insert(TimeSheet obj) throws DaoException
	{
        String id = UuidGenerator.getInstance().getUuid();
		Calendar now = Calendar.getInstance();
        obj.setId(id);
        super.update("INSERT INTO timesheet (id,createdDateTime,projectId,taskId,createdBy,"+
                "tsDate,duration,description,adjustment,adjustmentDateTime,"+
                "adjustmentBy,adjustmentById,adjustmentDescription,adjustedDuration,taskCategoryName,"+
                "taskTitle) "+
                "VALUES "+
                "(?,?,?,?,?,?,?,?,'N',?,?,?,?,0,?,?)", new Object[] {obj.getId(), now.getTime(),
				obj.getProjectId(), obj.getTaskId(), obj.getCreatedBy(), obj.getTsDate(), new Float(obj.getDuration()),
				obj.getDescription(), now.getTime(), obj.getAdjustmentBy(), obj.getAdjustmentById(),
				obj.getAdjustmentDescription(), obj.getTaskCategoryName(),obj.getTaskTitle()});
    }
	
	public void updateAdjustment(TimeSheet obj) throws DaoException
	{
		Calendar now = Calendar.getInstance();
		super.update("UPDATE timesheet SET "+
					"adjustment=?,"+
					"adjustmentDateTime=?,"+
					"adjustmentById=?,"+
					"adjustmentBy=?,"+
					"adjustmentDescription=?,"+
					"adjustedDuration=?"+
					"WHERE id=?", new Object[] {obj.getAdjustment(), now.getTime(), obj.getAdjustmentById(),
					obj.getAdjustmentBy(), obj.getAdjustmentDescription(), new Double(obj.getAdjustedDuration()),
					obj.getId()});
	}

}
