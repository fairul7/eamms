package com.tms.hr.employee.ui;

import kacang.stdui.*;
import kacang.stdui.*;
import kacang.ui.Forward;
import kacang.Application;
import kacang.util.Log;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

import com.tms.hr.employee.model.EmployeeModule;
import com.tms.hr.employee.model.DepartmentDataObject;


public class StaffHierarchyEditForm extends Form {
    private String userID;
    private String deptCode;
    private Label userName;
    private SelectBox reportToUsersList;
    private Button setup;
    private Button reset;
    private Button cancel;

    Log log = Log.getLog(getClass());

    public StaffHierarchyEditForm() {
        super();
    }

    public StaffHierarchyEditForm(String name) {
        setName(name);
    }

    public void init() {
        setMethod("POST");
        String name = "";
        String reportTo[] = new String[1];
        reportToUsersList = new EmployeeSelectBox("reportToUsersList", EmployeeSelectBox.SHOW_ALL);
        setup = new Button("setup");
        setup.setText(Application.getInstance().getMessage("employee.label.update","Update"));
        reset = new Button("reset");
        reset.setText(Application.getInstance().getMessage("employee.label.reset","Reset"));
        cancel = new Button("cancel");
        cancel.setText(Application.getInstance().getMessage("employee.label.cancel","Cancel"));

        EmployeeModule handler;
        Application application = Application.getInstance();
        handler = (EmployeeModule) application.getModule(EmployeeModule.class);
        if (getUserID() != null && !getUserID().equals("")) {
            try {
                Collection department = handler.getEmployeeReportTo(userID);
                DepartmentDataObject departmentDO = (DepartmentDataObject) department.iterator().next();
                name = departmentDO.getFullName();
                deptCode = departmentDO.getDeptCode();
                reportTo[0] = departmentDO.getReportTo();
//                Collection userList = handler.getAllEmployees();
//                reportToUsersList.setOptions(userList,"employeeID","name");
                reportToUsersList.setSelectedOptions(reportTo);
            }
            catch (Exception e) {
                Log.getLog(getClass()).error(e.toString(), e);
            }
            userName = new Label("userName", name);
        }
        else {
            userName = new Label("userName");
        }
        addChild(userName);
        addChild(reportToUsersList);
        addChild(setup);
        addChild(reset);
        addChild(cancel);
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public Label getUserName() {
        return userName;
    }

    public SelectBox getReportToUsersList() {
        return reportToUsersList;
    }

    public Button getSetup() {
        return setup;
    }

    public Button getReset() {
        return reset;
    }

    public Button getCancel() {
        return cancel;
    }

    public kacang.ui.Forward onSubmit(kacang.ui.Event evt) {
        kacang.ui.Forward result = null;
        result = super.onSubmit(evt);
        return result;
    }

    public kacang.ui.Forward onValidate(kacang.ui.Event evt) {
        Forward fwd = null;
        String buttonName = findButtonClicked(evt);
        if (buttonName != null &&  cancel.getAbsoluteName().equals(buttonName)) {
         
        	init();
            fwd = new kacang.ui.Forward();
            fwd.setName(Form.CANCEL_FORM_ACTION);
            return fwd;
        }
        else if(buttonName !=null && reset.getAbsoluteName().equals(buttonName)){
        	
        	reportToUsersList.setSelectedOption("-1");
        }
        else if (buttonName != null && setup.getAbsoluteName().equals(buttonName)) {
            fwd = updateHierarchy(evt.getRequest());
        }
        return fwd;
    }

    protected kacang.ui.Forward updateHierarchy(HttpServletRequest req) {
        kacang.ui.Forward forward = null;
        Collection selectedList;
        DepartmentDataObject setup = new DepartmentDataObject();
        if (getUserID() != null && !getUserID().equals("")) {
            setup.setEmployeeID(userID);
        }
        selectedList = (Collection) reportToUsersList.getValue();
        setup.setReportTo((String) selectedList.iterator().next());

        try {
            Application application = Application.getInstance();
            EmployeeModule handler = (EmployeeModule) application.getModule(EmployeeModule.class);
            handler.updateHierarchy(setup);
            forward = new kacang.ui.Forward("holidaySuccess");
        }
        catch (Exception le) {
            forward = new kacang.ui.Forward("fail");
        }
        return forward;
    }

    public String getDefaultTemplate() {
        return "employee/staffHierarchyEditForm";
    }
}
