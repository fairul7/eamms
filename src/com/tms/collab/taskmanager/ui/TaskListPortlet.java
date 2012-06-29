package com.tms.collab.taskmanager.ui;

import kacang.stdui.Form;
import kacang.stdui.Button;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.util.Log;
import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;

import java.util.*;

import com.tms.collab.taskmanager.model.TaskManager;
import com.tms.collab.taskmanager.model.TaskMailer;
import com.tms.collab.taskmanager.model.Task;
import org.apache.commons.collections.SequencedHashMap;

public class TaskListPortlet extends Form
{
    private Collection tasks;
    private Button viewAllButton;
	private Map map;

    public TaskListPortlet()
    {
    }

    public TaskListPortlet(String name)
    {
        super(name);
    }

    public String getDefaultTemplate()
    {
        return "taskmanager/tasklistportlet";
    }

    public void init()
    {
        super.init();
        viewAllButton = new Button("viewallbutton");
        viewAllButton.setText(Application.getInstance().getMessage("com.tms.collab.taskmanager.label.ViewAll","View All"));
        addChild(viewAllButton);
    }

    public void onRequest(Event evt)
    {
        super.onRequest(evt);
        refresh();
    }

    public void refresh(){
        String userId = getWidgetManager().getUser().getId();
        TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH,1);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        Date from = cal.getTime();
        try
        {
            tasks = tm.getCalendarTasks(from,new String[]{userId},true);
			map = new SequencedHashMap();
            for (Iterator i = tasks.iterator(); i.hasNext();)
			{
				Task task = (Task) i.next();
				String archived=(String)task.getProperty("projectArchived");
				if(archived==null||"0".equals(archived)){
				Collection collection = new ArrayList();
				if(map.containsKey(task.getCategory()))
                    collection = (Collection) map.get(task.getCategory());
				collection.add(task);
				map.put(task.getCategory(), collection);
				}
			}
        }
		catch (DaoException e)
        {
            Log.getLog(getClass()).error("Error while retrieving user tasks", e);
        }
    }

    public Collection getTasks()
    {
        return tasks;
    }

    public void setTasks(Collection tasks)
    {
        this.tasks = tasks;
    }

    public Button getViewAllButton()
    {
        return viewAllButton;
    }

    public void setViewAllButton(Button viewAllButton)
    {
        this.viewAllButton = viewAllButton;
    }
    public Forward actionPerformed(Event evt)
    {
        String buttonClicked = findButtonClicked(evt);
        if(viewAllButton.getAbsoluteName().equals(buttonClicked)){
            return new Forward("all");
        }
        String type = evt.getType();
        if(TaskListing.PARAMETER_EVENT_COMPLETE.equals(type)){
            String taskId = evt.getRequest().getParameter(TaskListing.PARAMETER_EVENT_COMPLETE_TASKID);
            if(taskId!=null&&taskId.trim().length()>0){
                TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
                try
                {
                    tm.completeTask(taskId,getWidgetManager().getUser().getId());
                    Task task = tm.getTask(taskId);
                    if(task.isCompleted())
                        TaskMailer.sentTaskCompletedNotification(task,evt,getWidgetManager().getUser().getId());
                } catch (DaoException e)
                {
                    Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                } catch (DataObjectNotFoundException e)
                {
                    Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                }
                refresh();
            }
        }
        return null;
    }

    public Forward onValidate(Event evt)
    {

        return super.onValidate(evt);
    }

	public Map getMap()
	{
		return map;
	}

	public void setMap(Map map)
	{
		this.map = map;
	}
}
