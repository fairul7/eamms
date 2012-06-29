package com.tms.portlet.ui;

import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.security.Group;
import kacang.Application;
import kacang.stdui.Button;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorIn;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;
import com.tms.portlet.PortletHandler;
import com.tms.portlet.Portlet;
import com.tms.portlet.PortletException;
import com.tms.portlet.Theme;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.collections.SequencedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Nov 13, 2003
 * Time: 11:17:20 AM
 * To change this template use Options | File Templates.
 */
public class PortletEdit extends PortletForm
{
    public static final String DEFAULT_TEMPLATE = "portal/portletEditForm";
    public static final String FORWARD_ADD_PREFERENCE = "addPreference";

    private String portletId;
    private Portlet portlet;
    private Button addPreferences;

    public PortletEdit()
    {
    }

    public PortletEdit(String name)
    {
        super(name);
    }

    public void init()
    {
        if(!(portletId == null || "".equals(portletId)))
        {
            try
            {

                SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
                User user= getWidgetManager().getUser();
                if(service.hasPermission(user.getId(), PortletHandler.PERMISSION_PORTLET_EDIT, null, null))
                {
                    super.init();
                    submit.setText(Application.getInstance().getMessage("portlet.label.update","Update"));
                    addPreferences = new Button("addPreferences");
                    addPreferences.setText(Application.getInstance().getMessage("portlet.label.addPreferences","Add Preferences"));
                    addChild(addPreferences);
                    //Initializing Values
                    if(portlet != null)
                    {
                        portletName.setValue(portlet.getPortletName());
                        portletTitle.setValue(portlet.getPortletTitle());
                        portletClass.setValue(portlet.getPortletClass());
                        portletDescription.setValue(portlet.getPortletDescription());
                    }
                    else
                        throw new PortletException("Portlet: " + portletId + "Could Not Be Retrieved");
                }
            }
            catch(Exception e)
            {
                Log.getLog(getClass()).error(e);
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
                query.addProperty(new OperatorEquals("portletName", portletName.getValue(), DaoOperator.OPERATOR_AND));
                query.addProperty(new OperatorEquals("portletId", portletId, DaoOperator.OPERATOR_NAN));
                Collection list = handler.getPortlets(query, 0, 1, null, false, false);
                if(list.size() <= 0)
                {
                    //Editing Portlet
                    portlet.setPortletName((String) portletName.getValue());
                    portlet.setPortletTitle((String) portletTitle.getValue());
                    portlet.setPortletClass((String) portletClass.getValue());
                    portlet.setPortletDescription((String) portletDescription.getValue());
                    handler.editPortlet(portlet);
                    //Adding themes
                    handler.deletePortletThemes(portletId);
                    for(Iterator i = themes.getRightValues().keySet().iterator(); i.hasNext();)
                    {
                        String themeId = (String) i.next();
                        handler.addThemePortlet(themeId, portletId);
                    }
                    forward = new Forward(ThemeForm.FORWARD_SUCCESS);
                }
                else
                {
                    message = Application.getInstance().getMessage("portlet.label.portletNameAlreadyInUse","Portlet Name Already In Use");
                    portletName.setInvalid(true);
                }
            }
            else if(addPreferences.getAbsoluteName().equals(findButtonClicked(evt)))
                forward = new Forward(FORWARD_ADD_PREFERENCE);
        }
        catch(Exception e)
        {
            Log.getLog(getClass()).error(e);
            forward = new Forward(ThemeForm.FORWARD_FAILED);
        }
        return forward;
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    protected void initThemes()
    {
        PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
        SequencedHashMap leftValues = new SequencedHashMap();
        SequencedHashMap rightValues = new SequencedHashMap();
        Collection themeId = new ArrayList();
        try
        {
            Collection themes = handler.getPortletThemes(portletId);
            for(Iterator i = themes.iterator(); i.hasNext();)
            {
                Theme theme = (Theme) i.next();
                rightValues.put(theme.getThemeId(), theme.getThemeName());
                themeId.add(theme.getThemeId());
            }
            DaoQuery query = new DaoQuery();
            query.addProperty(new OperatorIn("themeId", themeId.toArray(), DaoOperator.OPERATOR_NAN));
            Collection available = handler.getThemes(query, 0, -1, "themeName", false, false);
            for(Iterator i = available.iterator(); i.hasNext();)
            {
                Theme theme = (Theme) i.next();
                leftValues.put(theme.getThemeId(), theme.getThemeName());
            }
            this.themes.setLeftValues(leftValues);
            this.themes.setRightValues(rightValues);
        }
        catch(Exception e)
        {
            Log.getLog(PortletEdit.class).error(e);
        }
    }

    public String getPortletId()
    {
        return portletId;
    }

    public void setPortletId(String portletId)
    {
        this.portletId = portletId;
        try
        {
            PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
            portlet = handler.getPortlet(portletId);
        }
        catch(Exception e)
        {
            Log.getLog(getClass()).error(e);
        }
        init();
    }

    public Button getAddPreferences()
    {
        return addPreferences;
    }

    public void setAddPreferences(Button addPreferences)
    {
        this.addPreferences = addPreferences;
    }

    public Portlet getPortlet()
    {
        return portlet;
    }

    public void setPortlet(Portlet portlet)
    {
        this.portlet = portlet;
    }
}
