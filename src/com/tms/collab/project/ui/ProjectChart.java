package com.tms.collab.project.ui;

import kacang.ui.Forward;
import kacang.ui.LightWeightWidget;
import kacang.ui.Event;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.ButtonGroup;
import kacang.stdui.Radio;
import kacang.util.Log;
import com.tms.collab.project.*;
import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.taskmanager.model.TaskManager;

import java.util.*;

public class ProjectChart extends LightWeightWidget
{
    public static final String DEFAULT_TEMPLATE = "project/projectChart";
    public static final String DEFAULT_ATTRIBUTE = "projectChart";

    public static final String PROPERTY_START_DATE = "startDate";
    public static final String PROPERTY_END_DATE = "endDate";
    public static final String PROPERTY_DURATION = "duration";
    public static final String PROPERTY_PROJECT_PROGRESS = "projectProgress";
    public static final String PROPERTY_MILESTONE_END = "milestoneEnd_";

    protected Project project;
    protected boolean expandAll;
    protected boolean hideDetails = true;
    protected HashMap properties;
    protected String documentLink;
    protected String type;
    
    protected Radio[] view;
    protected ButtonGroup group;
    protected String viewType;

    public void onRequest(Event event)
    {
 	
    	String radio=event.getRequest().getParameter("view");
    	if("A".equals(radio)){
    		viewType="all"; 
    	}else if("P".equals(radio)){
    		viewType="partial"; 
    	}else if("H".equals(radio)){
    		viewType="hide"; 
    	}else{
    		viewType="all"; 
    	}
        super.onRequest(event);
        if(!(project == null))
        {
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
            TaskManager manager = (TaskManager) Application.getInstance().getModule(TaskManager.class);
            properties = new HashMap();

            boolean isAdmin = false;
            boolean isInvolved = false;
            User user = service.getCurrentUser(event.getRequest());
            try
            {
                isAdmin = worms.hasProjectPermission(project.getProjectId(), user.getId());
                isInvolved = worms.hasProjectInvolvement(project.getProjectId(), user.getId());
                project.setMilestones(worms.getMilestonesByProjectChart(project, true));
                if(!(isAdmin || isInvolved))
                    project = null;
                else
                {
                    Date startDate = worms.getProjectStart(project.getProjectId());
                    Date endDate = worms.getProjectEnd(project.getProjectId());
                    Calendar start = Calendar.getInstance();
                    Calendar end = Calendar.getInstance();
                    if(startDate != null)
                        start.setTime(startDate);
                    if(endDate != null)
                        end.setTime(endDate);

                    float total = 0;
                    for (Iterator i = project.getMilestones().iterator(); i.hasNext();)
                    {
                        Milestone milestone = (Milestone) i.next();
                        properties.put(PROPERTY_MILESTONE_END + milestone.getMilestoneId(), worms.getMilestoneEnd(milestone.getMilestoneId()));
                        Collection taskId = new ArrayList();
                        for (Iterator it = milestone.getTasks().iterator(); it.hasNext();)
                        {
                            Task task = (Task) it.next();
                            taskId.add(task.getId());
                        }
                        if(taskId.size() > 0)
                        {
                            float progress = manager.getAverageProgress((String[]) taskId.toArray(new String[] {}));
                            if(progress == -1)
                                progress = 0;
                            total += (progress*milestone.getMilestoneProgress())/100;
                            properties.put(milestone.getMilestoneId(), new Float(progress));
                        }
                        else
                            properties.put(milestone.getMilestoneId(), new Float(0));
                    }

                    properties.put(PROPERTY_START_DATE, startDate);
                    properties.put(PROPERTY_END_DATE, endDate);
                    /*properties.put(PROPERTY_DURATION, new Integer((end.get(Calendar.DAY_OF_YEAR)-start.get(Calendar.DAY_OF_YEAR)) + 1));*/
                    if("monthly".equals(type))
                    	properties.put(PROPERTY_DURATION, new Integer(WormsUtil.getMonthsBetween(start.getTime(), end.getTime())));
                    else if("weekly".equals(type))
                    	properties.put(PROPERTY_DURATION, new Integer(WormsUtil.getWeeksBetween(start.getTime(), end.getTime())));                    
                    else
					properties.put(PROPERTY_DURATION, new Integer(WormsUtil.getDaysBetween(start.getTime(), end.getTime())));
                    properties.put(PROPERTY_PROJECT_PROGRESS, new Float(total));

                    event.getRequest().setAttribute(DEFAULT_ATTRIBUTE, this);
                }
            }
            catch(Exception e)
            {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
        }
    }
    
    public String getDefaultTemplate()
    {
    	if("monthly".equals(type))
    		return "project/projectChartMonthly";
    	else if("weekly".equals(type))
    		return "project/projectChartWeekly";
    	else
    		return DEFAULT_TEMPLATE;
    }

    /* Getters and Setters */
    public Project getProject()
    {
        return project;
    }

    public void setProject(Project project)
    {
        this.project = project;
    }

    public void setProjectId(String projectId)
    {
        try
        {
            WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
            project = worms.getProject(projectId);
        }
        catch (WormsException e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
    }

    public boolean isExpandAll()
    {
        return expandAll;
    }

    public void setExpandAll(boolean expandAll)
    {
        this.expandAll = expandAll;
    }

    public HashMap getProperties()
    {
        return properties;
    }

    public void setProperties(HashMap properties)
    {
        this.properties = properties;
    }

    public boolean isHideDetails()
    {
        return hideDetails;
    }

    public void setHideDetails(boolean hideDetails)
    {
        this.hideDetails = hideDetails;
    }

	public String getDocumentLink()
	{
		return documentLink;
	}

	public void setDocumentLink(String documentLink)
	{
		this.documentLink = documentLink;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ButtonGroup getGroup() {
		return group;
	}

	public void setGroup(ButtonGroup group) {
		this.group = group;
	}

	public Radio[] getView() {
		return view;
	}

	public void setView(Radio[] view) {
		this.view = view;
	}

	public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}
	
}
