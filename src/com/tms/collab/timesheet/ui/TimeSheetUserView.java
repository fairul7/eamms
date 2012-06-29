package com.tms.collab.timesheet.ui;

import kacang.stdui.Panel;
import kacang.ui.Event;
import kacang.Application;
import kacang.services.security.SecurityService;
import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.taskmanager.model.TaskManager;
import com.tms.collab.timesheet.model.TimeSheetModule;

import java.util.Collection;
import java.util.Iterator;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: Apr 27, 2005
 * Time: 5:59:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimeSheetUserView extends Panel {
    private String userid;
    private Task[] taskList;
    private Collection[] tsList;
    private double[] totalHourSpent;
    private String userName;

    public String getDefaultTemplate() {
        return "timesheet/tsuserview";
    }

    public void init() {
        super.init();
    }

    public void onRequest(Event ev) {
        if (userid==null || userid.equals("")) {
            userid = getWidgetManager().getUser().getId();
        }
        TimeSheetModule mod = (TimeSheetModule)Application.getInstance().getModule(TimeSheetModule.class);
        TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
        Collection col = mod.getTaskListByUser(userid);
        try {
            if (col!=null && col.size()>0) {
                taskList = new Task[col.size()];
                tsList = new Collection[col.size()];
                totalHourSpent = new double[col.size()];
                int iCounter = 0;
                for (Iterator i=col.iterator();i.hasNext();){
                    HashMap hm = (HashMap)i.next();
                    taskList[iCounter] = tm.getTask(hm.get("task").toString());
                    tsList[iCounter] = mod.list(userid,null,hm.get("task").toString());
                    totalHourSpent[iCounter] = mod.getTotalHour(userid,null,hm.get("task").toString());
                    totalHourSpent[iCounter] += mod.getTotalAdjustedHour(userid,null,hm.get("task").toString());
                    iCounter++;
                }
                SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
                userName = service.getUser(userid).getName();
            }
        }
        catch(Exception e) {
        }
        super.onRequest(ev);
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Task[] getTaskList() {
        return taskList;
    }

    public void setTaskList(Task[] taskList) {
        this.taskList = taskList;
    }

    public Collection[] getTsList() {
        return tsList;
    }

    public void setTsList(Collection[] tsList) {
        this.tsList = tsList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double[] getTotalHourSpent() {
        return totalHourSpent;
    }

    public void setTotalHourSpent(double[] totalHourSpent) {
        this.totalHourSpent = totalHourSpent;
    }
}
