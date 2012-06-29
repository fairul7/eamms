package com.tms.cms.subscription.ui;

import com.tms.cms.subscription.Subscription;
import com.tms.cms.subscription.SubscriptionHandler;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: May 13, 2003
 * Time: 3:38:40 PM
 * To change this template use Options | File Templates.
 */
public class EditSubscriptionForm extends BaseSubscriptionForm
{
    private static final String FORWARD_SUCCESS = "com.tms.cms.subscription.ui.EditSubscriptionForm.success";
    private static final String FORWARD_FAIL = "com.tms.cms.subscription.ui.EditSubscriptionForm.fail";

//    private SubscriberForm subscriberForm;

    public EditSubscriptionForm() {}

    public EditSubscriptionForm(String name)
    {
        super(name);
    }

    public void init()
    {
        if(!idIsEmpty())
        {
            try
            {
                super.init();

//                subscriberForm = new SubscriberForm("subscriberForm");
//                addChild(subscriberForm);
//                subscriberForm.setSubscriptionId(getId());
//                subscriberForm.init();

                SubscriptionHandler handler = (SubscriptionHandler) Application.getInstance().getModule(SubscriptionHandler.class);
                Subscription subscription = handler.getSubscription(getId());
                subscriptionName.setValue(subscription.getName());
                description.setValue(subscription.getDescription());
                Collection list = new ArrayList();
                list.add(subscription.getGroupId());
                group.setValue(list);
                price.setValue(subscription.getPrice());
                months.setValue(Integer.toString(subscription.getMonths()));
                if(subscription.getState().equals("1"))
                    subscriptionState.setChecked(true);
                else
                    subscriptionState.setChecked(false);
            }
            catch(Exception e)
            {
                Log.getLog(EditSubscriptionForm.class).error(e.toString(), e);
            }
        }
    }

    public Forward onValidate(Event evt)
    {
        if(!idIsEmpty())
        {
            try
            {
                if(! isState())
                {
                    setMessage("");
                    SubscriptionHandler handler = (SubscriptionHandler) Application.getInstance().getModule(SubscriptionHandler.class);
                    DaoQuery properties = new DaoQuery();
                    properties.addProperty(new OperatorEquals("name", subscriptionName.getValue(), DaoOperator.OPERATOR_AND));
                    properties.addProperty(new OperatorEquals("id", id, DaoOperator.OPERATOR_NAN));
                    Collection list = handler.getSubscriptions(properties, 0, 1, null, false);
                    if(list.isEmpty())
                    {
                        Subscription subscription = new Subscription();
                        subscription.setId(id);
                        subscription.setName((String) subscriptionName.getValue());
                        subscription.setDescription((String) description.getValue());
                        Collection selectedGroup = (Collection) group.getValue();
                        subscription.setGroupId((String) selectedGroup.iterator().next());
                        subscription.setPrice((String) price.getValue());
                        subscription.setMonths(Integer.parseInt((String) months.getValue()));
                        if(subscriptionState.isChecked())
                            subscription.setState(new String("1"));
                        else
                            subscription.setState(new String("0"));
                        handler.editSubscription(subscription);
                        init();
                        setState(true);
                        return new Forward(FORWARD_SUCCESS, null, false);
                    }
                    else
                    {
	                    Application application = Application.getInstance();
                        setMessage(application.getMessage("general.error.subscriptionName", "Subscription Name Already In Use"));
                        return new Forward(FORWARD_FAIL, null, false);
                    }
                }
            }
            catch(Exception e)
            {
                Log.getLog(EditSubscriptionForm.class).error(e.toString(), e);
                return new Forward(FORWARD_FAIL, null, false);
            }
        }
        return new Forward(FORWARD_FAIL, null, false);
    }

    public String getDefaultTemplate()
    {
        if (id.equals(""))
            return "";
        else
            return super.getDefaultTemplate();
    }

    private boolean idIsEmpty()
    {
        if(getId() != null)
        {
            if(!("".equals(getId())))
            {
                return false;
            }
        }
        return true;
    }

//    public SubscriberForm getSubscriberForm()
//    {
//        return subscriberForm;
//    }
//
//    public void setSubscriberForm(SubscriberForm subscriberForm)
//    {
//        this.subscriberForm = subscriberForm;
//    }

}
