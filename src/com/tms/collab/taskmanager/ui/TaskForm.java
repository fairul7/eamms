package com.tms.collab.taskmanager.ui;

import kacang.stdui.*;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;
import kacang.model.DaoException;
import kacang.util.UuidGenerator;
import kacang.util.Log;
import kacang.services.security.User;
import kacang.services.security.SecurityService;
import kacang.services.security.SecurityException;
import com.tms.collab.calendar.ui.UserUtil;
import com.tms.collab.calendar.ui.CalendarEventView;
import com.tms.collab.calendar.ui.CalendarUsersSelectBox;
import com.tms.collab.calendar.model.*;
import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.taskmanager.model.TaskManager;
import com.tms.collab.taskmanager.model.TaskCategory;
import com.tms.collab.taskmanager.model.Assignee;
import com.tms.collab.project.WormsHandler;
import com.tms.collab.resourcemanager.model.ResourceManager;
import com.tms.collab.resourcemanager.model.Resource;
import com.tms.collab.messaging.model.*;
import com.tms.util.FormatUtil;

import java.util.*;
import java.text.SimpleDateFormat;

import org.apache.commons.collections.SequencedHashMap;

public class TaskForm extends Form implements CalendaringPermission
{
    public static final String DEFAULT_TEMPLATE = "taskmanager/taskform";
    public static final String DEFAULT_TEMPLATE_UPLOAD = "taskmanager/taskuploadfileform";
    public static final String FORWARD_TASK_ADD = "task added";
    public static final String FORWARD_TASK_UPDATED = "task updated";
    public static final String FORWARD_TASK_CANCEL = "cancel";
    public static final String FORWARD_ADD_RESOURCES = "add Resources";
    protected FileUploadForm uploadForm;
    protected FileListing fileListing;
    protected String taskId = null;
    protected DateField dueDate;
    protected TimeField dueTime;
    protected DateField startDate;
    protected TimeField startTime;
    protected ButtonGroup reassignBG;
    protected Radio reassignYes;
    protected Radio reassignNo;
    protected CalendarUsersSelectBox assignees;
    protected SelectBox categories;
    protected TextField title;
    protected TextBox description;
    protected CheckBox reminderRadio;
    /*protected SelectBox reminderModifier;
    protected TextField reminderQuantity;*/
    protected DateField reminderDate;
    protected TimeField reminderTime;
    protected ComboSelectBox resources;
    protected CheckBox notifyMemo;
    protected CheckBox notifyEmail;
    protected TextBox notifyNote;
    protected Radio radioPublic;
    protected Radio radioPrivate;
    protected boolean ignoreConflict = true;

    // 20050713 add for task priority
    protected SelectBox taskPriority;

    //20060614 add for task estimation
    protected SelectBox estimationType;
    protected TextField estimation;
    protected ValidatorIsNumeric validEstimation;

/*
protected Radio radioConfidential;
*/
    protected boolean edit = false;
    protected boolean uploadFile = false;
    protected Button attachFilesButton;
    protected Button submitButton,cancelButton;
    protected Button resetButton;
    protected String viewUrl = null;
    protected String taskProjectId = null;
    protected Task task = null;
    public TaskForm()
    {
    }

    public TaskForm(String name)
    {
        super(name);
    }

