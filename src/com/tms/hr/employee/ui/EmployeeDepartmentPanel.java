package com.tms.hr.employee.ui;

import kacang.stdui.Panel;
import kacang.stdui.Table;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.ui.Widget;
import kacang.util.Log;

/**
 * Created by IntelliJ IDEA.
 * User: vijay
 * Date: Nov 5, 2003
 * Time: 12:31:30 PM
 * To change this template use Options | File Templates.
 */
public class EmployeeDepartmentPanel extends Panel {
    private EmployeeDepartmentTable employeeDepartmentTable;
    private EmployeeDepartmentForm employeeDepartmentForm;
    private EmployeeDepartmentEditForm employeeDepartmentEditForm;

    public EmployeeDepartmentPanel() {
    }

    public EmployeeDepartmentPanel(String s) {
        super(s);
    }

    public void init() {
        removeChildren();
        employeeDepartmentTable = new EmployeeDepartmentTable("employeeDepartmentTable");
        employeeDepartmentForm = new EmployeeDepartmentForm("employeeDepartmentForm");
        employeeDepartmentEditForm = new EmployeeDepartmentEditForm("employeeDepartmentEditForm");

        addChild(employeeDepartmentTable);
        addChild(employeeDepartmentForm);
        addChild(employeeDepartmentEditForm);

        employeeDepartmentTable.init();
        employeeDepartmentForm.init();
        employeeDepartmentEditForm.init();

        employeeDepartmentTable.setHidden(false);
        employeeDepartmentForm.setHidden(true);
        employeeDepartmentEditForm.setHidden(true);

        employeeDepartmentTable.addEventListener(this);
        employeeDepartmentForm.addEventListener(this);
        employeeDepartmentEditForm.addEventListener(this);
    }

    public Forward actionPerformed(Event evt) {
        Widget widget = evt.getWidget();
        Log log = Log.getLog(this.getClass());
        try {
            log.debug("~~~ widget = " + widget.getName());
            if (widget.getName().equals(employeeDepartmentTable.getName())) {
                Table table = (Table) widget;
                log.debug("~~~ evt.getType() = " + evt.getType());
                String employeeID = evt.getRequest().getParameter("employeeID");
                if (evt.getType().equals(Table.PARAMETER_KEY_ACTION)) {
                    if (table.getSelectedAction().equalsIgnoreCase("add")) {
                        employeeDepartmentEditForm.setHidden(true);
                        employeeDepartmentTable.setHidden(true);
                        employeeDepartmentForm.init();
                        employeeDepartmentForm.setHidden(false);
                    }
                    else if (table.getSelectedAction().equalsIgnoreCase("delete")) {
                        employeeDepartmentTable.init();
                        employeeDepartmentTable.setHidden(false);
                        employeeDepartmentForm.setHidden(true);
                        employeeDepartmentEditForm.setHidden(true);
                    }
                }
                else if (evt.getType().equals(Table.PARAMETER_KEY_SELECTION)) {
                    if (employeeID != null && !employeeID.equals("")) {
                        employeeDepartmentTable.setHidden(true);
                        employeeDepartmentForm.setHidden(true);
                        employeeDepartmentEditForm.setEmployeeID(employeeID);
                        employeeDepartmentEditForm.init();
                        employeeDepartmentEditForm.setHidden(false);
                    }
                }
            }
            else if (widget.getName().equals(employeeDepartmentForm.getName())) {
                String buttonClicked = employeeDepartmentForm.findButtonClicked(evt);
                if (buttonClicked.equalsIgnoreCase(employeeDepartmentForm.getCancel().getAbsoluteName()) ||
                        (!employeeDepartmentForm.isInvalid() && buttonClicked.equalsIgnoreCase(employeeDepartmentForm.getAdd().getAbsoluteName()))) {
                    employeeDepartmentTable.init();
                    employeeDepartmentTable.setHidden(false);
                    employeeDepartmentForm.setHidden(true);
                    employeeDepartmentEditForm.setHidden(true);
                }
            }
            else if (widget.getName().equals(employeeDepartmentEditForm.getName())) {
                String buttonClicked = employeeDepartmentEditForm.findButtonClicked(evt);
                if (buttonClicked.equalsIgnoreCase(employeeDepartmentEditForm.getCancel().getAbsoluteName()) ||
                        (!employeeDepartmentEditForm.isInvalid() && buttonClicked.equalsIgnoreCase(employeeDepartmentEditForm.getAdd().getAbsoluteName()))) {
                    employeeDepartmentTable.init();
                    employeeDepartmentTable.setHidden(false);
                    employeeDepartmentForm.setHidden(true);
                    employeeDepartmentEditForm.setHidden(true);
                }
            }
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return new Forward();
    }

    public EmployeeDepartmentTable getEmployeeDepartmentTable() {
        return employeeDepartmentTable;
    }

    public EmployeeDepartmentForm getEmployeeDepartmentForm() {
        return employeeDepartmentForm;
    }

    public EmployeeDepartmentEditForm getEmployeeDepartmentEditForm() {
        return employeeDepartmentEditForm;
    }

    public String getDefaultTemplate() {
        return "employee/employeeDepartmentPanel";
    }


}
