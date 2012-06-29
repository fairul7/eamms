package com.tms.portlet;

import kacang.util.Log;
import com.tms.portlet.theme.ThemeManager;

import java.util.Collection;
import java.util.ArrayList;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Oct 15, 2003
 * Time: 3:50:40 PM
 * To change this template use Options | File Templates.
 */
public class Theme implements Serializable
{
    public static final String DEFAULT_THEME_PROPERTY = "com.tms.portlet.default-theme";

    private String themeId;
    private String themeManagerClass;
    private String themeName;
    private String themeDescription;
    private ThemeManager themeManager;
    private Collection defaultPortlets;
    private Collection groups;

    public Theme() throws Exception
    {
        defaultPortlets = new ArrayList();
        groups = new ArrayList();
    }

    public String getThemeId()
    {
        return themeId;
    }

    public void setThemeId(String themeId)
    {
        this.themeId = themeId;
    }

    public String getThemeManagerClass()
    {
        return themeManagerClass;
    }

    public void setThemeManagerClass(String themeManagerClass)
    {
        this.themeManagerClass = themeManagerClass;
    }

    public String getThemeName()
    {
        return themeName;
    }

    public void setThemeName(String themeName)
    {
        this.themeName = themeName;
    }

    public String getThemeDescription()
    {
        return themeDescription;
    }

    public void setThemeDescription(String themeDescription)
    {
        this.themeDescription = themeDescription;
    }

    public ThemeManager getThemeManager()
    {
        return themeManager;
    }

    public void setThemeManager(ThemeManager themeManager)
    {
        this.themeManager = themeManager;
    }

    public void setThemeManager(String name)
    {
        try
        {
            if(themeManagerClass != null || "".equals(themeManagerClass))
            {
                themeManager = (ThemeManager) Class.forName(themeManagerClass).newInstance();
                themeManager.setName(name);
            }
        }
        catch (InstantiationException e)
        {
            Log.getLog(getClass()).error(e);
        }
        catch (IllegalAccessException e)
        {
            Log.getLog(getClass()).error(e);
        }
        catch (ClassNotFoundException e)
        {
            Log.getLog(getClass()).error(e);
        }
    }

    public String getId()
    {
        return themeId;
    }

    public Collection getDefaultPortlets()
    {
        return defaultPortlets;
    }

    public void setDefaultPortlets(Collection defaultPortlets)
    {
        this.defaultPortlets = defaultPortlets;
    }

    public Collection getGroups()
    {
        return groups;
    }

    public void setGroups(Collection groups)
    {
        this.groups = groups;
    }
}
