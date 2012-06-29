package com.tms.hr.employee.ui;

import kacang.stdui.SelectBox;
import kacang.ui.Event;
import kacang.Application;
import kacang.util.Log;

import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.tms.hr.employee.model.EmployeeModule;
import com.tms.hr.employee.model.EmployeeDataObject;
import org.apache.commons.collections.SequencedHashMap;

public class EmployeeSelectBox extends SelectBox {

    public final static int SHOW_ALL = 0; // show all users
    public final static int SHOW_SETUP = 1; // show for adding users to module
    public final static int SHOW_DEPARTMENT = 2; // show for department setup
    public final static int SHOW_HIERARCHY = 3; // show for hierarchy setup

    private int type = SHOW_ALL;
    private String department;

    public EmployeeSelectBox() {
    }

    public EmployeeSelectBox(String name) {
        super(name);
    }

    public EmployeeSelectBox(String name, int type) {
        super(name);
        setType(type);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void onRequest(Event event) {
        populateUsers();
    }

    public void populateUsers() {
        Collection cUsers = new ArrayList();
        EmployeeModule handler;
        Application application = Application.getInstance();
        handler = (EmployeeModule) application.getModule(EmployeeModule.class);
        try {
            switch (getType()) {
                case SHOW_ALL:
                    cUsers = handler.getAllEmployees();
                    setOptionMap(new SequencedHashMap());
                    addOption("", Application.getInstance().getMessage("leave.label.pleaseselect","--Please Select--"));
                    for (Iterator i = cUsers.iterator(); i.hasNext();) {
                        EmployeeDataObject o = (EmployeeDataObject) i.next();
                        addOption(o.getEmployeeID(), o.getFullName());
                    }
                    break;
                case SHOW_SETUP:
                    cUsers = handler.getUsersForEmployeeSetup();
                    setOptionMap(new SequencedHashMap());
                    addOption("", Application.getInstance().getMessage("leave.label.pleaseselect","--Please Select--"));
                    for (Iterator i = cUsers.iterator(); i.hasNext();) {
                        EmployeeDataObject o = (EmployeeDataObject) i.next();
                        addOption(o.getEmployeeID(), o.getFullName());
                    }
                    break;
                case SHOW_DEPARTMENT:
                    cUsers = handler.getEmployeesForDepartmentSetup();
                    setOptionMap(new SequencedHashMap());
                    addOption("", Application.getInstance().getMessage("leave.label.pleaseselect","--Please Select--"));
                    for (Iterator i = cUsers.iterator(); i.hasNext();) {
                        EmployeeDataObject o = (EmployeeDataObject) i.next();
                        addOption(o.getEmployeeID(), o.getFullName());
                    }
                    break;
                case SHOW_HIERARCHY:
                    cUsers = handler.getEmployeesForHierarchySetup(getDepartment());
                    setOptionMap(new SequencedHashMap());
                    addOption("", Application.getInstance().getMessage("leave.label.pleaseselect","--Please Select--"));
                    for (Iterator i = cUsers.iterator(); i.hasNext();) {
                        EmployeeDataObject o = (EmployeeDataObject) i.next();
                        addOption(o.getEmployeeID(), o.getFullName());
                    }
                    break;
                default:
                    break;
            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving employees", e);
        }
    }
}
