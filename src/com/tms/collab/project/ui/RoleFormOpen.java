package com.tms.collab.project.ui;

import com.tms.hr.competency.Competency;
import com.tms.collab.project.Role;
import com.tms.collab.project.WormsException;
import com.tms.collab.project.WormsHandler;
import kacang.Application;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;

import java.util.Iterator;
import java.util.Map;

public class RoleFormOpen extends RoleForm
{
    public Forward onValidate(Event event)
    {
        Forward forward = super.onValidate(event);
        WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
        try
        {
            worms.editRole(generateRole());
            forward = new Forward(FORWARD_SUCCESSFUL);
        }
        catch (WormsException e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        return forward;
    }

    protected void refresh()
    {
        super.refresh();
        if(!isKeyEmpty())
        {
            try
            {
                WormsHandler handler = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
                Role role = handler.getRole(roleId);
                roleName.setValue(role.getRoleName());
                roleDescription.setValue(role.getRoleDescription());
                Map competencies = new SequencedHashMap();
                for(Iterator i = role.getCompetencies().iterator(); i.hasNext();)
                {
                    Competency competency = (Competency) i.next();
                    competencies.put(competency.getCompetencyId(), competency.getCompetencyName());
                }
                roleCompetencies.setOptionMap(competencies);
                if(!currentlyTemplate)
                {
                    Map users = new SequencedHashMap();
                    for(Iterator i = role.getPersonnel().iterator(); i.hasNext();)
                    {
                        User user = (User) i.next();
                        users.put(user.getId(), user.getProperty("firstName") + " " + user.getProperty("lastName"));
                    }
                    roleUsers.setOptionMap(users);
                }
                setProjectId(role.getProjectId());
            }
            catch (WormsException e)
            {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
        }
    }

    protected boolean isKeyEmpty()
    {
        boolean empty = true;
        if(!(roleId == null || "".equals(roleId)))
            empty = false;
        return empty;
    }

    public void setRoleId(String roleId)
    {
        super.setRoleId(roleId);
        init();
    }
}
