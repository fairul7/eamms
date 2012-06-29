package com.tms.collab.resourcemanager.ui;

import kacang.stdui.Form;
import kacang.stdui.Button;
import kacang.Application;
import kacang.services.security.SecurityException;
import kacang.util.Log;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DaoException;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

import com.tms.collab.calendar.model.CalendarModule;
import com.tms.collab.calendar.model.CalendarException;
import com.tms.collab.calendar.model.CalendarDao;
import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.resourcemanager.model.Resource;
import com.tms.collab.resourcemanager.model.ResourceManager;
import com.tms.collab.resourcemanager.model.ResourceMailer;
import com.tms.collab.messaging.model.MessagingException;

import javax.mail.internet.AddressException;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Feb 10, 2004
 * Time: 4:15:14 PM
 * To change this template use Options | File Templates.
 */
public class MailApproveForm extends Form
{
    public static final String FORWARD_RESOURCES_APPROVED = "approved";
    private String eventId;
    private String userId;
    private Button approveButton;
    private Collection resources;

    public void init()
    {
        super.init();
        approveButton = new Button("approveButton","Approve");
        approveButton.setOnClick("approveConfirm()");
        addChild(approveButton);
    }

    public Forward onValidate(Event event)
    {
        String buttonName = findButtonClicked(event);
        if(approveButton.getAbsoluteName().equals(buttonName)){
            String[] resourceIds = event.getRequest().getParameterValues("resourceCalID");
            ResourceManager rm = (ResourceManager) Application.getInstance().getModule(ResourceManager.class);
            CalendarModule cm = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
            CalendarDao cd = (CalendarDao)cm.getDao();
	        
	        if (resourceIds == null) {
		        return null;
	        }
	        
            try
            {
                Collection rcol = cd.selectRecurringCalendarEvents(eventId);
                Collection resources = new TreeSet();
                for (int i = 0; i < resourceIds.length; i++)
                {
                    String resourceId = resourceIds[i];
                    resources.add(rm.getResource(resourceId,true));
                }
                for (int i = 0; i < resourceIds.length; i++)
                {
                    String resourceId = resourceIds[i];
                    rm.approveBooking(resourceId,eventId,CalendarModule.DEFAULT_INSTANCE_ID);
                    for (Iterator iterator = rcol.iterator(); iterator.hasNext();)
                    {
                        CalendarEvent revent = (CalendarEvent) iterator.next();
                        rm.approveBooking(resourceId,eventId,revent.getInstanceId());
                    }
                }
                ResourceMailer.sendApproveNotification(cm.getCalendarEvent(eventId,CalendarModule.DEFAULT_INSTANCE_ID),resources,getWidgetManager().getUser().getId(),event);
                for (Iterator iterator = rcol.iterator(); iterator.hasNext();)
                {
                    CalendarEvent revent = (CalendarEvent) iterator.next();
                    ResourceMailer.sendApproveNotification(revent,resources,getWidgetManager().getUser().getId(),event);
                }
                return new Forward(FORWARD_RESOURCES_APPROVED);
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
        return super.onValidate(event);
    }

    public String getDefaultTemplate()
    {
        return "resourcemanager/mailapproveform";
    }

    public String getEventId()
    {
        return eventId;
    }

    public void setEventId(String eventId)
    {
        this.eventId = eventId;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public Button getApproveButton()
    {
        return approveButton;
    }

    public void setApproveButton(Button approveButton)
    {
        this.approveButton = approveButton;
    }

    public Collection getResources() throws CalendarException, DataObjectNotFoundException
    {
        if(eventId!=null&&eventId.trim().length()>0){
            CalendarModule cm = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
            Collection rcol = cm.getCalendarEvent(eventId).getResources();
            resources = new TreeSet();
            ResourceManager rm = (ResourceManager) Application.getInstance().getModule(ResourceManager.class);
            if(rcol== null)
                return null;
            for (Iterator iterator = rcol.iterator(); iterator.hasNext();)
            {
                Resource resource = (Resource) iterator.next();
                if(resource.isRequireApproval()){
                    resource.setAuthorities(rm.getAuthorizedMembers(resource.getId()));
                }
                if(resource.getAuthorities()!=null&&resource.getAuthorities().contains(userId)){
                 try{
                    resources.add(resource);
                 }catch(Exception e){
                     e.printStackTrace();
                 }
                }
            }
            return resources;
        }
        return null;
    }


    public void setResources(Collection resources)
    {
        this.resources = resources;
    }
}
