package com.tms.hr.employee.ui;

import kacang.stdui.*;
import kacang.Application;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;

import com.tms.hr.employee.model.EmployeeModule;
import com.tms.hr.employee.model.EmployeeException;
import com.tms.hr.employee.model.EmployeeDao;
import com.tms.hr.employee.model.DepartmentDataObject;
import com.tms.hr.employee.model.GlobalDataObject;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class NewEmployeeTable extends Table {
    Log log = Log.getLog(getClass());

    public NewEmployeeTable() {
    }

    public NewEmployeeTable(String s) {
        super(s);
    }

    public void init() {
        super.init();
        setPageSize(10);
        setModel(new NewEmployeeTableModel());
        setWidth("100%");
    }

    class NewEmployeeTableModel extends TableModel {
        public NewEmployeeTableModel() {
            Application application = Application.getInstance();
            EmployeeModule employeeHandler = (EmployeeModule) application.getModule(EmployeeModule.class);
            EmployeeDao dao = (EmployeeDao)employeeHandler.getDao();

            try {
                if (dao.isUsersExists() > 0)
                    addAction(new TableAction("add", Application.getInstance().getMessage("employee.label.add","Add")));
            }
            catch (Exception e) {
                Log.getLog(getClass()).error(e.toString(), e);
            }

            addAction(new TableAction("delete", Application.getInstance().getMessage("employee.label.delete","Delete")));
            TableColumn tcUserName = new TableColumn("name", Application.getInstance().getMessage("employee.label.username","Username"));
            tcUserName.setUrlParam("employeeID");
            addColumn(tcUserName);

            TableColumn tcFirstName = new TableColumn("firstName", Application.getInstance().getMessage("employee.label.firstName","First Name"));
            addColumn(tcFirstName);
            TableColumn tcLastName = new TableColumn("lastName", Application.getInstance().getMessage("employee.label.lastName","Last Name"));
            addColumn(tcLastName);

            TableColumn tcReportTo = new TableColumn("deptDesc", Application.getInstance().getMessage("employee.label.department","Department"));
            addColumn(tcReportTo);

            TableColumn tcService = new TableColumn("serviceDesc", Application.getInstance().getMessage("employee.label.serviceClass","Service Class"));
            addColumn(tcService);

/*
            TableColumn tcRecommender = new TableColumn("recommender", Application.getInstance().getMessage("employee.label.recommendation","Recommendation"));
            String contextPath = (String) getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
            TableFormat booleanFormat = new TableBooleanFormat("<img src=\"" + contextPath + "/common/table/booleantrue.gif\">", "");
            tcRecommender.setFormat(booleanFormat);
            tcRecommender.setSortable(false);
            addColumn(tcRecommender);
*/

            TableFilter tfSearchBy = new TableFilter("searchBy");
            addFilter(tfSearchBy);

            TableFilter tfDepartment = new TableFilter("department");
            SelectBox selectDepartment = new SelectBox("selectDepartment");
            selectDepartment.addOption("", Application.getInstance().getMessage("employee.label.allDepartments","All Departments"));
            Collection deptList = new ArrayList();
            try {
                deptList = employeeHandler.getDepartmentList();
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

            TableFilter tfService = new TableFilter("service");
            SelectBox selectService = new SelectBox("selectService");
            selectService.addOption("", Application.getInstance().getMessage("employee.label.allServiceClasses","All Service Classes"));
            Collection serviceList = new ArrayList();
            try {
                serviceList = employeeHandler.getServiceClassList(null, null, false, 0, -1);
            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Unable to retrieve service class list", e);
            }
            for (Iterator i = serviceList.iterator(); i.hasNext();) {
                GlobalDataObject service = (GlobalDataObject) i.next();
                selectService.setOptions(service.getServiceCode() + "=" + service.getServiceDesc());
            }
            tfService.setWidget(selectService);
            addFilter(tfService);

        }

        public String getTableRowKey() {
            return "employeeID";
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            Log log = Log.getLog(this.getClass());
            if ("add".equals(action)) {
                return new Forward("add");
            }
            else if ("delete".equals(action)) {
                try {
                    EmployeeModule employeeModule = (EmployeeModule) Application.getInstance().getModule(EmployeeModule.class);
                    for (int i = 0; i < selectedKeys.length; i++) {
                        employeeModule.deleteDepartmentEmployee(selectedKeys[i]);
                        employeeModule.deleteEmployee(selectedKeys[i]);
                        employeeModule.deleteHierarchy(selectedKeys[i]);
                    }
                    return new Forward("delete");
                }
                catch (EmployeeException e) {
                    log.error("Unable to delete employees", e);
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

                List serviceStr = (List) getFilterValue("service");
                String service = null;
                if (serviceStr != null && serviceStr.size() > 0)
                    service = (String) serviceStr.get(0);

                EmployeeModule employeeModule = (EmployeeModule) Application.getInstance().getModule(EmployeeModule.class);
                return employeeModule.getDepartmentEmployees(searchBy, department, service, getSort(), isDesc(), getStart(), getRows());
            }
            catch (EmployeeException e) {
                // log error and return an empty collection
                log.error("Error retrieving deparment employees", e);
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

                List serviceStr = (List) getFilterValue("service");
                String service = null;
                if (serviceStr != null && serviceStr.size() > 0)
                    service = (String) serviceStr.get(0);

                EmployeeModule employeeModule = (EmployeeModule) Application.getInstance().getModule(EmployeeModule.class);
                return employeeModule.getDepartmentEmployeesCount(searchBy, department, service);
            }
            catch (EmployeeException e) {
                // log error and return an empty collection
                log.error("Error retrieving deparment employee count", e);
                return 0;
            }
        }
    }

}
