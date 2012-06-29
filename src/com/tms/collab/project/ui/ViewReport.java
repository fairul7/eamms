package com.tms.collab.project.ui;

import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.Form;
import kacang.stdui.SelectBox;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.collab.project.Project;
import com.tms.collab.project.WormsException;
import com.tms.collab.project.WormsHandler;

public class ViewReport extends Form
{
    public static final String DEFAULT_SELECT_VALUE = "-1";
    public static final String DEFAULT_TEMPLATE = "project/allProject";

    private SelectBox projects;

    public void init()
    {
        super.init();
        projects = new SelectBox("projects");
        projects.setOnChange("submit();");
        projects.addOption(DEFAULT_SELECT_VALUE, Application.getInstance().getResourceBundle().getString("project.label.selectAProject"));
        addChild(projects);
    }

    public void onRequest(Event event)
    {
        super.onRequest(event);
        init();
        //Updating project list
        WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
        try
        {
            Collection list = worms.getProjects();
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
        	return new Forward("view","/ekms/worms/projectPerformaceMetricReport.jsp?projectId="+selectValue,true);
        }
        else
            return null;
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    

    public SelectBox getProjects()
    {
        return projects;
    }

    public void setProjects(SelectBox projects)
    {
        this.projects = projects;
    }   
}

