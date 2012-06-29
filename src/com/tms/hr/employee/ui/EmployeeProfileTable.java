package com.tms.hr.employee.ui;

import com.tms.hr.employee.model.DepartmentDataObject;
import com.tms.hr.employee.model.EmployeeDao;
import com.tms.hr.employee.model.EmployeeException;
import com.tms.hr.employee.model.EmployeeModule;
import com.tms.util.FormatUtil;
import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class EmployeeProfileTable extends Table {
    Log log = Log.getLog(getClass());

    public EmployeeProfileTable() {
    }

    public EmployeeProfileTable(String s) {
        super(s);
    }

    public void init() {
        super.init();
        setPageSize(10);
        setModel(new EmployeeProfileTableModel());
        setWidth("100%");
    }

    class EmployeeProfileTableModel extends TableModel {
        EmployeeProfileTableModel() {
            Application application = Application.getInstance();
            EmployeeDao dao = (EmployeeDao) application.getModule(EmployeeModule.class).getDao();
            try {
                if (dao.isUsersExists() > 0)
                    addAction(new TableAction("add", "Add"));
            }
            catch (DaoException e) {
                Log.getLog(getClass()).error(e.toString());
            }
            addAction(new TableAction("delete", "Delete"));
            TableColumn tcUserName = new TableColumn("name", "Username");
            tcUserName.setUrlParam("employeeID");
            addColumn(tcUserName);
            TableColumn tcFirstName = new TableColumn("firstName", "First Name");
            addColumn(tcFirstName);
            TableColumn tcLastName = new TableColumn("lastName", "Last Name");
            addColumn(tcLastName);
            TableColumn tcDOJ = new TableColumn("joinDate", "Date Joined");
            tcDOJ.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateFormat()));
            addColumn(tcDOJ);
            TableColumn tcDept = new TableColumn("deptDesc", "Department");
            addColumn(tcDept);
            TableColumn tcEmail = new TableColumn("email", "Email");
            addColumn(tcEmail);

            TableFilter tfSearchBy = new TableFilter("searchBy");
            addFilter(tfSearchBy);

            TableFilter tfDepartment = new TableFilter("department");
            SelectBox selectDepartment = new SelectBox("selectDepartment");
            selectDepartment.addOption("", Application.getInstance().getMessage("employee.label.allDepartments","All Departments"));
            Collection deptList = new ArrayList();
            try {
                EmployeeModule handler = (EmployeeModule) application.getModule(EmployeeModule.class);
                deptList = handler.getDepartmentList();
            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Unable to retrieve department list", e);
            }
            for (Iterator i = deptList.iterator(); i.hasNext();) {
                DepartmentDataObject dept = (DepartmentDataObject) i.next();
                selectDepartment.setOptions(dept.getDeptCode() + "=" + dept.getDeptDesc());
            }
            tfDepartment.setWidget(selectDepartment);
            addFilter(tfDepartment);

        }

        public String getTableRowKey() {
            return "employeeID";
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            Log log = Log.getLog(this.getClass());
            log.debug("~~~ action = " + action);
            if (action.equalsIgnoreCase("delete")) {
                try {
                    EmployeeModule employeeModule = (EmployeeModule) Application.getInstance().getModule(EmployeeModule.class);
                    for (int i = 0; i < selectedKeys.length; i++) {
                        employeeModule.deleteEmployee(selectedKeys[i]);
                    }
                }
                catch (EmployeeException e) {
                    log.error(e.getMessage(), e);
                }
            }
            return super.processAction(evt, action, selectedKeys);
        }


        public Collection getTableRows() {
            try {
                String searchBy = (String) getFilterValue("searchBy");

                List deptStr = (List) getFilterValue("department");
                String department = null;
                if (deptStr != null && deptStr.size() > 0)
                    department = (String) deptStr.get(0);

                EmployeeModule employeeModule = (EmployeeModule) Application.getInstance().getModule(EmployeeModule.class);
                return employeeModule.getEmployeeProfile(searchBy, department, getSort(), isDesc(), getStart(), getRows());
            }
            catch (EmployeeException e) {
                log.error(e.getMessage(), e);
                return new ArrayList();
            }
        }

        public int getTotalRowCount() {
            try {
                String searchBy = (String) getFilterValue("searchBy");

                List deptStr = (List) getFilterValue("department");
                String department = null;
                if (deptStr != null && deptStr.size() > 0)
                    department = (String) deptStr.get(0);

                EmployeeModule employeeModule = (EmployeeModule) Application.getInstance().getModule(EmployeeModule.class);
                return employeeModule.getEmployeeProfileCount(searchBy, department);
            }
            catch (EmployeeException e) {
                log.error(e.getMessage(), e);
                return 0;
            }
        }

    }
}
