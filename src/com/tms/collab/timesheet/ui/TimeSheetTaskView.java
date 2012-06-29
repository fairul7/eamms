package com.tms.collab.timesheet.ui;

import kacang.stdui.Panel;
import kacang.ui.Event;
import kacang.Application;
import kacang.services.security.SecurityService;

import java.util.*;

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
 * Time: 2:46:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimeSheetTaskView extends Panel {
    private Collection userList;
    private Collection[] tsList;
    private double[] totalHourSpent;
    private double totalHourSpentForTask;
    private double[] totalHour;
    private String taskId;
    private Task task;
    private Project project;
    private String[] useridList;
    private boolean header=true;
    private boolean print=false;
    private int totalAssignee;
    private long[] progress;

    public String getDefaultTemplate() {
        return "timesheet/tstaskview";
    }

    public void init() {
        super.init();
    }

    public void onRequest(Event ev) {
        super.onRequest(ev);
    }

    public void loadTask() {
        tsList=null;
        totalHourSpent = new double[]{0};
        totalHourSpentForTask=0;
        progress = new long[]{0};

        if (taskId!=null && !taskId.equals("")) {
            TimeSheetModule mod = (TimeSheetModule)Application.getInstance().getModule(TimeSheetModule.class);
            WormsHandler wm = (WormsHandler)Application.getInstance().getModule(WormsHandler.class);
            TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);

            try {
                task = tm.getTask(taskId);
                project = wm.getProjectByTaskId(taskId);
            }
            catch(Exception e) {
                try {
                    if (project==null)
                        project = wm.getProjectByProjectName(taskId);
                }
                catch(Exception we) {
                }
            }
            
            com.tms.collab.calendar.model.CalendarModule calMod = (com.tms.collab.calendar.model.CalendarModule)Application.getInstance().getModule(com.tms.collab.calendar.model.CalendarModule.class);
            Collection assigneeList = task.getAttendees();
                if (assigneeList!=null && assigneeList.size()>0) {
                    userList = assigneeList;
                }
            //userList = mod.getUserListByTask(taskId);

            if (userList!=null && userList.size()>0) {
                tsList = new Collection[userList.size()];
                totalHourSpent = new double[userList.size()];
                useridList = new String[userList.size()];
                totalHour = new double[userList.size()];
                progress = new long[userList.size()];

                int iCounter=0;
                for (Iterator i=userList.iterator();i.hasNext();) {
                    //HashMap hm = (HashMap)i.next();
                    //String s = (String)hm.get("userid");
                    Assignee a = (Assignee)i.next();
                    String s = a.getUserId();

                    try {
                        // set progress
                        Assignee as = tm.getAssignee(taskId,s);

                        progress[iCounter] = as.getProgress();
                        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
                        useridList[iCounter] = service.getUser(s).getName();//(String)hm.get("userid");//String)i.next());
                        com.tms.collab.calendar.model.CalendarEvent event = calMod.getCalendarEvent(taskId);
                        Calendar start = Calendar.getInstance();
                        Calendar end = Calendar.getInstance();
                        if (event.getStartDate()!=null)
                            start.setTime(event.getStartDate());
                        if (event.getEndDate()!=null)
                            end.setTime(event.getEndDate());
                    
                        int iTotalDay = 0;
                        if (project!=null) {
                            iTotalDay = WormsUtil.getWorkingDays(project.getProjectWorking(), start.getTime(), end.getTime()) ;
                        }
                        else {
                            iTotalDay = WormsUtil.getWorkingDays(TimeSheetUtil.getWorkingDays(), start.getTime(), end.getTime()) ;
                        }
                        if (iTotalDay==0) {
                            totalHour[iCounter] = end.get(Calendar.HOUR)-start.get(Calendar.HOUR);
                        }
                        else
                            totalHour[iCounter] = iTotalDay * TimeSheetUtil.WORKING_HOUR_PER_DAY;
                    }
                    catch(Exception e) {
                    }
                    tsList[iCounter] = mod.list(s,null,taskId);
                    totalHourSpent[iCounter] = mod.getTotalHour(s,null,taskId);
                    totalHourSpent[iCounter] += mod.getTotalAdjustedHour(s,null,taskId);

                    // get total hour spent for task (from time spent by all user)
                    totalHourSpentForTask += totalHourSpent[iCounter];

                    iCounter++;
                }
            }  // if

        }
        totalAssignee=userList.size();


    }

    public Collection getUserList() {
        return userList;
    }

    public void setUserList(Collection userList) {
        this.userList = userList;
    }

    public Collection[] getTsList() {
        return tsList;
    }

    public void setTsList(Collection[] tsList) {
        this.tsList = tsList;
    }

    public double[] getTotalHourSpent() {
        return totalHourSpent;
    }

    public void setTotalHourSpent(double[] totalHourSpent) {
        this.totalHourSpent = totalHourSpent;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
        loadTask();
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setUseridList(String[] useridList) {
        this.useridList = useridList;
    }

    public String[] getUseridList() {
        return this.useridList;
    }

    public boolean isHeader() {
        return header;
    }

    public void setHeader(boolean header) {
        this.header = header;
    }

    public void setTotalHour(double[] totalHour) {
        this.totalHour = totalHour;
    }

    public double[] getTotalHour() {
        return totalHour;
    }

    public void setTotalHourSpentForTask(double totalHourSpentForTask) {
        this.totalHourSpentForTask = totalHourSpentForTask;
    }

    public double getTotalHourSpentForTask() {
        return totalHourSpentForTask;
    }

    public void setProgress(long[] progress) {
        this.progress=progress;
    }

    public long[] getProgress() {
        return progress;
    }

    public boolean isPrint() {
        return print;
    }

    public void setPrint(boolean print) {
        this.print = print;
    }

	public int getTotalAssignee() {
		return totalAssignee;
	}

	public void setTotalAssignee(int totalAssignee) {
		this.totalAssignee = totalAssignee;
	}
    
}
