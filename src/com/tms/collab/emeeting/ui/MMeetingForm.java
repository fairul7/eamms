package com.tms.collab.emeeting.ui;

import kacang.stdui.DateField;
import kacang.stdui.Hidden;
import kacang.stdui.Button;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.services.security.User;
import kacang.services.security.SecurityService;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DaoException;
import kacang.util.Log;
import kacang.util.UuidGenerator;
//import com.tms.collab.calendar.ui.CalendarForm;
import com.tms.collab.calendar.model.*;
//import com.tms.collab.calendar.ui.MCalendarForm;
import com.tms.collab.calendar.ui.UserUtil;
import com.tms.collab.emeeting.MeetingHandler;
import com.tms.collab.emeeting.Meeting;
import com.tms.collab.emeeting.MeetingException;
import com.tms.collab.resourcemanager.model.ResourceManager;
import com.tms.collab.resourcemanager.model.Resource;

import java.util.*;

import org.apache.commons.collections.SequencedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: May 19, 2005
 * Time: 1:37:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class MMeetingForm extends MeetingForm
 {
    public static final String FORWARD_ATTENDEE = "attendees";
    private boolean populated = false;

    protected Hidden compulsoryHidden;
    protected Hidden optionalHidden;
    protected Hidden resourcesHidden;

    protected String[] compulsoryList;
    protected String[] optionalList;
    protected String[] resourcesList;

    protected Button attendeeButton;
    protected Map attendeeMap;
    protected Map compulsoryMap;

    public String formType="MEETING";

    public MMeetingForm() {

    }

    public MMeetingForm(String name) {
        super(name);
    }

    public void init() {
        removeChildren();
        super.init();
        setMethod("GET");
        populated = false;
        //
        removeChild(everyDay);
        removeChild(everyWeekDay);
        removeChild(dailyFrequentTextField);
        removeChild(weeklyFrequentTextField);
        removeChild(monthlyFrequentTextField);
        removeChild(certainDayOfMonth);
        removeChild(weekDayOrder);
        removeChild(everyMonth);
        removeChild(weekDay);
        removeChild(monthlyFrequentTextField2);
        removeChild(yearlyFrequentTextField);
        removeChild(occurenceTimes);
        removeChild(occurenceTimesTF);
        removeChild(endOccurenceDate);
        removeChild(endOccurenceDateDF);

        compulsory.setTemplate("kPopupSelectBox");
        optional.setTemplate("kPopupSelectBox");
        resources.setTemplate("kComboSelectBox");
        resources.setRows(5);
        description.setRows("5");
        agenda.setRows("5");
        notifyNote.setRows("5");
        recurrence.setHidden(true);
        removeChild(recurrence);
        removeChild(startDate);
        removeChild(endDate);
        startDate = new DateField("startDate");
        endDate = new DateField("endDate");

        addChild(startDate);
        addChild(endDate);

        compulsoryHidden = new Hidden("compulsoryhidden");
        optionalHidden = new Hidden("optionalhidden");
        resourcesHidden = new Hidden("resourceshidden");
        addChild(compulsoryHidden);
        addChild(optionalHidden);
        addChild(resourcesHidden);

        attendeeButton = new Button("attendeeButton", Application.getInstance().getMessage("calendar.label.Attendees"));
        addChild(attendeeButton);
        attendeeMap = new HashMap();
        compulsoryMap = new HashMap();

        title.setSize("25");
        description.setRows("5");
        description.setCols("20");
    }

    protected String getRecurrenceRule() {
        return "0";
    }

    protected Forward createEvent(Event evt) throws RuntimeException
       {
           try
           {
               CalendarModule module = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
               MeetingHandler handler = (MeetingHandler) Application.getInstance().getModule(MeetingHandler.class);
               CalendarEvent event = assembleEvent();
               String userId = getWidgetManager().getUser().getId();

               String eventId = event.getEventId();
               String prefix = Meeting.class.getName();
               boolean addEvent=false;
               if(!eventId.substring(0, prefix.length()).equals(prefix)) {
                   event.setEventId(prefix + "_" + eventId);
                   addEvent=true;
               }

               meeting = assembleMeeting(event);
               Collection col = generateRecurringEvent(event);
               for(Iterator i = col.iterator(); i.hasNext();)
                   module.addRecurringEvent((CalendarEvent) i.next(), userId, false);

               if (addEvent)
                   handler.addMeeting(meeting, userId);
               else   {
                   try {
                        handler.editMeeting(meeting,userId);
                   }
                   catch(Exception e) {
                       handler.addMeeting(meeting,userId);
                       addEvent=true;
                   }
               }

               if (notifyMemo.isChecked() || notifyEmail.isChecked())
                  sendNotification(event, true, evt);
               // send sms notification
 //              if (notifySms.isChecked())
 //                  sendSMSNotification(meeting);

               init();
               if (addEvent)
                   return new Forward("event added");
               else
                   return new Forward("event updated");
           }catch(ConflictException e)
           {
               eventId=null;
               evt.getRequest().getSession().setAttribute("conflict",e);
               evt.getRequest().getSession().setAttribute("event",meeting);
               evt.getRequest().getSession().setAttribute("recurringEvents",generateRecurringEvent(meeting.getEvent()));
               evt.getRequest().getSession().setAttribute("memo",notifyMemo.isChecked()?Boolean.TRUE:Boolean.FALSE);
               //evt.getRequest().getSession().setAttribute("email",notifyEmail.isChecked()?Boolean.TRUE:Boolean.FALSE);
               evt.getRequest().getSession().setAttribute("email",Boolean.FALSE);
               //evt.getRequest().getSession().setAttribute("sms",notifySms.isChecked()?Boolean.TRUE:Boolean.FALSE);
               //evt.getRequest().getSession().setAttribute("notes",notifyNote.getValue().toString());
               evt.getRequest().getSession().setAttribute("notes","");
               return new Forward("conflict exception");
           }
           catch(Exception e)
           {
               throw new RuntimeException(e.toString());
           }
       }


    protected CalendarEvent assembleEvent() throws DataObjectNotFoundException, CalendarException {

        CalendarModule handler = null;
        handler = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
        User user = getWidgetManager().getUser();
        String userId = user.getId();
        CalendarEvent event = null;
        if (eventId != null && eventId.trim().length() > 0) {
            event = handler.getCalendarEvent(getEventId());
        }
        else {
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
        event.setDescription((description.getValue()==null)?"":description.getValue().toString());
        event.setAgenda((String) agenda.getValue());
        event.setLocation((location.getValue()==null)?"":location.getValue().toString());
        //event.setAllDay(allDay.isChecked());
        event.setAllDay(false);

        event.setUniversal(universal.isChecked());

        if (radioPublic.isChecked()) {
            event.setClassification(CalendarModule.CLASSIFICATION_PUBLIC);
            if (CalendarEvent.class.getName().equals(getPrefix())) {
                event.setUniversal(true);
            }
        }
        else if (radioPrivate.isChecked())
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
        }
        else {
            event.setStartDate(startCal.getTime());
            endCal.add(Calendar.DAY_OF_MONTH, 1);
            endCal.add(Calendar.SECOND, -1);
            event.setEndDate(endCal.getTime());
        }
        String recurrenceRule = (getRecurrenceRule()==null)?"0":getRecurrenceRule();
        if (recurrenceRule != null && recurrenceRule.trim().length() > 0) {
            if (event.getRecurrenceRule() != null && !event.getRecurrenceRule().equals(recurrenceRule)) {
                handler.deleteRecurringEvents(event);
            }
        }
        else if (event.getRecurrenceRule() != null && event.getRecurrenceRule().trim().length() > 0) {
            handler.deleteRecurringEvents(event);
        }
        event.setRecurrenceRule(recurrenceRule);
        // handle attendees
        Collection attendeeList = new TreeSet(); // Attendee objects
        if (!exclude.isChecked()) {
            Attendee att = new Attendee();
            att.setUserId(userId);
            att.setProperty("username", user.getUsername());
            att.setProperty("firstName", user.getProperty("firstName"));
            att.setProperty("lastName", user.getProperty("lastName"));
            att.setCompulsory(true);
            att.setStatus(CalendarModule.ATTENDEE_STATUS_CONFIRMED);
            attendeeList.add(att);
        }
        //setting to attendees
        Collection list = new ArrayList();
        for (Iterator i = compulsoryMap.keySet().iterator(); i.hasNext();)
        {
            String key = (String) i.next();
            list.add(((User) compulsoryMap.get(key)).getId());
        }
        compulsoryList = (String[]) list.toArray(new String[] {});
        if (compulsory != null) {
            String[] compulsoryIds = compulsory.getIds();
            if (compulsoryIds!=null && compulsoryIds.length>0) {
                for (int i=0; i < compulsoryIds.length; i++) {
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
                        att.setCompulsory(true);
                        att.setStatus(CalendarModule.ATTENDEE_STATUS_CONFIRMED);
                        attendeeList.add(att);
                    }
                    catch (Exception e) {
                        throw new RuntimeException(e.toString());
                    }
                }
            }
            else if (compulsoryList!=null && compulsoryList.length>0) {
                for (int i=0; i < compulsoryList.length; i++) {
                    try {
                        String uid = compulsoryList[i];
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
                        att.setCompulsory(true);
                        att.setStatus(CalendarModule.ATTENDEE_STATUS_CONFIRMED);
                        attendeeList.add(att);
                    }
                    catch (Exception e) {
                        throw new RuntimeException(e.toString());
                    }
                }
            }
        }
        //setting to attendees
        list = new ArrayList();
        for (Iterator i = attendeeMap.keySet().iterator(); i.hasNext();)
        {
            String key = (String) i.next();
            list.add(((User) attendeeMap.get(key)).getId());
        }
        optionalList = (String[]) list.toArray(new String[] {});
        if (optional != null) {
            String[] optionalIds = optional.getIds();
            if (optionalIds!=null && optionalIds.length>0) {
                for (int i=0; i < optionalIds.length; i++) {
                    try {
                        String uid = optionalIds[i];
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
                        att.setCompulsory(false);
                        att.setStatus(CalendarModule.ATTENDEE_STATUS_PENDING);
                        attendeeList.add(att);
                    }
                    catch (Exception e) {
                        throw new RuntimeException(e.toString());
                    }
                }
            }
            else if (optionalList!=null && optionalList.length>0) {
                for (int i=0; i < optionalList.length; i++) {
                    try {
                        String uid = optionalList[i];
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
                        att.setCompulsory(false);
                        att.setStatus(CalendarModule.ATTENDEE_STATUS_PENDING);
                        attendeeList.add(att);
                    }
                    catch (Exception e) {
                        throw new RuntimeException(e.toString());
                    }
                }
            }

        }
        event.setAttendees(attendeeList);

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
        else if (resourcesList!=null && resourcesList.length>0) {
            rm = (ResourceManager) Application.getInstance().getModule(ResourceManager.class);
            for (int i=0;i<resourcesList.length; i++) {
                String resourceId = resourcesList[i];
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

    public Forward onSubmit(Event evt) {

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
        }
        else {
            // else check that end date is equal to or after start date
            kacang.ui.Forward result = super.onSuperSubmit(evt);
            if (startDate.getDate() != null && endDate.getDate() != null) {
                if (startDate.getDate().after(endDate.getDate())) {
                    startDate.setInvalid(true);
                    endDate.setInvalid(true);
                    setInvalid(true);
                    return result;
                }
                else if (!allDay.isChecked() && startDate.getDate().equals(endDate.getDate()) && startTime.getDate().after(endTime.getDate())) {
                    startTime.setInvalid(true);
                    endTime.setInvalid(true);
                    setInvalid(true);
                    return result;
                }
                /*
                if (!recurrence.getSelectedOptions().isEmpty() && !"0".equals(recurrence.getSelectedOptions().keySet().iterator().next())) {
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
                        }
                        else if (everyWeekDay.isChecked()) {
                            do {
                                rc.add(Calendar.DAY_OF_MONTH, 1);
                            }
                            while (rc.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || rc.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
                        }
                        //                    rduration = Integer.parseInt((String)dailyFrequentTextField.getValue())*24*60*60*1000;
                    }
                    else if ("2".equals(selected)) {
                        //                    rduration = Integer.parseInt((String)dailyFrequentTextField.getValue())*24*60*60*1000;
                        rc.add(Calendar.WEEK_OF_YEAR, Integer.parseInt((String) weeklyFrequentTextField.getValue()));
                    }
                    else if ("3".equals(selected)) {
                        if (everyMonth.isChecked()) {
                            rc.add(Calendar.MONTH, Integer.parseInt((String) monthlyFrequentTextField.getValue()));
                        }
                        else if (certainDayOfMonth.isChecked()) {
                            rc.add(Calendar.MONTH, Integer.parseInt((String) monthlyFrequentTextField2.getValue()));
                            String order = (String) weekDayOrder.getSelectedOptions().keySet().iterator().next();
                            int[] weekdays = {Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY};
                            rc.set(Calendar.DAY_OF_WEEK, weekdays[Integer.parseInt((String) weekDay.getSelectedOptions().keySet().iterator().next())]);
                            if ("5".equals(order))
                                rc.set(Calendar.DAY_OF_WEEK_IN_MONTH, -1);
                            else
                                rc.set(Calendar.DAY_OF_WEEK_IN_MONTH, Integer.parseInt(order));
                        }
                    }
                    else if ("4".equals(selected)) {
                        rc.add(Calendar.YEAR, Integer.parseInt((String) yearlyFrequentTextField.getValue()));
                    }
                    if (ec.getTime().after(rc.getTime())) {
                        setInvalid(true);
                        return new Forward("Recurrence Exception");
                    }

                }
                */
            }
            return result;
        }

        return null;
    }

    public Forward onValidate(Event event) {
        String sCH = (String)compulsoryHidden.getValue();
        String sOH = (String)optionalHidden.getValue();
        String sRH = (String)resourcesHidden.getValue();

        if (sCH!=null&&sCH.length()>0) {
            compulsoryList=sCH.split(",");
        }
        if (sOH!=null && sOH.length()>0) {
            optionalList=sOH.split(",");
        }
        if (sRH!=null&&sRH.length()>0) {
            resourcesList=sRH.split(",");
        }
        /*
        if (exclude.isChecked()) {
            if (compulsoryList.length == 0 && optionalList.length == 0) {
                return new Forward("selectAttendees");
            }
        }
        */
        if(!isInvalid())
        {
            if(attendeeButton.getAbsoluteName().equals(findButtonClicked(event)))
                return new Forward(FORWARD_ATTENDEE);
        }
        return super.onValidate(event);
    }

    public String getDefaultTemplate()
    {
        return "calendar/mappointmentForm"; //CalendarForm.DEFAULT_TEMPLATE;
    }

    public void onRequest(Event event)
    {
        if(!populated)
        {
            super.onRequest(event);
            if(eventId!=null&&eventId.trim().length()>0)
            {
                MeetingHandler handler = (MeetingHandler) Application.getInstance().getModule(MeetingHandler.class);
                try
                {
                    meeting = handler.getMeeting(eventId, true);
                }
                catch (MeetingException e)
                {
                    Log.getLog(MeetingFormEdit.class).error(e);
                }
                init();
                Map resourcesMap = new SequencedHashMap();
                ResourceManager rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);
                try{
                    Collection col = rm.getResources(getWidgetManager().getUser().getId(),true);
                    for(Iterator i=col.iterator();i.hasNext();){
                        Resource resource = (Resource)i.next();
                        resourcesMap.put(resource.getId(),resource.getName());
                    }
                }catch(Exception e){
                    Log.getLog(getClass()).error(e);
                }
                resources.setLeftValues(resourcesMap);
                populateForm();
                try {
                    SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
                    String sCompulsory="";
                    String sOptional="";
                    String sResources="";
                    boolean startC=true,startO=true,startR=true;
                    Collection col = meeting.getEvent().getAttendees();
                    if(col!=null&&col.size()>0) {
                        Attendee att;
                        for(Iterator i=col.iterator();i.hasNext();) {
                            att = (Attendee)i.next();
                            if (att.isCompulsory()) {
                                if (startC)
                                    sCompulsory=att.getUserId();
                                else
                                    sCompulsory+=","+att.getUserId();
                                startC=false;
                                try
                                {
                                    compulsoryMap.put(att.getUserId(), service.getUser(att.getUserId()));
                                }
                                catch(Exception e) {}
                            }
                            else {
                                if (startO)
                                    sOptional=att.getUserId();
                                else
                                    sOptional+=","+att.getUserId();
                                startO=false;
                                try
                                {
                                    attendeeMap.put(att.getUserId(), service.getUser(att.getUserId()));
                                }
                                catch(Exception e) {}
                            }
                        }
                    }

                    col = meeting.getEvent().getResources();
                    if (col!=null&&col.size()>0) {
                        for(Iterator i=col.iterator();i.hasNext();) {
                            Resource rc = (Resource)i.next();
                            if (startR)
                                sResources=rc.getId();
                            else
                                sResources+=","+rc.getId();
                            startR=false;
                        }
                    }

                    compulsoryHidden.setValue(sCompulsory);
                    optionalHidden.setValue(sOptional);
                    resourcesHidden.setValue(sResources);
                }
                catch(Exception e) {

                }
            }
            allDay.setHidden(true);
        }
        populated = false;
    }

    public Hidden getCompulsoryHidden() {
        return compulsoryHidden;
    }

    public void setCompulsoryHidden(Hidden compulsoryHidden) {
        this.compulsoryHidden = compulsoryHidden;
    }

    public Hidden getOptionalHidden() {
        return optionalHidden;
    }

    public void setOptionalHidden(Hidden optionalHidden) {
        this.optionalHidden = optionalHidden;
    }

    public Hidden getResourcesHidden() {
        return resourcesHidden;
    }

    public void setResourcesHidden(Hidden resourcesHidden) {
        this.resourcesHidden = resourcesHidden;
    }

    public String getFormType() {
        return formType;
    }

    public Button getAttendeeButton()
    {
        return attendeeButton;
    }

    public void setAttendeeButton(Button attendeeButton)
    {
        this.attendeeButton = attendeeButton;
    }

    public Map getAttendeeMap()
    {
        return attendeeMap;
    }

    public void setAttendeeMap(Map attendeeMap)
    {
        this.attendeeMap = attendeeMap;
    }

    public Map getCompulsoryMap()
    {
        return compulsoryMap;
    }

    public void setCompulsoryMap(Map compulsoryMap)
    {
        this.compulsoryMap = compulsoryMap;
    }

    public boolean isPopulated()
    {
        return populated;
    }

    public void setPopulated(boolean populated)
    {
        this.populated = populated;
    }
}
