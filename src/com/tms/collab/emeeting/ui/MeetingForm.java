package com.tms.collab.emeeting.ui;

import com.tms.collab.calendar.ui.CalendarForm;
import com.tms.collab.calendar.ui.UserUtil;
import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.calendar.model.Attendee;
import com.tms.collab.emeeting.MeetingHandler;
import com.tms.collab.emeeting.MeetingException;
import com.tms.collab.emeeting.Meeting;
import com.tms.collab.taskmanager.ui.FileUploadForm;
import com.tms.collab.taskmanager.ui.FileListing;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.MessagingException;
import kacang.stdui.TextField;
import kacang.stdui.SelectBox;
import kacang.stdui.Button;
import kacang.Application;
import kacang.services.security.*;
import kacang.ui.Event;
import kacang.util.Log;
import javax.mail.internet.AddressException;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashMap;

public abstract class MeetingForm extends CalendarForm
{
    public static final String RESPONSE_SUCESSFUL = "com.tms.collab.emeeting.ui.MeetingForm.success";
    public static final String RESPONSE_FAILED = "com.tms.collab.emeeting.ui.MeetingForm.failed";
    public static final String RESPONSE_CANCELED = "com.tms.collab.emeeting.ui.MeetingForm.canceled";
    public static final String NO_CATEGORY = "-1";

    protected SelectBox category;
    protected TextField otherCategory;
    protected SelectBox secretary;
    protected FileUploadForm uploadForm;
    protected FileListing fileListing;
    protected Button attachFilesButton;

    protected Meeting meeting;
    protected boolean uploadFile = false;

    public MeetingForm()
    {
    }

    public MeetingForm(String name)
    {
        super(name);
    }

    public void init()
    {
        super.init();
        MeetingHandler handler = (MeetingHandler) Application.getInstance().getModule(MeetingHandler.class);

        otherCategory = new TextField("otherCategory");
        otherCategory.setSize("20");
        secretary = new SelectBox("secretary");
        uploadForm = new FileUploadForm("fileupload");
        fileListing = new FileListing("filelisting");
        attachFilesButton = new Button("attachbutton","Attach Files");

        category = new SelectBox("category");
        try
        {
            Collection categories = handler.getCategory();
            category.addOption(NO_CATEGORY, "No Category");
            for(Iterator i = categories.iterator(); i.hasNext();)
            {
                String label = (String) i.next();
                category.addOption(label, label);
            }
        }
        catch (MeetingException e)
        {
            Log.getLog(getClass()).error(e);
        }

        addChild(otherCategory);
        addChild(secretary);
        addChild(category);
        addChild(uploadForm);
        addChild(fileListing);
        addChild(attachFilesButton);

        uploadForm.init();
        fileListing.init();
        fileListing.setDeleteable(true);

        recurrence.setOptionMap(new HashMap());
        recurrence.addOption("0", Application.getInstance().getMessage("emeeting.label.na"));
        recurrence.setSelectedOption("0");

        //notifyNote.setHidden(true);

        initForm();
    }

    public static void sendNotification(Meeting meeting, Event evt){
        try {
            Application app  = Application.getInstance();
            
            String content = MessageForm.getMessageBody(meeting, evt);
            Collection col = meeting.getEvent().getAttendees();
            
            String subject = app.getMessage("calendar.label.new.e-Meetings") + ": "+ meeting.getTitle();
            MessagingModule mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
            
            if(meeting.isNotifyMemo()){
            	for (Iterator iterator = col.iterator(); iterator.hasNext();) {
                    Attendee attendee = (Attendee) iterator.next();
                    User user = UserUtil.getUser(attendee.getUserId());
                    String toEmail = user.getUsername()+"@"+ MessagingModule.INTRANET_EMAIL_DOMAIN;
                    mm.sendStandardHtmlEmail(meeting.getEvent().getUserId(),toEmail,"","",subject,"",content);
                }
            }
            
            if(meeting.isNotifyEmail()){
            	for (Iterator iterator = col.iterator(); iterator.hasNext();) {
                    Attendee attendee = (Attendee) iterator.next();
                    User user = UserUtil.getUser(attendee.getUserId());
                    String toEmail = (String)user.getProperty("email1");
                    mm.sendStandardHtmlEmail(meeting.getEvent().getUserId(),toEmail,"","",subject,"",content);
                }
            }
            
        } catch (kacang.services.security.SecurityException e) {
            Log.getLog(MeetingForm.class).error(e.getMessage(), e);  //To change body of catch statement use Options | File Templates.
        } catch (MessagingException e) {
            Log.getLog(MeetingForm.class).error(e.getMessage(), e);  //To change body of catch statement use Options | File Templates.
        } catch (AddressException e) {
            Log.getLog(MeetingForm.class).error(e.getMessage(), e);  //To change body of catch statement use Options | File Templates.
        }
    }

