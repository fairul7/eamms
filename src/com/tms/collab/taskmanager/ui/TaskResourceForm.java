package com.tms.collab.taskmanager.ui;

import kacang.stdui.Form;
import kacang.stdui.Button;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.services.security.SecurityException;
import kacang.util.Log;
import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;

import java.util.*;

import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.taskmanager.model.TaskManager;
import com.tms.collab.resourcemanager.model.ResourceManager;
import com.tms.collab.resourcemanager.model.ResourceMailer;
import com.tms.collab.calendar.model.CalendarModule;
import com.tms.collab.calendar.model.ConflictException;
import com.tms.collab.calendar.model.CalendarEvent;

import javax.mail.internet.AddressException;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Mar 22, 2004
 * Time: 3:08:50 PM
 * To change this template use Options | File Templates.
 */
public class TaskResourceForm extends Form
{
    public static final String FORWARD_RESOURCES_BOOKED = "resources booked";
    public static final String FORWARD_RESOURCES_CONFLICT = "resources conflict";
    public static final String FORWARD_VIEW_CONFLICTS = "view conflicts";
    public static final String FORWARD_CANCEL ="cancel";
    private String taskId;
    private Task task;
    private Map resourceMap,sDateMap,eDateMap;
    private Collection resources,invalidDates,resourcesIds;
    private Button bookButton, cancelButton;
    private boolean edit;
    public TaskResourceForm()
    {
        resources = new TreeSet();

    }

    public TaskResourceForm(String s)
    {
        super(s);
        resources = new TreeSet();
    }

    public void init()
    {
        super.init();
        bookButton = new Button("bookButton",Application.getInstance().getMessage("taskmanager.label.Book","Book"));
        addChild(bookButton);
        cancelButton = new Button("cancelButton",Application.getInstance().getMessage("taskmanager.label.Cancel","Cancel"));
        addChild(cancelButton);
        setMethod("POST");
    }

