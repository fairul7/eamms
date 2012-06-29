package com.tms.collab.resourcemanager.ui;

import kacang.stdui.Form;
import kacang.stdui.Button;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;
import kacang.services.security.SecurityException;
import kacang.util.Log;
import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import com.tms.collab.calendar.ui.CalendarEventView;
import com.tms.collab.calendar.model.CalendarModule;
import com.tms.collab.calendar.model.CalendarException;
import com.tms.collab.resourcemanager.model.ResourceManager;
import com.tms.collab.resourcemanager.model.Resource;
import com.tms.collab.resourcemanager.model.ResourceBooking;
import com.tms.collab.resourcemanager.model.ResourceMailer;
import com.tms.collab.messaging.model.MessagingException;

import javax.mail.internet.AddressException;
import java.util.TreeSet;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jan 14, 2004
 * Time: 11:11:03 AM
 * To change this template use Options | File Templates.
 */
public class BookingDetailForm extends Form
{
    private Button cancelButton;
    private Button approveButton;
    private Button rejectButton;
    private CalendarEventView eventView;
    private String resourceId;
    private String eventId;
    private String instanceId;
    private Resource resource;
    private ResourceBooking booking;
    public BookingDetailForm()
    {
    }

    public BookingDetailForm(String s)
    {
        super(s);
    }

    public void init()
    {
        super.init();
        cancelButton = new Button("cancelbutton","Cancel");
        approveButton = new Button("approvebutton","Approve");
        rejectButton = new Button("rejectbutton","Reject");
        addChild(cancelButton);
        addChild(approveButton);
        addChild(rejectButton);
        eventView  = new CalendarEventView("eventView");
        eventView.setHiddenAction(true);
        addChild(eventView);
    }

    public void onRequest(Event event)
    {
        super.onRequest(event);
        if(instanceId!=null&&instanceId.trim().length()>0)
            eventView.setInstanceId(instanceId);
        if(eventId!=null&&eventId.trim().length()>0)
            eventView.setEventId(eventId);
        if(resourceId!=null&&resourceId.trim().length()>0)
        {
            ResourceManager rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);
            try
            {
                resource =  rm.getResource(resourceId,false);
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
            booking = rm.getResourceBooking(resourceId,eventId,instanceId);
        }
    }

    public Forward onValidate(Event event)
    {
        String buttonClicked = findButtonClicked(event);
        if(cancelButton.getAbsoluteName().equals(buttonClicked)){
            return new Forward("cancel");
        }else if(approveButton.getAbsoluteName().equals(buttonClicked)){
            ResourceManager rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);
            rm.approveBooking(resourceId,eventId,instanceId);
            CalendarModule cm = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
            try
            {
                Collection col = new TreeSet();
                col.add(rm.getResource(resourceId, true));
                ResourceMailer.sendApproveNotification(cm.getCalendarEvent(eventId,instanceId),col,getWidgetManager().getUser().getId(),event);
            } catch (SecurityException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            } catch (AddressException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            } catch (MessagingException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            } catch (DataObjectNotFoundException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            } catch (CalendarException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
            return new Forward("approved");
        }else if(rejectButton.getAbsoluteName().equals(buttonClicked)){
            return new Forward("rejected");
        }
        return super.onValidate(event);
    }

    public String getDefaultTemplate()
    {
        return "resourcemanager/bookingdetailform";
    }

    public Button getCancelButton()
    {
        return cancelButton;
    }

    public void setCancelButton(Button cancelButton)
    {
        this.cancelButton = cancelButton;
    }

    public Button getApproveButton()
    {
        return approveButton;
    }

    public void setApproveButton(Button approveButton)
    {
        this.approveButton = approveButton;
    }

    public Button getRejectButton()
    {
        return rejectButton;
    }

    public void setRejectButton(Button rejectButton)
    {
        this.rejectButton = rejectButton;
    }

    public CalendarEventView getEventView()
    {
        return eventView;
    }

    public void setEventView(CalendarEventView eventView)
    {
        this.eventView = eventView;
    }

    public String getResourceId()
    {
        return resourceId;
    }

    public void setResourceId(String resourceId)
    {
        this.resourceId = resourceId;
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
    public String getResourceName(){
        if(resource!=null){
            return resource.getName();
        }
        return null;
    }

    public Resource getResource()
    {
        return resource;
    }

    public void setResource(Resource resource)
    {
        this.resource = resource;
    }

    public ResourceBooking getBooking()
    {
        return booking;
    }

    public void setBooking(ResourceBooking booking)
    {
        this.booking = booking;
    }
}
