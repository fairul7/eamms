package com.tms.cms.subscription;

import kacang.services.security.User;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: May 26, 2003
 * Time: 11:07:51 AM
 * To change this template use Options | File Templates.
 */
public class SubscriptionHistory
{
    public static final String HISTORY_ACTION_SUBSCRIBE = "Subscribe";
    public static final String HISTORY_ACTION_APPROVE = "Approve";
    public static final String HISTORY_ACTION_RENEW = "Renew";

    private String id;
    private Subscription subscription;
    private User user;
    private Date actionDate;
    private String action;
    private String method;
    private String methodNumber;
    private double amount;
    private String subscriptionId;
    private String userId;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public Subscription getSubscription()
    {
        return subscription;
    }

    public void setSubscription(Subscription subscription)
    {
        this.subscription = subscription;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Date getActionDate()
    {
        return actionDate;
    }

    public void setActionDate(Date actionDate)
    {
        this.actionDate = actionDate;
    }

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public String getMethod()
    {
        return method;
    }

    public void setMethod(String method)
    {
        this.method = method;
    }

    public String getMethodNumber()
    {
        return methodNumber;
    }

    public void setMethodNumber(String methodNumber)
    {
        this.methodNumber = methodNumber;
    }

    public double getAmount()
    {
        return amount;
    }

    public void setAmount(double amount)
    {
        this.amount = amount;
    }

    public String getSubscriptionId()
    {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId)
    {
        this.subscriptionId = subscriptionId;
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
