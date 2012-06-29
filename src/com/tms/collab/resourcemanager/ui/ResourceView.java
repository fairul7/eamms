package com.tms.collab.resourcemanager.ui;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.model.DaoException;
import kacang.util.Log;
import kacang.services.security.SecurityService;
import kacang.stdui.Form;
import kacang.stdui.Button;
import kacang.stdui.event.FormEventAdapter;
import com.tms.collab.resourcemanager.model.Resource;
import com.tms.collab.resourcemanager.model.ResourceManager;


/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Sep 4, 2003
 * Time: 5:59:16 PM
 * To change this template use Options | File Templates.
 */
public class ResourceView extends Form
{
    private Resource resource = null;
    private String resourceId = null;
    private Button editButton = null;
    private Button viewAllButton = null;
    private boolean editable = false;
    private Form actionForm = null;
    private ResourceBookingView bookingView = null;
    public ResourceView()
    {
    }

    public ResourceView(String name)
    {
        super(name);
    }

    public void init()
    {
        //super.init();
        viewAllButton = new Button("viewAll",Application.getInstance().getMessage("resourcemanager.label.ViewAll","View All"));
        addChild(viewAllButton);
        bookingView = new ResourceBookingView("bookingViews");
        bookingView.init();
        editButton = new Button("editButton",Application.getInstance().getMessage("resourcemanager.label.Edit","Edit"));
        actionForm = new Form("actionForm");
        actionForm.addFormEventListener(new FormEventAdapter(){
            public Forward onValidate(Event evt)
            {
                String button = actionForm.findButtonClicked(evt);
                if(button!=null){
                    if(editButton!=null&&button.equals(editButton.getAbsoluteName())){
                        try{
                            evt.getResponse().sendRedirect("RM.jsp?resourceId="+resource.getId());
                        }catch(Exception e)
                        {Log.getLog(getClass()).error(e);}
                    }
                }
                return null;
            }
        });
        addChild(actionForm);
        addChild(bookingView);
    }

    public Forward onValidate(Event evt)
    {
        String button = findButtonClicked(evt);
        if(viewAllButton.getAbsoluteName().equals(button)){
            return new Forward("View All");
        }
        return null;
    }

    public void onRequest(Event evt)
    {

        String id = evt.getRequest().getParameter("id");
        if(id!=null && id.trim().length()>0)
            resourceId = id;
        if(resourceId!=null && resourceId.trim().length()>0){
           // if(resource == null||!resource.getId().equals(resourceId)){
                ResourceManager rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);
            try
            {
                resource = rm.getResource(resourceId,false);
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
            bookingView.setResourceId(resourceId);
                if(resource!=null)
                    bookingView.setResource(resource);
                bookingView.refresh();
             //   bookingView.init();
            //}
        }
        if(resource!=null){
            try{
                String userId = getWidgetManager().getUser().getId();
                SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
                if(resource.getCreator().equals(userId)||
                  ss.hasPermission(userId,ResourceManager.PERMISSION_EDIT_RESOURCE,null,null)){
                      editable = true;
                      actionForm.addChild(editButton);
                  }
                else{
                    actionForm.removeChild(editButton);
                    editable = false;
                }
            }catch(Exception e){
                Log.getLog(getClass()).error(e);
            }
        }  super.onRequest(evt);
    }

  /* public Forward onValidate(Event evt)
    {
        String buttonClicked = findButtonClicked(evt);
       /* System.out.println(buttonClicked);
        System.out.println(resource.getId());
        System.out.println("Edit button:"+editButton.getAbsoluteName());
        if(editable&&buttonClicked.equals(editButton.getAbsoluteName())){
            try{evt.getResponse().sendRedirect("RM.jsp?resourceId="+resource.getId());
            }catch(Exception e){e.printStackTrace();}
        }
        return super.onValidate(evt);
    }*/

    public String getDefaultTemplate()
    {
        return "resourcemanager/resourceview";
    }

    public String getResourceId()
    {
        return resourceId;
    }

    public void setResourceId(String resourceId)
    {
        this.resourceId = resourceId;
    }

    public Resource getResource()
    {
        return resource;
    }

    public void setResource(Resource resource)
    {
        this.resource = resource;
    }

    public Button getEditButton()
    {
        return editButton;
    }

    public void setEditButton(Button editButton)
    {
        this.editButton = editButton;
    }

    public boolean isEditable()
    {
        return editable;
    }

    public void setEditable(boolean editable)
    {
        this.editable = editable;
    }

    public Form getActionForm()
    {
        return actionForm;
    }

    public void setActionForm(Form actionForm)
    {
        this.actionForm = actionForm;
    }

    public String getCreator(){
        SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
        try{
            return ss.getUser(resource.getCreator()).getName();
        }catch (Exception e){
            Log.getLog(getClass()).error(e);
        }
        return null;
    }



    public ResourceBookingView getBookingView()
    {
        return bookingView;
    }

    public void setBookingView(ResourceBookingView bookingView)
    {
        this.bookingView = bookingView;
    }

    public Button getViewAllButton()
    {
        return viewAllButton;
    }

    public void setViewAllButton(Button viewAllButton)
    {
        this.viewAllButton = viewAllButton;
    }


}
