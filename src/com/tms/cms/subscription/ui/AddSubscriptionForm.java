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
import kacang.util.UuidGenerator;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: May 12, 2003
 * Time: 5:53:59 PM
 * To change this template use Options | File Templates.
 */
public class AddSubscriptionForm extends BaseSubscriptionForm
{
    private static final String ADD_SUBSCRIPTION_ID = "-1";
    private static final String FORWARD_SUCCESS = "kacang.services.security.ui.AddSubscriptionForm.success";
    private static final String FORWARD_FAIL = "kacang.services.security.ui.AddSubscriptionForm.fail";

    public AddSubscriptionForm() {}

    public AddSubscriptionForm(String name)
    {
        super(name);
    }

    public String getId()
    {
        return ADD_SUBSCRIPTION_ID;
    }

    public Forward onValidate(Event evt)
    {
        try
        {
            if(! isState())
            {
                setMessage("");
                SubscriptionHandler handler = (SubscriptionHandler) Application.getInstance().getModule(SubscriptionHandler.class);
                DaoQuery properties = new DaoQuery();
                properties.addProperty(new OperatorEquals("name", subscriptionName.getValue(), DaoOperator.OPERATOR_AND));
                Collection list = handler.getSubscriptions(properties, 0, 1, null, false);
                if(list.isEmpty())
                {
                    Subscription subscription = new Subscription();
                    subscription.setId(UuidGenerator.getInstance().getUuid());
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
                    handler.addSubscription(subscription);
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
            Log.getLog(AddSubscriptionForm.class).error(e.toString(), e);
            return new Forward(FORWARD_FAIL, null, false);
        }

        init();
        setState(true);
        return new Forward(FORWARD_SUCCESS, null, false);
    }
}
