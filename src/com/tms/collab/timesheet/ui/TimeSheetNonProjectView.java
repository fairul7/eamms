package com.tms.collab.timesheet.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.util.Log;

import com.tms.collab.project.Project;
import com.tms.collab.project.WormsHandler;
import com.tms.collab.project.WormsUtil;
import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.taskmanager.model.TaskCategory;
import com.tms.collab.taskmanager.model.TaskManager;
import com.tms.collab.timesheet.model.TimeSheetModule;

public class TimeSheetNonProjectView extends Form {
    protected String projectId;
    protected Collection taskList;
    protected Task[] task;
    protected Task[] noTSTask;
    protected TaskCategory category;
    protected double totalHourSpent;
    protected double estimatedHourSpent;
    protected boolean print=false;
    private static final String TIMESHEET_PERMISSION="com.tms.collab.timesheet.ViewProjects";

    public String getDefaultTemplate() {
        return "timesheet/tsnonprojectview";
    }

    public void init() {
        removeChildren();
        super.init();
    }

    public void onRequest(Event ev) {
        task=null;
        noTSTask=null;
        totalHourSpent=0;
        estimatedHourSpent=0;int i=0;
        TimeSheetModule mod = (TimeSheetModule)Application.getInstance().getModule(TimeSheetModule.class);
        TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
        WormsHandler wm = (WormsHandler)Application.getInstance().getModule(WormsHandler.class);

        try {
        	category = tm.getCategory(projectId);
        	Collection noTSCol = new ArrayList();
        	 String userId = getWidgetManager().getUser().getId();
        	 SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
             boolean hasPermission = security.hasPermission(userId, TIMESHEET_PERMISSION, TimeSheetModule.class.getName(), null);
             if (hasPermission) {
            	 taskList = mod.getNonProjectTaskList(projectId,null); 
            	 noTSCol = mod.getNoTimeSheetNonProjectTaskList(projectId,null);
            	 totalHourSpent = mod.getTotalHourSpentForNonProject(projectId,"");
             }else{
            	 taskList = mod.getNonProjectTaskList(projectId,userId);  
            	 noTSCol = mod.getNoTimeSheetNonProjectTaskList(projectId,userId);
            	 totalHourSpent = mod.getTotalHourSpentForNonProject(projectId,userId);
             }
            // get task list in timesheet
            

            if (taskList!=null && taskList.size()>0) {
                task = new Task[taskList.size()];
                int iCounter = 0;
                double estimate=0;
                for (Iterator j=taskList.iterator();j.hasNext();) {
                    HashMap hm = (HashMap)j.next();
                    task[iCounter] = tm.getTask((String)hm.get("task"));
                    Collection assigneeList = task[iCounter].getAttendees();
                        estimate = task[iCounter].getEstimation()*task[iCounter].getAttendees().size();

                    if("Manhours".equals(task[iCounter].getEstimationType()))    
                    estimatedHourSpent += estimate;
                    else
                    estimatedHourSpent += estimate*8;
                    iCounter++;
                }
            }

            if (noTSCol!=null && noTSCol.size()>0) {
                noTSTask = new Task[noTSCol.size()];
                
                double estimate=0;
                for (Iterator iterator=noTSCol.iterator();iterator.hasNext();) {
                    HashMap map = (HashMap)iterator.next();
                    noTSTask[i] = tm.getTask((String)map.get("taskId"));
                    if(i==877){
                    	String a=(String)map.get("taskId");
                    	System.out.println("Taskid="+a);
                    }
                    if(noTSTask[i].getAttendees()==null){
                    	noTSTask[i].setAttendees(new ArrayList());
                    }
                  
                    Collection assigneeList = noTSTask[i].getAttendees();
                    estimate = noTSTask[i].getEstimation()*noTSTask[i].getAttendees().size();
                    if("Manhours".equals(noTSTask[i].getEstimationType()))    
                        estimatedHourSpent += estimate;
                    else
                       estimatedHourSpent += estimate*8;
                    i++;
                }
            }

        }
        catch(Exception e) {
        Log.getLog(getClass()).error(e.toString(), e);
         System.out.println("error at"+i);  
        }

        super.onRequest(ev);
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Task[] getTask() {
        return task;
    }

    public void setTask(Task[] task) {
        this.task = task;
    }
    public TaskCategory getCategory() {
		return category;
	}

	public void setCategory(TaskCategory category) {
		this.category = category;
	}

	public void setTotalHourSpent(double totalHourSpent) {
        this.totalHourSpent = totalHourSpent;
    }

    public double getTotalHourSpent() {
        return totalHourSpent;
    }

    public void setEstimatedHourSpent(double estimatedHourSpent) {
        this.estimatedHourSpent = estimatedHourSpent;
    }

    public double getEstimatedHourSpent() {
        return estimatedHourSpent;
    }

    public void setNoTSTask(Task[] noTSTask) {
        this.noTSTask = noTSTask;
    }

    public Task[] getNoTSTask() {
        return noTSTask;
    }

    public boolean isPrint() {
        return print;
    }

    public void setPrint(boolean print) {
        this.print = print;
    }
}

