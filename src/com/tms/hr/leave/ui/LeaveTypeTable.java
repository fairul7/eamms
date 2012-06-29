package com.tms.hr.leave.ui;

import kacang.stdui.*;
import kacang.Application;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.ui.WidgetManager;
import kacang.util.Log;

import java.util.Collection;
import java.util.ArrayList;

import com.tms.hr.leave.model.LeaveModule;

public class LeaveTypeTable extends Table {

    public LeaveTypeTable() {
        super();
    }

    public LeaveTypeTable(String name) {
        super(name);
    }

    public void init() {
        super.init();
        setCurrentPage(1);
        setSortable(false);
        setPageSize(-1);
        setModel(new LeaveTypeTableModel());
    }

    public class LeaveTypeTableModel extends TableModel {

        private int rowCount = 0;

        public LeaveTypeTableModel() {
            Application application = Application.getInstance();
            addAction(new TableAction("add", application.getMessage("leave.label.add","Add")));
            addAction(new TableAction("delete", application.getMessage("leave.label.delete","Delete")));
            TableColumn tcLeaveCode = new TableColumn("id", application.getMessage("leave.label.leaveCode","Leave Code"));
            tcLeaveCode.setUrlParam("id");
            addColumn(tcLeaveCode);
            TableColumn tcLeaveType = new TableColumn("leaveType", application.getMessage("leave.label.leaveType","Leave Type"));
            addColumn(tcLeaveType);
            TableColumn tcLeaveDesc = new TableColumn("name", application.getMessage("leave.label.leaveTypeName","Leave Type"));
            addColumn(tcLeaveDesc);
            TableColumn tcGender = new TableColumn("gender", application.getMessage("employee.label.gender","Gender"));
            addColumn(tcGender);
            String contextPath = (String)getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
            TableColumn tcCredit = new TableColumn("creditAllowed", application.getMessage("leave.label.credit", "Credit"));
            tcCredit.setFormat(new TableBooleanFormat("<img src=\"" + contextPath + "/common/table/booleantrue.gif\">", ""));
            addColumn(tcCredit);
        }

        public String getTableRowKey() {
            return "id";
        }

        public Collection getTableRows() {
            Collection rows = new ArrayList();
            Application app = Application.getInstance();
            LeaveModule lm = (LeaveModule)app.getModule(LeaveModule.class);
            try {
                rows = lm.viewLeaveTypeList(null, false, true);
            }
            catch (Exception e) {
                Log.getLog(this.getClass()).error("Error retrieving leave type list", e);
            }
            rowCount = rows.size();
            return rows;
        }

        public int getTotalRowCount() {
            return rowCount;
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            if ("delete".equals(action)) {
                Application app = Application.getInstance();
                try {
                    LeaveModule lm = (LeaveModule)app.getModule(LeaveModule.class);
                    for (int i=0; i < selectedKeys.length; i++) {
                        lm.deleteLeaveType(selectedKeys[i]);
                    }
                }
                catch (Exception e) {
                    Log.getLog(this.getClass()).error("Error processing action " + e.toString());
                }
            }
            else if ("add".equals(action)) {
                return new Forward("add");
            }

            return super.processAction(evt, action, selectedKeys);
        }

    }
}
