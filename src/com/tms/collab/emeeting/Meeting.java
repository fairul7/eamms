package com.tms.collab.emeeting;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import kacang.Application;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.Form;
import kacang.ui.Forward;
import kacang.ui.Widget;
import kacang.util.Log;

import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.calendar.model.CalendaringEventView;
import com.tms.collab.calendar.ui.CalendarEventView;
import com.tms.collab.emeeting.ui.MeetingFormEdit;
import com.tms.collab.emeeting.ui.MeetingFormView;

public class Meeting implements CalendaringEventView, Comparable
{
    public static final String DEFAULT_VIEW = "meetingView";
    public static final String DEFAULT_EDIT = "meetingEdit";
    private String notify;
    private String eventId;
    private String title;
    private String category;
    private String chairman;
    private String secretary;
    private String userId;
    private Date startDate;
    private Date endDate;
    private CalendarEvent event;
    private Collection meetingAgenda;
    private Collection tasks;
    
    private boolean notifyMemo;
    private boolean notifyEmail;

    public Meeting()
    {
        meetingAgenda = new ArrayList();
    }

    public CalendarEventView getCalendarEventView()
    {
        return null;
    }

    /* Interface methods */
    public Widget getEventView()
    {
        return new MeetingFormView(DEFAULT_VIEW);
    }

    public Form getEventEdit()
    {
        return new MeetingFormEdit(DEFAULT_EDIT);
    }

    public Forward deleteEvent(String eventId, String userId)
    {
        Forward forward = null;
        MeetingHandler handler = (MeetingHandler) Application.getInstance().getModule(MeetingHandler.class);
        try
        {
            handler.deleteMeeting(eventId, userId);
            forward = new Forward(DELETE_SUCCESSFUL);
        }
        catch(Exception e)
        {
            Log.getLog(getClass()).error(e.toString(), e);
            forward = new Forward(DELETE_FAILED);
        }
        return forward;
    }

    public int compareTo(Object o)
    {
        if (o instanceof Meeting) {
            int result = 0;
            if(event.getStartDate()!=null &&((Meeting)o).getEvent().getStartDate()!=null )
                result = event.getStartDate().compareTo(((Meeting)o).getEvent().getStartDate());
            //else
            //  result = getEndDate().compareTo(((CalendarEvent)o).getStartDate())
            if (result == 0) {
                result = event.getId().compareTo(((Meeting)o).getEvent().getId());
            }
            return result;
        }
        else {
            return -1;
        }
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getEventId()
    {
        return eventId;
    }

    public void setEventId(String eventId)
    {
        this.eventId = eventId;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public void addAgendaItem(AgendaItem item)
    {
        meetingAgenda.add(item);
    }

    public Collection getMeetingAgenda()
    {
        return meetingAgenda;
    }

    public void setMeetingAgenda(Collection meetingAgenda)
    {
        this.meetingAgenda = meetingAgenda;
    }

    public String getChairman()
    {
        return chairman;
    }

    public void setChairman(String chairman)
    {
        this.chairman = chairman;
    }

    public String getSecretary()
    {
        return secretary;
    }

    public void setSecretary(String secretary)
    {
        this.secretary = secretary;
    }

    public CalendarEvent getEvent()
    {
        return event;
    }

    public void setEvent(CalendarEvent event)
    {
        this.event = event;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public String getDescription(){
        if(event!=null)
            return event.getDescription();
        return null;
    }

    public String getNotify() {
        return notify;
    }

    public void setNotify(String notify) {
        this.notify = notify;
    }

    /* Utility methods */
    public String getChairmanName()
    {
        return getUserName(chairman);
    }

    public String getSecretaryName()
    {
/*    	if(secretary==null||"".equals(secretary)|| secretary.trim().length() <= 0)
    		return"-";
    	else*/
        return getUserName(secretary);
    }

    private String getUserName(String userId)
    {
        String name = "";
        try
        {
            if((userId != null && !userId.equals("")) && !" - ".equals(userId) && !" -".equals(userId)){
                SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
                User user = service.getUser(userId);
                name = user.getName();
            }else {
                name = " - ";
            }
        }
        catch (SecurityException e)
        {
            Log.getLog(getClass()).error("Error while retrieving user", e);
        }
        return name;
    }

    public Collection getTasks() {
        return tasks;
    }

    public void setTasks(Collection tasks) {
        this.tasks = tasks;
    }

    public boolean isMoreThanOneDay()
    {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        return (startCal.get(Calendar.DAY_OF_YEAR)!=endCal.get(Calendar.DAY_OF_YEAR));
    }

	public boolean isNotifyEmail() {
		return notifyEmail;
	}

	public void setNotifyEmail(boolean notifyEmail) {
		this.notifyEmail = notifyEmail;
	}

	public boolean isNotifyMemo() {
		return notifyMemo;
	}

	public void setNotifyMemo(boolean notifyMemo) {
		this.notifyMemo = notifyMemo;
	}

	
    
    

}
