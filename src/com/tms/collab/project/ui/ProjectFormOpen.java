package com.tms.collab.project.ui;

import com.tms.cms.core.model.DefaultContentObject;
import com.tms.collab.calendar.model.Attendee;
import com.tms.collab.project.Project;
import com.tms.collab.project.ProjectMember;
import com.tms.collab.project.WormsException;
import com.tms.collab.project.WormsHandler;
import kacang.Application;
import kacang.stdui.Button;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class ProjectFormOpen extends ProjectForm
{
    protected String projectId;
    
    public void init()
    {
        if(!isEmptyProjectId())
            super.init();       
    }

    public void onRequest(Event event)
    {
        if(!isEmptyProjectId())
        {
            try
            {
                super.onRequest(event);
                WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
                Project project = worms.getProject(projectId);
                projectName.setValue(project.getProjectName());
                projectDescription.setValue(project.getProjectDescription());
                DecimalFormat format = new DecimalFormat("#######0.00");
                projectValue.setValue(format.format(project.getProjectValue()));
                projectCurrencyType.setSelectedOption(project.getProjectCurrencyType());
                projectCategory.setSelectedOptions(new String[] {project.getProjectCategory()});
                client.setValue(project.getClientName());
                owner.setSelectedOptions(new String[] {project.getOwnerId()});
				if(project.isArchived())
					archived.setChecked(true);
				else
					archived.setChecked(false);
				projectSummary.setValue(project.getProjectSummary());
                /* Set working days */
				Map working = new SequencedHashMap();
				Map notWorking = new SequencedHashMap();

                notWorking.put("1", Application.getInstance().getResourceBundle().getString("general.label.sunday"));
		        notWorking.put("7", Application.getInstance().getResourceBundle().getString("general.label.saturday"));
		        notWorking.put("2", Application.getInstance().getResourceBundle().getString("general.label.monday"));
		        notWorking.put("3", Application.getInstance().getResourceBundle().getString("general.label.tuesday"));
		        notWorking.put("4", Application.getInstance().getResourceBundle().getString("general.label.wednesday"));
		        notWorking.put("5", Application.getInstance().getResourceBundle().getString("general.label.thursday"));
		        notWorking.put("6", Application.getInstance().getResourceBundle().getString("general.label.friday"));

				for (Iterator i = project.getProjectWorking().iterator(); i.hasNext();)
				{
					String day = (String) i.next();
					if("1".equals(day))
						working.put("1", Application.getInstance().getResourceBundle().getString("general.label.sunday"));
					else if("2".equals(day))
						working.put("2", Application.getInstance().getResourceBundle().getString("general.label.monday"));
					else if("3".equals(day))
						working.put("3", Application.getInstance().getResourceBundle().getString("general.label.tuesday"));
					else if("4".equals(day))
						working.put("4", Application.getInstance().getResourceBundle().getString("general.label.wednesday"));
					else if("5".equals(day))
						working.put("5", Application.getInstance().getResourceBundle().getString("general.label.thursday"));
					else if("6".equals(day))
						working.put("6", Application.getInstance().getResourceBundle().getString("general.label.friday"));
					else if("7".equals(day))
						working.put("7", Application.getInstance().getResourceBundle().getString("general.label.saturday"));
					notWorking.remove(day);
				}
                projectWorkingDays.setLeftValues(notWorking);
				projectWorkingDays.setRightValues(working);
				/* Populating files */
				for (Iterator i = project.getFiles().iterator(); i.hasNext();)
				{
					DefaultContentObject document = (DefaultContentObject) i.next();
					projectFiles.addOption(document.getId(), document.getName());
				}
				if(project.getMembers()!=null){
					ProjectMember pm;
	                Collection memberList = new ArrayList();
	                for(Iterator i= project.getMembers().iterator();i.hasNext();){
	                	pm = (ProjectMember)i.next();
	                    memberList.add(pm.getMemberId());
	                }
	                String[] memberIds = (String[])memberList.toArray(new String[0]);
	                projectMembers.setIds(memberIds);
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
        if(!isEmptyProjectId())
            forward = super.actionPerformed(event);
        return forward;
    }

    public Forward onValidate(Event event)
    {
        Forward forward = super.onValidate(event);
        if(!isEmptyProjectId())
        {
            if(!(isNameExists(projectName.getValue().toString(), projectId)))
            {
                WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
                Project project = generateProject(projectId);
                try
                {
                    worms.editProject(project);
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
        }
        return forward;
    }
    
    public String getProjectId()
    {
        return projectId;
    }

    public void setProjectId(String projectId)
    {
        this.projectId = projectId;
        init();
        refresh();
    }

    protected boolean isEmptyProjectId()
    {
        boolean empty = true;
        if(!(projectId == null || "".equals(projectId)))
            empty = false;
        return empty;
    }

	public boolean isEditMode() {
		return true;
	}
}
