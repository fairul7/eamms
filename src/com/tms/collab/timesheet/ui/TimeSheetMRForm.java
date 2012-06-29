package com.tms.collab.timesheet.ui;

import kacang.stdui.Form;
import kacang.stdui.SelectBox;
import kacang.stdui.Button;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.DaoOperator;
import kacang.services.security.SecurityService;
import kacang.services.security.User;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Collection;

import com.tms.collab.timesheet.TimeSheetUtil;
import com.tms.collab.timesheet.model.TimeSheetModule;
import com.tms.collab.project.WormsHandler;
import com.tms.collab.project.Project;

/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: May 24, 2005
 * Time: 10:51:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class TimeSheetMRForm extends Form {

    protected SelectBox sbMonth;
    protected SelectBox sbProject;
    protected SelectBox sbUser;
    protected Button btViewReport;

    protected String type;

    private String userId;
    private boolean rights=false;
    private Collection col;
    private static final String TIMESHEET_PERMISSION="com.tms.collab.timesheet.ViewProjects";

    protected String selectedProjectId="";
    protected String selectedUserId="";
    protected String selectedMonth="";

    public String getDefaultTemplate() {
        return "timesheet/tsmrform";
    }

    public void initForm() {

        sbMonth = new SelectBox("sbmonth");
        sbProject = new SelectBox("sbproject");
        sbUser = new SelectBox("sbuser");
        btViewReport = new Button("btviewreport",Application.getInstance().getMessage("timesheet.label.viewmonthlyreport"));
        setUpMonth();
        if (type!=null && !type.equals("")) {
            if (type.equals("project")) {
                setUpProject();
            }
            else if (type.equals("user")) {
                setUpUser();
            }
        }
        addChild(sbMonth);
        addChild(sbProject);
        addChild(sbUser);
        addChild(btViewReport);

    }

    public void setUpMonth() {
        sbMonth.addOption("",Application.getInstance().getMessage("security.label.pleaseSelect","---Please Select---"));
        Calendar calendar = Calendar.getInstance();

        int iMonth = calendar.get(Calendar.MONTH);
        int iYear = calendar.get(Calendar.YEAR);

        for (int i=iMonth;i>=0;i--) {
            String year = ""+iYear;
            sbMonth.addOption((i)+","+year,TimeSheetUtil.getMonthDescription(i+1)+","+year);
        }

        for (int i=11;i>iMonth;i--) {
            String year = ""+(iYear-1);
            sbMonth.addOption((i)+","+year,TimeSheetUtil.getMonthDescription(i+1)+","+year);
        }

/*
        for (int i=0;i<12;i++)  {

            //int iMonth = calendar.get(Calendar.MONTH);
            //int iYear = calendar.get(Calendar.YEAR);
            if (iMonth<(i)) {
                String year = ""+(iYear-1);
                sbMonth.addOption((i)+","+year,TimeSheetUtil.getMonthDescription(i+1)+","+year);
            }
            else {
                String year = ""+iYear;
                sbMonth.addOption((i)+","+year,TimeSheetUtil.getMonthDescription(i+1)+","+year);
            }
        }
*/
    }

    public void setUpProject() {
        userId = getWidgetManager().getUser().getId();
        WormsHandler handler = (WormsHandler)Application.getInstance().getModule(WormsHandler.class);
        try {
            SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
            boolean hasPermission = security.hasPermission(userId, TIMESHEET_PERMISSION, TimeSheetModule.class.getName(), null);
            if (hasPermission) {
                DaoQuery query = new DaoQuery();
                query.addProperty(new OperatorEquals("archived","0",DaoOperator.OPERATOR_AND));
                col = handler.getProjects(query,0,-1,"projectName",false);
            }
            else {
                col = handler.getProjectsByOwner(userId);
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
                Project p = (Project)i.next();
                sbProject.addOption(p.getProjectId(),p.getProjectName());
            }
        }
    }

    public void setUpUser() {
        sbUser.addOption("",Application.getInstance().getMessage("security.label.pleaseSelect","---Please Select---"));
        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
        DaoQuery properties = new DaoQuery();
        properties.addProperty(new OperatorEquals("u.active", "1", DaoOperator.OPERATOR_AND));
        try {
            Collection colUser = service.getUsers(properties,0,-1,"firstName",false);
            if (colUser!=null && colUser.size()>0) {
                for (Iterator i=colUser.iterator();i.hasNext();) {
                    User user = (User)i.next();
                    sbUser.addOption(user.getId(),user.getName());
                }
            }
        }
        catch(Exception e) {

        }
    }

    public void init() {
        super.init();
    }

    public Forward onValidate(Event ev) {
        selectedProjectId="";
        selectedUserId="";
        if (btViewReport.getAbsoluteName().equals(findButtonClicked(ev))) {
            try {
                selectedProjectId = (String)sbProject.getSelectedOptions().keySet().iterator().next();
            }
            catch(Exception e){}

            try {
                selectedUserId = (String)sbUser.getSelectedOptions().keySet().iterator().next();
            }
            catch(Exception e) {}

            try {
                selectedMonth = (String)sbMonth.getSelectedOptions().keySet().iterator().next();
            }
            catch(Exception e) {}

            if (selectedMonth==null||selectedMonth.equals("")) {
                return new Forward("selectMonth");
            }
            
            if (selectedProjectId!=null && !selectedProjectId.equals("")) {
                return new Forward("project");
            }
            else if (selectedUserId!=null && !selectedUserId.equals("")) {
                return new Forward("user");
            }
            else {
                if (type.equals("project")) {
                    return new Forward("selectProject");
                }
                else {
                    return new Forward("selectUser");
                }
            }
        }
        return null;
    }

    public SelectBox getSbMonth() {
        return sbMonth;
    }

    public void setSbMonth(SelectBox sbMonth) {
        this.sbMonth = sbMonth;
    }

    public SelectBox getSbProject() {
        return sbProject;
    }

    public void setSbProject(SelectBox sbProject) {
        this.sbProject = sbProject;
    }

    public SelectBox getSbUser() {
        return sbUser;
    }

    public void setSbUser(SelectBox sbUser) {
        this.sbUser = sbUser;
    }

    public Button getBtViewReport() {
        return btViewReport;
    }

    public void setBtViewReport(Button btViewReport) {
        this.btViewReport = btViewReport;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        initForm();
    }

    public String getSelectedProjectId() {
        return selectedProjectId;
    }

    public void setSelectedProjectId(String selectedProjectId) {
        this.selectedProjectId = selectedProjectId;
    }

    public String getSelectedUserId() {
        return selectedUserId;
    }

    public void setSelectedUserId(String selectedUserId) {
        this.selectedUserId = selectedUserId;
    }

    public String getSelectedMonth() {
        return selectedMonth;
    }

    public void setSelectedMonth(String selectedMonth) {
        this.selectedMonth = selectedMonth;
    }


}
