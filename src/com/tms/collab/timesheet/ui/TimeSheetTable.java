package com.tms.collab.timesheet.ui;

import kacang.stdui.*;
import kacang.Application;
import kacang.util.Log;
import kacang.ui.Event;

import java.util.Collection;
import java.util.List;

import com.tms.collab.timesheet.model.TimeSheetModule;
import com.tms.collab.taskmanager.model.TaskManager;
import com.tms.collab.taskmanager.model.Task;

/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: Apr 22, 2005
 * Time: 3:52:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimeSheetTable extends Table {

    protected String uid;
    protected String pid;
    protected String tid;

    public void onRequest(Event ev) {
        if ((pid==null||pid.equals(""))&&(tid==null||tid.equals(""))&&(uid==null||uid.equals(""))) {
            uid = getWidgetManager().getUser().getId();
        }
        super.onRequest(ev);        
    }

    public void init() {
        setModel(new TimeSheetTableModel());
    }

    public class TimeSheetTableModel extends TableModel{

        public TimeSheetTableModel() {
            TableColumn tcDate = new TableColumn("tsDate",Application.getInstance().getMessage("timesheet.label.date","Date"));
            tcDate.setUrlParam("id");
            TableDateFormat dateFormat = new TableDateFormat("dd MMM yyyy");
            tcDate.setFormat(dateFormat);
            TableColumn tcDuration = new TableColumn("duration",Application.getInstance().getMessage("timesheet.label.duration","Duration"));
            TableColumn tcDescription = new TableColumn("description",Application.getInstance().getMessage("timesheet.label.description","Description"));
            TableColumn tcAdjustedDuration = new TableColumn("adjustedDuration",Application.getInstance().getMessage("timesheet.label.adjustedduration","Adjusted Duration"));
            TableColumn tcAdjustmentBy = new TableColumn("adjustmentBy",Application.getInstance().getMessage("timesheet.label.adjustmentby","Adjustment By"));

            addColumn(tcDate);
            addColumn(tcDuration);
            addColumn(tcDescription);
            addColumn(tcAdjustedDuration);
            addColumn(tcAdjustmentBy);

            //add filter
            TimeSheetModule mod = (TimeSheetModule)Application.getInstance().getModule(TimeSheetModule.class);
            if (pid!=null && !pid.equals("")) {
                SelectBox sbTask = new SelectBox("sbtask");
                Collection col = mod.getTaskListByProject(pid,uid);
                if (col.size()>0) {
                    try {
                        TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
                        while(col.iterator().hasNext()) {
                            Task t = tm.getTask((String)col.iterator().next());
                            sbTask.addOption(t.getId(),t.getTitle());
                        }
                        TableFilter tf = new TableFilter("taskFilter");
                        tf.setWidget(sbTask);
                        addFilter(tf);
                    }
                    catch(Exception e) {
                        Log.getLog(getClass()).error("Error "+e.toString());
                    }
                }
            }
            else {
                SelectBox sbTask = new SelectBox("sbtask");
                Collection col = mod.getTaskListByUser(uid);
                if (col.size()>0) {
                    try {
                        TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
                        while(col.iterator().hasNext()) {
                            Task t = tm.getTask((String)col.iterator().next());
                            sbTask.addOption(t.getId(),t.getTitle());
                        }
                        TableFilter tf = new TableFilter("taskFilter");
                        tf.setWidget(sbTask);
                        addFilter(tf);
                    }
                    catch(Exception e) {
                        Log.getLog(getClass()).error("Error "+e.toString());
                    }
                }
            }

        }

        public Collection getTableRows() {
            List list = (List)getFilterValue("taskFilter");
            if (list!=null && list.size()>0) {
                tid = (String)list.iterator().next();
            }

            TimeSheetModule mod = (TimeSheetModule)Application.getInstance().getModule(TimeSheetModule.class);
            return mod.list(uid,pid,tid);
        }

        public int getTotalRowCount() {
            List list = (List)getFilterValue("taskFilter");
            if (list!=null && list.size()>0) {
                tid = (String)list.iterator().next();
            }

            TimeSheetModule mod = (TimeSheetModule)Application.getInstance().getModule(TimeSheetModule.class);
            return mod.listCount(uid,pid,tid);
        }

        public String getTableRowKey() {
            return "id";
        }
    }
}
