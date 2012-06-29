package com.tms.portlet.theme;

import java.util.*;
import java.io.Serializable;

import kacang.ui.Widget;
import kacang.ui.Event;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Oct 17, 2003
 * Time: 11:28:54 AM
 * To change this template use Options | File Templates.
 */
public abstract class ThemeManager extends Widget implements Serializable
{
    public static final String DEFAULT_THEME_ROOT = "themes/";
    public static final String DEFAULT_PORTLET_ROOT = "/WEB-INF/portlets/";

    public static final String LABEL_ENTITY_KEY = "entityId";
    public static final String LABEL_ENTITY = "entity";

    public static final String FORWARD_UP = "up";
    public static final String FORWARD_DOWN = "down";
    public static final String FORWARD_LEFT = "left";
    public static final String FORWARD_RIGHT = "right";
    public static final String FORWARD_MAXIMIZE = "maximize";
    public static final String FORWARD_MINIMIZE = "minimize";
    public static final String FORWARD_EDIT = "edit";
    public static final String FORWARD_CLOSE = "close";

    protected Map portlets;

    public ThemeManager()
    {
    }

    public ThemeManager(String name)
    {
        super(name);
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_THEME_ROOT + getThemeTemplate();
    }

    public void onRequest(Event evt)
    {
        init();
    }

    public Map getPortlets()
    {
        return portlets;
    }

    public void setPortlets(Map portlets)
    {
        this.portlets = portlets;
    }

    public void addPortletsEntry(String key, Object value)
    {
        if(portlets == null)
            portlets = new HashMap();
        portlets.put(key, value);
    }

    public void removePortletsEntry(String key)
    {
        portlets.remove(key);
    }

    public abstract void addEntity(String userId, String portletId);

    public abstract String getPortletHeader();

    public abstract String getPortletFooter();

    public abstract String getThemeTemplate();

    public abstract String getThemeHeader();

    public abstract String getThemeFooter();

    public abstract String getStyleSheets();

    public abstract void initUserThemes(String themeId, String userId);
}
