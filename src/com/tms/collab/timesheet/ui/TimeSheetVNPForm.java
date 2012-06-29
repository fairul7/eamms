package com.tms.collab.timesheet.ui;

import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.services.security.SecurityService;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.SelectBox;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.collab.project.Project;
import com.tms.collab.project.WormsHandler;
import com.tms.collab.taskmanager.model.TaskCategory;
import com.tms.collab.timesheet.model.TimeSheetModule;

public class TimeSheetVNPForm extends Form {
    private SelectBox sbProject;
    private Button btViewProject;
    private Button btViewTask;
    private Button btMandaysReport;
    private String userId;
    private boolean rights=false;
    private Collection col;
    private String projectId;
    private static final String TIMESHEET_PERMISSION="com.tms.collab.timesheet.ViewProjects";


    private boolean viewPermission=false;

    public String getDefaultTemplate() {
        return "timesheet/tsvnpform";
    }

    public void onRequest(Event ev) {
        userId = getWidgetManager().getUser().getId();
        WormsHandler handler = (WormsHandler)Application.getInstance().getModule(WormsHandler.class);
        try {
            SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
            boolean hasPermission = security.hasPermission(userId, TIMESHEET_PERMISSION, TimeSheetModule.class.getName(), null);
            if (hasPermission) {
                viewPermission=true;
                rights=true;
                DaoQuery query=new DaoQuery();
                query.addProperty(new OperatorEquals("archived","0",DaoOperator.OPERATOR_AND));
                col = handler.getNonProjects(query,0,-1,"name",false);
            }
            else {
                viewPermission=false;
                col = handler.getNonProjectsByOwner(userId);
            }
            if (col!=null && col.size()>0) {
                rights=true;
            }
        }
        catch(Exception e) {

        }

        sbProject = new SelectBox("sbproject");
        sbProject.addOption("",Application.getInstance().getMessage("security.label.pleaseSelect","---Please Select---"));
        if (rights) {
            for (Iterator i=col.iterator();i.hasNext();) {
            	TaskCategory tc = (TaskCategory)i.next();
                sbProject.addOption(tc.getId(),tc.getName());
            }
        }
        btViewProject = new Button("btviewproject",Application.getInstance().getMessage("timesheet.label.viewalltask","View All Tasks"));
        btViewTask = new Button("btviewtask",Application.getInstance().getMessage("timesheet.label.viewindividualtask","View Individual Task"));
        addChild(sbProject);
        addChild(btViewProject);
        addChild(btViewTask);

      
        btMandaysReport = new Button("btMandaysReport",Application.getInstance().getMessage("timesheet.label.viewMandaysReport", "View Mandays Report"));
        addChild(btMandaysReport);
    }

    public void init() {
        super.init();
    }

   
    public Forward onValidate(Event ev) {
        if(btMandaysReport.getAbsoluteName().equals(findButtonClicked(ev))) {
        	projectId = (String)sbProject.getSelectedOptions().keySet().iterator().next();
        	if (projectId!=null && !projectId.equals("")) {
                return new Forward("view mandays report");
            }
            else {
                return new Forward("select");
            }
        }
        else {
            projectId = (String)sbProject.getSelectedOptions().keySet().iterator().next();
            if (projectId!=null && !projectId.equals("")) {
                if (btViewProject.getAbsoluteName().equals(findButtonClicked(ev))) {
                    return new Forward("project");
                }
                if (btViewTask.getAbsoluteName().equals(findButtonClicked(ev))) {
                    return new Forward("task");
                }
            }
            else {
                return new Forward("select");
            }
        }
        return null;
    }

    public SelectBox getSbProject() {
        return sbProject;
    }

    public void setSbProject(SelectBox sbProject) {
        this.sbProject = sbProject;
    }

    public Button getBtViewProject() {
        return btViewProject;
    }

    public void setBtViewProject(Button btViewProject) {
        this.btViewProject = btViewProject;
    }

    public Button getBtViewTask() {
        return btViewTask;
    }

    public void setBtViewTask(Button btViewTask) {
        this.btViewTask = btViewTask;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isRights() {
        return rights;
    }

    public void setRights(boolean rights) {
        this.rights = rights;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public boolean isViewPermission() {
        return viewPermission;
    }

    public void setViewPermission(boolean viewPermission) {
        this.viewPermission = viewPermission;
    }

	public Button getBtMandaysReport() {
		return btMandaysReport;
	}
}
