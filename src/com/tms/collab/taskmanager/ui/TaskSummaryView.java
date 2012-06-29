package com.tms.collab.taskmanager.ui;

import kacang.ui.Widget;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.util.Log;
import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.taskmanager.model.TaskManager;
import com.tms.collab.taskmanager.model.TaskMailer;
import com.tms.collab.taskmanager.model.Assignee;
import com.tms.collab.calendar.model.Attendee;
import com.tms.collab.calendar.model.CalendarException;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Nov 5, 2003
 * Time: 4:46:19 PM
 * To change this template use Options | File Templates.
 */
public class TaskSummaryView extends Widget
{
  //  private String taskId;
    private Task task;
    private FileListing fileListing;
    private Object view = null;


    public TaskSummaryView()
    {
    }

    public TaskSummaryView(String name)
    {
        super(name);
    }

    public void init()
    {
        super.init();
        if(task!=null){
            fileListing = new FileListing("TEST");
            fileListing.setFolderId("/"+TaskManager.TASKMANAGER_FILES_FOLDER+"/"+task.getId());
            fileListing.setDownloadable(true);
            fileListing.refresh();
            addChild(fileListing);
        }
    }

    public void onRequest(Event event)
    {
        super.onRequest(event);


    }

    public Forward actionPerformed(Event event)
    {
        super.actionPerformed(event);
        String eventType = event.getRequest().getParameter(Event.PARAMETER_KEY_EVENT_TYPE);
        String taskId = event.getRequest().getParameter("taskId");
        if("complete".equals(eventType)&&taskId!=null&&task.getId().equals(taskId)){
            TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
            try
            {
                tm.completeTask(task.getId(),getWidgetManager().getUser().getId());
                TaskMailer.sentTaskCompletedNotification(tm.getTask(task.getId()),event,getWidgetManager().getUser().getId());
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            } catch (DataObjectNotFoundException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
        }else if("start".equals(eventType)&&taskId!=null&&task.getId().equals(taskId)){
            TaskManager tm = (TaskManager) Application.getInstance().getModule(TaskManager.class);
            try
            {
                tm.startTask(taskId,getWidgetManager().getUser().getId());
            } catch (DataObjectNotFoundException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
        }else if("delete".equals(eventType)&&taskId!=null&&taskId.trim().length()>0){

            TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
            try
            {
                tm.deleteTask(taskId);
            } catch (CalendarException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
        }
        return null;
    }


    public String getDefaultTemplate()
    {
        return "taskmanager/tasksummary";
    }

    public Task getTask()
    {
        return task;
    }

    public void setTask(Task task)
    {
        this.task = task;
    }

    public FileListing getFileListing()
    {
        return fileListing;
    }

    public void setFileListing(FileListing fileListing)
    {
        this.fileListing = fileListing;
    }

    public Object getView()
    {
        return view;
    }

    public void setView(Object view)
    {
        this.view = view;
    }

    public boolean isReassignable()
    {
        if(task.isCompleted()||!task.isReassign())
            return false;
        Collection col = task.getAttendees();
        if(col!=null)
            for(Iterator i= col.iterator(); i.hasNext();){
                Attendee at = (Attendee)i.next();
                if(getWidgetManager().getUser().getId().equals(at.getUserId()))
                    return true;
            }

        return false;
    }

    public boolean isCompleteable(){
//        if(task.isCompleted())
//            return false;
        Collection col = task.getAttendees();
        if(col!=null)
            for(Iterator i= col.iterator(); i.hasNext();){
                Assignee at = (Assignee)i.next();
                if(getWidgetManager().getUser().getId().equals(at.getUserId())&&at.getTaskStatus()==Assignee.TASK_STATUS_IN_PROGRESS)
                    return true;
            }
        return false;
    }

    public Assignee getThisAssignee(){
        Collection col = task.getAttendees();
        if(col!=null)
            for(Iterator i= col.iterator(); i.hasNext();){
                Assignee at = (Assignee)i.next();
                if(getWidgetManager().getUser().getId().equals(at.getUserId()))
                    return at;
            }
        return null;
    }
}
