package com.tms.collab.timesheet.ui;

import kacang.services.security.SecurityService;
import kacang.stdui.Form;
import kacang.stdui.SelectBox;
import kacang.stdui.Button;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.taskmanager.model.TaskManager;
import com.tms.collab.timesheet.model.TimeSheetModule;
import com.tms.collab.project.WormsHandler;
import com.tms.collab.project.Project;

/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: Apr 28, 2005
 * Time: 2:01:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimeSheetVTForm extends Form {
    private SelectBox sbTask;
    private Button btSubmit;
    private String projectId;
    private String taskId;
    private Collection col;
    private boolean set=false;
    private Project project;
    private static final String TIMESHEET_PERMISSION="com.tms.collab.timesheet.ViewProjects";

    public String getDefaultTemplate() {
        return "timesheet/tsvtform";
    }

    public void onRequest(Event ev)  {
        WormsHandler handler = (WormsHandler)Application.getInstance().getModule(WormsHandler.class);
        TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
        try {
        	 String userId = getWidgetManager().getUser().getId();
        	 TimeSheetModule mod = (TimeSheetModule)Application.getInstance().getModule(TimeSheetModule.class);
        	 SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
             boolean hasPermission = security.hasPermission(userId, TIMESHEET_PERMISSION, TimeSheetModule.class.getName(), null);
             
            project = handler.getProject(projectId);
            if (hasPermission||userId.equals(project.getOwnerId())) {
            	col = handler.getTaskListAttachToProjects(projectId,null);
            }else {
            	col = handler.getTaskListAttachToProjects(projectId,userId);	
            }
            col=new ArrayList();
            Collection taskList=new ArrayList();
            Collection noTSCol=new ArrayList();
            if (hasPermission||userId.equals(project.getOwnerId())) {
           	 taskList = mod.getProjectTaskList(projectId,null); 
           	 noTSCol = mod.getNoTimeSheetTaskList(projectId,null);
           	
            }else{
           	 taskList = mod.getProjectTaskList(projectId,userId);  
           	 noTSCol = mod.getNoTimeSheetTaskList(projectId,userId);
            }
            for (Iterator j=taskList.iterator();j.hasNext();) {
                HashMap hm = (HashMap)j.next();
                Task task= tm.getTask((String)hm.get("task"));
                col.add(task);
            }
            for (Iterator iterator=noTSCol.iterator();iterator.hasNext();) {
                HashMap map = (HashMap)iterator.next();
                Task task = tm.getTask((String)map.get("taskId"));
                col.add(task);
            }
        }
        catch(Exception e) {
        }
        initForm();
    }

    public void init() {
        super.init();
    }

    public void initForm() {
        sbTask = new SelectBox("sbtask");
        sbTask.addOption("",Application.getInstance().getMessage("security.label.pleaseSelect","---Please Select---"));
        btSubmit = new Button("submit",Application.getInstance().getMessage("timesheet.label.submit","Submit"));

        if (col!=null && col.size()>0) {
            for (Iterator i=col.iterator();i.hasNext();){
                Task task = (Task)i.next();
                sbTask.addOption(task.getId(),task.getTitle());
            }
        }

        addChild(sbTask);
        addChild(btSubmit);
    }

    public Forward onValidate(Event ev) {
        if (btSubmit.getAbsoluteName().equals(findButtonClicked(ev))) {
            taskId = (String)sbTask.getSelectedOptions().keySet().iterator().next();
            if (taskId!=null && !taskId.equals(""))
                return new Forward("task");
            else
                return new Forward("select");
        }
        return null;
    }

    public SelectBox getSbTask() {
        return sbTask;
    }

    public void setSbTask(SelectBox sbTask) {
        this.sbTask = sbTask;
    }

    public Button getBtSubmit() {
        return btSubmit;
    }

    public void setBtSubmit(Button btSubmit) {
        this.btSubmit = btSubmit;
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

    public void setProject(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }
}
