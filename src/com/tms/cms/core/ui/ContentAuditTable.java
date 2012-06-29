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
public class ContentAuditTable extends Table {

    private String id;

    public ContentAuditTable() {
    }

    public ContentAuditTable(String name) {
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
        setModel(new ContentAuditTableModel());
    }

    public void onRequest(Event evt) {
        ContentObject co = ContentHelper.getContentObject(evt, getId());
        if (co != null) {
            setId(co.getId());
        }
    }


    public class ContentAuditTableModel extends TableModel {

        public ContentAuditTableModel() {

            // add columns
            Application application = Application.getInstance();
            TableColumn dateColumn = new TableColumn("date", application.getMessage("general.label.date", "Date"));
            dateColumn.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateTimeFormat()));
            addColumn(dateColumn);

            addColumn(new TableColumn("username", application.getMessage("general.label.username", "User Name")));
            addColumn(new TableColumn("host", application.getMessage("general.label.host", "Host")));

            TableColumn eventColumn = new TableColumn("event", application.getMessage("general.label.event", "Event"));
            eventColumn.setFormat(new TableResourceFormat("cms.label.", null));
            addColumn(eventColumn);

            addColumn(new TableColumn("param", application.getMessage("general.label.parameters", "Parameters")));

            // add actions
            addAction(new TableAction("clear", application.getMessage("general.label.clear", "Clear"), Application.getInstance().getMessage("cms.message.clearAudit", "Clear Audit Trail?")));

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
                return cm.viewAudit(getId(), null, null, getSort(), isDesc(), getStart(), getRows(), user);
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
                return cm.viewAuditCount(getId(), null, null, user);
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
                    cm.clearAudit(getId(), null, null, user);
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
