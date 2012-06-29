package com.tms.hr.employee.ui;

import kacang.stdui.*;
import kacang.ui.EventListener;
import kacang.util.Log;
import kacang.ui.*;
import kacang.Application;

public class
        HierarchySetupPanel extends Panel implements EventListener {
    private StaffHierarchySetupTable staffHierarchySetupTable;
    private StaffHierarchyEditForm staffHierarchyEditForm;
    private StaffHierarchySetupForm staffHierarchySetupForm;
    private String title;

    public HierarchySetupPanel() {
        super();
    }

    public HierarchySetupPanel(String name) {
        this();
        setName(name);
    }

    public void init() {
        removeChildren();
        try {
            title = Application.getInstance().getMessage("leave.label.EmployeeHierarchy","Employee Hierarchy");
            staffHierarchySetupTable = new StaffHierarchySetupTable("staffHierarchy");
            staffHierarchyEditForm = new StaffHierarchyEditForm("staffHierarchyEditForm");
            staffHierarchySetupForm = new StaffHierarchySetupForm("staffHierarchySetupForm");

            addChild(staffHierarchySetupTable);
            addChild(staffHierarchyEditForm);
            addChild(staffHierarchySetupForm);

            staffHierarchySetupTable.init();
            staffHierarchyEditForm.init();
            staffHierarchySetupForm.init();

            staffHierarchySetupTable.setHidden(false);
            staffHierarchyEditForm.setHidden(true);
            staffHierarchySetupForm.setHidden(true);

            staffHierarchySetupTable.addEventListener(this);
            staffHierarchyEditForm.addEventListener(this);
            staffHierarchySetupForm.addEventListener(this);
        }
        catch (Exception e) {
            Log log = Log.getLog(this.getClass());
            log.error(e.getMessage(), e);
        }
    }

    public Forward actionPerformed(Event evt) {
        Widget widget = evt.getWidget();
        Log log = Log.getLog(this.getClass());
        Application app = Application.getInstance();
        title = app.getMessage("leave.label.EmployeeHierarchy","Employee Hierarchy");
        try {
            log.debug("~~~ widget = " + widget.getName());
            if (widget.getName().equals(staffHierarchySetupTable.getName())) {
                Table table = (Table) widget;
                log.debug("~~~ evt.getType() = " + evt.getType());
                String employeeID = evt.getRequest().getParameter("employeeID");
                if (evt.getType().equals(Table.PARAMETER_KEY_ACTION)) {
                    if (table.getSelectedAction().equalsIgnoreCase("add")) {
                        staffHierarchySetupTable.init();
                        staffHierarchySetupTable.setHidden(true);
                        staffHierarchyEditForm.init();
                        staffHierarchyEditForm.setHidden(true);
                        staffHierarchySetupForm.init();
                        staffHierarchySetupForm.setHidden(false);
                        String title = app.getMessage("leave.label.addNewEmployeeHierarchy","Add New Employee Hierarchy");
                        setTitle(title);
                    }
                    else if (table.getSelectedAction().equalsIgnoreCase("delete")) {

                        staffHierarchySetupTable.init();
                        staffHierarchySetupTable.setHidden(false);
                        staffHierarchyEditForm.init();
                        staffHierarchyEditForm.setHidden(true);
                        staffHierarchySetupForm.init();
                        staffHierarchySetupForm.setHidden(true);
                    }
                }
                else if (evt.getType().equals(Table.PARAMETER_KEY_SELECTION)) {
                    if (employeeID != null && !employeeID.equals("")) {
                        staffHierarchySetupTable.init();
                        staffHierarchySetupTable.setHidden(true);
                        staffHierarchySetupForm.init();
                        staffHierarchySetupForm.setHidden(true);
                        staffHierarchyEditForm.setUserID(employeeID);
                        staffHierarchyEditForm.init();
                        staffHierarchyEditForm.setHidden(false);
                        String title = app.getMessage("leave.label.editEmployeeHierarchy","Edit Employee Hierarchy");
                        setTitle(title);
                    }
                }

            }
            else if (widget.getName().equals(staffHierarchySetupForm.getName())) {
                String buttonClicked = staffHierarchySetupForm.findButtonClicked(evt);
                if (buttonClicked != null && staffHierarchySetupForm.getCancel().getAbsoluteName().equalsIgnoreCase(buttonClicked) ||
                        (!staffHierarchySetupForm.isInvalid() && staffHierarchySetupForm.getSetup().getAbsoluteName().equalsIgnoreCase(buttonClicked))) {
                    staffHierarchySetupTable.init();
                    staffHierarchySetupTable.setHidden(false);
                    staffHierarchyEditForm.setHidden(true);
                    staffHierarchySetupForm.setHidden(true);
                }
            }
            else if (widget.getName().equals(staffHierarchyEditForm.getName())) {
                String buttonClicked = staffHierarchyEditForm.findButtonClicked(evt);
                if (buttonClicked != null && staffHierarchyEditForm.getCancel().getAbsoluteName().equalsIgnoreCase(buttonClicked) ||
                        (!staffHierarchyEditForm.isInvalid() && staffHierarchyEditForm.getSetup().getAbsoluteName().equalsIgnoreCase(buttonClicked))) {
                    staffHierarchySetupTable.init();
                    staffHierarchySetupTable.setHidden(false);
                    staffHierarchyEditForm.setHidden(true);
                    staffHierarchySetupForm.setHidden(true);
                }
            }

        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return new Forward();
    }

    public StaffHierarchySetupTable getStaffHierarchySetupTable() {
        return staffHierarchySetupTable;
    }

    public void setStaffHierarchySetupTable(StaffHierarchySetupTable staffHierarchySetupTable) {
        this.staffHierarchySetupTable = staffHierarchySetupTable;
    }

    public StaffHierarchyEditForm getstaffHierarchyEditForm() {
        return staffHierarchyEditForm;
    }

    public void setstaffHierarchyEditForm(StaffHierarchyEditForm staffHierarchyEditForm) {
        this.staffHierarchyEditForm = staffHierarchyEditForm;
    }

    public StaffHierarchyEditForm getStaffHierarchyEditForm() {
        return staffHierarchyEditForm;
    }

    public void setStaffHierarchyEditForm(StaffHierarchyEditForm staffHierarchyEditForm) {
        this.staffHierarchyEditForm = staffHierarchyEditForm;
    }

    public StaffHierarchySetupForm getStaffHierarchySetupForm() {
        return staffHierarchySetupForm;
    }

    public void setStaffHierarchySetupForm(StaffHierarchySetupForm staffHierarchySetupForm) {
        this.staffHierarchySetupForm = staffHierarchySetupForm;
    }

    public String getDefaultTemplate() {
        return "employee/hierarchySetupPanel";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
