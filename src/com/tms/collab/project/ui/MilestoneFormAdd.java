package com.tms.collab.project.ui;

import com.tms.collab.project.WormsException;
import com.tms.collab.project.WormsHandler;
import kacang.Application;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

public class MilestoneFormAdd extends MilestoneForm
{
    protected boolean isKeyEmpty()
    {
        if(!(projectId == null || "".equals(projectId)))
            return false;
        return true;
    }

    public Forward onValidate(Event event)
    {
        Forward forward = super.onValidate(event);
        try
        {
            WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
            worms.addMilestone(generateMilestone());
            init();
            forward = new Forward(FORWARD_SUCCESS);
        }
        catch (WormsException e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        return forward;
    }

    public void setProjectId(String projectId)
    {
        super.setProjectId(projectId);
        init();
    }
}
