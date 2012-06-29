package com.tms.collab.project.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.OperatorLike;
import kacang.model.operator.OperatorParenthesis;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDecimalFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.collab.project.WormsException;
import com.tms.collab.project.WormsHandler;


public class ProjectReportTable extends Table
{

    ProjectReportTableModel model;

    public void init()
    {
        User user = getWidgetManager().getUser();
        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
        try
        {
            if(service.hasPermission(user.getId(), WormsHandler.PERMISSION_PROJECTS_VIEW, WormsHandler.class.getName(), null))
            {
                super.init();
                setWidth("100%");
                model = new ProjectReportTableModel();
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

    public class ProjectReportTableModel extends TableModel
    {
        public ProjectReportTableModel()
        {
            /* Table Columns */
            TableColumn colName = new TableColumn("projectName", Application.getInstance().getResourceBundle().getString("project.label.name"));
            colName.setUrlParam("projectId");
            TableColumn colType = new TableColumn("projectCurrencyType", Application.getInstance().getResourceBundle().getString("project.label.projectCurrencyType"));
            TableColumn colValue = new TableColumn("projectValue", Application.getInstance().getResourceBundle().getString("project.label.projectValue"));
            TableColumn colCategory = new TableColumn("projectCategory", Application.getInstance().getResourceBundle().getString("project.label.category"));
            colValue.setFormat(new TableDecimalFormat("##,###,##0.00"));
            TableColumn colManaged = new TableColumn("ownerName", Application.getInstance().getResourceBundle().getString("project.label.projectOwner"));
            addColumn(colName);
            addColumn(colCategory);
            addColumn(colType);
            addColumn(colValue);
            addColumn(colManaged);
           
            /* Table Filters */
            TableFilter filterCategory = new TableFilter("category");
            SelectBox selectCategory = new SelectBox("selectCategory");
            selectCategory.setSelectedOptions(new String[] {"-1"});
            filterCategory.setWidget(selectCategory);
			TableFilter filterArchive = new TableFilter("archived");
			SelectBox selectArchived = new SelectBox("selectArchived");
            selectArchived.addOption("0", Application.getInstance().getMessage("project.label.activeProjects","Active Projects"));
            selectArchived.addOption("1", Application.getInstance().getMessage("project.label.archivedProjects","Archived Projects"));
			selectArchived.addOption("-1", Application.getInstance().getMessage("project.label.filterArchived","View All"));
			selectArchived.setSelectedOption("0");
			filterArchive.setWidget(selectArchived);
            addFilter(new TableFilter("search"));
            addFilter(filterCategory);
			addFilter(filterArchive);
        }

        public Collection getTableRows()
        {
            WormsHandler worm = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
            Collection list = new ArrayList();
            DaoQuery query = generateQuery();
            try
            {
				String sort = getSort();
				if("ownerName".equals(sort))
					sort = "ownerId";
                if(sort==null || "".equals(sort)){
                    sort = "modifiedDate";
                }
				list = worm.getProjects(query, getStart(), getRows(), sort, isDesc());
            }
            catch (WormsException e)
            {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
            return list;
        }

        public int getTotalRowCount()
        {
            WormsHandler worm = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
            int count = 0;
            DaoQuery query = generateQuery();
            try
            {
                count = worm.getProjectsCount(query);
            }
            catch (WormsException e)
            {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
            return count;
        }

        public String getTableRowKey()
        {
            return "projectId";
        }

        private DaoQuery generateQuery()
        {
            DaoQuery query = new DaoQuery();
            if(!("".equals(getFilterValue("search"))))
            {
                OperatorParenthesis parenthesis = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
                parenthesis.addOperator(new OperatorLike("projectName", getFilterValue("search"), null));
                parenthesis.addOperator(new OperatorLike("projectDescription", getFilterValue("search"), DaoOperator.OPERATOR_OR));
                query.addProperty(parenthesis);
            }
            String category = (String) ((SelectBox)getFilter("category").getWidget()).getSelectedOptions().keySet().iterator().next();
            if(!("-1".equals(category)))
                query.addProperty(new OperatorEquals("projectCategory", category, DaoOperator.OPERATOR_AND));
			String archived = (String) ((SelectBox)getFilter("archived").getWidget()).getSelectedOptions().keySet().iterator().next();
			if(!("-1".equals(archived)))
				query.addProperty(new OperatorEquals("archived", archived, DaoOperator.OPERATOR_AND));
            return query;
        }

        public Forward processAction(Event event, String action, String[] selectedKeys)
        {
            Forward forward = null;
           
            return forward;
        }

        protected void refresh()
        {
            try
            {
                TableFilter filter = getFilter("category");
                WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
                Collection list = worms.getProjectCategories();
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
    }
}
