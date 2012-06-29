package com.tms.tmsPIMSync.model;

import com.funambol.foundation.pdi.event.SIFE;
import com.funambol.foundation.pdi.event.RecurrencePattern;
import com.funambol.foundation.pdi.utils.SourceUtils;
import com.funambol.foundation.pdi.utils.TimeUtils;
import com.funambol.foundation.pdi.parser.RRuleParser;
import com.tms.collab.calendar.model.Appointment;
import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.calendar.model.CalendarModule;

import java.util.Map;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.logging.Logger;

public class SyncEvent implements SIFE {
    public static String EKP_APPOINTMENT = Appointment.class.getName();
    public static String EKP_EVENT = CalendarEvent.class.getName();

    private Map ekpCalendarEventToMap(CalendarEvent ce) throws Exception {
        Map map = new HashMap();
        map.put(SourceUtils.ROOT_NAME, ROOT_TAG);

        if(ce.getEventId()!= null) map.put(UID, ce.getEventId());
        if(ce.isAllDay()) map.put(ALL_DAY_EVENT, "1"); else map.put(ALL_DAY_EVENT, "0");
        if(ce.getDescription()!=null) map.put(BODY, ce.getDescription());
        if(ce.getCreationDate()!=null) map.put(DTSTAMP, TimeUtils.convertLocalDateToUTC(TimeUtils.convertDateTo(ce.getCreationDate(), TimeUtils.PATTERN_UTC_WOZ), TimeZone.getDefault()));
        if(ce.getEndDate()!=null) map.put(END, TimeUtils.convertLocalDateToUTC(TimeUtils.convertDateTo(ce.getEndDate(), TimeUtils.PATTERN_UTC_WOZ), TimeZone.getDefault()));

        if(ce.getRecurrenceRule()!=null){
            RRuleParser parser = new RRuleParser();
            RecurrencePattern rp = parser.extractRecurrencePattern(ce.getRecurrenceRule(), TimeUtils.convertLocalDateToUTC(TimeUtils.convertDateTo(ce.getStartDate(), TimeUtils.PATTERN_UTC_WOZ), TimeZone.getDefault()), TimeZone.getTimeZone("GMT"));
            map.put(RECURRENCE_IS_RECURRING, "1");
            map.put(RECURRENCE_TYPE, String.valueOf(rp.getTypeId()));
            map.put(RECURRENCE_DAY_OF_MONTH, String.valueOf(rp.getDayOfMonth()));
            map.put(RECURRENCE_DAY_OF_WEEK_MASK, String.valueOf(rp.getDayOfWeekMask()));
            map.put(RECURRENCE_INTERVAL, String.valueOf(rp.getInterval()));
            map.put(RECURRENCE_INSTANCE, String.valueOf(rp.getInstance()));
            map.put(RECURRENCE_MONTH_OF_YEAR, String.valueOf(rp.getMonthOfYear()));
            if(rp.isNoEndDate()) map.put(RECURRENCE_NO_END_DATE, "1"); else map.put(RECURRENCE_NO_END_DATE, "0");

            String startDatePattern = rp.getStartDatePattern();
            if (startDatePattern == null) {
                startDatePattern = "";
            }
            map.put(RECURRENCE_START_DATE_PATTERN, startDatePattern);

            String endDatePattern = rp.getEndDatePattern();
            if (endDatePattern == null) {
                endDatePattern = "";
            }
            map.put(RECURRENCE_END_DATE_PATTERN, endDatePattern);

            if (rp.getOccurrences() > 0) {
                map.put(RECURRENCE_OCCURRENCES, String.valueOf(rp.getOccurrences()));
            }

        }else map.put(RECURRENCE_IS_RECURRING, "0");

        if(ce.getLocation()!=null) map.put(LOCATION, ce.getLocation());
        if(ce.getReminderDate()!=null){
            String dtStart = TimeUtils.convertDateTo(ce.getStartDate(), TimeUtils.PATTERN_UTC_WOZ);
            String dtAlarm = TimeUtils.convertDateTo(ce.getReminderDate(), TimeUtils.PATTERN_UTC_WOZ);

            int minutes = TimeUtils.getAlarmMinutes(dtStart, dtAlarm, Logger.getLogger("SyncEvent.class"));
            map.put(REMINDER_SET, "1");
            map.put(REMINDER_MINUTES_BEFORE_START, Integer.toString(minutes));
        }
        if(ce.getClassification()!=null){
            if(CalendarModule.CLASSIFICATION_PRIVATE.equals(ce.getClassification())) map.put(SENSITIVITY, "2");
            else map.put(SENSITIVITY, "0");
        }map.put(SENSITIVITY, "0");
                
        if(ce.getStartDate()!=null) map.put(START, TimeUtils.convertLocalDateToUTC(TimeUtils.convertDateTo(ce.getStartDate(), TimeUtils.PATTERN_UTC_WOZ), TimeZone.getDefault()));
        if(ce.getTitle()!=null) map.put(SUBJECT, ce.getTitle());

        return map;
    }

    private String hashMapToXml(Map map) throws Exception {
        String xml = SourceUtils.hashMapToXml(map);
        return xml;
    }

    public String ekpCalendarEventToXml(CalendarEvent ce) throws Exception {
        Map map = ekpCalendarEventToMap(ce);
        return hashMapToXml(map);
    }
}
