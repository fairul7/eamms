package com.tms.collab.project.ui;

import com.tms.collab.project.Milestone;
import com.tms.collab.project.WormsException;
import com.tms.collab.project.WormsHandler;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.OperatorGreaterThan;
import kacang.model.operator.OperatorLessThan;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ProjectSchedule extends Form
{
    public static final String FORWARD_ADD_MILESTONE = "forward.worms.schedule.AddMilestone";
    public static final String FORWARD_ADD_TASK = "forward.worms.schedule.AddTask";
	public static final String FORWARD_ADD_MEETING = "forward.worms.schedule.AddMeeting";
    public static final String FORWARD_SUCCESS = "forward.worms.schedule.Success";
    public static final String FORWARD_FAILED = "forward.worms.schedule.Failed";
    public static final String FORWARD_CANCEL = "forward.worms.schedule.Cancel";
    public static final String FORWARD_SELECTION = "forward.worms.schedule.Select";

    public static final String DEFAULT_TEMPLATE = "project/projectSchedule";
    public static final String EVENT_MOVE_UP = "move_up";
    public static final String EVENT_MOVE_DOWN = "move_down";
    public static final String KEY_MILESTONE = "milestoneId";
    public static final String KEY_TASK = "taskId";
    public static final String PREFIX_CHECKBOX = "chk";

    protected String projectId;
    protected String templateId;
    protected boolean currentlyTemplate;
    protected Collection milestones;

    protected Button delete;
    protected Button addMilestone;
    protected Button addTask;
	protected Button addMeeting;
    protected Button cancel;

    public ProjectSchedule()
    {
    }

    public ProjectSchedule(String s)
    {
        super(s);
    }

    public void init()
    {
        if(!isKeyEmpty())
        {
            super.init();
            milestones = new ArrayList();

            delete = new Button("delete", Application.getInstance().getResourceBundle().getString("project.label.delete"));
            delete.setOnClick("if(!(confirm('" + Application.getInstance().getResourceBundle().getString("project.message.allTasksWillBeDeleted") + "'))) return false;");
            addMilestone = new Button("addMilestone", Application.getInstance().getResourceBundle().getString("project.label.addMilestone"));
            addTask = new Button("addTask", Application.getInstance().getResourceBundle().getString("project.label.addTask"));
			addMeeting = new Button("addMeeting", Application.getInstance().getResourceBundle().getString("project.label.addMeeting"));
            cancel = new Button("cancel", Application.getInstance().getResourceBundle().getString("project.label.cancel"));

            addChild(delete);
            addChild(addMilestone);
            addChild(addTask);
			addChild(addMeeting);
            addChild(cancel);
        }
    }

    public void onRequest(Event event)
    {
        if(!isKeyEmpty())
        {
            super.onRequest(event);
            refresh();
            if(currentlyTemplate)
            {
				addMeeting.setHidden(true);
                cancel.setHidden(true);
            }
        }
    }

    protected void refresh()
    {
        if(!isKeyEmpty())
        {
            try
            {
                WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
                if(currentlyTemplate)
                    milestones = worms.getMilestonesByProject(templateId, true);
                else
                    milestones = worms.getMilestonesByProject(projectId, true);
                removeChildren();
                addChild(delete);
                addChild(addMilestone);
                addChild(addTask);
				addChild(addMeeting);
                addChild(cancel);
                for(Iterator i = milestones.iterator(); i.hasNext();)
                {
                    Milestone milestone = (Milestone) i.next();
                    CheckBox checkbox = new CheckBox(PREFIX_CHECKBOX + milestone.getMilestoneId());
                    addChild(checkbox);
                }
            }
            catch (WormsException e)
            {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
        }
    }

    public Forward actionPerformed(Event event)
    {
        Forward forward = null;
        if(!isKeyEmpty())
        {
            if(FORWARD_SELECTION.equals(event.getType()))
                forward = new Forward(FORWARD_SELECTION);
            else if(cancel.getAbsoluteName().equals(findButtonClicked(event)))
                forward = new Forward(FORWARD_CANCEL);
            else if(addMilestone.getAbsoluteName().equals(findButtonClicked(event)))
                forward = new Forward(FORWARD_ADD_MILESTONE);
            else if(addTask.getAbsoluteName().equals(findButtonClicked(event)))
                forward = new Forward(FORWARD_ADD_TASK);
			else if(addMeeting.getAbsoluteName().equals(findButtonClicked(event)))
                forward = new Forward(FORWARD_ADD_MEETING);
            else
                forward = super.actionPerformed(event);
        }
        return forward;
    }

    public Forward onValidate(Event event)
    {
        Forward forward = null;
        if(!isKeyEmpty())
        {
            try
            {
                /* Determine Event Type */
                if(EVENT_MOVE_UP.equals(event.getType()))
                {
                    forward = moveUp(event);
                }
                else if(EVENT_MOVE_DOWN.equals(event.getType()))
                {
                    forward = moveDown(event);
                }
                else if(delete.getAbsoluteName().equals(findButtonClicked(event)))
                {
                    forward = deleteMilestones();
                }
            }
            catch(Exception e)
            {
                Log.getLog(getClass()).error(e.getMessage(), e);
                forward = new Forward(FORWARD_FAILED);
            }
        }
        return forward;
    }

    protected boolean isKeyEmpty()
    {
        if(currentlyTemplate)
        {
            if(!(templateId == null || "".equals(templateId)))
                return false;
        }
        else if(!(projectId == null || "".equals(projectId)))
            return false;
        return true;
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    protected Forward deleteMilestones()
    {
        Forward forward = null;
        WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
        for(Iterator i = milestones.iterator(); i.hasNext();)
        {
            Milestone milestone = (Milestone) i.next();
            CheckBox checkbox = (CheckBox) childMap.get(PREFIX_CHECKBOX + milestone.getMilestoneId());
            if(checkbox.isChecked())
            {
                try
                {
                    worms.deleteMilestone(milestone.getMilestoneId());
                }
                catch (WormsException e)
                {
                    Log.getLog(getClass()).error(e.getMessage(), e);
                }
            }
        }
        refresh();
        return forward;
    }

    protected Forward moveUp(Event event)
    {
        Forward forward = null;
        String milestoneId = event.getParameter(KEY_MILESTONE);
        if(!(milestoneId == null || "".equals(milestoneId)))
        {
            try
            {
                WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
                Milestone milestone = worms.getMilestone(milestoneId);
                if(milestone != null)
                {
                    DaoQuery query = new DaoQuery();
                    query.addProperty(new OperatorEquals("projectId", milestone.getProjectId(), DaoOperator.OPERATOR_AND));
                    query.addProperty(new OperatorLessThan("milestoneOrder", new Integer(milestone.getMilestoneOrder()), DaoOperator.OPERATOR_AND));
                    Collection list = worms.getMilestones(query, 0, 1, "milestoneOrder", true, false);
                    if(list.size() > 0)
                    {
                        Milestone swap = (Milestone) list.iterator().next();
                        int order = milestone.getMilestoneOrder();
                        milestone.setMilestoneOrder(swap.getMilestoneOrder());
                        swap.setMilestoneOrder(order);
                        worms.editMilestone(milestone);
                        worms.editMilestone(swap);
                        refresh();
                    }
                }
            }
            catch (WormsException e)
            {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
        }
        return forward;
    }

    protected Forward moveDown(Event event)
    {
        Forward forward = null;
        String milestoneId = event.getParameter(KEY_MILESTONE);
        if(!(milestoneId == null || "".equals(milestoneId)))
        {
            try
            {
                WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
                Milestone milestone = worms.getMilestone(milestoneId);
                if(milestone != null)
                {
                    DaoQuery query = new DaoQuery();
                    query.addProperty(new OperatorEquals("projectId", milestone.getProjectId(), DaoOperator.OPERATOR_AND));
                    query.addProperty(new OperatorGreaterThan("milestoneOrder", new Integer(milestone.getMilestoneOrder()), DaoOperator.OPERATOR_AND));
                    Collection list = worms.getMilestones(query, 0, 1, "milestoneOrder", false, false);
                    if(list.size() > 0)
                    {
                        Milestone swap = (Milestone) list.iterator().next();
                        int order = milestone.getMilestoneOrder();
                        milestone.setMilestoneOrder(swap.getMilestoneOrder());
                        swap.setMilestoneOrder(order);
                        worms.editMilestone(milestone);
                        worms.editMilestone(swap);
                        refresh();
                    }
                }
            }
            catch (WormsException e)
            {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
        }
        return forward;
    }

    /* Getters and Setters */
    public String getProjectId()
    {
        return projectId;
    }

    public void setProjectId(String projectId)
    {
        this.projectId = projectId;
        setCurrentlyTemplate(false);
        init();
    }

    public Collection getMilestones()
    {
        return milestones;
    }

    public void setMilestones(Collection milestones)
    {
        this.milestones = milestones;
    }

    public Button getDelete()
    {
        return delete;
    }

    public void setDelete(Button delete)
    {
        this.delete = delete;
    }

    public Button getAddMilestone()
    {
        return addMilestone;
    }

    public void setAddMilestone(Button addMilestone)
    {
        this.addMilestone = addMilestone;
    }

    public Button getAddTask()
    {
        return addTask;
    }

    public void setAddTask(Button addTask)
    {
        this.addTask = addTask;
    }

    public Button getCancel()
    {
        return cancel;
    }

    public void setCancel(Button cancel)
    {
        this.cancel = cancel;
    }

    public String getTemplateId()
    {
        return templateId;
    }

    public void setTemplateId(String templateId)
    {
        this.templateId = templateId;
        setCurrentlyTemplate(true);
        init();
    }

    public boolean isCurrentlyTemplate()
    {
        return currentlyTemplate;
    }

    public void setCurrentlyTemplate(boolean currentlyTemplate)
    {
        this.currentlyTemplate = currentlyTemplate;
    }

	public Button getAddMeeting()
	{
		return addMeeting;
	}

	public void setAddMeeting(Button addMeeting)
	{
		this.addMeeting = addMeeting;
	}
}
