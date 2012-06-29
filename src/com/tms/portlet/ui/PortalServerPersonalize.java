package com.tms.portlet.ui;

import kacang.stdui.Form;
import kacang.stdui.SelectBox;
import kacang.stdui.Button;
import kacang.util.Log;
import kacang.services.security.User;
import kacang.Application;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorIn;
import kacang.model.operator.DaoOperator;
import com.tms.portlet.*;
import com.tms.portlet.theme.ThemeManager;

import java.util.*;

import org.apache.commons.collections.SequencedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Nov 10, 2003
 * Time: 9:44:58 AM
 * To change this template use Options | File Templates.
 */
public class PortalServerPersonalize extends Form
{
    public static final String FORWARD_ADD = "add";
    public static final String FORWARD_UPDATE = "update";
    public static final String FORWARD_CANCEL = "cancel";
    public static final String DEFAULT_TEMPLATE = "portal/portalserverpersonalize";

    private SelectBox themes;
    private SelectBox portlets;
    private Button update;
    private Button cancel;
    private Button addPortlet;
    private Portlet selectedPortlet;

    public PortalServerPersonalize()
    {
    }

    public PortalServerPersonalize(String name)
    {
        super(name);
    }

    public void init()
    {
        try
        {
            selectedPortlet = null;

            update = new Button("update");
            addChild(update);
            update.setText(Application.getInstance().getMessage("portlet.label.update","Update"));
            update.setOnClick("if(!confirm('"+Application.getInstance().getMessage("portlet.label.changethemeprompt","Choosing A New Theme Will Reset All Personalized Settings For Your Portlets. Are You Sure You Want To Continue ?")+"')) return false;");

            cancel = new Button("cancel");
            addChild(cancel);
            cancel.setText(Application.getInstance().getMessage("portlet.label.cancel","Cancel"));

            addPortlet = new Button("addPortlet");
            addChild(addPortlet);
            addPortlet.setText(Application.getInstance().getMessage("portlet.label.addPortlet","Add Portlet"));

            themes = new SelectBox("themes");
            addChild(themes);
            initThemes();

            portlets = new SelectBox("portlets");
            addChild(portlets);
            initPortlets();
        }
        catch(Exception e)
        {
            Log.getLog(getClass()).error(e);
        }
    }

    public Forward onValidate(Event evt)
    {
        Forward forward = null;
        String button = findButtonClicked(evt);
        if(update.getAbsoluteName().equals(button))
        {
            processUpdateAction();
            forward = new Forward(FORWARD_UPDATE);
        }
        else if(cancel.getAbsoluteName().equals(button))
        {
            forward = new Forward(FORWARD_CANCEL);
        }
        else if(addPortlet.getAbsoluteName().equals(button))
        {
            Map map = portlets.getSelectedOptions();
            String portletId = (String) map.keySet().iterator().next();
            if(!(portletId.equals("-1")))
            {
                processAddAction(portletId);
                forward = new Forward(FORWARD_ADD);
            }
        }
        else
        {
            initSelectedPortlet();
        }
        return forward;
    }

