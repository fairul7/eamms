package com.tms.collab.emeeting.ui;

import com.tms.collab.taskmanager.ui.TaskForm;
import com.tms.collab.taskmanager.model.TaskManager;
import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.emeeting.MeetingHandler;
import com.tms.collab.emeeting.AgendaItem;
import com.tms.collab.emeeting.Meeting;
import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.calendar.model.Attendee;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorIn;
import kacang.model.operator.DaoOperator;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.util.Log;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Jan 7, 2004
 * Time: 12:29:00 PM
 * To change this template use Options | File Templates.
 */
public class MeetingTaskForm extends TaskForm
{
    protected String itemTaskId;
    protected String itemId;

    public MeetingTaskForm()
    {
    }

    public MeetingTaskForm(String name)
    {
        super(name);
    }

    public void init()
    {
        if(!(itemId == null || "".equals(itemId)))
            super.init();
    }

    public Forward onSubmit(Event evt)
    {
        Forward forward = null;
        if(!(itemId == null || "".equals(itemId)))
            forward = super.onSubmit(evt);
        return forward;
    }

    public Forward onValidate(Event evt)
    {
        Forward forward = null;
        if(!(itemId == null || "".equals(itemId)))
        {
            if(taskId!=null&&taskId.trim().length()>0)
                setEdit(true);
            forward = super.onValidate(evt);
            //Assigning Task to Meeting
            MeetingHandler handler = (MeetingHandler) Application.getInstance().getModule(MeetingHandler.class);
            try
            {
                handler.assignTask(itemId, itemTaskId);
            }
            catch(Exception e)
            {
                Log.getLog(MeetingTaskForm.class).error(e);
            }
        }else{
            super.setEdit(true);
            forward = super.onValidate(evt);
        }
        return forward;
    }

    public String generateTaskId()
    {
        itemTaskId = super.generateTaskId();
        return itemTaskId;
    }

    public void setGeneratedTaskId(String taskId)
    {
        itemTaskId = taskId;
    }

/*
    public void onRequest(Event evt)
    {
        //
          //  super.onRequest();
        super.onRequest(evt);
        if(!(itemId == null || "".equals(itemId))){
            try{
            MeetingHandler handler = (MeetingHandler) Application.getInstance().getModule(MeetingHandler.class);
            AgendaItem item = handler.getAgendaItem(itemId);
            //Initializing Form
            if(item != null)
            {
                SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
                Meeting meeting = handler.getMeeting(item.getEventId(), true);
                CalendarEvent event = meeting.getEvent();
                DaoQuery query = new DaoQuery();
                query.addProperty(new OperatorIn("id", event.getAttendeeMap().keySet().toArray(), DaoOperator.OPERATOR_AND));
                Collection users = service.getUsers(query, 0, -1, null, false);
                Map map = new HashMap();
                Map rmap = getAssignees().getRightValues();
                for(Iterator i = users.iterator(); i.hasNext();)
                {
                    User user = (User) i.next();
                    map.put(user.getId(), user.getName());
                }
                for (Iterator iterator = rmap.keySet().iterator(); iterator.hasNext();)
                {
                    String id = (String) iterator.next();
                    map.remove(id);
                }
                getAssignees().setLeftValues(map);
               // getAssignees()
            }
        }catch(Exception e){
                Log.getLog(MeetingTaskForm.class).error(e);
            }
        }
    }
*/

    public void initForm()
    {
        init();
        try
        {
            MeetingHandler handler = (MeetingHandler) Application.getInstance().getModule(MeetingHandler.class);
            AgendaItem item = handler.getAgendaItem(itemId);
            //Initializing Form
            if(item != null)
            {
/*
                SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
                Meeting meeting = handler.getMeeting(item.getEventId(), true);
                CalendarEvent event = meeting.getEvent();
                DaoQuery query = new DaoQuery();
                query.addProperty(new OperatorIn("id", event.getAttendeeMap().keySet().toArray(), DaoOperator.OPERATOR_AND));
                Collection users = service.getUsers(query, 0, -1, null, false);
                Map map = new HashMap();
                for(Iterator i = users.iterator(); i.hasNext();)
                {
                    User user = (User) i.next();
                    map.put(user.getId(), user.getUsername());
                }
                getAssignees().setLeftValues(map);
                if(!(taskId == null || "".equals(taskId)))
                {
                    TaskManager manager = (TaskManager) Application.getInstance().getModule(TaskManager.class);
                    Task task = manager.getTask(getTaskId());
                    if(task != null)
                    {
                        Map rmap = new HashMap();
                        for (Iterator iterator = task.getAttendeeMap().keySet().iterator(); iterator.hasNext();)
                        {
                              String id = (String) iterator.next();
                              rmap.put(id,((Attendee)task.getAttendeeMap().get(id)).getName());
                              getAssignees().getLeftValues().remove(id);
                        }
                        getAssignees().setRightValues(task.getAttendeeMap());
                    }
                }
                else
*/
                    description.setValue(item.getAction());
            }
        }
        catch(Exception e)
        {
            Log.getLog(MeetingTaskForm.class).error(e);
        }
    }

    public String getItemTaskId()
    {
        return itemTaskId;
    }

    public void setItemTaskId(String itemTaskId)
    {
        this.itemTaskId = itemTaskId;
    }

    public String getItemId()
    {
        return itemId;
    }

    public void setItemId(String itemId)
    {
        if(!(itemId == null || "".equals(itemId)))
        {
            this.itemId = itemId;
            initForm();
        }
    }
}
