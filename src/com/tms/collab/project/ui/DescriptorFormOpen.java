package com.tms.collab.project.ui;

import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;
import kacang.util.Log;
import com.tms.collab.project.WormsHandler;
import com.tms.collab.project.TaskDescriptor;
import com.tms.collab.project.WormsException;
import com.tms.collab.project.Milestone;

public class DescriptorFormOpen extends DescriptorForm
{
	public Forward onValidate(Event event)
	{
		Forward forward = super.onValidate(event);
		WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
		TaskDescriptor descriptor = generateDescriptor();
		try
		{
			worms.updateDescriptor(descriptor);
			forward = new Forward(FORWARD_SUCCESS);
		}
		catch (WormsException e)
		{
			Log.getLog(getClass()).error("Error while updating new task descriptor " + descriptor.getDescName(), e);
		}
        return forward;
	}

	protected boolean isKeyEmpty()
	{
		if(!(descId == null || "".equals(descId)))
			return false;
		return true;
	}

	public void setDescId(String descId)
	{
		super.setDescId(descId);
		init();
		try
		{
			WormsHandler handler = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
			TaskDescriptor descriptor = handler.getDescriptor(descId);
			Milestone object = handler.getMilestone(descriptor.getMilestoneId());
			setProjectId(object.getProjectId());
			//Initializing values
			milestone.setSelectedOption(descriptor.getMilestoneId());
			descName.setValue(descriptor.getDescName());
			descDescription.setValue(descriptor.getDescDescription());
			descStart.setValue(String.valueOf(descriptor.getDescStart()));
			descDuration.setValue(String.valueOf(descriptor.getDescDuration()));
		}
		catch (WormsException e)
		{
			Log.getLog(getClass()).error("Error while retrieving task descriptor " + descId, e);
		}

	}
}
