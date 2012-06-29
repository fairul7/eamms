package com.tms.collab.calendar.ui;

import kacang.ui.Widget;
import kacang.ui.Event;
import kacang.Application;
import kacang.util.Log;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;

import com.tms.collab.calendar.model.CalendarModule;
import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.calendar.model.CalendarException;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Dec 8, 2003
 * Time: 9:00:55 AM
 * To change this template use Options | File Templates.
 */
public class EventPortlet extends Widget
{
    private Collection events;
    public EventPortlet()
    {
    }

    public EventPortlet(String name)
    {
        super(name);
    }

    public void onRequest(Event evt)
    {
        super.onRequest(evt);
        if(events!=null&&events.size()>0)
            events.clear();
        CalendarModule cm = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
        String userId = getWidgetManager().getUser().getId();
        try
        {
            Collection tmp_events = cm.getDailyCalendarEvents(new Date(),userId,new String[]{userId},null);
            for(Iterator i=tmp_events.iterator();i.hasNext();){
                CalendarEvent e = (CalendarEvent)i.next();
                if(e.getEventId().startsWith("com.tms.collab.calendar.model.CalendarEvent_"))
                {
                    if(events== null)
                        events = new ArrayList(15);
                    events.add(e);
                }
            }
        } catch (CalendarException e)
        {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }
    }

    public String getDefaultTemplate()
    {
       return "calendar/eventportlet";
    }

    public Collection getEvents()
    {
        return events;
    }

    public void setEvents(Collection events)
    {
        this.events = events;
    }


}
