package com.tms.portlet.theme.themes;

import com.tms.portlet.theme.ThemeManager;
import com.tms.portlet.*;

import kacang.services.security.User;
import kacang.Application;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorLessThan;
import kacang.model.operator.OperatorGreaterThan;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Oct 22, 2003
 * Time: 2:44:35 PM
 * To change this template use Options | File Templates.
 */
public class DefaultTheme extends ThemeManager
{
    public static final String DEFAULT_TEMPLATE = "default/index";
    public static final String DEFAULT_PORTLET_HEADER = "portletHeader.jsp";
    public static final String DEFAULT_PORTLET_FOOTER = "portletFooter.jsp";

    public static final String PROPERTY_MAXIMIZE = "maximize";
    public static final String PROPERTY_EDIT = "edit";

    public static final String COLUMN_LEFT_LABEL = "left_column";
    public static final String COLUMN_CENTER_LABEL = "center_column";
    public static final String COLUMN_RIGHT_LABEL = "right_column";

    public static final String COMMAND_MOVE_UP = "move_up";
    public static final String COMMAND_MOVE_DOWN = "move_down";
    public static final String COMMAND_MOVE_LEFT = "move_left";
    public static final String COMMAND_MOVE_RIGHT = "move_right";
    public static final String COMMAND_MAXIMIZE = "maximize";
    public static final String COMMAND_EDIT = "edit";
    public static final String COMMAND_CLOSE = "close";

    public DefaultTheme()
    {
    }

    public DefaultTheme(String name)
    {
        super(name);
    }

