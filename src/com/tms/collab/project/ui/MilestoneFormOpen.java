package com.tms.collab.project.ui;

import com.tms.collab.project.Milestone;
import com.tms.collab.project.WormsException;
import com.tms.collab.project.WormsHandler;
import kacang.Application;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

public class MilestoneFormOpen extends MilestoneForm
{
    protected Milestone milestone;

    protected boolean isKeyEmpty()
    {
        if(!(milestoneId == null || "".equals(milestoneId)))
            return false;
        return true;
    }

    public Forward onValidate(Event event)
    {
        Forward forward = super.onValidate(event);
        try
        {
            WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
            Milestone milestone = generateMilestone();
            Milestone original = worms.getMilestone(milestone.getMilestoneId());
            milestone.setMilestoneOrder(original.getMilestoneOrder());
            worms.editMilestone(milestone);
            forward = new Forward(FORWARD_SUCCESS);
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
            milestoneName.setValue(milestone.getMilestoneName());
            milestoneDescription.setValue(milestone.getMilestoneDescription());
            milestoneProgress.setValue(Integer.toString(milestone.getMilestoneProgress()));
        }
    }

    public void setMilestoneId(String milestoneId)
    {
        super.setMilestoneId(milestoneId);
        try
        {
            WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
            milestone = worms.getMilestone(milestoneId);
            projectId = milestone.getProjectId();
        }
        catch (WormsException e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        init();
    }

    public Milestone getMilestone()
    {
        return milestone;
    }

    public void setMilestone(Milestone milestone)
    {
        this.milestone = milestone;
    }
}
