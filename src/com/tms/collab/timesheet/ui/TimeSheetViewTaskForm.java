package com.tms.collab.timesheet.ui;

import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.SelectBox;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.collab.project.Project;
import com.tms.collab.project.WormsHandler;
import com.tms.collab.taskmanager.model.Assignee;
import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.taskmanager.model.TaskCategory;
import com.tms.collab.taskmanager.model.TaskManager;


/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: Apr 26, 2005
 * Time: 3:22:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimeSheetViewTaskForm extends Form {
    private SelectBox sbTask;
    private Button btSubmit;
    private String projectId;
    private String taskId;
    private String userid;
    private boolean set=false;
    private Project project;
    
    private boolean addTimeSheet=false;

    public String getDefaultTemplate() {
        return "timesheet/tsviewtaskform";
    }

    public Forward onValidate(Event ev) {
        if (btSubmit.getAbsoluteName().equals(findButtonClicked(ev))) {
            taskId = (String) sbTask.getSelectedOptions().keySet().iterator().next();
            if (taskId!=null && !taskId.equals(""))
                return new Forward("view");
            else
                return new Forward("select");
        }
        return null;
        //return super.onValidate(ev);
    }

    public void init() {
        super.init();
    }

    public void onRequest(Event ev) {
        userid = getWidgetManager().getUser().getId();
        sbTask = new SelectBox("sbtask");
        sbTask.addOption("",Application.getInstance().getMessage("security.label.pleaseSelect","---Please Select---"));
        if (projectId!=null && !projectId.equals("")) {
            setUpTask();
        }
        initForm();
        //super.onRequest(ev);
    }

    public void initForm() {
        btSubmit = new Button("Submit",Application.getInstance().getMessage("timesheet.label.submit","Submit"));
        addChild(sbTask);
        addChild(btSubmit);
    }

    public void setUpTask() {
        sbTask = new SelectBox("sbtask");
        sbTask.addOption("",Application.getInstance().getMessage("security.label.pleaseSelect","---Please Select---"));
        //TimeSheetModule mod = (TimeSheetModule)Application.getInstance().getModule(TimeSheetModule.class);

        WormsHandler handler = (WormsHandler)Application.getInstance().getModule(WormsHandler.class);
        boolean isProject = false;
        try {
            project = handler.getProject(projectId);
            if (project!=null)
                isProject=true;
        }
        catch(Exception e) {

        }
        if (!isProject) {
            // get general task
            TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
            // currently get for own task list
            try {
            TaskCategory tc = tm.getCategory(projectId);
            project = new Project();
                project.setProjectId(tc.getId());
                project.setProjectName(tc.getName());

            Collection col = tm.getAllTasks(null,userid,projectId,0,-1,null,false);//mod.getTaskListByProject("",userid);
            //Collection col = tm.getTasks(null,userid,projectId,false,0,-1,null,false);
            if (col!=null && col.size()>0) {
                for (Iterator i=col.iterator();i.hasNext();) {
                    try {
                        Task task = (Task)i.next();
                        Assignee assignee = tm.getAssignee(task.getId(),userid);
                        if (addTimeSheet) {
                        	if (assignee!=null && assignee.getCompleteDate()==null) {
                        		sbTask.addOption(task.getId(),task.getTitle());
                        	}
                        }
                        else {
                        	if (assignee!=null) {
                        		sbTask.addOption(task.getId(),task.getTitle());
                        	}
                        }
                    }
                    catch(Exception e) {
                        Log.getLog(getClass()).error("Error in setUpTask 1 "+e.toString());
                    }
                }
            }
            }
            catch(Exception e) {

            }
        }
        else {
            // get task under a project
            try {
                Collection col = handler.getTaskList(projectId,getWidgetManager().getUser().getId()); //handler.getMilestonesByProject(projectId,true);
                TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
                
                if (col!=null && col.size()>0) {
                    for (Iterator i=col.iterator();i.hasNext();) {
                        Task task = (Task)i.next();
                        Assignee assignee = tm.getAssignee(task.getId(),userid);
                        if (addTimeSheet) {
                        	if (assignee != null && assignee.getCompleteDate()==null)
                        		sbTask.addOption(task.getId(),task.getTitle());
                        	}
                        else {
                        	if (assignee != null){
                        		sbTask.addOption(task.getId(),task.getTitle());
                        	}
                        }
                    }
                }

                col= handler.getTaskListAttachToProject(projectId,getWidgetManager().getUser().getId());
                if(col!=null && col.size() >0) {
                    for (Iterator i=col.iterator();i.hasNext();) {
                        Task task = (Task)i.next();
                        Assignee assignee = tm.getAssignee(task.getId(),userid);
                        if (addTimeSheet) {
                        	if (assignee != null && assignee.getCompleteDate()==null)
                        		sbTask.addOption(task.getId(),task.getTitle());
                        }
                        else {
                            if (assignee != null)
                            	sbTask.addOption(task.getId(),task.getTitle());
                        }
                    }
                }
            }
            catch(Exception e) {
                Log.getLog(getClass()).error("Error in setUpTask "+e.toString());
            }
        }
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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
        return this.project;
    }

	public boolean isAddTimeSheet() {
		return addTimeSheet;
	}

	public void setAddTimeSheet(boolean addTimeSheet) {
		this.addTimeSheet = addTimeSheet;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
}
