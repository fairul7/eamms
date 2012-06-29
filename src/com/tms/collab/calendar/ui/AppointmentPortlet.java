package com.tms.collab.calendar.ui;

import kacang.ui.Widget;
import kacang.ui.Event;
import kacang.Application;
import kacang.util.Log;

import java.util.*;

import com.tms.collab.calendar.model.CalendarModule;
import com.tms.collab.calendar.model.CalendarException;
import com.tms.collab.calendar.model.CalendarEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Dec 8, 2003
 * Time: 9:00:34 AM
 * To change this template use Options | File Templates.
 */
public class AppointmentPortlet extends Widget
{
    private Collection appointments;

    public AppointmentPortlet()
    {
    }

    public AppointmentPortlet(String name)
    {
        super(name);
    }

    public String getDefaultTemplate()
    {
        return "calendar/appointmentportlet";
    }

    public void onRequest(Event evt)
    {
        super.onRequest(evt);
        if(appointments!=null&&appointments.size()>0)
            appointments.clear();
        CalendarModule cm = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
        String userId = getWidgetManager().getUser().getId();
        Collection events = null;
        try
        {
            events = cm.getDailyCalendarEvents(new Date(),userId,new String[]{userId},null);
            for(Iterator i=events.iterator();i.hasNext();){
                CalendarEvent e = (CalendarEvent)i.next();
                if(e.getEventId().startsWith("com.tms.collab.calendar.model.Appointment_") ||
                        e.getEventId().startsWith("com.tms.collab.emeeting.Meeting_"))
                {
                    if(appointments== null)
                        appointments = new TreeSet();
                    appointments.add(e);
                }
            }
        } catch (CalendarException e)
        {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }


    }

    public Collection getAppointments()
    {
        return appointments;
    }

    public void setAppointments(Collection appointments)
    {
        this.appointments = appointments;
    }
}
