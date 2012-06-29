package com.tms.hr.leave.ui;

import kacang.stdui.*;
import kacang.Application;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.services.security.User;
import kacang.util.Log;

import java.util.*;

import com.tms.hr.leave.model.*;
import com.tms.util.FormatUtil;

/**
 * Displays leave entries for approval, up to the next year.
 * If userId is not set, the current user is used.
 * If year is not set, the current year is used.
 * If credit is true, credit leave submissions will be shown.
 */
public class ApproveLeaveEntryTable extends Table {

    private String userId;
    private String year;
    private boolean credit;

    public ApproveLeaveEntryTable() {
        super();
    }

    public ApproveLeaveEntryTable(String name) {
        super(name);
    }

    public void init() {
        setModel(new ApproveLeaveEntryTableModel());
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public boolean isCredit() {
        return credit;
    }

    public void setCredit(boolean credit) {
        this.credit = credit;
    }

    public class ApproveLeaveEntryTableModel extends TableModel {

        public ApproveLeaveEntryTableModel() {
            Application application = Application.getInstance();

            TableColumn applicationDate = new TableColumn("applicationDate", application.getMessage("leave.label.applyDate", "Apply Date"));
            applicationDate.setUrlParam("id");
            applicationDate.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateFormat()));
            addColumn(applicationDate);

            TableColumn employeeName = new TableColumn("employeeName", application.getMessage("leave.label.name", "Name"));
            addColumn(employeeName);

            TableColumn leaveTypeName = new TableColumn("leaveTypeName", application.getMessage("leave.label.leaveTypeName", "Leave Type"));
            addColumn(leaveTypeName);

            TableColumn startDate = new TableColumn("startDate", application.getMessage("leave.label.startDate", "Start Date"));
            startDate.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateFormat()));
            addColumn(startDate);

            TableColumn endDate = new TableColumn("endDate", application.getMessage("leave.label.endDate", "End Date"));
            endDate.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateFormat()));
            addColumn(endDate);

            TableColumn days = new TableColumn("calculatedDays", application.getMessage("leave.label.days", "Days"));
            days.setFormat(new TableDecimalFormat("#.#"));
            addColumn(days);

            TableColumn balance = new TableColumn("balance", application.getMessage("leave.label.balance","Balance"));
            addColumn(balance);



            TableColumn reason = new TableColumn("reason", application.getMessage("leave.label.reason", "Reason"));
            addColumn(reason);

            TableColumn status = new TableColumn("status", application.getMessage("leave.label.status", "Status"));
            addColumn(status);

            TableFilter dummyFilter = new TableFilter("dummy");
            dummyFilter.setWidget(new Label("dummy"));
            addFilter(dummyFilter);

            addAction(new TableAction("approve", application.getMessage("leave.label.approve", "Approve"), application.getMessage("leave.label.approveselecteditem", "Approve selected item(s)")));
            addAction(new TableAction("reject", application.getMessage("leave.label.reject", "Reject"), application.getMessage("leave.label.rejectselecteditem", "Reject selected item(s)")));
        }

        public String getTableRowKey() {
            return "id";
        }

        public Collection getTableRows() {

            Collection rows = new ArrayList();
            try {
                Application application = Application.getInstance();
                LeaveModule lm = (LeaveModule)application.getModule(LeaveModule.class);
                User user = getWidgetManager().getUser();

                // determine user
                String userId = getUserId();
                if (userId == null) {
                    // get current user
                    userId = user.getId();
                }

                // determine year
                int year = 0;
                try {
                    year = Integer.parseInt(getYear());
                }
                catch (Exception e) {
                    ;
                }
                if (year <= 0) {
                    // get current year
                    Calendar cal = Calendar.getInstance();
                    year = cal.get(Calendar.YEAR);
                }
                Calendar start = Calendar.getInstance();
                start.set(2000, Calendar.JANUARY, 1);
                Calendar end = Calendar.getInstance();
                end.set(year+1, Calendar.DECEMBER, 31);

                // get leave entries
                boolean credit = isCredit();
                if (!credit) {
                    rows = lm.viewLeaveToApproveList(start.getTime(), end.getTime(), user, getSort(), isDesc(), getStart(), getRows());
                }
                else {
                    rows = lm.viewCreditLeaveToApproveList(start.getTime(), end.getTime(), user, getSort(), isDesc(), getStart(), getRows());
                }

            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Error retrieving leave summary", e);
            }
            return rows;

        }

        public int getTotalRowCount() {
            int rowCount = 0;
            try {
                Application application = Application.getInstance();
                LeaveModule lm = (LeaveModule)application.getModule(LeaveModule.class);
                User user = getWidgetManager().getUser();

                // determine user
                String userId = getUserId();
                if (userId == null) {
                    // get current user
                    userId = user.getId();
                }

                // determine year
                int year = 0;
                try {
                    year = Integer.parseInt(getYear());
                }
                catch (Exception e) {
                    ;
                }
                if (year <= 0) {
                    // get current year
                    Calendar cal = Calendar.getInstance();
                    year = cal.get(Calendar.YEAR);
                }
                Calendar start = Calendar.getInstance();
                start.set(2000, Calendar.JANUARY, 1);
                Calendar end = Calendar.getInstance();
                end.set(year+1, Calendar.DECEMBER, 31);

                // get leave entries
                boolean credit = isCredit();
                if (!credit) {
                    rowCount = lm.countLeaveToApproveList(start.getTime(), end.getTime(), user);
                }
                else {
                    rowCount = lm.countCreditLeaveToApproveList(start.getTime(), end.getTime(), user);
                }
            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Error retrieving leave summary count", e);
            }
            return rowCount;
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            if ("approve".equals(action)) {

                Application application = Application.getInstance();
                LeaveModule lm = (LeaveModule)application.getModule(LeaveModule.class);
                User user = getWidgetManager().getUser();
                boolean credit = isCredit();

                for (int i=0; i<selectedKeys.length; i++) {
                    String leaveId = selectedKeys[i];
                    try {
                        if (!credit) {
                            lm.approveLeave(leaveId, "", user);
                        }
                        else {
                            lm.approveCreditLeave(leaveId, "", user);
                        }
                    }
                    catch (Exception e) {
                        Log.getLog(getClass()).debug("Error approving leave", e);
                    }
                }

            }
            else if ("reject".equals(action)) {

                Application application = Application.getInstance();
                LeaveModule lm = (LeaveModule)application.getModule(LeaveModule.class);
                User user = getWidgetManager().getUser();
                boolean credit = isCredit();

                for (int i=0; i<selectedKeys.length; i++) {
                    String leaveId = selectedKeys[i];
                    try {
                        if (!credit) {
                            lm.rejectLeave(leaveId, "", user);
                        }
                        else {
                            lm.rejectCreditLeave(leaveId, "", user);
                        }
                    }
                    catch (Exception e) {
                        Log.getLog(getClass()).debug("Error rejecting leave", e);
                    }
                }

            }
            return null;
        }
    }

}
