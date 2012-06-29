package com.tms.collab.calendar.ui;

import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.calendar.model.CalendarModule;
import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.stdui.validator.Validator;
import kacang.util.Log;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

/**
 * Form for editing a calendar event.
 */
public class EditCalendarEventForm extends AddCalendarEventForm {

    private String eventId;

    public EditCalendarEventForm() {
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void onRequest(Event evt) {
        populateForm();
    }

    public Forward onValidate(Event evt) throws RuntimeException {
        // create event
        updateEvent(evt.getRequest());

        // reset form
        populateForm();
        return new Forward("success");
    }

    protected void populateForm() {
        try {
            Application application = Application.getInstance();
            CalendarModule handler = (CalendarModule)application.getModule(CalendarModule.class);
            CalendarEvent event = handler.getCalendarEvent(getEventId());
            title.setValue(event.getTitle());
            description.setValue(event.getDescription());
            location.setValue(event.getLocation());
            startDate.setDate(event.getStartDate());
            startTime.setDate(event.getStartDate());
            endDate.setDate(event.getEndDate());
            endTime.setDate(event.getEndDate());
        }
        catch (Exception e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }
    }

    protected void updateEvent(HttpServletRequest request) throws RuntimeException {
        // get current user
        User user = getWidgetManager().getUser();
        String userId = user.getId();

        // create event object
        CalendarEvent event = new CalendarEvent();
        event.setEventId(getEventId());
        event.setTitle(title.getValue().toString());
        event.setDescription(description.getValue().toString());
        event.setLocation(location.getValue().toString());
        event.setUniversal(true);

        // set dates
        Calendar startCal = startDate.getCalendar();
        Calendar startTimeCal = startTime.getCalendar();
        startCal.set(Calendar.HOUR_OF_DAY, startTimeCal.get(Calendar.HOUR_OF_DAY));
        startCal.set(Calendar.MINUTE, startTimeCal.get(Calendar.MINUTE));
        event.setStartDate(startCal.getTime());
        Calendar endCal = endDate.getCalendar();
        Calendar endTimeCal = endTime.getCalendar();
        endCal.set(Calendar.HOUR_OF_DAY, endTimeCal.get(Calendar.HOUR_OF_DAY));
        endCal.set(Calendar.MINUTE, endTimeCal.get(Calendar.MINUTE));
        event.setEndDate(endCal.getTime());

        // invoke handler
        try {
            Application application = Application.getInstance();
            CalendarModule handler = (CalendarModule)application.getModule(CalendarModule.class);
            handler.updateCalendarEvent(event, userId, true);
        }
        catch(Exception e) {
            throw new RuntimeException(e.toString());
        }

    }

    public String getDefaultTemplate() {
        return "cms/calendar/editCalendarEventForm";
    }

	public boolean isEditMode() {
		return true;
	}

}