    public void init()
    {
        super.init();
        setMethod("POST");

        title = new TextField("title");
        dueDate = new DatePopupField("dueDate");
        Calendar TodayDate = Calendar.getInstance();
        dueDate.setDate(TodayDate.getTime());
        dueDate.setTemplate("calendar/taskDueDatePopupField");
        dueTime = new TimeField("dueTime");
        startDate = new DatePopupField("startDate");
        startDate.setDate(TodayDate.getTime());
        startDate.setTemplate("calendar/taskDatePopupField");
        startTime = new TimeField(("startTime"));
        reassignNo = new Radio("reassignNo",Application.getInstance().getMessage("taskmanager.label.No","No"));
        reassignNo.setChecked(true);
        reassignYes = new Radio("reassignYes",Application.getInstance().getMessage("taskmanager.label.Yes","Yes"));
        reassignBG = new ButtonGroup("reassignBG",new Radio[]{reassignYes,reassignNo});

        assignees = new CalendarUsersSelectBox("assignees");
        addChild(assignees);
        assignees.init();

        uploadForm = new FileUploadForm("fileupload");
        uploadForm.setFolderIdPrefix("/"+TaskManager.TASKMANAGER_FILES_FOLDER+"/" + Task.class.getName());
        uploadForm.init();
        description = new TextBox("description");
        ValidatorNotEmpty validator = new ValidatorNotEmpty("validator");
        description.addChild(validator);
        //title.addChild(new ValidatorNotEmpty("titleValidator"));
        reminderRadio = new CheckBox("reminderRadio");
        /*reminderModifier = new SelectBox("reminderModifier");*/
        reminderDate = new DateField("reminderDate");
        reminderTime = new TimeField("reminderTime");
        radioPublic = new Radio("radioPublic");
        radioPublic.setValue(CalendarModule.CLASSIFICATION_PUBLIC);
        radioPublic.setGroupName("classification");
        radioPublic.setChecked(true);
        radioPrivate = new Radio("radioPrivate");
        radioPrivate.setValue(CalendarModule.CLASSIFICATION_PRIVATE);
        radioPrivate.setGroupName("classification");
/*
radioConfidential = new Radio("radioConfidential");
radioConfidential.setValue(CalendarModule.CLASSIFICATION_CONFIDENTIAL);
radioConfidential.setGroupName("classification");
*/
        resources = new ComboSelectBox("resoruces");
        addChild(resources);
        resources.init();
        addChild(uploadForm);
        Map resourcesMap = new SequencedHashMap();
        ResourceManager rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);
        try{
            Collection col = rm.getResources(getWidgetManager().getUser().getId(),true);
            for(Iterator i=col.iterator();i.hasNext();){
                Resource resource = (Resource)i.next();
                resourcesMap.put(resource.getId(),resource.getName());
            }
            /*resources = new SelectBox("resoruces");
            resources.setMultiple(true);
            resources.setOptionMap(resourcesMap);
            addChild(resources);*/
            /*reminderModifier.addOption("1",Application.getInstance().getMessage("taskmanager.label.days","day(s)"));
            reminderModifier.addOption("2",Application.getInstance().getMessage("taskmanager.label.weeks","week(s)"));
            reminderQuantity = new TextField("reminderQuantity");
            reminderQuantity.setValue("1");
            reminderQuantity.setMaxlength("2");
            reminderQuantity.setSize("2");*/
            attachFilesButton = new Button("attachbutton",Application.getInstance().getMessage("taskmanager.label.AttachFiles","Attach Files"));
            addChild(attachFilesButton);
            categories = new SelectBox("categories");
            categories.setMultiple(false);
            /*Map catMap = new SequencedHashMap();
            TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
            col = tm.getUserTaskCategories(getWidgetManager().getUser().getId(),true,"name",true,0,-1);
            for(Iterator i = col.iterator();i.hasNext();){
                TaskCategory cat = (TaskCategory)i.next();
                if(cat.getProperty("projectId")==null){
                	catMap.put(cat.getId(),cat.getName());	
                }else{
                	if("0".equals(cat.getProperty("archived")))
                		catMap.put(cat.getId(),"[Project]"+cat.getName());
                }
            }
            categories.setOptionMap(catMap);*/
            
            // addon for task priority
            taskPriority = new SelectBox("taskPriority");
            Map priorityMap = new SequencedHashMap();
            String sPriority = Application.getInstance().getProperty("com.tms.collab.taskmanager.taskpriority");
            String[] array = sPriority.split(",");
            for (int i=0;i<array.length;i++) {
                String[] sLabel = array[i].split("-");
                priorityMap.put(sLabel[0],sLabel[1]);
            }
            taskPriority.setOptionMap(priorityMap);
            taskPriority.setSelectedOption(Application.getInstance().getProperty("com.tms.collab.taskmanager.defaultpriority"));

            fileListing = new FileListing("filelisting");
            fileListing.init();
            fileListing.setDeleteable(true);
            addChild(startDate);
            addChild(startTime);
            addChild(title);
            addChild(fileListing);
            addChild(categories);
            addChild(taskPriority);
            addChild(dueDate);
            addChild(dueTime);
            addChild(reassignNo);
            addChild(reassignYes);
        }
        catch (Exception e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }

        notifyMemo = new CheckBox("notifyMemo");
        notifyEmail = new CheckBox("notifyEmail");
        notifyNote = new TextBox("notifyNote");
        notifyMemo.setChecked(true);
        submitButton = new Button("submitButton");
        submitButton.setText(Application.getInstance().getMessage("taskmanager.label.Submit","Submit"));
        resetButton = new Button("resetButton");
        resetButton.setText(Application.getInstance().getMessage("taskmanager.label.Reset","Reset"));
        cancelButton = new Button("cancelButton");
        cancelButton.setText(Application.getInstance().getMessage("taskmanager.label.Cancel","Cancel"));
        addChild(cancelButton);
        addChild(reminderRadio);
        /*addChild(reminderModifier);
        addChild(reminderQuantity);*/
        addChild(reminderDate);
        addChild(reminderTime);
        addChild(description);
        addChild(radioPublic);
        addChild(radioPrivate);
/*
addChild(radioConfidential);
*/
        //addChild(resources);
        addChild(notifyMemo);
        addChild(notifyEmail);
        addChild(notifyNote);
        addChild(submitButton);
        addChild(resetButton);
        /*if(taskId!=null&&taskId.trim().length()>0){
        //init();
        pupulateForm();
        fileListing.setFolderId(taskId);
        fileListing.refresh();
        uploadForm.setFolderId(taskId);
        //uploadForm.
        edit = true;
        }*/
        
