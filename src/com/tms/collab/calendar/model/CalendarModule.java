package com.tms.collab.calendar.model;

import kacang.model.DataObjectNotFoundException;
import kacang.model.DefaultModule;
import kacang.model.DaoQuery;
import kacang.model.DaoException;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import kacang.Application;
import kacang.services.indexing.SearchableModule;
import kacang.services.indexing.SearchResult;
import kacang.services.indexing.QueryException;
import kacang.services.indexing.SearchResultItem;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;

import java.util.*;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

import javax.mail.internet.AddressException;

import com.tms.collab.messaging.model.MessagingException;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.resourcemanager.model.ResourceManager;
import com.tms.collab.resourcemanager.model.ResourceManager;
import com.tms.collab.resourcemanager.model.Resource;
import com.tms.collab.taskmanager.model.TaskManager;
import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.emeeting.Meeting;
import com.tms.util.StringUtil;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.PartStat;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Clazz;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStamp;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.Method;
import net.fortuna.ical4j.model.property.Organizer;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;

import org.htmlparser.util.ParserException;

/**
 * Handler for the Calendar Module.
 * In this module, appointments and events are both represented by CalendarEvent.
 */
public class CalendarModule extends DefaultModule implements SearchableModule {

    /**
     * Instance ID for the default (original) event.
     */
    public static final String PERMISSION_CALENDARING= "com.tms.collab.calendar.Calendaring";
    public static final String PERMISSION_MANAGE_EVENTS = "com.tms.collab.calendar.ManageEvents";
    public static final String PERMISSION_EDIT_EVENTS = "com.tms.collab.calendar.EditEvents";
    public static final String PERMISSION_DELETE_EVENTS = "com.tms.collab.calendar.DeleteEvents";

    public static final String DEFAULT_INSTANCE_ID = "0";

    public static final String CLASSIFICATION_PUBLIC = "pub";
    public static final String CLASSIFICATION_PRIVATE = "pri";
    public static final String CLASSIFICATION_CONFIDENTIAL = "con";

    public static final String ATTENDEE_STATUS_PENDING = "P";
    public static final String ATTENDEE_STATUS_CONFIRMED = "C";
    public static final String ATTENDEE_STATUS_REJECT = "R";

    Log log = Log.getLog(getClass());

    /**
     * Initializes the Calendar Module Handler, called once during startup.
     */
    public void init() {
    }

    public void acceptAppointment(String eventId,String instanceId, String userId){
        try{ CalendarDao dao = (CalendarDao)getDao();
            dao.acceptAppointment(eventId,instanceId,userId);
			try {
				String icsMessage = createReplyCalendarFile(eventId,
						CalendarModule.ATTENDEE_STATUS_CONFIRMED);
				
				sendAcceptNotification(eventId, userId, icsMessage);
			} catch (DataObjectNotFoundException e) {
				System.out.println("Error" + e.getMessage());
			} catch (CalendarException e) {
				System.out.println("Error" + e.getMessage());
			}
        }catch(Exception e){
            log.error("Error accepting appointment " + eventId, e);
        }
    }

	// Added by Arun on 14th July,2006
	// TO send accept notification to outside events.

	public static void sendAcceptNotification(String eventId, String userId,
			String icsMessage) throws SecurityException, AddressException,
			MessagingException, DataObjectNotFoundException, CalendarException {
		SecurityService ss;
		User fromUser;
		CalendarEvent event;
		MessagingModule mm;
		ss = (SecurityService) Application.getInstance().getService(
				SecurityService.class);
		String[] user = userId.split("_0_");
		userId = user[1];
		fromUser = ss.getUser(userId);
		CalendarModule cm = (CalendarModule) Application.getInstance()
				.getModule(CalendarModule.class);
		event = cm.getCalendarEvent(eventId);
		StringBuffer sb = new StringBuffer();
		sb.append(fromUser.getName()
				+ " has accepted the following appointment : \n\n<br><br>");
		sb.append("Title : " + event.getTitle() + "\n<br>");
		sb.append("Description : " + event.getDescription() + "\n<br>");
		sb.append("Start time : " + event.getStartDate() + "\n<br>");
		sb.append("End time : " + event.getEndDate() + "\n\n<br><br>");

		// String toEmail = ss.getUser(event.getUserId()).getUsername() + "@" +
		// MessagingModule.INTRANET_EMAIL_DOMAIN;
		String toEmail = (event.getOrganizerEmail() == null || "".equals(event.getOrganizerEmail())) ? ss.getUser(event.getUserId()).getUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN : event.getOrganizerEmail();
		String ccEmail = "";
		String bccEmail = "";
		String subject = "Notification of accepted appointment ";
		String title = subject;
		String content = sb.toString();
		try {
			mm = (MessagingModule) Application.getInstance().getModule(
					MessagingModule.class);
			mm.sendStandardHtmlEmail(userId, toEmail, ccEmail, bccEmail,
					subject, title, content, icsMessage);
		} catch (Exception e) {
            System.out.println("Error " + e.getMessage());

        }
	}

    public void rejectAppointment(String eventId,String instanceId, String userId,String reason){
        try{ CalendarDao dao = (CalendarDao)getDao();
            dao.rejectAppointment(eventId,instanceId,userId,reason);
        }catch(Exception e){
            log.error("Error rejecting appointment " + eventId, e);
        }
    }

	public String createReplyCalendarFile(String eventId, String replyStatus)
			throws DataObjectNotFoundException, CalendarException {
		CalendarModule cm = (CalendarModule) Application.getInstance()
				.getModule(CalendarModule.class);
		CalendarEvent cEvent = cm.getCalendarEvent(eventId);
		net.fortuna.ical4j.model.Calendar calendar = new net.fortuna.ical4j.model.Calendar();
		if (replyStatus.equals("C")) {
			replyStatus = "ACCEPTED";
		} else {
			replyStatus = "DECLINED";
		}
		calendar.getProperties().add(
				new ProdId("-//The Media Shoppe Bhd//tmsEkp 1.4.1//EN"));
		calendar.getProperties().add(Version.VERSION_2_0);
		calendar.getProperties().add(CalScale.GREGORIAN);
		calendar.getProperties().add(Method.REPLY);
		net.fortuna.ical4j.model.PropertyList eventProperties = new net.fortuna.ical4j.model.PropertyList();
		// Adding Start Date
		DtStart dtStart = new DtStart(new net.fortuna.ical4j.model.DateTime(
				cEvent.getStartDate()));
		try {
			dtStart.validate();
			eventProperties.add(dtStart);
		} catch (ValidationException e) {
		}
		// Adding End Date
		DtEnd dtEnd = new DtEnd(new net.fortuna.ical4j.model.DateTime(cEvent
				.getEndDate()));
		try {
			dtEnd.validate();
			eventProperties.add(dtEnd);
		} catch (ValidationException e) {
		}
		DtStamp dtStamp = new DtStamp(new net.fortuna.ical4j.model.DateTime(
				new Date()));
		eventProperties.add(dtStamp);
		Uid uid = new Uid(cEvent.getExternalEventId());
		if (uid == null) {
			uid.setValue(cEvent.getEventId());
		}
		eventProperties.add(uid);
		if (cEvent.getOrganizerEmail() != null) {
			try {
				Organizer organizer = new Organizer();
				organizer.setValue("MAILTO:" + cEvent.getOrganizerEmail());
				eventProperties.add(organizer);
			} catch (URISyntaxException e) {
				// Log.getLog(getClass()).error("Error while adding organizer to
				// reply iCalendar File", e);
			}
		}
		eventProperties.add(new Location(cEvent.getLocation()));
		String clazz = "";
		if (cEvent.getClassification().equals("pri")) {
			clazz = "PRIVATE";
		} else {
			clazz = "PUBLIC";
		}
		eventProperties.add(new Clazz(clazz));
		eventProperties.add(new Summary(replyStatus + ":" + cEvent.getTitle()));
		net.fortuna.ical4j.model.component.VEvent vEvent = new net.fortuna.ical4j.model.component.VEvent(
				eventProperties);

		// Adding attendee Status
		for (Iterator i = cEvent.getAttendees().iterator(); i.hasNext();) {
			Attendee attendee = (Attendee) i.next();
			User currentUser = Application.getInstance().getCurrentUser();
			if (currentUser.getId().equals(attendee.getUserId())
					|| currentUser.getProperty("email1").equals(
							attendee.getProperty("email"))) {
				net.fortuna.ical4j.model.ParameterList attedeeParameters = new net.fortuna.ical4j.model.ParameterList();
				attedeeParameters.add(new PartStat(replyStatus));
				attedeeParameters.add(new Cn((String) attendee.getName()));
				String mailTo = "";
				if (currentUser.getProperty("email1") != null) {
					mailTo = "MailTo:"
							+ (String) currentUser.getProperty("email1");
				} else {
					mailTo = "MailTo:" + (String) attendee.getProperty("email");
				}
				try {
					net.fortuna.ical4j.model.property.Attendee iCalAttendee = new net.fortuna.ical4j.model.property.Attendee(
							attedeeParameters, mailTo);
					eventProperties.add(iCalAttendee);
				} catch (URISyntaxException e) {
					Log.getLog(getClass()).error("Error while creating reply iCalendar File", e);
				}
			}
		}
		calendar.getComponents().add(vEvent);
		try {
			calendar.validate();
			CalendarOutputter outputter = new CalendarOutputter();
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			outputter.output(calendar, bao);
			byte[] buffer = bao.toByteArray();
			String str = new String(buffer);
			return str;
		} catch (ValidationException e) {
			Log.getLog(getClass()).error("", e);
			return "";
		} catch (IOException e) {
			Log.getLog(getClass()).error("", e);
			return "";
		}

	}

