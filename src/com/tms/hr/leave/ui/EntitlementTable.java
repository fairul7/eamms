package com.tms.hr.leave.ui;

import kacang.stdui.*;
import kacang.Application;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;

import java.util.*;

import com.tms.hr.leave.model.LeaveModule;
import org.apache.commons.collections.SequencedHashMap;

public class EntitlementTable extends Table {

    private String leaveType;
    private SelectBox yearSelectBox;

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public EntitlementTable() {
        super();
    }

    public EntitlementTable(String name) {
        super(name);
    }

    public void init() {
        setSortable(false);
        setPageSize(100);
        setModel(new EntitlementTableModel());
    }

    public class EntitlementTableModel extends TableModel {

        int rowCount;

        public EntitlementTableModel() {
            Application application = Application.getInstance();

            TableColumn serviceClassId = new TableColumn("serviceClassId", application.getMessage("leave.label.classificationCode", "Classification Code"));
            addColumn(serviceClassId);

            TableColumn serviceClassDescription = new TableColumn("serviceClassDescription", application.getMessage("leave.label.classificationDesc", "Classification Description"));
            addColumn(serviceClassDescription);

            TableColumn serviceYears = new TableColumn("serviceYears", application.getMessage("leave.label.serviceYears", "Service Years"));
            serviceYears.setFormat(new TableDecimalFormat("#.#"));
            addColumn(serviceYears);

            TableColumn entitlement = new TableColumn("entitlement", application.getMessage("leave.label.entitlement", "Entitlement"));
            entitlement.setFormat(new TableDecimalFormat("#.#"));
            addColumn(entitlement);

            TableFilter filter = new TableFilter("leaveType");
            LeaveTypeSelectBox leaveTypeSb = new LeaveTypeSelectBox("leaveType");
            filter.setWidget(leaveTypeSb);
            addFilter(filter);

            TableFilter yearSelect = new TableFilter("YearSelect");
            yearSelectBox = new SelectBox("YearSelectBox");
            Map selectYearMap = new SequencedHashMap();
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            selectYearMap.put(new Integer(year-2).toString(),new Integer(year-2).toString());
            selectYearMap.put(new Integer(year-1).toString(),new Integer(year-1).toString());
            selectYearMap.put(new Integer(year).toString(),new Integer(year).toString());

            yearSelectBox.setOptionMap(selectYearMap);
            yearSelectBox.setSelectedOption(new Integer(year).toString());
            yearSelect.setWidget(yearSelectBox);
            addFilter(yearSelect);


            addAction(new TableAction("delete", application.getMessage("general.label.delete", "Delete")));
        }

        public Collection getTableRows() {
            Collection rows = new ArrayList();
            rowCount = 0;
            try {
                Application application = Application.getInstance();
                LeaveModule lm = (LeaveModule)application.getModule(LeaveModule.class);

                // determine year
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);

                // get leave types
                String leaveType = null;
                Collection tmp = (Collection)getFilterValue("leaveType");
                if (tmp != null && tmp.size() > 0) {
                    leaveType = (String)tmp.iterator().next();
                }

                //get year selected by user
                List readYear = (List) getFilterValue("YearSelect");
                 String yearString;
                if (readYear.size() > 0)
                {yearString =  (String)readYear.get(0);
                 year = Integer.parseInt(yearString);
                 yearSelectBox.setSelectedOption(new Integer(year).toString());

                }


                // get entitlement
                rows = lm.viewLeaveEntitlementList(null, leaveType, year);
            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Error retrieving leave entitlement", e);
            }
            rowCount = rows.size();
            return rows;

        }

        public int getTotalRowCount() {
            return rowCount;
        }

        public String getTableRowKey() {
            return "id";
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            if ("delete".equals(action)) {
                try {
                    if (selectedKeys != null) {
                        Application application = Application.getInstance();
                        LeaveModule lm = (LeaveModule)application.getModule(LeaveModule.class);
                        lm.deleteLeaveEntitlement(selectedKeys);
                        return new Forward("success");
                    }
                }
                catch (Exception e) {
                    Log.getLog(getClass()).error("Error deleting entitlement", e);
                }
                return new Forward("failure");
            }
            else {
                return null;
            }
        }
    }

    public SelectBox getYearSelectBox() {
        return yearSelectBox;
    }

    public void setYearSelectBox(SelectBox yearSelectBox) {
        this.yearSelectBox = yearSelectBox;
    }

}

