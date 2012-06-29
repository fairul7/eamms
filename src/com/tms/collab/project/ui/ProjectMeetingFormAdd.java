package com.tms.collab.project.ui;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.stdui.SelectBox;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.calendar.model.CalendarModule;
import com.tms.collab.emeeting.Meeting;
import com.tms.collab.emeeting.MeetingHandler;
import com.tms.collab.emeeting.ui.MeetingFormAdd;
import com.tms.collab.project.Milestone;
import com.tms.collab.project.Project;
import com.tms.collab.project.WormsException;
import com.tms.collab.project.WormsHandler;

public class ProjectMeetingFormAdd extends MeetingFormAdd
{
	public static final String DEFAULT_TEMPLATE = "project/meetingForm";
	public static final String FORWARD_SUCCESSFUL = "forward_success";
	public static final String FORWARD_FAILED = "forward_failed";

    protected SelectBox milestones;
    protected String projectId;

	public ProjectMeetingFormAdd()
	{
		super();
	}

	public ProjectMeetingFormAdd(String name)
	{
		super(name);
	}

	public void init()
	{
		if(!isKeyEmpty())
		{
			super.init();
			milestones = new SelectBox("milestone");
			milestones.setSelectedOption("-1");
			addChild(milestones);
		}
	}

	public Forward onSubmit(Event evt)
	{
		Forward forward = super.onSubmit(evt);
		if(!isKeyEmpty())
		{
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
				Collection milestones = handler.getMilestonesByProject(projectId, false);
				Map map = new SequencedHashMap();
				map.put("-1", Application.getInstance().getMessage("project.label.selectAMilestone","Select A Milestone"));
				for (Iterator i = milestones.iterator(); i.hasNext();)
				{
					Milestone milestone = (Milestone) i.next();
					map.put(milestone.getMilestoneId(), milestone.getMilestoneName());
				}
				this.milestones.setOptionMap(map);
			}
			catch (WormsException e)
			{
				Log.getLog(getClass()).error("Error while retriving milestones", e);
			}
		}
	}

	protected Forward createEvent(Event evt) throws RuntimeException
    {
        try
        {
            CalendarModule module = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
            MeetingHandler handler = (MeetingHandler) Application.getInstance().getModule(MeetingHandler.class);
			WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);

            CalendarEvent event = assembleEvent();
            String userId = getWidgetManager().getUser().getId();
            String eventId = event.getEventId();
            String prefix = Meeting.class.getName();
            if(!eventId.substring(0, prefix.length()).equals(prefix))
                event.setEventId(prefix + "_" + eventId);

            meeting = assembleMeeting(event);
            String temp = notifyNote.getValue().toString();
            meeting.setNotify(temp);
            
            handler.addMeeting(meeting, userId, true);
			sendNotification(meeting,evt);

            Collection col = generateRecurringEvent(event);
            for(Iterator i = col.iterator(); i.hasNext();)
                module.addRecurringEvent((CalendarEvent) i.next(), userId, true);
            worms.addMilestoneMeeting((String) milestones.getSelectedOptions().keySet().iterator().next(), meeting.getEventId());
            init();
            return new Forward(FORWARD_SUCCESSFUL);
        }
        catch(Exception e)
        {
            throw new RuntimeException("Error while creating event", e);
        }
    }

	protected boolean isKeyEmpty()
	{
		if(!(projectId == null || "".equals(projectId)))
			return false;
		return true;
	}

	public String getDefaultTemplate()
	{
		return DEFAULT_TEMPLATE;
	}

	protected Meeting assembleMeeting(CalendarEvent event)
	{
		Meeting meeting = super.assembleMeeting(event);
		try
		{
			WormsHandler handler = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
			Project project = handler.getProject(projectId);
			if(!(project == null))
				meeting.setCategory(project.getProjectName());
		}
		catch (WormsException e)
		{
			Log.getLog(getClass()).error("Error while retrieving project", e);
		}
		return meeting;
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

	public String getProjectId()
	{
		return projectId;
	}

	public void setProjectId(String projectId)
	{
		this.projectId = projectId;
		init();
	}
}
