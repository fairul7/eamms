package com.tms.ekms.ekpmaildaemon.model.stub;

import com.tms.collab.calendar.model.*;
import com.tms.collab.emeeting.Meeting;
import com.tms.collab.emeeting.MeetingException;
import com.tms.collab.emeeting.MeetingHandler;
import com.tms.collab.resourcemanager.model.Resource;
import com.tms.collab.taskmanager.model.Task;

import kacang.Application;
import kacang.services.security.*;
import kacang.services.security.SecurityException;

import kacang.util.Log;

import java.util.*;
import java.text.SimpleDateFormat;


public class ListAppointment implements MailStub {
    private String search;
    private String type;
    private Date from;
    private Date to;
    private String userId;
    private String[] userIds;
    private String[] resourceIds;
    private boolean includeReminders;
    private boolean includeUniversal;
    private boolean includeDeleted;
    private String sort;
    private boolean desc;
    private int start;
    private int rows;

    public void doList() {
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String[] getUserIds() {
        return userIds;
    }

    public void setUserIds(String[] userIds) {
        this.userIds = userIds;
    }

    public String[] getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(String[] resourceIds) {
        this.resourceIds = resourceIds;
    }

    public boolean isIncludeReminders() {
        return includeReminders;
    }

    public void setIncludeReminders(boolean includeReminders) {
        this.includeReminders = includeReminders;
    }

    public boolean isIncludeUniversal() {
        return includeUniversal;
    }

    public void setIncludeUniversal(boolean includeUniversal) {
        this.includeUniversal = includeUniversal;
    }

    public boolean isIncludeDeleted() {
        return includeDeleted;
    }

    public void setIncludeDeleted(boolean includeDeleted) {
        this.includeDeleted = includeDeleted;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public boolean isDesc() {
        return desc;
    }

    public void setDesc(boolean desc) {
        this.desc = desc;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public Map processMail(String emailAddress, Map contentMap) //pass to mailmodule
     {
        Application app = Application.getInstance();
        String result = ""; //use to store error msg or result from query
        contentMap.put("EMAIL", emailAddress);

        String userId = contentMap.get("USERID").toString();
        String[] userIds = { contentMap.get("USERID").toString() };
        Application application = Application.getInstance();
        CalendarModule handler = (CalendarModule) application.getModule(CalendarModule.class);
        Calendar calendar = Calendar.getInstance();

        Collection tasks = null;
        Collection appointments = null;
        Collection events = null;
        Collection meetings = null;

        try {
            Collection eventList = handler.getDailyCalendarEvents(calendar.getTime(),
                    userId, userIds, null);

            if (eventList != null) {
                tasks = new ArrayList(10);
                appointments = new ArrayList(10);
                events = new ArrayList(10);
                meetings = new ArrayList(10);

                MeetingHandler mh = null;

                for (Iterator i = eventList.iterator(); i.hasNext();) {
                    CalendarEvent event = (CalendarEvent) i.next();
                    String eventId = event.getEventId();
                    String eventType = eventId.substring(0, eventId.indexOf('_'));

                    if (eventType.equals(CalendarEvent.class.getName())) {
                        events.add(event);
                    } else if (eventType.equals(Appointment.class.getName())) {
                        appointments.add(event);
                    } else if (eventType.equals(Task.class.getName())) {
                        if (event instanceof Task) {
                            tasks.add(event);
                        }
                    } else if (eventType.equals(Meeting.class.getName())) {
                        if (mh == null) {
                            mh = (MeetingHandler) Application.getInstance()
                                                             .getModule(MeetingHandler.class);
                        }

                        try {
                            Meeting meeting = mh.getMeeting(eventId, true);
                            meeting.setEvent(event);
                            meetings.add(meeting);
                        } catch (MeetingException e) {
                            Log.getLog(getClass()).error(e);
                        }
                    }
                }
            }

            for (Iterator itc = appointments.iterator(); itc.hasNext();) {
                CalendarEvent element = (CalendarEvent) itc.next();

                result += ("\n" +
                app.getMessage("maildaemon.stub.listappointment.label.title") +
                ": " + element.getTitle());





                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");


                result += ("\n" +
                app.getMessage(
                    "maildaemon.stub.listappointment.label.starttime") + ": " +
                sdf.format(element.getStartDate()));


                result += ("\n" +
                app.getMessage("maildaemon.stub.listappointment.label.enddate") +
                ": " + sdf.format(element.getEndDate()));


                result += ("\n" +
                app.getMessage(
                    "maildaemon.stub.listappointment.label.description") +
                ": " + element.getDescription());

                String classificationStr = element.getClassification();
                if(classificationStr.equalsIgnoreCase("pub"))
                     classificationStr = "Public";
                else classificationStr = "Private";

                result += ("\n" +
                app.getMessage(
                    "maildaemon.stub.listappointment.label.classification") +
                ": " + classificationStr);

                result += ("\n" +
                app.getMessage("maildaemon.stub.listappointment.label.agenda") +
                ": " + element.getAgenda());

                //get resources
                Collection resources = element.getResources();
                String resourcesStr="";
                boolean firstComma =true;
                for (Iterator icount = resources.iterator(); icount.hasNext();) {
                    Resource resource = (Resource) icount.next();

                    if(! firstComma )  resourcesStr += ", ";
                    resourcesStr += resource.getName();
                    firstComma = false;




                }


                result += ("\n" +
                app.getMessage("maildaemon.stub.listappointment.label.resources") +
                ": " + resourcesStr);




                //get Attendees
                Collection attendees = element.getAttendees();
                String cAttendees="";
                String pAttendees="";
                firstComma = true;
                boolean firstComma2 = true;
                for (Iterator icount2 = attendees.iterator();
                        icount2.hasNext();) {
                    Attendee attendee = (Attendee) icount2.next();

                 if( attendee.getStatus().equalsIgnoreCase("P"))
                 { if(!firstComma)
                        pAttendees += ", ";
                 }
                 else
                    if(!firstComma2)
                        cAttendees += ", ";


                    if( attendee.getStatus().equalsIgnoreCase("P"))
                    {pAttendees +=attendee.getName() ;
                     firstComma = false;

                    }
                    else
                    {cAttendees +=attendee.getName() ;
                     firstComma2 =false;
                    }


                }


                result += ("\n" +
                app.getMessage("maildaemon.stub.listappointment.label.compulsoryattendees") +
                ": " + cAttendees);


                result += ("\n" +
                app.getMessage("maildaemon.stub.listappointment.label.attendees") +
                ": " + pAttendees);





                //get last modified date
                Date lastModifiedDate = element.getLastModified();

                result += ("\n" +
                                app.getMessage("maildaemon.stub.listappointment.label.lastmodified") +
                                ": " + sdf.format(lastModifiedDate) );




                try {

                    result += "\n" +
                                    app.getMessage("maildaemon.stub.listappointment.label.modifiedby") +
                                    ": " + element.getLastModifiedName() ;



                } catch (SecurityException e) {

                }


                //line break
                result += "\n\n\n" ;





            }
        } catch (CalendarException e) {
            e.printStackTrace();
        }

        contentMap.put("ERROR", (String) result); //in this care, no error msg, only key *name only

        return contentMap;
    }

    public String getSubjectPattern() {
        Application app = Application.getInstance();

        return app.getMessage(
            "maildaemon.stub.listappointment.label.listappointment");
    }

    public String[] getBodyPattern() {
        String[] tempPattern = { "" };

        return tempPattern;
    }

    public String getInfo() {
        return "";
    }


     public String getHeaderInfo(){



          return "";
    }

}
