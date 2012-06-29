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

public class ComposedMailListTable extends Table {

    public ComposedMailListTable() {
        super();
        setWidth("100%");
    }

    public ComposedMailListTable(String name) {
        this();
        setName(name);
    }

    public void init() {
        super.init();
        setModel(new ComposedMailListTableModel());
    }

    class ComposedMailListTableModel extends TableModel {
        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            if("delete".equals(action)) {
                try {
                    MailListModule module = (MailListModule) Application.getInstance().getModule(MailListModule.class);
                    for(int i = 0; i < selectedKeys.length; i++) {
                        module.deleteMailList(selectedKeys[i]);
                    }
                } catch(MailListException e) {
                    Log log = Log.getLog(this.getClass());
                    log.error("Error deleting MailList", e);
                }

            } else if("new".equals(action)) {
                evt.setType("newComposedMailList");
            }

            return super.processAction(evt, action, selectedKeys);
        }

        public ComposedMailListTableModel() {
	        Application application = Application.getInstance();
            TableColumn tcName = new TableColumn("name", application.getMessage("general.label.name", "Name"));
            tcName.setUrlParam("id");
            addColumn(tcName);

            addColumn(new TableColumn("html", application.getMessage("maillist.label.html", "HTML")));

            addAction(new TableAction("new", application.getMessage("maillist.label.newComposedMailingList", "New Composed Mailing List")));
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
                return module.getMailLists(MailList.MAIL_LIST_TYPE_COMPOSED, search, getSort(), isDesc(), getStart(), getRows());

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
                return module.getMailListCount(MailList.MAIL_LIST_TYPE_COMPOSED, search);

            } catch(MailListException e) {
                // log error and return an empty collection
                Log log = Log.getLog(this.getClass());
                log.error("Error listing MailList", e);
                return 0;
            }
        }
    }
}