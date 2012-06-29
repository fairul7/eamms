package com.tms.hr.employee.ui;

import com.tms.hr.employee.model.DepartmentDataObject;
import com.tms.hr.employee.model.EmployeeDataObject;
import com.tms.hr.employee.model.EmployeeException;
import com.tms.hr.employee.model.EmployeeModule;
import com.tms.hr.leave.model.LeaveModule;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.security.SecurityException;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.Collection;

public class NewEditEmployeeForm extends NewAddEmployeeForm {

    protected String employeeID;

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public NewEditEmployeeForm() {
    }

    public NewEditEmployeeForm(String name) {
        super(name);
    }

    public void initForm() {
        super.initForm();
        userNames.setHidden(true);
    }

    public void onRequest(Event event) {
        initForm();
        populateForm();
    }

    protected void populateForm() {
        Application application = Application.getInstance();
        EmployeeModule handler = (EmployeeModule) application.getModule(EmployeeModule.class);
        if (employeeID != null && !employeeID.equals("")) {
            try {
                Collection cEmployee = handler.getEmployee(employeeID);
                EmployeeDataObject employeeDO = (EmployeeDataObject) cEmployee.iterator().next();
                if (employeeDO.getGender().equals(GENDER_MALE))
                    male.setChecked(true);
                else
                    female.setChecked(true);
                joinDate.setDate(employeeDO.getJoinDate());
                resignDate.setDate(employeeDO.getResignDate());
                department.setSelectedOptions(new String[] { employeeDO.getDeptCode() });

                DepartmentDataObject departmentDO = handler.getEmployeeDepartment(employeeID);
                department.setSelectedOptions(new String[] { departmentDO.getDeptCode() });
                serviceClass.setSelectedOptions(new String[] { departmentDO.getServiceCode() });
                if (departmentDO.isRecommender())
                    recommender.setChecked(true);
                else
                    recommender.setChecked(false);

                shiftWorker.setChecked(employeeDO.isShiftWorker());

                Collection department = handler.getEmployeeReportTo(employeeID);
                if (department.size() > 0) {
                    departmentDO = (DepartmentDataObject) department.iterator().next();
                    reportToUsersList.setSelectedOptions(new String[] { departmentDO.getReportTo() });
                }
            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Error populating employee form", e);
            }
        }
    }

    public Forward onValidate(Event evt) throws RuntimeException {
        Forward fwd = null;
        String buttonName = findButtonClicked(evt);
        if (buttonName != null && add.getAbsoluteName().equals(buttonName)) {
            fwd = editEmployee();
            fwd = editEmployeeDepartment();
            fwd = editHierarchy();
        }
        return fwd;
    }

    protected Forward editEmployee() throws RuntimeException {
        Forward forward = null;
        EmployeeDataObject employeeDO = new EmployeeDataObject();
        employeeDO.setEmployeeID(employeeID);
        User user = null;
        try {
            SecurityService security = (SecurityService) Application.getInstance().getService(SecurityService.class);
            user = security.getUser(employeeID);
        }
        catch (Exception e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }
        employeeDO.setName(user.getUsername());
        Collection selectedList = (Collection) department.getValue();
        String selected = (String) selectedList.iterator().next();
        employeeDO.setDeptCode(selected);
        employeeDO.setJoinDate(joinDate.getDate());
        employeeDO.setResignDate(resignDate.getDate());
        if (male.isChecked())
            employeeDO.setGender(GENDER_MALE);
        else
            employeeDO.setGender(GENDER_FEMALE);
        employeeDO.setRecommender("Y");
        employeeDO.setShiftWorker(shiftWorker.isChecked());

        try {
            Application application = Application.getInstance();
            EmployeeModule handler = (EmployeeModule) application.getModule(EmployeeModule.class);
            handler.editEmployee(employeeDO);
            forward = new kacang.ui.Forward("success");
        }
        catch (EmployeeException e) {
            errorMsg.setText(Application.getInstance().getMessage("employee.error.employeeNotInserted","Employee Not Inserted. Please Contact Your System Adminstrator"));
            forward = new Forward("fail");
            setInvalid(true);
            Log.getLog(getClass()).error(e.toString());
        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
        return forward;
    }

    protected Forward editEmployeeDepartment() throws RuntimeException {
        Forward forward;
        DepartmentDataObject departmentDO = new DepartmentDataObject();
        Collection selectedList = (Collection) department.getValue();
        String selected = (String) selectedList.iterator().next();
        departmentDO.setDeptCode(selected);
        selectedList = (Collection) serviceClass.getValue();
        selected = (String) selectedList.iterator().next();
        departmentDO.setServiceCode(selected);
        if (recommender.isChecked())
            departmentDO.setRecommender(true);
        departmentDO.setEmployeeID(employeeID);

        try {
            Application application = Application.getInstance();
            EmployeeModule handler = (EmployeeModule) application.getModule(EmployeeModule.class);
            handler.updateEmployeeDepartment(departmentDO);
            forward = new Forward("success");
        }
        catch (EmployeeException le) {
            forward = new Forward("fail");
        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
        return forward;
    }

    protected Forward editHierarchy() {
        Forward forward = null;
        Collection selectedList;
        DepartmentDataObject setup = new DepartmentDataObject();
        selectedList = (Collection) reportToUsersList.getValue();
        setup.setReportTo((String) selectedList.iterator().next());
        setup.setEmployeeID(employeeID);
        try {
            Application application = Application.getInstance();
            EmployeeModule handler = (EmployeeModule) application.getModule(EmployeeModule.class);
            handler.deleteHierarchy(employeeID);
            handler.setupHierarchy(setup);
            forward = new Forward("success");
        }
        catch (Exception le) {
            forward = new kacang.ui.Forward("fail");
        }
        return forward;
    }

    public User getEmployee() {
        try {
            return (employeeID != null) ? LeaveModule.utilGetUser(employeeID) : null;
        }
        catch (SecurityException e) {
            return null;
        }
    }

    public String getDefaultTemplate() {
        return "employee/employeeFormEdit";
    }

}