    public void init()
    {
        try
        {
            DaoQuery query;

            User user = getWidgetManager().getUser();
            PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);

            query = new DaoQuery();
            query.addProperty(new OperatorEquals("entityLocation", COLUMN_LEFT_LABEL, DaoOperator.OPERATOR_AND));
            query.addProperty(new OperatorEquals("userId", user.getId(), DaoOperator.OPERATOR_AND));
            addPortletsEntry(COLUMN_LEFT_LABEL, handler.getEntities(query, 0, -1, "entityOrder", false, true));

            query = new DaoQuery();
            query.addProperty(new OperatorEquals("entityLocation", COLUMN_CENTER_LABEL, DaoOperator.OPERATOR_AND));
            query.addProperty(new OperatorEquals("userId", user.getId(), DaoOperator.OPERATOR_AND));
            addPortletsEntry(COLUMN_CENTER_LABEL, handler.getEntities(query, 0, -1, "entityOrder", false, true));

            query = new DaoQuery();
            query.addProperty(new OperatorEquals("entityLocation", COLUMN_LEFT_LABEL, DaoOperator.OPERATOR_NAN));
            query.addProperty(new OperatorEquals("entityLocation", COLUMN_CENTER_LABEL, DaoOperator.OPERATOR_NAN));
            query.addProperty(new OperatorEquals("userId", user.getId(), DaoOperator.OPERATOR_AND));
            addPortletsEntry(COLUMN_RIGHT_LABEL, handler.getEntities(query, 0, -1, "entityOrder", false, true));
        }
        catch(Exception e)
        {
            Log.getLog(DefaultTheme.class).error(e.getMessage(), e);
        }
    }

    public Forward actionPerformed(Event evt)
    {
        Forward forward = new Forward();
        String type = evt.getType();
        String entityId = evt.getParameter(ThemeManager.LABEL_ENTITY_KEY);
        try
        {
            if((entityId != null || "".equals(entityId)) || (type == null || "".equals(type)))
            {
                PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
                Entity entity = handler.getEntity(entityId);
                if(type.equals(COMMAND_MOVE_UP))
                    forward = actionUp(entity);
                else if(type.equals(COMMAND_MOVE_DOWN))
                    forward = actionDown(entity);
                else if(type.equals(COMMAND_MOVE_LEFT))
                    forward = actionLeft(entity);
                else if(type.equals(COMMAND_MOVE_RIGHT))
                    forward = actionRight(entity);
                else if(type.equals(COMMAND_MAXIMIZE))
                    forward = actionMaximize(entity);
                else if(type.equals(COMMAND_CLOSE))
                    forward = actionClose(entity);
                else if(type.equals(COMMAND_EDIT))
                    forward = actionEdit(entity, evt.getRequest());
            }
            init();
        }
        catch(Exception e)
        {
            String message = "" + e.getMessage();
            Log.getLog(getClass()).error(message, e);
        }
        return forward;
    }

    public void addEntity(String userId, String portletId)
    {
        try
        {
            PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
            DaoQuery query = new DaoQuery();
            query.addProperty(new OperatorEquals("userId", userId, DaoOperator.OPERATOR_AND));
            query.addProperty(new OperatorEquals("entityLocation", COLUMN_LEFT_LABEL, DaoOperator.OPERATOR_AND));
            Collection entities = handler.getEntities(query, 0, 1, "entityOrder", true, false);
            int count = 0;
            if(entities.size() > 0)
            {
                Entity object = (Entity) entities.iterator().next();
                count = object.getEntityOrder();
            }
            Portlet portlet = handler.getPortlet(portletId);
            //Constructing Entity
            Entity entity = new Entity();
            entity.setUserId(userId);
            entity.setPortletId(portletId);
            entity.setEntityId(UuidGenerator.getInstance().getUuid());
            entity.setEntityLocation(COLUMN_LEFT_LABEL);
            entity.setEntityOrder(count + 1);
            entity.setEntityState("");
            entity.setPreferences(portlet.getPortletPreferences());
            handler.addEntity(entity);
        }
        catch (PortletException e)
        {
            Log.getLog(getClass()).error(e);
        }
    }

    protected Forward actionUp(Entity entity)
    {
        try
        {
            PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
            int order = entity.getEntityOrder();
            //Retrieving entities on top
            DaoQuery query = new DaoQuery();
            query.addProperty(new OperatorEquals("entityLocation", entity.getEntityLocation(), DaoOperator.OPERATOR_AND));
            query.addProperty(new OperatorLessThan("entityOrder", new Integer(order), DaoOperator.OPERATOR_AND));
            query.addProperty(new OperatorEquals("userId", entity.getUserId(), DaoOperator.OPERATOR_AND));
            Collection entities = handler.getEntities(query, 0, 1, "entityOrder", true, true);
            if(entities.size() > 0)
            {
                Entity swap = (Entity) entities.iterator().next();
                entity.setEntityOrder(swap.getEntityOrder());
                swap.setEntityOrder(order);
                handler.editEntity(entity);
                handler.editEntity(swap);
            }
        }
        catch(Exception e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        return new Forward(ThemeManager.FORWARD_UP);
    }

    protected Forward actionDown(Entity entity)
    {
        try
        {
            PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
            int order = entity.getEntityOrder();
            //Retrieving entities below
            DaoQuery query = new DaoQuery();
            query.addProperty(new OperatorEquals("entityLocation", entity.getEntityLocation(), DaoOperator.OPERATOR_AND));
            query.addProperty(new OperatorGreaterThan("entityOrder", new Integer(order), DaoOperator.OPERATOR_AND));
            query.addProperty(new OperatorEquals("userId", entity.getUserId(), DaoOperator.OPERATOR_AND));
            Collection entities = handler.getEntities(query, 0, 1, "entityOrder", false, true);
            if(entities.size() > 0)
            {
                Entity swap = (Entity) entities.iterator().next();
                entity.setEntityOrder(swap.getEntityOrder());
                swap.setEntityOrder(order);
                handler.editEntity(entity);
                handler.editEntity(swap);
            }
        }
        catch(Exception e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        return new Forward(ThemeManager.FORWARD_DOWN);
    }

    protected Forward actionLeft(Entity entity)
    {
        if(entity.getEntityLocation().equals(COLUMN_RIGHT_LABEL))
            entity.setEntityLocation(COLUMN_CENTER_LABEL);
        else if (entity.getEntityLocation().equals(COLUMN_CENTER_LABEL))
            entity.setEntityLocation(COLUMN_LEFT_LABEL);
        actionHorizontalMovement(entity);
        return new Forward(ThemeManager.FORWARD_LEFT);
    }

    protected Forward actionRight(Entity entity)
    {
        if(entity.getEntityLocation().equals(COLUMN_LEFT_LABEL))
            entity.setEntityLocation(COLUMN_CENTER_LABEL);
        else if(entity.getEntityLocation().equals(COLUMN_CENTER_LABEL))
            entity.setEntityLocation(COLUMN_RIGHT_LABEL);
        actionHorizontalMovement(entity);
        return new Forward(ThemeManager.FORWARD_RIGHT);
    }

    protected Forward actionMaximize(Entity entity)
    {
        return new Forward(ThemeManager.FORWARD_MAXIMIZE);
    }

    protected Forward actionClose(Entity entity)
    {
        try
        {
            PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
            handler.deleteEntity(entity.getEntityId());
        }
        catch(Exception e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        return new Forward(ThemeManager.FORWARD_CLOSE);
    }

    protected Forward actionEdit(Entity entity, HttpServletRequest request)
    {
        Forward forward = new Forward(FORWARD_EDIT);
        if(entity.getPreferences().containsKey(PROPERTY_EDIT))
        {
            PortletPreference preference = (PortletPreference) entity.getPreferences().get(PROPERTY_EDIT);
            //TODO: Fix filter forwarding so we don't have to load information in request scope
            if(!(preference.getPreferenceValue() == null || "".equals(preference.getPreferenceValue())))
                request.setAttribute(ThemeManager.FORWARD_EDIT, preference.getPreferenceValue());
//                forward.setUrl(preference.getPreferenceValue());
        }
        return forward;
    }

    private void actionHorizontalMovement(Entity entity)
    {
        try
        {
            PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
            DaoQuery query = new DaoQuery();
            query.addProperty(new OperatorEquals("entityLocation", entity.getEntityLocation(), DaoOperator.OPERATOR_AND));
            query.addProperty(new OperatorEquals("userId", entity.getUserId(), DaoOperator.OPERATOR_AND));
            Collection entities = handler.getEntities(query, 0, 1, "entityOrder", true, false);
            int order = 1;
            if(entities.size() > 0)
            {
                Entity swap = (Entity) entities.iterator().next();
                order = swap.getEntityOrder() + 1;
            }
            entity.setEntityOrder(order);
            handler.editEntity(entity);
        }
        catch(Exception e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
    }

    public String getThemeTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    public String getPortletHeader()
    {
        return DEFAULT_PORTLET_HEADER;
    }

    public String getPortletFooter()
    {
        return DEFAULT_PORTLET_FOOTER;
    }

    public String getThemeHeader()
    {
        return null;
    }

    public String getThemeFooter()
    {
        return null;
    }

    public String getStyleSheets()
    {
        return null;
    }

    public void initUserThemes(String themeId, String userId)
    {
        PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
        HashMap map = new HashMap();
        Theme theme = null;
        map.put(COLUMN_LEFT_LABEL, new Integer(1));
        map.put(COLUMN_RIGHT_LABEL, new Integer(1));
        map.put(COLUMN_CENTER_LABEL, new Integer(1));
        String currentColumn = COLUMN_LEFT_LABEL;
        try
        {
            theme = handler.getTheme(themeId);
            if(theme != null)
            {
				/*
					Determine if user already has an entitiy set. Break if entities are found, initialize
					if they aren't. Not using handler.getEntities(userId, deepRetrieval) only a single entity
					record needs to be found.
				*/
				DaoQuery query = new DaoQuery();
				query.addProperty(new OperatorEquals("userId", userId, DaoOperator.OPERATOR_AND));
				Collection currentPortlets = handler.getEntities(query, 0, 1, "entityOrder", false, true);
                if(currentPortlets.size() == 0)
				{
					Collection defaultPortlets = theme.getDefaultPortlets();
					Collection entities = handler.getEntities(userId, true);
					//Resetting existing entities
					for(Iterator i = entities.iterator(); i.hasNext();)
					{
						try
						{
							Entity entity = (Entity) i.next();
							if(defaultPortlets.contains(entity.getPortlet()))
							{
								//Writing entity
								Integer count = (Integer) map.get(currentColumn);
								if(count == null)
									count = new Integer(1);

								handler.editEntity(entity);
								defaultPortlets.remove(entity.getPortlet());

								if (entity.getEntityOrder() > count.intValue())
									map.put(currentColumn, new Integer(entity.getEntityOrder() + 1));
							}
							else
							{
								//Removing entity from user profile
								handler.deleteEntity(entity.getEntityId());
							}
						}
						catch(Exception e)
						{
							Log.getLog(DefaultTheme.class).error(e);
						}
					}
					//Inserting new portlets
					for(Iterator i = defaultPortlets.iterator(); i.hasNext();)
					{
						try
						{
							Portlet portlet = (Portlet) i.next();
							Integer count = (Integer) map.get(currentColumn);
							if(count == null)
								count = new Integer(1);

							Entity entity = new Entity();
							entity.setUserId(userId);
							entity.setPortletId(portlet.getPortletId());
							entity.setEntityId(UuidGenerator.getInstance().getUuid());
							entity.setEntityLocation(currentColumn);
							entity.setEntityOrder(count.intValue());
							entity.setEntityState("");
							entity.setPreferences(portlet.getPortletPreferences());
							handler.addEntity(entity);

							map.put(currentColumn, new Integer(count.intValue() + 1));
							if(COLUMN_LEFT_LABEL.equals(currentColumn))
								currentColumn = COLUMN_CENTER_LABEL;
							else if(COLUMN_CENTER_LABEL.equals(currentColumn))
								currentColumn = COLUMN_RIGHT_LABEL;
							else
								currentColumn = COLUMN_LEFT_LABEL;

						}
						catch(Exception e)
						{
							Log.getLog(DefaultTheme.class).error(e);
						}
					}
				}
                handler.editUserTheme(userId, themeId);
            }
        }
        catch(Exception e)
        {
            Log.getLog(PortletHandler.class).error(e);
        }
    }
}
