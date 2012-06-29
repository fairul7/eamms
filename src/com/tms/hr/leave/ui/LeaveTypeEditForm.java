package com.tms.hr.leave.ui;

import com.tms.hr.leave.model.LeaveModule;
import com.tms.hr.leave.model.LeaveType;
import kacang.Application;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

public class LeaveTypeEditForm extends LeaveTypeAddForm {

    protected Label leaveCode;
    protected Label leaveType;
    protected Label fixedCalendar;
    protected CheckBox fixedCalendarCheck;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LeaveTypeEditForm() {
        super();
    }

    public LeaveTypeEditForm(String name) {
        super(name);
    }

    public void initForm() {
        removeChildren();
        setMethod("POST");                
        setColumns(2);

        Application app = Application.getInstance();


        fixedCalendar = new Label("fixedCalendar",app.getMessage("employee.label.fixedCalendar","Fixed Calendar Calculation"));
        fixedCalendarCheck = new CheckBox("fixedCalendarCheck");
        // fields
        leaveCode = new Label("leaveCode");
        leaveType = new Label("leaveType");

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

        submitButton = new Button("submitButton", app.getMessage("general.label.update", "Update"));
        cancelButton = new Button(Form.CANCEL_FORM_ACTION, app.getMessage("general.label.cancel", "Cancel"));

        addChild(new Label("l1", app.getMessage("leave.label.leaveCode", "Leave Code")));
        addChild(leaveCode);
        addChild(new Label("l2", app.getMessage("leave.label.leaveType", "Leave Type")));
        addChild(leaveType);
        addChild(new Label("l3", app.getMessage("leave.label.leaveTypeName", "Leave Type")));
        addChild(leaveTypeName);
        addChild(new Label("l4", app.getMessage("employee.label.gender", "Gender")));
        Panel p1 = new Panel("p1");
        p1.addChild(genderAny);
        p1.addChild(genderMale);
        p1.addChild(genderFemale);
        addChild(p1);
        addChild(new Label("l5", ""));
        addChild(creditAllowed);




        addChild(fixedCalendar);
        addChild(fixedCalendarCheck);
        addChild(new Label("l6", ""));
        Panel p2 = new Panel("p2");
        p2.addChild(submitButton);
        p2.addChild(cancelButton);
        addChild(p2);

        String id = getId();
        if (id != null) {
            // load object
            try {
                LeaveModule lm = (LeaveModule)app.getModule(LeaveModule.class);
                LeaveType lt = lm.viewLeaveType(id);
                leaveCode.setText(lt.getId());
                leaveType.setText(lt.getLeaveType());
                leaveTypeName.setValue(lt.getName());
                creditAllowed.setChecked(lt.isCreditAllowed());
                if ("M".equals(lt.getGender())) {
                    genderMale.setChecked(true);
                }
                else if ("F".equals(lt.getGender())) {
                    genderFemale.setChecked(true);
                }
                else {
                    genderAny.setChecked(true);
                }

            fixedCalendarCheck.setChecked(lt.getFixedCalendar().equals("1") ? true : false);
            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Error retrieving leave type " + getId(), e);
            }
        }



    }

    public Forward onValidate(Event evt) {
        try {
            Application application = Application.getInstance();
            LeaveModule lm = (LeaveModule)application.getModule(LeaveModule.class);

            String id = getId();
            LeaveType lt = lm.viewLeaveType(id);
            lt.setName(leaveTypeName.getValue().toString());
            lt.setCreditAllowed(creditAllowed.isChecked());
            if (genderMale.isChecked()) {
                lt.setGender("M");
            }
            else if (genderFemale.isChecked()) {
                lt.setGender("F");
            }else {
                lt.setGender(null);
            }


            lt.setFixedCalendar(fixedCalendarCheck.isChecked()== true ? "1": "0");


            lm.updateLeaveType(lt);
            return new Forward("success");
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving leave types", e);
            return new Forward("failure");
        }
    }

}