    /**
     * Creates and stores a new calendar event.
     * @param event The event to store.
     * @param ignoreConflicts set true to ignore conflicts.
     * @throws com.tms.collab.calendar.model.ConflictException If there are conflicting events. Obtain these conflicting events from the ConflictException class.
     * @throws com.tms.collab.calendar.model.CalendarException
     */
    public CalendarEvent addCalendarEvent(String prefix, CalendarEvent event, String userId, boolean ignoreConflicts) throws ConflictException, CalendarException {
        return addCalendarEvent(prefix, event, userId, ignoreConflicts, new Date());
    }

    /**
     * Creates and stores a new calendar event.
     * @param event The event to store.
     * @param ignoreConflicts set true to ignore conflicts.
     * @param createdDate set created/modified date. Defaults to current date if parameter is null
     * @throws com.tms.collab.calendar.model.ConflictException If there are conflicting events. Obtain these conflicting events from the ConflictException class.
     * @throws com.tms.collab.calendar.model.CalendarException
     */
    public CalendarEvent addCalendarEvent(String prefix, CalendarEvent event, String userId, boolean ignoreConflicts, Date createdDate) throws ConflictException, CalendarException {
        try {
            // check for conflicts
            if (!ignoreConflicts) {
                ResourceManager rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);
                Collection resourceConflicts = rm.getBookingConflicts(event);
                Collection conflicts = getCalendarEventConflicts(event, userId);
                if (conflicts != null) {
                    for (Iterator i=conflicts.iterator(); i.hasNext();) {
                        CalendarEvent cf = (CalendarEvent)i.next();
                        if (cf.getEventId().equals(event.getEventId())) {
                            i.remove();
                        }
                    }
                }
                if ((conflicts!=null&&conflicts.size() > 0 )|| (resourceConflicts!=null&&resourceConflicts.size()>0)) {
                    ConflictException ce = new ConflictException();
                    ce.setConflictList(conflicts);
                    ce.setResourcesList(resourceConflicts);
                    throw ce;
                }
            }
            // set recurrence end date - this is used to determine when an recurring event last happens
            Date lastRecurrenceStartDate = CalendarUtil.getLastRecurrenceDate(event);
            if (lastRecurrenceStartDate != null) {
                long duration = event.getEndDate().getTime() - event.getStartDate().getTime();
                Calendar cal = Calendar.getInstance();
                cal.setTime(lastRecurrenceStartDate);
                cal.add(Calendar.MILLISECOND, (int)duration);
                event.setLastRecurrenceDate(cal.getTime());
                //log.debug("addCalendarEvent lastRecurrenceDate: " + event.getLastRecurrenceDate());
            }
            // set instance data
            if (event.getEventId() == null)
                event.setEventId(prefix + "_" + UuidGenerator.getInstance().getUuid());
            event.setUserId(userId);
            event.setNew(true);
            if(createdDate == null)
            {
                event.setCreationDate(new Date());
                event.setLastModified(new Date());
            }
            else
            {
                event.setCreationDate(createdDate);
                event.setLastModified(createdDate);
            }
            event.setLastModifiedBy(event.getUserId());
            event.setInstanceId(DEFAULT_INSTANCE_ID);
            event.setInstanceDate(event.getStartDate());
            if (event.getAttendees() != null) {
                for (Iterator i=event.getAttendees().iterator(); i.hasNext();) {
                    Attendee attendee = (Attendee)i.next();
                    attendee.setEventId(event.getEventId());
                    attendee.setAttendeeId(CalendarUtil.getAttendeeId(event, attendee.getUserId()));
                    attendee.setInstanceId(event.getInstanceId());
                }
            }
            // create event
            Collection col = event.getResources();
            if(col!=null&&col.size()>0){
                ResourceManager rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);
                for(Iterator i=col.iterator();i.hasNext();){
                    Resource resource = (Resource)i.next();
                    rm.bookResource(resource.getId(),event.getEventId(),event.getInstanceId(),userId,event.getStartDate()==null?event.getCreationDate():event.getStartDate(),event.getEndDate(),ignoreConflicts);
                }
            }
            CalendarDao dao = (CalendarDao)getDao();
            dao.insertCalendarEvent(event);
            return event;
        }
        catch(ConflictException e) {
            throw e;
        }
        catch(Exception e) {
            log.error("Error adding calendar event " + event, e);
            throw new CalendarException(e.toString());
        }
    }


    public void addRecurringEvent(CalendarEvent event,String userId, boolean ignoreConflicts)throws ConflictException, CalendarException {
        try{
            /*if (!ignoreConflicts) {
                Collection conflicts = getCalendarEventConflicts(event, userId);
                //log.debug("addCalendarEvent conflicts: " + conflicts);
                if (conflicts != null && conflicts.size() > 0) {
                    ConflictException ce = new ConflictException("Recurring Event Conflict : "+((CalendarEvent)conflicts.iterator().next()).getStartDate()
                    +" - "+((CalendarEvent)conflicts.iterator().next()).getEndDate());
                    ce.setConflictList(conflicts);
                    throw ce;
                }
            }*/
            /*  if (event.getAttendees() != null) {
                for (Iterator i=event.getAttendees().iterator(); i.hasNext();) {
                    Attendee attendee = (Attendee)i.next();
                    attendee.setEventId(event.getEventId());
                    attendee.setAttendeeId(CalendarUtil.getAttendeeId(event, attendee.getUserId()));
                    attendee.setInstanceId(event.getInstanceId());
                }
            }*/
            Collection col = event.getResources();
            if(col!=null&&col.size()>0){
                ResourceManager rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);
                for(Iterator i=col.iterator();i.hasNext();){
                    Resource resource = (Resource)i.next();
                    rm.deleteBooking(resource.getId(),event.getEventId(),event.getInstanceId());
                    rm.bookResource(resource.getId(),event.getEventId(),event.getInstanceId(),userId,event.getStartDate(),event.getEndDate(),true);
                }
            }
            CalendarDao dao = (CalendarDao)getDao();
            dao.insertRecurringEvent(event);
        } catch(ConflictException e) {
            throw e;
        } catch(Exception ie){
            throw new CalendarException(ie.toString());
        }
    }

    public void addEventAttendee(Attendee attendee) throws SQLException, DaoException
    {
        CalendarDao dao = (CalendarDao)getDao();
        dao.insertEventAttendee(attendee);
    }

    public void deleteEventAttendee(Attendee attendee) throws SQLException, DaoException
    {
        CalendarDao dao = (CalendarDao)getDao();
        dao.deleteEventAttendee(attendee);
    }

    public Appointment getAppointment(String id) throws DataObjectNotFoundException, DaoException
    {
        CalendarDao dao = (CalendarDao)getDao();
        Appointment p = (Appointment)dao.selectCalendarEvent(id,Appointment.class);
        return p;
    }

    public CalendarEvent getNextEvent(Date from,String[] userIds){
        CalendarDao dao = (CalendarDao)getDao();
        try{
            CalendarEvent e = null,re = null;
            e = dao.getNextEvent(from,userIds,true);
            re = dao.getNextRecurringEvent(from,userIds,true);
            if(e!=null&&re!=null){
                re.setRecurrence(true);
                if(e.getStartDate().before(re.getStartDate()))
                    return e;
                else
                    return re;
            } else if (re==null)
                return e;
            else
                return re;
        }catch(Exception e){}
        return null;
    }

    public CalendarEvent getPreviousEvent(Date from,String[] userIds){
        CalendarDao dao = (CalendarDao)getDao();
        try{
            CalendarEvent e = null,re = null;
            e = dao.getNextEvent(from,userIds,false);
            re = dao.getNextRecurringEvent(from,userIds,false);
            if(e!=null&&re!=null){
                if(e.getStartDate().after(re.getStartDate()))
                    return e;
                else
                    return re;
            }else if(re==null)
                return e;
            else
                return re;
        }catch(Exception e){}
        return null;
    }

    /**
     * Returns a Collection of CalendarEvent objects within a specified date range,
     * including recurring events.
     * The CalendarEvent objects that are returned are NOT populated with
     * attendee and resource information for performance reasons.
     * Make a separate method call to getCalendarEvent(eventId) for that purpose.
     * @param search A string to search for in the title or description. Use null to return all.
     * @param type The class name of event, e.g. com.tms.collab.calendar.model.Appointment. Use null to return all.
     * @param from The start of the required date range.
     * @param to The end of the required date range.
     * @param userId The user ID of the viewer.
     * @param userIds An array of User IDs - indicating the required users. Use null to return events for all users.
     * @param resourceIds An array of Resource IDs - indicating the required resources. Use null to return events for all resources.
     * @param includeReminders Set true to include reminders.
     * @param includeUniversal Set true to include universal events.
     * @param includeDeleted Set true to include events with the deleted flag.
     * @return A Collection of CalendarEvent objects, never null.
     * @throws com.tms.collab.calendar.model.CalendarException
     */
    public Collection getCalendarEvents(String search, String type, Date from, Date to, String userId, String[] userIds, String[] resourceIds, boolean includeReminders, boolean includeUniversal, boolean includeDeleted, String sort, boolean desc, int start, int rows) throws CalendarException {

        Collection eventList = new TreeSet();

        try {
            // check from and to dates
            Calendar cal = Calendar.getInstance();
            cal.setTime(from);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            from = cal.getTime();
            cal.setTime(to);
            cal.set(Calendar.HOUR, 11);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            to = cal.getTime();

            // get dao
            CalendarDao dao = (CalendarDao)getDao();

            // get events
            Collection events = dao.selectCalendarEvents(search, type, from, to,userId, userIds, resourceIds, false, includeUniversal, includeDeleted, sort, desc, start, rows);
            //eventList.addAll(events);
            eventList = events;
            ResourceManager rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);
            for(Iterator i= eventList.iterator();i.hasNext();){
                CalendarEvent event = (CalendarEvent) i.next();
                event.setAttendees(getAttendees(event.getEventId(),event.getInstanceId()));
                event.setResources(rm.getBookedResources(event.getEventId(),event.getInstanceId()));
            }
            //log.debug("getCalendarEvents: " + eventList);

            if (includeReminders) {
                // get reminders
                Collection reminders = dao.selectCalendarEvents(search, type, from, to, userId,userIds, resourceIds, true, includeUniversal, includeDeleted, sort, desc, start, rows);
                for (Iterator i=reminders.iterator(); i.hasNext();) {
                    CalendarEvent evt = (CalendarEvent)i.next();
                    if(evt.getEventId().startsWith(Task.class.getName())){
                        TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
                        Task task = tm.getTask(evt.getEventId());
                        task.setReminder(true);
                        i.remove();
                        eventList.add(task);
                    }
                    else {
                        Calendar startCal = Calendar.getInstance();
                        Calendar reminderCal = (Calendar)startCal.clone();
                        startCal.setTime(evt.getStartDate());
                        reminderCal.setTime(evt.getReminderDate());
                        if (startCal.get(Calendar.YEAR) == reminderCal.get(Calendar.YEAR) && startCal.get(Calendar.DAY_OF_YEAR) == reminderCal.get(Calendar.DAY_OF_YEAR)) {
                            // ignore reminder on same day as event
                            i.remove();
                        }
                        else {
                            evt.setReminder(true);
                            evt.setAttendees(getAttendees(evt.getEventId(),evt.getInstanceId()));
                            evt.setResources(rm.getBookedResources(evt.getEventId(),evt.getInstanceId()));
                        }
                    }
                }
                eventList.addAll(reminders);
            }

            // get recurring events
            if (search == null || search.trim().length() == 0) {
                Collection recurringCalendarEvents = dao.selectRecurringCalendarEvents(search, type, from, to, userIds, resourceIds,false, includeUniversal);
                for(Iterator i = recurringCalendarEvents.iterator();i.hasNext();){
                    CalendarEvent event = (CalendarEvent) i.next();
                    event.setAttendees(getAttendees(event.getEventId(),event.getInstanceId()));
                    event.setResources(rm.getBookedResources(event.getEventId(),event.getInstanceId()));
                }
                eventList.addAll(recurringCalendarEvents);
            }
            if (includeReminders) {
                Collection reminders = dao.selectRecurringCalendarEvents(search, type, from, to, userIds, resourceIds,true, includeUniversal);
                CalendarEvent evt;
                for (Iterator i=reminders.iterator(); i.hasNext();) {
                    evt = (CalendarEvent)i.next();
                    Calendar startCal = Calendar.getInstance();
                    Calendar reminderCal = (Calendar)startCal.clone();
                    startCal.setTime(evt.getStartDate());
                    reminderCal.setTime(evt.getReminderDate());
                    if (startCal.get(Calendar.YEAR) == reminderCal.get(Calendar.YEAR) && startCal.get(Calendar.DAY_OF_YEAR) == reminderCal.get(Calendar.DAY_OF_YEAR)) {
                        // ignore reminder on same day as event
                        i.remove();
                    }
                    else {
                        evt.setReminder(true);
                        evt.setRecurrence(true);
                    }
                }
                if (reminders!=null) {
                    eventList.addAll(reminders);
                }
            }
            // filter away private and confidential events
            eventList = filterEventList(eventList, userId);

            //log.debug("getCalendarEvents + recurrences: " + eventList);
        }
        catch(Exception e) {
            log.error("Error retrieving calendar events", e);
            throw new CalendarException(e.toString());
        }
        return eventList;
    }

    /**
     * Returns a Collection of CalendarEvent objects in which the lastModified date falls within a specified date range,
     * including recurring events.
     * The CalendarEvent objects that are returned are NOT populated with
     * attendee and resource information for performance reasons.
     * Make a separate method call to getCalendarEvent(eventId) for that purpose.
     * @param search A string to search for in the title or description. Use null to return all.
     * @param type The class name of event, e.g. com.tms.collab.calendar.model.Appointment. Use null to return all.
     * @param from The start of the required date range.
     * @param to The end of the required date range.
     * @param userId The user ID of the viewer.
     * @param userIds An array of User IDs - indicating the required users. Use null to return events for all users.
     * @param resourceIds An array of Resource IDs - indicating the required resources. Use null to return events for all resources.
     * @param includeUniversal Set true to include universal events.
     * @param includeDeleted Set true to include events with the deleted flag.
     * @return A Collection of CalendarEvent objects, never null.
     * @throws com.tms.collab.calendar.model.CalendarException
     */
    public Collection getCalendarEventsByModifiedDate(String search, String type, Date from, Date to, String userId, String[] userIds, String[] resourceIds, boolean includeUniversal, boolean includeDeleted, String sort, boolean desc, int start, int rows) throws CalendarException {

        Collection eventList = new TreeSet();

        try {
            // check from and to dates
            Calendar cal = Calendar.getInstance();
            cal.setTime(from);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            from = cal.getTime();
            cal.setTime(to);
            cal.set(Calendar.HOUR, 11);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            to = cal.getTime();

            // get dao
            CalendarDao dao = (CalendarDao)getDao();

            // get events
            Collection events = dao.selectCalendarEventsByModifiedDate(search, type, from, to,userId, userIds, resourceIds, includeUniversal, includeDeleted, sort, desc, start, rows);
            //eventList.addAll(events);
            eventList = events;
            ResourceManager rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);
            for(Iterator i= eventList.iterator();i.hasNext();){
                CalendarEvent event = (CalendarEvent) i.next();
                event.setAttendees(getAttendees(event.getEventId(),event.getInstanceId()));
                event.setResources(rm.getBookedResources(event.getEventId(),event.getInstanceId()));
            }
            //log.debug("getCalendarEvents: " + eventList);

            // get recurring events
            if (search == null || search.trim().length() == 0) {
                Collection recurringCalendarEvents = dao.selectRecurringCalendarEvents(search, type, from, to, userIds, resourceIds,false, includeUniversal);
                for(Iterator i = recurringCalendarEvents.iterator();i.hasNext();){
                    CalendarEvent event = (CalendarEvent) i.next();
                    event.setAttendees(getAttendees(event.getEventId(),event.getInstanceId()));
                    event.setResources(rm.getBookedResources(event.getEventId(),event.getInstanceId()));

                }
                eventList.addAll(recurringCalendarEvents);
            }
            // filter away private and confidential events
            eventList = filterEventList(eventList, userId);
            //log.debug("getCalendarEvents + recurrences: " + eventList);
        }
        catch(Exception e) {
            log.error("Error retrieving calendar events", e);
            throw new CalendarException(e.toString());
        }
        return eventList;
    }

    /**
     * Returns a count CalendarEvent objects within a specified date range,
     * including recurring events.
     * @param search A string to search for in the title or description. Use null to return all.
     * @param type The class name of event, e.g. com.tms.collab.calendar.model.Appointment. Use null to return all.
     * @param from The start of the required date range.
     * @param to The end of the required date range.
     * @param userId The user ID of the viewer.
     * @param userIds An array of User IDs - indicating the required users. Use null to return events for all users.
     * @param resourceIds An array of Resource IDs - indicating the required resources. Use null to return events for all resources.
     * @param includeReminders Set true to include reminders.
     * @param includeUniversal Set true to include universal events.
     * @param includeDeleted Set true to include events with the deleted flag.
     * @return A count CalendarEvent objects.
     * @throws com.tms.collab.calendar.model.CalendarException
     */
    public int getCalendarEventsCount(String search, String type, Date from, Date to, String userId, String[] userIds, String[] resourceIds, boolean includeReminders, boolean includeUniversal, boolean includeDeleted) throws CalendarException {

        try {
            // check from and to dates
            Calendar cal = Calendar.getInstance();
            cal.setTime(from);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            from = cal.getTime();
            cal.setTime(to);
            cal.set(Calendar.HOUR, 11);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            to = cal.getTime();

            // get dao
            CalendarDao dao = (CalendarDao)getDao();

            // get events count
            int count = dao.selectCalendarEventsCount(search, type, from, to,userId, userIds, resourceIds, false, includeUniversal, includeDeleted);
            return count;
        }
        catch(Exception e) {
            log.error("Error retrieving calendar events count", e);
            throw new CalendarException(e.toString());
        }
    }

    /**
     * Returns a count CalendarEvent objects within 24 hours from now,
     * including recurring events.
     * @param search A string to search for in the title or description. Use null to return all.
     * @param type The class name of event, e.g. com.tms.collab.calendar.model.Appointment. Use null to return all.
     * @param from The start of the required date range.
     * @param userId The user ID of the viewer.
     * @param userIds An array of User IDs - indicating the required users. Use null to return events for all users.
     * @param resourceIds An array of Resource IDs - indicating the required resources. Use null to return events for all resources.
     * @param includeReminders Set true to include reminders.
     * @param includeUniversal Set true to include universal events.
     * @param includeDeleted Set true to include events with the deleted flag.
     * @return A count CalendarEvent objects.
     * @throws com.tms.collab.calendar.model.CalendarException
     */
    public int getCalendarEventsCount(String search, String type, Date from, String userId, String[] userIds, String[] resourceIds, boolean includeReminders, boolean includeUniversal, boolean includeDeleted) throws CalendarException {

        try {
            // check from and to dates
            Calendar cal = Calendar.getInstance();
            Date to = from;
            cal.setTime(to);
            cal.set(Calendar.DAY_OF_MONTH,cal.get(Calendar.DAY_OF_MONTH)+1);
            to = cal.getTime();
            cal.setTime(from);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            from = cal.getTime();
            // get dao
            CalendarDao dao = (CalendarDao)getDao();

            // get events count
            int count = dao.selectCalendarEventsCount(search, type, from, to,userId, userIds, resourceIds, false, includeUniversal, includeDeleted);
            return count;
        }
        catch(Exception e) {
            log.error("Error retrieving calendar events count", e);
            throw new CalendarException(e.toString());
        }
    }

    /**
     * Returns a count CalendarEvent objects within a specified date range,
     * including recurring events.
     * @param search A string to search for in the title or description. Use null to return all.
     * @param type The class name of event, e.g. com.tms.collab.calendar.model.Appointment. Use null to return all.
     * @param from The start of the required date range.
     * @param to The end of the required date range.
     * @param userId The user ID of the viewer.
     * @param userIds An array of User IDs - indicating the required users. Use null to return events for all users.
     * @param resourceIds An array of Resource IDs - indicating the required resources. Use null to return events for all resources.
     * @param includeUniversal Set true to include universal events.
     * @param includeDeleted Set true to include events with the deleted flag.
     * @return A count CalendarEvent objects.
     * @throws com.tms.collab.calendar.model.CalendarException
     */
    public int getCalendarEventsByModifiedDateCount(String search, String type, Date from, Date to, String userId, String[] userIds, String[] resourceIds, boolean includeUniversal, boolean includeDeleted) throws CalendarException {

        try {
            // check from and to dates
            Calendar cal = Calendar.getInstance();
            cal.setTime(from);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            from = cal.getTime();
            cal.setTime(to);
            cal.set(Calendar.HOUR, 11);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            to = cal.getTime();

            // get dao
            CalendarDao dao = (CalendarDao)getDao();

            // get events count
            int count = dao.selectCalendarEventsByModifiedDateCount(search, type, from, to,userId, userIds, resourceIds, includeUniversal, includeDeleted);
            return count;
        }
        catch(Exception e) {
            log.error("Error retrieving calendar events count", e);
            throw new CalendarException(e.toString());
        }
    }

    public Collection getCalendarEvents(DaoQuery properties, boolean includeDeleted,int startIndex,int maxResults,String sort,boolean descending)throws CalendarException{
        try {
            Collection eventList = new TreeSet();
            CalendarDao dao = (CalendarDao)getDao();
            eventList = dao.selectCalendarEvents(properties,includeDeleted,startIndex,maxResults,sort,descending);
            return eventList;
        } catch(Exception e) {
            log.error("Error retrieving calendar events", e);
            throw new CalendarException(e.toString());
        }
    }

    public Collection getRecurringEvents(CalendarEvent event,Date from, Date to){
        Collection eventList = new TreeSet();

        return eventList;
    }

    /**
     * Filters a Collection of CalendarEvent objects by removing
     * confidential events.
     * @param eventList The collection to filter.
     * @param userId The current user ID.
     * @return The filtered Collection.
     */
    protected Collection filterEventList(Collection eventList, String userId) {
        if (eventList != null) {
            for (Iterator i=eventList.iterator(); i.hasNext(); ) {
                CalendarEvent event = (CalendarEvent)i.next();
                try {
                    if (CalendarModule.CLASSIFICATION_PUBLIC.equals(event.getClassification()) || event.getUserId().equals(userId)) {
                        continue;
                    }
                    else if (CalendarModule.CLASSIFICATION_CONFIDENTIAL.equals(event.getClassification())) {
                        if (!getCalendarEvent(event.getEventId()).getAttendeeMap().containsKey(userId))
                            i.remove();
                    }
                    else if (CalendarModule.CLASSIFICATION_PRIVATE.equals(event.getClassification())) {
                        if (!getCalendarEvent(event.getEventId()).getAttendeeMap().containsKey(userId))
                            event.setHidden(true);
                    }
                }
                catch(Exception e) {
                    ;
                }
            }
        }
        return eventList;
    }

	public CalendarEvent getCalendarEventByExternalEventId(String eventId)
		throws DataObjectNotFoundException, CalendarException, DaoException {
	CalendarDao dao = (CalendarDao) getDao();
	return dao.selectCalendarEventByExternalEventId(eventId,
			CalendarEvent.class);
	}

    /**
     * Returns a specific event based on the event's unique identifier,
     * including associated attendees and resources.
     * @param eventId The unique ID to identify the event.
     * @return A CalendarEvent object representing the desired event.
     * @throws kacang.model.DataObjectNotFoundException when the event does not exist.
     * @throws com.tms.collab.calendar.model.CalendarException when other errors occur.
     */
    public CalendarEvent getCalendarEvent(String eventId) throws DataObjectNotFoundException, CalendarException {
        return getCalendarEvent(eventId, DEFAULT_INSTANCE_ID);
    }

    /**
     * Returns a specific event instance (original or recurrence) based on the event's unique identifier and instance ID,
     * including associated attendees and resources.
     * @param eventId The unique ID to identify the event.
     * @param instanceId The instance ID to identify the specific recurrence of an event, null will return the original event.
     * @return A CalendarEvent object representing the desired event.
     * @throws kacang.model.DataObjectNotFoundException when the event does not exist.
     * @throws com.tms.collab.calendar.model.CalendarException when other errors occur.
     */
    public CalendarEvent getCalendarEvent(String eventId, String instanceId) throws DataObjectNotFoundException, CalendarException {
        try {
            // check instance id
            if (instanceId == null || instanceId.trim().length() == 0) {
                instanceId = DEFAULT_INSTANCE_ID;
            }
            boolean recurrence = !DEFAULT_INSTANCE_ID.equals(instanceId);

            // Get event from DAO
            CalendarDao dao = (CalendarDao)getDao();
            CalendarEvent event = dao.selectCalendarEvent(eventId,CalendarEvent.class);

            // get attendee status
            Collection attendeeList = dao.selectAttendeeStatus(eventId, instanceId);
            ResourceManager rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);
            event.setResources(rm.getBookedResources(eventId,instanceId));
/*
            Collection attendees = new TreeSet();
            Map attendeeMap = new HashMap();
            for (Iterator i=attendeeList.iterator(); i.hasNext();) {
                Attendee attendee = (Attendee)i.next();
                attendeeMap.put(attendee.getUserId(), attendee);
            }*//*
            for (Iterator i=event.getAttendees().iterator(); i.hasNext();) {
                Attendee attendee = (Attendee)i.next();
                if (attendeeMap.get(attendee.getUserId()) != null) {
                    attendees.add(attendeeMap.get(attendee.getUserId()));
                }
                else if (attendee.isCompulsory()) {
                    attendee.setStatus(CalendarModule.ATTENDEE_STATUS_CONFIRMED);
                    attendees.add(attendee);
                }
                else {
                    attendee.setStatus(CalendarModule.ATTENDEE_STATUS_PENDING);
                    attendees.add(attendee);
                }
            }
*/
            event.setAttendees(attendeeList);

            if (recurrence) {
                // calculate recurrence date
                Date recurrenceDate = CalendarUtil.getRecurrenceDate(instanceId);

                // get recurring events
                Calendar cal = Calendar.getInstance();
                cal.setTime(recurrenceDate);
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                Date toDate = cal.getTime();
                Collection recurringEvents = CalendarUtil.getRecurringEvents(event, event.getEndDate(), toDate);

                // check each recurring event
                //log.debug("getCalendarEvent recurrenceDate: " + recurrenceDate);
                //log.debug("getCalendarEvent recurringEvents: " + recurringEvents);
                for (Iterator i=recurringEvents.iterator(); i.hasNext();) {
                    CalendarEvent evt = (CalendarEvent)i.next();
                    //log.debug("getCalendarEvent r evt: " + evt.getStartDate() + "-" + evt.getEndDate());
                    if (evt.getStartDate().equals(recurrenceDate)) {
                        event = evt;
                        event.setAttendees(attendeeList);
                        break;
                    }
                }
            }
            return event;
        }
        catch(DataObjectNotFoundException e) {
            throw e;
        }
        catch(Exception e) {
            throw new CalendarException(e.toString());
        }
    }

    public Attendee getAttendee(String eventId,String userId) throws DaoException
    {
        CalendarDao dao = (CalendarDao)getDao();
        Collection col = dao.selectAttendee(eventId,userId);
        if(col!=null&&col.size()>0)
            return (Attendee)col.iterator().next();
        return null;
    }

    public Collection getAttendees(String eventId,String instanceId) throws DataObjectNotFoundException, DaoException
    {
        CalendarDao dao = (CalendarDao)getDao();
        Collection attendeeList = dao.selectAttendeeStatus(eventId, instanceId);
/*        for (Iterator i=attendeeList.iterator(); i.hasNext();) {
            Attendee attendee = (Attendee)i.next();
            System.out.println(attendee.getUserId()+" "+ attendee.getName());
        }
        System.out.println("----------------------------------");*/
        //Collection attendees = new TreeSet();
        return attendeeList;

        /*Map attendeeMap = new HashMap();
        for (Iterator i=attendeeList.iterator(); i.hasNext();) {
            Attendee attendee = (Attendee)i.next();
            attendeeMap.put(attendee.getUserId(), attendee);
        }

        for (Iterator i=dao.selectAttendess(eventId).iterator(); i.hasNext();) {
            Attendee attendee = (Attendee)i.next();
            if (attendeeMap.get(attendee.getUserId()) != null) {
                attendees.add(attendeeMap.get(attendee.getUserId()));
            }
            else if (attendee.isCompulsory()) {
                attendee.setStatus(CalendarModule.ATTENDEE_STATUS_CONFIRMED);
                attendees.add(attendee);
            }
            else {
                attendee.setStatus(CalendarModule.ATTENDEE_STATUS_PENDING);
                attendees.add(attendee);
            }
        }*/
        // return attendees;
    }

    /**
     * Retrieves a Collection of calendar events for a specific day.
     * This method returns events starting from 0000 hours to 2359 hours.
     * @param date Any date within the required day.
     * @param userId The user ID of the viewer.
     * @param userIds An array of User IDs - indicating the required users. Use null to return events for all users.
     * @param resourceIds An array of Resource IDs - indicating the required resources. Use null to return events for all resources.
     * @return A Collection of CalendarEvent objects, never null.
     * @throws com.tms.collab.calendar.model.CalendarException
     */
    public Collection getDailyCalendarEvents(Date date, String userId, String[] userIds, String[] resourceIds) throws CalendarException {
        try {
            // construct date range
            Date startDate = null;
            Date endDate = null;
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            startDate = cal.getTime();
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            endDate = cal.getTime();

            //logging.log(//logging.DEBUG, "getDailyCalendarEvents: " + startDate + "-" + endDate);

            // get events
            Collection eventList = getCalendarEvents(null, null, startDate, endDate, userId, userIds, resourceIds, true, true, false, null, false, 0, -1);
            TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
            Collection tasks =  tm.getCalendarTasks(startDate,endDate,userIds,true);
            for (Iterator iterator = tasks.iterator(); iterator.hasNext();)
            {
                Task task = (Task)iterator.next();
                task.setAttendees(tm.getAssignees(task.getId()));
            }
            filterEventList(tasks,userId);
            eventList.addAll(tasks);
            return eventList;
        }
        catch(Exception e) {
            log.error("Error retrieving daily calendar events", e);
            throw new CalendarException(e.toString());
        }
    }

    /**
     * Retrieves a Collection of calendar events for a specific week.
     * This method returns events starting from 0000 hours on Sunday to 2359 hours on Saturday.
     * @param date Any date within the required week.
     * @param userId The user ID of the viewer.
     * @param userIds An array of User IDs - indicating the required users. Use null to return events for all users.
     * @param resourceIds An array of Resource IDs - indicating the required resources. Use null to return events for all resources.
     * @param excludeTasks
     * @return A Collection of CalendarEvent objects, never null.
     * @throws com.tms.collab.calendar.model.CalendarException
     */
    public Collection getWeeklyCalendarEvents(Date date, String userId, String[] userIds, String[] resourceIds, boolean excludeTasks) throws CalendarException {
        try {
            // construct date range
            Date startDate = null;
            Date endDate = null;
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            while(cal.get(Calendar.DAY_OF_WEEK) != cal.getFirstDayOfWeek()) {
                cal.add(Calendar.DAY_OF_MONTH, -1);
            }
            startDate = cal.getTime();
            cal.add(Calendar.WEEK_OF_MONTH, 1);
            cal.add(Calendar.SECOND, -1);
            endDate = cal.getTime();

            //logging.log(//logging.DEBUG, "getWeeklyCalendarEvents: " + startDate + "-" + endDate);

            // get events
            Collection eventList = getCalendarEvents(null, null, startDate, endDate, userId, userIds, resourceIds, true, true, false, null, false, 0, -1);

            if (!excludeTasks) {
                TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
                Collection tasks =  tm.getCalendarTasks(startDate,endDate,userIds,true);
                Collection nonarchivetasks=new ArrayList();
                for (Iterator iterator = tasks.iterator(); iterator.hasNext();)
                {
                    Task task = (Task) iterator.next();
                    String archived=(String)task.getProperty("projectArchived");
    				if(archived==null||"0".equals(archived)){
    					nonarchivetasks.add(task);
    				}
                }
                filterEventList(nonarchivetasks,userId);
                eventList.addAll(nonarchivetasks);
                //filterEventList(tasks,userId);
                //eventList.addAll(tasks);
            }
            return eventList;
        }
        catch(Exception e) {
            log.error("Error retrieving weekly calendar events", e);
            throw new CalendarException(e.toString());
        }
    }

    /**
     * Retrieves a Collection of calendar events for a specific month.
     * This method returns events starting from 0000 hours on the first day of the month
     * to 2359 hours on the last day of the month.
     * @param date Any date within the required month.
     * @param userId The user ID of the viewer.
     * @param userIds An array of User IDs - indicating the required users. Use null to return events for all users.
     * @param resourceIds An array of Resource IDs - indicating the required resources. Use null to return events for all resources.
     * @return A Collection of CalendarEvent objects, never null.
     * @throws com.tms.collab.calendar.model.CalendarException
     */
    public Collection getMonthlyCalendarEvents(Date date, String userId, String[] userIds, String[] resourceIds) throws CalendarException {
        try {
            // construct date range
            Date startDate = null;
            Date endDate = null;
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            startDate = cal.getTime();
            cal.add(Calendar.MONTH, 1);
            cal.add(Calendar.SECOND, -1);
            endDate = cal.getTime();

            //logging.log(//logging.DEBUG, "getMonthlyCalendarEvents: " + startDate + "-" + endDate);

            // get events
            Collection evt=new TreeSet();
            Collection eventList = getCalendarEvents(null, null, startDate, endDate, userId, userIds, resourceIds, true, true, false, null, false, 0, -1);
            TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
            Collection tasks =  tm.getCalendarTasks(startDate,endDate,userIds,true);
            evt=eventList;
            if (evt != null) {
                for (Iterator i=evt.iterator(); i.hasNext(); ) {
                    CalendarEvent event = (CalendarEvent)i.next();
                    try {
                    	if(event.getEventId().startsWith(Task.class.getName())){
                            Task task = tm.getTask(event.getEventId());
                            String archived=(String)task.getProperty("projectArchived");
            				if(!(archived==null||"0".equals(archived))){
            					eventList.remove(event);
            				}

                        }
                        else {                           
                        }
                    }
                    catch(Exception e) {
                        ;
                    }
                }
            }
            Collection nonarchivetasks=new ArrayList();
            for (Iterator iterator = tasks.iterator(); iterator.hasNext();)
            {
                Task task = (Task) iterator.next();
                String archived=(String)task.getProperty("projectArchived");
				if(archived==null||"0".equals(archived)){
					nonarchivetasks.add(task);
				}
            }
            eventList.addAll(nonarchivetasks);
            //eventList.addAll(tasks);

            return eventList;
        }
        catch(Exception e) {
            log.error("Error retrieving monthly calendar events", e);
            throw new CalendarException(e.toString());
        }
    }

    /**
     * Retrieves conflicting events (if any) for an event.
     * @param event The event to check for.
     * @param userId The user ID to check for.
     * @return A Collection of CalendarEvent objects representing conflicting events.
     *         Returns empty Collection if there are no conflicts.
     * @throws com.tms.collab.calendar.model.CalendarException
     */
    public Collection getCalendarEventConflicts(CalendarEvent event, String userId) throws CalendarException {

        Collection conflictList = new TreeSet();
        try {
            // Construct list of attendees and resources
            String[] userIds = null;
            String[] resourceIds = null;
            if (event.getAttendees() != null) {
                int count = 0;
                userIds = new String[event.getAttendees().size()];
                for (Iterator i=event.getAttendees().iterator(); i.hasNext(); count++) {
                    Attendee attendee = (Attendee)i.next();
                    userIds[count] = attendee.getUserId();
                }
                if(userIds.length<1){
                    return null;
                }
            }
            /* if (event.getResources() != null) {
                int count = 0;
                resourceIds = new String[event.getResources().size()];
                for (Iterator i=event.getResources().iterator(); i.hasNext(); count++) {
                    Resource resource = (Resource)i.next();
                    resourceIds[count] = resource.getId();
                }
            }*/

            // Check for conflicting events
            ResourceManager rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);
            CalendarDao dao = (CalendarDao)getDao();
            Collection conflicts = dao.selectCalendarEventConflicts(event.getStartDate(), event.getEndDate(), userIds, resourceIds, true);
            for (Iterator iterator = conflicts.iterator(); iterator.hasNext();)
            {
                CalendarEvent ce = (CalendarEvent) iterator.next();
                if (ce.getId().startsWith(Task.class.getName())) {
                    // ignore task conflicts
                    iterator.remove();
                }
                else {
                    ce.setAttendees(getAttendees(ce.getEventId(),ce.getInstanceId()));
                    rm.getBookedResources(ce.getEventId(),ce.getInstanceId());
                }
            }
            conflictList.addAll(conflicts);
            //log.debug("getCalendarEventConflicts conflicts: " + conflicts);

            // get recurring events
            Collection recurringCalendarEvents = dao.selectRecurringCalendarEvents(null, null, event.getStartDate(), event.getEndDate(), userIds, resourceIds,false, true);
            //log.debug("getCalendarEventConflicts recurringCalendarEvents: " + recurringCalendarEvents);
            for (Iterator iterator = recurringCalendarEvents.iterator(); iterator.hasNext();)
            {
                CalendarEvent ce = (CalendarEvent) iterator.next();
                ce.setAttendees(getAttendees(ce.getEventId(),ce.getInstanceId()));
                ce.setResources(rm.getBookedResources(ce.getEventId(), ce.getInstanceId()));
            }
            conflictList.addAll(recurringCalendarEvents);

            if((event.getRecurrenceRule()!=null&&event.getRecurrenceRule().trim().length()>0)||event.getRecurrenceDates()!=null){
                Collection recurringEvents = CalendarUtil.getRecurringEvents(event, event.getStartDate(), event.getEndDate());
                // check each recurring event
                for (Iterator i=recurringEvents.iterator(); i.hasNext();) {
                    CalendarEvent ce = (CalendarEvent)i.next();
                    //log.debug("getCalendarEventConflicts recurringEvents: " + recurringEvents);
                    conflicts = dao.selectCalendarEventConflicts(ce.getStartDate(), ce.getEndDate(), userIds, resourceIds, true);
                    for (Iterator iterator = conflicts.iterator(); iterator.hasNext();)
                    {
                        CalendarEvent e = (CalendarEvent) iterator.next();
                        e.setAttendees(getAttendees(e.getEventId(),e.getInstanceId()));
                        e.setResources(rm.getBookedResources(e.getEventId(),e.getInstanceId()));
                    }
                    conflictList.addAll(conflicts);
                    conflicts = dao.selectRecurringCalendarEvents(null, null, ce.getStartDate(),ce.getEndDate(),userIds,resourceIds,false, true);
                    for (Iterator iterator = conflicts.iterator(); iterator.hasNext();)
                    {
                        CalendarEvent e = (CalendarEvent) iterator.next();
                        e.setAttendees(getAttendees(e.getEventId(),e.getInstanceId()));
                        e.setResources(rm.getBookedResources(e.getEventId(),e.getInstanceId()));
                    }
                    conflictList.addAll(conflicts);
                }
            }
            //log.debug("getCalendarEventConflicts conflictList: " + conflictList);

            // filter away private and confidential events
            conflictList = filterEventList(conflictList, userId);

            // filter away attendees who rejected the appointment
            if (conflictList != null) {
                for (Iterator i=conflictList.iterator(); i.hasNext(); ) {
                    CalendarEvent evt = (CalendarEvent)i.next();
                    // remove event that conflicts with itself
                    if (evt.getId().equals(event.getId()) && evt.getInstanceId().equals(event.getInstanceId())) {
                        i.remove();
                        continue;
                    }
                    Collection attendees = evt.getAttendees();
                    for (Iterator j=attendees.iterator(); j.hasNext();) {
                        Attendee att = (Attendee)j.next();
                        if (CalendarModule.ATTENDEE_STATUS_REJECT.equals(att.getStatus())) {
                            j.remove();
                        }
                    }
                    evt.setAttendees(attendees);
                }
            }
            return conflictList;
        }
        catch(Exception e) {
            log.error("Error retrieving calendar event conflicts", e);
            throw new CalendarException(e.toString());
        }
    }

    /**
     * Deletes an existing event. This only sets a flag to indicate that the event
     * is deleted. The event data is still intact.
     * Use destroyCalendarEvent() to permanently delete an event.
     * @param eventId The unique identifier for the event.
     * @param userId The user deleting the event.
     * @throws kacang.model.DataObjectNotFoundException when the event does not exist.
     * @throws com.tms.collab.calendar.model.CalendarException when other errors occur.
     */
    public void deleteCalendarEvent(String eventId, String userId) throws DataObjectNotFoundException, CalendarException {
        CalendarEvent event = getCalendarEvent(eventId);
        ResourceManager rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);
        rm.deleteBooking(eventId);
        event.setDeleted(true);
        event.setLastModified(new Date());
        event.setLastModifiedBy(userId);
        updateCalendarEventStatus(event);
    }

    /**
     * Deletes an existing event. This deletes all related data including attendees, resources, instances and status.
     * All data cannot be recovered.
     * @param eventId The unique identifier for the event.
     * @throws com.tms.collab.calendar.model.CalendarException
     */
    public void destroyCalendarEvent(String eventId) throws CalendarException {
        try {
            ResourceManager rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);
            rm.deleteBooking(eventId);
            CalendarDao dao = (CalendarDao)getDao();
            dao.deleteCalendarEvent(eventId);
        }
        catch(Exception e) {
            log.error("Error destroying calendar event " + eventId, e);
            throw new CalendarException(e.toString());
        }
    }

    public void deleteRecurringEvents(CalendarEvent event)throws CalendarException{
        try {
            CalendarDao dao = (CalendarDao)getDao();
            dao.deleteRecurringEvents(event);
        }
        catch(Exception e) {
            log.error("Error deleting calendar events", e);
            throw new CalendarException(e.toString());
        }
    }

    public void deleteRecurringEvent(CalendarEvent event)throws CalendarException{
        try {
            CalendarDao dao = (CalendarDao)getDao();
            dao.deleteRecurringEvent(event);
        }
        catch(Exception e) {
            log.error("Error deleting calendar event", e);
            throw new CalendarException(e.toString());
        }
    }

    /**
     * Updates an event.
     * @param event The event to update.
     * @param ignoreConflicts set true to ignore conflicts.
     * @throws kacang.model.DataObjectNotFoundException if the event does not exist.
     * @throws com.tms.collab.calendar.model.ConflictException If there are conflicting events. Obtain these conflicting events from the ConflictException class.
     * @throws com.tms.collab.calendar.model.CalendarException
     */
    public CalendarEvent updateCalendarEvent(CalendarEvent event, String userId, boolean ignoreConflicts) throws DataObjectNotFoundException, ConflictException, CalendarException {
        return updateCalendarEvent(event, userId, ignoreConflicts, new Date());
    }

    /**
     * Updates an event.
     * @param event The event to update.
     * @param ignoreConflicts set true to ignore conflicts.
     * @param modifiedDate sets to current date if null value
     * @throws kacang.model.DataObjectNotFoundException if the event does not exist.
     * @throws com.tms.collab.calendar.model.ConflictException If there are conflicting events. Obtain these conflicting events from the ConflictException class.
     * @throws com.tms.collab.calendar.model.CalendarException
     */
    public CalendarEvent updateCalendarEvent(CalendarEvent event, String userId, boolean ignoreConflicts, Date modifiedDate) throws DataObjectNotFoundException, ConflictException, CalendarException {
        try {
            // check for conflicts
            if (!ignoreConflicts) {
                ResourceManager rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);
                Collection resourceConflicts = rm.getBookingConflicts(event);
                Collection conflicts = getCalendarEventConflicts(event, userId);
                if (conflicts != null) {
                    for (Iterator i=conflicts.iterator(); i.hasNext();) {
                        CalendarEvent cf = (CalendarEvent)i.next();
                        if (cf.getEventId().equals(event.getEventId())) {
                            i.remove();
                        }
                    }
                }
                if ((conflicts!=null&&conflicts.size() > 0 )|| (resourceConflicts!=null&&resourceConflicts.size()>0)) {
                    ConflictException ce = new ConflictException();
                    ce.setConflictList(conflicts);
                    ce.setResourcesList(resourceConflicts);
                    throw ce;
                }
            }

            // set recurrence end date - this is used to determine when an recurring event last happens
            Date lastRecurrenceStartDate = CalendarUtil.getLastRecurrenceDate(event);
            if (lastRecurrenceStartDate != null) {
                long duration = event.getEndDate().getTime() - event.getStartDate().getTime();
                Calendar cal = Calendar.getInstance();
                cal.setTime(lastRecurrenceStartDate);
                cal.add(Calendar.MILLISECOND, (int)duration);
                event.setLastRecurrenceDate(cal.getTime());
                //log.debug("updateCalendarEvent lastRecurrenceDate: " + event.getLastRecurrenceDate());
            }

            // get existing event
            CalendarEvent currentEvent = getCalendarEvent(event.getEventId());

            // update
            CalendarDao dao = (CalendarDao)getDao();
            event.setEventId(currentEvent.getEventId());
            event.setInstanceId(DEFAULT_INSTANCE_ID);
            event.setUserId(currentEvent.getUserId());
            event.setNew(false);
            event.setModified(true);
            if(modifiedDate == null)
                event.setLastModified(new Date());
            else
                event.setLastModified(modifiedDate);
            event.setLastModifiedBy(userId);
            if (event.getAttendees() != null) {
                for (Iterator i=event.getAttendees().iterator(); i.hasNext();) {
                    Attendee attendee = (Attendee)i.next();
                    attendee.setEventId(event.getEventId());
                    attendee.setAttendeeId(CalendarUtil.getAttendeeId(event, attendee.getUserId()));
                    attendee.setInstanceId(event.getInstanceId());
                }
            }
            ResourceManager rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);
            rm.deleteBooking(event.getEventId());
            Collection col = event.getResources();
            if(col!=null&&col.size()>0){
                for(Iterator i=col.iterator();i.hasNext();){
                    Resource resource = (Resource)i.next();

                    rm.updateBooking(resource.getId(),event.getEventId(),event.getInstanceId(),userId,event.getStartDate()==null?event.getCreationDate():event.getStartDate(),event.getEndDate());
                }
            }
            dao.updateCalendarEvent(event);
            return event;
        }
        catch(DataObjectNotFoundException e) {
            throw e;
        }
        catch(ConflictException e) {
            throw e;
        }
        catch(Exception e) {
            log.error("Error updating calendar event " + event, e);
            throw new CalendarException(e.toString());
        }
    }

    /**
     * Updates an event's status.
     * @param event The event to update.
     * @throws com.tms.collab.calendar.model.CalendarException
     */
    public void updateCalendarEventStatus(CalendarEvent event) throws CalendarException {
        try {
            CalendarDao dao = (CalendarDao)getDao();
            dao.updateCalendarEventStatus(event);
        }
        catch(Exception e) {
            log.error("Error updating calendar event status", e);
            throw new CalendarException(e.toString());
        }
    }

    /**
     * Updates an attendee's status (CONFIRMED, DECLINED, etc) for an instance of the event.
     * @param eventId The event ID.
     * @param instanceId The event's instance ID.
     * @param userId The user ID for the attendee.
     * @param status The new status for the attendee.
     * @param comments Comments posted by the attendee.
     * @throws kacang.model.DataObjectNotFoundException if the attendee or event is unavailable.
     * @throws com.tms.collab.calendar.model.CalendarException for other errors.
     */
    public void updateAttendeeStatus(String eventId, String instanceId, String userId, String status, String comments) throws DataObjectNotFoundException, CalendarException {
        try {
            // get event
            CalendarEvent event = getCalendarEvent(eventId);
            if (instanceId == null || instanceId.trim().length() == 0) {
                if (event.isRecurrence()) {
                    instanceId = CalendarUtil.getRecurrenceInstanceId(event);
                }
                else {
                    instanceId = CalendarModule.DEFAULT_INSTANCE_ID;
                }
            }
            event.setInstanceId(instanceId);

            // locate attendee
            Attendee attendee = null;
            for (Iterator i=event.getAttendees().iterator(); i.hasNext();) {
                Attendee att = (Attendee)i.next();
                if (userId.equals(att.getUserId())) {
                    attendee = att;
                }
            }

            // update attendee status
            if (attendee != null) {
                attendee.setStatus(status);
                attendee.setComments(comments);
                attendee.setInstanceId(event.getInstanceId());
                CalendarDao dao = (CalendarDao)getDao();
                dao.updateAttendeeStatus(event, attendee);
            }
            else {
                throw new DataObjectNotFoundException();
            }
        }
        catch(DataObjectNotFoundException e) {
            throw e;
        }
        catch(Exception e) {
            log.error("Error updating calendar attendee status", e);
            throw new CalendarException(e.toString());
        }
    }

