package com.tms.cms.maillist.ui;

import com.tms.cms.maillist.model.MailList;
import com.tms.cms.maillist.model.MailListException;
import com.tms.cms.maillist.model.MailListModule;
import kacang.Application;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.Collection;
import java.util.HashSet;

public class ScheduledMailListTable extends Table {

    public ScheduledMailListTable() {
        super();
        setWidth("100%");
    }

    public ScheduledMailListTable(String name) {
        this();
        setName(name);
    }

    public void init() {
        super.init();
        setModel(new ScheduledMailListTableModel());
    }

    class ScheduledMailListTableModel extends TableModel {
        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            Log log = Log.getLog(this.getClass());
            if("delete".equals(action)) {
                try {
                    MailListModule module = (MailListModule) Application.getInstance().getModule(MailListModule.class);
                    for(int i = 0; i < selectedKeys.length; i++) {
                        module.deleteMailList(selectedKeys[i]);
                    }
                } catch(MailListException e) {
                    log.error("Error deleting MailList", e);
                }

            } else if("new".equals(action)) {
                evt.setType("newScheduledMailList");
            }

            return super.processAction(evt, action, selectedKeys);
        }

        public ScheduledMailListTableModel() {
	        Application application = Application.getInstance();
            TableColumn tcName = new TableColumn("name", application.getMessage("general.label.name", "Name"));
            tcName.setUrlParam("id");
            addColumn(tcName);

            addColumn(new TableColumn("html", application.getMessage("maillist.label.html", "HTML")));

            addAction(new TableAction("new", application.getMessage("maillist.label.newScheduledMailingList", "New Scheduled Mailing List")));
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
                return module.getMailLists(MailList.MAIL_LIST_TYPE_SCHEDULED, search, getSort(), isDesc(), getStart(), getRows());

            } catch(MailListException e) {
                // log error and return an empty collection
                Log log = Log.getLog(this.getClass());
                log.error("Error listing MailList", e);
                return new HashSet();
            }
        }

        public int getTotalRowCount() {
            try {
                String search = (String) getFilterValue("search");
                search = search == null ? "" : search;
                MailListModule module = (MailListModule) Application.getInstance().getModule(MailListModule.class);
                return module.getMailListCount(MailList.MAIL_LIST_TYPE_SCHEDULED, search);

            } catch(MailListException e) {
                // log error and return an empty collection
                Log log = Log.getLog(this.getClass());
                log.error("Error listing MailList", e);
                return 0;
            }
        }
    }
}