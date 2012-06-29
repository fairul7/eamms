package com.tms.collab.calendar.model;

import kacang.model.DefaultDataObject;
import kacang.model.DataObjectNotFoundException;
import kacang.ui.Widget;
import kacang.ui.Forward;
import kacang.stdui.Form;
import kacang.Application;
import kacang.util.Log;
import kacang.services.security.SecurityService;
import kacang.services.security.SecurityException;
import kacang.services.security.User;
import java.util.*;
import java.text.SimpleDateFormat;
import com.tms.collab.calendar.ui.GeneralEventView;
import com.tms.collab.calendar.ui.CalendarForm;
import org.apache.commons.collections.SequencedHashMap;


/**
 * Represents a calendar event, either an appointment or event.
 * Can also represent a reminder or a recurrence of an original event,
 * indicated by the methods isReminder() and isRecurrence().
 * <li>eventId - Unique identifier for the event</li>
 * <li>startDate - Start date/time</li>
 * <li>endDate - End date/time</li>
 * <li>allDay - true for an all day event</li>
 * <li>reminderDate - Reminder date, null if there is none</li>
 * <li>title</li>
 * <li>description</li>
 * <li>agenda</li>
 * <li>summary</li>
 * <li>location</li>
 * <li>status - Not used at the moment</li>
 * <li>categories - Not used</li>
 * <li>classification - public, private or confidential.
 * Public events are accessible by all users.
 * Private events will be seen by other users, but details hidden.
 * Confidential events will not be seen by other users.
 * </li>
 * <li>instanceId - ID to identify a specific recurrence</li>
 * <li>instanceDate - Not used</li>
 * <li>lastRecurrenceDate - The last date the event will recur, null if event does not recur</li>
 * <li>recurrenceRule - String representing the recurrence rule, follows the basic recurrence rule grammar</li>
 * <li>recurrenceDates - String representing the recurrence dates, following the vCalendar recurrence format</li>
 * <li>resources - A Collection Resource objects for the event</li>
 * <li>attendees - A Collection Attendee objects for the event</li>
 * <li>priority - Not used</li>
 * <li>userId - The user ID of the owner/scheduler of the event</li>
 * <li>reminderSent - Not used</li>
 * <li>reminder - true if this object is a reminder for another event</li>
 * <li>recurrence - true if this object is a recurrence for another event</li>
 * <li>modified - true if the event has been modified</li>
 * <li>new - true if the event is newly created</li>
 * <li>archived - Not used</li>
 * <li>deleted - true if the event has been deleted</li>
 * <li>lastModified - Date the event was last modified</li>
 * <li>lastModifiedBy - User ID of the last user to modify the event</li>
 * <li>hidden - true indicates a private event for which details should not be shown</li>
 */
public class CalendarEvent extends DefaultDataObject implements Comparable, Cloneable,CalendaringEventView {

    protected String eventId; // unique identifier
    protected Date startDate;
    protected Date endDate;
    protected Date creationDate;
    protected boolean allDay;
    protected Date reminderDate;
    protected String title;
    protected String description;
    protected String agenda;
    protected String summary;
    protected String location;
    protected String status; // cancelled, etc.
    protected String categories; // semi-colon delimited
    protected String classification; // public, private, confidential
    protected String instanceId;
    protected Date instanceDate; // start date for this instance
    protected Date lastRecurrenceDate;
    protected String recurrenceRule; // follows basic recurrence rule grammar
    protected String recurrenceDates; // iso 8601, ; delimited
    protected Collection resources; // Resource objects
    protected Collection attendees; // Attendee objects
    protected int priority;
    protected String userId; // owner user id
    protected boolean universal;
    protected boolean reminderSent;
    protected boolean reminder;
    protected boolean recurrence;
    protected boolean modified;
    protected boolean neww;
    protected boolean archived;
    protected boolean deleted;
    protected boolean ekpEvent;
    protected Date lastModified;
    protected String lastModifiedBy; // user id
    protected RecurrenceRule parsedRecurrenceRule;
    protected boolean hidden;
    protected Map attendeeMap;
    protected GeneralEventView eventView = null;
    protected CalendarForm calendarForm = null;
    protected String externalEventId;
    protected String organizerEmail;
	public String getExternalEventId() {
		return externalEventId;
	}

	public void setExternalEventId(String externalEventId) {
		this.externalEventId = externalEventId;
	}

