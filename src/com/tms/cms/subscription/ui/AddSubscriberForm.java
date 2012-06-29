package com.tms.cms.subscription.ui;

import com.tms.cms.subscription.Subscriber;
import com.tms.cms.subscription.Subscription;
import com.tms.cms.subscription.SubscriptionHandler;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorIn;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.SelectBox;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: May 27, 2003
 * Time: 2:14:30 PM
 * To change this template use Options | File Templates.
 */
public class AddSubscriberForm extends Form
{
    public static final String DEFAULT_TEMPLATE = "cms/subscription/subscriberAdd";
    public static final String FORWARD_SUCCESS = "com.tms.cms.subscription.ui.AddSubscriberForm.success";
    public static final String FORWARD_FAIL = "com.tms.cms.subscription.ui.AddSubscriberForm.fail";

    private String subscriptionId;
    private String userId;
    private SelectBox userSelect;
    private Button buttonSave;
    private Button buttonCancel;
    private boolean state;

    public AddSubscriberForm() {}

    public AddSubscriberForm(String name)
    {
        super(name);
    }

    public void init()
    {
        super.init();
        if(!(getSubscriptionId() == null) || ("".equals(getSubscriptionId())))
        {
            try
            {
                removeChildren();

                Collection list;
                Subscriber subscriber;
                User user;
                Collection subscribers = new ArrayList();
                SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
                SubscriptionHandler handler = (SubscriptionHandler) Application.getInstance().getModule(SubscriptionHandler.class);

                list = handler.getSubscribersBySubscription(getSubscriptionId());
                for(Iterator i = list.iterator(); i.hasNext();)
                {
                    subscriber = (Subscriber) i.next();
                    subscribers.add(subscriber.getUserId());
                }
                DaoQuery properties = new DaoQuery();
                properties.addProperty(new OperatorIn("id", subscribers.toArray(), DaoOperator.OPERATOR_NAN));
                list = service.getUsers(properties, 0, -1, "username", false);

                Application application = Application.getInstance();
                buttonSave = new Button("buttonSave");
                buttonSave.setText(application.getMessage("general.label.save", "Save"));
                buttonCancel = new Button("buttonCancel");
                buttonCancel.setText(application.getMessage("general.label.cancel", "Cancel"));
                userSelect = new SelectBox("user");
                for(Iterator i = list.iterator(); i.hasNext();)
                {
                    user = (User) i.next();
                    userSelect.addOption(user.getId(), user.getUsername() + " [" + user.getProperty("firstName") + " " + user.getProperty("lastName") + "]");
                }

                addChild(userSelect);
                addChild(buttonSave);
                addChild(buttonCancel);
            }
            catch(Exception e)
            {
                Log.getLog(AddSubscriberForm.class).error(e);
            }
        }
    }

    public Forward onValidate(Event evt)
    {
        if(!(getSubscriptionId() == null) || ("".equals(getSubscriptionId())))
        {
            try
            {
                Subscriber subscriber = null;
                SubscriptionHandler handler = (SubscriptionHandler) Application.getInstance().getModule(SubscriptionHandler.class);
                Collection list = (Collection) userSelect.getValue();
                String userId = (String) list.iterator().next();
                try
                {
                    subscriber = handler.getSubscriber(getSubscriptionId(), userId);
                }
                catch(Exception e) {}
                if(subscriber == null)
                {
                    subscriber = new Subscriber();
                    subscriber.setSubscriptionId(getSubscriptionId());
                    subscriber.setUserId(userId);
                    subscriber.setDateSubscribed(new Date());
                    subscriber.setState(Subscription.STATE_PENDING);
                    handler.addSubscriber(subscriber);
                    setUserId(userId);
                    setState(true);

                    return new Forward(FORWARD_SUCCESS, null, false);
                }
            }
            catch(Exception e)
            {
                Log.getLog(AddSubscriberForm.class).error(e);
            }
        }
        return new Forward(FORWARD_FAIL, null, false);
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    public String getSubscriptionId()
    {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId)
    {
        this.subscriptionId = subscriptionId;
    }

    public Button getButtonSave()
    {
        return buttonSave;
    }

    public void setButtonSave(Button buttonSave)
    {
        this.buttonSave = buttonSave;
    }

    public Button getButtonCancel()
    {
        return buttonCancel;
    }

    public void setButtonCancel(Button buttonCancel)
    {
        this.buttonCancel = buttonCancel;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    public SelectBox getUserSelect()
    {
        return userSelect;
    }

    public void setUserSelect(SelectBox userSelect)
    {
        this.userSelect = userSelect;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }
}
