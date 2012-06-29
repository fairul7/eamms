package com.tms.portlet.ui;

import kacang.services.security.User;
import kacang.services.security.SecurityService;
import kacang.Application;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.util.Log;

import com.tms.portlet.theme.ThemeManager;
import com.tms.portlet.PortletHandler;
import com.tms.portlet.Theme;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Nov 5, 2003
 * Time: 1:29:25 PM
 * To change this template use Options | File Templates.
 */
public class PortalServer extends Form
{
    public static final String DEFAULT_TEMPLATE = "portal/portalserver";
    public static final String FORWARD_PERSONALIZE = "personalize";
    public static final String FORWARD_PORTLETS = "portlets";
    public static final String FORWARD_THEMES = "themes";

    private Button personalize;
    private Button themes;
    private Button portlets;
    private ThemeManager manager;

    public PortalServer()
    {
    }

    public PortalServer(String name)
    {
        super(name);
    }

    public void init()
    {
        try
        {
            PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
            User user = getWidgetManager().getUser();

            personalize = new Button("personalize");
            personalize.setText(Application.getInstance().getMessage("portlet.label.personalize","Personalize"));
            addChild(personalize);
            themes = new Button("themes");
            themes.setText(Application.getInstance().getMessage("portlet.label.theme","Theme"));
            addChild(themes);
            portlets = new Button("portlets");
            portlets.setText(Application.getInstance().getMessage("portlet.label.portlets","Portlets"));
            addChild(portlets);
            Theme theme = handler.getUserTheme(user.getId());
            theme.setThemeManager("manager");
            manager = theme.getThemeManager();
            addChild(manager);
            manager.init();

            checkPermission();
        }
        catch (Exception e)
        {
            Log.getLog(getClass()).error(e);
        }
        super.init();
    }

    private void checkPermission()
    {
        User user = getWidgetManager().getUser();
        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
        try
        {
            if(service.hasPermission(user.getId(), PortletHandler.PERMISSION_THEME_VIEW, null, null))
                themes.setHidden(false);
            else
                themes.setHidden(true);

            if(service.hasPermission(user.getId(), PortletHandler.PERMISSION_PORTLET_VIEW, null, null))
                portlets.setHidden(false);
            else
                portlets.setHidden(true);
        }
        catch(Exception e)
        {
            Log.getLog(PortalServer.class).error(e.getMessage(), e);
        }
    }

    public Forward actionPerformed(Event evt)
    {
        Forward forward = null;
        String button = findButtonClicked(evt);
        if(personalize.getAbsoluteName().equals(button))
            forward = new Forward(FORWARD_PERSONALIZE);
        else if(themes.getAbsoluteName().equals(button))
            forward = new Forward(FORWARD_THEMES);
        else if(portlets.getAbsoluteName().equals(button))
            forward = new Forward(FORWARD_PORTLETS);
        return forward;
    }

    public void onRequest(Event evt)
    {
        checkPermission();
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    public Button getPersonalize()
    {
        return personalize;
    }

    public void setPersonalize(Button personalize)
    {
        this.personalize = personalize;
    }

    public Button getThemes()
    {
        return themes;
    }

    public void setThemes(Button themes)
    {
        this.themes = themes;
    }

    public Button getPortlets()
    {
        return portlets;
    }

    public void setPortlets(Button portlets)
    {
        this.portlets = portlets;
    }

    public ThemeManager getManager()
    {
        return manager;
    }

    public void setManager(ThemeManager manager)
    {
        this.manager = manager;
    }
}
