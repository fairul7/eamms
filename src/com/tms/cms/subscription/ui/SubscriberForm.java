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
 * Date: May 15, 2003
 * Time: 10:55:38 PM
 * To change this template use Options | File Templates.
 */
public class SubscriberForm extends Panel implements EventListener
{
    public static final String DEFAULT_TEMPLATE = "cms/subscription/subscriber";
    private SubscriberTable viewSubscriber;



    private AddSubscriberForm addSubscriber;
    private EditSubscriberForm editSubscriber;
    private String subscriptionId;

    public SubscriberForm() {}

    public SubscriberForm(String name)
    {
        super(name);
    }

    public void init()
    {
        if(getSubscriptionId() != null)
        {
            removeChildren();

            viewSubscriber = new SubscriberTable("viewSubscriber");
            addSubscriber = new AddSubscriberForm("addSubscriber");
            editSubscriber = new EditSubscriberForm("editSubscriber");

            addChild(viewSubscriber);
            addChild(addSubscriber);
            addChild(editSubscriber);

            viewSubscriber.setSubscriptionId(getSubscriptionId());
            viewSubscriber.init();
            addSubscriber.init();
            editSubscriber.init();

            addSubscriber.setHidden(true);
            editSubscriber.setHidden(true);

            viewSubscriber.addEventListener(this);
            addSubscriber.addEventListener(this);
            editSubscriber.addEventListener(this);
        }
    }

    public Forward actionPerformed(Event evt)
    {
        Widget widget = evt.getWidget();
        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
        User current = getWidgetManager().getUser();
        try
        {
            if(widget.getName().equals("viewSubscriber"))
            {
                if(evt.getType().equals(Table.PARAMETER_KEY_ACTION))
                {
                    Table table = (Table) widget;
                    if("add".equals(table.getSelectedAction()) && service.hasPermission(current.getId(), SubscriptionHandler.PERMISSION_SUBSCRIPTION_ADD, null, null))
                    {
                        addSubscriber.setSubscriptionId(getSubscriptionId());
                        addSubscriber.init();
                        addSubscriber.setState(false);
                        addSubscriber.setHidden(false);
                        editSubscriber.setHidden(true);
                        viewSubscriber.setHidden(true);
                    }
                }
                else if(evt.getType().equals(Table.PARAMETER_KEY_SELECTION))
                {
                    String userId = evt.getRequest().getParameter("userId");
                    if(userId != null)
                    {
                        if(!("".equals(userId)) && service.hasPermission(current.getId(), SubscriptionHandler.PERMISSION_SUBSCRIPTION_RENEW, null, null))
                        {
                            editSubscriber.setUserId(userId);
                            editSubscriber.setSubscriptionId(getSubscriptionId());
                            editSubscriber.init();
                            editSubscriber.setState(false);
                            editSubscriber.setHidden(false);
                            addSubscriber.setHidden(true);
                            viewSubscriber.setHidden(true);
                        }
                    }
                }

            }
            else if(widget.getName().equals("editSubscriber"))
            {
                if(editSubscriber.isState())
                {
                    editSubscriber.setHidden(true);
                    addSubscriber.setHidden(true);
                    viewSubscriber.setHidden(false);
                }
            }
            else if(widget.getName().equals("addSubscriber"))
            {
                if(addSubscriber.isState())
                {
                    if(addSubscriber.getUserId() == null || "".equals(addSubscriber.getUserId()))
                    {
                        editSubscriber.setHidden(true);
                        addSubscriber.setHidden(true);
                        viewSubscriber.setHidden(false);
                    }
                    else
                    {
                        editSubscriber.setUserId(addSubscriber.getUserId());
                        editSubscriber.setSubscriptionId(getSubscriptionId());
                        editSubscriber.init();
                        editSubscriber.setState(false);
                        editSubscriber.setHidden(false);
                        addSubscriber.setHidden(true);
                        viewSubscriber.setHidden(true);
                    }
                }
            }
        }
        catch(Exception e)
        {
            Log.getLog(SubscriptionForm.class).error(e.toString(), e);
        }
        return new Forward();
    }

    public SubscriberTable getViewSubscriber()
    {
        return viewSubscriber;
    }

    public void setViewSubscriber(SubscriberTable viewSubscriber)
    {
        this.viewSubscriber = viewSubscriber;
    }

    public EditSubscriberForm getEditSubscriber()
    {
        return editSubscriber;
    }

    public void setEditSubscriber(EditSubscriberForm editSubscriber)
    {
        this.editSubscriber = editSubscriber;
    }

    public String getSubscriptionId()
    {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId)
    {
        this.subscriptionId = subscriptionId;
    }

    public AddSubscriberForm getAddSubscriber()
    {
        return addSubscriber;
    }

    public void setAddSubscriber(AddSubscriberForm addSubscriber)
    {
        this.addSubscriber = addSubscriber;
    }
}
