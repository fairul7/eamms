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

public class ServiceClassSetupTable extends Table {
    public ServiceClassSetupTable(String name) {
        super(name);
    }

    public void init() {
        super.init();
        setCurrentPage(1);
        setPageSize(15);
        setWidth("100%");
        setModel(new ServiceClassSetupTableModel());
    }

    public class ServiceClassSetupTableModel extends TableModel {
        public ServiceClassSetupTableModel() {
            addAction(new TableAction("add", Application.getInstance().getMessage("leave.label.add","Add")));
            addAction(new TableAction("delete", Application.getInstance().getMessage("leave.label.delete","Delete")));
            TableColumn tcServiceCode = new TableColumn("serviceCode", Application.getInstance().getMessage("leave.label.classCode","Classification Code"));
            tcServiceCode.setUrlParam("serviceCode");
            addColumn(tcServiceCode);
            TableColumn tcServiceDescription = new TableColumn("serviceDesc", Application.getInstance().getMessage("leave.label.classDesc","Classification Description"));
            addColumn(tcServiceDescription);
        }

        public String getTableRowKey() {
            return "serviceCode";
        }

        public Collection getTableRows() {
            Collection rows = new ArrayList();
            String serviceCode = (String) getFilterValue("serviceCode");
            Application app = Application.getInstance();
            EmployeeModule module = (EmployeeModule) app.getModule(EmployeeModule.class);
            try {
                rows = module.getServiceClassList(serviceCode, getSort(), isDesc(), getStart(), getRows());
            }
            catch (Exception e) {
                Log.getLog(this.getClass()).error(e.toString());
            }

            return rows;
        }

        public int getTotalRowCount() {
            int rows = 0;
            String serviceCode = (String) getFilterValue("serviceCode");
            Application app = Application.getInstance();
            EmployeeModule module = (EmployeeModule) app.getModule(EmployeeModule.class);
            try {
                rows = module.getServiceClassListCount(serviceCode);
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
                        module.deleteServiceClass(selectedKeys[i]);
                    }
                    //return new Forward("deleted");
                }
                catch (Exception e) {
                    Log.getLog(this.getClass()).error("Error processing action " + e.toString());
                }
            }

            return super.processAction(evt, action, selectedKeys);
        }

    }
}
