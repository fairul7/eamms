package com.tms.cms.core.ui;

import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import com.tms.util.FormatUtil;
import kacang.Application;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Mar 24, 2003
 * Time: 6:40:53 PM
 * To change this template use Options | File Templates.
 */
public class RelatedContentTable extends Table {

    private String id;

    public RelatedContentTable() {
    }

    public RelatedContentTable(String name) {
        super(name);
    }

    public void init() {
        setModel(new RelatedContentTableModel());
    }

    public void onRequest(Event evt) {
        ContentObject co = ContentHelper.getContentObject(evt, getId());
        if (co != null) {
            setId(co.getId());
        }
    }

    public Forward onSelection(Event evt) {
        String id = evt.getRequest().getParameter("id");
        ContentHelper.setId(evt, id);
        Forward forward = super.onSelection(evt);
        forward = new Forward("selection");
        return forward;
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

            String contextPath = (String)getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
            TableFormat booleanFormat = new TableBooleanFormat("<img src=\"" + contextPath + "/common/table/booleantrue.gif\">", "");

            TableColumn publishedColumn = new TableColumn("published", application.getMessage("general.label.published", "Published"));
            publishedColumn.setFormat(booleanFormat);
            addColumn(publishedColumn);

            TableColumn archivedColumn = new TableColumn("archived", application.getMessage("general.label.archived", "Archived"));
            archivedColumn.setFormat(booleanFormat);
            addColumn(archivedColumn);

        }

        public String getTableRowKey() {
            return "id";
        }

        public Collection getTableRows() {
            try {
                // query
                Application application = Application.getInstance();
                ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
                Collection children = cm.viewRelated(getId(), null, getSort(), isDesc(), getStart(), getRows(), getWidgetManager().getUser());
                return children;
            }
            catch(Exception e) {
                throw new RuntimeException(e.toString());
            }
        }

        public int getTotalRowCount() {
            try {
                // query
                Application application = Application.getInstance();
                ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
                return cm.viewRelatedCount(getId(), null, getWidgetManager().getUser());
            }
            catch(Exception e) {
                throw new RuntimeException(e.toString());
            }
        }


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
