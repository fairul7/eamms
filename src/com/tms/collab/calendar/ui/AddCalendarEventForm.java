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

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

/**
 * Form for adding a calendar event.
 */
public class AddCalendarEventForm extends Form {

    protected String eventId;
    protected TextField title;
    protected Validator validTitle;
    protected DateField startDate;
    protected DateField endDate;
    protected TimeField startTime;
    protected TimeField endTime;
    protected TextField location;
    protected TextBox description;
    protected Button submitButton;

    public AddCalendarEventForm() {
    }

    public void init() {
        removeChildren();
        setMethod("POST");

        Application application = Application.getInstance();
        title = new TextField("title");
        validTitle = new ValidatorNotEmpty("validTitle");
        title.addChild(validTitle);
        startDate = new DateField("startDate");
        endDate = new DateField("endDate");
        startTime = new TimeField("startTime");
        endTime = new TimeField("endTime");
        location = new TextField("location");
        description = new RichTextBox("description");
        description.setCols("60");
        description.setRows("20");

        submitButton = new Button("submitButton");

		if(isEditMode()) {
			submitButton.setText(application.getMessage("general.label.update","Update"));
		} else {
        	submitButton.setText(application.getMessage("general.label.submit", "Submit"));
		}

        addChild(title);
        addChild(startDate);
        addChild(endDate);
        addChild(startTime);
        addChild(endTime);
        addChild(location);
        addChild(description);
        addChild(submitButton);
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Forward onSubmit(Event evt) {

        // check that end date is equal to or after start date
        Forward result = super.onSubmit(evt);
        if (startDate.getDate().after(endDate.getDate())) {
            startDate.setInvalid(true);
            endDate.setInvalid(true);
            setInvalid(true);
        }
        return result;
    }

    public Forward onValidate(Event evt) throws RuntimeException {
        // create event
        createEvent(evt.getRequest());

        // reset form
        init();
        return new Forward("success");
    }

    protected void createEvent(HttpServletRequest request) throws RuntimeException {
        // get current user
        User user = getWidgetManager().getUser();
        String userId = user.getId();

        // create event object
        CalendarEvent event = new CalendarEvent();
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
            handler.addCalendarEvent(CalendarEvent.class.getName(), event, userId, true);
        }
        catch(Exception e) {
            throw new RuntimeException(e.toString());
        }

    }

    public String getDefaultTemplate() {
        return "cms/calendar/addCalendarEventForm";
    }

	public boolean isEditMode() {
		return false;
	}


}
