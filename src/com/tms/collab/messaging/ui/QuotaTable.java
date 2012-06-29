package com.tms.collab.messaging.ui;

import kacang.stdui.*;
import kacang.Application;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;

import java.util.Collection;
import java.util.ArrayList;
import java.text.DecimalFormat;

import com.tms.collab.messaging.model.MessagingModule;

public class QuotaTable extends Table {

    public QuotaTable() {
        super();
    }

    public QuotaTable(String name) {
        super(name);
    }

    public void init() {
        setModel(new QuotaTableModel());
    }

    class QuotaTableModel extends TableModel {

        public QuotaTableModel() {
            Application app = Application.getInstance();

            TableColumn nameCol = new TableColumn("groupName", app.getMessage("security.label.group"));
            addColumn(nameCol);

            TableColumn quotaCol = new TableColumn("quota", app.getMessage("messaging.label.quota"));
            quotaCol.setFormat(new TableFormat() {
                public String format(Object value) {
                    String result;
                    long quota;
                    try {
                        quota = Long.parseLong(value.toString());
                    }
                    catch (Exception e) {
                        return value.toString();
                    }
                    if (quota >= 1024) {
                        double db = (((double)quota)/1024);
                        result = new DecimalFormat("0.00").format(db) + "MB";
                    }
                    else {
                        result = quota + "KB";
                    }
                    return result;
                }
            });
            addColumn(quotaCol);

            addAction(new TableAction("delete", app.getMessage("general.label.delete", "Delete"), app.getMessage("messaging.label.confirmDelete", "Confirm Deletion?")));
        }

        public String getTableRowKey() {
            return "id";
        }

        public Collection getTableRows() {
            try {
                MessagingModule mm = (MessagingModule)Application.getInstance().getModule(MessagingModule.class);
                Collection list = mm.getPrincipalQuotaList(null, getSort(), isDesc(), getStart(), getRows());
                return list;
            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Error retrieving principal quota list", e);
                return new ArrayList();
            }
        }

        public int getTotalRowCount() {
            try {
                MessagingModule mm = (MessagingModule)Application.getInstance().getModule(MessagingModule.class);
                int count = mm.getPrincipalQuotaCount(null);
                return count;
            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Error retrieving principal quota list: " + e.toString());
                return 0;
            }
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            if ("delete".equals(action)) {
                try {
                    MessagingModule mm = (MessagingModule)Application.getInstance().getModule(MessagingModule.class);
                    mm.deletePrincipalQuota(selectedKeys);
                }
                catch (Exception e) {
                    Log.getLog(getClass()).error("Error deleting principal quota", e);
                    return new Forward("error");
                }
                return new Forward("delete");
            }
            else {
                return null;
            }
        }
    }

}
