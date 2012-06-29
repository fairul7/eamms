package com.tms.collab.project.ui;

import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.DaoOperator;
import kacang.stdui.SelectBox;
import kacang.stdui.Label;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import com.tms.collab.project.*;
import com.tms.collab.taskmanager.model.Task;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Calendar;

import org.apache.commons.collections.SequencedHashMap;

public class TemplateAdd extends TemplateForm
{
    public static final String DEFAULT_PROJECT_SELECTION = "-1";

    private SelectBox archivedProject;
    protected String templateId;

    public TemplateAdd()
    {
    }

    public TemplateAdd(String s)
    {
        super(s);
    }

    public void init()
    {
        archivedProject = new SelectBox("archivedProject");
        archivedProject.setSelectedOption(DEFAULT_PROJECT_SELECTION);
        addChild(new Label("labelArchived", "Archived Project"));
        addChild(archivedProject);
        super.init();
    }

    public void onRequest(Event event)
    {
        WormsHandler handler = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
        DaoQuery query = new DaoQuery();
        query.addProperty(new OperatorEquals("archived", "1", DaoOperator.OPERATOR_AND));
        Collection projects = new ArrayList();
        try
        {
            projects = handler.getProjects(query, 0, -1, "projectName", false);
        }
        catch (WormsException e)
        {
            Log.getLog(getClass()).error("Error while retrieving archived projects", e);
        }
        SequencedHashMap map = new SequencedHashMap();
        map.put(DEFAULT_PROJECT_SELECTION, "Select A Project");
        for (Iterator i = projects.iterator(); i.hasNext();)
        {
            Project project = (Project) i.next();
            map.put(project.getProjectId(), project.getProjectName());
        }
        archivedProject.setOptionMap(map);
        super.onRequest(event);
    }

    public Forward onValidate(Event event)
    {
        Forward forward = super.onValidate(event);
        try
        {
            Template template = generateTemplate();
			templateId = template.getTemplateId();
            WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
            worms.addTemplate(template);
            String projectId = (String) archivedProject.getSelectedOptions().keySet().iterator().next();
            //Generating template based on project blueprint
            if(!(DEFAULT_PROJECT_SELECTION.equals(projectId)))
            {
                WormsHandler handler = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
                Calendar projectStart = Calendar.getInstance();
                //Initializing
                Project project = handler.getProject(projectId);
                project.setMilestones(handler.getMilestonesByProject(projectId, true));
                project.setRoles(handler.getRolesByProject(projectId, false));
                projectStart.setTime(handler.getProjectStart(projectId));
                //Iterating milestones
                for (Iterator mi = project.getMilestones().iterator(); mi.hasNext();)
                {
                    Milestone milestone = (Milestone) mi.next();
                    //Formulating new Milestone
                    Milestone templateMilestone = new Milestone();
                    templateMilestone.setMilestoneId(UuidGenerator.getInstance().getUuid());
                    templateMilestone.setProjectId(template.getTemplateId());
                    templateMilestone.setMilestoneName(milestone.getMilestoneName());
                    templateMilestone.setMilestoneDescription(milestone.getMilestoneDescription());
                    templateMilestone.setMilestoneProgress(milestone.getMilestoneProgress());
                    handler.addMilestone(templateMilestone);
                    for (Iterator ti = milestone.getTasks().iterator(); ti.hasNext();)
                    {
                        Task task = (Task) ti.next();
                        //Formulation new TaskDescriptor
                        TaskDescriptor descriptor = new TaskDescriptor();
                        descriptor.setDescId(UuidGenerator.getInstance().getUuid());
                        descriptor.setMilestoneId(templateMilestone.getMilestoneId());
                        descriptor.setDescName(task.getTitle());
                        descriptor.setDescDescription(task.getDescription());
                        descriptor.setDescStart((WormsUtil.getDaysBetween(projectStart.getTime(), task.getStartDate())) + 1);
                        descriptor.setDescDuration((WormsUtil.getDaysBetween(task.getStartDate(), task.getDueDate())) + 1);
                        handler.addDescriptor(descriptor);
                        handler.addMilestoneTask(templateMilestone.getMilestoneId(), descriptor.getDescId());
                    }
                }
                //Writing roles
                for (Iterator ri = project.getRoles().iterator(); ri.hasNext();)
                {
                    Role role = (Role) ri.next();
                    //Formulating role
                    Role templateRole = new Role();
                    templateRole.setRoleId(UuidGenerator.getInstance().getUuid());
                    templateRole.setProjectId(template.getTemplateId());
                    templateRole.setRoleName(role.getRoleName());
                    templateRole.setRoleDescription(role.getRoleDescription());
                    templateRole.setCompetencies(role.getCompetencies());
                    handler.addRole(templateRole);
                }
            }
            init();
            forward = new Forward(FORWARD_SUCESSFUL);
        }
        catch (WormsException e)
        {
            forward = new Forward(FORWARD_FAILED);
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        return forward;
    }

    /* Getters and Setters */
    public SelectBox getArchivedProject()
    {
        return archivedProject;
    }

    public void setArchivedProject(SelectBox archivedProject)
    {
        this.archivedProject = archivedProject;
    }

	public String getTemplateId()
	{
		return templateId;
	}

	public void setTemplateId(String templateId)
	{
		this.templateId = templateId;
	}
}
