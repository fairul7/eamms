package com.tms.collab.timesheet.ui;

import kacang.stdui.Form;
import kacang.stdui.Button;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.util.Log;
import com.tms.collab.timesheet.model.TimeSheet;
import com.tms.collab.timesheet.model.TimeSheetModule;
import com.tms.collab.project.Project;
import com.tms.collab.project.WormsHandler;
import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.taskmanager.model.TaskManager;

/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: Apr 22, 2005
 * Time: 4:22:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimeSheetDet extends Form {
    protected TimeSheet ts;
    protected Project project;
    protected String projectName;
    protected Task task;
    protected String id;
    protected Button btClose;
    protected Button btEdit;
    protected Button btAdjust;

    protected boolean assigner=false;

    public String getDefaultTemplate() {
        return "timesheet/tsdet";
    }

    public void onRequest(Event ev) {
        TimeSheetModule mod = (TimeSheetModule)Application.getInstance().getModule(TimeSheetModule.class);
        ts = mod.select(id);
        try {
            if (ts.getProjectId()!=null && !ts.getProjectId().equals("")) {
                WormsHandler handler = (WormsHandler)Application.getInstance().getModule(WormsHandler.class);
                project = handler.getProject(ts.getProjectId());
                projectName = project.getProjectName();
            }
            TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
            task = tm.getTask(ts.getTaskId());
            // check for whether the user can adjust the timesheet
            if (task.getAssignerId().equals(getWidgetManager().getUser().getId())) {
                assigner=true;
            }
        }
        catch(Exception e) {
            Log.getLog(getClass()).error("error in TimeSheetDet "+e.toString());
        }
        initForm();
    }

    public Forward onValidate(Event ev) {
       if (btEdit.getAbsoluteName().equals(findButtonClicked(ev))) {
           return new Forward("edit");
       }
        if (btAdjust.getAbsoluteName().equals(findButtonClicked(ev))) {
            return new Forward("adjust");
        }
        if (btClose.getAbsoluteName().equals(findButtonClicked(ev))) {
            return new Forward("close");
        }
        return super.onValidate(ev);
    }

    public void init() {
        initForm();
    }

    public void initForm() {
        removeChildren();
        btClose = new Button("Close", Application.getInstance().getMessage("timesheet.label.close","Close"));
        btEdit = new Button("Edit",Application.getInstance().getMessage("timesheet.label.edit","Edit"));
        btAdjust = new Button("Adjust",Application.getInstance().getMessage("timesheet.label.adjust","Adjustment"));
        addChild(btClose);
        addChild(btEdit);
        addChild(btAdjust);
    }

    public TimeSheet getTs() {
        return ts;
    }

    public void setTs(TimeSheet ts) {
        this.ts = ts;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Button getBtClose() {
        return btClose;
    }

    public void setBtClose(Button btCancel) {
        this.btClose = btCancel;
    }

    public Button getBtEdit() {
        return btEdit;
    }

    public void setBtEdit(Button btEdit) {
        this.btEdit = btEdit;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public void setProjectName(String projectName) {
        this.projectName=projectName;
    }

    public String getProjectName() {
        return projectName;
    }

    public Button getBtAdjust() {
        return btAdjust;
    }

    public void setBtAdjust(Button btAdjust) {
        this.btAdjust = btAdjust;
    }

    public boolean isAssigner() {
        return assigner;
    }

    public void setAssigner(boolean assigner) {
        this.assigner = assigner;
    }
}
