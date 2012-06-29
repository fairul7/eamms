package com.tms.hr.employee.ui;

import com.tms.hr.employee.model.EmailExistsException;
import com.tms.hr.employee.model.EmployeeDataObject;
import com.tms.hr.employee.model.EmployeeException;
import com.tms.hr.employee.model.EmployeeModule;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorEmail;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.ArrayList;
import java.util.Collection;

public class EmployeeProfileForm extends Form {

    private static final String GENDER_MALE = "MALE";
    private static final String GENDER_FEMALE = "FEMALE";
    private SelectBox userNames;
    private Radio male;
    private Radio female;
    private DateField joinDate;
    private SelectBox department;
    private TextField email;
    private Button add;
    private Button reset;
    private Button cancel;
    private ValidatorEmail validMail;
    private Label errorMsg;

    public EmployeeProfileForm() {
    }

    public EmployeeProfileForm(String s) {
        super(s);
    }

    public void init() {
        errorMsg = new Label("errorMsg");
        userNames = new EmployeeSelectBox("userNames", EmployeeSelectBox.SHOW_SETUP);
        joinDate = new DateField("joinDate");
        department = new SelectBox("department");
        email = new TextField("email");
        validMail = new ValidatorEmail("notEmail", "Please Enter Valid Email Address");
        email.addChild(validMail);
        male = new Radio("male", "male");
        male.setGroupName("gender");
        male.setChecked(true);
        female = new Radio("female", "female");
        female.setGroupName("gender");
        add = new Button("add");
        add.setText("Add");
        reset = new Button("reset");
        reset.setText("Reset");
        cancel = new Button("cancel");
        cancel.setText("Cancel");
        EmployeeModule handler;
        Application application = Application.getInstance();
        handler = (EmployeeModule) application.getModule(EmployeeModule.class);
        try {
            Collection cDeptList = handler.getDepartmentList();
            department.setOptions(cDeptList, "deptCode", "deptDesc");
        }
        catch (Exception e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }

        /* Map userMap = new SequencedHashMap();
         try {
             SecurityService security = (SecurityService) Application.getInstance().getService(SecurityService.class);
             Collection userList = security.getUsers(new DaoQuery(), 0, -1, null, false);
             for (Iterator i = userList.iterator(); i.hasNext();) {
                 User user = (User) i.next();
                 userMap.put(user.getId(), user.getName());
             }
         } catch (Exception e) {
             Log.getLog(getClass()).error(e.toString(), e);
         }*/
        //userNames.setOptionMap(userMap);

        addChild(errorMsg);
        addChild(userNames);
        addChild(joinDate);
        addChild(department);
        addChild(male);
        addChild(female);
        addChild(email);
        addChild(add);
        addChild(reset);
        addChild(cancel);
        setMethod("post");
    }

    public SelectBox getUserNames() {
        return userNames;
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

    public Label getErrorMsg() {
        return errorMsg;
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
        return fwd;
    }

    private kacang.ui.Forward addEmployee() throws RuntimeException {
        kacang.ui.Forward forward = null;
        EmployeeDataObject employeeDO = new EmployeeDataObject();
        Collection selectedList = (Collection) userNames.getValue();
        String selected = (String) selectedList.iterator().next();
        employeeDO.setEmployeeID(selected);
        User user = null;
        try {
            SecurityService security = (SecurityService) Application.getInstance().getService(SecurityService.class);
            user = security.getUser(selected);
        }
        catch (Exception e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }
        employeeDO.setName(user.getUsername());
        selectedList = (Collection) department.getValue();
        selected = (String) selectedList.iterator().next();
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
            handler.addEmployee(employeeDO);
            forward = new kacang.ui.Forward("success");
        }
        catch (EmailExistsException e) {
            errorMsg.setText("Email Exists With Another Employee.");
            forward = new Forward("emailExists");
            setInvalid(true);
            email.setInvalid(true);
            Log.getLog(getClass()).error(e.toString());
        }
        catch (EmployeeException e) {
            errorMsg.setText("Employee Not Inserted!Contact System Adminstrator");
            forward = new kacang.ui.Forward("fail");
            setInvalid(true);
            Log.getLog(getClass()).error(e.toString());
        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
        return forward;
    }

    public String getDefaultTemplate() {
        return "employee/employeeProfileForm";
    }

}
