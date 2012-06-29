package com.tms.collab.calendar.ui;

import com.tms.collab.calendar.model.*;
import kacang.Application;
import kacang.util.Log;
import kacang.services.security.SecurityService;
import kacang.services.security.SecurityException;
import kacang.ui.Widget;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.model.DataObjectNotFoundException;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Sep 29, 2003
 * Time: 3:23:21 PM
 * To change this template use Options | File Templates.
 */
public class GeneralEventView extends Widget implements CalendaringPermission
{
    private CalendarEvent event = null;
    private String userId = null;


    public GeneralEventView()
    {
    }

    public GeneralEventView(String s)
    {
        super(s);
    }

    public void onRequest(Event event)
    {
        super.onRequest(event);
        userId = getWidgetManager().getUser().getId();
        if(this.event!=null) {
            CalendarModule cm = (CalendarModule)Application.getInstance().getModule(CalendarModule.class);
            try
            {
                this.event = cm.getCalendarEvent(this.event.getEventId(),this.event.getInstanceId());
            } catch (DataObjectNotFoundException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            } catch (CalendarException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }

        }

    }

    public Forward actionPerformed(Event event)
    {
        String eventType = event.getRequest().getParameter(Event.PARAMETER_KEY_EVENT_TYPE);
        if(CalendarView.PARAMETER_KEY_EVENT_DELETE.equals(eventType)){
            return onEventDelete(event);
        } else if(CalendarView.PARAMETER_KEY_EVENT_DELETEALL.equals(eventType)){
            return onEventDeleteAll(event);
        }
        return super.actionPerformed(event);
    }

    public Forward onEventDelete(Event evt){
        CalendarModule cm = (CalendarModule)Application.getInstance().getModule(CalendarModule.class);
        try{
            if(event!=null){
                cm.deleteRecurringEvent(event);
            }else{
                String deleteId = evt.getRequest().getParameter(CalendarView.PARAMETER_KEY_EVENTID);
                if(deleteId!=null&&deleteId.trim().length()>0)
                    cm.deleteCalendarEvent(deleteId,userId);
            }
            String eId = event.getEventId();
            return new Forward(""+event.getEventId().substring(eId.lastIndexOf(".")+1,eId.indexOf("_"))+" deleted");
        } catch(Exception e){
            Log.getLog(getClass()).error(e);
        }
        return null;
    }

    public Forward onEventDeleteAll(Event evt){
        CalendarModule cm = (CalendarModule)Application.getInstance().getModule(CalendarModule.class);
        try{
            cm.deleteRecurringEvents(event);
            cm.deleteCalendarEvent(event.getEventId(),userId);
            String eId = event.getEventId();
            return new Forward(""+event.getEventId().substring(eId.lastIndexOf(".")+1,eId.indexOf("_"))+" deleted");
        }catch(Exception e){
            Log.getLog(getClass()).error(e);
        }
        return null;
    }
    public void init()
    {
        CalendarEventView cv = (CalendarEventView) getParent();
        event= cv.getEvent();
        String eventId = cv.getEventId();
        String instanceId = cv.getInstanceId();
        if(event==null||!event.getEventId().equals(eventId)||!event.getInstanceId().equals(instanceId)){

            userId = cv.getUserId();

            if(eventId!=null&&eventId.trim().length()>0){
                CalendarModule cm = (CalendarModule)Application.getInstance().getModule(CalendarModule.class);
                try
                {
                    event = cm.getCalendarEvent(eventId,instanceId);
                } catch (DataObjectNotFoundException e)
                {
                    Log.getLog(getClass()).error(e);
                } catch (CalendarException e)
                {
                    Log.getLog(getClass()).error(e);                 }
            }
        }
    }




    public String getDefaultTemplate()
    {
        return "calendar/generaleventview";
    }


    public CalendarEvent getEvent()
    {
        return event;
    }

    public void setEvent(CalendarEvent event)
    {
        this.event = event;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public boolean hasEditPermission(String userId)
    {
        if(event!=null&&event.getUserId().equals(userId))
            return true;
        SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
        try
        {
            if(ss.hasPermission(userId,CalendarModule.PERMISSION_MANAGE_EVENTS,null,null)
                    ||ss.hasPermission(userId,CalendarModule.PERMISSION_EDIT_EVENTS,null,null))
                return true;
        } catch (SecurityException e)
        {
            Log.getLog(getClass()).error(e);         }
        return false;
    }

    public boolean hasDeletePermission(String userId)
    {
        if(event!=null&&event.getUserId().equals(userId))
            return true;
        SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
        try
        {
            if(ss.hasPermission(userId,CalendarModule.PERMISSION_MANAGE_EVENTS,null,null)
                    ||ss.hasPermission(userId,CalendarModule.PERMISSION_DELETE_EVENTS,null,null))
                return true;
        } catch (SecurityException e)
        {
            Log.getLog(getClass()).error(e);
        }
        return false;
    }

    public boolean isEditable(){
        return hasEditPermission(getWidgetManager().getUser().getId());
    }

    public boolean isDeleteable()
    {
        return hasDeletePermission(getWidgetManager().getUser().getId());
    }

}
