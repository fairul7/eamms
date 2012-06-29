package com.tms.collab.taskmanager.ui;

import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.taskmanager.model.TaskManager;
import com.tms.collab.taskmanager.model.Assignee;
import com.tms.collab.taskmanager.model.TaskMailer;
import com.tms.collab.calendar.ui.CalendarEventView;
import com.tms.collab.calendar.model.Attendee;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.services.security.*;
import kacang.services.security.SecurityException;
import kacang.util.Log;
import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;

import java.util.Iterator;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: Jun 8, 2005
 * Time: 10:58:59 AM
 * To change this template use File | Settings | File Templates.
 */   
public class MTaskEventView extends TaskEventView {
    private String userId;
    private String taskId;
    private Task task;
    private String editUrl = "taskeditform.jsp";
    private FileListing fileListing;
    public static final String EVENT_TYPE_SET = "set";
    public static final String EVENT_TYPE_START = "start";
    public static final String EVENT_TYPE_COMPLETE = "complete";
    public static final String EVENT_TYPE_DELETE = "delete";
    public static final String EVENT_TYPE_REASSIGN = "reassign";
    public static final String FORWARD_REASSIGN = "reassign";
    public static final String FORWARD_SET_PROGRESS ="setprogress";

    // add in for kurnia
    private boolean mobile;

    public MTaskEventView()
    {
    }

    public MTaskEventView(String name)
    {
        super(name);
    }

    public void onRequest(Event evt)
    {
        super.onRequest(evt);
        TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
        if(getParent() instanceof CalendarEventView){
            taskId =((CalendarEventView)getParent()).getEventId();
            userId = ((CalendarEventView)getParent()).getUserId();
        }
        if(userId==null||userId.trim().length()<=0)
            userId = getWidgetManager().getUser().getId();
        if(taskId!=null && taskId.trim().length()>0){
            fileListing.setFolderId("/"+TaskManager.TASKMANAGER_FILES_FOLDER+"/"+taskId);
            fileListing.refresh();
            try
            {
                task = tm.getTask(taskId/*,userId*/);
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            } catch (DataObjectNotFoundException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
        }

    }

    public Forward actionPerformed(Event evt)
    {
        try{
            String type = evt.getType();
            if(EVENT_TYPE_START.equals(type)){
                String taskId = evt.getRequest().getParameter("taskId");
                if(taskId!=null&&taskId.equals(this.taskId)){
                    String userId = getWidgetManager().getUser().getId();
                    for (Iterator iterator = task.getAttendees().iterator(); iterator.hasNext();)
                    {
                        Assignee assignee = (Assignee) iterator.next();
                        if(assignee.getUserId().equals(userId)&&assignee.getTaskStatus()==Assignee.TASK_STATUS_NOT_STARTED){
                            TaskManager tm = (TaskManager) Application.getInstance().getModule(TaskManager.class);
                            tm.startTask(taskId,assignee.getUserId());
                            task.setAttendees(tm.getAssignees(this.taskId));
                        }
                    }

                }
            }
            else if(EVENT_TYPE_DELETE.equals(type)){
                String taskId = evt.getRequest().getParameter("taskId");
                if(taskId!=null&&taskId.trim().length()>0){
                    TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
                    tm.deleteTask(taskId);
                    return new Forward("delete successful",null,false);
                }
            }else if(EVENT_TYPE_COMPLETE.equals(type)){
                if(task!=null){
                    TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
                    tm.completeTask(task.getId(),getWidgetManager().getUser().getId());
                    task = tm.getTask(task.getId());
                    TaskMailer.sentTaskCompletedNotification(task,evt,userId);
                }
            } else if(EVENT_TYPE_REASSIGN.equals(type)){
                evt.getRequest().setAttribute("id",taskId);
                return new Forward(FORWARD_REASSIGN);
            } else if(EVENT_TYPE_SET.equals(type)){
                String taskId = evt.getRequest().getParameter("taskId");
                String userId = evt.getRequest().getParameter("userId");
                evt.getRequest().setAttribute("taskId",taskId);
                evt.getRequest().setAttribute("userId",userId);
                return new Forward(FORWARD_SET_PROGRESS);
            }
        }catch(Exception e){
            Log.getLog(getClass()).error(e);
        }
        return super.actionPerformed(evt);
    }

    public void init()
    {
        super.init();
        fileListing = new FileListing("filelisting");
        fileListing.setDownloadable(true);
        TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
        if(getParent() instanceof CalendarEventView){
            taskId =((CalendarEventView)getParent()).getEventId();
            userId = ((CalendarEventView)getParent()).getUserId();
        }
        if(userId==null||userId.trim().length()<=0)
            userId = getWidgetManager().getUser().getId();
        if(taskId!=null && taskId.trim().length()>0){
            try
            {
                task = tm.getTask(taskId/*,userId*/);
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e.getMessage(),e);  //To change body of catch statement use Options | File Templates.
            } catch (DataObjectNotFoundException e)
            {
                Log.getLog(getClass()).error(e.getMessage(), e);  //To change body of catch statement use Options | File Templates.
            }
            fileListing.setFolderId(taskId);
        }
        fileListing.init();
        addChild(fileListing);

    }

    public String getDefaultTemplate()
    {
        return "taskmanager/taskeventview";
    }

    public boolean isCalendarEvent(){
        return (getParent() instanceof CalendarEventView);
    }

    public boolean hasEditPermission(String userId)
    {
/*        if(task.isCompleted())
            return false;*/
        if(task.getAssignerId().equals(userId))
            return true;
        SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
        try
        {
            if(ss.hasPermission(userId,TaskManager.PERMISSION_MANAGETASK,null,null)){
                return true;
            }
        } catch (kacang.services.security.SecurityException e)
        {
            Log.getLog(getClass()).error(e);
        }
        return false;
    }

    public boolean hasDeletePermission(String userId)
    {
        if(task.getAssignerId().equals(userId))
            return true;
        SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
        try
        {
            if(ss.hasPermission(userId,TaskManager.PERMISSION_MANAGETASK,null,null)){
                return true;
            }
        } catch (SecurityException e)
        {
            Log.getLog(getClass()).error(e);
        }
        return false;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getTaskId()
    {
        return taskId;
    }

    public void setTaskId(String taskId)
    {
        this.taskId = taskId;
    }


    public Task getTask()
    {
        return task;
    }

    public void setTask(Task task)
    {
        this.task = task;
    }

    public boolean isEditable(){
        return hasEditPermission(getWidgetManager().getUser().getId());
    }

    public boolean isDeletable(){
        return hasDeletePermission(getWidgetManager().getUser().getId());
    }

    public String getEditUrl()
    {
        return editUrl;
    }

    public void setEditUrl(String editUrl)
    {
        this.editUrl = editUrl;
    }

    public FileListing getFileListing()
    {
        return fileListing;
    }

    public void setFileListing(FileListing fileListing)
    {
        this.fileListing = fileListing;
    }

    public boolean isReassignable()
    {
        boolean reassignable = false;
        if(task!=null&&(!task.isCompleted()&&task.isReassign())){
            Collection col = task.getAttendees();
            if(col!=null){
                for(Iterator i= col.iterator(); i.hasNext();){
                    Attendee at = (Attendee)i.next();
                    if(getWidgetManager().getUser().getId().equals(at.getUserId())){
                        reassignable =  true;
                        break;
                    }
                }
            }
        }
        return reassignable;
    }

    //add in for kurnia mobile
    public boolean isMobile() {
        return mobile;
    }

    public void setMobile(boolean mobile) {
        this.mobile = mobile;
    }

}
