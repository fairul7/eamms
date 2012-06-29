package com.tms.collab.taskmanager.model;

import com.tms.collab.calendar.model.Attendee;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Oct 9, 2003
 * Time: 6:47:06 PM
 * To change this template use Options | File Templates.
 */
public class Assignee extends Attendee
{
    private Date startDate;
    private Date completeDate;
    private long progress;
    private long taskStatus;
    public static final int TASK_STATUS_NOT_STARTED = 0;
    public static final int TASK_STATUS_IN_PROGRESS = 1;
    public static final int TASK_STATUS_COMPLETED = 2;

    // added for keep track on who's the one who set the completed date
    private String updateBy;
    private Date updateDate;
    private String completedSetBy;
    private Date completedDateSetOn;

    public Assignee()
    {
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Date getCompleteDate()
    {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate)
    {
        this.completeDate = completeDate;
    }



    public void setProgress(Integer progress){
        if(progress==null)
            this.progress = 0;
        else
            this.progress = progress.intValue();
    }

    public long getProgress()
    {
        return progress;
    }

    public void setProgress(long progress)
    {
        this.progress = progress;
    }

    public long getTaskStatus()
    {
        return taskStatus;
    }

    public void setTaskStatus(long taskStatus)
    {
        this.taskStatus = taskStatus;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getCompletedSetBy() {
        return completedSetBy;
    }

    public void setCompletedSetBy(String completedSetBy) {
        this.completedSetBy = completedSetBy;
    }

    public Date getCompletedDateSetOn() {
        return completedDateSetOn;
    }

    public void setCompletedDateSetOn(Date completedDateSetOn) {
        this.completedDateSetOn = completedDateSetOn;
    }
}
