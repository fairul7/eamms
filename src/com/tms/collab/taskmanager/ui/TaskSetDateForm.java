package com.tms.collab.taskmanager.ui;

import kacang.stdui.Form;
import kacang.stdui.DateField;
import kacang.stdui.TimeField;
import kacang.stdui.Button;
import kacang.Application;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.Forward;

import java.util.Date;
import java.util.Calendar;

import com.tms.collab.taskmanager.model.Assignee;
import com.tms.collab.taskmanager.model.TaskManager;
import com.tms.collab.taskmanager.model.TaskMailer;
import com.tms.collab.taskmanager.model.Task;

/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: Feb 20, 2006
 * Time: 3:34:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class TaskSetDateForm extends Form {

    public static final String FORWARD_SUCCESS="success";
    public static final String FORWARD_SUCCESS_START="successStart";
    public static final String FORWARD_CANCEL="cancel";
    public static final String FORWARD_FAIL="fail";
    public static final String DEFAULT_TEMPLATE="/taskmanager/dateSelectForm";

    protected DateField datePicker;
    protected TimeField timePicker;
    protected Button btnSubmit;
    protected Button btnCancel;

    protected String taskId;
    protected String taskEventType;
    protected boolean startTask=true;


    public void init() {
        datePicker = new DateField("datePicker");
        datePicker.init();
        timePicker = new TimeField("timePicker");
        timePicker.init();

        btnSubmit = new Button("btnSubmit", Application.getInstance().getMessage("general.label.submit","Submit"));
        btnCancel = new Button("btnCancel",Application.getInstance().getMessage("general.label.cancel","Cancel"));

        addChild(datePicker);
        addChild(timePicker);
        addChild(btnSubmit);
        addChild(btnCancel);

    }

    public String getDefaultTemplate() {
        return DEFAULT_TEMPLATE;
    }

    public void onRequest(Event ev) {
        init();

        if (taskEventType!=null && !taskEventType.equals("")) {
            startTask=false;
        }
        else {
            startTask=true;
        }
    }

    public Forward onValidate(Event ev) {
        Forward forward = null;

        if (btnCancel.getAbsoluteName().equals(findButtonClicked(ev))) {
            forward = new Forward(FORWARD_CANCEL);
        }
        else if (btnSubmit.getAbsoluteName().equals(findButtonClicked(ev))) {
            Calendar startCal = datePicker.getCalendar();
            Calendar startTimeCal = timePicker.getCalendar();
            startCal.set(Calendar.HOUR_OF_DAY, startTimeCal.get(Calendar.HOUR_OF_DAY));
            startCal.set(Calendar.MINUTE, startTimeCal.get(Calendar.MINUTE));
            Assignee assignee = new Assignee();
            TaskManager manager = (TaskManager)Application.getInstance().getModule(TaskManager.class);
            User user = Application.getInstance().getCurrentUser();
            try {

                if (startTask) {
                    manager.startTask(taskId,user.getId());
                    assignee = manager.getAssignee(taskId,user.getId());
                    assignee.setStartDate(startCal.getTime());
                    assignee.setUpdateBy(user.getId());
                    assignee.setUpdateDate(new Date());
                    manager.updateAssigneeStartTask(assignee);

                    forward = new Forward(FORWARD_SUCCESS_START);
                }
                else {

                    assignee = manager.getAssignee(taskId,user.getId());
                    if (startCal.getTime().before(assignee.getStartDate())) {
                    	return new Forward("INVALID_DATE");
                    }
                    assignee.setCompletedSetBy(user.getId());
                    assignee.setCompletedDateSetOn(new Date());
                    assignee.setCompleteDate(startCal.getTime());
                    manager.completeTask(taskId,assignee);
                    //assignee = manager.getAssignee(taskId,user.getId());
                    //manager.updateAssigneeTaskCompletion(assignee);
                    Task task = manager.getTask(taskId);
                    TaskMailer.sentTaskCompletedNotification(task,ev,user.getId());

                    forward = new Forward(FORWARD_SUCCESS);

                }

            }
            catch(Exception e) {
                forward = new Forward(FORWARD_FAIL);
            }

        }
        return forward;
    }

    public DateField getDatePicker() {
        return datePicker;
    }

    public void setDatePicker(DateField datePicker) {
        this.datePicker = datePicker;
    }

    public TimeField getTimePicker() {
        return timePicker;
    }

    public void setTimePicker(TimeField timePicker) {
        this.timePicker = timePicker;
    }

    public Button getBtnSubmit() {
        return btnSubmit;
    }

    public void setBtnSubmit(Button btnSubmit) {
        this.btnSubmit = btnSubmit;
    }

    public Button getBtnCancel() {
        return btnCancel;
    }

    public void setBtnCancel(Button btnCancel) {
        this.btnCancel = btnCancel;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskEventType() {
        return taskEventType;
    }

    public void setTaskEventType(String taskEventType) {
        this.taskEventType = taskEventType;
    }

    public boolean isStartTask() {
        return startTask;
    }

    public void setStartTask(boolean startTask) {
        this.startTask = startTask;
    }
}
