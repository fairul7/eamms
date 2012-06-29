package com.tms.collab.calendar.ui;

import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;
import kacang.util.Log;
import kacang.model.DataObjectNotFoundException;
import com.tms.collab.calendar.model.CalendarModule;
import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.calendar.model.CalendarException;


/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Dec 10, 2003
 * Time: 2:24:55 PM
 * To change this template use Options | File Templates.
 */
public class AppointmentView extends CalendarEventView
{

    public AppointmentView()
    {
    }

    public AppointmentView(String name)
    {
        super(name);
    }


    public String getDefaultTemplate()
    {
        return "calendar/appointmentview";
    }

    public Forward actionPerformed(Event evt)
    {
        String eventType = evt.getRequest().getParameter(Event.PARAMETER_KEY_EVENT_TYPE);
        if( CalendarView.PARAMETER_KEY_EVENT_EDIT.equals(eventType)){
            return onEventEdit(evt);
        } else if(CalendarView.PARAMETER_KEY_EVENT_DELETE.equals(eventType)){
            return onEventDelete(evt);
        } else if(CalendarView.PARAMETER_KEY_EVENT_DELETEALL.equals(eventType)){
            return onEventDeleteAll(evt);
        }
        return super.actionPerformed(evt);
    }

    public Forward onEventEdit(Event evt){
        try{
            /*evt.getResponse().sendRedirect(editUrl+"?eventId="+evt.getRequest().getParameter(PARAMETER_KEY_EVENTID)+
            "&viewUrl="+evt.getRequest().getRequestURI());*/
            setState(CalendarEventView.EDIT);
            setEventId(evt.getRequest().getParameter(CalendarView.PARAMETER_KEY_EVENTID));
        }catch(Exception e){}
        return null;
    }

    public Forward onEventDelete(Event evt){
        CalendarModule cm = (CalendarModule)Application.getInstance().getModule(CalendarModule.class);
        CalendarEvent e = null;
        try
        {
            e = cm.getCalendarEvent(eventId, instanceId);
            cm.deleteRecurringEvent(e);
            return new Forward("deleted");
        } catch (DataObjectNotFoundException e1)
        {
            Log.getLog(getClass()).error(e1);  //To change body of catch statement use Options | File Templates.
        } catch (CalendarException e1)
        {
            Log.getLog(getClass()).error(e1);  //To change body of catch statement use Options | File Templates.
        }
        return null;
    }

    public Forward onEventDeleteAll(Event evt){
        CalendarModule cm = (CalendarModule)Application.getInstance().getModule(CalendarModule.class);
        try
        {
            cm.deleteCalendarEvent(eventId,getWidgetManager().getUser().getId());
            return new Forward("deleted");
        } catch (DataObjectNotFoundException e)
        {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        } catch (CalendarException e)
        {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }

        return null;
    }


}
