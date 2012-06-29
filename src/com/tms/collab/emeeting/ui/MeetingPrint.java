package com.tms.collab.emeeting.ui;

import com.tms.collab.calendar.ui.CalendarEventView;
import com.tms.collab.emeeting.MeetingHandler;
import com.tms.collab.taskmanager.ui.FileListing;
import kacang.Application;
import kacang.util.Log;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Jan 8, 2004
 * Time: 3:26:58 PM
 * To change this template use Options | File Templates.
 */
public class MeetingPrint extends MeetingFormView
{
    public static final String DEFAULT_TEMPLATE = "emeeting/meetingPrint";

    public void init()
    {
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
        }

        fileListing = new FileListing("filelisting");
        addChild(fileListing);
        fileListing.setDownloadable(false);
        fileListing.setFolderId("/" + MeetingHandler.MEETING_FILES_FOLDER + "/" + eventId);
        fileListing.refresh();

    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }
}
