package test.com.tms.collab.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.util.JUnitTestCase;
import kacang.util.Log;

import com.tms.collab.calendar.model.Appointment;
import com.tms.collab.calendar.model.Attendee;
import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.calendar.model.CalendarModule;
import com.tms.collab.calendar.model.ConflictException;

/**
 * Unit tests for the Calendar Module
 * PREREQUISITE: requires a user with the username "admin"
 */
public class TestCalendarModule extends JUnitTestCase {

    Log log = Log.getLog(this.getClass());
	User adminUser = null;
    
	public TestCalendarModule() {
		
	}
	
	public TestCalendarModule(String test) {
		super(test);
	}
	
	protected void setUp() throws Exception {
		Application application = Application.getInstance();
		SecurityService security = (SecurityService)application.getService(SecurityService.class);
		Collection matchingUsers = security.getUsersByUsername("admin");
		if (matchingUsers.iterator().hasNext()) {
			adminUser = (User)matchingUsers.iterator().next();
		}
	}

	/**
	 * creates a calendar event lasting 1 hour at the current date and time for a user with the username "admin", conflicts ignored
	 *
	 */
	public void testAddAppointment() throws Exception {

		// create calendar event data object
        CalendarEvent event = generateEvent();
		
		// call module to add
		Application application = Application.getInstance();
        CalendarModule calendar = (CalendarModule)application.getModule(CalendarModule.class);
		calendar.addCalendarEvent(Appointment.class.getName(), event, adminUser.getId(), true);
		
		// get event from module
		String eventId = event.getEventId();
		CalendarEvent testEvent = calendar.getCalendarEvent(eventId);

		Assert.assertEquals("Event ID Mismatch", eventId, testEvent.getEventId());
		Assert.assertEquals("Event Title Mismatch", event.getTitle(), testEvent.getTitle());
		Assert.assertEquals("Event Description Mismatch", event.getDescription(), testEvent.getDescription());
		Assert.assertEquals("Event Attendee Mismatch", ((Attendee)event.getAttendees().iterator().next()).getUserId(), ((Attendee)testEvent.getAttendees().iterator().next()).getUserId());
		
	}
	
	/**
	 * attempts to create a calendar event lasting 1 hour at the current date and time for a user with the username "admin", conflict expected
	 *
	 */
	public void testAppointmentConflict() throws Exception {

		// create calendar event data object
        CalendarEvent event = generateEvent();
		
		// call module to add
		try {
			Application application = Application.getInstance();
			CalendarModule calendar = (CalendarModule)application.getModule(CalendarModule.class);
			calendar.addCalendarEvent(Appointment.class.getName(), event, adminUser.getId(), false);
			
			// no conflicts, so test failed
			Assert.fail("Conflict checking failed");
		} 
		catch (ConflictException e) {
			
			Collection conflictList = e.getConflictList();
			Assert.assertTrue("Conflict appointments not found", conflictList.iterator().hasNext());
			
		}

	}	
	
	protected void tearDown() throws Exception {
		// delete created appointments?
	}

	protected CalendarEvent generateEvent() {
        // get start and end times
        Calendar start = Calendar.getInstance();
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        Calendar end = (Calendar)start.clone();
        end.add(Calendar.HOUR_OF_DAY, 1);

		// create calendar event data object
        CalendarEvent event = new CalendarEvent();
        event.setTitle("Test Appointment " + start.getTime());
        event.setDescription("Description for test appointment " + start.getTime());
        event.setStartDate(start.getTime());
        event.setEndDate(end.getTime());
        event.setClassification(CalendarModule.CLASSIFICATION_PUBLIC);
        
        // set attendee
        List attendeeList = new ArrayList();
        Attendee att = new Attendee();
        att.setUserId(adminUser.getId());
        att.setProperty("username", adminUser.getUsername());
        att.setProperty("firstName", adminUser.getProperty("firstName"));
        att.setProperty("lastName", adminUser.getProperty("lastName"));
        att.setCompulsory(true);
        att.setStatus(CalendarModule.ATTENDEE_STATUS_CONFIRMED);
        attendeeList.add(att);
        event.setAttendees(attendeeList);

        return event;
	}

}
