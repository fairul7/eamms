package com.tms.collab.timesheet.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.SelectBox;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.collab.project.Project;
import com.tms.collab.project.WormsHandler;
import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.taskmanager.model.TaskCategory;
import com.tms.collab.taskmanager.model.TaskManager;
import com.tms.collab.timesheet.model.TimeSheetModule;

public class TimeSheetNonVTForm extends Form {
    private SelectBox sbTask;
    private Button btSubmit;
    private String projectId;
    private String taskId;
    private Collection col;
    private boolean set=false;
    private TaskCategory category;
    private static final String TIMESHEET_PERMISSION="com.tms.collab.timesheet.ViewProjects";

    public String getDefaultTemplate() {
        return "timesheet/tsnonvtform";
    }

    public void onRequest(Event ev)  {
        WormsHandler handler = (WormsHandler)Application.getInstance().getModule(WormsHandler.class);
        TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
        try {
        	 String userId = getWidgetManager().getUser().getId();
        	 SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
             boolean hasPermission = security.hasPermission(userId, TIMESHEET_PERMISSION, TimeSheetModule.class.getName(), null);
             TimeSheetModule mod = (TimeSheetModule)Application.getInstance().getModule(TimeSheetModule.class);
             category = tm.getCategory(projectId);
             Collection taskList=new ArrayList();
             Collection noTSCol=new ArrayList();
             col=new ArrayList();
             if (hasPermission) {
            	 taskList = mod.getNonProjectTaskList(projectId,null); 
            	 noTSCol = mod.getNoTimeSheetNonProjectTaskList(projectId,null);
            	
             }else{
            	 taskList = mod.getNonProjectTaskList(projectId,userId);  
            	 noTSCol = mod.getNoTimeSheetNonProjectTaskList(projectId,userId);
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
                if(task.getTitle()==null)
                	task.setTitle("");
                if(task.getTitle().length()>35)
                	task.setTitle(task.getTitle().substring(0,35)+"...");
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

	public TaskCategory getCategory() {
		return category;
	}

	public void setCategory(TaskCategory category) {
		this.category = category;
	}
}

