package com.tms.ekms.ekpmaildaemon.model.stub;

import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.calendar.model.CalendarException;
import com.tms.collab.calendar.model.CalendarModule;
import com.tms.collab.calendar.model.ConflictException;
import com.tms.collab.resourcemanager.model.Resource;
import com.tms.collab.resourcemanager.model.ResourceManager;

import kacang.Application;

import kacang.model.DaoException;

import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;

import kacang.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.*;


public class AddAppointment implements MailStub {
    private String prefix;
    private CalendarEvent leaveevent;
    private String userId;
    private boolean ignoreConflicts;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public CalendarEvent getLeaveevent() {
        return leaveevent;
    }

    public void setLeaveevent(CalendarEvent leaveevent) {
        this.leaveevent = leaveevent;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isIgnoreConflicts() {
        return ignoreConflicts;
    }

    public void setIgnoreConflicts(boolean ignoreConflicts) {
        this.ignoreConflicts = ignoreConflicts;
    }

    public boolean getPattern() //return subject pattern
     {
        return false; //To change body of implemented methods use File | Settings | File Templates.
    }

    public Map processMail(String emailAddress, Map contentMap) //pass to mailmodule
     {
        Application app = Application.getInstance();
        String errorMsg = "";
        CalendarModule module = (CalendarModule) Application.getInstance()
                                                            .getModule(CalendarModule.class);

        contentMap.put("EMAIL", emailAddress);

        /*        //do filtering of date from string into int
                int[] timeStart = { 0, 0, 0, 0, 0 };
                int[] timeEnd = { 0, 0, 0, 0, 0 };
                int count = 0;*/
        String startDate = contentMap.get(app.getMessage(
                    "maildaemon.stub.addappointment.label.startdate")).toString();
        String endDate = contentMap.get(app.getMessage(
                    "maildaemon.stub.addappointment.label.enddate")).toString();

        /*   StringTokenizer st = new StringTokenizer(startDate, " .-,/\\:");

           while (st.hasMoreTokens()) {
               try {
                   timeStart[count] = Integer.parseInt(st.nextToken());
               } catch (NumberFormatException e) {
                   timeStart[count] = 0;
               }

               count++;
           }

           count = 0;

           StringTokenizer st2 = new StringTokenizer(endDate, " .-,/\\:");

           while (st2.hasMoreTokens()) {
               try {
                   timeEnd[count] = Integer.parseInt(st2.nextToken());
               } catch (NumberFormatException e) {
                   timeEnd[count] = 0;
               }

               count++;
           }

           //force month +1 ,coz start count from 0
           timeStart[1] -= 1;
           timeEnd[1] -= 1;

           Calendar calStart = Calendar.getInstance();
           calStart.set(timeStart[0], timeStart[1], timeStart[2], timeStart[3],
               timeStart[4]);

           Calendar calEnd = Calendar.getInstance();
           calEnd.set(timeEnd[0], timeEnd[1], timeEnd[2], timeEnd[3], timeEnd[4]);
        */
        Calendar calStart = Calendar.getInstance();
        Calendar calEnd = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        sdf.setLenient(false);

        try {
            calStart.setTime(sdf.parse(startDate));

            calEnd.setTime(sdf.parse(endDate));
        } catch (ParseException e) {
            errorMsg = "\n" +
                app.getMessage("maildaemon.label.error.invalidDate");
        }

        CalendarEvent event = new CalendarEvent();
        event.setTitle(contentMap.get(app.getMessage(
                    "maildaemon.stub.addappointment.label.title")).toString());
        event.setStartDate(calStart.getTime());
        event.setEndDate(calEnd.getTime());
        event.setReminder(true);
        event.setDescription(contentMap.get(app.getMessage(
                    "maildaemon.stub.addappointment.label.description"))
                                       .toString());
        event.setLocation(contentMap.get(app.getMessage(
                    "maildaemon.stub.addappointment.label.location")).toString());

        /*
                String classification ="";
                       classification =*/
        /*      contentMap.get(app.getMessage(
           "maildaemon.stub.addappointment.label.classification"))
                                 .toString();*/
        /*  System.out.println(classification) ;

        event.setClassification(classification);*/
        String classification = contentMap.get(app.getMessage(
                    "maildaemon.stub.addappointment.label.classification"))
                                          .toString();

        if (classification.trim().replaceAll(" ", "").equalsIgnoreCase("PUB")) {
            classification = "pub";
        } else {
            classification = "pri";
        }

        event.setClassification(classification);

        event.setAgenda(contentMap.get(app.getMessage(
                    "maildaemon.stub.addappointment.label.agenda")).toString());

        /**
         try {
             userCollection = ss.getUsersByUsername(contentMap.get(
                         app.getMessage("maildaemon.label.username")).toString());
         } catch (kacang.services.security.SecurityException e) {
             e.printStackTrace();
         }

         for (Iterator itc = userCollection.iterator(); itc.hasNext();) {
             user = (User) itc.next();

         }
         ***/
        StringTokenizer resourceFilter = new StringTokenizer(contentMap.get(
                    app.getMessage(
                        "maildaemon.stub.addappointment.label.resources"))
                                                                       .toString(),
                ",");
        ResourceManager rm = (ResourceManager) Application.getInstance()
                                                          .getModule(ResourceManager.class);

        Collection col = null; //compared with all resources in database
        Collection resourcesWanted = new Vector();

        try {
            col = rm.getResources(contentMap.get("USERID").toString(), true);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (DaoException e) {
            e.printStackTrace();
        }

        while (resourceFilter.hasMoreTokens()) {
            try {
                String resourceFromMail = resourceFilter.nextToken();

                for (Iterator i = col.iterator(); i.hasNext();) {
                    Resource resource = (Resource) i.next();

                    if ((resourceFromMail.replaceAll(" ", "").trim()).equalsIgnoreCase(
                                resource.getName().replaceAll(" ", "").trim())) {
                        resourcesWanted.add(resource);
                    } else {
                    }
                }
            } catch (NoSuchElementException f) {
                f.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                Log.getLog(getClass()).error("Error retrieving resources", e);
            }
        }

        event.setResources(resourcesWanted);

        try {
            module.addCalendarEvent("com.tms.collab.calendar.model.Appointment",
                event, contentMap.get("USERID").toString(), true);
        } catch (ConflictException e) {
            errorMsg += ("\n" +
            app.getMessage("maildaemon.stub.addapponitment.label.conflict"));
            e.printStackTrace();
        } catch (CalendarException e) {
            errorMsg += ("\n" +
            app.getMessage(
                "maildaemon.stub.addappointment.label.calendar.error"));
            e.printStackTrace();
        }

        if ("".equals(errorMsg)) {
            errorMsg = "\n" + app.getMessage("maildaemon.label.successful");
            Log.getLog(getClass()).info("SUCCESSFUL ADDED APPOINTMENT <EKPMAILDAEMON>");
        }

        contentMap.put("ERROR", (String) errorMsg);

        return contentMap;
    }

    public String getSubjectPattern() {
        Application app = Application.getInstance();

        return app.getMessage(
            "maildaemon.stub.addappointment.label.addappointment");
    }

    public String[] getBodyPattern() {
        Application app = Application.getInstance();
        String[] tempPattern = {
            app.getMessage("maildaemon.stub.addappointment.label.title"),
            app.getMessage("maildaemon.stub.addappointment.label.startdate"),
            app.getMessage("maildaemon.stub.addappointment.label.enddate"),
            app.getMessage("maildaemon.stub.addappointment.label.description"),
            app.getMessage("maildaemon.stub.addappointment.label.location"),
            app.getMessage(
                "maildaemon.stub.addappointment.label.classification"),
            app.getMessage("maildaemon.stub.addappointment.label.resources"),
            app.getMessage("maildaemon.stub.addappointment.label.agenda")
        };

        return tempPattern; //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getInfo() {
        Application app = Application.getInstance();
        User user = new User();
        String customOption = "";

        SecurityService ss = (SecurityService) Application.getInstance()
                                                          .getService(SecurityService.class);
        Collection userCollection = null;

        try {
            userCollection = ss.getUsersByUsername((String) "anonymous");
        } catch (kacang.services.security.SecurityException e) {
            e.printStackTrace();
        }

        for (Iterator itc = userCollection.iterator(); itc.hasNext();) {
            user = (User) itc.next();
        }

        customOption += ("\n" +
        app.getMessage("maildaemon.stub.addappointment.label.resources") +
        ":\n");

        ResourceManager rm = (ResourceManager) Application.getInstance()
                                                          .getModule(ResourceManager.class);

        try {
            Collection col = rm.getResources(user.getId(), true);

            for (Iterator i = col.iterator(); i.hasNext();) {
                Resource resource = (Resource) i.next();

                customOption += (resource.getName());

                if (i.hasNext()) {
                    customOption += ", ";
                }
            }
        } catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving resources", e);
        }

        return customOption;
    }

    public String getHeaderInfo() {

        Application app = Application.getInstance();
        return app.getMessage(
            "maildaemon.label.info.timeInterval");
    }
}
