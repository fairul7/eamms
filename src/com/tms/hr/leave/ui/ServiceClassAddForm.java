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

public class ServiceClassAddForm extends Form {
    private Button cancel;
    private Button save;
    private TextField description;
    private TextField code;

    public ServiceClassAddForm() {
        super();
    }

    public ServiceClassAddForm(String name) {
        super(name);
    }

    public String getDefaultTemplate() {
        return "leave/serviceClassAddForm";
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
        ValidatorNotEmpty vne = new ValidatorNotEmpty("vne", Application.getInstance().getMessage("leave.label.enterService", "Please Enter Service Classication Code"));
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
            forward = saveService();
        }
        else if (buttonName != null && (cancel.getAbsoluteName().equals(buttonName))) {
            init();
            forward = new Forward();
            forward.setName(Form.CANCEL_FORM_ACTION);
        }

        return forward;
    }

    private Forward saveService() {
        Forward forward;
        Application app = Application.getInstance();
        EmployeeModule module = (EmployeeModule) app.getModule(EmployeeModule.class);
        DepartmentDataObject service = new DepartmentDataObject();
        service.setServiceCode((String) code.getValue());
        service.setServiceDesc((String) description.getValue());
        try {
            if (module.getServiceClassCodeCount(service.getServiceCode())>0) {
                forward = new Forward("duplicate");
            }
            else {
                module.addServiceClass(service);
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