    public CalendarEvent() {
		ekpEvent = true;
    }

    public Forward deleteEvent(String eventId,String userId)
    {
        CalendarModule cm = (CalendarModule)Application.getInstance().getModule(CalendarModule.class);
        try
        {
            cm.deleteCalendarEvent(eventId,userId);
        } catch (DataObjectNotFoundException e)
        {
            Log.getLog(getClass()).error(e);
        } catch (CalendarException e)
        {
            Log.getLog(getClass()).error(e);
        }
        return new Forward(CalendaringEventView.DELETE_SUCCESSFUL);
    }

    /**
     * Consists of the eventID, instanceID and whether or not the event is a reminder/recurrence.
     * @return
     */
    public String getId() {
        String sd = "";

        if(getStartDate()!=null) {
            sd = Long.toString(getStartDate().getTime());
        }
        return getEventId() + "_" + getInstanceId() + "_" + isReminder() + "_" + isRecurrence()+"_"+sd;
    }

    /**
     * @param another
     * @return true if the object IDs match.
     */
    public boolean equals(Object another) {
        return (another instanceof CalendarEvent) &&
                ((CalendarEvent)another).getId().equals(getId());
    }

    public int hashCode() {
        return getId().hashCode();
    }

    /**
     * Compares based on the event's start date.
     * @param o
     * @return
     */
    public int compareTo(Object o) {
        if (o instanceof CalendarEvent) {
            int result = 0;
            if(getStartDate()!=null &&((CalendarEvent)o).getStartDate()!=null )
                result = getStartDate().compareTo(((CalendarEvent)o).getStartDate());
            //else
            //  result = getEndDate().compareTo(((CalendarEvent)o).getStartDate())
            if (result == 0) {
                result = getId().compareTo(((CalendarEvent)o).getId());
            }
            return result;
        }
        else {
            return -1;
        }
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String toString() {
        return "[" + getId() + "_" + getDescription() + "]";
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public String getSDate() {
        SimpleDateFormat sdate = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" );
        String rejectSDate = sdate.format(startDate);
        return rejectSDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getEDate() {
        SimpleDateFormat edate = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" );
        String rejectEDate = edate.format(endDate);
        return rejectEDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(Date reminderDate) {
        this.reminderDate = reminderDate;
    }

    public String getDescription() {
        return description;
    }

    public String getDesc() {
        char[] temp = description.toCharArray();
        StringBuffer buf = new StringBuffer();

        for(int i=0; i<temp.length; i++){
            int ascii = temp[i];
            if(ascii == 13)
                buf.append("<br/>");
            else
                buf.append(temp[i]);
        }

        return buf.toString();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAgenda() {
        return agenda;
    }

    public String getAgen() {
        char[] temp = agenda.toCharArray();
        StringBuffer buf = new StringBuffer();

        for(int i=0; i<temp.length; i++){
            int ascii = temp[i];
            if(ascii == 13)
                buf.append("<br/>");
            else
                buf.append(temp[i]);
        }

        return buf.toString();
    }

    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getInstanceId() {
        return CalendarUtil.getRecurrenceInstanceId(this);
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public Date getLastRecurrenceDate() {
        return lastRecurrenceDate;
    }

    public void setLastRecurrenceDate(Date lastRecurrenceDate) {
        this.lastRecurrenceDate = lastRecurrenceDate;
    }

    public String getRecurrenceRule() {
        return recurrenceRule;
    }

    public void setRecurrenceRule(String recurrenceRule) {
        this.recurrenceRule = recurrenceRule;
    }

    public String getRecurrenceDates() {
        return recurrenceDates;
    }

    public void setRecurrenceDates(String recurrenceDates) {
        this.recurrenceDates = recurrenceDates;
    }

    public Collection getResources() {
        return resources;
    }

    public void setResources(Collection resources) {
        this.resources = resources;
    }

    public Collection getAttendees() {
        return attendees;
    }

    public void setAttendees(Collection attendees) {
        this.attendees = attendees;
        attendeeMap = new SequencedHashMap();
        if (attendees != null) {
            for(Iterator i=attendees.iterator(); i.hasNext();) {
                com.tms.collab.calendar.model.Attendee attendee = (com.tms.collab.calendar.model.Attendee)i.next();
                attendeeMap.put(attendee.getUserId(), attendee);
            }
        }
    }


    public Widget getEventView()
    {
        if(eventView==null)
            eventView = new GeneralEventView("generalEventView");
        return eventView;
    }

    public void setEventView(GeneralEventView eventView)
    {
        this.eventView = eventView;
    }

    public Form getEventEdit()
    {
        if(calendarForm == null)
            calendarForm = new CalendarForm("CALENDARFORM");
        return calendarForm;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isReminder() {
        return reminder;
    }

    public void setReminder(boolean reminder) {
        this.reminder = reminder;
    }

    public boolean isRecurrence() {
        return recurrence;
    }

    public void setRecurrence(boolean recurrence) {
        this.recurrence = recurrence;
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public String getLastModifiedName() throws SecurityException {
        try {
            SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
            return ss.getUser(lastModifiedBy).getName();
        }
        catch (SecurityException e) {
            return "";
        }
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public boolean isNew() {
        return neww;
    }

    public void setNew(boolean neww) {
        this.neww = neww;
    }

    public Date getInstanceDate() {
        return instanceDate;
    }

    public void setInstanceDate(Date instanceDate) {
        this.instanceDate = instanceDate;
    }

    public boolean isReminderSent() {
        return reminderSent;
    }

    public void setReminderSent(boolean reminderSent) {
        this.reminderSent = reminderSent;
    }

    public boolean isAllDay() {
        return allDay;
    }

    public boolean isMoreThanOneDay(){
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        return (startCal.get(Calendar.DAY_OF_YEAR)!=endCal.get(Calendar.DAY_OF_YEAR));
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isUniversal() {
        return universal;
    }

    public void setUniversal(boolean universal) {
        this.universal = universal;
    }

    public String getRecurrenceId() {
        return CalendarUtil.getRecurrenceInstanceId(this);
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

//--- Convenience Methods

    /**
     * Returns a RecurrenceRule object that is the parsed result of
     * the recurrenceRule property.
     * @return null if the parsing failed.
     */
    public RecurrenceRule getParsedRecurrenceRule() {
        try {
            if (parsedRecurrenceRule == null)
                parsedRecurrenceRule = CalendarUtil.parseRecurrenceRule(getRecurrenceRule());
            return parsedRecurrenceRule;
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Checks to see whether or not the user scheduling the event has excluded
     * himself/herself.
     * @return true if the scheduler is excluded.
     */
    public boolean isOwnerExcluded() {
        if (getAttendees() == null)
            return true;

        boolean included = false;
        for (Iterator i=getAttendees().iterator(); i.hasNext();) {
            com.tms.collab.calendar.model.Attendee attendee = (com.tms.collab.calendar.model.Attendee)i.next();
            if (getUserId().equals(attendee.getUserId())) {
                included = true;
                break;
            }
        }
        return !included;
    }

    /**
     * Calculates the number of days before the event for which a reminder is set.
     * @return 0 if there is no reminder.
     */
    public long getReminderDays() {
        if (getReminderDate() != null) {
            int days = 0;
            Calendar cal = Calendar.getInstance();
            cal.setTime(getReminderDate());
            while(cal.getTime().before(getStartDate())) {
                days++;
                cal.add(Calendar.DATE, 1);
            }
            return days;
        }
        else {
            return 0;
        }
    }

    /**
     * Returns a userId=Attendee Map.
     * @return never null.
     */
    public Map getAttendeeMap() {
        return attendeeMap;
    }
    public String getUserName()
    {
        SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
        try
        {
            User user = ss.getUser(userId);
            return user.getName();
            
        } catch (SecurityException e)
        {
        	if(userId.indexOf("@")>0){
        		return userId;	
        	}else{
//            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            return "*Deleted User*";
        }
    }
    }

    public boolean isPersonal(){
        if(attendees== null)
            return true;
        if(attendees.size()==1 &&((Attendee)attendees.iterator().next()).getUserId().equals(userId))
            return true;
        return false;
    }

    public Date getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(Date creationDate)
    {
        this.creationDate = creationDate;
    }
	public boolean isEkpEvent() {
		return ekpEvent;
	}

	public void setEkpEvent(boolean ekpEvent) {
		this.ekpEvent = ekpEvent;
	}

	public String getOrganizerEmail() {
		return organizerEmail;
	}

	public void setOrganizerEmail(String organizerEmail) {
		this.organizerEmail = organizerEmail;
    }
}
