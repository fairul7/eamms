package com.tms.hr.employee.ui;

import com.tms.hr.employee.model.DepartmentDataObject;
import com.tms.hr.employee.model.EmployeeDataObject;
import com.tms.hr.employee.model.EmployeeException;
import com.tms.hr.employee.model.EmployeeModule;
import com.tms.hr.leave.model.LeaveModule;
import com.tms.hr.leave.model.LeaveSettings;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Calendar;

public class NewAddEmployeeForm extends Form {

    public static final String GENDER_MALE = "MALE";
    public static final String GENDER_FEMALE = "FEMALE";

    protected SelectBox userNames;
    protected Radio male;
    protected Radio female;
    protected DateField joinDate;
    protected DateField resignDate;
    protected SelectBox department;
    protected Label errorMsg;
    protected CheckBox recommender;
    protected CheckBox shiftWorker;
    protected SelectBox serviceClass;
    protected SelectBox reportToUsersList;
    protected Button add;
    protected Button cancel;

    public NewAddEmployeeForm() {
    }

    public NewAddEmployeeForm(String name) {
        super(name);
    }

    public void init() {
        initForm();
    }

    public void onRequest(Event event) {
        initForm();
    }

    public void initForm() {
        removeChildren();

        errorMsg = new Label("errorMsg");
        userNames = new EmployeeSelectBox("userNames", EmployeeSelectBox.SHOW_SETUP);
        userNames.addChild(new SelectBoxValidatorNotEmpty("v"));
        joinDate = new DatePopupField("joinDate");
        ((DatePopupField) joinDate).setOptional(false);
        resignDate = new DatePopupField("resignDate");
        ((DatePopupField) resignDate).setOptional(true);
        department = new SelectBox("department");
        male = new Radio("male", Application.getInstance().getMessage("employee.label.male","male"));
        male.setGroupName("gender");
        male.setChecked(true);
        female = new Radio("female", Application.getInstance().getMessage("employee.label.female","female"));
        female.setGroupName("gender");
        recommender = new CheckBox("recommender", Application.getInstance().getMessage("employee.label.recommendationRequired","Recommendation Required"));
        serviceClass = new SelectBox("serviceClass");
        recommender.setValue("N");
        recommender.setHidden(true);
        shiftWorker = new CheckBox("shiftWorker", Application.getInstance().getMessage("employee.label.shiftWorker","Non fixed working time"));
        shiftWorker.setHidden(true);

        Application application = Application.getInstance();
        LeaveModule leaveMod = (LeaveModule)application.getModule(LeaveModule.class);
        try {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            LeaveSettings settings = leaveMod.viewLeaveSettings(year);
            if (settings.isAllowShift()) {
                shiftWorker.setHidden(false);
            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving leave settings", e);
        }

        reportToUsersList = new EmployeeSelectBox("reportToUsersList", EmployeeSelectBox.SHOW_ALL);
        add = new Button("add");
        add.setText(Application.getInstance().getMessage("employee.label.submit","Submit"));
        cancel = new Button(Form.CANCEL_FORM_ACTION);
        cancel.setText(Application.getInstance().getMessage("employee.label.cancel","Cancel"));

        EmployeeModule handler = (EmployeeModule) application.getModule(EmployeeModule.class);
        try {
            department.addOption("", Application.getInstance().getMessage("employee.label.PleaseSelect","--Please Select--"));
            Collection cDeptList = handler.getDepartmentList();
            for (Iterator i = cDeptList.iterator(); i.hasNext();) {
                DepartmentDataObject o = (DepartmentDataObject) i.next();
                department.addOption(o.getDeptCode(), o.getDeptDesc());

            }
            department.addChild(new SelectBoxValidatorNotEmpty("v"));

            serviceClass.addOption("", Application.getInstance().getMessage("employee.label.PleaseSelect","--Please Select--"));
            Collection cService = handler.getAllServiceClass();
            for (Iterator i = cService.iterator(); i.hasNext();) {
                DepartmentDataObject o = (DepartmentDataObject) i.next();
                serviceClass.addOption(o.getServiceCode(), o.getServiceDesc());
            }
            serviceClass.addChild(new SelectBoxValidatorNotEmpty("v"));
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving departments and service classes", e);
        }

        addChild(errorMsg);
        addChild(userNames);
        addChild(joinDate);
        addChild(resignDate);
        addChild(department);
        addChild(male);
        addChild(female);
        addChild(recommender);
        addChild(serviceClass);
        addChild(shiftWorker);
        addChild(reportToUsersList);
        addChild(add);
        addChild(cancel);

        setMethod("POST");
    }

    public Forward onValidate(Event evt) throws RuntimeException {
        Forward fwd = null;
        String buttonName = findButtonClicked(evt);
        if (buttonName != null && add.getAbsoluteName().equals(buttonName)) {
            fwd = addEmployee();
            fwd = addEmployeeDepartment();
            fwd = addHierarchy();
        }
        return fwd;
    }

    protected Forward addEmployee() throws RuntimeException {
        Forward forward = null;
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
            handler.addEmployee(employeeDO);
            forward = new kacang.ui.Forward("success");
        }
        catch (EmployeeException e) {
            errorMsg.setText(Application.getInstance().getMessage("employee.error.employeeNotInserted","Employee Not Inserted. Please Contact Your System Adminstrator"));
            forward = new Forward("fail");
            setInvalid(true);
            Log.getLog(getClass()).error(e.toString(), e);
        }
        catch (Exception e) {
            throw new RuntimeException(e.toString(), e);
        }
        return forward;
    }

    protected Forward addEmployeeDepartment() throws RuntimeException {
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
        selectedList = (Collection) userNames.getValue();
        selected = (String) selectedList.iterator().next();
        departmentDO.setEmployeeID(selected);

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
            throw new RuntimeException(e.toString(), e);
        }
        return forward;
    }

    protected Forward addHierarchy() {
        Forward forward = null;
        Collection selectedList;
        DepartmentDataObject setup = new DepartmentDataObject();
        selectedList = (Collection) reportToUsersList.getValue();
        setup.setReportTo((String) selectedList.iterator().next());
        selectedList = (Collection) userNames.getValue();
        String selected = (String) selectedList.iterator().next();
        setup.setEmployeeID(selected);
        try {
            Application application = Application.getInstance();
            EmployeeModule handler = (EmployeeModule) application.getModule(EmployeeModule.class);
            handler.setupHierarchy(setup);
            forward = new Forward("success");
        }
        catch (Exception le) {
            forward = new kacang.ui.Forward("fail");
        }
        return forward;
    }

    public String getDefaultTemplate() {
        return "employee/employeeForm";
    }

    public class SelectBoxValidatorNotEmpty extends ValidatorNotEmpty {

        public SelectBoxValidatorNotEmpty() {
        }

        public SelectBoxValidatorNotEmpty(String name) {
            super(name);
        }

        public SelectBoxValidatorNotEmpty(String name, String text) {
            super(name, text);
        }

        public boolean validate(FormField formField) {
            if (formField.isHidden()) {
                return true;
            }
            boolean valid = super.validate(formField);
            if (valid && formField instanceof SelectBox) {
                Map map = ((SelectBox)formField).getSelectedOptions();
                if (map != null && map.size() > 0) {
                    String key = (String)map.keySet().iterator().next();
                    if (key == null || key.trim().length() == 0) {
                        valid = false;
                    }
                }
                else {
                    valid = false;
                }
            }
            return valid;
        }

    }
}
