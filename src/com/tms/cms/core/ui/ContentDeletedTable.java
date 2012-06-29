package com.tms.cms.core.ui;

import com.tms.cms.core.model.ContentManager;
import com.tms.util.FormatUtil;
import kacang.Application;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class ContentDeletedTable extends Table {

    public ContentDeletedTable() {
    }

    public ContentDeletedTable(String name) {
        super(name);
    }

    public void init() {
        initTable();
    }

    public void onRequest(Event event) {
        initTable();
    }

    public void initTable() {
        setModel(new ContentDeletedTableModel());
    }

    public Forward onSelection(Event evt) {
        String id = evt.getRequest().getParameter("id");
        ContentHelper.setId(evt, id);
        Forward forward = super.onSelection(evt);
        forward = new Forward("selection");
        return forward;
    }

    public class ContentDeletedTableModel extends TableModel {

        public ContentDeletedTableModel() {

            // add columns
            Application application = Application.getInstance();
            TableColumn nameColumn = new TableColumn("name", application.getMessage("general.label.name", "Name"));
            nameColumn.setUrlParam("id");
            addColumn(nameColumn);

            TableColumn classColumn = new TableColumn("className", application.getMessage("general.label.type", "Type"));
            classColumn.setFormat(new TableResourceFormat("cms.label.iconLabel_", null));
            addColumn(classColumn);

            TableColumn dateColumn = new TableColumn("date", application.getMessage("general.label.date", "Date"));
            dateColumn.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateFormat()));
            addColumn(dateColumn);

            String contextPath = (String)getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
            TableFormat booleanFormat = new TableBooleanFormat("<img src=\"" + contextPath + "/common/table/booleantrue.gif\">", "");
            TableColumn deletedColumn = new TableColumn("deleted", application.getMessage("general.label.deleted", "Deleted"));
            deletedColumn.setFormat(booleanFormat);
            addColumn(deletedColumn);

            // add actions
            addAction(new TableAction("undelete", application.getMessage("general.label.undelete", "Undelete"), application.getMessage("cms.message.undelete", "Undelete Selected Content?")));
            try {
                SecurityService security = (SecurityService)application.getService(SecurityService.class);
                User user = getWidgetManager().getUser();
                boolean manageRecycleBin = security.hasPermission(user.getId(), ContentManager.PERMISSION_MANAGE_RECYCLE_BIN, getClass().getName(), null);
                if (manageRecycleBin) {
                    addAction(new TableAction("destroy", application.getMessage("general.label.destroy", "Destroy"), application.getMessage("cms.message.destroy", "Please confirm - Destroyed content cannot be recovered")));
                }
            }
            catch (SecurityException e) {
                Log.getLog(getClass()).error("Unable to check manager recycle bin permission", e);
            }

            // add filters
            TableFilter nameFilter = new TableFilter("name");
            TextField nameField = new TextField("name");
            nameField.setSize("15");
            nameFilter.setWidget(nameField);
            addFilter(nameFilter);

            TableFilter startFilterLabel = new TableFilter("startDateFilter");
            startFilterLabel.setWidget(new Label("label1", " From "));
            addFilter(startFilterLabel);
            TableFilter startFilter = new TableFilter("startDate");
            DateField sd = new DateField("startDate");
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.YEAR, 2000);
            sd.setDate(cal.getTime());
            startFilter.setWidget(sd);
            addFilter(startFilter);

            TableFilter endFilterLabel = new TableFilter("endDateFilter");
            endFilterLabel.setWidget(new Label("label2", " To "));
            addFilter(endFilterLabel);
            TableFilter endFilter = new TableFilter("endDate");
            DateField ed = new DateField("endDate");
            cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.add(Calendar.DAY_OF_MONTH, 30);
            ed.setDate(cal.getTime());
            endFilter.setWidget(ed);
            addFilter(endFilter);

            TableFilter classFilter = new TableFilter("classFilter");
            SelectBox sbClassFilter = new SelectBox("sbClassFilter");
            Map optionMap = new SequencedHashMap();
            optionMap.put("", application.getMessage("general.label.any", "Any"));
            ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
            Class[] classes = cm.getAllowedClasses(null);
            for (int i=0; i<classes.length; i++) {
                optionMap.put(classes[i].getName(), application.getMessage("cms.label_" + classes[i].getName(), classes[i].getName()));
            }
            sbClassFilter.setOptionMap(optionMap);
            classFilter.setWidget(sbClassFilter);
            addFilter(classFilter);
        }

        public String getTableRowKey() {
            return "id";
        }

        public Collection getTableRows() {
            try {
                String name = (String)getFilterValue("name");
                Collection selected = (Collection)getFilterValue("classFilter");
                String selectedClass = (selected != null && selected.size() > 0) ? (String)selected.iterator().next() : null;
                String[] classes = (selectedClass == null || "".equals(selectedClass)) ? null : new String[] {selectedClass};

                // get dates
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

                // check user permission
                Application app = Application.getInstance();
                SecurityService security = (SecurityService)app.getService(SecurityService.class);
                User user = getWidgetManager().getUser();
                boolean manageRecycleBin = security.hasPermission(user.getId(), ContentManager.PERMISSION_MANAGE_RECYCLE_BIN, getClass().getName(), null);
                String permission = (manageRecycleBin) ? null : ContentManager.USE_CASE_PREVIEW; // users that can manage recycle bin see all deleted content, regardless of permission

                // query
                ContentManager cm = (ContentManager)app.getModule(ContentManager.class);
                Collection children = cm.viewListByDate(null, classes, name, null, startDate, endDate, null, null, null, null, null, Boolean.TRUE, null, getSort(), isDesc(), getStart(), getRows(), permission, user);
                return children;
            }
            catch(Exception e) {
                throw new RuntimeException(e.toString());
            }
        }

        public int getTotalRowCount() {
            try {
                String name = (String)getFilterValue("name");
                Collection selected = (Collection)getFilterValue("classFilter");
                String selectedClass = (selected != null && selected.size() > 0) ? (String)selected.iterator().next() : null;
                String[] classes = (selectedClass == null || "".equals(selectedClass)) ? null : new String[] {selectedClass};

                // get dates
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

                // check user permission
                Application app = Application.getInstance();
                SecurityService security = (SecurityService)app.getService(SecurityService.class);
                User user = getWidgetManager().getUser();
                boolean manageRecycleBin = security.hasPermission(user.getId(), ContentManager.PERMISSION_MANAGE_RECYCLE_BIN, getClass().getName(), null);
                String permission = (manageRecycleBin) ? null : ContentManager.USE_CASE_PREVIEW; // users that can manage recycle bin see all deleted content, regardless of permission

                // query
                ContentManager cm = (ContentManager)app.getModule(ContentManager.class);
                int total = cm.viewCountByDate(null, classes, name, null, startDate, endDate, null, null, null, null, null, Boolean.TRUE, null, permission, user);
                return total;
            }
            catch(Exception e) {
                throw new RuntimeException(e.toString());
            }
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
//            System.out.println("Action: " + action + "; Keys: " + Arrays.asList(selectedKeys));

            Application application = Application.getInstance();
            ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
            User user = getWidgetManager().getUser();

            if ("undelete".equals(action)) {
//                System.out.println("Undelete: " + action + "; Keys: " + Arrays.asList(selectedKeys));
                try {
                    cm.undelete(selectedKeys, false, user);
                }
                catch (Exception e) {
                    Log.getLog(getClass()).error(e.toString(), e);
                }
            }
            else if ("destroy".equals(action)) {
//                System.out.println("Destroy: " + action + "; Keys: " + Arrays.asList(selectedKeys));
                try {
                    cm.destroy(selectedKeys, user);
                }
                catch (Exception e) {
                    Log.getLog(getClass()).error(e.toString(), e);
                }
            }
            else {

            }
            return new Forward("success");
        }
    }


}
