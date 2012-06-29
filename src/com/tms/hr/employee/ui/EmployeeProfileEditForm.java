package com.tms.hr.employee.ui;

import kacang.stdui.*;
import kacang.stdui.validator.ValidatorEmail;
import kacang.Application;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;
import com.tms.hr.employee.model.EmployeeModule;
import com.tms.hr.employee.model.EmployeeDataObject;
import com.tms.hr.employee.model.EmployeeException;

import java.util.Collection;

public class EmployeeProfileEditForm extends Form {
    private static final String GENDER_MALE = "MALE";
    private static final String GENDER_FEMALE = "FEMALE";
    private String employeeID;
    private TextField userName;
    private Radio male;
    private Radio female;
    private DateField joinDate;
    private SelectBox department;
    private TextField email;
    private Button add;
    private Button reset;
    private Button cancel;
    private ValidatorEmail validMail;

    public EmployeeProfileEditForm() {
    }

    public EmployeeProfileEditForm(String s) {
        super(s);
    }

    public void init() {

        add = new Button("Edit");
        add.setText("Update");
        reset = new Button("reset");
        reset.setText("Reset");
        cancel = new Button("cancel");
        cancel.setText("Cancel");

        EmployeeModule handler;
        Application application = Application.getInstance();
        handler = (EmployeeModule) application.getModule(EmployeeModule.class);

        if (employeeID != null && !employeeID.equals("")) {
            try {
                String[] selectedDepartment = new String[1];
                Collection cEmployee = handler.getEmployee(employeeID);
                EmployeeDataObject employeeDO = (EmployeeDataObject) cEmployee.iterator().next();
                joinDate = new DateField("joinDate");
                department = new SelectBox("department");
                male = new Radio("male", "male");
                male.setGroupName("gender");
                female = new Radio("female", "female");
                female.setGroupName("gender");
                if (employeeDO.getGender().equals(GENDER_MALE))
                    male.setChecked(true);
                else
                    female.setChecked(true);
                joinDate.setDate(employeeDO.getJoinDate());
                selectedDepartment[0] = employeeDO.getDeptCode();
                Collection cDeptList = handler.getDepartmentList();
                department.setOptions(cDeptList, "deptCode", "deptDesc");
                department.setSelectedOptions(selectedDepartment);
                userName = new TextField("userNames", employeeDO.getName());
                email = new TextField("email", employeeDO.getEmail());
                validMail = new ValidatorEmail("notEmpty", "Please Enter Valid Email Address.");
                email.addChild(validMail);
                addChild(userName);
                addChild(joinDate);
                addChild(department);
                addChild(male);
                addChild(female);
                addChild(email);

            }
            catch (Exception e) {
                Log.getLog(getClass()).error(e.toString(), e);
            }

        }

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

    public TextField getUserName() {
        return userName;
    }

    public Radio getMale() {
        return male;
    }

    public Radio getFemale() {
        return female;
    }

    public DateField getJoinDate() {
        return joinDate;
    }

    public SelectBox getDepartment() {
        return department;
    }

    public TextField getEmail() {
        return email;
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

    public kacang.ui.Forward onSubmit(kacang.ui.Event evt) {
        String buttonName = findButtonClicked(evt);
        kacang.ui.Forward result = super.onSubmit(evt);
        if (buttonName != null && reset.getAbsoluteName().equals(buttonName) || cancel.getAbsoluteName().equals(buttonName)) {
            init();
            kacang.ui.Forward forward = new kacang.ui.Forward();
            forward.setName(Form.CANCEL_FORM_ACTION);
            return forward;
        }
        return result;
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
            fwd = addEmployee();
        }
        else {
            fwd = addEmployee();
        }
        return fwd;
    }

    private kacang.ui.Forward addEmployee() throws RuntimeException {
        kacang.ui.Forward forward = null;
        EmployeeDataObject employeeDO = new EmployeeDataObject();
        employeeDO.setEmployeeID(employeeID);
        employeeDO.setName(userName.getValue().toString());
        Collection selectedList = (Collection) department.getValue();
        String selected = (String) selectedList.iterator().next();
        employeeDO.setDeptCode(selected);
        employeeDO.setJoinDate(joinDate.getCalendar().getTime());
        employeeDO.setEmail((String) email.getValue());
        if (male.isChecked())
            employeeDO.setGender(GENDER_MALE);
        else
            employeeDO.setGender(GENDER_FEMALE);
        employeeDO.setRecommender("Y");

        try {
            Application application = Application.getInstance();
            EmployeeModule handler = (EmployeeModule) application.getModule(EmployeeModule.class);
            handler.editEmployee(employeeDO);
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
        return "employee/employeeProfileEditForm";
    }
}
