package com.tms.collab.timesheet.ui;

import kacang.stdui.*;
//import kacang.stdui.validator.ValidatorNotEmpty;
//import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.util.Log;
import com.tms.collab.timesheet.model.TimeSheet;
import com.tms.collab.timesheet.model.TimeSheetModule;
import com.tms.collab.timesheet.TimeSheetUtil;
import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.taskmanager.model.TaskManager;
import com.tms.collab.project.WormsHandler;
import com.tms.collab.project.Project;

import java.util.Map;

import org.apache.commons.collections.SequencedHashMap;



/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: Apr 22, 2005
 * Time: 5:08:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimeSheetAdjForm extends Form {

    private String id;
    private TextBox tbDescription;
    private SelectBox sbDuration;
    private Panel panel;
    private Button btSubmit;
    private Button btCancel;

    private TimeSheet ts;
    private Task task;
    private String projectName;

    private boolean adjust=false;

    public void onRequest(Event ev) {
        TimeSheetModule mod = (TimeSheetModule)Application.getInstance().getModule(TimeSheetModule.class);
        ts = mod.select(id);

        try {
            TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
            task = tm.getTask(ts.getTaskId());
            if (ts.getProjectId()!=null && !ts.getProjectId().equals("")) {
                WormsHandler handler = (WormsHandler)Application.getInstance().getModule(WormsHandler.class);
				Project project = handler.getProject(ts.getProjectId());
				if (project != null) {
                	projectName = project.getProjectName();
				} else {
					projectName = "";
				}
            }
        }
        catch(Exception e) {
            Log.getLog(getClass()).error("Error in TimeSheetAdjForm "+e.toString(), e);
        }

        adjust = checkAdjustmentPermission();

        initForm();
    }

    public void init() {
    	setMethod("POST");
        super.init();
    }

    public Forward onValidate(Event ev) {
        String s1 = (String)sbDuration.getSelectedOptions().keySet().iterator().next();

        if (s1!=null && !s1.equals("")) {
            TimeSheetModule mod = (TimeSheetModule)Application.getInstance().getModule(TimeSheetModule.class);
            ts = mod.select(id);

            Double dbl = new Double(s1);
            double db = dbl.doubleValue();
            ts.setAdjustedDuration(db);
            ts.setAdjustmentBy(getWidgetManager().getUser().getName());
            ts.setAdjustmentById(getWidgetManager().getUser().getId());
            ts.setAdjustmentDescription((String)tbDescription.getValue());
            ts.setAdjustment("Y");
        
            boolean bUpdate = mod.updateAdjustment(ts);
            if (bUpdate) {
                return new Forward("updated");
            }
            else {
                return new Forward("fail");
            }
        }
        else {
            return new Forward("select");
        }
    }

    public void initForm() {
        setColumns(2);

        sbDuration = new SelectBox("sbduration");
        sbDuration.setMultiple(false);
        Map map = new SequencedHashMap();
        map.put("",Application.getInstance().getMessage("security.label.pleaseSelect","---Please Select---"));
        //for (int i=0; i<TimeSheetUtil.WORKING_HOUR_PER_DAY; i++) {
        map.put("0","0");
        for (int i=0; i<TimeSheetUtil.getTimeSheetWorkingHour(); i++) {
            map.put(""+(i+0.5),""+(i+0.5));
            map.put(""+(i+1),""+(i+1));
        }
        sbDuration.setOptionMap(map);
        addChild(sbDuration);

        tbDescription = new TextBox("tbdescription");
        addChild(tbDescription);

        btSubmit = new Button("submit",Application.getInstance().getMessage("timesheet.label.submit","Submit"));
        btCancel = new Button(Form.CANCEL_FORM_ACTION,Application.getInstance().getMessage("timesheet.label.cancel","Cancel"));
        panel = new Panel("pn");
        panel.addChild(btSubmit);
        panel.addChild(btCancel);
        addChild(new Label("lb70",""));
        addChild(panel);
    }

    public boolean checkAdjustmentPermission() {
        boolean bRet = true;
        String uid = getWidgetManager().getUser().getId();

        // check for assigner id
        // if current userid != assigner id, return false
        if (!uid.equals(task.getAssignerId())) {
            bRet = false;
        }
        return bRet;
    }

    public String getDefaultTemplate() {
        return "timesheet/tsadjform";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TextBox getTbDescription() {
        return tbDescription;
    }

    public void setTbDescription(TextBox tbDescription) {
        this.tbDescription = tbDescription;
    }

    public SelectBox getSbDuration() {
        return sbDuration;
    }

    public void setSbDuration(SelectBox sbDuration) {
        this.sbDuration = sbDuration;
    }

    public Button getBtSubmit() {
        return btSubmit;
    }

    public void setBtSubmit(Button btSubmit) {
        this.btSubmit = btSubmit;
    }

    public Button getBtCancel() {
        return btCancel;
    }

    public void setBtCancel(Button btCancel) {
        this.btCancel = btCancel;
    }

    public TimeSheet getTs() {
        return ts;
    }

    public void setTs(TimeSheet ts) {
        this.ts = ts;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Panel getPanel() {
        return panel;
    }

    public void setPanel(Panel panel) {
        this.panel = panel;
    }

    public boolean isAdjust() {
        return adjust;
    }

    public void setAdjust(boolean adjust) {
        this.adjust = adjust;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
