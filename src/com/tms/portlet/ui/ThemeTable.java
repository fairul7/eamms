package com.tms.portlet.ui;

import kacang.stdui.Table;
import kacang.stdui.TableModel;
import kacang.stdui.TableColumn;
import kacang.stdui.TableAction;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.security.SecurityException;
import kacang.Application;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.model.DaoQuery;
import kacang.util.Log;

import java.util.Collection;
import java.util.Iterator;

import com.tms.portlet.PortletHandler;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Nov 10, 2003
 * Time: 3:53:25 PM
 * To change this template use Options | File Templates.
 */
public class ThemeTable extends Table
{
    public static final String FORWARD_ADD = "add";
    public static final String FORWARD_DELETE = "delete";

    public ThemeTable()
    {
    }

    public ThemeTable(String name)
    {
        super(name);
    }

    public void init()
    {
        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
        User user = getWidgetManager().getUser();
        try
        {
            if(service.hasPermission(user.getId(), PortletHandler.PERMISSION_THEME_VIEW, null, null))
            {
                setModel(new ThemeTableModel());
                setWidth("100%");
            }
        }
        catch (SecurityException e)
        {
            Log.getLog(getClass()).error(e);
        }
    }

    public class ThemeTableModel extends TableModel
    {
        public ThemeTableModel()
        {
            super();
            //Adding Columns
            TableColumn columnName = new TableColumn("themeName", Application.getInstance().getMessage("portlet.label.name","Name"));
            TableColumn columnClass = new TableColumn("themeManagerClass", Application.getInstance().getMessage("portlet.label.themeManager","Theme Manager"));
            addColumn(columnName);
            addColumn(columnClass);
            //Adding Actions
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            User user = getWidgetManager().getUser();
            try
            {
                if(service.hasPermission(user.getId(), PortletHandler.PERMISSION_THEME_ADD, null, null))
                    addAction(new TableAction("add", Application.getInstance().getMessage("portlet.label.add","Add")));
                if(service.hasPermission(user.getId(), PortletHandler.PERMISSION_THEME_EDIT, null, null))
                    columnName.setUrlParam("themeId");
                if(service.hasPermission(user.getId(), PortletHandler.PERMISSION_THEME_DELETE, null, null))
                    addAction(new TableAction("delete", Application.getInstance().getMessage("portlet.label.delete","Delete"), Application.getInstance().getMessage("portlet.label.deleteThemesPrompt","Are You Sure You Want To Delete These Theme(s) ?")));
            }
            catch (SecurityException e)
            {
                Log.getLog(getClass()).error(e);
            }
        }

        public Collection getTableRows()
        {
            Collection list = null;
            try
            {
                PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
                list = handler.getThemes(new DaoQuery(), getStart(), getRows(), getSort(), isDesc(), false);
            }
            catch(Exception e)
            {
                Log.getLog(getClass()).error(e);
            }
            return list;
        }

        public int getTotalRowCount()
        {
            int count = 0;
            try
            {
                PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
                count = handler.getThemesCount(new DaoQuery());
            }
            catch(Exception e)
            {
                Log.getLog(getClass()).error(e);
            }
            return count;
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys)
        {
            Forward forward = null;
            if("add".equals(action))
                forward = new Forward(ThemeTable.FORWARD_ADD);
            else if("delete".equals(action))
            {
                PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
                try
                {
                    for(int i = 0; i < selectedKeys.length; i++)
                        handler.deleteTheme(selectedKeys[i]);
                }
                catch(Exception e)
                {
                    Log.getLog(getClass()).error(e);
                }
                forward = new Forward(ThemeTable.FORWARD_DELETE);
            }
            return forward;
        }

        public String getTableRowKey()
        {
            return "themeId";
        }
    }
}
