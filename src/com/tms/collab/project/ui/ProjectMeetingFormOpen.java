package com.tms.collab.project.ui;

import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.calendar.model.CalendarModule;
import com.tms.collab.emeeting.MeetingHandler;
import com.tms.collab.emeeting.ui.MeetingFormEdit;
import com.tms.collab.project.Milestone;
import com.tms.collab.project.WormsException;
import com.tms.collab.project.WormsHandler;
import kacang.Application;
import kacang.stdui.SelectBox;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class ProjectMeetingFormOpen extends MeetingFormEdit
{
	public static final String DEFAULT_TEMPLATE = "project/meetingFormEdit";
	public static final String FORWARD_SUCCESSFUL = "forward_success";
	public static final String FORWARD_FAILED = "forward_failed";

	protected SelectBox milestones;
	protected String projectId;
	protected String meetingId;

	public ProjectMeetingFormOpen()
	{
		super();
	}

	public ProjectMeetingFormOpen(String name)
	{
		super(name);
	}

	public void init()
	{
		if(!isKeyEmpty())
		{
			super.init();
			milestones = new SelectBox("milestone");
			addChild(milestones);
		}
	}

	public void onRequest(Event event)
	{
		if(!isKeyEmpty())
		{
			super.onRequest(event);
			milestones.setInvalid(false);
			setInvalid(false);
			try
			{
				WormsHandler handler = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
				Milestone milestone = handler.getMilestoneByMeeting(meetingId);
				Collection list = handler.getMilestonesByProject(milestone.getProjectId(), false);
				Map map = new SequencedHashMap();
				map.put("-1", Application.getInstance().getMessage("project.label.selectAMilestone","Select A Milestone"));
				for (Iterator i = list.iterator(); i.hasNext();)
				{
					Milestone object = (Milestone) i.next();
					map.put(object.getMilestoneId(), object.getMilestoneName());
				}
				milestones.setOptionMap(map);
                milestones.setSelectedOption(milestone.getMilestoneId());
			}
			catch (WormsException e)
			{
				Log.getLog(getClass()).error("Error while retriving milestones", e);
			}
		}
	}

	public Forward onSubmit(Event evt)
	{
		Forward forward = null;
		if(!isKeyEmpty())
		{
			forward = super.onSubmit(evt);
            String milestoneId = (String) milestones.getSelectedOptions().keySet().iterator().next();
			if(milestoneId == null || "".equals(milestoneId) || "-1".equals(milestoneId))
			{
				milestones.setInvalid(true);
				setInvalid(true);
				forward = new Forward(FORWARD_FAILED);
			}
		}
		return forward;
	}

	public Forward actionPerformed(Event evt)
	{
		Forward forward = null;
		if(!isKeyEmpty())
		{
			forward = super.actionPerformed(evt);
		}
		return forward;
	}

	protected Forward createEvent(Event evt) throws RuntimeException
	{
		Forward forward = null;
		if(!isKeyEmpty())
		{
			try
            {
                CalendarModule module = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
                MeetingHandler handler = (MeetingHandler) Application.getInstance().getModule(MeetingHandler.class);
				WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
                String userId = getWidgetManager().getUser().getId();

                CalendarEvent event = assembleEvent();
                meeting = assembleMeeting(event);

                handler.editMeeting(meeting, userId, true);
                Collection col = generateRecurringEvent(event);
                for(Iterator i = col.iterator(); i.hasNext();)
                    module.addRecurringEvent((CalendarEvent) i.next(), userId, true);
				/* Updating milestone mapping */
				Milestone milestone = worms.getMilestoneByMeeting(meetingId);
				worms.deleteMilestoneMeeting(milestone.getMilestoneId(), meetingId);
				worms.addMilestoneMeeting((String) milestones.getSelectedOptions().keySet().iterator().next(), meetingId);

                init();
                forward = new Forward(FORWARD_SUCCESSFUL);
            }
            catch(Exception e)
			{
            	Log.getLog(getClass()).error("Error while updating meeting", e);
            }
		}
		return forward;
	}

	protected boolean isKeyEmpty()
	{
		if(!(meetingId == null || "".equals(meetingId)))
			return false;
		return true;
	}

	public String getDefaultTemplate()
	{
		if(uploadFile)
            return DEFAULT_TEMPLATE_UPLOAD;
        else
            return DEFAULT_TEMPLATE;
	}

	/* Getters and Setters */
	public SelectBox getMilestones()
	{
		return milestones;
	}

	public void setMilestones(SelectBox milestones)
	{
		this.milestones = milestones;
	}

	public String getMeetingId()
	{
		return meetingId;
	}

	public void setMeetingId(String meetingId)
	{
		this.meetingId = meetingId;
		setEventId(meetingId);
		/* Setting projectId */
		try
		{
			WormsHandler handler = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
			Milestone milestone = handler.getMilestoneByMeeting(meetingId);
			projectId = milestone.getProjectId();
		}
		catch (WormsException e)
		{
			Log.getLog(getClass()).error("Error while retrieving meeting milestone", e);
		}
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
