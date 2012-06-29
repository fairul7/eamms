package com.tms.hr.employee.ui;

import com.tms.hr.employee.model.DepartmentDataObject;
import com.tms.hr.employee.model.EmployeeException;
import com.tms.hr.employee.model.EmployeeModule;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.Collection;

public class EmployeeDepartmentEditForm extends Form {
    private String employeeID;
    private Label employee;
    private SelectBox department;
    private CheckBox recommender;
    private SelectBox serviceClass;
    private Button add;
    private Button reset;
    private Button cancel;

    public EmployeeDepartmentEditForm() {
    }

    public EmployeeDepartmentEditForm(String s) {
        super(s);
    }

    public void init() {
        employee = new Label("employee");
        department = new SelectBox("department");
        recommender = new CheckBox("recommender", "Recommendation Required");
        serviceClass = new SelectBox("serviceClass");
        recommender.setValue("Y");
        add = new Button("add");
        add.setText("Update");
        reset = new Button("reset");
        reset.setText("Reset");
        cancel = new Button("cancel");
        cancel.setText("Cancel");

        EmployeeModule handler;
        Application application = Application.getInstance();
        handler = (EmployeeModule) application.getModule(EmployeeModule.class);
        DepartmentDataObject departmentDO = null;
        if (employeeID != null && !employeeID.equals("")) {
            try {
                String[] selectedDepartment = new String[1];
                String[] selectedService = new String[1];
                departmentDO = handler.getEmployeeDepartment(employeeID);
                selectedDepartment[0] = departmentDO.getDeptCode();
                selectedService[0] = departmentDO.getServiceCode();
                Collection cDeptList = handler.getDepartmentList();
                Collection cService = handler.getAllServiceClass();
                department.setOptions(cDeptList, "deptCode", "deptDesc");
                serviceClass.setOptions(cService, "serviceCode", "serviceDesc");
                department.setSelectedOptions(selectedDepartment);
                serviceClass.setSelectedOptions(selectedService);
                if (departmentDO.isRecommender())
                    recommender.setChecked(true);
                else
                    recommender.setChecked(false);
            }
            catch (Exception e) {
                Log.getLog(getClass()).error(e.toString(), e);
            }
            User user = null;
            try {
                SecurityService security = (SecurityService) Application.getInstance().getService(SecurityService.class);
                user = security.getUser(employeeID);
            }
            catch (Exception e) {
                Log.getLog(getClass()).error(e.toString(), e);
            }
            employee.setText(user.getUsername() + " (" + user.getName() + ")");
        }

        addChild(employee);
        addChild(department);
        addChild(recommender);
        addChild(serviceClass);
        addChild(add);
        addChild(reset);
        addChild(cancel);
        setMethod("post");
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public Label getEmployee() {
        return employee;
    }

    public SelectBox getDepartment() {
        return department;
    }

    public SelectBox getServiceClass() {
        return serviceClass;
    }

    public CheckBox getRecommender() {
        return recommender;
    }

    public Button getAdd() {
        return add;
    }

    public Button getReset() {
        return reset;
    }

    public Button getCancel() {
        return cancel;
    }

    public Forward onValidate(Event evt) throws RuntimeException {
        Forward fwd = null;
        String buttonName = findButtonClicked(evt);
        if (buttonName != null && (reset.getAbsoluteName().equals(buttonName) || cancel.getAbsoluteName().equals(buttonName))) {
            init();
            fwd = new kacang.ui.Forward();
            fwd.setName(Form.CANCEL_FORM_ACTION);
        }
        else if (buttonName != null && add.getAbsoluteName().equals(buttonName)) {
            fwd = addEmployeeDepartment();
        }
        else {
            fwd = addEmployeeDepartment();
        }
        return fwd;
    }

    private kacang.ui.Forward addEmployeeDepartment() throws RuntimeException {
        kacang.ui.Forward forward;
        DepartmentDataObject departmentDO = new DepartmentDataObject();
        departmentDO.setEmployeeID(employeeID);
        Collection selectedList = (Collection) department.getValue();
        String selected = (String) selectedList.iterator().next();
        departmentDO.setDeptCode(selected);
        selectedList = (Collection) serviceClass.getValue();
        selected = (String) selectedList.iterator().next();
        departmentDO.setServiceCode(selected);
        if (recommender.isChecked())
            departmentDO.setRecommender(true);
        try {
            Application application = Application.getInstance();
            EmployeeModule handler = (EmployeeModule) application.getModule(EmployeeModule.class);
            handler.updateEmployeeDepartment(departmentDO);
            forward = new kacang.ui.Forward("success");
        }
        catch (EmployeeException le) {
            forward = new kacang.ui.Forward("fail");
        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
        return forward;
    }

    public String getDefaultTemplate() {
        return "employee/employeeDepartmentEditForm";
    }
}
