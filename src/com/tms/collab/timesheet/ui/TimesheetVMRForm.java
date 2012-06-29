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
import com.tms.collab.timesheet.model.TimeSheetModule;

public class TimesheetVMRForm extends Form {    
    private String userId;
    private boolean rights=false;
    private static final String TIMESHEET_PERMISSION="com.tms.collab.timesheet.ViewProjects";
    protected SelectBox sbMonthlyReport;
    protected Button btMonthlyReport;
    private static final String VIEW_MONTHLY_PROJECT="viewMonthlyProject";
    private static final String VIEW_MONTHLY_USER="viewMonthlyUser";
    private static final String VIEW_MONTHLY_USER_PROJECT="viewUserProject";

    private boolean viewPermission=false;

    public String getDefaultTemplate() {
        return "timesheet/tsvmrform";
    }

    public void onRequest(Event ev) {
        userId = getWidgetManager().getUser().getId();
        try {
            SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
            boolean hasPermission = security.hasPermission(userId, TIMESHEET_PERMISSION, TimeSheetModule.class.getName(), null);
            if (hasPermission) {
                viewPermission=true;    
                rights=true;
            }
            else {
                viewPermission=false;
                WormsHandler handler = (WormsHandler)Application.getInstance().getModule(WormsHandler.class);
                Collection col=handler.getProjectsByOwner(userId);
                if (col!=null && col.size()>0) {
                    rights=true;
                }
            }           
        }
        catch(Exception e) {

        }      
        //set up for monthly report
        sbMonthlyReport = new SelectBox("sbmonthlyreport");
        sbMonthlyReport.addOption("",Application.getInstance().getMessage("security.label.pleaseSelect","---Please Select---"));
        sbMonthlyReport.addOption("project",Application.getInstance().getMessage("timesheet.label.viewproject"));
        if (viewPermission)  {
            sbMonthlyReport.addOption("user",Application.getInstance().getMessage("timesheet.label.viewuser"));
            sbMonthlyReport.addOption("userProject",Application.getInstance().getMessage("timesheet.label.viewuserproject"));
        }
        btMonthlyReport = new Button("btmonthlyreport",Application.getInstance().getMessage("timesheet.label.viewmonthlyreport"));
        addChild(sbMonthlyReport);
        addChild(btMonthlyReport);
               
    }

    public void init() {
        super.init();
    }

    public Forward monthlyReportButtonClicked(Event ev) {
        String sSelected = (String)sbMonthlyReport.getSelectedOptions().keySet().iterator().next();
        if (sSelected!=null && !sSelected.equals("")) {
            if (sSelected.equals("project")) {
                return new Forward(VIEW_MONTHLY_PROJECT);
            }
            else if (sSelected.equals("user")) {
                return new Forward(VIEW_MONTHLY_USER);
            }
            else if (sSelected.equals("userProject")) {
                return new Forward(VIEW_MONTHLY_USER_PROJECT);
            }
        }
        else {
            return new Forward("selectMonthlyReport");
        }
        return null;
    }

    public Forward onValidate(Event ev) {
        if (btMonthlyReport.getAbsoluteName().equals(findButtonClicked(ev))) {
            return monthlyReportButtonClicked(ev);
        }
        
        return null;
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

    public void setSbMonthlyReport(SelectBox sbMonthlyReport) {
        this.sbMonthlyReport = sbMonthlyReport;
    }

    public SelectBox getSbMonthlyReport() {
        return sbMonthlyReport;
    }

    public void setBtMonthlyReport(Button btMonthlyReport) {
        this.btMonthlyReport = btMonthlyReport;
    }

    public Button getBtMonthlyReport() {
        return btMonthlyReport;
    }

    public boolean isViewPermission() {
        return viewPermission;
    }

    public void setViewPermission(boolean viewPermission) {
        this.viewPermission = viewPermission;
    }
}

