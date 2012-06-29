package com.tms.hr.competency.ui;

import com.tms.hr.competency.Competency;
import com.tms.hr.competency.CompetencyException;
import com.tms.hr.competency.CompetencyHandler;
import kacang.Application;
import kacang.ui.Event;
import kacang.model.DaoQuery;
import kacang.model.operator.*;
import kacang.stdui.*;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;

public class CompetencySelectBox extends PopupSelectBox
{
    public CompetencySelectBox()
    {
    }

    public CompetencySelectBox(String s)
    {
        super(s);
    }

    protected Table initPopupTable()
    {
        return new CompetencySelectTable("competencyTable");
    }

    protected Map generateOptionMap(String[] strings)
    {
        Map map = new SequencedHashMap();
        if (strings == null || strings.length == 0)
            return map;
        try
        {
            CompetencyHandler handler = (CompetencyHandler) Application.getInstance().getModule(CompetencyHandler.class);
            DaoQuery query = new DaoQuery();
            query.addProperty(new OperatorIn("competencyId", strings, DaoOperator.OPERATOR_AND));
            Collection list = handler.getCompetencies(query, 0, -1, "competencyName", false);
            for(Iterator i = list.iterator(); i.hasNext();)
            {
                Competency competency = (Competency) i.next();
                map.put(competency.getCompetencyId(), competency.getCompetencyName());
            }
        }
        catch(CompetencyException e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        return map;
    }

    public class CompetencySelectTable extends PopupSelectBoxTable
    {
        private CompetencySelectModel model;

        public CompetencySelectTable()
        {
            super();
        }

        public CompetencySelectTable(String s)
        {
            super(s);
        }

        public void init()
        {
            super.init();
            setWidth("100%");
            model = new CompetencySelectModel();
            setModel(model);
        }

        public void onRequest(Event event)
        {
            model.populateTypes();
        }

        public class CompetencySelectModel extends PopupSelectBoxTableModel
        {
            public CompetencySelectModel()
            {
                super();
                //Adding columns
                TableColumn columnName = new TableColumn("competencyName", Application.getInstance().getMessage("project.label.name","Name"));
                TableColumn columnType = new TableColumn("competencyType", Application.getInstance().getMessage("project.label.type","Type"));
                TableColumn columnDescription = new TableColumn("competencyDescription", Application.getInstance().getMessage("project.label.description","Description"));
                addColumn(columnName);
                addColumn(columnType);
                addColumn(columnDescription);
                //Adding filters
                SelectBox types = new SelectBox("types");
                TableFilter filterName = new TableFilter("search");
                TableFilter filterType = new TableFilter("type");
                filterType.setWidget(types);
                addFilter(filterName);
                addFilter(filterType);
                //Adding Actions
                addAction(new TableAction(FORWARD_SELECT,  Application.getInstance().getMessage("general.label.select", "Select")));
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

            protected void populateTypes()
            {
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

            protected DaoQuery generateQuery()
            {
                DaoQuery query = new DaoQuery();
                if(!(getFilterValue("search") == null || "".equals(getFilterValue("search"))))
                {
                    OperatorParenthesis parenthesis = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
                    parenthesis.addOperator(new OperatorLike("competencyName", getFilterValue("search"), null));
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

            public String getTableRowKey()
            {
                return "competencyId";
            }
        }
    }
}
