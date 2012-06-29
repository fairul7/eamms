package com.tms.portlet.ui;

import kacang.stdui.*;
import kacang.util.Log;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.security.SecurityException;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorLike;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorParenthesis;
import kacang.ui.Forward;
import kacang.ui.Event;
import com.tms.portlet.PortletHandler;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Nov 11, 2003
 * Time: 4:29:46 PM
 * To change this template use Options | File Templates.
 */
public class PortletTable extends Table
{
    public static final String FORWARD_ADD = "add";
    public static final String FORWARD_DELETE = "delete";

    public PortletTable()
    {
    }

    public PortletTable(String name)
    {
        super(name);
    }

    public void init()
    {
        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
        User user = getWidgetManager().getUser();
        try
        {
            if(service.hasPermission(user.getId(), PortletHandler.PERMISSION_PORTLET_VIEW, null, null))
            {
                setModel(new PortletTableModel());
                setWidth("100%");
            }
        }
        catch(Exception e)
        {
            Log.getLog(getClass()).error(e);
        }
    }

    public class PortletTableModel extends TableModel
    {
        public PortletTableModel()
        {
            super();
            //Adding Columns
            TableColumn columnName = new TableColumn("portletName", Application.getInstance().getMessage("portlet.label.name","Name"));
            TableColumn columnTitle = new TableColumn("portletTitle", Application.getInstance().getMessage("portlet.label.title","Title"));
            TableColumn columnClass = new TableColumn("portletClass", Application.getInstance().getMessage("portlet.label.contextClass","Context/Class"));
            addColumn(columnName);
            addColumn(columnTitle);
            addColumn(columnClass);
            //Adding Actions
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            User user = getWidgetManager().getUser();
            try
            {
                if(service.hasPermission(user.getId(), PortletHandler.PERMISSION_PORTLET_ADD, null, null))
                    addAction(new TableAction("add", Application.getInstance().getMessage("portlet.label.add","Add")));
                if(service.hasPermission(user.getId(), PortletHandler.PERMISSION_PORTLET_EDIT, null, null))
                    columnName.setUrlParam("portletId");
                if(service.hasPermission(user.getId(), PortletHandler.PERMISSION_PORTLET_DELETE, null, null))
                    addAction(new TableAction("delete", Application.getInstance().getMessage("portlet.label.delete","Delete"), Application.getInstance().getMessage("portlet.label.deleteporletprompt","Are You Sure You Want To Delete These Portlet(s) ?")));
            }
            catch (SecurityException e)
            {
                Log.getLog(getClass()).error(e);
            }
            TableFilter filter = new TableFilter("name");
            addFilter(filter);
        }

        public Collection getTableRows()
        {
            Collection list = null;
            try
            {
                PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
                list = handler.getPortlets(generateDaoQuery(), getStart(), getRows(), getSort(), isDesc(), false);
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
                count = handler.getPortletsCount(generateDaoQuery());
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
                forward = new Forward(PortletTable.FORWARD_ADD);
            else if("delete".equals(action))
            {
                PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
                try
                {
                    for(int i = 0; i < selectedKeys.length; i++)
                        handler.deletePortlet(selectedKeys[i]);
                }
                catch(Exception e)
                {
                    Log.getLog(getClass()).error(e);
                }
                forward = new Forward(PortletTable.FORWARD_DELETE);
            }
            return forward;
        }

        public String getTableRowKey()
        {
            return "portletId";
        }

        private DaoQuery generateDaoQuery()
        {
            DaoQuery query = new DaoQuery();
            OperatorParenthesis parenthesis = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
            parenthesis.addOperator(new OperatorLike("portletName", getFilterValue("name"), null));
            parenthesis.addOperator(new OperatorLike("portletTitle", getFilterValue("name"), DaoOperator.OPERATOR_OR));
            parenthesis.addOperator(new OperatorLike("portletClass", getFilterValue("name"), DaoOperator.OPERATOR_OR));
            query.addProperty(parenthesis);
            return query;
        }
    }
}
