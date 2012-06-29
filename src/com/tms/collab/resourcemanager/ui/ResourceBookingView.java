package com.tms.collab.resourcemanager.ui;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.util.Log;
import kacang.model.DaoException;
import kacang.stdui.Form;
import kacang.stdui.DateField;
import kacang.stdui.Button;

import java.util.Collection;
import java.util.Date;
import java.util.Calendar;

import com.tms.collab.resourcemanager.model.ResourceManager;
import com.tms.collab.resourcemanager.model.Resource;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Sep 18, 2003
 * Time: 11:31:39 AM
 * To change this template use Options | File Templates.
 */
public class ResourceBookingView extends Form
{
    private String resourceId = null;
    private Collection bookings = null;
    private DateField startDate;
    private DateField endDate;
    private Button lookButton;
    private Resource resource;

    public ResourceBookingView()
    {
    }

    public ResourceBookingView(String name)
    {
        super(name);
    }

    public void init()
    {
        startDate = new DateField("startDate");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH,-1);
        startDate.setDate(cal.getTime());
        endDate = new DateField("endDate");
        lookButton = new Button("lookButton",Application.getInstance().getMessage("resourcemanager.label.Look","Show"));
        addChild(startDate);
        addChild(endDate);
        addChild(lookButton);
        /*if(resourceId!=null&&resourceId.trim().length()>0){
            ResourceManager rm = (ResourceManager) Application.getInstance().getModule(ResourceManager.class);
            cal.setTime(endDate.getDate());
            cal.set(Calendar.SECOND,43200);
            cal.add(Calendar.SECOND,43199);
            bookings = rm.getResourceBookings(resourceId,startDate.getDate(),cal.getTime());
        }*/
    }

    public Forward onSubmit(Event evt)
    {
        Forward result = super.onSubmit(evt);
        if (startDate.getDate().after(endDate.getDate())) {
            startDate.setInvalid(true);
            endDate.setInvalid(true);
            setInvalid(true);
        }
        return result;
    }

    public void onRequest(Event evt)
    {
        super.onRequest(evt);
        if(resourceId!=null&&resourceId.trim().length()>0){
            ResourceManager rm = (ResourceManager) Application.getInstance().getModule(ResourceManager.class);
            Calendar cal = Calendar.getInstance();
            cal.setTime(endDate.getDate());
            cal.set(Calendar.HOUR_OF_DAY,23);
            cal.set(Calendar.MINUTE,59);
            cal.set(Calendar.SECOND,59);
            
            try{
            	bookings = rm.getResourceBookings(resourceId,startDate.getDate(),cal.getTime());
            	if(resource==null){
                    // ResourceManager rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);
                     try
                     {
                         resource = rm.getResource(resourceId,true);
                     } catch (DaoException e)
                     {
                         Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                     }
                 }
            }
            catch(Exception e){
            	Log.getLog(getClass()).error("error in ResourceBookingView: " + e.getMessage());
            }
            
        }
    }

    public void refresh(){
        if(resourceId!=null&&resourceId.trim().length()>0){

        }
    }

    public Forward onValidate(Event evt)
    {
        super.onValidate(evt);
        String button = findButtonClicked(evt);
        if(lookButton.getAbsoluteName().equals(button)){
            ResourceManager rm = (ResourceManager) Application.getInstance().getModule(ResourceManager.class);
            Calendar cal = Calendar.getInstance();
            cal.setTime(endDate.getDate());
            cal.set(Calendar.HOUR_OF_DAY,23);
            cal.set(Calendar.MINUTE,59);
            cal.set(Calendar.SECOND,59);
            try{
            	bookings = rm.getResourceBookings(resourceId,startDate.getDate(),cal.getTime());
            }
            catch(Exception e){
            	Log.getLog(getClass()).error("error in ResourceBookingView: " + e.getMessage());
            }
        }
        return null;
    }

    public String getDefaultTemplate()
    {
        return "resourcemanager/ResourceBookingView";
    }

    public String getResourceId()
    {
        return resourceId;
    }

    public void setResourceId(String resourceId)
    {
        this.resourceId = resourceId;
    }

    public Collection getBookings()
    {
        return bookings;
    }

    public void setBookings(Collection bookings)
    {
        this.bookings = bookings;
    }

    public DateField getStartDate()
    {
        return startDate;
    }

    public void setStartDate(DateField startDate)
    {
        this.startDate = startDate;
    }

    public DateField getEndDate()
    {
        return endDate;
    }

    public void setEndDate(DateField endDate)
    {
        this.endDate = endDate;
    }

    public Button getLookButton()
    {
        return lookButton;
    }

    public void setLookButton(Button lookButton)
    {
        this.lookButton = lookButton;
    }

    public Resource getResource()
    {
        return resource;
    }

    public void setResource(Resource resource)
    {
        this.resource = resource;
    }
}
