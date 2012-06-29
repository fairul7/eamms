package com.tms.hr.competency.ui;

import com.tms.hr.competency.CompetencyException;
import com.tms.hr.competency.CompetencyHandler;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.OperatorLike;
import kacang.model.operator.OperatorParenthesis;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class CompetencyTable extends Table
{
    public static final String FORWARD_ADD = "add";
    public static final String FORWARD_DELETE = "delete";

    protected CompetencyTableModel model;

    public CompetencyTable()
    {
    }

    public CompetencyTable(String s)
    {
        super(s);
    }

    public void init()
    {
        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
        User user = getWidgetManager().getUser();
        try
        {
            if(service.hasPermission(user.getId(), CompetencyHandler.PERMISSION_COMPETENCY_VIEW, null, null))
            {
                model = new CompetencyTableModel();
                setModel(model);
                setWidth("100%");
            }
        }
        catch(Exception e)
        {
            Log.getLog(getClass()).error(e);
        }
    }

    public void onRequest(Event event)
    {
        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
        User user = getWidgetManager().getUser();
        try
        {
            if(service.hasPermission(user.getId(), CompetencyHandler.PERMISSION_COMPETENCY_VIEW, null, null))
            {
                model.populateTypes();
            }
        }
        catch(Exception e)
        {
            Log.getLog(getClass()).error(e);
        }
    }

    public class CompetencyTableModel extends TableModel
    {
        public CompetencyTableModel()
        {
            super();
            //Adding columns
            TableColumn columnName = new TableColumn("competencyName", Application.getInstance().getMessage("project.label.name","Name"));
            TableColumn columnType = new TableColumn("competencyType", Application.getInstance().getMessage("project.label.type","Type"));
            TableColumn columnDescription = new TableColumn("competencyDescription", Application.getInstance().getMessage("project.label.description","Description"));
            addColumn(columnName);
            addColumn(columnType);
            addColumn(columnDescription);
            //Adding actions
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            User user = getWidgetManager().getUser();
            try
            {
                if(service.hasPermission(user.getId(), CompetencyHandler.PERMISSION_COMPETENCY_ADD, null, null))
                    addAction(new TableAction("add", Application.getInstance().getMessage("project.label.add","Add")));
                if(service.hasPermission(user.getId(), CompetencyHandler.PERMISSION_COMPETENCY_UPDATE, null, null))
                    columnName.setUrlParam("competencyId");
                if(service.hasPermission(user.getId(), CompetencyHandler.PERMISSION_COMPETENCY_DELETE, null, null))
                    addAction(new TableAction("delete", Application.getInstance().getMessage("project.label.delete","Delete"), Application.getInstance().getMessage("project.label.deletecompetency","Are You Sure You Want To Delete These Competency(s) ?")));
            }
            catch (SecurityException e)
            {
                Log.getLog(getClass()).error(e);
            }
            SelectBox types = new SelectBox("types");
            TableFilter filterName = new TableFilter("search");
            TableFilter filterType = new TableFilter("type");
            filterType.setWidget(types);
            addFilter(filterName);
            addFilter(filterType);
        }

        public Collection getTableRows()
        {
            Collection list = new ArrayList();
            try
            {
                CompetencyHandler handler = (CompetencyHandler) Application.getInstance().getModule(CompetencyHandler.class);
                list = handler.getCompetencies(generateQuery(), getStart(), getRows(), getSort(), isDesc());
            }
            catch(CompetencyException e)
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
                CompetencyHandler handler = (CompetencyHandler) Application.getInstance().getModule(CompetencyHandler.class);
                count = handler.getCompetenciesCount(generateQuery(), getStart(), getRows(), getSort(), isDesc());
            }
            catch(CompetencyException e)
            {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
            return count;
        }

        public Forward processAction(Event event, String action, String[] selectedKeys)
        {
            Forward forward = null;
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            User user = getWidgetManager().getUser();

            try
                {
                    if("add".equals(action))
                    {
                        if(service.hasPermission(user.getId(), CompetencyHandler.PERMISSION_COMPETENCY_ADD, null, null))
                            forward = new Forward(CompetencyTable.FORWARD_ADD);

                    }
                    else if("delete".equals(action))
                    {
                        if(service.hasPermission(user.getId(), CompetencyHandler.PERMISSION_COMPETENCY_DELETE, null, null))
                        {
                            CompetencyHandler handler = (CompetencyHandler) Application.getInstance().getModule(CompetencyHandler.class);
                            try
                            {
                                for(int i = 0; i < selectedKeys.length; i++)
                                    handler.deleteCompetency(selectedKeys[i]);
                            }
                            catch(Exception e)
                            {
                                Log.getLog(getClass()).error(e);
                            }
                            forward = new Forward(CompetencyTable.FORWARD_DELETE);
                        }
                    }
                }
                catch (SecurityException e)
                {
                    Log.getLog(getClass()).error(e.getMessage(), e);
                }
            return forward;
        }

        public String getTableRowKey()
        {
            return "competencyId";
        }

        protected DaoQuery generateQuery()
        {
            DaoQuery query = new DaoQuery();
            if(!(getFilterValue("search") == null || "".equals(getFilterValue("search"))))
            {
                OperatorParenthesis parenthesis = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
                parenthesis.addOperator(new OperatorLike("competencyName", getFilterValue("search"), null));
                parenthesis.addOperator(new OperatorLike("competencyType", getFilterValue("search"), DaoOperator.OPERATOR_OR));
                parenthesis.addOperator(new OperatorLike("competencyDescription", getFilterValue("search"), DaoOperator.OPERATOR_OR));
                query.addProperty(parenthesis);
            }
            SelectBox type = (SelectBox) getFilter("type").getWidget();
            if(type.getSelectedOptions().size() > 0)
            {
                String selected = (String) type.getSelectedOptions().keySet().iterator().next();
                if(!(selected == null || "-1".equals(selected)))
                    query.addProperty(new OperatorEquals("competencyType", selected, DaoOperator.OPERATOR_AND));
            }
            return query;
        }

        protected void populateTypes()
        {
            //Adding filters
            Map options = new SequencedHashMap();
            options.put("-1", Application.getInstance().getMessage("project.label.alltypes","All Types"));
            Collection list = new ArrayList();
            CompetencyHandler handler = (CompetencyHandler) Application.getInstance().getModule(CompetencyHandler.class);
            try
            {
                list = handler.getCompetencyTypes();
                for(Iterator i = list.iterator(); i.hasNext();)
                {
                    String type = (String) i.next();
                    options.put(type, type);
                }
            }
            catch(CompetencyException e)
            {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
            ((SelectBox)getFilter("type").getWidget()).setOptionMap(options);
        }
    }
}
