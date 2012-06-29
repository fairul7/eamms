package com.tms.collab.calendar.model;

import kacang.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.tms.util.FormatUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Class containing utility methods for general calendar processing functions.
 */
public class CalendarUtil {

    private static SimpleDateFormat sdfLongDateTime; // to cache long date time format
    private static SimpleDateFormat sdfLongDate; // to cache long date format
    private static SimpleDateFormat sdfLongTime; // to cache long time format


    /**
     * Coerce a string formatted PDI date to a <code>Calendar</code> object.
     * @param  inValue	The PDI formatted time e.g. 20020925T1524Z
     * @exception  java.lang.IllegalArgumentException	Thrown if inValue is shorter than expected
     */
    public static Calendar parseCalendar(String inValue)
            throws IllegalArgumentException {
        /*
         * Determine the time zone to be associated with the Date. If nothing has been passed in
         * then the Date will be deemed to be floating.
         */
        TimeZone theDateTimeTZ = TimeZone.getDefault();

        try {
            /*
             * Extract the components of the date/time string
             */
            String theDateTimeYear = inValue.substring(0, 4);
            String theDateTimeMonth = inValue.substring(4, 6);
            String theDateTimeDay = inValue.substring(6, 8);

            /*
             * Create a calendar object using the extracted components. If we have a time then
             * we additionally initialise the calendar with a UTC offset of zero, or make it floating
             * if there is no 'Z' suffix. If there is no time then we again make the calendar
             * floating so that the date applies to any time zone.
             */
            GregorianCalendar theDateTimeCal = new GregorianCalendar(
                    Integer.valueOf(theDateTimeYear).intValue(),
                    Integer.valueOf(theDateTimeMonth).intValue() - 1,
                    Integer.valueOf(theDateTimeDay).intValue()
            );
            if (inValue.length() > 8) {
                if (inValue.charAt(8) == 'T') {
                    String theDateTimeHour = inValue.substring(9, 11);
                    String theDateTimeMinute = inValue.substring(11, 13);
                    String theDateTimeSeconds = inValue.substring(13, 15);

                    theDateTimeCal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(theDateTimeHour).intValue());
                    theDateTimeCal.set(Calendar.MINUTE, Integer.valueOf(theDateTimeMinute).intValue());
                    theDateTimeCal.set(Calendar.SECOND, Integer.valueOf(theDateTimeSeconds).intValue());

                    if (inValue.length() > 15) {
                        if (inValue.charAt(15) == 'Z')
                            theDateTimeCal.set(Calendar.ZONE_OFFSET, 0);
                        else
                            theDateTimeCal.setTimeZone(theDateTimeTZ);
                    } else
                        theDateTimeCal.setTimeZone(theDateTimeTZ);
                } else
                    theDateTimeCal.setTimeZone(theDateTimeTZ);
            } else
                theDateTimeCal.setTimeZone(theDateTimeTZ);

            /*
             * Cause GregorianCalendar to validate by trying to determine the date
             */
            theDateTimeCal.setLenient(false);
            // Check for valid fields

            /*
             * All is well
             */
            return theDateTimeCal;

        }
        catch (Exception e) {
//            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    public static String getPdiFormat(Date date) {
        if (sdfLongDate == null) {
            sdfLongDate = new SimpleDateFormat("yyyyMMdd");
        }
        if (sdfLongTime == null) {
            sdfLongTime = new SimpleDateFormat("HHmmss");
        }
        String pdi = sdfLongDate.format(date)
            + "T" + sdfLongTime.format(date);
        return pdi;
    }

    public static String getAttendeeId(com.tms.collab.calendar.model.CalendarEvent event, String userId) {
        return event.getEventId() + "_" + event.getInstanceId() + "_" + userId;
    }

    /**
     * Coerce a String into a collection of String given a
     * delimeter.
     *
     * @param  inValue	The string to be coerced
     * @param  inDelim	The delimeter to use
     */
    public static List parseList(String inValue, char inDelim) {
        List theList = new Vector();

        int theDelimPosn;
        int theLastDelimPosn = -1;

        while ((theDelimPosn = inValue.indexOf(inDelim, theLastDelimPosn + 1)) != -1) {
            theList.add(inValue.substring(theLastDelimPosn + 1, theDelimPosn));
            theLastDelimPosn = theDelimPosn;
        }

        theList.add(inValue.substring(theLastDelimPosn + 1));

        return theList;
    }

