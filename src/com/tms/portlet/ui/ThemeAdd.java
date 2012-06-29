package com.tms.portlet.ui;

import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.security.Group;
import kacang.Application;
import kacang.stdui.ComboSelectBox;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorIn;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import com.tms.portlet.PortletHandler;
import com.tms.portlet.Theme;
import com.tms.portlet.Portlet;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;

import org.apache.commons.collections.SequencedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Nov 11, 2003
 * Time: 12:17:02 PM
 * To change this template use Options | File Templates.
 */
public class ThemeAdd extends ThemeForm
{
    public ThemeAdd()
    {
    }

    public ThemeAdd(String name)
    {
        super(name);
    }

    public void init()
    {
        try
        {
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            User user= getWidgetManager().getUser();
            if(service.hasPermission(user.getId(), PortletHandler.PERMISSION_THEME_ADD, null, null))
            {
                super.init();
                submit.setText(Application.getInstance().getMessage("portlet.label.add","Add"));
            }
        }
        catch(Exception e)
        {
            Log.getLog(getClass()).error(e);
        }
    }

    public void initDefaultPortlets()
    {
        PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
        SequencedHashMap map = new SequencedHashMap();
        try
        {
            Collection portlets = handler.getPortlets(false);
            for(Iterator i = portlets.iterator(); i.hasNext();)
            {
                Portlet portlet = (Portlet) i.next();
                map.put(portlet.getPortletId(), portlet.getPortletName());
            }
            defaultPortlets.setLeftValues(map);
            defaultPortlets.setRightValues(new SequencedHashMap());
        }
        catch(Exception e)
        {
            Log.getLog(ThemeAdd.class).error(e);
        }
    }

    public void initGroups()
    {
        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
        SequencedHashMap map = new SequencedHashMap();
        try
        {
            Collection groups = service.getGroups(new DaoQuery(), 0, -1, "groupName", false);
            for(Iterator i = groups.iterator(); i.hasNext();)
            {
                Group group = (Group) i.next();
                map.put(group.getId(), group.getName());
            }
            this.groups.setLeftValues(map);
            this.groups.setRightValues(new SequencedHashMap());
        }
        catch(Exception e)
        {
            Log.getLog(ThemeAdd.class).error(e);
        }
    }

    public Forward onValidate(Event evt)
    {
        Forward forward = null;
        message = "";
        try
        {
            if(submit.getAbsoluteName().equals(findButtonClicked(evt)))
            {
                PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
                SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
                DaoQuery query = new DaoQuery();
                query.addProperty(new OperatorEquals("themeName", themeName.getValue(), DaoOperator.OPERATOR_AND));
                Collection list = handler.getThemes(query, 0, 1, null, false, false);
                if(list.size() <= 0)
                {
                    String id = UuidGenerator.getInstance().getUuid();
                    //Adding New Theme
                    Theme theme = new Theme();
                    theme.setThemeId(id);
                    theme.setThemeName((String) themeName.getValue());
                    theme.setThemeManagerClass((String) themeManagerClass.getValue());
                    theme.setThemeDescription((String) themeDescription.getValue());
                    theme.setDefaultPortlets(retrieveDefaultPortlets());
                    theme.setGroups(retrieveGroups());
                    handler.addTheme(theme);
                    init();
                    forward = new Forward(ThemeForm.FORWARD_SUCCESS);
                }
                else
                {
                    message = Application.getInstance().getMessage("portlet.label.themeNameAlreadyInUse","Theme Name Already In Use");
                    themeName.setInvalid(true);
                }
            }
        }
        catch(Exception e)
        {
            Log.getLog(getClass()).error(e);
            forward = new Forward(ThemeForm.FORWARD_FAILED);
        }
        return forward;
    }
}
