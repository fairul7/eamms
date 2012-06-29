package com.tms.hr.employee.ui;

import kacang.stdui.Panel;
import kacang.stdui.TabbedPanel;
import kacang.util.Log;


/**
 * Created by IntelliJ IDEA.
 * User: vijay
 * Date: Nov 1, 2003
 * Time: 9:53:29 AM
 * To change this template use Options | File Templates.
 */
public class AdminEmployeeTabbedPanel extends TabbedPanel {
    private Panel profilePanel;
    private Panel departmentPanel;
    private Panel hierarchyPanel;

    private HierarchySetupPanel hierarchySetupPanel;
    private EmployeeDepartmentPanel employeeDepartmentPanel;
    private EmployeeProfilePanel employeeProfilePanel;

    public AdminEmployeeTabbedPanel() {
        super();
    }

    public AdminEmployeeTabbedPanel(String name) {
        this();
        setName(name);
    }

    public void init() {
        removeChildren();
        try {
            profilePanel = new Panel("entitlementPanel");
            hierarchyPanel = new Panel("hierarchyPanel");
            departmentPanel = new Panel("departmentPanel");

            profilePanel.setText("Employee");
            hierarchyPanel.setText("Hierarchy");
            departmentPanel.setText("Department");

            employeeProfilePanel = new EmployeeProfilePanel("employeeProfile");
            hierarchySetupPanel = new HierarchySetupPanel("staffHierarchy");
            employeeDepartmentPanel = new EmployeeDepartmentPanel("employeeDepartmentPanel");

            hierarchyPanel.addChild(hierarchySetupPanel);
            departmentPanel.addChild(employeeDepartmentPanel);
            profilePanel.addChild(employeeProfilePanel);

            hierarchySetupPanel.init();
            employeeDepartmentPanel.init();
            employeeProfilePanel.init();

            addChild(profilePanel);
            addChild(departmentPanel);
            addChild(hierarchyPanel);
            setWidth("100%");
        }
        catch (Exception e) {
            Log log = Log.getLog(this.getClass());
            log.error(e.getMessage(), e);
        }
    }

    public Panel getDepartmentPanel() {
        return departmentPanel;
    }

    public Panel getHierarchyPanel() {
        return hierarchyPanel;
    }

    public Panel getProfilePanel() {
        return profilePanel;
    }

}
