package com.tms.cms.subscription;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.OperatorIn;
import kacang.services.scheduling.BaseJob;
import kacang.services.scheduling.JobTaskExecutionContext;
import kacang.services.scheduling.SchedulingException;
import kacang.services.security.SecurityService;
import kacang.util.Log;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: May 23, 2003
 * Time: 4:10:34 PM
 * To change this template use Options | File Templates.
 */
public class SubscriptionJob extends BaseJob
{
    public void execute(JobTaskExecutionContext context) throws SchedulingException
    {
        try
        {
            DaoQuery properties;
            Collection list;
            Subscription subscription;
            Subscriber subscriber;
            SubscriptionHandler handler = (SubscriptionHandler) Application.getInstance().getModule(SubscriptionHandler.class);
            Collection freeSubscriptions = new ArrayList();

            //Retrieving all subscriptions that are periodic
            properties = new DaoQuery();
            properties.addProperty(new OperatorEquals("months", new Integer(0), DaoOperator.OPERATOR_AND));
            list = handler.getSubscriptions(properties, 0, -1, null, false);
            for(Iterator i = list.iterator(); i.hasNext();)
            {
                subscription = (Subscription) i.next();
                freeSubscriptions.add(subscription.getId());
            }
            //Retrieving all active subscribers in periodic subscriptions
            properties = new DaoQuery();
            properties.addProperty(new OperatorEquals("state", Subscriber.STATE_ACTIVE, DaoOperator.OPERATOR_AND));
            properties.addProperty(new OperatorIn("subscriptionId", freeSubscriptions.toArray(), DaoOperator.OPERATOR_NAN));
            list = handler.getSubscribers(properties, 0, -1, null, false);
            for(Iterator i = list.iterator(); i.hasNext();)
            {
                subscriber = (Subscriber) i.next();
                expireSubscriber(subscriber);
            }
        }
        catch(Exception e)
        {
            Log.getLog(SubscriptionJob.class).error(e);
        }
    }

    private void expireSubscriber(Subscriber subscriber)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(subscriber.getDateExpire());
        if(calendar.after(new Date()))
        {
            //Expire User
            SubscriptionHandler handler = (SubscriptionHandler) Application.getInstance().getModule(SubscriptionHandler.class);
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            try
            {
                handler.setSubscriberState(Subscriber.STATE_EXPIRED, subscriber.getSubscriptionId(), subscriber.getUserId());
                service.unassignGroups(subscriber.getUserId(), new String[] {subscriber.getSubscription().getGroupId()});
            }
            catch (Exception e)
            {
                Log.getLog(SubscriptionJob.class).error(e);
            }
        }
    }
}
