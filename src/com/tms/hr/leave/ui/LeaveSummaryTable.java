package com.tms.hr.leave.ui;

import kacang.stdui.Table;
import kacang.stdui.TableModel;
import kacang.stdui.TableColumn;
import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.services.security.User;
import kacang.util.Log;

import java.util.*;

import com.tms.hr.leave.model.LeaveModule;
import com.tms.hr.leave.model.LeaveType;
import com.tms.hr.leave.model.LeaveSummary;
import com.tms.hr.leave.model.LeaveCalculator;

/**
 * Displays yearly leave summary info for all leave types for a user.
 * If userId is not set, the current user is used.
 * If year is not set, the current year is used.
 */
public class LeaveSummaryTable extends Table {

    private String userId;
    private int year;

    public LeaveSummaryTable() {
        super();
    }

    public LeaveSummaryTable(String name) {
        super(name);
    }

    public void init() {
        setSortable(false);
        setPageSize(-1);
        setModel(new LeaveSummaryTableModel());
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public class LeaveSummaryTableModel extends TableModel {

        private int rowCount;

        public LeaveSummaryTableModel() {
            Application application = Application.getInstance();

            TableColumn leaveType = new TableColumn("leaveType", application.getMessage("leave.label.leaveType", "Leave Type"));
            //leaveType.setUrl("viewLeaveEntryList.jsp");
            leaveType.setUrlParam("leaveType");
            addColumn(leaveType);

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

        }

        public String getTableRowKey() {
            return "leaveType";
        }

        public Collection getTableRows() {

            Collection rows = new ArrayList();
            rowCount = 0;
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
                int year = getYear();
                if (year <= 0) {
                    // get current year
                    Calendar cal = Calendar.getInstance();
                    year = cal.get(Calendar.YEAR);
                }

                // get leave types
                Collection typeList = lm.viewLeaveTypeList(userId, false, false);



                // get calculator
                LeaveCalculator calculator = LeaveModule.getLeaveCalculator(year);


                // get leave summary for each type
                HashSet set = new HashSet();
                for (Iterator i=typeList.iterator(); i.hasNext();) {
                    LeaveType lt = (LeaveType)i.next();


                    String type = lt.getLeaveType();
                    calculator.fixedCalendar(lt.getFixedCalendar().equals("1") ? true : false);
                    if (!set.contains(type)) {
                        set.add(type);
                        LeaveSummary summary = lm.viewLeaveSummary(calculator, type, userId, year, false, user);
                        rows.add(summary);
                    }
                }

            }
            catch (DataObjectNotFoundException e) {
                Log.getLog(getClass()).debug("Error retrieving leave summary, employee not found " + getUserId());
            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Error retrieving leave summary", e);
            }
            rowCount = rows.size();
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
