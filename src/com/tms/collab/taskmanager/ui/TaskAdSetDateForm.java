package com.tms.collab.taskmanager.ui;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.services.security.User;
import kacang.services.security.SecurityService;
import kacang.Application;
import kacang.stdui.DateField;
import kacang.stdui.TimeField;
import kacang.stdui.CheckBox;
import com.tms.collab.taskmanager.model.Assignee;
import com.tms.collab.taskmanager.model.TaskManager;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: Feb 21, 2006
 * Time: 9:55:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class TaskAdSetDateForm extends TaskSetDateForm {

    private String userId;
    private User user;

    private DateField completionDatePicker;
    private TimeField completionTimePicker;
    private CheckBox startCheckBox;
    private CheckBox completionCheckBox;

    public void init() {
        super.init();
        completionDatePicker = new DateField("completionDatePicker");
        completionTimePicker = new TimeField("completionTimePicker");
        startCheckBox = new CheckBox("startCheckBox");
        completionCheckBox = new CheckBox("completionCheckBox");

        addChild(completionDatePicker);
        addChild(completionTimePicker);
        addChild(startCheckBox);
        addChild(completionCheckBox);
    }

    public String getDefaultTemplate() {
        return "/taskmanager/adDateSelectForm";
    }

    public void onRequest(Event ev) {
        super.init();
        completionDatePicker.init();
        completionTimePicker.init();
        startCheckBox.setChecked(false);
        completionCheckBox.setChecked(false);

        if (userId!=null && !userId.equals("")) {
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            try {
                user = service.getUser(userId);
                TaskManager manager = (TaskManager)Application.getInstance().getModule(TaskManager.class);
                Assignee assignee = manager.getAssignee(taskId,userId);
                if (assignee.getStartDate()!=null) {
                    datePicker.setDate(assignee.getStartDate());
                    timePicker.setDate(assignee.getStartDate());
                }
                if (assignee.getCompleteDate()!=null) {
                    completionDatePicker.setDate(assignee.getCompleteDate());
                    completionTimePicker.setDate(assignee.getCompleteDate());
                }
            }
            catch(Exception e) {
                //ignore
            }
        }
    }

    public Forward onValidate(Event ev) {
        Forward forward = new Forward();

        if (btnSubmit.getAbsoluteName().equals(findButtonClicked(ev))) {

            boolean hasChecked=false;
            TaskManager manager = (TaskManager)Application.getInstance().getModule(TaskManager.class);
            Assignee assignee;

            // if startCheckBox checked
            if (startCheckBox.isChecked()) {
                hasChecked=true;
                Calendar startCal = datePicker.getCalendar();
                Calendar startTimeCal = timePicker.getCalendar();
                startCal.set(Calendar.HOUR_OF_DAY,startTimeCal.get(Calendar.HOUR_OF_DAY));
                startCal.set(Calendar.MINUTE,startTimeCal.get(Calendar.MINUTE));

                try {
                    assignee = manager.getAssignee(taskId,userId);
                    if (assignee.getProgress()<=0) {
                        manager.startTask(taskId,userId);
                        assignee = manager.getAssignee(taskId,userId);
                    }
                    assignee.setStartDate(startCal.getTime());
                    assignee.setUpdateBy(getWidgetManager().getUser().getId());
                    assignee.setUpdateDate(new Date());
                    manager.updateAssigneeStartTask(assignee);
                }
                catch(Exception e) {
                    return new Forward(FORWARD_FAIL);
                }

            }

            if (completionCheckBox.isChecked()) {

                hasChecked=true;
                Calendar completionCal = completionDatePicker.getCalendar();
                Calendar completionTimeCal = completionTimePicker.getCalendar();
                completionCal.set(Calendar.HOUR_OF_DAY, completionTimeCal.get(Calendar.HOUR_OF_DAY));
                completionCal.set(Calendar.MINUTE, completionTimeCal.get(Calendar.MINUTE));
                try {
                    assignee = manager.getAssignee(taskId,userId);
                    if (completionCal.getTime().before(assignee.getStartDate())) {
                        return new Forward("INVALID_DATE");
                    }
                    assignee.setCompleteDate(completionCal.getTime());
                    assignee.setCompletedSetBy(getWidgetManager().getUser().getId());
                    assignee.setCompletedDateSetOn(new Date());
                    manager.completeTask(taskId,assignee);
                    //manager.updateAssigneeTaskCompletion(assignee);

                }
                catch(Exception e) {
                    return new Forward(FORWARD_FAIL);
                }
            }

            if (!hasChecked)
                forward=  new Forward("INVALID");
            else
                forward = new Forward(FORWARD_SUCCESS);
        }
        else if (btnCancel.getAbsoluteName().equals(findButtonClicked(ev) )) {
            forward = new Forward(FORWARD_CANCEL);
        }

        return forward;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getUserName() {
        if (user!=null)
            return user.getProperty("firstName") + " " +user.getProperty("lastName");
        else
            return "";
    }

    public DateField getCompletionDatePicker() {
        return completionDatePicker;
    }

    public void setCompletionDatePicker(DateField completionDatePicker) {
        this.completionDatePicker = completionDatePicker;
    }

    public TimeField getCompletionTimePicker() {
        return completionTimePicker;
    }

    public void setCompletionTimePicker(TimeField completionTimePicker) {
        this.completionTimePicker = completionTimePicker;
    }

    public CheckBox getStartCheckBox() {
        return startCheckBox;
    }

    public void setStartCheckBox(CheckBox startCheckBox) {
        this.startCheckBox = startCheckBox;
    }

    public CheckBox getCompletionCheckBox() {
        return completionCheckBox;
    }

    public void setCompletionCheckBox(CheckBox completionCheckBox) {
        this.completionCheckBox = completionCheckBox;
    }
}