        //New
        estimationType = new SelectBox("estimationType");
        estimationType.setOptions("Mandays=Mandays;Manhours=Manhours");
        estimationType.setOnChange("change(this)");
        addChild(estimationType);
        validEstimation = new ValidatorIsNumeric("validEstimation","",true);
        estimation = new TextField("estimation");
        estimation.setMaxlength("8");
        estimation.setSize("10");
        estimation.setValue("1");
        estimation.addChild(validEstimation);

        addChild(estimation);

    }

//    public Forward actionPerformed(Event evt)
//    {
//        String buttonClicked = findButtonClicked(evt);
//        if(attachFilesButton.getAbsoluteName().equals(buttonClicked)){
//            onSubmit(evt);
//            uploadFile = true;
//            return null;
//        }
//        String event = evt.getRequest().getParameter("et");
//        if("done".equals(event)){
//            uploadFile = false;
//            String folderId = uploadForm.getFolderId();
//            if(folderId!=null&&folderId.trim().length()>0){
//                fileListing.setFolderId(folderId);
//                fileListing.refresh();
//            }
//            description.setInvalid(false);
//            return null;
//        }
//        return super.actionPerformed(evt);
//    }

    public void onRequest(Event event)
    {
        /*  */
        /* init();
        eventId = event.getRequest().getParameter("eventId");*/
        viewUrl = event.getRequest().getParameter("viewUrl");
        if(getParent() instanceof CalendarEventView){
            taskId = ((CalendarEventView)getParent()).getEventId();

        }
        if(taskId!=null&& taskId.trim().length()>0)
            init();

        Map resourcesMap = new SequencedHashMap();
        ResourceManager rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);
        try{
            Collection col = rm.getResources(getWidgetManager().getUser().getId(),true);
            for(Iterator i=col.iterator();i.hasNext();){
                Resource resource = (Resource)i.next();
                resourcesMap.put(resource.getId(),resource.getName());
            }
        }catch(Exception e){
            Log.getLog(getClass()).error(e);
        }
        resources.setLeftValues(resourcesMap);
        TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
        try
        {
            Collection col = tm.getUserTaskCategories(getWidgetManager().getUser().getId(),true,"name",true,0,-1);
/*            TreeSet set = new TreeSet();
            set.addAll(col);*/
            SequencedHashMap map = new SequencedHashMap();
            categories.removeAllOptions();
            for (Iterator iterator = col.iterator(); iterator.hasNext();) {
                TaskCategory taskCategory = (TaskCategory) iterator.next();
                if(taskCategory.getProperty("projectId")==null){
                	map.put(taskCategory.getId(),taskCategory.getName());	
                }/*else{
                	if("0".equals(taskCategory.getProperty("archived")))
                		map.put(taskCategory.getId(),"[Project]"+taskCategory.getName());
                }*/
            }
            //categories.set
            categories.setOptionMap(map);
            categories.setSelectedOption(TaskManager.DEFAULT_CATEGORY_ID);
        } catch (DaoException e)
        {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }

        if(taskId!=null&& taskId.trim().length()>0){
            pupulateForm();
            uploadForm.setFolderId("/"+TaskManager.TASKMANAGER_FILES_FOLDER+"/"+taskId);
            fileListing.setFolderId("/"+TaskManager.TASKMANAGER_FILES_FOLDER+"/"+taskId);
            fileListing.refresh();
            edit = true;
        }

        /*   if(eventId!=null&&eventId.trim().length()>0){
        taskId = eventId;
        populateForm();
        uploadForm.setFolderId(taskId);
        edit = true;
        }*/

    }

    private void pupulateForm(){
        try
        {
            TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
            WormsHandler worms = (WormsHandler)Application.getInstance().getModule(WormsHandler.class);
            Task task = tm.getTask(taskId);
            taskProjectId=worms.getTaskProjectID(taskId);
            title.setValue(task.getTitle());
            startDate.setDate(task.getStartDate()==null?task.getCreationDate():task.getStartDate());
            startTime.setDate(task.getStartDate()==null?task.getCreationDate():task.getStartDate());
            dueDate.setDate(task.getDueDate());
            dueTime.setDate(task.getDueDate());
            estimation.setValue(Double.toString(task.getEstimation()));
            estimationType.setSelectedOption(task.getEstimationType());
            SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
            if(ss.hasPermission(getWidgetManager().getUser().getId(),TaskManager.PERMISSION_MANAGETASK,null,null)){
                Map catMap = new SequencedHashMap();
                Collection col = tm.getAllCategories();
                categories.removeAllOptions();
                for(Iterator i = col.iterator();i.hasNext();){
                    TaskCategory cat = (TaskCategory)i.next();
                    if(cat.getProperty("projectId")==null){
                    	catMap.put(cat.getId(),cat.getName());	
                    }/*else{
                    	if("0".equals(cat.getProperty("archived")))
                    		catMap.put(cat.getId(),"[Project]"+cat.getName());
                    }*/
                }
                categories.setOptionMap(catMap);
            }
            categories.setSelectedOptions(new String[]{task.getCategoryId()});
            if (task.getTaskPriority()!=null && !task.getTaskPriority().equals(""))
                taskPriority.setSelectedOptions(new String[]{task.getTaskPriority()});
            else
                taskPriority.setSelectedOptions(new String[]{Application.getInstance().getProperty("com.tms.collab.taskmanager.defaultpriority")});
            description.setValue(task.getDescription());
            if(task.isReassign()){
            	reassignYes.setChecked(true);
            	reassignNo.setChecked(false);
            }else{
            	reassignNo.setChecked(true);
            	reassignYes.setChecked(false);
            }
               
            String c = task.getClassification();
            if(CalendarModule.CLASSIFICATION_PUBLIC.equals(c)){
                radioPublic.setChecked(true);
                radioPrivate.setChecked(false);
            }else if(CalendarModule.CLASSIFICATION_PRIVATE.equals(c)){
                radioPrivate.setChecked(true);
                radioPublic.setChecked(false);
/*
}else if(CalendarModule.CLASSIFICATION_CONFIDENTIAL.equals(c)){
radioConfidential.setChecked(true);
*/
            }
            if (task.getReminderDate() != null) {
                reminderRadio.setChecked(true);
                //Calendar cal = Calendar.getInstance();
                //cal.setTime(event.getStartDate());
                /*long t = task.getStartDate().getTime() - task.getReminderDate().getTime();
                float days = (float) t / 1000 / 60 / 60 / 24;
                if (days < 1) {
                    float hours = days * 24;
                    if (hours == (int)hours) {
                        reminderModifier.setSelectedOptions(new String[]{ new Integer(Calendar.HOUR_OF_DAY).toString() });
                        reminderQuantity.setValue("" + (int)hours);
                    }
                    else {
                        reminderModifier.setSelectedOptions(new String[]{ new Integer(Calendar.MINUTE).toString() });
                        reminderQuantity.setValue("" + (int)(hours * 60));
                    }
                }
                else if ((int)days % 7 == 0) {
                    reminderModifier.setSelectedOptions(new String[]{ new Integer(Calendar.WEEK_OF_YEAR).toString() });
                    reminderQuantity.setValue("" + (int)(days / 7));
                }
                else {
                    reminderModifier.setSelectedOptions(new String[]{ new Integer(Calendar.DAY_OF_MONTH).toString() });
                    reminderQuantity.setValue("" + (int)days);
                }*/
                reminderDate.setDate(task.getReminderDate());
                reminderTime.setDate(task.getReminderDate());
            }


            Collection resources = task.getResources();
            Map rrmap = new HashMap();
            Map rlmap = this.resources.getLeftValues();
            for (Iterator iterator = resources.iterator(); iterator.hasNext();)
            {
                Resource resource = (Resource) iterator.next();
                rrmap.put(resource.getId(),resource.getName());
                rlmap.remove(resource.getId());
            }
            this.resources.setLeftValues(rlmap);
            this.resources.setRightValues(rrmap); Collection col = task.getAttendees();
            if(col!=null){
                Attendee att;
                Collection attendeeList = new ArrayList();
                for(Iterator i= col.iterator();i.hasNext();){
                    att = (Attendee)i.next();
                    attendeeList.add(att.getUserId());
                }
                String[] attendeeIds = (String[])attendeeList.toArray(new String[0]);
                assignees.setIds(attendeeIds);
            }
            /*col = task.getResources();
            if(col!=null){
            Resource resource;
            for(Iterator i = col.iterator();i.hasNext();){
            resource = (Resource)i.next();
            resources.addSelectedOption(resource.getId());
            }
            }*/
        } catch (SecurityException e)
        {
            Log.getLog(getClass()).error(e);
        } catch (Exception e){
            Log.getLog(getClass()).error(e);
        }
    }

    public String getDefaultTemplate()
    {
        if(uploadFile)
        {
            fileListing.setDeleteable(true);
            return DEFAULT_TEMPLATE_UPLOAD;
        }
        else
        {
            fileListing.setDeleteable(false);
            return DEFAULT_TEMPLATE;
        }
    }

    public DateField getDueDate()
    {
        return dueDate;
    }

    public void setDueDate(DateField dueDate)
    {
        this.dueDate = dueDate;
    }

    public TimeField getDueTime()
    {
        return dueTime;
    }

    public void setDueTime(TimeField dueTime)
    {
        this.dueTime = dueTime;
    }

    public Radio getReassignYes()
    {
        return reassignYes;
    }

    public void setReassignYes(Radio reassignYes)
    {
        this.reassignYes = reassignYes;
    }

    public Radio getReassignNo()
    {
        return reassignNo;
    }

    public void setReassignNo(Radio reassignNo)
    {
        this.reassignNo = reassignNo;
    }

    public CalendarUsersSelectBox getAssignees()
    {
        return assignees;
    }

    public void setAssignees(CalendarUsersSelectBox assignees)
    {
        this.assignees = assignees;
    }

    public SelectBox getCategories()
    {
        return categories;
    }

    public void setCategories(SelectBox categories)
    {
        this.categories = categories;
    }
    public kacang.ui.Forward onSubmit(kacang.ui.Event evt) {
        String buttonName = findButtonClicked(evt);
        if (resetButton.getAbsoluteName().equals(buttonName)) {
            init();
            kacang.ui.Forward forward = new kacang.ui.Forward();
            forward.setName(Form.CANCEL_FORM_ACTION);
            return forward;
        } else if(cancelButton.getAbsoluteName().equals(buttonName)){
            return new Forward("cancel");
        }
        if(!"done".equals(evt.getType())){
            Forward result = super.onSubmit(evt);
            if(submitButton.getAbsoluteName().equals(buttonName) && startDate.getDate() != null && dueDate.getDate() != null){
                Date sDate = startDate.getDate();
                Date eDate = dueDate.getDate();
                if (sDate.after(eDate)) {
                    startDate.setInvalid(true);
                    dueDate.setInvalid(true);
                    setInvalid(true);
                } else if(sDate.equals(eDate)&&startTime.getDate().after(dueTime.getDate())){
                    startTime.setInvalid(true);
                    dueTime.setInvalid(true);
                    setInvalid(true);   
                }
            	if (title.getValue() == null || ((String)title.getValue()).trim().length() <= 0) {
            		title.setInvalid(true);
            		this.setInvalid(true);
            	}
            }
            return result;

        }else{

            description.setInvalid(false);
        }
        return null;
    }


    public Forward onValidate(Event evt)
    {
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
                    if(resources.getRightValues().size()>0)
                    {
                        evt.getRequest().getSession().setAttribute("resources",resources.getRightValues());
                        evt.getRequest().getSession().setAttribute("EDIT",Boolean.FALSE);
                        init();
                        return new Forward(FORWARD_ADD_RESOURCES);
                    }else{
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
                    if(resources.getRightValues().size()>0)
                    {
                        evt.getRequest().getSession().setAttribute("resources",resources.getRightValues());
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
            } else if("done".equals(evt.getType())) {
                uploadFile = false;
            } else if(cancelButton.getAbsoluteName().equals(buttonClicked))
                return new Forward(FORWARD_TASK_CANCEL);
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



    protected void sendNotification(Task task,boolean newTodo,Event evt){
        try{
            String category="";
            if (task.getCategory()==null || task.getCategory().equals("")) {
                TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
                TaskCategory cat = tm.getCategory(task.getCategoryId());
                category=cat.getName();
            }
            else{
                category=task.getCategory();
            }
            MessagingModule mm;
            User user;
            SmtpAccount smtpAccount;
            Message message;

            mm = Util.getMessagingModule();
            user = getWidgetManager().getUser();
            String userId = user.getId();
            smtpAccount = mm.getSmtpAccountByUserId(user.getId());

            // construct the message to send
            message = new Message();
            message.setMessageId(UuidGenerator.getInstance().getUuid());
            // setMessageProperties(message,event,newEvent,evt);
            IntranetAccount intranetAccount;

            Application app = Application.getInstance();
            intranetAccount = Util.getMessagingModule().getIntranetAccountByUserId(getWidgetManager().getUser().getId());
            message.setFrom(intranetAccount.getFromAddress());

            Collection col = task.getAttendees();
            List memoList,emailList;
            memoList = new ArrayList(col.size());
            emailList = new ArrayList(col.size());
            for(Iterator i=col.iterator();i.hasNext();){
                Attendee att = (Attendee)i.next();
                if(!userId.equals(att.getUserId())){
                    if(notifyMemo.isChecked()){
                        intranetAccount = mm.getIntranetAccountByUserId(att.getUserId());
                        if(intranetAccount!=null){
                            String add = intranetAccount.getIntranetUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
                            memoList.add(add);
                        }
                    }
                    if(notifyEmail.isChecked()){
                        User tempuser = UserUtil.getUser(att.getUserId());
                        String email = (String)tempuser.getProperty("email1");
                        emailList.add(email);
                    }
                }
            }

            if(memoList.size()>0)
                message.setToIntranetList(memoList);

            if(newTodo)
                message.setSubject(app.getMessage("taskmanager.label.newTask", "New Task") + ": " + task.getTitle());
            else
                message.setSubject(app.getMessage("taskmanager.label.updatedTask", "Updated Task") + ": " + task.getTitle());


            String temp = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">"+
                    "</head><body>" +
                    "<style>" +
                    ".contentBgColorMail {background-color: #FFFFFF; font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8.5pt}" +
                    //".contentFont {font-family: Arial, Helvetica, sans-serif; font-size: 8.5pt}"+
                    //".contentTitleFont {font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 7.5pt; color: #FFFFFF; background-color: #003366}"+
                    ".contentStrapColor {background-color: #E6E6CA; font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8.5pt}</style>"+
                    "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr valign=\"MIDDLE\">"+
                    "<td height=\"22\" bgcolor=\"#003366\" class=\"contentBgColorMail\"><b><font color=\"#000000\" class=\"contentBgColorMail\">"+
                    "<b><U>" + app.getMessage("taskmanager.label.mailTitle", "To Do Task Details") + "</U></b></font></b></td><td align=\"right\" bgcolor=\"#003366\" class=\"contentBgColorMail\">&nbsp;</td>"+
                    "</tr><tr><td colspan=\"2\" valign=\"TOP\" bgcolor=\"#EFEFEF\" class=\"contentBgColorMail\">&nbsp;</td>"+
                    "</tr><tr><td colspan=\"2\" valign=\"TOP\"><table width=\"100%\" border=\"0\" cellpadding=\"5\" cellspacing=\"1\">"+
                    "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>"+
                    "<b>" + app.getMessage("taskmanager.label.title", "Title") + "</b></strong></td><td class=\"contentBgColorMail\" width=\"90%\">"+ task.getTitle()+"</td></tr>"+
                    "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>"+
                    "<b>" + app.getMessage("taskmanager.label.category", "Category") + "</b></strong></td><td class=\"contentBgColorMail\" width=\"90%\">"+ category+"</td></tr>"+
                    "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>"+
                    "<b>" + app.getMessage("taskmanager.label.AssignedBy", "Assigned By") + "</b></strong></td><td class=\"contentBgColorMail\" width=\"90%\">"+ task.getUserName()+"</td></tr>"+
                    "<tr><td class=\"contentBgColorMail\" align=\"right\" valign=\"top\" nowrap><b>" + app.getMessage("taskmanager.label.dueDate", "Due Date") + "</b></td>"+
                    "<td class=\"contentBgColorMail\">" + new SimpleDateFormat(FormatUtil.getInstance().getLongDateTimeFormat()).format(task.getDueDate())+"</td>"+
                    "</tr><tr><td class=\"contentBgColorMail\" align=\"right\" valign=\"top\" nowrap><b>" + app.getMessage("taskmanager.label.description", "Description") + "</b></td><td class=\"contentBgColorMail\">"+
                    task.getDescription() + "</td></tr>";
            String notes = notifyNote.getValue().toString();
            if(notes!=null&&notes.trim().length()>0){
                temp += "<tr><td class=\"contentBgColorMail\" align=\"right\" valign=\"top\" nowrap><b>" + app.getMessage("taskmanager.label.notes", "Notes") + "</b></td>"+
                        "<td class=\"contentBgColorMail\">" + notes + "</td></tr>";

            }

            String footer = "</table></td></tr><tr><td colspan=\"2\" valign=\"TOP\" bgcolor=\"#CCCCCC\" class=\"contentBgColorMail\">&nbsp;</td>"+
                    "</tr></table><p>&nbsp; </p></body></html>";



            String link = "<tr><td></td><td class=\"contentBgColorMail\" align=\"left\" valign=\"top\"  nowrap><a href=\"http://"+ evt.getRequest().getServerName()+":"+evt.getRequest().getServerPort()+evt.getRequest().getContextPath()+
                    "/ekms/calendar/calendar.jsp?cn=calendarPage.calendarView&et=select&eventId="+task.getId()+"\">" + app.getMessage("taskmanager.label.clickToView", "Click here to view") + "</a>"+
                    "</td></tr>";
            message.setBody(temp+link+footer);


            message.setMessageFormat(Message.MESSAGE_FORMAT_HTML);
            message.setDate(new Date());

            if(notifyMemo.isChecked())
                mm.sendMessage(smtpAccount, message, user.getId(), false);

            if(notifyEmail.isChecked()&&emailList.size()>0){
                message.setToList(emailList);
                message.setToIntranetList(null);
                message.setBody(temp+link+footer);
                mm.sendMessage(smtpAccount, message, user.getId(), false);
            }
        }catch (Exception e){
            Log.getLog(getClass()).error(e);
        }

    }



    protected Task assembleTask(Task task){
        User assigner = getWidgetManager().getUser();
        task.setDescription(description.getValue().toString());
        Calendar dueCal = dueDate.getCalendar();
        dueCal.set(Calendar.HOUR,dueTime.getHour());
        dueCal.set(Calendar.MINUTE,dueTime.getMinute());
        task.setDueDate(dueCal.getTime());
        Calendar startCal = startDate.getCalendar();
        startCal.set(Calendar.HOUR,startTime.getHour());
        startCal.set(Calendar.MINUTE,startTime.getMinute());
        task.setStartDate(startCal.getTime());
        task.setDescription(description.getValue().toString());
        task.setUserId(assigner.getId());
        task.setAssigner(assigner.getName());
        task.setAssignerId(assigner.getId());
        task.setCategoryId((String)categories.getSelectedOptions().keySet().iterator().next());
        task.setEstimationType((String)estimationType.getSelectedOptions().keySet().iterator().next());
        double estimate=0;
        if("".equals(estimation.getValue()))
        	estimate=0;
        else
        	estimate=Double.parseDouble(estimation.getValue().toString());
        task.setEstimation(estimate);
		if(taskPriority.getSelectedOptions().size() <= 0)
			task.setTaskPriority(Application.getInstance().getProperty("com.tms.collab.taskmanager.defaultpriority"));
		else
			task.setTaskPriority((String)taskPriority.getSelectedOptions().keySet().iterator().next());
        if(reminderRadio.isChecked()){
            /*int rq = Integer.parseInt((String)reminderQuantity.getValue());
            int rm = Integer.parseInt((String)reminderModifier.getSelectedOptions().keySet().iterator().next());
            Calendar reminderCal = Calendar.getInstance();
            reminderCal.setTime(task.getStartDate());
            reminderCal.add(rm==1?Calendar.DAY_OF_MONTH:Calendar.WEEK_OF_YEAR,-rq);
            task.setReminderDate(reminderCal.getTime());*/
            Calendar cal = Calendar.getInstance();
            cal.setTime(reminderDate.getDate());
            cal.set(Calendar.HOUR_OF_DAY, reminderTime.getHour());
            cal.set(Calendar.MINUTE, reminderTime.getMinute());
            task.setReminderDate(cal.getTime());
        }
        //fix bug #2408 (after set the reminder, then cannot uncheck the remind)
        else{
        	task.setReminderDate(null);
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
        Calendar endTimeCal = dueTime.getCalendar();
        endCal.set(Calendar.HOUR_OF_DAY, endTimeCal.get(Calendar.HOUR_OF_DAY));
        endCal.set(Calendar.MINUTE, endTimeCal.get(Calendar.MINUTE));
        task.setEndDate(endCal.getTime());
        task.setTitle(title.getValue().toString());
/*
ResourceManager rm ;

ResourceManager rm ;
Map selectedResources = resources.getRightValues();
Collection resourcesCol = new ArrayList();

if(selectedResources.size()>0){
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
task.setResources(resourcesCol);
*/
        Collection attendeeList = new TreeSet();
        String[] assigneeIds = assignees.getIds();
        if (assigneeIds == null || assigneeIds.length==0) {
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
        } else {
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
        Collection col = attendeeList;
        task.setAttendees(col);
        if(task.isCompleted())
        {
            boolean completed = true;
            for (Iterator iterator = attendeeList.iterator(); iterator.hasNext();) {
                Assignee assignee = (Assignee) iterator.next();
                if(!((""+Assignee.TASK_STATUS_COMPLETED).equals(Long.toString(assignee.getTaskStatus()))))
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

    public String getTaskId()
    {
        return taskId;
    }

    public void setTaskId(String taskId)
    {
        if(!(taskId == null || "".equals(taskId)))
        {
            this.taskId = taskId;
            init();
            edit = true;
        }
        else
        {
            this.taskId = "";
            edit = false;
        }
    }

    public FileUploadForm getUploadForm()
    {
        return uploadForm;
    }

    public void setUploadForm(FileUploadForm uploadForm)
    {
        this.uploadForm = uploadForm;
    }

    public boolean isUploadFile()
    {
        return uploadFile;
    }

    public void setUploadFile(boolean uploadFile)
    {
        this.uploadFile = uploadFile;
    }

    public FileListing getFileListing()
    {
        return fileListing;
    }

    public void setFileListing(FileListing fileListing)
    {
        this.fileListing = fileListing;
    }

    public Button getAttachFilesButton()
    {
        return attachFilesButton;
    }

    public void setAttachFilesButton(Button attachFilesButton)
    {
        this.attachFilesButton = attachFilesButton;
    }

    public boolean hasEditPermission(String userId)
    {
        return false;
    }

    public boolean hasDeletePermission(String userId)
    {
        return false;
    }

    public ButtonGroup getReassignBG()
    {
        return reassignBG;
    }

    public void setReassignBG(ButtonGroup reassignBG)
    {
        this.reassignBG = reassignBG;
    }

    public TextBox getDescription()
    {
        return description;
    }

    public void setDescription(TextBox description)
    {
        this.description = description;
    }

    public CheckBox getReminderRadio()
    {
        return reminderRadio;
    }

    public void setReminderRadio(CheckBox reminderRadio)
    {
        this.reminderRadio = reminderRadio;
    }

    /*public SelectBox getReminderModifier()
    {
        return reminderModifier;
    }

    public void setReminderModifier(SelectBox reminderModifier)
    {
        this.reminderModifier = reminderModifier;
    }

    public TextField getReminderQuantity()
    {
        return reminderQuantity;
    }

    public void setReminderQuantity(TextField reminderQuantity)
    {
        this.reminderQuantity = reminderQuantity;
    }*/

    /* public SelectBox getResources()
    {
    return resources;
    }

    public void setResources(SelectBox resources)
    {
    this.resources = resources;
    }*/

    public CheckBox getNotifyMemo()
    {
        return notifyMemo;
    }

    public void setNotifyMemo(CheckBox notifyMemo)
    {
        this.notifyMemo = notifyMemo;
    }

    public CheckBox getNotifyEmail()
    {
        return notifyEmail;
    }

    public void setNotifyEmail(CheckBox notifyEmail)
    {
        this.notifyEmail = notifyEmail;
    }



    public TextBox getNotifyNote()
    {
        return notifyNote;
    }

    public void setNotifyNote(TextBox notifyNote)
    {
        this.notifyNote = notifyNote;
    }

    public Radio getRadioPublic()
    {
        return radioPublic;
    }

    public void setRadioPublic(Radio radioPublic)
    {
        this.radioPublic = radioPublic;
    }

    public Radio getRadioPrivate()
    {
        return radioPrivate;
    }

    public void setRadioPrivate(Radio radioPrivate)
    {
        this.radioPrivate = radioPrivate;
    }

/*
public Radio getRadioConfidential()
{
return radioConfidential;
}

public void setRadioConfidential(Radio radioConfidential)
{
this.radioConfidential = radioConfidential;
}
*/

    public boolean isEdit()
    {
        return edit;
    }

    public void setEdit(boolean edit)
    {
        this.edit = edit;
    }

    public Button getSubmitButton()
    {
        return submitButton;
    }

    public void setSubmitButton(Button submitButton)
    {
        this.submitButton = submitButton;
    }

    public Button getCancelButton()
    {
        return cancelButton;
    }

    public void setCancelButton(Button cancelButton)
    {
        this.cancelButton = cancelButton;
    }

    public Button getResetButton()
    {
        return resetButton;
    }

    public void setResetButton(Button resetButton)
    {
        this.resetButton = resetButton;
    }

    public String getViewUrl()
    {
        return viewUrl;
    }

    public void setViewUrl(String viewUrl)
    {
        this.viewUrl = viewUrl;
    }

    public String generateTaskId()
    {
        return Task.class.getName()+"_"+ UuidGenerator.getInstance().getUuid();
    }

    public void setGeneratedTaskId(String taskId)
    {
    }

    public Task getTask()
    {
        return task;
    }

    public void setTask(Task task)
    {
        this.task = task;
    }

    public TextField getTitle()
    {
        return title;
    }

    public void setTitle(TextField title)
    {
        this.title = title;
    }

    public ComboSelectBox getResources()
    {
        return resources;
    }

    public void setResources(ComboSelectBox resources)
    {
        this.resources = resources;
    }

    public DateField getStartDate()
    {
        return startDate;
    }

    public void setStartDate(DateField startDate)
    {
        this.startDate = startDate;
    }

    public TimeField getStartTime()
    {
        return startTime;
    }

    public void setStartTime(TimeField startTime)
    {
        this.startTime = startTime;
    }

    public boolean isIgnoreConflict()
    {
        return ignoreConflict;
    }

    public void setIgnoreConflict(boolean ignoreConflict)
    {
        this.ignoreConflict = ignoreConflict;
    }

    public void setTaskPriority(SelectBox taskPriority) {
        this.taskPriority=taskPriority;
    }

    public SelectBox getTaskPriority() {
        return this.taskPriority;
    }

	public TextField getEstimation() {
		return estimation;
	}

	public void setEstimation(TextField estimation) {
		this.estimation = estimation;
	}

	public SelectBox getEstimationType() {
		return estimationType;
	}

	public void setEstimationType(SelectBox estimationType) {
		this.estimationType = estimationType;
	}

	public String getTaskProjectId() {
		return taskProjectId;
	}

	public void setTaskProjectId(String taskProjectId) {
		this.taskProjectId = taskProjectId;
	}

    public DateField getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(DateField reminderDate) {
        this.reminderDate = reminderDate;
    }

    public TimeField getReminderTime()
    {
        return reminderTime;
    }

    public void setReminderTime(TimeField reminderTime)
    {
        this.reminderTime = reminderTime;
    }
}

