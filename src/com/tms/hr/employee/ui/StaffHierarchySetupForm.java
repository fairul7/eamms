package com.tms.hr.employee.ui;

import kacang.stdui.Form;
import kacang.stdui.SelectBox;
import kacang.stdui.Button;
import kacang.stdui.Label;
import kacang.util.Log;
import kacang.Application;
import kacang.services.security.User;
import kacang.ui.Forward;
import com.tms.hr.employee.model.DepartmentDataObject;
import com.tms.hr.employee.model.EmployeeModule;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Iterator;

public class StaffHierarchySetupForm extends Form {
    private Label errorMsg;
    private SelectBox departmentList;
    private SelectBox usersList;
    private SelectBox reportToUsersList;
    private Button setup;
    private Button reset;
    private Button cancel;

    Log log = Log.getLog(getClass());

    public StaffHierarchySetupForm() {
        super();
    }

    public StaffHierarchySetupForm(String name) {
        setName(name);
    }

    public void init() {
        setMethod("POST");
        
        errorMsg = new Label("errorMsg");
        departmentList = new SelectBox("departmentList");
        usersList = new EmployeeSelectBox("usersList", EmployeeSelectBox.SHOW_HIERARCHY);
        reportToUsersList = new EmployeeSelectBox("reportToUsersList", EmployeeSelectBox.SHOW_ALL);
        setup = new Button("setup");
        setup.setText(Application.getInstance().getMessage("employee.label.submit","Submit"));
        reset = new Button("reset");
        reset.setText(Application.getInstance().getMessage("employee.label.reset","Reset"));
        cancel = new Button("cancel");
        cancel.setText(Application.getInstance().getMessage("employee.label.cancel","Cancel"));

        EmployeeModule handler;
        Application application = Application.getInstance();
        handler = (EmployeeModule) application.getModule(EmployeeModule.class);
        try {
            Collection cDeptList = handler.getDepartmentList();
            departmentList.addOption("-1", "Select Department");
            for (Iterator i = cDeptList.iterator(); i.hasNext();) {
                DepartmentDataObject department = (DepartmentDataObject) i.next();
                departmentList.setOptions(department.getDeptCode() + "=" + department.getDeptDesc());
            }
            /*departmentList.setOptions(cDeptList,"deptCode","deptDesc");*/
        }
        catch (Exception e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }
        departmentList.setOnChange("document.forms['" + getAbsoluteName() + "'].submit()");
        /*usersList.addOption("-1","Select Dept..");
        reportToUsersList.addOption("_1","Select Dept..");
*/
        usersList.setHidden(true);
        reportToUsersList.setHidden(true);
        setup.setHidden(true);
        reset.setHidden(true);
        cancel.setHidden(false);
        errorMsg.setHidden(true);
        addChild(errorMsg);
        addChild(departmentList);
        addChild(usersList);
        addChild(reportToUsersList);
        addChild(setup);
        addChild(reset);
        addChild(cancel);
    }

    public SelectBox getDepartmentList() {
        return departmentList;
    }

    public SelectBox getUsersList() {
        return usersList;
    }

    public SelectBox getReportToUsersList() {
        return reportToUsersList;
    }

    public Label getErrorMsg() {
        return errorMsg;
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
        User user = getWidgetManager().getUser();
        String userID = user.getId();
        Collection selectedList = (Collection) departmentList.getValue();
        String selected = (String) selectedList.iterator().next();
        if (selected != null && !selected.equals("")) {
            EmployeeModule handler;
            Application application = Application.getInstance();
            handler = (EmployeeModule) application.getModule(EmployeeModule.class);
            try {
//                Collection cReportToEmployeeList = handler.getAllEmployees();
                Collection cUsersList = handler.getEmployeesForHierarchySetup(selected);
                ((EmployeeSelectBox) usersList).setDepartment(selected);
                ((EmployeeSelectBox) usersList).populateUsers();
                if (cUsersList.size() > 0) {
                    usersList.setHidden(false);
                    reportToUsersList.setHidden(false);
                    errorMsg.setHidden(true);
                    setup.setHidden(false);
                    reset.setHidden(true);
                    cancel.setHidden(false);
//                    usersList.setOptions(cUsersList,"employeeID","name");
//                    reportToUsersList.setOptions(cReportToEmployeeList,"employeeID","name");
                }
                else {
                    errorMsg.setHidden(false);
                    errorMsg.setText(Application.getInstance().getMessage("employee.error.noEmployeeExists","No Employee Exists for Hierarchy Setup"));
                }
            }
            catch (Exception e) {
                Log.getLog(getClass()).error(e.toString(), e);
            }
        }
        return result;
    }

    public kacang.ui.Forward onValidate(kacang.ui.Event evt) {
        Forward fwd = null;
        String buttonName = findButtonClicked(evt);
        if (buttonName != null && (reset.getAbsoluteName().equals(buttonName) || cancel.getAbsoluteName().equals(buttonName))) {
            init();
            fwd = new kacang.ui.Forward();
            fwd.setName(Form.CANCEL_FORM_ACTION);
            return fwd;
        }
        else if (buttonName != null && setup.getAbsoluteName().equals(buttonName)) {
            fwd = setupHierarchy(evt.getRequest());
        }
        return fwd;
    }

    protected kacang.ui.Forward setupHierarchy(HttpServletRequest req) {
        kacang.ui.Forward forward = null;
        Collection selectedList;
        DepartmentDataObject setup = new DepartmentDataObject();
        selectedList = (Collection) departmentList.getValue();
        setup.setDeptCode((String) selectedList.iterator().next());
        selectedList = (Collection) usersList.getValue();
        setup.setEmployeeID((String) selectedList.iterator().next());
        selectedList = (Collection) reportToUsersList.getValue();
        setup.setReportTo((String) selectedList.iterator().next());
        try {
            Application application = Application.getInstance();
            EmployeeModule handler = (EmployeeModule) application.getModule(EmployeeModule.class);
            handler.setupHierarchy(setup);
            forward = new kacang.ui.Forward("hierarchySuccess");
        }
        catch (Exception le) {
            forward = new kacang.ui.Forward("fail");
        }
        return forward;
    }

    public String getDefaultTemplate() {
        return "employee/staffHierarchySetupForm";
    }
}
