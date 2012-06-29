package com.tms.collab.taskmanager.ui;

import kacang.stdui.DateField;
import kacang.stdui.Hidden;
import kacang.stdui.SelectBox;
import kacang.stdui.Button;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.model.DaoException;
import kacang.util.Log;
import kacang.services.security.User;
import kacang.services.security.SecurityService;
import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.taskmanager.model.TaskManager;
import com.tms.collab.taskmanager.model.Assignee;
import com.tms.collab.calendar.model.Attendee;
import com.tms.collab.calendar.model.CalendarModule;
import com.tms.collab.calendar.model.ConflictException;
import com.tms.collab.calendar.ui.UserUtil;
import com.tms.collab.resourcemanager.model.Resource;
import com.tms.collab.resourcemanager.model.ResourceManager;

import java.util.*;

import org.apache.commons.collections.SequencedHashMap;
//import com.tms.collab.calendar.ui.CalendarUsersSelectBox;

//import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: May 19, 2005
 * Time: 3:18:00 PM
 * To change this template use File | Settings | File Templates.
 */   
public class MTaskForm extends TaskForm {

    public static final String DEFAULT_TEMPLATE = "taskmanager/mtaskform";
    public static final String FORWARD_ASSIGNEE = "assignees";

    protected Hidden userHidden;
    protected Hidden resourcesHidden;

    protected String[] resourcesList;
    protected String[] userList;
    protected Button assigneeButton;
    protected Map assigneeMap;
    private boolean populated = false;

    public MTaskForm() {}

    public MTaskForm(String name) {
        super(name);
    }

    public void init() {
        removeChildren();
        super.init();
        setMethod("GET");
        //Resetting onsubmit
        getAttributeMap().put("onSubmit", "");
        title.setSize("25");
        title.addChild(new ValidatorNotEmpty("validTitle"));
        description.setRows("5");
        description.setCols("20");

        removeChild(uploadForm);
        removeChild(fileListing);
        removeChild(dueDate);
        removeChild(startDate);

        assignees.setTemplate("kPopupSelectBox");
        resources.setTemplate("kComboSelectBox");

        dueDate = new DateField("dueDate");
        startDate = new DateField("startDate");
        addChild(dueDate);
        addChild(startDate);

        description.setRows("5");
        resources.setRows(5);
        notifyNote.setRows("5");

        userHidden=new Hidden("assigneeshidden");
        resourcesHidden = new Hidden("resourceshidden");
        addChild(userHidden);
        addChild(resourcesHidden);

        /*removeChild(reminderModifier);
        reminderModifier = new SelectBox("reminderModifier");
        reminderModifier.addOption(new Integer(Calendar.DAY_OF_MONTH).toString(), Application.getInstance().getMessage("calendar.label.optionDays","day(s)"));*/
        //reminderModifier.addOption(new Integer(Calendar.WEEK_OF_YEAR).toString(), Application.getInstance().getMessage("calendar.label.optionWeeks","week(s)"));
        /*addChild(reminderModifier);*/
        removeChild(startTime);
        removeChild(dueTime);

        assigneeButton = new Button("assigneeButton", Application.getInstance().getMessage("emeeting.label.assignees"));
        addChild(assigneeButton);
        populated = false;
        assigneeMap = new HashMap();
        //initializing assigneeMap
        if (taskId!=null&&taskId.length()>0) {
            TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
            try {
                Task task = tm.getTask(taskId);
                Collection col = task.getAttendees();
                String sAttendee="";
                boolean start=true;
                SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
                if (col!=null && col.size()>0) {
                    for (Iterator i=col.iterator();i.hasNext();) {
                        Attendee att = (Attendee)i.next();
                        if (start)
                            sAttendee=att.getUserId();
                        else
                            sAttendee+=","+att.getUserId();
                        start=false;
                        try
                        {
                            assigneeMap.put(att.getUserId(), service.getUser(att.getUserId()));
                        }
                        catch(Exception e) {}
                    }
                }
                String sResources="";
                start=true;
                col = task.getResources();
                if (col!=null&&col.size()>0) {
                    for (Iterator i=col.iterator();i.hasNext();) {
                        Resource resource = (Resource)i.next();
                        if (start)
                            sResources = resource.getId();
                        else
                            sResources += ","+resource.getId();
                        start=false;
                    }
                }
                resourcesHidden.setValue(sResources);
                userHidden.setValue(sAttendee);
            }
            catch(Exception e) {
            }
        }
    }

