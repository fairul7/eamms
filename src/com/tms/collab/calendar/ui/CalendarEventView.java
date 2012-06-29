package com.tms.collab.calendar.ui;

import kacang.ui.Widget;
import kacang.util.Log;
import com.tms.collab.calendar.model.CalendaringEventView;
import com.tms.collab.calendar.model.CalendaringPermission;
import com.tms.collab.calendar.model.CalendarEvent;


public class CalendarEventView extends Widget
{
    public final static int VIEW = 0;
    public final static int EDIT = 1;
    protected String eventId = null;
    protected Widget child = null;
    protected String userId = null;
    protected CalendarEvent event = null;
    protected String instanceId = null;
    protected int state = 0;
    protected boolean editable = false;
    protected boolean deleteable = false;
    protected boolean hiddenAction = false;
    protected boolean acceptReject = true;

    public CalendarEventView()
    {
    }

    public CalendarEventView(String s)
    {
        super(s);
    }

    public String getDefaultTemplate()
    {
        return "calendar/CalendarEventView";
    }

    public String getEventId()
    {
        return eventId;
    }

    public void setEventId(String eventId)
    {
        removeChildren();
        this.eventId = eventId;
        String className = eventId.substring(0, eventId.indexOf("_"));
        try
        {
            CalendaringEventView view = (CalendaringEventView)Class.forName(className).newInstance();
            if(state == VIEW) {
                child = view.getEventView();
                child.setParent(this);
            }
            else if(state == EDIT)
                child = view.getEventEdit();
            child.setWidgetManager(getWidgetManager());
            child.init();
            addChild(child);
            if(child instanceof CalendaringPermission){
                if(userId==null)
                    userId = getWidgetManager().getUser().getId();
                editable = ((CalendaringPermission)child).hasEditPermission(userId);
                deleteable = ((CalendaringPermission)child).hasDeletePermission(userId);
            }
        } catch (ClassNotFoundException e)
        {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        } catch (Exception e){
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }
    }

   /* public void onRequest(Event evt)
    {
        if(child!=null){
            editable = ((CalendaringPermission)child).hasEditPermission(userId);
            deleteable = ((CalendaringPermission)child).hasDeletePermission(userId);
        }
        super.onRequest(evt);
    }*/

    public Widget getChild()
    {
        return child;
    }

    public void setChild(Widget child)
    {
        this.child = child;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    public boolean isEditable()
    {
        return editable;
    }

    public void setEditable(boolean editable)
    {
        this.editable = editable;
    }

    public boolean isDeleteable()
    {
        return deleteable;
    }

    public void setDeleteable(boolean deleteable)
    {
        this.deleteable = deleteable;
    }

    public CalendarEvent getEvent()
    {
        return event;
    }

    public void setEvent(CalendarEvent event)
    {
        this.event = event;
    }

    public String getInstanceId()
    {
        return instanceId;
    }

    public void setInstanceId(String instanceId)
    {
        this.instanceId = instanceId;
    }

    public boolean isHiddenAction()
    {
        return hiddenAction;
    }

    public void setHiddenAction(boolean hiddenAction)
    {
        this.hiddenAction = hiddenAction;
    }

    public boolean isAcceptReject()
    {
        return acceptReject;
    }

    public void setAcceptReject(boolean acceptReject)
    {
        this.acceptReject = acceptReject;
    }
    
}
