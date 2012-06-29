package com.tms.collab.calendar.ui;

import com.tms.collab.calendar.model.*;
import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.taskmanager.ui.TaskSummaryView;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.SecurityException;
import kacang.services.security.User;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DaoException;
import kacang.stdui.*;
import kacang.stdui.event.FormEventAdapter;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.*;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.PartStat;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Clazz;
import net.fortuna.ical4j.model.property.Comment;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStamp;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.Method;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
public class CalendarView extends CalendarBox {

    private String backUrl = "";
    /*private Object[][] monthlyEntries = null;
    private Object[] weeklyEntries = null;
    private Object dailyEntry = null;*/
    private CalendarEvent selectedEvent = null;
    /*private Collection eventList = null;*/
    private Form userSelectForm;
    private Form groupSelectForm;
    private Form jumpForm;
    private DateField jumpDate;
    private Button jumpButton;
    private CalendarUsersSelectBox userSelectBox;
    private SelectBox groupSelectBox;
    private Button groupSelectButton;
    private Button userSelectButton;
    private String userId;
    private String[] userIds;
    private String eventUrl;
    private String selectedEventId;
    private String selectedInstanceId;
    private String view;
    private CalendarEventView calendarEventView = null;
    /*private Collection taskViews;*/
    public static final String PARAMETER_KEY_EVENT_ACCEPT = "accept";
    public static final String PARAMETER_KEY_EVENT_DELETE = "delete";
    public static final String PARAMETER_KEY_EVENT_DELETEALL = "deleteall";
    public static final String PARAMETER_KEY_EVENT_SELECT = "select";
    public static final String PARAMETER_KEY_EVENT_VIEW = "view";
    public static final String PARAMETER_KEY_EVENT_NEXT = "nextEvent";
    public static final String PARAMETER_KEY_EVENT_PREVIOUS = "previousEvent";
    public static final String PARAMETER_KEY_EVENT_EDIT = "edit";
    public static final String PARAMETER_KEY_EVENTID = "eventId";
    public static final String PARAMETER_KEY_INSTANCEID = "instanceId";
    public static final String VIEW_MONTHLY = "monthly";
    public static final String VIEW_WEEKLY = "weekly";
    public static final String VIEW_DAILY = "daily";
    public static final String VIEW_EVENT = "event";

    public CalendarView() {
    }

    public CalendarView(String name)
    {
        super(name);
    }

    public void init() {
        if(view == null||view.trim().length()<=0)
            view = VIEW_DAILY;
        calendarEventView = new CalendarEventView("eventView");
        addChild(calendarEventView);
        jumpForm = new Form("jumbForm");
        jumpForm.setMethod("post");
        jumpForm.addFormEventListener(new FormEventAdapter(){
            public Forward onValidate(Event event){
                setView(VIEW_DAILY);
                setDate(jumpDate.getDate());
                return null;
            }
        });
        jumpButton = new Button("jumpbutton",Application.getInstance().getMessage("calendar.label.goto","Go to"));
        jumpForm.addChild(jumpButton);
        jumpDate = new DateField("jumpdate");
        jumpForm.addChild(jumpDate);
        addChild(jumpForm);
        groupSelectForm = new Form("groupSelectForm");
        groupSelectForm.setMethod("post");
        groupSelectForm.addFormEventListener(new FormEventAdapter(){
            public Forward onValidate(Event event)
            {
                Map groupMap = groupSelectBox.getSelectedOptions();
                SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
                Collection groupIds = new TreeSet();
                for (Iterator iterator = groupMap.keySet().iterator(); iterator.hasNext();)
                {
                    String id = (String) iterator.next();
                    try
                    {
                        Collection col = ss.getGroupUsers(id);
                        for (Iterator it = col.iterator(); it.hasNext();)
                        {
                            User user = (User) it.next();
                            groupIds.add(user.getId());
                        }
                    } catch (SecurityException e)
                    {
                        Log.getLog(getClass()).error("Error retrieving group users", e);
                    }
                }
                userIds = (String[])groupIds.toArray(new String[0]);
                CalendarView.this.userSelectBox.setIds(new String[0]);
                if(CalendarView.this.getView().equals(CalendarView.VIEW_EVENT))
                    CalendarView.this.setView(CalendarView.VIEW_DAILY);

                return null;
            }
        });
        userSelectForm = new Form("userSelectForm");
        userSelectForm.setMethod("post");
        userSelectForm.addFormEventListener(new FormEventAdapter() {
            public Forward onValidate(Event event) {

                userIds = userSelectBox.getIds();
                if (userIds.length == 0) {
                    String userId = getWidgetManager().getUser().getId();
                    userIds = new String[] { userId };
                    userSelectBox.setIds(userIds);
                }
                CalendarView.this.groupSelectBox.setSelectedOptions(new String[]{});
                if(CalendarView.this.getView().equals(CalendarView.VIEW_EVENT))
                    CalendarView.this.setView(CalendarView.VIEW_DAILY);

                return null;
            }
        });
        addChild(userSelectForm);
                userSelectButton = new Button("userSelectButton");
        userSelectButton.setText(Application.getInstance().getMessage("calendar.label.view","View"));
        userSelectForm.addChild(userSelectButton);

        userSelectBox = new CalendarUsersSelectBox("userSelectBox");
        userSelectBox.setMultiple(true);
        userSelectBox.setRows(3);

        groupSelectButton = new Button("groupSelectButton",Application.getInstance().getMessage("calendar.label.view","View"));

        groupSelectBox = new SelectBox("groupSelectBox");
        groupSelectBox.setMultiple(false);
        groupSelectBox.setRows(5);

        SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);

