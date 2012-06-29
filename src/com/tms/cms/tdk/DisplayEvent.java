package com.tms.cms.tdk;

import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.calendar.model.CalendarModule;
import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;

public class DisplayEvent extends LightWeightWidget {

    private String id;
    private CalendarEvent event;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CalendarEvent getEvent() {
        return event;
    }

    public String getDefaultTemplate() {
        return "cms/tdk/displayEvent";
    }

    public void onRequest(Event evt) {
        try {
            String eventId = getId();
            if (eventId == null || eventId.trim().length() ==0) {
                eventId = evt.getRequest().getParameter("id");
            }

            // get events between specified period
            Application application = Application.getInstance();
//            User user = ((SecurityService)application.getService(SecurityService.class)).getCurrentUser(evt.getRequest());
            CalendarModule calendar = (CalendarModule)application.getModule(CalendarModule.class);
            this.event = calendar.getCalendarEvent(eventId);
        }
        catch(DataObjectNotFoundException e) {
            throw new RuntimeException(e.toString());
        }
        catch(Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

}
