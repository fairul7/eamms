package com.tms.collab.emeeting.ui;

import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;
import com.tms.collab.calendar.ui.CalendarForm;
import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.calendar.model.CalendarModule;
import com.tms.collab.calendar.model.ConflictException;
import com.tms.collab.emeeting.MeetingHandler;
import com.tms.collab.emeeting.Meeting;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Sep 26, 2003
 * Time: 10:15:23 AM
 * To change this template use Options | File Templates.
 */
public class MeetingFormAdd extends MeetingForm
{

    public MeetingFormAdd()
    {
    }

    public MeetingFormAdd(String name)
    {
        super(name);
    }

    protected Forward createEvent(Event evt) throws RuntimeException
    {
             String note = notifyNote.getValue().toString();
            if(note != null && !note.equals("")){
                note = StringUtils.replace(note,"\n","<br>");
            }

        try
        {
            CalendarModule module = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
            MeetingHandler handler = (MeetingHandler) Application.getInstance().getModule(MeetingHandler.class);
            CalendarEvent event = assembleEvent();
            String userId = getWidgetManager().getUser().getId();

            String eventId = event.getEventId();
            String prefix = Meeting.class.getName();
            if(!eventId.substring(0, prefix.length()).equals(prefix))
                event.setEventId(prefix + "_" + eventId);

            meeting = assembleMeeting(event);
           
            Collection col = generateRecurringEvent(event);
            for(Iterator i = col.iterator(); i.hasNext();)
                module.addRecurringEvent((CalendarEvent) i.next(), userId, false);
             //meeting.setFlag(flg);

            handler.addMeeting(meeting, userId);

            //Bug#:3856 - so that in the second page these values can be repopulate into the form
            evt.getRequest().getSession().setAttribute("memo",(Boolean)(notifyMemo.isChecked()?Boolean.TRUE:Boolean.FALSE));
            evt.getRequest().getSession().setAttribute("email",(Boolean)(notifyEmail.isChecked()?Boolean.TRUE:Boolean.FALSE));
            evt.getRequest().getSession().setAttribute("notes",note);

            init();

            return new Forward("event added");

        }catch(ConflictException e)
        {
            eventId=null;
            evt.getRequest().getSession().setAttribute("conflict",e);
            evt.getRequest().getSession().setAttribute("event",meeting);
            evt.getRequest().getSession().setAttribute("recurringEvents",generateRecurringEvent(meeting.getEvent()));
            evt.getRequest().getSession().setAttribute("memo",notifyMemo.isChecked()?Boolean.TRUE:Boolean.FALSE);
            evt.getRequest().getSession().setAttribute("email",notifyEmail.isChecked()?Boolean.TRUE:Boolean.FALSE);
            evt.getRequest().getSession().setAttribute("notes",note);
            return new Forward("conflict exception");
        }
        catch(Exception e)
        {
            throw new RuntimeException(e.toString());
        }
    }

    public String getDefaultTemplate()
    {
        return CalendarForm.DEFAULT_TEMPLATE;
    }

    public void onRequest(Event event)
    {
        super.onRequest(event);
    }

}