//  Added by Arun on july 12th 2006
	public void updateAttendeeStatus(String eventId, String attendeeEmail,
			String status, String comments) throws DataObjectNotFoundException,
			CalendarException {
		try {
			// get event
			CalendarEvent event = getCalendarEvent(eventId);
			event.setInstanceId(CalendarModule.DEFAULT_INSTANCE_ID);

			// locate attendee
			Attendee attendee = null;
			for (Iterator i = event.getAttendees().iterator(); i.hasNext();) {
				Attendee att = (Attendee) i.next();
				if (attendeeEmail.equals(att.getProperty("username"))) {
					attendee = att;
				}
			}

			// update attendee status
			if (attendee != null) {
				attendee.setStatus(status);
				attendee.setComments(comments);
				attendee.setInstanceId(event.getInstanceId());
				CalendarDao dao = (CalendarDao) getDao();
				dao.updateAttendeeStatus(event, attendee);
			} else {
				throw new DataObjectNotFoundException();
			}
		} catch (DataObjectNotFoundException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error updating calendar attendee status", e);
			throw new CalendarException(e.toString());
		}
	}
	
    public SearchResult search(String query, int start, int rows, String userId) throws QueryException {
        try {
            Calendar startCal = Calendar.getInstance();
            Calendar endCal = Calendar.getInstance();
            startCal.set(1970, 0, 1);
            endCal.set(3000, 0, 1);
            // perform search
            Collection list = getCalendarEvents(query, null, startCal.getTime(), endCal.getTime(), userId, new String[] {userId}, null, false, false, false, null, false, start, rows);
            int count = getCalendarEventsCount(query, null, startCal.getTime(), endCal.getTime(), userId, new String[] {userId}, null, false, false, false);

            return assembleSearchResult(list, count);
        }
        catch (CalendarException e) {
            log.error("Error retrieving search results: " + e.toString(), e);
            throw new QueryException("Error retrieving search results: " + e.toString());
        }
    }

    public SearchResult searchFullText(String query, int start, int rows, String userId) throws QueryException {
        return new SearchResult();
    }

    public SearchResult search(String query, int start, int rows, String userId, Date startDate, Date endDate) throws QueryException
    {
        try {
            Calendar startCal = Calendar.getInstance();
            Calendar endCal = Calendar.getInstance();
            startCal.setTime(startDate);
            endCal.setTime(endDate);
            // perform search
            Collection list = getCalendarEventsByModifiedDate(query, null, startCal.getTime(), endCal.getTime(), userId, new String[] {userId}, null, false, false, null, false, start, rows);
            int count = getCalendarEventsByModifiedDateCount(query, null, startCal.getTime(), endCal.getTime(), userId, new String[] {userId}, null, false, false);

            return assembleSearchResult(list, count);
        }
        catch (CalendarException e) {
            log.error("Error retrieving search results: " + e.toString(), e);
            throw new QueryException("Error retrieving search results: " + e.toString());
        }
    }

    public SearchResult searchFullText(String query, int start, int rows, String userId, Date startDate, Date endDate) throws QueryException
    {
        return new SearchResult();
    }

    protected SearchResult assembleSearchResult(Collection list, int count)
    {
        SearchResult sr = new SearchResult();
        for (Iterator i=list.iterator(); i.hasNext();) {
            CalendarEvent c = (CalendarEvent)i.next();
            SearchResultItem item = new SearchResultItem();
            Map valueMap = new HashMap();
            valueMap.put(SearchableModule.SEARCH_PROPERTY_KEY, c.getEventId());
            valueMap.put(SearchableModule.SEARCH_PROPERTY_MODULE_CLASS, getClass().getName());
            String prefix = "";
            if(c.getEventId().startsWith(Task.class.getName()))
                prefix = Application.getInstance().getMessage("calendar.label.task");
            else if(c.getEventId().startsWith(Appointment.class.getName()))
                prefix = Application.getInstance().getMessage("calendar.label.appointment");
            else if(c.getEventId().startsWith(Meeting.class.getName()))
                prefix = Application.getInstance().getMessage("com.tms.collab.emeeting.MeetingHandler");
            else if(c.getEventId().startsWith(CalendarEvent.class.getName()))
                prefix = Application.getInstance().getMessage("calendar.label.events");
            valueMap.put(SearchableModule.SEARCH_PROPERTY_TITLE, prefix + ": " + c.getTitle());
            String description = "";
            try
            {
                StringUtil util = StringUtil.getInstance();
                description = util.filterLength(util.filterHtml(c.getDescription()), 200);
            }
            catch (ParserException e) {}
            valueMap.put(SEARCH_PROPERTY_DESCRIPTION, description);
            valueMap.put(SearchableModule.SEARCH_PROPERTY_DATE, c.getLastModified());
            item.setValueMap(valueMap);
            sr.add(item);
        }
        sr.setTotalSize(count);
        return sr;
    }

    public boolean isSearchSupported() {
        return true;
    }

    public boolean isFullTextSearchSupported() {
        return false;
    }

     //----------------------------------PIMSyncService methods---------------------------

    public Collection getCalendarKeys(String userId, String[] userIds, boolean includeUniversal, boolean includeDeleted, Date sinceDate, Date untilDate, String type) throws DaoException {
        CalendarDao dao = (CalendarDao) getDao();
        Collection list = dao.getCalendarKeys(userId, userIds, includeUniversal, includeDeleted, sinceDate, untilDate, type);

        return list;
    }

    public CalendarEvent getCalendarEventByCriteria(String ownerId, CalendarEvent cal) throws DataObjectNotFoundException {
        CalendarDao dao = (CalendarDao) getDao();
        Collection keys = dao.getCalendarEventKeysByCriteria(cal);

        for(Iterator itr=keys.iterator();itr.hasNext();){
            String key = (String) itr.next();
            try {
                CalendarEvent ce = this.getCalendarEvent(key);
                // check if this person can view this calendar object or not
                boolean validView = ownerId.equals(ce.getUserId());
                Collection attendees = ce.getAttendees();
                for (Iterator i=attendees.iterator(); i.hasNext();) {
                    Attendee attendee = (com.tms.collab.calendar.model.Attendee)i.next();
                    if (ownerId.equals(attendee.getUserId())) {
                        validView = true;
                        break;
                    }
                }

                if(validView) return ce;
            } catch (CalendarException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return null;
    }
}
