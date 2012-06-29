package com.tms.cms.core.ui;

import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import com.tms.util.FormatUtil;
import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Mar 24, 2003
 * Time: 6:40:53 PM
 * To change this template use Options | File Templates.
 */
public class ContentReportTable extends Table {

    private String id;

    public ContentReportTable() {
    }

    public ContentReportTable(String name) {
        super(name);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void init() {
        setNumbering(false);
        setModel(new ContentReportTableModel());
    }

    public void onRequest(Event evt) {
        ContentObject co = ContentHelper.getContentObject(evt, getId());
        if (co != null) {
            setId(co.getId());
        }
    }


    public class ContentReportTableModel extends TableModel {

        public ContentReportTableModel() {

            // add columns
            Application application = Application.getInstance();
            TableColumn dateColumn = new TableColumn("date", application.getMessage("general.label.date", "Date"));
            dateColumn.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateTimeFormat()));
            addColumn(dateColumn);

            addColumn(new TableColumn("username", application.getMessage("general.label.username", "User Name")));
            addColumn(new TableColumn("host", application.getMessage("general.label.host", "Host")));
            addColumn(new TableColumn("event", application.getMessage("general.label.event", "Event")));
            addColumn(new TableColumn("param", application.getMessage("general.label.version", "Version")));

            // add actions
            addAction(new TableAction("clear", application.getMessage("general.label.clear", "Clear"), Application.getInstance().getMessage("cms.message.clearStatistics", "Clear Statistics?")));

        }

        public String getTableRowKey() {
            return null;
        }

        public Collection getTableRows() {
            try {
                // query
                Application application = Application.getInstance();
                ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
                User user = getWidgetManager().getUser();
                return cm.viewReport(getId(), null, null, getSort(), isDesc(), getStart(), getRows(), user);
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
                User user = getWidgetManager().getUser();
                return cm.viewReportCount(getId(), null, null, user);
            }
            catch(Exception e) {
                throw new RuntimeException(e.toString());
            }
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
//            System.out.println("Action: " + action + "; Keys: " + Arrays.asList(selectedKeys));

            if ("clear".equals(action) && getId() != null) {
                try {
                    Application application = Application.getInstance();
                    ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
                    User user = getWidgetManager().getUser();
                    cm.clearReport(getId(), null, null, user);
                    return new Forward("clear");
                }
                catch (ContentException e) {
                    Log.getLog(getClass()).error(e.toString(), e);
                    return null;
                }
            }
            else {
                return null;
            }
        }
    }


}
