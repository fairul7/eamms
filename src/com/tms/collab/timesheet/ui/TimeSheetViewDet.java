package com.tms.collab.timesheet.ui;

import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.Application;

import java.util.Collection;
import java.util.Calendar;
import java.util.Iterator;

import com.tms.collab.timesheet.model.TimeSheetModule;
import com.tms.collab.timesheet.TimeSheetUtil;
import com.tms.collab.project.Project;
import com.tms.collab.project.WormsHandler;
import com.tms.collab.project.WormsUtil;
import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.taskmanager.model.TaskManager;
import com.tms.collab.taskmanager.model.Assignee;

/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: Apr 27, 2005
 * Time: 10:41:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class TimeSheetViewDet extends Form {
    private Collection ts;
    private String userId;
    private String taskId;
    private String projectId;
    private double totalHourSpent;
    private Project project;
    private Task task;
    private double totalHour;
    private boolean projectExist=false;

    public String getDefaultTemplate() {
        return "timesheet/tsviewdet";
    }

    public void onRequest(Event ev) {
        project=null;
        totalHourSpent=0;
        TimeSheetModule mod = (TimeSheetModule)Application.getInstance().getModule(TimeSheetModule.class);
        userId = getWidgetManager().getUser().getId();
        WormsHandler handler = (WormsHandler)Application.getInstance().getModule(WormsHandler.class);
        TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);

        try {
            ts = mod.list(userId,null,taskId);
            task = tm.getTask(taskId);

            for (Iterator i=task.getAttendees().iterator();i.hasNext();) {
                Assignee att = (Assignee)i.next();
                if (att.getUserId().equals(userId)) {
                    task.setProgress(new Integer(""+att.getProgress()).intValue());
                }
            }

            totalHourSpent = mod.getTotalHour(userId,null,taskId);
            totalHourSpent += mod.getTotalAdjustedHour(userId,null,taskId);
            project = handler.getProjectByTaskId(taskId);
            projectExist=true;
        }
        catch(Exception e) {
            try {
                if (project==null)
                    project = handler.getProjectByProjectName(taskId);
                projectExist=true;
            }
            catch(Exception we) {

            }
        }

        if (project==null) {
            // get general tag
            project = new Project();
            project.setProjectId(task.getCategoryId());
            project.setProjectName(task.getCategory());
            projectExist=false;
        }

        com.tms.collab.calendar.model.CalendarModule calMod = (com.tms.collab.calendar.model.CalendarModule)Application.getInstance().getModule(com.tms.collab.calendar.model.CalendarModule.class);
        try {
            com.tms.collab.calendar.model.CalendarEvent event = calMod.getCalendarEvent(task.getId());
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();

            if (event.getStartDate()!=null) {
                start.setTime(event.getStartDate());
            }
            if(event.getEndDate()!=null) {
                end.setTime(event.getEndDate());
            }
            if("Manhours".equals(task.getEstimationType()))
            totalHour=task.getEstimation();
            else
            totalHour=(task.getEstimation()*8);

        }
        catch(Exception e) {

        }

        initForm();
        super.onRequest(ev);
    }

    public void init() {
        super.init();
    }

    public void initForm() {

    }

    public Collection getTs() {
        return ts;
    }

    public void setTs(Collection ts) {
        this.ts = ts;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userid) {
        this.userId = userid;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public double getTotalHourSpent() {
        return totalHourSpent;
    }

    public void setTotalHourSpent(double totalHourSpent) {
        this.totalHourSpent = totalHourSpent;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return this.task;
    }

    public void setProjectId(String projectId) {
        this.projectId=projectId;
    }

    public String getProjectId() {
        return this.projectId;
    }

	public double getTotalHour() {
		return totalHour;
	}

	public void setTotalHour(double totalHour) {
		this.totalHour = totalHour;
	}
}
