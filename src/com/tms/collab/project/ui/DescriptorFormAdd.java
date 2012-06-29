package com.tms.collab.project.ui;

import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;
import kacang.util.Log;
import com.tms.collab.project.WormsHandler;
import com.tms.collab.project.WormsException;
import com.tms.collab.project.TaskDescriptor;

public class DescriptorFormAdd extends DescriptorForm
{
	public Forward onValidate(Event event)
	{
		Forward forward = super.onValidate(event);
		WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
		TaskDescriptor descriptor = generateDescriptor();
		try
		{
			worms.addDescriptor(descriptor);
			worms.addMilestoneTask(descriptor.getMilestoneId(), descriptor.getDescId());
			init();
			refresh();
			forward = new Forward(FORWARD_SUCCESS);
		}
		catch (WormsException e)
		{
			Log.getLog(getClass()).error("Error while adding new task descriptor " + descriptor.getDescName(), e);
		}
        return forward;
	}

	public void setProjectId(String projectId)
	{
		super.setProjectId(projectId);
		init();
	}
}
