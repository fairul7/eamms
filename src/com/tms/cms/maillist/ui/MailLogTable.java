package com.tms.cms.maillist.ui;

import com.tms.cms.maillist.model.MailListException;
import com.tms.cms.maillist.model.MailListModule;
import kacang.Application;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.Collection;
import java.util.HashSet;

public class MailLogTable extends Table {

    public MailLogTable() {
        super();
        setWidth("100%");
    }

    public MailLogTable(String name) {
        this();
        setName(name);
    }

    public void init() {
        super.init();
        setModel(new MaiLogTableModel());
    }

    class MaiLogTableModel extends TableModel {
        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            try {
                MailListModule module = (MailListModule) Application.getInstance().getModule(MailListModule.class);
                for (int i = 0; i < selectedKeys.length; i++) {
                    module.deleteMailLog(selectedKeys[i]);
                }
            } catch(MailListException e) {
                Log log = Log.getLog(this.getClass());
                log.error("Error deleting MailLog", e);
            }

            return super.processAction(evt, action, selectedKeys);
        }

        public MaiLogTableModel() {
	        Application application = Application.getInstance();
            TableColumn tcMessage = new TableColumn("message", application.getMessage("maillist.label.message", "Message"));
            tcMessage.setUrlParam("id");
            addColumn(tcMessage);

            addColumn(new TableColumn("startDate", application.getMessage("general.label.startDate", "Start Date")));
            addColumn(new TableColumn("endDate", application.getMessage("general.label.endDate", "End Date")));

            addAction(new TableAction("delete", application.getMessage("general.label.delete", "Delete"), application.getMessage("maillist.message.delete", "Delete selected items?")));
            addFilter(new TableFilter("search", null));
        }

        public String getTableRowKey() {
            return "id";
        }

        public Collection getTableRows() {
            try {
                String search = (String) getFilterValue("search");
                search = search == null ? "" : search;
                MailListModule module = (MailListModule) Application.getInstance().getModule(MailListModule.class);
                return module.getMailLogs(search, getSort(), isDesc(), getStart(), getRows());

            } catch(MailListException e) {
                // log error and return an empty collection
                Log log = Log.getLog(this.getClass());
                log.error("Error listing MailLog", e);
                return new HashSet();
            }
        }

        public int getTotalRowCount() {
            try {
                String search = (String) getFilterValue("search");
                search = search == null ? "" : search;
                MailListModule module = (MailListModule) Application.getInstance().getModule(MailListModule.class);
                return module.getMailLogCount(search);

            } catch(MailListException e) {
                // log error and return an empty collection
                Log log = Log.getLog(this.getClass());
                log.error("Error listing MailLog", e);
                return 0;
            }
        }
    }
}