package com.tms.collab.calendar.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.DateField;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.stdui.event.FormEventAdapter;
import kacang.ui.Event;
import kacang.ui.EventListener;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.collab.calendar.model.Appointment;
import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.calendar.model.CalendarModule;
import com.tms.collab.emeeting.Meeting;
import com.tms.collab.taskmanager.model.Task;
import com.tms.util.FormatUtil;

public class CalendarEventListTable extends Table {
    public CalendarEventListTable() {
    }

    public CalendarEventListTable(String name) {
        super(name);
    }
    
    public void init() {
        setModel(new CalendarEventListTableModel());
        Form tableForm = getFilterForm();
        
        tableForm.addFormEventListener(new FormEventAdapter() {
                public Forward onSubmit(Event evt) {
                    Forward f = super.onSubmit(evt);
                    try {
                        Map childMap = getChildMap();
                        Form form = (Form)childMap.get("filterForm");
                        Map formChildMap = form.getChildMap();
                        
                        DateField sd = (DateField)formChildMap.get("startDate");
                        DateField ed = (DateField)formChildMap.get("endDate");
                        
                        if(ed.getDate().before(sd.getDate())){
                        	ed.setInvalid(true);
                        	setInvalid(true);
                        }
                        
                    } catch (Exception e) {
                        ;
                    }
                    return f;
                }
            });
        
    }
    
    public class CalendarEventListTableModel extends TableModel {

        public CalendarEventListTableModel() {
            // add columns
            Application application = Application.getInstance();
            TableColumn nameColumn = new TableColumn("title", application.getMessage("calendar.label.listEvent", "Event"));
            nameColumn.setUrlParam("eventId");
            addColumn(nameColumn);

            TableColumn startDate = new TableColumn("startDate", application.getMessage("general.label.startDate", "Start Date"));
            startDate.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateTimeFormat()));
            addColumn(startDate);

            TableColumn endDate = new TableColumn("endDate", application.getMessage("general.label.endDate", "End Date"));
            endDate.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateTimeFormat()));
            addColumn(endDate);

            // add filters
            TableFilter searchFilter = new TableFilter("search");
            addFilter(searchFilter);

            TableFilter typeFilter = new TableFilter("type");
            SelectBox sbType = new SelectBox("sbType");
            sbType.addOption("", Application.getInstance().getMessage("calendar.label.anytype","--Any Type--"));
            sbType.addOption(Appointment.class.getName(), Application.getInstance().getMessage("calendar.label.appointmentFilter","Appointment"));
            sbType.addOption(Meeting.class.getName(), Application.getInstance().getMessage("calendar.label.e-MeetingFilter","E-Meeting"));
            sbType.addOption(CalendarEvent.class.getName(), Application.getInstance().getMessage("calendar.label.eventFilter","Event"));
            sbType.addOption(Task.class.getName(), Application.getInstance().getMessage("calendar.label.task","Task"));
            typeFilter.setWidget(sbType);
            addFilter(typeFilter);

            TableFilter startFilterLabel = new TableFilter("startDateFilter");
            startFilterLabel.setWidget(new Label("label1", "<br>"+Application.getInstance().getMessage("calendar.label.from","From")));
            addFilter(startFilterLabel);
            TableFilter startFilter = new TableFilter("startDate");
            DateField sd = new DateField("startDate");
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            sd.setDate(cal.getTime());
            startFilter.setWidget(sd);
            addFilter(startFilter);

            TableFilter endFilterLabel = new TableFilter("endDateFilter");
            endFilterLabel.setWidget(new Label("label2", Application.getInstance().getMessage("calendar.label.to","To")));
            addFilter(endFilterLabel);
            TableFilter endFilter = new TableFilter("endDate");
            DateField ed = new DateField("endDate");
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.add(Calendar.DAY_OF_MONTH, 30);
            ed.setDate(cal.getTime());
            endFilter.setWidget(ed);
            addFilter(endFilter);

        }

        public Collection getTableRows() {
            try {
                // get current user
                User user = getWidgetManager().getUser();

                // get filters
                String search = (String)getFilterValue("search");

                String type = null;
                Collection selected = (Collection)getFilterValue("type");
                if (selected != null && selected.size() > 0) {
                    type = (String)selected.iterator().next();
                }

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
                Collection eventList = calendar.getCalendarEvents(search, type, startDate, endDate, user.getId(), new String[] {user.getId()}, null, false, false, false, getSort(), isDesc(), getStart(), getRows());

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
                String search = (String)getFilterValue("search");

                String type = null;
                Collection selected = (Collection)getFilterValue("type");
                if (selected != null && selected.size() > 0) {
                    type = (String)selected.iterator().next();
                }

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
                int count = calendar.getCalendarEventsCount(search, type, startDate, endDate, user.getId(), new String[] {user.getId()}, null, false, false, false);

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

    }
}
