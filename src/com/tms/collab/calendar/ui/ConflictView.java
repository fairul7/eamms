package com.tms.collab.calendar.ui;


import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.calendar.model.Attendee;
import com.tms.collab.resourcemanager.model.ResourceBooking;

import java.util.*;

import kacang.ui.Forward;
import kacang.ui.Event;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jan 9, 2004
 * Time: 2:45:29 PM
 * To change this template use Options | File Templates.
 */
public class ConflictView extends CalendarView
{
    private String userId;
    private Collection conflicts;
    private Collection resourceConflicts;
    private String resourceId;
    public static String EVENT_TYPE_SELECT ="select";
    public String getDefaultTemplate()
    {
        return "calendar/conflictview";
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public Forward actionPerformed(Event evt)
    {
        String type = evt.getType();
        if(EVENT_TYPE_SELECT.equals(type)){
            String eventId = evt.getRequest().getParameter("eventId");
            evt.getRequest().setAttribute("eventId",eventId);
            return new Forward("select");
        }
        return super.actionPerformed(evt);
    }

    public Collection getConflicts()
    {
        if((userId==null||userId.trim().length()<=0)&&(resourceId==null||resourceId.trim().length()<=0)){
            for (Iterator iterator = resourceConflicts.iterator(); iterator.hasNext();)
            {
                ResourceBooking resourceBooking = (ResourceBooking) iterator.next();
                conflicts.addAll(resourceBooking.getConflictEvents());
            }
            Set con = new HashSet(conflicts);
            return con;
        }else if(userId !=null &&userId.trim().length()>0){
            Collection col = new TreeSet();
            for (Iterator iterator = conflicts.iterator(); iterator.hasNext();)
            {
                CalendarEvent event = (CalendarEvent) iterator.next();
                Collection atts = event.getAttendees();
                for (Iterator it = atts.iterator(); it.hasNext();)
                {
                    Attendee attendee = (Attendee) it.next();
                    if(userId.equals(attendee.getUserId())){
                        col.add(event);
                        break;
                    }
                }
            }
            return col;
        }
        else if(resourceId != null&&resourceId.trim().length()>0){
            for (Iterator iterator = resourceConflicts.iterator(); iterator.hasNext();)
            {
                ResourceBooking resourceBooking = (ResourceBooking) iterator.next();
                if(resourceId.equals(resourceBooking.getResourceId()))
                    return resourceBooking.getConflictEvents();
            }
        }
        return null;
    }

    public void setConflicts(Collection conflicts)
    {
            this.conflicts = conflicts;

    }

    public Collection getResourceConflicts()
    {
        return resourceConflicts;
    }

    public void setResourceConflicts(Collection resourceConflicts)
    {
        this.resourceConflicts = resourceConflicts;
    }

    public String getResourceId()
    {
        return resourceId;
    }

    public void setResourceId(String resourceId)
    {
        this.resourceId = resourceId;
    }


}