      public static void sendNotificationUpdate(Meeting meeting, Event evt){
        try {
             Application app  = Application.getInstance();

            String content = MessageForm.getMessageBody(meeting, evt);
            Collection col = meeting.getEvent().getAttendees();

            String subject = app.getMessage("calendar.label.update.e-Meetings") + ": " + meeting.getTitle();
            MessagingModule mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
            
            if(meeting.isNotifyMemo()){
            	for (Iterator iterator = col.iterator(); iterator.hasNext();) {
                    Attendee attendee = (Attendee) iterator.next();
                    User user = UserUtil.getUser(attendee.getUserId());
                    String toEmail = user.getUsername()+"@"+ MessagingModule.INTRANET_EMAIL_DOMAIN;
                    mm.sendStandardHtmlEmail(meeting.getEvent().getUserId(),toEmail,"","",subject,"",content);
                }
            }
            
            if(meeting.isNotifyEmail()){
            	for (Iterator iterator = col.iterator(); iterator.hasNext();) {
                    Attendee attendee = (Attendee) iterator.next();
                    User user = UserUtil.getUser(attendee.getUserId());
                    String toEmail = (String)user.getProperty("email1");
                    mm.sendStandardHtmlEmail(meeting.getEvent().getUserId(),toEmail,"","",subject,"",content);
                }
            }
            
            
        } catch (kacang.services.security.SecurityException e) {
            Log.getLog(MeetingForm.class).error(e.getMessage(), e);  //To change body of catch statement use Options | File Templates.
        } catch (MessagingException e) {
            Log.getLog(MeetingForm.class).error(e.getMessage(), e);  //To change body of catch statement use Options | File Templates.
        } catch (AddressException e) {
            Log.getLog(MeetingForm.class).error(e.getMessage(), e);  //To change body of catch statement use Options | File Templates.
        }
    }

    protected Meeting assembleMeeting(CalendarEvent event)
    {
        Meeting meeting = new Meeting();
        meeting.setEventId(event.getEventId());
        meeting.setTitle(event.getTitle());
        meeting.setCategory(MeetingForm.NO_CATEGORY);
        meeting.setSecretary("");
        meeting.setStartDate(event.getStartDate());
        meeting.setEndDate(event.getEndDate());
        meeting.setEvent(event);
        
        //keep track of the check box for memo and email
        meeting.setNotifyEmail(notifyEmail != null ?notifyEmail.isChecked() :false);
        meeting.setNotifyMemo(notifyMemo != null ?notifyMemo.isChecked() :false);

        return meeting;
    }

    public void initForm()
    {
    }

    //Getters and Setters
    public SelectBox getCategory()
    {
        return category;
    }

    public void setCategory(SelectBox category)
    {
        this.category = category;
    }

    public TextField getOtherCategory()
    {
        return otherCategory;
    }

    public void setOtherCategory(TextField otherCategory)
    {
        this.otherCategory = otherCategory;
    }

    public SelectBox getSecretary()
    {
        return secretary;
    }

    public void setSecretary(SelectBox secretary)
    {
        this.secretary = secretary;
    }

    public Meeting getMeeting()
    {
        return meeting;
    }

    public void setMeeting(Meeting meeting)
    {
        this.meeting = meeting;
    }

    public String getPrefix()
    {
        return Meeting.class.getName();
    }

    public FileUploadForm getUploadForm()
    {
        return uploadForm;
    }

    public void setUploadForm(FileUploadForm uploadForm)
    {
        this.uploadForm = uploadForm;
    }

    public FileListing getFileListing()
    {
        return fileListing;
    }

    public void setFileListing(FileListing fileListing)
    {
        this.fileListing = fileListing;
    }

    public Button getAttachFilesButton()
    {
        return attachFilesButton;
    }

    public void setAttachFilesButton(Button attachFilesButton)
    {
        this.attachFilesButton = attachFilesButton;
    }
}
