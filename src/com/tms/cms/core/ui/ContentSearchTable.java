package com.tms.cms.core.ui;

import com.tms.cms.core.model.ContentManager;
import com.tms.util.FormatUtil;
import kacang.Application;
import kacang.services.indexing.QueryException;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Mar 24, 2003
 * Time: 6:40:53 PM
 * To change this template use Options | File Templates.
 */
public class ContentSearchTable extends Table {

    private String query;
    private boolean hideFilters;

    public ContentSearchTable() {
    }

    public ContentSearchTable(String name) {
        super(name);
    }

    public void init() {
        setModel(new RelatedContentTableModel());
        setSortable(false);
    }

    public Forward onSelection(Event evt) {
        String id = evt.getRequest().getParameter("id");
        ContentHelper.setId(evt, id);
        Forward forward = super.onSelection(evt);
        forward = new Forward("selection");
        return forward;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public boolean isHideFilters() {
        return hideFilters;
    }

    public void setHideFilters(boolean hideFilters) {
        this.hideFilters = hideFilters;
    }

    public class RelatedContentTableModel extends TableModel {

        public RelatedContentTableModel() {

            // add columns
            Application application = Application.getInstance();
            TableColumn nameColumn = new TableColumn("name", application.getMessage("general.label.name", "Name"));
            nameColumn.setUrlParam("id");
            addColumn(nameColumn);

            TableColumn classColumn = new TableColumn("className", application.getMessage("general.label.type", "Type"));
            classColumn.setFormat(new TableResourceFormat("cms.label.iconLabel_", null));
            addColumn(classColumn);

            TableColumn dateColumn = new TableColumn("date", application.getMessage("general.label.date", "Date"));
            dateColumn.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateFormat()));
            addColumn(dateColumn);

            addColumn(new TableColumn("contents", application.getMessage("general.label.score", "Score")));

            if (!hideFilters) {
                addFilter(new TableFilter("query", application.getMessage("general.label.query", "Query")));

                TableFilter sortFilter = new TableFilter("sort", application.getMessage("general.label.sort", "Sort"));
                SelectBox sortBox = new SelectBox("sortBox");
                sortBox.setOptions("score="+application.getMessage("general.label.closestMatch", "Closest Match")+";date="+application.getMessage("general.label.mostRecent", "Most Recent")+"");
                sortFilter.setWidget(sortBox);
                addFilter(sortFilter);
            }
        }

        public String getTableRowKey() {
            return "id";
        }

        public Collection getTableRows() {
            try {
                // query
                String queryStr = null;
                String sortStr = null;
                if (!hideFilters) {
                    queryStr = (String)getFilterValue("query");
                    List sortList = (List)getFilterValue("sort");
                    if (sortList != null && sortList.size() > 0) {
                        sortStr = (String)sortList.get(0);
                    }
                }
                else {
                    queryStr = getQuery();
                    sortStr = getTable().getSort();
                }
                if (queryStr != null && queryStr.trim().length() > 0) {
                    Application application = Application.getInstance();
                    ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
                    return cm.search(queryStr, sortStr, getStart(), getRows(), getWidgetManager().getUser());
                }
                else {
                    return new ArrayList();
                }
            }
            catch(Exception e) {
                if (e.toString().indexOf(QueryException.class.getName()) >= 0) {
                    Log.getLog(getClass()).error("Indexing query error: " + e.toString());
                    return new ArrayList();
                }
                throw new RuntimeException(e.toString());
            }
        }

        public int getTotalRowCount() {
            try {
                // query
                String queryStr = (!isHideFilters()) ? (String)getFilterValue("query") : getQuery();
                if (queryStr != null && queryStr.trim().length() > 0) {
                    Application application = Application.getInstance();
                    ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
                    return cm.searchCount(queryStr, getWidgetManager().getUser());
                }
                else {
                    return 0;
                }
            }
            catch(Exception e) {
                if (e.toString().indexOf(QueryException.class.getName()) >= 0) {
                    return 0;
                }
                throw new RuntimeException(e.toString());
            }
        }


    }

}
