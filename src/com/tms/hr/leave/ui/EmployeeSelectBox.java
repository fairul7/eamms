package com.tms.hr.leave.ui;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.*;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import com.tms.hr.employee.model.EmployeeModule;
import com.tms.hr.employee.model.DepartmentDataObject;

public class EmployeeSelectBox extends PopupSelectBox {

    public EmployeeSelectBox() {
        super();
    }

    public EmployeeSelectBox(String name) {
        super(name);
    }


    protected Table initPopupTable() {
        return new EmployeePopupTable();
    }

    protected Map generateOptionMap(String[] ids) {
        Map usersMap = new SequencedHashMap();
        if (ids == null || ids.length == 0) {
            return usersMap;
        }

        try {
            Application app = Application.getInstance();
            SecurityService security = (SecurityService)app.getService(SecurityService.class);
            DaoQuery query = new DaoQuery();
            query.addProperty(new OperatorIn("id", ids, DaoOperator.OPERATOR_AND));
            Collection userList = security.getUsers(query, 0, -1, "firstName", false);

            // build users map
            Map tmpMap = new SequencedHashMap();
            for (Iterator i=userList.iterator(); i.hasNext();) {
                User user = (User)i.next();
                tmpMap.put(user.getId(), user.getName());
            }

            // sort
            for (int j=0; j<ids.length; j++) {
                String name = (String)tmpMap.get(ids[j]);
                if (name == null) {
                    name = "---";
                }
                usersMap.put(ids[j], name);
            }
        }
        catch (SecurityException e) {
            Log.getLog(getClass()).error("Error retrieving users", e);
        }

        return usersMap;
    }

    public class EmployeePopupTable extends PopupSelectBoxTable {

        public EmployeePopupTable() {
        }

        public EmployeePopupTable(String name) {
            super(name);
        }

        public void init() {
            super.init();
            setWidth("100%");
            setModel(new EmployeeTableModel());
            loadDepts();
        }

        protected void loadDepts() {
            Application application = Application.getInstance();
            EmployeeModule em = (EmployeeModule)application.getModule(EmployeeModule.class);
            Map deptMap = new SequencedHashMap();
            try {
                Collection deptList = em.getDepartmentList();
                deptMap.put("", application.getMessage("leave.label.allDepartments", "All Departments"));
                for (Iterator i = deptList.iterator(); i.hasNext();) {
                    DepartmentDataObject dept = (DepartmentDataObject) i.next();
                    deptMap.put(dept.getDeptCode(), dept.getDeptDesc());
                }
            }
            catch (Exception e) {
                Log.getLog(EmployeePopupTable.class).error("Error loading departments: " + e.toString(), e);
            }
            //Adding Filters
            TableFilter deptFilter = getModel().getFilter("department");
            SelectBox deptSelect = (SelectBox) deptFilter.getWidget();
            deptSelect.setOptionMap(deptMap);
            deptSelect.setMultiple(false);
        }

        public void onRequest(Event evt) {
            loadDepts();
        }

        public class EmployeeTableModel extends PopupSelectBoxTableModel {
            public EmployeeTableModel() {
                super();

                Application application = Application.getInstance();
                //Adding Columns
                addColumn(new TableColumn("firstName", application.getMessage("security.label.firstName", "First Name")));
                addColumn(new TableColumn("lastName", application.getMessage("security.label.lastName", "Last Name")));

                //Adding Actions
                addAction(new TableAction(FORWARD_SELECT,  application.getMessage("general.label.select", "Select")));

                //Adding Filters
                TableFilter userNameFilter = new TableFilter("query");
                TableFilter deptFilter = new TableFilter("department");

                Map groupMap = new SequencedHashMap();
                SelectBox deptSelect = new SelectBox();
                deptSelect.setOptionMap(groupMap);
                deptSelect.setMultiple(false);
                deptFilter.setWidget(deptSelect);

                addFilter(userNameFilter);
                addFilter(deptFilter);
            }

            public Collection getTableRows() {
                Collection list = new ArrayList();
                Application application = Application.getInstance();
                EmployeeModule em = (EmployeeModule)application.getModule(EmployeeModule.class);
                try {
                    String query = (String)getFilterValue("query");
                    String deptCode = getDeptFilter();
                    list = em.getDepartmentEmployees(query, deptCode, null, getSort(), isDesc(), getStart(), getRows());
                }
                catch (Exception e) {
                    Log.getLog(EmployeePopupTable.class).error("Error retrieving employee rows", e);
                }
                return list;
            }

            public int getTotalRowCount() {
                int result = 0;
                Application application = Application.getInstance();
                EmployeeModule em = (EmployeeModule)application.getModule(EmployeeModule.class);
                try {
                    String query = (String)getFilterValue("query");
                    String deptCode = getDeptFilter();
                    result = em.getDepartmentEmployeesCount(query, deptCode, null);
                }
                catch (Exception e) {
                    Log.getLog(EmployeePopupTable.class).error("Error retrieving employee row count", e);
                }
                return result;
            }

            public String getDeptFilter() {
                Collection dept = (Collection) getFilterValue("department");
                if (dept != null)
                    if (!dept.isEmpty()) {
                        String val = (String) dept.iterator().next();
                        return val;
                    }
                return "";
            }

            public String getTableRowKey() {
                return "employeeID";
            }

        }
    }

}
