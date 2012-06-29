package com.tms.collab.project.ui;

import com.tms.collab.project.Project;
import com.tms.collab.project.Template;
import com.tms.collab.project.WormsException;
import com.tms.collab.project.WormsHandler;
import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.DatePopupField;
import kacang.stdui.Label;
import kacang.stdui.SelectBox;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class ProjectFormAdd extends ProjectForm
{
    public static final String DEFAULT_SELECTION = "-1";

    protected SelectBox templates;
    protected DatePopupField startDate;
    protected String projectId;

    public void init()
    {
        templates = new SelectBox("templates");
		startDate = new DatePopupField("startDate");
		startDate.setOptional(true);
        addChild(new Label("labelTemplate", Application.getInstance().getResourceBundle().getString("project.label.projectTemplate")));
        addChild(templates);
		addChild(new Label("labelStartDate", Application.getInstance().getResourceBundle().getString("project.label.startOn")+" *"));
		addChild(startDate);
        templates.setSelectedOptions(new String[] {DEFAULT_SELECTION});
        super.init();
		owner.setSelectedOption(((User)getWidgetManager().getUser()).getId());
    }

	public Forward onSubmit(Event event)
	{
		Forward forward = super.onSubmit(event);
		if(!(DEFAULT_SELECTION.equals(templates.getSelectedOptions().keySet().iterator().next())))
		{
			if(startDate.getDate() == null)
			{
				setInvalid(true);
				startDate.setInvalid(true);
			}
		}
		else
			startDate.setInvalid(false);
		return forward;
	}

    public void onRequest(Event event)
    {
        try
        {
            WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
            Collection list = worms.getTemplates();
            Map options = new SequencedHashMap();
            options.put(DEFAULT_SELECTION, Application.getInstance().getResourceBundle().getString("project.label.selectATemplate"));
            for (Iterator i = list.iterator(); i.hasNext();)
            {
                Template template = (Template) i.next();
                options.put(template.getTemplateId(), template.getTemplateName());
            }
            templates.setOptionMap(options);
        }
        catch (WormsException e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        super.onRequest(event);
    }

    public Forward onValidate(Event event)
    {
        Forward forward = super.onValidate(event);
        if(!(isNameExists(projectName.getValue().toString(), null)))
        {
            WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
            try
            {
                String selected = (String) templates.getSelectedOptions().keySet().iterator().next();
                Project project = generateProject(null);
				projectId = project.getProjectId();
				if(DEFAULT_SELECTION.equals(selected))
					worms.addProject(project);
				else
				{
					Template template = worms.getTemplate(selected);
					worms.addProject(project, template, startDate.getDate(), getWidgetManager().getUser());
				}
                init();
                forward = new Forward(FORWARD_SUCESSFUL);
            }
            catch (WormsException e)
            {
                forward = new Forward(FORWARD_FAILED);
            }
        }
        else
        {
            forward = new Forward(FORWARD_FAILED);
            setInvalid(true);
            projectName.setInvalid(true);
            validName.setInvalid(true);
            validName.setText(Application.getInstance().getResourceBundle().getString("project.message.projectNameInUse"));
        }
        return forward;
    }

    public SelectBox getTemplates()
    {
        return templates;
    }

    public void setTemplates(SelectBox templates)
    {
        this.templates = templates;
    }

	public DatePopupField getStartDate()
	{
		return startDate;
	}

	public void setStartDate(DatePopupField startDate)
	{
		this.startDate = startDate;
	}

	public String getProjectId()
	{
		return projectId;
	}

	public void setProjectId(String projectId)
	{
		this.projectId = projectId;
	}
}
