package com.tms.collab.timesheet.ui;

import kacang.ui.Widget;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.services.security.User;
import kacang.services.security.SecurityService;
import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import com.tms.collab.project.Project;
import com.tms.collab.project.WormsHandler;
import com.tms.collab.timesheet.model.TimeSheetModule;
import com.tms.collab.timesheet.TimeSheetUtil;
import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.taskmanager.model.TaskManager;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: May 24, 2005
 * Time: 2:09:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimeSheetMonthlyReport extends Form {
    protected String reportType;
    protected String projectId;
    protected String userId;
    protected String month;
    protected String year;
    protected Date startDate;
    protected Date endDate;
    protected int startDay;
    protected int endDay;
    protected Project project;
    protected User[] userList;
    protected Collection userDailySpent;
    protected double[] userTotalList;
    protected User user;
    protected Task[] taskList;
    protected Collection taskDailySpent;
    protected double[] taskTotalList;
    protected int[] dayList;

    protected double[] dailyTotal;
    protected double grandTotal;

    protected boolean print;
    protected Button btPrint;
    protected String monthParam;

    public String getDefaultTemplate() {
        return "timesheet/tsmonthlyreport"  ;
    }

    public void init() {
        super.init();
        btPrint = new Button("btprint",Application.getInstance().getMessage("timesheet.label.print"));
        addChild(btPrint);
    }

    public void onRequest(Event ev) {
        if (reportType.equals("project")) 
            initProjectReport();
        else
            initUserReport();
    }

    public void initProjectReport() {
        reportType="project";
        project = new Project();
        userList=null;
        dailyTotal=null;
        userDailySpent=new ArrayList();
        userTotalList=null;
        grandTotal=0;

        WormsHandler handler = (WormsHandler)Application.getInstance().getModule(WormsHandler.class);
        try {
            project = handler.getProject(projectId);
        }
        catch(Exception e) {

        }

        if (project!=null) {
            TimeSheetModule mod = (TimeSheetModule)Application.getInstance().getModule(TimeSheetModule.class);
            Collection col = mod.getUserListForProject(projectId,startDate,endDate);
            SecurityService service = (SecurityService)Application.getInstance().getService(SecurityService.class);
            if (col!=null && col.size()>0) {
                userList = new User[col.size()];
                userTotalList = new double[col.size()];
                userDailySpent = new ArrayList(col.size());
                dailyTotal=new double[endDay];
                int iCounter=0;
                boolean startDailyTotal=true;
                for (Iterator i=col.iterator();i.hasNext();) {
                    try {
                        HashMap map = (HashMap)i.next();
                        userList[iCounter] = service.getUser(map.get("userId").toString());
                        userTotalList[iCounter]=0.0;
                        Calendar calStart = Calendar.getInstance();
                        calStart.setTime(startDate);
                        Calendar calEnd = Calendar.getInstance();

                        double[] dbl = new double[endDay];
                        for (int iStart=0;iStart<endDay;iStart++) {
                            if (startDailyTotal)
                                dailyTotal[iStart]=0;
                            dbl[iStart]=0;
                            calStart.set(Calendar.DAY_OF_MONTH,(iStart+1));
                            calEnd.setTime(calStart.getTime());
                            calEnd.set(Calendar.HOUR,23);
                            calEnd.set(Calendar.MINUTE,59);
                            calEnd.set(Calendar.SECOND,59);

                            dbl[iStart] = mod.getTotalHourSpentForProjectPerDay(projectId,userList[iCounter].getId(),calStart.getTime(),calEnd.getTime());
                            dbl[iStart] += mod.getAdjustedHourSpentForProjectPerDay(projectId,userList[iCounter].getId(),calStart.getTime(),calEnd.getTime());

                            userTotalList[iCounter]+=dbl[iStart];
                            dailyTotal[iStart] += dbl[iStart];
                            grandTotal+=dbl[iStart];
                        }
                        userDailySpent.add(dbl);
                        iCounter++;
                        startDailyTotal=false;
                    }
                    catch(Exception e) {

                    }
                } // end for
            } // end if col
        }// end if (project!=null)

    }

    public void initUserReport() {
        reportType="user";
        user = new User();
        taskList = null;
        taskTotalList=null;
        dailyTotal=null;
        taskDailySpent = new ArrayList();
        grandTotal=0;

        SecurityService service = (SecurityService)Application.getInstance().getService(SecurityService.class);
        try {
            user = service.getUser(userId);
        }
        catch(Exception e) {

        }

        if (user!=null) {
            TimeSheetModule mod = (TimeSheetModule)Application.getInstance().getModule(TimeSheetModule.class);
            Collection col = mod.getTaskListForUser(userId,startDate,endDate);
            if (col!=null && col.size()>0) {
                taskList=new Task[col.size()];
                taskTotalList=new double[col.size()];
                taskDailySpent=new ArrayList(col.size());
                dailyTotal=new double[endDay];

                TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
                int iCounter=0;
                boolean startDailyTotal=true;
                for (Iterator i=col.iterator();i.hasNext();) {
                    try {
                        HashMap map = (HashMap)i.next();
                        taskList[iCounter] = tm.getTask(map.get("taskId").toString());
                        taskTotalList[iCounter]=0.0;

                        Calendar calStart = Calendar.getInstance();
                        calStart.setTime(startDate);
                        Calendar calEnd = Calendar.getInstance();

                        double[] dbl = new double[endDay];
                        for (int iStart=0;iStart<endDay;iStart++) {
                            if (startDailyTotal)
                                dailyTotal[iStart]=0;
                            dbl[iStart]=0;
                            calStart.set(Calendar.DAY_OF_MONTH,(iStart+1));
                            calEnd.setTime(calStart.getTime());
                            calEnd.set(Calendar.HOUR,23);
                            calEnd.set(Calendar.MINUTE,59);
                            calEnd.set(Calendar.SECOND,59);
                            /*
                            Date date = calStart.getTime();
                            String s = date.toString();
                            date = calEnd.getTime();
                            String s1 = date.toString();
                            */
                            dbl[iStart] = mod.getTotalHourSpentForTaskPerDay(userId,taskList[iCounter].getId(),calStart.getTime(),calEnd.getTime());
                            dbl[iStart] += mod.getAdjustedHourSpentForTaskPerDay(userId,taskList[iCounter].getId(),calStart.getTime(),calEnd.getTime());

                            taskTotalList[iCounter]+=dbl[iStart];
                            dailyTotal[iStart] += dbl[iStart];
                            grandTotal+=dbl[iStart];
                        }
                        taskDailySpent.add(dbl);
                        iCounter++;
                        startDailyTotal=false;
                    }
                    catch(Exception e) {

                    }
                }
            }
        }
    }

    public Forward onValidate(Event ev) {
        if (btPrint.getAbsoluteName().equals(findButtonClicked(ev))) {
            return new Forward("print");
        }
        return null;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
        initProjectReport();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        initUserReport();
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        monthParam=month;
        String[] s = month.split(",");
        this.month=s[0];
        this.year=s[1];
        int iMonth = Integer.parseInt(this.month);
        int iYear = Integer.parseInt(this.year);
        startDay=1;
        endDay=TimeSheetUtil.getLastDayOfMonth(iMonth,iYear);
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
            cal.set(Calendar.MONTH,iMonth);
            cal.set(Calendar.DAY_OF_MONTH,(i+1));
            int iDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            if (iDayOfWeek==Calendar.SATURDAY || iDayOfWeek==Calendar.SUNDAY)
                dayList[i]=0;
            else
                dayList[i]=1;
        }
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User[] getUserList() {
        return userList;
    }

    public void setUserList(User[] userList) {
        this.userList = userList;
    }

    public double[] getUserTotalList() {
        return userTotalList;
    }

    public void setUserTotalList(double[] userTotalList) {
        this.userTotalList = userTotalList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Task[] getTaskList() {
        return taskList;
    }

    public void setTaskList(Task[] taskList) {
        this.taskList = taskList;
    }

    public double[] getTaskTotalList() {
        return taskTotalList;
    }

    public void setTaskTotalList(double[] taskTotalList) {
        this.taskTotalList = taskTotalList;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Collection getUserDailySpent() {
        return userDailySpent;
    }

    public void setUserDailySpent(Collection userDailySpent) {
        this.userDailySpent = userDailySpent;
    }

    public Collection getTaskDailySpent() {
        return taskDailySpent;
    }

    public void setTaskDailySpent(Collection taskDailySpent) {
        this.taskDailySpent = taskDailySpent;
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

    public double[] getDailyTotal() {
        return dailyTotal;
    }

    public void setDailyTotal(double[] dailyTotal) {
        this.dailyTotal = dailyTotal;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public void setPrint(boolean print) {
        this.print = print;
    }

    public boolean isPrint() {
        return print;
    }

    public void setBtPrint(Button btPrint) {
        this.btPrint = btPrint;
    }

    public Button getBtPrint() {
        return this.btPrint;
    }

    public String getMonthParam() {
        return monthParam;
    }
}
