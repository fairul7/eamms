package com.tms.portlet;

import java.io.Serializable;

/**
 * A PortletPreference signfies a particular preference value for
 * a user and its corresponding Entity.
 * @author Michael Yap
 * @version 1.0.0
 * @see com.tms.portlet.Entity
 */
public class PortletPreference implements Serializable
{
    private String preferenceName;
    //TODO: Make preferenceValue a collection. Handle data storeage and retrieval at Handler and Dao level
    private String preferenceValue;
    private boolean preferenceEditable;

    public String getPreferenceName()
    {
        return preferenceName;
    }

    public void setPreferenceName(String preferenceName)
    {
        this.preferenceName = preferenceName;
    }

    public String getPreferenceValue()
    {
        return preferenceValue;
    }

    public void setPreferenceValue(String preferenceValue)
    {
        this.preferenceValue = preferenceValue;
    }

    public boolean isPreferenceEditable()
    {
        return preferenceEditable;
    }

    public void setPreferenceEditable(boolean preferenceEditable)
    {
        this.preferenceEditable = preferenceEditable;
    }
}
