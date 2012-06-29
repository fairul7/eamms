package com.tms.hr.leave.ui;

import kacang.stdui.Form;
import kacang.stdui.Button;
import kacang.stdui.TextField;
import kacang.stdui.Label;
import kacang.Application;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;
import com.tms.hr.employee.model.EmployeeModule;
import com.tms.hr.employee.model.DepartmentDataObject;

public class ServiceClassEditForm extends Form {
    private Button cancel;
    private Button save;
    private TextField description;
    private TextField serviceCode;
    private Label codeLabel;
    private String code;

    public ServiceClassEditForm() {
    }

    public ServiceClassEditForm(String name) {
        super(name);
    }

    public String getDefaultTemplate() {
        return "leave/serviceClassEditForm";
    }

    public void init() {
        setMethod("POST");

        codeLabel = new Label("codeLabel");
        description = new TextField("description");
        serviceCode = new TextField("serviceCode");
        serviceCode.setHidden(true);
        save = new Button("save");
        save.setText(Application.getInstance().getMessage("leave.label.update", "Update"));
        cancel = new Button("cancel");
        cancel.setText(Application.getInstance().getMessage("leave.label.cancel", "Cancel"));

        Application app = Application.getInstance();
        try {
            EmployeeModule module = (EmployeeModule) app.getModule(EmployeeModule.class);
            DepartmentDataObject valueObject = module.getServiceClass(getCode());
            codeLabel.setText(valueObject.getServiceCode());
            serviceCode.setValue(valueObject.getServiceCode());
            description.setValue(valueObject.getServiceDesc());
        }
        catch (Exception e) {
            Log.getLog(this.getClass()).error(e.toString());
        }

        addChild(codeLabel);
        addChild(serviceCode);
        addChild(description);
        addChild(save);
        addChild(cancel);
    }

    public Forward onValidate(Event evt) {
        Forward forward = null;
        String buttonClicked = findButtonClicked(evt);

        if (buttonClicked != null && (save.getAbsoluteName().equals(buttonClicked))) {
            forward = updateService();
        }
        else if (buttonClicked != null && (cancel.getAbsoluteName().equals(buttonClicked))) {
            init();
            forward = new Forward();
            forward.setName(Form.CANCEL_FORM_ACTION);
        }

        return forward;
    }

    private Forward updateService() {
        Forward forward;
        Application app = Application.getInstance();
        EmployeeModule module = (EmployeeModule) app.getModule(EmployeeModule.class);
        DepartmentDataObject service = new DepartmentDataObject();
        service.setServiceCode(codeLabel.getText());
        service.setServiceDesc((String) description.getValue());
        try {
            module.updateServiceClass(service);
            forward = new Forward("editsuccess");
        }
        catch (Exception e) {
            Log.getLog(this.getClass()).error(e.toString());
            forward = new Forward("editfail");
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

    public TextField getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(TextField serviceCode) {
        this.serviceCode = serviceCode;
    }

    public Label getCodeLabel() {
        return codeLabel;
    }

    public void setCodeLabel(Label codeLabel) {
        this.codeLabel = codeLabel;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
