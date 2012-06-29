package com.tms.hr.leave.ui;

import com.tms.hr.leave.model.LeaveModule;
import com.tms.hr.leave.model.LeaveType;

import com.tms.util.FormatUtil;

import kacang.Application;

import kacang.services.security.User;

import kacang.stdui.*;

import kacang.ui.WidgetManager;

import kacang.util.Log;

import java.util.*;


/**
 * Displays yearly leave entries for a leave types for a specified user.
 * The property leaveType must be set.
 * If userId is not set, the current user is used.
 * If year is not set, the current year is used.
 */
public class LeaveEntryTable extends Table {
    private String leaveType;
    private String userId;
    private String year;
    
    private SelectBox selectRead;

    public LeaveEntryTable() {
        super();
    }

    public LeaveEntryTable(String name) {
        super(name);
    }

    public void init() {
        setModel(new LeaveEntryTableModel());
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
        if (selectRead != null)
            selectRead.setSelectedOption(getLeaveType());
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
    
    public String getMyLeaveType() {
        LeaveEntryTableModel m = (LeaveEntryTableModel) getModel();
        if (m != null) {
            List readStatus = (List) m.getFilterValue("leaveType");
            if (readStatus != null && readStatus.size() > 0) {
                return (String) readStatus.get(0);
            }
        }
        return leaveType;
    }

    public class LeaveEntryTableModel extends TableModel {
        public LeaveEntryTableModel() {
            Application application = Application.getInstance();

            TableColumn applicationDate = new TableColumn("applicationDate",
                    application.getMessage("leave.label.applyDate", "Apply Date"));
            applicationDate.setUrlParam("id");
            applicationDate.setFormat(new TableDateFormat(
                    FormatUtil.getInstance().getLongDateFormat()));
            addColumn(applicationDate);

            TableColumn leaveTypeName = new TableColumn("leaveTypeName",
                    application.getMessage("leave.label.leaveTypeName",
                        "Leave Type"));
            addColumn(leaveTypeName);

            TableColumn startDate = new TableColumn("startDate",
                    application.getMessage("leave.label.startDate", "Start Date"));
            startDate.setFormat(new TableDateFormat(FormatUtil.getInstance()
                                                              .getLongDateFormat()));
            addColumn(startDate);

            TableColumn endDate = new TableColumn("endDate",
                    application.getMessage("leave.label.endDate", "End Date"));
            endDate.setFormat(new TableDateFormat(FormatUtil.getInstance()
                                                            .getLongDateFormat()));
            addColumn(endDate);

            TableColumn days = new TableColumn("calculatedDays",
                    application.getMessage("leave.label.days", "Days"));
            days.setFormat(new TableDecimalFormat("#.#"));
            addColumn(days);

            String contextPath = (String) getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
            TableColumn credit = new TableColumn("credit",
                    application.getMessage("leave.label.credit", "Credit"));
            credit.setFormat(new TableBooleanFormat("<img src=\"" +
                    contextPath + "/common/table/booleantrue.gif\">", ""));
            addColumn(credit);

            TableColumn adjustment = new TableColumn("adjustment",
                    application.getMessage("leave.label.adjustment",
                        "Adjustment"));
            adjustment.setFormat(new TableBooleanFormat("<img src=\"" +
                    contextPath + "/common/table/booleantrue.gif\">", ""));
            addColumn(adjustment);

            TableColumn reason = new TableColumn("reason",
                    application.getMessage("leave.label.reason", "Reason"));
            addColumn(reason);

            TableColumn status = new TableColumn("status",
                    application.getMessage("leave.label.status", "Status"));
            addColumn(status);

            TableFilter dummyFilter = new TableFilter("dummy");
            dummyFilter.setWidget(new Label("dummy"));
            addFilter(dummyFilter);


            TableFilter tfRead = new TableFilter("leaveType");
            selectRead = new SelectBox("select_leaveType");

            LeaveModule lm = (LeaveModule) application.getModule(LeaveModule.class);
            Collection leaveType = lm.getLeaveType();
                   //selectRead.addOption("", application.getMessage("leave.label.viewallleavetype"));
            
           
            selectRead.setOptions(leaveType, "leaveType", "leaveType");
            if (leaveType.size() > 0) {
            	selectRead.setSelectedOption(((LeaveType)leaveType.iterator().next()).getLeaveType());
            }
            
                 /*   for (Iterator icount = leaveType.iterator(); icount.hasNext();) {
                    LeaveType leaveTypeObject = (LeaveType) icount.next();
                     selectRead.addOption(leaveTypeObject.getId(), leaveTypeObject.getName());
                }*/
                    

            selectRead.setMultiple(false);
            tfRead.setWidget(selectRead);
            addFilter(tfRead);


        }

        public String getTableRowKey() {
            return "id";
        }

        
        
        
        
        public Collection getTableRows() {
            Collection rows = new ArrayList();

            try {
                Application application = Application.getInstance();
                LeaveModule lm = (LeaveModule) application.getModule(LeaveModule.class);
                User user = getWidgetManager().getUser();

                // A very hacky way to patch bug #4504. But then what the hack, this whole thing is
                // so backy anyway.
                boolean override = false;
                List readStatus = (List) getFilterValue("leaveType");
                               String leaveTypeName = "";
                               if (readStatus.size() > 0) {
                                   leaveTypeName = (String) readStatus.get(0);
                                   setLeaveType(leaveTypeName);
                                   override=true;
                               }
                               if (leaveTypeName != null && (leaveTypeName.trim().equalsIgnoreCase("null") || leaveTypeName.trim().equals(""))) {
                                   leaveTypeName = "Annual";
                                   override = false;
                               }


                               


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
                } catch (Exception e) {
                    ;
                }

                if (year <= 0) {
                    // get current year
                    Calendar cal = Calendar.getInstance();
                    year = cal.get(Calendar.YEAR);
                }

                Calendar start = Calendar.getInstance();
                start.set(year, Calendar.JANUARY, 1);

                Calendar end = Calendar.getInstance();
                end.set(year, Calendar.DECEMBER, 31);

                // get leave type
                String leaveType = getLeaveType();

                // get leave entries
                rows = lm.viewAllLeaveList(leaveTypeName,start.getTime(), end.getTime(),
                        new String[] { userId }, null, override?leaveTypeName:leaveType, null, null,
                        null, user, getSort(), isDesc(), getStart(), getRows());
                
            } catch (Exception e) {
                Log.getLog(getClass()).error("Error retrieving leave summary", e);
            }

            return rows;
        }

        public int getTotalRowCount() {
            int rowCount = 0;

            // Once again, hacky patch for hacky code. (#4505) fair deal.
            boolean override = false;
            List readStatus = (List) getFilterValue("leaveType");
                                String leaveTypeName = "";
                                if (readStatus.size() > 0) {
                                    leaveTypeName = (String) readStatus.get(0);
                                    setLeaveType(leaveTypeName);
                                    override = true;
                                }
                                if (leaveTypeName != null && (leaveTypeName.trim().equalsIgnoreCase("null") || leaveTypeName.trim().equals(""))) {
                                    leaveTypeName = "Annual";
                                    override = false;
                                }





            try {
                Application application = Application.getInstance();
                LeaveModule lm = (LeaveModule) application.getModule(LeaveModule.class);
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
                } catch (Exception e) {
                    ;
                }

                if (year <= 0) {
                    // get current year
                    Calendar cal = Calendar.getInstance();
                    year = cal.get(Calendar.YEAR);
                }

                Calendar start = Calendar.getInstance();
                start.set(year, Calendar.JANUARY, 1);

                Calendar end = Calendar.getInstance();
                end.set(year, Calendar.DECEMBER, 31);

                // get leave type
                String leaveType = getLeaveType();

                // get leave entries
                rowCount = lm.countAllLeaveList(leaveTypeName,start.getTime(), end.getTime(),
                        new String[] { userId }, null, override?leaveTypeName:leaveType, null, null,
                        null, user);
            } catch (Exception e) {
                Log.getLog(getClass()).error("Error retrieving leave summary count",
                    e);
            }

            return rowCount;
        }
    }
}
