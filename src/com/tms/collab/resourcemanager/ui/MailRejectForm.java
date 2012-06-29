package com.tms.collab.resourcemanager.ui;

import kacang.stdui.Button;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;
import kacang.services.security.SecurityException;
import kacang.util.Log;
import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import com.tms.collab.resourcemanager.model.ResourceManager;
import com.tms.collab.resourcemanager.model.ResourceMailer;
import com.tms.collab.resourcemanager.model.Resource;
import com.tms.collab.calendar.model.CalendarModule;
import com.tms.collab.calendar.model.CalendarDao;
import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.calendar.model.CalendarException;
import com.tms.collab.messaging.model.MessagingException;

import javax.mail.internet.AddressException;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Feb 10, 2004
 * Time: 4:15:24 PM
 * To change this template use Options | File Templates.
 */
public class MailRejectForm extends MailApproveForm
{
    private Button rejectButton;
    public static final String FORWARD_RESOURCES_REJECTED= "rejected";

    public void init()
    {
        rejectButton = new Button("rejectButton",Application.getInstance().getMessage("resourcemanager.label.Reject","Reject"));
        rejectButton.setOnClick("rejectConfirm()");
        addChild(rejectButton);
    }

    public Forward onValidate(Event evt)
    {
        String but = findButtonClicked(evt);
        if(rejectButton.getAbsoluteName().equals(but)){
            try
            {
                ResourceManager rm = (ResourceManager) Application.getInstance().getModule(ResourceManager.class);
                String[] resourceIds = evt.getRequest().getParameterValues("resourceCalID");
                CalendarModule cm = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
                CalendarEvent event = cm.getCalendarEvent(getEventId());
                CalendarDao cd = (CalendarDao)cm.getDao();
                Collection rcol = cd.selectRecurringCalendarEvents(getEventId());
	            
	            if (resourceIds == null) {
		            return null;
	            }
	            
                for (int i = 0; i < resourceIds.length; i++)
                {
                    String resourceId = resourceIds[i];
                    Resource resource = rm.getResource(resourceId,true);
                    String reason = evt.getRequest().getParameter("reason"+resourceId);
                    if(reason==null)reason = "";
                    rm.rejectBooking(resourceId,getEventId(),CalendarModule.DEFAULT_INSTANCE_ID,reason);
                    ResourceMailer.sendRejectNotification(event,resource,reason,getWidgetManager().getUser().getId());
                    for (Iterator iterator = rcol.iterator(); iterator.hasNext();)
                    {
                        CalendarEvent revent = (CalendarEvent) iterator.next();
                        rm.rejectBooking(resourceId,getEventId(),revent.getInstanceId(),reason);
                        ResourceMailer.sendRejectNotification(revent,resource,reason,getWidgetManager().getUser().getId());
                    }
                }
                return new Forward(FORWARD_RESOURCES_REJECTED);
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            } catch (DataObjectNotFoundException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            } catch (CalendarException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            } catch (SecurityException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            } catch (AddressException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            } catch (MessagingException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
        }
        return null;
    }

    public String getDefaultTemplate()
    {
        return "resourcemanager/mailrejectform";
    }

    public Button getRejectButton()
    {
        return rejectButton;
    }

    public void setRejectButton(Button rejectButton)
    {
        this.rejectButton = rejectButton;
    }

}
