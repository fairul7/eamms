package com.tms.portlet.theme.themes;

import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorParenthesis;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.OperatorGreaterThan;
import kacang.util.Log;
import com.tms.portlet.theme.ThemeManager;
import com.tms.portlet.PortletHandler;
import com.tms.portlet.Entity;
import com.tms.portlet.PortletException;

import java.util.Collection;
import java.util.Iterator;

public class Ekp2005Theme extends DefaultTheme
{
	public static final String DEFAULT_TEMPLATE = "ekp2005/index";
    public static final String DEFAULT_HEADER = "ekp2005/header";
    public static final String DEFAULT_FOOTER = "ekp2005/footer";
    public static final String DEFAULT_STYLESHEET = "ekp2005/default.css";

	public static final String PARAMETER_SOURCE = "source";
	public static final String PARAMETER_TARGET = "target";
    public static final String PREFIX_SOURCE = "entity_";
	public static final String PREFIX_TARGET = "placeholder_";
    public static final String PREFIX_FINAL = "final_";

	public static final String FORWARD_DRAG_DROP = "drag_drop";
	public static final String COMMAND_DRAG_DROP = "drag_drop";

	public Forward actionPerformed(Event evt)
    {
        Forward forward = new Forward();
        String type = evt.getType();
        String entityId = evt.getParameter(ThemeManager.LABEL_ENTITY_KEY);
        try
        {
			//The Ekp2004 theme is not built to recognize the move actions. Drag and Drop replaces that
			if(type.equals(COMMAND_DRAG_DROP))
				forward = actionDragDrop(evt.getParameter(PARAMETER_SOURCE), evt.getParameter(PARAMETER_TARGET));
            else if((entityId != null || "".equals(entityId)) || (type == null || "".equals(type)))
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

	protected Forward actionDragDrop(String source, String target)
	{
		Forward forward = null;

		//Decipher source and target strings
        String entityId = "";
        PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);

		if(!(source.indexOf(PREFIX_SOURCE) == -1))
        	entityId = source.substring(PREFIX_SOURCE.length());
		try
		{
			Entity sourceEntity = handler.getEntity(entityId);
            if(!(target.indexOf(PREFIX_TARGET) == -1))
			{
				target = target.substring(PREFIX_TARGET.length());
				if(!(target.indexOf(PREFIX_FINAL) == -1))
				{
					//Final Placeholders
					sourceEntity.setEntityLocation(target.substring(PREFIX_FINAL.length()) + "_column");
					DaoQuery query =  new DaoQuery();
					query.addProperty(new OperatorEquals("userId", getWidgetManager().getUser().getId(), DaoOperator.OPERATOR_AND));
					query.addProperty(new OperatorEquals("entityLocation", sourceEntity.getEntityLocation(), DaoOperator.OPERATOR_AND));
					Collection entities = handler.getEntities(query, 0, 1, "entityOrder", true, false);
					if(entities.size() > 0)
					{
						Entity entity = (Entity) entities.iterator().next();
						sourceEntity.setEntityOrder(entity.getEntityOrder() + 1);
					}
					else
					{
						sourceEntity.setEntityOrder(1);
					}
					handler.editEntity(sourceEntity);
				}
				else
				{
					Entity entity = handler.getEntity(target);
					if(!(entity == null))
					{
						sourceEntity.setEntityLocation(entity.getEntityLocation());
						sourceEntity.setEntityOrder(entity.getEntityOrder());
						int count = entity.getEntityOrder();
                        //Retrieving all entities below the target
						DaoQuery query =  new DaoQuery();
						OperatorParenthesis parenthesis = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
						parenthesis.addOperator(new OperatorEquals("entityOrder", new Integer(sourceEntity.getEntityOrder()), null));
						parenthesis.addOperator(new OperatorGreaterThan("entityOrder", new Integer(sourceEntity.getEntityOrder()), DaoOperator.OPERATOR_OR));
						query.addProperty(parenthesis);
						query.addProperty(new OperatorEquals("userId", getWidgetManager().getUser().getId(), DaoOperator.OPERATOR_AND));
						query.addProperty(new OperatorEquals("entityLocation", sourceEntity.getEntityLocation(), DaoOperator.OPERATOR_AND));
						Collection entities = handler.getEntities(query, 0, -1, "entityOrder", false, true);
						for (Iterator i = entities.iterator(); i.hasNext();)
						{
							count++;
							Entity ent = (Entity) i.next();
							ent.setEntityOrder(count);
							handler.editEntity(ent);
						}
						//Updating source entity
						handler.editEntity(sourceEntity);
					}
				}
			}
			forward = new Forward(FORWARD_DRAG_DROP);
		}
		catch (PortletException e)
		{
			Log.getLog(getClass()).error("Error while processing drag and dropped entity: " + entityId, e);
		}

		return forward;
	}

    public String getThemeTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    public String getThemeHeader()
    {
        return DEFAULT_HEADER;
    }

    public String getThemeFooter()
    {
        return DEFAULT_FOOTER;
    }

    public String getStyleSheets()
    {
        return DEFAULT_STYLESHEET;
    }
}
