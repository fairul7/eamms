package com.tms.collab.emeeting.ui;

import com.tms.collab.emeeting.MeetingHandler;
import com.tms.collab.emeeting.Meeting;
import com.tms.collab.emeeting.MeetingException;
import com.tms.collab.calendar.model.*;
import com.tms.collab.resourcemanager.model.ResourceManager;
import com.tms.collab.resourcemanager.model.Resource;
import com.tms.collab.resourcemanager.model.ResourceMailer;
import kacang.Application;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorIn;
import kacang.model.operator.DaoOperator;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.collections.SequencedHashMap;

public class MeetingFormEdit extends MeetingForm
{
    public static final String DEFAULT_TEMPLATE = "emeeting/meetingEdit";
    public static final String DEFAULT_TEMPLATE_UPLOAD = "taskmanager/taskuploadfileform";
    private String notifyNoteValue;
    private String newflag;

    public MeetingFormEdit()
    {
    }

    public MeetingFormEdit(String name)
    {
        super(name);
    }

    public void onRequest(Event event)
    {
        //eventId = event.getRequest().getParameter("eventId");

        if(eventId!=null&&eventId.trim().length()>0)
        {
            MeetingHandler handler = (MeetingHandler) Application.getInstance().getModule(MeetingHandler.class);
            try
            {
                meeting = handler.getMeeting(eventId, true);
            }
            catch (MeetingException e)
            {
                Log.getLog(MeetingFormEdit.class).error(e);
            }
            init();
            Map resourcesMap = new SequencedHashMap();
            ResourceManager rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);
            try{
                Collection col = rm.getResources(getWidgetManager().getUser().getId(),true);
                for(Iterator i=col.iterator();i.hasNext();){
                    Resource resource = (Resource)i.next();
                    resourcesMap.put(resource.getId(),resource.getName());
                }
            }catch(Exception e){
                Log.getLog(getClass()).error(e);
            }
            resources.setLeftValues(resourcesMap);
            populateForm();

            // set file upload values
            uploadForm.setFolderId("/" + MeetingHandler.MEETING_FILES_FOLDER + "/" + eventId);
            fileListing.setFolderId("/" + MeetingHandler.MEETING_FILES_FOLDER + "/" + eventId);
            fileListing.refresh();

            //get memo/email/notify msg value from the first page (MeetingFormAdd)
            Boolean memoValue = (Boolean)event.getRequest().getSession().getAttribute("memo");
            Boolean emailValue = (Boolean)event.getRequest().getSession().getAttribute("email");
            notifyNoteValue = (String)event.getRequest().getSession().getAttribute("notes");

            if(memoValue != null){
                notifyMemo.setChecked(memoValue.booleanValue()?true:false);
                event.getRequest().getSession().setAttribute("memo", null);
            }
            if(emailValue != null){
                notifyEmail.setChecked(emailValue.booleanValue()?true:false);
                event.getRequest().getSession().setAttribute("email", null);
            }
            if(notifyNoteValue != null){
                notifyNote.setValue(notifyNoteValue);
                event.getRequest().getSession().setAttribute("notes", notifyNoteValue);
            }
        }
    }

    public void init()
    {
        if(eventId != null || "".equals(eventId))
        {
            try
            {
                super.init();
                MeetingHandler handler = (MeetingHandler) Application.getInstance().getModule(MeetingHandler.class);
                meeting = handler.getMeeting(eventId, true);
            }
            catch (Exception e)
            {
                Log.getLog(MeetingFormEdit.class).error(e);
            }
        }
    }

    public Forward actionPerformed(Event evt)
    {
        String buttonClicked = findButtonClicked(evt);
        if(attachFilesButton.getAbsoluteName().equals(buttonClicked)){
            onSubmit(evt);
            uploadFile = true;
            return null;
        }
        String event = evt.getRequest().getParameter("et");
        if("done".equals(event)){
            uploadFile = false;
            String folderId = uploadForm.getFolderId();
            if(folderId!=null&&folderId.trim().length()>0){
                fileListing.setFolderId(folderId);
                fileListing.refresh();
            }
            description.setInvalid(false);
            return null;
        }
        return super.actionPerformed(evt);
    }

    protected void populateForm()
    {
        super.populateForm();
        CalendarModule module = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
        MeetingHandler handler = (MeetingHandler) Application.getInstance().getModule(MeetingHandler.class);
        Collection attendees;

        try
        {
            //Populating secretary field
            secretary.removeAllOptions();
            attendees = module.getAttendees(eventId, CalendarModule.DEFAULT_INSTANCE_ID);
            Collection userId = new ArrayList();
            for(Iterator i = attendees.iterator(); i.hasNext();)
                userId.add(((Attendee) i.next()).getUserId());
            DaoQuery query = new DaoQuery();
            query.addProperty(new OperatorIn("id", userId.toArray(), DaoOperator.OPERATOR_AND));
            attendees = service.getUsers(query, 0, -1, "firstName", false);
            secretary.addOption(" - ", "No Secretary");
            for(Iterator i = attendees.iterator(); i.hasNext();)
            {
                User user = (User) i.next();
                secretary.addOption(user.getId(), (String) user.getProperty("firstName") + " " + (String) user.getProperty("lastName"));
            }
            secretary.setSelectedOptions(new String[] {meeting.getSecretary()});
            //Populating category field
            category.removeAllOptions();
            category.addOption("-1", "No Category");
            Collection categories = handler.getCategory();
            for(Iterator i = categories.iterator(); i.hasNext();)
            {
                String cat = (String) i.next();
                if(!cat.equals("-1"))
                    category.addOption(cat, cat);
            }
            category.setSelectedOptions(new String[] {meeting.getCategory()});
        }
        catch (Exception e)
        {
            Log.getLog(MeetingFormEdit.class).error(e);
        }
    }

    protected Forward createEvent(Event evt) throws RuntimeException
    {
        String temp = notifyNote.getValue().toString();
        if(eventId != null || "".equals(eventId))
        {
            try
            {
                meeting.setNotify(temp);
                CalendarModule module = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
                MeetingHandler handler = (MeetingHandler) Application.getInstance().getModule(MeetingHandler.class);
                String userId = getWidgetManager().getUser().getId();

                CalendarEvent event = assembleEvent();
                meeting = assembleMeeting(event);

                Collection col = generateRecurringEvent(event);
                for(Iterator i = col.iterator(); i.hasNext();)
                    module.addRecurringEvent((CalendarEvent) i.next(), userId, false);

                handler.editMeeting(meeting, userId);
                if(getNewflag() != null && !getNewflag().equals("0")){
                    sendNotification(meeting,evt);

                } else {
                    sendNotificationUpdate(meeting,evt);
                }

                ResourceMailer.sendResourceApprovalMail(getWidgetManager().getUser().getId(),meeting.getEvent(),evt);
                init();

                // reset alert notification
                CalendarUtil.resetReminderAlert(evt.getRequest(), eventId);

                return new Forward("meeting updated");
//                populateForm();
            }
            catch(ConflictException e)
            {
                eventId=null;
                evt.getRequest().getSession().setAttribute("conflict",e);
                evt.getRequest().getSession().setAttribute("event",meeting);
                evt.getRequest().getSession().setAttribute("recurringEvents",generateRecurringEvent(meeting.getEvent()));

                if(getNewflag() != null && getNewflag().endsWith("1")){
                    return new Forward("conflict exception1");
                }  else {
                    return new Forward("conflict exception");
                }
            }
            catch(Exception e){
                throw new RuntimeException(e.toString());
            }
        }
        return null;
    }

    protected Meeting assembleMeeting(CalendarEvent event)
    {
        Meeting meeting = super.assembleMeeting(event);
        if((String) otherCategory.getValue() == null || "".equals(otherCategory.getValue()))
            meeting.setCategory((String) ((Collection) category.getValue()).iterator().next());
        else
            meeting.setCategory((String) otherCategory.getValue());
        meeting.setSecretary((String) ((Collection) secretary.getValue()).iterator().next());
        meeting.setNotify(notifyNote.getValue().toString());

        return meeting;
    }

    public void setEventId(String eventId)
    {
        this.eventId = eventId;
        init();
    }

    public String getDefaultTemplate()
    {
        if(uploadFile)
            return DEFAULT_TEMPLATE_UPLOAD;
        else
            return DEFAULT_TEMPLATE;
    }

    public String getNewflag() {
        return newflag;
    }

    public void setNewflag(String newflag) {
        this.newflag = newflag;
    }
}


