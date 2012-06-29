package com.tms.cms.subscription;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.DefaultModule;
import kacang.services.security.SecurityService;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: May 10, 2003
 * Time: 9:28:53 PM
 * To change this template use Options | File Templates.
 */
public class SubscriptionHandler extends DefaultModule
{
    public static final String PERMISSION_SUBSCRIPTION_ADD = "com.tms.cms.subscription.Subscription.add";
    public static final String PERMISSION_SUBSCRIPTION_EDIT = "com.tms.cms.subscription.Subscription.edit";
    public static final String PERMISSION_SUBSCRIPTION_DELETE = "com.tms.cms.subscription.Subscription.delete";
    public static final String PERMISSION_SUBSCRIPTION_VIEW = "com.tms.cms.subscription.Subscription.view";
    public static final String PERMISSION_SUBSCRIPTION_APPROVE = "com.tms.cms.subscription.Subscription.approve";
    public static final String PERMISSION_SUBSCRIPTION_RENEW = "com.tms.cms.subscription.Subscription.renew";

    public SubscriptionHandler() {}

    public void init() {}

    public Subscription getSubscription(String id) throws SubscriptionException
    {
        try
        {
            if(id == null)
                throw new SubscriptionException("Subscription ID Not Specified");
            SubscriptionDao dao = (SubscriptionDao) getDao();
            return dao.selectSubscription(id);
        }
        catch(Exception e)
        {
            throw new SubscriptionException(e.toString(), e);
        }
    }

    public Collection getSubscriptions(int start, int maxResults) throws SubscriptionException
    {
        try
        {
            SubscriptionDao dao = (SubscriptionDao) getDao();
            return dao.selectSubscriptions(start, maxResults);
        }
        catch(Exception e)
        {
            throw new SubscriptionException(e.toString(), e);
        }
    }

    public Collection getSubscriptions(DaoQuery properties, int start, int maxResults, String sort, boolean descending) throws SubscriptionException
    {
        try
        {
            if(properties == null)
                throw new SubscriptionException("Invalid Dao Property Specified");
            SubscriptionDao dao = (SubscriptionDao) getDao();
            return dao.selectSubscriptions(properties, start, maxResults, sort, descending);
        }
        catch(Exception e)
        {
            throw new SubscriptionException(e.toString(), e);
        }
    }

    public Collection getSubscriptionsBySubscriber(String userId) throws SubscriptionException
    {
        try
        {
            if(userId == null)
                throw new SubscriptionException("Invalid User ID Specified");
            SubscriptionDao dao = (SubscriptionDao) getDao();
            return dao.selectSubscriptionsBySubscriber(userId);
        }
        catch(Exception e)
        {
            throw new SubscriptionException(e.toString(), e);
        }
    }

    public Subscriber getSubscriber(String subscriptionId, String userId) throws SubscriptionException
    {
        try
        {
            if(subscriptionId == null || userId == null)
                throw new SubscriptionException("Invalid Subscription/User ID Specified");
            SubscriptionDao dao = (SubscriptionDao) getDao();
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            Subscriber subscriber = dao.selectSubscriber(subscriptionId, userId);
            subscriber.setUser(service.getUser(userId));
            return subscriber;
        }
        catch(Exception e)
        {
            throw new SubscriptionException(e.toString(), e);
        }
    }

    public Collection getSubscribers(int start, int maxResults) throws SubscriptionException
    {
        try
        {
            SubscriptionDao dao = (SubscriptionDao) getDao();
            return convertSubscriberList(dao.selectSubscribers(start, maxResults));
        }
        catch(Exception e)
        {
            throw new SubscriptionException(e.toString(), e);
        }
    }

    public Collection getSubscribers(DaoQuery properties, int start, int maxResults, String sort, boolean descending) throws SubscriptionException
    {
        try
        {
            if(properties == null)
                throw new SubscriptionException("Invalid Dao Property Specified");
            SubscriptionDao dao = (SubscriptionDao) getDao();
            return convertSubscriberList(dao.selectSubscribers(properties, start, maxResults, sort, descending));
        }
        catch(Exception e)
        {
            throw new SubscriptionException(e.toString(), e);
        }
    }