    public void onRequest(Event event)
    {
        if(!populated)
        {
            if (taskId!=null&&taskId.length()>0) {
                TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
                try {
                    Task task = tm.getTask(taskId);
                    Collection col = task.getAttendees();
                    String sAttendee="";
                    boolean start=true;
                    SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
                    if (col!=null && col.size()>0) {
                        for (Iterator i=col.iterator();i.hasNext();) {
                            Attendee att = (Attendee)i.next();
                            if (start)
                                sAttendee=att.getUserId();
                            else
                                sAttendee+=","+att.getUserId();
                            start=false;
                            try
                            {
                                assigneeMap.put(att.getUserId(), service.getUser(att.getUserId()));
                            }
                            catch(Exception e) {}
                        }
                    }
                    String sResources="";
                    start=true;
                    col = task.getResources();
                    if (col!=null&&col.size()>0) {
                        for (Iterator i=col.iterator();i.hasNext();) {
                            Resource resource = (Resource)i.next();
                            if (start)
                                sResources = resource.getId();
                            else
                                sResources += ","+resource.getId();
                            start=false;
                        }
                    }

                    resourcesHidden.setValue(sResources);
                    userHidden.setValue(sAttendee);
                }
                catch(Exception e) {
                }
            }
            super.onRequest(event);
            setMethod("GET");
        }
        populated = false;
    }

    public String getDefaultTemplate() {
        return DEFAULT_TEMPLATE;
    }

