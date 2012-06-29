package com.tms.collab.calendar.ui;

import com.tms.collab.calendar.model.CalendarException;
import com.tms.collab.calendar.model.CalendarModule;
import com.tms.util.FormatUtil;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

public class CalendarEventTable extends Table {
    public CalendarEventTable() {
    }

    public CalendarEventTable(String name) {
        super(name);
    }

    public void init() {
        setSortable(false);
        setModel(new CalendarEventTableModel());
    }

    public class CalendarEventTableModel extends TableModel {

        public CalendarEventTableModel() {
            // add columns
            Application application = Application.getInstance();
            TableColumn nameColumn = new TableColumn("title", application.getMessage("general.label.title", "Title"));
            nameColumn.setUrlParam("eventId");
            addColumn(nameColumn);

/*
            TableColumn descColumn = new TableColumn("description", "Description");
            addColumn(descColumn);
*/

            TableColumn startDate = new TableColumn("startDate", application.getMessage("general.label.startDate", "Start Date"));
            startDate.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateTimeFormat()));
            addColumn(startDate);

            TableColumn endDate = new TableColumn("endDate", application.getMessage("general.label.endDate", "End Date"));
            endDate.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateTimeFormat()));
            addColumn(endDate);

            // add filters
            TableFilter searchFilter = new TableFilter("searchFilter");
            addFilter(searchFilter);

            TableFilter startFilterLabel = new TableFilter("startDateFilter", "");
            startFilterLabel.setWidget(new Label("label1", " From "));
            addFilter(startFilterLabel);
            TableFilter startFilter = new TableFilter("startDate", application.getMessage("general.label.startDate", "Start Date"));
            DateField sd = new DateField("startDate");
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            sd.setDate(cal.getTime());
            startFilter.setWidget(sd);
            addFilter(startFilter);

            TableFilter endFilterLabel = new TableFilter("endDateFilter", "");
            endFilterLabel.setWidget(new Label("label2", " To "));
            addFilter(endFilterLabel);
            TableFilter endFilter = new TableFilter("endDate", application.getMessage("general.label.endDate", "End Date"));
            DateField ed = new DateField("endDate");
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.add(Calendar.DAY_OF_MONTH, 30);
            ed.setDate(cal.getTime());
            endFilter.setWidget(ed);
            addFilter(endFilter);

            // add actions
            addAction(new TableAction("add", application.getMessage("calendar.label.newEvent", "New Event")));
            addAction(new TableAction("delete", application.getMessage("general.label.delete", "Delete"), application.getMessage("calendar.message.confirmDelete", "Delete")));
        }

        public Collection getTableRows() {
            try {
                // get current user
                User user = getWidgetManager().getUser();

                // get filters
                String search = (String)getFilterValue("searchFilter");
                Date startDate = ((DateField)getFilter("startDate").getWidget()).getDate();
                Calendar cal = Calendar.getInstance();
                cal.setTime(startDate);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                startDate = cal.getTime();
                Date endDate = ((DateField)getFilter("endDate").getWidget()).getDate();
                cal.setTime(endDate);
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                endDate = cal.getTime();

                // query
                CalendarModule calendar = (CalendarModule)Application.getInstance().getModule(CalendarModule.class);
                Collection eventList = calendar.getCalendarEvents(search, null, startDate, endDate, user.getId(), new String[] {SecurityService.ANONYMOUS_USER_ID}, null, true, true, false, null, false, 0, -1);

                return eventList;
            }
            catch (Exception e) {
                Log.getLog(getClass()).error(e.toString(), e);
                return new ArrayList();
            }
        }

        public int getTotalRowCount() {
            try {
                // get current user
                User user = getWidgetManager().getUser();

                // get filters
                String search = (String)getFilterValue("searchFilter");
                Date startDate = ((DateField)getFilter("startDate").getWidget()).getDate();
                Calendar cal = Calendar.getInstance();
                cal.setTime(startDate);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                startDate = cal.getTime();
                Date endDate = ((DateField)getFilter("endDate").getWidget()).getDate();
                cal.setTime(endDate);
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                endDate = cal.getTime();

                // query
                CalendarModule calendar = (CalendarModule)Application.getInstance().getModule(CalendarModule.class);
                int count = calendar.getCalendarEventsCount(search, null, startDate, endDate, user.getId(), new String[] {SecurityService.ANONYMOUS_USER_ID}, null, true, true, false);

                return count;
            }
            catch (Exception e) {
                Log.getLog(getClass()).error(e.toString(), e);
                return 0;
            }
        }

        public String getTableRowKey() {
            return "eventId";
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            if ("delete".equals(action)) {
                // check permission
                try {
                    Application app = Application.getInstance();
                    User user = getWidgetManager().getUser();
                    SecurityService ss = (SecurityService)app.getService(SecurityService.class);
                    if (ss.hasPermission(user.getId(), CalendarModule.PERMISSION_DELETE_EVENTS, CalendarModule.class.getName(), null)) {
                        CalendarModule calendar = (CalendarModule)app.getModule(CalendarModule.class);
                        for (int i=0; i<selectedKeys.length; i++) {
                            try {
                                calendar.destroyCalendarEvent(selectedKeys[i]);
                            }
                            catch (CalendarException e) {
                                ;
                            }
                        }
                        return new Forward("deleted");
                    }
                    else {
                        return null;
                    }
                }
                catch (Exception e) {
                    Log.getLog(getClass()).error("Error deleting events", e);
                    return null;
                }
            }
            else if ("add".equals(action)) {
                return new Forward("add");
            }
            else {
                return null;
            }
        }
    }
}