    /**
     * Retrieves a Collection of recurring CalendarEvent for a specified
     * event in a specified range.
     * @param event The original event for which to retrieve the recurrences.
     * @param from The start date of the range to retrieve.
     * @param to The end date of the range to retrieve.
     * @return
     */
    public static Collection getRecurringEvents(com.tms.collab.calendar.model.CalendarEvent event, Date from, Date to) {

        Collection eventList = new TreeSet();

        if (event.getRecurrenceDates() != null && event.getRecurrenceDates().trim().length() > 0) {
            // process direct recurrence dates
            List recurrenceDateList = CalendarUtil.parseList(event.getRecurrenceDates(), ';');
            for (Iterator i=recurrenceDateList.iterator(); i.hasNext();) {
                String dateStr = (String)i.next();
                Date start = CalendarUtil.parseCalendar(dateStr).getTime();
                long duration = event.getEndDate().getTime() - event.getStartDate().getTime();
                Calendar cal = Calendar.getInstance();
                cal.setTime(start);
                cal.add(Calendar.MILLISECOND, (int)duration);
                Date end = cal.getTime();
                if (start.equals(from) ||
                        start.equals(end) ||
                        end.equals(from) ||
                        end.equals(end) ||
                        (start.before(from) && end.after(from)) ||
                        (start.after(from) && start.before(to))) {

                    addRecurringEvent(eventList, event, start);
                }
            }
        }

        else if (event.getRecurrenceRule() != null && event.getRecurrenceRule().trim().length() > 0) {
            try {

                RecurrenceRule rule = CalendarUtil.parseRecurrenceRule(event.getRecurrenceRule());
                int frequency = rule.getFrequency();
                switch(frequency) {
                    case RecurrenceRule.FREQUENCY_DAILY:
                        processRecurrence(rule, event, to, from, eventList, Calendar.DATE);
                        break;
                    case RecurrenceRule.FREQUENCY_WEEKLY:
                        rule.setInterval(rule.getInterval() * 7);
                        processRecurrence(rule, event, to, from, eventList, Calendar.DATE);
                        break;
                    case RecurrenceRule.FREQUENCY_MONTHLY_BY_DAY:
                        processRecurrence(rule, event, to, from, eventList, Calendar.MONTH);
                        break;
                    case RecurrenceRule.FREQUENCY_MONTHLY_BY_POSITION:
                        processRecurrence(rule, event, to, from, eventList, Calendar.MONTH);
                        break;
                    case RecurrenceRule.FREQUENCY_YEARLY_BY_DAY:
                        processRecurrence(rule, event, to, from, eventList, Calendar.YEAR);
                        break;
                    case RecurrenceRule.FREQUENCY_YEARLY_BY_MONTH:
                        processRecurrence(rule, event, to, from, eventList, Calendar.YEAR);
                        break;
                    default:
                        break;
                }
            }
            catch(ParseException pe) {
            }
        }

        return eventList;
    }



    /**
     * Processes a recurrence rule, adding recurrences for a specified event
     * within a specified date range into a Collection.
     * @param rule The recurrence rule to process.
     * @param event The original event.
     * @param to The start of the date range.
     * @param from The end of the date range.
     * @param eventList The Collection to add events to.
     * @param calendarField The type of Calendar field to
     * process, e.g. Calendar.DATE, Calendar.MONTH, Calendar.YEAR
     */
    protected static void processRecurrence(RecurrenceRule rule, com.tms.collab.calendar.model.CalendarEvent event, Date to, Date from, Collection eventList, int calendarField) {
    //    int interval = rule.getInterval();
        int duration = rule.getDuration();
        RecurrenceRuleModifier modifier =  null;
        if(rule.getModifier()!=null && rule.getModifier().trim().length()>0){
            modifier = parseModifier(rule.getModifier());
        }
        if (duration >= 0) {
            // handle specific occurences
            Calendar cal = Calendar.getInstance();
            cal.setTime(event.getStartDate());
            Date d = cal.getTime();
            int count = 0;
            while(duration == 0 || count < duration  ) {
                //cal.add(calendarField, interval);
                setNextRecurrenceDate(cal,rule,calendarField,modifier);
                d = cal.getTime();
             //   if ((d.after(from) || d.equals(from)) && (d.before(to) || d.equals(to))) {
                    addRecurringEvent(eventList, event, d);
                count++;
                //}
            }
        }
        else {
            // handle up to an end date
            Date endDate = rule.getEndDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(event.getStartDate());
            Date d = cal.getTime();
            while((d.before(endDate) || d.equals(endDate)) && (d.before(to) || d.equals(to))) {
                //cal.add(calendarField, interval);
                setNextRecurrenceDate(cal,rule,calendarField,modifier);
                d = cal.getTime();
                if ((d.after(from) || d.equals(from)) && (d.before(to) || d.equals(to)) && (d.before(endDate) || d.equals(endDate))) {
                    addRecurringEvent(eventList, event, d);
                }
            }
        }
    }


