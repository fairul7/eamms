package com.tms.cms.subscription.ui;

import com.tms.cms.subscription.SubscriptionHandler;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.Panel;
import kacang.stdui.Table;
import kacang.ui.Event;
import kacang.ui.EventListener;
import kacang.ui.Forward;
import kacang.ui.Widget;
import kacang.util.Log;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: May 13, 2003
 * Time: 8:24:22 PM
 * To change this template use Options | File Templates.
 */
public class SubscriptionForm extends Panel implements EventListener
{
    public static final String DEFAULT_TEMPLATE = "cms/subscription/subscriptionForm";
    private AddSubscriptionForm addSubscription;
    private EditSubscriptionForm editSubscription;
    private SubscriptionTable viewSubscription;
    private SubscriberForm subscriberForm;

    public SubscriptionForm() {}

    public SubscriptionForm(String name)
    {
        super(name);
    }

    public String getDefaultTemplate() {
        return DEFAULT_TEMPLATE;
    }

    public void init()
    {
        removeChildren();

        addSubscription = new AddSubscriptionForm("addSubscription");
        editSubscription = new EditSubscriptionForm("editSubscription");
        viewSubscription = new SubscriptionTable("viewSubscription");
        subscriberForm = new SubscriberForm("subscriberForm");

        addChild(addSubscription);
        addChild(editSubscription);
        addChild(viewSubscription);
        addChild(subscriberForm);

        addSubscription.init();
        editSubscription.init();
        viewSubscription.init();
        subscriberForm.init();

        addSubscription.setHidden(true);
        editSubscription.setHidden(true);
        subscriberForm.setHidden(true);

        addSubscription.addEventListener(this);
        editSubscription.addEventListener(this);
        viewSubscription.addEventListener(this);
    }

    public Forward actionPerformed(Event evt)
    {
        Widget widget = evt.getWidget();
        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
        User current = getWidgetManager().getUser();

        try
        {
            if(widget.getName().equals("viewSubscription"))
            {
                if(evt.getType().equals(Table.PARAMETER_KEY_ACTION))
                {
                    Table table = (Table) widget;
                    if("add".equals(table.getSelectedAction()) && service.hasPermission(current.getId(), SubscriptionHandler.PERMISSION_SUBSCRIPTION_ADD, null, null))
                    {
                        addSubscription.init();
                        addSubscription.setState(false);
                        addSubscription.setHidden(false);
                        editSubscription.setHidden(true);
                        subscriberForm.setHidden(true);
                        viewSubscription.setHidden(true);
                    }
                }
                else if(evt.getType().equals(Table.PARAMETER_KEY_SELECTION))
                {
                    String id = evt.getRequest().getParameter("id");

                    if(id != null)
                    {
                        if(!("".equals(id)) && service.hasPermission(current.getId(), SubscriptionHandler.PERMISSION_SUBSCRIPTION_EDIT, null, null))
                        {
                            editSubscription.setId(id);
                            editSubscription.init();
                            editSubscription.setState(false);
                            editSubscription.setHidden(false);
                            subscriberForm.setSubscriptionId(id);
                            subscriberForm.init();
                            subscriberForm.setHidden(false);
                            addSubscription.setHidden(true);
                            viewSubscription.setHidden(true);
                        }
                    }
                }
            }
            else if(widget.getName().equals("addSubscription"))
            {
                if(addSubscription.isState())
                {
                    addSubscription.setState(false);
                    addSubscription.setHidden(true);
                    editSubscription.setHidden(true);
                    subscriberForm.setHidden(true);
                    viewSubscription.setHidden(false);
                }
            }
            else if(widget.getName().equals("editSubscription"))
            {
                if(editSubscription.isState())
                {
                    editSubscription.setState(false);
                    editSubscription.setHidden(true);
                    subscriberForm.setHidden(true);
                    addSubscription.setHidden(true);
                    viewSubscription.setHidden(false);
                }
            }
        }
        catch(Exception e)
        {
            Log.getLog(SubscriptionForm.class).error(e.toString(), e);
        }
        return new Forward();
    }

    public AddSubscriptionForm getAddSubscription()
    {
        return addSubscription;
    }

    public void setAddSubscription(AddSubscriptionForm addSubscription)
    {
        this.addSubscription = addSubscription;
    }

    public EditSubscriptionForm getEditSubscription()
    {
        return editSubscription;
    }

    public void setEditSubscription(EditSubscriptionForm editSubscription)
    {
        this.editSubscription = editSubscription;
    }

    public SubscriptionTable getViewSubscription()
    {
        return viewSubscription;
    }

    public void setViewSubscription(SubscriptionTable viewSubscription)
    {
        this.viewSubscription = viewSubscription;
    }

    public SubscriberForm getSubscriberForm()
    {
        return subscriberForm;
    }

    public void setSubscriberForm(SubscriberForm subscriberForm)
    {
        this.subscriberForm = subscriberForm;
    }


}
