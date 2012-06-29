package com.tms.hr.leave.ui;

import kacang.stdui.*;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorLike;
import kacang.model.operator.DaoOperator;
import kacang.services.security.User;
import kacang.services.security.SecurityService;
import kacang.util.Log;

import java.util.*;

import com.tms.hr.leave.model.LeaveModule;
import com.tms.hr.leave.model.LeaveType;

/**
 * Displays yearly leave summary info for a leave type.
 */
public class LeaveReportTable extends Table {

    public LeaveReportTable() {
        super();
    }

    public LeaveReportTable(String name) {
        super(name);
    }

    public void init() {
        setSortable(false);
        setModel(new LeaveReportTableModel());
    }

    public String getYear() {
        int year = 0;
        TableModel model = getModel();
        if (model != null) {
            try {
                Collection tmp = (Collection)model.getFilterValue("yearFilter");
                year = Integer.parseInt(tmp.iterator().next().toString());
            }
            catch (Exception e1) {
            }
        }
        if (year == 0) {
            // get current year
            Calendar cal = Calendar.getInstance();
            year = cal.get(Calendar.YEAR);
        }
        return new Integer(year).toString();
    }

    public void setYear(String year) {
        TableModel model = getModel();
        if (model != null) {
            try {
                SelectBox tmp = (SelectBox)model.getFilter("yearFilter").getWidget();
                tmp.setSelectedOption(year);
            }
            catch (Exception e1) {
            }
        }
    }

    public class LeaveReportTableModel extends TableModel {

        private int rowCount;

        public LeaveReportTableModel() {
            Application application = Application.getInstance();

            TableColumn employeeName = new TableColumn("employeeName", application.getMessage("leave.label.name", "Name"));
            employeeName.setUrlParam("userId");
            addColumn(employeeName);

            TableColumn entitlement = new TableColumn("entitlement", application.getMessage("leave.label.entitlement", "Entitlement"));
            entitlement.setFormat(new TableDecimalFormat("#.#"));
            addColumn(entitlement);

            TableColumn carryForward = new TableColumn("carryForward", application.getMessage("leave.label.broughtforward", "Brought Forward"));
            carryForward.setFormat(new TableDecimalFormat("#.#"));
            addColumn(carryForward);

            TableColumn credited = new TableColumn("credited", application.getMessage("leave.label.credited", "Credited"));
            credited.setFormat(new TableDecimalFormat("#.#"));
            addColumn(credited);

            TableColumn taken = new TableColumn("taken", application.getMessage("leave.label.taken", "Taken"));
            taken.setFormat(new TableDecimalFormat("#.#", true));
            addColumn(taken);

            TableColumn adjustments = new TableColumn("adjustments", application.getMessage("leave.label.adjustments", "Adjustments"));
            adjustments.setFormat(new TableDecimalFormat("#.#"));
            addColumn(adjustments);

            TableColumn balance = new TableColumn("balance", application.getMessage("leave.label.balance", "Balance"));
            balance.setFormat(new TableDecimalFormat("#.#"));
            addColumn(balance);

            TableFilter userFilter = new TableFilter("userFilter");
            addFilter(userFilter);

            TableFilter yearFilter = new TableFilter("yearFilter");
            SelectBox sb = new SelectBox("yearFilter");
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            for (int i=year-10; i<year+10; i++) {
                Integer iy = new Integer(i);
                sb.addOption(iy.toString(), iy.toString());
            }
            sb.setSelectedOption(new Integer(year).toString());
            yearFilter.setWidget(sb);
            addFilter(yearFilter);

            TableFilter leaveTypeFilter = new TableFilter("leaveTypeFilter");
            SelectBox sb2 = new SelectBox("leaveTypeFilter");
            try {
                LeaveModule handler = (LeaveModule) application.getModule(LeaveModule.class);
                Collection leaveTypes = handler.viewLeaveTypeList(null, false, true);
                sb2.setOptions(leaveTypes, "leaveType", "leaveType");
                if (leaveTypes.size() > 0) {
                    sb2.setSelectedOption(((LeaveType)leaveTypes.iterator().next()).getLeaveType());
                }
            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Error retrieving leave types", e);
            }
            leaveTypeFilter.setWidget(sb2);
            addFilter(leaveTypeFilter);
        }

        public String getTableRowKey() {
            return "userId";
        }

        public Collection getTableRows() {

            Collection rows = new ArrayList();
            rowCount = 0;
            try {
                Application application = Application.getInstance();
                LeaveModule lm = (LeaveModule)application.getModule(LeaveModule.class);
                User user = getWidgetManager().getUser();

                // determine year
                int year;
                try {
                    Collection tmp = (Collection)getFilterValue("yearFilter");
                    year = Integer.parseInt(tmp.iterator().next().toString());
                }
                catch (Exception e1) {
                    // get current year
                    Calendar cal = Calendar.getInstance();
                    year = cal.get(Calendar.YEAR);
                }

                // get leave types
                String leaveType = null;
                Collection typeList = (Collection)getFilterValue("leaveTypeFilter");
                if (typeList != null && typeList.size() > 0) {
                    leaveType = typeList.iterator().next().toString();
                }

                // determine users
                Collection userIdList = new ArrayList();
                String search = (String)getFilterValue("userFilter");
                SecurityService security = (SecurityService)application.getService(SecurityService.class);
                DaoQuery q = new DaoQuery();
                q.addProperty(new OperatorLike("firstName", search, DaoOperator.OPERATOR_AND));
                q.addProperty(new OperatorLike("lastName", search, DaoOperator.OPERATOR_OR));
                Collection users = security.getUsers(q, 0, -1, "firstName", false);
                for (Iterator i=users.iterator(); i.hasNext();) {
                    User u = (User)i.next();
                    userIdList.add(u.getId());
                }
                boolean isAdmin = lm.checkForAdmin(user);
                if (!isAdmin) {
                    // get relevant users only
                    String[] employees = lm.viewEmployeeListForApprover(user.getId());
                    userIdList.retainAll(Arrays.asList(employees));
                }
                String[] userIdArray = (String[])userIdList.toArray(new String[0]);

                // get leave summary
                rows = lm.viewLeaveSummaryList(leaveType, userIdArray, year, user);
                rowCount = rows.size();

                // paging
                int start = getStart();
                if (start < 0 || start > rowCount) {
                    start = 0;
                }
                int end = start + getRows();
                if (end > rowCount || getRows() < 0) {
                    end = rowCount;
                }
                rows = new ArrayList(rows).subList(start, end);
            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Error retrieving leave summary", e);
            }
            return rows;

        }

        public int getTotalRowCount() {
            return rowCount;
        }

    }

    public class TableDecimalFormat extends kacang.stdui.TableDecimalFormat {

        boolean unsigned;

        public TableDecimalFormat(String pattern) {
            super(pattern);
        }

        public TableDecimalFormat(String pattern, boolean unsigned) {
            super(pattern);
            this.unsigned = unsigned;
        }

        public String format(Object value) {
            if (unsigned) {
                try {
                    float f = Float.parseFloat(value.toString());
                    if (f < 0) {
                        value = new Float(-f);
                    }
                }
                catch (Exception e) {
                    ;
                }
            }
            String val = super.format(value);
            if ("0".equals(val)) {
                val = "-";
            }
            return val;
        }
    }

}
