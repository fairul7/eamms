package com.tms.collab.timesheet.ui;

import kacang.model.DaoException;
import kacang.services.security.User;
import kacang.stdui.Form;
import kacang.stdui.SelectBox;
import kacang.stdui.Button;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;
import kacang.Application;
import kacang.util.Log;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.tms.collab.project.WormsHandler;
import com.tms.collab.project.Project;
import com.tms.collab.taskmanager.model.Assignee;
import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.taskmanager.model.TaskManager;
import com.tms.collab.taskmanager.model.TaskCategory;


/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: Apr 26, 2005
 * Time: 2:40:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimeSheetViewForm extends Form {
    private SelectBox sbProject;
    private SelectBox sbTask;
    private Button btSubmit;
    private String userId;
    private Collection categories;
    private String projectId;
    private String taskId;
    private boolean set=false;
    private Project project;
    
    private boolean addTimeSheet=false;

    public String getDefaultTemplate() {
        return "timesheet/tsviewform";
    }

    public void init() {
        super.init();
    }

    public void onRequest(Event ev) {
    	removeChildren();
        userId = getWidgetManager().getUser().getId();
        initTask();
        setUpProject();
        setUpTask();
        initForm();
    }
    
    public void initTask() {
    sbTask = new SelectBox("sbtask");
    sbTask.removeAllOptions();
    sbTask.addOption("",Application.getInstance().getMessage("security.label.pleaseSelect","---Please Select---"));
    }
    
    public void initForm() {
    	setMethod("POST");   
    	addChild(sbTask);
        addChild(sbProject);
        btSubmit=new Button("submit",Application.getInstance().getMessage("timesheet.label.submit","Submit"));
        addChild(btSubmit);
    }

    public void setUpProject() {
        sbProject = new SelectBox("sbproject");
        sbProject.addOption("",Application.getInstance().getMessage("security.label.pleaseSelect","---Please Select---"));
        sbProject.setOnChange("submit()");
        TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
        WormsHandler handler = (WormsHandler)Application.getInstance().getModule(WormsHandler.class);
        try {
            // get all task categories list
            Collection col = handler.getProjectForTimeSheet(userId,null);

            if (col!=null && col.size()>0) {
                int iCount=0;
                for  (Iterator i=col.iterator();i.hasNext();) {
                    Project p = (Project)i.next();
                    sbProject.addOption(p.getProjectId(),"[Project] "+p.getProjectName());
                }
            }

            col = handler.getProjects();
            String[] projectId={};
            if (col!=null && col.size()>0) {
            	projectId = new String[col.size()];
                int iCount=0;
                for  (Iterator i=col.iterator();i.hasNext();) {
                    Project p = (Project)i.next();
                    projectId[iCount]=p.getProjectId();
                    iCount++;
                }
            }

            categories = tm.getNonProjectCategories(userId,projectId);
            if (categories!=null && categories.size()>0) {
                for (Iterator i=categories.iterator();i.hasNext();) {
                    TaskCategory category=(TaskCategory)i.next();
                    sbProject.addOption(category.getId(),category.getName());
                }
            }
            /*
            categories = tm.getCategories(userId);
            if (categories!=null && categories.size()>0) {
                for(Iterator i=categories.iterator();i.hasNext();) {
                    TaskCategory category = (TaskCategory)i.next();
                    boolean bSet=false;
                    if (col!=null && col.size()>0) {
                        for (Iterator j=col.iterator();j.hasNext();) {
                            Project p = (Project)j.next();
                            if (p.getProjectName().equals(category.getName())) {
                                bSet = true;
                                sbProject.addOption(p.getProjectId(),p.getProjectName());
                            }
                            if (bSet)
                                break;
                        }
                        if (!bSet)
                            sbProject.addOption(category.getId(),category.getName());
                    }
                    else {
                        sbProject.addOption(category.getId(),category.getName());
                    }
                }
            }

            */
        }
        catch(Exception e) {
            Log.getLog(getClass()).error("error in setUpProject "+e.toString());
        }

    }
    
    public void setUpTask() {
        //TimeSheetModule mod = (TimeSheetModule)Application.getInstance().getModule(TimeSheetModule.class);
    	sbTask.removeAllOptions();
    	sbTask.addOption("",Application.getInstance().getMessage("security.label.pleaseSelect","---Please Select---"));
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

            Collection col = tm.getAllTasks(null,userId,projectId,0,-1,null,false);//mod.getTaskListByProject("",userid);
            //Collection col = tm.getTasks(null,userid,projectId,false,0,-1,null,false);
            if (col!=null && col.size()>0) {
                for (Iterator i=col.iterator();i.hasNext();) {
                    try {
                        Task task = (Task)i.next();
                        Assignee assignee = tm.getAssignee(task.getId(),userId);
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
                        Assignee assignee = tm.getAssignee(task.getId(),userId);
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
                        Assignee assignee = tm.getAssignee(task.getId(),userId);
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
    
    public Forward onSubmit(Event evt) {
        String button = findButtonClicked(evt);
        super.onSubmit(evt);

        if (!btSubmit.getAbsoluteName().equals(button)) {
        	projectId = (String)sbProject.getSelectedOptions().keySet().iterator().next();
        	if (projectId==null || projectId.equals(""))
                return new Forward("select");
            else
            	setUpTask();
            setInvalid(true);
        }
        return null;
    }
    
    public Forward onValidate(Event ev) {
        if (btSubmit.getAbsoluteName().equals(findButtonClicked(ev))) {
        	taskId = (String) sbTask.getSelectedOptions().keySet().iterator().next();
            projectId = (String)sbProject.getSelectedOptions().keySet().iterator().next();
            if ((projectId==null || projectId.equals(""))||!(taskId!=null && !taskId.equals("")))
                return new Forward("select");
            else
                return new Forward("cont");
        }
        return null;
    }

    public SelectBox getSbProject() {
        return sbProject;
    }

    public void setSbProject(SelectBox sbProject) {
        this.sbProject = sbProject;
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

    public boolean isSet() {
        return set;
    }

    public void setSet(boolean set) {
        this.set = set;
    }

	public SelectBox getSbTask() {
		return sbTask;
	}

	public void setSbTask(SelectBox sbTask) {
		this.sbTask = sbTask;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public boolean isAddTimeSheet() {
		return addTimeSheet;
	}

	public void setAddTimeSheet(boolean addTimeSheet) {
		this.addTimeSheet = addTimeSheet;
	}
    
}
