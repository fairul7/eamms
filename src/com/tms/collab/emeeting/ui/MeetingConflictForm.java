package com.tms.collab.emeeting.ui;

import com.tms.collab.calendar.ui.ConflictForm;
import com.tms.collab.calendar.ui.UserUtil;
import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.calendar.model.Attendee;
import com.tms.collab.calendar.model.CalendarModule;
import com.tms.collab.emeeting.Meeting;
import com.tms.collab.emeeting.MeetingHandler;
import com.tms.collab.resourcemanager.model.ResourceManager;
import com.tms.collab.resourcemanager.model.Resource;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.services.security.User;
import kacang.Application;
import kacang.util.Log;
import java.util.*;

public class MeetingConflictForm extends ConflictForm
{
    private Meeting meeting;
    int count = 0;
     private String newflag;

    public void setEvent(Meeting meeting)
    {
        this.meeting = meeting;
    }

    public Forward onValidate(Event event)
    {
        String buttonClicked = findButtonClicked(event);
        if(getAddButton().getAbsoluteName().equals(buttonClicked)){
            try{
                String[] attIds = event.getParameterValues("comAttendeesCB");
                Collection attendeeList = new TreeSet(); // Attendee objects
                if(attIds!=null){
                    for (int i = 0; i < attIds.length; i++)
                    {
                        String userId = attIds[i];
                        User tmpUser = UserUtil.getUser(userId.trim());
                        Attendee att = new Attendee();
                        att.setUserId(tmpUser.getId());
                        att.setProperty("username", tmpUser.getUsername());
                        att.setProperty("firstName", tmpUser.getProperty("firstName"));
                        att.setProperty("lastName", tmpUser.getProperty("lastName"));
                        att.setCompulsory(true);
                        att.setStatus(CalendarModule.ATTENDEE_STATUS_CONFIRMED);
                        attendeeList.add(att);
                    }
                }
                attIds = event.getParameterValues("opAttendeesCB");
                if(attIds!=null){
                    for (int i = 0; i < attIds.length; i++)
                    {
                        String userId = attIds[i];
                        User tmpUser = UserUtil.getUser(userId.trim());
                        Attendee att = new Attendee();
                        att.setUserId(tmpUser.getId());
                        att.setProperty("username", tmpUser.getUsername());
                        att.setProperty("firstName", tmpUser.getProperty("firstName"));
                        att.setProperty("lastName", tmpUser.getProperty("lastName"));
                        att.setCompulsory(false);
                        att.setStatus(CalendarModule.ATTENDEE_STATUS_PENDING);
                        attendeeList.add(att);
                    }
                }
                this.meeting.getEvent().setAttendees(attendeeList);
                ResourceManager rm ;
                String [] resourceIds = event.getParameterValues("resourcesCB");
                rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);
                Collection resourcesCol = new ArrayList();
                if(resourceIds!=null){
                    for (int i = 0; i < resourceIds.length; i++)
                    {
                        String resourceId = resourceIds[i];
                        Resource resource = rm.getResource(resourceId,true);
                        resourcesCol.add(resource);
                    }
                }
                this.meeting.getEvent().setResources(resourcesCol);
                MeetingHandler handler = (MeetingHandler)Application.getInstance().getModule(MeetingHandler.class);
                if(!isUpdate()){
                    handler.addMeeting(meeting,getWidgetManager().getUser().getId(),true);
                }
                else{
                    handler.editMeeting(meeting,getWidgetManager().getUser().getId(),true);
                }
                CalendarModule cm =(CalendarModule)Application.getInstance().getModule(CalendarModule.class);
                for (Iterator iterator = getRecurringEvents().iterator(); iterator.hasNext();)
                {
                    CalendarEvent revent = (CalendarEvent) iterator.next();
                    cm.addRecurringEvent(revent,getWidgetManager().getUser().getId(),true);
                }
                if(meeting.getSecretary() != null && !"".equals(meeting.getSecretary())){
                if(getNewflag() != null && getNewflag().equals("1")){
                    MeetingForm.sendNotification(meeting,event);
                }else{
                   MeetingForm.sendNotificationUpdate(meeting,event);
                }
                }
                if(!isUpdate()){
                    return new Forward("added");
                }
                else{
                    return new Forward("updated");
                }
            }catch(Exception e){
                Log.getLog(ConflictForm.class).error(e);
            }
        } else if(getCancelButton().getAbsoluteName().equals(buttonClicked)){
            return new Forward("cancel");
        }
        return null;
        //return super.onValidate(event);
    }

    public void setConflicts(Collection conflicts)
    {
        this.conflicts = conflicts;
        conflictAttendees = new TreeSet();
        Map allAttendees = new HashMap();
        for (Iterator iterator = conflicts.iterator(); iterator.hasNext();)
        {
            CalendarEvent event = (CalendarEvent) iterator.next();
            Collection atts = event.getAttendees();
            for (Iterator attit = atts.iterator(); attit.hasNext();)
            {
                Attendee attendee = (Attendee) attit.next();
                Collection conflictingAppts = (Collection)allAttendees.get(attendee.getUserId());
                if (conflictingAppts == null) {
                    conflictingAppts = new ArrayList();
                }
                conflictingAppts.add(event);
                allAttendees.put(attendee.getUserId(), conflictingAppts);
            }
        }
        Collection attendees = meeting.getEvent().getAttendees();
        for (Iterator atts = attendees.iterator(); atts.hasNext();)
        {
            Attendee att = (Attendee) atts.next();
            if(allAttendees.containsKey(att.getUserId())) {
                Collection conflictingAppts = (Collection)allAttendees.get(att.getUserId());
                att.setProperty("conflicts", conflictingAppts); // store conflicting appointments in "conflicts" property
                conflictAttendees.add(att.getUserId());
            }
        }
    }

    public String getHeader(){
        return "E-Meeting";
    }

    public boolean isOwnerExcluded(){
        String userId = meeting.getEvent().getUserId();
        Collection col = meeting.getEvent().getAttendees();
        for (Iterator iterator = col.iterator(); iterator.hasNext();)
        {
            Attendee attendee = (Attendee) iterator.next();
            if(userId.equals(attendee.getUserId())&&attendee.isCompulsory())
                return false;
        }
        return true;
    }

    public String getDefaultTemplate()
    {
        return "emeeting/meetingconflictsform";
    }

    public Meeting getMeeting()
    {
        return meeting;
    }

    public void setMeeting(Meeting meeting)
    {
        this.meeting = meeting;
    }

    public String getNewflag() {
        return newflag;
    }

    public void setNewflag(String newflag) {
        this.newflag = newflag;
    }
}

