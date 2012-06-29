package com.tms.collab.project.ui;

import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.services.security.User;
import kacang.services.security.SecurityService;
import kacang.services.security.SecurityException;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorParenthesis;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorLike;
import kacang.model.operator.OperatorEquals;
import kacang.util.Log;

import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;

import com.tms.collab.project.WormsHandler;
import com.tms.collab.project.WormsException;
import org.apache.commons.collections.SequencedHashMap;

public class TemplateTable extends Table
{
    public static final String FORWARD_ADD = "forward.worms.template.Add";
    public static final String FORWARD_DELETE = "forward.worms.template.Delete";

    private TemplateModel model;

    public void init()
    {
        User user = getWidgetManager().getUser();
        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
        try
        {
            if(service.hasPermission(user.getId(), WormsHandler.PERMISSION_PROJECTS_VIEW, WormsHandler.class.getName(), null))
            {
                super.init();
                model = new TemplateModel();
                setModel(model);
            }
        }
        catch (SecurityException e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
    }

    public void onRequest(Event event)
    {
        super.onRequest(event);
        model.refresh();
    }

    public class TemplateModel extends TableModel
    {
        public TemplateModel()
        {
            /* Adding Columns */
            setWidth("100%");
            TableColumn colName = new TableColumn("templateName", Application.getInstance().getResourceBundle().getString("project.label.name"));
            addColumn(colName);
            addColumn(new TableColumn("templateCategory", Application.getInstance().getResourceBundle().getString("project.label.category")));
            /* Adding Actions */
            User user = getWidgetManager().getUser();
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            try
            {
                if(service.hasPermission(user.getId(), WormsHandler.PERMISSION_PROJECTS_ADD, WormsHandler.class.getName(), null))
                    addAction(new TableAction("add", Application.getInstance().getResourceBundle().getString("project.label.add")));
                if(service.hasPermission(user.getId(), WormsHandler.PERMISSION_PROJECTS_DELETE, WormsHandler.class.getName(), null))
                    addAction(new TableAction("delete", Application.getInstance().getResourceBundle().getString("project.label.delete")));
                if(service.hasPermission(user.getId(), WormsHandler.PERMISSION_PROJECTS_EDIT, WormsHandler.class.getName(), null))
                    colName.setUrlParam("templateId");
            }
            catch (SecurityException e)
            {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
            /* Adding Filters */
            TableFilter categoryFilter = new TableFilter("category");
            SelectBox categories = new SelectBox("categories");
            categories.setSelectedOptions(new String[] {"-1"});
            categoryFilter.setWidget(categories);
            addFilter(categoryFilter);
            addFilter(new TableFilter("search"));
        }

        public Collection getTableRows()
        {
            Collection list = new ArrayList();
            try
            {
                WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
                DaoQuery query = generateQuery();
                list = worms.getTemplates(query, getStart(), getRows(), getSort(), isDesc());
            }
            catch (WormsException e)
            {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
            return list;
        }

        public int getTotalRowCount()
        {
            int count = 0;
            try
            {
                WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
                DaoQuery query = generateQuery();
                count = worms.getTemplateCount(query);
            }
            catch (WormsException e)
            {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
            return count;
        }

        private DaoQuery generateQuery()
        {
            DaoQuery query = new DaoQuery();
            if(!("".equals(getFilterValue("search"))))
            {
                OperatorParenthesis parenthesis = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
                parenthesis.addOperator(new OperatorLike("templateName", getFilterValue("search"), null));
                parenthesis.addOperator(new OperatorLike("templateDescription", getFilterValue("search"), DaoOperator.OPERATOR_AND));
                query.addProperty(parenthesis);
            }
            String category = (String) ((SelectBox)getFilter("category").getWidget()).getSelectedOptions().keySet().iterator().next();
            if(!("-1".equals(category)))
                query.addProperty(new OperatorEquals("templateCategory", category, DaoOperator.OPERATOR_AND));
            return query;
        }

        protected void refresh()
        {
            try
            {
                TableFilter filter = getFilter("category");
                WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
                Collection list = worms.getTemplateCategories();
                SequencedHashMap map = new SequencedHashMap();
                map.put("-1", Application.getInstance().getResourceBundle().getString("project.label.allCategories"));
                for (Iterator i = list.iterator(); i.hasNext();)
                {
                    String category = (String) i.next();
                    map.put(category, category);
                }
                ((SelectBox)filter.getWidget()).setOptionMap(map);
            }
            catch (WormsException e)
            {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
        }

        public Forward processAction(Event event, String action, String[] selectedKeys)
        {
            Forward forward = super.processAction(event, action, selectedKeys);
            if("add".equals(action))
                forward = new Forward(FORWARD_ADD);
            else if("delete".equals(action))
            {
                WormsHandler handler = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
                try
                {
                    for(int i = 0; i < selectedKeys.length; i++)
                        handler.deleteTemplate(selectedKeys[i]);
                }
                catch(Exception e)
                {
                    Log.getLog(getClass()).error(e);
                }
                forward = new Forward(FORWARD_DELETE);
            }
            return forward;
        }

        public String getTableRowKey()
        {
            return "templateId";
        }
    }
}