    public Forward onValidate(Event evt) {
        userList=null;
        resourcesList=null;

        String aList = (String)userHidden.getValue();
        String rList = (String)resourcesHidden.getValue();

        if (aList!=null && aList.length()>0)
            userList=aList.split(",");
        if (rList!=null && rList.length()>0)
            resourcesList=rList.split(",") ;

        //return super.onValidate(event);
        // get from super
        try
        {
            String buttonClicked = findButtonClicked(evt);
            if(submitButton.getAbsoluteName().equals(buttonClicked)){
               /* super.setPrefix(Task.class.getName());*/

                TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
                CalendarModule cm = (CalendarModule)Application.getInstance().getModule(CalendarModule.class);
                if(!edit){
                    task = new Task();
                    task = assembleTask(task);
                    task.setCompleted(false);
                    if(uploadForm.getFolderId()==null||uploadForm.getFolderId().trim().length()<1)
                    {
                        taskId = generateTaskId();
                    }
                    else
                    {
                        taskId = Task.class.getName() + "_" + uploadForm.getGeneratedId();
                        setGeneratedTaskId(taskId);
                    }
                    task.setEventId(taskId);
                    task.setId(taskId);
                    cm.addCalendarEvent(Task.class.getName(),task,task.getUserId(),ignoreConflict);
                    tm.addTask(task);
					User user = getWidgetManager().getUser();
					Log.getLog(getClass()).write(new Date(), taskId, user.getId(), "kacang.services.log.task.CreateTask", "New task " + task.getTitle() + " created by user "+ user.getName(), evt.getRequest().getRemoteAddr(), evt.getRequest().getSession().getId());
                    taskId = null;
                  //  ResourceMailer.sendResourceApprovalMail(getWidgetManager().getUser().getId(),task,evt);
                    if(notifyMemo.isChecked()||notifyEmail.isChecked())
                        sendNotification(task,true,evt);
 //                   if (notifySms.isChecked())
 //                       sendSMSNotification(task,true,evt);
                    if(resources.getRightValues().size()>0)
                    {
                        evt.getRequest().getSession().setAttribute("resources",resources.getRightValues());
                        evt.getRequest().getSession().setAttribute("EDIT",Boolean.FALSE);
                        init();
                        return new Forward(FORWARD_ADD_RESOURCES);
                    }
                    else if (resourcesList!=null&&resourcesList.length>0) { // add in for mobile
                        Map map = new SequencedHashMap();
                        for (int i=0;i<resourcesList.length;i++)
                            map.put(resourcesList[i],resourcesList[i]);
                        evt.getRequest().getSession().setAttribute("resources",map);
                        evt.getRequest().getSession().setAttribute("EDIT",Boolean.FALSE);
                        init();
                        return new Forward(FORWARD_ADD_RESOURCES);
                    }
                    else{
                        init();
                        return new Forward(FORWARD_TASK_ADD);
                    }
                } else{
                    task = tm.getTask(taskId);

                    //delete previously booked resources
                    ResourceManager rm = (ResourceManager) Application.getInstance().getModule(ResourceManager.class);
                    if(resources.getRightValues().size()>0){

                        if(task.getResources()!=null&&task.getResources().size()>0){
                            for (Iterator iterator = task.getResources().iterator(); iterator.hasNext();)
                            {
                                Resource resource = (Resource) iterator.next();
                                if(!resources.getRightValues().containsKey(resource.getId())){
                                    rm.deleteBooking(resource.getId(),taskId,CalendarModule.DEFAULT_INSTANCE_ID);
                                }
                            }
                        }
                    }
                    else if (resourcesList!=null && resourcesList.length>0) {
                        if(task.getResources()!=null&&task.getResources().size()>0){
                            for (Iterator iterator = task.getResources().iterator(); iterator.hasNext();)
                            {
                                Resource resource = (Resource) iterator.next();
                                for (int i=0;i<resourcesList.length;i++) {
                                    if(resourcesList[i].equals(resource.getId())){
                                        rm.deleteBooking(resource.getId(),taskId,CalendarModule.DEFAULT_INSTANCE_ID);
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    task = assembleTask(task);
                    task.setEventId(taskId);
                    task.setId(taskId);
                    cm.updateCalendarEvent(task,getWidgetManager().getUser().getId(),ignoreConflict);
                    tm.updateTask(task);
					User user = getWidgetManager().getUser();
					Log.getLog(getClass()).write(new Date(), taskId, user.getId(), "kacang.services.log.task.UpdateTask", "Task " + task.getTitle() + " updated by user "+ user.getName(), evt.getRequest().getRemoteAddr(), evt.getRequest().getSession().getId());
                    tm.clearTaskReassignments(taskId);
                 //   ResourceMailer.sendResourceApprovalMail(getWidgetManager().getUser().getId(),task,evt);
                    if(notifyMemo.isChecked()||notifyEmail.isChecked())
                        sendNotification(task,false,evt);
                    // add for send sms
//                    if (notifySms.isChecked())
//                        sendSMSNotification(task,false,evt);

                    if((resources!=null&&resources.getRightValues().size()>0) || (resourcesList!=null&&resourcesList.length>0))
                    {
                        if (resources!=null && resources.getRightValues().size()>0)
                            evt.getRequest().getSession().setAttribute("resources",resources.getRightValues());
                        else
                            evt.getRequest().getSession().setAttribute("resources",resourcesList);
                        evt.getRequest().getSession().setAttribute("EDIT",Boolean.TRUE);
                        init();
                        return new Forward(FORWARD_ADD_RESOURCES);
                    }else{
                        rm.deleteBooking(taskId);
                        init();
                        return new Forward(FORWARD_TASK_UPDATED);
                    }
                }
            } else if(attachFilesButton.getAbsoluteName().equals(buttonClicked)){
                uploadFile = true;
            } else if(cancelButton.getAbsoluteName().equals(buttonClicked))
            {
                return new Forward(FORWARD_TASK_CANCEL);
            }
            else if(assigneeButton.getAbsoluteName().equals(buttonClicked)) {
                return new Forward(FORWARD_ASSIGNEE);
            }
        }catch(ConflictException e){
            taskId = null;
            init();
            evt.getRequest().getSession().setAttribute("conflict",e);
            evt.getRequest().getSession().setAttribute("event",task);
            evt.getRequest().getSession().setAttribute("recurringEvents",null);
            evt.getRequest().getSession().setAttribute("memo",notifyMemo.isChecked()?Boolean.TRUE:Boolean.FALSE);
            evt.getRequest().getSession().setAttribute("email",notifyEmail.isChecked()?Boolean.TRUE:Boolean.FALSE);
            if(notifyNote.getValue()!=null)
                evt.getRequest().getSession().setAttribute("notes",notifyNote.getValue().toString());
            return new Forward("conflict exception");
        }
        catch(Exception e){
            taskId = null;
            edit = false;
            Log.getLog(getClass()).error("Error creating/updating task", e);
        }

        return null;

    }

    protected Task assembleTask(Task task){
        User assigner = getWidgetManager().getUser();
        task.setDescription(description.getValue().toString());
        Calendar dueCal = dueDate.getCalendar();
        //dueCal.set(Calendar.HOUR,dueTime.getHour());
        //dueCal.set(Calendar.MINUTE,dueTime.getMinute());
        task.setDueDate(dueCal.getTime());
        Calendar startCal = startDate.getCalendar();
        //startCal.set(Calendar.HOUR,startTime.getHour());
        //startCal.set(Calendar.MINUTE,startTime.getMinute());
        task.setStartDate(startCal.getTime());
        task.setDescription(description.getValue().toString());
        task.setUserId(assigner.getId());
        task.setAssigner(assigner.getName());
        task.setAssignerId(assigner.getId());
        task.setCategoryId((String)categories.getSelectedOptions().keySet().iterator().next());
//        task.setSynchronizationPriority((String)synchronizationPriority.getSelectedOptions().keySet().iterator().next());
//        task.setLocation((String) location.getValue().toString());
        if(reminderRadio.isChecked()){
            /*int rq = Integer.parseInt((String)reminderQuantity.getValue());
            int rm = Integer.parseInt((String)reminderModifier.getSelectedOptions().keySet().iterator().next());
            Calendar reminderCal = Calendar.getInstance();
            reminderCal.setTime(task.getDueDate());
            reminderCal.add(rm==1?Calendar.DAY_OF_MONTH:Calendar.WEEK_OF_YEAR,-rq);
            task.setReminderDate(reminderCal.getTime());*/
            task.setReminderDate(reminderDate.getDate());
        }
        if(reassignBG.getSelectedButton().equals(reassignYes.getAbsoluteName()))
            task.setReassign(true);
        else
            task.setReassign(false);
        if (radioPublic.isChecked())
            task.setClassification(CalendarModule.CLASSIFICATION_PUBLIC);
        else if (radioPrivate.isChecked())
            task.setClassification(CalendarModule.CLASSIFICATION_PRIVATE);
/*
        else if (radioConfidential.isChecked())
            task.setClassification(CalendarModule.CLASSIFICATION_CONFIDENTIAL);
*/
        task.setCreationDate(new Date());
        Calendar endCal = dueDate.getCalendar();
        //Calendar endTimeCal = dueTime.getCalendar();
        //endCal.set(Calendar.HOUR_OF_DAY, endTimeCal.get(Calendar.HOUR_OF_DAY));
        //endCal.set(Calendar.MINUTE, endTimeCal.get(Calendar.MINUTE));
        task.setEndDate(endCal.getTime());
        task.setTitle(title.getValue().toString());

        ResourceManager rm ;
        Map selectedResources = resources.getRightValues();
        Collection resourcesCol = new ArrayList();

        if(selectedResources!=null && selectedResources.size()>0){
            rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);
            for(Iterator i = selectedResources.keySet().iterator();i.hasNext();){
                Resource resource = null;
                try
                {
                    resource = rm.getResource((String)i.next(),true);
                } catch (DaoException e)
                {
                    Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                }
                resourcesCol.add(resource);
            }
        }
//        else if (resourcesList!=null && resourcesList.length>0) {
//            rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);
//            for(int i=0;i<resourcesList.length;i++){
//                Resource resource = null;
//                try
//                {
//                    resource = rm.getResource(resourcesList[i],true);
//                } catch (DaoException e)
//                {
//                    Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
//                }
//                resourcesCol.add(resource);
//            }
//        }
        task.setResources(resourcesCol);

        Collection attendeeList = new TreeSet();
        //setting to attendees
        Collection list = new ArrayList();
        for (Iterator i = assigneeMap.keySet().iterator(); i.hasNext();)
        {
            String key = (String) i.next();
            list.add(((User) assigneeMap.get(key)).getId());
        }
        String[] assigneeIds = (String[]) list.toArray(new String[] {});
        if (assigneeIds != null && assigneeIds.length>0) {
            Collection col = task.getAttendees();
            Map map = new HashMap();
            if(col!=null){
                for (Iterator iterator = col.iterator(); iterator.hasNext();) {
                    Attendee attendee = (Attendee) iterator.next();
                    map.put(attendee.getUserId(), attendee);
                }
            }
            for (int i=0; i<assigneeIds.length; i++) {
                try {
                    String uid = assigneeIds[i];
                   // boolean included = false;
                    if(map.get(uid)==null){
                        User tmpUser = UserUtil.getUser(uid);
                        Assignee att = new Assignee();
                        att.setUserId(tmpUser.getId());
                        att.setProperty("username", tmpUser.getUsername());
                        att.setProperty("firstName", tmpUser.getProperty("firstName"));
                        att.setProperty("lastName", tmpUser.getProperty("lastName"));
                        att.setCompulsory(true);
                        att.setStatus(CalendarModule.ATTENDEE_STATUS_CONFIRMED);
                        att.setProgress(0);
                        att.setTaskStatus(Assignee.TASK_STATUS_NOT_STARTED);
                        attendeeList.add(att);
                    } else {
                        attendeeList.add(map.get(uid));
                    }
                }
                catch(Exception e) {
                    throw new RuntimeException(e.toString());
                }
            }
        }
        else if (userList!=null && userList.length>0) {
            Collection col = task.getAttendees();
            Map map = new HashMap();
            if(col!=null){
                for (Iterator iterator = col.iterator(); iterator.hasNext();) {
                    Attendee attendee = (Attendee) iterator.next();
                    map.put(attendee.getUserId(), attendee);
                }
            }
            for (int i=0; i<userList.length; i++) {
                try {
                    String uid = userList[i];
                   // boolean included = false;
                    if(map.get(uid)==null){
                        User tmpUser = UserUtil.getUser(uid);
                        Assignee att = new Assignee();
                        att.setUserId(tmpUser.getId());
                        att.setProperty("username", tmpUser.getUsername());
                        att.setProperty("firstName", tmpUser.getProperty("firstName"));
                        att.setProperty("lastName", tmpUser.getProperty("lastName"));
                        att.setCompulsory(true);
                        att.setStatus(CalendarModule.ATTENDEE_STATUS_CONFIRMED);
                        att.setProgress(0);
                        att.setTaskStatus(Assignee.TASK_STATUS_NOT_STARTED);
                        attendeeList.add(att);
                    } else {
                        attendeeList.add(map.get(uid));
                    }
                }
                catch(Exception e) {
                    throw new RuntimeException(e.toString());
                }
            }
        }
        else {
            User user = getWidgetManager().getUser();
            Assignee att = new Assignee();
            att.setUserId(user.getId());
            att.setProperty("username", user.getUsername());
            att.setProperty("firstName", user.getProperty("firstName"));
            att.setProperty("lastName", user.getProperty("lastName"));
            att.setStatus(CalendarModule.ATTENDEE_STATUS_CONFIRMED);
            att.setProgress(0);
            att.setTaskStatus(Assignee.TASK_STATUS_NOT_STARTED);
            attendeeList.add(att);
        }

        Collection col = attendeeList;
        task.setAttendees(col);
        if(task.isCompleted())
        {
            boolean completed = true;
            for (Iterator iterator = attendeeList.iterator(); iterator.hasNext();) {
                Assignee assignee = (Assignee) iterator.next();
                if(!((""+Assignee.TASK_STATUS_COMPLETED).equals(assignee.getStatus())))
                {
                    completed = false;
                    break;
                }
            }
            if(!completed){
                task.setCompleted(false);
                task.setCompleteDate(null);
            }
        }
      /*  Map selectedResources = resources.getSelectedOptions();
        if(selectedResources.size()>0){
            rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);
            Collection resourcesCol = new ArrayList();
            for(Iterator i = selectedResources.keySet().iterator();i.hasNext();){
                Resource resource = rm.getResource((String)i.next(),true);
                resourcesCol.add(resource);
            }
            task.setResources(resourcesCol);
        }*/
        return task;
    }
    

    public Hidden getUserHidden() {
        return userHidden;
    }

    public void setUserHidden(Hidden userHidden) {
        this.userHidden = userHidden;
    }

    public Hidden getResourcesHidden() {
        return resourcesHidden;
    }

    public void setResourcesHidden(Hidden resourcesHidden) {
        this.resourcesHidden = resourcesHidden;
    }

    public String[] getResourcesList()
    {
        return resourcesList;
    }

    public void setResourcesList(String[] resourcesList)
    {
        this.resourcesList = resourcesList;
    }

    public String[] getUserList()
    {
        return userList;
    }

    public void setUserList(String[] userList)
    {
        this.userList = userList;
    }

    public Button getAssigneeButton()
    {
        return assigneeButton;
    }

    public void setAssigneeButton(Button assigneeButton)
    {
        this.assigneeButton = assigneeButton;
    }

    public Map getAssigneeMap()
    {
        return assigneeMap;
    }

    public void setAssigneeMap(Map assigneeMap)
    {
        this.assigneeMap = assigneeMap;
    }

    public boolean isPopulated()
    {
        return populated;
    }

    public void setPopulated(boolean populated)
    {
        this.populated = populated;
    }
}
