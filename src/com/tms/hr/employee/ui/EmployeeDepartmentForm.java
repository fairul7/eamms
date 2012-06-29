package com.tms.hr.employee.ui;

import com.tms.hr.employee.model.DepartmentDataObject;
import com.tms.hr.employee.model.EmployeeException;
import com.tms.hr.employee.model.EmployeeModule;
import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.Form;
import kacang.stdui.SelectBox;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.Collection;

public class EmployeeDepartmentForm extends Form {
    private SelectBox employee;
    private SelectBox department;
    private CheckBox recommender;
    private SelectBox serviceClass;
    private Button add;
    private Button reset;
    private Button cancel;

    public EmployeeDepartmentForm() {
    }

    public EmployeeDepartmentForm(String s) {
        super(s);
    }

    public void init() {
        employee = new SelectBox("employee");
        department = new SelectBox("department");
        recommender = new CheckBox("recommender", "Recommender");
        serviceClass = new SelectBox("serviceClass");
        recommender.setValue("N");
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
            Collection cEmployeeList = handler.getEmployeesForDepartmentSetup();
            Collection cService = handler.getAllServiceClass();
            department.setOptions(cDeptList, "deptCode", "deptDesc");
            employee.setOptions(cEmployeeList, "employeeID", "name");
            serviceClass.setOptions(cService, "serviceCode", "serviceDesc");
        }
        catch (Exception e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }

        /*  Map userMap = new SequencedHashMap();
          try {
              SecurityService security = (SecurityService) Application.getInstance().getService(SecurityService.class);
              Collection userList = security.getUsers(new DaoQuery(), 0, -1, null, false);
              for (Iterator i = userList.iterator(); i.hasNext();) {
                  User user = (User) i.next();
                  userMap.put(user.getId(), user.getName());
              }
          } catch (Exception e) {
              Log.getLog(getClass()).error(e.toString(), e);
          }
          employee.setOptionMap(userMap);*/

        addChild(employee);
        addChild(department);
        addChild(recommender);
        addChild(serviceClass);
        addChild(add);
        addChild(reset);
        addChild(cancel);
        setMethod("post");
    }

    public SelectBox getEmployee() {
        return employee;
    }

    public SelectBox getDepartment() {
        return department;
    }

    public CheckBox getRecommender() {
        return recommender;
    }

    public SelectBox getServiceClass() {
        return serviceClass;
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
        Collection selectedList = (Collection) employee.getValue();
        String selected = (String) selectedList.iterator().next();
        departmentDO.setEmployeeID(selected);
        selectedList = (Collection) department.getValue();
        selected = (String) selectedList.iterator().next();
        departmentDO.setDeptCode(selected);
        selectedList = (Collection) serviceClass.getValue();
        selected = (String) selectedList.iterator().next();
        departmentDO.setServiceCode(selected);
        if (recommender.isChecked())
            departmentDO.setRecommender(true);

        try {
            Application application = Application.getInstance();
            EmployeeModule handler = (EmployeeModule) application.getModule(EmployeeModule.class);
            handler.addEmployeeDepartment(departmentDO);
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
        return "employee/employeeDepartmentForm";
    }
}
