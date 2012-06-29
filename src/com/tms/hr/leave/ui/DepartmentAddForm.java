package com.tms.hr.leave.ui;

import kacang.stdui.Form;
import kacang.stdui.Button;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.Application;
import kacang.util.Log;
import kacang.ui.Forward;
import kacang.ui.Event;

import com.tms.hr.employee.model.EmployeeModule;
import com.tms.hr.employee.model.DepartmentDataObject;

public class DepartmentAddForm extends Form {
    private Button cancel;
    private Button save;
    private TextField description;
    private TextField code;

    public DepartmentAddForm() {
        super();
    }

    public DepartmentAddForm(String name) {
        super(name);
    }

    public String getDefaultTemplate() {
        return "leave/departmentSetupForm";
    }

    public void init() {
        setMethod("POST");
        
        code = new TextField("code");
        code.setSize("10");
        description = new TextField("description");
        save = new Button("save");
        save.setText(Application.getInstance().getMessage("leave.label.submit", "Submit"));
        cancel = new Button("cancel");
        cancel.setText(Application.getInstance().getMessage("leave.label.cancel", "Cancel"));
        ValidatorNotEmpty vne = new ValidatorNotEmpty("vne", Application.getInstance().getMessage("leave.label.enterDepartment", "Please Enter Department Code"));
        code.addChild(vne);

        addChild(code);
        addChild(description);
        addChild(save);
        addChild(cancel);
    }

    public Forward onValidate(Event evt) {
        Forward forward = null;
        String buttonName = findButtonClicked(evt);
        if (buttonName != null && (save.getAbsoluteName().equals(buttonName))) {
            forward = saveDepartment();
        }
        else if (buttonName != null && (cancel.getAbsoluteName().equals(buttonName))) {
            init();
            forward = new Forward();
            forward.setName(Form.CANCEL_FORM_ACTION);
        }

        return forward;
    }

    private Forward saveDepartment() {
        Forward forward;
        Application app = Application.getInstance();
        EmployeeModule module = (EmployeeModule) app.getModule(EmployeeModule.class);
        DepartmentDataObject department = new DepartmentDataObject();
        department.setDeptCode((String) code.getValue());
        department.setDeptDesc((String) description.getValue());
        try {
            if (module.getDepartmentCodeCount(department.getDeptCode())>0) {
                forward = new Forward("duplicate");
            }
            else {
                module.addDepartment(department);
                forward = new Forward("success");
            }
        }
        catch (Exception e) {
            Log.getLog(this.getClass()).error(e.toString());
            forward = new Forward("fail");
        }

        return forward;
    }

    public Button getCancel() {
        return cancel;
    }

    public void setCancel(Button cancel) {
        this.cancel = cancel;
    }

    public Button getSave() {
        return save;
    }

    public void setSave(Button save) {
        this.save = save;
    }

    public TextField getDescription() {
        return description;
    }

    public void setDescription(TextField description) {
        this.description = description;
    }

    public TextField getCode() {
        return code;
    }

    public void setCode(TextField code) {
        this.code = code;
    }
}