        try
        {
            groupSelectBox.setOptions(ss.getGroupsByPermission(CalendarModule.PERMISSION_CALENDARING,Boolean.TRUE,"groupName",false,0,-1),"id","groupName");
        } catch (SecurityException e)
        {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }
        userSelectForm.addChild(userSelectBox);
        groupSelectForm.addChild(groupSelectBox);
        groupSelectForm.addChild(groupSelectButton);
        addChild(groupSelectForm);

        // init users select box
        userSelectBox.init();
    }

    public Form getUserSelectForm() {
        return userSelectForm;
    }

    public SelectBox getUserSelectBox() {
        return userSelectBox;
    }

    public Button getUserSelectButton() {
        return userSelectButton;
    }

    public String[] getUserIds() {
        return userIds;
    }

    public void setUserIds(String[] userIds)
    {
        this.userIds = userIds;
    }

    public String getEventUrl() {
        return eventUrl;
    }

    public void setEventUrl(String eventUrl) {
        this.eventUrl = eventUrl;
    }

    public String getSelectedEventId() {
        return selectedEventId;
    }

    public void setSelectedEventId(String selectedEventId) {
        this.selectedEventId = selectedEventId;
    }

    public String getSelectedInstanceId() {
        return selectedInstanceId;
    }

    public void setSelectedInstanceId(String selectedInstanceId) {
        this.selectedInstanceId = selectedInstanceId;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public CalendarEventView getCalendarEventView()
    {
        return calendarEventView;
    }

    public void setCalendarEventView(CalendarEventView calendarEventView)
    {
        this.calendarEventView = calendarEventView;
    }

    public String getUserId() {
    	return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDefaultTemplate() {
        String template;

        if (VIEW_MONTHLY.equals(getView())) {
            template = "calendar/monthlyCalendarView";
        }
        else if (VIEW_WEEKLY.equals(getView())) {
            template = "calendar/weeklyCalendarView";
        }
        else if (VIEW_DAILY.equals(getView())) {
            template = "calendar/dailyCalendarView";
        }
        else if(VIEW_EVENT.equals(getView())) {
            template = "calendar/eventView";
        }
        else {
            template = super.getDefaultTemplate();
        }

        return template;
    }

    public Forward actionPerformed(Event evt) {

        String eventType = evt.getRequest().getParameter(Event.PARAMETER_KEY_EVENT_TYPE);
        if (PARAMETER_KEY_EVENT_SELECT.equals(eventType)) {
            Forward forward = onEventSelect(evt);
            return forward;

        }
        else if (PARAMETER_KEY_EVENT_VIEW.equals(eventType)) {
            return onEventView(evt);
        }
        else if (PARAMETER_KEY_DATE_SELECT.equals(eventType)) {
            return onDateSelect(evt);
        }
        else if (PARAMETER_KEY_SCROLL_NEXT.equals(eventType)) {
            return onScrollNext(evt);
        }
        else if (PARAMETER_KEY_SCROLL_PREVIOUS.equals(eventType)) {
            return onScrollPrevious(evt);
        } else if(PARAMETER_KEY_EVENT_NEXT.equals(eventType)){
            return onEventNext(evt);
        } else if(PARAMETER_KEY_EVENT_PREVIOUS.equals(eventType)){
            try
            {
                return onEventPrevious(evt);
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            } catch (DataObjectNotFoundException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
        } else if(PARAMETER_KEY_EVENT_EDIT.equals(eventType)){
            return onEventEdit(evt);
        } else if(PARAMETER_KEY_EVENT_DELETE.equals(eventType)){
            return onEventDelete(evt);
        } else if(PARAMETER_KEY_EVENT_DELETEALL.equals(eventType)){
           return onEventDeleteAll(evt);
        } else if(PARAMETER_KEY_EVENT_ACCEPT.equals(eventType)) {
           return onEventAccept(evt);
        }
/*
        else {
            setView(VIEW_MONTHLY);
        }
*/
        return null;
    }

    public Forward onEventAccept(Event evt){
        CalendarModule cm = (CalendarModule)Application.getInstance().getModule(CalendarModule.class);
        String eventId = evt.getRequest().getParameter(PARAMETER_KEY_EVENTID);
        String instanceId = evt.getRequest().getParameter(PARAMETER_KEY_INSTANCEID);
        String attendeeId = evt.getRequest().getParameter("attendeeId");

        if(eventId!=null&&eventId.trim().length()>0)
            cm.acceptAppointment(eventId,instanceId,attendeeId);
        return null;
    }

    public Forward onEventDelete(Event evt){
        CalendarModule cm = (CalendarModule)Application.getInstance().getModule(CalendarModule.class);
        try{
            if(selectedEvent!=null){
            	cm.deleteRecurringEvent(selectedEvent);
            	//setView(VIEW_MONTHLY);
            }
            else{
                String deleteId = evt.getRequest().getParameter(CalendarView.PARAMETER_KEY_EVENTID);
                if(deleteId!=null&&deleteId.trim().length()>0){
                	
                	CalendarEvent event = getCalendarEvent(deleteId, CalendarModule.DEFAULT_INSTANCE_ID);
                	
                	if(event.getRecurrenceRule() != null && !"".equals(event.getRecurrenceRule())){
                		Calendar eventStartDate = Calendar.getInstance();
                		eventStartDate.setTime(event.getStartDate());
                		Calendar calDate = Calendar.getInstance();
                		calDate.setTime(getDate());
                		eventStartDate.set(Calendar.DAY_OF_MONTH, calDate.get(Calendar.DAY_OF_MONTH));
                		eventStartDate.set(Calendar.MONTH, calDate.get(Calendar.MONTH));
                		eventStartDate.set(Calendar.YEAR, calDate.get(Calendar.YEAR));
                		event.setStartDate(eventStartDate.getTime());
                		cm.deleteRecurringEvent(event);
                	}
                	else{
                		cm.deleteCalendarEvent(deleteId,userId);
                	}
                	
                    
                }
            }
        } catch(Exception e){
            Log.getLog(getClass()).error(e, e);  //To change body of catch statement use Options | File Templates.
        }
        return null;
    }

    public Forward onEventDeleteAll(Event evt){
        CalendarModule cm = (CalendarModule)Application.getInstance().getModule(CalendarModule.class);
        try{
        	if(getSelectedEventId() != null){
	            String className = getSelectedEventId().substring(0, getSelectedEventId().indexOf("_"));
	            CalendaringEventView view = (CalendaringEventView)Class.forName(className).newInstance();
	            CalendarEvent event = getCalendarEvent(getSelectedEventId(), CalendarModule.DEFAULT_INSTANCE_ID);
	            Forward result = view.deleteEvent(getSelectedEventId(),userId);
	            //cm.deleteCalendarEvent(getSelectedEventId(),userId);
	            if(result.getName().equals(CalendaringEventView.DELETE_SUCCESSFUL)){
	                cm.deleteRecurringEvents(event);
	//                setView(VIEW_MONTHLY);
	            }
        	}else{
        		//trigger in the daily view delete all button for appointment
        		String eventId = evt.getRequest().getParameter("eventId");
        		
        		if(eventId != null && !"".equals(eventId)){
        			CalendarEvent event = getCalendarEvent(eventId, CalendarModule.DEFAULT_INSTANCE_ID);
        			String className = eventId.substring(0, eventId.indexOf("_"));
        			CalendaringEventView view = (CalendaringEventView)Class.forName(className).newInstance();
        			Forward result = view.deleteEvent(eventId,userId);
        			if(result.getName().equals(CalendaringEventView.DELETE_SUCCESSFUL)){
    	                cm.deleteRecurringEvents(event);
    	            }
        		}
        		
        	}
        }catch(Exception e){
            Log.getLog(getClass()).error(e, e);  //To change body of catch statement use Options | File Templates.
        }
        return null;
    }

    public Forward onEventEdit(Event evt){
       // try{
            /*evt.getResponse().sendRedirect(editUrl+"?eventId="+evt.getRequest().getParameter(PARAMETER_KEY_EVENTID)+
                    "&viewUrl="+evt.getRequest().getRequestURI());*/
/*
            calendarEventView.setState(CalendarEventView.EDIT);
            calendarEventView.setEventId(evt.getRequest().getParameter(PARAMETER_KEY_EVENTID));
            setView(VIEW_EVENT);
        }catch(Exception e){}
        return null;
*/
        String id = evt.getRequest().getParameter(PARAMETER_KEY_EVENTID);
        calendarEventView.setEventId(id);
        return new Forward("Edit"+id.substring(id.lastIndexOf(".")+1,id.indexOf("_")));
    }

    public Forward onEventView(Event evt) {
        String v = evt.getRequest().getParameter(PARAMETER_KEY_EVENT_VIEW);
        selectedEvent = null;
        if (v != null && (v.equals(VIEW_MONTHLY) || v.equals(VIEW_WEEKLY) || v.equals(VIEW_DAILY))) {
            setView(v);
            return onDateSelect(evt);
        }
        else {
            return null;
        }
    }

    public Forward onEventNext(Event evt){
        CalendarModule cm = (CalendarModule)Application.getInstance().getModule(CalendarModule.class);
        selectedEvent = cm.getNextEvent(getDate(),getUserIds());
        if(selectedEvent!=null){
            setSelectedEventId(selectedEvent.getEventId());
            setSelectedInstanceId(selectedEvent.getRecurrenceId());
            try
            {
                selectedEvent.setAttendees(cm.getAttendees(getSelectedEventId(),getSelectedInstanceId()));
            } catch (DataObjectNotFoundException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
            calendarEventView.setState(CalendarEventView.VIEW);
            calendarEventView.setEvent(selectedEvent);
            calendarEventView.setInstanceId(selectedEvent.getInstanceId());
            calendarEventView.setEventId(selectedEvent.getEventId());
            setDate(selectedEvent.getStartDate()==null?
                    selectedEvent.getEndDate():selectedEvent.getStartDate());
        } else {
            setView(CalendarView.VIEW_DAILY);
        }
        return fireActionEvent(evt);
    }

    public Forward onEventPrevious(Event evt) throws DaoException, DataObjectNotFoundException
    {
        CalendarModule cm = (CalendarModule)Application.getInstance().getModule(CalendarModule.class);
        selectedEvent = cm.getPreviousEvent(getDate(),getUserIds());
        if(selectedEvent!=null){
            setSelectedEventId(selectedEvent.getEventId());
            setSelectedInstanceId(selectedEvent.getRecurrenceId());
            try
            {
                selectedEvent.setAttendees(cm.getAttendees(getSelectedEventId(),getSelectedInstanceId()));
            } catch (DataObjectNotFoundException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
            calendarEventView.setState(CalendarEventView.VIEW);
            calendarEventView.setEvent(selectedEvent);
            calendarEventView.setInstanceId(selectedEvent.getInstanceId());
            calendarEventView.setEventId(selectedEvent.getEventId());
            setDate(selectedEvent.getStartDate()==null?
                    selectedEvent.getEndDate():selectedEvent.getStartDate());
        } else {
            setView(CalendarView.VIEW_DAILY);
        }
        return fireActionEvent(evt);
    }

    public Forward onEventSelect(Event evt) {
        backUrl = evt.getRequest().getRequestURI() + "?"+evt.getRequest().getQueryString();
       /* Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.
*/      String reminderId = evt.getRequest().getParameter("remindId");

        setSelectedEventId(evt.getRequest().getParameter(PARAMETER_KEY_EVENTID));
        setSelectedInstanceId(evt.getRequest().getParameter(PARAMETER_KEY_INSTANCEID));
        if(reminderId!=null&&reminderId.trim().length()>0){
            selectedEvent = getCalendarEvent(reminderId);
            //   selectedEvent= null;
        } else
            selectedEvent=getCalendarEvent(getSelectedEventId(),getSelectedInstanceId());
        setSelectedEventId(selectedEvent.getEventId());
        setSelectedInstanceId(selectedEvent.getInstanceId());
        //calendarEventView.setEventId(getSelectedEventId());
        try{
            CalendarModule cm = (CalendarModule)Application.getInstance().getModule(CalendarModule.class);
            selectedEvent.setAttendees(cm.getAttendees(getSelectedEventId(),getSelectedInstanceId()));
            calendarEventView.setUserId(getWidgetManager().getUser().getId());
            calendarEventView.setState(CalendarEventView.VIEW);
            calendarEventView.setInstanceId(getSelectedInstanceId());
            calendarEventView.setEventId(getSelectedEventId());
            /*if(reminder)
                calendarEventView.setEvent(selectedEvent);
            else
                calendarEventView.setEvent(null);*/
            if(!getSelectedInstanceId().equals("0")&&getSelectedInstanceId().trim().length()>0){
                setDate(CalendarUtil.getRecurrenceDate(getSelectedInstanceId()));
            }else{
                setDate(selectedEvent.getStartDate()==null?
                        selectedEvent.getEndDate():selectedEvent.getStartDate());
            }
        }catch(Exception e){
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }
        setView(VIEW_EVENT);
        return fireActionEvent(evt);
    }

    protected CalendarEvent getCalendarEvent(String id){
        /*CalendarEvent event;
        for(Iterator i= eventList.iterator();i.hasNext();){
            event = (CalendarEvent)i.next();
            if(event.getId().equals(id)){
                    return event;
            }
        }
        return null;*/
        return getCalendarEvent(id, CalendarModule.DEFAULT_INSTANCE_ID);
    }

    protected CalendarEvent getCalendarEvent(String eventId,String InstanceId){
    //    CalendarEvent event;
     //   if(eventList == null){
            CalendarModule cm = (CalendarModule)Application.getInstance().getModule(CalendarModule.class);
            try
            {
                return cm.getCalendarEvent(eventId,InstanceId);
            } catch (DataObjectNotFoundException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            } catch (CalendarException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
        /* }
        for(Iterator i= eventList.iterator();i.hasNext();){
            event = (CalendarEvent)i.next();
            if(event.getEventId().equals(eventId)){
                if(InstanceId!=null&&!InstanceId.equals("0")){
                    if(InstanceId.equals(event.getRecurrenceId()))
                        return event;
                }else
                    return event;
            }
        }*/
        return null;
    }

    public Forward onDateSelect(Event evt) {
        try {
            String dayStr = evt.getRequest().getParameter(getDayOfMonthName());
            String weekStr = evt.getRequest().getParameter(getWeekOfMonthName());
            String monthStr = evt.getRequest().getParameter(getMonthName());
            String yearStr = evt.getRequest().getParameter(getYearName());
            Calendar cal = Calendar.getInstance();
            cal.setLenient(false);
            cal.setTime(getDate());
            if(dayStr!=null&&dayStr.trim().length()>0){
                int day = Integer.parseInt(dayStr);
                cal.set(Calendar.DAY_OF_MONTH, day);
            }

            if(monthStr!=null&&monthStr.trim().length()>0){
                cal.set(Calendar.MONTH,Integer.parseInt(monthStr));
            }
            if(weekStr!=null&&weekStr.trim().length()>0){
                cal.set(Calendar.WEEK_OF_MONTH,Integer.parseInt(weekStr));
            }
            if(yearStr!=null&&yearStr.trim().length()>0){
                cal.set(Calendar.YEAR,Integer.parseInt(yearStr));
            }
            setSelectedDate(cal.getTime());
            setDate(cal.getTime());
            return fireCalendarBoxDateSelectEvent(evt);
        }
        catch(Exception e) {
            return null;
        }
    }

    public Forward onScrollNext(Event evt) {
        try {
            if (VIEW_MONTHLY.equals(getView())) {
                String monthStr = evt.getRequest().getParameter(getMonthName());
                int month = Integer.parseInt(monthStr);
                Calendar cal = Calendar.getInstance();
                cal.setTime(getDate());
                if (cal.get(Calendar.MONTH) == month) {
                    cal.add(Calendar.MONTH, 1);
                    setDate(cal.getTime());
                    return fireCalendarBoxScrollNextEvent(evt);
                }
                else {
                    return null;
                }
            }
            else if (VIEW_WEEKLY.equals(getView())) {
                String weekStr = evt.getRequest().getParameter(getWeekOfMonthName());
                int week = Integer.parseInt(weekStr);
                Calendar cal = Calendar.getInstance();
                cal.setTime(getDate());
                if (cal.get(Calendar.WEEK_OF_MONTH) == week) {
                    cal.add(Calendar.WEEK_OF_MONTH, 1);
                    setDate(cal.getTime());
                    return fireCalendarBoxScrollNextEvent(evt);
                }
                else {
                    return null;
                }
            }
            else if (VIEW_DAILY.equals(getView())) {
                String dayStr = evt.getRequest().getParameter(getDayOfMonthName());
                int day = Integer.parseInt(dayStr);
                Calendar cal = Calendar.getInstance();
                cal.setTime(getDate());
                if (cal.get(Calendar.DAY_OF_MONTH) == day) {
                    cal.add(Calendar.DAY_OF_MONTH, 1);
                    setDate(cal.getTime());
                    return fireCalendarBoxScrollNextEvent(evt);
                }
                else {
                    return null;
                }
            }
            else {
                return null;
            }
        }
        catch(Exception e) {
            return null;
        }
    }

    public Forward onScrollPrevious(Event evt) {
        try {
            if (VIEW_MONTHLY.equals(getView())) {
                String monthStr = evt.getRequest().getParameter(getMonthName());
                int month = Integer.parseInt(monthStr);
                Calendar cal = Calendar.getInstance();
                cal.setTime(getDate());
                if (cal.get(Calendar.MONTH) == month) {
                    cal.add(Calendar.MONTH, -1);
                    setDate(cal.getTime());
                    return fireCalendarBoxScrollNextEvent(evt);
                }
                else {
                    return null;
                }
            }
            else if (VIEW_WEEKLY.equals(getView())) {
                String weekStr = evt.getRequest().getParameter(getWeekOfMonthName());
                int week = Integer.parseInt(weekStr);
                Calendar cal = Calendar.getInstance();
                cal.setTime(getDate());
                if (cal.get(Calendar.WEEK_OF_MONTH) == week) {
                    cal.add(Calendar.WEEK_OF_MONTH, -1);
                    setDate(cal.getTime());
                    return fireCalendarBoxScrollNextEvent(evt);
                }
                else {
                    return null;
                }
            }
            else if (VIEW_DAILY.equals(getView())) {
                String dayStr = evt.getRequest().getParameter(getDayOfMonthName());
                int day = Integer.parseInt(dayStr);
                Calendar cal = Calendar.getInstance();
                cal.setTime(getDate());
                if (cal.get(Calendar.DAY_OF_MONTH) == day) {
                    cal.add(Calendar.DAY_OF_MONTH, -1);
                    setDate(cal.getTime());
                    return fireCalendarBoxScrollNextEvent(evt);
                }
                else {
                    return null;
                }
            }
            else {
                return null;
            }
        }
        catch(Exception e) {
            return null;
        }
    }


    /**
     * Calculates and returns a multidimensional array of 6x7 CalendarEntry objects to
     * represent the days for each daily entry in a month.
     * The entries not in the current month will be null.
     * @return
     */
    public Object[][] getMonthlyEntries() {
        if(userId==null){
            userId = getWidgetManager().getUser().getId();
        }
        if(userIds==null||userIds.length<=0){
            if(userId.trim().length()>0) {
                userIds= new String[] {getUserId()};
            }
        }
        Object[][] monthlyEntries = null;
        try {
            CalendarHelperBean helper = new CalendarHelperBean(); // instantiate bean
            helper.setDate(getDate()); // set date
            try {
                Application application = Application.getInstance();
                CalendarModule handler = (CalendarModule)application.getModule(CalendarModule.class);
                Collection eventList = handler.getMonthlyCalendarEvents(getDate(), getUserId(), getUserIds(), null);
                helper.setEventList(eventList); // set list of events
                /*this.eventList = eventList;*/
            }
            catch(Exception ie) {
                Log.getLog(getClass()).error(ie.toString(), ie);
            }
            monthlyEntries = helper.getMonthlyCalendarEntries(); // construct monthly entries
        }
        catch(Exception e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }
        return monthlyEntries;
    }

    public Object[] getWeeklyEntriesWithoutTasks() {
        return getWeeklyEntries(true);
    }

    public Object[] getWeeklyEntries() {
        return getWeeklyEntries(false);
    }

    /**
     * Calculates and returns an array of 7 CalendarEntry objects
     * to represent the days for each daily entry in a week.
     * @return
     */
    public Object[] getWeeklyEntries(boolean excludeTasks) {
        if(userId==null){
            userId = getWidgetManager().getUser().getId();
        }
        if(userIds==null||userIds.length<=0){
            if(userId.trim().length()>0) {
                userIds= new String[] {getUserId()};
            }
        }
        Object[] weeklyEntries = null;
        try {
            CalendarHelperBean helper = new CalendarHelperBean(); // instantiate bean
            helper.setDate(getDate()); // set date
            try {
                Application application = Application.getInstance();
                CalendarModule handler = (CalendarModule)application.getModule(CalendarModule.class);
                Collection eventList = handler.getWeeklyCalendarEvents(getDate(), getUserId(), getUserIds(), null, excludeTasks);
                helper.setEventList(eventList); // set list of events
                /*this.eventList = eventList;*/
            }
            catch(Exception ie) {
                Log.getLog(getClass()).error(ie.toString(), ie);
            }
            weeklyEntries = helper.getWeeklyCalendarEntries(); // construct monthly entries
        }
        catch(Exception e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }
        return weeklyEntries;
    }

    public void onRequest(Event evt)
    {
        userId = getWidgetManager().getUser().getId();

        if(userIds==null) {
            userIds = new String[] {getUserId()};
            userSelectBox.setIds(userIds);
        }

        try {
            SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
            groupSelectBox.setOptions(ss.getGroupsByPermission(CalendarModule.PERMISSION_CALENDARING,Boolean.TRUE,"groupName",false,0,-1),"id","groupName");
        }
        catch(Exception e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }

        String eventType = evt.getRequest().getParameter(Event.PARAMETER_KEY_EVENT_TYPE);
        if(eventType==null&&getView().equals(VIEW_EVENT)){
            try{
                CalendarModule cm = (CalendarModule)Application.getInstance().getModule(CalendarModule.class);
                CalendarEvent event = cm.getCalendarEvent(getSelectedEventId(),getSelectedInstanceId());
                if((event.getStartDate()!=null&&!event.getStartDate().equals(selectedEvent.getStartDate()))||
                        (event.getRecurrenceRule()!=null&&!event.getRecurrenceRule().equals(selectedEvent.getRecurrenceRule())))
                    setView(VIEW_DAILY);
                else
                    selectedEvent = event;
            }catch(Exception e){
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
        }/*else if(eventType.equals(VIEW_EVENT))
            onEventSelect(evt);*/
        super.onRequest(evt);
    }

    /**
     * Calculates and  retrieves a CalendarEntry to represent a daily entry.
     * @return
     */
    public Object getDailyEntry() {
        if(userId==null){
            userId = getWidgetManager().getUser().getId();
        }
        if(userIds==null||userIds.length<=0){
            if(userId.trim().length()>0) {
                userIds= new String[] {getUserId()};
            }
        }
        Object dailyEntry = null;
        try {
            CalendarHelperBean helper = new CalendarHelperBean(); // instantiate bean
            helper.setDate(getDate()); // set date
            try {
                Application application = Application.getInstance();
                CalendarModule handler = (CalendarModule)application.getModule(CalendarModule.class);
                Collection eventList = handler.getDailyCalendarEvents(getDate(), getUserId(), getUserIds(), null);
                helper.setEventList(eventList); // set list of events
                /*this.eventList = eventList;*/
            }
            catch(Exception ie) {
                Log.getLog(getClass()).error(ie.toString(), ie);
            }
            dailyEntry = helper.getDailyCalendarEntries(); // construct monthly entries
        }
        catch(Exception e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }
        return dailyEntry;
    }

    public CalendarEvent getSelectedEvent()
    {
        return selectedEvent;
    }

    public void setSelectedEvent(CalendarEvent selectedEvent)
    {
        this.selectedEvent = selectedEvent;
    }

    /*public Collection getEventList()
    {
        return eventList;
    }

    public void setEventList(Collection eventList)
    {
        this.eventList = eventList;
    }*/

    public String getBackUrl()
    {
        return backUrl;
    }

    public void setBackUrl(String backUrl)
    {
        this.backUrl = backUrl;
    }

    public Collection getTaskViews(){
        /*if(taskViews!=null){
            for(Iterator i = taskViews.iterator();i.hasNext();){
                removeChild((TaskSummaryView)i.next());
            }
        }*/
        Object dailyEntry = getDailyEntry();
        Collection tempTasks = ((CalendarHelperBean.CalendarEntry)dailyEntry).getTasks();
        Collection tasks = sortTaskBaseonCategory(tempTasks);  
        Collection taskViews = new ArrayList(tasks.size());
        int j=0;
        for(Iterator i = tasks.iterator();i.hasNext();){
            Task task = (Task)i.next();
            String archived=(String)task.getProperty("projectArchived");
			if(archived==null||"0".equals(archived)){
                TaskSummaryView view = new TaskSummaryView("taskviw"+j);
                view.setTask(task);
                view.init();
                view.setView(this);
                taskViews.add(view);
                addChild(view);
                j++;
			}
        }
        return taskViews;
    }
    
    public Collection sortTaskBaseonCategory(Collection tasks){
    	Vector vec = new Vector();
    	int counter = 0;
    	for(Iterator i=tasks.iterator(); i.hasNext();){
    		Task task = (Task)i.next();          
    		vec.add(counter, task);
    		counter++;
    	}
    	
    	String selectedCat = new String();
    	int selectedCatIndex = 0;
    	int pointerOne = 0;
    	int pointerTwo = 0;
    	
    	while(selectedCatIndex < vec.size()){
    		Task task = (Task)vec.elementAt(selectedCatIndex);
    		selectedCat = task.getCategory();
    	
    		pointerOne = selectedCatIndex + 1;
    		
    		while(pointerOne < vec.size()){
    			
    			Task pointerOneTask = (Task)vec.elementAt(pointerOne);
    			    
    			if(pointerOneTask.getCategory().equals(selectedCat)){
    				
    				selectedCatIndex = pointerOne;
        			pointerOne++;
        			 
        		}else{
        			
        			pointerTwo = pointerOne + 1;
        			
        			while(pointerTwo < vec.size()){
        				
        				Task pointerTwoTask = (Task)vec.elementAt(pointerTwo);
        				        
        				if(pointerTwoTask.getCategory().equals(selectedCat)){
        			        
        					
        					vec.setElementAt(pointerTwoTask, pointerOne);
        				
        					vec.setElementAt(pointerOneTask, pointerTwo);
        					
        					
        					
        					selectedCatIndex = pointerOne;
        					pointerOne++;
        					
        				}else{
        					pointerTwo++;
        				}
        			}
        			break;
        		}
    			
    		}
    		
    		selectedCatIndex++;
    	}
    	
    	Collection returnCol = new ArrayList();
    	
    	for(int i=0; i<vec.size(); i++){
    		Task task = (Task)vec.elementAt(i);
    		
    		if(!returnCol.contains(task))
    		{
    			returnCol.add(task);
    		 
    		}
    	}
    	
    	return returnCol;
    }

    public Date getPreviousDate(){
        Date date = getDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int field=0;
        String view = getView();
        if(VIEW_DAILY.equals(view))
            field = Calendar.DAY_OF_MONTH;
        else if(VIEW_WEEKLY.equals(view))
            field = Calendar.WEEK_OF_MONTH;
        else if(VIEW_MONTHLY.equals(view))
            field = Calendar.MONTH;
        cal.add(field,-1);
        return cal.getTime();
    }

    public Date getNextDate(){
        Date date = getDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int field=0;
        String view = getView();
        if(VIEW_DAILY.equals(view))
            field = Calendar.DAY_OF_MONTH;
        else if(VIEW_WEEKLY.equals(view))
            field = Calendar.WEEK_OF_MONTH;
        else if(VIEW_MONTHLY.equals(view))
            field = Calendar.MONTH;
        cal.add(field,1);
        return cal.getTime();
    }

    public Form getJumpForm()
    {
        return jumpForm;
    }

    public void setJumpForm(Form jumpForm)
    {
        this.jumpForm = jumpForm;
    }

    public Button getJumpButton()
    {
        return jumpButton;
    }

    public void setJumpButton(Button jumpButton)
    {
        this.jumpButton = jumpButton;
    }

    public DateField getJumpDate()
    {
        return jumpDate;
    }

    public void setJumpDate(DateField jumpDate)
    {
        this.jumpDate = jumpDate;
    }

    public SelectBox getGroupSelectBox()
    {
        return groupSelectBox;
    }

    public void setGroupSelectBox(SelectBox groupSelectBox)
    {
        this.groupSelectBox = groupSelectBox;
    }

    public Button getGroupSelectButton()
    {
        return groupSelectButton;
    }

    public void setGroupSelectButton(Button groupSelectButton)
    {
        this.groupSelectButton = groupSelectButton;
    }

    public Form getGroupSelectForm()
    {
        return groupSelectForm;
    }

    public void setGroupSelectForm(Form groupSelectForm)
    {
        this.groupSelectForm = groupSelectForm;
    }
}
