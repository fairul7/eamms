package com.tms.ekms.ekpmaildaemon.model.stub;

import com.tms.collab.calendar.model.CalendarException;
import com.tms.collab.calendar.model.CalendarModule;
import com.tms.collab.calendar.model.ConflictException;
import com.tms.collab.resourcemanager.model.Resource;
import com.tms.collab.resourcemanager.model.ResourceManager;
import com.tms.collab.taskmanager.model.Assignee;
import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.taskmanager.model.TaskCategory;
import com.tms.collab.taskmanager.model.TaskManager;

import kacang.Application;

import kacang.model.DaoException;

import kacang.services.security.SecurityService;
import kacang.services.security.User;

import kacang.util.Log;
import kacang.util.UuidGenerator;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.*;


public class AddToDo implements MailStub {
    private Task task;

    public void doAdd() {
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public boolean getPattern() //return subject pattern
     {
        return false;
    }

    public Map processMail(String emailAddress, Map contentMap) //pass to mailmodule
     {
        Application app = Application.getInstance();
        String errorMsg = "";
        CalendarModule cmodule = (CalendarModule) Application.getInstance()
                                                             .getModule(CalendarModule.class);
        TaskManager module = (TaskManager) Application.getInstance().getModule(TaskManager.class);

        contentMap.put("EMAIL", emailAddress);

        /*   //do filtering of date from string into int
           int[] timeStart = { 0, 0, 0, 0, 0 };
           int[] timeEnd = { 0, 0, 0, 0, 0 };
           int count = 0;
        */
        String startDate = contentMap.get(app.getMessage(
                    "maildaemon.stub.addtodo.label.startdate")).toString();
        String endDate = contentMap.get(app.getMessage(
                    "maildaemon.stub.addtodo.label.enddate")).toString();

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

        // Calendar calStart = Calendar.getInstance();
        Task task = new Task();
        task.setCreationDate(calStart.getTime());
        task.setInstanceDate(calStart.getTime());
        task.setNew(true);
        task.setLastModified(calStart.getTime());

        task.setTitle(contentMap.get(app.getMessage(
                    "maildaemon.stub.addtodo.label.title")).toString());

        /*
                calStart.set(timeStart[0], timeStart[1] - 1, timeStart[2],
                    timeStart[3], timeStart[4]);
        */

        //Calendar calEnd = Calendar.getInstance();

        /*
                calEnd.set(timeEnd[0], timeEnd[1] - 1, timeEnd[2], timeEnd[3],
                    timeEnd[4]);
        */
        task.setStartDate(calStart.getTime());
        task.setEndDate(calEnd.getTime());
        task.setDueDate(calEnd.getTime());

        task.setDescription(contentMap.get(app.getMessage(
                    "maildaemon.stub.addtodo.label.description")).toString());

        SecurityService ss = (SecurityService) Application.getInstance()
                                                          .getService(SecurityService.class);

        User user = new User();

        try {
            user = ss.getUser(contentMap.get("USERID").toString());
        } catch (kacang.services.security.SecurityException e) {
            e.printStackTrace();
        }

        StringTokenizer resourceFilter = new StringTokenizer(contentMap.get(
                    app.getMessage("maildaemon.stub.addtodo.label.resources"))
                                                                       .toString(),
                ",");

        ResourceManager rm = (ResourceManager) Application.getInstance()
                                                          .getModule(ResourceManager.class);

        Collection col = null; //compared with all resources in database

        Collection resourcesWanted = new Vector();

        try {
            col = rm.getResources(user.getId(), true);
        } catch (kacang.services.security.SecurityException e) {
            e.printStackTrace();
        } catch (DaoException e) {
            e.printStackTrace();
        }

        //processing the resource
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

        task.setAssigner(user.getName());
        task.setAssignerId(user.getId());

        task.setUserId(user.getId());

        String id = Task.class.getName() + "_" +
            UuidGenerator.getInstance().getUuid();
        task.setId(id);
        task.setEventId(id);

        task.setResources(resourcesWanted);

        task.setClassification("pub");
        task.setInstanceId("0");
        task.setLastModifiedBy(user.getId());

        TaskManager tm = (TaskManager) Application.getInstance().getModule(TaskManager.class);

        try {
            col = tm.getUserCategories(user.getId(), true, "name", true, 0, -1);
        } catch (DaoException e) {
            e.printStackTrace();
        }

        for (Iterator i = col.iterator(); i.hasNext();) {
            TaskCategory cat = (TaskCategory) i.next();

            if (cat.getName().replaceAll(" ", "").trim().equals(contentMap.get(
                            app.getMessage(
                                "maildaemon.stub.addtodo.label.category"))
                                                                              .toString()
                                                                              .replaceAll(" ",
                            "").trim())) {
                task.setCategoryId(cat.getId());
            }
        }

        Assignee assignee = new Assignee();
        assignee.setAttendeeId(user.getId());
        assignee.setAttendeeId(id + "_" + CalendarModule.DEFAULT_INSTANCE_ID +
            "_" + user.getId());

        assignee.setUserId(user.getId());

        assignee.setEventId(task.getId());
        assignee.setStatus("C");
        assignee.setInstanceId("0");
        assignee.setCompulsory(true);

        assignee.setProperty("username", user.getUsername());
        assignee.setProperty("firstName", user.getProperty("firstName"));
        assignee.setProperty("lastName", user.getProperty("lastName"));

        Collection assignees = new ArrayList();

        assignees.add(assignee);

        task.setAttendees(assignees);

        try {
            cmodule.addCalendarEvent(Task.class.getName(), task,
                task.getUserId(), true);
        } catch (ConflictException e) {
            e.printStackTrace();
        } catch (CalendarException e) {
            e.printStackTrace();
        }

        try {
            module.addTask(task);
        } catch (CalendarException e) {
            e.printStackTrace();
            errorMsg += ("\n" +
            app.getMessage("maildaemon.stub.addtodo.label.invaliddate"));
        } catch (DaoException e) {
            e.printStackTrace();
            errorMsg += ("\n" +
            app.getMessage("maildaemon.stub.addtodo.label.invalidentry"));
        }

        if ("".equals(errorMsg)) {
            errorMsg = "\n" + app.getMessage("maildaemon.label.successful");
            Log.getLog(getClass()).info("SUCCESSFUL ADDED TODO <EKPMAILDAEMON>");
        }

        contentMap.put("ERROR", (String) errorMsg);

        return contentMap;
    }

    public String getSubjectPattern() {
        Application app = Application.getInstance();

        return app.getMessage("maildaemon.stub.addtodo.label.addtodo");
    }

    public String[] getBodyPattern() {
        Application app = Application.getInstance();
        String[] tempPattern = {
            app.getMessage("maildaemon.stub.addtodo.label.title"),
            app.getMessage("maildaemon.stub.addtodo.label.startdate"),
            app.getMessage("maildaemon.stub.addtodo.label.enddate"),
            app.getMessage("maildaemon.stub.addtodo.label.category"),
            app.getMessage("maildaemon.stub.addtodo.label.description"),
            app.getMessage("maildaemon.stub.addtodo.label.resources")
        };

        return tempPattern;
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
        app.getMessage("maildaemon.stub.addtodo.label.resources") + ":\n");

        ResourceManager rm = (ResourceManager) Application.getInstance()
                                                          .getModule(ResourceManager.class);

        try {
            Collection col = rm.getResources(user.getId(), true);

            for (Iterator i = col.iterator(); i.hasNext();) {
                Resource resource = (Resource) i.next();

                customOption += (resource.getName() + ",  ");
            }

            //get task category
            customOption += ("\n" +
            app.getMessage("maildaemon.stub.addtodo.label.category") + ":\n");

            TaskManager tm = (TaskManager) Application.getInstance().getModule(TaskManager.class);
            col = tm.getUserCategories(user.getId(), true, "name", true, 0, -1);

            for (Iterator i = col.iterator(); i.hasNext();) {
                TaskCategory cat = (TaskCategory) i.next();
                customOption += (cat.getName());

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

        return app.getMessage("maildaemon.label.info.timeInterval");
    }
}
