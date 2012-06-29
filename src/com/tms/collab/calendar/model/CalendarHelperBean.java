package com.tms.collab.calendar.model;

import kacang.util.Log;
import kacang.Application;

import java.text.SimpleDateFormat;
import java.util.*;
import java.io.Serializable;

import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.emeeting.Meeting;
import com.tms.collab.emeeting.MeetingHandler;
import com.tms.collab.emeeting.MeetingException;

/**
 * A helper JavaBean class to aid the rendering of calendar
 * events in a JSP page.
 * Example usage to retrieve monthly calendar entries:
 * <pre>
 * CalendarHelperBean helper = new CalendarHelperBean(); // instantiate bean
 * helper.setDate(date); // set date
 * helper.setEventList(eventList); // set list of events
 * helper.getMonthlyCalendarEntries(); // construct monthly entries
 * </pre>
 */
public class CalendarHelperBean implements Serializable {

    private Date date = new Date();
    private Collection eventList = new TreeSet();
    private Object[][] monthlyEntries = new Object[6][7];
    private Object[] weeklyEntries = new Object[7];
    private CalendarEntry dailyEntry;
    Log log = Log.getLog(getClass());

    public CalendarHelperBean() {
/*
        getMonthlyCalendarEntries();
*/
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Collection getEventList() {
        return eventList;
    }

    public void setEventList(Collection eventList) {
        this.eventList = eventList;
    }

    /**
     * Returns a multidimensional array of 6x7 CalendarEntry objects to
     * represent the days for each daily entry in a month.
     * The entries not in the current month will be null.
     * @return
     */
    public Object[][] getMonthlyEntries() {
        return monthlyEntries;
    }

    public void setMonthlyEntries(Object[][] monthlyEntries) {
        this.monthlyEntries = monthlyEntries;
    }

    /**
     * Returns an array of 7 CalendarEntry objects to represent the
     * days for each daily entry in a week.
     * @return
     */
    public Object[] getWeeklyEntries() {
        return weeklyEntries;
    }

    public void setWeeklyEntries(Object[] weeklyEntries) {
        this.weeklyEntries = weeklyEntries;
    }

    /**
     * Retrieves a CalendarEntry to represent a daily entry.
     * @return
     */
    public CalendarEntry getDailyEntry() {
        return dailyEntry;
    }

    public void setDailyEntry(CalendarEntry dailyEntry) {
        this.dailyEntry = dailyEntry;
    }

    public String[] getDaysInWeek() {
        Locale userLocale = Application.getInstance().getLocale();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE", userLocale);
        Calendar cal = Calendar.getInstance(userLocale);
        cal.set(Calendar.DAY_OF_WEEK, 1);
        while (cal.get(Calendar.DAY_OF_WEEK) != cal.getFirstDayOfWeek()) {
            cal.roll(Calendar.DAY_OF_WEEK, false);
        }
        String[] days = new String[7];
        for (int i=0; i<7; i++) {
            days[i] = sdf.format(cal.getTime());
            cal.add(Calendar.DAY_OF_WEEK, 1);
        }
        return days;
    }

    public int getDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDate());
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public int getMonth() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDate());
        return cal.get(Calendar.MONTH);
    }

    public int getYear() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDate());
        return cal.get(Calendar.YEAR);
    }

    /**
     * Calculates and returns a multidimensional array of 6x7 CalendarEntry objects to
     * represent the days for each daily entry in a month.
     * The entries not in the current month will be null.
     * @return
     */
    public Object[][] getMonthlyCalendarEntries() {
        Date date = getDate();
        Collection eventList = getEventList();

        Locale userLocale = Application.getInstance().getLocale();  //added on 22nd march 2006
        // get first day of week, current month and year
        Calendar cal = Calendar.getInstance(userLocale);            //modified on 22nd march 2006
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        // construct array
        int padding = 0;
        while(cal.get(Calendar.DAY_OF_WEEK) != cal.getFirstDayOfWeek()) {
            cal.roll(Calendar.DAY_OF_WEEK, false);
            padding++;
        }
        Calendar baseCal = Calendar.getInstance();
        baseCal.setTime(cal.getTime());
        for (int w=0; w<6; w++) {
            for (int d=0; d<7; d++) {
                if (cal.get(Calendar.MONTH) == month) {
                    CalendarEntry entry = new CalendarEntry();
                    Calendar tmpCal = Calendar.getInstance();
                    tmpCal.setTime(cal.getTime());
                    entry.setCalendar(tmpCal);
                    monthlyEntries[w][d] = entry;
                }
                else {
                    monthlyEntries[w][d] = null;
                }
                cal.add(Calendar.DATE, 1);
            }
        }

        // populate array with CalendarEntry objects
        for (Iterator i=eventList.iterator(); i.hasNext();) {
            com.tms.collab.calendar.model.CalendarEvent event = (com.tms.collab.calendar.model.CalendarEvent)i.next();

            Calendar startCal = Calendar.getInstance();
            if (!event.isReminder()){
                if(event.getEventId().startsWith(Task.class.getName()))
                    startCal.setTime(event.getEndDate());
                else
                    startCal.setTime(event.getStartDate());
            }
            else
                startCal.setTime(event.getReminderDate());
            startCal.set(Calendar.HOUR_OF_DAY, 0);
            startCal.set(Calendar.MINUTE, 0);
            startCal.set(Calendar.SECOND, 0);

            Calendar endCal = Calendar.getInstance();
            if (!event.isReminder())
                endCal.setTime(event.getEndDate());
            else
                endCal.setTime(event.getReminderDate());
            endCal.set(Calendar.HOUR_OF_DAY, 23);
            endCal.set(Calendar.MINUTE, 59);
            endCal.set(Calendar.SECOND, 59);

            if (startCal.before(baseCal))
                startCal.setTime(baseCal.getTime());
            while(startCal.before(endCal) && startCal.before(cal)) {
                if (startCal.get(Calendar.MONTH) == month && startCal.get(Calendar.YEAR) ==year) {
                    int relDay = padding + startCal.get(Calendar.DAY_OF_MONTH) - 1;
                    int day = relDay % 7;
                    int week = relDay / 7;
                    CalendarEntry entry = (CalendarEntry)monthlyEntries[week][day];
                    if (entry != null) {
                        entry.addCalendarEvent(event);
                        monthlyEntries[week][day] = entry;

                    }
                }
                startCal.add(Calendar.DAY_OF_MONTH, 1);
            }

        }
        for(int i=0;i<6;i++){
            for(int j=0;j<7;j++){
                if(monthlyEntries[i][j]!=null)
                    ((CalendarEntry )monthlyEntries[i][j]).categorize();
            }
        }
        if(monthlyEntries[4][0]==null)
            monthlyEntries[4] = null;
        if(monthlyEntries[5][0]==null)
            monthlyEntries[5] = null;
        return monthlyEntries;
    }

    /**
     * Calculates and returns an array of 7 CalendarEntry objects to represent the
     * days for each daily entry in a week.
     * @return
     */
    public Object[] getWeeklyCalendarEntries() {
        Date date = getDate();
        Collection eventList = getEventList();

        Locale userLocale = Application.getInstance().getLocale();  //added on 20th march 2006
        // get first day of week
        Calendar cal = Calendar.getInstance(userLocale);            //modify on 20th march 2006
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        while(cal.get(Calendar.DAY_OF_WEEK) != cal.getFirstDayOfWeek()) {
            cal.add(Calendar.DAY_OF_WEEK, -1);
        }
        Calendar baseCal = Calendar.getInstance();
        baseCal.setTime(cal.getTime());

        // construct array
        for (int d=0; d<7; d++) {
            CalendarEntry entry = new CalendarEntry();
            Calendar tmpCal = Calendar.getInstance();
            tmpCal.setTime(cal.getTime());
            entry.setCalendar(tmpCal);
            weeklyEntries[d] = entry;
            cal.add(Calendar.DATE, 1);
        }
//        cal.add(Calendar.DATE, -1);
        // populate array with CalendarEntry objects
        for (Iterator i=eventList.iterator(); i.hasNext();) {
            com.tms.collab.calendar.model.CalendarEvent event = (com.tms.collab.calendar.model.CalendarEvent)i.next();
            Calendar startCal = Calendar.getInstance();
            if (!event.isReminder()){
                if(event.getEventId().startsWith(Task.class.getName()))
                    startCal.setTime(event.getEndDate());
                else
                    startCal.setTime(event.getStartDate());
            }
            else
                startCal.setTime(event.getReminderDate());
            startCal.set(Calendar.HOUR_OF_DAY, 0);
            startCal.set(Calendar.MINUTE, 0);
            startCal.set(Calendar.SECOND, 0);
            Calendar endCal = Calendar.getInstance();
            if (!event.isReminder())
                endCal.setTime(event.getEndDate());
            else
                endCal.setTime(event.getReminderDate());
            endCal.set(Calendar.HOUR_OF_DAY, 23);
            endCal.set(Calendar.MINUTE, 59);
            endCal.set(Calendar.SECOND, 59);
            Calendar tmpCal = Calendar.getInstance();
            if (startCal.before(baseCal))
                startCal.setTime(baseCal.getTime());
            while(startCal.before(endCal) && (startCal.before(cal))) {
                int day = 0;
                tmpCal.setTime(baseCal.getTime());
                while(tmpCal.before(startCal)) {
                    tmpCal.add(Calendar.DATE, 1);
                    day++;
                }
                if(day<7){
                    //System.out.println(event.getTitle()+"  "+event.getStartDate());
                CalendarEntry entry = (CalendarEntry)weeklyEntries[day];
                if (entry != null) {
                    entry.addCalendarEvent(event);
                    weeklyEntries[day] = entry;
                }
                }
                startCal.add(Calendar.DAY_OF_MONTH, 1);

            }
        }
        for(int i=0;i<7;i++){
            if(weeklyEntries[i]!=null){
                ((CalendarEntry)weeklyEntries[i]).categorize();
            }
        }
        return weeklyEntries;
    }

    /**
     * Calculates and retrieves a CalendarEntry to represent a daily entry.
     * @return
     */
    public CalendarEntry getDailyCalendarEntries() {
        Date date = getDate();
        Collection eventList = getEventList();

        // create calendar entry
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        dailyEntry = new CalendarEntry();
        dailyEntry.setCalendar(cal);
        dailyEntry.setEventList(eventList);
        dailyEntry.categorize();
        return dailyEntry;
    }

    /**
     * Represents a daily entry in a calendar.
     */
    public class CalendarEntry implements Serializable {

        private Calendar calendar;
        private Collection eventList;
        private Collection appointments;
        private Collection tasks;
        private Collection events;
        private Collection meetings;

        public CalendarEntry() {
            eventList = new TreeSet();
        }

        public Calendar getCalendar() {
            return calendar;
        }

        public void setCalendar(Calendar calendar) {
            this.calendar = calendar;
        }

        /**
         * @return a Collection of CalendarEvent objects for the entry.
         */
        public Collection getEventList() {
            return eventList;
        }

        public void setEventList(Collection eventList) {
            this.eventList = eventList;
        }

        public void addCalendarEvent(com.tms.collab.calendar.model.CalendarEvent event) {
            eventList.add(event);
        }

        public String getDayOfWeekLabel() {
            Locale userLocale = Application.getInstance().getLocale();
            SimpleDateFormat sdf = new SimpleDateFormat("EEE", userLocale);
            return sdf.format(calendar.getTime());
        }

        public int getDayOfMonth() {
            return calendar.get(Calendar.DAY_OF_MONTH);
        }

        public boolean isToday() {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            return (cal.get(Calendar.YEAR)==calendar.get(Calendar.YEAR) &&
                    cal.get(Calendar.MONTH)==calendar.get(Calendar.MONTH)&&
                    cal.get(Calendar.DAY_OF_MONTH)==calendar.get(Calendar.DAY_OF_MONTH));
        }

        public int getMonth() {
            return calendar.get(Calendar.MONTH);
        }

        public int getYear() {
            return calendar.get(Calendar.YEAR);
        }

        public Collection getAppointments()
        {
            return appointments;
        }

        public void setAppointments(Collection appointments)
        {
            this.appointments = appointments;
        }

        public Collection getTasks()
        {
            return tasks;
        }

        public void setTasks(Collection tasks)
        {
            this.tasks = tasks;
        }

        public Collection getEvents()
        {
            return events;
        }

        public void setEvents(Collection events)
        {
            this.events = events;
        }

        public Collection getMeetings()
        {
            return meetings;
        }

        public void setMeetings(Collection meetings)
        {
            this.meetings = meetings;
        }

        public boolean isContainingEvents(){
            return (eventList.size()>0);
        }

        public void categorize(){
            if(eventList!=null){
                tasks = new ArrayList(10);
                appointments = new ArrayList(10);
                events = new ArrayList(10);
                meetings = new ArrayList(10);
                MeetingHandler mh=null;
                for(Iterator i=eventList.iterator();i.hasNext();){
                    CalendarEvent event = (CalendarEvent)i.next();
                    String eventId = event.getEventId();
                    String eventType = eventId.substring(0,eventId.indexOf('_'));
                    if(eventType.equals(CalendarEvent.class.getName())){
                        events.add(event);
                    }else if(eventType.equals(Appointment.class.getName())){
                        appointments.add(event);
                    }else if(eventType.equals(Task.class.getName())){
/*
                        boolean contained = false;
                        for (Iterator iterator = tasks.iterator(); iterator.hasNext();)
                        {
                            Task task = (Task) iterator.next();
                            if(task.getId().equals(event.getEventId())){
                                contained = true;
                                break;
                            }
                        }
                        if(!contained)
                            tasks.add(event);
*/
                        if(event instanceof Task)
                            tasks.add(event);
                    }else if(eventType.equals(Meeting.class.getName())){
                        if(mh== null)
                            mh=(MeetingHandler)Application.getInstance().getModule(MeetingHandler.class);
                        try
                        {
                            Meeting meeting = mh.getMeeting(eventId,true);
                            meeting.setEvent(event);
                            meetings.add(meeting);
                        } catch (MeetingException e)
                        {
                            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                        }
                    }
                }

            }
        }

    }

}
