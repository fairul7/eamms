package com.tms.collab.project.ui;

import com.tms.collab.project.WormsException;
import com.tms.collab.project.WormsHandler;
import kacang.Application;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

public class RoleFormAdd extends RoleForm
{
    public Forward onValidate(Event event)
    {
        Forward forward = super.onValidate(event);
        WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
        try
        {
            worms.addRole(generateRole());
            forward = new Forward(FORWARD_SUCCESSFUL);
        }
        catch (WormsException e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        return forward;
    }

    protected boolean isKeyEmpty()
    {
        boolean empty = true;
        if(!(projectId == null || "".equals(projectId)))
            empty = false;
        return empty;
    }

    public void setProjectId(String projectId)
    {
        super.setProjectId(projectId);
        init();
    }
}
