package com.tms.collab.calendar.ui;

import kacang.stdui.CalendarBox;
import kacang.ui.Forward;
import kacang.ui.Event;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Dec 8, 2003
 * Time: 11:03:27 AM
 * To change this template use Options | File Templates.
 */
public class CalendarPortlet extends CalendarBox
{
    public CalendarPortlet()
    {
    }

    public CalendarPortlet(String name)
    {
        super(name);
    }

    public Forward actionPerformed(Event evt)
    {
        super.actionPerformed(evt);
        String eventType = evt.getRequest().getParameter(Event.PARAMETER_KEY_EVENT_TYPE);
        if (PARAMETER_KEY_DATE_SELECT.equals(eventType)) {
            evt.getRequest().setAttribute("selectedDate",getDate());
            evt.getRequest().setAttribute("dayOfMonth",new Integer(getDayOfMonth()));
            return new Forward("dateselected");
        }
        return null;
    }

    public String getDefaultTemplate()
    {
        return "calendar/calendarbox";
    }

}