    public void onRequest(Event evt)
    {
        try
        {
            initThemes();
            initPortlets();
        }
        catch (PortletException e)
        {
            Log.getLog(getClass()).error(e);
        }
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    private void initThemes() throws PortletException
    {
        PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
        User user = getWidgetManager().getUser();

        Collection themeList = handler.getAvailableThemes(user.getId());
        Map themeMap = new HashMap();
        for(Iterator i = themeList.iterator(); i.hasNext();)
        {
            Theme theme = (Theme) i.next();
            themeMap.put(theme.getThemeId(), theme.getThemeName());
        }
        themes.setOptionMap(themeMap);
        themes.setSelectedOptions(new String[] {handler.getUserTheme(user.getId()).getThemeId()});
    }

    private void initPortlets() throws PortletException
    {
        PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
        User user = getWidgetManager().getUser();

        Collection userPortlets = handler.getEntities(user.getId(), false);
        Collection portletId = new ArrayList();
        for(Iterator i = userPortlets.iterator(); i.hasNext();)
        {
            Entity entity = (Entity) i.next();
            portletId.add(entity.getPortletId());
        }
        Collection availablePortlets = handler.getAvailablePortlets(user.getId());
        Collection availablePortletsId = new ArrayList();
        for(Iterator i = availablePortlets.iterator(); i.hasNext();)
        {
            Portlet portlet = (Portlet) i.next();
            availablePortletsId.add(portlet.getPortletId());
        }

        Map portletMap = new SequencedHashMap();
        portletMap.put("-1", Application.getInstance().getMessage("portlet.label.selectAPortlet","Select A Portlet"));
        if (availablePortletsId.size() > 0)
        {
            DaoQuery query = new DaoQuery();
            query.addProperty(new OperatorIn("portletId", portletId.toArray(), DaoOperator.OPERATOR_NAN));
            query.addProperty(new OperatorIn("portletId", availablePortletsId.toArray(), DaoOperator.OPERATOR_AND));
            Collection portletList = handler.getPortlets(query, 0, -1, "portletName", false, false);
            for(Iterator i = portletList.iterator(); i.hasNext();)
            {
                Portlet portlet = (Portlet) i.next();
                portletMap.put(portlet.getPortletId(), portlet.getPortletName());
            }
        }
        portlets.removeAllOptions();
        portlets.setOptionMap(portletMap);
        portlets.setOnChange("submit();");
    }

    private void initSelectedPortlet()
    {
        try
        {
            PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
            Map map = portlets.getSelectedOptions();
            String portletId = (String) map.keySet().iterator().next();
            if(portletId.equals("-1"))
                selectedPortlet = null;
            else
                selectedPortlet = handler.getPortlet(portletId);
        }
        catch(PortletException e)
        {
            Log.getLog(getClass()).error(e);
        }
    }

    private void processUpdateAction()
    {
        try
        {
            PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
            User user = getWidgetManager().getUser();
            Theme originalTheme = handler.getUserTheme(user.getId());
            Map map = themes.getSelectedOptions();
            String themeId = (String) map.keySet().iterator().next();
            if(!(originalTheme.getThemeId().equals(themeId)))
            {
                //handler.deleteUserEntity(user.getId());
                Theme theme = handler.getTheme(themeId);
                ThemeManager manager = (ThemeManager) Class.forName(theme.getThemeManagerClass()).newInstance();
                manager.initUserThemes(themeId, user.getId());
                initPortlets();
            }
        }
        catch (Exception e)
        {
            Log.getLog(getClass()).error(e);
        }
    }

    private void processAddAction(String portletId)
    {
        try
        {
            PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
            User user = getWidgetManager().getUser();
            Theme theme = handler.getUserTheme(user.getId());
            ThemeManager manager = (ThemeManager) Class.forName(theme.getThemeManagerClass()).newInstance();
            manager.addEntity(user.getId(), portletId);
            initPortlets();
            portlets.setSelectedOptions(new String[] {});
            selectedPortlet = null;
        }
        catch (Exception e)
        {
            Log.getLog(getClass()).error(e);
        }
    }

    public SelectBox getThemes()
    {
        return themes;
    }

    public void setThemes(SelectBox themes)
    {
        this.themes = themes;
    }

    public SelectBox getPortlets()
    {
        return portlets;
    }

    public void setPortlets(SelectBox portlets)
    {
        this.portlets = portlets;
    }

    public Portlet getSelectedPortlet()
    {
        return selectedPortlet;
    }

    public void setSelectedPortlet(Portlet selectedPortlet)
    {
        this.selectedPortlet = selectedPortlet;
    }

    public Button getUpdate()
    {
        return update;
    }

    public void setUpdate(Button update)
    {
        this.update = update;
    }

    public Button getCancel()
    {
        return cancel;
    }

    public void setCancel(Button cancel)
    {
        this.cancel = cancel;
    }

    public Button getAddPortlet()
    {
        return addPortlet;
    }

    public void setAddPortlet(Button addPortlet)
    {
        this.addPortlet = addPortlet;
    }
}
