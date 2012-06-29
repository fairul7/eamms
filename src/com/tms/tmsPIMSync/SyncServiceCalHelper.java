package com.tms.tmsPIMSync;

import com.tms.collab.calendar.model.Appointment;
import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.taskmanager.model.Task;
import com.tms.tmsPIMSync.model.SyncEvent;
import com.funambol.foundation.pdi.event.Calendar;
import com.funambol.foundation.pdi.event.RecurrencePattern;
import com.funambol.foundation.pdi.parser.XMLEventParser;

import java.util.Map;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.Date;
import java.util.logging.Level;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.io.ByteArrayInputStream;

import kacang.stdui.CountrySelectBox;

/**
 * Assist Calendar methods in PIMSyncService
 */
public class SyncServiceCalHelper {
    public static DateFormat utcFormat;

    static{
        utcFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        utcFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public static DateFormat format;
    static{
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public static DateFormat dateOnlyFormat;

     static{
        dateOnlyFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateOnlyFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public Date dateParser(String dateString, Date defaultDate) throws ParseException {
        if(dateString == null || "".equals(dateString))
            return new Date();
        if(dateString.length() == 16){
            return utcFormat.parse(dateString);
        }else if(dateString.length() == 10){
            return dateOnlyFormat.parse(dateString);
        }else if(dateString.length() == 19){
            return format.parse(dateString);
        }else if(defaultDate != null){
            return defaultDate;
        }
        
        return new Date();
    }

    public String ekpCalendarEventToXml(CalendarEvent ce) throws Exception {
        if(ce.getEventId().startsWith(SyncEvent.EKP_APPOINTMENT)){
            return ekpCalendarToXml(ce);
        }else if(ce.getEventId().startsWith(SyncEvent.EKP_EVENT)){
            return ekpCalendarToXml(ce);
        }else throw new Exception("Invalid type");
    }

    private String ekpCalendarToXml(CalendarEvent ce) throws Exception {
        SyncEvent se = new SyncEvent();
        return se.ekpCalendarEventToXml(ce);
    }

     /**
     * @return a representation of the event field RRULE:
     */
    public StringBuffer composeFieldRrule(RecurrencePattern rrule) {
        StringBuffer result = new StringBuffer(60); // Estimate 60 is needed
        if (rrule != null) {
            result.append(rrule.getTypeDesc()).append(rrule.getInterval());

            if (rrule.getInstance() != 0) {
                //is possible return only positive instance (number+)
                result.append(" " + rrule.getInstance() + "+");
            }
            for (int i=0; i<rrule.getDayOfWeek().size(); i++) {
                result.append(" " + rrule.getDayOfWeek().get(i));
            }
            if (rrule.getDayOfMonth() != 0 && !"YM".equals(rrule.getTypeDesc())) {
                result.append(" " + rrule.getDayOfMonth());
            }
            if (rrule.getMonthOfYear() != 0) {
                result.append(" " + rrule.getMonthOfYear());
            }
            if (rrule.getOccurrences() != -1 && rrule.isNoEndDate()) {
                result.append(" #" + rrule.getOccurrences());
            } else {
                if (rrule.isNoEndDate()) {
                    result.append(" #0"); //forever
                }
            }
            if (!rrule.isNoEndDate()               &&
                 rrule.getEndDatePattern() != null &&
                !rrule.getEndDatePattern().equals("")) {
                result.append(" " + rrule.getEndDatePattern());
            }
        }
        return result;
    }


    /**
      * Get Data from XML message
      * converting the xml item into a Calendar object
      *
      * the calendar object is a com.funambol.foundation.pdi.event.Calendar
      *
      * @param content String
      * @return Calendar
      */

    protected Calendar getFoundationCalendarFromXML(String content) throws Exception {
        ByteArrayInputStream buffer = null;
        XMLEventParser parser       = null;
        Calendar calendar           = null;
        try {
            calendar = new Calendar();
            buffer = new ByteArrayInputStream(content.getBytes());
            if ((content.getBytes()).length > 0) {
                parser = new XMLEventParser(buffer);
                calendar = parser.parse();
            }
        } catch (Exception e) {
            throw new Exception(e.toString());
        }
        return calendar;
    }

   
}