    public Collection getSubscribersBySubscription(String subscriptionId) throws SubscriptionException
    {
        try
        {
            if(subscriptionId == null)
                throw new SubscriptionException("Invalid Subscription ID Specified");
            SubscriptionDao dao = (SubscriptionDao) getDao();
            return convertSubscriberList(dao.selectSubscribersBySubscription(subscriptionId));
        }
        catch(Exception e)
        {
            throw new SubscriptionException(e.toString(), e);
        }
    }

    public Collection getSubscriberHistory(DaoQuery properties, int start, int maxResults, String sort, boolean descending) throws SubscriptionException
    {
        try
        {
            if(properties == null)
                throw new SubscriptionException("Invalid Dao Property Specified");
            SubscriptionDao dao = (SubscriptionDao) getDao();
            return dao.selectSubscriberHistory(properties, start, maxResults, sort, descending);
        }
        catch(Exception e)
        {
            throw new SubscriptionException(e.toString(), e);
        }
    }

    public int getSubscriptionCount(DaoQuery properties) throws SubscriptionException
    {
        try
        {
            if(properties == null)
                throw new SubscriptionException("Invalid Dao Property Specified");
            SubscriptionDao dao = (SubscriptionDao) getDao();
            return dao.selectSubscriptionCount(properties);
        }
        catch(Exception e)
        {
            throw new SubscriptionException(e.toString(), e);
        }
    }

    public int getSubscriberCount(DaoQuery properties) throws SubscriptionException
    {
        try
        {
            if(properties == null)
                throw new SubscriptionException("Invalid Dao Property Specified");
            SubscriptionDao dao = (SubscriptionDao) getDao();
            return dao.selectSubscriberCount(properties);
        }
        catch(Exception e)
        {
            throw new SubscriptionException(e.toString(), e);
        }
    }

    public void addSubscription(Subscription subscription) throws SubscriptionException
    {
        try
        {
            if(subscription == null || subscription.getId() == null || subscription.getName() == null || subscription.getState() == null)
                throw new SubscriptionException("Invalid Subscription. Unable To Add");
            SubscriptionDao dao = (SubscriptionDao) getDao();
            dao.insertSubscription(subscription);
        }
        catch(Exception e)
        {
            throw new SubscriptionException(e.toString(), e);
        }
    }

    public void addSubscriber(Subscriber subscriber) throws SubscriptionException
    {
        try
        {
            if(subscriber == null || subscriber.getSubscriptionId() == null || subscriber.getUserId() == null || subscriber.getDateSubscribed() == null || subscriber.getState() == null)
                throw new SubscriptionException("Invalid Subscriber. Unable To Add");
            SubscriptionDao dao = (SubscriptionDao) getDao();
            dao.insertSubscriber(subscriber);
        }
        catch(Exception e)
        {
            throw new SubscriptionException(e.toString(), e);
        }
    }

    public void addSubscriberHistory(SubscriptionHistory history) throws SubscriptionException
    {
        try
        {
            if(history == null)
                throw new SecurityException("Invalid History Parameters. Unable To Add");
            SubscriptionDao dao = (SubscriptionDao) getDao();
            dao.insertHistory(history);
        }
        catch(Exception e)
        {
            throw new SubscriptionException(e.toString(), e);
        }
    }

    public void editSubscription(Subscription subscription) throws SubscriptionException
    {
        try
        {
            if(subscription == null || subscription.getId() == null || subscription.getName() == null || subscription.getState() == null)
                throw new SubscriptionException("Invalid Subscription. Unable To Update");
            SubscriptionDao dao = (SubscriptionDao) getDao();
            dao.updateSubscription(subscription);
        }
        catch(Exception e)
        {
            throw new SubscriptionException(e.toString(), e);
        }
    }

    public void editSubscriber(Subscriber subscriber) throws SubscriptionException
    {
        try
        {
            if(subscriber == null || subscriber.getSubscriptionId() == null || subscriber.getUserId() == null || subscriber.getDateSubscribed() == null || subscriber.getState() == null)
                throw new SubscriptionException("Invalid Subscriber. Unable To Add");
            SubscriptionDao dao = (SubscriptionDao) getDao();
            dao.updateSubscriber(subscriber);
        }
        catch(Exception e)
        {
            throw new SubscriptionException(e.toString(), e);
        }
    }

