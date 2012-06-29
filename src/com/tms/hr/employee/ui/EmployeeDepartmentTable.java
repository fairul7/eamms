package com.tms.hr.employee.ui;

import com.tms.hr.employee.model.DepartmentDataObject;
import com.tms.hr.employee.model.EmployeeException;
import com.tms.hr.employee.model.EmployeeModule;
import com.tms.hr.employee.model.GlobalDataObject;
import kacang.Application;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;
import kacang.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class EmployeeDepartmentTable extends Table {
    Log log = Log.getLog(getClass());

    public EmployeeDepartmentTable() {
    }

    public EmployeeDepartmentTable(String s) {
        super(s);
    }

    public void init() {
        super.init();
        setPageSize(10);
        setModel(new EmployeeDepartmentTableModel());
        setWidth("100%");
    }

    class EmployeeDepartmentTableModel extends TableModel {
        public EmployeeDepartmentTableModel() {
            Application application = Application.getInstance();
            EmployeeModule employeeHandler = (EmployeeModule) application.getModule(EmployeeModule.class);

            try {
                if (employeeHandler.countEmployeesForDepartmentSetup() > 0)
                    addAction(new TableAction("add", "Add"));
            }
            catch (Exception e) {
                Log.getLog(getClass()).error(e.toString(), e);
            }

            addAction(new TableAction("delete", "Delete"));
            TableColumn tcUserName = new TableColumn("name", "Username");
            tcUserName.setUrlParam("employeeID");
            addColumn(tcUserName);

            TableColumn tcFirstName = new TableColumn("firstName", "First Name");
            addColumn(tcFirstName);
            TableColumn tcLastName = new TableColumn("lastName", "Last Name");
            addColumn(tcLastName);

            TableColumn tcReportTo = new TableColumn("deptDesc", "Department");
            addColumn(tcReportTo);

            TableColumn tcService = new TableColumn("serviceDesc", "Service Class");
            addColumn(tcService);

            TableColumn tcRecommender = new TableColumn("recommender", "Recommendation");
            String contextPath = (String) getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
            TableFormat booleanFormat = new TableBooleanFormat("<img src=\"" + contextPath + "/common/table/booleantrue.gif\">", "");
            tcRecommender.setFormat(booleanFormat);
            tcRecommender.setSortable(false);
            addColumn(tcRecommender);
            EmployeeModule handler;

            handler = (EmployeeModule) application.getModule(EmployeeModule.class);

/*
            Collection serviceList= null;
            TableFilter tfSearchBy = new TableFilter("searchBy", "Department");
            SelectBox selectSearchBy = new SelectBox("selectService");
            selectSearchBy.setOptions("0=Show All..");
            try {

                serviceList = handler.getDepartmentList();
                //selectSearchBy.setOptions(serviceList, "deptCode", "deptDesc");
            } catch (Exception e) {
                Log.getLog(getClass()).error(e.toString(), e);
            }
            for(Iterator i = serviceList.iterator();i.hasNext();){
                DepartmentDataObject dept = (DepartmentDataObject) i.next();
                selectSearchBy.setOptions(dept.getDeptCode()+"="+dept.getDeptDesc());
            }
            tfSearchBy.setWidget(selectSearchBy);
            addFilter(tfSearchBy);
*/

            TableFilter tfSearchBy = new TableFilter("searchBy");
            addFilter(tfSearchBy);

            TableFilter tfDepartment = new TableFilter("department");
            SelectBox selectDepartment = new SelectBox("selectDepartment");
            selectDepartment.addOption("", Application.getInstance().getMessage("employee.label.allDepartments","All Departments"));
            Collection deptList = new ArrayList();
            try {
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

            TableFilter tfService = new TableFilter("service");
            SelectBox selectService = new SelectBox("selectService");
            selectService.addOption("", "All Service Classes");
            Collection serviceList = new ArrayList();
            try {
                serviceList = handler.getAllServiceClass();
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
            log.debug("~~~ action = " + action);
            if (action.equalsIgnoreCase("delete")) {
                try {
                    EmployeeModule employeeModule = (EmployeeModule) Application.getInstance().getModule(EmployeeModule.class);
                    for (int i = 0; i < selectedKeys.length; i++) {
                        employeeModule.deleteDepartmentEmployee(selectedKeys[i]);
                    }
                }
                catch (EmployeeException e) {
                    log.error(e.getMessage(), e);
                }
            }
            return super.processAction(evt, action, selectedKeys);
        }

        public Collection getTableRows() {
/*
            List search = (List) getFilterValue("searchBy");
            String searchBy = "";
             if(search.size()>0)
                 searchBy = (String)search.get(0);
*/

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
/*
            List search = (List) getFilterValue("searchBy");
            String searchBy = "";
             if(search.size()>0)
                 searchBy = (String)search.get(0);
*/

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
