package com.tms.cms.subscription;

import kacang.services.security.Group;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: May 10, 2003
 * Time: 5:37:03 PM
 * To change this template use Options | File Templates.
 */
public class Subscription implements Serializable
{
    public static final String STATE_PENDING = "0";
    public static final String STATE_ACTIVE = "1";

    private String id;
    private String name;
    private String description;
    private String price;
    private int months;
    private String groupId;
    private String state;
    private Group group;

    public Subscription() {}

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public int getMonths()
    {
        return months;
    }

    public void setMonths(int months)
    {
        this.months = months;
    }

    public String getGroupId()
    {
        return groupId;
    }

    public void setGroupId(String groupId)
    {
        this.groupId = groupId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Group getGroup()
    {
        return group;
    }

    public void setGroup(Group group)
    {
        this.group = group;
    }
}
