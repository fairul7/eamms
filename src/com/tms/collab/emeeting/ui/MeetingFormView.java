package com.tms.collab.emeeting.ui;

import com.tms.collab.calendar.ui.CalendarEventView;
import com.tms.collab.calendar.model.CalendaringPermission;
import com.tms.collab.calendar.model.CalendarModule;
import com.tms.collab.emeeting.Meeting;
import com.tms.collab.emeeting.MeetingHandler;
import com.tms.collab.emeeting.MeetingException;
import com.tms.collab.taskmanager.ui.FileListing;
import kacang.ui.Widget;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.SecurityException;

import java.util.Collection;

public class MeetingFormView extends CalendarEventView implements CalendaringPermission
{
    public static final String DEFAULT_TEMPLATE = "emeeting/meetingView";
    public static final String DEFAULT_ACTION_ITEM_PREFIX = "ActionItems";
    public static final String EVENT_TYPE_DELETE = "delete";
    public static final String FORWARD_DELETE_SUCCESSFUL = "delete successfully";

    protected Meeting meeting;
    protected Collection relatedMeetings;
    protected FileListing fileListing;

    public MeetingFormView()
    {
    }

    public MeetingFormView(String name)
    {
        super(name);
    }

    public void init()
    {
        CalendarEventView parent = (CalendarEventView) getParent();
        if(parent != null)
            eventId = parent.getEventId();
        if(eventId != null || "".equals(eventId))
        {
            try
            {
                removeChildren();
                MeetingHandler handler = (MeetingHandler) Application.getInstance().getModule(MeetingHandler.class);
                setMeeting(handler.getMeeting(eventId, true));
                if((meeting.getCategory() != null) && (! "".equals(meeting.getCategory())) && (! MeetingForm.NO_CATEGORY.equals(meeting.getCategory())))
                    relatedMeetings = handler.getRelatedMeeting(meeting.getCategory(), false);
            }
            catch(Exception e)
            {
                Log.getLog(MeetingFormView.class).error(e);
            }

            fileListing = new FileListing("filelisting");
            addChild(fileListing);
            fileListing.setDownloadable(true);
            fileListing.setFolderId("/" + MeetingHandler.MEETING_FILES_FOLDER + "/" + eventId);
            fileListing.refresh();
        }
    }

    public Forward actionPerformed(Event event)
    {
        String type = event.getType();
        if(EVENT_TYPE_DELETE.equals(type)&&meeting!=null){
            MeetingHandler mh = (MeetingHandler)Application.getInstance().getModule(MeetingHandler.class);
            try
            {
                mh.deleteMeeting(meeting.getEventId(),getWidgetManager().getUser().getId());
                return new Forward(FORWARD_DELETE_SUCCESSFUL);
            } catch (MeetingException e)
            {
                Log.getLog(MeetingFormView.class).error(e);
            }

        }
        return super.actionPerformed(event);
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    public void onRequest(Event evt)
    {
        //super.onRequest(evt);
        init();
        setUserId(getWidgetManager().getUser().getId());
    }

    //Getter and Setters
    public Meeting getMeeting()
    {
        return meeting;
    }

    public void setMeeting(Meeting meeting)
    {
        this.meeting = meeting;
    }

    public void setEventId(String eventId)
    {
        this.eventId = eventId;
        init();
    }

    public Widget getEventView()
    {
        return this;
    }

    public Collection getRelatedMeetings()
    {
        return relatedMeetings;
    }

    public void setRelatedMeetings(Collection relatedMeetings)
    {
        this.relatedMeetings = relatedMeetings;
    }

    public FileListing getFileListing()
    {
        return fileListing;
    }

    public void setFileListing(FileListing fileListing)
    {
        this.fileListing = fileListing;
    }

    public boolean hasEditPermission(String userId)
    {
        if(meeting!=null && meeting.getEvent().getUserId().equals(userId))
            return true;
        SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
        try
        {
            if(ss.hasPermission(userId,CalendarModule.PERMISSION_MANAGE_EVENTS,null,null) ||ss.hasPermission(userId,CalendarModule.PERMISSION_EDIT_EVENTS,null,null))
                return true;
        } catch (SecurityException e)
        {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        }
        return false;
    }

    public boolean hasDeletePermission(String userId)
    {
        if(meeting!=null && meeting.getEvent().getUserId().equals(userId))
            return true;
        SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
        try
        {
            if(ss.hasPermission(userId,CalendarModule.PERMISSION_MANAGE_EVENTS,null,null)
                    ||ss.hasPermission(userId,CalendarModule.PERMISSION_DELETE_EVENTS,null,null))
                return true;
        } catch (SecurityException e)
        {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        }
        return false;
    }

    public boolean isEditable(){
        return hasEditPermission(getWidgetManager().getUser().getId());
    }

    public boolean isDeletable(){
        return hasDeletePermission(getWidgetManager().getUser().getId());
    }

}
