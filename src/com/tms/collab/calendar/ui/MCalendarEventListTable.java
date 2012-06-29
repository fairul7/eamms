package com.tms.collab.calendar.ui;

/*
import com.tms.collab.calendar.model.Appointment;
import com.tms.collab.calendar.model.CalendarEvent;
*/
import com.tms.collab.calendar.model.CalendarModule;
import com.tms.collab.calendar.model.Appointment;
import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.taskmanager.model.TaskManager;
import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.emeeting.Meeting;
/*
import com.tms.collab.emeeting.Meeting;
import com.tms.collab.taskmanager.model.Task;
*/
import com.tms.util.FormatUtil;
import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

public class MCalendarEventListTable extends Table {
    private String eventType;
    private Date startDate = new Date();
    private String labelMessage;
    private String label;

    public String getEventType() {
        return eventType;   
    }

    public void setEventType(String eventType) {
    	int page = getCurrentPage();
    	if (page>1)  
    		setCurrentPage(1);  
        this.eventType = eventType;
        if (this.eventType.equals(Task.class.getName())) {
            labelMessage="Tasks";
            label="taskmanager.label.Task";
        }
        else if (this.eventType.equals(Appointment.class.getName())) {
            labelMessage="Appointment";
            label="calendar.label.appointments";
        }
        else if (this.eventType.equals(Meeting.class.getName())) {
            labelMessage="Meeting";
            label="calendar.label.e-Meetings";
        }
        else if (this.eventType.equals(CalendarEvent.class.getName()))  {
            labelMessage="Event";
            label="calendar.label.events";
        }
        setModel(new CalendarEventListTableModel());
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public MCalendarEventListTable() {
    }

    public MCalendarEventListTable(String name) {
        super(name);
    }

    public void init() {
        setModel(new CalendarEventListTableModel());
    }

    public class CalendarEventListTableModel extends TableModel {

        public CalendarEventListTableModel() {
            // add columns
            Application application = Application.getInstance();
            TableColumn nameColumn = new TableColumn("title", application.getMessage(label, labelMessage));
            nameColumn.setUrlParam("eventId");
            addColumn(nameColumn);

            TableColumn startDate = new TableColumn("startDate", application.getMessage("general.label.startDate", "Start Date"));
            startDate.setFormat(new TableDateFormat(FormatUtil.getInstance().getShortDateTimeFormat()));
            addColumn(startDate);

            TableColumn endDate = new TableColumn("endDate", application.getMessage("general.label.endDate", "End Date"));
            endDate.setFormat(new TableDateFormat(FormatUtil.getInstance().getShortDateTimeFormat()));
            addColumn(endDate);

            // add filters
            TableFilter searchFilter = new TableFilter("search");
            addFilter(searchFilter);

/*
            TableFilter typeFilter = new TableFilter("type");
            SelectBox sbType = new SelectBox("sbType");
            sbType.addOption("", Application.getInstance().getMessage("calendar.label.anytype","--Any Type--"));
            sbType.addOption(Appointment.class.getName(), Application.getInstance().getMessage("calendar.label.appointment","Appointment"));
            sbType.addOption(Meeting.class.getName(), Application.getInstance().getMessage("calendar.label.e-Meeting","E-Meeting"));
            sbType.addOption(CalendarEvent.class.getName(), Application.getInstance().getMessage("calendar.label.event","Event"));
            sbType.addOption(Task.class.getName(), Application.getInstance().getMessage("calendar.label.task","Task"));
            typeFilter.setWidget(sbType);
            addFilter(typeFilter);
*/

/*
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
*/

        }

        public Collection getTableRows() {
            try {
                // get current user
                User user = getWidgetManager().getUser();

                // get filters
                String search = (String)getFilterValue("search");

/*
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
*/

                Calendar cal = Calendar.getInstance();
                Date endDate = startDate;
                cal.setTime(endDate);
                //cal.set(Calendar.HOUR_OF_DAY, 23);
                //cal.set(Calendar.MINUTE, 59);
                //cal.set(Calendar.SECOND, 59);
                cal.set(Calendar.DAY_OF_MONTH,cal.get(Calendar.DAY_OF_MONTH)+1);
                endDate = cal.getTime();

                // query
                Collection eventList = null;
                if(!eventType.equals(Task.class.getName())){
                    CalendarModule calendar = (CalendarModule)Application.getInstance().getModule(CalendarModule.class);
                    if (eventType.equals(CalendarEvent.class.getName()))
                        eventList = calendar.getCalendarEvents(search, eventType, startDate, endDate, user.getId(), new String[] {}, null, false, false, false, getSort(), isDesc(), getStart(), getRows());
                    else
                        eventList = calendar.getCalendarEvents(search, eventType, startDate, endDate, user.getId(), new String[] {user.getId()}, null, false, false, false, getSort(), isDesc(), getStart(), getRows());
                }else{
                    TaskManager tm = (TaskManager) Application.getInstance().getModule(TaskManager.class);
                    eventList = tm.getCalendarTasks(search,startDate,endDate,new String[]{user.getId()},true,getStart(), getRows(),getSort(),isDesc());

                }
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

/*
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
*/
                Calendar cal = Calendar.getInstance();
                Date endDate = startDate;
                cal.setTime(endDate);
                //cal.set(Calendar.HOUR_OF_DAY, 23);
                //cal.set(Calendar.MINUTE, 59);
                //cal.set(Calendar.SECOND, 59);
                cal.set(Calendar.DAY_OF_MONTH,cal.get(Calendar.DAY_OF_MONTH)+1);
                endDate = cal.getTime();

                // query
                CalendarModule calendar = (CalendarModule)Application.getInstance().getModule(CalendarModule.class);
                int count = 0;
                if(!eventType.equals(Task.class.getName()))
                    if (eventType.equals(CalendarEvent.class.getName()))
                        count = calendar.getCalendarEventsCount(search, eventType, startDate, endDate, user.getId(), new String[] {}, null, false, false, false);
                    else
                        count = calendar.getCalendarEventsCount(search, eventType, startDate, endDate, user.getId(), new String[] {user.getId()}, null, false, false, false);
                else{
                    TaskManager tm = (TaskManager) Application.getInstance().getModule(TaskManager.class);
                    count = tm.countCalendarTasks(cal.getTime(),new String[]{user.getId()},true);
                }
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
