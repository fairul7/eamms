package com.tms.ekms.ekpmaildaemon.model.stub;

import com.tms.collab.calendar.model.Appointment;
import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.calendar.model.CalendarException;
import com.tms.collab.calendar.model.CalendarModule;
import com.tms.collab.emeeting.Meeting;
import com.tms.collab.emeeting.MeetingException;
import com.tms.collab.emeeting.MeetingHandler;
import com.tms.collab.taskmanager.model.Assignee;
import com.tms.collab.taskmanager.model.Task;

import kacang.Application;
import kacang.services.security.*;
import kacang.services.security.SecurityException;

import kacang.util.Log;

import java.text.SimpleDateFormat;

import java.util.*;


public class ListToDo implements MailStub {
    private String filter;
    private String userId;
    private String categoryId;
    private int sIndex;
    private int maxRow;
    private String sort;
    private boolean desc;

    public void doList() {
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public int getsIndex() {
        return sIndex;
    }

    public void setsIndex(int sIndex) {
        this.sIndex = sIndex;
    }

    public int getMaxRow() {
        return maxRow;
    }

    public void setMaxRow(int maxRow) {
        this.maxRow = maxRow;
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

            for (Iterator itc = tasks.iterator(); itc.hasNext();) {
                Task element = (Task) itc.next();

                result += ("\n" +
                app.getMessage("maildaemon.stub.listtodo.label.title") + ": " +
                element.getTitle());

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");

                result += ("\n" +
                app.getMessage("maildaemon.stub.listtodo.label.startdate") +
                ": " + sdf.format(element.getStartDate()));

                result += ("\n" +
                app.getMessage("maildaemon.stub.listtodo.label.enddate") +
                ": " + sdf.format(element.getEndDate()));

                result += ("\n" +
                app.getMessage("maildaemon.stub.listtodo.label.description") +
                ": " + element.getDescription());
                result += ("\n" +
                app.getMessage("maildaemon.stub.listtodo.label.category") +
                ": " + element.getCategory());
                result += ("\n" +
                app.getMessage("maildaemon.stub.listtodo.label.overallprogress") +
                ": " + Float.toString(element.getOverallProgress()) + " % ");

                //get assignees
                Collection assignees = element.getAttendees();
                String assigneesStr = "";
                boolean firstComma =true;
                for (Iterator icount = assignees.iterator(); icount.hasNext();) {
                    Assignee assignee = (Assignee) icount.next();

                    if(!firstComma) assigneesStr += ", ";

                    assigneesStr += (assignee.getName() );

                    firstComma =false;

                }

                result += ("\n" +
                app.getMessage("maildaemon.stub.listtodo.label.assignees") +
                ": " + assigneesStr);

                result += ("\n" +
                app.getMessage("maildaemon.stub.listtodo.label.assigner") +
                ": " + element.getAssigner());



             


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

        return app.getMessage("maildaemon.stub.listtodo.label.listtodo");
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
