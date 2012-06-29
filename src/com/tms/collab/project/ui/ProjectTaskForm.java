package com.tms.collab.project.ui;

import com.tms.collab.calendar.model.Attendee;
import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.taskmanager.model.TaskManager;
import com.tms.collab.taskmanager.model.TaskCategory;
import com.tms.collab.taskmanager.model.TaskException;
import com.tms.collab.taskmanager.ui.TaskForm;
import com.tms.collab.project.Milestone;
import com.tms.collab.project.WormsException;
import com.tms.collab.project.WormsHandler;
import com.tms.collab.project.Project;
import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.DaoOperator;
import kacang.stdui.Button;
import kacang.stdui.SelectBox;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ProjectTaskForm extends TaskForm
{
    public static final String DEFAULT_SELECTION = "-1";
    public static final String DEFAULT_TEMPLATE = "project/taskForm";
    public static final String FORWARD_TASK_DELETE = "com.tms.worms.Task.Deleted";

    protected Button deleteButton;
    protected SelectBox milestones;
    protected String projectId;
    protected String milestoneTaskId;

    public ProjectTaskForm()
    {
    }

    public ProjectTaskForm(String name)
    {
        super(name);
    }

    public void init()
    {
        if(!isKeyEmpty()) {
            super.init();
            dueDate.setTemplate("calendar/projectTaskDueDatePopupField");
            removeChild(assignees);
            assignees = new ProjectUsersSelectBox("assignees");
            addChild(assignees);
            assignees.init();
            milestones = new SelectBox("milestone");
            milestones.setSelectedOption("-1");
            addChild(milestones);
            deleteButton = new Button("delete", Application.getInstance().getResourceBundle().getString("project.label.deleteTask"));
            addChild(deleteButton);
        }
    }

    public Forward onSubmit(Event evt)
    {
        Forward forward = null;
        if(!isKeyEmpty())
        {
            forward = super.onSubmit(evt);
            if(deleteButton.getAbsoluteName().equals(findButtonClicked(evt)))
            {
                if(!(milestoneTaskId == null || "".equals(milestoneTaskId)))
                {
                    TaskManager manager = (TaskManager) Application.getInstance().getModule(TaskManager.class);
                    WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
                    try
                    {
                        Milestone milestone = worms.getMilestoneByTask(milestoneTaskId);
                        worms.deleteMilestoneTask(milestone.getMilestoneId(), milestoneTaskId);
                        manager.deleteTask(milestoneTaskId);
                        forward = new Forward(FORWARD_TASK_DELETE);
                        initForm();
                        setInvalid(true);
                    }
                    catch (Exception e)
                    {
                        Log.getLog(getClass()).error(e.getMessage(), e);
                    }
                }
            }
            else if(!(cancelButton.getAbsoluteName().equals(findButtonClicked(evt))))
            {
                String selected = (String) milestones.getSelectedOptions().keySet().iterator().next();
                if(DEFAULT_SELECTION.equals(selected))
                {
                    setInvalid(true);
                    milestones.setInvalid(true);
                }
            }
        }
        return forward;
    }

    public Forward onValidate(Event evt)
    {
        Forward forward = null;
        if(!isKeyEmpty())
        {
            if(!(cancelButton.getAbsoluteName().equals(findButtonClicked(evt))))
            {
                // set category to project name
                WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
                try
                {
                    TaskManager manager = (TaskManager) Application.getInstance().getModule(TaskManager.class);
                    Project project = worms.getProject(projectId);
                    DaoQuery query = new DaoQuery();
                    query.addProperty(new OperatorEquals("id", project.getProjectId(), DaoOperator.OPERATOR_AND));
                    Collection list = manager.getCategories(query, 0, 1, null, false);
                    if(list.size() > 0)
                    {
                        TaskCategory category = (TaskCategory) list.iterator().next();
                        categories.setSelectedOption(category.getId());
                    } else {
                        // default to general category
                        categories.setSelectedOption(TaskManager.DEFAULT_CATEGORY_ID);
                    }
                }
                catch (Exception e)
                {
                    Log.getLog(getClass()).error("Error occured while retrieving task categories", e);
                }

                String milestoneId = (String) milestones.getSelectedOptions().keySet().iterator().next();
                forward = super.onValidate(evt);

                // create task
                try
                {
                    if(!(taskId == null || "".equals(taskId)))
                    {
                        Milestone milestone = worms.getMilestoneByTask(taskId);
                        worms.deleteMilestoneTask(milestone.getMilestoneId(), taskId);
                    }
                    worms.addMilestoneTask(milestoneId, milestoneTaskId);
                    if(!(FORWARD_ADD_RESOURCES.equals(forward.getName())))
                    {
                        taskId = null;
                        milestoneTaskId = null;
                        init();
                        initForm();
                        onRequest(evt);
                    }
                }
                catch(Exception e)
                {
                    Log.getLog(getClass()).error(e);
                }
            }
        }
        else
        {
            super.setEdit(true);
            forward = super.onValidate(evt);
        }
        return forward;
    }

    public void onRequest(Event evt)
    {
        super.onRequest(evt);
        ignoreConflict = true;
        if(!isKeyEmpty())
        {
            try
            {
                WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
                ((ProjectUsersSelectBox)assignees).setProjectId(projectId);

                //Initializing milestones
                Collection milestones = worms.getMilestonesByProject(projectId, false);
                Map options = new SequencedHashMap();
                options.put(DEFAULT_SELECTION, Application.getInstance().getResourceBundle().getString("project.label.selectAMilestone"));
                for(Iterator i = milestones.iterator(); i.hasNext();)
                {
                    Milestone milestone = (Milestone) i.next();
                    options.put(milestone.getMilestoneId(), milestone.getMilestoneName());
                }
                this.milestones.setOptionMap(options);
                if(!(taskId == null || "".equals(taskId)))
                {
                    Milestone milestone = worms.getMilestoneByTask(taskId);
                    this.milestones.setSelectedOptions(new String[] {milestone.getMilestoneId()});
                }
            }
            catch(WormsException e)
            {
                Log.getLog(getClass()).error(e);
            }
        }
        if(milestoneTaskId == null || "".equals(milestoneTaskId))
        {
            if(deleteButton != null)
                deleteButton.setHidden(true);
        }
    }

    public void initForm()
    {
        if(!isKeyEmpty())
        {
            init();
            
            try
            {
                WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
                if(!(taskId == null || "".equals(taskId)))
                {
                    TaskManager manager = (TaskManager) Application.getInstance().getModule(TaskManager.class);
                    Task task = null;
                    task = manager.getTask(getTaskId());
                    if(task != null)
                    {
                        Map rmap = new HashMap();
                        for (Iterator iterator = task.getAttendeeMap().keySet().iterator(); iterator.hasNext();)
                        {
                              String id = (String) iterator.next();
                              rmap.put(id,((Attendee)task.getAttendeeMap().get(id)).getName());
                        }
                        String[] userIdArray = (String[])task.getAttendeeMap().keySet().toArray(new String[0]);
                        getAssignees().setIds(userIdArray);
                    }
				}
				else
				{
					//Setting category
					try
					{
						TaskManager manager = (TaskManager) Application.getInstance().getModule(TaskManager.class);
						Project project = worms.getProject(projectId);
						DaoQuery query = new DaoQuery();
						query.addProperty(new OperatorEquals("name", project.getProjectName(), DaoOperator.OPERATOR_AND));
						Collection list = manager.getCategories(query, 0, 1, null, false);
						if(list.size() > 0)
						{
							TaskCategory category = (TaskCategory) list.iterator().next();
							categories.setSelectedOption(category.getId());
						}
					}
					catch (TaskException e)
					{
						Log.getLog(getClass()).error("Error occured while retrieving task categories", e);
					}
				}
                //Initializing milestones
                Collection milestones = worms.getMilestonesByProject(projectId, false);
                Map options = new SequencedHashMap();
                options.put(DEFAULT_SELECTION, Application.getInstance().getResourceBundle().getString("project.label.selectAMilestone"));
                for(Iterator i = milestones.iterator(); i.hasNext();)
                {
                    Milestone milestone = (Milestone) i.next();
                    options.put(milestone.getMilestoneId(), milestone.getMilestoneName());
                }
                this.milestones = new SelectBox("milestone");
                this.milestones.setOptionMap(options);
                addChild(this.milestones);
                deleteButton = new Button("delete", Application.getInstance().getResourceBundle().getString("project.label.deleteTask"));
                addChild(deleteButton);
            }
            catch (WormsException e)
            {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
            catch (DaoException e)
            {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
            catch (DataObjectNotFoundException e)
            {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
        }
    }

    public String generateTaskId()
    {
        setMilestoneTaskId(super.generateTaskId());
        return milestoneTaskId;
    }

    public void setGeneratedTaskId(String taskId)
    {
        setMilestoneTaskId(taskId);
    }

    protected boolean isKeyEmpty()
    {
        if(!(projectId == null || "".equals(projectId)))
            return false;
        return true;
    }

    public String getDefaultTemplate()
    {
        if(isUploadFile())
            return DEFAULT_TEMPLATE_UPLOAD;
        else
            return DEFAULT_TEMPLATE;
    }

    /* Getters and Setters */
    public SelectBox getMilestones()
    {
        return milestones;
    }

    public void setMilestones(SelectBox milestones)
    {
        this.milestones = milestones;
    }

    public String getMilestoneTaskId()
    {
        return milestoneTaskId;
    }

    public void setMilestoneTaskId(String milestoneTaskId)
    {
        this.milestoneTaskId = milestoneTaskId;
        deleteButton.setHidden(false);
    }

    public String getProjectId()
    {
        return projectId;
    }

    public void setProjectId(String projectId)
    {
        if(!(projectId == null || "".equals(projectId)))
        {
            this.projectId = projectId;
            initForm();
        }
    }

    public void setTaskId(String taskId)
    {
        super.setTaskId(taskId);
        try
        {
            WormsHandler handler = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
            Milestone milestone = handler.getMilestoneByTask(taskId);
            setProjectId(milestone.getProjectId());
            milestoneTaskId = taskId;
        }
        catch (WormsException e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }

    }

    public void setReset(String reset)
    {
        if(!(reset == null || "".equals(reset)))
        {
            milestoneTaskId = null;
            taskId = null;
            setEdit(false);
        }
    }

    public Button getDeleteButton()
    {
        return deleteButton;
    }

    public void setDeleteButton(Button deleteButton)
    {
        this.deleteButton = deleteButton;
    }
}
