package com.tms.collab.timesheet.model;

import kacang.model.DaoException;
import kacang.util.UuidGenerator;

import java.util.Calendar;

public class TimeSheetDaoOracle extends TimeSheetDao
{
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
