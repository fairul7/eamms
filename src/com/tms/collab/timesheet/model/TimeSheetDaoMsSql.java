package com.tms.collab.timesheet.model;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;

import kacang.model.DaoException;
import kacang.util.UuidGenerator;

public class TimeSheetDaoMsSql extends TimeSheetDao
{
	
	public void init() throws DaoException{
        
		try{
			super.init();
		}
		catch(Exception e){
			
		}
		
		try {
        super.update("CREATE TABLE timesheet ("+
                "id VARCHAR(255),"+
                "createdDateTime DATETIME,"+
                "projectId VARCHAR(255),"+
                "taskId VARCHAR(255),"+
                "createdBy VARCHAR(255),"+
                "tsDate DATETIME,"+
                "duration FLOAT,"+
                "description TEXT,"+
                "adjustment VARCHAR(1) DEFAULT 'N',"+
                "adjustmentDateTime DATETIME,"+
                "adjustmentBy VARCHAR(255),"+
                "adjustmentById VARCHAR(255),"+
                "adjustmentDescription TEXT,"+
                "adjustedDuration FLOAT, " +
                "taskTitle VARCHAR(255) DEFAULT '', " +
                "taskCategoryName VARCHAR(255) DEFAULT ''" + 
                ")",null);
        }catch(Exception e) {}
        
    }
	
	public Collection getTaskListByProjectReport(String projectId) throws DaoException {

        String sql = "SELECT DISTINCT(worms_milestone_task.taskId) as task " +
        		"FROM worms_milestone_task,worms_milestone,tm_task " +
        		"INNER JOIN cal_event ce ON (ce.eventId = tm_task.id)   " +
        		"LEFT JOIN timesheet ON tm_task.id=timesheet.taskId AND timesheet.projectId= ? " +
        		"WHERE worms_milestone.projectId=? " +
        		"AND worms_milestone.milestoneId=worms_milestone_task.milestoneId " +
        		"AND worms_milestone_task.taskId=tm_task.id " +
        		"ORDER BY worms_milestone_task.taskId";
        Collection col = super.select(sql,HashMap.class,new Object[]{projectId, projectId},0,-1);
        return col;
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
	    			"?, ?, ?, ?, ?, getDate())";

	    	String newId = UuidGenerator.getInstance().getUuid();
	    	String userId = getCurrentUserFromDWR().getId();

	    	super.update(sqlDelete, new Object[] {taskId, assigneeUserId});
	    	super.update(sqlInsert, new Object[] {newId, taskId, assigneeUserId, remarks, userId});
    	}
    	else {
    		throw new DaoException("Parameter taskId shouldn't be null");
    	}
    }
	
	public Collection getProjectTaskList(String projectId,String userid) throws DaoException {
        String sUser = (userid==null)?"":" and (tm_task.assignerId='"+userid+"' OR worms_project.ownerId='"+userid+"')";
        String sql = "SELECT DISTINCT(timesheet.taskId) as task FROM timesheet " +
        		"LEFT JOIN worms_project ON timesheet.projectId=worms_project.projectId  " +
        		"LEFT JOIN tm_task ON tm_task.id = timesheet.taskId "+
        		"WHERE timesheet.projectId=? "+sUser;
        		
        Collection col = super.select(sql,HashMap.class,new String[]{projectId},0,-1);
        return col;
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
