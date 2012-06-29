package com.tms.portlet.ui;

import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.security.Group;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorIn;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;
import com.tms.portlet.PortletHandler;
import com.tms.portlet.Theme;
import com.tms.portlet.PortletException;
import com.tms.portlet.Portlet;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.collections.SequencedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Nov 11, 2003
 * Time: 4:05:48 PM
 * To change this template use Options | File Templates.
 */
public class ThemeEdit extends ThemeForm
{
    private String themeId;

    public ThemeEdit()
    {
    }

    public ThemeEdit(String name)
    {
        super(name);
    }

    public void init()
    {
        if(!(themeId == null || "".equals(themeId)))
        {
            try
            {
                SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
                User user= getWidgetManager().getUser();
                if(service.hasPermission(user.getId(), PortletHandler.PERMISSION_THEME_EDIT, null, null))
                {
                    super.init();
                    submit.setText(Application.getInstance().getMessage("portlet.label.update","Update"));
                    //Initializing Values
                    PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
                    Theme theme = handler.getTheme(themeId);
                    if(theme != null)
                    {
                        themeName.setValue(theme.getThemeName());
                        themeManagerClass.setValue(theme.getThemeManagerClass());
                        themeDescription.setValue(theme.getThemeDescription());
                    }
                    else
                        throw new PortletException("Theme: " + themeId + " Could Not Be Retrieved");
                }
            }
            catch(Exception e)
            {
                Log.getLog(getClass()).error(e);
            }
        }
    }

    public void initDefaultPortlets()
    {
        if(!(themeId == null || "".equals(themeId)))
        {
            SequencedHashMap leftValues = new SequencedHashMap();
            SequencedHashMap rightValues = new SequencedHashMap();
            Collection portletId = new ArrayList();
            PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
            try
            {
                Theme theme = handler.getTheme(themeId);
                for(Iterator i = theme.getDefaultPortlets().iterator(); i.hasNext();)
                {
                    Portlet portlet = (Portlet) i.next();
                    rightValues.put(portlet.getPortletId(), portlet.getPortletName());
                    portletId.add(portlet.getPortletId());
                }
                DaoQuery query = new DaoQuery();
                query.addProperty(new OperatorIn("portletId", portletId.toArray(), DaoOperator.OPERATOR_NAN));
                Collection portlets = handler.getPortlets(query, 0, -1, "portletName", false, false);
                for(Iterator i = portlets.iterator(); i.hasNext();)
                {
                    Portlet portlet = (Portlet) i.next();
                    leftValues.put(portlet.getPortletId(), portlet.getPortletName());
                }
                defaultPortlets.setLeftValues(leftValues);
                defaultPortlets.setRightValues(rightValues);
            }
            catch(Exception e)
            {
                Log.getLog(ThemeEdit.class).error(e);
            }
        }
    }

    public void initGroups()
    {
        if(!(themeId == null || "".equals(themeId)))
        {
            PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            SequencedHashMap leftValues = new SequencedHashMap();
            SequencedHashMap rightValues = new SequencedHashMap();
            Collection groupId = new ArrayList();
            try
            {
                Theme theme = handler.getTheme(themeId);
                for(Iterator i = theme.getGroups().iterator(); i.hasNext();)
                {
                    Group group = (Group) i.next();
                    rightValues.put(group.getId(), group.getName());
                    groupId.add(group.getId());
                }
                DaoQuery query = new DaoQuery();
                query.addProperty(new OperatorIn("id", groupId.toArray(), DaoOperator.OPERATOR_NAN));
                Collection groups = service.getGroups(query, 0, -1, "groupName", false);
                for(Iterator i = groups.iterator(); i.hasNext();)
                {
                    Group group = (Group) i.next();
                    leftValues.put(group.getId(), group.getName());
                }
                this.groups.setLeftValues(leftValues);
                this.groups.setRightValues(rightValues);
            }
            catch(Exception e)
            {
                Log.getLog(ThemeEdit.class).error(e);
            }
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
                DaoQuery query = new DaoQuery();
                query.addProperty(new OperatorEquals("themeName", themeName.getValue(), DaoOperator.OPERATOR_AND));
                query.addProperty(new OperatorEquals("themeId", themeId, DaoOperator.OPERATOR_NAN));
                Collection list = handler.getThemes(query, 0, 1, null, false, true);
                if(list.size() <= 0)
                {
                    //Editing Theme
                    Theme theme = new Theme();
                    theme.setThemeId(themeId);
                    theme.setThemeName((String) themeName.getValue());
                    theme.setThemeManagerClass((String) themeManagerClass.getValue());
                    theme.setThemeDescription((String) themeDescription.getValue());
                    theme.setDefaultPortlets(retrieveDefaultPortlets());
                    theme.setGroups(retrieveGroups());
                    handler.editTheme(theme);
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

    public String getThemeId()
    {
        return themeId;
    }

    public void setThemeId(String themeId)
    {
        this.themeId = themeId;
        init();
    }

}