    private static void setNextRecurrenceDate(Calendar cal,RecurrenceRule rule,
                                       int calendarField,RecurrenceRuleModifier modifier){

        int interval = rule.getInterval();
        if(modifier==null){
            cal.add(calendarField, interval);
        } else{
            if(modifier.hasPosition()){
                String pos = (String)modifier.getPosition().get(0);
                cal.add(calendarField,interval);
                cal.set(Calendar.DAY_OF_WEEK,getWeekDay((String)modifier.getWeekDay().get(0)));
                if(pos.endsWith("+")){ //the first x-th
                    cal.set(Calendar.DAY_OF_WEEK_IN_MONTH,Integer.parseInt(pos.substring(0,pos.indexOf('+'))));
                } else// the last x-th
                {
                    cal.set(Calendar.DAY_OF_WEEK_IN_MONTH,-(Integer.parseInt(pos.substring(0,pos.indexOf('-')))));
                    cal.get(Calendar.DAY_OF_WEEK_IN_MONTH);
                }
            } else{
                // make sure the daily interval is ok
                List weekdays = modifier.getWeekDay();
                if (interval >= 7) {
                    interval = 6;
                }
                // generate collection of week days
                Collection dayList = new ArrayList();
                for(int i=0;i<weekdays.size();i++) {
                    dayList.add(new Integer(getWeekDay((String)weekdays.get(i))));
                }
                // add interval until matching day found
                while (true) {
                    cal.add(Calendar.DAY_OF_MONTH, interval);
                    if (dayList.contains(new Integer(cal.get(Calendar.DAY_OF_WEEK)))) {
                        break;
                    }
                }
            }
        }

    }

    private static int getWeekDay(String wd){
        if("SU".equals(wd))
            return Calendar.SUNDAY;
        else if("MO".equals(wd))
            return Calendar.MONDAY;
        else if("TU".equals(wd))
            return Calendar.TUESDAY;
        else if("WE".equals(wd))
            return Calendar.WEDNESDAY;
        else if("TH".equals(wd))
            return Calendar.THURSDAY;
        else if("FR".equals(wd))
            return Calendar.FRIDAY;
        else if("SA".equals(wd))
            return Calendar.SATURDAY;
        return -1;
    }

    public static RecurrenceRuleModifier parseModifier(String m){
        String sModifier = m;
        StringTokenizer st = new StringTokenizer(sModifier," ");
        String token;
        RecurrenceRuleModifier modifier = new RecurrenceRuleModifier();
        while(st.hasMoreTokens()){
            token = st.nextToken();
            if(token.endsWith("+")||token.endsWith("-")){
                modifier.addPosition(token);
            } else{
                modifier.addWeekDay(token);
            }
        }
        return modifier;
    }

    /**
     * Generates an instance ID based on a calendar event.
     * @param event
     * @return The generated eventID,
     * returns CalendarModule.DEFAULT_INSTANCE_ID for original events.
     */
    public static String getRecurrenceInstanceId(com.tms.collab.calendar.model.CalendarEvent event) {
        if (sdfLongDateTime == null) {
            sdfLongDateTime = new SimpleDateFormat(FormatUtil.getInstance().getLongDateTimeFormat());
        }
        if (event.isRecurrence())
            return sdfLongDateTime.format(event.getStartDate());
        else
            return com.tms.collab.calendar.model.CalendarModule.DEFAULT_INSTANCE_ID;
    }

