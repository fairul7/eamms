package com.tms.collab.calendar.model;

import com.tms.collab.calendar.ui.CalendarEventView;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Sep 16, 2003
 * Time: 5:19:19 PM
 * To change this template use Options | File Templates.
 */
public class Appointment extends CalendarEvent
{
   // CalendarEventView cView = null;

    public Appointment()
    {
      //  cView = new CalendarEventView("appointmentView");
    }



    public CalendarEventView getCalendarEventView()
    {
      //  if (cView == null)
            CalendarEventView cView = new CalendarEventView("appointmentView");;

        cView.setEventId(getEventId());
        return cView;
    }
}
