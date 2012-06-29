package com.tms.collab.timesheet.ui;

import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.util.Log;
import com.tms.collab.project.Project;
import com.tms.collab.project.WormsHandler;
import com.tms.collab.project.Milestone;
import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.taskmanager.model.TaskManager;
import com.tms.collab.taskmanager.model.TaskCategory;
import com.tms.collab.taskmanager.model.Assignee;
import com.tms.collab.timesheet.model.TimeSheet;
import com.tms.collab.timesheet.model.TimeSheetModule;
import com.tms.collab.timesheet.TimeSheetUtil;
import com.tms.util.validator.SelectBoxValidatorNotEmpty;


import java.util.*;

import org.apache.commons.collections.SequencedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: Apr 22, 2005
 * Time: 2:13:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimeSheetForm extends Form {

    private String projectId;
    private String taskId;
    private Project project;
    private Task task;
    private boolean set=false;
    private boolean projectAttach=false;

    // widget in form
    private DateField tfDate;
    private TextField tfDuration;

    private SelectBox sbDuration;
    private TextBox tbDescription;
    private Button btSubmit,btCancel,btnSubmitAdd;   
    private SelectBox sbProject;
    private static  String FORWARD_SUBMIT_ADD="submitadd";
    private static  String FORWARD_ADDED="added";
    private static String FORWARD_ADDED_FORM="form";
    private static  String FORWARD_ERROR="error";
    private static String FORWARD_SELECT="select";
    private static String FORWARD_DATE="date";
    private static  String DEFAULT_TEMPLATE="timesheet/tsform";
    private SelectBoxValidatorNotEmpty durationValidator;

    public String getDefaultTemplate() {
        return DEFAULT_TEMPLATE;
    }
    
    public void onRequest(Event ev) {
        projectAttach=false;
        getValue();
        initForm();
        super.onRequest(ev);
    }

    public Forward onValidate(Event ev) {
    	String buttonClicked = findButtonClicked(ev);
        String s1 = (String)sbDuration.getSelectedOptions().keySet().iterator().next();
        Date tsDate = tfDate.getDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(tsDate);
        boolean isDate=false;

        if (cal.before(Calendar.getInstance())) {
            isDate=true;
        }
        if (s1!=null && !s1.equals("") && isDate) {
            TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
            try {
                task = tm.getTask(taskId);
            }
            catch(Exception e) {}
            Application app = Application.getInstance();
            TimeSheet ts = new TimeSheet();
            ts.setTaskId(taskId);
            Project p = getProjectByTaskId(taskId);
            if (p==null) {
                // removed
                //ts.setProjectId((String)sbProject.getSelectedOptions().keySet().iterator().next());
                // end removed
                p = getProjectByProjectName(taskId);
                if (p==null)
                    ts.setProjectId(task.getCategoryId());
                else
                    ts.setProjectId(p.getProjectId());
            }
            else {
                ts.setProjectId(p.getProjectId());
            }
            ts.setTsDate(tsDate);
            ts.setDescription((String)tbDescription.getValue());
            Double dbl = new Double(s1);

            double db = dbl.doubleValue();
            ts.setDuration(db);
            ts.setCreatedBy(getWidgetManager().getUser().getId());

            ts.setTaskCategoryName(task.getCategory());

            TimeSheetModule mod = (TimeSheetModule)app.getModule(TimeSheetModule.class);
            if (mod.add(ts)) {
                try {
                    //TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
                    Task myTask = tm.getTask(ts.getTaskId());
                    for (Iterator i=myTask.getAttendees().iterator();i.hasNext();) {
                        Assignee att = (Assignee)i.next();
                        if (att.getUserId().equals(getWidgetManager().getUser().getId())) {
                            if (att.getProgress()<=0 && att.getTaskStatus()<=0) {
                                tm.startTask(taskId,getWidgetManager().getUser().getId());
                            }
                        }
                    }
                }
                catch(Exception e){

                }
                if (buttonClicked.equals(btnSubmitAdd.getAbsoluteName())) {
                	return new Forward(FORWARD_SUBMIT_ADD);
                }else{
                	if (set)
                        return new Forward(FORWARD_ADDED_FORM);
                    else
                        return new Forward(FORWARD_ADDED);
                	
                }
            }
            else {
                return new Forward(FORWARD_ERROR);
            }
        }
        else {
            if (isDate)
                return new Forward(FORWARD_SELECT);
            else
                return new Forward(FORWARD_DATE);
        }
    }

    public void init() {
    	setMethod("POST");
        super.init();
    }

    public void setUpProject() {

        try {
            TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
            Task objTask = tm.getTask(taskId);
            WormsHandler handler = (WormsHandler)Application.getInstance().getModule(WormsHandler.class);

            // get task category list
            Collection categories = tm.getCategories(getWidgetManager().getUser().getId());
            if (categories!=null && categories.size()>0) {
                for (Iterator i=categories.iterator();i.hasNext();) {
                    TaskCategory category = (TaskCategory)i.next();
                    Milestone ms = handler.getMilestoneByTask(taskId);
                    if (ms!=null) {
                        Project p = handler.getProject(ms.getProjectId());
                        sbProject.addOption(p.getProjectId(),p.getProjectName());
                        if (p.getProjectName().equals(objTask.getCategory()))
                            sbProject.setSelectedOption(p.getProjectId());
                    }
                    else {
                        sbProject.addOption(category.getId(),category.getName());
                        if (category.getName().equals(objTask.getCategory()))
                            sbProject.setSelectedOption(category.getId());
                    }
                }
            }
            
            
        }
        catch(Exception e) {

        }


    }

    public void initForm() {
        sbProject = new SelectBox("sbselect");
        if (!projectAttach) {
            setUpProject();
        }
        addChild(sbProject);

        tfDate = new DatePopupField("tfdate");
        tfDate.setDate(Calendar.getInstance().getTime());
        
        sbDuration = new SelectBox("sbduration");
        durationValidator = new SelectBoxValidatorNotEmpty("durationValidator");
        sbDuration.addChild(durationValidator);
        sbDuration.setMultiple(false);
        Map map = new SequencedHashMap();
        map.put("-1",Application.getInstance().getMessage("security.label.pleaseSelect","---Please Select---"));
        //for (int i=0; i<TimeSheetUtil.WORKING_HOUR_PER_DAY; i++) {
        for (int i=0; i<TimeSheetUtil.getTimeSheetWorkingHour(); i++) {
            map.put(""+(i+0.5),""+(i+0.5));
            map.put(""+(i+1)+".0",""+(i+1));
        }
        sbDuration.setOptionMap(map);
        sbDuration.setSelectedOption("-1");
        tbDescription  = new TextBox("tbdescription");
        btSubmit = new Button("Submit", Application.getInstance().getMessage("timesheet.label.submit","Submit"));
        btCancel = new Button(Form.CANCEL_FORM_ACTION, Application.getInstance().getMessage("timesheet.label.cancel","Cancel"));
        btnSubmitAdd = new Button("SubmitAdd", Application.getInstance().getMessage("timesheet.label.submitAdd","Submit & Continue"));
        
        addChild(tfDate);
        addChild(sbDuration);
        addChild(tbDescription);
        addChild(btnSubmitAdd);
        addChild(btSubmit);
        addChild(btCancel);
    }

    public void getValue() {
        projectAttach=false;
        TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);

        try {
            if (taskId != null) {
                task = tm.getTask(taskId);

                project = getProjectByTaskId(taskId);
                if (project!=null) {
                    projectId = project.getProjectId();
                    projectAttach=true;
                }
                else if (task.getCategoryId().equals(TaskManager.DEFAULT_CATEGORY_ID)) {
                    project = new Project();
                    project.setProjectId(TaskManager.DEFAULT_CATEGORY_ID);
                    project.setProjectName(tm.getCategory(TaskManager.DEFAULT_CATEGORY_ID).getName());

                }
                else {
                    project = getProjectByProjectName(taskId);
                    if (project !=null) {
                        projectId = project.getProjectId();
                        projectAttach=true;
                    }
                    else {
                        TaskCategory category = tm.getCategory(task.getCategoryId());
                        project = new Project();
                        project.setProjectId(category.getId());
                        project.setProjectName(category.getName());
                    }

                }
                projectAttach=true;
            }

        }
        catch(Exception e) {
            Log.getLog(getClass()).error("Error in getValue "+e.toString());
        }
    }

    private Project getProjectByProjectName(String taskId) {
        WormsHandler handler = (WormsHandler)Application.getInstance().getModule(WormsHandler.class);
        Project p = null;
        try {
            p = handler.getProjectByProjectName(taskId);
        }
        catch(Exception e) {

        }
        return p;
    }

    private Project getProjectByTaskId(String taskid) {
        WormsHandler handler = (WormsHandler)Application.getInstance().getModule(WormsHandler.class);
        Project p=null;
        try {
            p = handler.getProjectByTaskId(taskid);
        }
        catch(Exception e) {
            //Log.getLog(getClass()).error("Error in getProjectByTaskId "+e.toString());
            p=null;
        }
        return p;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public DateField getTfDate() {
        return tfDate;
    }

    public void setTfDate(DateField tfDate) {
        this.tfDate = tfDate;
    }

    public TextField getTfDuration() {
        return tfDuration;
    }

    public void setTfDuration(TextField tfDuration) {
        this.tfDuration = tfDuration;
    }

    public TextBox getTbDescription() {
        return tbDescription;
    }

    public void setTbDescription(TextBox tbDescription) {
        this.tbDescription = tbDescription;
    }

    public Button getBtSubmit() {
        return btSubmit;
    }

    public void setBtSubmit(Button btSubmit) {
        this.btSubmit = btSubmit;
    }

    public Button getBtCancel() {
        return btCancel;
    }

    public void setBtCancel(Button btCancel) {
        this.btCancel = btCancel;
    }

    public SelectBox getSbDuration() {
        return sbDuration;
    }

    public void setSbDuration(SelectBox sbDuration) {
        this.sbDuration = sbDuration;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public boolean isSet() {
        return set;
    }

    public void setSet(boolean set) {
        this.set = set;
    }

    public SelectBox getSbProject() {
        return sbProject;
    }

    public void setSbProject(SelectBox sbProject) {
        this.sbProject = sbProject;
    }

    public boolean isProjectAttach() {
        return projectAttach;
    }

    public void setProjectAttach(boolean projectAttach) {
        this.projectAttach = projectAttach;
    }

	public Button getBtnSubmitAdd() {
		return btnSubmitAdd;
	}

	public void setBtnSubmitAdd(Button btnSubmitAdd) {
		this.btnSubmitAdd = btnSubmitAdd;
	}
    
}
