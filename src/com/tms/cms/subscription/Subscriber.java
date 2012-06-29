package com.tms.cms.subscription;

import kacang.services.security.User;

import java.util.Date;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: May 10, 2003
 * Time: 9:23:06 PM
 * To change this template use Options | File Templates.
 */
public class Subscriber implements Serializable
{
    //TODO: Enhance TableModel to support nested property calls so redundant properties can be removed
    public static final String STATE_PENDING = "0";
    public static final String STATE_ACTIVE = "1";
    public static final String STATE_EXPIRED = "2";

    private String subscriptionId;
    private String userId;
    private Date dateSubscribed;
    private Date dateLastRenewed;
    private Date dateExpire;
    private String state;
    private User user;
    private Subscription subscription;

    public Subscriber() {}

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

    public Date getDateSubscribed()
    {
        return dateSubscribed;
    }

    public void setDateSubscribed(Date dateSubscribed)
    {
        this.dateSubscribed = dateSubscribed;
    }

    public Date getDateLastRenewed()
    {
        return dateLastRenewed;
    }

    public void setDateLastRenewed(Date dateLastRenewed)
    {
        this.dateLastRenewed = dateLastRenewed;
    }

    public Date getDateExpire()
    {
        return dateExpire;
    }

    public void setDateExpire(Date dateExpire)
    {
        this.dateExpire = dateExpire;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public String getUsername()
    {
        return user.getUsername();
    }

    public void setUsername(String username)
    {
        user.setUsername(username);
    }

    public String getFirstName()
    {
        return (String) user.getProperty("firstName");
    }

    public void setFirstName(String firstName)
    {
        user.setProperty("firstName", firstName);
    }

    public String getLastName()
    {
        return (String) user.getProperty("lastName");
    }

    public void setLastName(String lastName)
    {
        user.setProperty("lastName", lastName);
    }

    public Subscription getSubscription()
    {
        return subscription;
    }

    public void setSubscription(Subscription subscription)
    {
        this.subscription = subscription;
    }
}
