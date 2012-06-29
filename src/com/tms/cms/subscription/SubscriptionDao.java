package com.tms.cms.subscription;

import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DataSourceDao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: May 10, 2003
 * Time: 11:04:38 PM
 * To change this template use Options | File Templates.
 */
public class SubscriptionDao extends DataSourceDao
{
    public void init() throws DaoException
    {
        super.update("CREATE TABLE subscription_package(id VARCHAR(35) NOT NULL, name VARCHAR(250) NOT NULL, description TEXT, price VARCHAR(7), months INT NOT NULL, groupId VARCHAR(255) NOT NULL, state VARCHAR(1) NOT NULL, PRIMARY KEY(id))", null);
        super.update("CREATE TABLE subscription_subscriber(subscriptionId VARCHAR(35) NOT NULL, userId VARCHAR(255) NOT NULL, dateSubscribed DATETIME NOT NULL, dateExpire DATETIME, dateLastRenewed DATETIME, state VARCHAR(3) NOT NULL, PRIMARY KEY(subscriptionId, userId))", null);
        super.update("CREATE TABLE subscription_history(id VARCHAR(35) NOT NULL, subscriptionId VARCHAR(35) NOT NULL, userId VARCHAR(255) NOT NULL, actionDate DATETIME NOT NULL, action VARCHAR(100) NOT NULL, method VARCHAR(250), methodNumber VARCHAR(250), amount DOUBLE, PRIMARY KEY(id))", null);
    }

    public Subscription selectSubscription(String id) throws DaoException, DataObjectNotFoundException
    {
        Collection list = super.select("SELECT id, name, description, price, months, groupId, state FROM subscription_package WHERE id = ?", Subscription.class, new String[] {id}, 0, 1);
        if(list.size() <= 0) throw new DataObjectNotFoundException("Subscription Id " + id + " Unavailable");
        return (Subscription) list.iterator().next();
    }

    public Collection selectSubscriptions(int start, int maxResults) throws DaoException
    {
        return super.select("SELECT id, name, description, price, months, groupId, state FROM subscription_package", Subscription.class, null, start, maxResults);
    }

    public Collection selectSubscriptions(DaoQuery properties, int start, int maxResults, String sort, boolean descending) throws DaoException
    {
        return super.select("SELECT id, name, description, price, months, groupId, state FROM subscription_package WHERE id = id" + properties.getStatement() + getSort(sort, descending), Subscription.class, properties.getArray(), start, maxResults);
    }

    //TODO: Use nested selects when mySQL version permits
    public Collection selectSubscriptionsBySubscriber(String userId) throws DaoException
    {
        return super.select("SELECT subscription_package.id, subscription_package.name, subscription_package.description, subscription_package.price, subscription_package.months, subscription_package.groupId, subscription_package.state FROM subscription_package, subscription_subscriber WHERE subscription_package.id = subscription_subscriber.subscriptionId AND subscription_subscriber.userId = ?", Subscription.class, new String[] {userId}, 0, -1);
    }

    public Subscriber selectSubscriber(String subscriptionId, String userId) throws DaoException, DataObjectNotFoundException
    {
        Collection list = super.select("SELECT subscriptionId, userId, dateSubscribed, dateExpire, dateLastRenewed, state FROM subscription_subscriber WHERE subscriptionId = ? AND userId = ?", Subscriber.class, new String[] {subscriptionId, userId}, 0, 1);
        if(list.size() <= 0) throw new DataObjectNotFoundException("Subscriber " + userId + " With Subscription " + subscriptionId + " Unavailable");
        return (Subscriber) list.iterator().next();
    }

    public Collection selectSubscribers(int start, int maxResults) throws DaoException
    {
        return super.select("SELECT subscriptionId, userId, dateSubscribed, dateExpire, dateLastRenewed, state FROM subscription_subscriber", Subscriber.class, null, 0, 1);
    }

    public Collection selectSubscribers(DaoQuery properties, int start, int maxResults, String sort, boolean descending) throws DaoException
    {
        return super.select("SELECT subscriptionId, userId, dateSubscribed, dateExpire, dateLastRenewed, state FROM subscription_subscriber WHERE subscriptionId = subscriptionId" + properties.getStatement() + getSort(sort, descending), Subscriber.class, properties.getArray(), start, maxResults);
    }

