package com.tms.portlet;

import java.util.*;
import java.io.Serializable;

/**
 * Entities are instantiation of portlets. Each entity is created based on the
 * schema from a Portlet object and belong to specific users.
 * @author Michael Yap
 * @version 1.0.0
 * @see com.tms.portlet.Portlet
 * @see com.tms.portlet.PortletPreference
 */
public class Entity implements Serializable
{
    private String entityId;
    private String userId;
    private String portletId;
    private String entityLocation;
    private int entityOrder;
    private String entityState;
    private Portlet portlet;
    private Map preferences;

    public Entity()
    {
        preferences = new HashMap();
    }

    public String getEntityId()
    {
        return entityId;
    }

    public void setEntityId(String entityId)
    {
        this.entityId = entityId;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getPortletId()
    {
        return portletId;
    }

    public void setPortletId(String portletId)
    {
        this.portletId = portletId;
    }

    public String getEntityLocation()
    {
        return entityLocation;
    }

    public void setEntityLocation(String entityLocation)
    {
        this.entityLocation = entityLocation;
    }

    public int getEntityOrder()
    {
        return entityOrder;
    }

    public void setEntityOrder(int entityOrder)
    {
        this.entityOrder = entityOrder;
    }

    public String getEntityState()
    {
        return entityState;
    }

    public void setEntityState(String entityState)
    {
        this.entityState = entityState;
    }

    public Portlet getPortlet()
    {
        return portlet;
    }

    public void setPortlet(Portlet portlet)
    {
        this.portlet = portlet;
    }

    public Map getPreferences()
    {
        return preferences;
    }

    /**
     * Sets a map which key-value combinations are preference key-PortletPreference object
     * @param preferences
     */
    public void setPreferences(Map preferences)
    {
        this.preferences = preferences;
    }

    public PortletPreference getPreference(String preferenceName)
    {
        return (PortletPreference) preferences.get(preferenceName);
    }

    /**
     * Deprecated method
     * @param name
     * @param value
     * @deprecated 1.0.0
     */
    public void setPreference(String name, String value)
    {
        preferences.put(name, value);
    }
}
