package com.tms.collab.taskmanager.model;

import kacang.model.DefaultDataObject;
import kacang.services.security.SecurityException;
import kacang.util.Log;

import java.util.Date;

import com.tms.collab.calendar.ui.UserUtil;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Oct 22, 2003
 * Time: 12:23:53 PM
 * To change this template use Options | File Templates.
 */
public class Reassignment extends DefaultDataObject
{
    private Date reassignmentDate;
    private String assignerId;
    private String assigneeId;

    public Date getReassignmentDate()
    {
        return reassignmentDate;
    }

    public void setReassignmentDate(Date reassignmentDate)
    {
        this.reassignmentDate = reassignmentDate;
    }

    public String getAssignerId()
    {
        return assignerId;
    }

    public void setAssignerId(String assignerId)
    {
        this.assignerId = assignerId;
    }

    public String getAssigneeId()
    {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId)
    {
        this.assigneeId = assigneeId;
    }

    public String getAssignerName()
    {
        try
        {
            return UserUtil.getUser(assignerId).getName();
        } catch (SecurityException e)
        {
            Log.getLog(Reassignment.class).error(e);
        }
        return null;
    }

    public String getAssigneeName()
    {
        try
        {
            return UserUtil.getUser(assigneeId).getName();
        } catch (SecurityException e)
        {
            Log.getLog(Reassignment.class).error(e);
        }
        return null;
    }
}
