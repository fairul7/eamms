package com.tms.hr.leave.ui;

import com.tms.hr.leave.model.LeaveException;
import com.tms.hr.leave.model.LeaveModule;

import com.tms.util.FormatUtil;

import kacang.Application;

import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;

import kacang.stdui.*;

import kacang.ui.Event;
import kacang.ui.Forward;

import kacang.util.Log;

import java.util.*;

import org.apache.commons.collections.SequencedHashMap;


public class PublicHolidaysSetupTable extends Table {
    private int year;

    protected SelectBox yearSelect;


    public PublicHolidaysSetupTable() {
    }

    public PublicHolidaysSetupTable(String s) {
        super(s);
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void init() {
        super.init();
        setCurrentPage(1);
        setPageSize(10);
        setModel(new PublicHolidaySetupTableModel());
        setWidth("100%");
    }

    class PublicHolidaySetupTableModel extends TableModel {
        public PublicHolidaySetupTableModel() {
            boolean leaveManager;
            Application app = Application.getInstance();
            SecurityService service = (SecurityService) app.getService(SecurityService.class);

            try {
                leaveManager = service.hasPermission(app.getCurrentUser().getId(),
                        LeaveModule.PERMISSION_MANAGE_LEAVE, null, null);
            } catch (SecurityException e) {
                //user not found
                leaveManager = false;
            }

            if (leaveManager) {
                addAction(new TableAction("add",
                        Application.getInstance().getMessage("Add",
                            "Add")));
                addAction(new TableAction("delete",
                        Application.getInstance().getMessage("leave.label.delete",
                            "Delete")));
            }

            TableColumn tcDate = new TableColumn("holidayDate",
                    Application.getInstance().getMessage("leave.label.date",
                        "Date"));
            tcDate.setFormat(new TableDateFormat(FormatUtil.getInstance()
                                                           .getLongDateFormat()));
            if (leaveManager) {
            tcDate.setUrlParam("holidayDate");
            }
            addColumn(tcDate);

            TableColumn tcHoliday = new TableColumn("description",
                    Application.getInstance().getMessage("leave.label.holidayDesc",
                        "Holiday Desc"));
            addColumn(tcHoliday);

            // Application application = Application.getInstance();

            /*
                        LeaveModule handler = (LeaveModule) application.getModule(LeaveModule.class);
                        LeaveDao dao = (LeaveDao) handler.getDao();
                        GlobalDataObject global = new GlobalDataObject();
                        try {
                            if (dao.selectGlobalLeaveParameters().iterator().hasNext()) {
                                global = (GlobalDataObject) dao.selectGlobalLeaveParameters().iterator().next();
                                if (global.getAutoIncBalance().equals("1")) {
                                    TableColumn tcCredited = new TableColumn("isCredited", Application.getInstance().getMessage("leave.label.credited","Credited"));
                                    String contextPath = (String) getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
                                    TableFormat booleanFormat = new TableBooleanFormat("<img src=\"" + contextPath + "/common/table/booleantrue.gif\">", "");
                                    tcCredited.setFormat(booleanFormat);
                                    addColumn(tcCredited);
                                }
                            }

                        }
                        catch (DaoException e) {
                            Log.getLog(getClass()).error(e.toString(), e);
                        }
            */






       yearSelect = new SelectBox("yearSelect");
       yearSelect.setAlign(Form.ALIGN_RIGHT);

       Map yearMap = new SequencedHashMap();
       Calendar cal = Calendar.getInstance();
       year = cal.get(Calendar.YEAR);

       yearMap.put((String) Integer.toString(year - 2),
           (String) Integer.toString(year - 2));
       yearMap.put((String) Integer.toString(year - 1),
           (String) Integer.toString(year - 1));
       yearMap.put((String) Integer.toString(year),
           (String) Integer.toString(year));
       yearMap.put((String) Integer.toString(year+1),
                   (String) Integer.toString(year+1));
       yearMap.put((String) Integer.toString(year+2),
                   (String) Integer.toString(year+2));


       yearSelect.setOptionMap(yearMap);
       yearSelect.setSelectedOption(Integer.toString(year));
       TableFilter tfRead = new TableFilter("year");

       tfRead.setWidget(yearSelect);
       addFilter(tfRead);






        }



        public Forward processAction(Event evt, String action,
                                     String[] selectedKeys) {
            Log log = Log.getLog(this.getClass());
            

            boolean leaveManager;
            Application app = Application.getInstance();
            SecurityService service = (SecurityService) app.getService(SecurityService.class);

            try {
                leaveManager = service.hasPermission(app.getCurrentUser().getId(),
                        LeaveModule.PERMISSION_MANAGE_LEAVE, null, null);
            } catch (SecurityException e) {
                //user not found
                leaveManager = false;
            }

            if (leaveManager) {
                if (action.equalsIgnoreCase("delete")) {
                    try {
                        LeaveModule leaveModule = (LeaveModule) Application.getInstance()
                                                                           .getModule(LeaveModule.class);

                        for (int i = 0; i < selectedKeys.length; i++) {
                            
                        	int result = leaveModule.deleteHolidays(selectedKeys[i]);
                            
                        	if(result ==0)
                        		return new Forward("requiredManualErase");
                        	
                        }
                    } catch (LeaveException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }

            return super.processAction(evt, action, selectedKeys);
        }

        public String getTableRowKey() {
            return "holidayDate";
        }

        public Collection getTableRows() {
            Log log = Log.getLog(this.getClass());



            List readStatus = (List) getFilterValue("year");
                          String year= "";
                          if (readStatus.size() > 0)
                              year = (String) readStatus.get(0);
                          if (year != null && (year.trim().equalsIgnoreCase("null") || year.trim().equals("")))
                              year = "";



            // String year = Integer.toString((Calendar.getInstance().get(Calendar.YEAR)));
            try {
                LeaveModule leaveModule = (LeaveModule) Application.getInstance()
                                                                   .getModule(LeaveModule.class);

                return leaveModule.getPublicHolidays(year,
                    getSort(), isDesc(), getStart(), getRows());
            } catch (LeaveException e) {
                // log error and return an empty collection
                log.error(e.getMessage());

                return new ArrayList();
            }
        }

        public int getTotalRowCount() {



            Log log = Log.getLog(this.getClass());



                List readStatus = (List) getFilterValue("year");
                String year= "";
                if (readStatus.size() > 0)
                    year = (String) readStatus.get(0);
                if (year != null && (year.trim().equalsIgnoreCase("null") || year.trim().equals("")))
                    year = "";











            //  String year = Integer.toString((Calendar.getInstance().get(Calendar.YEAR)));
            try {
                LeaveModule leaveModule = (LeaveModule) Application.getInstance()
                                                                   .getModule(LeaveModule.class);

                return leaveModule.getPublicHolidays(year);
            } catch (LeaveException e) {
                log.error(e.getMessage());

                return 0;
            }
        }
    }
}
