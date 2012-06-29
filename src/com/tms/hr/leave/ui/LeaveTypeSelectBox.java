package com.tms.hr.leave.ui;

import kacang.stdui.SelectBox;
import kacang.ui.Event;
import kacang.util.Log;
import kacang.Application;

import java.util.Collection;

import com.tms.hr.leave.model.LeaveType;
import com.tms.hr.leave.model.LeaveModule;

public class LeaveTypeSelectBox extends SelectBox {

    public LeaveTypeSelectBox() {
        super();
    }

    public LeaveTypeSelectBox(String name) {
        super(name);
    }

    public void onRequest(Event evt) {
        try {
            Application application = Application.getInstance();
            LeaveModule lm = (LeaveModule)application.getModule(LeaveModule.class);
            Collection leaveTypes = lm.viewLeaveTypeList(null, false, true);
            setOptions(leaveTypes, "leaveType", "leaveType");
            if (leaveTypes.size() > 0 && (getSelectedOptions() == null || getSelectedOptions().size() == 0)) {
                setSelectedOption(((LeaveType)leaveTypes.iterator().next()).getLeaveType());
            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving leave types", e);
        }
    }
    
}
