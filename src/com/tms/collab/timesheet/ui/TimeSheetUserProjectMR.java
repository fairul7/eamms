package com.tms.collab.timesheet.ui;

import kacang.ui.Widget;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.services.security.User;
import kacang.services.security.SecurityService;
import kacang.stdui.ComboSelectBox;
import kacang.stdui.SelectBox;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.DaoOperator;

import java.util.*;

import com.tms.collab.project.Project;
import com.tms.collab.project.WormsHandler;
import com.tms.collab.timesheet.TimeSheetUtil;
import com.tms.collab.timesheet.model.TimeSheetModule;
import com.tms.collab.timesheet.model.UserProjectReport;
import com.tms.collab.taskmanager.model.TaskManager;
import com.tms.collab.taskmanager.model.TaskCategory;
import org.apache.commons.collections.SequencedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: Feb 28, 2006
 * Time: 4:49:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimeSheetUserProjectMR extends Form {

    protected String month;
    protected String year;
    protected int startDay;
    protected int endDay;
    protected int[] dayList;
    protected String[] userIds;
    protected Date startDate;
    protected Date endDate;
    protected double[] grandTotal;

    protected UserProjectReport[] reportDetails;

    // UI
    protected ComboSelectBox comboBox;
    protected SelectBox monthSelect;
    protected Button btnSubmit;
    protected Button btnCancel;
    protected Button btnPrint;
    protected String actionType;

    public void init() {
        setMethod("post");
        setupUI();
    }

    public void onRequest(Event ev) {
        setupUI();
    }

    public String getDefaultTemplate() {
        return "/timesheet/upReport";
    }

    public Forward onValidate(Event ev) {
        Forward forward = new Forward();

        if (btnSubmit.getAbsoluteName().equals(findButtonClicked(ev))) {
            forward = setupReport(ev);
        }
        else if (btnCancel.getAbsoluteName().equals(findButtonClicked(ev))) {
            forward = new Forward("cancel");
        }
        else if (btnPrint.getAbsoluteName().equals(findButtonClicked(ev))) {
            forward = new Forward("print");
        }
        return forward;
    }

    public Forward setupReport(Event ev) {
        String strMonth = (String)monthSelect.getSelectedOptions().keySet().iterator().next();
        if (strMonth==null || strMonth.equals("")) {
            return new Forward("invalidMonth");
        }
        else {
            getMonthDetails(strMonth);
        }

        Map userIdList = comboBox.getRightValues();
        if (userIdList.size()>0) {
            userIds = new String[userIdList.size()];
            int iCounter=0;
            for (Iterator i = userIdList.keySet().iterator();i.hasNext();) {
                userIds[iCounter]=(String)i.next();
                iCounter++;
            }
        }
        else {
            return new Forward("invalidUsers");
        }

        if (userIds!=null && userIds.length>0) {
            actionType="report";
            // start grab details from db
            reportDetails = new UserProjectReport[userIds.length];
            grandTotal = new double[userIds.length];
            SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
            TimeSheetModule mod = (TimeSheetModule)Application.getInstance().getModule(TimeSheetModule.class);
            TaskManager manager = (TaskManager)Application.getInstance().getModule(TaskManager.class);
            WormsHandler handler = (WormsHandler)Application.getInstance().getModule(WormsHandler.class);

            for (int i=0; i<userIds.length; i++) {
                reportDetails[i] = new UserProjectReport();
                grandTotal[i]=0;
                try {
                    reportDetails[i].user = ss.getUser(userIds[i]);
                }catch(Exception e) {
                    //ignore
                }

                Collection projectList = mod.getProjectListForUser(userIds[i],startDate,endDate);
                if (projectList!=null && projectList.size()>0) {

                    int j=-1;
                    reportDetails[i].projects = new Project[projectList.size()];
                    reportDetails[i].totalDetails = new ArrayList();
                    reportDetails[i].projectTotal = new double[projectList.size()];
                    reportDetails[i].dailyTotal = new double[endDay];
                    boolean start=true;

                    for (Iterator iterator=projectList.iterator();iterator.hasNext();) {
                        j++;
                        HashMap map = (HashMap)iterator.next();
                        String s = (String)map.get("projectId");
                        try {
                            Project project = handler.getProject(s);
                            if(project != null)
                                reportDetails[i].projects[j] = handler.getProject(s);
                            else
                            {
                                reportDetails[i].projects[j] = new Project();
                                TaskCategory category = manager.getCategory(s);
                                reportDetails[i].projects[j].setProjectId(category.getId());
                                reportDetails[i].projects[j].setProjectName(category.getName());
                            }
                        }
                        catch(Exception e) {
                            try {
                                reportDetails[i].projects[j] = new Project();
                                TaskCategory category = manager.getCategory(s);
                                reportDetails[i].projects[j].setProjectId(category.getId());
                                reportDetails[i].projects[j].setProjectName(category.getName());
                            }
                            catch(Exception e1) {
                                reportDetails[i].projects[j].setProjectId(s);
                                reportDetails[i].projects[j].setProjectName("-deleted-");
                            }
                        }

                        // start daily total for each task category
                        Calendar sCal = Calendar.getInstance();
                        sCal.setTime(startDate);
                        Calendar eCal = Calendar.getInstance();
                        eCal.setTime(startDate);
                        reportDetails[i].daily = new double[endDay];
                        reportDetails[i].projectTotal[j]=0;

                        for (int iDay=0;iDay<endDay;iDay++) {
                            if (start)
                                reportDetails[i].dailyTotal[iDay] = 0;
                            sCal.set(Calendar.DAY_OF_MONTH,(iDay+1));
                            eCal.setTime(sCal.getTime());
                            eCal.set(Calendar.HOUR,23);
                            eCal.set(Calendar.MINUTE,59);
                            eCal.set(Calendar.SECOND,59);


                            reportDetails[i].daily[iDay] = mod.getTotalHourSpentForProjectPerDay(
                                    reportDetails[i].projects[j].getProjectId(),reportDetails[i].user.getId(),sCal.getTime(),eCal.getTime()
                                    );

                            reportDetails[i].daily[iDay] += mod.getAdjustedHourSpentForProjectPerDay(
                                    reportDetails[i].projects[j].getProjectId(),reportDetails[i].user.getId(),sCal.getTime(),eCal.getTime()
                                    );

                            reportDetails[i].dailyTotal[iDay] += reportDetails[i].daily[iDay];
                            reportDetails[i].projectTotal[j] += reportDetails[i].daily[iDay];
                            grandTotal[i] += reportDetails[i].daily[iDay];
                        }
                        start=false;
                        reportDetails[i].totalDetails.add(reportDetails[i].daily);
                    }
                }
            }

        }

        return null;
    }

    public void getMonthDetails(String strMonth) {
        String[] s = strMonth.split(",");
        this.month=s[0];
        this.year=s[1];
        int iMonth = Integer.parseInt(this.month);
        int iYear = Integer.parseInt(this.year);
        startDay=1;
        endDay= TimeSheetUtil.getLastDayOfMonth(iMonth,iYear);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH,iMonth);
        calendar.set(Calendar.YEAR,iYear);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        startDate = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH,endDay);
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        endDate = calendar.getTime();

        this.month = TimeSheetUtil.getMonthDescription(iMonth+1);
        dayList=new int[endDay];
        for (int i=0;i<endDay;i++) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR,iYear);
            cal.set(Calendar.MONTH,iMonth);
            cal.set(Calendar.DAY_OF_MONTH,(i+1));
            int iDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            if (iDayOfWeek==Calendar.SATURDAY || iDayOfWeek==Calendar.SUNDAY)
                dayList[i]=0;
            else
                dayList[i]=1;
        }
    }


    public void setupUI() {

        monthSelect = new SelectBox("monthSelect");
        comboBox = new ComboSelectBox("comboBox");
        btnSubmit = new Button("btnSubmit","Submit");
        btnCancel = new Button("btnCancel","Cancel");
        btnPrint = new Button("btnPrint","Print");

        // setup month select box
        monthSelect.addOption("",Application.getInstance().getMessage("security.label.pleaseSelect","---Please Select---"));
        Calendar calendar = Calendar.getInstance();
        int iMonth = calendar.get(Calendar.MONTH);
        int iYear = calendar.get(Calendar.YEAR);

        for (int i=iMonth;i>=0;i--) {
            String year = ""+iYear;
            monthSelect.addOption((i)+","+year,TimeSheetUtil.getMonthDescription(i+1)+","+year);
        }

        for (int i=11;i>iMonth;i--) {
            String year = ""+(iYear-1);
            monthSelect.addOption((i)+","+year,TimeSheetUtil.getMonthDescription(i+1)+","+year);
        }

/*
        for (int i=0;i<12;i++)  {

            int iMonth = calendar.get(Calendar.MONTH);
            int iYear = calendar.get(Calendar.YEAR);
            if (iMonth<(i)) {
                String year = ""+(iYear-1);
                monthSelect.addOption((i)+","+year,TimeSheetUtil.getMonthDescription(i+1)+","+year);
            }
            else {
                String year = ""+iYear;
                monthSelect.addOption((i)+","+year,TimeSheetUtil.getMonthDescription(i+1)+","+year);
            }
        }
*/
        addChild(monthSelect);
        addChild(comboBox);
        addChild(btnSubmit);
        addChild(btnCancel);
        addChild(btnPrint);

        comboBox.init();

        // set up user combo select box
        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
        DaoQuery properties = new DaoQuery();
        properties.addProperty(new OperatorEquals("u.active", "1", DaoOperator.OPERATOR_AND));
        Map sbUser = new SequencedHashMap();
        try {
            Collection colUser = service.getUsers(properties,0,-1,"firstName",false);
            if (colUser!=null && colUser.size()>0) {
                for (Iterator i=colUser.iterator();i.hasNext();) {
                    User user = (User)i.next();
                    sbUser.put(user.getId(),user.getName());
                }
            }
        }
        catch(Exception e) {

        }
        comboBox.setLeftValues(sbUser);


    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getStartDay() {
        return startDay;
    }

    public void setStartDay(int startDay) {
        this.startDay = startDay;
    }

    public int getEndDay() {
        return endDay;
    }

    public void setEndDay(int endDay) {
        this.endDay = endDay;
    }

    public int[] getDayList() {
        return dayList;
    }

    public void setDayList(int[] dayList) {
        this.dayList = dayList;
    }

    public String[] getUserIds() {
        return userIds;
    }

    public void setUserIds(String[] userIds) {
        this.userIds = userIds;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Button getBtnCancel() {
        return btnCancel;
    }

    public void setBtnCancel(Button btnCancel) {
        this.btnCancel = btnCancel;
    }

    public Button getBtnSubmit() {
        return btnSubmit;
    }

    public void setBtnSubmit(Button btnSubmit) {
        this.btnSubmit = btnSubmit;
    }

    public SelectBox getMonthSelect() {
        return monthSelect;
    }

    public void setMonthSelect(SelectBox monthSelect) {
        this.monthSelect = monthSelect;
    }

    public ComboSelectBox getComboBox() {
        return comboBox;
    }

    public void setComboBox(ComboSelectBox comboBox) {
        this.comboBox = comboBox;
    }

    public UserProjectReport[] getReportDetails() {
        return reportDetails;
    }

    public void setReportDetails(UserProjectReport[] reportDetails) {
        this.reportDetails = reportDetails;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public void setGrandTotal(double[] grandTotal) {
        this.grandTotal = grandTotal;
    }

    public double[] getGrandTotal() {
        return this.grandTotal;
    }

    public void setBtnPrint(Button btnPrint) {
        this.btnPrint = btnPrint;
    }

    public Button getBtnPrint() {
        return this.btnPrint;
    }
}