    public void onRequest(Event event)
    {
        super.onRequest(event);
        if(taskId!=null&&taskId.trim().length()>0){
            TaskManager tm = (TaskManager) Application.getInstance().getModule(TaskManager.class);
            try
            {
                task = tm.getTask(taskId);
                resourceMap = (Map)event.getRequest().getSession().getAttribute("resources");
                edit = ((Boolean)event.getRequest().getSession().getAttribute("EDIT")).booleanValue();

               // event.getRequest().getSession().removeAttribute("resources");
               /* for (Iterator iterator = resourceMap.keySet().iterator(); iterator.hasNext();)
                {
                    String resourceId = (String) iterator.next();

                }*/
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            } catch (DataObjectNotFoundException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
        }

    }

    public Forward actionPerformed(Event event)
    {
        if("viewConflict".equals(event.getType())){
            String resourceId = event.getRequest().getParameter("resourceId");
            if(resourceId!=null&&resourceId.trim().length()>0){
                Map map = (Map)event.getRequest().getSession().getAttribute("conflictMap");
                event.getRequest().getSession().setAttribute("conflicts",map.get(resourceId));
                return new Forward(FORWARD_VIEW_CONFLICTS);
            }
        }
        return super.actionPerformed(event);
    }

    private Date getStartTime(String resourceId,Event event){
        String dayStr = event.getRequest().getParameter(resourceId+"*sdate");
        String monthStr = event.getRequest().getParameter(resourceId+"*smonth");
        String yearStr = event.getRequest().getParameter(resourceId+"*syear");
        String hourStr = event.getRequest().getParameter(resourceId+"*shour");
        String minuteStr = event.getRequest().getParameter(resourceId+"*sminute");
        if(dayStr==null||monthStr==null||yearStr==null||hourStr==null||minuteStr==null||yearStr.trim().length()==0)
            return null;
        int day = Integer.parseInt(dayStr);
        int month = Integer.parseInt(monthStr);
        int year = Integer.parseInt(yearStr);
        int minute = Integer.parseInt(minuteStr);
        int hour = Integer.parseInt(hourStr);
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setLenient(false);
        cal.set(year,month,day,hour,minute);
        return cal.getTime();
    }

    private Date getEndTime(String resourceId,Event event){
        String dayStr = event.getRequest().getParameter(resourceId+"*edate");
        String monthStr = event.getRequest().getParameter(resourceId+"*emonth");
        String yearStr = event.getRequest().getParameter(resourceId+"*eyear");
        String hourStr = event.getRequest().getParameter(resourceId+"*ehour");
        String minuteStr = event.getRequest().getParameter(resourceId+"*eminute");
        if(dayStr==null||monthStr==null||yearStr==null||hourStr==null||minuteStr==null||yearStr.trim().length()==0)
            return null;
        int day = Integer.parseInt(dayStr);
        int month = Integer.parseInt(monthStr);
        int year = Integer.parseInt(yearStr);
        int minute = Integer.parseInt(minuteStr);
        int hour = Integer.parseInt(hourStr);
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setLenient(false);
        cal.set(year,month,day,hour,minute);
        return cal.getTime();
    }


    public Forward onValidate(Event event)
    {
        if(bookButton.getAbsoluteName().equals(findButtonClicked(event))){
            ResourceManager rm = (ResourceManager) Application.getInstance().getModule(ResourceManager.class);

            Map conflictMap = (Map)event.getRequest().getSession().getAttribute("conflictMap");
            if(conflictMap == null){
                conflictMap = new HashMap();
                for (Iterator iterator = resourceMap.keySet().iterator(); iterator.hasNext();)
                {
                    String resourceId = (String) iterator.next();
                    Collection col = rm.getBookingConflictEvents(resourceId,(Date)sDateMap.get(resourceId),(Date)eDateMap.get(resourceId));
                    if(col!=null){
                        for (Iterator cIterator = col.iterator(); cIterator.hasNext();)
                        {
                            CalendarEvent cEvent = (CalendarEvent) cIterator.next();
                            if(cEvent.getEventId().equals(taskId))
                                cIterator.remove();
                        }
                        if( col.size()>0){
                            conflictMap.put(resourceId, col);
                        }
                    }
                }
                if(conflictMap.size()>0){
                    event.getRequest().getSession().setAttribute("conflictMap",conflictMap);
                    return new Forward(FORWARD_RESOURCES_CONFLICT);
                }else{
                    try
                    {
                        rm.deleteBooking(taskId);
                        for (Iterator iterator = resourceMap.keySet().iterator(); iterator.hasNext();)
                        {
                            String resourceId = (String) iterator.next();
                            rm.bookResource(resourceId,taskId,CalendarModule.DEFAULT_INSTANCE_ID,getWidgetManager().getUser().getId(),getStartTime(resourceId,event),getEndTime(resourceId,event),true);
                        }
                        event.getRequest().getSession().removeAttribute("conflictMap");
                        event.getRequest().getSession().removeAttribute("resources");
                        TaskManager tm = (TaskManager) Application.getInstance().getModule(TaskManager.class);
                        try
                        {
                            ResourceMailer.sendResourceApprovalMail(getWidgetManager().getUser().getId(),tm.getTask(taskId),event);
                        } catch (SecurityException e)
                        {
                            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                        } catch (AddressException e)
                        {
                            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                        } catch (DaoException e)
                        {
                            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                        } catch (DataObjectNotFoundException e)
                        {
                            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                        }
                        return new Forward(FORWARD_RESOURCES_BOOKED);
                    } catch (ConflictException e)
                    {
                        Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                    } catch (DaoException e)
                    {
                        Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                    }
                }
            }else{
                try
                {
                    rm.deleteBooking(taskId);
                    for (Iterator iterator = resourceMap.keySet().iterator(); iterator.hasNext();)
                    {
                        String resourceId = (String) iterator.next();
                        if(!conflictMap.containsKey(resourceId)||containsId(resourceId))
                            rm.bookResource(resourceId,taskId,CalendarModule.DEFAULT_INSTANCE_ID,getWidgetManager().getUser().getId(),getStartTime(resourceId,event),getEndTime(resourceId,event),true);
                    }
                    event.getRequest().getSession().removeAttribute("conflictMap");
                    event.getRequest().getSession().removeAttribute("resources");
                    TaskManager tm = (TaskManager) Application.getInstance().getModule(TaskManager.class);
                    try
                    {
                        ResourceMailer.sendResourceApprovalMail(getWidgetManager().getUser().getId(),tm.getTask(taskId),event);
                    } catch (SecurityException e)
                    {
                        Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                    } catch (AddressException e)
                    {
                        Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                    } catch (DaoException e)
                    {
                        Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                    } catch (DataObjectNotFoundException e)
                    {
                        Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                    }
                    return new Forward(FORWARD_RESOURCES_BOOKED);
                } catch (ConflictException e)
                {
                    Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                } catch (DaoException e)
                {
                    Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                }
            }
        }

        return super.onValidate(event);
    }
    private boolean containsId(String resourceId){
        for (Iterator iterator = resourcesIds.iterator(); iterator.hasNext();)
        {
            String id = (String) iterator.next();
            if(id.equals(resourceId))
                return true;
        }
        return false;
    }

    public String getDefaultTemplate()
    {
        return "taskmanager/taskresourceform";
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

    public Map getResourceMap()
    {
        return resourceMap;
    }

    public void setResourceMap(Map resourceMap)
    {
        this.resourceMap = resourceMap;
    }

    public Collection getResources()
    {
        return resources;
    }

    public void setResources(Collection resources)
    {
        this.resources = resources;
    }

    public Button getBookButton()
    {
        return bookButton;
    }

    public void setBookButton(Button bookButton)
    {
        this.bookButton = bookButton;
    }

    public Button getCancelButton()
    {
        return cancelButton;
    }

    public void setCancelButton(Button cancelButton)
    {
        this.cancelButton = cancelButton;
    }

    public Forward onSubmit(Event event)
    {
        if(cancelButton.getAbsoluteName().equals(findButtonClicked(event)))
            return new Forward(FORWARD_CANCEL);
        String [] ids  = event.getParameterValues("resourcesCB");
        if(ids!=null){
            for (int i = 0; i < ids.length; i++)
            {
                String id = ids[i];
                if(resourcesIds == null)
                    resourcesIds =  new TreeSet();
                resourcesIds.add(id);
            }
        }
        sDateMap = new HashMap();
        eDateMap = new HashMap();
        invalidDates = new TreeSet();
        for (Iterator iterator = resourceMap.keySet().iterator(); iterator.hasNext();)
        {
            String resourceId = (String) iterator.next();
            Date sdate = getStartTime(resourceId,event);
            Date edate = getEndTime(resourceId,event);
            if(sdate==null||edate==null||edate.before(sdate))
            {
                invalidDates.add(resourceId);
            }
            sDateMap.put(resourceId, sdate);
            eDateMap.put(resourceId,edate);
        }

        if(invalidDates.size()>0)
            setInvalid(true);

        return super.onSubmit(event);
    }

    public Map getsDateMap()
    {
        return sDateMap;
    }

    public void setsDateMap(Map sDateMap)
    {
        this.sDateMap = sDateMap;
    }

    public Map geteDateMap()
    {
        return eDateMap;
    }

    public void seteDateMap(Map eDateMap)
    {
        this.eDateMap = eDateMap;
    }

    public Collection getInvalidDates()
    {
        return invalidDates;
    }

    public void setInvalidDates(Collection invalidDates)
    {
        this.invalidDates = invalidDates;
    }

    public Collection getResourcesIds()
    {
        return resourcesIds;
    }

    public void setResourcesIds(Collection resourcesIds)
    {
        this.resourcesIds = resourcesIds;
    }

    public boolean isEdit()
    {
        return edit;
    }

    public void setEdit(boolean edit)
    {
        this.edit = edit;
    }

}
