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
 * Date: Nov 6, 2003
 * Time: 3:24:35 PM
 * To change this template use Options | File Templates.
 */
public class EmployeeProfilePanel extends Panel {
    private EmployeeProfileTable employeeProfileTable;
    private EmployeeProfileForm employeeProfileForm;
    private EmployeeProfileEditForm employeeProfileFormEdit;

    public EmployeeProfilePanel() {
    }

    public EmployeeProfilePanel(String s) {
        super(s);
    }

    public void init() {
        removeChildren();
        employeeProfileTable = new EmployeeProfileTable("employeeDepartmentTable");
        employeeProfileForm = new EmployeeProfileForm("employeeProfileForm");
        employeeProfileFormEdit = new EmployeeProfileEditForm("employeeProfileFormEdit");

        addChild(employeeProfileTable);
        addChild(employeeProfileForm);
        addChild(employeeProfileFormEdit);

        employeeProfileTable.init();
        employeeProfileForm.init();
        employeeProfileFormEdit.init();

        employeeProfileTable.setHidden(false);
        employeeProfileForm.setHidden(true);
        employeeProfileFormEdit.setHidden(true);

        employeeProfileTable.addEventListener(this);
        employeeProfileForm.addEventListener(this);
        employeeProfileFormEdit.addEventListener(this);
    }

    public Forward actionPerformed(Event evt) {
        Widget widget = evt.getWidget();
        Log log = Log.getLog(this.getClass());
        try {
            log.debug("~~~ widget = " + widget.getName());
            if (widget.getName().equals(employeeProfileTable.getName())) {
                Table table = (Table) widget;
                log.debug("~~~ evt.getType() = " + evt.getType());
                String employeeID = evt.getRequest().getParameter("employeeID");
                if (evt.getType().equals(Table.PARAMETER_KEY_ACTION)) {
                    if (table.getSelectedAction().equalsIgnoreCase("add")) {
                        employeeProfileTable.setHidden(true);
                        employeeProfileForm.init();
                        employeeProfileForm.setHidden(false);
                        employeeProfileFormEdit.setHidden(true);
                    }
                    else if (table.getSelectedAction().equalsIgnoreCase("delete")) {
                        employeeProfileTable.init();
                        employeeProfileTable.setHidden(false);
                        employeeProfileForm.setHidden(true);
                        employeeProfileFormEdit.setHidden(true);
                    }
                }
                else if (evt.getType().equals(Table.PARAMETER_KEY_SELECTION)) {
                    if (employeeID != null && !employeeID.equals("")) {
                        employeeProfileTable.setHidden(true);
                        employeeProfileForm.setHidden(true);
                        employeeProfileFormEdit.setEmployeeID(employeeID);
                        employeeProfileFormEdit.init();
                        employeeProfileFormEdit.setHidden(false);
                        /*     employeeDepartmentForm.setHidden(true);
                             employeeDepartmentEditForm.setEmployeeID(employeeID);
                             employeeDepartmentEditForm.init();
                             employeeDepartmentEditForm.setHidden(false);*/
                    }
                }
            }
            else if (widget.getName().equals(employeeProfileForm.getName())) {
                String buttonClicked = employeeProfileForm.findButtonClicked(evt);
                if (buttonClicked.equalsIgnoreCase(employeeProfileForm.getCancel().getAbsoluteName()) ||
                        (!employeeProfileForm.isInvalid() && buttonClicked.equalsIgnoreCase(employeeProfileForm.getAdd().getAbsoluteName()))) {
                    employeeProfileTable.init();
                    employeeProfileTable.setHidden(false);
                    employeeProfileForm.setHidden(true);
                    employeeProfileFormEdit.setHidden(true);
                }
            }
            else if (widget.getName().equals(employeeProfileFormEdit.getName())) {
                String buttonClicked = employeeProfileFormEdit.findButtonClicked(evt);
                if (buttonClicked.equalsIgnoreCase(employeeProfileFormEdit.getCancel().getAbsoluteName()) ||
                        (!employeeProfileFormEdit.isInvalid() && buttonClicked.equalsIgnoreCase(employeeProfileFormEdit.getAdd().getAbsoluteName()))) {
                    employeeProfileTable.init();
                    employeeProfileTable.setHidden(false);
                    employeeProfileForm.setHidden(true);
                    employeeProfileFormEdit.setHidden(true);
                }
            }

        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return new Forward();
    }

    public EmployeeProfileTable getEmployeeProfileTable() {
        return employeeProfileTable;
    }

    public EmployeeProfileForm getEmployeeProfileForm() {
        return employeeProfileForm;
    }

    public EmployeeProfileEditForm getEmployeeProfileFormEdit() {
        return employeeProfileFormEdit;
    }

    public String getDefaultTemplate() {
        return "employee/employeeProfilePanel";
    }


}
