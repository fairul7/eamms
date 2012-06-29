package com.tms.collab.calendar.ui;

import com.tms.collab.calendar.model.*;
import com.tms.collab.calendar.model.Attendee;
import com.tms.collab.directory.model.AddressBookDao;
import com.tms.collab.directory.model.AddressBookModule;
import com.tms.collab.directory.model.Contact;
import com.tms.collab.directory.model.DirectoryModule;
import com.tms.collab.directory.ui.AddressBookSelectBox;
import com.tms.collab.messaging.model.*;
import com.tms.collab.resourcemanager.model.Resource;
import com.tms.collab.resourcemanager.model.ResourceMailer;
import com.tms.collab.resourcemanager.model.ResourceManager;
import com.tms.util.FormatUtil;
import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.stdui.validator.Validator;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.stdui.validator.ValidatorRange;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.Daylight;
import net.fortuna.ical4j.model.component.Standard;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.PartStat;
import net.fortuna.ical4j.model.property.*;
import org.apache.commons.collections.SequencedHashMap;
import org.apache.commons.lang.StringUtils;

import javax.mail.internet.AddressException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CalendarForm extends Form {

    public final static String PARAMETER_NEW_RESOURCE_BOOKING = "newresource";
    public static final String DEFAULT_TEMPLATE = "calendar/appointmentForm";

    protected CalendarEvent event;
    protected String eventId;
    protected String viewUrl;
    protected String prefix;
    protected boolean newResourceBooking = false;

    protected String init = "0";
    protected TextField title;
    protected Validator validTitle;
    protected DateField startDate;
    protected DateField endDate;
    protected TimeField startTime;
    protected TimeField endTime;
    protected CheckBox reminderRadio;
    protected SelectBox reminderModifier;
    protected TextField reminderQuantity;
    protected TextBox description;
    protected TextBox agenda;
    protected TextField location;
    protected CheckBox exclude;
    protected CheckBox universal;
    protected Radio radioPublic;
    protected Radio radioPrivate;
/*
    protected Radio radioConfidential;
*/
    protected CalendarUsersSelectBox compulsory;
    protected AddressBookSelectBox optional;
    protected TextBox outsideAttendee;
    protected ComboSelectBox resources;
    protected CheckBox notifyMemo;
    protected CheckBox notifyEmail;
    protected CheckBox allDay;
    protected TextBox notifyNote;
    protected Button submitButton, cancelButton;
/*
    protected Button resetButton;
*/
    protected SelectBox recurrence, weekDayOrder, weekDay;
    protected Radio everyDay, everyWeekDay, everyMonth, certainDayOfMonth, occurenceTimes, endOccurenceDate;
    protected TextField dailyFrequentTextField, weeklyFrequentTextField, monthlyFrequentTextField, monthlyFrequentTextField2, yearlyFrequentTextField, occurenceTimesTF;
    protected DateField endOccurenceDateDF;
    protected ButtonGroup dailyBG, monthlyBG, occurenceBG;
    protected ValidatorRange recurrenceRange;

    protected Label attendeeAlert;

    public CalendarForm() {
    }

    public CalendarForm(String name) {
        super(name);
    }

    public void init() {

        removeChildren();
        setMethod("POST");
        title = new TextField("title");
        if (!newResourceBooking) {
            validTitle = new ValidatorNotEmpty("validTitle");
            title.addChild(validTitle);
        }
        attendeeAlert = new Label("attendeeAlert");
        title.addChild(attendeeAlert);

        startDate = new DatePopupField("startDate");
        startDate.setTemplate("calendar/calendarDatePopupField");

        Calendar TodayDate = Calendar.getInstance();
        startDate.setDate(TodayDate.getTime());

//        startDate.setTemplate("calendar/calendardatefield");
        endDate = new DatePopupField("endDate");
        endDate.setDate(TodayDate.getTime());
        startTime = new TimeField("startTime");
        startTime.setTemplate("calendar/timefield");
        endTime = new TimeField("endTime");
        Date now = TodayDate.getTime();
        now.setHours(now.getHours() + 1);
        endTime.setDate(now);
        allDay = new CheckBox("allDay");
        //    reminder = new SelectBox("reminder");
        reminderRadio = new CheckBox("reminderRadio");
        reminderModifier = new SelectBox("reminderModifier");
        description = new TextBox("description");
        //validDescription = new ValidatorNotEmpty("validDescription");
        //description.addChild(validDescription);
        agenda = new TextBox("agenda");
        location = new TextField("location");
        exclude = new CheckBox("exclude");
        universal = new CheckBox("universal");
        recurrence = new SelectBox("recurrence");
        radioPublic = new Radio("radioPublic");
        radioPublic.setValue(CalendarModule.CLASSIFICATION_PUBLIC);
        radioPublic.setGroupName("classification");
        radioPublic.setChecked(true);
        radioPrivate = new Radio("radioPrivate");
        radioPrivate.setValue(CalendarModule.CLASSIFICATION_PRIVATE);
        radioPrivate.setGroupName("classification");
/*
        radioConfidential = new Radio("radioConfidential");
        radioConfidential.setValue(CalendarModule.CLASSIFICATION_CONFIDENTIAL);
        radioConfidential.setGroupName("classification");
*/

        compulsory = new CalendarUsersSelectBox("compulsory");
        optional = new AddressBookSelectBox("optional");
        outsideAttendee = new TextBox("outsideAttendee");
        outsideAttendee.setRows("2");
        outsideAttendee.setMaxlength("10");
        resources = new ComboSelectBox("resoruces");

        reminderModifier.addOption(new Integer(Calendar.MINUTE).toString(), Application.getInstance().getMessage("calendar.label.optionMinutes", "minute(s)"));
        reminderModifier.addOption(new Integer(Calendar.HOUR_OF_DAY).toString(), Application.getInstance().getMessage("calendar.label.optionHours", "hour(s)"));
        reminderModifier.addOption(new Integer(Calendar.DAY_OF_MONTH).toString(), Application.getInstance().getMessage("calendar.label.optionDays", "day(s)"));
        reminderModifier.addOption(new Integer(Calendar.WEEK_OF_YEAR).toString(), Application.getInstance().getMessage("calendar.label.optionWeeks", "week(s)"));
        reminderQuantity = new TextField("reminderQuantity");
        reminderQuantity.setValue("1");
        reminderQuantity.setMaxlength("2");
        reminderQuantity.setSize("2");
        recurrence.addOption("0", Application.getInstance().getMessage("calendar.label.recurrenceNone", "None"));
        recurrence.addOption("1", Application.getInstance().getMessage("calendar.label.recurrenceDaily", "Daily"));
        recurrence.addOption("2", Application.getInstance().getMessage("calendar.label.recurrenceWeekly", "Weekly"));
        recurrence.addOption("3", Application.getInstance().getMessage("calendar.label.recurrenceMonthly", "Monthly"));
        recurrence.addOption("4", Application.getInstance().getMessage("calendar.label.recurrenceYearly", "Yearly"));
        recurrence.setOnChange("javascript:showDiv(this)");

        weekDayOrder = new SelectBox("weedDayOrder");
        weekDayOrder.addOption("1", Application.getInstance().getMessage("calendar.label.weekDayOrderfirst", "first"));
        weekDayOrder.addOption("2", Application.getInstance().getMessage("calendar.label.weekDayOrdersecond", "second"));
        weekDayOrder.addOption("3", Application.getInstance().getMessage("calendar.label.weekDayOrderthird", "third"));
        weekDayOrder.addOption("4", Application.getInstance().getMessage("calendar.label.weekDayOrderfourth", "fourth"));
        weekDayOrder.addOption("5", Application.getInstance().getMessage("calendar.label.weekDayOrderlast", "last"));
        weekDay = new SelectBox("weekDay");

        weekDay.addOption("0", Application.getInstance().getMessage("calendar.label.sunday", "Sunday"));
        weekDay.addOption("1", Application.getInstance().getMessage("calendar.label.monday", "Monday"));
        weekDay.addOption("2", Application.getInstance().getMessage("calendar.label.tuesday", "Tuesday"));
        weekDay.addOption("3", Application.getInstance().getMessage("calendar.label.wednesday", "Wednesday"));
        weekDay.addOption("4", Application.getInstance().getMessage("calendar.label.thursday", "Thursday"));
        weekDay.addOption("5", Application.getInstance().getMessage("calendar.label.friday", "Friday"));
        weekDay.addOption("6", Application.getInstance().getMessage("calendar.label.saturday", "Saturday"));

        everyDay = new Radio("EveryDay");
        everyDay.setChecked(true);
        everyWeekDay = new Radio("EveryWeekday");
        everyMonth = new Radio("EveryMonth", Application.getInstance().getMessage("calendar.label.every", "Every"), true);
        certainDayOfMonth = new Radio("CertainDayOfMonth");
        occurenceTimes = new Radio("times", Application.getInstance().getMessage("calendar.label.endafter", "End after"), true);
        endOccurenceDate = new Radio("endOccurenceDate", Application.getInstance().getMessage("calendar.label.endby", "End by"));
        dailyFrequentTextField = new TextField("dailyFrequentTextField");
        dailyFrequentTextField.setValue("1");
        dailyFrequentTextField.setMaxlength("3");
        dailyFrequentTextField.setSize("3");

        weeklyFrequentTextField = new TextField("weeklyFrequentTextField");
        weeklyFrequentTextField.setValue("1");
        weeklyFrequentTextField.setMaxlength("3");
        weeklyFrequentTextField.setSize("3");

        monthlyFrequentTextField = new TextField("monthlyFrequentTextField");
        monthlyFrequentTextField.setValue("1");
        monthlyFrequentTextField.setMaxlength("3");
        monthlyFrequentTextField.setSize("3");
        monthlyFrequentTextField2 = new TextField("monthlyFrequentTextField2");
        monthlyFrequentTextField2.setValue("1");
        monthlyFrequentTextField2.setMaxlength("3");
        monthlyFrequentTextField2.setSize("3");
        yearlyFrequentTextField = new TextField("yearlyFrequentTextField");
        yearlyFrequentTextField.setValue("1");
        yearlyFrequentTextField.setMaxlength("3");
        yearlyFrequentTextField.setSize("3");

        occurenceTimesTF = new TextField("occurenceTime");
        occurenceTimesTF.setValue("1");
        occurenceTimesTF.setMaxlength("4");
        occurenceTimesTF.setSize("4");
        recurrenceRange = new ValidatorRange("recurrenceRange", Application.getInstance().getMessage("calendar.label.recurrenceRange"), Double.valueOf("1"), Double.valueOf(Application.getInstance().getProperty("com.tms.collab.calendar.ui.CalendarForm.recurrenceMax")));
        occurenceTimesTF.addChild(recurrenceRange);
        dailyBG = new ButtonGroup("dailyBG", new Radio[]{everyDay, everyWeekDay});
        monthlyBG = new ButtonGroup("montlyBG", new Radio[]{everyMonth, certainDayOfMonth});
        occurenceBG = new ButtonGroup("occurence", new Radio[]{occurenceTimes, endOccurenceDate});
        endOccurenceDateDF = new DateField("endOccurenceDateDF");
        endOccurenceDateDF.setDate(new Date());
        notifyMemo = new CheckBox("notifyMemo");
        notifyEmail = new CheckBox("notifyEmail");
        notifyNote = new TextBox("notifyNote");
        notifyMemo.setChecked(true);
        submitButton = new Button("submitButton");
        submitButton.setText(Application.getInstance().getMessage("calendar.label.submit", "Submit"));
/*
        resetButton = new Button("resetButton");
        resetButton.setText("Reset");
*/

        cancelButton = new Button("cancelButton");
        cancelButton.setText(Application.getInstance().getMessage("calendar.label.cancel", "Cancel"));
        addChild(cancelButton);
        addChild(allDay);
        addChild(weeklyFrequentTextField);
        addChild(monthlyFrequentTextField);
        addChild(monthlyFrequentTextField2);
        addChild(yearlyFrequentTextField);
        addChild(occurenceBG);
        addChild(occurenceTimesTF);
        addChild(endOccurenceDateDF);
        addChild(occurenceTimes);
        addChild(endOccurenceDate);
        addChild(weekDay);
        addChild(certainDayOfMonth);
        addChild(weekDayOrder);
        addChild(everyMonth);
        addChild(dailyFrequentTextField);
        addChild(dailyBG);
        addChild(monthlyBG);
        addChild(everyDay);
        addChild(everyWeekDay);
        addChild(recurrence);
        addChild(title);
        addChild(startDate);
        addChild(endDate);
        addChild(startTime);
        addChild(endTime);
        addChild(reminderRadio);
        addChild(reminderModifier);
        addChild(reminderQuantity);
        addChild(description);
        addChild(agenda);
        addChild(location);
        addChild(exclude);
        addChild(universal);
        addChild(radioPublic);
        addChild(radioPrivate);
/*
        addChild(radioConfidential);
*/
        //addChild(resources);
        addChild(notifyMemo);
        addChild(notifyEmail);
        addChild(notifyNote);
        addChild(submitButton);
/*
        addChild(resetButton);
*/
        //requentTextField.setOnBlur("javascript:updateValue(this);");

        addChild(compulsory);
        addChild(optional);
        addChild(outsideAttendee);
        addChild(resources);

        compulsory.init();
        optional.init();
        resources.init();
        outsideAttendee.init();
    }

    public void onRequest(Event event) {
        eventId = event.getRequest().getParameter("eventId");
        viewUrl = event.getRequest().getParameter("viewUrl");
        // prefix = null;
        if ("1".equals(init)) {
            init = "0";
            init();
        } else if (eventId != null && eventId.trim().length() > 0)
            init();
        Map resourcesMap = new SequencedHashMap();
        ResourceManager rm = (ResourceManager) Application.getInstance().getModule(ResourceManager.class);
        try {
            Collection col = rm.getResources(getWidgetManager().getUser().getId(), true);
            for (Iterator i = col.iterator(); i.hasNext();) {
                Resource resource = (Resource) i.next();
                resourcesMap.put(resource.getId(), resource.getName());
            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving resources", e);
        }
        resources.setLeftValues(resourcesMap);
        resourcesMap = new SequencedHashMap();
        resources.setRightValues(resourcesMap);
        if (eventId != null && eventId.trim().length() > 0)
            populateForm();

        attendeeAlert.setText("");
    }

    public DateField getEndOccurenceDateDF() {
        return endOccurenceDateDF;
    }

    public void setEndOccurenceDateDF(DateField endOccurenceDateDF) {
        this.endOccurenceDateDF = endOccurenceDateDF;
    }

    public TextField getOccurenceTimesTF() {
        return occurenceTimesTF;
    }

    public void setOccurenceTimesTF(TextField occurenceTimesTF) {
        this.occurenceTimesTF = occurenceTimesTF;
    }

    public DateField getEndOccurenceDateTF() {
        return endOccurenceDateDF;
    }

    public void setEndOccurenceDateTF(DateField endOccurenceDateDF) {
        this.endOccurenceDateDF = endOccurenceDateDF;
    }

    public Radio getEndOccurenceDate() {
        return endOccurenceDate;
    }

    public void setEndOccurenceDate(Radio endOccurenceDate) {
        this.endOccurenceDate = endOccurenceDate;
    }

    public Radio getOccurenceTimes() {
        return occurenceTimes;
    }

    public void setOccurenceTimes(Radio occurenceTimes) {
        this.occurenceTimes = occurenceTimes;
    }

    public SelectBox getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(SelectBox weekDay) {
        this.weekDay = weekDay;
    }

    public Radio getCertainDayOfMonth() {
        return certainDayOfMonth;
    }

    public void setCertainDayOfMonth(Radio certainDayOfMonth) {
        this.certainDayOfMonth = certainDayOfMonth;
    }

    public SelectBox getWeekDayOrder() {
        return weekDayOrder;
    }

    public void setWeekDayOrder(SelectBox weekDayOrder) {
        this.weekDayOrder = weekDayOrder;
    }

    public Radio getEveryMonth() {
        return everyMonth;
    }

    public void setEveryMonth(Radio everyMonth) {
        this.everyMonth = everyMonth;
    }

    public ButtonGroup getDailyBG() {
        return dailyBG;
    }

    public void setDailyBG(ButtonGroup dailyBG) {
        this.dailyBG = dailyBG;
    }


    public Radio getEveryDay() {
        return everyDay;
    }

    public void setEveryDay(Radio everyDay) {
        this.everyDay = everyDay;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public TextField getTitle() {
        return title;
    }

    public DateField getStartDate() {
        return startDate;
    }

    public DateField getEndDate() {
        return endDate;
    }

    public TimeField getStartTime() {
        return startTime;
    }

    public TimeField getEndTime() {
        return endTime;
    }

    /*  public SelectBox getReminder() {
    return reminder;
    }*/

    public TextBox getDescription() {
        return description;
    }

    public CheckBox getReminderRadio() {
        return reminderRadio;
    }

    public void setReminderRadio(CheckBox reminderRadio) {
        this.reminderRadio = reminderRadio;
    }

    public Radio getEveryWeekDay() {
        return everyWeekDay;
    }

    public void setEveryWeekDay(Radio everyWeekDay) {
        this.everyWeekDay = everyWeekDay;
    }

    public TextBox getAgenda() {
        return agenda;
    }

    public TextField getLocation() {
        return location;
    }

    public CheckBox getExclude() {
        return exclude;
    }

    public CheckBox getUniversal() {
        return universal;
    }

    public Radio getRadioPublic() {
        return radioPublic;
    }

    public Radio getRadioPrivate() {
        return radioPrivate;
    }
/*
    public Radio getRadioConfidential() {
        return radioConfidential;
    }
*/

    public CheckBox getNotifyMemo() {
        return notifyMemo;
    }

    public CheckBox getNotifyEmail() {
        return notifyEmail;
    }

    public TextBox getNotifyNote() {
        return notifyNote;
    }

    public Validator getValidTitle() {
        return validTitle;
    }

    public Button getSubmitButton() {
        return submitButton;
    }
/*
    public Button getResetButton() {
        return resetButton;
*/
//    }

    public kacang.ui.Forward onSubmit(kacang.ui.Event evt) {

        // check for reset request
        String buttonName = findButtonClicked(evt);
        if (buttonName != null && cancelButton.getAbsoluteName().equals(buttonName)) {
            try {
                if (viewUrl != null && viewUrl.trim().length() > 0)
                    evt.getResponse().sendRedirect(viewUrl);
            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Error redirecting to URL " + viewUrl, e);
            }
        } else {
            // else check that end date is equal to or after start date
            kacang.ui.Forward result = super.onSubmit(evt);
            if (startDate.getDate() != null && endDate.getDate() != null) {
                if (startDate.getDate().after(endDate.getDate())) {
                    startDate.setInvalid(true);
                    endDate.setInvalid(true);
                    setInvalid(true);
                    return result;
                } else
                if (!allDay.isChecked() && startDate.getDate().equals(endDate.getDate()) && startTime.getDate().after(endTime.getDate()))
                {
                    startTime.setInvalid(true);
                    endTime.setInvalid(true);
                    setInvalid(true);
                    return result;
                }
                if (outsideAttendee.getValue() != null) {
                    if (!isMessageAddressesValid()) {
                        outsideAttendee.setInvalid(true);
                        setInvalid(true);
                        return new Forward("invalidAddress");
                    }
                }
                if (!recurrence.getSelectedOptions().isEmpty() && !"0".equals(recurrence.getSelectedOptions().keySet().iterator().next()))
                {
                    Calendar tc = startDate.getCalendar();
                    Calendar st = startTime.getCalendar();
                    Calendar ec = endDate.getCalendar();
                    Calendar et = endTime.getCalendar();
                    tc.set(Calendar.HOUR_OF_DAY, st.get(Calendar.HOUR_OF_DAY));
                    tc.set(Calendar.MINUTE, st.get(Calendar.MINUTE));
                    ec.set(Calendar.HOUR_OF_DAY, et.get(Calendar.HOUR_OF_DAY));
                    ec.set(Calendar.MINUTE, et.get(Calendar.MINUTE));
                    Calendar rc = Calendar.getInstance();
                    rc.setTime(tc.getTime());
                    //long duration = endDate.getDate().getTime() - startDate.getDate().getTime();
                    String selected = (String) recurrence.getSelectedOptions().keySet().iterator().next();
                    // long rduration;
                    if ("1".equals(selected)) {
                        if (everyDay.isChecked()) {
                            rc.add(Calendar.DAY_OF_MONTH, Integer.parseInt((String) dailyFrequentTextField.getValue()));
                        } else if (everyWeekDay.isChecked()) {
                            do {
                                rc.add(Calendar.DAY_OF_MONTH, 1);
                            }
                            while (rc.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || rc.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
                        }
                        //                    rduration = Integer.parseInt((String)dailyFrequentTextField.getValue())*24*60*60*1000;
                    } else if ("2".equals(selected)) {
                        //                    rduration = Integer.parseInt((String)dailyFrequentTextField.getValue())*24*60*60*1000;
                        rc.add(Calendar.WEEK_OF_YEAR, Integer.parseInt((String) weeklyFrequentTextField.getValue()));
                    } else if ("3".equals(selected)) {
                        if (everyMonth.isChecked()) {
                            rc.add(Calendar.MONTH, Integer.parseInt((String) monthlyFrequentTextField.getValue()));
                        } else if (certainDayOfMonth.isChecked()) {
                            rc.add(Calendar.MONTH, Integer.parseInt((String) monthlyFrequentTextField2.getValue()));
                            String order = (String) weekDayOrder.getSelectedOptions().keySet().iterator().next();
                            int[] weekdays = {Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY};
                            rc.set(Calendar.DAY_OF_WEEK, weekdays[Integer.parseInt((String) weekDay.getSelectedOptions().keySet().iterator().next())]);
                            if ("5".equals(order))
                                rc.set(Calendar.DAY_OF_WEEK_IN_MONTH, -1);
                            else
                                rc.set(Calendar.DAY_OF_WEEK_IN_MONTH, Integer.parseInt(order));
                        }
                    } else if ("4".equals(selected)) {
                        rc.add(Calendar.YEAR, Integer.parseInt((String) yearlyFrequentTextField.getValue()));
                    }
                    if (ec.getTime().after(rc.getTime())) {
                        setInvalid(true);
                        return new Forward("Recurrence Exception");
                    }
                }
                if (exclude.isChecked()) {
                    String[] comp = compulsory.getIds();
                    String[] opts = optional.getIds();

                    if (comp.length == 0 && opts.length == 0) {
                        String msg = Application.getInstance().getMessage("calendar.message.atLeastOneAttendee", "Please select at least one attendee.");
                        attendeeAlert.setText("<script>alert('" + msg + "');</script>");
                        setInvalid(true);
                    }
                }
            }
            return result;
        }
        return null;
    }

    public Forward actionPerformed(Event evt) {
        String eventType = evt.getRequest().getParameter(Event.PARAMETER_KEY_EVENT_TYPE);
        if (PARAMETER_NEW_RESOURCE_BOOKING.equals(eventType)) {
            newResourceBooking = true;
            init();
            initResourceForm(evt);
            return null;
            //return new Forward(null,evt.getRequest().getRequestURI(),true);
        }
        return super.actionPerformed(evt);
    }

    protected void initResourceForm(Event evt) {
        String resourceId = evt.getRequest().getParameter("resourceId");
        Resource resource = null;
        if (resourceId != null && resourceId.trim().length() > 0)
            try {
                resource = ((ResourceManager) Application.getInstance().getModule(ResourceManager.class)).getResource(resourceId, true);
            }
            catch (DaoException e) {
                Log.getLog(getClass()).error("Error retrieving resource " + resourceId, e);
            }
        if (resource != null) {
            title.setValue("*Resource Booking*");
            Map right = new SequencedHashMap();
            right.put(resource.getId(), resource.getName());
            // resources.setSelectedOptions(new String[]{(String)right.keySet().iterator().next()});
            resources.setRightValues(right);
        }
    }

    protected void populateForm() {
        try {
            Application application = Application.getInstance();
            CalendarModule handler = (CalendarModule) application.getModule(CalendarModule.class);
            CalendarEvent event = handler.getCalendarEvent(getEventId());
            title.setValue(event.getTitle());
            description.setValue(event.getDescription());
            startDate.setDate(event.getStartDate() == null ? event.getCreationDate() : event.getStartDate());
            startTime.setDate(event.getStartDate() == null ? event.getCreationDate() : event.getStartDate());
            endDate.setDate(event.getEndDate());
            endTime.setDate(event.getEndDate());
            if (event.isAllDay())
                allDay.setChecked(true);
            if (event.getReminderDate() != null) {
                reminderRadio.setChecked(true);
                //Calendar cal = Calendar.getInstance();
                //cal.setTime(event.getStartDate());
                long t = event.getStartDate().getTime() - event.getReminderDate().getTime();
                float days = (float) t / 1000 / 60 / 60 / 24;
                if (days < 1) {
                    float hours = days * 24;
                    if (hours == (int) hours) {
                        reminderModifier.setSelectedOptions(new String[]{new Integer(Calendar.HOUR_OF_DAY).toString()});
                        reminderQuantity.setValue("" + (int) hours);
                    } else {
                        reminderModifier.setSelectedOptions(new String[]{new Integer(Calendar.MINUTE).toString()});
                        reminderQuantity.setValue("" + (int) (hours * 60));
                    }
                } else if ((int) days % 7 == 0) {
                    reminderModifier.setSelectedOptions(new String[]{new Integer(Calendar.WEEK_OF_YEAR).toString()});
                    reminderQuantity.setValue("" + (int) (days / 7));
                } else {
                    reminderModifier.setSelectedOptions(new String[]{new Integer(Calendar.DAY_OF_MONTH).toString()});
                    reminderQuantity.setValue("" + (int) days);
                }
            }
            Collection col = event.getAttendees();
            Attendee att;
            Collection compulsoryIdList = new ArrayList();
            Collection optionalIdList = new ArrayList();
            String nonEkpAttendees = "";
            for (Iterator i = col.iterator(); i.hasNext();) {
                att = (Attendee) i.next();
                if (att.isCompulsory()) {
                    compulsoryIdList.add(att.getUserId());
                } else {
                    if (att.getUserId().equals(att.getProperty("username"))) {
                        nonEkpAttendees += att.getProperty("username") + ",";
                    } else {
                        optionalIdList.add(att.getUserId());
                    }
                }
            }
            compulsory.setIds((String[]) compulsoryIdList.toArray(new String[0]));
            optional.setIds((String[]) optionalIdList.toArray(new String[0]));
            outsideAttendee.setValue(nonEkpAttendees);
            Collection resources = event.getResources();
            Map rrmap = new HashMap();
            Map rlmap = this.resources.getLeftValues();
            for (Iterator iterator = resources.iterator(); iterator.hasNext();) {
                Resource resource = (Resource) iterator.next();
                rrmap.put(resource.getId(), resource.getName());
                rlmap.remove(resource.getId());
            }
            this.resources.setLeftValues(rlmap);
            this.resources.setRightValues(rrmap);
            agenda.setValue(event.getAgenda());
            location.setValue(event.getLocation());
            exclude.setChecked(event.isOwnerExcluded());
            universal.setChecked(event.isUniversal());
            String c = event.getClassification();
            if (CalendarModule.CLASSIFICATION_PUBLIC.equals(c)){
            	 radioPublic.setChecked(true);
            	 radioPrivate.setChecked(false);
            }else if (CalendarModule.CLASSIFICATION_PRIVATE.equals(c)){
            	 radioPrivate.setChecked(true);
            	 radioPublic.setChecked(false);
            }
                
/*
            else if(CalendarModule.CLASSIFICATION_CONFIDENTIAL.equals(c))
                radioConfidential.setChecked(true);
*/
            if (event.getRecurrenceRule() != null && event.getRecurrenceRule().trim().length() > 0) {
                RecurrenceRule rule = CalendarUtil.parseRecurrenceRule(event.getRecurrenceRule());
                switch (rule.getFrequency()) {
                    case RecurrenceRule.FREQUENCY_DAILY:
                        recurrence.addSelectedOption("1");
                        if (rule.getModifier() != null && rule.getModifier().trim().length() > 0) {
                            everyWeekDay.setChecked(true);
                        } else {
                            everyDay.setChecked(true);
                            dailyFrequentTextField.setValue("" + rule.getInterval());
                        }
                        break;
                    case RecurrenceRule.FREQUENCY_WEEKLY:
                        recurrence.addSelectedOption("2");
                        weeklyFrequentTextField.setValue("" + rule.getInterval());
                        break;
                    case RecurrenceRule.FREQUENCY_MONTHLY_BY_DAY:
                        recurrence.addSelectedOption("3");
                        everyMonth.setChecked(true);
                        monthlyFrequentTextField.setValue("" + rule.getInterval());
                        break;
                    case RecurrenceRule.FREQUENCY_MONTHLY_BY_POSITION:
                        recurrence.addSelectedOption("3");
                        certainDayOfMonth.setChecked(true);
                        RecurrenceRuleModifier m = CalendarUtil.parseModifier(rule.getModifier());
                        String o = (String) m.getPosition().get(0);
                        if (o.endsWith("+"))
                            weekDayOrder.addSelectedOption(o.substring(0, o.indexOf("+")));
                        else
                            weekDayOrder.addSelectedOption("5");
                        weekDay.addSelectedOption(getWeekday((String) m.getWeekDay().get(0)));
                        monthlyFrequentTextField2.setValue("" + rule.getInterval());
                        break;
                    case RecurrenceRule.FREQUENCY_YEARLY_BY_DAY:
                        recurrence.addSelectedOption("4");
                        yearlyFrequentTextField.setValue("" + rule.getInterval());
                }
                if (rule.getDuration() == -1) {
                    endOccurenceDate.setChecked(true);
                    endOccurenceDateDF.setDate(rule.getEndDate());
                } else {
                    occurenceTimes.setChecked(true);
                    occurenceTimesTF.setValue("" + rule.getDuration());
                }
            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error populating calendar form", e);
        }
    }

    protected String getWeekday(String wd) {
        if ("SU".equals(wd))
            return "0";
        else if ("MO".equals(wd))
            return "1";
        else if ("TU".equals(wd))
            return "2";
        else if ("WE".equals(wd))
            return "3";
        else if ("TH".equals(wd))
            return "4";
        else if ("FR".equals(wd))
            return "5";
        else if ("SA".equals(wd))
            return "6";
        return "0";
    }

    protected void updateEvent(kacang.ui.Event evt) {

    }

    public kacang.ui.Forward onValidate(kacang.ui.Event evt) {
        String buttonClicked = findButtonClicked(evt);
        if (cancelButton.getAbsoluteName().equals(buttonClicked))
            return new Forward("cancel");
        try {
            Forward forward = createEvent(evt);
            if (forward != null) {
                //  prefix = null;
                init();
                return forward;
            }
            if (viewUrl != null && viewUrl.trim().length() > 0)
                evt.getResponse().sendRedirect(viewUrl);
        }
        catch (ConflictException e) {
            eventId = null;
            evt.getRequest().getSession().setAttribute("conflict", e);
            evt.getRequest().getSession().setAttribute("event", event);
            evt.getRequest().getSession().setAttribute("recurringEvents", generateRecurringEvent(event));
            evt.getRequest().getSession().setAttribute("memo", notifyMemo.isChecked() ? Boolean.TRUE : Boolean.FALSE);
            evt.getRequest().getSession().setAttribute("email", notifyEmail.isChecked() ? Boolean.TRUE : Boolean.FALSE);
            if (notifyNote.getValue() != null)
                evt.getRequest().getSession().setAttribute("notes", notifyNote.getValue().toString());
            // init();
            return new Forward("conflict exception");
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error handling calendar form submission", e);
        }
        return null;
    }

    protected Forward createEvent(Event evt) throws ConflictException {
        try {
            boolean update = false;
            event = assembleEvent();
            String prefix = getPrefix();
            String userId = getWidgetManager().getUser().getId();
            CalendarModule handler = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
            Collection col = generateRecurringEvent(event);
            if (eventId != null && eventId.trim().length() > 0)
                update = true;
            if (!update) {
                handler.addCalendarEvent(prefix, event, userId, /*(Appointment.class.getName().equals(prefix)?false:true)*/false);
                User user = getWidgetManager().getUser();
                Log.getLog(getClass()).write(new Date(), event.getEventId(), user.getId(), "kacang.services.log.calendar.CreateEvent", "New calendar event " + event.getTitle() + " created by user " + user.getName(), evt.getRequest().getRemoteAddr(), evt.getRequest().getSession().getId());
            } else {
                handler.updateCalendarEvent(event, userId, /*(Appointment.class.getName().equals(prefix)?false:true)*/false);
                User user = getWidgetManager().getUser();
                Log.getLog(getClass()).write(new Date(), eventId, user.getId(), "kacang.services.log.calendar.UpdatedEvent", "Calendar event " + event.getTitle() + " updated by user " + user.getName(), evt.getRequest().getRemoteAddr(), evt.getRequest().getSession().getId());

                // reset alert notification
                CalendarUtil.resetReminderAlert(evt.getRequest(), eventId);
            }
            for (Iterator i = col.iterator(); i.hasNext();) {
                handler.addRecurringEvent((CalendarEvent) i.next(), userId, false);
            }
            ResourceMailer.sendResourceApprovalMail(userId, event, evt);
            if (notifyMemo.isChecked() || notifyEmail.isChecked())
                sendNotification(event, !update, evt);
            if (prefix == null) {
                if (eventId != null && eventId.trim().length() > 0) {
                    prefix = eventId.substring(0, eventId.indexOf("_"));
                }
            }
            if (update) {
                if (prefix != null && prefix.equals(CalendarEvent.class.getName()))
                    return new Forward("event updated");
                else if (prefix != null && prefix.equals(Appointment.class.getName()))
                    return new Forward("appointment updated");
            } else {
                if (prefix != null && prefix.equals(CalendarEvent.class.getName()))
                    return new Forward("event added");
                else if (prefix != null && prefix.equals(Appointment.class.getName()))
                    return new Forward("appointment added");

            }

        }
        catch (ConflictException e) {
            throw e;
        }
        catch (DataObjectNotFoundException ce) {
        	Log.getLog(getClass()).error("Error creating event", ce);
        }
        catch (Exception ce) {
            Log.getLog(getClass()).error("Error creating event", ce);
        }
        return null;
    }

    protected void sendNotification(CalendarEvent event, boolean newEvent, Event evt) {
        try {
            MessagingModule mm;
            User user;
            SmtpAccount smtpAccount;
            Message message;

            mm = Util.getMessagingModule();
            user = getWidgetManager().getUser();
            String userId = user.getId();
            smtpAccount = mm.getSmtpAccountByUserId(user.getId());

            // construct the message to send
            message = new Message();
            message.setMessageId(UuidGenerator.getInstance().getUuid());
            // setMessageProperties(message,event,newEvent,evt);
            IntranetAccount intranetAccount;

            intranetAccount = Util.getMessagingModule().getIntranetAccountByUserId(getWidgetManager().getUser().getId());
            message.setFrom(intranetAccount.getFromAddress());

            Collection col = event.getAttendees();
            List memoList, emailList, bizContactsList;
            memoList = new ArrayList(col.size());
            emailList = new ArrayList(col.size());
            bizContactsList = new ArrayList(col.size());

            for (Iterator i = col.iterator(); i.hasNext();) {
                Attendee att = (Attendee) i.next();
                if (!userId.equals(att.getUserId())) {
                    if (notifyMemo.isChecked()) {
                        intranetAccount = Util.getMessagingModule().getIntranetAccountByUserId(att.getUserId());
                        if (intranetAccount != null) {
                            String add = intranetAccount.getIntranetUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
                            memoList.add(add);
                        }
                    }
                    if (notifyEmail.isChecked()) {
                        User tempuser = UserUtil.getUser(att.getUserId());
                        String email = (String) tempuser.getProperty("email1");
                        emailList.add(email);
                    } else {
                        if (att.isNonEKPUser()) {
                            bizContactsList.add(att.getProperty("email"));
                        } else if (!att.isEkpUser()) {
                            bizContactsList.add(att.getProperty("email"));
                        }

                    }
                }
            }
            if (memoList.size() > 0)
                message.setToIntranetList(memoList);

            Application app = Application.getInstance();

            if (newEvent)
                message.setSubject(app.getMessage("calendar.label.newAppointment", "New Appointment") + ": " + event.getTitle());
            else
                message.setSubject(app.getMessage("calendar.label.updatedAppointment", "Updated Appointment") + ": " + event.getTitle());

            String desc = " - ";
            String sdesc = description.getValue().toString();
            if (sdesc != null && sdesc.trim().length() > 0) {
                desc = StringUtils.replace(sdesc, "\n", "<br>");
            }
            String temp = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">" +
                    "</head><body>" +
                    "<style>" +
                    ".contentBgColorMail {background-color: #FFFFFF; font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8.5pt}" +
                    "</style>" +
                    "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr valign=\"MIDDLE\">" +
                    "<td height=\"22\" bgcolor=\"#003366\" class=\"contentBgColorMail\"><b><font color=\"#000000\" class=\"contentBgColorMail\">" +
                    "<b><U>" +
                    app.getMessage("calendar.label.appointmentDetails", "Appointment Details") +
                    "</U></b></font></b></td><td align=\"right\" bgcolor=\"#003366\" class=\"contentBgColorMail\">&nbsp;</td>" +
                    "</tr><tr><td colspan=\"2\" valign=\"TOP\" bgcolor=\"#EFEFEF\" class=\"contentBgColorMail\">&nbsp;</td>" +
                    "</tr><tr><td colspan=\"2\" valign=\"TOP\"><table width=\"100%\" border=\"0\" cellpadding=\"5\" cellspacing=\"1\">" +
                    "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>" +
                    "<b>" + app.getMessage("calendar.label.title", "Title") + "</b></strong></td><td class=\"contentBgColorMail\" width=\"90%\">" + event.getTitle() + "</td></tr>" +
                    "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>" +
                    "<b>" + app.getMessage("calendar.label.scheduler", "Scheduler") + "</b></strong></td><td class=\"contentBgColorMail\" width=\"90%\">" + event.getUserName() + "</td></tr>" +
                    "<tr><td class=\"contentBgColorMail\" align=\"right\" valign=\"top\" nowrap><b>" + app.getMessage("calendar.label.startDate", "Start Date") + "</b></td>" +
                    "<td class=\"contentBgColorMail\">" + new SimpleDateFormat(FormatUtil.getInstance().getLongDateTimeFormat()).format(event.getStartDate()) + "</td>" +
                    "</tr><tr><td class=\"contentBgColorMail\" align=\"right\" valign=\"top\" nowrap><b>" + app.getMessage("calendar.label.endDate", "End Date") + "</b></td>" +
                    "<td class=\"contentBgColorMail\">" + new SimpleDateFormat(FormatUtil.getInstance().getLongDateTimeFormat()).format(event.getEndDate()) + "</td>" +
                    "</tr><tr><td class=\"contentBgColorMail\" align=\"right\" valign=\"top\" nowrap><b>" + app.getMessage("calendar.label.description", "Description") + "</b></td><td class=\"contentBgColorMail\">" +
                    desc + "</td></tr>";

            String agen = " - ";
            String sagenda = agenda.getValue().toString();
            if (sagenda != null && sagenda.trim().length() > 0) {
                agen = StringUtils.replace(sagenda, "\n", "<br>");
            }

            //if ( != null && event.getAgenda().trim().length() > 0) {
            temp += "<tr><td class=\"contentBgColorMail\" align=\"right\" valign=\"top\" nowrap><b>" + app.getMessage("calendar.label.agenda", "Agenda") + "</b></td>" +
                    "<td class=\"contentBgColorMail\">" + agen + "</td></tr>";
            // }
            String location = "- ";
            String slocation = event.getLocation();
            if (slocation != null && slocation.trim().length() > 0) {
                location = slocation;
            }
            //     if (location != null && location.trim().length() > 0) {
            temp += "<tr><td class=\"contentBgColorMail\" align=\"right\" valign=\"top\" nowrap><b>" + app.getMessage("calendar.label.location", "Location") + "</b></td>" +
                    "<td class=\"contentBgColorMail\">" + location + "</td></tr>";
            //       }
            StringBuffer resourceSb = new StringBuffer("");
            Collection resources = event.getResources();
            int size = resources.size();
            if (resources != null && size > 0) {
                for (Iterator iterator = resources.iterator(); iterator.hasNext();) {
                    Resource obj = (Resource) iterator.next();
                    String name = obj.getName();
                    resourceSb.append(name);
                    resourceSb.append(",");
                }
            } else {
                resourceSb.append(" - ");
            }
            String str = resourceSb.toString();
            if (str.length() > 0 && !" - ".equals(str)) {
                str = str.substring(0, str.length() - 1);
            }
            //   if (str != null && str.trim().length() > 0) {
            temp += "<tr><td class=\"contentBgColorMail\" align=\"right\" valign=\"top\" nowrap><b>" + app.getMessage("calendar.label.resources", "Resources") + "</b></td>" +
                    "<td class=\"contentBgColorMail\">" + str + "</td></tr>";
            //   }

            String notes = " - ";
            String snotes = notifyNote.getValue().toString();
            if (snotes != null && snotes.trim().length() > 0) {
                notes = StringUtils.replace(snotes, "\n", "<br>");
            }
            //if (notes != null && notes.trim().length() > 0) {
            temp += "<tr><td class=\"contentBgColorMail\" align=\"right\" valign=\"top\" nowrap><b>" + app.getMessage("calendar.label.notes", "Notes") + "</b></td>" +
                    "<td class=\"contentBgColorMail\">" + notes + "</td></tr>";

            //}

            String footer = "</table></td></tr><tr><td colspan=\"2\" valign=\"TOP\"  class=\"contentBgColorMail\">&nbsp;</td>" +
                    "</tr></table><p>&nbsp; </p></body></html>";


            String link = "<tr><td></td><td class=\"contentBgColorMail\" align=\"left\" valign=\"top\"  nowrap><a href=\"http://" + evt.getRequest().getServerName() + ":" + evt.getRequest().getServerPort() + evt.getRequest().getContextPath() +
                    "/ekms/calendar/calendar.jsp?cn=calendarPage.calendarView&et=select&eventId=" + event.getEventId() + "&instanceId=" + event.getInstanceId() + "\">" + app.getMessage("calendar.label.clickToView", "Click here to view") + "</a>" +
                    "</td></tr>";
            message.setBody(temp + link + footer);


            message.setMessageFormat(Message.MESSAGE_FORMAT_HTML);
            message.setDate(new Date());

            if (notifyMemo.isChecked())
                mm.sendMessage(smtpAccount, message, user.getId(), false);
                //mm.sendMessage(smtpAccount, message, user.getId(), false, null, createRequestCalendarFile(event));

            if (notifyEmail.isChecked() && emailList.size() > 0) {
                message.setToList(emailList);
                message.setToIntranetList(null);
                message.setBody(temp + link + footer);
                mm.sendMessage(smtpAccount, message, user.getId(), false, null,
                        createRequestCalendarFile(event));

            } else {
                if (bizContactsList.size() > 0) {
                    message.setToList(bizContactsList);
                    message.setToIntranetList(null);
                    message.setBody(temp + footer);
                    mm.sendMessage(smtpAccount, message, user.getId(), false,
                            null, createRequestCalendarFile(event));
                }
            }
        } catch (Exception e) {
            Log.getLog(getClass()).error("Error sending notification", e);
        }
    }

    public String createRequestCalendarFile(CalendarEvent cEvent)
            throws DataObjectNotFoundException, CalendarException {
        net.fortuna.ical4j.model.Calendar calendar = new net.fortuna.ical4j.model.Calendar();
        User currentUser = Application.getInstance().getCurrentUser();
        calendar.getProperties().add(
                new ProdId("-//The Media Shoppe Bhd//tmsEkp 1.4.1//EN"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);
        calendar.getProperties().add(Method.REQUEST);
        net.fortuna.ical4j.model.PropertyList tzProperties = new net.fortuna.ical4j.model.PropertyList();
        try {

            TimeZone myTz = TimeZone.getDefault();
            TzId tzId = new TzId(myTz.getID());
            TzName tzName = new TzName(myTz.getDisplayName());
            tzProperties.add(tzName);

            TzOffsetFrom tzOffsetFrom = new TzOffsetFrom(myTz.getRawOffset() + "");
            tzProperties.add(tzOffsetFrom);

            TzOffsetTo tzOffsetTo = new TzOffsetTo();
            tzOffsetTo.setValue(myTz.getRawOffset() + "");
            tzProperties.add(tzOffsetTo);

            Daylight daylight = new Daylight(tzProperties);
            Standard standard = new Standard(tzProperties);
            VTimeZone vTimeZone = new VTimeZone();
            vTimeZone.getProperties().add(tzId);
            vTimeZone.getObservances().add(daylight);
            vTimeZone.getObservances().add(standard);
            //calendar.getComponents().add(vTimeZone);

        } catch (Exception e) {

        }


        net.fortuna.ical4j.model.PropertyList eventProperties = new net.fortuna.ical4j.model.PropertyList();
        // Adding Start Date
        DtStart dtStart;
        try {
            //dtStart = new DtStart(new net.fortuna.ical4j.model.DateTime(ICalendarUtil.convertToGMTDate(cEvent.getStartDate())));
            dtStart = new DtStart(new net.fortuna.ical4j.model.DateTime(cEvent.getStartDate()));
            dtStart.validate();
            eventProperties.add(dtStart);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        // Adding End Date
        try {
            DtEnd dtEnd = new DtEnd(new net.fortuna.ical4j.model.DateTime(cEvent.getEndDate()));
            dtEnd.validate();
            eventProperties.add(dtEnd);
        } catch (ValidationException e) {
        }
        DtStamp dtStamp = new DtStamp(new net.fortuna.ical4j.model.DateTime(new Date()));
        eventProperties.add(dtStamp);
        Uid uid = new Uid(cEvent.getEventId());
        eventProperties.add(uid);
        try {
            Organizer organizer = new Organizer();
            organizer.setValue("MAILTO:" + currentUser.getProperty("email1"));
            organizer.getParameters().add(new Cn(currentUser.getName()));
            eventProperties.add(organizer);
        } catch (URISyntaxException e) {
            Log.getLog(getClass()).error("Error while adding Organizer to ICalender " + e.getMessage());
        }
        String clazz = "";
        if (cEvent.getClassification().equals("pri")) {
            clazz = "PRIVATE";
        } else {
            clazz = "PUBLIC";
        }
        eventProperties.add(new Clazz(clazz));
        eventProperties.add(new Summary(cEvent.getTitle()));
        eventProperties.add(new Location(cEvent.getLocation()));
        String description = "";
        if (cEvent.getDescription() != null)
            description = cEvent.getDescription();
        if (cEvent.getAgenda() != null && !cEvent.getAgenda().trim().equals(""))
            description = description + "\nAgenda : " + cEvent.getAgenda();
        eventProperties.add(new Description(description));
        VEvent vEvent = new VEvent(eventProperties);

        // Adding attendees
        for (Iterator i = cEvent.getAttendees().iterator(); i.hasNext();) {
            Attendee attendee = (Attendee) i.next();
            net.fortuna.ical4j.model.ParameterList attedeeParameters = new net.fortuna.ical4j.model.ParameterList();
            String status = "";

            if (attendee.getStatus().equals("C")) {
                status = "ACCEPTED";
            } else if (attendee.getStatus().equals("R")) {
                status = "DECLINED";
            } else {
                status = "NEEDS_ACTION";
            }
            attedeeParameters.add(new PartStat(status));
            attedeeParameters.add(new Cn((String) attendee.getName()));
            String mailTo = "MailTo:";
            if ((String) attendee.getProperty("email") != null) {
                mailTo = mailTo + (String) attendee.getProperty("email");
            }
            try {
                net.fortuna.ical4j.model.property.Attendee iCalAttendee = new net.fortuna.ical4j.model.property.Attendee(
                        attedeeParameters, mailTo);
                eventProperties.add(iCalAttendee);
            } catch (URISyntaxException e) {
                Log.getLog(getClass()).info("Error while creating request iCalendar File", e);
            }
        }
        calendar.getComponents().add(vEvent);
        try {
            calendar.validate();
            CalendarOutputter outputter = new CalendarOutputter();
            //FileOutputStream fout = new FileOutputStream("C:/Documents and Settings/arunkumar/Desktop/noname/mycalendar.ics");
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            //outputter.output(calendar, fout);
            outputter.output(calendar, bao);
            byte[] buffer = bao.toByteArray();
            String str = new String(buffer);
            return str;
        } catch (ValidationException e) {
            Log.getLog(getClass()).info("Error while creating request iCalendar File", e);
            return "";
        } catch (IOException e) {
            Log.getLog(getClass()).info("Error while creating request iCalendar File", e);
            return "";
        }
    }

    protected Collection generateRecurringEvent(CalendarEvent event) {
        Collection col = null;
        col = CalendarUtil.getRecurringEvents(event, event.getStartDate(), CalendarUtil.getLastRecurrenceDate(event));
        if (reminderRadio.isChecked()) {
            int rq = Integer.parseInt((String) reminderQuantity.getValue());
            int rm = Integer.parseInt((String) reminderModifier.getSelectedOptions().keySet().iterator().next());
            Calendar reminderCal = Calendar.getInstance();
            reminderCal.setTime(event.getStartDate());
            int reminderType;
            switch (rm) {
                case Calendar.MINUTE:
                    reminderType = Calendar.MINUTE;
                    break;
                case Calendar.HOUR_OF_DAY:
                    reminderType = Calendar.HOUR_OF_DAY;
                    break;
                case Calendar.DAY_OF_MONTH:
                    reminderType = Calendar.DAY_OF_MONTH;
                    break;
                case Calendar.WEEK_OF_YEAR:
                    reminderType = Calendar.WEEK_OF_YEAR;
                    break;
                default:
                    reminderType = Calendar.MINUTE;
                    break;
            }
            reminderCal.add(reminderType, -rq);
            event.setReminderDate(reminderCal.getTime());
            CalendarEvent recurringEvent;
            for (Iterator i = col.iterator(); i.hasNext();) {
                recurringEvent = (CalendarEvent) i.next();
                reminderCal.setTime(recurringEvent.getStartDate());
                reminderCal.add(reminderType, -rq);
                recurringEvent.setReminderDate(reminderCal.getTime());
            }
        } else {
            event.setReminderDate(null);
        }
        for (Iterator iterator = col.iterator(); iterator.hasNext();) {
            CalendarEvent recurringEvent = (CalendarEvent) iterator.next();
            recurringEvent.setResources(event.getResources());
        }
        return col;
    }

    protected CalendarEvent assembleEvent() throws DataObjectNotFoundException, CalendarException {

        CalendarModule handler = null;
        handler = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
        User user = getWidgetManager().getUser();
        String userId = user.getId();
        CalendarEvent event = null;
        if (eventId != null && eventId.trim().length() > 0) {
            event = handler.getCalendarEvent(getEventId());
        } else {
            // create event object
            event = new CalendarEvent();
        }
        if (event.getEventId() == null) {
            event.setEventId(getPrefix() + "_" + UuidGenerator.getInstance().getUuid());
        }
        if (!newResourceBooking)
            event.setTitle((String) title.getValue());
        else
            event.setTitle("*Resource Booking*");
        event.setDescription(description.getValue().toString());
        event.setAgenda((String) agenda.getValue());
        event.setLocation(location.getValue().toString());
        event.setAllDay(allDay.isChecked());

        event.setUniversal(universal.isChecked());

        if (radioPublic.isChecked()) {
            event.setClassification(CalendarModule.CLASSIFICATION_PUBLIC);
            if (CalendarEvent.class.getName().equals(getPrefix())) {
                event.setUniversal(true);
            }
        } else if (radioPrivate.isChecked())
            event.setClassification(CalendarModule.CLASSIFICATION_PRIVATE);
/*
        else if (radioConfidential.isChecked())
            event.setClassification(CalendarModule.CLASSIFICATION_CONFIDENTIAL);
*/
        // set dates
        Calendar startCal = startDate.getCalendar();
        Calendar endCal = endDate.getCalendar();
        if (!event.isAllDay()) {
            Calendar startTimeCal = startTime.getCalendar();
            startCal.set(Calendar.HOUR_OF_DAY, startTimeCal.get(Calendar.HOUR_OF_DAY));
            startCal.set(Calendar.MINUTE, startTimeCal.get(Calendar.MINUTE));
            event.setStartDate(startCal.getTime());
            Calendar endTimeCal = endTime.getCalendar();
            endCal.set(Calendar.HOUR_OF_DAY, endTimeCal.get(Calendar.HOUR_OF_DAY));
            endCal.set(Calendar.MINUTE, endTimeCal.get(Calendar.MINUTE));
            event.setEndDate(endCal.getTime());
        } else {
            event.setStartDate(startCal.getTime());
            endCal.add(Calendar.DAY_OF_MONTH, 1);
            endCal.add(Calendar.SECOND, -1);
            event.setEndDate(endCal.getTime());
        }
        String recurrenceRule = getRecurrenceRule();
        if (recurrenceRule != null && recurrenceRule.trim().length() > 0) {
            if (event.getRecurrenceRule() != null && !event.getRecurrenceRule().equals(recurrenceRule)) {
               // handler.deleteRecurringEvents(event);
            }
        } else if (event.getRecurrenceRule() != null && event.getRecurrenceRule().trim().length() > 0) {
            //handler.deleteRecurringEvents(event);
        }
        event.setRecurrenceRule(recurrenceRule);
        // handle attendees
        HashMap attendeeList = new HashMap(); // Attendee objects
        //Collection attendeeList = new TreeSet();
        if (!exclude.isChecked()) {
            Attendee att = new Attendee();
            att.setUserId(userId);
            att.setProperty("username", user.getUsername());
            att.setProperty("firstName", user.getProperty("firstName"));
            att.setProperty("lastName", user.getProperty("lastName"));
            att.setCompulsory(true);
            att.setStatus(CalendarModule.ATTENDEE_STATUS_CONFIRMED);
            attendeeList.put(att.getId(), att);
        }
        if (compulsory != null) {
            String[] compulsoryIds = compulsory.getIds();
            for (int i = 0; i < compulsoryIds.length; i++) {
                try {
                    String uid = compulsoryIds[i];
                    if (exclude.isChecked() && userId.equals(uid)) {
                        // ignore if creator is excluded
                        continue;
                    }
                    User tmpUser = UserUtil.getUser(uid);
                    Attendee att = new Attendee();
                    att.setUserId(tmpUser.getId());
                    att.setProperty("username", tmpUser.getUsername());
                    att.setProperty("firstName", tmpUser.getProperty("firstName"));
                    att.setProperty("lastName", tmpUser.getProperty("lastName"));
                    att.setProperty("email", tmpUser.getProperty("email1"));
                    att.setCompulsory(true);
                    att.setEkpUser(true);
                    att.setStatus(CalendarModule.ATTENDEE_STATUS_CONFIRMED);
                    attendeeList.put(att.getId(), att);
                }
                catch (Exception e) {
                    throw new RuntimeException(e.toString());
                }
            }
        }
        if (optional != null) {
            String[] optionalIds = optional.getIds();
            for (int i = 0; i < optionalIds.length; i++) {
                try {
                    boolean isNonEKPUser = false;
                    String uid = "";
                    try {
                        String[] uidSplit = optionalIds[i].split(":");
                        uid = uidSplit[0];
                        if (uidSplit[1].equals("BC"))
                            isNonEKPUser = true;
                    } catch (Exception e) {
                        uid = optionalIds[i];
                        isNonEKPUser = false;
                    }
                    if (exclude.isChecked() && userId.equals(uid)) {
                        // ignore if creator is excluded
                        continue;
                    }
                    if (!isNonEKPUser) {
                        User tmpUser = UserUtil.getUser(uid);
                        Attendee att = new Attendee();
                        att.setUserId(tmpUser.getId());
                        att.setProperty("username", tmpUser.getUsername());
                        att.setProperty("firstName", tmpUser
                                .getProperty("firstName"));
                        att.setProperty("lastName", tmpUser
                                .getProperty("lastName"));
                        att.setProperty("email", tmpUser.getProperty("email1"));
                        att.setCompulsory(false);
                        att.setEkpUser(true);
                        att.setNonEKPUser(isNonEKPUser);
                        att.setStatus(CalendarModule.ATTENDEE_STATUS_PENDING);
                        attendeeList.put(att.getId(), att);
                    } else {
                        Application app = Application.getInstance();
                        Contact tmpUser;
                        try {
                            AddressBookModule dm = (AddressBookModule) app
                                    .getModule(AddressBookModule.class);
                            AddressBookDao abdao = (AddressBookDao) dm.getDao();
                            tmpUser = abdao.selectContact(uid);
                        } catch (DataObjectNotFoundException e) {
                            DirectoryModule dm = (DirectoryModule) app
                                    .getModule(DirectoryModule.class);
                            AddressBookDao abdao = (AddressBookDao) dm.getDao();
                            tmpUser = abdao.selectContact(uid);
                        }
                        Attendee att = new Attendee();
                        att.setUserId(tmpUser.getId());
                        att.setProperty("username", tmpUser
                                .getDisplayFirstName());
                        att.setProperty("firstName", tmpUser.getFirstName());
                        att.setProperty("lastName", tmpUser.getLastName());
                        att.setProperty("email", tmpUser.getEmail());
                        att.setCompulsory(false);
                        att.setNonEKPUser(isNonEKPUser);
                        att.setEkpUser(true);
                        att.setStatus(CalendarModule.ATTENDEE_STATUS_PENDING);
                        attendeeList.put(att.getId(), att);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e.toString());
                }
            }
        }
        if (outsideAttendee.getValue() != null
                && !outsideAttendee.getValue().equals("")) {
            String ids = outsideAttendee.getValue().toString();
            String[] outsideIds;
            try {
                outsideIds = ids.split(",");
            } catch (Exception e) {
                outsideIds = new String[1];
                outsideIds[0] = ids;
            }
            for (int i = 0; i < outsideIds.length; i++) {
                try {
                    String userEmail = "";
                    String temp = outsideIds[i];
                    // String[] userName;
                    try {
                        int st = temp.indexOf("<");
                        int en = temp.indexOf(">");
                        userEmail = temp.substring(st + 1, en);
                        // userName=temp.split("<");
                    } catch (Exception e) {
                        userEmail = temp;
                        // userName=userEmail.split("@");
                    }
                    Attendee att = new Attendee();
                    att.setProperty("username", userEmail);
                    att.setProperty("firstName", userEmail);
                    att.setProperty("email", userEmail);
                    att.setUserId(userEmail);
                    att.setCompulsory(false);
                    att.setEkpUser(false);
                    att.setStatus(CalendarModule.ATTENDEE_STATUS_PENDING);
                    attendeeList.put(att.getId(), att);
                } catch (Exception e) {
                    throw new RuntimeException(e.toString());
                }
            }
        }
        Collection attList = new ArrayList();
        for (Iterator i = attendeeList.keySet().iterator(); i.hasNext();)
        {
            String attId = (String) i.next();
            attList.add(attendeeList.get(attId));
        }
        event.setAttendees(attList);

        ResourceManager rm;
        Map selectedResources = resources.getRightValues();
        Collection resourcesCol = new ArrayList();

        if (selectedResources.size() > 0) {
            rm = (ResourceManager) Application.getInstance().getModule(ResourceManager.class);
            for (Iterator i = selectedResources.keySet().iterator(); i.hasNext();) {
                String resourceId = (String) i.next();
                Resource resource = null;
                try {
                    resource = rm.getResource(resourceId, true);
                }
                catch (DaoException e) {
                    Log.getLog(getClass()).error("Error retrieving resource " + resourceId + ": " + e.toString());
                }
                resourcesCol.add(resource);
            }
        }
        event.setResources(resourcesCol);
        event.setUserId(getWidgetManager().getUser().getId());
        return event;
    }

    protected String getRecurrenceRule() {
        //set Recurrence and recurrenceRules
        if (recurrence.getSelectedOptions().isEmpty())
            return null;
        String selected = (String) recurrence.getSelectedOptions().keySet().iterator().next();
        if (!"0".equals(selected)) {
            StringBuffer recurrenceRule = new StringBuffer();
            if ("1".equals(selected)) { //daily
                recurrenceRule.append("D");
                if (dailyBG.getSelectedButton().equals(everyDay.getAbsoluteName())) {
                    recurrenceRule.append(dailyFrequentTextField.getValue() + " ");
                } else {
                    recurrenceRule.append("1 MO TU WE TH FR ");
                }
            } else if ("2".equals(selected)) {//weekly
                recurrenceRule.append("W" + weeklyFrequentTextField.getValue() + " ");
            } else if ("3".equals(selected)) { //montly
                recurrenceRule.append("M");
                if (monthlyBG.getSelectedButton().equals(everyMonth.getAbsoluteName())) {
                    recurrenceRule.append("D" + monthlyFrequentTextField.getValue() + " ");
                } else {
                    recurrenceRule.append("P" + monthlyFrequentTextField2.getValue() + " ");
                    String order = (String) weekDayOrder.getSelectedOptions().keySet().iterator().next();
                    if ("5".equals(order)) {//the last Xday
                        recurrenceRule.append("1- ");
                    } else {
                        recurrenceRule.append(order + "+ ");
                    }
                    String weekday = (String) weekDay.getSelectedOptions().keySet().iterator().next();
                    String[] weekdays = {"SU", "MO", "TU", "WE", "TH", "FR", "SA"};
                    recurrenceRule.append(weekdays[Integer.parseInt(weekday)] + " ");
                }
            } else if ("4".equals(selected)) { //yearly
                recurrenceRule.append("YD" + yearlyFrequentTextField.getValue() + " ");
            }
            if (occurenceBG.getSelectedButton().equals(occurenceTimes.getAbsoluteName())) {
                recurrenceRule.append("#" + occurenceTimesTF.getValue() + " ");
            } else {
                recurrenceRule.append("" + CalendarUtil.getPdiFormat(endOccurenceDateDF.getDate()) + " ");
            }
            return recurrenceRule.toString();
        }
        return null;
    }

    public SelectBox getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(SelectBox recurrence) {
        this.recurrence = recurrence;
    }

    public String getDefaultTemplate() {
        String prefix = getPrefix();
        if (CalendarEvent.class.getName().equals(prefix) || (eventId != null && eventId.startsWith(CalendarEvent.class.getName())))
        {
            return "calendar/calendarEventForm";
        } else
        if (Appointment.class.getName().equals(prefix) || (eventId != null && eventId.startsWith(CalendarEvent.class.getName())))
        {
            return "calendar/appointmentForm";
        }

        return DEFAULT_TEMPLATE;
    }

    public TextField getYearlyFrequentTextField() {
        return yearlyFrequentTextField;
    }

    public void setYearlyFrequentTextField(TextField yearlyFrequentTextField) {
        this.yearlyFrequentTextField = yearlyFrequentTextField;
    }

    public TextField getDailyFrequentTextField() {
        return dailyFrequentTextField;
    }

    public void setDailyFrequentTextField(TextField dailyFrequentTextField) {
        this.dailyFrequentTextField = dailyFrequentTextField;
    }

    public TextField getWeeklyFrequentTextField() {
        return weeklyFrequentTextField;
    }

    public void setWeeklyFrequentTextField(TextField weeklyFrequentTextField) {
        this.weeklyFrequentTextField = weeklyFrequentTextField;
    }

    public TextField getMonthlyFrequentTextField() {
        return monthlyFrequentTextField;
    }

    public void setMonthlyFrequentTextField(TextField monthlyFrequentTextField) {
        this.monthlyFrequentTextField = monthlyFrequentTextField;
    }

    public TextField getMonthlyFrequentTextField2() {
        return monthlyFrequentTextField2;
    }

    public void setMonthlyFrequentTextField2(TextField monthlyFrequentTextField2) {
        this.monthlyFrequentTextField2 = monthlyFrequentTextField2;
    }

    public SelectBox getReminderModifier() {
        return reminderModifier;
    }

    public void setReminderModifier(SelectBox reminderModifier) {
        this.reminderModifier = reminderModifier;
    }

    public TextField getReminderQuantity() {
        return reminderQuantity;
    }

    public void setReminderQuantity(TextField reminderQuantity) {
        this.reminderQuantity = reminderQuantity;
    }

    public CheckBox getAllDay() {
        return allDay;
    }

    public void setAllDay(CheckBox allDay) {
        this.allDay = allDay;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(Button cancelButton) {
        this.cancelButton = cancelButton;
    }

    public boolean isNewResourceBooking() {
        return newResourceBooking;
    }

    public void setNewResourceBooking(boolean newResourceBooking) {
        this.newResourceBooking = newResourceBooking;
    }

    public ComboSelectBox getResources() {
        return resources;
    }

    public void setResources(ComboSelectBox resources) {
        this.resources = resources;
    }

    public String getPrefix() {
        if (prefix == null) {
            prefix = Appointment.class.getName();
            if (eventId != null && eventId.trim().length() > 0) {
                prefix = eventId.substring(0, eventId.indexOf("_"));
            }
        }
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public CalendarEvent getEvent() {
        return event;
    }

    public void setEvent(CalendarEvent event) {
        this.event = event;
    }

    public AddressBookSelectBox getOptional() {
        return optional;
    }

    public void setOptional(AddressBookSelectBox optional) {
        this.optional = optional;
    }

    public CalendarUsersSelectBox getCompulsory() {
        return compulsory;
    }

    public void setCompulsory(CalendarUsersSelectBox compulsory) {
        this.compulsory = compulsory;
    }

    public String getInit() {
        return init;
    }

    public void setInit(String init) {
        this.init = init;
    }

    public kacang.ui.Forward onSuperSubmit(kacang.ui.Event evt) {
        return super.onSubmit(evt);
    }

    public TextBox getOutsideAttendee() {
        return outsideAttendee;
    }

    public void setOutsideAttendee(TextBox outsideAttendee) {
        this.outsideAttendee = outsideAttendee;
    }

    protected boolean isMessageAddressesValid() {
        try {
            Util.validateStringInternetAddress(outsideAttendee.getValue()
                    .toString());
        } catch (AddressException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }

        return true;
    }

}
