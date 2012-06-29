package com.tms.portlet;

import java.util.Map;
import java.io.Serializable;

/**
 * The portlet class represents the schema of a portlet. Portlets do not
 * have the concept of user ownership since they are ment to be meta information
 * rather than real instantiations.
 * @author Michael Yap
 * @version 1.0.0
 * @see com.tms.portlet.Entity
 */
public class Portlet implements Serializable
{
    private String portletId;
    private String portletClass;
    private String portletName;
    private String portletDescription;
    private String portletTitle;
    private Map portletPreferences;

    public String getPortletId()
    {
        return portletId;
    }

    public void setPortletId(String portletId)
    {
        this.portletId = portletId;
    }

    public String getPortletClass()
    {
        return portletClass;
    }

    public void setPortletClass(String portletClass)
    {
        this.portletClass = portletClass;
    }

    public String getPortletName()
    {
        return portletName;
    }

    public void setPortletName(String portletName)
    {
        this.portletName = portletName;
    }

    public String getPortletDescription()
    {
        return portletDescription;
    }

    public void setPortletDescription(String portletDescription)
    {
        this.portletDescription = portletDescription;
    }

    public String getPortletTitle()
    {
        return portletTitle;
    }

    public void setPortletTitle(String portletTitle)
    {
        this.portletTitle = portletTitle;
    }

    public Map getPortletPreferences()
    {
        return portletPreferences;
    }

    /**
     * Sets a map which key value should state the preference key - preference default value
     * @param portletPreferences
     */
    public void setPortletPreferences(Map portletPreferences)
    {
        this.portletPreferences = portletPreferences;
    }

    public void addPreference(String key, String defaultValue)
    {
        portletPreferences.put(key, defaultValue);
    }

    /**
     * The equals method for a Portlet checks the portlet Id of 2 Portlet objects
     * @param o
     * @return
     */
    public boolean equals(Object o)
    {
        boolean equal = false;
        if(o instanceof Portlet)
            if(((Portlet) o).getPortletId().equals(portletId))
                equal = true;
        return equal;
    }
}
