package com.tms.collab.project.ui;

import com.tms.collab.project.*;
import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashMap;

public class RoleTable extends Form
{
    public static final String FORWARD_DELETE = "forward.worms.role.Delete";
    public static final String FORWARD_ADD = "forward.worms.role.Add";
    public static final String PREFIX_CHECK = "chk";
    public static final String DEFAULT_TEMPLATE = "project/roleTable";

    protected Project project;
    protected Template projectTemplate;
    protected Collection roles;
    protected boolean currentlyTemplate = false;
    protected HashMap roleMap;

    protected Button add;
    protected Button delete;

    public RoleTable()
    {
    }

    public RoleTable(String s)
    {
        super(s);
    }

    public void init()
    {
        roles = new ArrayList();
        if(!isKeyEmpty())
        {
            super.init();
            roleMap = new HashMap();
            add = new Button("add", Application.getInstance().getResourceBundle().getString("project.label.add"));
            delete = new Button("delete", Application.getInstance().getResourceBundle().getString("project.label.delete"));
            addChild(add);
            addChild(delete);
        }
    }

    public void onRequest(Event event)
    {
        if(!isKeyEmpty())
        {
            super.onRequest(event);
            try
            {
                WormsHandler handler = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
                roles = handler.getRolesByProject(getKey(), true);
                removeChildren();
                roleMap = new HashMap();
                for(Iterator i = roles.iterator(); i.hasNext();)
                {
                    Role role = (Role) i.next();
                    CheckBox checkbox = new CheckBox(PREFIX_CHECK + role.getRoleId());
                    roleMap.put(checkbox.getName(), role.getRoleName());
                    /*checkbox.setText(role.getRoleName());*/
                    addChild(checkbox);
                }
                addChild(add);
                addChild(delete);
            }
            catch (WormsException e)
            {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
        }
    }

    public Forward actionPerformed(Event event)
    {
        if(!isKeyEmpty())
            return super.actionPerformed(event);
        else
            return null;
    }

    public Forward onSubmit(Event event)
    {
        Forward forward = super.onSubmit(event);
        if(add.getAbsoluteName().equals(findButtonClicked(event)))
            forward = new Forward(FORWARD_ADD);
        else if(delete.getAbsoluteName().equals(findButtonClicked(event)))
        {
            WormsHandler handler = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
            Collection deleted = new ArrayList();
            for(Iterator i = roles.iterator(); i.hasNext();)
            {
                Role role = (Role) i.next();
                if(childMap.containsKey(PREFIX_CHECK + role.getRoleId()))
                {
                    CheckBox checkBox = (CheckBox) childMap.get(PREFIX_CHECK + role.getRoleId());
                    if(checkBox.isChecked())
                    {
                        try
                        {
                            deleted.add(role);
                            handler.deleteRole(role.getRoleId());
                        }
                        catch (WormsException e)
                        {
                            Log.getLog(getClass()).error(e.getMessage(), e);
                        }
                    }
                }
            }
            for(Iterator i = deleted.iterator(); i.hasNext();)
            {
                Role role = (Role) i.next();
                roles.remove(role);
            }
        }
        return forward;
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    /* Getters and Setters */
    public Button getAdd()
    {
        return add;
    }

    public void setAdd(Button add)
    {
        this.add = add;
    }

    public Button getDelete()
    {
        return delete;
    }

    public void setDelete(Button delete)
    {
        this.delete = delete;
    }

    public String getProjectId()
    {
        if(project != null)
            return project.getProjectId();
        return null;
    }

    public void setProjectId(String projectId)
    {
        try
        {
            WormsHandler handler = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
            project = handler.getProject(projectId);
            init();
            currentlyTemplate = false;
        }
        catch (WormsException e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
    }

    public Collection getRoles()
    {
        return roles;
    }

    public void setRoles(Collection roles)
    {
        this.roles = roles;
    }

    public Project getProject()
    {
        return project;
    }

    public void setProject(Project project)
    {
        this.project = project;
    }

    public boolean isCurrentlyTemplate()
    {
        return currentlyTemplate;
    }

    public void setTemplateId(String templateId)
    {
        try
        {
            WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
            projectTemplate = worms.getTemplate(templateId);
            currentlyTemplate = true;
            init();
        }
        catch (WormsException e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
    }

    public String getTemplateId()
    {
        if(projectTemplate != null)
            return projectTemplate.getTemplateId();
        return null;
    }

    public void setProjectTemplate(Template projectTemplate)
    {
        this.projectTemplate = projectTemplate;
    }

    public Template getProjectTemplate()
    {
            return projectTemplate;
        }

    public HashMap getRoleMap()
    {
        return roleMap;
    }

    public void setRoleMap(HashMap roleMap)
    {
        this.roleMap = roleMap;
    }

    protected boolean isKeyEmpty()
    {
        boolean empty = true;
        if(currentlyTemplate)
        {
            if(projectTemplate != null)
               empty = false;
        }
        if(project != null)
            empty = false;
        return empty;
    }

    protected String getKey()
    {
        if(currentlyTemplate)
        {
            if(projectTemplate != null)
                return projectTemplate.getTemplateId();
        }
        else if(project != null)
            return project.getProjectId();
        return null;
    }
}