    /**
     * Gets the appropriate date from an instance ID.
     * @param recurrenceId The instance ID.
     * @return The generated Date object.
     * @throws java.text.ParseException if a date could not be obtained.
     */
    public static Date getRecurrenceDate(String recurrenceId) throws ParseException {
        if (sdfLongDateTime == null) {
            sdfLongDateTime = new SimpleDateFormat(FormatUtil.getInstance().getLongDateTimeFormat());
        }
        return sdfLongDateTime.parse(recurrenceId);
    }

    /**
     * Adds a recurring event into a Collection.
     * @param eventList The Collection to add to.
     * @param event The recurring event instance.
     * @param newStartDate The start date for the event instance.
     */
    protected static void addRecurringEvent(Collection eventList, com.tms.collab.calendar.model.CalendarEvent event, Date newStartDate) {
        try {
            com.tms.collab.calendar.model.CalendarEvent newEvent = (com.tms.collab.calendar.model.CalendarEvent)event.clone();
            newEvent.setStartDate(newStartDate);
            long duration = event.getEndDate().getTime() - event.getStartDate().getTime();
            Calendar cal = Calendar.getInstance();
            cal.setTime(newStartDate);
            cal.add(Calendar.MILLISECOND, (int)duration);
            newEvent.setEndDate(cal.getTime());
            newEvent.setRecurrence(true);
            Collection atts = event.getAttendees();
            Collection attendees = new ArrayList();
            for(Iterator i=atts.iterator();i.hasNext();){
                Attendee att = new Attendee();
                Attendee temp = (Attendee)i.next();
                att.setStatus(temp.getStatus());
                att.setAttendeeId(CalendarUtil.getAttendeeId(event, temp.getUserId()));
                att.setInstanceId(newEvent.getInstanceId());
                att.setEventId(newEvent.getEventId());
                attendees.add(att);
            }
            newEvent.setAttendees(attendees);
            eventList.add(newEvent);
        }
        catch(Exception e) {
            Log.getLog(CalendarUtil.class).error("addRecurringEvent: " + e.toString(), e);
        }
    }

    /**
     * Retrieves the last recurrence date for a calendar event.
     * @param event
     * @return null if the event does not recur.
     */
    public static Date getLastRecurrenceDate(com.tms.collab.calendar.model.CalendarEvent event) {

        Date endDate = null;

        if (event.getRecurrenceDates() != null && event.getRecurrenceDates().trim().length() > 0) {
            // process direct recurrence dates
            List recurrenceDateList = CalendarUtil.parseList(event.getRecurrenceDates(), ';');
            for (Iterator i=recurrenceDateList.iterator(); i.hasNext();) {
                try {
                    String dateStr = (String)i.next();
                    Date d = CalendarUtil.parseCalendar(dateStr).getTime();
                    if (endDate == null || endDate.before(d)) {
                        endDate = d;
                    }
                }
                catch(Exception e) {
//                    e.printStackTrace();
                }
            }
        }else if (event.getRecurrenceRule() != null && event.getRecurrenceRule().trim().length() > 0) {
            try {
                RecurrenceRule rule = CalendarUtil.parseRecurrenceRule(event.getRecurrenceRule());
                if (rule.getEndDate() != null) {
                    return rule.getEndDate();
                }
                else if (rule.getDuration() == 0) {
                    // repeat forever
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR,  3000);
                    return cal.getTime();
                }
                else {
                    // calculate based on the frequency
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(event.getStartDate());
                    int frequency = rule.getFrequency();
                    switch(frequency) {
                        case RecurrenceRule.FREQUENCY_DAILY:
                            cal.add(Calendar.DATE, rule.getDuration() * rule.getInterval());
                            break;
                        case RecurrenceRule.FREQUENCY_WEEKLY:
                            cal.add(Calendar.DATE, rule.getDuration() * rule.getInterval() * 7);
                            break;
                        case RecurrenceRule.FREQUENCY_MONTHLY_BY_DAY:
                            cal.add(Calendar.MONTH, rule.getDuration() * rule.getInterval());
                            break;
                        case RecurrenceRule.FREQUENCY_MONTHLY_BY_POSITION:
                            cal.add(Calendar.MONTH, rule.getDuration() * rule.getInterval());
                            break;
                        case RecurrenceRule.FREQUENCY_YEARLY_BY_DAY:
                            cal.add(Calendar.YEAR, rule.getDuration() * rule.getInterval());
                            break;
                        case RecurrenceRule.FREQUENCY_YEARLY_BY_MONTH:
                            cal.add(Calendar.YEAR, rule.getDuration() * rule.getInterval());
                            break;
                        default:
                            break;
                    }
                    return cal.getTime();
                }
            }
            catch(ParseException e) {
                ;
            }
        }