    public void removeSubscription(String id) throws SubscriptionException
    {
        try
        {
            if(id == null)
                throw new SubscriptionException("Invalid Subscription ID. Unable To Delete");
            Subscriber subscriber;
            SubscriptionDao dao = (SubscriptionDao) getDao();
            Collection list = dao.selectSubscribersBySubscription(id);
            for(Iterator it = list.iterator(); it.hasNext();)
            {
                subscriber = (Subscriber) it.next();
                removeSubscriber(subscriber.getSubscriptionId(), subscriber.getUserId());
            }
            dao.deleteSubscription(id);
        }
        catch(Exception e)
        {
            throw new SubscriptionException(e.toString(), e);
        }
    }

    public void removeSubscriber(String subscriptionId, String userId) throws SubscriptionException
    {
        try
        {
            if(subscriptionId == null || userId == null)
                throw new SubscriptionException("Invalid Subscription/User ID. Unable To Delete");
            SubscriptionDao dao = (SubscriptionDao) getDao();
            removeSubscriberHistory(subscriptionId, userId);
            dao.deleteSubscriber(subscriptionId, userId);
        }
        catch(Exception e)
        {
            throw new SubscriptionException(e.toString(), e);
        }
    }

    public void removeSubscriberHistory(String id) throws SubscriptionException
    {
        try
        {
            if(id == null)
                throw new SubscriptionException("Invalid History ID. Unable To Delete");
            SubscriptionDao dao = (SubscriptionDao) getDao();
            dao.deleteHistory(id);
        }
        catch(Exception e)
        {
            throw new SubscriptionException(e.toString(), e);
        }
    }

    public void removeSubscriberHistory(String subscriptionId, String userId) throws SubscriptionException
    {
        try
        {
            if(subscriptionId == null && userId == null)
                throw new SubscriptionException("Invalid Subscription/User ID. Unable To Delete");
            SubscriptionDao dao = (SubscriptionDao) getDao();
            dao.deleteHistory(subscriptionId, userId);
        }
        catch(Exception e)
        {
            throw new SubscriptionException(e.toString(), e);
        }
    }

    public void renewSubscription(SubscriptionHistory history) throws SubscriptionException
    {
        try
        {
            if(history == null)
                throw new SubscriptionException("Invalid Subscription History ID. Unable To Renew Subscription");
            if(history.getSubscription().getMonths() > 0)
            {
                SubscriptionDao dao = (SubscriptionDao) getDao();
                Subscriber subscriber = dao.selectSubscriber(history.getSubscriptionId(), history.getUserId());
                Calendar calendar = Calendar.getInstance();
                if(subscriber.getDateExpire() == null)
                    subscriber.setDateExpire(new Date());
                calendar.setTime(subscriber.getDateExpire());
                calendar.add(Calendar.MONTH, history.getSubscription().getMonths());
                subscriber.setDateExpire(calendar.getTime());
                subscriber.setDateLastRenewed(new Date());
                subscriber.setState(Subscriber.STATE_ACTIVE);
                dao.updateSubscriber(subscriber);
                //Adding History
                addSubscriberHistory(history);
            }
        }
        catch(Exception e)
        {
            throw new SubscriptionException(e.toString(), e);
        }
    }

    public void setSubscriberState(String state, String subscriptionId, String userId) throws SubscriptionException
    {
        try
        {
            if(state == null)
                throw new SubscriptionException("Invalid state. Unable To Update State");
            SubscriptionDao dao = (SubscriptionDao) getDao();
            Subscriber subscriber = dao.selectSubscriber(subscriptionId, userId);
            subscriber.setState(state);
            dao.updateSubscriber(subscriber);
        }
        catch(Exception e)
        {
            throw new SubscriptionException(e.toString(), e);
        }
    }

    private Collection convertSubscriberList(Collection list) throws SubscriptionException
    {
        try
        {
            Subscriber subscriber;
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            for(Iterator i = list.iterator(); i.hasNext();)
            {
                subscriber = (Subscriber) i.next();
                subscriber.setUser(service.getUser(subscriber.getUserId()));
                subscriber.setSubscription(getSubscription(subscriber.getSubscriptionId()));
            }
        }
        catch(Exception e)
        {
            throw new SubscriptionException(e.toString(), e);
        }
        return list;
    }
}
