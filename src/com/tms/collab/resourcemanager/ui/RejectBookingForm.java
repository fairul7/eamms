package com.tms.collab.resourcemanager.ui;

import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.calendar.model.CalendarModule;
import com.tms.collab.messaging.model.MessagingException;
import com.tms.collab.resourcemanager.model.Resource;
import com.tms.collab.resourcemanager.model.ResourceManager;
import com.tms.collab.resourcemanager.model.ResourceMailer;
import kacang.Application;
import kacang.model.DaoException;
import kacang.services.security.SecurityException;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.TextBox;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import javax.mail.internet.AddressException;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jan 14, 2004
 * Time: 2:45:00 PM
 * To change this template use Options | File Templates.
 */
public class RejectBookingForm extends Form
{
    private TextBox reasonTB;
    private Button rejectButton,cancelButton;
    private String eventId,instanceId, resourceId;
    private CalendarEvent event;

    public RejectBookingForm()
    {
    }

    public RejectBookingForm(String s)
    {
        super(s);
    }

    public void onRequest(Event event)
    {
        super.onRequest(event);
        CalendarModule cm = (CalendarModule)Application.getInstance().getModule(CalendarModule.class);
        if(eventId!=null&&instanceId!=null){
            try
            {
                this.event = cm.getCalendarEvent(eventId,instanceId);
            } catch (Exception e)
            {
                Log.getLog(getClass()).error(e);
            }
        }
    }

    public Forward onSubmit(Event event)
    {
        if(cancelButton.getAbsoluteName().equals(findButtonClicked(event)))
            return new Forward("cancel");
        return super.onSubmit(event);
    }

    public void init()
    {
        super.init();
        reasonTB= new TextBox("reasontb");
        reasonTB.addChild(new ValidatorNotEmpty("notempty"));
        reasonTB.setRows("8");
        rejectButton = new Button("rejectButton",Application.getInstance().getMessage("resourcemanager.label.Reject","Reject"));
        cancelButton = new Button("cancelButton",Application.getInstance().getMessage("resourcemanager.label.Cancel","Cancel"));
        addChild(cancelButton);
        addChild(reasonTB);
        addChild(rejectButton);
    }

    public Forward onValidate(Event event)
    {
        super.onValidate(event);
        String buttonClicked = findButtonClicked(event);
        if(rejectButton.getAbsoluteName().equals(buttonClicked)){
            ResourceManager rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);
            rm.rejectBooking(resourceId, eventId, instanceId,reasonTB.getValue().toString());
            Resource resource = null;
            try
            {
                resource = rm.getResource(resourceId,true);
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
            try
            {
                ResourceMailer.sendRejectNotification(this.event,resource,reasonTB.getValue().toString(),getWidgetManager().getUser().getId());
            } catch (SecurityException e)
            {
                Log.getLog(getClass()).error(e.getMessage(), e);
            } catch (AddressException e)
            {
                Log.getLog(getClass()).error(e.getMessage(),e);
            } catch (MessagingException e)
            {
                Log.getLog(getClass()).error(e.getMessage(),e);
            }
            init();
            return new Forward("rejected");
        }else if(cancelButton.getAbsoluteName().equals(buttonClicked))
            return new Forward("cancel");
        return null;
    }

    public String getDefaultTemplate()
    {
        return "calendar/rejectform";
    }

    public TextBox getReasonTB()
    {
        return reasonTB;
    }

    public void setReasonTB(TextBox reasonTB)
    {
        this.reasonTB = reasonTB;
    }

    public Button getRejectButton()
    {
        return rejectButton;
    }

    public void setRejectButton(Button rejectButton)
    {
        this.rejectButton = rejectButton;
    }

    public String getEventId()
    {
        return eventId;
    }

    public void setEventId(String eventId)
    {
        this.eventId = eventId;
    }

    public String getInstanceId()
    {
        return instanceId;
    }

    public void setInstanceId(String instanceId)
    {
        this.instanceId = instanceId;
    }

    public String getResourceId()
    {
        return resourceId;
    }

    public void setResourceId(String resourceId)
    {
        this.resourceId = resourceId;
    }

    public CalendarEvent getEvent()
    {
        return event;
    }

    public void setEvent(CalendarEvent event)
    {
        this.event = event;
    }

    public Button getCancelButton()
    {
        return cancelButton;
    }

    public void setCancelButton(Button canButton)
    {
        this.cancelButton = canButton;
    }

/*
    public void sendNotification(CalendarEvent event, Resource resource, String reason) throws SecurityException, AddressException, MessagingException {
        SecurityService ss;
        User toUser;
        MessagingModule mm;
        StringBuffer sb = new StringBuffer();
        sb.append("Your resource booking of \"" + resource.getName() + "\" for the\n");
        sb.append("purpose of \"" + event.getTitle() + "\" has been REJECTED due\n");
        sb.append("to the following reason:\n");
        sb.append("\n");
        sb.append("<br><br><li>" + reason + "</li>\n");
        sb.append("\n");
        sb.append("\n");
        sb.append("<br><br>Your request was rejected at "+ new Date() +".");
        ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
        toUser = ss.getUser(event.getUserId());

        String toEmail = toUser.getUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
        String ccEmail = "";
        String bccEmail = "";
        String subject = "Notification of rejected resource booking";
        String title = subject;
        String content = sb.toString();

        mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
        mm.sendStandardHtmlEmail(getWidgetManager().getUser().getId(), toEmail, ccEmail, bccEmail, subject, title, content);
    }
*/
}