    public Collection selectSubscribersBySubscription(String subscriptionId) throws DaoException
    {
        return super.select("SELECT subscriptionId, userId, dateSubscribed, dateExpire, dateLastRenewed, state FROM subscription_subscriber where subscriptionId = ?", Subscriber.class, new String[] {subscriptionId}, 0, 1);
    }

    public Collection selectSubscriberHistory(DaoQuery properties, int start, int maxResults, String sort, boolean descending) throws DaoException
    {
        return super.select("SELECT actionDate, action, method, methodNumber, amount FROM subscription_history WHERE subscriptionId = subscriptionId" + properties.getStatement() + getSort(sort, descending), SubscriptionHistory.class, properties.getArray(), start, maxResults);
    }

    public int selectSubscriptionCount(DaoQuery properties) throws DaoException
    {
        Collection list = super.select("SELECT COUNT(id) AS value FROM subscription_package WHERE id = id" + properties.getStatement(), HashMap.class, properties.getArray(), 0, -1);
        Map map = (HashMap) list.iterator().next();
        return Integer.parseInt(map.get("value").toString());
    }

    public int selectSubscriberCount(DaoQuery properties) throws DaoException
    {
        Collection list = super.select("SELECT COUNT(subscriptionId) AS value FROM subscription_subscriber WHERE subscriptionId = subscriptionId" + properties.getStatement(), HashMap.class, properties.getArray(), 0, -1);
        Map map = (HashMap) list.iterator().next();
        return Integer.parseInt(map.get("value").toString());
    }

    private String getSort(String sort, boolean descending)
    {
        String strSort = "";
        if(sort != null)
        {
            strSort += " ORDER BY " + sort;
            if(descending)
                strSort += " DESC";
        }
        return strSort;
    }

    public void insertSubscription(Subscription subscription) throws DaoException
    {
        super.update("INSERT INTO subscription_package(id, name, description, price, months, groupId, state) VALUES(#id#, #name#, #description#, #price#, #months#, #groupId#, #state#)", subscription);
    }

    public void insertSubscriber(Subscriber subscriber) throws DaoException
    {
        super.update("INSERT INTO subscription_subscriber(subscriptionId, userId, dateSubscribed, dateExpire, dateLastRenewed, state) VALUES(#subscriptionId#, #userId#, #dateSubscribed#, #dateExpire#, #dateLastRenewed#, #state#)", subscriber);
    }

    public void insertHistory(SubscriptionHistory history) throws DaoException
    {
        super.update("INSERT INTO subscription_history(id, subscriptionId, userId, actionDate, action, method, methodNumber, amount) VALUES(#id#, #subscriptionId#, #userId#, #actionDate#, #action#, #method#, #methodNumber#, #amount#)", history);
    }

    public void updateSubscription(Subscription subscription) throws DaoException
    {
        super.update("UPDATE subscription_package SET name=#name#, description=#description#, price=#price#, months=#months#, groupId=#groupId#, state=#state# WHERE id=#id#", subscription);
    }

    public void updateSubscriber(Subscriber subscriber) throws DaoException
    {
        super.update("UPDATE subscription_subscriber SET dateExpire=#dateExpire#, dateLastRenewed=#dateLastRenewed#, state=#state# WHERE subscriptionId=#subscriptionId# AND userId=#userId# ", subscriber);
    }

    public void deleteSubscription(String id) throws DaoException
    {
        super.update("DELETE FROM subscription_package WHERE id = ?", new String[] {id});
    }

    public void deleteSubscriber(String subscriptionId, String userId) throws DaoException
    {
        String query = "DELETE FROM subscription_subscriber WHERE subscriptionId = subscriptionId";
        Collection args = new ArrayList();
        if(subscriptionId != null)
        {
            query += " AND subscriptionId = ?";
            args.add(subscriptionId);
        }
        if(userId != null)
        {
            query += " AND userId = ?";
            args.add(userId);
        }
        super.update(query, args.toArray());
    }

    public void deleteHistory(String id) throws DaoException
    {
        super.update("DELETE FROM subscription_history WHERE id = ?", new String[] {id});
    }

    public void deleteHistory(String subscriptionId, String userId) throws DaoException
    {
        String query = "DELETE FROM subscription_history WHERE subscriptionId = subscriptionId";
        Collection args = new ArrayList();
        if(subscriptionId != null)
        {
            query += " AND subscriptionId = ?";
            args.add(subscriptionId);
        }
        if(userId != null)
        {
            query += " AND userId = ?";
            args.add(userId);
        }
        super.update(query, args.toArray());
    }


}
