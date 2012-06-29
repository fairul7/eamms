package com.tms.portlet.ui;

import kacang.util.Log;
import kacang.util.UuidGenerator;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.DaoOperator;
import kacang.ui.Forward;
import kacang.ui.Event;
import com.tms.portlet.PortletHandler;
import com.tms.portlet.Portlet;
import com.tms.portlet.Theme;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.collections.SequencedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Nov 13, 2003
 * Time: 10:19:37 AM
 * To change this template use Options | File Templates.
 */
public class PortletAdd extends PortletForm
{
    public PortletAdd()
    {
    }

    public PortletAdd(String name)
    {
        super(name);
    }

    public void init()
    {
        try
        {
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            User user= getWidgetManager().getUser();
            if(service.hasPermission(user.getId(), PortletHandler.PERMISSION_PORTLET_ADD, null, null))
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

    protected void initThemes()
    {
        SequencedHashMap map = new SequencedHashMap();
        PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
        try
        {
            Collection groups = handler.getThemes(false);
            for(Iterator i = groups.iterator(); i.hasNext();)
            {
                Theme theme = (Theme) i.next();
                map.put(theme.getThemeId(), theme.getThemeName());
            }
        }
        catch(Exception e)
        {
            Log.getLog(PortletAdd.class).error(e);
        }
        themes.setLeftValues(map);
        themes.setRightValues(new SequencedHashMap());
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
                Collection list = handler.getPortlets(query, 0, 1, null, false, false);
                if(list.size() <= 0)
                {
                    //Adding New Portlet
                    String portletId = UuidGenerator.getInstance().getUuid();
                    Portlet portlet = new Portlet();
                    portlet.setPortletId(portletId);
                    portlet.setPortletName((String) portletName.getValue());
                    portlet.setPortletTitle((String) portletTitle.getValue());
                    portlet.setPortletClass((String) portletClass.getValue());
                    portlet.setPortletDescription((String) portletDescription.getValue());
                    portlet.setPortletPreferences(new HashMap());
                    handler.addPortlet(portlet);
                    //Adding Themes
                    for(Iterator i = themes.getRightValues().keySet().iterator(); i.hasNext();)
                    {
                        String themeId = (String) i.next();
                        handler.addThemePortlet(themeId, portletId);
                    }
                    init();
                    forward = new Forward(FORWARD_SUCCESS);
                }
                else
                {
                    message = Application.getInstance().getMessage("portlet.label.portletNameAlreadyInUse","Portlet Name Already In Use");
                    portletName.setInvalid(true);
                }
            }
        }
        catch(Exception e)
        {
            Log.getLog(getClass()).error(e);
            forward = new Forward(FORWARD_FAILED);
        }
        return forward;
    }
}
