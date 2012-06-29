package com.tms.hr.employee.ui;

import kacang.stdui.*;
import kacang.util.Log;
import kacang.Application;
import kacang.ui.Forward;
import kacang.ui.Event;

import com.tms.hr.employee.model.EmployeeModule;
import com.tms.hr.employee.model.EmployeeException;
import com.tms.hr.employee.model.DepartmentDataObject;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Vijay Kumar.k
 * User: vijay
 * Date: Oct 22, 2003
 * Time: 1:23:10 PM
 */
public class StaffHierarchySetupTable extends Table {
    Log log = Log.getLog(getClass());

    public StaffHierarchySetupTable() {
        super();
    }

    public StaffHierarchySetupTable(String name) {
        this();
        setName(name);
    }

    public void init() {
        super.init();
        setPageSize(10);
        setModel(new StaffHierarchySetupTableModel());
        setWidth("100%");
    }

    class StaffHierarchySetupTableModel extends TableModel {
        public StaffHierarchySetupTableModel() {
            EmployeeModule handler;
            Application application = Application.getInstance();
            handler = (EmployeeModule) application.getModule(EmployeeModule.class);

            addAction(new TableAction("add", Application.getInstance().getMessage("employee.label.add","Add")));
            addAction(new TableAction("delete", Application.getInstance().getMessage("employee.label.delete","Delete")));
            TableColumn tcUserName = new TableColumn("fullName", Application.getInstance().getMessage("employee.label.name","Name"), false);
            tcUserName.setUrlParam("employeeID");
            tcUserName.setSortable(true);
            addColumn(tcUserName);
            TableColumn tcReportTo = new TableColumn("reportToFullName", Application.getInstance().getMessage("employee.label.reportTo","Report To"), false);
            tcReportTo.setSortable(true);
            addColumn(tcReportTo);

           // TableFilter tfSearchBy = new TableFilter("searchBy", Application.getInstance().getMessage("employee.label.department","Department"));
            TableFilter tfSearchBy = new TableFilter("searchBy");

            SelectBox selectSearchBy = new SelectBox("selectDepartment");
            selectSearchBy.setOptions("0=" + Application.getInstance().getMessage("employee.label.allDepartments","All Departments"));
            Collection deptList = null;
            try {
                deptList = handler.getDepartmentList();
                //selectSearchBy.setOptions(deptList,"deptCode","deptDesc");
            }
            catch (Exception e) {
                Log.getLog(getClass()).error(e.toString(), e);
            }
            for (Iterator i = deptList.iterator(); i.hasNext();) {
                DepartmentDataObject department = (DepartmentDataObject) i.next();
                selectSearchBy.setOptions(department.getDeptCode() + "=" + department.getDeptDesc());
            }
            selectSearchBy.setMultiple(false);
            tfSearchBy.setWidget(selectSearchBy);
            addFilter(tfSearchBy);


                addFilter(new TableFilter("searchKey"));
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
                        employeeModule.deleteHierarchy(selectedKeys[i]);
                    }
                }
                catch (EmployeeException e) {
                    log.error(e.getMessage(), e);
                }
            }
            return super.processAction(evt, action, selectedKeys);
        }


        public Collection getTableRows() {



            List search = (List) getFilterValue("searchBy");
            String searchBy = "";
            if (search.size() > 0)
                searchBy = (String) search.get(0);



             String searchKey = (String)getFilterValue("searchKey");


            try {
                EmployeeModule employeeModule = (EmployeeModule) Application.getInstance().getModule(EmployeeModule.class);
                return employeeModule.getDepartmentHierarchy(searchBy,searchKey, getSort(), getStart(), getRows());
            }
            catch (EmployeeException e) {
                // log error and return an empty collection
                log.error(e.getMessage());
                return new ArrayList();
            }
        }

        public int getTotalRowCount() {



            List search = (List) getFilterValue("searchBy");
            String searchBy = "";
            if (search.size() > 0)
                searchBy = (String) search.get(0);


              String searchKey = (String)getFilterValue("searchKey");
                     if(searchKey !=null && !("".equals(searchKey)))
                         searchBy = searchKey;

            try {
                EmployeeModule employeeModule = (EmployeeModule) Application.getInstance().getModule(EmployeeModule.class);
                return employeeModule.getDepartmentHierarchyCount(searchBy,searchKey );
            }
            catch (EmployeeException e) {
                // log error and return an empty collection
                log.error(e.getMessage());
                return 0;
            }
        }
    }
}
