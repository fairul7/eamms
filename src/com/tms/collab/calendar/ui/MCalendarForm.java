package com.tms.collab.calendar.ui;

import kacang.stdui.*;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;
import kacang.services.security.*;
import kacang.services.security.SecurityException;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import java.util.*;

import org.apache.commons.collections.SequencedHashMap;
import com.tms.collab.resourcemanager.model.ResourceManager;
import com.tms.collab.resourcemanager.model.Resource;
import com.tms.collab.calendar.model.*;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Oct 6, 2004
 * Time: 2:48:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class MCalendarForm extends MACalendarForm{
    private boolean populated = false;

    public static final String FORWARD_BACK = "back";
    public static final String FORWARD_ATTENDEE = "attendees";
    public static final String FORWARD_APPOINTMENT = "appointment";

    protected Hidden compulsoryHidden;
    protected Hidden optionalHidden;
    protected Hidden resourcesHidden;

    protected String[] compulsoryList;
    protected String[] optionalList;
    protected String[] resourcesList;

    protected Button attendeeButton;
    protected Map attendeeMap;
    protected Map compulsoryMap;

    public String formType="APPOINTMENT";

    public void init() {
        super.init();
        setMethod("GET");
        title.setSize("25");
        description.setRows("5");
        description.setCols("20");
        location.setSize("25");

        populated = false;
        removeChild(recurrence);
        removeChild(startDate);
        removeChild(endDate);

        startDate = new DateField("startDate");
        endDate = new DateField("endDate");

        addChild(startDate);
        addChild(endDate);
        addChild(resources);

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
    }


    public void onRequest(Event event) {

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
            Log.getLog(getClass()).error(e);
        }
        resources.setLeftValues(resourcesMap);
        
        if(eventId!=null&&eventId.trim().length()>0&&!populated){
            init();
            resources.setLeftValues(resourcesMap);
            populateForm();
        }

        if (getPrefix().equals("com.tms.collab.calendar.model.Appointment")) {
            allDay.setHidden(true);
        }
        else {
            allDay.setHidden(true);
            startTime.setHidden(true);
            endTime.setHidden(true);
            //startTime.setDate(Calendar.getInstance().getTime());
            //endTime.setDate(Calendar.getInstance().getTime());
            removeChild(reminderModifier);
            reminderModifier = new SelectBox("reminderModifier");
            reminderModifier.addOption(new Integer(Calendar.DAY_OF_MONTH).toString(), Application.getInstance().getMessage("calendar.label.optionDays","day(s)"));
            //reminderModifier.addOption(new Integer(Calendar.WEEK_OF_YEAR).toString(), Application.getInstance().getMessage("calendar.label.optionWeeks","week(s)"));
            addChild(reminderModifier);                    
            removeChild(startTime);
            removeChild(endTime);
        }

        populated = false;
           //To change body of overridden methods use File | Settings | File Templates.
    }

    protected void populateForm() {

        super.populateForm();    //To change body of overridden methods use File | Settings | File Templates.
        try {
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            CalendarModule handler = (CalendarModule)Application.getInstance().getModule(CalendarModule.class);
            CalendarEvent event = handler.getCalendarEvent(getEventId());
            String sCompulsory="";
            String sOptional="";
            String sResources="";
            boolean startC=true,startO=true,startR=true;
            Collection col = event.getAttendees();
            if (col!=null&&col.size()>0) {
                Attendee att;
                for (Iterator i=col.iterator();i.hasNext();) {
                    att = (Attendee)i.next();
                    if (att.isCompulsory()){
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

            col = event.getResources();
            if (col!=null&&col.size()>0) {
                for (Iterator i=col.iterator();i.hasNext();) {
                    Resource rc = (Resource)i.next();
                    if (startR)
                        sResources = rc.getId();
                    else
                        sResources += ","+rc.getId();
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

    public Forward actionPerformed(Event evt) {
        return super.actionPerformed(evt);    //To change body of overridden methods use File | Settings | File Templates.
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
            kacang.ui.Forward result = super.onSuperSubmit(evt);
            // else check that end date is equal to or after start date
            if (startDate.getDate() != null && endDate.getDate() != null) {
                if (startDate.getDate().after(endDate.getDate())) {
                    startDate.setInvalid(true);
                    endDate.setInvalid(true);
                    setInvalid(true);
                    return result;
                }
            }
            return result;
        }
        return null;
    }

    public Forward onValidate(Event evt) {
        compulsoryList=null;
        optionalList=null;
        resourcesList=null;

        String sCH = (String)compulsoryHidden.getValue();
        String sOH = (String)optionalHidden.getValue();
        String sRH = (String)resourcesHidden.getValue();

        if (sCH!=null && sCH.length()>0)
            compulsoryList = sCH.split(",");
        if (sOH!=null && sOH.length()>0)
            optionalList = sOH.split(",");
        if (sRH!=null && sRH.length()>0)
            resourcesList = sRH.split(",");

        if (exclude.isChecked()) {
            if (compulsoryList.length == 0 && optionalList.length == 0) {
                return new Forward("selectAttendees");
                //String msg = Application.getInstance().getMessage("calendar.message.atLeastOneAttendee", "Please select at least one attendee.");
                //attendeeAlert.setText("<script>alert('" + msg + "');</script>");
                //setInvalid(true);
            }
        }
        //else {
        if(!isInvalid())
        {
            if(attendeeButton.getAbsoluteName().equals(findButtonClicked(evt)))
                return new Forward(FORWARD_ATTENDEE);
        }
        return super.onValidate(evt);    //To change body of overridden methods use File | Settings | File Templates.
        //}
    }

    public String getDefaultTemplate() {
        String prefix = getPrefix();
        if (CalendarEvent.class.getName().equals(prefix) || (eventId != null && eventId.startsWith(CalendarEvent.class.getName()))) {
            return "calendar/mcalendarEventForm";
        }
        else if (Appointment.class.getName().equals(prefix) || (eventId != null && eventId.startsWith(CalendarEvent.class.getName()))) {
            return "calendar/mappointmentForm";
        } 

        return DEFAULT_TEMPLATE;
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
        if (getPrefix().equals("com.tms.collab.calendar.model.Appointment")) {
            event.setAllDay(false);
        }
        else {
            event.setAllDay(true);
        }
        //event.setAllDay(allDay.isChecked());

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

    public void setCompulsoryHidden(Hidden compulsoryHidden) {
        this.compulsoryHidden = compulsoryHidden;
    }

    public Hidden getCompulsoryHidden() {
        return compulsoryHidden;
    }

    public void setOptionalHidden(Hidden optionalHidden) {
        this.optionalHidden = optionalHidden;
    }

    public Hidden getOptionalHidden() {
        return optionalHidden;
    }

    public void setResourcesHidden(Hidden resourcesHidden) {
        this.resourcesHidden = resourcesHidden;
    }

    public Hidden getResourcesHidden() {
        return resourcesHidden;
    }

    public String[] getCompulsoryList() {
        return compulsoryList;
    }

    public void setCompulsoryList(String[] compulsoryList) {
        this.compulsoryList = compulsoryList;
    }

    public String[] getOptionalList() {
        return optionalList;
    }

    public void setOptionalList(String[] optionalList) {
        this.optionalList = optionalList;
    }

    public String[] getResourcesList() {
        return resourcesList;
    }

    public void setResourcesList(String[] resourcesList) {
        this.resourcesList = resourcesList;
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
