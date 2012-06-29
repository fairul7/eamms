package com.tms.collab.resourcemanager.model;

import kacang.model.DefaultDataObject;
import kacang.services.security.SecurityException;
import kacang.util.Log;
import java.util.Date;
import java.util.Collection;

import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.calendar.ui.UserUtil;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Sep 15, 2003
 * Time: 3:40:23 PM
 * To change this template use Options | File Templates.
 */


public class ResourceBooking extends DefaultDataObject
{
    private Resource resource;
    private String eventId;
    private String instanceId;
    private String userId;
    private String resourceId;
    private String comments;
//    Resource resource = null;
    private Date startDate;
    private Date endDate;
    private Date returnedDate;
    private String status;
    private Collection conflictEvents;
    
    private CalendarEvent event;
    private boolean isLoginUserInvolved;
    
    public ResourceBooking()
    {
    }

    public ResourceBooking(String eventId,String instanceId,String resourceId,String userId ,Date startDate, Date endDate, String status)
    {
        this.eventId = eventId;
        this.resourceId = resourceId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.userId = userId;
        this.instanceId = instanceId;
    }

    public CalendarEvent getEvent() {
		return event;
	}

	public void setEvent(CalendarEvent event) {
		this.event = event;
	}

	public boolean isLoginUserInvolved() {
		return isLoginUserInvolved;
	}

	public void setLoginUserInvolved(boolean isLoginUserInvolved) {
		this.isLoginUserInvolved = isLoginUserInvolved;
	}

	public String getEventId()
    {
        return eventId;
    }

    public void setEventId(String eventId)
    {
        this.eventId = eventId;
    }


    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public String getStatus()
    {
        return status.trim();
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

     public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getUserName()
    {
        try
        {
            return UserUtil.getUser(userId).getName();
        } catch (SecurityException e)
        {
            Log.getLog(ResourceBooking.class).error(e);
        }
        return null;
    }


    public Date getReturnedDate()
    {
        return returnedDate;
    }

    public void setReturnedDate(Date returnedDate)
    {
        this.returnedDate = returnedDate;
    }

    public String getResourceId()
    {
        return resourceId;
    }

    public void setResourceId(String resourceId)
    {
        this.resourceId = resourceId;
    }


    public String getInstanceId()
    {
        return instanceId;
    }

    public void setInstanceId(String instanceId)
    {
        this.instanceId = instanceId;
    }

    public Resource getResource()
    {
        return resource;
    }

    public void setResource(Resource resource)
    {
        this.resource = resource;
    }

    public Collection getConflictEvents()
    {
        return conflictEvents;
    }

    public void setConflictEvents(Collection conflictEvents)
    {
        this.conflictEvents = conflictEvents;
    }

    public boolean equals(Object obj)
    {
        boolean equals = false;
        if(obj instanceof ResourceBooking)
        {
            ResourceBooking booking = (ResourceBooking) obj;
            String eventId = booking.getEventId();
            String resourceId = booking.getResourceId();
            if(eventId == null)
                eventId = "";
            if(resourceId == null)
                resourceId = "";
            if(eventId.equals(getEventId()) && resourceId.equals(getResourceId()))
                equals = true;
        }
        return equals;
    }

    public String getComments()
    {
        return comments;
    }

    public void setComments(String comments)
    {
        this.comments = comments;
    }
}
