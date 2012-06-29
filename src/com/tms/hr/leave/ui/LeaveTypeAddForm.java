package com.tms.hr.leave.ui;

import com.tms.hr.leave.model.LeaveModule;
import com.tms.hr.leave.model.LeaveType;
import kacang.Application;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

public class LeaveTypeAddForm extends Form {

    protected TextField leaveCode;
    protected TextField leaveType;
    protected TextField leaveTypeName;
    protected Radio genderAny;
    protected Radio genderMale;
    protected Radio genderFemale;
    protected CheckBox creditAllowed;
    protected Button submitButton;
    protected Button cancelButton;

    
    public String getDefaultTemplate() {
    	return "leave/leaveTypeSetupAdd";
    }
    
    public LeaveTypeAddForm() {
        super();
    }

    public LeaveTypeAddForm(String name) {
        super(name);
    }

    public void init() {
        initForm();
    }

    public void onRequest(Event evt) {
        initForm();
    }

    public void initForm() {
        removeChildren();
        setMethod("POST");                
        setColumns(2);

        Application app = Application.getInstance();

        leaveCode = new TextField("leaveCode");
        leaveCode.addChild(new ValidatorNotEmpty("v1"));

        leaveType = new TextField("leaveType");
        leaveType.addChild(new ValidatorNotEmpty("v1"));

        leaveTypeName = new TextField("leaveTypeName");
        leaveTypeName.addChild(new ValidatorNotEmpty("v1"));

        genderAny = new Radio("genderAny", app.getMessage("employee.label.genderAny", "Any"));
        genderAny.setGroupName("gender");
        genderAny.setChecked(true);
        genderMale = new Radio("genderMale", app.getMessage("employee.label.genderMale", "Male"));
        genderMale.setGroupName("gender");
        genderFemale = new Radio("genderFemale", app.getMessage("employee.label.genderFemale", "Female"));
        genderFemale.setGroupName("gender");

        creditAllowed = new CheckBox("creditAllowed", app.getMessage("employee.label.creditAllowed", "Credit Allowed"));

        submitButton = new Button("submitButton", app.getMessage("general.label.submit", "Submit"));
        cancelButton = new Button(Form.CANCEL_FORM_ACTION, app.getMessage("general.label.cancel", "Cancel"));

        addChild(new Label("l1", app.getMessage("leave.label.leaveCode", "Leave Code")+" *"));
        addChild(leaveCode);
        addChild(new Label("l2", app.getMessage("leave.label.leaveType", "Leave Type")+" *"));
        addChild(leaveType);
        addChild(new Label("l3", app.getMessage("leave.label.leaveTypeName", "Leave Type")+" *"));
        addChild(leaveTypeName);
        addChild(new Label("l4", app.getMessage("employee.label.gender", "Gender")));
        Panel p1 = new Panel("p1");
        p1.addChild(genderAny);
        p1.addChild(genderMale);
        p1.addChild(genderFemale);
        addChild(p1);
        addChild(new Label("l5", ""));
        addChild(creditAllowed);

        addChild(new Label("l6", ""));
        Panel p2 = new Panel("p2");
        p2.addChild(submitButton);
        p2.addChild(cancelButton);
        addChild(p2);
    }

    public Forward onValidate(Event evt) {
        try {
            Application application = Application.getInstance();
            LeaveModule lm = (LeaveModule)application.getModule(LeaveModule.class);

            LeaveType lt = new LeaveType();
            lt.setId(leaveCode.getValue().toString());
            lt.setLeaveType(leaveType.getValue().toString());
            lt.setName(leaveTypeName.getValue().toString());
            lt.setCreditAllowed(creditAllowed.isChecked());
            if (genderMale.isChecked()) {
                lt.setGender("M");
            }
            else if (genderFemale.isChecked()) {
                lt.setGender("F");
            }
            lm.updateLeaveType(lt);
            return new Forward("success");
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving leave types", e);
            return new Forward("failure");
        }
    }

}
