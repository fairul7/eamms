package com.tms.hr.leave.ui;

import kacang.stdui.Table;
import kacang.stdui.TableModel;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.Application;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;

import java.util.Collection;
import java.util.ArrayList;

import com.tms.hr.employee.model.EmployeeModule;

public class DepartmentSetupTable extends Table {
    public DepartmentSetupTable(String name) {
        super(name);
    }

    public void init() {
        super.init();
        setCurrentPage(1);
        setPageSize(15);
        setWidth("100%");
        setModel(new DepartmentSetupTableModel());
    }

    public class DepartmentSetupTableModel extends TableModel {
        public DepartmentSetupTableModel() {
            addAction(new TableAction("add", Application.getInstance().getMessage("leave.label.add","Add")));
            addAction(new TableAction("delete", Application.getInstance().getMessage("leave.label.delete","Delete")));
            TableColumn tcDeptCode = new TableColumn("deptCode", Application.getInstance().getMessage("leave.label.deptCode","Department Code"));
            tcDeptCode.setUrlParam("deptCode");
            addColumn(tcDeptCode);
            TableColumn tcDeptDescription = new TableColumn("deptDesc", Application.getInstance().getMessage("leave.label.deptDesc","Department Description"));
            addColumn(tcDeptDescription);
        }

        public String getTableRowKey() {
            return "deptCode";
        }

        public Collection getTableRows() {
            Collection rows = new ArrayList();
            String deptCode = (String) getFilterValue("deptCode");
            Application app = Application.getInstance();
            EmployeeModule module = (EmployeeModule) app.getModule(EmployeeModule.class);
            try {
                rows = module.getDepartmentList(deptCode, getSort(), isDesc(), getStart(), getRows());
            }
            catch (Exception e) {
                Log.getLog(this.getClass()).error(e.toString());
            }

            return rows;
        }

        public int getTotalRowCount() {
            int rows = 0;
            String deptCode = (String) getFilterValue("deptCode");
            Application app = Application.getInstance();
            EmployeeModule module = (EmployeeModule) app.getModule(EmployeeModule.class);
            try {
                rows = module.getDepartmentListCount(deptCode);
            }
            catch (Exception e) {
                Log.getLog(this.getClass()).error(e.toString());
            }

            return rows;
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            if ("delete".equals(action)) {
                Application app = Application.getInstance();
                try {
                    EmployeeModule module = (EmployeeModule) app.getModule(EmployeeModule.class);
                    for (int i=0; i < selectedKeys.length; i++) {
                        module.deleteDepartment(selectedKeys[i]);
                    }
                }
                catch (Exception e) {
                    Log.getLog(this.getClass()).error("Error processing action " + e.toString());
                }
            }

            return super.processAction(evt, action, selectedKeys);
        }

    }
}
