package com.tms.cms.tdk;

import com.tms.collab.calendar.model.CalendarModule;
import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.util.FormatUtil;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;

import java.text.SimpleDateFormat;
import java.util.*;

public class DisplayEventList extends LightWeightWidget {

    public static final String DATE_FORMAT = "yyyyMMdd";

    private Collection eventList = new ArrayList();
    private String start;
    private String end;

    public Collection getEventList() {
        return eventList;
    }

    public void setEventList(Collection eventList) {
        this.eventList = eventList;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getDefaultTemplate() {
        return "cms/tdk/displayEventList";
    }

    public void onRequest(Event evt) {
        try {
            Date startDate = null;
            Date endDate = null;

            // get requested date range
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
                startDate = sdf.parse(start);
                endDate = sdf.parse(end);
            }
            catch (Exception e) {
                // get events for next 30 days
                Calendar cal = Calendar.getInstance();
                startDate = cal.getTime();
                cal.add(Calendar.DAY_OF_MONTH, 30);
                endDate = cal.getTime();
            }

            // get events between specified period
            Application application = Application.getInstance();
            User user = ((SecurityService)application.getService(SecurityService.class)).getCurrentUser(evt.getRequest());
            CalendarModule calendar = (CalendarModule)application.getModule(CalendarModule.class);
            this.eventList = calendar.getCalendarEvents(null, null, startDate, endDate, user.getId(), new String[] {SecurityService.ANONYMOUS_USER_ID}, null, true, true, false, null, false, 0, -1);
        }
        catch(Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

}
