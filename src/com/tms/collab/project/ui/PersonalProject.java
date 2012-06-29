package com.tms.collab.project.ui;

import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.SelectBox;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.util.Log;
import kacang.services.security.User;
import com.tms.collab.project.Project;
import com.tms.collab.project.WormsHandler;
import com.tms.collab.project.WormsException;

import java.util.Collection;
import java.util.Iterator;

public class PersonalProject extends Form
{
    public static final String DEFAULT_SELECT_VALUE = "-1";
    public static final String DEFAULT_TEMPLATE = "project/personalProject";

    private SelectBox projects;
    private Button daily;
    private Button monthly;
    private Button weekly;
    private String type;
    private Project project;
    private String documentLink;

    public void init()
    {

        super.init();
        projects = new SelectBox("projects");
        projects.setOnChange("submit();");
        projects.addOption(DEFAULT_SELECT_VALUE, Application.getInstance().getResourceBundle().getString("project.label.selectAProject"));
        project = null;
        addChild(projects);
        daily = new Button("daily","Daily");
        daily.setOnClick("submit()");
        addChild(daily);
        monthly = new Button("monthly","Monthly");
        monthly.setOnClick("submit()");
        addChild(monthly);
        weekly = new Button("weekly","Weekly");
        weekly.setOnClick("submit()");
        addChild(weekly);
    }

    public void onRequest(Event event)
    {
        super.onRequest(event);
        init();
        type="daily";
        //Updating project list        
        WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
        User user = getWidgetManager().getUser();
        try
        {
            Collection list = worms.getProjectsInvolved(user.getId());
            for (Iterator i = list.iterator(); i.hasNext();)
            {
                Project project = (Project) i.next();
                projects.addOption(project.getProjectId(), project.getProjectName());
            }
        }
        catch (WormsException e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
    }

    public Forward onSubmit(Event event)
    {
        Forward forward = super.onSubmit(event);
        String selectValue = (String) projects.getSelectedOptions().keySet().iterator().next();
        if(!(DEFAULT_SELECT_VALUE.equals(selectValue)))
        {
            try
            {
                WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
                project = worms.getProject(selectValue);
                if((daily.getAbsoluteName().equals(findButtonClicked(event)))){
                	type="daily";
                }else if((monthly.getAbsoluteName().equals(findButtonClicked(event)))){
                	type="monthly";
                }else if((weekly.getAbsoluteName().equals(findButtonClicked(event)))){
                	type="weekly";
                }
            }
            catch (WormsException e)
            {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }catch (Exception e)
            {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
        }
        else
            project = null;
        return forward;
    }

    public String getDefaultTemplate()
    {
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

    public SelectBox getProjects()
    {
        return projects;
    }

    public void setProjects(SelectBox projects)
    {
        this.projects = projects;
    }

    public String getProjectId()
    {
        String id = "";
        if(project != null)
            id = project.getProjectId();
        return id;
    }

    public void setProjectId(String projectId)
    {
        if(projectId != null || "".equals(projectId))
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
    }

    public String getProjectName()
    {
        String name = "";
        if(project != null)
            name = project.getProjectName();
        return name;
    }

	public String getDocumentLink()
	{
		return documentLink;
	}

	public void setDocumentLink(String documentLink)
	{
		this.documentLink = documentLink;
	}

	public Button getDaily() {
		return daily;
	}

	public void setDaily(Button daily) {
		this.daily = daily;
	}

	public Button getMonthly() {
		return monthly;
	}

	public void setMonthly(Button monthly) {
		this.monthly = monthly;
	}

	public Button getWeekly() {
		return weekly;
	}

	public void setWeekly(Button weekly) {
		this.weekly = weekly;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