        return endDate;

    }

    /**
     * Parses a recurrence rule based on the basic recurrence rule grammar.
     * @param recurrenceRule
     * @return A RecurrenceRule representing the recurrence rule.
     * @throws java.text.ParseException if the parsing failed.
     */
    public static RecurrenceRule parseRecurrenceRule(String recurrenceRule) throws ParseException {

        RecurrenceRule rule = new RecurrenceRule();
        StringTokenizer st = new StringTokenizer(recurrenceRule, " ");
        String token = st.nextToken();
        Date endDate = null;
        int interval;
        int duration;

        try {
            // init rule object
            rule.setDuration(-1);

            // get frequency
            if (token.startsWith("D")) {
                token = token.substring(1);
                rule.setFrequency(RecurrenceRule.FREQUENCY_DAILY);
            }
            else if (token.startsWith("W")) {
                token = token.substring(1);
                rule.setFrequency(RecurrenceRule.FREQUENCY_WEEKLY);
            }
            else if (token.startsWith("MP")) {
                token = token.substring(2);
                rule.setFrequency(RecurrenceRule.FREQUENCY_MONTHLY_BY_POSITION);
            }
            else if (token.startsWith("MD")) {
                token = token.substring(2);
                rule.setFrequency(RecurrenceRule.FREQUENCY_MONTHLY_BY_DAY);
            }
            else if (token.startsWith("YM")) {
                token = token.substring(2);
                rule.setFrequency(RecurrenceRule.FREQUENCY_YEARLY_BY_MONTH);
            }
            else if (token.startsWith("YD")) {
                token = token.substring(2);
                rule.setFrequency(RecurrenceRule.FREQUENCY_YEARLY_BY_DAY);
            }
            else {
                throw new ParseException("", 0);
            }

            // get interval
            if(token.trim().length()>0){
                interval = Integer.parseInt(token);
                rule.setInterval(interval);
            }
            StringBuffer modifier = new StringBuffer(0);
            while(st.hasMoreTokens()) {
                token = st.nextToken();

                // try to parse end date
                try {
                    endDate = parseCalendar(token).getTime();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(endDate);
                    cal.add(Calendar.DAY_OF_MONTH,1);
                    cal.add(Calendar.SECOND,-1);
                    endDate = cal.getTime();
                    rule.setEndDate(endDate);
                    continue;
                }
                catch(Exception ie) {
                }

                // try to get duration
                if (token.startsWith("#")) {
                    token = token.substring(1);
                    duration = Integer.parseInt(token);
                    rule.setDuration(duration);
                } else{            //modifiers
                    modifier.append(token+" ");
                }
            }
            if(modifier.length()>0)
                rule.setModifier(modifier.toString());
        }
        catch(Exception e) {
//            e.printStackTrace();
            throw new ParseException(e.toString(), 0);
        }

        return rule;
    }

    public static boolean checkForReminderAlerts(HttpServletRequest request, CalendarEvent event, Date currentDate) {
        boolean showAlert = false;
        long startTime = event.getStartDate().getTime();
        long reminderTime = (event.getReminderDate() != null) ? event.getReminderDate().getTime() : 0;
        long currentTime = (currentDate != null) ? currentDate.getTime() : System.currentTimeMillis();
        if (reminderTime > 0 && currentTime < startTime && currentTime >= reminderTime) {
            HttpSession session = request.getSession();
            Map sessionAlertMap = (Map)session.getAttribute("sessionAlertKey");
            if (sessionAlertMap == null) {
                sessionAlertMap = new HashMap();
            }
            if (sessionAlertMap.get(event.getEventId()) == null) {
                showAlert = true;
                sessionAlertMap.put(event.getEventId(), Boolean.TRUE);
                session.setAttribute("sessionAlertKey", sessionAlertMap);
            }
        }
        return showAlert;
    }

    public static void resetReminderAlert(HttpServletRequest request, String eventId) {
        HttpSession session = request.getSession();
        Map sessionAlertMap = (Map)session.getAttribute("sessionAlertKey");
        if (sessionAlertMap != null) {
            sessionAlertMap.remove(eventId);
        }
    }

}
