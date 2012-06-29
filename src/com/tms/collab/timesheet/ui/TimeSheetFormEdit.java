package com.tms.collab.timesheet.ui;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.stdui.Button;
import com.tms.collab.timesheet.model.TimeSheetModule;
import com.tms.collab.timesheet.model.TimeSheet;
import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.taskmanager.model.TaskManager;

import java.util.Date;
import java.util.Calendar;


public class TimeSheetFormEdit extends TimeSheetForm {

    private String timesheetId;
    private Button btnCancel;
    private boolean allow;
    private TimeSheet timesheet;

    public void init(){
        super.init();
    }

    public void onRequest(Event ev) {
        allow=true;
        if (timesheetId!=null && !timesheetId.equals("")) {
            super.onRequest(ev);
            TimeSheetModule mod = (TimeSheetModule) Application.getInstance().getModule(TimeSheetModule.class);
            TimeSheet ts = mod.select(timesheetId);
            setTimesheet(ts);
            if (ts.getAdjustmentById()!=null && !ts.getAdjustmentById().equals("")) {
//                removeChildren();
                allow=false;
            }
//            else {
                getTfDate().setDate(ts.getTsDate());
                getSbDuration().setSelectedOption(ts.getDuration()+"");
                getTbDescription().setValue(ts.getDescription());
//            }

            TaskManager manager = (TaskManager)Application.getInstance().getModule(TaskManager.class);
            Task task = new Task();
            try {
                task = manager.getTask(ts.getTaskId());
            }
            catch(Exception e) {

            }
            setTask(task);
        }
        btnCancel = new Button("btnCancel","Cancel");
        addChild(btnCancel);
    }

    public Forward onValidate(Event ev) {
        Forward forward = new Forward();

        if (getBtSubmit().getAbsoluteName().equals(findButtonClicked(ev))) {
            if (timesheetId!=null && !timesheetId.equals("")) {
                TimeSheet ts = new TimeSheet();
                TimeSheetModule mod = (TimeSheetModule)Application.getInstance().getModule(TimeSheetModule.class);
                ts = mod.select(timesheetId);
                String s1 = (String)getSbDuration().getSelectedOptions().keySet().iterator().next();
                Date tsDate = getTfDate().getDate();
                Calendar cal = Calendar.getInstance();
                cal.setTime(tsDate);
                boolean isDate=false;

                if (cal.before(Calendar.getInstance())) {
                    isDate=true;
                }
                if (isDate) {
                    String description = (String)getTbDescription().getValue();
                    ts.setDescription(description);
                    Double dbl = new Double(s1);
                    ts.setDuration(dbl.doubleValue());
                    ts.setTsDate(cal.getTime());
                    mod.update(ts);
                    forward = new Forward("success");
                }
            }
        }
        else if (btnCancel.getAbsoluteName().equals(findButtonClicked(ev))) {
            forward = new Forward("cancel");
        }

        return forward;
    }

    public String getTimesheetId() {
        return timesheetId;
    }

    public void setTimesheetId(String timesheetId) {
        this.timesheetId = timesheetId;
    }

    public String getDefaultTemplate() {
        return "timesheet/tsEDForm";
    }

    public Button getBtnCancel() {
        return btnCancel;
    }

    public void setBtnCancel(Button btnCancel) {
        this.btnCancel = btnCancel;
    }

    public boolean isAllow() {
        return allow;
    }

    public void setAllow(boolean allow) {
        this.allow = allow;
    }

    public TimeSheet getTimesheet() {
        return timesheet;
    }

    public void setTimesheet(TimeSheet timesheet) {
        this.timesheet = timesheet;
    }
}
